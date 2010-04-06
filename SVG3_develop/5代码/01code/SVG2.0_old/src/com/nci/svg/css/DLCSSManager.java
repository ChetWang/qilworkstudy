package com.nci.svg.css;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * 电力psms下css管理器
 * @author Qil.Wong
 *
 */
public class DLCSSManager extends CSSManager{

	public DLCSSManager(Editor editor){
		super(editor);
	}
	
	public  String getData(String xmlName){
		String data = "";
		Document doc = ResourcesManager.getXMLDocument(xmlName);
		Element root = doc.getDocumentElement();
		NodeList nodes = root.getChildNodes();
		for(int i=0;i<nodes.getLength();i++){
			Node n = nodes.item(i);
			if(n instanceof CDATASection){
				data =((CDATASection)n).getData();
			}
		}
		return data;
	}

	

}
