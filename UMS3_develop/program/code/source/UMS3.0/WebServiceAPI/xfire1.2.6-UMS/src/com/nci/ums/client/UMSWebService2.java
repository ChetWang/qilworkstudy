package com.nci.ums.client;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import com.nci.ums.v3.service.impl.ObjectFactory;
import com.nci.ums.v3.service.impl.UMS3_MsgSendClient;

public class UMSWebService2 extends AbstractUMSSend {

	private static UMSWebService2 ums = null;

	UMS3_MsgSendClient client = null;
	ObjectFactory factory = new ObjectFactory();

	private UMSWebService2() {
		super();
		if (inited)
			inited = initWebService();
	}

	protected boolean initWebService() {

		client = new UMS3_MsgSendClient();
		return true;

	}

	public synchronized static UMSWebService2 getInstance() {
		if (ums == null) {
			ums = new UMSWebService2();
		}
		return ums;
	}

	protected synchronized String send(String appid, String apppsw, String xml) {

		JAXBElement<String> appidEle = new JAXBElement<String>(new QName(
				"http://impl.service.v3.ums.nci.com", "appID"), String.class,
				appid);
		JAXBElement<String> appPswEle = new JAXBElement<String>(new QName(
				"http://impl.service.v3.ums.nci.com", "password"),
				String.class, apppsw);
		JAXBElement<String> xmlEle = new JAXBElement<String>(new QName(
				"http://impl.service.v3.ums.nci.com", "basicMsgsXML"),
				String.class, xml);
		JAXBElement<String> result = client.getUMS3_MsgSendSOAP11port_http()
				.sendWithAck(factory.createSendWithAckAppID(appid),
						factory.createSendWithAckAppID(apppsw),
						factory.createSendWithAckAppID(xml));
		return result.getValue();
		// return "REMOTE_ERR";
	}
}
