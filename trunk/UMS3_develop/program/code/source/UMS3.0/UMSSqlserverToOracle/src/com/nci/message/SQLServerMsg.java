/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nci.message;

import java.util.Date;

/**
 *
 * @author Qil.Wong
 */
public class SQLServerMsg {

    private int ID;
    private String creatorID;
    private String taskName;
    private int smSendedNum;
    private String operationType;
    private String subOperationType;
    private int sendType;
    private String orgAddr;
    private String destAddr;
    private String sm_Content;
    private Date sendTime;
    private int needStateReport;
    private String serviceID;
    private String feeType;
    private String feeCode;
    private String msgID;
    private int smType = 0;
    private String messageID = "0";
    private int destAddrType = 0;
    private Date subTime;
    private int taskStatus;
    private int sendLevel;
    private int sendState;
    private int trytimes;
    private int count;
    private int successID;
    /**
     * ????????
     */
    public static final String OPERATION_TYPE_MOBILE_OFFICE = "WAS";
    /**
     * ????????
     */
    public static final String OPERATION_TYPE_SP_CUSTOM = "SPS";
    /**
     * ????
     */
    public static final int SENDTYPE_NORMAL = 1;
    /**
     * ????
     */
    public static final int SENDTYPE_GROUP = 2;
    /**
     * ???
     */
    public static final int SENDTYPE_POINT2POINT = 3;
    /**
     * ????
     */
    public static final int SENDTYPE_COMMON_BUSINESS = 4;
    /**
     * ??wap-push??
     */
    public static final int SENDTYPE_WAP_PUSH = 5;
    /**
     * ??????
     */
    public static final int NEED_STATE_REPORT_YES = 1;
    /**
     * ??????
     */
    public static final int NEED_STATE_REPORT_NO = 2;
    /**
     * ????
     */
    public static final int SMTYPE_NORMAL = 0;
    /**
     * ????
     */
    public static final int SMTYPE_ORDERED = 1;
    /**
     * ??????
     */
    public static final int SMTYPE_SUBSCRIBE_REVERSE = 3;
    /**
     * ????????
     */
    public static final int SMTYPE_CANCEL_SUBSCRIBE_REVERSE = 4;
    /**
     * ??????
     */
    public static final int SMTYPE_RESPONSE = 5;
    /**
     * ??????
     */
    public static final int DEST_ADDRTYPE_TRUE = 0;
    /**
     * ???CMPP3??
     */
    public static final int DEST_ADDRTYPE_FALSE = 1;
    /**
     * ????
     */
    public static final int SEND_LEVEL_HIGHEST = 0;
    /**
     * ???
     */
    public static final int SEND_LEVEL_HIGH = 1;
    /**
     * ???
     */
    public static final int SEND_LEVEL_NORMAL = 2;
    /**
     * ???
     */
    public static final int SEND_LEVEL_LOW = 3;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getSmSendedNum() {
        return smSendedNum;
    }

    public void setSmSendedNum(int smSendedNum) {
        this.smSendedNum = smSendedNum;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getSubOperationType() {
        return subOperationType;
    }

    public void setSubOperationType(String subOperationType) {
        this.subOperationType = subOperationType;
    }

    public int getSendType() {
        return sendType;
    }

    public void setSendType(int sendType) {
        this.sendType = sendType;
    }

    public String getOrgAddr() {
        return orgAddr;
    }

    public void setOrgAddr(String orgAddr) {
        this.orgAddr = orgAddr;
    }

    public String getDestAddr() {
        return destAddr;
    }

    public void setDestAddr(String destAddr) {
        this.destAddr = destAddr;
    }

    public String getSm_Content() {
        return sm_Content;
    }

    public void setSm_Content(String sm_Content) {
        this.sm_Content = sm_Content;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public int getNeedStateReport() {
        return needStateReport;
    }

    public void setNeedStateReport(int needStateReport) {
        this.needStateReport = needStateReport;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getFeeCode() {
        return feeCode;
    }

    public void setFeeCode(String feeCode) {
        this.feeCode = feeCode;
    }

    public String getMsgID() {
        return msgID;
    }

    public void setMsgID(String msgID) {
        this.msgID = msgID;
    }

    public int getSmType() {
        return smType;
    }

    public void setSmType(int smType) {
        this.smType = smType;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public int getDestAddrType() {
        return destAddrType;
    }

    public void setDestAddrType(int destAddrType) {
        this.destAddrType = destAddrType;
    }

    public Date getSubTime() {
        return subTime;
    }

    public void setSubTime(Date subTime) {
        this.subTime = subTime;
    }

    public int getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
    }

    public int getSendLevel() {
        return sendLevel;
    }

    public void setSendLevel(int sendLevel) {
        this.sendLevel = sendLevel;
    }

    public int getSendState() {
        return sendState;
    }

    public void setSendState(int sendState) {
        this.sendState = sendState;
    }

    public int getTrytimes() {
        return trytimes;
    }

    public void setTrytimes(int trytimes) {
        this.trytimes = trytimes;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getSuccessID() {
        return successID;
    }

    public void setSuccessID(int successID) {
        this.successID = successID;
    }
}
