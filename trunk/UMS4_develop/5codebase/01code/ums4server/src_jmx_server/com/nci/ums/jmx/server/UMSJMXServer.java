package com.nci.ums.jmx.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import com.cmcc.mm7.vasp.common.ConcurrentHashMap;
import com.nci.ums.basic.UMSModule;
import com.nci.ums.channel.channelinfo.MediaInfo;
import com.nci.ums.channel.channelmanager.ChannelManager;
import com.nci.ums.jmx.UMSManageStandard;
import com.nci.ums.media.MediaBean;
import com.nci.ums.util.DynamicUMSStreamReader;
import com.nci.ums.util.Res;

/**
 * UMSԶ�̼�ص�JMX����Ҳ��UMS��������������
 * 
 * @author Qil.Wong
 * 
 */
public class UMSJMXServer implements UMSModule {

	// MBean�����ṩ��
	private MBeanServer mbs;

	// JMX�����ṩ��
	private JMXConnectorServer cs;

	// Ӧ�ó������ڵĸ�·��
	private static String appBinDir = System.getProperty("user.dir");

	public UMSJMXServer() {
		loadExternalClasspath();
	}

	/**
	 * ����ext classpath
	 */
	private void loadExternalClasspath() {
		File ext_dir = new File(appBinDir + "/lib/ext");
		File[] jars = ext_dir.listFiles();
		if (jars != null) {
			for (int i = 0; i < jars.length; i++) {
				if (jars[i].getName().lastIndexOf(".jar") >= 0) {
					ClassLoaderUtil.addExtClassPath(jars[i]);
				}
			}
		}
	}

	public void startModule() {
		mbs = MBeanServerFactory.createMBeanServer();
		InputStream is = new DynamicUMSStreamReader()
				.getInputStreamFromFile("/resources/jmx.props");
		Properties p = new Properties();
		System.out.println("����JMXԶ�̹���������ģ��");
		try {
			p.load(is);
			is.close();
			int port = Integer.parseInt(p.getProperty("port"));
			JMXServiceURL url = new JMXServiceURL(
					"service:jmx:rmi:///jndi/rmi://127.0.0.1:" + port
							+ "/server");
			LocateRegistry.createRegistry(port);

			cs = JMXConnectorServerFactory
					.newJMXConnectorServer(url, null, mbs);
			cs.start();
			System.out.println("JMXԶ�̹���������ģ������");
			UMSManageStandard mbean = new UMSManageStandard(1);
			mbean.startUMS();
//			ServerJMXHandler.getInstance().registerServerMBeanListener(null);
//			ServerJMXHandler.getInstance().stopModule();
		} catch (MalformedURLException e) {
			Res.logExceptionTrace(e);
		} catch (RemoteException e) {
			Res.logExceptionTrace(e);
		} catch (IOException e) {
			Res.logExceptionTrace(e);
		}

	}

	/**
	 * �ر�JMX server
	 */
	public void stopModule() {
		if (cs != null) {
			try {
				cs.stop();
			} catch (IOException e) {
				Res.logExceptionTrace(e);
			}
		}
		mbs = null;
	}

	/**
	 * ��ȡUMS������ע���������Ϣ
	 * 
	 * @return �������ϣ�������ص���null�������쳣��UMS�޷�����
	 */
	public synchronized static List getChannels() {
		List channels = null;
		Connection conn = null;
		Map channelMap = new ConcurrentHashMap();
		try {
			conn = Res.getConnection();
			String sql = "select * from UMS_MEDIA";
			ResultSet rs = conn.createStatement().executeQuery(sql);
			channels = new ArrayList();
			while (rs.next()) {
				MediaBean bean = new MediaBean(rs.getString("media_id"), rs
						.getString("media_name"), rs.getInt("media_type"),
						MediaBean.STATUS_STOPPED);
				// channels.add(new MediaBean(rs.getString("media_id"),
				// rs.getString("medianame"), rs.getInt("type"),
				// rs.getInt("statusflag")));
				// �Ƚ����������ȷ���hashmap��״̬Ϊֹͣ�����ݿ��б�������������Ҫ���������������ChannelManager���ж�
				channelMap.put(bean.getMediaID(), bean);
			}
			rs.close();
			Map inMediaMap = ChannelManager.getInMediaInfoHash();
			Map outMediaMap = ChannelManager.getOutMediaInfoHash();
			Iterator it_in = inMediaMap.values().iterator();
			Iterator it_out = outMediaMap.values().iterator();
			while (it_in.hasNext()) {
				MediaInfo media = (MediaInfo) it_in.next();
				if (media.getChannelObject() != null) {
					if (media.getChannelObject().getThreadState() == true) {
						((MediaBean) channelMap.get(media.getMediaId()))
								.setMediaStatus(MediaBean.STATUS_RUNNING);
					}
				}
			}
			while (it_out.hasNext()) {
				MediaInfo media = (MediaInfo) it_out.next();
				if (media.getChannelObject() != null) {
					if (media.getChannelObject().getThreadState() == true) {
						((MediaBean) channelMap.get(media.getMediaId()))
								.setMediaStatus(MediaBean.STATUS_RUNNING);
					}
				}
			}
		} catch (Exception e) {
			Res.log(Res.ERROR, "��ȡ������Ϣʱ�������ݿ����" + e.getMessage());
			Res.logExceptionTrace(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					Res.logExceptionTrace(e);
				}
			}
		}
		Iterator channelsIT = channelMap.values().iterator();
		while (channelsIT.hasNext())
			channels.add(channelsIT.next());
		return channels;
	}

	public static void main(String[] qil_wong) {
		new UMSJMXServer().startModule();
	}
}
