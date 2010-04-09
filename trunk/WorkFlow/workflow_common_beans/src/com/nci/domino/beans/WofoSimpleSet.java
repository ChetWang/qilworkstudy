package com.nci.domino.beans;

import java.io.Serializable;

/**
 * 简单流程集合类，无实际意义，只用来分类流程各种属性对象
 * 
 * @author Qil.Wong
 * 
 */
public class WofoSimpleSet implements Serializable {

	private static final long serialVersionUID = -2523112977939418932L;

	private int type = -1;

	private String name = "";

	/**
	 * 流程消息集合
	 */
	public static final int WORKFLOW_MESSAGE_TYPE = 0;

	/**
	 * 流程事件集合
	 */
	public static final int WORKFLOW_EVENT_TYPE = 1;

	/**
	 * 流程条件集合
	 */
	public static final int WORKFLOW_CONDITION_TYPE = 2;

	/**
	 * 流程参数集合
	 */
	public static final int WORKFLOW_PARAMETER_TYPE = 3;

	/**
	 * 流程项集合
	 */
	public static final int WORKFLOW_ITEM_TYPE = 4;

	/**
	 * 业务项集合
	 */
	public static final int BUSINESS_ITEM_TYPE = 5;

	/**
	 * 业务流程集合
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
