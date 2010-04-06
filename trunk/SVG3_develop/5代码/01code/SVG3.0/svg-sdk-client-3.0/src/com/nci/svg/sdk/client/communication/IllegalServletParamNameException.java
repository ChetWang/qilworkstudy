package com.nci.svg.sdk.client.communication;


/**
 * 非法servlet请求参数名异常
 * @author Qil.Wong
 *
 */
public class IllegalServletParamNameException extends Exception {

	public IllegalServletParamNameException() {
		super("非法的Servlet参数名！Servlet参数名不能包含\"" + CommunicationBean.SERVLET_REQUEST_TYPE_NAME
				+ "\"");
	}

}
