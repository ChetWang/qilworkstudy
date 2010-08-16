package com.nci.ums.transmit.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.nci.ums.transmit.common.SocketCallBackRunnable;
import com.nci.ums.transmit.common.TransmitData;
import com.nci.ums.transmit.common.UMSTransmitException;
import com.nci.ums.transmit.common.message.CommonMessage;
import com.nci.ums.transmit.common.message.ControlCode;
import com.nci.ums.transmit.common.message.GlobalConstant;
import com.nci.ums.transmit.common.message.UnPackageMessage;
import com.nci.ums.util.Res;
import com.nci.ums.util.Util;

public class ServerTransRunnable extends SocketCallBackRunnable {

	private DataInputStream in; // input stream
	private DataOutputStream out; // output stream
	// 连接超时
	private static int conn_timeout = 60;// seconds

	private boolean connected = true;

	/**
	 * 连入的ip
	 */
	private String ip;

	/**
	 * 连入的端口
	 */
	private int port;

	/**
	 * 接入的逻辑地址
	 */
	private int address = -1;

	/**
	 * 转发子服务
	 */
	private SubTransmitServer subServer;

	/**
	 * 待转发数据集合
	 */
	// private ConcurrentLinkedQueue outDataQueue = new ConcurrentLinkedQueue();
	private BlockingQueue outDataQueue = new LinkedBlockingQueue();

	/**
	 * 数据发送线程
	 */
	private OutTransRunnable outRunnable = new OutTransRunnable();

	private Thread outThread = new Thread(outRunnable);

	private Thread transThread = new Thread(this);

	private boolean stopped = false;

	// 心跳帧
	protected static TransmitData heartBeat = new TransmitData(CommonMessage
			.getServerHeartBeatPackage(), 1);

	/**
	 * 数据接收线程
	 * 
	 * @param socket
	 * @param subServer
	 */
	public ServerTransRunnable(Socket socket, SubTransmitServer subServer) {
		super(socket);
		this.subServer = subServer;
		ip = socket.getInetAddress().getHostAddress();
		port = socket.getLocalPort();

		try {
			socket.setSoTimeout(conn_timeout * 1000);
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			Res.logExceptionTrace(e);
		}

		outThread.setDaemon(true);
		outThread.setName(toStringDesc() + " ServerTransRunnable Out");
		// UMSExecutorPool.getInstance().enqueue(outRunnable);
		transThread.setName(toStringDesc() + " ServerTransRunnable");
	}

	/**
	 * 应用或终端接入登录，登录帧在一个连接中只能使用一次，后续登录将无效
	 * 
	 * @param oneFrame
	 */
	private void login(TransmitData transData) {
		ControlCode control = new ControlCode(transData.getOneFrameData());
		if (control.isToTerminal()) {
			address = UnPackageMessage.getManufactureAddress(transData
					.getOneFrameData());
		} else {
			address = UnPackageMessage.getConsumerAddress(transData
					.getOneFrameData());
		}
		if (subServer.getConnectedThreads().containsKey(new Integer(address))) {
			address = -1;
			Res.log(Res.DEBUG, "重复地址" + address + "登录！");
			sendError(GlobalConstant.ERR_ADDRESS_ALREAY_LOGIN, transData);
		} else {
			subServer.getConnectedThreads().put(new Integer(address), this);
			Res.log(Res.DEBUG, address + "@" + ip + ":" + port + "已登录！");
		}
		outThread.setName(toStringDesc() + " ServerTransRunnable Out");
		transThread.setName(toStringDesc() + " ServerTransRunnable");
	}

	public void start() {

		transThread.start();
		outThread.start();
	}

	public String toStringDesc() {
		return ip + ":" + port + "[" + address + "]";
	}

