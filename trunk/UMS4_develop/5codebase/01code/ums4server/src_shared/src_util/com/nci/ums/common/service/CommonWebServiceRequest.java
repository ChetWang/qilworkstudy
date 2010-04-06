// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   CommonWebServiceRequest.java

package com.nci.ums.common.service;

import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

import com.nci.ums.util.Res;



public class CommonWebServiceRequest implements WebServiceRequest {

	private String endPoint;
	private Call call;

	private CommonWebServiceRequest() {
		endPoint = "";
		call = null;
	}

	public CommonWebServiceRequest(String endPoint) {
		this.endPoint = "";
		call = null;
		this.endPoint = endPoint;
		initWebService();
	}

	protected boolean initWebService() {
		try {
			Service service = new Service();
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new URL(endPoint));
			call.setProperty("SOAPAction", Boolean.TRUE);
			return true;
		} catch (Exception e) {

			Res.logExceptionTrace(e);
		}
		return false;
	}

	public synchronized Object invoke(String namespaceURI, String methodName,
			Object params[]) {
		try {
			Object result;
			call.setOperationName(new QName(namespaceURI, methodName));
			result = call.invoke(params);			
			return result;
		} catch (RemoteException e) {
			Res.logExceptionTrace(e);
		}
		return null;
	}

	public Object invoke(Object params[]) {
		return null;
	}
}
