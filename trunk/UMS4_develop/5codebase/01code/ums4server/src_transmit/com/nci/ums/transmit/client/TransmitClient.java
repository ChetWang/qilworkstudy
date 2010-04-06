package com.nci.ums.transmit.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.nci.ums.transmit.common.TransmitData;
import com.nci.ums.transmit.common.UMSTransmitException;
import com.nci.ums.transmit.common.UMSTransmitLogger;
import com.nci.ums.transmit.common.message.CommonMessage;
import com.nci.ums.transmit.common.message.ControlCode;
import com.nci.ums.transmit.common.message.GlobalConstant;
import com.nci.ums.transmit.common.message.UnPackageMessage;

import edu.emory.mathcs.backport.java.util.concurrent.BlockingQueue;
import edu.emory.mathcs.backport.java.util.concurrent.ConcurrentHashMap;
import edu.emory.mathcs.backport.java.util.concurrent.CopyOnWriteArrayList;
import edu.emory.mathcs.backport.java.util.concurrent.LinkedBlockingQueue;

public class TransmitClient {

	/**
	 * 远程转发服务IP
	 */
	private String serverIP;
	/**
	 * 转发服务对应端口
	 */
	private int serverPort;

	/**
	 * 接入地址
	 */
	private int clientAddress;

	/**
	 * 接入地址类型（来自终端还是接入应用）
	 */
	private int subServerType;

	private boolean connected = false;

	private DataInputStream in; // input stream
	private DataOutputStream out; // output stream

	/**
	 * 待发数据队列
	 */
	// private ConcurrentLinkedQueue outBuffer = new ConcurrentLinkedQueue();
	private BlockingQueue outBuffer = new LinkedBlockingQueue();
	/**
	 * 接收数据队列
	 */
	// private ConcurrentLinkedQueue inBuffer = new ConcurrentLinkedQueue();
	private BlockingQueue inBuffer = new LinkedBlockingQueue();

	/**
	 * 注册的数据接收处理器集合
	 */
	private CopyOnWriteArrayList dataReceiveHandlers = new CopyOnWriteArrayList();

	/**
	 * 客户端发出命令等待接收回应处理器集合
	 */
	private Map requestHandlers = new ConcurrentHashMap();
	/**
	 * 多帧转发时的帧有效时间
	 */
	private int multipleFrameValidTime = 60;// seconds

	/**
	 * 发生异常，是否在断开后需要重连
	 */
	private boolean reconnectableOnException = false;
	// /**
	// * 正常断开后是否需要重连
	// */
	// private boolean currentDisconnectionNeedReconnect = true;

	/**
	 * 重连等待时间
	 */
	private int reconnectWaitTimeSeconds = 30;// seconds

	/**
	 * 是否正在重连
	 */
	private boolean reconnecting = false;

	/**
	 * 默认用户自定义数据命令号最大值65536，两个字节
	 */
	public static final int DEFAULT_MAX_USER_COMMAND = 65535;

	/**
	 * 不需命令号时的"命令"参数
	 */
	public static final int COMMAND_NO = 0;

	/**
	 * 命令字节长度
	 */
	public static final int COMMAND_BYTES_LENGTH = 2;

	/**
	 * 心跳报文
	 */
	private static byte[] heartBeat = CommonMessage.getServerHeartBeatPackage();

	private UMSTransmitLogger logger;

	private Timer timer = new Timer();

	private int heartBeatPeriod = 30;// seconds

	private Socket socket;

	/**
	 * 数据转发客户端连接
	 * 
	 * @param serverIP
	 *            数据转发服务器ip地址
	 * @param serverPort
	 *            数据转发服务器端口
	 * @param clientAddress
	 *            分配的接入地址
	 * @param subServerType
	 *            接入对应的中转子服务类型
	 * @see com.nci.ums.transmit.common.message.ControlCode.DIRECTION_FROM_APPLICATION
	 * @see com.nci.ums.transmit.common.message.ControlCode.DIRECTION_FROM_TERMINAL
	 */
	public TransmitClient(String serverIP, int serverPort, int clientAddress,
			int subServerType) {
		this.serverIP = serverIP;
		this.serverPort = serverPort;
		this.clientAddress = clientAddress;
		this.subServerType = subServerType;
		reconnectableOnException = false;
		this.logger = new DefaultTransmitLogger();
	}

