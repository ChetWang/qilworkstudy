package com.tdt.unicom.domains;

import com.tdt.unicom.util.SGIPCodeHelper;

/**
 * @author sunnylocus
 * 在发送或接收短信发时的异常
 */
public class SGIPException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private int errorCode = 0;
	
	public SGIPException() {
		super();
	}

	public SGIPException(int errorCode) {
		this.errorCode = errorCode;
	}
	
	public SGIPException(String message, Throwable cause) {
		super(message, cause);
	}

	public SGIPException(String message) {
		super(message);
	}

	public SGIPException(Throwable cause) {
		super(cause);
	}
	
	public int getSGIPCode() {
		return this.errorCode;
	}
	
	public String getSGIPCause() {
		return SGIPCodeHelper.getDescription(this.errorCode);
	}
}
