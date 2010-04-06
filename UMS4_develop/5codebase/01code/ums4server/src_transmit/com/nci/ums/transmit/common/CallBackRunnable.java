package com.nci.ums.transmit.common;

public abstract class CallBackRunnable implements Runnable {

	public void run() {
		try {
			excute();
		} catch (Exception e) {			
			onException(e);
		} finally {
			done();
		}
	}

	/**
	 * ����excuteִ�гɹ�����ʧ�ܣ����������ִ�еķ���
	 */
	protected abstract void done();

	/**
	 * �߳�����ִ�з���
	 */
	protected abstract void excute();

	/**
	 * �����쳣ʱ�Ĳ���
	 * @param e
	 */
	protected abstract void onException(Exception e);

}
