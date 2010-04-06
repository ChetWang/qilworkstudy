package com.nci.svg.server.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

public class Utilities {
	
	/**
	 * 与Servlet一次通信过程，得到一个返回对象
	 * 
	 * @param baseURL
	 *            基本URL
	 * @param param
	 *            传递给servlet的参数
	 * @return
	 * @throws IOException
	 */
	public Object communicateWithURL(String baseURL, String[][] param)
			throws IOException {
		try {
			URL url = new URL(baseURL);
			URLConnection rConn = url.openConnection();
			rConn.setDoOutput(true);
			rConn.setDoInput(true);
//			rConn.setReadTimeout(Global.SERVLET_TIME_OUT);
			PrintStream output = new PrintStream(rConn.getOutputStream());
			StringBuffer rs = new StringBuffer();
			if (param != null && param.length > 0) {
				for (int i = 0; i < param.length; i++) {
					if (param[i][0] == null || param[i][1] == null)
						continue;
					if (i != 0) {
						rs.append("&");
					}
					rs.append(URLEncoder.encode(param[i][0], "UTF-8")).append(
							'=')
							.append(URLEncoder.encode(param[i][1], "UTF-8"));
				}
			}
			output.print(rs.toString());
			output.flush();
			ObjectInputStream ois = new ObjectInputStream(
					new BufferedInputStream(rConn.getInputStream()));
			return ois.readObject();
		}catch   (OutOfMemoryError   e){//可以用一个共通函数来执行.   
	        System.out.println("No   Memory!   ");   
	        System.gc();   
	 
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
	/**
	 * @function：业务工具类-将传入以xml的参数转换成HashMap
	 * @param xml 传入以xml形式的参数
	 * @return HashMap
	 * @author ZHANGSF
	 */
	public static HashMap parseXml(String xml) throws Exception{
		HashMap hm=new HashMap();
		SAXBuilder bld=new SAXBuilder();   
		String code,value;
		Element e;
		
		if(xml!=null){
			Document  doc=bld.build(new ByteArrayInputStream(xml.getBytes()));   
			XPath   xpath   =   XPath.newInstance("//param");   
			List   childNodes   =   xpath.selectNodes(doc); 
			
			Iterator it=childNodes.iterator();
			while(it.hasNext()){
				e=(Element) it.next();
				code=e.getChild("code").getText();
				System.out.println("code:"+code);
				value=e.getChild("value").getText();
				System.out.println("value:"+value);
				hm.put(code, value);
			}
		}
		return hm;
	}
	
	
	/**
	 * @function：业务工具类-将参数整合成xml返回
	 * @param rs 结果集
	 * @param name 从结果集取出做名称
	 * @param value 从结果集取出做值
	 * @return xml
	 * @author ZHANGSF
	 */
	public static String getModXml(HashMap param) throws SQLException{
		StringBuffer res=new StringBuffer("");
		Iterator it=param.keySet().iterator();
		String key;
		res.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
		.append("<params>");
		while(it.hasNext()){
				key=(String) it.next();
				res.append("<param>")
				.append("<name>")
				.append(key).append("</name>")
				.append("<value>")
				.append(param.get(key)).append("</value>")
				.append("</param>");
		}
		res.append("</params>");
		return res.toString();
	}
	

public static void main(String[] args) {
	try {
		System.out.println(Utilities.parseXml("<?xml version=\"1.0\" encoding=\"UTF-8\"?><params><param><name>MODENAME</name><value>EPMS_ZD</value></param></params>").get("MODENAME"));
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
	

}
