package com.nci.ums.v3.service;


import java.io.Serializable;

public class ServiceInfo implements Serializable {

	private static final long serialVersionUID = 1038513186657589018L;
	private String serviceID = "";
	private String serviceName = "";
	private String appID = "";
	private String appPsw= "";
	private String serviceDesc = "";
	private String serviceAddr = "";
	private String port = "";
	private int status = -1;
	private int serviceType = -1;
	private int directionType = -1;
	
	/**
	 * 有效的，正在运行的服务
	 */
	public static final int SERVICE_STATUS_VALID = 0;
	/**
	 * 无效的，已经停止运行的服务
	 */
	public static final int SERVICE_STATUS_INVALID = 1;
	/**
	 * 主动发送消息的服务类型
	 */
	public static final int SERVICE_DIRECTION_INITIATIVE = 1;
	/**
	 * 被动接收消息的服务类型
	 */
	public static final int SERVICE_DIRECTION_PASSIVE = 2;
	
	/**
	 * socket形式开放的服务
	 */
	public static final int SERVICE_TYPE_SOCKET = 1;
	
	/**
	 * jms形式开放的服务
	 */
	public static final int SERVICE_TYPE_JMS = 2;
	/**
	 * webservice形式开放的服务
	 */
	public static final int SERVICE_TYPE_WEBSERVICE = 3;
	/**
	 * @return the serviceID
	 */
	public String getServiceID() {
		return serviceID;
	}
	/**
	 * @param serviceID the serviceID to set
	 */
	public void setServiceID(String serviceID) {
		this.serviceID = serviceID;
	}
	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}
	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	/**
	 * @return the appID
	 */
	public String getAppID() {
		return appID;
	}
	/**
	 * @param appID the appID to set
	 */
	public void setAppID(String appID) {
		this.appID = appID;
	}
	/**
	 * @return the serviceDesc
	 */
	public String getServiceDesc() {
		return serviceDesc;
	}
	/**
	 * @param serviceDesc the serviceDesc to set
	 */
	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}
	/**
	 * @return the serviceAddr
	 */
	public String getServiceAddr() {
		return serviceAddr;
	}
	/**
	 * @param serviceAddr the serviceAddr to set
	 */
	public void setServiceAddr(String serviceAddr) {
		this.serviceAddr = serviceAddr;
	}
	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * @return the serviceType
	 */
	public int getServiceType() {
		return serviceType;
	}
	/**
	 * @param serviceType the serviceType to set
	 */
	public void setServiceType(int serviceType) {
		this.serviceType = serviceType;
	}
	/**
	 * @return the directionType
	 */
	public int getDirectionType() {
		return directionType;
	}
	/**
	 * @param directionType the directionType to set
	 */
	public void setDirectionType(int directionType) {
		this.directionType = directionType;
	}
	public String getAppPsw() {
		return appPsw;
	}
	public void setAppPsw(String appPsw) {
		this.appPsw = appPsw;
	}
	
	public String toString(){
		return "["+appID+"]"+serviceID;
	}
	
}
