package com.nci.ums.periphery.application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.Timer;

import com.nci.ums.channel.channelinfo.QuitLockFlag;
import com.nci.ums.channel.inchannel.email.EmailMsgPlus;
import com.nci.ums.desktop.bean.SwingWorker;
import com.nci.ums.util.AppServiceUtil;
import com.nci.ums.util.ClientWSUtil;
import com.nci.ums.util.DBUtil_V3;
import com.nci.ums.util.DataBaseOp;
import com.nci.ums.util.PropertyUtil;
import com.nci.ums.util.Res;
import com.nci.ums.util.Util;
import com.nci.ums.v3.message.UMSMsg;
import com.nci.ums.v3.message.basic.BasicMsg;
import com.nci.ums.v3.message.basic.MsgAttachment;
import com.nci.ums.v3.message.basic.MsgContent;
import com.nci.ums.v3.message.basic.Participant;
import com.nci.ums.v3.service.ServiceInfo;
import com.nci.ums.v3.service.common.UMSClientWS;
import com.nci.ums.v3.service.impl.UMSReceiveService;
import com.thoughtworks.xstream.XStream;

import edu.emory.mathcs.backport.java.util.concurrent.ConcurrentHashMap;

public class ReceiveCenter extends Thread {

	private DBUtil_V3 dbUtil;
	private XStream xstream;
	/**
	 * 存放接收到的缓存消息
	 */
	private static Map inMsgMap;
	/**
	 * 存放接收到的缓存消息的其他几个重要属性，但这些属性不包含在应用接收的xml字符串中。 包括BatchNO,SerialNO和SequenceNO
	 */
	private static Map inMsgBatchMap;
	/**
	 * 客户端socket连接的线程哈希Map
	 */
	private static Map clientSocketThreadMap;

	private static Map clientWSArr;

	protected QuitLockFlag quitFlag;
	/**
	 * 状态标记
	 */
	protected boolean stop = false;
	/**
	 * 同步标记，在UMS结束过程中需要返存缓存中的消息，此时不应让消息接收服务继续运行，
	 * 即使相应的服务存在接收消息，也不能将消息发送过去，否则会造成冗余。
	 */
	private static boolean syncFlag;
	/**
	 * 心跳检测信号
	 */
	private static String HEARTSIG = "1982";
	/**
	 * 服务重复登录信号
	 */
	private static String DUPLICATE = "duplicate";

	// ReceiveCenter主要运行的两个线程，扫描线程和Socket通信线程
	ReceiveThread_Scan scan_thread = new ReceiveThread_Scan();
	ReceiveThread_Socket socket_thread = new ReceiveThread_Socket();

	private static ReceiveCenter instance;

	private ReceiveCenter() {
		quitFlag = QuitLockFlag.getInstance();
		dbUtil = new DBUtil_V3();
		syncFlag = true;
		xstream = new XStream();
		xstream.setClassLoader(getClass().getClassLoader());
		xstream.alias("UMSMsg", UMSMsg.class);
		xstream.alias("BasicMsg", BasicMsg.class);
		xstream.alias("MsgAttachment", MsgAttachment.class);
		xstream.alias("MsgContent", MsgContent.class);
		xstream.alias("Participant", Participant.class);
		xstream.alias("EmailMsgPlus", EmailMsgPlus.class);
		inMsgMap = new ConcurrentHashMap();
		inMsgBatchMap = new ConcurrentHashMap();
		clientSocketThreadMap = new ConcurrentHashMap();
		try {
			initClientWS();
		} catch (Exception e) {
			Res.logExceptionTrace(e);
		}
		Res.log(Res.INFO, "UMS3.0消息接收线程启动");
	}

	public synchronized static ReceiveCenter getInstance() {
		if (instance == null) {
			instance = new ReceiveCenter();
		}
		return instance;
	}

	private void initClientWS() throws Exception {
		clientWSArr = ClientWSUtil.getClientWSMap();
//		clientWSArr = new ConcurrentHashMap();
//		Properties p = new Properties();
//
//		InputStream is = new ServletTemp()
//				.getInputStreamFromFile("/resources/clientws.props");
//		p.load(is);
//		is.close();
//		Iterator serviceid_it = p.keySet().iterator();
//		while (serviceid_it.hasNext()) {
//			String serviceid = (String) serviceid_it.next();
//			String className = p.getProperty(serviceid);
//			Object clientWS = Class.forName(className).newInstance();
//			clientWSArr.put(serviceid, clientWS);
//		}

	}

