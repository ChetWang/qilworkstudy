package com.nci.ums.v3.service.client;


import java.util.EventListener;

public interface ReceiveListener extends EventListener {

	public void onUMSMessage(ReceiveEvent evt);
}
