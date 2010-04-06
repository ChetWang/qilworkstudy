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
	 * 不管excute执行成功还是失败，都必须最后执行的方法
	 */
	protected abstract void done();

	/**
	 * 线程任务执行方法
	 */
	protected abstract void excute();

	/**
	 * 产生异常时的操作
	 * @param e
	 */
	protected abstract void onException(Exception e);

}
