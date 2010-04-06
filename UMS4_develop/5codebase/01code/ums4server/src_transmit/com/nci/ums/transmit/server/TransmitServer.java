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
 * ����ת��������ڳ���
 * 
 * @author Qil.Wong
 * 
 */
public class TransmitServer implements UMSModule {

	private static TransmitServer server;

	// ������Ӧ���
	private static int heartBeat_period = 50;// seconds
	// �������Ͷ�ʱ��
	private Timer heartBeatTimer;
	private TimerTask heartBeatTask;

	// ת�����ݴ洢�ػ��߳�
	private TransmitDataStoreManager dataStoreManager;

	private int appTransPort = -1;

	private int terminalTransPort = -1;

	SubTransmitServer appSubServer;

	SubTransmitServer terminalSubServer;
	
	boolean stoped;

	private TransmitServer() {

	}

	/**
	 * ��ȡת������TransmitServerʵ��
	 * 
	 * @return ת������
	 */
	public static TransmitServer getInstance() {
		if (server == null) {
			server = new TransmitServer();
		}
		return server;
	}

	/**
	 * ��������ת������
	 */
	public void startModule() {
		
		Res.log(Res.INFO, "��ʼ����ת������...");
		Properties p = new DynamicUMSStreamReader()
				.getProperties("/resources/transmit.props");
		appTransPort = Integer.parseInt((String) p.getProperty("app_port"));
		terminalTransPort = Integer.parseInt((String) p
				.getProperty("terminal_port"));
		Res.log(Res.INFO, "����ת������Ӧ�ö�Ӧ�˿�:" + appTransPort);
		Res.log(Res.INFO, "����ת�������ն˶�Ӧ�˿�:" + terminalTransPort);
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
		// ����������ʱ��
		heartBeatTimer = new Timer();
		heartBeatTask = new TimerTask() {
			public void run() {
				Res.log(Res.DEBUG, "���ͼ�������");
				sendHeartBeat();
			}
		};
		heartBeatTimer.scheduleAtFixedRate(heartBeatTask,
				heartBeat_period * 1000, heartBeat_period * 1000);
		stoped = false;
	}

	/**
	 * ֹͣ����ת������
	 */
	public void stopModule() {
		stoped = true;
		Res.log(Res.INFO, "��ʼֹͣ����ת������...");
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
		Res.log(Res.INFO, "����ת��������ֹͣ");
	}

	/**
	 * ������subTransServerMap�����ӷ�������
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
	 * �ж�ת�������Ƿ�ֹͣ
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
