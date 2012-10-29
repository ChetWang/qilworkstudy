package com.nci.ums.util;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * UMS�̳߳�
 * 
 * @author Qil.Wong
 * 
 */
public class UMSExecutorPool {

	private static UMSExecutorPool pool = null;

	ThreadPoolExecutor threadPool = new ThreadPoolExecutor(300,
			Integer.MAX_VALUE, 5, TimeUnit.SECONDS, new SynchronousQueue(),
			// new LinkedBlockingQueue(),
			new ThreadPoolExecutor.DiscardOldestPolicy());

	private UMSExecutorPool() {
	}

	public synchronized static UMSExecutorPool getInstance() {
		if (pool == null) {
			pool = new UMSExecutorPool();
		}
		return pool;
	}

	public synchronized void enqueue(Runnable run) {
		threadPool.execute(run);
	}

}