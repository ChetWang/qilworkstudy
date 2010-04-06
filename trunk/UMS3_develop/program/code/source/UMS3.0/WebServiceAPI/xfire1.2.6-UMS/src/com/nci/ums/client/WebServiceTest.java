package com.nci.ums.client;

import java.net.MalformedURLException;
import java.net.URL;

import org.codehaus.xfire.client.Client;

public class WebServiceTest {

	private static WebServiceTest ums = null;
	
	private String endpoint = "";

	Client client = null;

	private WebServiceTest() {
		initWebService();
	}

	protected boolean initWebService() {
		try {
			client = new Client(new URL(endpoint));
			return true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public synchronized static WebServiceTest getInstance() {
		if (ums == null) {
			ums = new WebServiceTest();
		}
		return ums;
	}

	protected synchronized String send(String appid, String apppsw, String xml) {
		try {
			Object[] param = new Object[] {
					appid, apppsw, xml };
			
			Object[] results = client.invoke("sendWithAck", param);
			return (String) results[0];
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "REMOTE_ERR";
	}

}
