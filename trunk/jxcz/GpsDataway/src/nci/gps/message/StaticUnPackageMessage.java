package nci.gps.message;


/**
 * @���ܣ���̬��������
 * @ʱ�䣺2008-07-23
 * @author ZHOUHM
 *
 */
public class StaticUnPackageMessage {
	
	/**
	 * ��ȡ�ն��߼���ַ
	 * @param message byte[] ��Ϣ�ֽ���
	 */
	public static byte[] getLogicalAddress(byte[] message){
		byte[] logicalAddress = subMessage(message, 1, 4);
		return logicalAddress;
	}
	
	/**
	 * ��ȡ�����򳤶�
	 * @param message
	 * @return
	 */
	public static int getDataLength(byte[] message) {
		byte[] dataLength = subMessage(message, 9, 2);
		int height = (dataLength[1] + 256) % 256;	// ��������Ӱ��
		int low = (dataLength[0] + 256) % 256;	// ��������Ӱ��
		return height * 256 + low;
	}
	
	/**
	 * ��ȡ������
	 * @param message
	 * @return
	 */
	public static byte[] getData(byte[] message){
		int length = getDataLength(message);
		byte[] data = subMessage(message, 11, length);
		return data;
	}
	
	/**
	 * ��ȡ������
	 * @param message
	 * @return
	 */
	public static byte getControlCode(byte[] message){
		byte[] bs = subMessage(message, 8, 1);
		return bs[0];
	}
	
	/**
	 * ����֡�Ƿ��Ƿ����ն˵�
	 * @param message
	 * @return
	 */
	public static boolean isToTerminal(byte[] message){
		byte controlCode = getControlCode(message);
		if ((controlCode & 0x80) == 0)
			return true;
		return false;
	}
	
	/**
	 * �õ�������Ĺ�����
	 * @param message ��������֡
	 * @return �������еĹ�����
	 */
	public static byte getFunctionCode(byte[] message){
		return (byte) (getControlCode(message) & 0x3F);
	}
	
	/**
	 * �����Ϣ�Ƿ�Ϊ�쳣��Ϣ
	 * @param message ��������֡
	 * @return trueΪ�쳣��Ϣ��falseΪ������Ϣ
	 */
	public static boolean getIsErrorCode(byte[] message){
		byte bError = (byte) (getControlCode(message)& 0x40);
		if(bError == 0) return false;
		else return true;
	}
	
	/**
	 * ��ȡ����֡�е���ˮ��
	 * @param message
	 * @return
	 */
	public static int getFSEQ(byte[] message){
		int fseq = -1;
		byte[] mstaSeq = subMessage(message, 5, 2);
		int msta0 = mstaSeq[0] & 0xC0;
		int msta1 = mstaSeq[1] & 0x1F;
		fseq = (msta1<<2) | (msta0)/64; 
		return fseq;
	}
	
	/**
	 * ��message�����н�ȡһ�����ֽ�
	 * @param message byte[] ��Ϣ�ֽ���
	 * @param begin int ��ʼλ��
	 * @param length int ��ȡ����
	 * @return
	 */
	public synchronized static byte[] subMessage(byte[] message, int begin, int length) {
		byte[] b = new byte[length];

		for (int i = 0; i < length; i++) {
			b[i] = message[begin + i];
		}

		return b;
	}
	
	/**
	 * ��message�����н�ȡһ�����ֽ�
	 * @param message byte[] ��Ϣ�ֽ���
	 * @param begin byte ��ʼ�ַ�
	 * @return
	 */
	public synchronized static byte[] subMessage(byte[] message, byte bByte) {
		int size = message.length;
		int begin = -1;
		// ��ѯbeginB��һ��λ��
		for(int i=0; i<size; i++){
			if(message[i] == bByte) {
				begin = i;
				break;
			}
		}
		byte[] b = new byte[message.length - begin];
		

		for (int i = 0; i < size; i++) {
			b[i-begin] = message[i];
		}

		return b;
	}

}
