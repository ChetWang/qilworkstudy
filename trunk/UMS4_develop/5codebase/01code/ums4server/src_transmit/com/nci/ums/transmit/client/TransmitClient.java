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
	 * Զ��ת������IP
	 */
	private String serverIP;
	/**
	 * ת�������Ӧ�˿�
	 */
	private int serverPort;

	/**
	 * �����ַ
	 */
	private int clientAddress;

	/**
	 * �����ַ���ͣ������ն˻��ǽ���Ӧ�ã�
	 */
	private int subServerType;

	private boolean connected = false;

	private DataInputStream in; // input stream
	private DataOutputStream out; // output stream

	/**
	 * �������ݶ���
	 */
	// private ConcurrentLinkedQueue outBuffer = new ConcurrentLinkedQueue();
	private BlockingQueue outBuffer = new LinkedBlockingQueue();
	/**
	 * �������ݶ���
	 */
	// private ConcurrentLinkedQueue inBuffer = new ConcurrentLinkedQueue();
	private BlockingQueue inBuffer = new LinkedBlockingQueue();

	/**
	 * ע������ݽ��մ���������
	 */
	private CopyOnWriteArrayList dataReceiveHandlers = new CopyOnWriteArrayList();

	/**
	 * �ͻ��˷�������ȴ����ջ�Ӧ����������
	 */
	private Map requestHandlers = new ConcurrentHashMap();
	/**
	 * ��֡ת��ʱ��֡��Чʱ��
	 */
	private int multipleFrameValidTime = 60;// seconds

	/**
	 * �����쳣���Ƿ��ڶϿ�����Ҫ����
	 */
	private boolean reconnectableOnException = false;
	// /**
	// * �����Ͽ����Ƿ���Ҫ����
	// */
	// private boolean currentDisconnectionNeedReconnect = true;

	/**
	 * �����ȴ�ʱ��
	 */
	private int reconnectWaitTimeSeconds = 30;// seconds

	/**
	 * �Ƿ���������
	 */
	private boolean reconnecting = false;

	/**
	 * Ĭ���û��Զ���������������ֵ65536�������ֽ�
	 */
	public static final int DEFAULT_MAX_USER_COMMAND = 65535;

	/**
	 * ���������ʱ��"����"����
	 */
	public static final int COMMAND_NO = 0;

	/**
	 * �����ֽڳ���
	 */
	public static final int COMMAND_BYTES_LENGTH = 2;

	/**
	 * ��������
	 */
	private static byte[] heartBeat = CommonMessage.getServerHeartBeatPackage();

	private UMSTransmitLogger logger;

	private Timer timer = new Timer();

	private int heartBeatPeriod = 30;// seconds

	private Socket socket;

	/**
	 * ����ת���ͻ�������
	 * 
	 * @param serverIP
	 *            ����ת��������ip��ַ
	 * @param serverPort
	 *            ����ת���������˿�
	 * @param clientAddress
	 *            ����Ľ����ַ
	 * @param subServerType
	 *            �����Ӧ����ת�ӷ�������
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
	 * ����ת���ͻ�������
	 * 
	 * @param serverIP
	 *            ����ת��������ip��ַ
	 * @param serverPort
	 *            ����ת���������˿�
	 * @param clientAddress
	 *            ����Ľ����ַ
	 * @param subServerType
	 *            �����Ӧ����ת�ӷ�������
	 * @see com.nci.ums.transmit.common.message.ControlCode.DIRECTION_FROM_APPLICATION
	 * @see com.nci.ums.transmit.common.message.ControlCode.DIRECTION_FROM_TERMINAL
	 * 
	 * @param reconnectable
	 *            �Ƿ����쳣�Ͽ�����Ҫ����
	 * @param reconnectWaitTimeSeconds
	 *            �����ȴ�ʱ��
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
	 * ���ӷ�����
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
							.log(UMSTransmitLogger.DEBUG, "��������"
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
	 * ��ȡ��������
	 * 
	 * @return
	 */
	public String toStringDesc() {
		String t_a = subServerType == ControlCode.DIRECTION_FROM_APPLICATION ? "A"
				: "T";
		return serverIP + ":" + serverPort + "-[" + clientAddress + "]-" + t_a;
	}

	/**
	 * ��ȡ��ʱ��
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
	 * �ж�ת���ӷ�������
	 * 
	 * @return int
	 */
	public int getSubServerType() {
		return subServerType;
	}

	/**
	 * ����������
	 */
	protected void reconnect() {
		if (!reconnecting) {
			reconnecting = true;
			new Thread() {
				public void run() {
					logger.log(UMSTransmitLogger.ERROR, toStringDesc()
							+ "���ӶϿ����ȴ�" + reconnectWaitTimeSeconds
							+ "���ʼ����������...");
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
							+ "�����������ɹ�");
					reconnecting = false;
				}
			}.start();
		}
	}

	/**
	 * ��½
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
	 * �ǳ������ҶϿ�����
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
	 * �رգ��ȴ�SocketIn������������������
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
	 * �Ͽ�����
	 * 
	 * @param needReconnect
	 *            �Ƿ���ֹͣ����Ҫ����
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
		logger.log(UMSTransmitLogger.DEBUG, toStringDesc() + "����������ӶϿ�!");
	}

	public int getClientAddress() {
		return clientAddress;
	}

	/**
	 * ע��������ݴ�����
	 * 
	 * @param handler
	 *            �������ݴ�����
	 */
	public void registerDataReceivedHandler(DataReceivedHandler handler) {
		this.dataReceiveHandlers.add(handler);
	}

	/**
	 * ɾ���Ѿ�ע��Ľ������ݴ�����
	 * 
	 * @param handler
	 *            �������ݴ�����
	 */
	public void removeDataReceivedHandler(DataReceivedHandler handler) {
		this.dataReceiveHandlers.remove(handler);
	}

	// /**
	// * ������֡�ѽ���
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
	 * ������֡�ѽ���
	 * 
	 * @param data
	 */
	protected void fireDataReceived(TransmitData[] data) {
		for (int i = 0; i < dataReceiveHandlers.size(); i++) {
			((DataReceivedHandler) dataReceiveHandlers.get(i)).onReceived(data);
		}
	}

	/**
	 * ���յ���������ִ��
	 * 
	 * @param data
	 * @param success
	 */
	protected void fireRequestHandlers(TransmitData[] data, boolean success) {
		ControlCode control = new ControlCode(data[0].getOneFrameData());
		if (control.getFunctionCode() == ControlCode.FUNCTION_USER_DEFINED) {
			// �û��Զ������ݲŽ���request��������һ֡������ͷ���ֽ����û�����
			// byte[] content = UnPackageMessage
			// .getData(data[0].getOneFrameData());

			int fseq = UnPackageMessage.getFSEQ(data[0].getOneFrameData());

			if (success) {
				// �ͻ��˽��յ������ݵĿ����뷽������͵�ǰsubservertype�෴�����ǶԷ�����Ӧ֡������������֡
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
						logger.log(UMSTransmitLogger.DEBUG, "֡����:" + fseq
								+ ", ��Ӧ�Ļص�request�����ڣ����ѳ�ʱ");
					}
				}
			}

		}
	}

	/**
	 * ���ն�֡ʧ�ܣ�δ���յ����һ֡
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
	 * �����Ͷ����������
	 * 
	 * @param oneFrameData
	 *            �Ѿ���װ�õĵ�֡���ݣ�ֻ���ǵ�֡����
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
	 * �����ն�������û��Զ�����������
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
	 * ����ת����
	 */
	private SimpleDateFormat defaultCurrentFmt = new SimpleDateFormat(
			"yyyyMMddHHmmss");

	/**
	 * ��ȡ��ǰʱ���
	 * 
	 * @return
	 */
	protected synchronized String getCurrentTimeStr() {
		return defaultCurrentFmt.format(new java.util.Date());
	}

	/**
	 * �Ƿ�����
	 * 
	 * @return
	 */
	public boolean isConnected() {
		return connected;
	}

	/**
	 * ���յ����ݺ�Ĵ����߳�
	 * 
	 * @author Qil.Wong
	 * 
	 */
	private class InDataProcessor implements Runnable {

		// ��֡���ݵ���ʱ��ŵ�
		private Map tempMap = new ConcurrentHashMap();

		// ��֡����key�ļ�����ţ�key����֡��ˮ�ź�������ŵļ����壬��"|"�ָ�
		private String tempKeySep = "|";

		// ����ƴ�Ӷ�ʱ��
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
					// ֡����ţ���֡Ϊ0����֡Ϊ��ţ����һ֡255��
					int iseq = UnPackageMessage.getISEQ(b);

					if (iseq == 0) {
						TransmitData[] datas = new TransmitData[] { data };
						fireDataReceived(datas);
						fireRequestHandlers(datas, true);
					} else {
						// ����֡��ˮ�ţ��ɷ��ͷ�ѭ������
						int fseq = UnPackageMessage.getFSEQ(b);
						// ��ȡ�������
						int msta = UnPackageMessage.getMSTA(b);
						TimerTask task = null;
						final String key = fseq + tempKeySep + msta;
						if (iseq == 1) { // ��һ֡ʱ������ʱ��ŵغͶ�ʱ�����
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
							logger.log(UMSTransmitLogger.DEBUG, "�յ��������ݰ�");
						}
						// ���ж�֡����
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
							// �Ѿ���ȡ�����һ֡����ȡ����ʱ����
							if (task != null)
								task.cancel();
						}
					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			logger.log(UMSTransmitLogger.INFO, toStringDesc()
					+ " �������ݴ���Processor�߳��˳�");
		}

		/**
		 * �����ն���ʱЧ�ԣ���ʱ����Ҫ��������
		 * 
		 * @param key
		 */
		public void checkValidation(String key, int totalFrameSize) {
			TransmitData[] all = (TransmitData[]) tempMap.get(key);
			if (all != null && all[totalFrameSize - 1] == null) {
				logger.log(UMSTransmitLogger.INFO, "�Ƴ���Ч���ݣ�key:" + key);
				tempMap.remove(key);
				fireMultipleDataReceivedFailed(all);
			}
		}
	}

	/**
	 * ���ݽ����߳�
	 * 
	 * @author Qil.Wong
	 * 
	 */
	private class SocketIn implements Runnable {

		public void run() {

			// int index = 0;// ��ǰ��ȡ���ֽ����
			// int dataLength = 0;// ���ݶ��ֽڵĳ���
			//
			// // ͷ11���ֽڵ�����
			// byte[] firstHead = new byte[GlobalConstant.INDEX_DATA];
			//
			// // ��������֡������
			// byte[] oneFrame = null;
			// // ֡��ʼ
			// boolean frameStart = false;
			//
			// TransmitData transmitData = null;
			// byte[] buff = new byte[4096];

			int sumLen = 0;// ��ʼ����
			int len = 0;
			int headIndex = GlobalConstant.INDEX_DATA;
			int endIndex = 2;
			byte[] heads = new byte[headIndex];// ͷ12���ֽڵ�����
			byte[] ends = new byte[endIndex];// У���롢������
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
						// ����ǰʮ�����ֽ�
						while ((len = in
								.read(heads, sumLen, headIndex - sumLen)) > 0
								&& sumLen <= headIndex) {
							sumLen += len;
						}
						// ���ݳ���λ��������ݱ䳤
						int height = (heads[GlobalConstant.INDEX_DATA_LENGTH + 1] + 256) % 256;
						int low = (heads[GlobalConstant.INDEX_DATA_LENGTH] + 256) % 256;
						int dataLength = height * 256 + low;
						byte[] oneFrame = new byte[headIndex + dataLength
													+ endIndex];// ���յ�������һ֡����
						byte[] data = new byte[dataLength];

						// ����ҵ������
						sumLen = 0;
						while ((len = in
								.read(data, sumLen, dataLength - sumLen)) > 0
								&& sumLen <= dataLength) {
							sumLen += len;
						}

						// ����У���롢������
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
						System.out.println("����oneframe��ʱ"
								+ (System.currentTimeMillis() - current));
						TransmitData transmitData = new TransmitData(oneFrame,
								subServerType);
						transmitData.setReceivedTime(getCurrentTimeStr());
						// ������յ�����
						parseFunctionCode(transmitData);

					}
				} catch (IOException e) {
					// һ����IOException�����ӶϿ�
					if (e instanceof EOFException == false) {
						logger.log(UMSTransmitLogger.ERROR, e.getClass()
								.getName()
								+ "  " + e.getMessage());
					}

					disconnect(reconnectableOnException);
					break;
				}

			}
			logger.log(UMSTransmitLogger.INFO, toStringDesc() + " �����߳��˳�");
		}

	}

	/**
	 * �Խ��յ�����Ϣ���д���
	 * 
	 * @param inData
	 */
	private void parseFunctionCode(TransmitData inData) {
		ControlCode control = new ControlCode(inData.getOneFrameData());

		switch (control.getFunctionCode()) {
		case ControlCode.FUNCTION_CURRENT_DATA:
			break;
		case ControlCode.FUNCTION_HEARTBEAT:
			logger.log(UMSTransmitLogger.DEBUG, "�յ�����" + toStringDesc());
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
	 * ���ݷ����߳�
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
						// Ϊ���ⳬ��֡�������write���������ɶ�ݷ���
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
					// ����ֻ����close����������disconnect�������������������disconnect����socketinһ���ط���
					close();
					break;
				}
			}
			logger.log(UMSTransmitLogger.INFO, toStringDesc() + " �����߳��˳�");
		}
	}

	/**
	 * ��ȡ��־��¼��
	 * 
	 * @return
	 */
	public UMSTransmitLogger getLogger() {
		return logger;
	}

	/**
	 * ������־��¼��
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
