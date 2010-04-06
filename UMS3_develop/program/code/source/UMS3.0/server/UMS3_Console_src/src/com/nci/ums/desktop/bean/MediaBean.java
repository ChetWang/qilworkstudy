package com.nci.ums.desktop.bean;

public 	class MediaBean {
	String mediaID;
	String mediaName;
	int mediaType;
	int mediaStatus;
	public final static String STATUS_RUNNING_String= "RUNNING";
	public final static String STATUS_STOPPED_String = "STOPPED";

	public final static String TYPE_IN_String = "IN";
	public final static String TYPE_OUT_String = "OUT";
	/**
	 * 状态标志，表示渠道正在运行
	 */
	public static final int STATUS_RUNNING = 0;
	/**
	 * 状态标志，表示渠道已经停止
	 */
	public static final int STATUS_STOPPED= 1;
	
	/**
	 * 用于接收消息的渠道类型
	 */
	public static final int TYPE_IN = 0;
	/**
	 * 用于发送消息的渠道类型
	 */
	public static final int TYPE_OUT = 1;
	/**
	 * 这里TYPE_CMSMS_IN=2是一个特例，只对接收移动短消息而言，因为使用华为API是通过监听方式来接收消息而不是渠道扫描，
	 * 因此UMS不需再创建另一个渠道来接收消息，但监视消息接收时，需要能够看到，所以开了一个新类型，作为“伪渠道”
	 */
	public static final int TYPE_CMSMS_IN = 2;
	
	public MediaBean(){}
	
	public MediaBean(String mediaID,String mediaName,int mediaType,int mediaStatus){
		this.mediaID = mediaID;
		this.mediaName=mediaName;
		this.mediaType = mediaType;
		this.mediaStatus = mediaStatus;
	}
	public String getMediaID() {
		return mediaID;
	}
	public void setMediaID(String mediaID) {
		this.mediaID = mediaID;
	}
	public String getMediaName() {
		return mediaName;
	}
	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}
	public int getMediaType() {
		return mediaType;
	}
	public void setMediaType(int mediaType) {
		this.mediaType = mediaType;
	}
	public int getMediaStatus() {
		return mediaStatus;
	}
	public void setMediaStatus(int mediaStatus) {
		this.mediaStatus = mediaStatus;
	}
	public String toString(){
		return "["+mediaID+"]"+mediaName;
	}
}
