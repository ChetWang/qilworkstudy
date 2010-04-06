package com.nci.ums.transmit.common.message;

/**
 * <p>
 * 标题：GlobalConstant.java
 * </p>
 * <p>
 * 描述： 全局静态变量类
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
/**
 * <p>
 * 标题：GlobalConstant.java
 * </p>
 * <p>
 * 描述： 
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
/**
 * <p>
 * 标题：GlobalConstant.java
 * </p>
 * <p>
 * 描述： 
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
public class GlobalConstant {
	/**
	 * 报文起始符
	 */
	public static final byte START_CHARACTER = 0x68;
	/**
	 * 报文终止符
	 */
	public static final byte STOP_CHARACTER = 0x16;
	
	/**
	 * 报文正确，无错误
	 */
	public static final byte ERR_RIGHT = 0x00;
	/**
	 * 报文内容错误
	 */
	public static final byte ERR_CONTENT = 0x02;
	/**
	 * 密码权限不足
	 */
	public static final byte ERR_PASSWORD = 0x03;
	/**
	 * 报文目的地址不存在或已断开连接
	 */
	public static final byte ERR_TARGET = 0x11;
	/**
	 * 报文发送失败
	 */
	public static final byte ERR_SENT = 0x12;
	/**
	 * 报文过长
	 */
	public static final byte ERR_DATA_LONGER = 0x13;
	
	/**
	 * 相同地址已经连接上转发平台
	 */
	public static final byte ERR_ADDRESS_ALREAY_LOGIN = 0x14;
	/**
	 * 应用号起始位置
	 */
	public static final int INDEX_MANUFACTURER = 1;
	/**
	 * 终端号起始位置
	 */
	public static final int INDEX_CONSUMER = 3;
	/**
	 * 命令序号位置
	 */
	public static final int INDEX_MSTA = 5;
	/**
	 * 帧流水号位置
	 */
	public static final int INDEX_FSEQ = 6;
	/**
	 * 帧总数
	 */
	public static final int INDEX_FNUM = 7;
	/**
	 * 帧内序号位置
	 */
	public static final int INDEX_ISEQ = 8;
	/**
	 * 第二个起始符位置
	 */
	public static final int INDEX_SECOND_START = 9;
	/**
	 * 控制码位置
	 */
	public static final int INDEX_CONTROL = 10;
	/**
	 * 数据域长度开始位置
	 */
	public static final int INDEX_DATA_LENGTH = 11;
	/**
	 * 数据域开始位置
	 */
	public static final int INDEX_DATA = 13;
	/**
	 * 报文长度（除掉数据域）
	 */
	public static final int MESSAGE_LENGTH = 15;
	
	/**
	 * 数据域最大长度
	 */
	public static final int DATA_MAX_LENGTH = 32*1024;
	/**
	 * 最大帧内序号
	 */
	public static final int ISEQ_MAX_NUMBER	= 255;
}
