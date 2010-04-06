package com.nci.ums.v3.service.client;

import java.util.EventObject;

public class ReceiveEvent extends EventObject{
	
	Object obj;
	
	public ReceiveEvent(Object source){
		super(source);
		this.obj = source;
	}
	
	public Object getReceivedObj(){
		return obj;
	}	

}
