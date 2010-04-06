package com.nci.svg.sdk.tooltip;

import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import com.nci.svg.sdk.bean.SimpleIndunormBean;
import com.nci.svg.sdk.client.DataManageAdapter;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.client.util.EditorToolkit;
import com.nci.svg.sdk.client.util.Utilities;
import com.nci.svg.sdk.module.IndunormModuleAdapter;

public class CanvasToolTipManager {

    private EditorAdapter editor;
    private Element element;

    public CanvasToolTipManager(EditorAdapter editor) {
        this.editor = editor;
    }

    /**
     * 处理指定节点的tooltip
     *
     * @param element
     */
    public void handleToolTip(Element originalElement) {
        this.element = originalElement;
        String tooltipText = getToolTip(element);
              
        if (tooltipText != null && !tooltipText.equals("")) {

            editor.getHandlesManager().getCurrentHandle().getCanvas().setToolTipText(tooltipText);
        }
    }

    /**
     * 显示tooltip
     *
     * @param element
     */
    private String getToolTip(Element element) {
        editor.getHandlesManager().getCurrentHandle().getCanvas().setToolTipText(null);
        if (element == null) {
            return "";
        }
        if (!EditorToolkit.isElementVisible(element)) {
            return "";
        }
        // Node metadata = element.getNextSibling();
        Element metadata = Utilities.getSingleChildElement(
                Constants.NCI_SVG_METADATA, element);
        if(metadata == null)
        	return "";

        String nciTypeValue = element.getAttribute(Constants.NCI_SVG_Type_Attr);
        // 如果是nciType的设备类型，或者是有metadata的非text类型节点，或者是包含metadata的g节点（含多个子设备），都当作是一个设备
        HashMap<String,SimpleIndunormBean> map = (HashMap<String,SimpleIndunormBean>)editor.getDataManage().getData(DataManageAdapter.KIND_GLOBAL, 
        		IndunormModuleAdapter.DATA_ID, null).getReturnObj();
        if(map == null || map.size() == 0)
        	return "";
        NodeList properties = metadata.getChildNodes();
		StringBuffer sb = new StringBuffer("<html>\n<body>\n");
		Element property = null;
		for (int i = 0; i < properties.getLength(); i++) {
			if (properties.item(i) instanceof Element) {
				property = (Element) properties.item(i);
				NamedNodeMap attrMap = property.getAttributes();
				String nodeName = property.getNodeName();
				for (int n = 0; n < attrMap.getLength(); n++) {
					String attrName = attrMap.item(n).getNodeName();
					String attrValue = attrMap.item(n).getNodeValue();
					String showName = map.get(nodeName + ":" + attrName).getFieldName();
					if (attrValue != null && !attrValue.trim().equals("")) {
						sb.append("<p>\n").append(showName)
								.append(":").append(attrValue).append("\n");
					}
				}
			}
		}
		return sb.toString();
    }
}
