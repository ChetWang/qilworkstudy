package com.nci.ums.client;

import java.io.Serializable;

public class UMSReturnMessage extends UMSMessage implements Serializable{
	
	private static final long serialVersionUID = 1554442075806979699L;
	
	public void setReturnMsg(String returnMsg) {
		put(MESSAGE_RETURN_MSG,returnMsg);
	}

	public String getReturnMsg() {
		return get(MESSAGE_RETURN_MSG);
	}

	public void setReturnCode(String returnCode) {
		put(MESSAGE_RETURN_CODE,returnCode);
	}

	public String getReturnCode() {
		return get(MESSAGE_RETURN_CODE);
	}
	
	public static final String MESSAGE_RETURN_CODE	= "com.nci.ums.message.return.code";
	public static final String MESSAGE_RETURN_MSG 	= "com.nci.ums.message.return.msg";
}
