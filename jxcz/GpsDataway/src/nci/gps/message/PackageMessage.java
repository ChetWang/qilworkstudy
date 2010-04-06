package nci.gps.message;

import nci.gps.util.ByteCoding;

/**
 * @���ܣ���Ϣ�����
 * @ʱ�䣺2008-07-21
 * @author ZHOUHM
 *
 */
public class PackageMessage {
	
	// �ն��߼���ַ
	private byte[] logicalAddress;	
	// ��վ��ַ���������
	private byte[] msta_seq;
	// ������
	private byte controlCode;
	// ���ݳ���
	private byte[] dataLength;
	// ������
	private byte[] data;
	// ��У��
	private byte cs;
	
	/**
	 * ���캯��
	 *
	 */
	public PackageMessage(){
		logicalAddress = new byte[4];
		msta_seq = new byte[2];
		controlCode = 0x00;
		dataLength = new byte[]{0x00,0x00};
		data = new byte[0];
		cs = 0x00;
	}
	
	
	/**
	 * ��Ϣ�������
	 * @return ��ð�����Ϣ
	 */
	public byte[] packageMessage() {
		int length = data.length;
		byte[] result = new byte[13 + length];
		
		// ֡��ʼ��
		result[0] = 0x68;
		// �ն��߼���ַ
		for (int i = 0; i < 4; i++) {
			result[1 + i] = logicalAddress[i];
		}
		// ��վ��ַ���������
		for (int i = 0; i < 2; i++) {
			result[5 + i] = msta_seq[i];
		}
		// ֡��ʼ��
		result[7] = 0x68;
		// ������
		result[8] = controlCode;
		// ���ݳ���
		for (int i = 0; i < 2; i++) {
			result[9 + i] = dataLength[i];
		}
		// ������
		for (int i = 0; i < length; i++) {
			result[11 + i] = data[i];
		}
		// ��У��
		setCS(result);
		result[11 + length] = cs;
		// ������
		result[12 + length] = 0x16;

		return result;
	}
	
	/**
	 * �����ն��߼���ַ
	 * @param logicalAddress
	 */
	public void setLogicalAddress(byte[] logicalAddress){
		this.logicalAddress = logicalAddress;
	}
	
	/**
	 * ������վ��ַ���������
	 * @param iseq ֡����ţ�0��ʾ��֡��1~6������7��ʾ���һ֡
	 * @param fseq ֡��ţ�01H~07FHѭ������ʹ��
	 * @return 
	 */
	public void setMstaSeq(int iseq, int fseq){
		byte[] mstaSeq = new byte[2];
		
		/**
		 * ��վ��ַ
		 * ����ʹ��GPRSͨ�ţ�D0~D5����32
		 */
		ByteCoding obc = new ByteCoding(mstaSeq[0]);
		obc.setBitTrue(5);
		
		/**
		 * ֡�����
		 */
		byte bIseq = (byte) iseq;
		bIseq <<= 5;
		
		/**
		 * ֡���
		 */
		byte bFseq = (byte) fseq;
		mstaSeq[0] = (byte) ((bFseq << 6)|obc.getData());
		mstaSeq[1] = (byte) ((bFseq >> 2)|bIseq);
		
		this.msta_seq = mstaSeq;
	}
	
	/**
	 * ���ÿ�����
	 * 
	 * @param direction
	 *            boolean ���䷽��false��ʾ��վ�����նˣ�true��ʾ�ն˷�����վ
	 * @param errFlag
	 *            boolean �쳣��־ һ��Ϊ0
	 * @param functionCode
	 *            int ������(����ʮ��������)
	 *            21H��¼��22H�˳���24H������顢0FH�û��Զ�������
	 * @return byte ������
	 */
	public void setControlCode(boolean direction, boolean errFlag, int functionCode){
		byte bCode = 0x00;
		ByteCoding bc = new ByteCoding(bCode);
		
		/**
		 * ���䷽��
		 * 1��ʾ�ն˷�����վ��0��ʾ��վ�����ն�
		 */
		if(direction)
			bc.setBitTrue(7);	// ��D7λ����Ϊ1
		else 
			bc.setBitFalse(7);	// ��D7λ����Ϊ0
		
		/**
		 * �쳣��־
		 * 0ȷ��֡��1����֡
		 */
		if(errFlag)
			bc.setBitTrue(6);
		else
			bc.setBitFalse(6);
		
		/**
		 * ������
		 * 0FH:15��ʾ�û��Զ�������
		 * 21H:33��ʾ��¼
		 * 22H:34��ʾ�˳�
		 * 24H:36��ʾ�������
		 */
		byte bFunc = (byte) functionCode;
		bFunc = (byte) (bFunc & 0x3F);	// ��ǰ��λ��Ϊ0
		
		/**
		 * �ϳɿ�����
		 */
		bCode = bc.getData();
		bCode = (byte) (bCode | bFunc);
		
		controlCode = bCode;
	}
	
	/**
	 * ����������
	 * @param data
	 */
	public void setData(byte[] data){
		this.data = data;
		setDataLength(data.length);
	}
	
	/**
	 * �������ݳ���
	 * ���ݳ���Ϊ�����򳤶�
	 * @param length int ʮ���Ʊ�ʾ�����ݳ���
	 * @return
	 */
	private void setDataLength(int length){
		byte[] bs = new byte[2];
		
		bs[0] = (byte) length;
		bs[1] = (byte) (length >> 8);
		
		dataLength = bs;
	}
	
	/**
	 * ���ü�У��
	 * @param b �ֽ�����
	 * @return
	 */
	private void setCS(byte[] b){
		int size = b.length;
		int sum = 0;
		
		for(int i=0; i<size-2; i++){	// ���㵽��Уλ֮ǰ
			sum += b[i];
		}
		
		cs = (byte)sum;
	}
}
