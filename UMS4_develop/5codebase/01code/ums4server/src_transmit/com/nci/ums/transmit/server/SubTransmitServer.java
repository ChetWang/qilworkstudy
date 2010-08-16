package com.nci.ums.transmit.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.nci.ums.transmit.common.message.ControlCode;
import com.nci.ums.util.Res;

public class SubTransmitServer extends Thread {

	// // Ӧ��ʵ��
	// private AppInfo app;

	// ���������б�ǣ�trueΪֹͣ��falseΪ����
	private boolean stop = false;

	// ת������
	private int serverType;

	// ת����Ӧ�˿�
	private int port = -1;

	private ServerSocket server;

	private Map connectedThreads;
	/**
	 * ��Ӧ��SubTransmitServer
	 */
	private SubTransmitServer coralateSubServer;
	/**
	 * ת��ģ��������
	 */
	private TransmitServer transServer;

	public SubTransmitServer(TransmitServer transServer, int type) {

		serverType = type;
		this.transServer = transServer;
		connectedThreads = new ConcurrentHashMap();
		if (serverType == ControlCode.DIRECTION_FROM_APPLICATION) {
			port = transServer.getAppTransPort();

		} else if (serverType == ControlCode.DIRECTION_FROM_TREMINAL) {
			port = transServer.getTerminalTransPort();

		}
	}

	public void run() {
		try {
			if (port != -1) {
				server = new ServerSocket(port);
			}
		} catch (IOException e) {
			Res.log(Res.ERROR, e.getMessage());
			Res.log(Res.ERROR, "����ת������SocketServerʧ�ܣ�port:" + port);
		}
		while (!stop) {
			try {
				if (port != -1) {

					// ���ݴ������̳߳�
					Socket socket = server.accept();
					ServerTransRunnable run = new ServerTransRunnable(socket,
							this);
					run.start();
				} else {
					Res.log(Res.FATAL, "�������Ͳ���ȷ��" + serverType + "���޴�����");
					stopServer();
				}
			} catch (IOException e) {
				if (!transServer.isStoped()) {
					Res.logExceptionTrace(e);
				}
			}
		}
	}

	/**
	 * ֹͣ����
	 */
	public void stopServer() {
		if (port != -1) {
			Res.log(Res.INFO, "����ת������ֹͣ��Ӧ�ã�" + "���˿�:" + port);
			try {
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		stop = true;
	}

	/**
	 * ��ȡ�ӷ������ͣ��Ƕ�Ӧ����Ӧ�ã�ע���UMS�������Ƕ�Ӧ�ⲿӦ�û��ն�
	 * 
	 * @see SubTransmitServer.APPLICATION_SERVER
	 * @see SubTransmitServer.TERMINAL_SERVER
	 * @return �ӷ�������
	 */
	public int getServerType() {
		return serverType;
	}

	/**
	 * �����ӷ������ͣ��Ƕ�Ӧ����Ӧ�ã�ע���UMS�������Ƕ�Ӧ�ⲿӦ�û��ն�
	 * 
	 * @see SubTransmitServer.APPLICATION_SERVER
	 * @see SubTransmitServer.TERMINAL_SERVER
	 * @param serverType
	 */
	public void setServerType(int serverType) {
		this.serverType = serverType;
	}

	/**
	 * ��ȡ�÷����Ӧ�Ķ˿�
	 * 
	 * @return
	 */
	public int getPort() {
		return port;
	}

	/**
	 * ���ø÷����Ӧ�Ķ˿�
	 * 
	 * @param port
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * ��ȡ�����ӵ�tcp����
	 * 
	 * @return
	 */
	public Map getConnectedThreads() {
		return connectedThreads;
	}

	/**
	 * ��ȡ��Ӧ����ת������
	 * 
	 * @return
	 */
	public SubTransmitServer getCoralateSubServer() {
		return coralateSubServer;
	}

	/**
	 * ���ö�Ӧ����ת������
	 * 
	 * @param coralateSubServer
	 */
	public void setCoralateSubServer(SubTransmitServer coralateSubServer) {
		this.coralateSubServer = coralateSubServer;
	}

	/**
	 * ��ȡת��������
	 * 
	 * @return
	 */
	public TransmitServer getTransServer() {
		return transServer;
	}
}