	protected void excute() {
		Res.log(Res.INFO, "数据转发连接建立，准备数据转发.IP:" + ip + "，port:" + port);
		int sumLen = 0;// 初始长度
		int len = 0;
		int headIndex = GlobalConstant.INDEX_DATA;
		int endIndex = 2;
		byte[] heads = new byte[headIndex];// 头12个字节的数据
		byte[] ends = new byte[endIndex];// 校验码、结束码
		boolean frameStart = false;

		while (connected) {
			try {
				if (!frameStart) {
					sumLen = 0;
					byte readByte = in.readByte();
					
					if (readByte == GlobalConstant.START_CHARACTER) {
						frameStart = true;
						heads[0] = GlobalConstant.START_CHARACTER;
						sumLen += 1;
					}
				} else {
					// 接收前十三个字节
					while ((len = in.read(heads, sumLen, headIndex - sumLen)) > 0
							&& sumLen <= headIndex) {
						sumLen += len;
					}
					// 数据长度位计算出数据变长
					int height = (heads[GlobalConstant.INDEX_DATA_LENGTH + 1] + 256) % 256;
					int low = (heads[GlobalConstant.INDEX_DATA_LENGTH] + 256) % 256;
					int dataLength = height * 256 + low;
					byte[] data = new byte[dataLength];

					// 接收业务数据
					sumLen = 0;
					while ((len = in.read(data, sumLen, dataLength - sumLen)) > 0
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
					byte[] oneFrame = new byte[headIndex + dataLength
							+ endIndex];// 接收到的完整一帧数据
					System.arraycopy(heads, 0, oneFrame, 0, headIndex);
					System.arraycopy(data, 0, oneFrame, headIndex, dataLength);
					System.arraycopy(ends, 0, oneFrame, headIndex + dataLength,
							endIndex);
					
					TransmitData transmitData = new TransmitData(oneFrame,
							subServer.getServerType());
					transmitData.setReceivedTime(Util.getCurrentTimeStr());
					// 处理接收的内容
					parseFunctionCode(transmitData);

				}
			} catch (IOException e) {
				// 一旦有IOException，连接断开
				if (e instanceof EOFException == false) {
					e.printStackTrace();
				}
				stop();
			}

		}
	}

	protected void onException(Exception e) {
		e.printStackTrace();
		Res.logExceptionTrace(e);
	}

	protected void done() {
		Res.log(Res.INFO, toStringDesc() + "接收线程退出");
		stop();
	}

	/**
	 * 停止当前连接，并从连接队列中删除
	 */
	public void stop() {
		if (!stopped) {
			try {
				enqueueDataToTransmit(TransmitData.QUIT_SIGNAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (UMSTransmitException e) {
				Res.logExceptionTrace(e);
			}
			// synchronized (outLock) {
			// outLock.notify();
			// }
			stopped = true;
			try {
				in.close();
			} catch (Exception e) {
			}
			try {
				out.close();
			} catch (Exception e) {
			}
			try {
				socket.close();
			} catch (Exception e) {
			}
			subServer.getConnectedThreads().remove(new Integer(address));
			connected = false;

			Res.log(Res.INFO, "转发服务连接断开:" + toStringDesc());
		}
	}

	/**
	 * 功能码解析并处理
	 * 
	 * @param control
	 * @param oneFrame
	 */
	private void parseFunctionCode(TransmitData transData) {
		ControlCode control = new ControlCode(transData.getOneFrameData());
		if (control.getFunctionCode() == ControlCode.FUNCTION_LOGGING_IN) {
			login(transData);
		} else {
			if (isLogin()) {
				switch (control.getFunctionCode()) {
				case ControlCode.FUNCTION_CURRENT_DATA:
					break;
				case ControlCode.FUNCTION_HEARTBEAT:
					break;
				case ControlCode.FUNCTION_LOGGING_IN:
					break;
				case ControlCode.FUNCTION_LOGGING_OUT:
					stop();
					break;
				case ControlCode.FUNCTION_RELAY:
					break;
				case ControlCode.FUNCTION_TASK_DATA:
					break;
				case ControlCode.FUNCTION_USER_DEFINED:
					transmitUserData(transData);
					break;
				case ControlCode.FUNCTION_WARNING:
					break;
				case ControlCode.FUNCTION_WARNING_DONFIRM:
					break;
				default:
					break;
				}
			} else {
				sendError(GlobalConstant.ERR_PASSWORD, transData);
			}
		}
	}

	/**
	 * 业务数据转发
	 * 
	 * @param oneFrame
	 *            一帧的数据
	 */
	private void transmitUserData(TransmitData transData) {

		Map coreatedThreads = subServer.getCoralateSubServer()
				.getConnectedThreads();
		ServerTransRunnable runnable = (ServerTransRunnable) coreatedThreads
				.get(new Integer(transData.getDestinationAddress()));
		if (runnable != null) {
			// 将数据转移给另一个与其对应的TransRunnable对象进行处理
			try {
				runnable.enqueueDataToTransmit(transData);
			} catch (InterruptedException e) {
				Res.logExceptionTrace(e);
			} catch (UMSTransmitException e) {
				Res.logExceptionTrace(e);
			}

		} else {
			Res.log(Res.ERROR, "与" + transData.getSourceAddress() + "对应的目的地址"
					+ transData.getDestinationAddress() + "不存在或已断开连接！");
			sendError(GlobalConstant.ERR_TARGET, transData);
		}
	}

	/**
	 * 是否已登陆
	 * 
	 * @return
	 */
	private boolean isLogin() {
		return address != -1;
	}

	/**
	 * 发送异常报文
	 * 
	 * @param errorType
	 *            异常类型
	 * @param oneFrame
	 *            源帧
	 */
	private void sendError(int errorType, TransmitData transErrorData) {
		TransmitData data = new TransmitData(CommonMessage.getReturnErrMessage(
				transErrorData.getOneFrameData(), (byte) errorType),
				transErrorData.getSubServerType());
		data.setUid(transErrorData.getUid());
		data.setSuccess(false);
		data.setReceivedTime(transErrorData.getReceivedTime());
		data.setErrorType(errorType);
		try {
			enqueueDataToTransmit(data);
		} catch (InterruptedException e) {
			Res.logExceptionTrace(e);
		} catch (UMSTransmitException e) {
			Res.logExceptionTrace(e);
		}
	}

	/**
	 * 发送数据，这里只需将数据压送给队列，由其它线程负责发送
	 * 
	 * @param oneFrameData
	 * @return
	 */
	public void enqueueDataToTransmit(TransmitData transData)
			throws InterruptedException, UMSTransmitException {
		if (connected) {
			outDataQueue.put(transData);
		} else {
			throw new UMSTransmitException();
		}
	}

	/**
	 * 给连接的客户端发送心跳
	 * 
	 * @throws InterruptedException
	 * @throws UMSTransmitException
	 */
	public void heartBeat() throws InterruptedException, UMSTransmitException {
		if (connected) {
			if (outDataQueue.size() == 0)
				enqueueDataToTransmit(heartBeat);
		} else {
			throw new UMSTransmitException();
		}
	}

	// private Object outLock = new Object();

	private class OutTransRunnable implements Runnable {

		// 缓冲发送区单块大小
		private int buffLength = 4096;

		private boolean quit = false;

		public void run() {
			while (connected && !quit) {

				try {
					TransmitData data = (TransmitData) outDataQueue.take();
					if (data == TransmitData.QUIT_SIGNAL) {
						quit = true;
						continue;
					}
					try {
						data.setTransmitTime(Util.getCurrentTimeStr());

						byte[] b = data.getOneFrameData();
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
//						Res.log(Res.DEBUG, "ServerTransRunnable"
//								+ toStringDesc() + "发送数据:"
//								+ data.getOneFrameData().length + "bytes");
					} catch (IOException e) {
						data.setSuccess(false);
						data.setErrorType(GlobalConstant.ERR_SENT);
						Res.logExceptionTrace(e);
						stop();
						break;
					} finally {
						// 存储数据,只存用户转发数据
						ControlCode control = new ControlCode(data
								.getOneFrameData());
						if (control.getFunctionCode() == ControlCode.FUNCTION_USER_DEFINED) {
							subServer.getTransServer().getDataStoreManager()
									.enqueDataToStore(data);
						}
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			Res.log(Res.INFO, toStringDesc() + "发送线程退出");
		}
	}

}
