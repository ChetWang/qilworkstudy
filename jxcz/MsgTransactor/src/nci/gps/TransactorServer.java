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
	 * 消息缓冲池
	 */
	private GPSMsgPool msgPool;
	/**
	 * JMS的消息生产者
	 */
	private ServerMsgProducerTool producerTool;
	/**
	 * JMS的消息消费者
	 */
	private ServerMsgConsumerTool consumerTool;
	/**
	 * 心跳发送程序
	 */
	private ServerHeartBeatTool heartBeatTool;
	/**
	 * 发往省公司前置机的数据流
	 */
	private DataOutputStream out;
	/**
	 * 从省公司前置机传过来的数据流
	 */
	private DataInputStream in;
	/**
	 * 连接到省公司前置机的套接字
	 */
	private Socket socket;

	/**
	 * 重连socket的锁，避免多个线程同时去重连
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
	 * 初始化套接字
	 * 
	 * @return
	 */
	private boolean iniSocket() {
		Document serverDoc = Utilities
				.getConfigXMLDocument("frontServerConf.xml");
		// 读取省公司前置机的设置
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
			MsgLogger.log(MsgLogger.INFO, "正在连接前置机，" + "IP:" + frontServerIP
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
			MsgLogger.log(MsgLogger.INFO, "成功连接至前置机，" + "IP:" + frontServerIP
					+ ",port:" + frontServerPort);
			isLogin = true;
			return true;
		} catch (Exception e) {
			MsgLogger.log(MsgLogger.INFO, "socket连接出错！" + "IP:" + frontServerIP
					+ ",port:" + frontServerPort + "准备在10秒后重连");
			try {
				Thread.sleep(10 * 1000);
			} catch (InterruptedException e1) {
			}
			iniSocket();
		}
		return false;
	}

	/**
	 * 启动服务器
	 */
	public void start() {
		if (!isLogin) {
			MsgLogger.log(MsgLogger.ERROR, "系统没有登录到前置机，无法启动.");
		} else {
			producerTool.start();
			consumerTool.start();
			heartBeatTool.start();
			socketIn.start();
			socketOut.start();
		}
	}

	/**
	 * 关闭Transactor服务器
	 */
	public void stopTransactor() {
		socketIn.statusFlag = false;
		socketOut.statusFlag = false;
		consumerTool.stopConsumer();
		producerTool.stopProducer();
		heartBeatTool.stopHeartBeat();
	}

	/**
	 * 获取套接字
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
	 * 检查套接字的有效性，无效则重连
	 */
	private void checkSocket() {
		if (socket == null || !socket.isConnected()) {
			synchronized (recnectLock) {
				reconnectToFrontServer();
			}
		}
	}

	/**
	 * 重连前置机
	 */
	private void reconnectToFrontServer() {
		isLogin = false;

		try {
			if (socket != null)
				socket.close();
		} catch (IOException e1) {
			MsgLogger.logExceptionTrace("socket close出现异常！", e1);
		}
		socket = null;
		while (iniSocket() == false) {
			MsgLogger.log(MsgLogger.INFO, "准备在10秒后重连");
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
					.logExceptionTrace("consumerTool设置MessageListener出现异常！", e);
		}
		// isLogin = login();
		isLogin = true;
	}

	/**
	 * 接收前置机消息的线程
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
			MsgLogger.log(MsgLogger.INFO, "接收省公司前置机消息的线程启动.");
			int index = 0;// 当前读取的字节序号
			int dataLength = 0;// 数据段字节的长度

			// 头11个字节的数据
			byte[] firstHead = new byte[11];

			// 整个数据帧的数据
			byte[] oneFrame = null;

			String subject = null;

			String logicalAddr = null;
			boolean frameStart = false;
			while (true) {
				if (statusFlag) {
					// 接收前置机消息,并发入到msgPool待转发哈希表
					try {
						checkSocket();
						if (!frameStart) {
							byte readByte = in.readByte();

							if (readByte == 0x68) {
								frameStart = true;
								firstHead[0] = 0x68;
								index++;
							}
						} else {// 开始

							if (index <= 10) {
								firstHead[index] = in.readByte();

							} else {
								if (index == 11) {
									// 数据长度位计算出数据变长
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
								// 一帧的数据包
								oneFrame[index] = in.readByte();
							}
							index++;
							if (index == dataLength + 13) {// 这里index(已经最后加1)是最后一位
								MsgLogger.log(MsgLogger.INFO, "接收到字节："+CharCoding.byte2hex(oneFrame));
								frameStart = false;
								index = 0;
								byte functionCode = StaticUnPackageMessage
										.getFunctionCode(oneFrame);
								// 判断是否是省公司前置机发送过来的非终端发送的需要中转的消息
								if (isMessageToTransact(functionCode)) {

									// 将数据帧转换成jms消息放入到待转哈希Map
									logicalAddr = getLogicalAddr(oneFrame);

									subject = (String) msgPool
											.getLogicalAddrsSubjectMap().get(
													logicalAddr);
									if (subject == null) {
										MsgLogger.log(MsgLogger.WARN,
												"无法根据逻辑地址:" + logicalAddr
														+ " 找到对应的主题!");
									} else {
										Session jmsSession = (Session) msgPool
												.getSessionMap().get(subject);
										if (jmsSession == null) {
											MsgLogger
													.log(
															MsgLogger.WARN,
															"无法根据主题:"
																	+ subject
																	+ " 找到对应的JMS Session!");
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
									// 如果是其它类型的，比如回执、心跳响应、登录请求、注销等等

									MsgLogger.log(MsgLogger.INFO,
											"收到非转发消息.控制码中的功能码：" + functionCode);
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
										+ ".超时，socket将重新开始读取，先前数据丢弃.");
							} else if (e.getMessage().equalsIgnoreCase(
									"Connection reset")) {
								MsgLogger.log(MsgLogger.WARN, e.getMessage()
										+ ".socket连接被重置. 开始重新初始化socket连接.");
								reconnectToFrontServer();
							} else {
								MsgLogger.logExceptionTrace("socket读取数据出现异常！",
										e);
							}
						}else{
							MsgLogger.logExceptionTrace(e.getClass().getName(),
									e);
							reconnectToFrontServer();
						}						
						index = 0;
					} catch (JMSException e) {
						MsgLogger.logExceptionTrace("JMS消息产生过程中出现异常！", e);
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
	 * 发送消息给前置机的线程
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
			MsgLogger.log(MsgLogger.INFO, "消息转发给应用的线程启动.");
			while (true) {
				if (statusFlag) {
					// 给前置机发送消息
					if (msgPool.hasToFrontMessage()) {
						it = msgPool.getToFrontMsgMap().keySet().iterator();
						serial = (String) it.next();
						msgBytes = msgPool.getToMessageBytes(serial);
						// 发送到前置机
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
		 * 将消息发送给前置机
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
					// 判断有没有发送成功：
					// TODO...

					msgPool.removeToMessage(msgSerial);
				} catch (IOException e) {
					if (e.getMessage().equalsIgnoreCase("Read timed out")) {
						MsgLogger.log(MsgLogger.INFO, e.getMessage()
								+ ".超时，socket将重新开始读取，先前数据丢弃.");
					} else if (e.getMessage().equalsIgnoreCase("Connection reset")) {
						MsgLogger.log(MsgLogger.WARN, e.getMessage()
								+ ".socket连接被重置.");
						try {
							consumerTool.getConsumer().setMessageListener(null);
						} catch (JMSException e1) {
							MsgLogger.logExceptionTrace(
									"consumerTool移除MessageListener出现异常！", e);
						}
						reconnectToFrontServer();
					} else {
						if (e instanceof SocketException) {
							reconnectToFrontServer();
						} else
							MsgLogger.logExceptionTrace("socket读取数据出现异常！", e);
					}
				}
			} 
	}
	

	private class ShutDown extends Thread {
		public void run() {
			stopTransactor();
			MsgLogger.log(MsgLogger.INFO, "Transactor Server关闭，退出服务!");
		}
	}

	/**
	 * 从字节中读取逻辑地址
	 * 
	 * @param b
	 *            整个数据帧
	 * @return 逻辑地址
	 */
	private String getLogicalAddr(byte[] oneFrame) {
		byte[] logicalAddrByte = StaticUnPackageMessage
				.getLogicalAddress(oneFrame);
		return CharCoding.byte2hex(logicalAddrByte);
	}

	/**
	 * 判断当前帧是否是自定义数据帧，这类帧是需要转发给应用的，其它的（登录、告警、退出等）不用
	 * 
	 * @param oneFrame
	 *            整个数据帧
	 * @return
	 */
	private boolean isMessageToTransact(byte functionCode) {
		if (functionCode == 0x0F) { // 0x0F是自定义数据的帧
			return true;
		}
		return false;
	}

	/**
	 * 登录到省公司前置机
	 * 
	 * @return
	 */
	// private boolean login() {
	// if(isLogin)
	// return true;
	// MsgLogger.log(MsgLogger.INFO, "开始发送登陆信息...");
	// try {
	// byte[] loginBytes = CommonPackage.getLoginPackage(loginPsw
	// .getBytes("utf-8"));
	// byte[] loginReturnBytes = new byte[13];// 前置机返回13个字节的登录响应
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
	// .log(MsgLogger.INFO, "登录响应信息的控制码中的功能码:" + functionCode);
	// if (functionCode == 0xA1) {
	// MsgLogger.log(MsgLogger.INFO, "登录到省公司前置机成功！");
	// return true;
	// }
	// } catch (IOException e) {
	// MsgLogger.logExceptionTrace("登录过程产生异常！", e);
	// }
	// } catch (UnsupportedEncodingException e) {
	// e.printStackTrace();
	// }
	// MsgLogger.log(MsgLogger.INFO, "登录到省公司前置机失败！");
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
		MsgLogger.log(MsgLogger.INFO, "服务器正在启动!");
		TransactorServer server = new TransactorServer(new GPSMsgPool());
		server.start();

	}

	/**
	 * 获取输出流
	 * 
	 * @return
	 */
	public DataOutputStream getOutputStream() {
		return out;
	}

	/**
	 * 获取输入流
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
