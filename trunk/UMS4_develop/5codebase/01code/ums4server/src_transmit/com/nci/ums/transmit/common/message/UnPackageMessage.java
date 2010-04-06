package com.nci.ums.transmit.common.message;

/**
 * <p>
 * ���⣺StaticUnPackageMessage.java
 * </p>
 * <p>
 * ������ ��̬���ķ�����
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2009-8-24
 * @version 1.0
 */
public class UnPackageMessage {
	/**
	 * @���� ��鱨�ĳ����Ƿ�Ϸ�
	 * @param message
	 *            byte[] �����ֽ���
	 * @return ���ĳ�������Ϸ�����true��������Ϸ�����false
	 * 
	 * @Add by ZHM 2009-8-24
	 */
	public static boolean isRightLength(byte[] message) {
		int outLength = message.length;
		int inLength = getDataLength(message) + GlobalConstant.MESSAGE_LENGTH;
		if (outLength == inLength)
			return true;
		return false;
	}

	/**
	 * @���� ����У���Ƿ���ȷ
	 * @param message
	 *            byte[] �����ֽ���
	 * @return ��У�������ȷ����true��������󷵻�false
	 * 
	 * @Add by ZHM 2009-8-24
	 */
	public static boolean isRightCs(byte[] message) {
		int size = message.length;
		int sum = 0;

		for (int i = 0; i < size - 2; i++) { // ���㵽��Уλ֮ǰ
			sum += message[i];
		}

		byte cs = getCs(message);
		if ((byte) sum == cs)
			return true;
		return false;
	}

	/**
	 * @���� ��ȡ��У��
	 * @param message
	 *            byte[] �����ֽ���
	 * @return ��У��
	 * 
	 * @Add by ZHM 2009-8-24
	 */
	private static byte getCs(byte[] message) {
		int length = getDataLength(message);
		byte[] cs = subMessage(message, length + GlobalConstant.INDEX_DATA, 1);
		return cs[0];
	}

	/**
	 * @���� ��ȡӦ���߼���ַ
	 * @param message
	 *            byte[] �����ֽ���
	 * @return Ӧ���߼���ַ
	 * 
	 * @Add by ZHM 2009-8-24
	 */
	public static byte[] getManufacturerAddressBytes(byte[] message) {
		byte[] manufacturerAddress = subMessage(message,
				GlobalConstant.INDEX_MANUFACTURER, 2);
		return manufacturerAddress;
	}

	/**
	 * @���� ��ȡӦ���߼���ַ
	 * @param message
	 *            byte[] �����ֽ���
	 * @return Ӧ���߼���ַ
	 * 
	 * @Add by ZHM 2009-8-26
	 */
	public static int getManufactureAddress(byte[] message) {
		byte[] manufacturerAddress = subMessage(message,
				GlobalConstant.INDEX_MANUFACTURER, 2);
		return twoBytesToInt(manufacturerAddress);
	}

	/**
	 * @���� ��ȡ�ն��߼���ַ
	 * @param message
	 *            byte[] �����ֽ���
	 * @return byte[] �ն��߼��ֽ���
	 * 
	 * @Add by ZHM 2009-8-24
	 */
	public static byte[] getConsumerAddressBytes(byte[] message) {
		byte[] consumerAddress = subMessage(message,
				GlobalConstant.INDEX_CONSUMER, 2);
		return consumerAddress;
	}

	/**
	 * @���� ��ȡ�ն��߼���ַ
	 * @param message
	 *            byte[] �����ֽ���
	 * @return �ն��߼��ֽ���
	 * 
	 * @Add by ZHM 2009-8-26
	 */
	public static int getConsumerAddress(byte[] message) {
		byte[] consumerAddress = subMessage(message,
				GlobalConstant.INDEX_CONSUMER, 2);
		return twoBytesToInt(consumerAddress);
	}

	/**
	 * @���� ��byte[2]����ת����int
	 * @param bytes
	 *            byte[2] �ֽ�����
	 * @return ����
	 * 
	 * @Add by ZHM 2009-8-26
	 */
	public static int twoBytesToInt(byte[] bytes) {
		int num = bytes[0] & 0xFF;
		if (bytes.length >= 2)
			num |= ((bytes[1] << 8) & 0xFF00);
		return num;
	}

	/**
	 * @���� ��ȡ�����򳤶�
	 * @param message
	 *            byte[] �����ֽ���
	 * @return int �����򳤶�
	 * 
	 * @Add by ZHM 2009-8-24
	 */
	public static int getDataLength(byte[] message) {
		byte[] dataLength = subMessage(message,
				GlobalConstant.INDEX_DATA_LENGTH, 2);
		int height = (dataLength[1] + 256) % 256; // ��������Ӱ��
		int low = (dataLength[0] + 256) % 256; // ��������Ӱ��
		return height * 256 + low;
	}

	/**
	 * @���� ��ȡ������
	 * @param message
	 *            byte[] �����ֽ���
	 * @return byte[] �������ֽ���
	 * 
	 * @Add by ZHM 2009-8-24
	 */
	public static byte[] getData(byte[] message) {
		int length = getDataLength(message);
		byte[] data = subMessage(message, GlobalConstant.INDEX_DATA, length);
		return data;
	}

	/**
	 * @���� ��ȡ������
	 * @param message
	 *            byte[] �����ֽ���
	 * @return byte ������
	 * 
	 * @Add by ZHM 2009-8-24
	 */
	public static byte getControlCode(byte[] message) {
		byte[] bs = subMessage(message, GlobalConstant.INDEX_CONTROL, 1);
		return bs[0];
	}

