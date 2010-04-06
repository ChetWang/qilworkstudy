package com.nci.svg.server.automapping;

import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;

public class WriteXMLFile {

	public WriteXMLFile() {
	}

	public void CreateXMLFile(String inFile) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.newDocument();
		Element root = doc.createElement("content");
		doc.appendChild(root);

		Element title = doc.createElement("title");
		root.appendChild(title);
		Text ttitle = doc.createTextNode("用XSLT进行断行");
		title.appendChild(ttitle);
		Node node = doc.createCDATASection(".KV500 { stroke:rgb(255,140,0);fill:rgb(255,140,0)}"+
		".KV330 { stroke:rgb(255,255,100);fill:rgb(255,255,100)}"+
		".KV220 { stroke:rgb(227,0,227);fill:rgb(227,0,227)}"+
		".KV110 { stroke:rgb(255,0,0);fill:rgb(255,0,0)}"+
		".KV35 { stroke:rgb(255,255,0);fill:rgb(255,255,0)}"+
		".KV20 { stroke:rgb(180,255,180);fill:rgb(180,255,180)}"+
		".KV10 { stroke:rgb(96,126,229);fill:rgb(96,126,229)}"+
		".KV18 { stroke:rgb(0,170,0);fill:rgb(0,170,0)}"+
		".KV6 { stroke:rgb(180,0,0);fill:rgb(180,0,0)}"+
		".V380 { stroke:rgb(255,140,0);fill:rgb(255,140,0)}"+
		".KV中性点 { stroke:rgb(110,0,0);fill:rgb(110,0,0)}");
		root.appendChild(node);
//		node = doc.createAttribute("测试2");
//		root.appendChild(node);

		Element author = doc.createElement("author");
		root.appendChild(author);
		Text tauthor = doc.createTextNode("Kevin   Manley");
		author.appendChild(tauthor);

		Element body = doc.createElement("body");
		root.appendChild(body);
		String str_body = "this   is   the   body";
		Text tbody = doc.createTextNode(str_body);
		body.appendChild(tbody);

		try {
			TransformerFactory tff = TransformerFactory.newInstance();
			Transformer tf = tff.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult rs = new StreamResult(new File(inFile));
			tf.transform(source, rs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		WriteXMLFile createXMLFile1 = new WriteXMLFile();
		createXMLFile1.CreateXMLFile("log/ww.xml");
	}
}