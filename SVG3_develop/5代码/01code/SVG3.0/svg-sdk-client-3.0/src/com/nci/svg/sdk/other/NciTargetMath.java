package com.nci.svg.sdk.other;

import java.awt.geom.AffineTransform;
import java.util.HashMap;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.util.EditorToolkit;
import com.nci.svg.sdk.client.util.Utilities;

import fr.itris.glips.svgeditor.display.handle.SVGHandle;

public class NciTargetMath {
    public Element target = null;
    public Element target_center = null;
    private EditorAdapter editor = null;
    private HashMap<String, SVGNciPropertyItem> map = new HashMap<String, SVGNciPropertyItem>();

    /**
     * @param target_center the target_center to set
     */
    public void setTarget_center(Element target_center) {
        this.target_center = target_center;
    }

    /**
     * @param map the map to set
     */
    public void setMap(String name, SVGNciPropertyItem item) {
        map.put(name, item);
    }

    /**
     * @param target the target to set
     */
    public void setTarget(Element target) {
        this.target = target;
    }

    public NciTargetMath(EditorAdapter editor) {
        this.editor = editor;
    }

    //通过预先设置的计算方式，调整指针角度
    public void cal() {
        if (target == null || target_center == null)
            return;
        SVGNciPropertyItem item = map.get("指标数值");
        String strValue = "";
        if (item == null)
            return;
        double curnum;
        try {
            strValue = item.getGeneralPropertyValue();
            if (strValue.lastIndexOf("%") == strValue.length() - 1) {
                strValue = strValue.substring(0, strValue.length() - 1);
                curnum = new Double(strValue).doubleValue();
                curnum /= 100;
            } else
                curnum = new Double(strValue).doubleValue();
        } catch (Exception ex) {
            return;
        }
        item = map.get("起始数值");
        if (item == null)
            return;
        double beginNum;
        try {
            strValue = item.getGeneralPropertyValue();
            if (strValue.lastIndexOf("%") == strValue.length() - 1) {
                strValue = strValue.substring(0, strValue.length() - 1);
                beginNum = new Double(strValue).doubleValue();
                beginNum /= 100;
            } else
                beginNum = new Double(item.getGeneralPropertyValue())
                        .doubleValue();
        } catch (Exception ex) {
            return;
        }
        item = map.get("终止数值");
        if (item == null)
            return;
        double endNum;
        try {
            strValue = item.getGeneralPropertyValue();
            if (strValue.lastIndexOf("%") == strValue.length() - 1) {
                strValue = strValue.substring(0, strValue.length() - 1);
                endNum = new Double(strValue).doubleValue();
                endNum /= 100;
            } else
                endNum = new Double(item.getGeneralPropertyValue())
                        .doubleValue();
        } catch (Exception ex) {
            return;
        }
        item = map.get("区间1数值");
        if (item == null)
            return;
        double area1Num;
        try {
            strValue = item.getGeneralPropertyValue();
            if (strValue.lastIndexOf("%") == strValue.length() - 1) {
                strValue = strValue.substring(0, strValue.length() - 1);
                area1Num = new Double(strValue).doubleValue();
                area1Num /= 100;
            } else
                area1Num = new Double(item.getGeneralPropertyValue())
                        .doubleValue();
        } catch (Exception ex) {
            return;
        }
        item = map.get("区间2数值");
        if (item == null)
            return;
        double area2Num;
        try {
            strValue = item.getGeneralPropertyValue();
            if (strValue.lastIndexOf("%") == strValue.length() - 1) {
                strValue = strValue.substring(0, strValue.length() - 1);
                area2Num = new Double(strValue).doubleValue();
                area2Num /= 100;
            } else
                area2Num = new Double(item.getGeneralPropertyValue())
                        .doubleValue();
        } catch (Exception ex) {
            return;
        }
        if (beginNum > area1Num && area1Num > area2Num && area2Num > endNum) {
            if (curnum > beginNum)
                curnum = beginNum;
            if (curnum < endNum)
                curnum = endNum;

            double angle = 0;
            if (curnum == beginNum) {
                angle = -45;
            } else if (curnum == area2Num) {
                angle = 135;
            } else if (curnum == area1Num) {
                angle = 45;
            } else if (curnum == endNum) {
                angle = 225;
            } else if (curnum > area1Num) {
                angle = 45 - ((double) (curnum - area1Num)) * 90
                        / (beginNum - area1Num);
            } else if (curnum > area2Num) {
                angle = 135 - ((double) (curnum - area2Num)) * 90
                        / (area1Num - area2Num);
            } else if (curnum > endNum) {
                angle = 225 - ((double) (curnum - endNum)) * 90
                        / (area2Num - endNum);
            }
            testFun(angle);
        } else if (beginNum < area1Num && area1Num < area2Num
                && area2Num < endNum) {
            if (curnum < beginNum)
                curnum = beginNum;
            if (curnum > endNum)
                curnum = endNum;

            double angle = 0;
            if (curnum == beginNum) {
                angle = -45;
            } else if (curnum == area2Num) {
                angle = 135;
            } else if (curnum == area1Num) {
                angle = 45;
            } else if (curnum == endNum) {
                angle = 225;
            } else if (curnum > area2Num) {
                angle = 135 + ((double) (curnum - area2Num)) * 90
                        / (endNum - area2Num);
            } else if (curnum > area1Num) {
                angle = 45 + ((double) (curnum - area1Num)) * 90
                        / (area2Num - area1Num);
            } else if (curnum > beginNum) {
                angle = -45 + ((double) (curnum - beginNum)) * 90
                        / (area1Num - beginNum);
            }
            testFun(angle);
        }

    }

