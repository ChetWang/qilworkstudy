package com.nci.ums.transmit.client;

import com.nci.ums.transmit.common.TransmitData;

/**
 * ���ݽ��մ������
 * 
 * @author Qil.Wong
 * 
 */
public interface  DataReceivedHandler {

	/**
	 * �յ����ݵ���Ӧ
	 * @param data
	 */
	public  void onReceived(TransmitData[] data);

	/**
	 * ��֡���ݽ���ʧ��
	 * @param data
	 */
	public  void failedReceivedMultiple(TransmitData[] data);




}
