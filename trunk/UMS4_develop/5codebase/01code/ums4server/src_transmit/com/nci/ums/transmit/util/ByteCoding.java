package com.nci.ums.transmit.util;

/**
 * <p>
 * 标题：ByteCoding.java
 * </p>
 * <p>
 * 描述： 字节处理类
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2009-8-20
 * @version 1.0
 */
public class ByteCoding {
	private byte data;

	/**
	 * @功能 构造函数
	 * @param data
	 *            byte 要操作的字节对象
	 * 
	 * @Add by ZHM 2009-8-20
	 */
	public ByteCoding(byte data) {
		this.data = data;
	}

	/**
	 * @功能 获取字节值
	 * @return byte 字节值
	 * 
	 * @Add by ZHM 2009-8-20
	 */
	public byte getData() {
		return this.data;
	}

	/**
	 * @功能 将指定位设置成1
	 * @param index
	 *            int 位置标识(0~7)
	 * 
	 * @Add by ZHM 2009-8-20
	 */
	public void setBitTrue(int index) {
		if (index >= 0 && index < 8) {
			byte b = 1;
			b <<= index;
			data = (byte) (data | b);
		}
	}

	/**
	 * @功能 将指定位设置成0
	 * @param index
	 *            int 位置标识(0~7)
	 * 
	 * @Add by ZHM 2009-8-20
	 */
	public void setBitFalse(int index) {
		if (index >= 0 && index < 8) {
			// 获取指定位为0其它位为1的字节
			byte b = (byte) 0xFF;
			byte c = 1;
			c <<= index;
			b = (byte) (b ^ c);
			// 将指定位设置成0
			data = (byte) (data & b);
		}
	}

	/**
	 * @功能 获取字节中指定位的值
	 * @param index
	 *            int 位置标识(0~7)
	 * @return int 该位的值，-1表示输入的index值不对
	 * 
	 * @Add by ZHM 2009-8-21
	 */
	public int getBit(int index) {
		if (index >= 0 && index < 8) {
			byte b = 1;
			b <<= index;

			if ((b & data) != 0)
				return 1;
			else
				return 0;
		}
		return -1;
	}
}
