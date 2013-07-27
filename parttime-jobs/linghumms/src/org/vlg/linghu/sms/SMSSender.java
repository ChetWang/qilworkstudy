package org.vlg.linghu.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SMSSender extends Thread{
	
	private static final Logger logger = LoggerFactory.getLogger(SMSSender.class);

	public SMSSender(){
		setDaemon(true);
		setName("SMS-Sender");
	}
	
	public void run(){
		logger.info("SMS Sender started");
	}
	

}
