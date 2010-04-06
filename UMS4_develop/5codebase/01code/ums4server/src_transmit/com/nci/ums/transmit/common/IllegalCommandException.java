package com.nci.ums.transmit.common;

/**
 * 用户定义命令超长异常，这是UMS自封装的命令，默认两个字节长度。 如果用户不用UMS提供的命令封装方案，命令长度则不受此异常限制。
 * 
 * @author Qil.Wong
 * 
 */
public class IllegalCommandException extends Exception {

	private static final long serialVersionUID = -5196759859271537157L;

	public IllegalCommandException(int userCommand) {
		super("Address value should be at range of 0～65535, current is "
				+ userCommand);
	}
}
