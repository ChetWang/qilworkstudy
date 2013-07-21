package org.vlg.linghu.mybatis.bean;

import java.io.Serializable;
import java.util.Date;

public class SmsReceiveMessage implements Serializable {
    private Integer receiveId;

    private Date receiveAddtime;

    private String receiveText;

    private String userId;

    private Boolean isok;

    private String serviceid;

    private static final long serialVersionUID = 1L;

    public Integer getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(Integer receiveId) {
        this.receiveId = receiveId;
    }

    public Date getReceiveAddtime() {
        return receiveAddtime;
    }

    public void setReceiveAddtime(Date receiveAddtime) {
        this.receiveAddtime = receiveAddtime;
    }

    public String getReceiveText() {
        return receiveText;
    }

    public void setReceiveText(String receiveText) {
        this.receiveText = receiveText == null ? null : receiveText.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Boolean getIsok() {
        return isok;
    }

    public void setIsok(Boolean isok) {
        this.isok = isok;
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
        SmsReceiveMessage other = (SmsReceiveMessage) that;
        return (this.getReceiveId() == null ? other.getReceiveId() == null : this.getReceiveId().equals(other.getReceiveId()))
            && (this.getReceiveAddtime() == null ? other.getReceiveAddtime() == null : this.getReceiveAddtime().equals(other.getReceiveAddtime()))
            && (this.getReceiveText() == null ? other.getReceiveText() == null : this.getReceiveText().equals(other.getReceiveText()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getIsok() == null ? other.getIsok() == null : this.getIsok().equals(other.getIsok()))
            && (this.getServiceid() == null ? other.getServiceid() == null : this.getServiceid().equals(other.getServiceid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getReceiveId() == null) ? 0 : getReceiveId().hashCode());
        result = prime * result + ((getReceiveAddtime() == null) ? 0 : getReceiveAddtime().hashCode());
        result = prime * result + ((getReceiveText() == null) ? 0 : getReceiveText().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getIsok() == null) ? 0 : getIsok().hashCode());
        result = prime * result + ((getServiceid() == null) ? 0 : getServiceid().hashCode());
        return result;
    }
}