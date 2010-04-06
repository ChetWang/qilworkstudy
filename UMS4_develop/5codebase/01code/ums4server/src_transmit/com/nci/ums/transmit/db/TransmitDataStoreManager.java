package com.nci.ums.transmit.db;

import com.nci.ums.transmit.common.TransmitData;

import edu.emory.mathcs.backport.java.util.concurrent.LinkedBlockingQueue;
import edu.emory.mathcs.backport.java.util.concurrent.ThreadPoolExecutor;
import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;

public class TransmitDataStoreManager {

	/**
	 * ר�Ÿ����ݴ洢ʹ�õ��̳߳أ�Ϊ��ֹ��������ͬʱ�洢������ֻ�������5���߳�ͬʱ�洢
	 */
	private ThreadPoolExecutor pool = null;

	/**
	 * ���ݿ⽻�����������洢����
	 */
	private RecordMessage record = null;

	public TransmitDataStoreManager() {

	}

	/**
	 * ������ת�����洢���������д洢
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
	 * ֹͣ
	 */
	public void stopModule() {
		record = null;
		pool.shutdown();
	}

	/**
	 * ���ݴ����̣߳�������poolִ��
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
