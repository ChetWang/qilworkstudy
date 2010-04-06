package com.nci.svg.sdk.ui.equip;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.client.util.NCIEquipPropConstants;
import com.nci.svg.sdk.client.util.Utilities;


/**
 * yux转换后的文件的metadata及设备属性、电压等级的获取方式
 * @author Qil.Wong
 *
 */
public class YXTransferedMetaDataManager extends MetaDataManager{
	
	public YXTransferedMetaDataManager(EditorAdapter editor){
		super(editor);
	}

	public String getCIMType(Element ele) {
		String cimType = "";
		if (!ele.getAttribute(Constants.NCI_SVG_Type_Attr)
				.equals("")) {
			// 新世纪svg标准的设备类型获取方式......
		} else {// 其他转过来的svg图的处理
			try {
				// selectedEquipElement =
				// Utilities.parseSelectedElement(selectedEquipElement);
				Element metadataElement = Utilities.getSingleChildElement(
						Constants.NCI_SVG_METADATA, ele);
				if (metadataElement != null) {
					Element cimClassEle = Utilities.getSingleChildElement(
							"PSR:CimClass", metadataElement);
					if (cimClassEle != null)
						cimType = cimClassEle.getAttribute("CimType");
				}
			} catch (Exception e) {
			}
		}
		return cimType;
	}

	public String getCapability(Element ele) {
		
		return null;
	}

	public String getEquipType(Element ele) {
		
		return getCIMType(ele);
	}

	public String getName(Element ele) {
		
		return null;
	}

	public String getParentID(Element ele) {
		
		return null;
	}

	public String getScadaID(Element ele) {
		String id = "";
		if (!ele.getAttribute(Constants.NCI_SVG_Type_Attr)
				.equals("")) {

		} else {
			try {
				Element metadataElement = Utilities.getSingleChildElement(
						Constants.NCI_SVG_METADATA, ele);
				if (metadataElement != null) {
					Element cimClassEle = Utilities.getSingleChildElement(
							Constants.NCI_SVG_PSR_OBJREF, metadataElement);
					if (cimClassEle != null)
						id = cimClassEle
								.getAttribute(Constants.NCI_SVG_SCADAID_ATTR);
				}
			} catch (Exception e) {
			}

		}
		return id;
	}

	public String getPSMSID(Element ele) {
		String id = "";
		if (!ele.getAttribute(Constants.NCI_SVG_Type_Attr)
				.equals("")) {
			NodeList properties = ele
					.getElementsByTagName("property");
			for (int i = 0; i < properties.getLength(); i++) {
				Element propertyElement = (Element) properties.item(i);
				if (propertyElement.getAttribute("name").equals(
						NCIEquipPropConstants.EQUIP_ID_CODE)) {
					id = propertyElement.getAttribute("value");
					break;
				}
			}
		} else {

			try {
				Element metadataElement = Utilities.getSingleChildElement(
						Constants.NCI_SVG_METADATA, ele);
				if (metadataElement != null) {
					Element cimClassEle = Utilities.getSingleChildElement(
							Constants.NCI_SVG_PSR_OBJREF, metadataElement);
					if (cimClassEle != null)
						id = cimClassEle
								.getAttribute(Constants.NCI_SVG_APPCODE_ATTR);
				}
			} catch (Exception e) {
			}

		}
		return id;
	}

	public String getVoltageLevel(Element ele) {
		String level = "";
		if (!ele.getAttribute(Constants.NCI_SVG_Type_Attr)
				.equals("")) {
			// 新世纪svg标准的电压等级获取方式......
		} else {// 其他转过来的svg图的处理
			// selectedEquipElement =
			// Utilities.parseSelectedElement(selectedEquipElement);
			level = ele.getAttribute("class");
		}
		if(level.equalsIgnoreCase("fill")||level.equalsIgnoreCase("fillnone")){
			level = "";
		}
		return level;
	}

	@Override
	public String getTooltipString(Element selectedShapeEle) {
		String scadaCimType = getCIMType(selectedShapeEle);
		String scadaId = getScadaID(selectedShapeEle);
		String name = getName(selectedShapeEle);
		String parentID = getParentID(selectedShapeEle);
		String equipType = getCIMType(selectedShapeEle);
		String vol = getVoltageLevel(selectedShapeEle);
		String equipID = getPSMSID(selectedShapeEle);
		String capability = getCapability(selectedShapeEle);
		StringBuffer tooltip = new StringBuffer("<html>\n<body>\n");
		if (scadaCimType != null && !scadaCimType.trim().equals("")) {
			tooltip.append("<p>\n").append("SCADA设备类型:").append(scadaCimType)
					.append("\n");
		}
		if (scadaId != null && !scadaId.trim().equals("")) {
			tooltip.append("<p>\n").append("SCADA设备编码:").append(scadaId)
					.append("\n");
		}
		if (name != null && !name.trim().equals("")) {
			tooltip.append("<p>\n").append("名称:").append(name).append("\n");
		}
		if (parentID != null && !parentID.trim().equals("")) {
			tooltip.append("<p>\n").append("父设备:").append(parentID)
					.append("\n");
		}
		if (equipType != null && !equipType.trim().equals("")) {
			tooltip.append("<p>\n").append("设备类型:").append(equipType).append(
					"\n");
		}

		if (vol != null && !vol.trim().equals("")) {
			tooltip.append("<p>\n").append("电压等级:").append(vol).append("\n");
		}

		if (equipID != null && !equipID.trim().equals("")) {
			tooltip.append("<p>\n").append("设备编号:").append(equipID)
					.append("\n");
		}
		if (capability != null && !capability.trim().equals("")) {
			tooltip.append("<p>\n").append("容量:").append(capability).append(
					"\n");
		}
		return tooltip.toString();
	}

}
