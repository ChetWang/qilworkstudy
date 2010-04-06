package com.nci.svg.sdk.client.communication;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.SysSetDefines;

/**
 * 通讯组件时的客户端与服务端交互时的对象， 通讯组件将解析CommunicationBean对象并最终组合成相应的格式传给Server端
 * 
 * @author Qil.Wong
 * @since 3.0
 * 
 */
public class CommunicationBean {

	public final static String SERVLET_REQUEST_TYPE_NAME = "action";

	/**
	 * servlet服务所在的URL路径
	 */
	private String url;

	/**
	 * 请求类型
	 */
	private String actionType;

	/**
	 * 传递给servlet的参数,是一个二维数组，第一个元素表示参数名称，第二个元素表示参数值
	 */
	private String[][] params;

	/**
	 * ServletBean无参数构造函数
	 */
	public CommunicationBean() {

	}

	/**
	 * @param actionType
	 *            请求类型，服务端将根据请求类型进行服务分流，执行相应的业务逻辑处理
	 * @param params
	 *            传递给servlet的参数,是一个二维数组，第一个元素表示参数名称，第二个元素表示参数值
	 */
	public CommunicationBean(String actionType, String[][] params) {
		try {
			checkParams(params);
		} catch (IllegalServletParamNameException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			return;
		}

		this.url = null;
		this.actionType = actionType;
		this.params = params;
	}

	/**
	 * ServletBean构造函数
	 * 
	 * @param servletURL
	 *            servlet服务所在的URL路径
	 * @param actionType
	 *            请求类型，服务端将根据请求类型进行服务分流，执行相应的业务逻辑处理
	 * @param params
	 *            传递给servlet的参数,是一个二维数组，第一个元素表示参数名称，第二个元素表示参数值
	 */
	public CommunicationBean(String servletURL, String actionType,
			String[][] params) {
		try {
			checkParams(params);
		} catch (IllegalServletParamNameException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			return;
		}
		this.url = servletURL;
		this.actionType = actionType;
		this.params = params;
	}

	/**
	 * 获取传递给servlet请求的参数
	 * 
	 * @return params，servlet请求的参数
	 */
	public String[][] getParams() {
		return params;
	}

	/**
	 * 检查servlet请求的参数的有效性
	 * 
	 * @param params
	 *            servlet请求的参数
	 * @throws IllegalServletParamNameException
	 *             非法servlet请求参数名异常
	 */
	private void checkParams(String[][] params)
			throws IllegalServletParamNameException {
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				if (SERVLET_REQUEST_TYPE_NAME.equalsIgnoreCase(params[i][0]))
					throw new IllegalServletParamNameException();
			}
		}
	}

	/**
	 * 设置传递给servlet请求的参数
	 * 
	 * @param params
	 *            servlet请求的参数
	 */
	public void setParams(String[][] params)
			throws IllegalServletParamNameException {
		checkParams(params);
		this.params = params;
	}

	/**
	 * 获取Servlet服务的URL路径
	 * 
	 * @return Servlet服务的URL路径
	 */
	public String getURL() {
		return url;
	}

	/**
	 * 设置Servlet服务的URL路径
	 * 
	 * @param url
	 *            Servlet服务的URL路径
	 */
	public void setURL(String url) {
		this.url = url;
	}

	/**
	 * 获取Servlet请求类型
	 * 
	 * @return Servlet请求类型
	 */
	public String getActionType() {
		return actionType;
	}

	/**
	 * 设置Servlet请求类型
	 * 
	 * @param actionType
	 *            Servlet请求类型
	 */
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (url != null)
			sb.append(url);
		if (actionType != null) {
			sb.append("?");
			sb.append(SERVLET_REQUEST_TYPE_NAME).append("=").append(actionType);
		}
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				sb.append("&");
				sb.append(params[i][0]).append("=").append(params[i][1]);
			}
		}
		return sb.toString();
	}

}
