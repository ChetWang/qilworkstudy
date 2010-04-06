package com.nci.ums.test;


import org.w3c.dom.*;
import org.apache.xerces.parsers.*;


public class XmlUtil{
	
	public static Document loadXmlFromFile(String fileName){
		DOMParser parser = new DOMParser();
		Document result=null;
		try {
			//InputSource in=new InputSource(new FileInputStream(new File(fileName)));
			//Log.log(Log.DEBUG,"装入XML文件:"+fileName);
			parser.parse(fileName);
			result = parser.getDocument();
		}catch (Exception e) {

			//System.out.println("config error!"+e);
		}		
		
		return result;
	}

	public static String getValueFromElement(Element element,String name){
		String result=null;
		try{
			Element nameElement=(Element) element.getElementsByTagName(name).item(0);		
			if(nameElement!=null){
				result=nameElement.getFirstChild().getNodeValue();		
			}
		}catch(Exception e){

		}
			
		return result;
	}

	public static String getValueFromElement(Element element){
		String result=null;
		try{
			if(element!=null){
				result=element.getFirstChild().getNodeValue();		
			}
		}catch(Exception e){

		}
		return result;
	}

	public static Element getElementFromElement(Element element,String name){
		Element result=(Element) element.getElementsByTagName(name).item(0);		
		return result;
	}
	
	public static String getAttributeFromElement(Element element,String name,String attributeName){
		String result=null;
		Element nameElement=(Element) element.getElementsByTagName(name).item(0);		
		if(nameElement!=null){
			result=nameElement.getAttribute(attributeName).toString();		
		}
		if(result!=null&&result.length()==0){
			result=null;
		}
		return result;
	}	

	public static String getAttributeFromElement(Element element,String attributeName){
		String result=null;
		if(element!=null){
			result=element.getAttribute(attributeName).toString();		
		}
			
		return result;
	}	
	
}