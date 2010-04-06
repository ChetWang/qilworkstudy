package com.nci.ums.v3.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import com.nci.ums.channel.ChannelIfc;
import com.nci.ums.channel.channelinfo.MediaInfo;
import com.nci.ums.channel.channelmanager.ChannelManager;
import com.nci.ums.channel.outchannel.Filter;
import com.nci.ums.channel.outchannel.FilterInfo;
import com.nci.ums.periphery.application.AppInfo;
import com.nci.ums.sync.impl.UMSSynOrg;
import com.nci.ums.sync.impl.UMSSynPer;
import com.nci.ums.sync.ldap.LdapOrgResult;
import com.nci.ums.sync.ldap.LdapPerResult;
import com.nci.ums.util.AppServiceUtil;
import com.nci.ums.util.DBUtil_V3;
import com.nci.ums.util.DataBaseOp;
import com.nci.ums.util.FeeUtil;
import com.nci.ums.util.LoginUtil_V3;
import com.nci.ums.util.Res;
import com.nci.ums.v3.fee.FeeBean;
import com.nci.ums.v3.service.ServiceInfo;

/**
 * The main web service interface for inter ums communication between UMS engine
 * and UMS web management platform. Such as App(Service) status monitoring, if
 * the status is changed, ums Enginge must has some action immediately(start or
 * stop the channel thread).
 * 
 * @author Qil.Wong
 * 
 */
public class InterCommandCenter {

