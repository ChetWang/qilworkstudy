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
 * Copyright: 2007 Hangzhou NCI System Engineering�� Ltd.
 * </p>
 * <p>
 * Company: Hangzhou NCI System Engineering�� Ltd.
 * </p>
 * 
 * @author Qil.Wong Created in 2007.09.22, since UMS3.0
 * @version 1.0
 */
// @WebService
public class UMSSendService implements SendService {

	/**
	 * XStream�����������л��ͷ����л���Ϣ����
	 */
	public final XStream xstream;
	// private ActiveMQJMS activeMQ;
	// private UMSServiceParser serviceParser;
	/**
	 * UMS�������ģ����еĲ����жϣ���Ч���жϵȶ��ڵ������ķ�������
	 */
	private UMSCommandCenter commandCenter;
	/**
	 * ��Ϣ����ʱ��Ӧ�÷����¼����
	 */
	public static final String RETCODE_NOT_LOGIN = "WSV_NO_LOGIN";
	/**
	 * ��Ϣ����ʱ�����ݿ��������
	 */
	public static final String RETCODE_DATABASE_ERROR = "WSV_DB_ERR";
	/**
	 * ��Ϣ���͸�UMS�ɹ�
	 */
	public static final String RETCODE_SEND_SUCCESS = "WSV_SUCCESS";
	/**
	 * ��Ϣ�а����Ƿ����ַ�
	 */
	public static final String RETCODE_SEND_ILLEGAL_WORDS = "WSV_ILLEGAL";
	/**
	 * ��Ϣ�ĸ�������
	 */
	public static final String RETCODE_ATTACHMENT_TOOLARGE = "WSV_ATT_BIG";
	/**
	 * ֱ�ӷ��͵���Ϣû��ָ������
	 */
	public static final String RETCODE_CHANNEL_NEEDED = "WSV_NO_MEDIA";
	/**
	 * ���͹�����xml��Ϣ��ʽ�д���
	 */
	public static final String RETCODE_SEND_ILLEGAL_XML = "WSV_XML_ERR";
	/**
	 * ������Ϣ���͵���Ϣ�������������ֵ
	 */
	public static final String RETCODE_SEND_OUT_OF_MAX_COUNT = "WSV_MAX_ERR";
	/**
	 * ֱ�ӷ��͵���Ϣ��ָ���������Ѿ�ֹͣ����
	 */
	public static final String RETCODE_MEDIA_CHANNEL_STOPPED = "WSV_MEDIA_ST";
	/**
	 * ��ʱ���͵���Ϣָ�������ڴ���
	 */
	public static final String RETCODE_SET_DATE_INVALID = "WSV_DATE_ERR";
	/**
	 * �������ڵ�Ӧ���Ѿ���ֹͣ���޷�ʹ��
	 */
	public static final String RETCODE_APP_DISABLED = "WSV_APP_DISA";
	/**
	 * ������ָ����Ӧ��
	 */
	public static final String RETCODE_APP_NOT_EXIST = "WSV_NO_APP";
	/**
	 * Ӧ������Ӧ���������
	 */
	public static final String RETCODE_APP_WRONG_PASSWORD = "WSV_PSW_ERR";

	/**
	 * ֱ�ӷ��͵Ķ���ϢӦ����ˮ�Ŵ���4λ
	 */
	public static final String RETCODE_APPSERIAL_MOBILE_LARGER_THAN_FOUR = "WSV_SRI_LAR";

	/**
	 * ֱ�ӷ��͵Ķ���ϢӦ����ˮ�Ų�������
	 */
	public static final String RETCODE_APPSERIAL_MOBILE_ISNOT_NUMBERIC = "WSV_SRI_ERR";

	/**
	 * ��Ϣ�������
	 */
	public static final String RETCODE_SUBJECT_TOO_LONG = "WSV_SUJ_LON";

	/**
	 * ��Ϣ���ݹ���
	 */
	public static final String RETCODE_CONTENT_TOO_LONG = "WSV_CON_LON";

	/**
	 * �������
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
	 * ���յ�����Ϣ������UMS��������
	 * 
	 * @param basicMsgs
	 *            �յ�����Ϣ���ϣ����Զ���1��
	 * @return ��Ϣ���͸�UMS��״̬�������ǳɹ��ģ�������ʧ�ܵ�
	 */
	private String sendToUMS(BasicMsg[] basicMsgs) {
		String retCode = "";
		try {
			retCode = commandCenter.start(basicMsgs);
			return retCode;
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
			retCode = RETCODE_DATABASE_ERROR;
			Res.log(Res.ERROR, "��" + ex.getMessage());
			Res.logExceptionTrace(ex);
		} catch (OutOfMaxSequenceException ex) {
			retCode = RETCODE_SEND_OUT_OF_MAX_COUNT;
			System.out.println(ex.getMessage());
			Res.log(Res.ERROR, "��Ϣһ�η�������������������" + ex.getMessage());
			Res.logExceptionTrace(ex);
		}
		return retCode;
	}

