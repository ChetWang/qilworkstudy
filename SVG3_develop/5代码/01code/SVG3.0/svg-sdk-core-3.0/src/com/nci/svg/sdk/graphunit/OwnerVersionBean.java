package com.nci.svg.sdk.graphunit;

import java.io.Serializable;


/**
 * @author Qil.Wong
 *
 */
public class OwnerVersionBean implements Serializable{

	private static final long serialVersionUID = 6410099358621696695L;

	/**
	 * ͼԪ������
	 */
	private String owner = null;

	/**
	 * ͼԪ�汾
	 */
	private String version = null;

	/**
	 * OwnerVersionBean toString()������owner��version�ļ��
	 */
	public static final String BEAN_STR_SEP = "BEAN_STR_SEP";
	
	/**
	 * ���Owner����Ҫ���client�����������ݣ���ļ��
	 */
	public static final String OWNER_SEP = "OWNER_SEP";
	
	/**
	 * �汾��Ϣ��ļ������Ҫ��Է���˰汾��Ϣ���͸��ͻ���
	 */
	public static final String VERSION_SEP = "VERSION_SEP";
	
	/**
	 * �ѷ�����������
	 */
	public static final String OWNER_RELEASED = "released";

	/**
	 * �޲ι���
	 */
	public OwnerVersionBean() {
	}

	/**
	 * �вι���
	 * @param owner ������
	 * @param version �汾
	 */
	public OwnerVersionBean(String owner, String version) {
		this.owner = owner;
		this.version = version;
	}

	/**
	 * ��ȡ��������Ϣ
	 * @return ������
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * ������������Ϣ
	 * @param owner ������
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * ��ȡ�汾��Ϣ
	 * @return �汾��Ϣ
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * ���ð汾��Ϣ
	 * @param version �汾��Ϣ
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	public String toString() {
		return owner + BEAN_STR_SEP + version;
	}

}
