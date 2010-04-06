package com.nci.ums.transmit.common;

public class UMSTransmitException extends Exception{

	private static final long serialVersionUID = 8557251087430502168L;

	public UMSTransmitException(){
		super("连接已无效，无法进行数据传输");
	}
}
