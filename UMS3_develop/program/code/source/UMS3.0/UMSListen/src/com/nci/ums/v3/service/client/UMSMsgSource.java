package com.nci.ums.v3.service.client;

import java.util.Enumeration;
import java.util.Vector;

public class UMSMsgSource {
	private Vector repository = new Vector();
	ReceiveListener listener;

	public UMSMsgSource() {

	}

	public void addReceiveListener(ReceiveListener listener) {
		repository.addElement(listener);
	}

	public void notifyReceiveEvent(Object obj) {
		Enumeration enumer = repository.elements();
		while (enumer.hasMoreElements()) {
			listener = (ReceiveListener) enumer.nextElement();
			listener.onUMSMessage(new ReceiveEvent(obj));
		}
	}

}
