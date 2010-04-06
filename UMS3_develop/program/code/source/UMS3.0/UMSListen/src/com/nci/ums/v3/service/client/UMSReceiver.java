package com.nci.ums.v3.service.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.Timer;


public abstract class UMSReceiver {

	protected static String connectionTestSignal = "UMSCONNTEST";

	protected static String connectionTestReturn = "CONN_SUCESS";
	
	protected static String HEARTSIG = "1982";
	protected static String DUPLICATE = "duplicate";

	//以web service方式监听时的属性
	protected String receiveWSDL;
	//以socket方式监听时的属性
	protected String serverIP;
	protected int serverPort;
	
	protected String appID;
	protected String appPsw;
	protected String serviceID;
	protected UMSMsgSource source;
	protected String propsFile;
	protected Timer timer;
	protected int reconnectInterval;
	protected int heartBeatInterval;
	protected int heartBeatCounts;
	protected int timeOut;
	protected Timer clientToServerHeartTimer;
	
	public UMSReceiver(String propFile) throws IOException{
		this.propsFile = propFile;
		init();
	}
	
	protected void init() throws IOException{
		InputStream ins = ClassLoader.getSystemResourceAsStream(propsFile);
		Properties props = new Properties();
		props.load(ins);
		appID = props.getProperty("appID");
		appPsw = props.getProperty("appPsw");
		serviceID = props.getProperty("serviceID");
		receiveWSDL = props.getProperty("receiveWSDL");
		serverIP = props.getProperty("serverIP");
		serverPort = Integer.parseInt(props.getProperty("serverPort"));
		reconnectInterval = Integer.parseInt(props
				.getProperty("reconnectInterval"));
		timeOut = Integer.parseInt(props.getProperty("timeOut"));
//		heartBeatInterval = Integer.parseInt(props.getProperty("heatBeatInterval"));
//		heartBeatCounts = Integer.parseInt(props.getProperty("heatBeatCounts"));
		ins.close();
		source = new UMSMsgSource();
		source.addReceiveListener(new ReceiveListener() {
			public void onUMSMessage(ReceiveEvent evt) {
				onUMSMsg(evt);
			}
		});		
		timer = new Timer(timeOut * 1000, new TimerListener());

	}
	
	public abstract void onUMSMsg(ReceiveEvent evt);
	
	
	public abstract void startListening() throws Exception;	
	
	class TimerListener implements java.awt.event.ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println("与UMS消息接收Web服务交互超时." + reconnectInterval
					+ "秒后重连。");
			try {
				init();
				Thread.sleep(reconnectInterval * 1000);
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e2) {
				e2.printStackTrace();
			}
		}
	}
}
