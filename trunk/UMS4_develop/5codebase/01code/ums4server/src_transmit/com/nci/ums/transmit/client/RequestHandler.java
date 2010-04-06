package com.nci.ums.transmit.client;

import java.util.TimerTask;

import com.nci.ums.transmit.common.TransmitData;

/**
 * 命令发送后结果反馈的回调对象
 * 
 * @author Qil.Wong
 * 
 */
public abstract class RequestHandler {

	/**
	 * 回调超时时间，毫秒
	 */
	private long timeoutMill;

	/**
	 * 超时处理任务
	 */
	private TimerTask timeoutTask;

	/**
	 * 连接客户端
	 */
	private TransmitClient client;

	/**
	 * 帧序号
	 */
	private int fseq;

	private int userCommand;

	/**
	 * 默认超时时间，1分钟
	 */
	public static final long DEFAULT_TIMEOUT = 60 * 1000;

	/**
	 * 构造函数，默认有效时间
	 * 
	 * @see RequestHandler.DEFAULT_TIMEOUT
	 */
	public RequestHandler(TransmitClient client, int userCommand) {
		timeoutMill = DEFAULT_TIMEOUT;
		this.client = client;
		this.userCommand = userCommand;
	}

	/**
	 * 构造函数
	 * 
	 * @param timeout_mil
	 *            request有效时间
	 */
	public RequestHandler(TransmitClient client, int userCommand,
			int timeout_mil) {
		this.timeoutMill = timeout_mil;
		this.client = client;
		this.userCommand = userCommand;
	}

	/**
	 * 获取超时任务
	 * 
	 * @return
	 */
	public TimerTask getTimeoutTask() {
		return timeoutTask;
	}

	/**
	 * 获取超时时间
	 * 
	 * @return
	 */
	public long getTimeOut() {
		return timeoutMill;
	}

	/**
	 * 获取用户命令
	 * 
	 * @return
	 */
	public int getUserCommand() {
		return userCommand;
	}

	/**
	 * 命令执行后得到反馈
	 * 
	 * @param data
	 *            反馈数据
	 * @param 先前命令帧序号
	 */
	public abstract void requestFinished(TransmitData[] data, int fseq);

	/**
	 * 命令执行失败，超时仍未得到反馈
	 */
	public abstract void requestFailedTimeout();

	/**
	 * 获取先前命令帧内序号
	 * 
	 * @return
	 */
	public int getFseq() {
		return fseq;
	}

	/**
	 * 设置先前命令帧内序号
	 * 
	 * @param int 先前命令帧内序号
	 */
	public void setFseq(final int fseq) {
		this.fseq = fseq;
		if (timeoutTask != null)
			timeoutTask.cancel();
		timeoutTask = new TimerTask() {
			public void run() {
				client.getRequestHandlers().remove(new Integer(fseq));
				requestFailedTimeout();
			}
		};
		client.getTimer().schedule(timeoutTask, timeoutMill);
	}

}
