package com.nci.ums.transmit.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.nci.ums.transmit.common.message.ControlCode;
import com.nci.ums.util.Res;

public class SubTransmitServer extends Thread {

	// // 应用实体
	// private AppInfo app;

	// 服务器运行标记，true为停止，false为运行
	private boolean stop = false;

	// 转发类型
	private int serverType;

	// 转发对应端口
	private int port = -1;

	private ServerSocket server;

	private Map connectedThreads;
	/**
	 * 对应的SubTransmitServer
	 */
	private SubTransmitServer coralateSubServer;
	/**
	 * 转发模块主服务
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
			Res.log(Res.ERROR, "创建转发任务SocketServer失败！port:" + port);
		}
		while (!stop) {
			try {
				if (port != -1) {

					// 数据处理交给线程池
					Socket socket = server.accept();
					ServerTransRunnable run = new ServerTransRunnable(socket,
							this);
					run.start();
				} else {
					Res.log(Res.FATAL, "接入类型不正确：" + serverType + "，无此类型");
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
	 * 停止服务
	 */
	public void stopServer() {
		if (port != -1) {
			Res.log(Res.INFO, "数据转发服务停止，应用：" + "，端口:" + port);
			try {
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		stop = true;
	}

	/**
	 * 获取子服务类型，是对应内网应用（注册进UMS），还是对应外部应用或终端
	 * 
	 * @see SubTransmitServer.APPLICATION_SERVER
	 * @see SubTransmitServer.TERMINAL_SERVER
	 * @return 子服务类型
	 */
	public int getServerType() {
		return serverType;
	}

	/**
	 * 设置子服务类型，是对应内网应用（注册进UMS），还是对应外部应用或终端
	 * 
	 * @see SubTransmitServer.APPLICATION_SERVER
	 * @see SubTransmitServer.TERMINAL_SERVER
	 * @param serverType
	 */
	public void setServerType(int serverType) {
		this.serverType = serverType;
	}

	/**
	 * 获取该服务对应的端口
	 * 
	 * @return
	 */
	public int getPort() {
		return port;
	}

	/**
	 * 设置该服务对应的端口
	 * 
	 * @param port
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * 获取已连接的tcp连接
	 * 
	 * @return
	 */
	public Map getConnectedThreads() {
		return connectedThreads;
	}

	/**
	 * 获取对应的子转发服务
	 * 
	 * @return
	 */
	public SubTransmitServer getCoralateSubServer() {
		return coralateSubServer;
	}

	/**
	 * 设置对应的子转发服务
	 * 
	 * @param coralateSubServer
	 */
	public void setCoralateSubServer(SubTransmitServer coralateSubServer) {
		this.coralateSubServer = coralateSubServer;
	}

	/**
	 * 获取转发主服务
	 * 
	 * @return
	 */
	public TransmitServer getTransServer() {
		return transServer;
	}
}
