package com.nci.ums.jmx.client;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.management.AttributeChangeNotification;
import javax.management.Notification;
import javax.management.NotificationListener;

import com.nci.ums.jmx.LogInfo;
import com.nci.ums.jmx.desktop.DesktopConsole;
import com.nci.ums.jmx.desktop.JMXLogger;
import com.nci.ums.jmx.desktop.panels.ChannelMonitorPanel.ChartShowThread;

public class ClientListener implements NotificationListener {

	public static final String UMS_NOTIFICATION_MESSAGE = "UMS_NOTIF";
	public static final int ATTRNAME_SERVER_START = 1;
	public static final int ATTRNAME_SERVER_STARTING = 2;
	public static final int ATTRNAME_SERVER_STOP = 3;
	public static final int ATTRNAME_SERVER_STOPPING = 4;
	public static final int ATTRNAME_LOG = 5;
	public static final int ATTRNAME_MONITOR = 6;
	public static final int ATTRNAME_MEDIA = 7;

	public void handleNotification(Notification notification, Object handback) {
		if (notification instanceof AttributeChangeNotification) {
			AttributeChangeNotification notif = (AttributeChangeNotification) notification;
			String attrName = notif.getAttributeName();
			if (attrName != null) {
				try {
					int n = Integer.parseInt(attrName);
					switch (n) {
					case ATTRNAME_SERVER_START:
						handleServerStart(notif);
						break;
					case ATTRNAME_SERVER_STARTING:
						handleServerStarting();
						break;
					case ATTRNAME_SERVER_STOP:
						handleServerStop(notif);
						break;
					case ATTRNAME_SERVER_STOPPING:
						handleServerStopping();
						break;
					case ATTRNAME_LOG:
						handleLog(notif);
						break;
					case ATTRNAME_MONITOR:
						handleMonitor(notif);
						break;
					case ATTRNAME_MEDIA:
						handleMedia(notif);
						break;
					default:
						break;
					}
				} catch (NumberFormatException e) {
					JMXLogger.log(JMXLogger.DEBUG, "收到非数字信号属性响应");
				}
			}
		}
	}

	private void handleMedia(AttributeChangeNotification notif) {
		List channels = (List) notif.getNewValue();
		DesktopConsole.getInstance().getChannelPanel().refresh(channels);
	}

	private void handleMonitor(AttributeChangeNotification notif) {
		Map monitorInfos = (Map) notif.getNewValue();
		Iterator it = monitorInfos.keySet().iterator();
		while (it.hasNext()) {
			String mediaID = (String) it.next();
			int count = ((Integer) monitorInfos.get(mediaID)).intValue();
			ChartShowThread chartShow = (ChartShowThread) DesktopConsole
					.getInstance().getMonitorPanel().getShowChartThreadMap()
					.get(mediaID);
			if (chartShow != null) {
				chartShow.showChart(count);
			}
		}
	}

	/**
	 * 处理远程日志
	 * 
	 * @param notif
	 */
	private void handleLog(AttributeChangeNotification notif) {
		LogInfo logInfo = (LogInfo) notif.getNewValue();
		JMXLogger.log(logInfo.getLevel(), logInfo.getInfo());
	}

	/**
	 * 处理服务器启动
	 * 
	 * @param notif
	 */
	private void handleServerStart(AttributeChangeNotification notif) {
		boolean serverStarted = ((Boolean) notif.getNewValue()).booleanValue();
		if (serverStarted) {
			DesktopConsole.getInstance().resetUIStarted();
			// DesktopConsole.getInstance().getMonitorPanel().initActiveChannels();
			JMXLogger.log(JMXLogger.INFO, "UMS 服务器启动完成");
		} else {
			DesktopConsole.getInstance().resetUIUnStarted();
			JMXLogger.log(JMXLogger.INFO, "UMS 服务器启动失败，详细情况请查看日志");
		}
	}

	public void handleServerStarting() {
		DesktopConsole.getInstance().resetUIStarting();
	}

	public void handleServerStopping() {
		DesktopConsole.getInstance().resetUIStopping();
	}

	/**
	 * 处理服务器停止
	 * 
	 * @param notif
	 */
	private void handleServerStop(AttributeChangeNotification notif) {
		boolean serverStoped = ((Boolean) notif.getNewValue()).booleanValue();
		if (!serverStoped) {
			DesktopConsole.getInstance().resetUIStarted();
		} else {
			DesktopConsole.getInstance().resetUIUnStarted();
		}
	}
}
