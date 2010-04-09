package com.nci.domino.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 * XML工具类
 * 
 * @author denvelope
 * 
 */
public class XmlUtils {

	public static Document fromXML(InputStream is) {
		Document doc = null;
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream(is.available());
			if (is != null && os != null) {
				byte[] buffer = new byte[32 * 1024];
				int bytesRead = 0;
				while ((bytesRead = is.read(buffer)) != -1) {
					os.write(buffer, 0, bytesRead);
				}
			}
			// 通过这种方式可以读取 UTF-8 格式的数据，中文不会乱码
			doc = fromXML(new String(os.toByteArray(), "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}

	public static Document fromXML(String xml) {
		Document doc = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			StringReader sr = new StringReader(xml);
			InputSource iSrc = new InputSource(sr);
			doc = builder.parse(iSrc);
			doc.normalize();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}

	public static String toXML(Node doc) {
		try {
			Source source = new DOMSource(doc);
			Transformer xformer = TransformerFactory.newInstance()
					.newTransformer();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(baos);
			Result result = new StreamResult(out);
			xformer.transform(source, result);
			out.close();
			return new String(baos.toByteArray(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
