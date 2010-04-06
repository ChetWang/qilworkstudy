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
 * ���⣺Transactor.java
 * </p>
 * <p>
 * ������ ��ת�����Ľ�������ͨ��ʾ��
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2009-8-27
 * @version 1.0
 */
public class TransactorDemo {
	/**
	 * �����̶߳���
	 */
	private SocketIn socketIn;
	/**
	 * �����̶߳���
	 */
	private SocketOut socketOut;
	/**
	 * �����׽���
	 */
	private Socket socket;
	/**
	 * ��ʱʱ��
	 */
	private int timeOut;
	/**
	 * ���յ�����ת�����ĵ�������
	 */
	private DataInputStream in;
	/**
	 * ��������ת�����ĵ�������
	 */
	private DataOutputStream out;
	/**
	 * ����socket�������������߳�ͬʱȥ����
	 */
	private byte[] recnectLock = new byte[0];
	/**
	 * ���ӱ�־
	 */
	private boolean isLogin = false;

	/**
	 * @���� ���캯��
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
	 * @���� ��ʼ������
	 * @return
	 * 
	 * @Add by ZHM 2009-8-27
	 */
	private boolean initSocket() {
		String serverIP = "127.0.0.1";
		String serverPort = "10234";
		Res.log(Res.INFO, "������������ת������,IP:" + serverIP + ",port:" + serverPort
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
			Res.log(Res.INFO, "�ɹ���������ת�����ĳɹ���");

			isLogin = true;
			return true;
		} catch (Exception e) {
			Res.log(Res.INFO, "socket����ʧ�ܣ�");
			initSocket();
		}
		return false;
	}

	private void start() {
		if (!isLogin) {
			Res.log(Res.ERROR, "ϵͳû�е�¼��ǰ�û����޷�����.");
		} else {
			socketIn.start();
			socketOut.start();
		}
	}

	/**
	 * @���� ����׽�����Ч�ԣ���Ч������
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
	 * @���� ����ǰ�û�
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
			Res.log(Res.INFO, "׼����10�������");
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
	 * ������ �����߳���
	 * </p>
	 */
	private class SocketIn extends Thread {
		public SocketIn() {
			setName("ReceiveThread");
		}

		public void run() {
			Res.log(Res.INFO, "�����߳�������");
			boolean frameStart = false;

			byte[] firstHead = new byte[12];// ͷ12���ֽڵ�����
			int index = 0;// ��ǰ��ȡ���ֽ����
			int dataLength = 0;// ���ݶ��ֽڵĳ���
			byte[] oneFrame = null;// ��������֡������

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
								// ���ݳ���λ��������ݱ䳤
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
						if (index == dataLength + 13) {// ����index(�Ѿ�����1)�����һλ
							Res.log(Res.INFO, "���յ��ֽڣ�"
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
	 * ������ �����߳���
	 * </p>
	 */
	private class SocketOut extends Thread {
		public SocketOut() {
			setName("SendThread");
		}

		public void run() {
			Res.log(Res.INFO, "��Ϣ�����߳�������");
		}
	}

	public static void main(String[] args) throws Exception {
		Res.load();
		Res.log(Res.INFO, "����Demo��");
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
