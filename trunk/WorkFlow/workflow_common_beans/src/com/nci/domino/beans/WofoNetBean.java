package com.nci.domino.beans;

import java.io.Serializable;

/**
 * �����н������紫��Ķ����Դ������и��ද��
 * 
 * @author Qil.Wong
 * 
 */
public class WofoNetBean implements Serializable {

	private static final long serialVersionUID = 4846556001552353892L;

	/**
	 * ��������
	 */
	private String actionName = "";

	/**
	 * ������
	 */
	private String user = "";

	/**
	 * �����������
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
