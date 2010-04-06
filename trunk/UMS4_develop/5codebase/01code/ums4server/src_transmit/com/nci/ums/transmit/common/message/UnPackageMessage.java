package com.nci.ums.transmit.common.message;

/**
 * <p>
 * 标题：StaticUnPackageMessage.java
 * </p>
 * <p>
 * 描述： 静态报文分析类
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2009-8-24
 * @version 1.0
 */
public class UnPackageMessage {
	/**
	 * @功能 检查报文长度是否合法
	 * @param message
	 *            byte[] 报文字节组
	 * @return 报文长度如果合法返回true，如果不合法返回false
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
	 * @功能 检查检校码是否正确
	 * @param message
	 *            byte[] 报文字节组
	 * @return 检校码如果正确返回true，如果错误返回false
	 * 
	 * @Add by ZHM 2009-8-24
	 */
	public static boolean isRightCs(byte[] message) {
		int size = message.length;
		int sum = 0;

		for (int i = 0; i < size - 2; i++) { // 计算到检校位之前
			sum += message[i];
		}

		byte cs = getCs(message);
		if ((byte) sum == cs)
			return true;
		return false;
	}

	/**
	 * @功能 获取检校码
	 * @param message
	 *            byte[] 报文字节组
	 * @return 检校码
	 * 
	 * @Add by ZHM 2009-8-24
	 */
	private static byte getCs(byte[] message) {
		int length = getDataLength(message);
		byte[] cs = subMessage(message, length + GlobalConstant.INDEX_DATA, 1);
		return cs[0];
	}

	/**
	 * @功能 获取应用逻辑地址
	 * @param message
	 *            byte[] 报文字节组
	 * @return 应用逻辑地址
	 * 
	 * @Add by ZHM 2009-8-24
	 */
	public static byte[] getManufacturerAddressBytes(byte[] message) {
		byte[] manufacturerAddress = subMessage(message,
				GlobalConstant.INDEX_MANUFACTURER, 2);
		return manufacturerAddress;
	}

	/**
	 * @功能 获取应用逻辑地址
	 * @param message
	 *            byte[] 报文字节组
	 * @return 应用逻辑地址
	 * 
	 * @Add by ZHM 2009-8-26
	 */
	public static int getManufactureAddress(byte[] message) {
		byte[] manufacturerAddress = subMessage(message,
				GlobalConstant.INDEX_MANUFACTURER, 2);
		return twoBytesToInt(manufacturerAddress);
	}

	/**
	 * @功能 获取终端逻辑地址
	 * @param message
	 *            byte[] 报文字节组
	 * @return byte[] 终端逻辑字节组
	 * 
	 * @Add by ZHM 2009-8-24
	 */
	public static byte[] getConsumerAddressBytes(byte[] message) {
		byte[] consumerAddress = subMessage(message,
				GlobalConstant.INDEX_CONSUMER, 2);
		return consumerAddress;
	}

	/**
	 * @功能 获取终端逻辑地址
	 * @param message
	 *            byte[] 报文字节组
	 * @return 终端逻辑字节组
	 * 
	 * @Add by ZHM 2009-8-26
	 */
	public static int getConsumerAddress(byte[] message) {
		byte[] consumerAddress = subMessage(message,
				GlobalConstant.INDEX_CONSUMER, 2);
		return twoBytesToInt(consumerAddress);
	}

	/**
	 * @功能 将byte[2]数组转换成int
	 * @param bytes
	 *            byte[2] 字节数组
	 * @return 整型
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
	 * @功能 获取数据域长度
	 * @param message
	 *            byte[] 报文字节组
	 * @return int 数据域长度
	 * 
	 * @Add by ZHM 2009-8-24
	 */
	public static int getDataLength(byte[] message) {
		byte[] dataLength = subMessage(message,
				GlobalConstant.INDEX_DATA_LENGTH, 2);
		int height = (dataLength[1] + 256) % 256; // 消除负数影响
		int low = (dataLength[0] + 256) % 256; // 消除负数影响
		return height * 256 + low;
	}

