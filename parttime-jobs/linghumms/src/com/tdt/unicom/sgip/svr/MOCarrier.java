package com.tdt.unicom.sgip.svr;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @author sunnylocus
 * 负责转发上行短信至业务处理层
 * 负责将因为参数配置错误不能下行的短信送回原发送处
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
	
	//尝试与业务层IP建立连接
	

	class Woker implements Runnable {

		public void run() {
			
		}
		
	}
}
