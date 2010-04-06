package com.nci.ums.common.service;

/**
 * 平台内部UMS调用命令
 * 
 * @author Qil.Wong
 * 
 */
public interface InterCommand {

	/**
	 * 增加渠道
	 */
	public static final int MEDIA_ADD = 10;
	/**
	 * 修改渠道
	 */
	public static final int MEDIA_UPDATE = 11;
	/**
	 * 删除渠道
	 */
	public static final int MEDIA_DELETE = 12;
	/**
	 * 启动渠道
	 */
	public static final int MEDIA_START = 13;
	/**
	 * 停止渠道
	 */
	public static final int MEDIA_STOP = 14;
	/**
	 * 新增应用
	 */
	public static final int APP_ADD = 20;
	/**
	 * 修改应用
	 */
	public static final int APP_UPDATE = 21;
	/**
	 * 删除应用
	 */
	public static final int APP_DELETE = 22;
	/**
	 * 启动应用
	 */
	public static final int APP_START= 23;
	/**
	 * 停止应用
	 */
	public static final int APP_STOP = 24;
	/**
	 * 新增服务
	 */
	public static final int SERVICE_ADD = 30;
	/**
	 * 修改服务
	 */
	public static final int SERVICE_UPDATE = 31;
	/**
	 * 删除服务
	 */
	public static final int SERVICE_DELETE = 32;
	/**
	 * 启动服务
	 */
	public static final int SERVICE_START = 33;
	/**
	 * 停止服务
	 */
	public static final int SERVICE_STOP = 34;
	/**
	 * 新增转发内容
	 */
	public static final int FORWARD_CONTENT_ADD = 40;
	/**
	 * 修改转发内容
	 */
	public static final int FORWARD_CONTENT_UPDATE = 41;
	/**
	 * 删除转发内容
	 */
	public static final int FORWARD_CONTENT_DELETE = 42;
	/**
	 * 使用转发内容
	 */
	public static final int FORWARD_CONTENT_START = 43;
	/**
	 * 停用转发内容
	 */
	public static final int FORWARD_CONTENT_STOP = 44;
	/**
	 * 新增收费
	 */
	public static final int FEE_ADD = 50;
	/**
	 * 修改收费
	 */
	public static final int FEE_UPDATE = 51;
	/**
	 * 删除收费
	 */
	public static final int FEE_DELETE = 52;
	
	/**
	 * 新增过滤
	 */
	public static final int FILTER_ADD = 60;
	/**
	 * 修改过滤
	 */
	public static final int FILTER_UPDATE = 61;
	/**
	 * 删除过滤
	 */
	public static final int FILTER_DELETE = 62;

	/**
	 * 使用过滤
	 */
	public static final int FILTER_START = 63;
	/**
	 * 停用过滤
	 */
	public static final int FILTER_STOP = 64;

}
