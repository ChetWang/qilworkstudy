/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nci.ums.util;

import com.nci.ums.channel.inchannel.email.EmailMsgPlus;
import com.nci.ums.v3.message.UMSMsg;
import com.nci.ums.v3.message.basic.BasicMsg;
import com.nci.ums.v3.message.basic.MsgAttachment;
import com.nci.ums.v3.message.basic.MsgContent;
import com.nci.ums.v3.message.basic.Participant;
import com.thoughtworks.xstream.XStream;

/**
 * 
 * @author Qil.Wong
 * @deprecated 线程安全没处理好，不建议使用。
 */
public class XStreamUtil {
	private static XStream xstream = null;
	private static XStreamUtil xu = null;
	private static byte[] lock = new byte[0];

	static {
		synchronized (lock) {
			if (xu == null) {
				xu = new XStreamUtil();
			}
		}
	}

	private XStreamUtil() {
		xstream = new XStream();
		xstream.setClassLoader(getClass().getClassLoader());
		xstream.alias("UMSMsg", UMSMsg.class);
		xstream.alias("BasicMsg", BasicMsg.class);
		xstream.alias("MsgAttachment", MsgAttachment.class);
		xstream.alias("MsgContent", MsgContent.class);
		xstream.alias("Participant", Participant.class);
		xstream.alias("EmailMsgPlus", EmailMsgPlus.class);
	}

	public static XStreamUtil getInstance() {
		return xu;
	}

	public XStream getXStream() {
		return xstream;
	}
}
