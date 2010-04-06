package com.nci.ums.v3.service.common;

import com.nci.ums.periphery.application.ReceiveCenter;
import com.nci.ums.util.Res;

public abstract class UMSClientWS {

	/**
	 * UMS���͸�������������Ϣ�Ľ����ǣ��ɹ���
	 */
	public final static String SEND_SUCCEED = "0";
	/**
	 * UMS���͸�������������Ϣ�Ľ����ǣ�ʧ�ܡ�
	 */
	public final static String SEND_FAILED = "1";

	public WebServiceRequest wsRequest;

	public UMSClientWS() {
		wsRequest = initWebServiceRequest();
	}

	protected abstract WebServiceRequest initWebServiceRequest();

	/**
	 * ���ͻ�ִ��Ϣ��Ӧ��
	 * @param s
	 * @return
	 * @throws Exception
	 */
	protected abstract String sendAckMsgToService(String s) throws Exception;

	/**
	 * ���󷽷������м̳�AbstractUMSClientWS�Ķ��󶼱���ʵ�����������
	 * �÷���ֻ������Ϣ���͸����������ŵ�webservice�������ط��ͽ����ǡ�
	 * 
	 * @param serviceID
	 *            ��Ϣ���յľ������ID
	 * @param xml
	 *            ��׼UMS��ʽ��xml��Ϣ
	 * @return ���ͳɹ���ʧ�ܵı�ǣ���0����ʾ�ɹ�����1����ʾʧ��
	 */
	protected abstract String sendInMsgToService(String serviceID, String xml)
			throws Exception;

	/**
	 * ����xml��Ϣ��������Է��ͽ���ĳɹ���ʧ�ܽ��в�ͬ�Ĵ���
	 * 
	 * @param serviceID
	 *            ��Ϣ���յľ������ID
	 * @param xml
	 *            ��׼UMS��ʽ��xml��Ϣ
	 */
	public void sendInMsg(String serviceID, String xml) {
		try {
			String result = this.sendInMsgToService(serviceID, xml);
			if (result.equalsIgnoreCase(UMSClientWS.SEND_FAILED)) {
				ReceiveCenter.getInstance().pullBackMsg(serviceID, xml);
			}
		} catch (Exception e) {
			ReceiveCenter.getInstance().pullBackMsg(serviceID, xml);
			Res.logExceptionTrace(e);
		}
	}

	public void sendAckMsg(String xml) throws Exception{
		sendAckMsgToService(xml);
	}

}
