package org.vlg.linghu.mybatis.bean;

import java.io.Serializable;
import java.util.Date;

public class SmsSendMessage implements Serializable {
    private Integer sendId;

    private String userId;

    private String sendText;

    private Date sendAddtime;

    private Integer sendStatus;

    private Date sendDowntime;

    private String msgid;

    private String backMsg;

    private Boolean issend;

    private String serviceid;

    private static final long serialVersionUID = 1L;

    public Integer getSendId() {
        return sendId;
    }

    public void setSendId(Integer sendId) {
        this.sendId = sendId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getSendText() {
        return sendText;
    }

    public void setSendText(String sendText) {
        this.sendText = sendText == null ? null : sendText.trim();
    }

    public Date getSendAddtime() {
        return sendAddtime;
    }

    public void setSendAddtime(Date sendAddtime) {
        this.sendAddtime = sendAddtime;
    }

    public Integer getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
    }

    public Date getSendDowntime() {
        return sendDowntime;
    }

    public void setSendDowntime(Date sendDowntime) {
        this.sendDowntime = sendDowntime;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid == null ? null : msgid.trim();
    }

    public String getBackMsg() {
        return backMsg;
    }

    public void setBackMsg(String backMsg) {
        this.backMsg = backMsg == null ? null : backMsg.trim();
    }

    public Boolean getIssend() {
        return issend;
    }

    public void setIssend(Boolean issend) {
        this.issend = issend;
    }

    public String getServiceid() {
        return serviceid;
    }

    public void setServiceid(String serviceid) {
        this.serviceid = serviceid == null ? null : serviceid.trim();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        SmsSendMessage other = (SmsSendMessage) that;
        return (this.getSendId() == null ? other.getSendId() == null : this.getSendId().equals(other.getSendId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getSendText() == null ? other.getSendText() == null : this.getSendText().equals(other.getSendText()))
            && (this.getSendAddtime() == null ? other.getSendAddtime() == null : this.getSendAddtime().equals(other.getSendAddtime()))
            && (this.getSendStatus() == null ? other.getSendStatus() == null : this.getSendStatus().equals(other.getSendStatus()))
            && (this.getSendDowntime() == null ? other.getSendDowntime() == null : this.getSendDowntime().equals(other.getSendDowntime()))
            && (this.getMsgid() == null ? other.getMsgid() == null : this.getMsgid().equals(other.getMsgid()))
            && (this.getBackMsg() == null ? other.getBackMsg() == null : this.getBackMsg().equals(other.getBackMsg()))
            && (this.getIssend() == null ? other.getIssend() == null : this.getIssend().equals(other.getIssend()))
            && (this.getServiceid() == null ? other.getServiceid() == null : this.getServiceid().equals(other.getServiceid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getSendId() == null) ? 0 : getSendId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getSendText() == null) ? 0 : getSendText().hashCode());
        result = prime * result + ((getSendAddtime() == null) ? 0 : getSendAddtime().hashCode());
        result = prime * result + ((getSendStatus() == null) ? 0 : getSendStatus().hashCode());
        result = prime * result + ((getSendDowntime() == null) ? 0 : getSendDowntime().hashCode());
        result = prime * result + ((getMsgid() == null) ? 0 : getMsgid().hashCode());
        result = prime * result + ((getBackMsg() == null) ? 0 : getBackMsg().hashCode());
        result = prime * result + ((getIssend() == null) ? 0 : getIssend().hashCode());
        result = prime * result + ((getServiceid() == null) ? 0 : getServiceid().hashCode());
        return result;
    }
}