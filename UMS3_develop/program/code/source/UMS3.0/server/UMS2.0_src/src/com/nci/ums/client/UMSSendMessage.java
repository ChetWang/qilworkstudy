package com.nci.ums.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class UMSSendMessage extends UMSMessage implements Serializable{
	private ArrayList contents;
	private Vector dests;
	public UMSSendMessage() {
		super();
		dests		= new Vector();
		contents 	= new ArrayList();
	}
	public UMSMessage setContent(String msg) {
		add(contents, new UMSContent().setText(msg));
		return this;
	}

	public UMSMessage addContent(UMSContent content) {
		add(contents, content);
		return this;
	}

	public Iterator getUMSContents() {
		return contents.iterator();
	}

	public UMSMessage addTo(String dest) {
		add(dests,dest);
		return this;
	}

	public UMSMessage addTo(String[] dest) {
		if( dest != null && dest.length > 0 ) {
			for(int i = 0; i < dest.length; i++) {
				add(dests,dest[i]);
			}
		}
		return this;
	}

	public void setReply(String reply) {
		put(MESSAGE_REPLY,reply);
	}

	public String getReply() {
		return get(MESSAGE_REPLY);
	}

	public void setAck(String ack) {
		put(MESSAGE_ACK,ack);
	}

	public String getAck() {
		return get(MESSAGE_ACK);
	}

	public void setID(String id) {
		put(MESSAGE_ID,id);
	}

	public String getID() {
		return get(MESSAGE_ID);
	}

	public void setPriority(String priority) {
		put(MESSAGE_PRIORITY,priority);
	}

	public String getPriority() {
		return get(MESSAGE_PRIORITY);
	}

	public void setType(String type) {
		put(MESSAGE_TYPE,type);
	}

	public String getType() {
		return get(MESSAGE_TYPE);
	}

	public void setSubApp(String subApp) {
		put(MESSAGE_SUBAPP,subApp);
	}

	public String getSubApp() {
		return get(MESSAGE_SUBAPP);
	}

	public void setTradeCode(String appCode) {
		put(MESSAGE_TRADECODE,appCode);
	}

	public String getTradeCode() {
		return get(MESSAGE_TRADECODE);
	}

	public void setAppID(String appID) {
		put(MESSAGE_APPID,appID);
	}

	public String getAppID() {
		return get(MESSAGE_APPID);
	}

	private void add(List list, Object obj) {
		if( obj != null) {
			list.add(obj);
		}
	}

	public Iterator getDests() {
		return dests.iterator();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(msgPros.toString());
		sb.append("/n");
		sb.append(contents.toArray());
		sb.append("/n");
		sb.append(dests.toString());
		return sb.toString();
	}
//	-------- 
	public static final String MESSAGE_REPLY 		= "com.nci.ums.message.reply";
	public static final String MESSAGE_ACK		= "com.nci.ums.message.ack";
	public static final String MESSAGE_ID			= "com.nci.ums.message.id";
	public static final String MESSAGE_PRIORITY	= "com.nci.ums.message.priority";
	public static final String MESSAGE_TYPE		= "com.nci.ums.message.type";
	public static final String MESSAGE_SUBAPP		= "com.nci.ums.message.subapp";
	public static final String MESSAGE_TRADECODE	= "com.nci.ums.message.tradecode";
	public static final String MESSAGE_APPID		= "com.nci.ums.message.appid";
}
