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
		Res.log(Res.INFO, "修改收费服务类型" + newFeeInfo.toString() + "成功!");
		return flag;
	}

	/**
	 * 移除已经存在的费用服务信息
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
		Res.log(Res.INFO, "删除收费服务类型" + feeServiceNO + "成功!");
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
			Res.log(Res.INFO, "服务" + serviceID + "注册成功！");
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
			Res.log(Res.INFO, "管理平台尝试替换掉服务：" + originalServiceID
					+ ",但UMS Server并未接入该服务，或该服务已经被停止");
		}
		if (status == ServiceInfo.SERVICE_STATUS_VALID) {// 修改现有服务内容或开启服务
			ServiceInfo info = new ServiceInfo();
			info.setAppID(serviceAPPID);
			info.setServiceID(currentServiceID);
			info.setServiceName(serviceName);
			activeServiceMap.put(currentServiceID, info);
			Res.log(Res.INFO, "服务" + currentServiceID + "修改成功！");
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
			Res.log(Res.INFO, "服务" + serviceID + "删除成功！");
		} else {
			Res.log(Res.INFO, "管理平台尝试移除服务：" + serviceID
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
		Res.log(Res.INFO, "删除转发内容：" + removedForwardContent + "成功");
		return flag;
	}

	/**
	 * 与LDAP同步
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
			Res.log(Res.ERROR, "LDAP用户信息同步失败");
			return false;
		}
		return true;
	}

	// /**
	// * 将LDAP的用户信息导入至UUM
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
	// Res.log(Res.ERROR, "LDAP UUM 同步时Socket无法打开!");
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
