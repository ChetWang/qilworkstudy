package com.nci.ums.transmit.common.message;

/**
 * <p>
 * ���⣺ControlCode.java
 * </p>
 * <p>
 * ��������������
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2009-8-25
 * @version 1.0
 */
public class ControlCode {
	/**
	 * ���ķ��ͷ���
	 */
	private boolean isToTerminal;
	/**
	 * �����Ƿ����쳣����
	 */
	private boolean isErr;
	/**
	 * ������
	 */
	private byte functionCode;

	/**
	 * ������ �м�
	 */
	public static final byte FUNCTION_RELAY = 0x00;
	/**
	 * ������ ����ǰ����
	 */
	public static final byte FUNCTION_CURRENT_DATA = 0x01;
	/**
	 * ������ ����������
	 */
	public static final byte FUNCTION_TASK_DATA = 0x02;
	/**
	 * ������ �澯
	 */
	public static final byte FUNCTION_WARNING = 0x09;
	/**
	 * ������ �澯ȷ��
	 */
	public static final byte FUNCTION_WARNING_DONFIRM = 0x0A;
	/**
	 * ������ �û��Զ�������
	 */
	public static final byte FUNCTION_USER_DEFINED = 0x0F;
	/**
	 * ������ ��¼
	 */
	public static final byte FUNCTION_LOGGING_IN = 0x21;
	/**
	 * ������ ��¼�˳�
	 */
	public static final byte FUNCTION_LOGGING_OUT = 0x22;
	/**
	 * ������ ��������
	 */
	public static final byte FUNCTION_HEARTBEAT = 0x24;
	
	/**
	 * ���ն˷��͹���
	 */
	public static final int DIRECTION_FROM_TREMINAL = 1;
	
	/**
	 * �ӽ���Ӧ�÷��͹���
	 */
	public static final int DIRECTION_FROM_APPLICATION = 0;

	/**
	 * @���� ���캯��
	 * @param message
	 *            byte[] ����
	 * 
	 * @Add by ZHM 2009-8-25
	 */
	public ControlCode(byte[] message) {
		// this.isToTerminal = UnPackageMessage.isToTerminal(message);
		// this.isErr = UnPackageMessage.getIsErrorCode(message);
		// this.functionCode = UnPackageMessage.getFunctionCode(message);
		byte controlCode = UnPackageMessage.getControlCode(message);
		// ��ȡ����
		if ((controlCode & 0x80) == 0) {
			this.isToTerminal = true;
		} else {
			this.isToTerminal = false;
		}
		// ��ȡ�쳣��־
		if ((controlCode & 0x40) == 0) {
			this.isErr = false;
		} else {
			this.isErr = true;
		}
		// ��ȡ������
		this.functionCode = (byte) (controlCode & 0x3F);
	}

	/**
	 * @���� ��ȡ���ͷ��ͷ���
	 * @return ��վ���ն�:true���նˡ���վ:false
	 * 
	 * @Add by ZHM 2009-8-25
	 */
	public boolean isToTerminal() {
		return isToTerminal;
	}

	/**
	 * @���� ��ȡ�����쳣��־
	 * @return �쳣����:true����������:false
	 * 
	 * @Add by ZHM 2009-8-25
	 */
	public boolean isErr() {
		return isErr;
	}

	/**
	 * @���� ��ȡ������
	 * @return byte ������
	 * 
	 * @Add by ZHM 2009-8-25
	 */
	public byte getFunctionCode() {
		return functionCode;
	}

}
