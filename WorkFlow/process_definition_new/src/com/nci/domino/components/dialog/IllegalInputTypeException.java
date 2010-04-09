package com.nci.domino.components.dialog;

/**
 * 对话框输入参数异常
 * 
 * @author Qil.Wong
 * 
 */
public class IllegalInputTypeException extends Exception {

	private static final long serialVersionUID = -7481332209748089692L;

	public IllegalInputTypeException(Class<?> need, Class<?> input) {
		super("非法输入类型:" + input.getName() + ", 需要" + need.getName());
	}

}
