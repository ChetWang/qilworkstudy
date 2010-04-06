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

	private ArrayList umsServers; // UMS服务端监听

	private AppServer appServer;
	private MonitorServer monitorServer;
	private MonitorServer_V3 monitorServer_V3;
	private ReceiveCenter receiveCenter;
	private AckCenter ackCenter;
	private MessageTimer messageTimer;

	// private JMSReceiver jmsReceiver;

	private static UMSServer_V3 server_v3;

	// 数据库是否正常的标记位
	private static boolean dbFlag = true;
	
	private static boolean socketFlag = true;

	private UMSServer_V3() {
		umsServers = new ArrayList();
	}

	public void checkDB() {
		Res.log(Res.INFO, "UMS3.0正在检测运行环境...");
		try {
			DBConnect db = new DBConnect();
			db.close();
			Res.log(Res.INFO, "UMS3.0数据库环境正常!");
			dbFlag = true;
		} catch (Exception e) {
			Res.log(Res.ERROR, "UMS3.0所指定的数据库无法正常工作!请检查UMS服务器的数据库设置!");
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
		Res.log(Res.INFO, "UMS3.0服务器开始启动...");
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
					Res.log(Res.WARN, "UMS Server端口"+port+"冲突，系统无法正常启动，可能UMS服务已经正在运行!");
					socketFlag = false;
					return;
				}
				/*
				 * UMSThread umsThread=new UMSThread("UMS
				 * 外部接口平台","UMS外部接口线程",Util.getCurrentTimeStr("yyyyMMddHHmmss"),"已启动",umsServer,"在端口"+port+"监听");
				 * Res.getUMSThreads().add(umsThread);
				 */
			}
			rs.close();
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
			Res.log(Res.ERROR, "不能打开数据库或者System表未初始化" + e);
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
		 * 短消息平台","UMS短消息管理线程",Util.getCurrentTimeStr("yyyyMMddHHmmss"),"已启动",sendout,"");
		 * Res.getUMSThreads().add(umsThread);
		 */
		/*
		 * umsThread=new UMSThread("UMS
		 * 邮件服务平台","UMS邮件接收线程",Util.getCurrentTimeStr("yyyyMMddHHmmss"),"已启动",sendout,"");
		 * Res.getUMSThreads().add(umsThread);
		 */
		appServer.start();
		// 启动监控报告服务
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
	// * server 启动时就加载到内存
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