    //根据角度转换指针位置
    public void testFun(double angle) {
        SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
        Document doc = handle.getCanvas().getDocument();

        Element element = target;
        element.removeAttribute("transform");
        double dx= Double.parseDouble(target_center.getAttribute("cx")) - Double.parseDouble(element.getAttribute("width"));
        element.setAttribute("x", String.valueOf(dx));
        double dy= Double.parseDouble(target_center.getAttribute("cy")) - ((int)Double.parseDouble(element.getAttribute("height")))/2;
        element.setAttribute("y", String.valueOf(dy));
        double x = new Double(target_center.getAttribute("cx")).doubleValue();
        //            double width = new Double(element.getAttribute("width")).doubleValue();
        double y = new Double(target_center.getAttribute("cy")).doubleValue();
        //            double height = new Double(element.getAttribute("height")).doubleValue();
        //            x = x + ((int)width);
        //            y += ((int)height)/2;

        AffineTransform hourTransform = AffineTransform.getRotateInstance(0, x,
                y);
        hourTransform.setToRotation(angle * (Math.PI / 180), x, y);
        //            hourTransform.setToRotation((-20) * 
        //                    (Math.PI / 180), x, y); 
        handle.getSvgElementsManager().setTransform(element, hourTransform);
        editor.getSvgSession().refreshCurrentHandleImediately();

        //        handle.getSvgElementsManager().
        //        setTransform(element, newElementTransform);
    }

    public void changeTarget(Element element, String name, String value) {
        if (name.equals("nci-targetorder")) {
            Element exchangeableelement = null;
            try {
                exchangeableelement = (Element) Utilities.findNode(
                        "*[@type='exchangeable']", element);
                if (exchangeableelement != null) {
                    exchangeableelement.setAttributeNS(
                            EditorToolkit.xmlnsXLinkNS, "xlink:href",
                            "#basetarget_" + value);
                    cal();
                    editor.getSvgSession().refreshCurrentHandleImediately();
                }
            } catch (XPathExpressionException e) {
                
                e.printStackTrace();
            }

        }
    }
}
