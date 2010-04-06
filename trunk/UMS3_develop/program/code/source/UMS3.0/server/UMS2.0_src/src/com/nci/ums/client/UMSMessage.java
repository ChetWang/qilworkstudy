package com.nci.ums.client;

import java.io.Serializable;
import java.util.HashMap;

public class UMSMessage implements Serializable{
	private static final long serialVersionUID = -3075824939241149695L;
	protected HashMap msgPros;				//消息体属性Map表
	public UMSMessage() {
		msgPros 	= new HashMap();
	}
	
	public void put(String key,String value) {
		if( key != null && value != null ) {
			msgPros.put(key, value);
		}
	}
	
	public String get(String key) {
		if( key != null && msgPros.containsKey(key) ) {
			return (String) msgPros.get(key);
		}
		return "";
	}
	
}
