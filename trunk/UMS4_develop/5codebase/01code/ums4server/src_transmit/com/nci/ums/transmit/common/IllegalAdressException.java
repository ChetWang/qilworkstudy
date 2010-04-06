package com.nci.ums.transmit.common;

/**
 * 非法地址异常，UMS默认的客户端接入地址是2个字节长度，在0-65535之间
 * @author Qil.Wong
 *
 */
public class IllegalAdressException extends Exception{

	private static final long serialVersionUID = 8557251087430502168L;

	public IllegalAdressException(int address){
		super("Address value should be at range of 0～65535, current is "+address);
	}
}