	/**
	 * 获取已经存储在数据库中的服务接收的消息
	 * 
	 * @param serviceIDs
	 *            UMS服务集合
	 * @return
	 */
	private Vector[] receiveMsgsFromDB(List serviceIDs) {
		if (serviceIDs == null || serviceIDs.size() == 0) {
			return null;
		}
		ArrayList msgList = queryInMsgs(serviceIDs);
		if (msgList == null || msgList.size() == 0) {
			return null;
		}
		Vector[] vectors = new Vector[serviceIDs.size()];
		for (int n = 0; n < serviceIDs.size(); n++) {
			String xml = "";
			Vector v = new Vector(2);// 有两个元素，第一个元素是xml
			// basic消息字符串，第二个元素是与之对应的BatchBean
			// 先分离出某一服务号的所有消息
			ArrayList singleServiceMsgList = new ArrayList();
			for (int i = 0; i < msgList.size(); i++) {
				if (((UMSMsg) msgList.get(i)).getBasicMsg().getServiceID()
						.equalsIgnoreCase(serviceIDs.get(n).toString())) {
					singleServiceMsgList.add(msgList.get(i));
				}
			}
			// 然后再对分离出来的消息进行组合和转换
			BatchBean[] batchBeans = new BatchBean[singleServiceMsgList.size()];
			BasicMsg[] basics = new BasicMsg[singleServiceMsgList.size()];
			for (int i = 0; i < singleServiceMsgList.size(); i++) {
				UMSMsg msg = (UMSMsg) singleServiceMsgList.get(i);
				basics[i] = msg.getBasicMsg();
				batchBeans[i] = new BatchBean(msg.getBatchNO(), msg
						.getSerialNO(), msg.getSequenceNO());
			}
			try {
				if (basics.length > 0) {
					xml = xstream.toXML(basics);
					v.add(xml);
					v.add(batchBeans);
				}
			} catch (Exception e) {
				Res.log(Res.ERROR, "消息对象解析错误" + e.getMessage());
				Res.logExceptionTrace(e);
				e.printStackTrace();
			}
			vectors[n] = v;
		}
		this.opData(msgList);
		return vectors;
	}

