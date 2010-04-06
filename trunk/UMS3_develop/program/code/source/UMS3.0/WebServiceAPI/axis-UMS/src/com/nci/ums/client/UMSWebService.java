package com.nci.ums.client;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

import com.nci.ums.client.AbstractUMSSend;

public class UMSWebService extends AbstractUMSSend {

	private Call call = null;

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
		Service service = new Service();
		try {
			call = (Call) service.createCall();
			call.setOperationName(new QName(
					"http://impl.service.v3.ums.nci.com", "sendWithAck"));
			call.setTargetEndpointAddress(new java.net.URL(endpoint));
			return true;
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return false;
	}

	protected synchronized String send(String appid, String apppsw, String xml) {
		try {
			String result = (String) call.invoke(new Object[] { appid, apppsw,
					xml });
			return result;
		} catch (RemoteException e) {
			e.printStackTrace();
			return REMOTE_ERROR;
		}
	}

	public static void main(String[] xxx) {
		UMSWebService ums = UMSWebService.getInstance();
		long t1 = System.currentTimeMillis();
		int totalcounts = 300;
		for (int i = 0; i < totalcounts; i++)
			System.out.println(ums.sendMessage("13819155409", "",
					"contentc²âÊÔing,Axis1")
					+ i);
		System.out.println("·¢ËÍ" + totalcounts + "ºÄÊ±"
				+ (System.currentTimeMillis() - t1) + "ºÁÃë");
	}

}
