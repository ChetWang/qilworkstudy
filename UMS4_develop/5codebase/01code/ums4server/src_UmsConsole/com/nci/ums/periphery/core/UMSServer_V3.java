/*
 * UMSServer_V3.java
 * 
 * Created on 2007-10-8, 16:03:09
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nci.ums.periphery.core;

import java.util.ArrayList;

import com.nci.ums.channel.MessageTimer;
import com.nci.ums.channel.channelmanager.SendOut_V3;
import com.nci.ums.channel.myproxy.SendMessage;
import com.nci.ums.jmx.server.ServerJMXHandler;
import com.nci.ums.periphery.application.AckCenter;
import com.nci.ums.periphery.application.MonitorServer_V3;
import com.nci.ums.periphery.application.ReceiveCenter;
import com.nci.ums.transmit.server.TransmitServer;
import com.nci.ums.util.DBConnect;
import com.nci.ums.util.DataBaseOp;
import com.nci.ums.util.Res;
import com.nci.ums.util.db.DBTableOptimizer;
import com.nci.ums.v3.service.impl.activemq.ActiveMQJMSService;

/**
 * ss This class make a main class to server for other application request
 */
public class UMSServer_V3 {

	// private SendOut_V3 sendout_v3; // media send out thread

	private ThreadGroup threadGroup; // Control threads of socketreaders

//	private Map parserMap; // request code -> parser class

	private ArrayList umsServers; // UMS服务端监听

	// private AppServer appServer;

	// private MonitorServer_V3 monitorServer_V3;
	// private ReceiveCenter receiveCenter;
	// private AckCenter ackCenter;
	// private MessageTimer messageTimer;
	private UMSServer umsServer;
	// private JMSReceiver jmsReceiver;

	private static UMSServer_V3 server_v3;

	// 数据库是否正常的标记位
	private boolean dbFlag = true;

	private boolean socketFlag = true;

	private UMSServer_V3() {
		umsServers = new ArrayList();
	}

	public void checkDB() {
		Res.log(Res.INFO, "UMS4.0正在检测运行环境...");
		try {
			DBConnect db = new DBConnect();
			db.close();
			Res.log(Res.INFO, "UMS4.0数据库环境正常!");
			dbFlag = true;
		} catch (Exception e) {
			Res.log(Res.ERROR, "UMS4.0所指定的数据库无法正常工作!请检查UMS服务器的数据库设置!");
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
		Res.log(Res.INFO, "UMS4.0服务器开始启动...");
		checkDB();
		if (dbFlag)
			initThreads();
		else
			return;
		DBConnect db = null;
		try {
			db = new DBConnect();
			// db.executeUpdate("update out_ready set statusFlag=0");
			db.executeUpdate("update UMS_SEND_READY set statusFlag=0");
//			InputStream is = new DynamicUMSStreamReader()
//					.getInputStreamFromFile("/resources/serverConfig.props");
//			Properties p = new Properties();
//			p.load(is);
//			is.close();
//			int port = Integer.parseInt((String) p.get("port"));
////			umsServer = new UMSServer(port, parserMap);
//			umsServer.start();
		} catch (Exception e) {
			e.printStackTrace();
			Res.log(Res.ERROR, "不能打开数据库或者serverConfig.props未初始化" + e);
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

		ReceiveCenter.getInstance().startModule();
		SendOut_V3.getInstance().startModule();

		MonitorServer_V3.getInstance().startModule();

		AckCenter.getInstance().startModule();
		MessageTimer.getInstance().startModule();
		ServerJMXHandler.getInstance().startModule();
		DBTableOptimizer.getInstance().startModule();
		TransmitServer.getInstance().startModule();
		ActiveMQJMSService.getInstance().startModule();
	}

	/***************************************************************************
	 * This is the polite way to get a Listener to stop accepting connections
	 **************************************************************************/
	public void pleaseStop() {
		SendOut_V3.getInstance().stopModule();
		AckCenter.getInstance().stopModule();
		MonitorServer_V3.getInstance().stopModule();
		ReceiveCenter.getInstance().stopModule();
		MessageTimer.getInstance().stopModule();

		if (umsServer != null) {
			umsServer.pleaseStop();
			umsServer = null;
		}
		server_v3 = null;
		SendMessage.stop();
		ServerJMXHandler.getInstance().stopModule();
		DBTableOptimizer.getInstance().stopModule();
		TransmitServer.getInstance().stopModule();
		ActiveMQJMSService.getInstance().stopModule();
	}

	private void initThreads() {

		threadGroup = new ThreadGroup(UMSServer_V3.class.getName());
		// jmsReceiver = new JMSReceiver();
//		Properties res = new Properties(); // Empty properties
//		try {
//			InputStream in = new DynamicUMSStreamReader()
//					.getInputStreamFromFile("/resources/parser.props");
//			res.load(in); // Try to load properties
//		} catch (Exception e) {
//			System.err.println("Load parser properties failed.");
//			System.exit(0);
//		}
//		parserMap = res;
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

	public boolean isDbFlag() {
		return dbFlag;
	}

	public void setDbFlag(boolean f) {
		dbFlag = f;
	}

	public boolean isSocketFlag() {
		return socketFlag;
	}
}
