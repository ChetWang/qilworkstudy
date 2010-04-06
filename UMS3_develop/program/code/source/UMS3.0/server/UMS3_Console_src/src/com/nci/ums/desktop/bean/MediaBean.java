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
	 * ״̬��־����ʾ������������
	 */
	public static final int STATUS_RUNNING = 0;
	/**
	 * ״̬��־����ʾ�����Ѿ�ֹͣ
	 */
	public static final int STATUS_STOPPED= 1;
	
	/**
	 * ���ڽ�����Ϣ����������
	 */
	public static final int TYPE_IN = 0;
	/**
	 * ���ڷ�����Ϣ����������
	 */
	public static final int TYPE_OUT = 1;
	/**
	 * ����TYPE_CMSMS_IN=2��һ��������ֻ�Խ����ƶ�����Ϣ���ԣ���Ϊʹ�û�ΪAPI��ͨ��������ʽ��������Ϣ����������ɨ�裬
	 * ���UMS�����ٴ�����һ��������������Ϣ����������Ϣ����ʱ����Ҫ�ܹ����������Կ���һ�������ͣ���Ϊ��α������
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
