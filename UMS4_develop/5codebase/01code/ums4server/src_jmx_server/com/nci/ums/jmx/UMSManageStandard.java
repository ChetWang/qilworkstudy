package com.nci.ums.jmx;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.management.AttributeChangeNotification;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

import com.nci.ums.axis2.UMSAxisServer;
import com.nci.ums.channel.ChannelIfc;
import com.nci.ums.channel.channelinfo.MediaInfo;
import com.nci.ums.channel.channelmanager.ChannelManager;
import com.nci.ums.jmx.client.ClientListener;
import com.nci.ums.jmx.server.ServerJMXHandler;
import com.nci.ums.jmx.server.UMSJMXServer;
import com.nci.ums.jmx.server.WSUtil;
import com.nci.ums.media.MediaBean;
import com.nci.ums.periphery.core.UMSServer_V3;
import com.nci.ums.util.AppServiceUtil;
import com.nci.ums.util.CMPPUtil_V3;
import com.nci.ums.util.ClientWSUtil;
import com.nci.ums.util.DialectUtil;
import com.nci.ums.util.FeeUtil;
import com.nci.ums.util.LoginUtil_V3;
import com.nci.ums.util.PropertyUtil;
import com.nci.ums.util.Res;
import com.nci.ums.util.ReservedString;

