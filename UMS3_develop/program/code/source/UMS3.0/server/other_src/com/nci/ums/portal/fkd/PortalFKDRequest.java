package com.nci.ums.portal.fkd;

import java.util.Properties;

import com.nci.ums.util.Res;
import com.nci.ums.v3.service.common.CommonWebServiceRequest;
import com.nci.ums.v3.service.common.UMSClientWS;
import com.nci.ums.v3.service.common.WebServiceRequest;

public class PortalFKDRequest extends UMSClientWS {

	private String endpoint, methodname, namespace;

	public PortalFKDRequest() {
	}

	protected WebServiceRequest initWebServiceRequest() {
		Properties p = new Properties();
		try {
			p.load(getClass().getResourceAsStream("portal.prop"));
		} catch (Exception e) {
			e.printStackTrace();
			Res.logExceptionTrace(e);
		}
		endpoint = p.getProperty("endpoint");
		methodname = p.getProperty("methodname");
		namespace = p.getProperty("namespace");
		WebServiceRequest request = new CommonWebServiceRequest(endpoint);
		return request;
	}

	protected String sendAckMsgToService(String xml) throws Exception {
		return (String) wsRequest.invoke(namespace, methodname,
				new Object[] { xml });
	}

	protected String sendInMsgToService(String serviceID, String xml)
			throws Exception {
		return "";
	}

}
