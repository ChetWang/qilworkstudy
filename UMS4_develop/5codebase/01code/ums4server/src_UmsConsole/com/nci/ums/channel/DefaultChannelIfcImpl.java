package com.nci.ums.channel;

import com.nci.ums.channel.channelinfo.LockFlag;

public class DefaultChannelIfcImpl implements ChannelIfc {

	// ĳʱ����ڴ�����������������
	private int msgProcessedCountDual;

	private byte[] lock = new byte[0];

	/**
	 * �����ڸ�ʱ����ڴ�����Ϣ����
	 * 
	 * @param count
	 */
	public void addProcessCount(int count) {
		synchronized (lock) {
			msgProcessedCountDual = msgProcessedCountDual + count;
		}
	}

	/**
	 * ��ȡʱ����ڴ�����Ϣ����,������
	 * 
	 * @return
	 */
	public int getProcessCountDualAndReset() {
		synchronized (lock) {
			int result = msgProcessedCountDual;
			msgProcessedCountDual = 0;
			return result;
		}

	}

	public boolean getThreadState() {
		return false;
	}

	public boolean isStartOnce() {
		return false;
	}

	public void start() {

	}

	public void stop() {

	}

	public LockFlag getDataLockFlag() {
		return null;
	}

}
