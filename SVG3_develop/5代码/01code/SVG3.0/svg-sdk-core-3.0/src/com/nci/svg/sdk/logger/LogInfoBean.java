package com.nci.svg.sdk.logger;

/**
 * ��־��Ϣ����
 * @author Qil.Wong
 *
 */
public class LogInfoBean {
	
	//��־��¼�µĲ�����
	private String user;
	
	//��־��¼�µĲ�������
	private String operType;
	
	//��־��¼�µĲ�������
	private String operDesc;

	/**
	 * ��ȡ������
	 * @return ������
	 */
	public String getUser() {
		return user;
	}

	/**
	 * ���ò�����
	 * @param user ������
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * ��ȡ��������
	 * @return ��������
	 */
	public String getOperType() {
		return operType;
	}

	/**
	 * ���ò�������
	 * @param operType ��������
	 */
	public void setOperType(String operType) {
		this.operType = operType;
	}

	/**
	 * ��ȡ��������
	 * @return ��������
	 */
	public String getOperDesc() {
		return operDesc;
	}

	/**
	 * ���ò�������
	 * @param operDesc ��������
	 */
	public void setOperDesc(String operDesc) {
		this.operDesc = operDesc;
	}
	
	

}
