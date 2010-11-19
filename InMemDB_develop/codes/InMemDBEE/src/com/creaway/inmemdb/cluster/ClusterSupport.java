/*
 * @(#)ClusterH2.java	1.0  08/23/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.cluster;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.jgroups.Address;
import org.jgroups.Channel;
import org.jgroups.ChannelClosedException;
import org.jgroups.ChannelException;
import org.jgroups.ChannelNotConnectedException;
import org.jgroups.ExtendedReceiverAdapter;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.View;
import org.jgroups.blocks.RpcDispatcher;

import com.creaway.inmemdb.cluster.restore.MasterRestorHandler;
import com.creaway.inmemdb.cluster.restore.MasterRestoreNotification;
import com.creaway.inmemdb.cluster.restore.RestoreNote;
import com.creaway.inmemdb.cluster.restore.SlaveRestorHandler;
import com.creaway.inmemdb.core.InMemDBServer;
import com.creaway.inmemdb.core.InMemDBThreadFactory;
import com.creaway.inmemdb.table.TableSupportIfc;
import com.creaway.inmemdb.util.ConnectionManager;
import com.creaway.inmemdb.util.DBLogger;

/**
 * 内存数据库集群模块
 * 
 * @author Qil.Wong
 * 
 */
public class ClusterSupport extends ExtendedReceiverAdapter implements
		ClusterIfc {

	/**
	 * 远程时间派发器
	 */
	private RpcDispatcher disp;

	/**
	 * 集群通道
	 */
	private Channel channel;

	/**
	 * 集群属性，一般是配置文件，udp或tcp之类
	 */
	private String props;

	/**
	 * 集群连接缓存
	 */
	private Map<Integer, Connection> slaveConnections = new ConcurrentHashMap<Integer, Connection>();

	/**
	 * 数据库事务最后的Commit/Rollback处理类
	 */
	private ClusterCommitRollbackHandler crhandler;

	/**
	 * 集群条件下，从属连接的数据库操作处理对象
	 */
	private ClusterDBOperationHandler dboperHandler;

	/**
	 * 从属服务数据恢复对象
	 */
	private SlaveRestorHandler slaveRestoreHandler;

	/**
	 * 从属服务数据恢复时Master主服务响应对象
	 */
	private MasterRestorHandler masterRestoreHandler;

	/**
	 * 集群对外主服务标记，由于服务器需要同外界同步信息，但一般情况下只需要一个集群服务去做同步工作，其它服务器会自动更新内容
	 */
	private boolean isMaster = false;

	private Address masterAddress;

	private Timer slaveConnTimer;

	private Map<Integer, SlaveConnectionTimeOutTask> timeoutTasks = new ConcurrentHashMap<Integer, SlaveConnectionTimeOutTask>();

	private List<ClusterListener> clusterListeners = new CopyOnWriteArrayList<ClusterListener>();

	private List<Address> members;

	public ClusterSupport() {

	}

	@Override
	public void startModule(Map<?, ?> params) {
		DBLogger.log(DBLogger.INFO, "启动集群模块，开始创建内存数据库集群支持");
		singleSequencePool = new ThreadPoolExecutor(1, 1, 10, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(), new InMemDBThreadFactory(
						"cluster-sequence-thread"),
				new ThreadPoolExecutor.CallerRunsPolicy());
		slaveConnTimer = new Timer("SlaveConnectionTimer", true);
		crhandler = new ClusterCommitRollbackHandler(this);
		dboperHandler = new ClusterDBOperationHandler(this);
		slaveRestoreHandler = new SlaveRestorHandler(this);
		masterRestoreHandler = new MasterRestorHandler(this);
		try {
			if (props == null || props.equals("")) {
				props = getClass().getResource("/resources/cluster.xml")
						.getFile();
			}
			channel = new JChannel(props);
			disp = new RpcDispatcher(channel, this, this, this);
			channel.connect(CLUSTER_NAME);
			channel.getState(null, 0);
			channel.setOpt(Channel.LOCAL, false); // 发送消息时，自己不需要收到
		} catch (ChannelException e) {
			DBLogger.log(DBLogger.ERROR, "Cluster starting error", e);
		}
		createTrigger();

		restoreFromCluster();
		DBLogger.log(DBLogger.INFO, "内存数据库集群支持创建完成");
	}

	private void createTrigger() {
		// 添加集群trigger
		TableSupportIfc tableSupport = (TableSupportIfc) InMemDBServer
				.getInstance().getModule(TableSupportIfc.class);
		Set<String> tables = tableSupport.getTableStructure().keySet();
		Connection conn = ConnectionManager.getConnection();
		try {
			Statement st = conn.createStatement();
			for (String tableName : tables) {
				String cluserTiggerSQL_insert = "CREATE TRIGGER " + tableName
						+ "_INS_CLUSTER AFTER INSERT ON " + tableName
						+ " FOR EACH ROW CALL \""
						+ ClusterTrigger.class.getName() + "\"";
				String cluserTiggerSQL_update = "CREATE TRIGGER " + tableName
						+ "_UPD_CLUSTER AFTER UPDATE ON " + tableName
						+ " FOR EACH ROW CALL \""
						+ ClusterTrigger.class.getName() + "\"";
				String cluserTiggerSQL_delete = "CREATE TRIGGER " + tableName
						+ "_DEL_CLUSTER AFTER DELETE ON " + tableName
						+ " FOR EACH ROW CALL \""
						+ ClusterTrigger.class.getName() + "\"";
				DBLogger.log(DBLogger.DEBUG, "创建集群表支持："
						+ cluserTiggerSQL_insert + ", "
						+ cluserTiggerSQL_update + ", "
						+ cluserTiggerSQL_delete);
				st.execute(cluserTiggerSQL_insert);
				st.execute(cluserTiggerSQL_update);
				st.execute(cluserTiggerSQL_delete);
			}
			st.close();
			conn.commit();
		} catch (SQLException e) {
			DBLogger.log(DBLogger.ERROR, "创建集群触发器失败", e);
		} finally {
			ConnectionManager.releaseConnection(conn);
		}
	}

	public void restoreFromCluster() {
		if (!getCurrentClusterAddress().equals(getMasterAddress())) {
			DBLogger.log(DBLogger.INFO, getCurrentClusterAddress()
					+ "开始数据恢复：Master=" + getMasterAddress());
			MasterRestoreNotification mrn = new MasterRestoreNotification();
			mrn.setSlaveAddress(getCurrentClusterAddress());
			sendDataToMembers(mrn, getMasterAddress());
		}
	}

	@Override
	public void shutdownModule(Map<?, ?> params) {
		DBLogger.log(DBLogger.INFO, "关闭数据库集群支持模块");
		props = null;
		disp.stop();
		disp = null;
		channel.disconnect();
		channel = null;
		for (TimerTask task : timeoutTasks.values()) {
			task.cancel();
		}
		timeoutTasks.clear();
		for (Connection conn : slaveConnections.values()) {
			SlaveConnectionFactory.releaseSlaveConnection(conn);
		}
		slaveConnections.clear();
		isMaster = false;
		slaveConnTimer.cancel();
		singleSequencePool.shutdown();
	}

	/************** message listener ****************/

	@Override
	public void receive(Message msg) {
		Object o = msg.getObject();
		if (o instanceof CommitRollbackNote) {
			crhandler.handleCommitRollback((CommitRollbackNote) o);
		} else if (o instanceof DBOperationNote) {
			dboperHandler.hanleDBOperations((DBOperationNote) o);
		} else if (o instanceof RestoreNote[]) {
			slaveRestoreHandler.handleRestoreData((RestoreNote[]) o);
		} else if (o instanceof MasterRestoreNotification) {
			masterRestoreHandler.sendRestoreData((MasterRestoreNotification) o);
		} else {
			DBLogger.log(DBLogger.WARN, "无法识别的集群通知对象：" + o);
		}
	}

	@Override
	public byte[] getState() {
		return null;
	}

	@Override
	public void setState(byte[] state) {
	}

	/************** member ship *****************/

	@Override
	public void viewAccepted(View new_view) {
		members = new_view.getMembers();
		// 无论顺序如何，加入的（不管是启动还是断线），总有一个是master
		boolean oldIsMaster = isMaster;
		isMaster = members.get(0).equals(channel.getAddress());
		if (!oldIsMaster && isMaster) {
			fireMasterGained();
		}
		if (oldIsMaster && !isMaster) {
			fireMasterDeprived();
		}
		masterAddress = members.get(0);
		DBLogger.log(DBLogger.INFO, "成员变更:" + new_view);
		DBLogger.log(DBLogger.INFO, "Master Cluster是:" + members.get(0));
	}

	@Override
	public void suspect(Address suspected_mbr) {

	}

	@Override
	public void block() {

	}

	/*********************** Cluster ***************************/

	//单线程的池，必须以lingkedblockingqueue作为无界工作队列，保证集群消息的顺序
	private ThreadPoolExecutor singleSequencePool = null;

	@Override
	public synchronized void sendDataToMembers(Serializable messageObj,
			Object dest) {
		if (members.size() > 1) {

			final Message message = new Message();
			if (dest != null) {
				message.setDest((Address) dest);
			}
			message.setObject(messageObj);
			singleSequencePool.execute(new Runnable() {
				public void run() {
					try {
						channel.send(message);
					} catch (ChannelNotConnectedException e) {
						DBLogger.log(DBLogger.ERROR, "集群通道未连接", e);
					} catch (ChannelClosedException e) {
						DBLogger.log(DBLogger.ERROR, "集群通道已关闭", e);
					}
				}
			});
		}
	}

	@Override
	public Map<Integer, Connection> getSlaveConnections() {
		return slaveConnections;
	}

	@Override
	public void addConnectionTimeOutTimerTask(SlaveConnectionTimeOutTask task,
			int timeout) {
		slaveConnTimer.schedule(task, timeout);
		timeoutTasks.put(task.getMasterSessionNO(), task);
	}

	public Map<Integer, SlaveConnectionTimeOutTask> getSlaveConnectionTimeOutTasks() {
		return timeoutTasks;
	}

	public boolean isMasterCluser() {
		return isMaster;
	}

	public void setMasterCluser(boolean masterCluser) {
		this.isMaster = masterCluser;
	}

	@Override
	public Serializable getCurrentClusterAddress() {
		return channel.getAddress();
	}

	@Override
	public Serializable getMasterAddress() {
		return masterAddress;
	}

	@Override
	public void addClusterListener(ClusterListener cl) {
		clusterListeners.add(cl);
	}

	protected void fireMasterGained() {
		for (ClusterListener cl : clusterListeners) {
			cl.masterGained(this);
		}
	}

	protected void fireMasterDeprived() {
		for (ClusterListener cl : clusterListeners) {
			cl.masterDeprived(this);
		}
	}

	public List getMembers() {
		return members;
	}

}
