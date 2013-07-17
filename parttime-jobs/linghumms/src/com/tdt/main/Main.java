package com.tdt.main;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.tdt.unicom.domains.Submit;
import com.tdt.unicom.sgip.svr.UnicomSPMonitor;
import com.tdt.unicom.spsvr.ClientUnicomMonitor;

/**
 * @project UNICOM
 * @author sunnylocus
 * @vresion 1.0 2009-8-15
 * @description  系统启动类
 */
public class Main  {
	private final static Map<String, Submit> MT_SENDED_MAP = Collections.synchronizedMap(new HashMap<String,Submit>()); //保存已发送的MT请求，当收到report命令时再删除
	
	public void lauchSystem()  {
		 new Thread(new launchUnicomSPMonitor()).start();
		 new Thread(new launchClientReqMonitor()).start();
	}
	public static void main(String[] args) {
		Main main = new Main();
		main.lauchSystem();
	}
	
	static class launchClientReqMonitor implements Runnable {
		public void run() {
			 new ClientUnicomMonitor(MT_SENDED_MAP).recsvr();
		}
	}
	static class launchUnicomSPMonitor implements Runnable {
		public void run() {
			 try {
				new UnicomSPMonitor(MT_SENDED_MAP).startSvr();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
