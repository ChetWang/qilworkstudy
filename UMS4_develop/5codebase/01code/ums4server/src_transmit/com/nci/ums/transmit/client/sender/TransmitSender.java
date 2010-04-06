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
	 * ���ݷ����Ƿ�ת����ת��ʾ�ǻ�ִ���������͵���Ϣ
	 */
	protected boolean reverseDirection;

	private int fseq = -1;

	/**
	 * ���캯��
	 * 
	 * @param client
	 *            ת���ͻ��˶���
	 * @param toAddress
	 *            ת����ַ
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
	 * @���� �������ݱ���
	 * @param manufacturerAddress
	 *            int Ӧ���߼���ַ(0��65536)
	 * @param consumerAddress
	 *            int �ն��߼���ַ(0��65536)
	 * @param direction
	 *            int ��¼����0:��վ��1:�ն�
	 * @param data
	 *            byte[] ����������
	 * @param fseq
	 *            int ��ˮ�� (1~255)
	 * @param funm
	 *            int ֡����
	 * @param iseq
	 *            int ֡�����(1~255)
	 * @return
	 * 
	 * @Add by ZHM 2009-8-28
	 */
	protected byte[] getDataMessage(int manufacturerAddress,
			int consumerAddress, int direction, byte[] data, int fseq,
			int fnum, int iseq) {
		PackageMessage pm = new PackageMessage();

		pm.setManufacturerAddress(manufacturerAddress);// ����Ӧ�õ�ַ
		pm.setConsumerAddress(consumerAddress);// �����ն˵�ַ
		pm.setMstaSeq(35, fseq, fnum, iseq); // ����������ź���ˮ��
		// ���ÿ�����
		if (direction > 0) {
			pm.setControlCode(true, false, 0x0F);
		} else {
			pm.setControlCode(false, false, 0x0F);
		}
		pm.setData(data);

		return pm.packageMessage();
	}

	/**
	 * �����
	 * 
	 * @param userCommand
	 *            �������û�����
	 * @param request
	 *            ����ͺ�Ļص�����
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
	 * ���������ķ���
	 * 
	 * @param fseq
	 *            ���������������ţ������������û�����
	 * @throws OutOfMaxFrameSizeException
	 * @throws IllegalCommandException
	 * @throws InterruptedException
	 * @throws UMSTransmitException
	 */
	public void feedBack(int fseq) throws OutOfMaxFrameSizeException,
			IllegalCommandException, UMSTransmitException, InterruptedException {
		// ������sender��feedback�����κ�ʵ��
	}

	public int getManufacturerAddress() {
		return manufacturerAddress;
	}

	public int getConsumerAddress() {
		return consumerAddress;
	}

	/**
	 * �������ݷ�������ǻ�ִ�����������ݣ�����subservertype�෴
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
	 * ����fseqֵ
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
