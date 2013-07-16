/**
 * SyncNotifySPSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.unicom.vac.bossagent.soap;

import java.lang.reflect.Method;

public class SyncNotifySPSoapBindingImpl implements com.unicom.vac.bossagent.soap.SyncNotifySPService{
    public com.unicom.vac.bossagent.soap.sync.rsp.OrderRelationUpdateNotifyResponse orderRelationUpdateNotify(com.unicom.vac.bossagent.soap.sync.req.OrderRelationUpdateNotifyRequest orderRelationUpdateNotifyRequest) throws java.rmi.RemoteException {
    	Method[] methods = orderRelationUpdateNotifyRequest.getClass()
				.getMethods();
		for (Method m : methods) {
			if (m.getName().startsWith("get")) {
				try {
					Object value = m.invoke(orderRelationUpdateNotifyRequest,
							new Object[] {});
					System.out.println(m.getName() + " -->    " + value);
				} catch (Exception e) {
					System.err.println(m.getName() + "-----" + e.getMessage());
				}
			}
		}
		com.unicom.vac.bossagent.soap.sync.rsp.OrderRelationUpdateNotifyResponse resp = new com.unicom.vac.bossagent.soap.sync.rsp.OrderRelationUpdateNotifyResponse();
		resp.setRecordSequenceId(orderRelationUpdateNotifyRequest
				.getRecordSequenceId());
		resp.setResultCode(0);
		return resp;
    }

}
