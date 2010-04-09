package com.nci.domino.beans;

import java.io.Serializable;

/**
 * �����̼����࣬��ʵ�����壬ֻ�����������̸������Զ���
 * 
 * @author Qil.Wong
 * 
 */
public class WofoSimpleSet implements Serializable {

	private static final long serialVersionUID = -2523112977939418932L;

	private int type = -1;

	private String name = "";

	/**
	 * ������Ϣ����
	 */
	public static final int WORKFLOW_MESSAGE_TYPE = 0;

	/**
	 * �����¼�����
	 */
	public static final int WORKFLOW_EVENT_TYPE = 1;

	/**
	 * ������������
	 */
	public static final int WORKFLOW_CONDITION_TYPE = 2;

	/**
	 * ���̲�������
	 */
	public static final int WORKFLOW_PARAMETER_TYPE = 3;

	/**
	 * �������
	 */
	public static final int WORKFLOW_ITEM_TYPE = 4;

	/**
	 * ҵ�����
	 */
	public static final int BUSINESS_ITEM_TYPE = 5;

	/**
	 * ҵ�����̼���
	 */
	public static final int BUSINESS_WORKFLOW_TYPE = 6;
	
	public WofoSimpleSet() {

	}

	public WofoSimpleSet(int type, String name) {
		this.type = type;
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

}