	/**
	 * 查询所有服务对应的接收消息
	 * 
	 * @param serviceIDs
	 *            所有活动服务的ID
	 * @return 接收消息集合
	 */
	private ArrayList queryInMsgs(List serviceIDs) {
		Connection conn = null;
		ArrayList msgArr = null;
		try {
			conn = DriverManager.getConnection(DataBaseOp.getPoolName());
			Statement st = conn.createStatement();
			PreparedStatement attPrep = conn
					.prepareStatement("SELECT * FROM IN_OUT_ATTACHMENTS_V3 WHERE BATCHNO = ? AND SERIALNO = ? AND SEQUENCENO = ?");
			ResultSet rs = st.executeQuery(Util.getDialect()
					.getInMsgReceiveSQL_V3(serviceIDs));
			msgArr = dbUtil.queryInMsgs(rs, attPrep);
			st.close();
			attPrep.close();
		} catch (SQLException e) {
			Res.log(Res.ERROR, "接收消息过程中发生数据库错误." + e.getMessage());
			Res.logExceptionTrace(e);
			e.printStackTrace();
		} catch (IOException e) {
			Res.log(Res.ERROR, "接收消息过程中附件读取失败." + e.getMessage());
			Res.logExceptionTrace(e);
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return msgArr;
	}

	/**
	 * 将in_ready_v3中的消息移到in_ok_v3中，表示这些消息已经被读取了
	 */
	private void opData(ArrayList msgs) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(DataBaseOp.getPoolName());
			conn.setAutoCommit(false);
			// save to in_ok_v3
			PreparedStatement inPrep = conn.prepareStatement(DBUtil_V3
					.createInMsgInsertSQL(DBUtil_V3.MESSAGE_IN_OK_V3));
			// delete from in_ready_v3
			PreparedStatement deletePrep = conn.prepareStatement(DBUtil_V3
					.createDeleteMsgSQL(DBUtil_V3.MESSAGE_IN_READY_V3));
			for (int i = 0; i < msgs.size(); i++) {
				UMSMsg msg = (UMSMsg) msgs.get(i);
				try {
					dbUtil.executeInMsgInsertStatement(inPrep, null, msg,
							DBUtil_V3.MESSAGE_IN_OK_V3, conn);
				} catch (IOException e) {
					Res.log(Res.ERROR, "接收消息的附件出现异常." + e.getMessage());
					Res.logExceptionTrace(e);
				}
				dbUtil.excuteMsgDeleteStatement(msg.getBatchNO(), msg
						.getSerialNO(), msg.getSequenceNO(), deletePrep);
			}
			inPrep.close();
			deletePrep.close();
			conn.commit();
		} catch (SQLException e) {
			Res.log(Res.ERROR, "拨入消息数据库表转移过程中出现错误！" + e.getMessage());
			Res.logExceptionTrace(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					Res.log(Res.ERROR, "拨入消息数据库表转移过程中数据库连接关闭出现错误！"
							+ e.getMessage());
					Res.logExceptionTrace(e);
				}
			}
		}
	}

	/**
	 * 获取拨入消息的内容. UMS在运行时会收取拨入消息，并放入一个Map中，作为缓存，
	 * 外部应用从UMS提供的接收接口接收消息时，其实是从Map中拿值。
	 * 有三种方式可能会调用这个方法：1.服务主动调用UMS的接收WebService；2.UMS通过Socket主动发送给服务它所接收到的消息；3.UMS通过服务提供的WebService主动发送给服务它所接收到的消息；
	 * 具体服务调用该方法是有且仅有一种情况，不能有两种或两种以上的方法同时调用。
	 * 
	 * @param serviceID
	 *            拨入消息接收方服务的ID
	 * @return String，xml格式
	 */
	public static String getInMsg4Service(String serviceID) {
		String xml = "";
		if (syncFlag) {
			xml = (String) inMsgMap.get(serviceID);
			if (inMsgMap.get(serviceID) == null) {// 没有接收消息存在
				xml = "";
			} else {
				// 如果存在接收消息，则在消息被收走后，应从缓存中移除
				inMsgMap.remove(serviceID);
				// batchBean不移走是因为在socket断开连接反存过程中还需要用到
				// inMsgBatchMap.remove(serviceID);
			}
		}
		return xml;
	}

	public void pullBackMsg(String serviceID, String receivedXML) {
		Res.log(Res.INFO, "UMS将已断开连接服务的缓存中的接收消息返存至数据库...");
		// syncFlag = false;//这里不需要将同步标记设置为false，单个服务的接收停止不会影响UMS全局
		Connection conn = null;
		PreparedStatement inPrep = null;
		PreparedStatement attachPrep = null;
		PreparedStatement deleteStatement = null;
		try {
			conn = DriverManager.getConnection(DataBaseOp.getPoolName());
			conn.setAutoCommit(false);
			inPrep = conn.prepareStatement(DBUtil_V3
					.createInMsgInsertSQL(DBUtil_V3.MESSAGE_IN_READY_V3));
			attachPrep = conn.prepareStatement(DBUtil_V3
					.createAttachInsertSQL());
			deleteStatement = conn.prepareStatement(DBUtil_V3
					.createDeleteMsgSQL(DBUtil_V3.MESSAGE_IN_OK_V3));

			BatchBean[] beans = (BatchBean[]) inMsgBatchMap.get(serviceID);
			BasicMsg[] basics = (BasicMsg[]) xstream.fromXML(receivedXML);
			for (int i = 0; i < basics.length; i++) {
				UMSMsg msg = new UMSMsg();
				msg.setBasicMsg(basics[i]);
				msg.setBatchMode(UMSMsg.UMSMSG_BATCHMODE_SINGLE);
				msg.setBatchNO(beans[i].getBatchNO());
				msg.setSerialNO(beans[i].getSerialNO());
				msg.setSequenceNO(beans[i].getSequenceNO());
				msg.setStatusFlag(UMSMsg.UMSMSG_STATUS_VALID);
				dbUtil.executeInMsgInsertStatement(inPrep, attachPrep, msg,
						DBUtil_V3.MESSAGE_IN_READY_V3, conn);
				dbUtil.excuteMsgDeleteStatement(beans[i].getBatchNO(), beans[i]
						.getSerialNO(), beans[i].getSequenceNO(),
						deleteStatement);

			}
			inMsgMap.remove(serviceID);
			inMsgBatchMap.remove(serviceID);
			conn.commit();
			Res.log(Res.INFO, new StringBuffer("断开连接的服务:").append(serviceID)
					.append("缓存中的接收消息返存处理完毕！").toString());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			Res.log(Res.ERROR, "接收消息的附件时出现异常" + e.getMessage());
			Res.logExceptionTrace(e);
		} finally {
			try {
				if (inPrep != null) {
					inPrep.close();
				}
				if (attachPrep != null) {
					attachPrep.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
				Res.log(Res.ERROR, "UMS将缓存中的接收消息返存至数据库时数据库产生异常");
				Res.logExceptionTrace(ex);
			}
		}
	}

	/**
	 * 将所有服务的接收消息的缓存返存至数据库
	 */
	private void pullBackMsgs() {
		Res.log(Res.INFO, "UMS将缓存中的接收消息返存至数据库...");
		syncFlag = false;
		Iterator it = inMsgMap.keySet().iterator();
		Connection conn = null;
		PreparedStatement inPrep = null;
		PreparedStatement attachPrep = null;
		PreparedStatement deleteStatement = null;
		try {
			conn = DriverManager.getConnection(DataBaseOp.getPoolName());
			conn.setAutoCommit(false);
			inPrep = conn.prepareStatement(DBUtil_V3
					.createInMsgInsertSQL(DBUtil_V3.MESSAGE_IN_READY_V3));
			attachPrep = conn.prepareStatement(DBUtil_V3
					.createAttachInsertSQL());
			deleteStatement = conn.prepareStatement(DBUtil_V3
					.createDeleteMsgSQL(DBUtil_V3.MESSAGE_IN_OK_V3));
			while (it.hasNext()) {
				String serviceID = (String) it.next();
				String xmlBasics = (String) inMsgMap.get(serviceID);
				BatchBean[] beans = (BatchBean[]) inMsgBatchMap.get(serviceID);
				BasicMsg[] basics = (BasicMsg[]) xstream.fromXML(xmlBasics);
				for (int i = 0; i < basics.length; i++) {
					UMSMsg msg = new UMSMsg();
					msg.setBasicMsg(basics[i]);
					msg.setBatchMode(UMSMsg.UMSMSG_BATCHMODE_SINGLE);
					msg.setBatchNO(beans[i].getBatchNO());
					msg.setSerialNO(beans[i].getSerialNO());
					msg.setSequenceNO(beans[i].getSequenceNO());
					msg.setStatusFlag(UMSMsg.UMSMSG_STATUS_VALID);
					dbUtil.executeInMsgInsertStatement(inPrep, attachPrep, msg,
							DBUtil_V3.MESSAGE_IN_READY_V3, conn);
					dbUtil.excuteMsgDeleteStatement(beans[i].getBatchNO(),
							beans[i].getSerialNO(), beans[i].getSequenceNO(),
							deleteStatement);

				}
				inMsgMap.remove(serviceID);
				inMsgBatchMap.remove(serviceID);
			}
			conn.commit();
			Res.log(Res.INFO, "缓存中的接收消息返存处理完毕！");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			Res.log(Res.ERROR, "接收消息的附件时出现异常" + e.getMessage());
			Res.logExceptionTrace(e);
		} finally {
			try {
				if (inPrep != null) {
					inPrep.close();
				}
				if (attachPrep != null) {
					attachPrep.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
				Res.log(Res.ERROR, "UMS将缓存中的接收消息返存至数据库时数据库产生异常");
				Res.logExceptionTrace(ex);
			}
		}

	}

	public static Map getInMsgMap() {
		return inMsgMap;
	}

	public void run() {

		scan_thread.start();
		socket_thread.start();		
		while (!stop) {
			boolean ws_hasnext = false;
			Iterator serviceid_it = clientWSArr.keySet().iterator();
			while (serviceid_it.hasNext()) {
				String serviceID = (String) serviceid_it.next();
				// 相应的serviceID是否有xml消息接收到？
				String xml = ReceiveCenter.getInMsg4Service(serviceID);
				if (xml != null && !xml.equals("")) {
					// 有消息时，将消息发送给指定的服务
					ws_hasnext = true;
					((UMSClientWS) clientWSArr.get(serviceID)).sendInMsg(serviceID,
							xml);
				}					
			}
			if (!ws_hasnext) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
				}
			}
		}
	}

	public void pleaseStop() {
		this.pullBackMsgs();
		this.stop = true; // Set the stop flag
		this.interrupt();
		Iterator it = clientSocketThreadMap.keySet().iterator();
		while (it.hasNext()) {
			ProcessSocket t = (ProcessSocket) clientSocketThreadMap.get(it
					.next());
			t.setStatusflag(false);
		}
		try {
			if (socket_thread.getServerSocket() != null
					&& socket_thread.getServerSocket().isBound()
					&& !socket_thread.getServerSocket().isClosed()) {
				Res.log(Res.INFO, "关闭消息接收线程ServerSocket："
						+ socket_thread.getServerSocket().getLocalPort()
						+ "端口.");

				socket_thread.getServerSocket().close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		instance = null;
		ReceiveCenter.inMsgMap.clear();
		ReceiveCenter.clientSocketThreadMap.clear();
		ReceiveCenter.clientWSArr.clear();
		ReceiveCenter.inMsgBatchMap.clear();
		Res.log(Res.INFO, "UMS消息接收线程退出服务！");
	}

	/** **************内部类***************** */
	/**
	 * 通过读数据库，将已有的回复消息保存在内存中
	 */
	class ReceiveThread_Scan extends Thread {

		public ReceiveThread_Scan() {
			super("ReceiveThread_Scan");
		}

		public void run() {
			Res.log(Res.INFO, "应用接收消息缓存线程启动.");
			boolean hasNext = false;
			List services = new ArrayList();
			while (!Thread.interrupted() && !quitFlag.getLockFlag() && !stop) {
				// System.out.println("UMS开始扫描接收到的消息...");
				Map activeServiceMap = AppServiceUtil.getActiveAppServiceMap();
				Iterator it = activeServiceMap.keySet().iterator();
				services.clear();
				while (it.hasNext()) {
					String serviceID = (String) it.next();
					// 如果缓存中已有相关service的接收消息，则不必再查
					if (!inMsgMap.containsKey(serviceID)) {
						services.add(serviceID);
					}
				}
				Vector[] vectors = receiveMsgsFromDB(services);
				if (vectors != null) {
					for (int i = 0; i < vectors.length; i++) {
						if (vectors[i] != null && vectors[i].size() > 0) {
							// 要同时得到消息的xml对象和BatchBean对象
							String xml = (String) vectors[i].get(0);
							BatchBean[] beans = (BatchBean[]) vectors[i].get(1);
							inMsgMap.put(services.get(i), xml);
							inMsgBatchMap.put(services.get(i), beans);
							// 通知接收消息外推线程，可以启动，并将消息推给服务
							// Thread t = (Thread) clientSocketThreadMap
							// .get(services.get(i));
							// if (t != null)
							// t.notify();
						}
					}
					hasNext = true;
				} else {
					hasNext = false;
				}
				try {
					if (!hasNext) {
						sleep(1000 * 1);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 用于Socket接收消息的线程,与外部应用的API对接
	 * 
	 * @author Qil.Wong
	 * 
	 */
	class ReceiveThread_Socket extends Thread {

		private ServerSocket s;

		public ReceiveThread_Socket() {
			super("ReceiveThread_Socket");
		}

		public void run() {
			try {
				// 开启线程，打开端口，等待连接
				int port = Integer.parseInt(PropertyUtil.getProperty(
						"socket_port", "/resources/receive.props"));
				int timeout = Integer.parseInt(PropertyUtil.getProperty(
						"time_out", "/resources/receive.props"));
				Res.log(Res.INFO, "应用接收消息Socket线程启动. Socket 端口为：" + port);
				s = new ServerSocket(port);

				while (!Thread.interrupted() && !quitFlag.getLockFlag()
						&& !stop) {
					Socket socket = null;
					try {
						socket = s.accept();
						// 再开启一个处理线程，处理具体的socket业务
						new ProcessSocket(socket, timeout);
					} catch (Exception e) {
						Res.log(Res.ERROR, "Server Socket断开");
						// Res.logExceptionTrace(e);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public ServerSocket getServerSocket() {
			return s;
		}
	}

	/**
	 * 客户端服务socket处理线程,只能允许一个ServiceID同时连接，否则会在消息介绍反馈时产生误差，会返回给多个同时链接进来的socket进程
	 * 
	 * @author Qil.Wong
	 * 
	 */
	class ProcessSocket extends Thread implements ActionListener {

		private Socket socket;
		private int timeout;
		private ObjectInputStream ois;
		private ObjectOutputStream oos;
		// 客户端的地址
		private String socketAddr;
		// 状态标记，如果是true，该线程会一直运行下去，如果为false，则会终止
		private boolean statusflag = true;
		Timer serverToClientHeartTimer;
		int serverToClientHeartbeatCount;

		public ProcessSocket(Socket socket, int timeout) {
			super("ReceiveThread_Socket:" + socket.getInetAddress().toString());
			this.socket = socket;
			this.socketAddr = socket.getInetAddress().toString();
			this.timeout = timeout;
			iniSocket();
			serverToClientHeartTimer = new Timer(30 * 1000, this);
			serverToClientHeartbeatCount = 3;
			start();
		}

		// UMS Server发送心跳信号给client端，确认是否正常连接
		public void actionPerformed(ActionEvent e) {
			// 因为这个是awt线程，为防止长时间等待客户端响应，这里需使用SwingWorker来处理
			SwingWorker worker = new SwingWorker() {
				public Object construct() {
					int count = 0;
					while (count < serverToClientHeartbeatCount) {
						try {
							// 发送心跳信号
							oos.writeObject(HEARTSIG);
							String ret = ois.readObject().toString();
							if (!ret.equalsIgnoreCase(HEARTSIG)) {
								Res.log(Res.DEBUG, "Socket Client:"
										+ socket.getInetAddress().toString()
										+ " 发送过来的不是心跳信号：" + HEARTSIG + ",而是"
										+ ret);
								serverToClientHeartTimer.stop();
								setStatusflag(false);
							}
						} catch (IOException e1) {
							if (count == serverToClientHeartbeatCount - 1) {
								Res.log(Res.DEBUG, "Socket Client:"
										+ socket.getInetAddress().toString()
										+ " 已无效。");
								serverToClientHeartTimer.stop();
								setStatusflag(false);
							}
						} catch (ClassNotFoundException e1) {
							e1.printStackTrace();
						}
						count++;
					}
					return null;
				}
			};
			worker.start();
		}

		/**
		 * 初始化端口
		 */
		public void iniSocket() {
			try {
				// socket.setSoTimeout(timeout * 1000);
				ois = new ObjectInputStream(socket.getInputStream());
				oos = new ObjectOutputStream(socket.getOutputStream());
			} catch (SocketException e) {
				statusflag = false;
				Res.log(Res.ERROR, "消息接收Socket出错" + e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				statusflag = false;
				Res.log(Res.ERROR, "消息接收Socket出错" + e.getMessage());
				e.printStackTrace();
			}
		}

		public void run() {
			ServiceInfo receiveServiceBean = null;
			String receivedXML = "";
			boolean heartBeatFlag = false;
			boolean dupe = false;// 重复登录标记位
			try {
				Object obj = ois.readObject();
				// 客户端心跳信号
				if (obj instanceof String) {
					heartBeatFlag = true;
					oos.writeObject(HEARTSIG);
				} else {// 正常消息接收信号
					receiveServiceBean = (ServiceInfo) obj;
					// 测试连接是否通畅，发送的是测试信号
					if (receiveServiceBean.getAppID().equals(
							UMSReceiveService.connectionTestSignal)
							&& receiveServiceBean.getAppPsw().equals(
									UMSReceiveService.connectionTestSignal)
							&& receiveServiceBean.getServiceID().equals(
									UMSReceiveService.connectionTestSignal)) {
						oos.writeObject(UMSReceiveService.connectionTestReturn);
						statusflag = false;
					} else {
						// 外部应用服务登陆
						int loginFlag = DBUtil_V3.login(receiveServiceBean
								.getAppID(), receiveServiceBean.getAppPsw());
						if (loginFlag != DBUtil_V3.LOGIN_SUCCESS) {
							oos.writeObject(DBUtil_V3
									.parseLoginErrResult(loginFlag));
							statusflag = false;
						}
						// 如果之前已经有连接进来,而且还对应同一serviceID，则不允许再进入
						if (clientSocketThreadMap.get(receiveServiceBean
								.getServiceID()) != null) {
							statusflag = false;
							oos.writeObject(DUPLICATE);
							dupe = true;
						} else {
							Res.log(Res.INFO, "外部应用服务Socket接入，地址为："
									+ socket.getInetAddress().toString()
									+ "，服务号为："
									+ receiveServiceBean.getServiceID());
							clientSocketThreadMap.put(receiveServiceBean
									.getServiceID(), this);
							super.setName(this.getName() + ";ServiceID:"
									+ receiveServiceBean.getServiceID());// 给线程一个名字
						}
					}
					while (statusflag) {
						receivedXML = ReceiveCenter
								.getInMsg4Service(receiveServiceBean
										.getServiceID());
						// 定时器启动，测试与客户端的连通有效性
						serverToClientHeartTimer.start();
						// 如果没有应用对应的消息，则得到的是""
						if (!receivedXML.equals("")) {
							oos.writeObject(receivedXML);
							serverToClientHeartTimer.stop();
						} else {
							sleep(1000);
							// wait();
						}
					}
					// 跳出了循环。如果线程中断,但不是重复登录
					if (!dupe) {
						clientSocketThreadMap.remove(receiveServiceBean
								.getServiceID());
					}
				}
			} catch (IOException e) {
				statusflag = false;
				if (!dupe) {
					Res.log(Res.ERROR, "接收消息Socket读取数据出错,client端已断开。"
							+ e.getMessage());
					// 客户端如果断开，则需要将已经在缓存中的消息返存到数据库中
					if (receiveServiceBean != null && !heartBeatFlag) {
						pullBackMsg(receiveServiceBean.getServiceID(),
								receivedXML);
						// removed from thread map
						clientSocketThreadMap.remove(receiveServiceBean
								.getServiceID());
					}
				}
			} catch (ClassNotFoundException e) {
				statusflag = false;
				Res.log(Res.ERROR, "接收消息Socket读取输入流时数据出错，传入的不是合法的登录消息"
						+ e.getMessage());
				Res.logExceptionTrace(e);
			} catch (InterruptedException e) {
				e.printStackTrace();
				// removed from thread map
				if (receiveServiceBean != null && !heartBeatFlag) {
					clientSocketThreadMap.remove(receiveServiceBean
							.getServiceID());
				}
			}
			if (!heartBeatFlag && !dupe) {
				Res.log(Res.DEBUG, socketAddr + "连接断开。");
			}
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			serverToClientHeartTimer.stop();
			serverToClientHeartTimer = null;
		}

		// private synchronized void
		public boolean isStatusflag() {
			return statusflag;
		}

		public void setStatusflag(boolean statusflag) {
			this.statusflag = statusflag;
		}
	}

	/**
	 * 为保证应用接收基本消息BasicMsg后能保留batchNO，serialNO和sequenceNO，
	 * 将在另一个HashMap中保留缓存中的消息对应的batchNO，serialNO和sequenceNO的值。
	 * 
	 * @author Qil.Wong
	 * 
	 */
	class BatchBean {

		private String batchNO;
		private int serialNO;
		private int[] sequenceNO;

		public BatchBean() {
		}

		public BatchBean(String batchNO, int serialNO, int[] sequenceNO) {
			this.batchNO = batchNO;
			this.serialNO = serialNO;
			this.sequenceNO = sequenceNO;
		}

		public String getBatchNO() {
			return batchNO;
		}

		public void setBatchNO(String batchNO) {
			this.batchNO = batchNO;
		}

		public int getSerialNO() {
			return serialNO;
		}

		public void setSerialNO(int serialNO) {
			this.serialNO = serialNO;
		}

		public int[] getSequenceNO() {
			return sequenceNO;
		}

		public void setSequenceNO(int[] sequenceNO) {
			this.sequenceNO = sequenceNO;
		}
	}
}
