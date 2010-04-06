package com.nci.ums.v3.service.impl;

import com.nci.ums.channel.inchannel.email.EmailMsgPlus;
import com.nci.ums.periphery.exception.OutOfMaxSequenceException;
import com.nci.ums.util.AppServiceUtil;
import com.nci.ums.util.DBUtil_V3;

import com.nci.ums.util.Res;

import com.nci.ums.v3.message.UMSMsg;
import com.nci.ums.v3.message.basic.BasicMsg;
import com.nci.ums.v3.message.basic.MsgAttachment;
import com.nci.ums.v3.message.basic.MsgContent;
import com.nci.ums.v3.message.basic.Participant;
import com.nci.ums.v3.service.ServiceInfo;
import com.nci.ums.v3.service.common.SendService;

import com.thoughtworks.xstream.XStream;
import java.sql.SQLException;

/**
 * 
 * <p>
 * Copyright: 2007 Hangzhou NCI System Engineering， Ltd.
 * </p>
 * <p>
 * Company: Hangzhou NCI System Engineering， Ltd.
 * </p>
 * 
 * @author Qil.Wong Created in 2007.09.22, since UMS3.0
 * @version 1.0
 */
// @WebService
public class UMSSendService implements SendService {

	/**
	 * XStream对象，用于序列化和反序列化消息对象
	 */
	public final XStream xstream;
	// private ActiveMQJMS activeMQ;
	// private UMSServiceParser serviceParser;
	/**
	 * UMS调度中心，所有的策略判断，有效性判断等都在调度中心发生作用
	 */
	private UMSCommandCenter commandCenter;
	/**
	 * 消息发送时，应用服务登录错误
	 */
	public static final String RETCODE_NOT_LOGIN = "WSV_NO_LOGIN";
	/**
	 * 消息发送时，数据库产生错误
	 */
	public static final String RETCODE_DATABASE_ERROR = "WSV_DB_ERR";
	/**
	 * 消息发送给UMS成功
	 */
	public static final String RETCODE_SEND_SUCCESS = "WSV_SUCCESS";
	/**
	 * 消息中包含非法的字符
	 */
	public static final String RETCODE_SEND_ILLEGAL_WORDS = "WSV_ILLEGAL";
	/**
	 * 消息的附件过大
	 */
	public static final String RETCODE_ATTACHMENT_TOOLARGE = "WSV_ATT_BIG";
	/**
	 * 直接发送的消息没有指定渠道
	 */
	public static final String RETCODE_CHANNEL_NEEDED = "WSV_NO_MEDIA";
	/**
	 * 发送过来的xml消息格式有错误
	 */
	public static final String RETCODE_SEND_ILLEGAL_XML = "WSV_XML_ERR";
	/**
	 * 单次消息发送的消息个数超过了最大值
	 */
	public static final String RETCODE_SEND_OUT_OF_MAX_COUNT = "WSV_MAX_ERR";
	/**
	 * 直接发送的消息所指定的渠道已经停止工作
	 */
	public static final String RETCODE_MEDIA_CHANNEL_STOPPED = "WSV_MEDIA_ST";
	/**
	 * 定时发送的消息指定的日期错误
	 */
	public static final String RETCODE_SET_DATE_INVALID = "WSV_DATE_ERR";
	/**
	 * 服务所在的应用已经被停止，无法使用
	 */
	public static final String RETCODE_APP_DISABLED = "WSV_APP_DISA";
	/**
	 * 不存在指定的应用
	 */
	public static final String RETCODE_APP_NOT_EXIST = "WSV_NO_APP";
	/**
	 * 应用所对应的密码错误
	 */
	public static final String RETCODE_APP_WRONG_PASSWORD = "WSV_PSW_ERR";

	/**
	 * 直接发送的短消息应用流水号大于4位
	 */
	public static final String RETCODE_APPSERIAL_MOBILE_LARGER_THAN_FOUR = "WSV_SRI_LAR";

	/**
	 * 直接发送的短消息应用流水号不是数字
	 */
	public static final String RETCODE_APPSERIAL_MOBILE_ISNOT_NUMBERIC = "WSV_SRI_ERR";

	/**
	 * 消息标题过长
	 */
	public static final String RETCODE_SUBJECT_TOO_LONG = "WSV_SUJ_LON";

	/**
	 * 消息内容过长
	 */
	public static final String RETCODE_CONTENT_TOO_LONG = "WSV_CON_LON";

