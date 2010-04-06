package com.nci.ums.transmit.util;

/**
 * <p>
 * ���⣺ByteCoding.java
 * </p>
 * <p>
 * ������ �ֽڴ�����
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2009-8-20
 * @version 1.0
 */
public class ByteCoding {
	private byte data;

	/**
	 * @���� ���캯��
	 * @param data
	 *            byte Ҫ�������ֽڶ���
	 * 
	 * @Add by ZHM 2009-8-20
	 */
	public ByteCoding(byte data) {
		this.data = data;
	}

	/**
	 * @���� ��ȡ�ֽ�ֵ
	 * @return byte �ֽ�ֵ
	 * 
	 * @Add by ZHM 2009-8-20
	 */
	public byte getData() {
		return this.data;
	}

	/**
	 * @���� ��ָ��λ���ó�1
	 * @param index
	 *            int λ�ñ�ʶ(0~7)
	 * 
	 * @Add by ZHM 2009-8-20
	 */
	public void setBitTrue(int index) {
		if (index >= 0 && index < 8) {
			byte b = 1;
			b <<= index;
			data = (byte) (data | b);
		}
	}

	/**
	 * @���� ��ָ��λ���ó�0
	 * @param index
	 *            int λ�ñ�ʶ(0~7)
	 * 
	 * @Add by ZHM 2009-8-20
	 */
	public void setBitFalse(int index) {
		if (index >= 0 && index < 8) {
			// ��ȡָ��λΪ0����λΪ1���ֽ�
			byte b = (byte) 0xFF;
			byte c = 1;
			c <<= index;
			b = (byte) (b ^ c);
			// ��ָ��λ���ó�0
			data = (byte) (data & b);
		}
	}

	/**
	 * @���� ��ȡ�ֽ���ָ��λ��ֵ
	 * @param index
	 *            int λ�ñ�ʶ(0~7)
	 * @return int ��λ��ֵ��-1��ʾ�����indexֵ����
	 * 
	 * @Add by ZHM 2009-8-21
	 */
	public int getBit(int index) {
		if (index >= 0 && index < 8) {
			byte b = 1;
			b <<= index;

			if ((b & data) != 0)
				return 1;
			else
				return 0;
		}
		return -1;
	}
}
