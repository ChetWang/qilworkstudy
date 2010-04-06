package com.nci.ums.transmit.common;

import com.nci.ums.transmit.common.message.ControlCode;
import com.nci.ums.transmit.common.message.GlobalConstant;
import com.nci.ums.transmit.common.message.UnPackageMessage;

/**
 * ת�����ݶ���
 * 
 * @author Qil.Wong
 * 
 */
public class TransmitData {

	/**
	 * ��֡������
	 */
	private byte[] oneFrameData;
	/**
	 * ��֡���ݶ�Ӧ��uid
	 */
	private String uid;
	/**
	 * ��֡���ݴ��ͳɹ����ı��
	 */
	private boolean success = true;

	/**
	 * ���ݽ���ʱ��
	 */
	private String receivedTime;

	/**
	 * ����ת��ʱ��
	 */
	private String transmitTime;

	/**
	 * ����Ŀ�ĵ�ַ
	 */
	private int destinationAddress;

	/**
	 * ����Դ��ַ
	 */
	private int sourceAddress;

	/**
	 * �쳣����
	 */
	private int errorType = 0;

	/**
	 * �����������ڵ���ת����������
	 */
	private int subServerType;

	/**
	 * ת���߳��˳����ź���
	 */
	public static final TransmitData QUIT_SIGNAL = new TransmitData();

	/**
	 * only for quit
	 */
	private TransmitData() {
		oneFrameData = new byte[0];
	}

	/**
	 * ת�����ݶ����캯��������ת�����ݱ���byte���飬��Ӧ��uid�������ͳɹ����ı�ǣ���ʼ��ʱĬ�ϳɹ���
	 * 
	 * @param uid
	 *            ��֡���ݶ�Ӧ��uid
	 * @param successFlag
	 *            ��֡���ݴ��ͳɹ����ı��
	 * @param oneFrameData
	 *            ��֡������
	 * @param subServerType
	 *            ���ݽ��մ����ӷ������ͣ�������ⲿӦ�û��ն��ӷ�����գ�����2��������ڲ�Ӧ���ӷ�����գ�����1��
	 */
	public TransmitData(String uid, boolean successFlag, byte[] oneFrameData,
			int subServerType) {
		this.uid = uid;
		this.success = successFlag;
		this.oneFrameData = oneFrameData;
		this.subServerType = subServerType;
		anlysisData();
	}

	/**
	 * ת�����ݶ����캯��������ת�����ݱ���byte���飬��Ӧ��uid�������ͳɹ����ı�ǣ���ʼ��ʱĬ�ϳɹ���
	 * 
	 * @param oneFrameData
	 *            ��֡������
	 * @param subServerType
	 *            ���ݽ��մ����ӷ������ͣ�������ⲿӦ�û��ն��ӷ�����գ�����2��������ڲ�Ӧ���ӷ�����գ�����1��
	 */
	public TransmitData(byte[] oneFrameData, int subServerType) {
		this.uid = TransmitUID.getUMSUID(TransmitUID.UMSUID_SERVER);
		this.oneFrameData = oneFrameData;
		this.subServerType = subServerType;
		anlysisData();
	}

	private void anlysisData() {

		int terminalAdress = UnPackageMessage.getConsumerAddress(oneFrameData);
		int appAddress = UnPackageMessage.getManufactureAddress(oneFrameData);

		// ����Ŀ�ĵ�ַ��Դ��ַ
		if (subServerType == ControlCode.DIRECTION_FROM_TREMINAL) {
			destinationAddress = appAddress;
			sourceAddress = terminalAdress;
		} else if (subServerType == ControlCode.DIRECTION_FROM_APPLICATION) {
			destinationAddress = terminalAdress;
			sourceAddress = appAddress;
		}
	}

	/**
	 * ��ȡ��֡������
	 * 
	 * @return
	 */
	public byte[] getOneFrameData() {
		return oneFrameData;
	}

	/**
	 * ���õ�֡������
	 * 
	 * @param oneFrameData
	 */
	public void setOneFrameData(byte[] oneFrameData) {
		this.oneFrameData = oneFrameData;
	}

	/**
	 * @���� ��ȡ��֡���û����ݣ�������ǰ2���ֽڵ��û�����
	 * @return
	 * 
	 * @Add by ZHM 2009-10-23
	 */
	public byte[] getOneFrameUserDataWithoutUserCommand() {
		int length = this.oneFrameData.length;
		byte[] temp = new byte[length - GlobalConstant.MESSAGE_LENGTH - 2]; // -2��ʾȥ���������С��û����
		System.arraycopy(this.oneFrameData, GlobalConstant.INDEX_DATA + 2, // +2��ʾȥ����������ǰ��λ���û����
				temp, 0, temp.length);
		return temp;
		// return UnPackageMessage.getData(oneFrameData);
	}

	/**
	 * ��ȡ�û���������2���ֽڵ��û�����
	 * @return
	 */
	public int getUserCommand() {
		int length = this.oneFrameData.length;
		byte[] temp = new byte[2]; // 2��ʾ�������С��û����2���ֽ�
		System.arraycopy(this.oneFrameData, GlobalConstant.INDEX_DATA, temp, 0,
				2);
		return UnPackageMessage.twoBytesToInt(temp);
	}

	/**
	 * ��ȡ��֡���ݶ�Ӧ��uid
	 * 
	 * @return
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * ���ø�֡���ݶ�Ӧ��uid
	 * 
	 * @param uid
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * �ж�ת���Ƿ�ɹ�
	 * 
	 * @return
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * ����ת���ɹ������
	 * 
	 * @param successFlag
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * ��ȡ���ݽ��յ���ʱ��
	 * 
	 * @return
	 */
	public String getReceivedTime() {
		return receivedTime;
	}

	/**
	 * �������ݽ��յ���ʱ��
	 * 
	 * @param receivedTime
	 */
	public void setReceivedTime(String receivedTime) {
		this.receivedTime = receivedTime;
	}

	/**
	 * ��ȡ����ת����ʱ��
	 * 
	 * @return
	 */
	public String getTransmitTime() {
		return transmitTime;
	}

	/**
	 * ��������ת����ʱ��
	 * 
	 * @param transmitTime
	 */
	public void setTransmitTime(String transmitTime) {
		this.transmitTime = transmitTime;
	}

	/**
	 * ��ȡĿ�ĵ�ַ
	 * 
	 * @return
	 */
	public int getDestinationAddress() {
		return destinationAddress;
	}

	/**
	 * ����Ŀ�ĵ�ַ
	 * 
	 * @param destinationAddress
	 */
	public void setDestinationAddress(int destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

	/**
	 * ��ȡԴ��ַ
	 * 
	 * @return
	 */
	public int getSourceAddress() {
		return sourceAddress;
	}

	/**
	 * ����Դ��ַ
	 * 
	 * @param sourceAddress
	 */
	public void setSourceAddress(int sourceAddress) {
		this.sourceAddress = sourceAddress;
	}

	/**
	 * ��ȡ��������������ת�����������
	 * 
	 * @return
	 */
	public int getSubServerType() {
		return subServerType;
	}

	/**
	 * ��ȡ�쳣����
	 * 
	 * @return
	 */
	public int getErrorType() {
		return errorType;
	}

	/**
	 * �����쳣����
	 * 
	 * @param errorType
	 */
	public void setErrorType(int errorType) {
		this.errorType = errorType;
	}

}
