package com.nci.ums.client;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;

import com.nci.ums.client.ws.UMSSendStub;
import com.nci.ums.client.ws.UMSSendStub.SendWithAck;
import com.nci.ums.client.ws.UMSSendStub.SendWithAckResponse;

public class UMSWebService extends AbstractUMSSend {

	private static UMSWebService ums = null;

	private UMSSendStub stub = null;

	private UMSWebService() {
		super();
		if (inited)
			inited = initWebService();
	}

	protected boolean initWebService() {
		try {
			stub = new UMSSendStub(endpoint);
			return true;
		} catch (AxisFault e) {
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
		SendWithAck sendWithAck = new SendWithAck();
		sendWithAck.setAppID(appid);
		sendWithAck.setPassword(apppsw);
		sendWithAck.setBasicMsgsXML(xml);
		try {
			SendWithAckResponse resp = stub.sendWithAck(sendWithAck);
			return resp.get_return();
		} catch (RemoteException e) {
			e.printStackTrace();
			return REMOTE_ERROR;
		}
	}

}
