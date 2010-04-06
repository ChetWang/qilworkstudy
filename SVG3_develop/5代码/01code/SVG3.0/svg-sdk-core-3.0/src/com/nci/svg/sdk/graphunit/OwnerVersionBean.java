package com.nci.svg.sdk.graphunit;

import java.io.Serializable;


/**
 * @author Qil.Wong
 *
 */
public class OwnerVersionBean implements Serializable{

	private static final long serialVersionUID = 6410099358621696695L;

	/**
	 * 图元所有者
	 */
	private String owner = null;

	/**
	 * 图元版本
	 */
	private String version = null;

	/**
	 * OwnerVersionBean toString()方法中owner和version的间隔
	 */
	public static final String BEAN_STR_SEP = "BEAN_STR_SEP";
	
	/**
	 * 多个Owner（主要针对client传过来的数据）间的间隔
	 */
	public static final String OWNER_SEP = "OWNER_SEP";
	
	/**
	 * 版本信息间的间隔，主要针对服务端版本信息发送给客户端
	 */
	public static final String VERSION_SEP = "VERSION_SEP";
	
	/**
	 * 已发布的所有者
	 */
	public static final String OWNER_RELEASED = "released";

	/**
	 * 无参构造
	 */
	public OwnerVersionBean() {
	}

	/**
	 * 有参构造
	 * @param owner 所有者
	 * @param version 版本
	 */
	public OwnerVersionBean(String owner, String version) {
		this.owner = owner;
		this.version = version;
	}

	/**
	 * 获取所有者信息
	 * @return 所有者
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * 设置所有者信息
	 * @param owner 所有者
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * 获取版本信息
	 * @return 版本信息
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * 设置版本信息
	 * @param version 版本信息
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	public String toString() {
		return owner + BEAN_STR_SEP + version;
	}

}
