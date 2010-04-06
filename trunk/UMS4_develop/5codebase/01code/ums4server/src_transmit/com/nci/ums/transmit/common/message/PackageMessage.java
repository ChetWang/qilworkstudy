package com.nci.ums.transmit.common.message;

import com.nci.ums.transmit.common.IllegalAdressException;
import com.nci.ums.transmit.util.ByteCoding;

/**
 * <p>
 * 标题：PackageMessage.java
 * </p>
 * <p>
 * 描述： 报文打包类
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
public class PackageMessage {

	// /**
	// * 终端逻辑地址
	// */
	// private byte[] logicalAddress;
	/**
	 * 应用逻辑地址
	 */
	private byte[] manufacturerAddress;
	/**
	 * 终端逻辑地址
	 */
	private byte[] consumerAddress;
	/**
	 * 主站地址与命令序号
	 */
	private byte[] msta_seq;
	/**
	 * 控制码
	 */
	private byte controlCode;
	/**
	 * 数据长度
	 */
	private byte[] dataLength;
	/**
	 * 数据域
	 */
	private byte[] data;
	/**
	 * 检校码
	 */
	private byte cs;

	/**
	 * @功能 构造函数
	 * 
	 * @Add by ZHM 2009-8-24
	 */
	public PackageMessage() {
		// logicalAddress = new byte[4];
		manufacturerAddress = new byte[2];
		consumerAddress = new byte[2];
		msta_seq = new byte[4];
		controlCode = 0x00;
		dataLength = new byte[] { 0x00, 0x00 };
		data = new byte[0];
		cs = 0x00;
	}

	/**
	 * @功能 报文打包函数
	 * @return byte[] 打好包的报文
	 * 
	 * @Add by ZHM 2009-8-24
	 */
	public byte[] packageMessage() {
		int length = data.length;
		byte[] result = new byte[GlobalConstant.MESSAGE_LENGTH + length];

		// 帧起始符
		result[0] = GlobalConstant.START_CHARACTER;
		// // 终端逻辑地址
		// for (int i = 0; i < 4; i++) {
		// result[1 + i] = logicalAddress[i];
		// }
		// 应用逻辑地址
		for (int i = 0; i < 2; i++) {
			result[GlobalConstant.INDEX_MANUFACTURER + i] = manufacturerAddress[i];
		}
		// 终端逻辑地址
		for (int i = 0; i < 2; i++) {
			result[GlobalConstant.INDEX_CONSUMER + i] = consumerAddress[i];
		}
		// 主站地址与命令序号
		for (int i = 0; i < 4; i++) {
			result[GlobalConstant.INDEX_MSTA + i] = msta_seq[i];
		}
		// 帧起始符
		result[GlobalConstant.INDEX_SECOND_START] = GlobalConstant.START_CHARACTER;
		// 控制码
		result[GlobalConstant.INDEX_CONTROL] = controlCode;
		// 数据长度
		for (int i = 0; i < 2; i++) {
			result[GlobalConstant.INDEX_DATA_LENGTH + i] = dataLength[i];
		}
		// 数据域
		for (int i = 0; i < length; i++) {
			result[GlobalConstant.INDEX_DATA + i] = data[i];
		}
		// 检校码
		setCS(result);
		result[GlobalConstant.INDEX_DATA + length] = cs;
		// 结束码
		result[GlobalConstant.INDEX_DATA + 1 + length] = GlobalConstant.STOP_CHARACTER;

		return result;
	}

	// /**
	// * @功能 设置终端逻辑地址
	// * @param logicalAddress byte[4] 终端逻辑地址字节组
	// *
	// * @Add by ZHM 2009-8-24
	// */
	// public void setLogicalAddress(byte[] logicalAddress){
	// this.logicalAddress = logicalAddress;
	// }

	/**
	 * @功能 设置应用逻辑地址
	 * @param manufacturerAddress
	 *            byte[2] 应用逻辑地址字节组
	 * @return 成功设置应用逻辑地址标志
	 * @Add by ZHM 2009-8-24
	 */
	public boolean setManufacturerAddress(byte[] manufacturerAddress) {
		if (manufacturerAddress != null && manufacturerAddress.length == 2) {
			for (int i = 0; i < 2; i++) {
				this.manufacturerAddress[i] = manufacturerAddress[i];
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @功能 设置应用逻辑地址
	 * @param manufacturerAddress
	 *            int 应用逻辑地址(0～65536)
	 * @return 设置成功标志
	 * 
	 * @Add by ZHM 2009-8-27
	 */
	public boolean setManufacturerAddress(int manufacturerAddress) {
		try {
			byte[] manufacturer = addressIntToByte(manufacturerAddress);
			return setManufacturerAddress(manufacturer);
		} catch (IllegalAdressException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @功能 设置终端逻辑地址
	 * @param consumerAddress
	 *            byte[2] 终端逻辑地址字节组
	 * @return 成功设置终端逻辑地址标志
	 * @Add by ZHM 2009-8-24
	 */
	public boolean setConsumerAddress(byte[] consumerAddress) {
		if (consumerAddress != null && consumerAddress.length == 2) {
			for (int i = 0; i < 2; i++) {
				this.consumerAddress[i] = consumerAddress[i];
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @功能 设置终端逻辑地址
	 * @param consumerAddress
	 *            int 终端逻辑地址(0～65536)
	 * @return 设置终端逻辑地址成功标志
	 * 
	 * @Add by ZHM 2009-8-27
	 */
	public boolean setConsumerAddress(int consumerAddress) {
		try {
			byte[] consumer = addressIntToByte(consumerAddress);
			return setConsumerAddress(consumer);
		} catch (IllegalAdressException e) {
//			Res.logExceptionTrace(e);
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @功能 设置主站地址与命令序号
	 * @param mstaSeq
	 *            byte[2] 主站地址与命令序号
	 * @return 成功设置主站地址与命令序号标志
	 * 
	 * @Add by ZHM 2009-8-25
	 */
	public boolean setMstaSeq(byte[] mstaSeq) {
		if (mstaSeq != null && mstaSeq.length == 3) {
			for (int i = 0; i < 4; i++) {
				this.msta_seq[i] = mstaSeq[i];
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @功能 设置主站地址与命令序号
	 * @param order
	 *            int 命令序号 短信:31/GPRS:32/Ethernet:35
	 * @param fseq
	 *            int 帧序号，01H~0FFH(255)循环递增使用
	 * @param fnum
	 *            int 帧总数，01H~0FFH(255)
	 * @param iseq
	 *            int 帧内序号，0表示单帧，1~255递增，255表示最后一帧
	 * @return 成功设置主站地址与命令序号标志
	 * 
	 * @Add by ZHM 2009-8-27
	 */
	public boolean setMstaSeq(int order, int fseq, int fnum, int iseq) {
		if ((order < 0 || order > 255) || (iseq < 0 || iseq > 255)
				|| (fnum < 0 || fnum > 255) || (fseq < 0 || fseq > 255))
			return false;
		byte[] mstaSeq = new byte[4];
		mstaSeq[0] = (byte) order;
		mstaSeq[1] = (byte) fseq;
		mstaSeq[2] = (byte) fnum;
		mstaSeq[3] = (byte) iseq;
		this.msta_seq = mstaSeq;
		return true;
	}

	// /**
	// * @功能 设置主站地址与命令序号
	// * @param iseq
	// * int 帧内序号，0表示单帧，1~6递增，7表示最后一帧
	// * @param fseq
	// * int 帧序号，01H~07FH(127)循环递增使用
	// * @return 成功设置主站地址与命令序号标志
	// * @Add by ZHM 2009-8-24
	// */
	// public boolean setMstaSeq(int iseq, int fseq) {
	// if (iseq > 7 || iseq < 0) {
	// // 帧内序号不合法
	// return false;
	// }
	// if (fseq > 127 || fseq < 1) {
	// // 帧序号不合法
	// return false;
	// }
	//
	// byte[] mstaSeq = new byte[2];
	// /**
	// * 主站地址 这里使用Ethernet通信，D0~D5填入35
	// */
	// ByteCoding obc = new ByteCoding(mstaSeq[0]);
	// obc.setBitTrue(0);
	// obc.setBitTrue(1);
	// obc.setBitTrue(5);
	//
	// /**
	// * 帧内序号
	// */
	// byte bIseq = (byte) iseq;
	// bIseq <<= 5;
	//
	// /**
	// * 帧序号
	// */
	// byte bFseq = (byte) fseq;
	// mstaSeq[0] = (byte) ((bFseq << 6) | obc.getData());
	// mstaSeq[1] = (byte) ((bFseq >> 2) | bIseq);
	//
	// this.msta_seq = mstaSeq;
	// return true;
	// }

	/**
	 * @功能 设置控制码
	 * @param controlCode
	 *            byte 控制码
	 * @return
	 * 
	 * @Add by ZHM 2009-8-25
	 */
	public boolean setControlCode(byte controlCode) {
		this.controlCode = controlCode;
		return true;
	}

	/**
	 * @功能 设置控制码
	 * @param direction
	 *            boolean 传输方向，false:主站→终端，true:终端→主站
	 * @param errFlag
	 *            boolean 异常标志，false:正常，true:异常
	 * @param functionCode
	 *            int 功能码(输入十进制整数) 21H登录、22H退出、24H心跳检查、0FH用户自定义数据
	 * @return 成功设置控制码标志
	 * 
	 * @Add by ZHM 2009-8-24
	 */
	public boolean setControlCode(boolean direction, boolean errFlag,
			int functionCode) {
		if (functionCode > 63 || functionCode < 0) {
			// 功能码非法
			return false;
		}
		byte bCode = 0x00;
		ByteCoding bc = new ByteCoding(bCode);

		/**
		 * 传输方向 1表示终端发向主站、0表示主站发向终端
		 */
		if (direction)
			bc.setBitTrue(7); // 将D7位设置为1
		else
			bc.setBitFalse(7); // 将D7位设置为0

		/**
		 * 异常标志 0确认帧、1否认帧
		 */
		if (errFlag)
			bc.setBitTrue(6);
		else
			bc.setBitFalse(6);

		/**
		 * 功能码 0FH:15表示用户自定义数据 21H:33表示登录 22H:34表示退出 24H:36表示心跳检查
		 */
		byte bFunc = (byte) functionCode;
		bFunc = (byte) (bFunc & 0x3F); // 将前两位置为0

		/**
		 * 合成控制码
		 */
		bCode = bc.getData();
		bCode = (byte) (bCode | bFunc);

		controlCode = bCode;
		return true;
	}

	/**
	 * @功能 设置数据域
	 * @param data
	 *            byte[] 数据域字节组
	 * @return 成功设置数据域标志
	 * 
	 * @Add by ZHM 2009-8-24
	 */
	public boolean setData(byte[] data) {
		if (data != null) {
			this.data = data;
			setDataLength(data.length);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @功能 设置数据长度（数据长度为数据域长度）
	 * @param length
	 *            int 十进制表示的数据长度
	 * 
	 * @Add by ZHM 2009-8-24
	 */
	private void setDataLength(int length) {
		byte[] bs = new byte[2];

		bs[0] = (byte) length;
		bs[1] = (byte) (length >> 8);

		dataLength = bs;
	}

	/**
	 * @功能 设置检校码
	 * @param b
	 *            byte[] 报文字节组
	 * 
	 * @Add by ZHM 2009-8-24
	 */
	private void setCS(byte[] b) {
		int size = b.length;
		int sum = 0;

		for (int i = 0; i < size - 2; i++) { // 计算到检校位之前
			sum += b[i];
		}

		cs = (byte) sum;
	}

	/**
	 * @功能 将0～65536间的数据转换成byte[2]数组
	 * @param address
	 *            int 要转换的参数
	 * @return byte[2]
	 * @throws Exception
	 *             数据不在范围内
	 * 
	 * @Add by ZHM 2009-8-26
	 */
	private byte[] addressIntToByte(int address) throws IllegalAdressException {
		if (address < 0 || address >= 65536) {
			throw new IllegalAdressException(address);
		}
		byte[] bt = new byte[2];
		bt[0] = (byte) (0xFF & address);
		bt[1] = (byte) ((0xFF00 & address) >> 8);
		return bt;
	}

}
