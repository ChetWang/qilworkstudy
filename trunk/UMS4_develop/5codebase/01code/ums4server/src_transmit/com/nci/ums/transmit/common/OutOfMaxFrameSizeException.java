package com.nci.ums.transmit.common;

import com.nci.ums.transmit.common.message.GlobalConstant;

/**
 * 超长数据异常，UMS对多帧也有长度限制
 * @author Qil.Wong
 *
 */
public class OutOfMaxFrameSizeException extends Exception {

	private static final long serialVersionUID = 8557251087430502168L;

	public OutOfMaxFrameSizeException(int currentBytes) {
		super(
				"User data too large, max frame-counts for one whole user data is "
						+ GlobalConstant.ISEQ_MAX_NUMBER
						+ ", max user data size for single frame is "
						+ GlobalConstant.DATA_MAX_LENGTH
						+ ", total whole user data is limit to "
						+ GlobalConstant.ISEQ_MAX_NUMBER
						* GlobalConstant.DATA_MAX_LENGTH
						+ " bytes, current bytes length is " + currentBytes);
		;
	}
}
