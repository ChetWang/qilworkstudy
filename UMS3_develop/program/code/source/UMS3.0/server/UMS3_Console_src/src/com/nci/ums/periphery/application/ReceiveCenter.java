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
	 * ��Ž��յ��Ļ�����Ϣ
	 */
	private static Map inMsgMap;
	/**
	 * ��Ž��յ��Ļ�����Ϣ������������Ҫ���ԣ�����Щ���Բ�������Ӧ�ý��յ�xml�ַ����С� ����BatchNO,SerialNO��SequenceNO
	 */
	private static Map inMsgBatchMap;
	/**
	 * �ͻ���socket���ӵ��̹߳�ϣMap
	 */
	private static Map clientSocketThreadMap;

	private static Map clientWSArr;

	protected QuitLockFlag quitFlag;
	/**
	 * ״̬���
	 */
	protected boolean stop = false;
	/**
	 * ͬ����ǣ���UMS������������Ҫ���滺���е���Ϣ����ʱ��Ӧ����Ϣ���շ���������У�
	 * ��ʹ��Ӧ�ķ�����ڽ�����Ϣ��Ҳ���ܽ���Ϣ���͹�ȥ�������������ࡣ
	 */
	private static boolean syncFlag;
	/**
	 * ��������ź�
	 */
	private static String HEARTSIG = "1982";
	/**
	 * �����ظ���¼�ź�
	 */
	private static String DUPLICATE = "duplicate";

	// ReceiveCenter��Ҫ���е������̣߳�ɨ���̺߳�Socketͨ���߳�
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
		Res.log(Res.INFO, "UMS3.0��Ϣ�����߳�����");
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
	 * ��ȡ�Ѿ��洢�����ݿ��еķ�����յ���Ϣ
	 * 
	 * @param serviceIDs
	 *            UMS���񼯺�
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
			Vector v = new Vector(2);// ������Ԫ�أ���һ��Ԫ����xml
			// basic��Ϣ�ַ������ڶ���Ԫ������֮��Ӧ��BatchBean
			// �ȷ����ĳһ����ŵ�������Ϣ
			ArrayList singleServiceMsgList = new ArrayList();
			for (int i = 0; i < msgList.size(); i++) {
				if (((UMSMsg) msgList.get(i)).getBasicMsg().getServiceID()
						.equalsIgnoreCase(serviceIDs.get(n).toString())) {
					singleServiceMsgList.add(msgList.get(i));
				}
			}
			// Ȼ���ٶԷ����������Ϣ������Ϻ�ת��
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
				Res.log(Res.ERROR, "��Ϣ�����������" + e.getMessage());
				Res.logExceptionTrace(e);
				e.printStackTrace();
			}
			vectors[n] = v;
		}
		this.opData(msgList);
		return vectors;
	}

	/**
	 * ��ѯ���з����Ӧ�Ľ�����Ϣ
	 * 
	 * @param serviceIDs
	 *            ���л�����ID
	 * @return ������Ϣ����
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
			Res.log(Res.ERROR, "������Ϣ�����з������ݿ����." + e.getMessage());
			Res.logExceptionTrace(e);
			e.printStackTrace();
		} catch (IOException e) {
			Res.log(Res.ERROR, "������Ϣ�����и�����ȡʧ��." + e.getMessage());
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
	 * ��in_ready_v3�е���Ϣ�Ƶ�in_ok_v3�У���ʾ��Щ��Ϣ�Ѿ�����ȡ��
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
					Res.log(Res.ERROR, "������Ϣ�ĸ��������쳣." + e.getMessage());
					Res.logExceptionTrace(e);
				}
				dbUtil.excuteMsgDeleteStatement(msg.getBatchNO(), msg
						.getSerialNO(), msg.getSequenceNO(), deletePrep);
			}
			inPrep.close();
			deletePrep.close();
			conn.commit();
		} catch (SQLException e) {
			Res.log(Res.ERROR, "������Ϣ���ݿ��ת�ƹ����г��ִ���" + e.getMessage());
			Res.logExceptionTrace(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					Res.log(Res.ERROR, "������Ϣ���ݿ��ת�ƹ��������ݿ����ӹرճ��ִ���"
							+ e.getMessage());
					Res.logExceptionTrace(e);
				}
			}
		}
	}

	/**
	 * ��ȡ������Ϣ������. UMS������ʱ����ȡ������Ϣ��������һ��Map�У���Ϊ���棬
	 * �ⲿӦ�ô�UMS�ṩ�Ľ��սӿڽ�����Ϣʱ����ʵ�Ǵ�Map����ֵ��
	 * �����ַ�ʽ���ܻ�������������1.������������UMS�Ľ���WebService��2.UMSͨ��Socket�������͸������������յ�����Ϣ��3.UMSͨ�������ṩ��WebService�������͸������������յ�����Ϣ��
	 * ���������ø÷��������ҽ���һ����������������ֻ��������ϵķ���ͬʱ���á�
	 * 
	 * @param serviceID
	 *            ������Ϣ���շ������ID
	 * @return String��xml��ʽ
	 */
	public static String getInMsg4Service(String serviceID) {
		String xml = "";
		if (syncFlag) {
			xml = (String) inMsgMap.get(serviceID);
			if (inMsgMap.get(serviceID) == null) {// û�н�����Ϣ����
				xml = "";
			} else {
				// ������ڽ�����Ϣ��������Ϣ�����ߺ�Ӧ�ӻ������Ƴ�
				inMsgMap.remove(serviceID);
				// batchBean����������Ϊ��socket�Ͽ����ӷ�������л���Ҫ�õ�
				// inMsgBatchMap.remove(serviceID);
			}
		}
		return xml;
	}

	public void pullBackMsg(String serviceID, String receivedXML) {
		Res.log(Res.INFO, "UMS���ѶϿ����ӷ���Ļ����еĽ�����Ϣ���������ݿ�...");
		// syncFlag = false;//���ﲻ��Ҫ��ͬ���������Ϊfalse����������Ľ���ֹͣ����Ӱ��UMSȫ��
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
			Res.log(Res.INFO, new StringBuffer("�Ͽ����ӵķ���:").append(serviceID)
					.append("�����еĽ�����Ϣ���洦����ϣ�").toString());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			Res.log(Res.ERROR, "������Ϣ�ĸ���ʱ�����쳣" + e.getMessage());
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
				Res.log(Res.ERROR, "UMS�������еĽ�����Ϣ���������ݿ�ʱ���ݿ�����쳣");
				Res.logExceptionTrace(ex);
			}
		}
	}

	/**
	 * �����з���Ľ�����Ϣ�Ļ��淵�������ݿ�
	 */
	private void pullBackMsgs() {
		Res.log(Res.INFO, "UMS�������еĽ�����Ϣ���������ݿ�...");
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
			Res.log(Res.INFO, "�����еĽ�����Ϣ���洦����ϣ�");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			Res.log(Res.ERROR, "������Ϣ�ĸ���ʱ�����쳣" + e.getMessage());
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
				Res.log(Res.ERROR, "UMS�������еĽ�����Ϣ���������ݿ�ʱ���ݿ�����쳣");
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
				// ��Ӧ��serviceID�Ƿ���xml��Ϣ���յ���
				String xml = ReceiveCenter.getInMsg4Service(serviceID);
				if (xml != null && !xml.equals("")) {
					// ����Ϣʱ������Ϣ���͸�ָ���ķ���
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
				Res.log(Res.INFO, "�ر���Ϣ�����߳�ServerSocket��"
						+ socket_thread.getServerSocket().getLocalPort()
						+ "�˿�.");

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
		Res.log(Res.INFO, "UMS��Ϣ�����߳��˳�����");
	}

	/** **************�ڲ���***************** */
	/**
	 * ͨ�������ݿ⣬�����еĻظ���Ϣ�������ڴ���
	 */
	class ReceiveThread_Scan extends Thread {

		public ReceiveThread_Scan() {
			super("ReceiveThread_Scan");
		}

		public void run() {
			Res.log(Res.INFO, "Ӧ�ý�����Ϣ�����߳�����.");
			boolean hasNext = false;
			List services = new ArrayList();
			while (!Thread.interrupted() && !quitFlag.getLockFlag() && !stop) {
				// System.out.println("UMS��ʼɨ����յ�����Ϣ...");
				Map activeServiceMap = AppServiceUtil.getActiveAppServiceMap();
				Iterator it = activeServiceMap.keySet().iterator();
				services.clear();
				while (it.hasNext()) {
					String serviceID = (String) it.next();
					// ����������������service�Ľ�����Ϣ���򲻱��ٲ�
					if (!inMsgMap.containsKey(serviceID)) {
						services.add(serviceID);
					}
				}
				Vector[] vectors = receiveMsgsFromDB(services);
				if (vectors != null) {
					for (int i = 0; i < vectors.length; i++) {
						if (vectors[i] != null && vectors[i].size() > 0) {
							// Ҫͬʱ�õ���Ϣ��xml�����BatchBean����
							String xml = (String) vectors[i].get(0);
							BatchBean[] beans = (BatchBean[]) vectors[i].get(1);
							inMsgMap.put(services.get(i), xml);
							inMsgBatchMap.put(services.get(i), beans);
							// ֪ͨ������Ϣ�����̣߳�����������������Ϣ�Ƹ�����
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
	 * ����Socket������Ϣ���߳�,���ⲿӦ�õ�API�Խ�
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
				// �����̣߳��򿪶˿ڣ��ȴ�����
				int port = Integer.parseInt(PropertyUtil.getProperty(
						"socket_port", "/resources/receive.props"));
				int timeout = Integer.parseInt(PropertyUtil.getProperty(
						"time_out", "/resources/receive.props"));
				Res.log(Res.INFO, "Ӧ�ý�����ϢSocket�߳�����. Socket �˿�Ϊ��" + port);
				s = new ServerSocket(port);

				while (!Thread.interrupted() && !quitFlag.getLockFlag()
						&& !stop) {
					Socket socket = null;
					try {
						socket = s.accept();
						// �ٿ���һ�������̣߳���������socketҵ��
						new ProcessSocket(socket, timeout);
					} catch (Exception e) {
						Res.log(Res.ERROR, "Server Socket�Ͽ�");
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
	 * �ͻ��˷���socket�����߳�,ֻ������һ��ServiceIDͬʱ���ӣ����������Ϣ���ܷ���ʱ�������᷵�ظ����ͬʱ���ӽ�����socket����
	 * 
	 * @author Qil.Wong
	 * 
	 */
	class ProcessSocket extends Thread implements ActionListener {

		private Socket socket;
		private int timeout;
		private ObjectInputStream ois;
		private ObjectOutputStream oos;
		// �ͻ��˵ĵ�ַ
		private String socketAddr;
		// ״̬��ǣ������true�����̻߳�һֱ������ȥ�����Ϊfalse�������ֹ
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

		// UMS Server���������źŸ�client�ˣ�ȷ���Ƿ���������
		public void actionPerformed(ActionEvent e) {
			// ��Ϊ�����awt�̣߳�Ϊ��ֹ��ʱ��ȴ��ͻ�����Ӧ��������ʹ��SwingWorker������
			SwingWorker worker = new SwingWorker() {
				public Object construct() {
					int count = 0;
					while (count < serverToClientHeartbeatCount) {
						try {
							// ���������ź�
							oos.writeObject(HEARTSIG);
							String ret = ois.readObject().toString();
							if (!ret.equalsIgnoreCase(HEARTSIG)) {
								Res.log(Res.DEBUG, "Socket Client:"
										+ socket.getInetAddress().toString()
										+ " ���͹����Ĳ��������źţ�" + HEARTSIG + ",����"
										+ ret);
								serverToClientHeartTimer.stop();
								setStatusflag(false);
							}
						} catch (IOException e1) {
							if (count == serverToClientHeartbeatCount - 1) {
								Res.log(Res.DEBUG, "Socket Client:"
										+ socket.getInetAddress().toString()
										+ " ����Ч��");
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
		 * ��ʼ���˿�
		 */
		public void iniSocket() {
			try {
				// socket.setSoTimeout(timeout * 1000);
				ois = new ObjectInputStream(socket.getInputStream());
				oos = new ObjectOutputStream(socket.getOutputStream());
			} catch (SocketException e) {
				statusflag = false;
				Res.log(Res.ERROR, "��Ϣ����Socket����" + e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				statusflag = false;
				Res.log(Res.ERROR, "��Ϣ����Socket����" + e.getMessage());
				e.printStackTrace();
			}
		}

		public void run() {
			ServiceInfo receiveServiceBean = null;
			String receivedXML = "";
			boolean heartBeatFlag = false;
			boolean dupe = false;// �ظ���¼���λ
			try {
				Object obj = ois.readObject();
				// �ͻ��������ź�
				if (obj instanceof String) {
					heartBeatFlag = true;
					oos.writeObject(HEARTSIG);
				} else {// ������Ϣ�����ź�
					receiveServiceBean = (ServiceInfo) obj;
					// ���������Ƿ�ͨ�������͵��ǲ����ź�
					if (receiveServiceBean.getAppID().equals(
							UMSReceiveService.connectionTestSignal)
							&& receiveServiceBean.getAppPsw().equals(
									UMSReceiveService.connectionTestSignal)
							&& receiveServiceBean.getServiceID().equals(
									UMSReceiveService.connectionTestSignal)) {
						oos.writeObject(UMSReceiveService.connectionTestReturn);
						statusflag = false;
					} else {
						// �ⲿӦ�÷����½
						int loginFlag = DBUtil_V3.login(receiveServiceBean
								.getAppID(), receiveServiceBean.getAppPsw());
						if (loginFlag != DBUtil_V3.LOGIN_SUCCESS) {
							oos.writeObject(DBUtil_V3
									.parseLoginErrResult(loginFlag));
							statusflag = false;
						}
						// ���֮ǰ�Ѿ������ӽ���,���һ���ӦͬһserviceID���������ٽ���
						if (clientSocketThreadMap.get(receiveServiceBean
								.getServiceID()) != null) {
							statusflag = false;
							oos.writeObject(DUPLICATE);
							dupe = true;
						} else {
							Res.log(Res.INFO, "�ⲿӦ�÷���Socket���룬��ַΪ��"
									+ socket.getInetAddress().toString()
									+ "�������Ϊ��"
									+ receiveServiceBean.getServiceID());
							clientSocketThreadMap.put(receiveServiceBean
									.getServiceID(), this);
							super.setName(this.getName() + ";ServiceID:"
									+ receiveServiceBean.getServiceID());// ���߳�һ������
						}
					}
					while (statusflag) {
						receivedXML = ReceiveCenter
								.getInMsg4Service(receiveServiceBean
										.getServiceID());
						// ��ʱ��������������ͻ��˵���ͨ��Ч��
						serverToClientHeartTimer.start();
						// ���û��Ӧ�ö�Ӧ����Ϣ����õ�����""
						if (!receivedXML.equals("")) {
							oos.writeObject(receivedXML);
							serverToClientHeartTimer.stop();
						} else {
							sleep(1000);
							// wait();
						}
					}
					// ������ѭ��������߳��ж�,�������ظ���¼
					if (!dupe) {
						clientSocketThreadMap.remove(receiveServiceBean
								.getServiceID());
					}
				}
			} catch (IOException e) {
				statusflag = false;
				if (!dupe) {
					Res.log(Res.ERROR, "������ϢSocket��ȡ���ݳ���,client���ѶϿ���"
							+ e.getMessage());
					// �ͻ�������Ͽ�������Ҫ���Ѿ��ڻ����е���Ϣ���浽���ݿ���
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
				Res.log(Res.ERROR, "������ϢSocket��ȡ������ʱ���ݳ�������Ĳ��ǺϷ��ĵ�¼��Ϣ"
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
				Res.log(Res.DEBUG, socketAddr + "���ӶϿ���");
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
	 * Ϊ��֤Ӧ�ý��ջ�����ϢBasicMsg���ܱ���batchNO��serialNO��sequenceNO��
	 * ������һ��HashMap�б��������е���Ϣ��Ӧ��batchNO��serialNO��sequenceNO��ֵ��
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
