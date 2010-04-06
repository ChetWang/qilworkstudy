package com.nci.ums.client;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.nci.ums.v3.message.UMSMsg;
import com.nci.ums.v3.message.basic.BasicMsg;
import com.nci.ums.v3.message.basic.MsgAttachment;
import com.nci.ums.v3.message.basic.MsgContent;
import com.nci.ums.v3.message.basic.Participant;
import com.thoughtworks.xstream.XStream;

public abstract class AbstractUMSSend {

	protected SimpleDateFormat sdf = null;

	protected String appid = null;

	protected String apppsw = null;

	protected String endpoint = null;

	protected XStream xstream = null;

	protected String mediaID = null;

	protected boolean inited = false;

	public static final String REMOTE_ERROR = "REMOTE_ERR";

	public static final String INIT_FAILED = "INIT_FAILED";

	public static final String GROUP_MALFORM = "GRP_MALFORM";

	public AbstractUMSSend() {
		inited = init();
	}

	private boolean init() {
		sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			Document doc = factory.newDocumentBuilder().parse(
					getClass().getResourceAsStream("ums.xml"));
			Element umsEle = (Element) doc.getElementsByTagName("ums").item(0);
			appid = umsEle.getAttribute("appid");
			apppsw = umsEle.getAttribute("apppsw");
			endpoint = umsEle.getAttribute("endpoint");
			mediaID = umsEle.getAttribute("mediaID");
			initXStream();
		} catch (SAXException e) {
			System.out.println(e.getMessage());
			return false;
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return false;
		} catch (ParserConfigurationException e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}

	private void initXStream() {
		xstream = new XStream();
		xstream.setClassLoader(getClass().getClassLoader());
		xstream.alias("UMSMsg", UMSMsg.class);
		xstream.alias("BasicMsg", BasicMsg.class);
		xstream.alias("MsgAttachment", MsgAttachment.class);
		xstream.alias("MsgContent", MsgContent.class);
		xstream.alias("Participant", Participant.class);
	}

	public synchronized String sendMessage(String receiver, String content) {
		return sendMessage(receiver, "", content);
	}

	public synchronized String sendMessage(String receiver, String title,
			String content) {
		String[] receivers = { receiver };
		return sendMessage(receivers, title, content);
	}

	public synchronized String sendMessage(String[] receivers, String title,
			String content) {
		String[][] groupReceivers = new String[1][receivers.length];
		for (int i = 0; i < receivers.length; i++) {
			groupReceivers[0][i] = receivers[i];
		}
		String[] contents = { content };
		String[] titles = { title };
		return sendMessage(groupReceivers, titles, contents);
	}

	public synchronized String sendMessage(String[][] groupReceivers,
			String[] titles, String[] contents) {
		return sendMessage(groupReceivers, titles, contents, appid, apppsw);
	}

	public synchronized String sendMessage(String receiver, String title,
			String content, String appid, String appPsw) {
		String[] receivers = { receiver };
		return sendMessage(receivers, title, content, appid, appPsw);
	}

	public synchronized String sendMessage(String[] receivers, String title,
			String content, String appid, String appPsw) {
		String[][] groupReceivers = new String[1][receivers.length];
		for (int i = 0; i < receivers.length; i++) {
			groupReceivers[0][i] = receivers[i];
		}
		String[] contents = { content };
		String[] titles = { title };
		return sendMessage(groupReceivers, titles, contents, appid, appPsw);
	}

	public synchronized String sendMessage(String[][] groupReceivers,
			String[] titles, String[] contents, String appid, String apppsw) {

		if (!inited) {
			return INIT_FAILED;
		}
		if (groupReceivers.length != contents.length) {
			return GROUP_MALFORM;
		}
		BasicMsg[] basicMsgs = new BasicMsg[groupReceivers.length];
		for (int i = 0; i < groupReceivers.length; i++) {
			basicMsgs[i] = new BasicMsg();
			basicMsgs[i].setAck(BasicMsg.UMSMsg_ACK_NO);
			basicMsgs[i].setContentMode(BasicMsg.BASICMSG_CONTENTE_MODE_GBK);
			basicMsgs[i].setDirectSendFlag(BasicMsg.BASICMSG_DIRECTSEND_YES);
			basicMsgs[i].setMediaID(mediaID);
			basicMsgs[i].setMsgContent(new MsgContent(titles[i], contents[i]));
			basicMsgs[i].setPriority(BasicMsg.UMSMsg_PRIORITY_NORMAL);
			ArrayList rawReceivers = new ArrayList();
			for (int n = 0; n < groupReceivers[i].length; n++) {
				if (groupReceivers[i][n] != null
						&& !groupReceivers[i][n].equals("")) {
					rawReceivers.add(groupReceivers[i][n]);
				}
			}
			Participant[] receivers = new Participant[rawReceivers.size()];
			for (int n = 0; n < rawReceivers.size(); n++) {
				receivers[n] = new Participant((String) rawReceivers.get(n),
						Participant.PARTICIPANT_MSG_TO,
						Participant.PARTICIPANT_ID_MOBILE);
			}
			basicMsgs[i].setReceivers(receivers);
			basicMsgs[i].setSender(new Participant(appid,
					Participant.PARTICIPANT_MSG_FROM,
					Participant.PARTICIPANT_ID_MOBILE));
			basicMsgs[i].setServiceID(appid);
			String s = sdf.format(new Date());
			basicMsgs[i].setSubmitDate(s.substring(0, 8));
			basicMsgs[i].setSubmitTime(s.substring(8, 14));
			basicMsgs[i].setTimeSetFlag(BasicMsg.BASICMSG_SENDTIME_NOCUSTOM);
		}
		String xmlMsg = xstream.toXML(basicMsgs);
		return send(appid, apppsw, xmlMsg);
	}

	protected abstract String send(String appid, String apppsw, String xml);

	public String getMediaID() {
		return mediaID;
	}

	public void setMediaID(String mediaID) {
		this.mediaID = mediaID;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getApppsw() {
		return apppsw;
	}

	public void setApppsw(String apppsw) {
		this.apppsw = apppsw;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

}
