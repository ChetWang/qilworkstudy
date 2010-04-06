package com.nci.svg.ui.equip;

import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import com.nci.svg.util.Constants;
import com.nci.svg.util.Utilities;

import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * GIS �����svg��metadata
 * 
 * @author Qil.Wong
 * 
 */
public class GISMetaDataManager extends MetaDataManager {

	private HashMap<String, String> codeMap = new HashMap<String, String>();

	public GISMetaDataManager(Editor editor) {
		super(editor);
		Document otherTipDoc = ResourcesManager
				.getXMLDocument("gistooltipcode.xml");
		NodeList tooltipCodes = otherTipDoc.getElementsByTagName("tooltip");
		for (int i = 0; i < tooltipCodes.getLength(); i++) {
			Element tipCode = (Element) tooltipCodes.item(i);
			codeMap.put(tipCode.getAttribute("attributeCode"), tipCode
					.getAttribute("attributeName"));
		}
	}

	public String getCIMType(Element ele) {
		return getValueFromSelectedShapeEle("PSR:CimClass", "CimType", ele);
	}

	public String getCapability(Element ele) {
		return getValueFromSelectedShapeEle("PSR:ObjRef", "Capability", ele);
	}

	public String getEquipType(Element ele) {
		return getValueFromSelectedShapeEle("PSR:ObjRef", "equipType", ele);
	}

	public String getName(Element ele) {
		return getValueFromSelectedShapeEle("PSR:ObjRef", "Name", ele);
	}

	public String getParentID(Element ele) {
		return getValueFromSelectedShapeEle("PSR:ObjRef", "parentID", ele);
	}

	public String getScadaID(Element ele) {
		return getValueFromSelectedShapeEle("PSR:ObjRef", "ObjectID", ele);
	}

	public String getPSMSID(Element ele) {
		return getValueFromSelectedShapeEle("PSR:ObjRef", "MisID", ele);
	}

	public String getVoltageLevel(Element ele) {
		return getValueFromSelectedShapeEle("PSR:ObjRef", "VoltageLevel", ele);
	}

	/**
	 * ��ѡ�е�ͼ�νڵ��ȡmetadataֵ
	 * 
	 * @param eleTagName
	 *            metadata���ӽڵ������
	 * @param attributeName
	 *            metadata����Ҫ��ȡ����������
	 * @param selectedShapeEle
	 *            ѡ�е�ͼ�νڵ�
	 * @return
	 */
	private String getValueFromSelectedShapeEle(String eleTagName,
			String attributeCode, Element selectedShapeEle) {
		Element metadataEle = Utilities.getSingleChildElement(
				Constants.NCI_SVG_METADATA, selectedShapeEle);
		return getValueFromMetaDataEle(eleTagName, attributeCode, metadataEle);

	}

	/**
	 * ��ѡ�е�ͼ�νڵ��metadata�ڵ��ȡmetadataֵ
	 * 
	 * @param eleTagName
	 *            metadata���ӽڵ������
	 * @param attributeName
	 *            metadata����Ҫ��ȡ����������
	 * @param selectedShapeEle
	 *            ѡ�е�ͼ�νڵ��metadata�ڵ�
	 * @return
	 */
	private String getValueFromMetaDataEle(String eleTagName,
			String attributeCode, Element metadataEle) {
		if (metadataEle != null) {
			NodeList metadataChildren = metadataEle.getChildNodes();
			Element property = null;
			for (int i = 0; i < metadataChildren.getLength(); i++) {
				if (metadataChildren.item(i) instanceof Element) {
					property = (Element) metadataChildren.item(i);
					if (eleTagName.equals(property.getNodeName())) {
						NamedNodeMap attrMap = property.getAttributes();
						for (int n = 0; n < attrMap.getLength(); n++) {
							if(attrMap.item(n).getNodeName().equals(attributeCode)){//�����attrName����xml�е��������ƣ���ӦattributeCode
								return attrMap.item(n).getNodeValue();
							}
						}
					}
				}
			}
		}
		return null;
	}

	// public String getOthers(Element ele) {
	// StringBuffer sb = new StringBuffer();
	// NodeList tooptips = otherTipDoc.getElementsByTagName("tooltip");
	// Element metadataEle = Utilities.getSingleChildElement(
	// Constants.NCI_SVG_METADATA, ele);
	// Element tooltipEle = null;
	// String elementName = null;
	// String attributeName = null;
	// String attributeCode = null;
	// String tooltipValue = null;
	// if (tooptips.getLength() > 0) {
	// for (int i = 0; i < tooptips.getLength(); i++) {
	// tooltipEle = (Element) tooptips.item(i);
	// elementName = tooltipEle.getAttribute("elementName");
	// attributeName = tooltipEle.getAttribute("attributeName");
	// attributeCode = tooltipEle.getAttribute("attributeCode");
	// tooltipValue = getValueFromMetaDataEle(elementName,
	// attributeCode, metadataEle);
	// if (tooltipValue != null && !tooltipValue.trim().equals("")) {
	// sb.append("<p>\n").append(attributeName).append(":")
	// .append(tooltipValue).append("\n");
	// }
	// }
	// }
	// if (sb.length() > 0)
	// return sb.toString();
	// return null;
	// }

	@Override
	public String getTooltipString(Element selectedShapeEle) {
		Element metadataEle = Utilities.getSingleChildElement(
				Constants.NCI_SVG_METADATA, selectedShapeEle);
		NodeList properties = metadataEle.getChildNodes();
		StringBuffer sb = new StringBuffer("<html>\n<body>\n");
		Element property = null;
		for (int i = 0; i < properties.getLength(); i++) {
			if (properties.item(i) instanceof Element) {
				property = (Element) properties.item(i);
				NamedNodeMap attrMap = property.getAttributes();
				for (int n = 0; n < attrMap.getLength(); n++) {
					String attrName = attrMap.item(n).getNodeName();
					String attrValue = attrMap.item(n).getNodeValue();
					//��ѹ�ȼ���css�ҹ���Ĭ�ϻ���fill��fillnone����Ͳ�����Ϊʵ�ʵĵ�ѹ�ȼ�
					if(attrName.equals("VoltageLevel") && attrValue.indexOf("fill")>=0){
						attrValue = "";
					}
					if (attrValue != null && !attrValue.trim().equals("")) {
						sb.append("<p>\n").append(codeMap.get(attrName))
								.append(":").append(attrValue).append("\n");
					}
				}
			}
		}
		return sb.toString();
	}
}
