package nci.gps.util;


public class CellByteCoding {
	private int[] bit;	// 整数表示的字节
	
	public CellByteCoding(){
		bit = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };	// 初始化为8位
	}
	
	/**
	 * 对bit进行逐位设置
	 * @param index int 指定位号
	 * @param flag boolean 设置值，true时为1，false时为0
	 */
	public void setBitValue(int index, boolean flag){
		if (index >= 0 && index < 8)
			bit[index] = flag ? 1 : 0;
	}
	
	/**
	 * 将指定二进制表示的字符串转变成字节
	 * 如输入：00110011转换成0x33字节，十进制为51
	 * @param bit
	 * @return
	 */
	public byte getByte(){
		String[] strH = { "0000", "0001", "0010", "0011", "0100", "0101",
				"0110", "0111", "1000", "1001", "1010", "1011", "1100", "1101",
				"1110", "1111" };
		byte[] bH = "0123456789ABCDEF".getBytes();
		
		String bStr = getBitString();
		
		if(bStr.length() != 8) 
			throw new IllegalArgumentException("长度不为8");
		
		String heighStr = bStr.substring(0, 4);
		String lowStr = bStr.substring(4);
		byte heighB = bH[findIndex(heighStr, strH)];
		byte lowB = bH[findIndex(lowStr, strH)];
		
		byte b = uniteBytes(heighB, lowB);
		return b;
	}
	
	/**
	 * 获得bit的字符表现形式
	 * @return
	 */
	private String getBitString(){
		StringBuffer result = new StringBuffer();
		for (int i = bit.length-1; i >=0; i--) {
			result.append(Integer.toString(bit[i]));
		}
		return result.toString();
	}
	
	/**
	 * 查找四位字符串对应的数值
	 * 如：0011对应3
	 * @param str
	 * @param strH
	 * @return
	 */
	private int findIndex(String str, String[] strH){
		for(int i=0,size=strH.length; i<size; i++){
			if(str.equals(strH[i])) return i;
		}
		return -1;
	}
	
	/** 
	  * 将两个ASCII字符合成一个字节； 
	  * 如："EF"--> 0xEF 
	  * @param src0 byte 
	  * @param src1 byte 
	  * @return byte 
	  */ 
	private byte uniteBytes(byte src0, byte src1) { 
	   byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue(); 
	   _b0 = (byte)(_b0 << 4); 
	   byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue(); 
	   byte ret = (byte)(_b0 ^ _b1); 
	   return ret; 
	} 

}