public class UMSManageStandard extends NotificationBroadcasterSupport implements
		UMSManageStandardMBean {

	// ����������ǣ���������Ǿ�̬����������UMS MBean���ʵ����ͻ
	private static boolean serverStarted = false;

	private ServerJMXHandler handler = ServerJMXHandler.getInstance();

	
	
	public UMSManageStandard() {
		if (handler.getRegisteredMBean() == null) {
			handler.registerServerMBeanListener(this);
		}
	}
	
	public UMSManageStandard(int none){
		
	}

	public Properties getProperties(String resourceName) {

		return null;
	}

	public void saveProperties(String resourceName, Properties p) {
	}

	public void setXMLDocument(String xmlName, String doc) {
	}

	public void startMedia(MediaBean media) {
		WSUtil.startChannel(media);
	}

	public String getXMLDocument(String xmlName) {

		return null;
	}

	public void stopMedia(MediaBean media) {
		WSUtil.stopChannel(media);
	}

	public List getMediaList() {

		return UMSJMXServer.getChannels();
	}
	
	/**
	 * ���ؾ�̬�༰����̬�ౣ��Ļ�������
	 */
	private void reloadUtilities(){		
		DialectUtil.load();
		Res.load();	
		ReservedString.load();
		PropertyUtil.load();
		LoginUtil_V3.load();
		AppServiceUtil.load();
		ClientWSUtil.load();
		CMPPUtil_V3.load();
		FeeUtil.load();
//		JMSUtil.load();		
	}
	/**
	 * ����UMS������
	 */
	public boolean startUMS() {
		reloadUtilities();
		AttributeChangeNotification notif = new AttributeChangeNotification(
				this, 0, 0, ClientListener.UMS_NOTIFICATION_MESSAGE, String
						.valueOf(ClientListener.ATTRNAME_SERVER_STARTING),
				LogInfo.class.getName(), null, null);
		sendNotification(notif);
		if (serverStarted) {
			Res.log(Res.INFO, "UMS������������");
		} else {
			// ����UMS����
			UMSServer_V3 server = UMSServer_V3.getInstance();
			server.start();
			if (!server.isDbFlag() || !server.isSocketFlag()) {
				serverStarted = false;
			} else {
				// ����WebService ����
				UMSAxisServer wsServer = UMSAxisServer.getInstance();// ͬʱ������Axis2��������������,ִ��UMS3Server_Axis2LifeCycle��startUp
				if (wsServer != null) {
					serverStarted = true;
				} else {
					serverStarted = false;
				}
			}
		}
		if (serverStarted) {
			String port = "";
			try {
				// �õ�axis2 server�Ķ˿�
				port = ConsoleUtil.getAxis2PortVector(
						System.getProperty("user.dir") + "/conf/ums_axis2.xml")
						.get(1).toString();
				Thread.sleep(1000);// �ȴ��������������꣬�����û�����
				boolean flag = false;
				while (!flag) {
					Thread.sleep(1000);
					flag = checkChannelStarting();
				}
			} catch (Exception e) {
				Res.logExceptionTrace(e);
			}
			try {
				Res.log(Res.INFO, new StringBuffer().append(
						"UMS Web Service������ɣ��˿�Ϊ:").append(port).append(
						". ��ϸ������鿴��http://").append(
						InetAddress.getLocalHost().getHostAddress())
						.append(":").append(port).toString());
			} catch (UnknownHostException e) {
			}
		}
		AttributeChangeNotification notif2 = new AttributeChangeNotification(
				this, 0, 0, ClientListener.UMS_NOTIFICATION_MESSAGE, String
						.valueOf(ClientListener.ATTRNAME_SERVER_START),
				LogInfo.class.getName(), null, new Boolean(serverStarted));
		sendNotification(notif2);
		if(serverStarted){
			ServerJMXHandler.getInstance().sendMediaInfos();
		}
		System.gc();
		return serverStarted;
	}

	/**
	 * ��������߳�����״��
	 * @return
	 */
	private boolean checkChannelStarting() {
		boolean allStarted = true;
		try {
			Iterator it_out = ChannelManager.getOutMediaInfoHash().values()
					.iterator();
			Iterator it_in = ChannelManager.getInMediaInfoHash().values()
					.iterator();
			//In out������Ҫ���
			while (it_out.hasNext()) {
				MediaInfo media = (MediaInfo) it_out.next();
				ChannelIfc ifc = media.getChannelObject();
				if (ifc == null || ifc.isStartOnce() == false) {
					allStarted = false;
				}
			}
			while (it_in.hasNext()) {
				MediaInfo media = (MediaInfo) it_in.next();
				ChannelIfc ifc = media.getChannelObject();
				if (ifc == null || ifc.isStartOnce() == false) {
					allStarted = false;
				}
			}
		} catch (Exception e) {
			allStarted = false;
		}
		return allStarted;
	}

	/**
	 * UMSʵ���Ƿ��Ѿ�����
	 * 
	 * @return boolean true�Ѿ����У�false�Ѿ�ֹͣ
	 */
	public boolean isUMSStarted() {
		return serverStarted;
	}

	/**
	 * ֹͣUMS����
	 * 
	 * @return boolean true�Ѿ�ֹͣ��false��������
	 */
	public boolean stopUMS() {
		AttributeChangeNotification notif = new AttributeChangeNotification(
				this, 0, 0, ClientListener.UMS_NOTIFICATION_MESSAGE, String
						.valueOf(ClientListener.ATTRNAME_SERVER_STOPPING),
				LogInfo.class.getName(), null, null);
		sendNotification(notif);
		if (!serverStarted) {
			return !serverStarted;
		}
		UMSServer_V3.getInstance().pleaseStop();
		try {
			UMSAxisServer.getInstance().stop();
			serverStarted = false;
		} catch (Exception e) {
			e.printStackTrace();
			serverStarted = true;
		}
		Res.destroyDBPool();
		//����ֹ֪ͣͨ
		AttributeChangeNotification notif2 = new AttributeChangeNotification(
				this, 0, 0, ClientListener.UMS_NOTIFICATION_MESSAGE, String
						.valueOf(ClientListener.ATTRNAME_SERVER_STOP),
				LogInfo.class.getName(), null, new Boolean(!serverStarted));
		sendNotification(notif2);
		System.gc();
		return !serverStarted;
	}

	/**
	 * �����ӵ�MBean��Դ������־��¼
	 * 
	 * @param info
	 */
	public synchronized void sendLog(LogInfo info) {
		AttributeChangeNotification notif = new AttributeChangeNotification(
				this, 0, 0, ClientListener.UMS_NOTIFICATION_MESSAGE, String
						.valueOf(ClientListener.ATTRNAME_LOG), LogInfo.class
						.getName(), null, info);
		sendNotification(notif);
	}

	/**
	 * ����������Ϣ
	 */
	public synchronized void sendMediaInfos(List mediaBeans) {
		AttributeChangeNotification notif = new AttributeChangeNotification(
				this, 0, 0, ClientListener.UMS_NOTIFICATION_MESSAGE, String
						.valueOf(ClientListener.ATTRNAME_MEDIA), "java.util.List", null, mediaBeans);
		sendNotification(notif);
	}
	
	/**
	 * ���ͼ����Ϣ
	 */
	public void sendMonitorInfos(Map monitorInfos) {
		AttributeChangeNotification notif = new AttributeChangeNotification(
				this, 0, 0, ClientListener.UMS_NOTIFICATION_MESSAGE, String
						.valueOf(ClientListener.ATTRNAME_MONITOR), "java.util.Map", null, monitorInfos);
		sendNotification(notif);
	}
	
	public void sendNotification(Notification notif){
		super.sendNotification(notif);
	}
}
