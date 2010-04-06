package com.nci.ums.common.service;

public class Test {

	public static void main(String[] args) {
		CommonWebServiceRequest wsreq = new CommonWebServiceRequest(
				"http://127.0.0.1:19829/axis2/services/UMS3_MsgSend");
		Object o = wsreq.invoke("http://impl.service.v3.ums.nci.com",
				"sendWithAck", new Object[] { "1001", "1001", "xx" });
		System.out.println(o);
	}
}
