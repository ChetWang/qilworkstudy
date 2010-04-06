package com.nci.ums.v3.message.basic;

import java.io.Serializable;

/**
 * <p>
 * Title: BasicMsg.java
 * </p>
 * <p>
 * Description: The basic information for an application service to send,
 * without the BasicMsg definition, application cannot send messages
 * successfully.
 * </p>
 * <p>
 * Copyright: 2007 Hangzhou NCI System Engineering， Ltd.
 * </p>
 * <p>
 * Company: Hangzhou NCI System Engineering， Ltd.
 * </p>
 * 
 * @author Qil.Wong Created in 2007.09.19
 * @version 1.0
 */
public class BasicMsg implements Serializable {

	private Participant sender = new Participant();
	private Participant[] receivers;
	private MsgContent msgContent = new MsgContent();
	// the number that application service creates and set into message.
	// it is used for message replying to map a unique original message.
	private String appSerialNO = "";
	private String serviceID = "";
	private int sendDirectly = -1;
	private String mediaID = "";
	private MsgAttachment[] msgAttachment;

	// date of message generated
	private String submitDate = "";
	// time of message generated
	private String submitTime = "";
	// 0-normal,1-urgent
	private int priority = -1;
	private int ack = -1;
	// flag of timer sending
	private int timeSetFlag = -1;
	// timer date
	private String setSendDate = "";
	// timer time
	private String setSendTime = "";

	// the invalid time and date for a set sending message
	private String invalidDate = "";
	private String invalidTime = "";
	private String replyDestination = "";
	private int needReply = -1;
	private String feeServiceNO = "";
	private int umsFlag = -1;
	private String companyID = "";
	private int contentMode = 15;
	public static final int UMSMsg_PRIORITY_NORMAL = 0;
	public static final int UMSMsg_PRIORITY_URGENT = 1;

	/**
	 * Acknowledge flag,the message needs send acknowledge from service
	 * provider, value is 2
	 */
	public static final int UMSMsg_ACK = 2;

	/**
	 * Acknowledge flag,the message needs connection acknowledge from ums, value
	 * is 1
	 */
	public static final int UMSMsg_ACK_YES = 1;
	/**
	 * Acknowledge flag,the message does not need acknowledge, value is 0
	 */
	public static final int UMSMsg_ACK_NO = 0;
	
	
	/**
	 * 回执已发送
	 */
	public static final int UMSMsg_ACK_ACKNOLEDGED = -1;
	/**
	 * 回执发送成功
	 */
	public static final int UMSMsg_ACK_SUCCESS = 9;

	/**
	 * 想要回执的消息所在的服务没有注册接收webservice
	 */
	public static final int UMSMsg_ACK_NO_SERVICE_REGESTERED = -3;

	/**
	 * 回执处理过程中和注册webservice交互产生错误
	 */
	public static final int UMSMsg_ACK_COMMUNICATION_ERROR = -2;
	
	/**
	 * Configure a timer for the message to be sent, the message will be sent at
	 * a specific time. Value is 1.
	 */
	public static final int BASICMSG_SENDTIME_CUSTOM = 1;
	/**
	 * No need to configure a timer for the message to be sent. Value is 0.
	 */
	public static final int BASICMSG_SENDTIME_NOCUSTOM = 0;
	public static final int BASICMSG_DIRECTSEND_YES = 1;
	public static final int BASICMSG_DIRECTSEND_NO = 0;
	public static final int BASICMSG_NEEDREPLY_YES = 1;
	public static final int BASICMSG_NEEDREPLY_NO = 0;
	/**
	 * indicate that this message will be sent into current UMS system
	 */
	public static final int BASICMSG_SELF_UMS = 1;
	/**
	 * indicate that this message will be sent other UMS system
	 */
	public static final int BASICMSG_OTHER_UMS = 2;
	public static final int BASICMSG_CONTENTE_MODE_GBK = 15;
	public static final int BASICMSG_CONTENTE_MODE_8859_1 = 0;
	public static final int BASICMSG_CONTENTE_MODE_UNICODE_BIG = -1;
	public static final int BASICMSG_CONTENTE_MODE_PDU = 21;
	public static final int BASICMSG_CONTENTE_MODE_PDU2 = 4;

