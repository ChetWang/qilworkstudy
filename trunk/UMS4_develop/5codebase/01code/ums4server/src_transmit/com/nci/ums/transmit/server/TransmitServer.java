package com.nci.ums.transmit.server;

import java.util.Iterator;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import com.nci.ums.basic.UMSModule;
import com.nci.ums.transmit.common.UMSTransmitException;
import com.nci.ums.transmit.common.message.ControlCode;
import com.nci.ums.transmit.db.TransmitDataStoreManager;
import com.nci.ums.util.DynamicUMSStreamReader;
import com.nci.ums.util.Res;

/**
 * 数据转发服务入口程序
 * 
 * @author Qil.Wong
 * 
 */
public class TransmitServer implements UMSModule {

	private static TransmitServer server;

	// 心跳响应间隔
	private static int heartBeat_period = 50;// seconds
	// 心跳发送定时器
	private Timer heartBeatTimer;
	private TimerTask heartBeatTask;

	// 转发数据存储守护线程
	private TransmitDataStoreManager dataStoreManager;

	private int appTransPort = -1;

	private int terminalTransPort = -1;

	SubTransmitServer appSubServer;

	SubTransmitServer terminalSubServer;
	
	boolean stoped;

	private TransmitServer() {

	}

	/**
	 * 获取转发服务TransmitServer实例
	 * 
	 * @return 转发服务
	 */
	public static TransmitServer getInstance() {
		if (server == null) {
			server = new TransmitServer();
		}
		return server;
	}

	/**
	 * 启动数据转发服务
	 */
	public void startModule() {
		
		Res.log(Res.INFO, "开始数据转发服务...");
		Properties p = new DynamicUMSStreamReader()
				.getProperties("/resources/transmit.props");
		appTransPort = Integer.parseInt((String) p.getProperty("app_port"));
		terminalTransPort = Integer.parseInt((String) p
				.getProperty("terminal_port"));
		Res.log(Res.INFO, "数据转发服务应用对应端口:" + appTransPort);
		Res.log(Res.INFO, "数据转发服务终端对应端口:" + terminalTransPort);
		dataStoreManager = new TransmitDataStoreManager();
		dataStoreManager.startModule();
		appSubServer = new SubTransmitServer(this,
				ControlCode.DIRECTION_FROM_APPLICATION);
		// transApplicationMap.put(app.getAppID(), subServer);
		appSubServer.start();
		terminalSubServer = new SubTransmitServer(this,
				ControlCode.DIRECTION_FROM_TREMINAL);
		// transTerminalMap.put(app.getAppID(), subServer2);
		terminalSubServer.start();
		appSubServer.setCoralateSubServer(terminalSubServer);
		terminalSubServer.setCoralateSubServer(appSubServer);
		// 启动心跳定时器
		heartBeatTimer = new Timer();
		heartBeatTask = new TimerTask() {
			public void run() {
				Res.log(Res.DEBUG, "发送集体心跳");
				sendHeartBeat();
			}
		};
		heartBeatTimer.scheduleAtFixedRate(heartBeatTask,
				heartBeat_period * 1000, heartBeat_period * 1000);
		stoped = false;
	}

	/**
	 * 停止数据转发服务
	 */
	public void stopModule() {
		stoped = true;
		Res.log(Res.INFO, "开始停止数据转发服务...");
		terminalSubServer.stopServer();
		appSubServer.stopServer();
		appSubServer = null;
		terminalSubServer = null;
		appTransPort = -1;
		terminalTransPort = -1;
		if (dataStoreManager != null) {
			dataStoreManager.stopModule();
		}
		heartBeatTask.cancel();
		heartBeatTask = null;
		heartBeatTimer.cancel();
		heartBeatTimer = null;
		Res.log(Res.INFO, "数据转发服务已停止");
	}

	/**
	 * 给所有subTransServerMap下连接发送心跳
	 * 
	 * @param subTransServerMap
	 */
	public void sendHeartBeat() {
		Iterator it = appSubServer.getConnectedThreads().values().iterator();
		while (it.hasNext()) {
			ServerTransRunnable runnable = (ServerTransRunnable) it.next();
			try {
				runnable.heartBeat();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (UMSTransmitException e) {
				Res.logExceptionTrace(e);
			}
		}
		it = terminalSubServer.getConnectedThreads().values().iterator();
		while (it.hasNext()) {
			ServerTransRunnable runnable = (ServerTransRunnable) it.next();
			try {
				runnable.heartBeat();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (UMSTransmitException e) {
				Res.logExceptionTrace(e);
			}
		}
	}

	/**
	 * 判断转发服务是否停止
	 * 
	 * @return
	 */
	public boolean isStoped() {
		return stoped;
	}

	public TransmitDataStoreManager getDataStoreManager() {
		return dataStoreManager;
	}

	public static void main(String xxx[]) {
		new TransmitServer().startModule();
	}

	public int getAppTransPort() {
		return appTransPort;
	}

	public int getTerminalTransPort() {
		return terminalTransPort;
	}
}
