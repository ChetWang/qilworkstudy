package com.nci.svg.sdk.client.update;

/**
 * ��������洢����������Ϣ�Ķ���bean�����ݲ�ͬ��UpdateBean�����жϣ�SVGƽ̨�ͻ��˿����ж���Щ�ļ��������Ҫ������
 * 
 * @author Qil.Wong
 * 
 */
public class UpdateBean {

	/**
	 * ��������ļ��
	 */
	private String shortName;

	/**
	 * ���������ȫ��
	 */
	private String name;

	/**
	 * �������������
	 */
	private String type;

	/**
	 * ��������İ汾
	 */
	private String edition;

	/**
	 * �޲ι��캯��
	 */
	public UpdateBean() {

	}

	/**
	 * �вι��캯��
	 * @param shortName ��������ļ��
	 * @param name ���������ȫ��
	 * @param type �������������
	 * @param edition ��������İ汾
	 */
	public UpdateBean(String shortName, String name, String type, String edition) {
		this.shortName = shortName;
		this.name = name;
		this.type = type;
		this.edition = edition;
	}

	/**
	 * ��ȡ����ļ��
	 * 
	 * @return ����ļ��
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * ������������ļ��
	 * 
	 * @param shortName
	 *            ��������ļ��
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	/**
	 * ��ȡ���������ȫ��
	 * 
	 * @return ���������ȫ��
	 */
	public String getName() {
		return name;
	}

	/**
	 * �������������ȫ��
	 * 
	 * @param name
	 *            ���������ȫ��
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * ��ȡ�������������
	 * 
	 * @return �������������
	 */
	public String getType() {
		return type;
	}

	/**
	 * �����������������
	 * 
	 * @param type
	 *            �������������
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * ��ȡ��������汾
	 * 
	 * @return ��������汾
	 */
	public String getEdition() {
		return edition;
	}

	/**
	 * ������������汾
	 * 
	 * @param edition
	 *            ��������汾
	 */
	public void setEdition(String edition) {
		this.edition = edition;
	}

}
