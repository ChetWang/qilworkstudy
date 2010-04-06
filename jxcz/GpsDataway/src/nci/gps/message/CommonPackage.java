package nci.gps.message;

import nci.gps.util.SerialFSEQ;

/**
 * @���ܣ���װ���õİ�
 * @ʱ�䣺2008-07-22
 * @author ZHOUHM
 *
 */
public class CommonPackage {
	
	/**
	 * ��ȡ��¼�ֽ���
	 * @param password 3���ֽڵ�BCD��
	 * @return
	 */
	public static byte[] getLoginPackage(byte[] password){
		PackageMessage pm = new PackageMessage();
		
//		byte[] logicalAddress = {(byte) 0x96,0x00,(byte) 0xC1,(byte) 0xD1};
//		pm.setLogicalAddress(logicalAddress);
		byte[] data = password;
		pm.setMstaSeq(0, SerialFSEQ.getInstance().getSerial());
		pm.setControlCode(false, false, 33);
		pm.setData(data);
		
		/*
		 * �����ֽ��飬��
		 * 6800000000200068210300������������1A16
		 */
		return pm.packageMessage();
	}
	
	/**
	 * ��ȡ�ǳ��ֽ���
	 * @return
	 */
	public static byte[] getLogoutPackage(){
		PackageMessage pm = new PackageMessage();
		
//		byte[] logicalAddress = {(byte) 0x96,0x00,(byte) 0xC1,(byte) 0xD1};
//		pm.setLogicalAddress(logicalAddress);
		pm.setMstaSeq(0, SerialFSEQ.getInstance().getSerial());
		pm.setControlCode(false, false, 34);
		
		/**
		 * �����ֽ��飬��
		 * 68000000002000682200001216
		 */
		return pm.packageMessage();
	}
	
	/**
	 * ��ȡ�����ֽ���
	 * @return
	 */
	public static byte[] getHeartBeatPackage(){
		PackageMessage pm = new PackageMessage();
		
		pm.setMstaSeq(0, SerialFSEQ.getInstance().getSerial());
		pm.setControlCode(false, false, 36);
//		byte[] logicalAddress = {(byte) 0x96,0x00,0x00,0x02};
//		pm.setLogicalAddress(logicalAddress);
		
		/**
		 * �����ֽ��飬��
		 * 68000000002000682400001416
		 */
		return pm.packageMessage();
	}
}