	public TransmitClient(String serverIP, int serverPort, int clientAddress,
			int subServerType, UMSTransmitLogger logger) {
		this.serverIP = serverIP;
		this.serverPort = serverPort;
		this.clientAddress = clientAddress;
		this.subServerType = subServerType;
		reconnectableOnException = false;
		this.logger = logger;
		if(logger == null){
			this.logger = new DefaultTransmitLogger();
		}
	}

	/**
	 * 数据转发客户端连接
	 * 
	 * @param serverIP
	 *            数据转发服务器ip地址
	 * @param serverPort
	 *            数据转发服务器端口
	 * @param clientAddress
	 *            分配的接入地址
	 * @param subServerType
	 *            接入对应的中转子服务类型
	 * @see com.nci.ums.transmit.common.message.ControlCode.DIRECTION_FROM_APPLICATION
	 * @see com.nci.ums.transmit.common.message.ControlCode.DIRECTION_FROM_TERMINAL
	 * 
	 * @param reconnectable
	 *            是否在异常断开后需要重连
	 * @param reconnectWaitTimeSeconds
	 *            重连等待时间
	 */
	public TransmitClient(String serverIP, int serverPort, int clientAddress,
			int subServerType, boolean reconnectable,
			int reconnectWaitTimeSeconds) {
		this.serverIP = serverIP;
		this.serverPort = serverPort;
		this.clientAddress = clientAddress;
		this.subServerType = subServerType;
		this.reconnectableOnException = reconnectable;
		this.reconnectWaitTimeSeconds = reconnectWaitTimeSeconds;
		this.logger = new DefaultTransmitLogger();
	}

	public TransmitClient(String serverIP, int serverPort, int clientAddress,
			int subServerType, boolean reconnectable,
			int reconnectWaitTimeSeconds, UMSTransmitLogger logger) {
		this.serverIP = serverIP;
		this.serverPort = serverPort;
		this.clientAddress = clientAddress;
		this.subServerType = subServerType;
		this.reconnectableOnException = reconnectable;
		this.reconnectWaitTimeSeconds = reconnectWaitTimeSeconds;
		this.logger = logger;
	}

	/**
	 * 连接服务器
	 * 
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public void connect() throws UnknownHostException, IOException {
		inBuffer.clear();
		outBuffer.clear();
		socket = new Socket(serverIP, serverPort);
		socket.setSoTimeout(60 * 1000);
		in = new DataInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());
		connected = true;
		final Thread t1 = new Thread(new SocketIn());
		t1.setName("Transmit Client Receive " + toStringDesc());
		final Thread t2 = new Thread(new SocketOut());
		t2.setName("Transmit Client Send " + toStringDesc());
		final Thread t3 = new Thread(new InDataProcessor());
		t3.setName("Transmit Client Received Data Processor " + toStringDesc());
		t1.start();
		t2.start();
		t3.start();

		TimerTask heartBeatTask = new TimerTask() {
			public void run() {
				try {
					logger
							.log(UMSTransmitLogger.DEBUG, "发送心跳"
									+ toStringDesc());
					if (outBuffer.size() == 0)
						enqueueOutData(heartBeat);
				} catch (UMSTransmitException e) {
					logger.log(UMSTransmitLogger.ERROR, e);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		TimerTask aliveCheck = new TimerTask() {
			public void run() {
				if (!t1.isAlive() || !t2.isAlive() || !t3.isAlive()) {
					disconnect(reconnectableOnException);
				}
			}
		};
		timer.scheduleAtFixedRate(heartBeatTask, heartBeatPeriod * 1000,
				heartBeatPeriod * 1000);
		timer.scheduleAtFixedRate(aliveCheck, 10 * 1000, 3 * 1000);
	}

	/**
	 * 获取连接描述
	 * 
	 * @return
	 */
	public String toStringDesc() {
		String t_a = subServerType == ControlCode.DIRECTION_FROM_APPLICATION ? "A"
				: "T";
		return serverIP + ":" + serverPort + "-[" + clientAddress + "]-" + t_a;
	}

	/**
	 * 获取定时器
	 * 
	 * @return
	 */
	public Timer getTimer() {
		return timer;
	}

	public Map getRequestHandlers() {
		return requestHandlers;
	}

	/**
	 * 判断转发子服务类型
	 * 
	 * @return int
	 */
	public int getSubServerType() {
		return subServerType;
	}

