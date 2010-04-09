package com.nci.domino.components.dialog;

import java.io.Serializable;

/**
 * 对话框赋值的监听事件
 * 
 * @author Qil.Wong
 * 
 */
public interface WfSetValueListener {

	/**
	 * 赋值前的动作
	 * 
	 * @param value
	 */
	public void beforeValueSet(Serializable value);

	/**
	 * 赋值后的动作
	 * 
	 * @param value
	 */
	public void afterValueSet(Serializable value);

}
