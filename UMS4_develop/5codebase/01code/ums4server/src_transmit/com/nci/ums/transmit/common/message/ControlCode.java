package com.nci.ums.transmit.common.message;

/**
 * <p>
 * 标题：ControlCode.java
 * </p>
 * <p>
 * 描述：控制码类
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
public class ControlCode {
	/**
	 * 报文发送方向
	 */
	private boolean isToTerminal;
	/**
	 * 报文是否是异常报文
	 */
	private boolean isErr;
	/**
	 * 功能码
	 */
	private byte functionCode;

	/**
	 * 功能码 中继
	 */
	public static final byte FUNCTION_RELAY = 0x00;
	/**
	 * 功能码 读当前数据
	 */
	public static final byte FUNCTION_CURRENT_DATA = 0x01;
	/**
	 * 功能码 读任务数据
	 */
	public static final byte FUNCTION_TASK_DATA = 0x02;
	/**
	 * 功能码 告警
	 */
	public static final byte FUNCTION_WARNING = 0x09;
	/**
	 * 功能码 告警确认
	 */
	public static final byte FUNCTION_WARNING_DONFIRM = 0x0A;
	/**
	 * 功能码 用户自定义数据
	 */
	public static final byte FUNCTION_USER_DEFINED = 0x0F;
	/**
	 * 功能码 登录
	 */
	public static final byte FUNCTION_LOGGING_IN = 0x21;
	/**
	 * 功能码 登录退出
	 */
	public static final byte FUNCTION_LOGGING_OUT = 0x22;
	/**
	 * 功能码 心跳检验
	 */
	public static final byte FUNCTION_HEARTBEAT = 0x24;
	
	/**
	 * 从终端发送过来
	 */
	public static final int DIRECTION_FROM_TREMINAL = 1;
	
	/**
	 * 从接入应用发送过来
	 */
	public static final int DIRECTION_FROM_APPLICATION = 0;

	/**
	 * @功能 构造函数
	 * @param message
	 *            byte[] 报文
	 * 
	 * @Add by ZHM 2009-8-25
	 */
	public ControlCode(byte[] message) {
		// this.isToTerminal = UnPackageMessage.isToTerminal(message);
		// this.isErr = UnPackageMessage.getIsErrorCode(message);
		// this.functionCode = UnPackageMessage.getFunctionCode(message);
		byte controlCode = UnPackageMessage.getControlCode(message);
		// 获取方向
		if ((controlCode & 0x80) == 0) {
			this.isToTerminal = true;
		} else {
			this.isToTerminal = false;
		}
		// 获取异常标志
		if ((controlCode & 0x40) == 0) {
			this.isErr = false;
		} else {
			this.isErr = true;
		}
		// 获取功能码
		this.functionCode = (byte) (controlCode & 0x3F);
	}

	/**
	 * @功能 获取报送发送方向
	 * @return 主站→终端:true，终端→主站:false
	 * 
	 * @Add by ZHM 2009-8-25
	 */
	public boolean isToTerminal() {
		return isToTerminal;
	}

	/**
	 * @功能 获取报文异常标志
	 * @return 异常报文:true，正常报文:false
	 * 
	 * @Add by ZHM 2009-8-25
	 */
	public boolean isErr() {
		return isErr;
	}

	/**
	 * @功能 获取功能码
	 * @return byte 功能码
	 * 
	 * @Add by ZHM 2009-8-25
	 */
	public byte getFunctionCode() {
		return functionCode;
	}

}
