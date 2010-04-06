package com.nci.svg.server.automapping.comm;

import junit.framework.TestCase;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nci.svg.server.util.XmlUtil;

public class AutoMapUtilitiesTest extends TestCase {

	public AutoMapUtilitiesTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testDocToString() {
		String symbolStr = AutoMapComm
				.readDataString("\\WEB-INF\\classes\\Symbol.xml");
		Document doc = XmlUtil.getXMLDocumentByString(symbolStr);
		NodeList rootNodeList = doc.getElementsByTagName("defs");
		if (rootNodeList.getLength() <= 0)
			return;
		Node root = rootNodeList.item(0);
		String strTemp = null;
		NodeList nodelist = root.getChildNodes();
		for (int i = 0, size = nodelist.getLength(); i < size; i++) {
			Node node = nodelist.item(i);
			if (node.getNodeName().equalsIgnoreCase("symbol")) {
				strTemp = XmlUtil.printNode(node, false);
//				strTemp = AutoMapUtilities.nodeToString(node);
				System.out.println(strTemp);
			}
		}
	}
}
