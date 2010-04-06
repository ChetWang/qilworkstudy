package com.nci.svg.sdk.client.communication;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.SysSetDefines;

/**
 * ͨѶ���ʱ�Ŀͻ��������˽���ʱ�Ķ��� ͨѶ���������CommunicationBean����������ϳ���Ӧ�ĸ�ʽ����Server��
 * 
 * @author Qil.Wong
 * @since 3.0
 * 
 */
public class CommunicationBean {

	public final static String SERVLET_REQUEST_TYPE_NAME = "action";

	/**
	 * servlet�������ڵ�URL·��
	 */
	private String url;

	/**
	 * ��������
	 */
	private String actionType;

	/**
	 * ���ݸ�servlet�Ĳ���,��һ����ά���飬��һ��Ԫ�ر�ʾ�������ƣ��ڶ���Ԫ�ر�ʾ����ֵ
	 */
	private String[][] params;

	/**
	 * ServletBean�޲������캯��
	 */
	public CommunicationBean() {

	}

	/**
	 * @param actionType
	 *            �������ͣ�����˽������������ͽ��з��������ִ����Ӧ��ҵ���߼�����
	 * @param params
	 *            ���ݸ�servlet�Ĳ���,��һ����ά���飬��һ��Ԫ�ر�ʾ�������ƣ��ڶ���Ԫ�ر�ʾ����ֵ
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
	 * ServletBean���캯��
	 * 
	 * @param servletURL
	 *            servlet�������ڵ�URL·��
	 * @param actionType
	 *            �������ͣ�����˽������������ͽ��з��������ִ����Ӧ��ҵ���߼�����
	 * @param params
	 *            ���ݸ�servlet�Ĳ���,��һ����ά���飬��һ��Ԫ�ر�ʾ�������ƣ��ڶ���Ԫ�ر�ʾ����ֵ
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
	 * ��ȡ���ݸ�servlet����Ĳ���
	 * 
	 * @return params��servlet����Ĳ���
	 */
	public String[][] getParams() {
		return params;
	}

	/**
	 * ���servlet����Ĳ�������Ч��
	 * 
	 * @param params
	 *            servlet����Ĳ���
	 * @throws IllegalServletParamNameException
	 *             �Ƿ�servlet����������쳣
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
	 * ���ô��ݸ�servlet����Ĳ���
	 * 
	 * @param params
	 *            servlet����Ĳ���
	 */
	public void setParams(String[][] params)
			throws IllegalServletParamNameException {
		checkParams(params);
		this.params = params;
	}

	/**
	 * ��ȡServlet�����URL·��
	 * 
	 * @return Servlet�����URL·��
	 */
	public String getURL() {
		return url;
	}

	/**
	 * ����Servlet�����URL·��
	 * 
	 * @param url
	 *            Servlet�����URL·��
	 */
	public void setURL(String url) {
		this.url = url;
	}

	/**
	 * ��ȡServlet��������
	 * 
	 * @return Servlet��������
	 */
	public String getActionType() {
		return actionType;
	}

	/**
	 * ����Servlet��������
	 * 
	 * @param actionType
	 *            Servlet��������
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
