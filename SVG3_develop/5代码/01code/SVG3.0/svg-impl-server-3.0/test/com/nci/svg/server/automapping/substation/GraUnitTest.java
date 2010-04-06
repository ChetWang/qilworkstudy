package com.nci.svg.server.automapping.substation;

import junit.framework.TestCase;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nci.svg.server.automapping.comm.AutoMapComm;
import com.nci.svg.server.util.XmlUtil;

public class GraUnitTest extends TestCase {

	public GraUnitTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGraUnitNode() {
		String fileContent = AutoMapComm.readDataString("\\WEB-INF\\classes\\Symbol.xml");
		Document doc = XmlUtil.getXMLDocumentByString(fileContent);
		NodeList rootNodeList = doc.getElementsByTagName("defs");
		if (rootNodeList.getLength() <= 0)
			return;
		Node root = rootNodeList.item(0);
		NodeList nodelist = root.getChildNodes();
		for (int i = 0, size = nodelist.getLength(); i < size; i++) {
			Node node = nodelist.item(i);
			if (node.getNodeName().equalsIgnoreCase("symbol")) {
				GraUnit graUnit = new GraUnit(node);
				System.out.println(graUnit);
			}
		}
	}
}
