/**
 * <p>Title: MsgInfo.java</p>
 * <p>Description:
 *短消息内容类，供滑动窗口使用。
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering， Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering， Ltd.</p>
 * Date        Author      Changes
 * NOV. 19 2003   张志勇        Created
 * @version 1.0
 */

package com.nci.ums.channel.outchannel;

import java.util.ArrayList; 
import java.util.List; 


public  class MsgInfo
 {
    //外发所需信息
    private String recvID;
    private String sendID;
    private String content;

    //定位数据库所需信息
    private String batchNO;
    private int serialNO;
    
    private int sequenceNO;
    private int ack;
    private String subApp;
    //状态
    private int status;
    private int count;
    private int retCode;

    //命令序号
    private int seq;

    //其它信息

     private String appId;
     private String appSerialNo;
     private String mediaId;
     private String oldSendId;
     private String submitDate;
     private String submitTime;
    private int rep;
    private String priority;
    private String batchMode;
    private int contentMode;
    private String timeSetFlag;
    private String setDate;
    private String setTime;
    private String InvalidDate;
    private String InvalidTime;
    private String replyDes;
    private String reply;
    private int feeType;
    private int fee;
    private List file = new ArrayList(); 

    public MsgInfo()
    {
    }

    public String getBatchNO() {
        return batchNO;
    }

    public void setBatchNO(String batchNO) {
        this.batchNO = batchNO;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRecvID() {
        return recvID;
    }

    public void setRecvID(String recvID) {
        this.recvID = recvID;
    }

    public String getSendID() {
        return sendID;
    }

    public void setSendID(String sendID) {
        this.sendID = sendID;
    }

    public int getSequenceNO() {
        return sequenceNO;
    }

    public void setSequenceNO(int sequenceNO) {
        this.sequenceNO = sequenceNO;
    }

    public int getSerialNO() {
        return serialNO;
    }

    public void setSerialNO(int serialNO) {
        this.serialNO = serialNO;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getAck() {
        return ack;
    }

    public void setAck(int ack) {
        this.ack = ack;
    }

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSerialNo() {
        return appSerialNo;
    }

    public void setAppSerialNo(String appSerialNo) {
        this.appSerialNo = appSerialNo;
    }

    public String getBatchMode() {
        return batchMode;
    }

    public void setBatchMode(String batchMode) {
        this.batchMode = batchMode;
    }

    public int getContentMode() {
        return contentMode;
    }

    public void setContentMode(int contentMode) {
        this.contentMode = contentMode;
    }

    public String getInvalidDate() {
        return InvalidDate;
    }

    public void setInvalidDate(String invalidDate) {
        InvalidDate = invalidDate;
    }

    public String getInvalidTime() {
        return InvalidTime;
    }

    public void setInvalidTime(String invalidTime) {
        InvalidTime = invalidTime;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getOldSendId() {
        return oldSendId;
    }

    public void setOldSendId(String oldSendId) {
        this.oldSendId = oldSendId;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public int getRep() {
        return rep;
    }

    public void setRep(int rep) {
        this.rep = rep;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getReplyDes() {
        return replyDes;
    }

    public void setReplyDes(String replyDes) {
        this.replyDes = replyDes;
    }

    public String getSetDate() {
        return setDate;
    }

    public void setSetDate(String setDate) {
        this.setDate = setDate;
    }

    public String getSetTime() {
        return setTime;
    }

    public void setSetTime(String setTime) {
        this.setTime = setTime;
    }

    public String getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(String submitDate) {
        this.submitDate = submitDate;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public String getTimeSetFlag() {
        return timeSetFlag;
    }

    public void setTimeSetFlag(String timeSetFlag) {
        this.timeSetFlag = timeSetFlag;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getFeeType() {
        return feeType;
    }

    public void setFeeType(int feeType) {
        this.feeType = feeType;
    }
    
	/**
	 * @return Returns the subApp.
	 */
	public String getSubApp() {
		return subApp;
	}
	/**
	 * @param subApp The subApp to set.
	 */
	public void setSubApp(String subApp) {
		this.subApp = subApp;
	}
	
 
	/**
	 * @return Returns the file.
	 */
	public List getFile() {
		return file;
	}
	/**
	 * @param file The file to set.
	 */
	public void setFile(List file) {
		this.file = file;
	}
}