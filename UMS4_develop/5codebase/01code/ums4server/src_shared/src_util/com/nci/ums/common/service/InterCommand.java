package com.nci.ums.common.service;

/**
 * ƽ̨�ڲ�UMS��������
 * 
 * @author Qil.Wong
 * 
 */
public interface InterCommand {

	/**
	 * ��������
	 */
	public static final int MEDIA_ADD = 10;
	/**
	 * �޸�����
	 */
	public static final int MEDIA_UPDATE = 11;
	/**
	 * ɾ������
	 */
	public static final int MEDIA_DELETE = 12;
	/**
	 * ��������
	 */
	public static final int MEDIA_START = 13;
	/**
	 * ֹͣ����
	 */
	public static final int MEDIA_STOP = 14;
	/**
	 * ����Ӧ��
	 */
	public static final int APP_ADD = 20;
	/**
	 * �޸�Ӧ��
	 */
	public static final int APP_UPDATE = 21;
	/**
	 * ɾ��Ӧ��
	 */
	public static final int APP_DELETE = 22;
	/**
	 * ����Ӧ��
	 */
	public static final int APP_START= 23;
	/**
	 * ֹͣӦ��
	 */
	public static final int APP_STOP = 24;
	/**
	 * ��������
	 */
	public static final int SERVICE_ADD = 30;
	/**
	 * �޸ķ���
	 */
	public static final int SERVICE_UPDATE = 31;
	/**
	 * ɾ������
	 */
	public static final int SERVICE_DELETE = 32;
	/**
	 * ��������
	 */
	public static final int SERVICE_START = 33;
	/**
	 * ֹͣ����
	 */
	public static final int SERVICE_STOP = 34;
	/**
	 * ����ת������
	 */
	public static final int FORWARD_CONTENT_ADD = 40;
	/**
	 * �޸�ת������
	 */
	public static final int FORWARD_CONTENT_UPDATE = 41;
	/**
	 * ɾ��ת������
	 */
	public static final int FORWARD_CONTENT_DELETE = 42;
	/**
	 * ʹ��ת������
	 */
	public static final int FORWARD_CONTENT_START = 43;
	/**
	 * ͣ��ת������
	 */
	public static final int FORWARD_CONTENT_STOP = 44;
	/**
	 * �����շ�
	 */
	public static final int FEE_ADD = 50;
	/**
	 * �޸��շ�
	 */
	public static final int FEE_UPDATE = 51;
	/**
	 * ɾ���շ�
	 */
	public static final int FEE_DELETE = 52;
	
	/**
	 * ��������
	 */
	public static final int FILTER_ADD = 60;
	/**
	 * �޸Ĺ���
	 */
	public static final int FILTER_UPDATE = 61;
	/**
	 * ɾ������
	 */
	public static final int FILTER_DELETE = 62;

	/**
	 * ʹ�ù���
	 */
	public static final int FILTER_START = 63;
	/**
	 * ͣ�ù���
	 */
	public static final int FILTER_STOP = 64;

}
