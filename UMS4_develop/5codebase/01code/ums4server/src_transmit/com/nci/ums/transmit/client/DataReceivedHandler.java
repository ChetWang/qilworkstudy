package com.nci.ums.transmit.client;

import com.nci.ums.transmit.common.TransmitData;

/**
 * 数据接收处理对象
 * 
 * @author Qil.Wong
 * 
 */
public interface  DataReceivedHandler {

	/**
	 * 收到数据的相应
	 * @param data
	 */
	public  void onReceived(TransmitData[] data);

	/**
	 * 多帧数据接收失败
	 * @param data
	 */
	public  void failedReceivedMultiple(TransmitData[] data);




}