	public BasicMsg() {
	}

	public BasicMsg(Participant sender, Participant[] receivers,
			MsgContent msgContent, String appSerialNO, String serviceID,
			int sendDirectly, String mediaID, MsgAttachment[] msgAttachment,
			String submitDate, String submitTime, int priority, int ack,
			int timeSetFlag, String setSendDate, String setSendTime,
			String invalidDate, String invalidTime, String replyDestination,
			int needReply, String feeServiceNO, int umsFlag, String companyID,
			int contentMode) {
		this.sender = sender;
		this.receivers = receivers;
		this.msgContent = msgContent;
		this.appSerialNO = appSerialNO;
		this.serviceID = serviceID;
		this.sendDirectly = sendDirectly;
		this.mediaID = mediaID;
		this.msgAttachment = msgAttachment;
		this.submitDate = submitDate;
		this.submitTime = submitTime;
		this.priority = priority;
		this.ack = ack;
		this.timeSetFlag = timeSetFlag;
		this.setSendDate = setSendDate;
		this.setSendTime = setSendTime;
		this.invalidDate = invalidDate;
		this.invalidTime = invalidTime;
		this.replyDestination = replyDestination;
		this.needReply = needReply;
		this.feeServiceNO = feeServiceNO;
		this.umsFlag = umsFlag;
		this.companyID = companyID;
		this.contentMode = contentMode;
	}

	public BasicMsg(Participant sender, Participant[] receivers,
			MsgContent msgContent, String appSerialNO, String serviceID,
			int sendDirectly, String mediaID, MsgAttachment[] msgAttachment,
			String submitDate, String submitTime, int priority, int ack,
			int timeSetFlag, String setSendDate, String setSendTime,
			String invalidDate, String invalidTime, String feeServiceNO,
			int umsFlag, String companyID) {
		this.sender = sender;
		this.receivers = receivers;
		this.msgContent = msgContent;
		this.appSerialNO = appSerialNO;
		this.serviceID = serviceID;
		this.sendDirectly = sendDirectly;
		this.mediaID = mediaID;
		this.msgAttachment = msgAttachment;
		this.submitDate = submitDate;
		this.submitTime = submitTime;
		this.priority = priority;
		this.ack = ack;
		this.timeSetFlag = timeSetFlag;
		this.setSendDate = setSendDate;
		this.setSendTime = setSendTime;
		this.invalidDate = invalidDate;
		this.invalidTime = invalidTime;
		this.feeServiceNO = feeServiceNO;
		this.umsFlag = umsFlag;
		this.companyID = companyID;
	}

	/**
	 * retrieve value of BasicMsg sender, a Participant Object, including
	 * Participant id and type
	 * 
	 * @return the sender
	 */
	public Participant getSender() {
		return sender;
	}

	/**
	 * set value of BasicMsg sender, a Participant Object, including Participant
	 * id and type
	 * 
	 * @param sender
	 *            the sender to set
	 */
	public void setSender(Participant sender) {
		this.sender = sender;
	}

	/**
	 * retrieve the BasicMsg receivers, an array Object, child attribute is
	 * Participant Object, including Participant id and type
	 * 
	 * @return the receivers
	 */
	public Participant[] getReceivers() {
		return receivers;
	}

	/**
	 * set value of BasicMsg receivers, an array Object, child attribute are
	 * Participant Objects, including Participant id and type
	 * 
	 * @param receivers
	 *            the receivers to set
	 */
	public void setReceivers(Participant[] receivers) {
		this.receivers = receivers;
	}

	/**
	 * retrieve BasicMsg content.
	 * 
	 * @return the content
	 */
	public MsgContent getMsgContent() {
		return msgContent;
	}

	/**
	 * set value of BasicMsg content
	 * 
	 * @param msgContent
	 *            the content to set
	 */
	public void setMsgContent(MsgContent msgContent) {
		this.msgContent = msgContent;
	}

