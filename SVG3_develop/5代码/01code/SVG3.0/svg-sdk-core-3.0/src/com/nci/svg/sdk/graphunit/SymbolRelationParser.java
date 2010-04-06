package com.nci.svg.sdk.graphunit;

import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nci.svg.sdk.XMLPrint;

/**
 * ģ���symbol��ͼԪ����ģ�壩֮���ϵת����. 
 * <root-template name="" id=""> 
 * 	<graphunit name="" id="" /> 
 * 	<graphunit name="" id="" /> 
 * 	... 
 * 	<template name="" id="" /> 
 * 	<template name="" id="" /> 
 * 	... 
 * </root-template>
 * 
 * @author Qil.Wong
 * 
 */
public class SymbolRelationParser {

	private static final String ROOT_TEMPLATE_ELE_NAME = "root-template";

	private static final String SYMBOL_ELE_NAME = "name";

	private static final String SYMBOL_ELE_ID = "id";

	private SymbolRelationParser() {
	}

	/**
	 * �ͻ��˽�ģ����ͼԪ����ģ��Ĺ�ϵת�����ַ���
	 * @param rootTemplate ��ģ��
	 * @param subSymbolBeans ��ģ���µ���ͼԪ����ģ��
	 * @return
	 * @throws ParserConfigurationException
	 */
	public static String parseRelation(NCIEquipSymbolBean rootTemplate,
			List subSymbolBeans) throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document relationDoc = factory.newDocumentBuilder().newDocument();
		Element root = relationDoc.createElement(ROOT_TEMPLATE_ELE_NAME);
		root.setAttribute(SYMBOL_ELE_NAME, rootTemplate.getName());
		root.setAttribute(SYMBOL_ELE_ID, rootTemplate.getId());
		if (subSymbolBeans != null && subSymbolBeans.size() > 0) {
			for (int i = 0; i < subSymbolBeans.size(); i++) {
				NCIEquipSymbolBean subBean = (NCIEquipSymbolBean) subSymbolBeans
						.get(i);
				Element subEle = relationDoc.createElement(subBean.getType());
				subEle.setAttribute(SYMBOL_ELE_NAME, subBean.getName());
				subEle.setAttribute(SYMBOL_ELE_ID, subBean.getId());
			}
		}
		return XMLPrint.printNode(relationDoc, false);
	}
}
