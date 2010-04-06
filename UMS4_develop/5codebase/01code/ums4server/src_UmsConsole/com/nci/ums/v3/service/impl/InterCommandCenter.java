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
	 * 停止指定的渠道
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
			Res.log(Res.INFO, "停止外发渠道：" + media.getMediaId());
			return this.process(ChannelManager.getOutMediaInfoHash(), media,
					MediaInfo.STATUS_STOPPED);
		case MediaInfo.TYPE_INCHANNEL:
			Res.log(Res.INFO, "停止接收渠道：" + media.getMediaId());
			return this.process(ChannelManager.getInMediaInfoHash(), media,
					MediaInfo.STATUS_STOPPED);
		case -1:
			System.out.println("请指定MediaInfo对象的发送接收类型");
			return false;
		}
		return false;
	}

	/**
	 * 开启指定的渠道
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
			Res.log(Res.INFO, "开启外发渠道：" + media.getMediaId());
			return this.process(ChannelManager.getOutMediaInfoHash(), media,
					MediaInfo.STATUS_STARTED);
		case MediaInfo.TYPE_INCHANNEL:
			Res.log(Res.INFO, "开启接收渠道：" + media.getMediaId());
			return this.process(ChannelManager.getInMediaInfoHash(), media,
					MediaInfo.STATUS_STARTED);
		case -1:
			System.out.println("请指定MediaInfo对象的发送接收类型");
			return false;
		}
		return false;
	}

	/**
	 * 处理指定渠道的启动和停止事件。
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
				Res.log(Res.DEBUG, "获取渠道启动类："+sql.toString());
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
				// Res.log(Res.ERROR,"["+media.getMediaId()+"]"+media.getMediaName()+"渠道产生失败!");
			}
			break;
		case MediaInfo.STATUS_STOPPED:
			// 已经存在的media渠道线程
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
	 * 消息过滤的内容的增加行为,filters已经被定义为线程安全型。
	 * 
	 * @param content
	 *            新增的过滤内容
	 * @return
	 */
	private boolean addFilter(String content) {

		boolean flag = false;
		try {
			FilterInfo info = new FilterInfo();
			info.setContent(content);
			Filter.filters.add(info);
			flag = true;
			Res.log(Res.INFO, "新增消息过滤器：" + content + "成功!");
		} catch (Exception e) {
			e.printStackTrace();
			Res.log(Res.ERROR, "新增消息过滤器：" + content + "失败!");
			Res.logExceptionTrace(e);
		}
		return flag;
	}

	/**
	 * 移除过滤器
	 * 
	 * @param content
	 *            移除的过滤器内容
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
						Res.log(Res.INFO, "移除消息过滤器：" + content + "成功!");
						flag = true;
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Res.log(Res.ERROR, "移除消息过滤器:" + content + "失败!" + e.getMessage());
			Res.logExceptionTrace(e);
		}
		return flag;

	}

	/**
	 * 修改已经存在的过滤器内容
	 * 
	 * @param originalContent
	 *            原先的过滤器
	 * @param currentContent
	 *            修改后的过滤器
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
					Res.log(Res.INFO, "修改消息过滤器：" + originalContent + "为："
							+ currentContent + "成功!");
					break;
				}
			}
		}
		return flag;
	}

	/**
	 * 新增费用服务，参数各类型的含义，参见FeeBean
	 * 
	 * @param feeServiceNO
	 *            费用服务代号
	 * @param feeUserType
	 * @param fee
	 * @param feeTerminalID
	 * @param feeDdescription
	 * @return true新增成功，false新增失败
	 */
	private boolean addFeeService(String newData) {

		boolean flag = false;
		FeeBean newFeeInfo = (FeeBean) this.serializer.deserialize(newData);
		Map fees = (Map) FeeUtil.getFeeMap();
		fees.put(newFeeInfo.getFeeServiceNO(), newFeeInfo);
		FeeUtil.setFeeMap(fees);
		flag = true;
		Res.log(Res.INFO, "新增收费服务类型" + newFeeInfo.toString() + "成功!");
		return flag;
	}

	/**
	 * 修改已经存在的费用服务信息
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
		Res.log(Res.INFO, "修改收费服务类型" + newFeeInfo.toString() + "成功!");
		return flag;
	}

	/**
	 * 移除已经存在的费用服务信息
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
		Res.log(Res.INFO, "删除收费服务类型" + newFeeInfo.getFeeServiceNO() + "成功!");
		return flag;
	}

	/**
	 * 服务注册
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
			Res.log(Res.INFO, "服务" + info.getServiceID() + "注册成功！");
		}
		flag = true;
		return flag;
	}

	/**
	 * 修改已注册的服务
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
			Res.log(Res.INFO, "管理平台尝试替换掉服务：" + oldInfo.getServiceID()
					+ ",但UMS Server并未接入该服务，或该服务已经被停止");
		}
		if (newInfo.getStatus() == ServiceInfo.SERVICE_STATUS_VALID) {// 修改现有服务内容或开启服务

			activeServiceMap.put(newInfo.getServiceID(), newInfo);
			Res.log(Res.INFO, "服务" + newInfo.getServiceID() + "修改成功！");
		}
		flag = true;
		return flag;
	}

	/**
	 * 移除已注册的服务
	 * 
	 * @param appID
	 *            登录的管理员ID
	 * @param appPassword
	 *            登录的管理员密码
	 * @param serviceID
	 *            移除的服务ID
	 * @return 成功或失败标记，true为成功，false为失败
	 */
	private boolean removeService(String newData) {
		// System.out.println(newData);
		ServiceInfo info = (ServiceInfo) serializer.deserialize(newData);
		boolean flag = false;
		Map activeServiceMap = AppServiceUtil.getActiveAppServiceMap();
		if (activeServiceMap.containsKey(info.getServiceID())) {
			activeServiceMap.remove(info.getServiceID());
			Res.log(Res.INFO, "服务" + info.getServiceID() + "删除成功！");
		} else {
			Res.log(Res.INFO, "管理平台尝试移除服务：" + info.getServiceID()
					+ ",但UMS Server并未接入该服务，或该服务已经被停止");
		}
		flag = true;
		return flag;
	}

	/**
	 * 新增注册应用
	 * 
	 * @param adminAppID
	 *            登录的管理员ID
	 * @param adminAppPsw
	 *            登录的管理员密码
	 * @param newAppID
	 *            新应用ID
	 * @param newAppPsw
	 *            新应用密码
	 * @param newAppName
	 *            新应用名称
	 * @param newAppStatus
	 *            新应用状态
	 * @return 成功或失败标记，true为成功，false为失败
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
	 * 修改已注册的应用
	 * 
	 * @param adminAppID
	 *            登录的管理员ID
	 * @param adminAppPsw
	 *            登录的管理员密码
	 * @param newAppID
	 *            修改后的应用ID
	 * @param newAppPsw
	 *            修改后的应用密码
	 * @param newAppName
	 *            修改后的应用名称
	 * @param newAppStatus
	 *            修改后的应用状态
	 * @return 成功或失败标记，true为成功，false为失败
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
			Res.log(Res.INFO, "应用" + newAppID + "修改成功！");
		}
		return flag;
	}

	/**
	 * 删除已注册的应用
	 * 
	 * @param adminAppID
	 *            登录的管理员ID
	 * @param adminAppPsw
	 *            登录的管理员密码
	 * @param removedAppID
	 *            删除的应用号
	 * @return 成功或失败标记，true为成功，false为失败
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
	 * 新增转发的内容
	 * 
	 * @param adminAppID
	 *            登录的管理员ID
	 * @param adminAppPsw
	 *            登录的管理员密码
	 * @param newForwardContent
	 *            新转发的内容
	 * @param forwardServiceID
	 *            新转发内容对应的服务ID
	 * @return 成功或失败标记，true为成功，false为失败
	 */
	private boolean addForwardContent(String newForwardContent,
			String forwardServiceID) {

		boolean flag = false;
		Map forwardServiceMap = AppServiceUtil.getForwardServiceMap();
		forwardServiceMap.put(newForwardContent, forwardServiceID);
		flag = true;
		Res.log(Res.INFO, "新增转发内容：" + newForwardContent + "成功");
		return flag;
	}

	/**
	 * 修改已有的转发内容
	 * 
	 * @param adminAppID
	 *            登录的管理员ID
	 * @param adminAppPsw
	 *            登录的管理员密码
	 * @param currentForwardContent
	 *            修改前的转发内容
	 * @param newForwardContent
	 *            新转发的内容
	 * @param forwardServiceID
	 *            原内容对应的服务ID
	 * @return 成功或失败标记，true为成功，false为失败
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
		Res.log(Res.INFO, "修改转发内容：" + newForwardContent + "成功");
		return flag;
	}

	/**
	 * 删除已有设置的转发内容
	 * 
	 * @param adminAppID
	 *            登录的管理员ID
	 * @param adminAppPsw
	 *            登录的管理员密码
	 * @param removedForwardContent
	 *            要删除的转发内容
	 * @return 成功或失败标记，true为成功，false为失败
	 */
	private boolean removeForwardContent(String removedForwardContent) {
		boolean flag = false;
		Map forwardServiceMap = AppServiceUtil.getForwardServiceMap();
		forwardServiceMap.remove(removedForwardContent);
		flag = true;
		Res.log(Res.INFO, "删除转发内容：" + removedForwardContent + "成功");
		return flag;
	}

	/**
	 * @功能 内部接口转发函数
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

		ForwardContentBean forward; // 转发对象
		FilterInfo filter; // 过滤对象
		MediaBean media; // 渠道对象
		switch (changeType) {
		case InterCommand.APP_ADD:
			// 新增应用
			this.addApplication(newData);
			break;
		case InterCommand.APP_DELETE:
			// 删除应用
			this.removeApplication(oldData);
			break;
		case InterCommand.APP_START:
			// 启动应用
			this.addApplication(newData);// 在UMS服务器中，start和stop类似add和remove
			break;
		case InterCommand.APP_STOP:
			// 停止应用
			this.removeApplication(newData);
			break;
		case InterCommand.APP_UPDATE:
			// 更新应用
			this.removeApplication(oldData);
			this.addApplication(newData);
			break;
		case InterCommand.FEE_ADD:
			// 新增费用
			this.addFeeService(newData);
			break;
		case InterCommand.FEE_DELETE:
			// 删除费用
			this.removeFeeService(oldData);
			break;
		case InterCommand.FEE_UPDATE:
			// 更新费用
			this.removeFeeService(oldData);
			addFeeService(newData);
			break;
		case InterCommand.FILTER_ADD:
			// 新增过滤
			filter = (FilterInfo) serializer.deserialize(newData);
			this.addFilter(filter.getContent());
			break;
		case InterCommand.FILTER_DELETE:
			// 删除过滤
			filter = (FilterInfo) serializer.deserialize(oldData);
			this.removeFilter(filter.getContent());
			break;
		case InterCommand.FILTER_START:
			// 启动过滤
			filter = (FilterInfo) serializer.deserialize(newData);
			this.addFilter(filter.getContent());
			break;
		case InterCommand.FILTER_STOP:
			// 停止过滤
			filter = (FilterInfo) serializer.deserialize(newData);
			this.removeFilter(filter.getContent());
			break;
		case InterCommand.FILTER_UPDATE:
			// 修改过滤
			filter = (FilterInfo) serializer.deserialize(oldData);
			this.removeFilter(filter.getContent());
			filter = (FilterInfo) serializer.deserialize(newData);
			this.addFilter(filter.getContent());
			break;
		case InterCommand.FORWARD_CONTENT_ADD:
			// 新建转发
			forward = (ForwardContentBean) serializer.deserialize(newData);
			this.addForwardContent(forward.getForwardContent(), forward
					.getServiceid());
			break;
		case InterCommand.FORWARD_CONTENT_DELETE:
			// 删除转发
			forward = (ForwardContentBean) serializer.deserialize(oldData);
			this.removeForwardContent(forward.getForwardContent());
			break;
		case InterCommand.FORWARD_CONTENT_START:
			// 启动转发
			forward = (ForwardContentBean) serializer.deserialize(newData);
			this.addForwardContent(forward.getForwardContent(), forward
					.getServiceid());
			break;
		case InterCommand.FORWARD_CONTENT_STOP:
			// 停止转发
			forward = (ForwardContentBean) serializer.deserialize(newData);
			this.removeForwardContent(forward.getForwardContent());
			break;
		case InterCommand.FORWARD_CONTENT_UPDATE:
			// 更新转发
			forward = (ForwardContentBean) serializer.deserialize(oldData);
			this.removeForwardContent(forward.getForwardContent());
			forward = (ForwardContentBean) serializer.deserialize(newData);
			this.addForwardContent(forward.getForwardContent(), forward
					.getServiceid());
			break;
		case InterCommand.MEDIA_ADD:
			// 新增渠道
//			media = (MediaBean) serializer.deserialize(newData);
//			try {
//				startChannel("DESKTOPADMIN", "", media.getMediaID(), media
//						.getMediaName(), media.getMediaType());
//			} catch (Exception e) {
//				Res.logExceptionTrace(e);
//			}
			break;
		case InterCommand.MEDIA_DELETE:
			// 删除渠道
			media = (MediaBean) serializer.deserialize(oldData);
			try {
				stopChannel("DESKTOPADMIN", "", media.getMediaID(), media
						.getMediaName(), media.getMediaType());
			} catch (Exception e) {
				Res.logExceptionTrace(e);
			}
			break;
		case InterCommand.MEDIA_START:
			// 启动渠道
			media = (MediaBean) serializer.deserialize(newData);
			try {
				startChannel("DESKTOPADMIN", "", media.getMediaID(), media
						.getMediaName(), media.getMediaType());
			} catch (Exception e) {
				Res.logExceptionTrace(e);
			}
			break;
		case InterCommand.MEDIA_STOP:
			// 停止渠道
			media = (MediaBean) serializer.deserialize(oldData);
			try {
				stopChannel("DESKTOPADMIN", "", media.getMediaID(), media
						.getMediaName(), media.getMediaType());
			} catch (Exception e) {
				Res.logExceptionTrace(e);
			}
			break;
		case InterCommand.MEDIA_UPDATE:
			// 更新渠道
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
			// 新增服务
			addService(newData);
			break;
		case InterCommand.SERVICE_DELETE:
			// 删除服务
			removeService(oldData);
			break;
		case InterCommand.SERVICE_START:
			// 启动服务
			addService(newData);
			break;
		case InterCommand.SERVICE_STOP:
			// 停止服务
			removeService(oldData);
			break;
		case InterCommand.SERVICE_UPDATE:
			// 修改服务
			removeService(oldData);
			addService(newData);
			break;
		default:
			break;
		}
		return true;
	}

}
