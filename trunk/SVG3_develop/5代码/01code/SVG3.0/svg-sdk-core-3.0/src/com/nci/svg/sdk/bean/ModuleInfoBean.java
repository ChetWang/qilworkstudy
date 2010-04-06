
package com.nci.svg.sdk.bean;

import java.io.Serializable;

/**
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author YUX
 * @ʱ�䣺2008-11-21
 * @���ܣ�ģ����Ϣʵ����
 *
 */
public class ModuleInfoBean implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -37817942385437624L;
	/**
     * ģ��������
     */
    private String moduleShortName;
    /**
     * ģ��ȫ������
     */
    private String name;
    /**
     * ģ�鲿��λ��
     */
    private String location;
    /**
     * �������
     */
    private String kind;
    /**
     * �������
     */
    private String type;
    /**
     * ��־�ȼ�
     */
    private String logLevel;
    /**
     * ��־����
     */
    private String logType;
    /**
     * ���������
     */
    private String className;
    
    /**
     * ����汾��
     */
    private String edition;
    
    /**
     * ���Ӧ�õ�ҵ��ϵͳ���
     */
    private String bussID;

	/**
	 * ��ȡ���ģ�����
	 * @return�����ģ�����
	 */
	public String getModuleShortName() {
		return moduleShortName;
	}

	/**
	 * �������ģ�����
	 * @param moduleShortName�����ģ�����
	 */
	public void setModuleShortName(String moduleShortName) {
		this.moduleShortName = moduleShortName;
	}

	/**
	 * ��ȡ���ģ����
	 * @return�����ģ����
	 */
	public String getName() {
		return name;
	}

	/**
	 * �������ģ����
	 * @param name�����ģ����
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * ��ȡ����λ��
	 * @return������λ��
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * ���ò���λ��
	 * @param location������λ��
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * ��ȡ���ģ�����
	 * @return�����ģ�����
	 */
	public String getKind() {
		return kind;
	}

	/**
	 * �������ģ�����
	 * @param kind�����ģ�����
	 */
	public void setKind(String kind) {
		this.kind = kind;
	}

	/**
	 * ��ȡ���ģ������
	 * @return�����ģ������
	 */
	public String getType() {
		return type;
	}

	/**
	 * �������ģ������
	 * @param type�����ģ������
	 */
	public void setType(String type) {
		this.type = type;
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
	 * ��ȡ�����������
	 * @return�������������
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * ���������������
	 * @param className�������������
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * ��ȡ������°汾��
	 * @return��������°汾��
	 */
	public String getEdition() {
		return edition;
	}

	/**
	 * ����������°汾��
	 * @param edition��������°汾��
	 */
	public void setEdition(String edition) {
		this.edition = edition;
	}

	/**
	 * ��ȡҵ��ϵͳ���
	 * @return
	 */
	public String getBussID() {
		return bussID;
	}
	/**
	 * ����ҵ��ϵͳ���
	 * @param bussID
	 */
	public void setBussID(String bussID) {
		this.bussID = bussID;
	}
	
}
