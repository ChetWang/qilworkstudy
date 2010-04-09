package com.nci.domino.beans;

import java.io.Serializable;

/**
 * 操作中进行网络传输的对象，以此来进行各类动作
 * 
 * @author Qil.Wong
 * 
 */
public class WofoNetBean implements Serializable {

	private static final long serialVersionUID = 4846556001552353892L;

	/**
	 * 操作名称
	 */
	private String actionName = "";

	/**
	 * 操作者
	 */
	private String user = "";

	/**
	 * 操作所需参数
	 */
	private Serializable param;

	public WofoNetBean() {

	}

	public WofoNetBean(String actionName,String user,Serializable param) {
		this.actionName = actionName;
		this.user = user;
		this.param = param;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Serializable getParam() {
		return param;
	}

	public void setParam(Serializable param) {
		this.param = param;
	}
}
