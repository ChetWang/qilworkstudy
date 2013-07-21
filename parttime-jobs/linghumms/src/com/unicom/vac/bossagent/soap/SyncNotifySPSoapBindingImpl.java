/**
 * SyncNotifySPSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.unicom.vac.bossagent.soap;

import org.slf4j.Logger;
import org.vlg.linghu.SpringUtils;
import org.vlg.linghu.vac.VACNotifyHandler;

public class SyncNotifySPSoapBindingImpl implements
		com.unicom.vac.bossagent.soap.SyncNotifySPService {
	
	public SyncNotifySPSoapBindingImpl(){
		
	}
	
	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(SyncNotifySPSoapBindingImpl.class);
	public com.unicom.vac.bossagent.soap.sync.rsp.OrderRelationUpdateNotifyResponse orderRelationUpdateNotify(
			com.unicom.vac.bossagent.soap.sync.req.OrderRelationUpdateNotifyRequest orderRelationUpdateNotifyRequest)
			throws java.rmi.RemoteException {
		logger.info("got notification request from VAC center");
		VACNotifyHandler notify = (VACNotifyHandler)SpringUtils.getBean("vacNotifyHandler");
		notify.handle(orderRelationUpdateNotifyRequest);

		com.unicom.vac.bossagent.soap.sync.rsp.OrderRelationUpdateNotifyResponse resp = new com.unicom.vac.bossagent.soap.sync.rsp.OrderRelationUpdateNotifyResponse();
		resp.setRecordSequenceId(orderRelationUpdateNotifyRequest
				.getRecordSequenceId());
		resp.setResultCode(0);
		logger.info("sent response to VAC center");
		return resp;
	}

}
