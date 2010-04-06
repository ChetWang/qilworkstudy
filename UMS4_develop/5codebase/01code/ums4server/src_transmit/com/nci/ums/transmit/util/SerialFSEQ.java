package com.nci.ums.transmit.util;

/**
 * <p>
 * 标题：SerialFSEQ.java
 * </p>
 * <p>
 * 描述： 帧序号产生类
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2009-8-25
 * @version 1.0
 */
public class SerialFSEQ {

	/**
	 * 帧序号产生类对象
	 */
	private static SerialFSEQ fseq = null;

	/**
	 * 流水号计数器
	 */
	private int serial = 0;

	/**
	 * 流水号最大数值
	 */
	private int max_serial = 128;

	/**
	 * @功能 获取帧序号产生类对象实例
	 * @return 帧序号产生类对象实例
	 *
	 * @Add by ZHM 2009-8-25
	 */
	public static SerialFSEQ getInstance() {
		if (fseq == null) {
			fseq = new SerialFSEQ();
		}
		return fseq;
	}

	/**
	 * @功能 获取流水号
	 * @return int 流水号
	 * 
	 * @Add by ZHM 2009-8-25
	 */
	public synchronized int getSerial() {
		serial++;
		if (serial >= max_serial) {
			serial = 1;
		}
		return serial;
	}
}
