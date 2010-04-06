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
	 * 将指定位设置成1
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
	 * 将指定位设置成0
	 * @param index
	 */
	public void setBitFalse(int index) {
		if (index >= 0 && index < 8) {
			// 获取指定位为0其它位为1的字节
			byte b = (byte) 0xFF;
			byte c = 1;
			c <<= index;
			b = (byte) (b^c);
			// 将指定位设置成0
			data = (byte) (data&b);
		}
	}
	
	/**
	 * 获取字节中指定位的值
	 * @param index 位号
	 * @return 该位值，-1表示输入的index值不对
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
