package com.nci.domino.beans;

import java.io.Serializable;

/**
 * ҵ�����״̬�仯������
 * 
 * @author Qil.Wong
 * 
 */
public interface WofoBeanStateChangedListener extends Serializable{

	/**
	 * ״̬�仯
	 * 
	 * @param oldState
	 *            ��״̬
	 * @param newState
	 *            ��״̬
	 */
	public void wofoBeanStateChanged(int oldState, int newState);
}
