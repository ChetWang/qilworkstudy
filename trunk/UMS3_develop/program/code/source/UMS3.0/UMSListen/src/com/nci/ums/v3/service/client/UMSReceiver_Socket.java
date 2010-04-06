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

		System.out.println("开始连接UMS的消息接收端口...");

		ServiceInfo serviceInfo = new ServiceInfo();
		serviceInfo.setAppID(connectionTestSignal);
		serviceInfo.setAppPsw(connectionTestSignal);
		serviceInfo.setServiceID(connectionTestSignal);
		oos.writeObject(serviceInfo);
		String xml = (String) ois.readObject();
		if (xml != null && xml.equals(connectionTestReturn)) {
			System.out.println("初始化链接成功.");
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
					throw new DuplicateServiceException("服务"+serviceInfo.toString()+"已被其它应用登录，无法连接到UMS服务器。");
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
			System.out.println("无法连接到服务器socket端口。" + e2.getMessage());
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
					.println("与UMS Socket交互超时." + reconnectInterval + "秒后重连。");
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
//		// 客户端连接Server的心跳响应
//		public void actionPerformed(ActionEvent e) {
//			int count = 0;
//			while (count < heartBeatCounts) {
//				try {
//					System.out.println("第" + (count + 1) + "次心跳检测...");
//					hoos.writeObject(HEARTSIG);
//					String ret = (String) hois.readObject();
//					if (!ret.equalsIgnoreCase(HEARTSIG)) {
//						setStatusFlag(false);
//						break;
//					}
//					System.out.println("第" + (count + 1) + "次心跳检测正常");
//				} catch (IOException e1) {
//					System.out.println("第" + (count + 1) + "次心跳检测失败");
//					if (count == heartBeatCounts - 1) {
//						setStatusFlag(false);
//						System.out.println("与Server连接断开，准备重连");
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
