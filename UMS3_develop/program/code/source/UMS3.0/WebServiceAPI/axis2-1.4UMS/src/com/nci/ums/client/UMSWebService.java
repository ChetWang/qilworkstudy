package com.nci.ums.client;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;

import com.nci.ums.client.ws.UMS3_MsgSendStub;
import com.nci.ums.client.ws.UMS3_MsgSendStub.SendWithAck;
import com.nci.ums.client.ws.UMS3_MsgSendStub.SendWithAckResponse;

public class UMSWebService extends AbstractUMSSend {

	private static UMSWebService ums = null;

	private UMS3_MsgSendStub stub = null;

	private UMSWebService() {
		super();
		if (inited)
			inited = initWebService();
	}

	protected boolean initWebService() {
		try {
			stub = new UMS3_MsgSendStub(endpoint);
		} catch (AxisFault e) {
			e.printStackTrace();
			return false;
		}
		return true;
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
