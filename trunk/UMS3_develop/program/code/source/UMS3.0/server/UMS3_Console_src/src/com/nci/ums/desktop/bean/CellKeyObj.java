package com.nci.ums.desktop.bean;

/**
 * ��Swing�ؼ��д洢�Ķ��󣬿��ԱȽϣ����򣩣���ʾ������������ݷ���
 * @author Qil.Wong
 *
 */
public class CellKeyObj implements Comparable {
	Object keyObj;
	String name;

	public CellKeyObj() {
	}

	public CellKeyObj(Object keyObj, String name) {
		this.keyObj = keyObj;
		this.name = name;
	}

	/**
	 * ��ȡ����еĶ���
	 * @return ����
	 */
	public Object getKeyObj() {
		return keyObj;
	}

	/**
	 * �����������
	 * @param keyObj �������ֵ
	 */
	public void setKeyObj(Object keyObj) {
		this.keyObj = keyObj;
	}

	/**
	 * ��ȡ��������ڽ�������ʾ������
	 * @return ����
	 */
	public String getName() {
		return name;
	}

	/**
	 * ������������ڽ�������ʾ������
	 * @param name ����
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

	public int compareTo(Object obj) {
		return this.toString().compareTo(obj.toString());
	}

}
