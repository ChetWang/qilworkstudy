/**
 * <p>Title: MediaInfo.java</p>
 * <p>Description:
 *    �������Ҫ��������Ӧ�ó��������������Ϣ���������������Ͳ�������
 *    ��Ϣ�����ݴ����ݿ��application�ж�������Ӧ�ó������������߳�ʹ��
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering�� Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering�� Ltd.</p>
 * Date        Author      Changes
 * NOV. 17 2003   ��־��      Created
 * @version 1.0
 */

package com.nci.ums.periphery.application;

import java.net.Socket;


public class AppInfo {
  //������Ϣ
  private String companyID;
  private String appID;
  private String appName;
  private String password;
  //IP���˿���Ϣ
  private String ip;
  private int port;
 //��Ӧ�ó��������ط���,��UMS3.0��ʼ��ÿ��spNO��ֵ��app�µ�ÿ��serviceIDһ��
  private String spNO;
  private Socket socket;

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

  private boolean passed;
 //������Ϣ
  private int channelType;//0�����ģ�1��Api��2��email
  private String email;
  private String objectName;
  private Object object;
  private int timeOut;
   //Ӧ�ó����ṩ��UMS�ĵ�¼�û���������
  private String loginName;
  private String loginPwd;
  private String status;

    public AppInfo() {
    }

    public AppInfo(String appID, String appName, int channelType, String companyID, String email, String ip, String loginName, String loginPwd, Object object, String objectName, String password, int port, String spNO, int timeOut) {
        this.appID = appID;
        this.appName = appName;
        this.channelType = channelType;
        this.companyID = companyID;
        this.email = email;
        this.ip = ip;
        this.loginName = loginName;
        this.loginPwd = loginPwd;
        this.object = object;
        this.objectName = objectName;
        this.password = password;
        this.port = port;
        this.spNO = spNO;
        this.timeOut = timeOut;
        passed=false;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getChannelType() {
        return channelType;
    }

    public void setChannelType(int channelType) {
        this.channelType = channelType;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getSpNO() {
        return spNO;
    }

    public void setSpNO(String spNO) {
        this.spNO = spNO;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
}