	/**
	 * 重连服务器
	 */
	protected void reconnect() {
		if (!reconnecting) {
			reconnecting = true;
			new Thread() {
				public void run() {
					logger.log(UMSTransmitLogger.ERROR, toStringDesc()
							+ "连接断开，等待" + reconnectWaitTimeSeconds
							+ "秒后开始重连服务器...");
					try {
						sleep(reconnectWaitTimeSeconds);
					} catch (InterruptedException e) {
						logger.log(UMSTransmitLogger.ERROR, e);
					}
					try {
						connect();
						login();
					} catch (UnknownHostException e) {
						logger.log(UMSTransmitLogger.ERROR, e);
						disconnect(false);
					} catch (IOException e) {
						logger.log(UMSTransmitLogger.ERROR, e);
						disconnect(reconnectableOnException);
					}
					logger.log(UMSTransmitLogger.DEBUG, toStringDesc()
							+ "重连服务器成功");
					reconnecting = false;
				}
			}.start();
		}
	}

	/**
	 * 登陆
	 */
	public void login() {
		try {
			if (connected) {
				Thread.sleep(1500);
				logger.log(UMSTransmitLogger.DEBUG, "start login!");
				byte[] loginBytes = null;
				if (subServerType == ControlCode.DIRECTION_FROM_APPLICATION) {
					loginBytes = CommonMessage.getLoginMessage(clientAddress,
							0, subServerType);
				} else {
					loginBytes = CommonMessage.getLoginMessage(0,
							clientAddress, subServerType);
				}
				out.write(loginBytes);
				out.flush();
			} else {
				logger.log(UMSTransmitLogger.ERROR, new Exception(
						"not connected!"));
			}
		} catch (Exception e) {
			close();
			logger.log(UMSTransmitLogger.ERROR, e);
		}
	}

