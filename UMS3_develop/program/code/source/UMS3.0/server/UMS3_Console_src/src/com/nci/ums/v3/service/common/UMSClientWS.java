package com.nci.ums.v3.service.common;

import com.nci.ums.periphery.application.ReceiveCenter;
import com.nci.ums.util.Res;

public abstract class UMSClientWS {

	/**
	 * UMS发送给具体服务接收消息的结果标记，成功。
	 */
	public final static String SEND_SUCCEED = "0";
	/**
	 * UMS发送给具体服务接收消息的结果标记，失败。
	 */
	public final static String SEND_FAILED = "1";

	public WebServiceRequest wsRequest;

	public UMSClientWS() {
		wsRequest = initWebServiceRequest();
	}

	protected abstract WebServiceRequest initWebServiceRequest();

	/**
	 * 发送回执信息给应用
	 * @param s
	 * @return
	 * @throws Exception
	 */
	protected abstract String sendAckMsgToService(String s) throws Exception;

	/**
	 * 抽象方法，所有继承AbstractUMSClientWS的对象都必须实现这个方法。
	 * 该方法只负责将消息发送给服务所开放的webservice，并返回发送结果标记。
	 * 
	 * @param serviceID
	 *            消息接收的具体服务ID
	 * @param xml
	 *            标准UMS格式的xml消息
	 * @return 发送成功或失败的标记，“0”表示成功，“1”表示失败
	 */
	protected abstract String sendInMsgToService(String serviceID, String xml)
			throws Exception;

	/**
	 * 发送xml消息至服务，针对发送结果的成功和失败进行不同的处理
	 * 
	 * @param serviceID
	 *            消息接收的具体服务ID
	 * @param xml
	 *            标准UMS格式的xml消息
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
