package com.nci.ums.periphery.core;

public class IpLogin{
	private String ip;
	private boolean status;
	private String appID;
	private int loginTime;
	
	/**
	 * @return Returns the appID.
	 */
	public String getAppID() {
		return appID;
	}
	/**
	 * @param appID The appID to set.
	 */
	public void setAppID(String appID) {
		this.appID = appID;
	}
	/**
	 * @return Returns the ip.
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @param ip The ip to set.
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * @return Returns the loginTime.
	 */
	public int getLoginTime() {
		return loginTime;
	}
	/**
	 * @param loginTime The loginTime to set.
	 */
	public void setLoginTime(int loginTime) {
		this.loginTime = loginTime;
	}
	/**
	 * @return Returns the status.
	 */
	public boolean isStatus() {
		return status;
	}
	/**
	 * @param status The status to set.
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}
}