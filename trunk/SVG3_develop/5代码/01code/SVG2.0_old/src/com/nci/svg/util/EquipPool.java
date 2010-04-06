package com.nci.svg.util;

import com.nci.svg.equip.NCIPropertyComboBean;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.itris.glips.svgeditor.resources.ResourcesManager;

public class EquipPool {

	private static Document equipPropertiesDoc = null;
	private static Document comboMetadataDoc = null;
	private static HashMap<String, HashMap<String,NCIPropertyComboBean>> comboMap = new HashMap<String, HashMap<String,NCIPropertyComboBean>>();
	/**
	 * 设备属性表，key是代码，value是名称，如名称是"变电所"，代码是substation
	 */
	private static HashMap<String,String> equipTypeMap = new HashMap<String, String>();
	
	
	static {
		equipPropertiesDoc = ResourcesManager
				.getXMLDocument("nciEquipProperties.xml");
		comboMetadataDoc = ResourcesManager
				.getXMLDocument("nciComboMetadata.xml");
		// 初始化combobox的值
		NodeList combos = comboMetadataDoc.getElementsByTagName("DataType");
		for (int i = 0; i < combos.getLength(); i++) {
			Node combo = combos.item(i);
			if (combo instanceof Element) {
				String name = ((Element) combo).getAttribute("name");
				HashMap<String,NCIPropertyComboBean> values = new HashMap<String,NCIPropertyComboBean>();
				NodeList valueList = combo.getChildNodes();
				for (int n = 0; n < valueList.getLength(); n++) {
					Node value = valueList.item(n);
					if (value instanceof Element) {
						String valueName = ((Element) value)
								.getAttribute("name");
						String valueID = ((Element) value).getAttribute("id");
						NCIPropertyComboBean comboBean = new NCIPropertyComboBean(
								valueID, valueName);
						values.put(valueName, comboBean);
					}
				}
				comboMap.put(name, values);
			}
		}
		
		//初始化设备类型对照表
		NodeList properties  = equipPropertiesDoc.getElementsByTagName("Properties");
		for(int i=0;i<properties.getLength();i++){
			Element property = (Element)properties.item(i);
			equipTypeMap.put(property.getAttribute("value"),property.getAttribute("equipType"));
		}
	}

	/**
	 * 获取所有设备属性Document对象
	 * @return
	 */
	public static Document getEquipPropertiesDoc() {
		return equipPropertiesDoc;
	}

	/**
	 * 获取设备属性ComboBox组件元数据Document对象
	 * @return
	 */
	public static Document getComboMetadataDoc() {
		return comboMetadataDoc;
	}

	/**
	 * 获取所有已定义的ComboBox设备属性组件的哈希map，第一个
	 * @return
	 */
	public static HashMap<String, HashMap<String,NCIPropertyComboBean>> getComboMap() {
		return comboMap;
	}

	public static HashMap<String, String> getEquipTypeMap() {
		return equipTypeMap;
	}

	public static void setEquipTypeMap(HashMap<String, String> equipTypeMap) {
		EquipPool.equipTypeMap = equipTypeMap;
	}

}
