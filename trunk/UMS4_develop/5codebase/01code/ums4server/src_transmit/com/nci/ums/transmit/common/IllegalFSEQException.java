package com.nci.ums.transmit.common;

/**
 * FSEQ异常，回馈的数据（reverseDirection=true），fseq应与先前收到的数据的fseq值相等
 * @author Qil.Wong
 *
 */
public class IllegalFSEQException extends Exception{

	private static final long serialVersionUID = -8125356639252511289L;

	public IllegalFSEQException(){
		super("回馈的数据（reverseDirection=true），fseq应与先前收到的数据的fseq值相等");
	}
}
