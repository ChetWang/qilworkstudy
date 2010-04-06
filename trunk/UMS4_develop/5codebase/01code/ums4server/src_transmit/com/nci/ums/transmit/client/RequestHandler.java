package com.nci.ums.transmit.client;

import java.util.TimerTask;

import com.nci.ums.transmit.common.TransmitData;

/**
 * ����ͺ��������Ļص�����
 * 
 * @author Qil.Wong
 * 
 */
public abstract class RequestHandler {

	/**
	 * �ص���ʱʱ�䣬����
	 */
	private long timeoutMill;

	/**
	 * ��ʱ��������
	 */
	private TimerTask timeoutTask;

	/**
	 * ���ӿͻ���
	 */
	private TransmitClient client;

	/**
	 * ֡���
	 */
	private int fseq;

	private int userCommand;

	/**
	 * Ĭ�ϳ�ʱʱ�䣬1����
	 */
	public static final long DEFAULT_TIMEOUT = 60 * 1000;

	/**
	 * ���캯����Ĭ����Чʱ��
	 * 
	 * @see RequestHandler.DEFAULT_TIMEOUT
	 */
	public RequestHandler(TransmitClient client, int userCommand) {
		timeoutMill = DEFAULT_TIMEOUT;
		this.client = client;
		this.userCommand = userCommand;
	}

	/**
	 * ���캯��
	 * 
	 * @param timeout_mil
	 *            request��Чʱ��
	 */
	public RequestHandler(TransmitClient client, int userCommand,
			int timeout_mil) {
		this.timeoutMill = timeout_mil;
		this.client = client;
		this.userCommand = userCommand;
	}

	/**
	 * ��ȡ��ʱ����
	 * 
	 * @return
	 */
	public TimerTask getTimeoutTask() {
		return timeoutTask;
	}

	/**
	 * ��ȡ��ʱʱ��
	 * 
	 * @return
	 */
	public long getTimeOut() {
		return timeoutMill;
	}

	/**
	 * ��ȡ�û�����
	 * 
	 * @return
	 */
	public int getUserCommand() {
		return userCommand;
	}

	/**
	 * ����ִ�к�õ�����
	 * 
	 * @param data
	 *            ��������
	 * @param ��ǰ����֡���
	 */
	public abstract void requestFinished(TransmitData[] data, int fseq);

	/**
	 * ����ִ��ʧ�ܣ���ʱ��δ�õ�����
	 */
	public abstract void requestFailedTimeout();

	/**
	 * ��ȡ��ǰ����֡�����
	 * 
	 * @return
	 */
	public int getFseq() {
		return fseq;
	}

	/**
	 * ������ǰ����֡�����
	 * 
	 * @param int ��ǰ����֡�����
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
