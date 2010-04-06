package nci.gps.util;

public class ByteCoding {
	private byte data;
	
	public ByteCoding(byte data){
		this.data = data;
	}
	
	public byte getData(){
		return this.data;
	}
	
	/**
	 * ��ָ��λ���ó�1
	 * @param index 0~7
	 */
	public void setBitTrue(int index){
		if(index>=0 && index <8){
			byte b = 1;
			b <<= index;
			data = (byte) (data | b);
		}
	}
	
	/**
	 * ��ָ��λ���ó�0
	 * @param index
	 */
	public void setBitFalse(int index) {
		if (index >= 0 && index < 8) {
			// ��ȡָ��λΪ0����λΪ1���ֽ�
			byte b = (byte) 0xFF;
			byte c = 1;
			c <<= index;
			b = (byte) (b^c);
			// ��ָ��λ���ó�0
			data = (byte) (data&b);
		}
	}
	
	/**
	 * ��ȡ�ֽ���ָ��λ��ֵ
	 * @param index λ��
	 * @return ��λֵ��-1��ʾ�����indexֵ����
	 */
	public int getBit(int index){
		if(index >=0 && index < 8){
			byte b = 1;
			b <<= index;
			
			if((b&data) != 0) return 1;
			else return 0;
		}
		return -1;
	}
}
