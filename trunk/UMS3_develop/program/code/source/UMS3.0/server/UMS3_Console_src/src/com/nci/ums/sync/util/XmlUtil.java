package com.nci.ums.sync.util;



import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nci.ums.sync.ldap.LdapOrgResult;
import com.nci.ums.sync.ldap.LdapPerResult;

public class XmlUtil {
	
	
	public int gettype(String xmlstr) {
		return 0;
	}
	private final static String getsex(String sex) {
		
		return "ÄÐ".equalsIgnoreCase(sex)?"1":"2";
	}
	
	
	public static LdapOrgResult parseOrgXml(String xmlstr) {
		LdapOrgResult lp =new LdapOrgResult();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new ByteArrayInputStream(xmlstr.toString()
					.getBytes("UTF-8")));
			doc.normalize();
			NodeList nl = doc.getElementsByTagName("property");
			for (int i = 0; i < nl.getLength(); i++) {
				Node my_node = nl.item(i);
				String parname = my_node.getAttributes().getNamedItem("name")
						.getNodeValue();
				if("deptcodename".equalsIgnoreCase(parname)) {
					String message = null;
					if (my_node.getFirstChild() != null)
						message = my_node.getFirstChild().getNodeValue();
					    lp.setOrgid(message);
			    }else if("orgstructure".equalsIgnoreCase(parname)) {
					String message = null;
					if (my_node.getFirstChild() != null)
						message = my_node.getFirstChild().getNodeValue();	
					   lp.setOrgname(getOrgname(message));
					   lp.setParentid(getBeforename(message));
				}else if("issubroot".equalsIgnoreCase(parname)) {
					String message = null;
					if (my_node.getFirstChild() != null)
						message = my_node.getFirstChild().getNodeValue();	
					   lp.setOrgtype(message);
				}
			}
		} catch (Exception e) {
		}		
		return lp;
	}
	
	
	
	public static int getXmlType(String xmlstr) {
		int a =0;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new ByteArrayInputStream(xmlstr.toString()
					.getBytes("UTF-8")));
			doc.normalize();
			NodeList invokeNodeList = doc.getElementsByTagName("invoke");
			Node invokeNode = invokeNodeList.item(0);
			String parname = invokeNode.getAttributes().getNamedItem("type")
					.getNodeValue();
			if ("invoke.createOrUpdateDepartment".equals(parname)) {
				a = 1;
			} else if ("invoke.updateUser".equals(parname)) {

				a = 2;
			}
		} catch (Exception e) {
		}	
		return a;
	}
	
	public static String getOrgname(String input) {
		String orgname = null;
		if(null!=input&&!"".equals(input)) {
			String  orglist [] =input.split(":");
			if(orglist.length>=1) {
				orgname= orglist[orglist.length-1];
			}
			
		}else {
			orgname= "";
		}
		return orgname;
	}
	
	public static String getBeforename(String input) {
		String Beforename = null;
		if(null!=input&&!"".equals(input)) {
			String  orglist [] =input.split(":");
			if(orglist.length>=1) {
				Beforename= orglist[orglist.length-2];
			}
			
		}else {
			Beforename= "";
		}
		return Beforename;
	}
	
	
	
	
	public static LdapPerResult parsePerXml(String xmlstr) {
		LdapPerResult lp =new LdapPerResult();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new ByteArrayInputStream(xmlstr.toString()
					.getBytes("UTF-8")));
			doc.normalize();
			NodeList nl = doc.getElementsByTagName("property");
			for (int i = 0; i < nl.getLength(); i++) {
				Node my_node = nl.item(i);
				String parname = my_node.getAttributes().getNamedItem("name")
						.getNodeValue();
				if("newloginname".equalsIgnoreCase(parname)) {
					String message = null;
					if (my_node.getFirstChild() != null)
						message = my_node.getFirstChild().getNodeValue();
					    lp.setSeqkey(message);
					    lp.setLoginname(message);
					   
				}else if("email".equalsIgnoreCase(parname)) {
					String message = null;
					if (my_node.getFirstChild() != null)
						message = my_node.getFirstChild().getNodeValue();	
					    lp.setEmail(message);
				}else if("deptcodename".equalsIgnoreCase(parname)) {
					String message = null;
					if (my_node.getFirstChild() != null)
						message = my_node.getFirstChild().getNodeValue();	
					    lp.setOrgid(message);
				}else if("username".equalsIgnoreCase(parname)) {
					String message = null;
					if (my_node.getFirstChild() != null)
						message = my_node.getFirstChild().getNodeValue();	
					    lp.setUsername(message);
				}else if("sex".equalsIgnoreCase(parname)) {
					String message = null;
					if (my_node.getFirstChild() != null)
						message = my_node.getFirstChild().getNodeValue();	
					    lp.setSex(getsex(message));
				}else if("mobilephone".equalsIgnoreCase(parname)) {
					String message = null;
					if (my_node.getFirstChild() != null)
						message = my_node.getFirstChild().getNodeValue();	
					    lp.setPhone(message);
				}else if("password".equalsIgnoreCase(parname)) {
					String message = null;
					if (my_node.getFirstChild() != null)
						message = my_node.getFirstChild().getNodeValue();	
					    lp.setPassword(message);
				}
			}
		} catch (Exception e) {
		}		
		return lp;
	}
	
	public static LdapOrgResult parseOrgXml() {
		LdapOrgResult lp = new LdapOrgResult();
		
		
		return lp;
	}
	public static void main(String[] args) {
		try {
			StringBuffer sb = new StringBuffer();
			BufferedReader reader = new BufferedReader(new FileReader(
					"C:/1234.xml"));
			String s = "";
			while ((s = reader.readLine()) != null) {
				sb.append(s);
			}
			System.out.println(sb.toString());
			XmlUtil.parseOrgXml(sb.toString());
		} catch (Exception e) {
		}		
		
    }
	

}