	/**
	 * retrieve the channel which the message including BasicMsg will be sent
	 * with.
	 * 
	 * @return the media_id,channel id.
	 */
	public String getMediaID() {
		return mediaID;
	}

	/**
	 * set value of the channel id. This channel is used to send the message,and
	 * do not need channel policy.
	 * 
	 * @param mediaID
	 *            the media_id to set
	 */
	public void setMediaID(String mediaID) {
		this.mediaID = mediaID;
	}

	/**
	 * @return the msgAttachment
	 */
	public MsgAttachment[] getMsgAttachment() {
		return msgAttachment;
	}

	/**
	 * @param msgAttachment
	 *            the msgAttachment to set
	 */
	public void setMsgAttachment(MsgAttachment[] msgAttachment) {
		this.msgAttachment = msgAttachment;
	}

	/**
	 * @return the submitDate
	 */
	public String getSubmitDate() {
		return submitDate;
	}

	/**
	 * @param submitDate
	 *            the submitDate to set
	 */
	public void setSubmitDate(String submitDate) {
		this.submitDate = submitDate;
	}

	/**
	 * @return the submitTime
	 */
	public String getSubmitTime() {
		return submitTime;
	}

	/**
	 * @param submitTime
	 *            the submitTime to set
	 */
	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}

	/**
	 * @return the ack,1 = yes, 0 = no
	 */
	public int getAck() {
		return ack;
	}

	/**
	 * @param ack
	 *            the ack to set
	 */
	public void setAck(int ack) {
		this.ack = ack;
	}

	/**
	 * @return the setSendTimeFlag
	 */
	public int getTimeSetFlag() {
		return timeSetFlag;
	}

	/**
	 * @param timeSetFlag
	 *            the sendTimeFlag to set
	 */
	public void setTimeSetFlag(int timeSetFlag) {
		this.timeSetFlag = timeSetFlag;
	}

	/**
	 * @return the setSendDate
	 */
	public String getSetSendDate() {
		return setSendDate;
	}

	/**
	 * @param setSendDate
	 *            the setSendDate to set
	 */
	public void setSetSendDate(String setSendDate) {
		this.setSendDate = setSendDate;
	}

	/**
	 * @return the setSendTime
	 */
	public String getSetSendTime() {
		return setSendTime;
	}

	/**
	 * @param setSendTime
	 *            the setSendTime to set
	 */
	public void setSetSendTime(String setSendTime) {
		this.setSendTime = setSendTime;
	}

	/**
	 * @return the invalidDate
	 */
	public String getInvalidDate() {
		return invalidDate;
	}

	/**
	 * @param invalidDate
	 *            the invalidDate to set
	 */
	public void setInvalidDate(String invalidDate) {
		this.invalidDate = invalidDate;
	}

	/**
	 * @return the invalidTime
	 */
	public String getInvalidTime() {
		return invalidTime;
	}

	/**
	 * @param invalidTime
	 *            the invalidTime to set
	 */
	public void setInvalidTime(String invalidTime) {
		this.invalidTime = invalidTime;
	}

	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * @param priority
	 *            the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * @return the directSend
	 */
	public int getDirectSendFlag() {
		return sendDirectly;
	}

	/**
	 * @param sendDirectly
	 *            the directSend to set
	 */
	public void setDirectSendFlag(int sendDirectly) {
		this.sendDirectly = sendDirectly;
	}

	public int getUmsFlag() {
		return umsFlag;
	}

	public void setUmsFlag(int umsFlag) {
		this.umsFlag = umsFlag;
	}

	public String getCompanyID() {
		return companyID;
	}

	public void setCompanyID(String companyID) {
		this.companyID = companyID;
	}

	/**
	 * the number that application service creates and set into message. it is
	 * used for message replying to map a unique original message.
	 * 
	 * @return the appSerialNO
	 */
	public String getAppSerialNO() {
		return appSerialNO;
	}

	/**
	 * the number that application service creates and set into message. it is
	 * used for message replying to map a unique original message.
	 * 
	 * @param appSerialNO
	 *            the appSerialNO to set
	 */
	public void setAppSerialNO(String appSerialNO) {
		this.appSerialNO = appSerialNO;
	}

	/**
	 * @return the serviceID
	 */
	public String getServiceID() {
		return serviceID;
	}

	/**
	 * @param serviceID
	 *            the serviceID to set
	 */
	public void setServiceID(String serviceID) {
		this.serviceID = serviceID;
	}

	// /**
	// * @return the sendDirectly
	// */
	// public int getSendDirectly() {
	// return sendDirectly;
	// }
	//
	// /**
	// * @param sendDirectly the sendDirectly to set
	// */
	// public void setSendDirectly(int sendDirectly) {
	// this.sendDirectly = sendDirectly;
	// }
	/**
	 * @return the replyDestination
	 */
	public String getReplyDestination() {
		return replyDestination;
	}

	/**
	 * @param replyDestination
	 *            the replyDestination to set
	 */
	public void setReplyDestination(String replyDestination) {
		this.replyDestination = replyDestination;
	}

	/**
	 * @return the needReply
	 */
	public int getNeedReply() {
		return needReply;
	}

	/**
	 * @param needReply
	 *            the needReply to set
	 */
	public void setNeedReply(int needReply) {
		this.needReply = needReply;
	}

	/**
	 * @return the feeServiceNO
	 */
	public String getFeeServiceNO() {
		return feeServiceNO;
	}

	/**
	 * @param feeServiceNO
	 *            the feeServiceNO to set
	 */
	public void setFeeServiceNO(String feeServiceNO) {
		this.feeServiceNO = feeServiceNO;
	}

	/**
	 * get the encode type of a message content
	 * 
	 * @return int, like GBK,8859-1
	 */
	public int getContentMode() {
		return contentMode;
	}

	/**
	 * set the encode type of a message content
	 * 
	 * @param contentMode
	 *            , like GBK,8859-1
	 */
	public void setContentMode(int contentMode) {
		this.contentMode = contentMode;
	}

	public boolean equals(Object o) {
		if (o instanceof BasicMsg) {
			return ((BasicMsg) o).getAck() == this.getAck()
					&& ((BasicMsg) o).getAppSerialNO().equals(
							this.getAppSerialNO())
					&& ((BasicMsg) o).getCompanyID()
							.equals(this.getCompanyID())
					&& ((BasicMsg) o).getContentMode() == this.getContentMode()
					&& ((BasicMsg) o).getDirectSendFlag() == this
							.getDirectSendFlag()
					&& ((BasicMsg) o).getFeeServiceNO().equals(
							this.getFeeServiceNO())
					&& ((BasicMsg) o).getInvalidDate().equals(
							this.getInvalidDate())
					&& ((BasicMsg) o).getInvalidTime().equals(
							this.getInvalidTime())
					&& ((BasicMsg) o).getMediaID().equals(this.getMediaID())
					&& ((BasicMsg) o).getMsgAttachment().equals(
							this.getMsgAttachment())
					&& ((BasicMsg) o).getMsgContent().equals(
							this.getMsgContent())
					&& ((BasicMsg) o).getNeedReply() == this.getNeedReply()
					&& ((BasicMsg) o).getPriority() == this.getPriority()
					&& ((BasicMsg) o).getReceivers()
							.equals(this.getReceivers())
					&& ((BasicMsg) o).getReplyDestination().equals(
							this.getReplyDestination())
					&& ((BasicMsg) o).getSender().equals(this.getSender())
					&& ((BasicMsg) o).getServiceID()
							.equals(this.getServiceID())
					&& ((BasicMsg) o).getSetSendDate().equals(
							this.getSetSendDate())
					&& ((BasicMsg) o).getSetSendTime().equals(
							this.getSetSendTime())
					&& ((BasicMsg) o).getSubmitDate().equals(
							this.getSubmitDate())
					&& ((BasicMsg) o).getSubmitTime().equals(
							this.getSubmitTime())
					&& ((BasicMsg) o).getTimeSetFlag() == this.getTimeSetFlag()
					&& ((BasicMsg) o).getUmsFlag() == this.getUmsFlag();
		}
		return false;
	}
}
