package nci.gps.message;

import nci.gps.util.ByteCoding;

/**
 * @功能：消息打包类
 * @时间：2008-07-21
 * @author ZHOUHM
 *
 */
public class PackageMessage {
	
	// 终端逻辑地址
	private byte[] logicalAddress;	
	// 主站地址与命令序号
	private byte[] msta_seq;
	// 控制码
	private byte controlCode;
	// 数据长度
	private byte[] dataLength;
	// 数据域
	private byte[] data;
	// 检校码
	private byte cs;
	
	/**
	 * 构造函数
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
	 * 消息打包函数
	 * @return 打好包的消息
	 */
	public byte[] packageMessage() {
		int length = data.length;
		byte[] result = new byte[13 + length];
		
		// 帧起始符
		result[0] = 0x68;
		// 终端逻辑地址
		for (int i = 0; i < 4; i++) {
			result[1 + i] = logicalAddress[i];
		}
		// 主站地址与命令序号
		for (int i = 0; i < 2; i++) {
			result[5 + i] = msta_seq[i];
		}
		// 帧起始符
		result[7] = 0x68;
		// 控制码
		result[8] = controlCode;
		// 数据长度
		for (int i = 0; i < 2; i++) {
			result[9 + i] = dataLength[i];
		}
		// 数据域
		for (int i = 0; i < length; i++) {
			result[11 + i] = data[i];
		}
		// 检校码
		setCS(result);
		result[11 + length] = cs;
		// 结束码
		result[12 + length] = 0x16;

		return result;
	}
	
	/**
	 * 设置终端逻辑地址
	 * @param logicalAddress
	 */
	public void setLogicalAddress(byte[] logicalAddress){
		this.logicalAddress = logicalAddress;
	}
	
	/**
	 * 设置主站地址与命令序号
	 * @param iseq 帧内序号，0表示单帧，1~6递增，7表示最后一帧
	 * @param fseq 帧序号，01H~07FH循环递增使用
	 * @return 
	 */
	public void setMstaSeq(int iseq, int fseq){
		byte[] mstaSeq = new byte[2];
		
		/**
		 * 主站地址
		 * 这里使用GPRS通信，D0~D5填入32
		 */
		ByteCoding obc = new ByteCoding(mstaSeq[0]);
		obc.setBitTrue(5);
		
		/**
		 * 帧内序号
		 */
		byte bIseq = (byte) iseq;
		bIseq <<= 5;
		
		/**
		 * 帧序号
		 */
		byte bFseq = (byte) fseq;
		mstaSeq[0] = (byte) ((bFseq << 6)|obc.getData());
		mstaSeq[1] = (byte) ((bFseq >> 2)|bIseq);
		
		this.msta_seq = mstaSeq;
	}
	
	/**
	 * 设置控制码
	 * 
	 * @param direction
	 *            boolean 传输方向，false表示主站发向终端，true表示终端发向主站
	 * @param errFlag
	 *            boolean 异常标志 一般为0
	 * @param functionCode
	 *            int 功能码(输入十进制整数)
	 *            21H登录、22H退出、24H心跳检查、0FH用户自定义数据
	 * @return byte 控制码
	 */
	public void setControlCode(boolean direction, boolean errFlag, int functionCode){
		byte bCode = 0x00;
		ByteCoding bc = new ByteCoding(bCode);
		
		/**
		 * 传输方向
		 * 1表示终端发向主站、0表示主站发向终端
		 */
		if(direction)
			bc.setBitTrue(7);	// 将D7位设置为1
		else 
			bc.setBitFalse(7);	// 将D7位设置为0
		
		/**
		 * 异常标志
		 * 0确认帧、1否认帧
		 */
		if(errFlag)
			bc.setBitTrue(6);
		else
			bc.setBitFalse(6);
		
		/**
		 * 功能码
		 * 0FH:15表示用户自定义数据
		 * 21H:33表示登录
		 * 22H:34表示退出
		 * 24H:36表示心跳检查
		 */
		byte bFunc = (byte) functionCode;
		bFunc = (byte) (bFunc & 0x3F);	// 将前两位置为0
		
		/**
		 * 合成控制码
		 */
		bCode = bc.getData();
		bCode = (byte) (bCode | bFunc);
		
		controlCode = bCode;
	}
	
	/**
	 * 设置数据域
	 * @param data
	 */
	public void setData(byte[] data){
		this.data = data;
		setDataLength(data.length);
	}
	
	/**
	 * 设置数据长度
	 * 数据长度为数据域长度
	 * @param length int 十进制表示的数据长度
	 * @return
	 */
	private void setDataLength(int length){
		byte[] bs = new byte[2];
		
		bs[0] = (byte) length;
		bs[1] = (byte) (length >> 8);
		
		dataLength = bs;
	}
	
	/**
	 * 设置检校码
	 * @param b 字节数组
	 * @return
	 */
	private void setCS(byte[] b){
		int size = b.length;
		int sum = 0;
		
		for(int i=0; i<size-2; i++){	// 计算到检校位之前
			sum += b[i];
		}
		
		cs = (byte)sum;
	}
}
