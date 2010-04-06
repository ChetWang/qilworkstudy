package com.nci.ums.v3.service.common.jms;

/**
 * <p>
 * Title: JMSConnBean.java
 * </p>
 * <p>
 * Description: <code>JMSConnBean</code>contains the necessary attributes for
 * JMS connection, including login user, login password and JMS server URL.
 * </p>
 * <p>
 * Copyright: 2007 Hangzhou NCI System Engineering， Ltd.
 * </p>
 * <p>
 * Company: Hangzhou NCI System Engineering， Ltd.
 * </p>
 * 
 * @author Qil.Wong Created in 2007.09.20
 * @version 1.0
 */
public class JMSConnBean implements java.io.Serializable {

	/**
	 * UMS JMS链接用户
	 */
	private String user;
	/**
	 * UMS JMS链接用户密码
	 */
	private String password;
	/**
	 * JMS URL
	 */
	private String url;
	
	/**
	 * JMS链接目的主题
	 */
	private String destinationSubject;

	public JMSConnBean() {
	}

	public JMSConnBean(String user, String password, String url,
			String destinationSubject) {
		this.user = user;
		this.password = password;
		this.url = url;
		this.destinationSubject = destinationSubject;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDestinationSubject() {
		return destinationSubject;
	}

	public void setDestinationSubject(String destinationSubject) {
		this.destinationSubject = destinationSubject;
	}

}
