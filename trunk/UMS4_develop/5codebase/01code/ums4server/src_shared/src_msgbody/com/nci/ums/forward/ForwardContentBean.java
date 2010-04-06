package com.nci.ums.forward;

import java.sql.Date;

public class ForwardContentBean {

	private String forwardContent;

	private String serviceid;
	private String status;
	private int statusFlag;
	private String creator;
	private Date createTime;

	public ForwardContentBean() {
	}

	public ForwardContentBean(String content, String serviceid) {
		this.forwardContent = content;
		this.serviceid = serviceid;
	}

	public String getForwardContent() {
		return forwardContent;
	}

	public void setForwardContent(String forwardContent) {
		this.forwardContent = forwardContent;
	}

	public String getServiceid() {
		return serviceid;
	}

	public void setServiceid(String serviceid) {
		this.serviceid = serviceid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(int statusFlag) {
		this.statusFlag = statusFlag;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
