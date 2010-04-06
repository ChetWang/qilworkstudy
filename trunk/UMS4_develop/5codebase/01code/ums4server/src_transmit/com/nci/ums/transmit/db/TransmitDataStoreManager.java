package com.nci.ums.transmit.db;

import com.nci.ums.transmit.common.TransmitData;

import edu.emory.mathcs.backport.java.util.concurrent.LinkedBlockingQueue;
import edu.emory.mathcs.backport.java.util.concurrent.ThreadPoolExecutor;
import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;

public class TransmitDataStoreManager {

	/**
	 * 专门给数据存储使用的线程池，为防止大量数据同时存储，这里只允许最大5个线程同时存储
	 */
	private ThreadPoolExecutor pool = null;

	/**
	 * 数据库交互对象，真正存储数据
	 */
	private RecordMessage record = null;

	public TransmitDataStoreManager() {

	}

	/**
	 * 将数据转交给存储管理器进行存储
	 * 
	 * @param data
	 */
	public void enqueDataToStore(TransmitData data) {
		pool.execute(new DataStoreRunnable(data));
	}
	
	public void startModule(){
		pool = new ThreadPoolExecutor(5, 5, 5,
				TimeUnit.SECONDS, new LinkedBlockingQueue(),
				new ThreadPoolExecutor.DiscardOldestPolicy());
		record = new RecordMessage();
	}

	/**
	 * 停止
	 */
	public void stopModule() {
		record = null;
		pool.shutdown();
	}

	/**
	 * 数据处理线程，交付给pool执行
	 * 
	 * @author Qil.Wong
	 * 
	 */
	private class DataStoreRunnable implements Runnable {

		private TransmitData data;

		public DataStoreRunnable(TransmitData data) {
			this.data = data;
		}

		public void run() {
			record.recordMessage(data);
		}
	}
}
