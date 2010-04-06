package com.nci.ums.desktop.bean;

/**
 * 在Swing控件中存储的对象，可以比较（排序），显示名称与对象内容分离
 * @author Qil.Wong
 *
 */
public class CellKeyObj implements Comparable {
	Object keyObj;
	String name;

	public CellKeyObj() {
	}

	public CellKeyObj(Object keyObj, String name) {
		this.keyObj = keyObj;
		this.name = name;
	}

	/**
	 * 获取组件中的对象
	 * @return 对象
	 */
	public Object getKeyObj() {
		return keyObj;
	}

	/**
	 * 设置组件对象
	 * @param keyObj 组件对象值
	 */
	public void setKeyObj(Object keyObj) {
		this.keyObj = keyObj;
	}

	/**
	 * 获取组件对象在界面上显示的名称
	 * @return 名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置组件对象在界面上显示的名称
	 * @param name 名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

	public int compareTo(Object obj) {
		return this.toString().compareTo(obj.toString());
	}

}
