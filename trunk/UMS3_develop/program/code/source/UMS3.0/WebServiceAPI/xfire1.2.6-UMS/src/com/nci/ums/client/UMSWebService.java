package com.nci.ums.client;

import java.net.MalformedURLException;
import java.net.URL;

import org.codehaus.xfire.client.Client;

public class UMSWebService extends AbstractUMSSend {

	private static UMSWebService ums = null;

	Client client = null;

	private UMSWebService() {
		super();
		if (inited)
			inited = initWebService();
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

	public synchronized static UMSWebService getInstance() {
		if (ums == null) {
			ums = new UMSWebService();
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
