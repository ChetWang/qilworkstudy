package com.nci.domino.components.dialog;

import java.io.Serializable;

/**
 * �Ի���ֵ�ļ����¼�
 * 
 * @author Qil.Wong
 * 
 */
public interface WfSetValueListener {

	/**
	 * ��ֵǰ�Ķ���
	 * 
	 * @param value
	 */
	public void beforeValueSet(Serializable value);

	/**
	 * ��ֵ��Ķ���
	 * 
	 * @param value
	 */
	public void afterValueSet(Serializable value);

}
