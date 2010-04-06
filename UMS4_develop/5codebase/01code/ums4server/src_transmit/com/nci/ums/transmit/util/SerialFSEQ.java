package com.nci.ums.transmit.util;

/**
 * <p>
 * ���⣺SerialFSEQ.java
 * </p>
 * <p>
 * ������ ֡��Ų�����
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
public class SerialFSEQ {

	/**
	 * ֡��Ų��������
	 */
	private static SerialFSEQ fseq = null;

	/**
	 * ��ˮ�ż�����
	 */
	private int serial = 0;

	/**
	 * ��ˮ�������ֵ
	 */
	private int max_serial = 128;

	/**
	 * @���� ��ȡ֡��Ų��������ʵ��
	 * @return ֡��Ų��������ʵ��
	 *
	 * @Add by ZHM 2009-8-25
	 */
	public static SerialFSEQ getInstance() {
		if (fseq == null) {
			fseq = new SerialFSEQ();
		}
		return fseq;
	}

	/**
	 * @���� ��ȡ��ˮ��
	 * @return int ��ˮ��
	 * 
	 * @Add by ZHM 2009-8-25
	 */
	public synchronized int getSerial() {
		serial++;
		if (serial >= max_serial) {
			serial = 1;
		}
		return serial;
	}
}
