package com.nci.svg.server.automapping.comm;

import java.util.LinkedHashMap;

/**
 * <p>
 * ���⣺BasicPorperty.java
 * </p>
 * <p>
 * ����������������
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2009-03-05
 * @version 1.0
 */
public class BasicProperty {
	/**
	 * �豸�������Զ���
	 */
	private LinkedHashMap properties;
	/**
	 * �豸�����������ƶ���
	 */
	private String[] propertNames;

	/**
	 * ���캯��
	 */
	public BasicProperty() {
		properties = new LinkedHashMap();
		propertNames = new String[0];
	}

	/**
	 * ���캯��
	 * 
	 * @param propertName
	 */
	public BasicProperty(String[] propertName) {
		if (propertName != null && propertName.length > 0)
			this.propertNames = propertName;
		else
			this.propertNames = new String[0];
		properties = new LinkedHashMap();
	}

	/**
	 * ��ȡָ���������Ƶ�����ֵ
	 * 
	 * @param propertName:Object:����ֵ
	 * @return
	 */
	public Object getProperty(String proName) {
		if (checkNames(proName))
			return properties.get(proName);
		else
			return null;
	}

	/**
	 * ����ָ���������Ƶ�����ֵ
	 * 
	 * @param proName
	 * @param proValue
	 */
	public void setProperty(String proName, Object proValue) {
		if (checkNames(proName))
			properties.put(proName, proValue);
	}

	/**
	 * �ж�ָ�����������Ƿ���������Զ�����
	 * 
	 * @param proName:String:������
	 * @return �Ƿ����
	 */
	private boolean checkNames(String proName) {
		for (int i = 0, size = propertNames.length; i < size; i++) {
			if (proName.equals(propertNames[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * ��ȡ�豸�����������ƶ���
	 * 
	 * @return String
	 */
	public String[] getPropertNames() {
		return propertNames;
	}

	/**
	 * �����豸�����������ƶ���
	 * 
	 * @param propertName:String[]:�豸�����������ƶ���
	 */
	public void setPropertNames(String[] propertNames) {
		this.propertNames = propertNames;
	}

	/**
	 * ��ȡ��������ֵ
	 * 
	 * @return LinkedHashMap
	 */
	protected LinkedHashMap getProperties() {
		return properties;
	}

	/**
	 * ���û�������ֵ
	 * 
	 * @param properties:LinkedHashMap:��������ֵ
	 */
	protected void setProperties(LinkedHashMap properties) {
		this.properties = properties;
	}
}
