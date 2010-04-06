package com.nci.ums.transmit.common.message;

import com.nci.ums.transmit.common.IllegalAdressException;
import com.nci.ums.transmit.util.ByteCoding;

/**
 * <p>
 * ���⣺PackageMessage.java
 * </p>
 * <p>
 * ������ ���Ĵ����
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
public class PackageMessage {

	// /**
	// * �ն��߼���ַ
	// */
	// private byte[] logicalAddress;
	/**
	 * Ӧ���߼���ַ
	 */
	private byte[] manufacturerAddress;
	/**
	 * �ն��߼���ַ
	 */
	private byte[] consumerAddress;
	/**
	 * ��վ��ַ���������
	 */
	private byte[] msta_seq;
	/**
	 * ������
	 */
	private byte controlCode;
	/**
	 * ���ݳ���
	 */
	private byte[] dataLength;
	/**
	 * ������
	 */
	private byte[] data;
	/**
	 * ��У��
	 */
	private byte cs;

	/**
	 * @���� ���캯��
	 * 
	 * @Add by ZHM 2009-8-24
	 */
	public PackageMessage() {
		// logicalAddress = new byte[4];
		manufacturerAddress = new byte[2];
		consumerAddress = new byte[2];
		msta_seq = new byte[4];
		controlCode = 0x00;
		dataLength = new byte[] { 0x00, 0x00 };
		data = new byte[0];
		cs = 0x00;
	}

	/**
	 * @���� ���Ĵ������
	 * @return byte[] ��ð��ı���
	 * 
	 * @Add by ZHM 2009-8-24
	 */
	public byte[] packageMessage() {
		int length = data.length;
		byte[] result = new byte[GlobalConstant.MESSAGE_LENGTH + length];

		// ֡��ʼ��
		result[0] = GlobalConstant.START_CHARACTER;
		// // �ն��߼���ַ
		// for (int i = 0; i < 4; i++) {
		// result[1 + i] = logicalAddress[i];
		// }
		// Ӧ���߼���ַ
		for (int i = 0; i < 2; i++) {
			result[GlobalConstant.INDEX_MANUFACTURER + i] = manufacturerAddress[i];
		}
		// �ն��߼���ַ
		for (int i = 0; i < 2; i++) {
			result[GlobalConstant.INDEX_CONSUMER + i] = consumerAddress[i];
		}
		// ��վ��ַ���������
		for (int i = 0; i < 4; i++) {
			result[GlobalConstant.INDEX_MSTA + i] = msta_seq[i];
		}
		// ֡��ʼ��
		result[GlobalConstant.INDEX_SECOND_START] = GlobalConstant.START_CHARACTER;
		// ������
		result[GlobalConstant.INDEX_CONTROL] = controlCode;
		// ���ݳ���
		for (int i = 0; i < 2; i++) {
			result[GlobalConstant.INDEX_DATA_LENGTH + i] = dataLength[i];
		}
		// ������
		for (int i = 0; i < length; i++) {
			result[GlobalConstant.INDEX_DATA + i] = data[i];
		}
		// ��У��
		setCS(result);
		result[GlobalConstant.INDEX_DATA + length] = cs;
		// ������
		result[GlobalConstant.INDEX_DATA + 1 + length] = GlobalConstant.STOP_CHARACTER;

		return result;
	}

	// /**
	// * @���� �����ն��߼���ַ
	// * @param logicalAddress byte[4] �ն��߼���ַ�ֽ���
	// *
	// * @Add by ZHM 2009-8-24
	// */
	// public void setLogicalAddress(byte[] logicalAddress){
	// this.logicalAddress = logicalAddress;
	// }

	/**
	 * @���� ����Ӧ���߼���ַ
	 * @param manufacturerAddress
	 *            byte[2] Ӧ���߼���ַ�ֽ���
	 * @return �ɹ�����Ӧ���߼���ַ��־
	 * @Add by ZHM 2009-8-24
	 */
	public boolean setManufacturerAddress(byte[] manufacturerAddress) {
		if (manufacturerAddress != null && manufacturerAddress.length == 2) {
			for (int i = 0; i < 2; i++) {
				this.manufacturerAddress[i] = manufacturerAddress[i];
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @���� ����Ӧ���߼���ַ
	 * @param manufacturerAddress
	 *            int Ӧ���߼���ַ(0��65536)
	 * @return ���óɹ���־
	 * 
	 * @Add by ZHM 2009-8-27
	 */
	public boolean setManufacturerAddress(int manufacturerAddress) {
		try {
			byte[] manufacturer = addressIntToByte(manufacturerAddress);
			return setManufacturerAddress(manufacturer);
		} catch (IllegalAdressException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @���� �����ն��߼���ַ
	 * @param consumerAddress
	 *            byte[2] �ն��߼���ַ�ֽ���
	 * @return �ɹ������ն��߼���ַ��־
	 * @Add by ZHM 2009-8-24
	 */
	public boolean setConsumerAddress(byte[] consumerAddress) {
		if (consumerAddress != null && consumerAddress.length == 2) {
			for (int i = 0; i < 2; i++) {
				this.consumerAddress[i] = consumerAddress[i];
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @���� �����ն��߼���ַ
	 * @param consumerAddress
	 *            int �ն��߼���ַ(0��65536)
	 * @return �����ն��߼���ַ�ɹ���־
	 * 
	 * @Add by ZHM 2009-8-27
	 */
	public boolean setConsumerAddress(int consumerAddress) {
		try {
			byte[] consumer = addressIntToByte(consumerAddress);
			return setConsumerAddress(consumer);
		} catch (IllegalAdressException e) {
//			Res.logExceptionTrace(e);
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @���� ������վ��ַ���������
	 * @param mstaSeq
	 *            byte[2] ��վ��ַ���������
	 * @return �ɹ�������վ��ַ��������ű�־
	 * 
	 * @Add by ZHM 2009-8-25
	 */
	public boolean setMstaSeq(byte[] mstaSeq) {
		if (mstaSeq != null && mstaSeq.length == 3) {
			for (int i = 0; i < 4; i++) {
				this.msta_seq[i] = mstaSeq[i];
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @���� ������վ��ַ���������
	 * @param order
	 *            int ������� ����:31/GPRS:32/Ethernet:35
	 * @param fseq
	 *            int ֡��ţ�01H~0FFH(255)ѭ������ʹ��
	 * @param fnum
	 *            int ֡������01H~0FFH(255)
	 * @param iseq
	 *            int ֡����ţ�0��ʾ��֡��1~255������255��ʾ���һ֡
	 * @return �ɹ�������վ��ַ��������ű�־
	 * 
	 * @Add by ZHM 2009-8-27
	 */
	public boolean setMstaSeq(int order, int fseq, int fnum, int iseq) {
		if ((order < 0 || order > 255) || (iseq < 0 || iseq > 255)
				|| (fnum < 0 || fnum > 255) || (fseq < 0 || fseq > 255))
			return false;
		byte[] mstaSeq = new byte[4];
		mstaSeq[0] = (byte) order;
		mstaSeq[1] = (byte) fseq;
		mstaSeq[2] = (byte) fnum;
		mstaSeq[3] = (byte) iseq;
		this.msta_seq = mstaSeq;
		return true;
	}

	// /**
	// * @���� ������վ��ַ���������
	// * @param iseq
	// * int ֡����ţ�0��ʾ��֡��1~6������7��ʾ���һ֡
	// * @param fseq
	// * int ֡��ţ�01H~07FH(127)ѭ������ʹ��
	// * @return �ɹ�������վ��ַ��������ű�־
	// * @Add by ZHM 2009-8-24
	// */
	// public boolean setMstaSeq(int iseq, int fseq) {
	// if (iseq > 7 || iseq < 0) {
	// // ֡����Ų��Ϸ�
	// return false;
	// }
	// if (fseq > 127 || fseq < 1) {
	// // ֡��Ų��Ϸ�
	// return false;
	// }
	//
	// byte[] mstaSeq = new byte[2];
	// /**
	// * ��վ��ַ ����ʹ��Ethernetͨ�ţ�D0~D5����35
	// */
	// ByteCoding obc = new ByteCoding(mstaSeq[0]);
	// obc.setBitTrue(0);
	// obc.setBitTrue(1);
	// obc.setBitTrue(5);
	//
	// /**
	// * ֡�����
	// */
	// byte bIseq = (byte) iseq;
	// bIseq <<= 5;
	//
	// /**
	// * ֡���
	// */
	// byte bFseq = (byte) fseq;
	// mstaSeq[0] = (byte) ((bFseq << 6) | obc.getData());
	// mstaSeq[1] = (byte) ((bFseq >> 2) | bIseq);
	//
	// this.msta_seq = mstaSeq;
	// return true;
	// }

	/**
	 * @���� ���ÿ�����
	 * @param controlCode
	 *            byte ������
	 * @return
	 * 
	 * @Add by ZHM 2009-8-25
	 */
	public boolean setControlCode(byte controlCode) {
		this.controlCode = controlCode;
		return true;
	}

	/**
	 * @���� ���ÿ�����
	 * @param direction
	 *            boolean ���䷽��false:��վ���նˣ�true:�նˡ���վ
	 * @param errFlag
	 *            boolean �쳣��־��false:������true:�쳣
	 * @param functionCode
	 *            int ������(����ʮ��������) 21H��¼��22H�˳���24H������顢0FH�û��Զ�������
	 * @return �ɹ����ÿ������־
	 * 
	 * @Add by ZHM 2009-8-24
	 */
	public boolean setControlCode(boolean direction, boolean errFlag,
			int functionCode) {
		if (functionCode > 63 || functionCode < 0) {
			// ������Ƿ�
			return false;
		}
		byte bCode = 0x00;
		ByteCoding bc = new ByteCoding(bCode);

		/**
		 * ���䷽�� 1��ʾ�ն˷�����վ��0��ʾ��վ�����ն�
		 */
		if (direction)
			bc.setBitTrue(7); // ��D7λ����Ϊ1
		else
			bc.setBitFalse(7); // ��D7λ����Ϊ0

		/**
		 * �쳣��־ 0ȷ��֡��1����֡
		 */
		if (errFlag)
			bc.setBitTrue(6);
		else
			bc.setBitFalse(6);

		/**
		 * ������ 0FH:15��ʾ�û��Զ������� 21H:33��ʾ��¼ 22H:34��ʾ�˳� 24H:36��ʾ�������
		 */
		byte bFunc = (byte) functionCode;
		bFunc = (byte) (bFunc & 0x3F); // ��ǰ��λ��Ϊ0

		/**
		 * �ϳɿ�����
		 */
		bCode = bc.getData();
		bCode = (byte) (bCode | bFunc);

		controlCode = bCode;
		return true;
	}

	/**
	 * @���� ����������
	 * @param data
	 *            byte[] �������ֽ���
	 * @return �ɹ������������־
	 * 
	 * @Add by ZHM 2009-8-24
	 */
	public boolean setData(byte[] data) {
		if (data != null) {
			this.data = data;
			setDataLength(data.length);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @���� �������ݳ��ȣ����ݳ���Ϊ�����򳤶ȣ�
	 * @param length
	 *            int ʮ���Ʊ�ʾ�����ݳ���
	 * 
	 * @Add by ZHM 2009-8-24
	 */
	private void setDataLength(int length) {
		byte[] bs = new byte[2];

		bs[0] = (byte) length;
		bs[1] = (byte) (length >> 8);

		dataLength = bs;
	}

	/**
	 * @���� ���ü�У��
	 * @param b
	 *            byte[] �����ֽ���
	 * 
	 * @Add by ZHM 2009-8-24
	 */
	private void setCS(byte[] b) {
		int size = b.length;
		int sum = 0;

		for (int i = 0; i < size - 2; i++) { // ���㵽��Уλ֮ǰ
			sum += b[i];
		}

		cs = (byte) sum;
	}

	/**
	 * @���� ��0��65536�������ת����byte[2]����
	 * @param address
	 *            int Ҫת���Ĳ���
	 * @return byte[2]
	 * @throws Exception
	 *             ���ݲ��ڷ�Χ��
	 * 
	 * @Add by ZHM 2009-8-26
	 */
	private byte[] addressIntToByte(int address) throws IllegalAdressException {
		if (address < 0 || address >= 65536) {
			throw new IllegalAdressException(address);
		}
		byte[] bt = new byte[2];
		bt[0] = (byte) (0xFF & address);
		bt[1] = (byte) ((0xFF00 & address) >> 8);
		return bt;
	}

}
