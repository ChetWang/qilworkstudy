package com.nci.ums.transmit.client.sender;

import com.nci.ums.transmit.client.RequestHandler;
import com.nci.ums.transmit.client.TransmitClient;
import com.nci.ums.transmit.common.IllegalCommandException;
import com.nci.ums.transmit.common.IllegalFSEQException;
import com.nci.ums.transmit.common.OutOfMaxFrameSizeException;
import com.nci.ums.transmit.common.UMSTransmitException;
import com.nci.ums.transmit.common.message.ControlCode;
import com.nci.ums.transmit.common.message.PackageMessage;
import com.nci.ums.transmit.util.SerialFSEQ;

public abstract class TransmitSender {

	protected TransmitClient client;

	protected int toAddress;

	protected int manufacturerAddress;

	protected int consumerAddress;

	/**
	 * 数据方向是否反转，反转表示是回执、反馈类型的信息
	 */
	protected boolean reverseDirection;

	private int fseq = -1;

	/**
	 * 构造函数
	 * 
	 * @param client
	 *            转发客户端对象
	 * @param toAddress
	 *            转发地址
	 */
	public TransmitSender(TransmitClient client, int toAddress,
			boolean reverseDirection) {
		this.client = client;
		this.toAddress = toAddress;
		this.reverseDirection = reverseDirection;
		manufacturerAddress = client.getSubServerType() == ControlCode.DIRECTION_FROM_TREMINAL ? toAddress
				: client.getClientAddress();
		consumerAddress = client.getSubServerType() == ControlCode.DIRECTION_FROM_TREMINAL ? client
				.getClientAddress()
				: toAddress;
	}

	/**
	 * @功能 生成数据报文
	 * @param manufacturerAddress
	 *            int 应用逻辑地址(0～65536)
	 * @param consumerAddress
	 *            int 终端逻辑地址(0～65536)
	 * @param direction
	 *            int 登录方，0:主站，1:终端
	 * @param data
	 *            byte[] 数据域数据
	 * @param fseq
	 *            int 流水号 (1~255)
	 * @param funm
	 *            int 帧总数
	 * @param iseq
	 *            int 帧内序号(1~255)
	 * @return
	 * 
	 * @Add by ZHM 2009-8-28
	 */
	protected byte[] getDataMessage(int manufacturerAddress,
			int consumerAddress, int direction, byte[] data, int fseq,
			int fnum, int iseq) {
		PackageMessage pm = new PackageMessage();

		pm.setManufacturerAddress(manufacturerAddress);// 设置应用地址
		pm.setConsumerAddress(consumerAddress);// 设置终端地址
		pm.setMstaSeq(35, fseq, fnum, iseq); // 设置命令序号和流水号
		// 设置控制码
		if (direction > 0) {
			pm.setControlCode(true, false, 0x0F);
		} else {
			pm.setControlCode(false, false, 0x0F);
		}
		pm.setData(data);

		return pm.packageMessage();
	}

	/**
	 * 命令发送
	 * 
	 * @param userCommand
	 *            数据域用户命令
	 * @param request
	 *            命令发送后的回调对象
	 * 
	 * @throws UMSTransmitException
	 * @throws InterruptedException
	 * @throws IllegalCommandException
	 * @throws OutOfMaxFrameSizeException
	 */
	public abstract void send(int userCommand, RequestHandler request)
			throws UMSTransmitException, InterruptedException,
			IllegalCommandException, OutOfMaxFrameSizeException,
			IllegalFSEQException;

	/**
	 * 接收命令后的反馈
	 * 
	 * @param fseq
	 *            接收命令的命令序号，不是数据域用户命令
	 * @throws OutOfMaxFrameSizeException
	 * @throws IllegalCommandException
	 * @throws InterruptedException
	 * @throws UMSTransmitException
	 */
	public void feedBack(int fseq) throws OutOfMaxFrameSizeException,
			IllegalCommandException, UMSTransmitException, InterruptedException {
		// 基本的sender对feedback不做任何实现
	}

	public int getManufacturerAddress() {
		return manufacturerAddress;
	}

	public int getConsumerAddress() {
		return consumerAddress;
	}

	/**
	 * 计算数据方向，如果是回执、反馈的数据，则与subservertype相反
	 * 
	 * @return
	 */
	protected int getDirection() {
		int direction = client.getSubServerType();
		if (reverseDirection) {
			if (client.getSubServerType() == ControlCode.DIRECTION_FROM_APPLICATION) {
				direction = ControlCode.DIRECTION_FROM_TREMINAL;
			} else {
				direction = ControlCode.DIRECTION_FROM_APPLICATION;
			}
		}
		return direction;
	}

	/**
	 * 设置fseq值
	 * 
	 * @param fseq
	 */
	public void setFseq(int fseq) {
		this.fseq = fseq;
	}

	protected int getFseq() throws IllegalFSEQException {
		if (!reverseDirection) {
			fseq = SerialFSEQ.getInstance().getSerial();
		}
		if (reverseDirection && fseq == -1) {
			throw new IllegalFSEQException();
		}
		return fseq;
	}

}
