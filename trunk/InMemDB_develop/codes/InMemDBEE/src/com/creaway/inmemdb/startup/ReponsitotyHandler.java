/*
 * @(#)ReponsitotyHandler.java	1.0  08/24/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.startup;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 配置文件监听器
 * @author Qil.Wong
 *
 */
public class ReponsitotyHandler {
	
	public ReponsitotyHandler(){
		//监听触发器
		t.scheduleAtFixedRate(new TriggerTask(), 1000, 5000);
		
	}
	
    Timer t = new Timer("ReponsitotyHandler", true);
	
	public void startListening(){
		
	}

	
	
	/**
	 * 触发器监听任务
	 * @author Qil.Wong
	 *
	 */
	public class TriggerTask extends TimerTask{

		@Override
		public void run() {
			
		}
		
	}

}