	/**
	 * 服务错误
	 */
	public static final String RETCODE_SERVICE_ERROR = "WSV_SVC_ERR";

	public UMSSendService() {
		commandCenter = new UMSCommandCenter();
		// initialize xstream object to convert objects between xml String and
		// messages
		xstream = new XStream();
		xstream.setClassLoader(getClass().getClassLoader());
		xstream.alias("UMSMsg", UMSMsg.class);
		xstream.alias("BasicMsg", BasicMsg.class);
		xstream.alias("MsgAttachment", MsgAttachment.class);
		xstream.alias("MsgContent", MsgContent.class);
		xstream.alias("Participant", Participant.class);
		xstream.alias("EmailMsgPlus", EmailMsgPlus.class);
		// activeMQ = new ActiveMQJMS(JMSUtil.getJMSConnBean());
	}

	/**
	 * Check whether the application is login or not.
	 * 
	 * @return boolean true, login; false, not login.
	 */
	// private boolean isLogin() {
	// return loginFlag;
	// }
	// /**
	// * send the messages to jms server
	// *
	// * @return int result of messages sending in a transaction
	// */
	// private String sendToJMS(BasicMsg[] basicMsgs) {
	// String resultCode = activeMQ.produceMessage(basicMsgs);
	// return resultCode;
	// }
	/**
	 * 将收到的消息发送至UMS调度中心
	 * 
	 * @param basicMsgs
	 *            收到的消息集合，可以多于1个
	 * @return 消息发送给UMS的状态，可能是成功的，可能是失败的
	 */
	private String sendToUMS(BasicMsg[] basicMsgs) {
		String retCode = "";
		try {
			retCode = commandCenter.start(basicMsgs);
			return retCode;
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
			retCode = RETCODE_DATABASE_ERROR;
			Res.log(Res.ERROR, "：" + ex.getMessage());
			Res.logExceptionTrace(ex);
		} catch (OutOfMaxSequenceException ex) {
			retCode = RETCODE_SEND_OUT_OF_MAX_COUNT;
			System.out.println(ex.getMessage());
			Res.log(Res.ERROR, "消息一次发送数量超过最大次数：" + ex.getMessage());
			Res.logExceptionTrace(ex);
		}
		return retCode;
	}

	/**
	 * 指定消息发送给哪个server，这里是UMS服务器
	 * 
	 * @param basicMsgs
	 *            消息集合
	 * @return 消息发送的状态
	 */
	private String sendBasicMsgs(BasicMsg[] basicMsgs) {
		// TODO some special
		// return sendToJMS(umsMsgs);
		return sendToUMS(basicMsgs);

	}

	// //@WebMethod
	// public String send(BasicMsg[] basicMsgs) {
	// return sendBasicMsgs(basicMsgs);
	// }
	//
	// //@WebMethod
	// public String send(String[] basicMsgsString) {
	//
	// BasicMsg[] basicMsgs = new BasicMsg[basicMsgsString.length];
	// for (int i = 0; i < basicMsgsString.length; i++) {
	// basicMsgs[i] = (BasicMsg) xstream.fromXML(basicMsgsString[i]);
	// }
	// return sendBasicMsgs(basicMsgs);
	// }