	/**
	 * @���� ��鱨���Ƿ��͸�������
	 * @param message
	 *            byte[] �����ֽ���
	 * @return boolean ��վ���ն�:true���նˡ���վ:false
	 * 
	 * @Add by ZHM 2009-8-24
	 */
	public static boolean isToTerminal(byte[] message) {
		byte controlCode = getControlCode(message);
		if ((controlCode & 0x80) == 0)
			return true;
		return false;
	}

	/**
	 * @���� ��ȡ������Ĺ�����
	 * @param message
	 *            byte[] �����ֽ���
	 * @return byte �������еĹ�����
	 * 
	 * @Add by ZHM 2009-8-24
	 */
	public static byte getFunctionCode(byte[] message) {
		return (byte) (getControlCode(message) & 0x3F);
	}

	/**
	 * @���� ��鱨���Ƿ�Ϊ�쳣����
	 * @param message
	 *            byte[] �����ֽ���
	 * @return boolean �쳣(true)/����(false)
	 * 
	 * @Add by ZHM 2009-8-24
	 */
	public static boolean isErrorMessage(byte[] message) {
		byte bError = (byte) (getControlCode(message) & 0x40);
		if (bError == 0)
			return false;
		else
			return true;
	}
	
	/**
	 * @���� ��ȡ����֡�е��������
	 * @param message byte[] �����ֽ���
	 * @return int ����֡�е��������
	 *
	 * @Add by ZHM 2009-8-28
	 */
	public static int getMSTA(byte[] message){
		byte[] msta = subMessage(message, GlobalConstant.INDEX_MSTA, 1);
		return twoBytesToInt(msta);
	}

	/**
	 * @���� ��ȡ����֡�е���ˮ��
	 * @param message byte[] �����ֽ���
	 * @return int ����֡�е���ˮ��
	 *
	 * @Add by ZHM 2009-8-27
	 */
	public static int getFSEQ(byte[] message){
		byte[] fseq = subMessage(message, GlobalConstant.INDEX_FSEQ,1);
		return twoBytesToInt(fseq);
	}
	
	/**
	 * @���� ��ȡ����֡�е�֡����
	 * @param message byte[] �����ֽ���
	 * @return ����֡�е�֡����
	 *
	 * @Add by ZHM 2009-9-6
	 */
	public static int getFNUM(byte[] message){
		byte[] fnum = subMessage(message, GlobalConstant.INDEX_FNUM, 1);
		return twoBytesToInt(fnum);
	}
	
	/**
	 * @���� ��ȡ���ĵ�֡�����
	 * @param message byte[] �����ֽ���
	 * @return ֡�����
	 *
	 * @Add by ZHM 2009-8-27
	 */
	public static int getISEQ(byte[] message){
		byte[] iseq = subMessage(message, GlobalConstant.INDEX_ISEQ, 1);
		return twoBytesToInt(iseq);
	}

	// /**
	// * @���� ��ȡ����֡�е���ˮ��
	// * @param message
	// * byte[] �����ֽ���
	// * @return int ����֡�е���ˮ��
	// *
	// * @Add by ZHM 2009-8-24
	// */
	// public static int getFSEQ(byte[] message) {
	// int fseq = -1;
	// byte[] mstaSeq = subMessage(message, 5, 2);
	// int msta0 = mstaSeq[0] & 0xC0;
	// int msta1 = mstaSeq[1] & 0x1F;
	// fseq = (msta1 << 2) | (msta0) / 64;
	// return fseq;
	// }
	
	 /**
	 * @���� ��ȡ��վ��ַ���������
	 * @param message
	 * byte[] �����ֽ���
	 * @return byte[2] ��վ��ַ���������
	 *
	 * @Add by ZHM 2009-8-25
	 */
	 public static byte[] getMstaSeq(byte[] message) {
	 return subMessage(message, GlobalConstant.INDEX_MSTA, 4);
	 }

	/**
	 * @���� �ӱ����н�ȡָ�����ȵ��ֽ�
	 * @param message
	 *            byte[] �����ֽ���
	 * @param begin
	 *            int ��ʼλ��
	 * @param length
	 *            int ��ȡ����
	 * @return byte[] ��ȡ���ֽ���
	 * 
	 * @Add by ZHM 2009-8-24
	 */
	private static byte[] subMessage(byte[] message, int begin, int length) {
		byte[] b = new byte[length];

//		for (int i = 0; i < length; i++) {
//			b[i] = message[begin + i];
//		}
        System.arraycopy(message, begin, b, 0, length);

		return b;
	}

	// /**
	// * @���� �ӱ����н�ȡָ��λ�ú���ֽ�
	// * @param message
	// * byte[] �����ֽ���
	// * @param bByte
	// * byte ��ʼ�ַ�
	// * @return byte[] ��ȡ���ֽ���
	// *
	// * @Add by ZHM 2009-8-24
	// */
	// private static byte[] subMessage(byte[] message, byte bByte) {
	// int size = message.length;
	// int begin = -1;
	// // ��ѯbeginB��һ��λ��
	// for (int i = 0; i < size; i++) {
	// if (message[i] == bByte) {
	// begin = i;
	// break;
	// }
	// }
	// byte[] b = new byte[message.length - begin];
	//
	// for (int i = 0; i < size; i++) {
	// b[i - begin] = message[i];
	// }
	//
	//		return b;
	//	}

}
