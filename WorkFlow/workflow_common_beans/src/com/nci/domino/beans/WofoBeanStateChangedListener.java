package com.nci.domino.beans;

import java.io.Serializable;

/**
 * 业务对象状态变化监听器
 * 
 * @author Qil.Wong
 * 
 */
public interface WofoBeanStateChangedListener extends Serializable{

	/**
	 * 状态变化
	 * 
	 * @param oldState
	 *            旧状态
	 * @param newState
	 *            新状态
	 */
	public void wofoBeanStateChanged(int oldState, int newState);
}
