package com.nci.ums.transmit.common.message;

import com.nci.ums.transmit.common.IllegalAdressException;
import com.nci.ums.transmit.util.ByteCoding;
import com.nci.ums.transmit.util.SerialFSEQ;

/**
 * <p>
 * ���⣺CommonMessage.java
 * </p>
 * <p>
 * ������ ���ñ���������
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
public class CommonMessage {

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
	private static byte[] addressIntToByte(int address) throws IllegalAdressException {
		if (address < 0 || address >= 65536) {
			throw new IllegalAdressException(address);
		}
		byte[] bt = new byte[2];
		bt[0] = (byte) (0xFF & address);
		bt[1] = (byte) ((0xFF00 & address) >> 8);
		return bt;
	}

	/**
	 * @���� ���ɵ�¼����
	 * @param manufacturerAddress
	 *            int Ӧ���߼���ַ(0��65536)
	 * @param consumerAddress
	 *            int �ն��߼���ַ(0��65536)
	 * @param direction
	 *            int ��¼����0:��վ��1:�ն�
	 * @return ��¼����
	 * @throws Exception
	 *             �߼���ַ���ڷ�Χ��
	 * 
	 * @Add by ZHM 2009-8-26
	 */
	public static byte[] getLoginMessage(int manufacturerAddress,
			int consumerAddress, int direction)
			throws IllegalAdressException {
		byte[] manufacturer = addressIntToByte(manufacturerAddress);
		byte[] consumer = addressIntToByte(consumerAddress);
		return getLoginMessage(manufacturer, consumer, direction);
	}

	/**
	 * @���� �����˳���¼����
	 * @param manufacturerAddress
	 *            int Ӧ���߼���ַ(0��65536)
	 * @param consumerAddress
	 *            int �ն��߼���ַ(0��65536)
	 * @param direction
	 *            int ��¼����0:��վ��1:�ն�
	 * @return �˳���¼����
	 * @throws Exception
	 *             �߼���ַ���ڷ�Χ��
	 * 
	 * @Add by ZHM 2009-8-26
	 */
	public static byte[] getLogoutMessage(int manufacturerAddress,
			int consumerAddress, int direction)
			throws IllegalAdressException {
		byte[] manufacturer = addressIntToByte(manufacturerAddress);
		byte[] consumer = addressIntToByte(consumerAddress);
		return getLogoutMessage(manufacturer, consumer, direction);
	}

	/**
	 * @���� ���ɵ�¼����
	 * @param manufacturerAddress
	 *            byte[] Ӧ���߼���ַ
	 * @param consumerAddress
	 *            byte[] �ն��߼���ַ
	 * @param direction
	 *            int ��¼����0:��վ��1:�ն�
	 * 
	 * @return ��¼����
	 * 
	 * @Add by ZHM 2009-8-25
	 */
	private static byte[] getLoginMessage(byte[] manufacturerAddress,
			byte[] consumerAddress, int direction) {
		PackageMessage pm = new PackageMessage();

		pm.setManufacturerAddress(manufacturerAddress);
		pm.setConsumerAddress(consumerAddress);
		// pm.setMstaSeq(0, SerialFSEQ.getInstance().getSerial());
		pm.setMstaSeq(35, SerialFSEQ.getInstance().getSerial(), 0, 0);
		if (direction > 0) {
			pm.setControlCode(true, false, 33);
		} else {
			pm.setControlCode(false, false, 33);
		}

		return pm.packageMessage();
	}

	/**
	 * @���� �����˳���¼����
	 * @param manufacturerAddress
	 *            byte[] Ӧ���߼���ַ
	 * @param consumerAddress
	 *            byte[] �ն��߼���ַ
	 * @param direction
	 *            int ��¼����0:��վ��1:�ն�
	 * 
	 * @return �˳���¼����
	 * 
	 * @Add by ZHM 2009-8-25
	 */
	private static byte[] getLogoutMessage(byte[] manufacturerAddress,
			byte[] consumerAddress, int direction) {
		PackageMessage pm = new PackageMessage();

		pm.setManufacturerAddress(manufacturerAddress);
		pm.setConsumerAddress(consumerAddress);
		// pm.setMstaSeq(0, SerialFSEQ.getInstance().getSerial());
		pm.setMstaSeq(35, SerialFSEQ.getInstance().getSerial(), 0, 0);
		if (direction > 0) {
			pm.setControlCode(true, false, 34);
		} else {
			pm.setControlCode(false, false, 34);
		}

		return pm.packageMessage();
	}

	/**
	 * @���� ������������
	 * @param manufacturerAddress
	 *            byte[] Ӧ���߼���ַ
	 * @param consumerAddress
	 *            byte[] �ն��߼���ַ
	 * @param direction
	 *            ���䷽��false:��վ���նˣ�true:�նˡ���վ
	 * @return ��������
	 * 
	 * @Add by ZHM 2009-8-25
	 */
	public static byte[] getHeartBeatPackage(byte[] manufacturerAddress,
			byte[] consumerAddress, boolean direction) {
		PackageMessage pm = new PackageMessage();

		pm.setManufacturerAddress(manufacturerAddress);
		pm.setConsumerAddress(consumerAddress);
		// pm.setMstaSeq(0, SerialFSEQ.getInstance().getSerial());
		pm.setMstaSeq(35, SerialFSEQ.getInstance().getSerial(), 0, 0);
		pm.setControlCode(direction, false, 36);

		return pm.packageMessage();
	}

	/**
	 * ��ȡ�����ֽ���
	 * 
	 * @return ��������
	 */
	public static byte[] getServerHeartBeatPackage() {
		PackageMessage pm = new PackageMessage();

		// pm.setMstaSeq(0, SerialFSEQ.getInstance().getSerial());
		pm.setMstaSeq(35, SerialFSEQ.getInstance().getSerial(), 0, 0);
		pm.setControlCode(false, false, 36);
		// byte[] logicalAddress = {(byte) 0x96,0x00,0x00,0x02};
		// pm.setLogicalAddress(logicalAddress);

		/**
		 * �����ֽ��飬�� 68000000002000682400001416
		 */
		return pm.packageMessage();
	}

	/**
	 * @���� ���ݷ��͵ı���������Ӧ���쳣Ӧ��֡
	 * @param message
	 *            byte[] ���յ��ı���
	 * @param errType
	 *            byte ��������
	 * @return �쳣Ӧ��֡
	 * 
	 * @Add by ZHM 2009-8-25
	 */
	public static byte[] getReturnErrMessage(byte[] message, byte errType) {
		byte[] manufacturerAddress = UnPackageMessage
				.getManufacturerAddressBytes(message);
		byte[] consumerAddress = UnPackageMessage
				.getConsumerAddressBytes(message);
		byte controlCode = UnPackageMessage.getControlCode(message);
		byte[] mstaSeq = UnPackageMessage.getMstaSeq(message);

		// ������
		byte[] data = new byte[1];
		data[0] = errType;
		// ���ɷ��ؿ�����
		ByteCoding bc = new ByteCoding(controlCode);
		bc.setBitTrue(6); // �����쳣��־
		controlCode = bc.getData();

		PackageMessage pm = new PackageMessage();
		pm.setConsumerAddress(consumerAddress);
		pm.setManufacturerAddress(manufacturerAddress);
		pm.setControlCode(controlCode);
		pm.setMstaSeq(mstaSeq);
		pm.setData(data);
		return pm.packageMessage();
	}
}
