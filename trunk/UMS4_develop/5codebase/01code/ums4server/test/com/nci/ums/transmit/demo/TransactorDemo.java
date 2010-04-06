package com.nci.ums.transmit.demo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.nci.ums.transmit.util.StringCoding;
import com.nci.ums.util.Res;

/**
 * <p>
 * 标题：Transactor.java
 * </p>
 * <p>
 * 描述： 和转发中心进行数据通信示例
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2009-8-27
 * @version 1.0
 */
public class TransactorDemo {
	/**
	 * 接收线程对象
	 */
	private SocketIn socketIn;
	/**
	 * 发送线程对象
	 */
	private SocketOut socketOut;
	/**
	 * 连接套接字
	 */
	private Socket socket;
	/**
	 * 超时时间
	 */
	private int timeOut;
	/**
	 * 接收到数据转发中心的数据流
	 */
	private DataInputStream in;
	/**
	 * 发向数据转发中心的数据流
	 */
	private DataOutputStream out;
	/**
	 * 重连socket的锁，避免多个线程同时去重连
	 */
	private byte[] recnectLock = new byte[0];
	/**
	 * 连接标志
	 */
	private boolean isLogin = false;

	/**
	 * @功能 构造函数
	 * 
	 * @Add by ZHM 2009-8-27
	 */
	public TransactorDemo() {
		socketIn = new SocketIn();
		socketOut = new SocketOut();
		timeOut = 30;
		initSocket();
	}

	/**
	 * @功能 初始化函数
	 * @return
	 * 
	 * @Add by ZHM 2009-8-27
	 */
	private boolean initSocket() {
		String serverIP = "127.0.0.1";
		String serverPort = "10234";
		Res.log(Res.INFO, "正在连接数据转发中心,IP:" + serverIP + ",port:" + serverPort
				+ "...");
		try {
			if (socket != null) {
				socket.close();
				socket = null;
			}
			socket = new Socket(serverIP, Integer.parseInt(serverPort));
			socket.setSoTimeout(timeOut * 1000);

			if (in != null) {
				in.close();
				in = null;
			}
			in = new DataInputStream(socket.getInputStream());

			if (out != null) {
				out.close();
				out = null;
			}
			out = new DataOutputStream(socket.getOutputStream());
			Res.log(Res.INFO, "成功连接数据转发中心成功！");

			isLogin = true;
			return true;
		} catch (Exception e) {
			Res.log(Res.INFO, "socket连接失败！");
			initSocket();
		}
		return false;
	}

	private void start() {
		if (!isLogin) {
			Res.log(Res.ERROR, "系统没有登录到前置机，无法启动.");
		} else {
			socketIn.start();
			socketOut.start();
		}
	}

	/**
	 * @功能 检查套接字有效性，无效则重连
	 * 
	 * @Add by ZHM 2009-8-27
	 */
	private void checkSocket() {
		if (socket == null || !socket.isConnected()) {
			synchronized (recnectLock) {
				reconnectToFrontServer();
			}
		}
	}

	/**
	 * @功能 重连前置机
	 * 
	 * @Add by ZHM 2009-8-27
	 */
	private void reconnectToFrontServer() {
		isLogin = false;

		try {
			if (socket != null)
				socket.close();
		} catch (IOException e) {
			Res.logExceptionTrace(e);
		}
		socket = null;
		while (initSocket() == false) {
			Res.log(Res.INFO, "准备在10秒后重连");
			try {
				Thread.sleep(10 * 1000);
			} catch (InterruptedException e) {
			}
		}
		// isLogin = login();
		isLogin = true;
	}

	/**
	 * <p>
	 * 描述： 接收线程类
	 * </p>
	 */
	private class SocketIn extends Thread {
		public SocketIn() {
			setName("ReceiveThread");
		}

		public void run() {
			Res.log(Res.INFO, "接收线程启动！");
			boolean frameStart = false;

			byte[] firstHead = new byte[12];// 头12个字节的数据
			int index = 0;// 当前读取的字节序号
			int dataLength = 0;// 数据段字节的长度
			byte[] oneFrame = null;// 整个数据帧的数据

			while (true) {
				try {
					checkSocket();
					if (!frameStart) {
						byte readByte = in.readByte();

						if (readByte == 0x68) {
							frameStart = true;
							firstHead[0] = 0x68;
							index++;
						}
					} else {
						if (index <= 11) {
							firstHead[index] = in.readByte();
						} else {
							if (index == 12) {
								// 数据长度位计算出数据变长
								int height = (firstHead[11] + 256) % 256;
								int low = (firstHead[10] + 256) % 256;
								dataLength = height * 256 + low;
								oneFrame = new byte[dataLength + 13];
								for (int i = 0; i < 11; i++) {
									oneFrame[i] = firstHead[i];
								}
							}
							oneFrame[index] = in.readByte();
						}
						index++;
						if (index == dataLength + 13) {// 这里index(已经最后加1)是最后一位
							Res.log(Res.INFO, "接收到字节："
									+ StringCoding.byte2hex(oneFrame));
						}
					}
				} catch (Exception e) {

				}
			}

		}
	}

	/**
	 * <p>
	 * 描述： 发送线程类
	 * </p>
	 */
	private class SocketOut extends Thread {
		public SocketOut() {
			setName("SendThread");
		}

		public void run() {
			Res.log(Res.INFO, "消息发送线程启动！");
		}
	}

	public static void main(String[] args) throws Exception {
		Res.load();
		Res.log(Res.INFO, "运行Demo！");
		// TransactorDemo server = new TransactorDemo();
		// server.start();
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		JFrame frame = new JFrame("Transactor Demo");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new TransmitSocketPanel());
		frame.pack();
//		frame.setLocationByPlatform(true);
		frame.setVisible(true);
	}
}
