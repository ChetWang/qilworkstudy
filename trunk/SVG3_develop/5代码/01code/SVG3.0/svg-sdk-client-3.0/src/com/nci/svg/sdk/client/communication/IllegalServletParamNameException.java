package com.nci.svg.sdk.client.communication;


/**
 * �Ƿ�servlet����������쳣
 * @author Qil.Wong
 *
 */
public class IllegalServletParamNameException extends Exception {

	public IllegalServletParamNameException() {
		super("�Ƿ���Servlet��������Servlet���������ܰ���\"" + CommunicationBean.SERVLET_REQUEST_TYPE_NAME
				+ "\"");
	}

}
