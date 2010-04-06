package com.nci.ums.jmx.server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.nci.ums.basic.UMSModule;
import com.nci.ums.channel.DefaultChannelIfcImpl;
import com.nci.ums.channel.channelinfo.MediaInfo;
import com.nci.ums.channel.channelmanager.ChannelManager;
import com.nci.ums.jmx.UMSManageStandard;

/**
 * 服务器端处理与JMX Desktop相关的事件
 * 
 * @author Qil.Wong
 * 
 */
public class ServerJMXHandler implements UMSModule {

	private UMSManageStandard mBean = null;

	private static ServerJMXHandler handler = new ServerJMXHandler();

	private Timer timer;

	private TimerTask mediaInfoTask;

	private TimerTask channelMonitorTask;

	private ServerJMXHandler() {

	}

	public void startModule() {
		timer = new Timer();
		mediaInfoTask = new TimerTask() {
			public void run() {
				sendMediaInfos();
			}
		};

		timer.schedule(mediaInfoTask, 2, 30 * 1000);
		channelMonitorTask = new TimerTask() {
			public void run() {
				sendMonitorCounts();
			}
		};

		timer.schedule(channelMonitorTask, 2, 2 * 1000);

	}

	public void stopModule() {
		if (mediaInfoTask != null) {
			mediaInfoTask.cancel();
		}
		if (channelMonitorTask != null) {
			channelMonitorTask.cancel();
		}
		timer.cancel();
		timer = null;
	}

	public synchronized static ServerJMXHandler getInstance() {
		if (handler == null) {
			handler = new ServerJMXHandler();
		}
		return handler;
	}

	/**
	 * 发送渠道状态
	 */
	public void sendMediaInfos() {
		if (mBean != null) {
			List channels = UMSJMXServer.getChannels();
			mBean.sendMediaInfos(channels);
		}
	}

	private void sendMonitorCounts() {
		Map map = new HashMap();
		Iterator it = ChannelManager.getOutMediaInfoHash().keySet().iterator();
		while (it.hasNext()) {
			String mediaID = (String) it.next();
			MediaInfo media = (MediaInfo) ChannelManager.getOutMediaInfoHash()
					.get(mediaID);
			map.put(mediaID, new Integer(((DefaultChannelIfcImpl) media
					.getChannelObject()).getProcessCountDualAndReset()));
		}
		if (mBean != null)
			mBean.sendMonitorInfos(map);
	}

	// protected void

	public UMSManageStandard getRegisteredMBean() {
		return mBean;
	}

	public void registerServerMBeanListener(UMSManageStandard mBean) {
		this.mBean = mBean;
	}

}
