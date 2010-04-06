/**
 * 类名：com.nci.svg.other
 * 创建人:yx.nci
 * 创建日期：2008-7-24
 * 类作用:TODO
 * 修改日志：
 */
package com.nci.svg.other;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import fr.itris.glips.svgeditor.properties.SVGProperties;
import fr.itris.glips.svgeditor.properties.SVGPropertyItem;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * @author yx.nci
 *
 */
public class SVGNciPropertyItem extends SVGPropertyItem {
    /* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.properties.SVGPropertyItem#setAttributeValue(java.lang.String, java.util.LinkedHashMap)
     */
    @Override
    public void setAttributeValue(String name, LinkedHashMap values) {
        // TODO Auto-generated method stub
        super.setAttributeValue(name, values);
        if(math != null)
            math.changeTarget((Element)element,name, generalPropertyValue);
    }
    /* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.properties.SVGPropertyItem#changePropertyValue(java.lang.String)
     */
    @Override
    public void changePropertyValue(String value) {
        super.changePropertyValue(value);
        if(math != null)
            math.cal();
    }
    protected Node element = null;
    protected String  strID = null;
    private NciTargetMath math = null;
    /**
     * @param math the math to set
     */
    public void setMath(NciTargetMath math) {
        this.math = math;
    }
    /* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.properties.SVGPropertyItem#getChildValue(java.lang.String)
     */
    @Override
    public String getChildValue(String name) {
        propertyValues.clear();
        String value = "";
        Node cur;
        for (cur = element.getFirstChild(); cur != null; cur = cur
                .getNextSibling()) {

            if (cur.getNodeName().equals(name)) {

                value = cur.getNodeValue();
                break;
            }
        }
        propertyValues.put(element, value);
        return value;
    }
    public SVGNciPropertyItem(SVGProperties properties, Node element,
            String propertyType, String propName, String propertyValueType,
            String defaultPropertyValue, String propertyConstraint,String showText,
            LinkedHashMap valuesMap)
    {

        this.properties = properties;
        this.element = element;
        this.propertyType = propertyType;
        this.valuesMap = valuesMap;

        try {
            this.propertyName = propName.substring(propName.indexOf("_") + 1,
                    propName.length());
        } catch (Exception ex) {
            this.propertyName = propName;
        }

        this.propertyValueType = propertyValueType;
        this.defaultPropertyValue = defaultPropertyValue;
        this.propertyConstraint = propertyConstraint;

        this.bundle = ResourcesManager.bundle;
        this.propertyLabel = showText;
        this.nodeList = new LinkedList();
        nodeList.add(element);
        if (valuesMap != null && valuesMap.size() > 0 )
        {
            valuesLabelMap = new LinkedHashMap();
            String name;
            for (Iterator it = valuesMap.keySet().iterator(); it.hasNext();) {
                try {
                    name = (String) it.next();
                } catch (Exception ex) {
                    name = null;
                }

                if (name != null && !name.equals("")) {
                    valuesLabelMap.put(name, name);
                }
            }
        }
 

         // sets the value of the property taking it from the node attributes
        if (propertyType != null && this.propertyName != null) {

            if (propertyType.equals("style")) {

                generalPropertyValue = getStylePropertyValue(this.propertyName);

            } else if (propertyType.equals("attribute")) {

                generalPropertyValue = getAttributeValue(this.propertyName);

            } else if (propertyType.equals("child")) {

                generalPropertyValue = getChildValue(this.propertyName);
            }
        }
    }
}
