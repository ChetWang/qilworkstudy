package com.tdt.unicom.sgip.svr;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @author sunnylocus
 * ����ת�����ж�����ҵ�����
 * ������Ϊ�������ô��������еĶ����ͻ�ԭ���ʹ�
 */
public class MOCarrier {
	private ExecutorService exec = Executors.newCachedThreadPool(new ThreadFactory() {
		int threadNo = 0;
		public Thread newThread(Runnable task) {
			threadNo ++;
			Thread thread = new Thread(task);
			thread.setName("MO-Carrier-"+threadNo);
			return thread;
		}
	});
	
	//������ҵ���IP��������
	

	class Woker implements Runnable {

		public void run() {
			
		}
		
	}
}
