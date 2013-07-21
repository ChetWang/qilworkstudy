package org.vlg.linghu.mybatis.bean;

import java.io.Serializable;
import java.util.Date;

public class MmsSendMessage implements Serializable {
    private Integer sendId;

    private String sendTitle;

    private Date sendAddtime;

    private String sendPic;

    private String sendMusic;

    private Integer sendSmilParnum;

    private Integer userId;

    private String userName;

    private String userMobile;

    private Integer sendScore;

    private String sendMobile;

    private Integer sendStatus;

    private Date sendDowntime;

    private String sendCode;

    private String msgid;

    private String linkid;

    private String backMsg;

    private Integer sendIsdel;

    private Integer indexId;

    private String serviceid;

    private static final long serialVersionUID = 1L;

    public Integer getSendId() {
        return sendId;
    }

    public void setSendId(Integer sendId) {
        this.sendId = sendId;
    }

    public String getSendTitle() {
        return sendTitle;
    }

    public void setSendTitle(String sendTitle) {
        this.sendTitle = sendTitle == null ? null : sendTitle.trim();
    }

    public Date getSendAddtime() {
        return sendAddtime;
    }

    public void setSendAddtime(Date sendAddtime) {
        this.sendAddtime = sendAddtime;
    }

    public String getSendPic() {
        return sendPic;
    }

    public void setSendPic(String sendPic) {
        this.sendPic = sendPic == null ? null : sendPic.trim();
    }

    public String getSendMusic() {
        return sendMusic;
    }

    public void setSendMusic(String sendMusic) {
        this.sendMusic = sendMusic == null ? null : sendMusic.trim();
    }

    public Integer getSendSmilParnum() {
        return sendSmilParnum;
    }

    public void setSendSmilParnum(Integer sendSmilParnum) {
        this.sendSmilParnum = sendSmilParnum;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile == null ? null : userMobile.trim();
    }

    public Integer getSendScore() {
        return sendScore;
    }

    public void setSendScore(Integer sendScore) {
        this.sendScore = sendScore;
    }

    public String getSendMobile() {
        return sendMobile;
    }

    public void setSendMobile(String sendMobile) {
        this.sendMobile = sendMobile == null ? null : sendMobile.trim();
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

    public String getSendCode() {
        return sendCode;
    }

    public void setSendCode(String sendCode) {
        this.sendCode = sendCode == null ? null : sendCode.trim();
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid == null ? null : msgid.trim();
    }

    public String getLinkid() {
        return linkid;
    }

    public void setLinkid(String linkid) {
        this.linkid = linkid == null ? null : linkid.trim();
    }

    public String getBackMsg() {
        return backMsg;
    }

    public void setBackMsg(String backMsg) {
        this.backMsg = backMsg == null ? null : backMsg.trim();
    }

    public Integer getSendIsdel() {
        return sendIsdel;
    }

    public void setSendIsdel(Integer sendIsdel) {
        this.sendIsdel = sendIsdel;
    }

    public Integer getIndexId() {
        return indexId;
    }

    public void setIndexId(Integer indexId) {
        this.indexId = indexId;
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
        MmsSendMessage other = (MmsSendMessage) that;
        return (this.getSendId() == null ? other.getSendId() == null : this.getSendId().equals(other.getSendId()))
            && (this.getSendTitle() == null ? other.getSendTitle() == null : this.getSendTitle().equals(other.getSendTitle()))
            && (this.getSendAddtime() == null ? other.getSendAddtime() == null : this.getSendAddtime().equals(other.getSendAddtime()))
            && (this.getSendPic() == null ? other.getSendPic() == null : this.getSendPic().equals(other.getSendPic()))
            && (this.getSendMusic() == null ? other.getSendMusic() == null : this.getSendMusic().equals(other.getSendMusic()))
            && (this.getSendSmilParnum() == null ? other.getSendSmilParnum() == null : this.getSendSmilParnum().equals(other.getSendSmilParnum()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getUserName() == null ? other.getUserName() == null : this.getUserName().equals(other.getUserName()))
            && (this.getUserMobile() == null ? other.getUserMobile() == null : this.getUserMobile().equals(other.getUserMobile()))
            && (this.getSendScore() == null ? other.getSendScore() == null : this.getSendScore().equals(other.getSendScore()))
            && (this.getSendMobile() == null ? other.getSendMobile() == null : this.getSendMobile().equals(other.getSendMobile()))
            && (this.getSendStatus() == null ? other.getSendStatus() == null : this.getSendStatus().equals(other.getSendStatus()))
            && (this.getSendDowntime() == null ? other.getSendDowntime() == null : this.getSendDowntime().equals(other.getSendDowntime()))
            && (this.getSendCode() == null ? other.getSendCode() == null : this.getSendCode().equals(other.getSendCode()))
            && (this.getMsgid() == null ? other.getMsgid() == null : this.getMsgid().equals(other.getMsgid()))
            && (this.getLinkid() == null ? other.getLinkid() == null : this.getLinkid().equals(other.getLinkid()))
            && (this.getBackMsg() == null ? other.getBackMsg() == null : this.getBackMsg().equals(other.getBackMsg()))
            && (this.getSendIsdel() == null ? other.getSendIsdel() == null : this.getSendIsdel().equals(other.getSendIsdel()))
            && (this.getIndexId() == null ? other.getIndexId() == null : this.getIndexId().equals(other.getIndexId()))
            && (this.getServiceid() == null ? other.getServiceid() == null : this.getServiceid().equals(other.getServiceid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getSendId() == null) ? 0 : getSendId().hashCode());
        result = prime * result + ((getSendTitle() == null) ? 0 : getSendTitle().hashCode());
        result = prime * result + ((getSendAddtime() == null) ? 0 : getSendAddtime().hashCode());
        result = prime * result + ((getSendPic() == null) ? 0 : getSendPic().hashCode());
        result = prime * result + ((getSendMusic() == null) ? 0 : getSendMusic().hashCode());
        result = prime * result + ((getSendSmilParnum() == null) ? 0 : getSendSmilParnum().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getUserName() == null) ? 0 : getUserName().hashCode());
        result = prime * result + ((getUserMobile() == null) ? 0 : getUserMobile().hashCode());
        result = prime * result + ((getSendScore() == null) ? 0 : getSendScore().hashCode());
        result = prime * result + ((getSendMobile() == null) ? 0 : getSendMobile().hashCode());
        result = prime * result + ((getSendStatus() == null) ? 0 : getSendStatus().hashCode());
        result = prime * result + ((getSendDowntime() == null) ? 0 : getSendDowntime().hashCode());
        result = prime * result + ((getSendCode() == null) ? 0 : getSendCode().hashCode());
        result = prime * result + ((getMsgid() == null) ? 0 : getMsgid().hashCode());
        result = prime * result + ((getLinkid() == null) ? 0 : getLinkid().hashCode());
        result = prime * result + ((getBackMsg() == null) ? 0 : getBackMsg().hashCode());
        result = prime * result + ((getSendIsdel() == null) ? 0 : getSendIsdel().hashCode());
        result = prime * result + ((getIndexId() == null) ? 0 : getIndexId().hashCode());
        result = prime * result + ((getServiceid() == null) ? 0 : getServiceid().hashCode());
        return result;
    }
}