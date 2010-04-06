package com.nci.ums.client;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;

import com.nci.ums.client.ws.UMSSendStub;
import com.nci.ums.client.ws.UMSSendStub.SendWithAck;
import com.nci.ums.client.ws.UMSSendStub.SendWithAckResponse;

public class UMSWebService extends AbstractUMSSend {

	private UMSSendStub stub = null;

	private static UMSWebService ums = null;

	private UMSWebService() {
		super();
		if (inited)
			inited = initWebService();
	}

	public synchronized static UMSWebService getInstance() {
		if (ums == null) {
			ums = new UMSWebService();
		}
		return ums;
	}

	protected boolean initWebService() {
		try {
			stub = new UMSSendStub(endpoint);
		} catch (AxisFault e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
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
	
	public static void main(String[] xxx) {
		UMSWebService ums = UMSWebService.getInstance();

		long t1 = System.currentTimeMillis();
		int totalcounts = 1;
		for (int i = 0; i < totalcounts; i++)
			System.out.println(ums.sendMessage("13819155409", "","contentc²âÊÔing")
					+ i);
		System.out.println("·¢ËÍ" + totalcounts + "ºÄÊ±"
				+ (System.currentTimeMillis() - t1) + "ºÁÃë");
	}
}
