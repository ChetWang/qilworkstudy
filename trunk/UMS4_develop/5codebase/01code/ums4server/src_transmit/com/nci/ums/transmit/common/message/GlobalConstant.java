package com.nci.ums.transmit.common.message;

/**
 * <p>
 * ���⣺GlobalConstant.java
 * </p>
 * <p>
 * ������ ȫ�־�̬������
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2009-8-25
 * @version 1.0
 */
/**
 * <p>
 * ���⣺GlobalConstant.java
 * </p>
 * <p>
 * ������ 
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2009-8-25
 * @version 1.0
 */
/**
 * <p>
 * ���⣺GlobalConstant.java
 * </p>
 * <p>
 * ������ 
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2009-8-25
 * @version 1.0
 */
public class GlobalConstant {
	/**
	 * ������ʼ��
	 */
	public static final byte START_CHARACTER = 0x68;
	/**
	 * ������ֹ��
	 */
	public static final byte STOP_CHARACTER = 0x16;
	
	/**
	 * ������ȷ���޴���
	 */
	public static final byte ERR_RIGHT = 0x00;
	/**
	 * �������ݴ���
	 */
	public static final byte ERR_CONTENT = 0x02;
	/**
	 * ����Ȩ�޲���
	 */
	public static final byte ERR_PASSWORD = 0x03;
	/**
	 * ����Ŀ�ĵ�ַ�����ڻ��ѶϿ�����
	 */
	public static final byte ERR_TARGET = 0x11;
	/**
	 * ���ķ���ʧ��
	 */
	public static final byte ERR_SENT = 0x12;
	/**
	 * ���Ĺ���
	 */
	public static final byte ERR_DATA_LONGER = 0x13;
	
	/**
	 * ��ͬ��ַ�Ѿ�������ת��ƽ̨
	 */
	public static final byte ERR_ADDRESS_ALREAY_LOGIN = 0x14;
	/**
	 * Ӧ�ú���ʼλ��
	 */
	public static final int INDEX_MANUFACTURER = 1;
	/**
	 * �ն˺���ʼλ��
	 */
	public static final int INDEX_CONSUMER = 3;
	/**
	 * �������λ��
	 */
	public static final int INDEX_MSTA = 5;
	/**
	 * ֡��ˮ��λ��
	 */
	public static final int INDEX_FSEQ = 6;
	/**
	 * ֡����
	 */
	public static final int INDEX_FNUM = 7;
	/**
	 * ֡�����λ��
	 */
	public static final int INDEX_ISEQ = 8;
	/**
	 * �ڶ�����ʼ��λ��
	 */
	public static final int INDEX_SECOND_START = 9;
	/**
	 * ������λ��
	 */
	public static final int INDEX_CONTROL = 10;
	/**
	 * �����򳤶ȿ�ʼλ��
	 */
	public static final int INDEX_DATA_LENGTH = 11;
	/**
	 * ������ʼλ��
	 */
	public static final int INDEX_DATA = 13;
	/**
	 * ���ĳ��ȣ�����������
	 */
	public static final int MESSAGE_LENGTH = 15;
	
	/**
	 * ��������󳤶�
	 */
	public static final int DATA_MAX_LENGTH = 32*1024;
	/**
	 * ���֡�����
	 */
	public static final int ISEQ_MAX_NUMBER	= 255;
}
