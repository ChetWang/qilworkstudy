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

}