	/**
	 * ָֹͣ��������
	 * 
	 * @param mediaID
	 * @param mediaName
	 * @param mediaType
	 * @return
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public boolean stopChannel(String appID, String appPassword,
			String mediaID, String mediaName, int mediaType)
			throws IllegalArgumentException, SecurityException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		int login = DBUtil_V3.login(appID, appPassword);
		if (login != DBUtil_V3.LOGIN_SUCCESS) {
			return false;
		}
		MediaInfo media = new MediaInfo();
		media.setMediaID(mediaID);
		media.setMediaName(mediaName);
		media.setType(mediaType);
		System.out.println(media.getMediaId() + "," + media.getMediaName()
				+ "," + mediaType);
		switch (mediaType) {
		case MediaInfo.TYPE_OUTCHANNEL:
			Res.log(Res.INFO, "ֹͣ�ⷢ������" + media.getMediaId());
			return this.process(ChannelManager.getOutMediaInfoHash(), media,
					MediaInfo.STATUS_STOPPED);
		case MediaInfo.TYPE_INCHANNEL:
			Res.log(Res.INFO, "ֹͣ����������" + media.getMediaId());
			return this.process(ChannelManager.getInMediaInfoHash(), media,
					MediaInfo.STATUS_STOPPED);
		case -1:
			System.out.println("��ָ��MediaInfo����ķ��ͽ�������");
			return false;
		}
		return false;
	}

	/**
	 * ����ָ��������
	 * 
	 * @param mediaID
	 * @param mediaName
	 * @param mediaType
	 * @return
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public boolean startChannel(String appID, String appPassword,
			String mediaID, String mediaName, int mediaType)
			throws IllegalArgumentException, SecurityException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		int login = DBUtil_V3.login(appID, appPassword);
		if (login != DBUtil_V3.LOGIN_SUCCESS) {
			return false;
		}
		MediaInfo media = new MediaInfo();
		media.setMediaID(mediaID);
		media.setMediaName(mediaName);
		media.setType(mediaType);
		switch (mediaType) {
		case MediaInfo.TYPE_OUTCHANNEL:
			Res.log(Res.INFO, "�����ⷢ������" + media.getMediaId());
			return this.process(ChannelManager.getOutMediaInfoHash(), media,
					MediaInfo.STATUS_STARTED);
		case MediaInfo.TYPE_INCHANNEL:
			Res.log(Res.INFO, "��������������" + media.getMediaId());
			return this.process(ChannelManager.getInMediaInfoHash(), media,
					MediaInfo.STATUS_STARTED);
		case -1:
			System.out.println("��ָ��MediaInfo����ķ��ͽ�������");
			return false;
		}
		return false;
	}

	/**
	 * ����ָ��������������ֹͣ�¼���
	 * 
	 * @param channels
	 * @param media
	 * @param status
	 * @throws ClassNotFoundException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 */
	private boolean process(Map channels, MediaInfo media, int status)
			throws ClassNotFoundException, IllegalArgumentException,
			SecurityException, InstantiationException, IllegalAccessException,
			InvocationTargetException {
		if (media.getMediaName() == null || media.getMediaId() == null) {
			return false;
		}

		boolean flag = false;
		switch (status) {
		case MediaInfo.STATUS_STARTED:
			String className = "";
			Connection conn = null;
			try {
				conn = DriverManager.getConnection(DataBaseOp.getPoolName());
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt
						.executeQuery("SELECT CLASS FROM MEDIA WHERE MEDIA_ID = "
								+ media.getMediaId());
				while (rs.next()) {
					className = rs.getString(1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			media.setClassName(className);
			if (className == null || className.equals("")) {
				return false;
			}
			ChannelIfc threadChannel = ChannelManager
					.constructMediaChannel(media);
			if (threadChannel != null) {
				threadChannel.start();
				if (threadChannel.getThreadState() == true) {
					media.setChannelObject(threadChannel);
					channels.put(media.getMediaId(), media);
					flag = true;
				} else
					flag = false;
			} else {
				flag = false;
				// Res.log(Res.ERROR,"["+media.getMediaId()+"]"+media.getMediaName()+"��������ʧ��!");
			}
			break;
		case MediaInfo.STATUS_STOPPED:
			// �Ѿ����ڵ�media�����߳�
			// MediaInfo mediaInitd = (MediaInfo)
			// channels.get(media.getMediaId()
			// + media.getMeidaName());
			MediaInfo mediaInitd = (MediaInfo) channels.get(media.getMediaId());
			ChannelIfc channelThread = mediaInitd.getChannelObject();
			channelThread.stop();
			channels.remove(media.getMediaId());
			flag = true;
			break;
		}
		return flag;
	}

	/**
	 * ��Ϣ���˵����ݵ�������Ϊ,filters�Ѿ�������Ϊ�̰߳�ȫ�͡�
	 * 
	 * @param content
	 *            �����Ĺ�������
	 * @return
	 */
	public synchronized boolean addFilter(String appID, String appPassword,
			String content) {
		int login = DBUtil_V3.login(appID, appPassword);
		if (login != DBUtil_V3.LOGIN_SUCCESS) {
			return false;
		}
		boolean flag = false;
		try {
			FilterInfo info = new FilterInfo();
			info.setContent(content);
			Filter.filters.add(info);
			flag = true;
			Res.log(Res.INFO, "������Ϣ��������" + content + "�ɹ�!");
		} catch (Exception e) {
			e.printStackTrace();
			Res.log(Res.ERROR, "������Ϣ��������" + content + "ʧ��!");
			Res.logExceptionTrace(e);
		}
		return flag;
	}

	/**
	 * �Ƴ�������
	 * 
	 * @param content
	 *            �Ƴ��Ĺ���������
	 * @return
	 */
	public boolean removeFilter(String appID, String appPassword, String content) {
		int login = DBUtil_V3.login(appID, appPassword);
		if (login != DBUtil_V3.LOGIN_SUCCESS) {
			return false;
		}
		boolean flag = false;
		try {
			if (content != null) {
				for (int i = 0; i < Filter.filters.size(); i++) {
					FilterInfo info = (FilterInfo) Filter.filters.get(i);
					if (content.equalsIgnoreCase(info.getContent())) {
						Filter.filters.remove(i);
						Res.log(Res.INFO, "�Ƴ���Ϣ��������" + content + "�ɹ�!");
						flag = true;
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Res.log(Res.ERROR, "�Ƴ���Ϣ������:" + content + "ʧ��!" + e.getMessage());
			Res.logExceptionTrace(e);
		}
		return flag;

	}

	/**
	 * �޸��Ѿ����ڵĹ���������
	 * 
	 * @param originalContent
	 *            ԭ�ȵĹ�����
	 * @param currentContent
	 *            �޸ĺ�Ĺ�����
	 * @return
	 */
	public boolean modifyFilter(String appID, String appPassword,
			String originalContent, String currentContent) {
		int login = DBUtil_V3.login(appID, appPassword);
		if (login != DBUtil_V3.LOGIN_SUCCESS) {
			return false;
		}
		boolean flag = false;
		if (currentContent != null) {
			for (int i = 0; i < Filter.filters.size(); i++) {
				FilterInfo info = (FilterInfo) Filter.filters.get(i);
				if (originalContent.equalsIgnoreCase(info.getContent())) {
					Filter.filters.remove(i);
					FilterInfo newInfo = new FilterInfo();
					newInfo.setContent(currentContent);
					Filter.filters.add(i, newInfo);
					flag = true;
					Res.log(Res.INFO, "�޸���Ϣ��������" + originalContent + "Ϊ��"
							+ currentContent + "�ɹ�!");
					break;
				}
			}
		}
		return flag;
	}

	/**
	 * �������÷��񣬲��������͵ĺ��壬�μ�FeeBean
	 * 
	 * @param feeServiceNO
	 *            ���÷������
	 * @param feeUserType
	 * @param fee
	 * @param feeTerminalID
	 * @param feeDdescription
	 * @return true�����ɹ���false����ʧ��
	 */
	public boolean addFeeService(String appID, String appPassword,
			String feeServiceNO, int feeUserType, int fee,
			String feeTerminalID, String feeDescription) {
		int login = DBUtil_V3.login(appID, appPassword);
		if (login != DBUtil_V3.LOGIN_SUCCESS) {
			return false;
		}
		boolean flag = false;
		FeeBean newFeeInfo = new FeeBean(feeServiceNO, feeUserType, fee,
				feeTerminalID, feeDescription);
		Map fees = (Map) FeeUtil.getFeeMap();
		fees.put(feeServiceNO, newFeeInfo);
		FeeUtil.setFeeMap(fees);
		flag = true;
		Res.log(Res.INFO, "�����շѷ�������" + newFeeInfo.toString() + "�ɹ�!");
		return flag;
	}

	/**
	 * �޸��Ѿ����ڵķ��÷�����Ϣ
	 * 
	 * @param originalFeeServiceNO
	 * @param currentFeeServiceNO
	 * @param feeUserType
	 * @param fee
	 * @param feeTerminalID
	 * @param feeDdescription
	 * @return
	 */
	public boolean modifyFeeService(String appID, String appPassword,
			String originalFeeServiceNO, String currentFeeServiceNO,
			int feeUserType, int fee, String feeTerminalID,
			String feeDescription) {
		int login = DBUtil_V3.login(appID, appPassword);
		if (login != DBUtil_V3.LOGIN_SUCCESS) {
			return false;
		}
		boolean flag = false;
		FeeBean newFeeInfo = new FeeBean(currentFeeServiceNO, feeUserType, fee,
				feeTerminalID, feeDescription);
		Map fees = (Map) FeeUtil.getFeeMap();
		fees.remove(originalFeeServiceNO);
		fees.put(currentFeeServiceNO, newFeeInfo);
		FeeUtil.setFeeMap(fees);
		flag = true;
		Res.log(Res.INFO, "�޸��շѷ�������" + newFeeInfo.toString() + "�ɹ�!");
		return flag;
	}

	/**
	 * �Ƴ��Ѿ����ڵķ��÷�����Ϣ
	 * 
	 * @param feeServiceNO
	 * @return
	 */
	public boolean removeFeeService(String appID, String appPassword,
			String feeServiceNO) {
		int login = DBUtil_V3.login(appID, appPassword);
		if (login != DBUtil_V3.LOGIN_SUCCESS) {
			return false;
		}
		boolean flag = false;
		Map fees = (Map) FeeUtil.getFeeMap();
		fees.remove(feeServiceNO);
		flag = true;
		Res.log(Res.INFO, "ɾ���շѷ�������" + feeServiceNO + "�ɹ�!");
		return flag;
	}

	/**
	 * ����ע��
	 * 
	 * @param appID
	 * @param appPassword
	 * @param serviceAPPID
	 * @param serviceID
	 * @param serviceName
	 * @param status
	 * @return
	 */
	public boolean addService(String appID, String appPassword,
			String serviceAPPID, String serviceID, String serviceName,
			int status) {
		int login = DBUtil_V3.login(appID, appPassword);
		if (login != DBUtil_V3.LOGIN_SUCCESS) {
			return false;
		}
		boolean flag = false;
		if (status == ServiceInfo.SERVICE_STATUS_VALID) {
			ServiceInfo info = new ServiceInfo();
			info.setAppID(serviceAPPID);
			info.setServiceID(serviceID);
			info.setServiceName(serviceName);
			Map activeServiceMap = AppServiceUtil.getActiveAppServiceMap();
			activeServiceMap.put(serviceID, info);
			Res.log(Res.INFO, "����" + serviceID + "ע��ɹ���");
		}
		flag = true;
		return flag;
	}

	/**
	 * �޸���ע��ķ���
	 * 
	 * @param appID
	 * @param appPassword
	 * @param serviceAPPID
	 * @param originalServiceID
	 * @param currentServiceID
	 * @param serviceName
	 * @param status
	 * @return
	 */
	public boolean modifyService(String appID, String appPassword,
			String serviceAPPID, String originalServiceID,
			String currentServiceID, String serviceName, int status) {
		int login = DBUtil_V3.login(appID, appPassword);
		if (login != DBUtil_V3.LOGIN_SUCCESS) {
			return false;
		}
		boolean flag = false;
		Map activeServiceMap = AppServiceUtil.getActiveAppServiceMap();
		if (activeServiceMap.containsKey(originalServiceID)) {
			activeServiceMap.remove(originalServiceID);
		} else {
			Res.log(Res.INFO, "����ƽ̨�����滻������" + originalServiceID
					+ ",��UMS Server��δ����÷��񣬻�÷����Ѿ���ֹͣ");
		}
		if (status == ServiceInfo.SERVICE_STATUS_VALID) {// �޸����з������ݻ�������
			ServiceInfo info = new ServiceInfo();
			info.setAppID(serviceAPPID);
			info.setServiceID(currentServiceID);
			info.setServiceName(serviceName);
			activeServiceMap.put(currentServiceID, info);
			Res.log(Res.INFO, "����" + currentServiceID + "�޸ĳɹ���");
		}
		flag = true;
		return flag;
	}

	/**
	 * �Ƴ���ע��ķ���
	 * 
	 * @param appID
	 *            ��¼�Ĺ���ԱID
	 * @param appPassword
	 *            ��¼�Ĺ���Ա����
	 * @param serviceID
	 *            �Ƴ��ķ���ID
	 * @return �ɹ���ʧ�ܱ�ǣ�trueΪ�ɹ���falseΪʧ��
	 */
	public boolean removeService(String appID, String appPassword,
			String serviceID) {
		int login = DBUtil_V3.login(appID, appPassword);
		if (login != DBUtil_V3.LOGIN_SUCCESS) {
			return false;
		}
		boolean flag = false;
		Map activeServiceMap = AppServiceUtil.getActiveAppServiceMap();
		if (activeServiceMap.containsKey(serviceID)) {
			activeServiceMap.remove(serviceID);
			Res.log(Res.INFO, "����" + serviceID + "ɾ���ɹ���");
		} else {
			Res.log(Res.INFO, "����ƽ̨�����Ƴ�����" + serviceID
					+ ",��UMS Server��δ����÷��񣬻�÷����Ѿ���ֹͣ");
		}
		flag = true;
		return flag;
	}

	/**
	 * ����ע��Ӧ��
	 * 
	 * @param adminAppID
	 *            ��¼�Ĺ���ԱID
	 * @param adminAppPsw
	 *            ��¼�Ĺ���Ա����
	 * @param newAppID
	 *            ��Ӧ��ID
	 * @param newAppPsw
	 *            ��Ӧ������
	 * @param newAppName
	 *            ��Ӧ������
	 * @param newAppStatus
	 *            ��Ӧ��״̬
	 * @return �ɹ���ʧ�ܱ�ǣ�trueΪ�ɹ���falseΪʧ��
	 */
	public boolean addApplication(String adminAppID, String adminAppPsw,
			String newAppID, String newAppPsw, String newAppName,
			String newAppStatus) {
		int login = DBUtil_V3.login(adminAppID, adminAppPsw);
		if (login != DBUtil_V3.LOGIN_SUCCESS) {
			return false;
		}
		boolean flag = false;
		AppInfo app = new AppInfo();
		app.setAppID(newAppID);
		app.setAppName(newAppName);
		app.setPassword(newAppPsw);
		app.setStatus(newAppStatus);
		Map appMap = LoginUtil_V3.getAppMap();
		appMap.put(app.getAppID(), app);
		flag = true;
		return flag;
	}

	/**
	 * �޸���ע���Ӧ��
	 * 
	 * @param adminAppID
	 *            ��¼�Ĺ���ԱID
	 * @param adminAppPsw
	 *            ��¼�Ĺ���Ա����
	 * @param newAppID
	 *            �޸ĺ��Ӧ��ID
	 * @param newAppPsw
	 *            �޸ĺ��Ӧ������
	 * @param newAppName
	 *            �޸ĺ��Ӧ������
	 * @param newAppStatus
	 *            �޸ĺ��Ӧ��״̬
	 * @return �ɹ���ʧ�ܱ�ǣ�trueΪ�ɹ���falseΪʧ��
	 */
	public boolean modifyApplication(String adminAppID, String adminAppPsw,
			String originalAppID, String newAppID, String newAppPsw,
			String newAppName, String newAppStatus) {
		// boolean flag = addApplication(adminAppID, adminAppPsw, newAppID,
		// newAppPsw,
		// newAppName, newAppStatus);
		int login = DBUtil_V3.login(adminAppID, adminAppPsw);
		if (login != DBUtil_V3.LOGIN_SUCCESS) {
			return false;
		}
		boolean flag = false;
		Map appMap = LoginUtil_V3.getAppMap();
		appMap.remove(originalAppID);
		AppInfo app = new AppInfo();
		app.setAppID(newAppID);
		app.setAppName(newAppName);
		app.setPassword(newAppPsw);
		app.setStatus(newAppStatus);
		appMap.put(newAppID, app);
		if (flag == true) {
			Res.log(Res.INFO, "Ӧ��" + newAppID + "�޸ĳɹ���");
		}
		return flag;
	}

	/**
	 * ɾ����ע���Ӧ��
	 * 
	 * @param adminAppID
	 *            ��¼�Ĺ���ԱID
	 * @param adminAppPsw
	 *            ��¼�Ĺ���Ա����
	 * @param removedAppID
	 *            ɾ����Ӧ�ú�
	 * @return �ɹ���ʧ�ܱ�ǣ�trueΪ�ɹ���falseΪʧ��
	 */
	public boolean removeApplication(String adminAppID, String adminAppPsw,
			String removedAppID) {
		int login = DBUtil_V3.login(adminAppID, adminAppPsw);
		if (login != DBUtil_V3.LOGIN_SUCCESS) {
			return false;
		}
		boolean flag = false;
		Map appMap = LoginUtil_V3.getAppMap();
		appMap.remove(removedAppID);
		flag = true;
		return flag;
	}

	/**
	 * ����ת��������
	 * 
	 * @param adminAppID
	 *            ��¼�Ĺ���ԱID
	 * @param adminAppPsw
	 *            ��¼�Ĺ���Ա����
	 * @param newForwardContent
	 *            ��ת��������
	 * @param forwardServiceID
	 *            ��ת�����ݶ�Ӧ�ķ���ID
	 * @return �ɹ���ʧ�ܱ�ǣ�trueΪ�ɹ���falseΪʧ��
	 */
	public boolean addForwardContent(String adminAppID, String adminAppPsw,
			String newForwardContent, String forwardServiceID) {
		int login = DBUtil_V3.login(adminAppID, adminAppPsw);
		if (login != DBUtil_V3.LOGIN_SUCCESS) {
			return false;
		}
		boolean flag = false;
		Map forwardServiceMap = AppServiceUtil.getForwardServiceMap();
		forwardServiceMap.put(newForwardContent, forwardServiceID);
		flag = true;
		Res.log(Res.INFO, "����ת�����ݣ�" + newForwardContent + "�ɹ�");
		return flag;
	}

	/**
	 * �޸����е�ת������
	 * 
	 * @param adminAppID
	 *            ��¼�Ĺ���ԱID
	 * @param adminAppPsw
	 *            ��¼�Ĺ���Ա����
	 * @param currentForwardContent
	 *            �޸�ǰ��ת������
	 * @param newForwardContent
	 *            ��ת��������
	 * @param forwardServiceID
	 *            ԭ���ݶ�Ӧ�ķ���ID
	 * @return �ɹ���ʧ�ܱ�ǣ�trueΪ�ɹ���falseΪʧ��
	 */
	public boolean modifyForwardContent(String adminAppID, String adminAppPsw,
			String currentForwardContent, String newForwardContent,
			String forwardServiceID) {
		int login = DBUtil_V3.login(adminAppID, adminAppPsw);
		if (login != DBUtil_V3.LOGIN_SUCCESS) {
			return false;
		}
		boolean flag = false;
		Map forwardServiceMap = AppServiceUtil.getForwardServiceMap();
		forwardServiceMap.remove(currentForwardContent);
		forwardServiceMap.put(newForwardContent, forwardServiceID);
		flag = true;
		Res.log(Res.INFO, "�޸�ת�����ݣ�" + newForwardContent + "�ɹ�");
		return flag;
	}

	/**
	 * ɾ���������õ�ת������
	 * 
	 * @param adminAppID
	 *            ��¼�Ĺ���ԱID
	 * @param adminAppPsw
	 *            ��¼�Ĺ���Ա����
	 * @param removedForwardContent
	 *            Ҫɾ����ת������
	 * @return �ɹ���ʧ�ܱ�ǣ�trueΪ�ɹ���falseΪʧ��
	 */
	public boolean removeForwardContent(String adminAppID, String adminAppPsw,
			String removedForwardContent) {
		int login = DBUtil_V3.login(adminAppID, adminAppPsw);
		if (login != DBUtil_V3.LOGIN_SUCCESS) {
			return false;
		}
		boolean flag = false;
		Map forwardServiceMap = AppServiceUtil.getForwardServiceMap();
		forwardServiceMap.remove(removedForwardContent);
		flag = true;
		Res.log(Res.INFO, "ɾ��ת�����ݣ�" + removedForwardContent + "�ɹ�");
		return flag;
	}

	/**
	 * ��LDAPͬ��
	 */
	public boolean  receiveUserInfoFromLDAP(String adminAppID,String adminAppPsw,String userXML) {
		int login = DBUtil_V3.login(adminAppID, adminAppPsw);
		if (login != DBUtil_V3.LOGIN_SUCCESS) {
			return false;
		}
		try{
		int a = com.nci.ums.sync.util.XmlUtil.getXmlType(userXML);
		if (2 == a) {
			LdapPerResult lp = com.nci.ums.sync.util.XmlUtil
					.parsePerXml(userXML);
			UMSSynPer umssynper = new UMSSynPer();
			umssynper.doSync(lp);
		} else if (1 == a) {
			LdapOrgResult lp = com.nci.ums.sync.util.XmlUtil
					.parseOrgXml(userXML);
			UMSSynOrg umssynorg = new UMSSynOrg();
			umssynorg.doSync(lp);
		}
		}catch(Exception e){
			Res.log(Res.ERROR, "LDAP�û���Ϣͬ��ʧ��");
			return false;
		}
		return true;
	}

	// /**
	// * ��LDAP���û���Ϣ������UUM
	// * @param userXML
	// * @return
	// */
	// public boolean receiveUserInfoFromLDAP(String adminAppID,String
	// adminAppPsw,String userXML) {
	// try {
	// int login = DBUtil_V3.login(adminAppID, adminAppPsw);
	// if (login != DBUtil_V3.LOGIN_SUCCESS) {
	// return false;
	// }
	// InterCommandCenter.writeToUUM(userXML);
	// return true;
	// } catch (IOException e) {
	// Res.logExceptionTrace(e);
	// try {
	// client = new Socket(remote_ip, port);
	// oos = new ObjectOutputStream(client.getOutputStream());
	// } catch (UnknownHostException e1) {
	// Res.logExceptionTrace(e1);
	// } catch (IOException e1) {
	// Res.log(Res.ERROR, "LDAP UUM ͬ��ʱSocket�޷���!");
	// Res.logExceptionTrace(e1);
	// }
	// return false;
	// }
	// }
	//	
	public static void main(String[] args) throws IOException {
		// InterCommandCenter inter = new InterCommandCenter();
		// // FileInputStream fis = new FileInputStream(new File("C:/123.xml"));
		// StringBuffer sb = new StringBuffer();
		// BufferedReader reader = new BufferedReader(new
		// FileReader("C:/123.xml"));
		// String s = "";
		// while((s=reader.readLine())!=null){
		// sb.append(s);
		// }
		// System.out.println(sb.toString());
		// inter.receiveUserInfoFromLDAP(sb.toString());

	}

}
