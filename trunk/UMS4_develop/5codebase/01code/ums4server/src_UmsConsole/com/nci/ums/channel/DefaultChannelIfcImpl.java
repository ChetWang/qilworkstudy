package com.nci.ums.channel;

import com.nci.ums.channel.channelinfo.LockFlag;

public class DefaultChannelIfcImpl implements ChannelIfc {

	// 某时间段内处理的条数，方便监视
	private int msgProcessedCountDual;

	private byte[] lock = new byte[0];

	/**
	 * 增加在该时间段内处理消息条数
	 * 
	 * @param count
	 */
	public void addProcessCount(int count) {
		synchronized (lock) {
			msgProcessedCountDual = msgProcessedCountDual + count;
		}
	}

	/**
	 * 获取时间段内处理消息条数,并重置
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
