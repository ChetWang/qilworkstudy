package org.vlg.linghu.mybatis.bean;

import java.io.Serializable;
import java.util.Date;

public class VacReceiveMessage implements Serializable {
    private Integer vacId;

    private Date vacAddtime;

    private String recordsequenceid;

    private Integer useridtype;

    private String userid;

    private String servicetype;

    private String spid;

    private String productid;

    private Integer updatetype;

    private String updatetime;

    private String updatedesc;

    private String linkid;

    private String content;

    private String effectivedate;

    private String expiredate;

    private Integer resultcode;

    private String timeStamp;

    private String encodestr;

    private Integer userId;

    private String userName;

    private Boolean isputwelcomesms;

    private static final long serialVersionUID = 1L;

    public Integer getVacId() {
        return vacId;
    }

    public void setVacId(Integer vacId) {
        this.vacId = vacId;
    }

    public Date getVacAddtime() {
        return vacAddtime;
    }

    public void setVacAddtime(Date vacAddtime) {
        this.vacAddtime = vacAddtime;
    }

    public String getRecordsequenceid() {
        return recordsequenceid;
    }

    public void setRecordsequenceid(String recordsequenceid) {
        this.recordsequenceid = recordsequenceid == null ? null : recordsequenceid.trim();
    }

    public Integer getUseridtype() {
        return useridtype;
    }

    public void setUseridtype(Integer useridtype) {
        this.useridtype = useridtype;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    public String getServicetype() {
        return servicetype;
    }

    public void setServicetype(String servicetype) {
        this.servicetype = servicetype == null ? null : servicetype.trim();
    }

    public String getSpid() {
        return spid;
    }

    public void setSpid(String spid) {
        this.spid = spid == null ? null : spid.trim();
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid == null ? null : productid.trim();
    }

    public Integer getUpdatetype() {
        return updatetype;
    }

    public void setUpdatetype(Integer updatetype) {
        this.updatetype = updatetype;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime == null ? null : updatetime.trim();
    }

    public String getUpdatedesc() {
        return updatedesc;
    }

    public void setUpdatedesc(String updatedesc) {
        this.updatedesc = updatedesc == null ? null : updatedesc.trim();
    }

    public String getLinkid() {
        return linkid;
    }

    public void setLinkid(String linkid) {
        this.linkid = linkid == null ? null : linkid.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getEffectivedate() {
        return effectivedate;
    }

    public void setEffectivedate(String effectivedate) {
        this.effectivedate = effectivedate == null ? null : effectivedate.trim();
    }

    public String getExpiredate() {
        return expiredate;
    }

    public void setExpiredate(String expiredate) {
        this.expiredate = expiredate == null ? null : expiredate.trim();
    }

    public Integer getResultcode() {
        return resultcode;
    }

    public void setResultcode(Integer resultcode) {
        this.resultcode = resultcode;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp == null ? null : timeStamp.trim();
    }

    public String getEncodestr() {
        return encodestr;
    }

    public void setEncodestr(String encodestr) {
        this.encodestr = encodestr == null ? null : encodestr.trim();
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

    public Boolean getIsputwelcomesms() {
        return isputwelcomesms;
    }

    public void setIsputwelcomesms(Boolean isputwelcomesms) {
        this.isputwelcomesms = isputwelcomesms;
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
        VacReceiveMessage other = (VacReceiveMessage) that;
        return (this.getVacId() == null ? other.getVacId() == null : this.getVacId().equals(other.getVacId()))
            && (this.getVacAddtime() == null ? other.getVacAddtime() == null : this.getVacAddtime().equals(other.getVacAddtime()))
            && (this.getRecordsequenceid() == null ? other.getRecordsequenceid() == null : this.getRecordsequenceid().equals(other.getRecordsequenceid()))
            && (this.getUseridtype() == null ? other.getUseridtype() == null : this.getUseridtype().equals(other.getUseridtype()))
            && (this.getUserid() == null ? other.getUserid() == null : this.getUserid().equals(other.getUserid()))
            && (this.getServicetype() == null ? other.getServicetype() == null : this.getServicetype().equals(other.getServicetype()))
            && (this.getSpid() == null ? other.getSpid() == null : this.getSpid().equals(other.getSpid()))
            && (this.getProductid() == null ? other.getProductid() == null : this.getProductid().equals(other.getProductid()))
            && (this.getUpdatetype() == null ? other.getUpdatetype() == null : this.getUpdatetype().equals(other.getUpdatetype()))
            && (this.getUpdatetime() == null ? other.getUpdatetime() == null : this.getUpdatetime().equals(other.getUpdatetime()))
            && (this.getUpdatedesc() == null ? other.getUpdatedesc() == null : this.getUpdatedesc().equals(other.getUpdatedesc()))
            && (this.getLinkid() == null ? other.getLinkid() == null : this.getLinkid().equals(other.getLinkid()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getEffectivedate() == null ? other.getEffectivedate() == null : this.getEffectivedate().equals(other.getEffectivedate()))
            && (this.getExpiredate() == null ? other.getExpiredate() == null : this.getExpiredate().equals(other.getExpiredate()))
            && (this.getResultcode() == null ? other.getResultcode() == null : this.getResultcode().equals(other.getResultcode()))
            && (this.getTimeStamp() == null ? other.getTimeStamp() == null : this.getTimeStamp().equals(other.getTimeStamp()))
            && (this.getEncodestr() == null ? other.getEncodestr() == null : this.getEncodestr().equals(other.getEncodestr()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getUserName() == null ? other.getUserName() == null : this.getUserName().equals(other.getUserName()))
            && (this.getIsputwelcomesms() == null ? other.getIsputwelcomesms() == null : this.getIsputwelcomesms().equals(other.getIsputwelcomesms()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getVacId() == null) ? 0 : getVacId().hashCode());
        result = prime * result + ((getVacAddtime() == null) ? 0 : getVacAddtime().hashCode());
        result = prime * result + ((getRecordsequenceid() == null) ? 0 : getRecordsequenceid().hashCode());
        result = prime * result + ((getUseridtype() == null) ? 0 : getUseridtype().hashCode());
        result = prime * result + ((getUserid() == null) ? 0 : getUserid().hashCode());
        result = prime * result + ((getServicetype() == null) ? 0 : getServicetype().hashCode());
        result = prime * result + ((getSpid() == null) ? 0 : getSpid().hashCode());
        result = prime * result + ((getProductid() == null) ? 0 : getProductid().hashCode());
        result = prime * result + ((getUpdatetype() == null) ? 0 : getUpdatetype().hashCode());
        result = prime * result + ((getUpdatetime() == null) ? 0 : getUpdatetime().hashCode());
        result = prime * result + ((getUpdatedesc() == null) ? 0 : getUpdatedesc().hashCode());
        result = prime * result + ((getLinkid() == null) ? 0 : getLinkid().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getEffectivedate() == null) ? 0 : getEffectivedate().hashCode());
        result = prime * result + ((getExpiredate() == null) ? 0 : getExpiredate().hashCode());
        result = prime * result + ((getResultcode() == null) ? 0 : getResultcode().hashCode());
        result = prime * result + ((getTimeStamp() == null) ? 0 : getTimeStamp().hashCode());
        result = prime * result + ((getEncodestr() == null) ? 0 : getEncodestr().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getUserName() == null) ? 0 : getUserName().hashCode());
        result = prime * result + ((getIsputwelcomesms() == null) ? 0 : getIsputwelcomesms().hashCode());
        return result;
    }
}