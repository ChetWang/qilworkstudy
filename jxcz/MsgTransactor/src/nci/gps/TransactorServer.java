package nci.gps;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Iterator;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import nci.gps.message.StaticUnPackageMessage;
import nci.gps.messages.ServerHeartBeatTool;
import nci.gps.messages.mq.GPSMsgPool;
import nci.gps.messages.mq.MessageBean;
import nci.gps.messages.mq.ServerMsgConsumerTool;
import nci.gps.messages.mq.ServerMsgProducerTool;
import nci.gps.util.CharCoding;
import nci.gps.util.MsgLogger;
import nci.gps.util.Utilities;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TransactorServer {

	/**
	 * ��Ϣ�����
	 */
	private GPSMsgPool msgPool;
	/**
	 * JMS����Ϣ������
	 */
	private ServerMsgProducerTool producerTool;
	/**
	 * JMS����Ϣ������
	 */
	private ServerMsgConsumerTool consumerTool;
	/**
	 * �������ͳ���
	 */
	private ServerHeartBeatTool heartBeatTool;
	/**
	 * ����ʡ��˾ǰ�û���������
	 */
	private DataOutputStream out;
	/**
	 * ��ʡ��˾ǰ�û���������������
	 */
	private DataInputStream in;
	/**
	 * ���ӵ�ʡ��˾ǰ�û����׽���
	 */
	private Socket socket;

	/**
	 * ����socket�������������߳�ͬʱȥ����
	 */
	private byte[] recnectLock = new byte[0];

	private String loginName = "";

	private String loginPsw = "";

	private boolean isLogin = false;

	private int timeOut = 0;

	private FunctionCodeCommunicator functionCodeCommunicator;

	private SocketIn socketIn;

	private SocketOut socketOut;

	public TransactorServer(GPSMsgPool msgPool) {
		Runtime.getRuntime().addShutdownHook(new ShutDown());
		this.msgPool = msgPool;
		producerTool = new ServerMsgProducerTool(msgPool);
		consumerTool = new ServerMsgConsumerTool(msgPool);
		heartBeatTool = new ServerHeartBeatTool(this);
		socketIn = new SocketIn();
		socketOut = new SocketOut();
		iniSocket();
		functionCodeCommunicator = new FunctionCodeCommunicator(this);

	}

	/**
	 * ��ʼ���׽���
	 * 
	 * @return
	 */
	private boolean iniSocket() {
		Document serverDoc = Utilities
				.getConfigXMLDocument("frontServerConf.xml");
		// ��ȡʡ��˾ǰ�û�������
		Element serverConf = (Element) serverDoc.getElementsByTagName("server")
				.item(0);
		Element clientConf = (Element) serverDoc.getElementsByTagName("client")
				.item(0);
		String frontServerPort = serverConf.getAttribute("frontServerPort");
		String frontServerIP = serverConf.getAttribute("frontServerIP");
		loginName = clientConf.getAttribute("loginName");
		loginPsw = clientConf.getAttribute("loginPassword");
		timeOut = Integer.parseInt(clientConf.getAttribute("timeOut"));
		try {
			MsgLogger.log(MsgLogger.INFO, "��������ǰ�û���" + "IP:" + frontServerIP
					+ ",port:" + frontServerPort + "...");
			if (socket != null) {
				try {
					socket.close();
				} catch (Exception e) {
				}
				socket = null;
			}
			socket = new Socket(frontServerIP, Integer
					.parseInt(frontServerPort));
			if (timeOut > 0)
				socket.setSoTimeout(timeOut * 1000);
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
				in = null;
			}
			in = new DataInputStream(socket.getInputStream());
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
				}
				out = null;
			}
			out = new DataOutputStream(socket.getOutputStream());
			MsgLogger.log(MsgLogger.INFO, "�ɹ�������ǰ�û���" + "IP:" + frontServerIP
					+ ",port:" + frontServerPort);
			isLogin = true;
			return true;
		} catch (Exception e) {
			MsgLogger.log(MsgLogger.INFO, "socket���ӳ���" + "IP:" + frontServerIP
					+ ",port:" + frontServerPort + "׼����10�������");
			try {
				Thread.sleep(10 * 1000);
			} catch (InterruptedException e1) {
			}
			iniSocket();
		}
		return false;
	}

	/**
	 * ����������
	 */
	public void start() {
		if (!isLogin) {
			MsgLogger.log(MsgLogger.ERROR, "ϵͳû�е�¼��ǰ�û����޷�����.");
		} else {
			producerTool.start();
			consumerTool.start();
			heartBeatTool.start();
			socketIn.start();
			socketOut.start();
		}
	}

	/**
	 * �ر�Transactor������
	 */
	public void stopTransactor() {
		socketIn.statusFlag = false;
		socketOut.statusFlag = false;
		consumerTool.stopConsumer();
		producerTool.stopProducer();
		heartBeatTool.stopHeartBeat();
	}

	/**
	 * ��ȡ�׽���
	 */
	public Socket getSocket() {
		if (socket.isConnected())
			return socket;
		else {
			reconnectToFrontServer();
			return socket;
		}
	}

	/**
	 * ����׽��ֵ���Ч�ԣ���Ч������
	 */
	private void checkSocket() {
		if (socket == null || !socket.isConnected()) {
			synchronized (recnectLock) {
				reconnectToFrontServer();
			}
		}
	}

	/**
	 * ����ǰ�û�
	 */
	private void reconnectToFrontServer() {
		isLogin = false;

		try {
			if (socket != null)
				socket.close();
		} catch (IOException e1) {
			MsgLogger.logExceptionTrace("socket close�����쳣��", e1);
		}
		socket = null;
		while (iniSocket() == false) {
			MsgLogger.log(MsgLogger.INFO, "׼����10�������");
			try {
				Thread.sleep(10 * 1000);
			} catch (InterruptedException e) {
			}
		}
		try {
			consumerTool.getConsumer().setMessageListener(null);
			consumerTool.getConsumer().setMessageListener(consumerTool);
		} catch (JMSException e) {
			MsgLogger
					.logExceptionTrace("consumerTool����MessageListener�����쳣��", e);
		}
		// isLogin = login();
		isLogin = true;
	}

	/**
	 * ����ǰ�û���Ϣ���߳�
	 * 
	 * @author Qil.Wong
	 * 
	 */
	private class SocketIn extends Thread {
		boolean statusFlag = true;
		String logicalAddr;
		String subject;
		Session session;
		BytesMessage bMsg;

		public SocketIn() {
			setName("ReceiveFromFrontServer");
		}

		public void run() {
			MsgLogger.log(MsgLogger.INFO, "����ʡ��˾ǰ�û���Ϣ���߳�����.");
			int index = 0;// ��ǰ��ȡ���ֽ����
			int dataLength = 0;// ���ݶ��ֽڵĳ���

			// ͷ11���ֽڵ�����
			byte[] firstHead = new byte[11];

			// ��������֡������
			byte[] oneFrame = null;

			String subject = null;

			String logicalAddr = null;
			boolean frameStart = false;
			while (true) {
				if (statusFlag) {
					// ����ǰ�û���Ϣ,�����뵽msgPool��ת����ϣ��
					try {
						checkSocket();
						if (!frameStart) {
							byte readByte = in.readByte();

							if (readByte == 0x68) {
								frameStart = true;
								firstHead[0] = 0x68;
								index++;
							}
						} else {// ��ʼ

							if (index <= 10) {
								firstHead[index] = in.readByte();

							} else {
								if (index == 11) {
									// ���ݳ���λ��������ݱ䳤
									int height = (firstHead[10] + 256) % 256;
									int low = (firstHead[9] + 256) % 256;
									dataLength = height * 256 + low;
									// dataLength = firstHead[9] * 256 +
									// firstHead[10];
									oneFrame = new byte[dataLength + 13];
									for (int i = 0; i < 11; i++) {
										oneFrame[i] = firstHead[i];
									}
								}
								// һ֡�����ݰ�
								oneFrame[index] = in.readByte();
							}
							index++;
							if (index == dataLength + 13) {// ����index(�Ѿ�����1)�����һλ
								MsgLogger.log(MsgLogger.INFO, "���յ��ֽڣ�"+CharCoding.byte2hex(oneFrame));
								frameStart = false;
								index = 0;
								byte functionCode = StaticUnPackageMessage
										.getFunctionCode(oneFrame);
								// �ж��Ƿ���ʡ��˾ǰ�û����͹����ķ��ն˷��͵���Ҫ��ת����Ϣ
								if (isMessageToTransact(functionCode)) {

									// ������֡ת����jms��Ϣ���뵽��ת��ϣMap
									logicalAddr = getLogicalAddr(oneFrame);

									subject = (String) msgPool
											.getLogicalAddrsSubjectMap().get(
													logicalAddr);
									if (subject == null) {
										MsgLogger.log(MsgLogger.WARN,
												"�޷������߼���ַ:" + logicalAddr
														+ " �ҵ���Ӧ������!");
									} else {
										Session jmsSession = (Session) msgPool
												.getSessionMap().get(subject);
										if (jmsSession == null) {
											MsgLogger
													.log(
															MsgLogger.WARN,
															"�޷���������:"
																	+ subject
																	+ " �ҵ���Ӧ��JMS Session!");
										} else {
											BytesMessage bMsg = jmsSession
													.createBytesMessage();
											
											bMsg.writeBytes(oneFrame);
											msgPool
													.addMessageIntoFromPool(new MessageBean(
															subject, bMsg));
										}
									}
								} else {
									// ������������͵ģ������ִ��������Ӧ����¼����ע���ȵ�

									MsgLogger.log(MsgLogger.INFO,
											"�յ���ת����Ϣ.�������еĹ����룺" + functionCode);
									functionCodeCommunicator.parse(
											functionCode, oneFrame);
								}

							}
						}
					} catch (IOException e) {
						if (e.getMessage() != null) {
							if (e.getMessage().equalsIgnoreCase(
									"Read timed out")) {
								MsgLogger.log(MsgLogger.INFO, e.getMessage()
										+ ".��ʱ��socket�����¿�ʼ��ȡ����ǰ���ݶ���.");
							} else if (e.getMessage().equalsIgnoreCase(
									"Connection reset")) {
								MsgLogger.log(MsgLogger.WARN, e.getMessage()
										+ ".socket���ӱ�����. ��ʼ���³�ʼ��socket����.");
								reconnectToFrontServer();
							} else {
								MsgLogger.logExceptionTrace("socket��ȡ���ݳ����쳣��",
										e);
							}
						}else{
							MsgLogger.logExceptionTrace(e.getClass().getName(),
									e);
							reconnectToFrontServer();
						}						
						index = 0;
					} catch (JMSException e) {
						MsgLogger.logExceptionTrace("JMS��Ϣ���������г����쳣��", e);
						index = 0;
					}
				} else {
					try {
						sleep(1000);
					} catch (InterruptedException e) {
					}
				}
			}
		}
	}

	/**
	 * ������Ϣ��ǰ�û����߳�
	 * 
	 * @author Qil.Wong
	 * 
	 */
	private class SocketOut extends Thread {
		private boolean statusFlag = true;
		private Iterator it;
		private String serial;
		private byte[] msgBytes;

		public SocketOut() {
			setName("SendToFrontServer");
		}

		public void run() {
			MsgLogger.log(MsgLogger.INFO, "��Ϣת����Ӧ�õ��߳�����.");
			while (true) {
				if (statusFlag) {
					// ��ǰ�û�������Ϣ
					if (msgPool.hasToFrontMessage()) {
						it = msgPool.getToFrontMsgMap().keySet().iterator();
						serial = (String) it.next();
						msgBytes = msgPool.getToMessageBytes(serial);
						// ���͵�ǰ�û�
						sendToFrontServer(serial, msgBytes);
					} else {
						try {
							sleep(1000);
						} catch (InterruptedException e) {
						}
					}
				} else {
					try {
						sleep(1000);
					} catch (InterruptedException e) {
					}
				}
			}
		}

		public void stopSocketOut() {
			statusFlag = false;
		}
		
		/**
		 * ����Ϣ���͸�ǰ�û�
		 * 
		 * @param msgBytes
		 */
		private void sendToFrontServer(String msgSerial, byte[] msgBytes) {
			checkSocket();
//			Message message = bean.getMessage();
//			if (message instanceof BytesMessage) {
				try {
//					byte[] b = new byte[(int) ((BytesMessage) message)
//							.getBodyLength()];
//					((BytesMessage) message).readBytes(b);
					out.write(msgBytes);
					// �ж���û�з��ͳɹ���
					// TODO...

					msgPool.removeToMessage(msgSerial);
				} catch (IOException e) {
					if (e.getMessage().equalsIgnoreCase("Read timed out")) {
						MsgLogger.log(MsgLogger.INFO, e.getMessage()
								+ ".��ʱ��socket�����¿�ʼ��ȡ����ǰ���ݶ���.");
					} else if (e.getMessage().equalsIgnoreCase("Connection reset")) {
						MsgLogger.log(MsgLogger.WARN, e.getMessage()
								+ ".socket���ӱ�����.");
						try {
							consumerTool.getConsumer().setMessageListener(null);
						} catch (JMSException e1) {
							MsgLogger.logExceptionTrace(
									"consumerTool�Ƴ�MessageListener�����쳣��", e);
						}
						reconnectToFrontServer();
					} else {
						if (e instanceof SocketException) {
							reconnectToFrontServer();
						} else
							MsgLogger.logExceptionTrace("socket��ȡ���ݳ����쳣��", e);
					}
				}
			} 
	}
	

	private class ShutDown extends Thread {
		public void run() {
			stopTransactor();
			MsgLogger.log(MsgLogger.INFO, "Transactor Server�رգ��˳�����!");
		}
	}

	/**
	 * ���ֽ��ж�ȡ�߼���ַ
	 * 
	 * @param b
	 *            ��������֡
	 * @return �߼���ַ
	 */
	private String getLogicalAddr(byte[] oneFrame) {
		byte[] logicalAddrByte = StaticUnPackageMessage
				.getLogicalAddress(oneFrame);
		return CharCoding.byte2hex(logicalAddrByte);
	}

	/**
	 * �жϵ�ǰ֡�Ƿ����Զ�������֡������֡����Ҫת����Ӧ�õģ������ģ���¼���澯���˳��ȣ�����
	 * 
	 * @param oneFrame
	 *            ��������֡
	 * @return
	 */
	private boolean isMessageToTransact(byte functionCode) {
		if (functionCode == 0x0F) { // 0x0F���Զ������ݵ�֡
			return true;
		}
		return false;
	}

	/**
	 * ��¼��ʡ��˾ǰ�û�
	 * 
	 * @return
	 */
	// private boolean login() {
	// if(isLogin)
	// return true;
	// MsgLogger.log(MsgLogger.INFO, "��ʼ���͵�½��Ϣ...");
	// try {
	// byte[] loginBytes = CommonPackage.getLoginPackage(loginPsw
	// .getBytes("utf-8"));
	// byte[] loginReturnBytes = new byte[13];// ǰ�û�����13���ֽڵĵ�¼��Ӧ
	// try {
	// out.write(loginBytes);
	// int index = 0;
	// while (index < 13) {
	// loginReturnBytes[index] = in.readByte();
	// index++;
	// }
	// byte functionCode = StaticUnPackageMessage
	// .getFunctionCode(loginReturnBytes);
	// MsgLogger
	// .log(MsgLogger.INFO, "��¼��Ӧ��Ϣ�Ŀ������еĹ�����:" + functionCode);
	// if (functionCode == 0xA1) {
	// MsgLogger.log(MsgLogger.INFO, "��¼��ʡ��˾ǰ�û��ɹ���");
	// return true;
	// }
	// } catch (IOException e) {
	// MsgLogger.logExceptionTrace("��¼���̲����쳣��", e);
	// }
	// } catch (UnsupportedEncodingException e) {
	// e.printStackTrace();
	// }
	// MsgLogger.log(MsgLogger.INFO, "��¼��ʡ��˾ǰ�û�ʧ�ܣ�");
	// return false;
	// }
	// public void dispose(){
	//		
	// }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MsgLogger.log(MsgLogger.INFO, "��������������!");
		TransactorServer server = new TransactorServer(new GPSMsgPool());
		server.start();

	}

	/**
	 * ��ȡ�����
	 * 
	 * @return
	 */
	public DataOutputStream getOutputStream() {
		return out;
	}

	/**
	 * ��ȡ������
	 * 
	 * @return
	 */
	public DataInputStream getInputStream() {
		return in;
	}

	public GPSMsgPool getMsgPool() {
		return msgPool;
	}
	
	public boolean isLogin(){
		return isLogin;
	}

}