	/**
	 * ָ����Ϣ���͸��ĸ�server��������UMS������
	 * 
	 * @param basicMsgs
	 *            ��Ϣ����
	 * @return ��Ϣ���͵�״̬
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
	 * UMS��¶���ⲿӦ�÷�����Ϣ��WebService�ӿڡ�
	 * �÷�����ͬ����״̬�ķ�����Ӧ�ôӷ��͵�UMS���գ�WebService����һֱ�������ӣ�ֱ��UMS���ظ�Ӧ�÷���һ����Ϣ���յ�״ֵ̬��
	 * 
	 * @param appID
	 *            ������Ϣ��Ӧ��ID
	 * @param password
	 *            Ӧ����Ӧ������
	 * @param basicMsgsXML
	 *            ����UMS��׼���͵���ϢXML�ַ���
	 * @return ��Ϣ���͵�״̬����
	 */
	public String sendWithAck(String appID, String password, String basicMsgsXML) {
		String result = null;
		// Ӧ�õ�¼
		int loginFlag = DBUtil_V3.login(appID, password);
		if (loginFlag != DBUtil_V3.LOGIN_SUCCESS) {
			return DBUtil_V3.parseLoginErrResult(loginFlag);
		}
		try {
			// ��XML�ַ������л�ΪBasicMsg���϶���
			BasicMsg[] basicMsgs = (BasicMsg[]) xstream.fromXML(basicMsgsXML);
			String checkResult = checkService(appID, basicMsgs);
			// ��Ϣ���͸�UMS��������
			if (checkResult == null) {
				result = sendBasicMsgs(basicMsgs);
			} else {
				Res.log(Res.INFO, "�Ƿ��ķ������. APPID:" + appID + ",ServiceID:"
						+ checkResult);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Res.log(Res.ERROR, "Ӧ��" + appID + "�����UMS��Ϣ�����." + e.getMessage());
			// Res.logExceptionTrace(e);
			result = RETCODE_SEND_ILLEGAL_XML;
		}
		return result;
	}

	/**
	 * UMS��¶���ⲿӦ�÷�����Ϣ��WebService�ӿڡ�
	 * �÷������첽��״̬�ķ�����Ӧ�ôӽ��յ���Ϣ��ʼ���ͶϿ���UMS��WebService���ӣ�
	 * 
	 * @param appID
	 *            ������Ϣ��Ӧ��ID
	 * @param password
	 *            Ӧ����Ӧ������
	 * @param basicMsgsXML
	 *            ����UMS��׼���͵���ϢXML�ַ���
	 */
	public void sendWithoutAck(final String appID, String password,
			final String basicMsgsXML) {
		int loginFlag = DBUtil_V3.login(appID, password);
		if (loginFlag != DBUtil_V3.LOGIN_SUCCESS) {
			Res.log(Res.INFO, "�Ƿ���Ӧ�ý��룺" + appID + ",����������޷�ͨ����֤!");
		}
		try {
			// �����첽�߳�
			Thread ansy = new Thread(new Runnable() {
				public void run() {
					// ��XML�ַ������л�ΪBasicMsg���϶���
					BasicMsg[] basicMsgs = (BasicMsg[]) xstream
							.fromXML(basicMsgsXML);
					String result = checkService(appID, basicMsgs);
					if (result == null) {
						try {
							// ��Ϣ���͸�UMS��������
							new UMSCommandCenter().start(basicMsgs);
						} catch (SQLException e) {
							e.printStackTrace();
						} catch (OutOfMaxSequenceException e) {
							e.printStackTrace();
						}
					} else {
						Res.log(Res.INFO, "�Ƿ��ķ������. APPID:" + appID
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
			Res.log(Res.ERROR, "Ӧ��" + appID + "�����UMS��Ϣ�����." + e.getMessage());
			// Res.logExceptionTrace(e);
		}
	}

	private String checkService(String appid, BasicMsg[] basicMsgs) {
		//���ڲ��Ե�app�Ͳ�����service�����
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
	// <msgContent> <subject></subject> <content>���Զ���Ϣ</content> </msgContent>
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