	/**
	 * 登出，并且断开连接
	 */
	public void logout() {
		try {
			if (connected) {
				byte[] logoutBytes = null;
				if (subServerType == ControlCode.DIRECTION_FROM_APPLICATION) {
					logoutBytes = CommonMessage.getLogoutMessage(clientAddress,
							0, subServerType);
				} else {
					logoutBytes = CommonMessage.getLogoutMessage(0,
							clientAddress, subServerType);
				}
				enqueueOutData(logoutBytes);
				close();
			} else {
				new Exception("not connected!").printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭，等待SocketIn进行重连或其它处理
	 */
	public void close() {
		try {
			if (in != null)
				in.close();
		} catch (IOException e) {
		}
		try {
			if (out != null)
				out.close();
		} catch (IOException e) {
		}
		try {
			if (socket != null)
				socket.close();
		} catch (IOException e) {
		}
	}

	/**
	 * 断开连接
	 * 
	 * @param needReconnect
	 *            是否在停止后需要重连
	 */
	protected void disconnect(boolean needReconnect) {

		// synchronized (inDataLock) {
		// inDataLock.notifyAll();
		// }
		// synchronized (outDataLock) {
		// outDataLock.notifyAll();
		// }
		// currentDisconnectionNeedReconnect = needReconnect;
		try {
			enqueUserInData(TransmitData.QUIT_SIGNAL);
			enqueueOutData((TransmitData.QUIT_SIGNAL.getOneFrameData()));
		} catch (UMSTransmitException e1) {
			logger.log(UMSTransmitLogger.ERROR, e1);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		connected = false;
		timer.cancel();
		try {
			if (in != null)
				in.close();
		} catch (IOException e) {
		}
		try {
			if (out != null)
				out.close();
		} catch (IOException e) {
		}
		try {
			if (socket != null)
				socket.close();
		} catch (IOException e) {
		}
		if (needReconnect) {
			reconnect();
		}
		logger.log(UMSTransmitLogger.DEBUG, toStringDesc() + "与服务器连接断开!");
	}

	public int getClientAddress() {
		return clientAddress;
	}

	/**
	 * 注册接收数据处理器
	 * 
	 * @param handler
	 *            接收数据处理器
	 */
	public void registerDataReceivedHandler(DataReceivedHandler handler) {
		this.dataReceiveHandlers.add(handler);
	}

	/**
	 * 删除已经注册的接收数据处理器
	 * 
	 * @param handler
	 *            接收数据处理器
	 */
	public void removeDataReceivedHandler(DataReceivedHandler handler) {
		this.dataReceiveHandlers.remove(handler);
	}

	// /**
	// * 触发单帧已接收
	// *
	// * @param data
	// */
	// protected void fireSingleDataReceived(TransmitData data) {
	// for (int i = 0; i < dataReceiveHandlers.size(); i++) {
	// ((DataReceivedHandler) dataReceiveHandlers.get(i))
	// .receivedSingle(data);
	// }
	// }

	/**
	 * 触发多帧已接收
	 * 
	 * @param data
	 */
	protected void fireDataReceived(TransmitData[] data) {
		for (int i = 0; i < dataReceiveHandlers.size(); i++) {
			((DataReceivedHandler) dataReceiveHandlers.get(i)).onReceived(data);
		}
	}

	/**
	 * 接收到命令反馈后的执行
	 * 
	 * @param data
	 * @param success
	 */
	protected void fireRequestHandlers(TransmitData[] data, boolean success) {
		ControlCode control = new ControlCode(data[0].getOneFrameData());
		if (control.getFunctionCode() == ControlCode.FUNCTION_USER_DEFINED) {
			// 用户自定义数据才进行request反馈，第一帧数据域头两字节是用户命令
			// byte[] content = UnPackageMessage
			// .getData(data[0].getOneFrameData());

			int fseq = UnPackageMessage.getFSEQ(data[0].getOneFrameData());

			if (success) {
				// 客户端接收到的数据的控制码方向如果和当前subservertype相反，则是对方的响应帧，否则是命令帧
				if ((subServerType == ControlCode.DIRECTION_FROM_APPLICATION && control
						.isToTerminal())
						|| (subServerType == ControlCode.DIRECTION_FROM_TREMINAL && !control
								.isToTerminal())) {
					Integer key = new Integer(fseq);
					RequestHandler request = (RequestHandler) requestHandlers
							.get(key);
					if (request != null) {
						requestHandlers.remove(key);
						request.getTimeoutTask().cancel();
						request.requestFinished(data, fseq);
					} else {
						// request
						logger.log(UMSTransmitLogger.DEBUG, "帧命令:" + fseq
								+ ", 对应的回调request不存在，或已超时");
					}
				}
			}

		}
	}

	/**
	 * 接收多帧失败，未能收到最后一帧
	 * 
	 * @param data
	 */
	protected void fireMultipleDataReceivedFailed(TransmitData[] data) {
		for (int i = 0; i < dataReceiveHandlers.size(); i++) {
			((DataReceivedHandler) dataReceiveHandlers.get(i))
					.failedReceivedMultiple(data);
		}
	}

	/**
	 * 往发送队列添加数据
	 * 
	 * @param oneFrameData
	 *            已经组装好的单帧数据，只能是单帧数据
	 * @throws InterruptedException
	 */
	public void enqueueOutData(byte[] oneFrameData)
			throws UMSTransmitException, InterruptedException {
		if (!connected) {
			throw new UMSTransmitException();
		}
		outBuffer.put(oneFrameData);
	}

	/**
	 * 往接收队列添加用户自定义数据数据
	 * 
	 * @param message
	 * @throws InterruptedException
	 */
	protected void enqueUserInData(TransmitData transData)
			throws UMSTransmitException, InterruptedException {
		if (!connected) {
			throw new UMSTransmitException();
		}
		inBuffer.put(transData);
	}

	/**
	 * 日期转换器
	 */
	private SimpleDateFormat defaultCurrentFmt = new SimpleDateFormat(
			"yyyyMMddHHmmss");

	/**
	 * 获取当前时间戳
	 * 
	 * @return
	 */
	protected synchronized String getCurrentTimeStr() {
		return defaultCurrentFmt.format(new java.util.Date());
	}

	/**
	 * 是否连接
	 * 
	 * @return
	 */
	public boolean isConnected() {
		return connected;
	}

	/**
	 * 接收到数据后的处理线程
	 * 
	 * @author Qil.Wong
	 * 
	 */
	private class InDataProcessor implements Runnable {

		// 多帧数据的临时存放地
		private Map tempMap = new ConcurrentHashMap();

		// 多帧数据key的间隔符号，key是以帧流水号和命令序号的集合体，以"|"分割
		private String tempKeySep = "|";

		// 数据拼接定时器
		private Timer combineTimer = new Timer();

		private boolean quit = false;

		public void run() {
			while (connected && !quit) {
				try {
					TransmitData data = (TransmitData) inBuffer.take();
					if (data == TransmitData.QUIT_SIGNAL) {
						quit = true;
						continue;
					}
					byte[] b = data.getOneFrameData();
					// 帧内序号，单帧为0，多帧为序号（最后一帧255）
					int iseq = UnPackageMessage.getISEQ(b);

					if (iseq == 0) {
						TransmitData[] datas = new TransmitData[] { data };
						fireDataReceived(datas);
						fireRequestHandlers(datas, true);
					} else {
						// 数据帧流水号，由发送方循环生成
						int fseq = UnPackageMessage.getFSEQ(b);
						// 获取命令序号
						int msta = UnPackageMessage.getMSTA(b);
						TimerTask task = null;
						final String key = fseq + tempKeySep + msta;
						if (iseq == 1) { // 第一帧时创建临时存放地和定时检查器
							final int totalFrameSize = UnPackageMessage
									.getFNUM(b);
							tempMap.put(key, new TransmitData[totalFrameSize]);
							task = new TimerTask() {
								public void run() {
									checkValidation(key, totalFrameSize);
								}
							};
							combineTimer.schedule(task,
									multipleFrameValidTime * 1000);
						}
						TransmitData[] datas = (TransmitData[]) tempMap
								.get(key);
						if (datas != null) {
							if (iseq != GlobalConstant.ISEQ_MAX_NUMBER) {
								datas[iseq - 1] = data;
							} else {
								datas[datas.length - 1] = data;
							}
						} else {
							logger.log(UMSTransmitLogger.DEBUG, "收到过期数据包");
						}
						// 进行多帧处理
						if (iseq == GlobalConstant.ISEQ_MAX_NUMBER
								&& datas != null) {
							TransmitData[] all = (TransmitData[]) tempMap
									.get(key);
							boolean correct = true;
							for (int a = 0; a < all.length; a++) {
								if (all[a] == null) {
									correct = false;
									break;
								}
							}
							tempMap.remove(key);
							if (correct) {
								fireDataReceived(all);
								fireRequestHandlers(all, true);
							} else {
								fireMultipleDataReceivedFailed(all);
								fireRequestHandlers(all, false);
							}
							// 已经获取到最后一帧，就取消定时任务
							if (task != null)
								task.cancel();
						}
					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.log(UMSTransmitLogger.INFO, toStringDesc()
					+ " 接收数据处理Processor线程退出");
		}

		/**
		 * 检查接收队列时效性，超时数据要定期清理
		 * 
		 * @param key
		 */
		public void checkValidation(String key, int totalFrameSize) {
			TransmitData[] all = (TransmitData[]) tempMap.get(key);
			if (all != null && all[totalFrameSize - 1] == null) {
				logger.log(UMSTransmitLogger.INFO, "移除无效数据，key:" + key);
				tempMap.remove(key);
				fireMultipleDataReceivedFailed(all);
			}
		}
	}

	/**
	 * 数据接收线程
	 * 
	 * @author Qil.Wong
	 * 
	 */
	private class SocketIn implements Runnable {

		public void run() {

			// int index = 0;// 当前读取的字节序号
			// int dataLength = 0;// 数据段字节的长度
			//
			// // 头11个字节的数据
			// byte[] firstHead = new byte[GlobalConstant.INDEX_DATA];
			//
			// // 整个数据帧的数据
			// byte[] oneFrame = null;
			// // 帧起始
			// boolean frameStart = false;
			//
			// TransmitData transmitData = null;
			// byte[] buff = new byte[4096];

			int sumLen = 0;// 初始长度
			int len = 0;
			int headIndex = GlobalConstant.INDEX_DATA;
			int endIndex = 2;
			byte[] heads = new byte[headIndex];// 头12个字节的数据
			byte[] ends = new byte[endIndex];// 校验码、结束码
			boolean frameStart = false;
			long current = 0;
			while (connected) {
				try {

					

					if (!frameStart) {
						sumLen = 0;
						byte readByte = in.readByte();
						current = System.currentTimeMillis();
						if (readByte == GlobalConstant.START_CHARACTER) {
							frameStart = true;
							heads[0] = GlobalConstant.START_CHARACTER;
							sumLen += 1;
						}
					} else {
						// 接收前十三个字节
						while ((len = in
								.read(heads, sumLen, headIndex - sumLen)) > 0
								&& sumLen <= headIndex) {
							sumLen += len;
						}
						// 数据长度位计算出数据变长
						int height = (heads[GlobalConstant.INDEX_DATA_LENGTH + 1] + 256) % 256;
						int low = (heads[GlobalConstant.INDEX_DATA_LENGTH] + 256) % 256;
						int dataLength = height * 256 + low;
						byte[] oneFrame = new byte[headIndex + dataLength
													+ endIndex];// 接收到的完整一帧数据
						byte[] data = new byte[dataLength];

						// 接收业务数据
						sumLen = 0;
						while ((len = in
								.read(data, sumLen, dataLength - sumLen)) > 0
								&& sumLen <= dataLength) {
							sumLen += len;
						}

						// 接收校验码、结束码
						sumLen = 0;
						while ((len = in.read(ends, sumLen, endIndex - sumLen)) > 0
								&& sumLen <= endIndex) {
							sumLen += len;
						}

						frameStart = false;
						
						System.arraycopy(heads, 0, oneFrame, 0, headIndex);
						System.arraycopy(data, 0, oneFrame, headIndex,
								dataLength);
						System.arraycopy(ends, 0, oneFrame, headIndex
								+ dataLength, endIndex);
						System.out.println("接收oneframe耗时"
								+ (System.currentTimeMillis() - current));
						TransmitData transmitData = new TransmitData(oneFrame,
								subServerType);
						transmitData.setReceivedTime(getCurrentTimeStr());
						// 处理接收的内容
						parseFunctionCode(transmitData);

					}
				} catch (IOException e) {
					// 一旦有IOException，连接断开
					if (e instanceof EOFException == false) {
						logger.log(UMSTransmitLogger.ERROR, e.getClass()
								.getName()
								+ "  " + e.getMessage());
					}

					disconnect(reconnectableOnException);
					break;
				}

			}
			logger.log(UMSTransmitLogger.INFO, toStringDesc() + " 接收线程退出");
		}

	}

	/**
	 * 对接收到的信息进行处理
	 * 
	 * @param inData
	 */
	private void parseFunctionCode(TransmitData inData) {
		ControlCode control = new ControlCode(inData.getOneFrameData());

		switch (control.getFunctionCode()) {
		case ControlCode.FUNCTION_CURRENT_DATA:
			break;
		case ControlCode.FUNCTION_HEARTBEAT:
			logger.log(UMSTransmitLogger.DEBUG, "收到心跳" + toStringDesc());
			break;
		case ControlCode.FUNCTION_LOGGING_IN:
			break;
		case ControlCode.FUNCTION_LOGGING_OUT:
			break;
		case ControlCode.FUNCTION_RELAY:
			break;
		case ControlCode.FUNCTION_TASK_DATA:
			break;
		case ControlCode.FUNCTION_USER_DEFINED:
			try {
				enqueUserInData(inData);
			} catch (UMSTransmitException e) {
				logger.log(UMSTransmitLogger.ERROR, e);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		case ControlCode.FUNCTION_WARNING:
			break;
		case ControlCode.FUNCTION_WARNING_DONFIRM:
			break;
		default:
			break;
		}
	}

	/**
	 * 数据发送线程
	 * 
	 * @author Qil.Wong
	 * 
	 */
	private class SocketOut implements Runnable {

		private int buffLength = 1024;

		private boolean quit = false;

		public void run() {

			while (connected && !quit) {
				try {
					byte[] b = (byte[]) outBuffer.take();
					if (b == TransmitData.QUIT_SIGNAL.getOneFrameData()) {
						quit = true;
						continue;
					}
					if (b.length > buffLength) {
						// 为避免超大帧发送造成write阻塞，需拆成多份发送
						for (int i = 0; i < b.length; i = i + buffLength) {
							int writeLength = buffLength;
							if (i > b.length - buffLength) {
								writeLength = b.length - i;
							}
							out.write(b, i, writeLength);
						}
					} else {
						out.write(b);
					}
					out.flush();
					logger
							.log(UMSTransmitLogger.DEBUG, "send data:"
									+ b.length);

				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (IOException e) {
					logger.log(UMSTransmitLogger.ERROR, e.getClass().getName()
							+ "  " + e.getMessage());
					// 这里只能用close，而不能用disconnect，否则会产生多次重连。disconnect用在socketin一个地方。
					close();
					break;
				}
			}
			logger.log(UMSTransmitLogger.INFO, toStringDesc() + " 发送线程退出");
		}
	}

	/**
	 * 获取日志记录器
	 * 
	 * @return
	 */
	public UMSTransmitLogger getLogger() {
		return logger;
	}

	/**
	 * 设置日志记录器
	 * 
	 * @param logger
	 */
	public void setLogger(UMSTransmitLogger logger) {
		this.logger = logger;
	}

	private class DefaultTransmitLogger implements UMSTransmitLogger {

		public void log(int level, Object info) {
			if (info instanceof Exception)
				((Exception) info).printStackTrace();
			else {
				if (level == ERROR || level == FATAL)
					System.err.println(info);
				else {
					System.out.println(info);
				}
			}
		}

	}

}
