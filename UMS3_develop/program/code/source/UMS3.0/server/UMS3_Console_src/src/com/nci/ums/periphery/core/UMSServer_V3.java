/*
 * UMSServer_V3.java
 * 
 * Created on 2007-10-8, 16:03:09
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nci.ums.periphery.core;

import java.io.InputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import com.nci.ums.channel.MessageTimer;
import com.nci.ums.channel.channelmanager.SendOut_V3;
import com.nci.ums.channel.myproxy.SendMessage;
import com.nci.ums.periphery.application.AckCenter;
import com.nci.ums.periphery.application.AppServer;
import com.nci.ums.periphery.application.MonitorServer;
import com.nci.ums.periphery.application.MonitorServer_V3;
import com.nci.ums.periphery.application.ReceiveCenter;
import com.nci.ums.util.DBConnect;
import com.nci.ums.util.DataBaseOp;
import com.nci.ums.util.Res;
import com.nci.ums.util.ServletTemp;

/**
 * ss This class make a main class to server for other application request
 */
public class UMSServer_V3 {

	private SendOut_V3 sendout_v3; // media send out thread

	private ThreadGroup threadGroup; // Control threads of socketreaders

	private Map parserMap; // request code -> parser class

	private ArrayList umsServers; // UMS����˼���

	private AppServer appServer;
	private MonitorServer monitorServer;
	private MonitorServer_V3 monitorServer_V3;
	private ReceiveCenter receiveCenter;
	private AckCenter ackCenter;
	private MessageTimer messageTimer;

	// private JMSReceiver jmsReceiver;

	private static UMSServer_V3 server_v3;

	// ���ݿ��Ƿ������ı��λ
	private static boolean dbFlag = true;
	
	private static boolean socketFlag = true;

	private UMSServer_V3() {
		umsServers = new ArrayList();
	}

	public void checkDB() {
		Res.log(Res.INFO, "UMS3.0���ڼ�����л���...");
		try {
			DBConnect db = new DBConnect();
			db.close();
			Res.log(Res.INFO, "UMS3.0���ݿ⻷������!");
			dbFlag = true;
		} catch (Exception e) {
			Res.log(Res.ERROR, "UMS3.0��ָ�������ݿ��޷���������!����UMS�����������ݿ�����!");
			DataBaseOp.clear();
			dbFlag = false;
			server_v3 = null;
		}
	}

	public static UMSServer_V3 getInstance() {
		if (server_v3 == null) {
			server_v3 = new UMSServer_V3();
		}
		return server_v3;
	}

	public void start() {
		Res.log(Res.INFO, "UMS3.0��������ʼ����...");
		checkDB();
		if (dbFlag)
			initThreads();
		else
			return;
		DBConnect db = null;
		try {
			db = new DBConnect();
			db.executeUpdate("update out_ready set statusFlag=0");
			db.executeUpdate("update out_ready_v3 set statusFlag=0");
			ResultSet rs = db
					.executeQuery("select serverport from system_server");
			while (rs.next()) {
				int port = rs.getInt("serverPort");
				UMSServer umsServer = new UMSServer(port, parserMap);
				umsServer.start();
				umsServers.add(umsServer);
				while(umsServer.getStartFinishedFlag()==UMSServer.UMSSERVER_SOCKET_NOT_STARTED){
					Thread.sleep(100);
				}
				if(umsServer.getStartFinishedFlag()==UMSServer.UMSSERVER_SOCKET_BIND){
					Res.log(Res.WARN, "UMS Server�˿�"+port+"��ͻ��ϵͳ�޷���������������UMS�����Ѿ���������!");
					socketFlag = false;
					return;
				}
				/*
				 * UMSThread umsThread=new UMSThread("UMS
				 * �ⲿ�ӿ�ƽ̨","UMS�ⲿ�ӿ��߳�",Util.getCurrentTimeStr("yyyyMMddHHmmss"),"������",umsServer,"�ڶ˿�"+port+"����");
				 * Res.getUMSThreads().add(umsThread);
				 */
			}
			rs.close();
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
			Res.log(Res.ERROR, "���ܴ����ݿ����System��δ��ʼ��" + e);
                        Res.logExceptionTrace(e);
			// System.exit(1);
		} finally {
			try {
				if (db != null) {
					db.close();
				}
			} catch (Exception e) {
			}
		}
	
		// open sendout media thread
		sendout_v3.start();
		/*
		 * UMSThread umsThread=new UMSThread("UMS
		 * ����Ϣƽ̨","UMS����Ϣ�����߳�",Util.getCurrentTimeStr("yyyyMMddHHmmss"),"������",sendout,"");
		 * Res.getUMSThreads().add(umsThread);
		 */
		/*
		 * umsThread=new UMSThread("UMS
		 * �ʼ�����ƽ̨","UMS�ʼ������߳�",Util.getCurrentTimeStr("yyyyMMddHHmmss"),"������",sendout,"");
		 * Res.getUMSThreads().add(umsThread);
		 */
		appServer.start();
		// ������ر������
		monitorServer.start();
		monitorServer_V3.start();
		receiveCenter.start();
		ackCenter.start();
		messageTimer.start();
		// jmsReceiver.start();
	}

	/***************************************************************************
	 * This is the polite way to get a Listener to stop accepting connections
	 **************************************************************************/
	public void pleaseStop() {
		this.sendout_v3.stopThread();
		sendout_v3 = null;
		this.appServer.pleaseStop();
		ackCenter.pleaseStop();
		ackCenter = null;
		appServer = null;
		this.monitorServer.pleaseStop();
		monitorServer = null;
		this.monitorServer_V3.pleaseStop();
		monitorServer_V3 = null;
		this.receiveCenter.pleaseStop();
		messageTimer.pleaseStop();
		messageTimer = null;
		receiveCenter = null;
		for (int i = 0; i < umsServers.size(); i++) {
			UMSServer ums = (UMSServer) umsServers.get(i);
			ums.pleaseStop();
			ums = null;
		}
		server_v3 = null;
		SendMessage.stop();
		clearStaticUtils();
	}
	
	private void clearStaticUtils(){
		
	}

	// /**
	// * server ����ʱ�ͼ��ص��ڴ�
	// */
	// private void iniService(){
	// XStreamUtil.getInstance();
	// }

	private void initThreads() {
		sendout_v3 = new SendOut_V3();
		appServer = new AppServer();
		monitorServer = new MonitorServer();
		monitorServer_V3 = MonitorServer_V3.getInstance();
		receiveCenter = ReceiveCenter.getInstance();
		ackCenter = AckCenter.getInstance();
		messageTimer = new MessageTimer();
		threadGroup = new ThreadGroup(UMSServer_V3.class.getName());
		// jmsReceiver = new JMSReceiver();
		Properties res = new Properties(); // Empty properties
		try {
			InputStream in = new ServletTemp()
					.getInputStreamFromFile("/resources/parser.props");
			res.load(in); // Try to load properties
		} catch (Exception e) {
			System.err.println("Load parser properties failed.");
			System.exit(0);
		}
		parserMap = res;
	}

	public static void main(String[] args) {
		UMSServer_V3 server = new UMSServer_V3();
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("START")) {
				server.start();
			} else {
				server.pleaseStop();
			}
		} else {
			server.start();
		}
	}

	public static boolean isDbFlag() {
		return dbFlag;
	}

	public static void setDbFlag(boolean f) {
		dbFlag = f;
	}
	
	public static boolean isSocketFlag() {
		return socketFlag;
	}
}
