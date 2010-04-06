package com.nci.svg.client.css;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.css.CSSManager;

import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * 通过XML进行配置的css管理器
 * @author Qil.Wong
 *
 */
public class XMLCSSManager extends CSSManager{

	public XMLCSSManager(EditorAdapter editor){
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
