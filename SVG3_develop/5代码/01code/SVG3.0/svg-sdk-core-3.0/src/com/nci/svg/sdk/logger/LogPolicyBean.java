
package com.nci.svg.sdk.logger;

import java.io.Serializable;

/**
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @ʱ�䣺2008-11-25
 * @���ܣ���־������Ϣʵ����
 *
 */
public class LogPolicyBean implements Serializable {
	private static final long serialVersionUID = 3379568818370365243L;

	/**
	 * ����λ��
	 */
	private String moduleLocation;
	
	/**
	 * �������
	 */
	private String moduleKind;
	/**
	 * �������
	 */
	private String moduleType;
	/**
	 * ��־����
	 */
	private String logLevel;
	/**
	 * ��־����
	 */
	private String logType;
	/**
	 * ��ȡ�������
	 * @return�������������
	 */
	public String getModuleType() {
		return moduleType;
	}
	/**
	 * �����������
	 * @param moduleType���������
	 */
	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}
	/**
	 * ��ȡ��־�ȼ�
	 * @return����־�ȼ�
	 */
	public String getLogLevel() {
		return logLevel;
	}
	/**
	 * ������־�ȼ�
	 * @param logLevel����־�ȼ�
	 */
	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}
	/**
	 * ��ȡ��־����
	 * @return����־����
	 */
	public String getLogType() {
		return logType;
	}
	/**
	 * ������־����
	 * @param logType����־����
	 */
	public void setLogType(String logType) {
		this.logType = logType;
	}
	/**
	 * ��ȡ���λ��
	 * @return���������λ��
	 */
	public String getModuleLocation() {
		return moduleLocation;
	}
	/**
	 * �����������λ��
	 * @param moduleLocation���������λ��
	 */
	public void setModuleLocation(String moduleLocation) {
		this.moduleLocation = moduleLocation;
	}
	/**
	 * ��ȡ�������
	 * @return���������
	 */
	public String getModuleKind() {
		return moduleKind;
	}
	/**
	 * �����������
	 * @param moduleKind���������
	 */
	public void setModuleKind(String moduleKind) {
		this.moduleKind = moduleKind;
	}

}
