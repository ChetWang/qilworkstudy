package nci.gps.message;


/**
 * @功能：静态分析包类
 * @时间：2008-07-23
 * @author ZHOUHM
 *
 */
public class StaticUnPackageMessage {
	
	/**
	 * 获取终端逻辑地址
	 * @param message byte[] 消息字节组
	 */
	public static byte[] getLogicalAddress(byte[] message){
		byte[] logicalAddress = subMessage(message, 1, 4);
		return logicalAddress;
	}
	
	/**
	 * 获取数据域长度
	 * @param message
	 * @return
	 */
	public static int getDataLength(byte[] message) {
		byte[] dataLength = subMessage(message, 9, 2);
		int height = (dataLength[1] + 256) % 256;	// 消除负数影响
		int low = (dataLength[0] + 256) % 256;	// 消除负数影响
		return height * 256 + low;
	}
	
	/**
	 * 获取数据域
	 * @param message
	 * @return
	 */
	public static byte[] getData(byte[] message){
		int length = getDataLength(message);
		byte[] data = subMessage(message, 11, length);
		return data;
	}
	
	/**
	 * 获取控制码
	 * @param message
	 * @return
	 */
	public static byte getControlCode(byte[] message){
		byte[] bs = subMessage(message, 8, 1);
		return bs[0];
	}
	
	/**
	 * 数据帧是否是发向终端的
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
	 * 得到控制码的功能码
	 * @param message 整个数据帧
	 * @return 控制码中的功能码
	 */
	public static byte getFunctionCode(byte[] message){
		return (byte) (getControlCode(message) & 0x3F);
	}
	
	/**
	 * 检查消息是否为异常消息
	 * @param message 整个数据帧
	 * @return true为异常消息，false为正常消息
	 */
	public static boolean getIsErrorCode(byte[] message){
		byte bError = (byte) (getControlCode(message)& 0x40);
		if(bError == 0) return false;
		else return true;
	}
	
	/**
	 * 获取数据帧中的流水号
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
	 * 从message数组中截取一部分字节
	 * @param message byte[] 消息字节组
	 * @param begin int 开始位置
	 * @param length int 截取长度
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
	 * 从message数组中截取一部分字节
	 * @param message byte[] 消息字节组
	 * @param begin byte 开始字符
	 * @return
	 */
	public synchronized static byte[] subMessage(byte[] message, byte bByte) {
		int size = message.length;
		int begin = -1;
		// 查询beginB第一个位置
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
