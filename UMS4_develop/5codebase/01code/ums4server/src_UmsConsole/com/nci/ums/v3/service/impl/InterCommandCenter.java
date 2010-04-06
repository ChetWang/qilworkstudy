package com.nci.ums.v3.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import com.nci.ums.channel.ChannelIfc;
import com.nci.ums.channel.channelinfo.MediaInfo;
import com.nci.ums.channel.channelmanager.ChannelManager;
import com.nci.ums.channel.outchannel.Filter;
import com.nci.ums.common.service.InterCommand;
import com.nci.ums.filter.FilterInfo;
import com.nci.ums.forward.ForwardContentBean;
import com.nci.ums.media.MediaBean;
import com.nci.ums.periphery.application.AppInfo;
import com.nci.ums.util.AppServiceUtil;
import com.nci.ums.util.DBUtil_V3;
import com.nci.ums.util.FeeUtil;
import com.nci.ums.util.LoginUtil_V3;
import com.nci.ums.util.Res;
import com.nci.ums.util.serialize.UMSXMLSerializer;
import com.nci.ums.v3.fee.FeeBean;
import com.nci.ums.v3.service.ServiceInfo;
import com.nci.ums.v3.service.impl.activemq.UMSJMSAuthorization;

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

	private UMSXMLSerializer serializer = new UMSXMLSerializer();

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
				conn = Res.getConnection();
				Statement stmt = conn.createStatement();

				StringBuffer sql = new StringBuffer();
				sql.append("SELECT MEDIA_CLASS FROM").append(
						" UMS_MEDIA WHERE MEDIA_ID = '").append(
						media.getMediaId()).append("'");
				Res.log(Res.DEBUG, "��ȡ���������ࣺ"+sql.toString());
				ResultSet rs = stmt
						.executeQuery(sql.toString());
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
	private boolean addFilter(String content) {

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
	private boolean removeFilter(String content) {
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
	private boolean modifyFilter(String appID, String appPassword,
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
	private boolean addFeeService(String newData) {

		boolean flag = false;
		FeeBean newFeeInfo = (FeeBean) this.serializer.deserialize(newData);
		Map fees = (Map) FeeUtil.getFeeMap();
		fees.put(newFeeInfo.getFeeServiceNO(), newFeeInfo);
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
	private boolean modifyFeeService(String originalFeeServiceNO,
			String currentFeeServiceNO, int feeUserType, int fee,
			String feeTerminalID, String feeDescription) {
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
	private boolean removeFeeService(String newData) {

		boolean flag = false;
		Map fees = (Map) FeeUtil.getFeeMap();
		FeeBean newFeeInfo = (FeeBean) this.serializer.deserialize(newData);
		fees.remove(newFeeInfo.getFeeServiceNO());
		flag = true;
		Res.log(Res.INFO, "ɾ���շѷ�������" + newFeeInfo.getFeeServiceNO() + "�ɹ�!");
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
	private boolean addService(String newData) {
		ServiceInfo info = (ServiceInfo) serializer.deserialize(newData);
		boolean flag = false;
		if (info.getStatus() == ServiceInfo.SERVICE_STATUS_VALID) {
			// ServiceInfo info = new ServiceInfo();
			// info.setAppKey(serviceAPPID);
			// info.setServiceID(serviceID);
			// info.setServiceName(serviceName);
			Map activeServiceMap = AppServiceUtil.getActiveAppServiceMap();
			activeServiceMap.put(info.getServiceID(), info);
			Res.log(Res.INFO, "����" + info.getServiceID() + "ע��ɹ���");
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
	private boolean modifyService(String oldData, String newData) {
		ServiceInfo oldInfo = (ServiceInfo) serializer.deserialize(oldData);
		ServiceInfo newInfo = (ServiceInfo) serializer.deserialize(newData);
		boolean flag = false;
		Map activeServiceMap = AppServiceUtil.getActiveAppServiceMap();
		if (activeServiceMap.containsKey(oldInfo.getServiceID())) {
			activeServiceMap.remove(oldInfo.getServiceID());
		} else {
			Res.log(Res.INFO, "����ƽ̨�����滻������" + oldInfo.getServiceID()
					+ ",��UMS Server��δ����÷��񣬻�÷����Ѿ���ֹͣ");
		}
		if (newInfo.getStatus() == ServiceInfo.SERVICE_STATUS_VALID) {// �޸����з������ݻ�������

			activeServiceMap.put(newInfo.getServiceID(), newInfo);
			Res.log(Res.INFO, "����" + newInfo.getServiceID() + "�޸ĳɹ���");
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
	private boolean removeService(String newData) {
		// System.out.println(newData);
		ServiceInfo info = (ServiceInfo) serializer.deserialize(newData);
		boolean flag = false;
		Map activeServiceMap = AppServiceUtil.getActiveAppServiceMap();
		if (activeServiceMap.containsKey(info.getServiceID())) {
			activeServiceMap.remove(info.getServiceID());
			Res.log(Res.INFO, "����" + info.getServiceID() + "ɾ���ɹ���");
		} else {
			Res.log(Res.INFO, "����ƽ̨�����Ƴ�����" + info.getServiceID()
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
	private boolean addApplication(String newData) {

		boolean flag = false;
		AppInfo app = (AppInfo) serializer.deserialize(newData);

		Map appMap = LoginUtil_V3.getAppMap();
		appMap.put(app.getAppID(), app);
		UMSJMSAuthorization.getInstance().getUserPasswords().put(
				app.getAppID(),app.getPassword());
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
	private boolean modifyApplication(String adminAppID, String adminAppPsw,
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
	private boolean removeApplication(String data) {

		boolean flag = false;
		Map appMap = LoginUtil_V3.getAppMap();
		AppInfo app = (AppInfo) serializer.deserialize(data);
		appMap.remove(app.getAppID());
		UMSJMSAuthorization.getInstance().getUserPasswords().remove(
				app.getAppID());
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
	private boolean addForwardContent(String newForwardContent,
			String forwardServiceID) {

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
	private boolean modifyForwardContent(String adminAppID, String adminAppPsw,
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
	private boolean removeForwardContent(String removedForwardContent) {
		boolean flag = false;
		Map forwardServiceMap = AppServiceUtil.getForwardServiceMap();
		forwardServiceMap.remove(removedForwardContent);
		flag = true;
		Res.log(Res.INFO, "ɾ��ת�����ݣ�" + removedForwardContent + "�ɹ�");
		return flag;
	}

	/**
	 * @���� �ڲ��ӿ�ת������
	 * @param appid
	 * @param apppsw
	 * @param changeType
	 * @param oldData
	 * @param newData
	 * @return
	 */
	public boolean interChanged(String appid, String apppsw, int changeType,
			String oldData, String newData) {
		if (!appid.equals("DESKTOPADMIN")) {
			return false;
		}
		// System.out.println(changeType);
		// System.out.println("newData:\n" + newData);
		// System.out.println("oldData:\n" + oldData);

		ForwardContentBean forward; // ת������
		FilterInfo filter; // ���˶���
		MediaBean media; // ��������
		switch (changeType) {
		case InterCommand.APP_ADD:
			// ����Ӧ��
			this.addApplication(newData);
			break;
		case InterCommand.APP_DELETE:
			// ɾ��Ӧ��
			this.removeApplication(oldData);
			break;
		case InterCommand.APP_START:
			// ����Ӧ��
			this.addApplication(newData);// ��UMS�������У�start��stop����add��remove
			break;
		case InterCommand.APP_STOP:
			// ֹͣӦ��
			this.removeApplication(newData);
			break;
		case InterCommand.APP_UPDATE:
			// ����Ӧ��
			this.removeApplication(oldData);
			this.addApplication(newData);
			break;
		case InterCommand.FEE_ADD:
			// ��������
			this.addFeeService(newData);
			break;
		case InterCommand.FEE_DELETE:
			// ɾ������
			this.removeFeeService(oldData);
			break;
		case InterCommand.FEE_UPDATE:
			// ���·���
			this.removeFeeService(oldData);
			addFeeService(newData);
			break;
		case InterCommand.FILTER_ADD:
			// ��������
			filter = (FilterInfo) serializer.deserialize(newData);
			this.addFilter(filter.getContent());
			break;
		case InterCommand.FILTER_DELETE:
			// ɾ������
			filter = (FilterInfo) serializer.deserialize(oldData);
			this.removeFilter(filter.getContent());
			break;
		case InterCommand.FILTER_START:
			// ��������
			filter = (FilterInfo) serializer.deserialize(newData);
			this.addFilter(filter.getContent());
			break;
		case InterCommand.FILTER_STOP:
			// ֹͣ����
			filter = (FilterInfo) serializer.deserialize(newData);
			this.removeFilter(filter.getContent());
			break;
		case InterCommand.FILTER_UPDATE:
			// �޸Ĺ���
			filter = (FilterInfo) serializer.deserialize(oldData);
			this.removeFilter(filter.getContent());
			filter = (FilterInfo) serializer.deserialize(newData);
			this.addFilter(filter.getContent());
			break;
		case InterCommand.FORWARD_CONTENT_ADD:
			// �½�ת��
			forward = (ForwardContentBean) serializer.deserialize(newData);
			this.addForwardContent(forward.getForwardContent(), forward
					.getServiceid());
			break;
		case InterCommand.FORWARD_CONTENT_DELETE:
			// ɾ��ת��
			forward = (ForwardContentBean) serializer.deserialize(oldData);
			this.removeForwardContent(forward.getForwardContent());
			break;
		case InterCommand.FORWARD_CONTENT_START:
			// ����ת��
			forward = (ForwardContentBean) serializer.deserialize(newData);
			this.addForwardContent(forward.getForwardContent(), forward
					.getServiceid());
			break;
		case InterCommand.FORWARD_CONTENT_STOP:
			// ֹͣת��
			forward = (ForwardContentBean) serializer.deserialize(newData);
			this.removeForwardContent(forward.getForwardContent());
			break;
		case InterCommand.FORWARD_CONTENT_UPDATE:
			// ����ת��
			forward = (ForwardContentBean) serializer.deserialize(oldData);
			this.removeForwardContent(forward.getForwardContent());
			forward = (ForwardContentBean) serializer.deserialize(newData);
			this.addForwardContent(forward.getForwardContent(), forward
					.getServiceid());
			break;
		case InterCommand.MEDIA_ADD:
			// ��������
//			media = (MediaBean) serializer.deserialize(newData);
//			try {
//				startChannel("DESKTOPADMIN", "", media.getMediaID(), media
//						.getMediaName(), media.getMediaType());
//			} catch (Exception e) {
//				Res.logExceptionTrace(e);
//			}
			break;
		case InterCommand.MEDIA_DELETE:
			// ɾ������
			media = (MediaBean) serializer.deserialize(oldData);
			try {
				stopChannel("DESKTOPADMIN", "", media.getMediaID(), media
						.getMediaName(), media.getMediaType());
			} catch (Exception e) {
				Res.logExceptionTrace(e);
			}
			break;
		case InterCommand.MEDIA_START:
			// ��������
			media = (MediaBean) serializer.deserialize(newData);
			try {
				startChannel("DESKTOPADMIN", "", media.getMediaID(), media
						.getMediaName(), media.getMediaType());
			} catch (Exception e) {
				Res.logExceptionTrace(e);
			}
			break;
		case InterCommand.MEDIA_STOP:
			// ֹͣ����
			media = (MediaBean) serializer.deserialize(oldData);
			try {
				stopChannel("DESKTOPADMIN", "", media.getMediaID(), media
						.getMediaName(), media.getMediaType());
			} catch (Exception e) {
				Res.logExceptionTrace(e);
			}
			break;
		case InterCommand.MEDIA_UPDATE:
			// ��������
			try {
				media = (MediaBean) serializer.deserialize(oldData);
				stopChannel("DESKTOPADMIN", "", media.getMediaID(), media
						.getMediaName(), media.getMediaType());
				// media = (MediaBean) serializer.deserialize(newData);
				// startChannel("DESKTOPADMIN", "", media.getMediaID(), media
				// .getMediaName(), media.getMediaType());
			} catch (Exception e) {
				Res.logExceptionTrace(e);
			}
			break;
		case InterCommand.SERVICE_ADD:
			// ��������
			addService(newData);
			break;
		case InterCommand.SERVICE_DELETE:
			// ɾ������
			removeService(oldData);
			break;
		case InterCommand.SERVICE_START:
			// ��������
			addService(newData);
			break;
		case InterCommand.SERVICE_STOP:
			// ֹͣ����
			removeService(oldData);
			break;
		case InterCommand.SERVICE_UPDATE:
			// �޸ķ���
			removeService(oldData);
			addService(newData);
			break;
		default:
			break;
		}
		return true;
	}

}
