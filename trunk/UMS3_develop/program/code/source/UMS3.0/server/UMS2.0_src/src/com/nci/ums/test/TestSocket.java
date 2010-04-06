package com.nci.ums.test;

import java.net.*;
import java.io.*;

public class TestSocket {
	/**
	 * UMS������IP
	 */
	// public String remoteIP = "10.44.65.91";
	public String remoteIP = "127.0.0.1";

	/**
	 * UMS�������˿�,Ĭ����10000
	 */
	public int remotePort = 10000;
	/**
	 * Ӧ��ID
	 */
	public String appID = "1001";
	/**
	 * Ӧ������
	 */
	public String appPsw = "1001";
	/**
	 * ���յ��ֻ���
	 */
	public String phoneNumber = "13819155409";

	public Socket client;

	public DataOutputStream out;

	public DataInputStream in;

	public boolean passFlag = false;

	public final int MAX_PACKET_SIZE = 1024;

	public byte[] buffer = new byte[MAX_PACKET_SIZE];

	public boolean initSocket() // �ͻ������ӳ�ʼ��
	{
		try {

			InetAddress addr = InetAddress.getByName(remoteIP);

			client = new Socket(addr, remotePort);
			client.setSoTimeout(5 * 60 * 1000);
			in = new DataInputStream(client.getInputStream());
			out = new DataOutputStream(client.getOutputStream());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// ���ӷ������ж�Ӧ��ID�������Ƿ�ƥ��
	/**
	 * @param appID
	 * @param password
	 * @return
	 */
	public boolean checkPassword(String appID, String password) {
		boolean result = false;
		StringBuffer sb = new StringBuffer("38   100101").append(
				Util.getFixedString(appID, 12)).append(
				Util.getFixedString(password, 20));
		try {
			out.write(sb.toString().getBytes());
//			boolean flag = true;
			byte[] rets = new byte[1024];
			in.read(rets);
			byte retCode[] = new byte[4];
			for (int i = 5; i <= 8; i++) {
				retCode[i - 5] = rets[i];
			}
			String retCodeStr = new String(retCode);
			if (retCodeStr.equalsIgnoreCase("0000")) {
				result = true;
				System.out.println("password invalid passed");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int sendMessage(String appID, String mobilePhone, String content,
			String reply, String ack, String id, String rep, String priority,
			String messageType, String subApp) {
		int result = 1;

		StringBuffer sb = new StringBuffer("506  100201");
		sb.append(Util.getFixedString(appID, 12)).append(
				Util.getFixedString(id, 35)).append(
				Util.getFixedString(messageType, 3)).append(
				Util.getFixedString(mobilePhone, 255)).append(
				Util.getFixedString(content, 160)).append(ack).append(
				Util.getFixedString(reply, 30)).append(
				Util.getFixedString("", 2)).append(Util.getFixedString("", 2));
		System.out.println(sb.toString().length());
		try {
			out.write(sb.toString().getBytes());
			byte[] rets = new byte[1024];
			in.read(rets);
			byte retCode[] = new byte[4];
			for (int i = 5; i <= 8; i++) {
				retCode[i - 5] = rets[i];
			}
			String retCodeStr = new String(retCode);
			if (retCodeStr.equalsIgnoreCase("0000")) {// ����"0000"��ʾ���ͳɹ����������"0000"�����Ϣ����ʧ��
				result = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @param argv
	 */
	public static void main(String[] argv) {
		TestSocket test = new TestSocket();
		int n = 1;
		try {
			if (test.initSocket()) {
				boolean f = test.checkPassword(test.appID, test.appPsw); // Ӧ�ú�Ϊ1001,����Ϊ1001��Ӧ�õ�¼������
				while (true) {
					System.out.println(test.client.isClosed());
					if (test.client.isClosed()) {
						break;
					}
					Thread.sleep(200);
					String msg = "test";
					// ������Ϣ
					long time = System.currentTimeMillis();
					for (int i = 0; i < n; i++) {
						System.out.println(test.sendMessage(test.appID,
								test.phoneNumber, msg, "", "0", "", "", "",
								"0", ""));
//						Thread.sleep(20);
					}
					System.out.println("ȫ�����ͺ�ʱ��"+(System.currentTimeMillis()-time)/1000+"��");
					test.in.close();
					test.out.close();
					System.exit(0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

class Util {
	public static synchronized String getFixedString(String msg, int len) {
		if (msg == null) {
			msg = "";
		}

		StringBuffer sb = new StringBuffer(msg);
		for (int i = msg.getBytes().length + 1; i <= len; i++) {
			sb.append(" ");
		}
		return sb.toString();
	}

	public static byte[] getByte(String msg, int data_code) {
		try {
			if (data_code == 15) {
				return msg.getBytes("GBK");
			} else if (data_code == 0) {
				return msg.getBytes("8859_1");
			} else if (data_code == 21 || data_code == 4) {
				return Util.getByte(msg);
			} else {
				return msg.getBytes("UnicodeBig");
			}
		} catch (Exception e) {
			return msg.getBytes();
		}
	}

	public static byte[] getByte(String msg) {

		// ȡ��Ϣ����
		int length = msg.getBytes().length;
		byte[] temp = new byte[length];
		int i = 0;
		int j = 0;

		// ���ֽ�ѹ��
		for (i = 0; i < length; i++) {
			// ȡһ���ֽ�
			int b1 = Integer.parseInt(msg.substring(i, i + 1), 16);
			// ��λ
			b1 <<= 4;
			i++;
			if (i < length) {
				// ȡ��һ���ֽ�
				int b2 = Integer.parseInt(msg.substring(i, i + 1), 16);
				b1 = b1 + b2;
			}
			temp[j] = (byte) b1;
			j++;
		}
		byte[] result = new byte[j];
		for (int f = 0; f < j; f++) {
			result[f] = temp[f];
		}
		// result[j]=0x0;
		return result;
	}
}