package com.nci.ums.v3.service.client;

import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.nci.ums.v3.service.ServiceInfo;

public class UMSReceiver_Socket extends UMSReceiver {

	Socket client;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	DataOutputStream dos;
	DataInputStream dis;
	int timeout;
	boolean statusFlag = true;

	public UMSReceiver_Socket(String propsFile) throws IOException,
			 DuplicateServiceException {
		super(propsFile);
		iniSocket();
//		HeartBeat hb = new HeartBeat(new Socket(serverIP,serverPort));
//		clientToServerHeartTimer = new Timer(heartBeatInterval * 1000, hb);
	}

	private void iniSocket() throws UnknownHostException, IOException,
			 DuplicateServiceException {
		client = new Socket(serverIP, serverPort);
		client.setSoTimeout(timeout * 1000);
		oos = new ObjectOutputStream(client.getOutputStream());
		ois = new ObjectInputStream(client.getInputStream());
		if (!isStatusFlag()) {
			this.setStatusFlag(true);
			startListening();
		}
	}

	protected void connect() throws UnknownHostException, IOException,
			ClassNotFoundException {

		System.out.println("��ʼ����UMS����Ϣ���ն˿�...");

		ServiceInfo serviceInfo = new ServiceInfo();
		serviceInfo.setAppID(connectionTestSignal);
		serviceInfo.setAppPsw(connectionTestSignal);
		serviceInfo.setServiceID(connectionTestSignal);
		oos.writeObject(serviceInfo);
		String xml = (String) ois.readObject();
		if (xml != null && xml.equals(connectionTestReturn)) {
			System.out.println("��ʼ�����ӳɹ�.");
		}

	}

	public void onUMSMsg(ReceiveEvent evt) {

	}

	public void startListening() throws UMSDisconnectException, DuplicateServiceException {
		try {
			ServiceInfo serviceInfo = new ServiceInfo();
			serviceInfo.setAppID(appID);
			serviceInfo.setAppPsw(appPsw);
			serviceInfo.setServiceID(serviceID);
			oos.writeObject(serviceInfo);
			while (statusFlag) {
//				clientToServerHeartTimer.start();
				String xml = (String) ois.readObject();
				if (xml.equalsIgnoreCase(HEARTSIG)) {
					oos.writeObject(HEARTSIG);
				}
				if(xml.equalsIgnoreCase(DUPLICATE)){
					throw new DuplicateServiceException("����"+serviceInfo.toString()+"�ѱ�����Ӧ�õ�¼���޷����ӵ�UMS��������");
				}
				if (xml != null && xml.indexOf("Fail") < 0 && !xml.equals("")
						&& !xml.equalsIgnoreCase(HEARTSIG)) {
//					clientToServerHeartTimer.stop();
					source.notifyReceiveEvent(xml);
				} else {
					try {
						if (!xml.equalsIgnoreCase(HEARTSIG))
							Thread.sleep(2000);
					} catch (InterruptedException e) {
						System.out.println(e.getMessage());
					}
				}
			}
			client.close();
		} catch (IOException e2) {
			statusFlag = false;
			System.out.println("�޷����ӵ�������socket�˿ڡ�" + e2.getMessage());
			throw new UMSDisconnectException();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public boolean isStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(boolean statusFlag) {
		this.statusFlag = statusFlag;
	}

	class ReconnectListener implements java.awt.event.ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out
					.println("��UMS Socket������ʱ." + reconnectInterval + "���������");
			try {
				init();
				Thread.sleep(reconnectInterval * 1000);
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e2) {
				e2.printStackTrace();
			}
		}
	}

//	class HeartBeat extends Thread implements ActionListener {
//		Socket s;
//		ObjectOutputStream hoos;
//		ObjectInputStream hois;
//
//		public HeartBeat(Socket s) {
//			this.s = s;
//			try {
//				hoos = new ObjectOutputStream(s.getOutputStream());
//				hois = new ObjectInputStream(s.getInputStream());
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//
//		// �ͻ�������Server��������Ӧ
//		public void actionPerformed(ActionEvent e) {
//			int count = 0;
//			while (count < heartBeatCounts) {
//				try {
//					System.out.println("��" + (count + 1) + "���������...");
//					hoos.writeObject(HEARTSIG);
//					String ret = (String) hois.readObject();
//					if (!ret.equalsIgnoreCase(HEARTSIG)) {
//						setStatusFlag(false);
//						break;
//					}
//					System.out.println("��" + (count + 1) + "�������������");
//				} catch (IOException e1) {
//					System.out.println("��" + (count + 1) + "���������ʧ��");
//					if (count == heartBeatCounts - 1) {
//						setStatusFlag(false);
//						System.out.println("��Server���ӶϿ���׼������");
//						try {
//							iniSocket();
//						} catch (UnknownHostException e2) {
//							e2.printStackTrace();
//						} catch (IOException e2) {
//							e2.printStackTrace();
//						} catch (ClassNotFoundException e2) {
//							// TODO Auto-generated catch block
//							e2.printStackTrace();
//						} catch (DuplicateServiceException e2) {
//							// TODO Auto-generated catch block
//							e2.printStackTrace();
//						}
//					}
//					e1.printStackTrace();
//				} catch (ClassNotFoundException e1) {
//					e1.printStackTrace();
//				}
//				count++;
//			}
//
//		}

//		public void run() {
//			while(isStatusFlag()){
//				if()
//			}
//				
//		}
//	}
}
