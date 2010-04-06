package nci.gps.util;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class Utilities {

	public static Document getConfigXMLDocument(String xmlName) {
		try {
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(new File("conf/" + xmlName));
			return doc;
		} catch (Exception e) {
			MsgLogger.logExceptionTrace("¶ÁÈ¡xmlÎÄ¼þ´íÎó£º" + xmlName, e);
		}
		return null;
	}

}
