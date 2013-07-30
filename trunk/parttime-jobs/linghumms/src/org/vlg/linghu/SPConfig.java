package org.vlg.linghu;

import org.virbraligo.util.VLGFunction;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class SPConfig {
	private static String userName;
	private static String password;
	private static String vaspId;
	private static String vasId;
	private static String spNumber;
	private static String attachmentDir;
	private static int msgSendDuration;
	private static int msgDetectDuration;
	private static String smsGateway;
	private static int smsGatewayPort;
	private static String smsReceiveIp;
	private static int smsReceivePort;
	

	static {
		Document doc = VLGFunction.getXMLDocument(SPConfig.class
				.getResource("/mm7Config.xml"));
		Node userNameNode = doc.getElementsByTagName("UserName").item(0);
		userName = userNameNode.getFirstChild().getNodeValue();
		Node passwordNode = doc.getElementsByTagName("Password").item(0);
		password = passwordNode.getFirstChild().getNodeValue();

		Node vaspIdNode = doc.getElementsByTagName("VASPID").item(0);
		vaspId = vaspIdNode.getFirstChild().getNodeValue();
		Node vasIdNode = doc.getElementsByTagName("VASID").item(0);
		vasId = vasIdNode.getFirstChild().getNodeValue();
		Node spNumberNode = doc.getElementsByTagName("SPNUMBER").item(0);
		spNumber = spNumberNode.getFirstChild().getNodeValue();

		Node attDirNode = doc.getElementsByTagName("AttachmentDir").item(0);
		attachmentDir = attDirNode.getFirstChild().getNodeValue();
		
		Node msgSendDurationNode = doc.getElementsByTagName("MsgSendDuration").item(0);
		msgSendDuration = Integer.parseInt(msgSendDurationNode.getFirstChild().getNodeValue());
		
		Node msgDetectDurationNode = doc.getElementsByTagName("MsgDetectDuration").item(0);
		msgDetectDuration = Integer.parseInt(msgDetectDurationNode.getFirstChild().getNodeValue());
		
		Node smsGatewayNode = doc.getElementsByTagName("SMS_Gateway").item(0);
		smsGateway = smsGatewayNode.getFirstChild().getNodeValue();

		Node smsGatewayPortNode = doc.getElementsByTagName("SMS_Gateway_port").item(0);
		smsGatewayPort = Integer.parseInt(smsGatewayPortNode.getFirstChild().getNodeValue());
		
		Node smsReceiveIPNode = doc.getElementsByTagName("SMS_Receive_IP").item(0);
		smsReceiveIp = smsReceiveIPNode.getFirstChild().getNodeValue();
		
		Node smsReceivePOrtNode = doc.getElementsByTagName("SMS_Receive_port").item(0);
		smsReceivePort = Integer.parseInt(smsReceivePOrtNode.getFirstChild().getNodeValue());
		
//		<SMS_Gateway>211.90.223.213</SMS_Gateway>
//	    <c>8801</SMS_Gateway_port>
//	    <SMS_Receive_IP>0.0.0.0</SMS_Receive_IP>
//	    <SMS_Receive_port>6888</SMS_Receive_port>
	}

	public static String getAttachmentDir() {
		return attachmentDir;
	}

	public static String getUserName() {
		return userName;
	}

	public static String getPassword() {
		return password;
	}

	public static String getVaspId() {
		return vaspId;
	}

	public static String getVasId() {
		return vasId;
	}

	public static String getSpNumber() {
		return spNumber;
	}

	public static int getMsgSendDuration() {
		return msgSendDuration;
	}

	public static int getMsgDetectDuration() {
		return msgDetectDuration;
	}

	public static String getSmsGateway() {
		return smsGateway;
	}

	public static int getSmsGatewayPort() {
		return smsGatewayPort;
	}

	public static String getSmsReceiveIp() {
		return smsReceiveIp;
	}

	public static int getSmsReceivePort() {
		return smsReceivePort;
	}

}
