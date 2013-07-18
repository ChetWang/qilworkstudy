package org.vlg.linghu.sms.zte.client;

import java.util.Date;

import org.apache.log4j.Logger;

public class TestMulThread {
	private static Logger log = Logger.getLogger(TestThread.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Date date1 = new Date();
		Thread t1 = new Thread(new TestThread("wzh001c", "wzh001c"));
		t1.start();
		Thread t2 = new Thread(new TestThread("wzh002c", "wzh002c"));
		t2.start();
		Thread t3 = new Thread(new TestThread("wzh003c", "wzh003c"));
		t3.start();

		log.info("start time="+ date1.toString());
		log.info("end time="+ (new Date()).toString());
	}

}