	/**
	 * @功能 获取数据域
	 * @param message
	 *            byte[] 报文字节组
	 * @return byte[] 数据域字节组
	 * 
	 * @Add by ZHM 2009-8-24
	 */
	public static byte[] getData(byte[] message) {
		int length = getDataLength(message);
		byte[] data = subMessage(message, GlobalConstant.INDEX_DATA, length);
		return data;
	}

	/**
	 * @功能 获取控制码
	 * @param message
	 *            byte[] 报文字节组
	 * @return byte 控制码
	 * 
	 * @Add by ZHM 2009-8-24
	 */
	public static byte getControlCode(byte[] message) {
		byte[] bs = subMessage(message, GlobalConstant.INDEX_CONTROL, 1);
		return bs[0];
	}

	/**
	 * @功能 检查报文是否发送给消费者
	 * @param message
	 *            byte[] 报文字节组
	 * @return boolean 主站→终端:true，终端→主站:false
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
	 * @功能 获取控制码的功能码
	 * @param message
	 *            byte[] 报文字节组
	 * @return byte 控制码中的功能码
	 * 
	 * @Add by ZHM 2009-8-24
	 */
	public static byte getFunctionCode(byte[] message) {
		return (byte) (getControlCode(message) & 0x3F);
	}

	/**
	 * @功能 检查报文是否为异常报文
	 * @param message
	 *            byte[] 报文字节组
	 * @return boolean 异常(true)/正常(false)
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
	 * @功能 获取数据帧中的命令序号
	 * @param message byte[] 报文字节组
	 * @return int 数据帧中的命令序号
	 *
	 * @Add by ZHM 2009-8-28
	 */
	public static int getMSTA(byte[] message){
		byte[] msta = subMessage(message, GlobalConstant.INDEX_MSTA, 1);
		return twoBytesToInt(msta);
	}

	/**
	 * @功能 获取数据帧中的流水号
	 * @param message byte[] 报文字节组
	 * @return int 数据帧中的流水号
	 *
	 * @Add by ZHM 2009-8-27
	 */
	public static int getFSEQ(byte[] message){
		byte[] fseq = subMessage(message, GlobalConstant.INDEX_FSEQ,1);
		return twoBytesToInt(fseq);
	}
	
	/**
	 * @功能 获取数据帧中的帧总数
	 * @param message byte[] 报文字节组
	 * @return 数据帧中的帧总数
	 *
	 * @Add by ZHM 2009-9-6
	 */
	public static int getFNUM(byte[] message){
		byte[] fnum = subMessage(message, GlobalConstant.INDEX_FNUM, 1);
		return twoBytesToInt(fnum);
	}
	
	/**
	 * @功能 获取报文的帧内序号
	 * @param message byte[] 报文字节组
	 * @return 帧内序号
	 *
	 * @Add by ZHM 2009-8-27
	 */
	public static int getISEQ(byte[] message){
		byte[] iseq = subMessage(message, GlobalConstant.INDEX_ISEQ, 1);
		return twoBytesToInt(iseq);
	}

	// /**
	// * @功能 获取数据帧中的流水号
	// * @param message
	// * byte[] 报文字节组
	// * @return int 数据帧中的流水号
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
	 * @功能 获取主站地址与命令序号
	 * @param message
	 * byte[] 报文字节组
	 * @return byte[2] 主站地址与命令序号
	 *
	 * @Add by ZHM 2009-8-25
	 */
	 public static byte[] getMstaSeq(byte[] message) {
	 return subMessage(message, GlobalConstant.INDEX_MSTA, 4);
	 }

	/**
	 * @功能 从报文中截取指定长度的字节
	 * @param message
	 *            byte[] 报文字节组
	 * @param begin
	 *            int 开始位置
	 * @param length
	 *            int 截取长度
	 * @return byte[] 截取的字节组
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
	// * @功能 从报文中截取指定位置后的字节
	// * @param message
	// * byte[] 报文字节组
	// * @param bByte
	// * byte 开始字符
	// * @return byte[] 截取的字节组
	// *
	// * @Add by ZHM 2009-8-24
	// */
	// private static byte[] subMessage(byte[] message, byte bByte) {
	// int size = message.length;
	// int begin = -1;
	// // 查询beginB第一个位置
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
