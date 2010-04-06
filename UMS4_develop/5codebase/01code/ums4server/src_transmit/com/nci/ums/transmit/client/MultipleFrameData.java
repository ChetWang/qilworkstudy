package com.nci.ums.transmit.client;

import com.nci.ums.transmit.common.TransmitData;

/**
 * ��֡����µ�������
 * 
 * @author Qil.Wong
 * 
 */
public class MultipleFrameData {

	/**
	 * ֡�����
	 */
	private int iseq;

	/**
	 * ��������
	 */
	private TransmitData data;

	/**
	 * 
	 * @param iseq
	 *            ֡�����
	 * @param data
	 *            ��������
	 */
	public MultipleFrameData(int iseq, TransmitData data) {
		this.iseq = iseq;
		this.data = data;
	}

	/**
	 * ��ȡ֡�����
	 * 
	 * @return
	 */
	public int getIseq() {
		return iseq;
	}

	public void setIseq(int iseq) {
		this.iseq = iseq;
	}

	/**
	 * ��ȡ֡����
	 * 
	 * @return
	 */
	public TransmitData getData() {
		return data;
	}

	public void setData(TransmitData data) {
		this.data = data;
	}

}
