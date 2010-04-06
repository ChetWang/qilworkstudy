package com.nci.svg.sdk.logger;

/**
 * 日志信息对象
 * @author Qil.Wong
 *
 */
public class LogInfoBean {
	
	//日志记录下的操作人
	private String user;
	
	//日志记录下的操作类型
	private String operType;
	
	//日志记录下的操作内容
	private String operDesc;

	/**
	 * 获取操作人
	 * @return 操作人
	 */
	public String getUser() {
		return user;
	}

	/**
	 * 设置操作人
	 * @param user 操作人
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * 获取操作类型
	 * @return 操作类型
	 */
	public String getOperType() {
		return operType;
	}

	/**
	 * 设置操作类型
	 * @param operType 操作类型
	 */
	public void setOperType(String operType) {
		this.operType = operType;
	}

	/**
	 * 获取操作内容
	 * @return 操作内容
	 */
	public String getOperDesc() {
		return operDesc;
	}

	/**
	 * 设置操作内容
	 * @param operDesc 操作内容
	 */
	public void setOperDesc(String operDesc) {
		this.operDesc = operDesc;
	}
	
	

}
