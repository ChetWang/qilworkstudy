package com.nci.svg.server.automapping.comm;

/**
 * <p>
 * 标题：AutoMapResultBean.java
 * </p>
 * <p>
 * 描述：自动成图返回消息类
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2009-03-03
 * @version 1.0
 */
public class AutoMapResultBean {
	/**
	 * 成功标志
	 */
	private boolean flag;
	/**
	 * 错误消息
	 */
	private String errMsg;
	/**
	 * 消息
	 */
	private Object msg;
	
	/**
	 * 构造函数
	 */
	public AutoMapResultBean(){
		flag = true;
	}

	/**
	 * 获取成功标志
	 * 
	 * @return String
	 */
	public boolean isFlag() {
		return flag;
	}

	/**
	 * 设置成功标志
	 * 
	 * @param flag:boolean:成功标志
	 */
	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	/**
	 * 获取错误消息
	 * 
	 * @return String
	 */
	public String getErrMsg() {
		return errMsg;
	}

	/**
	 * 设置错误信息
	 * 
	 * @param errMsg:String:错误消息
	 */
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	/**
	 * 获取消息
	 * 
	 * @return Object
	 */
	public Object getMsg() {
		return msg;
	}

	/**
	 * 设置消息
	 * 
	 * @param msg:Object:消息
	 */
	public void setMsg(Object msg) {
		this.msg = msg;
	}

}
