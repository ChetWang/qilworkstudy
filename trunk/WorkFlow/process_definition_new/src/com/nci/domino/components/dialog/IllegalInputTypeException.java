package com.nci.domino.components.dialog;

/**
 * �Ի�����������쳣
 * 
 * @author Qil.Wong
 * 
 */
public class IllegalInputTypeException extends Exception {

	private static final long serialVersionUID = -7481332209748089692L;

	public IllegalInputTypeException(Class<?> need, Class<?> input) {
		super("�Ƿ���������:" + input.getName() + ", ��Ҫ" + need.getName());
	}

}