	/**
	 * UMS暴露给外部应用发送消息的WebService接口。
	 * 该方法是同步有状态的方法，应用从发送到UMS接收，WebService都将一直保持连接，直到UMS返回给应用服务一个消息接收的状态值。
	 * 
	 * @param appID
	 *            发送消息的应用ID
	 * @param password
	 *            应用相应的密码
	 * @param basicMsgsXML
	 *            按照UMS标准发送的消息XML字符串
	 * @return 消息发送的状态反馈
	 */
	public String sendWithAck(String appID, String password, String basicMsgsXML) {
		String result = null;
		// 应用登录
		int loginFlag = DBUtil_V3.login(appID, password);
		if (loginFlag != DBUtil_V3.LOGIN_SUCCESS) {
			return DBUtil_V3.parseLoginErrResult(loginFlag);
		}
		try {
			// 将XML字符串序列化为BasicMsg集合对象
			BasicMsg[] basicMsgs = (BasicMsg[]) xstream.fromXML(basicMsgsXML);
			String checkResult = checkService(appID, basicMsgs);
			// 消息发送给UMS调度中心
			if (checkResult == null) {
				result = sendBasicMsgs(basicMsgs);
			} else {
				Res.log(Res.INFO, "非法的服务接入. APPID:" + appID + ",ServiceID:"
						+ checkResult);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Res.log(Res.ERROR, "应用" + appID + "传入的UMS消息体错误." + e.getMessage());
			// Res.logExceptionTrace(e);
			result = RETCODE_SEND_ILLEGAL_XML;
		}
		return result;
	}

	/**
	 * UMS暴露给外部应用发送消息的WebService接口。
	 * 该方法是异步无状态的方法，应用从接收到消息开始，就断开与UMS的WebService连接，
	 * 
	 * @param appID
	 *            发送消息的应用ID
	 * @param password
	 *            应用相应的秘密
	 * @param basicMsgsXML
	 *            按照UMS标准发送的消息XML字符串
	 */
	public void sendWithoutAck(final String appID, String password,
			final String basicMsgsXML) {
		int loginFlag = DBUtil_V3.login(appID, password);
		if (loginFlag != DBUtil_V3.LOGIN_SUCCESS) {
			Res.log(Res.INFO, "非法的应用接入：" + appID + ",或密码错误，无法通过验证!");
		}
		try {
			// 开启异步线程
			Thread ansy = new Thread(new Runnable() {
				public void run() {
					// 将XML字符串序列化为BasicMsg集合对象
					BasicMsg[] basicMsgs = (BasicMsg[]) xstream
							.fromXML(basicMsgsXML);
					String result = checkService(appID, basicMsgs);
					if (result == null) {
						try {
							// 消息发送给UMS调度中心
							new UMSCommandCenter().start(basicMsgs);
						} catch (SQLException e) {
							e.printStackTrace();
						} catch (OutOfMaxSequenceException e) {
							e.printStackTrace();
						}
					} else {
						Res.log(Res.INFO, "非法的服务接入. APPID:" + appID
								+ ",ServiceID:" + result);
					}
				}
			});
			ansy.start();
			// BasicMsg[] basicMsgs = (BasicMsg[])
			// xstream.fromXML(basicMsgsXML);
			// this.sendBasicMsgs(basicMsgs);
		} catch (Exception e) {
			e.printStackTrace();
			Res.log(Res.ERROR, "应用" + appID + "传入的UMS消息体错误." + e.getMessage());
			// Res.logExceptionTrace(e);
		}
	}

	private String checkService(String appid, BasicMsg[] basicMsgs) {
		//用于测试的app就不用做service检查了
		if(AppServiceUtil.isTestApp(appid) || appid.equals("DESKTOPADMIN")){
			return null;
		}
		String serviceID = null;
		for (int i = 0; i < basicMsgs.length; i++) {
			serviceID = basicMsgs[i].getServiceID();
			if (serviceID != null && !serviceID.trim().equals("")) {
				ServiceInfo serviceInfo = (ServiceInfo) AppServiceUtil
						.getActiveAppServiceMap().get(serviceID);
				if (serviceInfo != null) {
					if (!serviceInfo.getAppID().equals(appid)) {
						return serviceID;
					}
				}
			}
		}
		return null;
	}

	// public static void main(String args[]) {
	// String xml = "<BasicMsg-array> <BasicMsg> <sender>
	// <participantID>1001</participantID> <idType>5</idType>
	// <participantType>1</participantType> </sender> <receivers> <Participant>
	// <participantID>13957126889</participantID> <idType>1</idType>
	// <participantType>2</participantType> </Participant> </receivers>
	// <msgContent> <subject></subject> <content>测试短消息</content> </msgContent>
	// <appSerialNO></appSerialNO> <serviceID>3003</serviceID>
	// <sendDirectly>1</sendDirectly> <mediaID></mediaID>
	// <msgAttachment></msgAttachment> <submitDate>20081221</submitDate>
	// <submitTime>142109</submitTime> <priority>0</priority> <ack>0</ack>
	// <timeSetFlag>0</timeSetFlag> <setSendDate></setSendDate>
	// <setSendTime></setSendTime> <invalidDate></invalidDate>
	// <invalidTime></invalidTime> <replyDestination></replyDestination>
	// <needReply>0</needReply> <feeServiceNO></feeServiceNO>
	// <umsFlag>1</umsFlag> <companyID></companyID>
	// <contentMode>15</contentMode> </BasicMsg></BasicMsg-array>";
	// UMSSendService send = new UMSSendService();
	// send.sendWithAck("", "", xml);
	// }
}
