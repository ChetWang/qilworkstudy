package com.nci.ums.transmit.client;

import com.nci.ums.transmit.common.TransmitData;

/**
 * 多帧情况下的数据类
 * 
 * @author Qil.Wong
 * 
 */
public class MultipleFrameData {

	/**
	 * 帧内序号
	 */
	private int iseq;

	/**
	 * 完整数据
	 */
	private TransmitData data;

	/**
	 * 
	 * @param iseq
	 *            帧内序号
	 * @param data
	 *            完整数据
	 */
	public MultipleFrameData(int iseq, TransmitData data) {
		this.iseq = iseq;
		this.data = data;
	}

	/**
	 * 获取帧内序号
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
	 * 获取帧数据
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
