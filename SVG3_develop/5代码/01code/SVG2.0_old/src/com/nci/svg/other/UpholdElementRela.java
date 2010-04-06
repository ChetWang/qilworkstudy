/**
 * 类名：com.nci.svg.other.UpholdElementRela
 * 创建人:yx
 * 创建日期：2008-5-5
 * 类作用:负责维护结点连接关联信息
 * 修改日志：
 */
package com.nci.svg.other;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.Set;

import fr.itris.glips.svgeditor.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.nci.svg.graphunit.NCIMultiXMLGraphUnitManager;
import com.nci.svg.util.Constants;

import fr.itris.glips.library.geom.path.Path;
import fr.itris.glips.library.geom.path.segment.Segment;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.shape.path.PathShape;
import fr.itris.glips.svgeditor.shape.path.DrawingHandler;
import fr.itris.glips.svgeditor.shape.AbstractShape;
import fr.itris.glips.svgeditor.shape.ShapeToolkit;

public class UpholdElementRela {
    public static final int ADD_TYPE = 0;// 新增模式
    public static final int MODIFY_TYPE = 1;// 修改模式
    public static final int REMOVE_TYPE = 2;// 删除模式

    protected static String dAtt = "d";
    protected static String lP0 = "p0", lP1 = "p1";
    protected static String lineCount = "LineCount";
    protected static LinkPointManager lpManager = null;

    public UpholdElementRela(LinkPointManager lpManager) {
        this.lpManager = lpManager;
    }

    public void upholdElements(SVGHandle handle, Set<Element> elements, int nHandleType) {
//        System.out.println("多选操作");
        svgHandle = handle;
        
        for(Element element:elements)
        {
            if (element.getNodeName().equals("image")
                    || element.getNodeName().equals("use")
                    || element.getNodeName().equals("g"))
            {
                switch (nHandleType) {
                case ADD_TYPE: {
                        addSymbol(handle, element);
                    break;
                }
                case MODIFY_TYPE: {
                        modifySymbol(handle, element);
                    break;
                }
                case REMOVE_TYPE: {
                        removeSymbol(handle, element);
                    break;
                }
                }
            }
        }
        for(Element element:elements)
        {
            if (element.getNodeName().equals("path"))
            {
                switch (nHandleType) {
                case ADD_TYPE: {
                        addPath(handle, element);
                    break;
                }
                case MODIFY_TYPE: {
                        modifyPath(handle, element);
                    break;
                }
                case REMOVE_TYPE: {
                        removePath(handle, element);
                    break;
                }
                }
            }
        }
        
        return;
    }
    private SVGHandle svgHandle = null;
    /**
     * 根据图层句柄、节点和操作类型，分配具体的操作步骤
     * 
     * @param handle:图层句柄
     * @param element：操作的节点
     * @param nHandleType：操作的类型
     */
    public void upholdElement(SVGHandle handle, Element element, int nHandleType) {
        if (handle == null || element == null)
            return;
        svgHandle = handle;
        Element hElement = element;
        Element parentElement = (Element)element.getParentNode();
        if(parentElement != null)
        {
            String type = parentElement.getAttribute("type");
            if(parentElement.getNodeName().equals("g") && type != null && type.equals("solidified"))
            {
                hElement = parentElement;
            }
        }

        // 分析节点类型，不符合直接返回
        int nElementType = 0;
        if (element.getNodeName().equals("image")
                || element.getNodeName().equals("use"))
            nElementType = 0;
        else if (element.getNodeName().equals("path"))
            nElementType = 1;
        else
            return;

        // 分配操作函数
        switch (nHandleType) {
        case ADD_TYPE: {
            if (nElementType == 0)
                addSymbol(handle, hElement);
            else
                addPath(handle, element);
            break;
        }
        case MODIFY_TYPE: {
            if (nElementType == 0)
                modifySymbol(handle, hElement);
            else
                modifyPath(handle, element);
            break;
        }
        case REMOVE_TYPE: {
            if (nElementType == 0)
                removeSymbol(handle, hElement);
            else
                removePath(handle, element);
            break;
        }
        }
        return;
    }

    /**
     * 基于图元新增，恢复线关联，主要用于删除后重新使用ctrl+z恢复用
     * 
     * @param handle：图层句柄
     * @param element：节点
     */
    private void addSymbol(SVGHandle handle, Element element) {
        // 获取节点基础信息
        String strSymbolID = element.getAttribute("id");
        NodeList ncipoints = getNciPointList(element);

        if (ncipoints == null)
            return;

        // 逐个连接点分析线信息，并恢复线连接关系
        for (int i = 0; i < ncipoints.getLength(); i++) {
            Element mdelement = (Element) ncipoints.item(i);
            String strTemp = mdelement.getAttribute(lineCount);
            String strName = mdelement.getAttribute("name");

            if (strTemp == null || strTemp.length() == 0)
                continue;
            int nLineCount = new Integer(strTemp).intValue();
            if (nLineCount == 0)
                continue;

            String strLineID = null;
            for (int j = 0; j < nLineCount; j++) {
                strTemp = String.format("line%d", j);
                strLineID = mdelement.getAttribute(strTemp);
                String[] strTmp = strLineID.split(":");

                Element lElement = getLineElement(handle, strTmp[0]);
                if (lElement != null) {
                    addSymbolToLine(lElement, strTmp[1], strSymbolID + ":"
                            + strName);
                }
            }
        }

        return;
    }

    /**
     * 基于线新增，恢复图元关联，主要用于删除后重新使用ctrl+z恢复用
     * 
     * @param handle：图层句柄
     * @param element：节点
     */
    private void addPath(SVGHandle handle, Element element) {
        String strP0 = element.getAttribute(lP0);
        String strP1 = element.getAttribute(lP1);
        String strLineID = element.getAttribute("id");

        if (strP0 != null && strP0.length() != 0) {
            String[] strSymbol = strP0.split(":");
            Element eSymbol = getSymbolElement(handle, strSymbol[0]);
            if (eSymbol != null) {
                addLineToSymbol(eSymbol, strSymbol[1], strLineID + ":" + lP0);
            }
        }

        if (strP1 != null && strP1.length() != 0) {
            String[] strSymbol = strP1.split(":");
            Element eSymbol = getSymbolElement(handle, strSymbol[0]);
            if (eSymbol != null) {
                addLineToSymbol(eSymbol, strSymbol[1], strLineID + ":" + lP1);
            }
        }
        return;
    }

    /**
     * 基于图元位置尺寸角度修改，修正线关联，主要用于图元位置尺寸角度修改的情况
     * 
     * @param handle：图层句柄
     * @param element：节点
     * @param elements：连接在该图元上的连接线信息
     */
    // private void modifySymbol(SVGHandle handle, Element element,Set<Element>
    // elements) {
    // if(elements == null || elements.size() ==0)
    // return;
    //        
    // for(Element el :elements)
    // {
    //            
    // }
    // return;
    // }
    /**
     * 基于图元位置尺寸角度修改，修正线关联，主要用于图元位置尺寸角度修改的情况
     * 
     * @param handle：图层句柄
     * @param element：节点
     */
    private void modifySymbol(SVGHandle handle, Element element) {
        if(element.getNodeName().equals("image"))
        {
        String strID = element.getAttribute("id");
        Set<LinkPointManager.LineData> sets = lpManager.getLineData(strID);
        if (sets == null || sets.size() == 0)
            return;

        double dx = new Double(element.getAttribute("x")).doubleValue();
        double dy = new Double(element.getAttribute("y")).doubleValue();
        double nw = new Double(element.getAttribute("width")).doubleValue();
        double nh = new Double(element.getAttribute("height")).doubleValue();
        int x1 = 0, x2 = 0, y1 = 0, y2 = 0;
        final AffineTransform initialTransform = handle.getSvgElementsManager()
                .getTransform(element);
        for (LinkPointManager.LineData data : sets) {
            double[] pointo = { dx, dy };
//            Point2D.Double point = new Point2D.Double(dx,dy);
//            Point2D.Double point2 = new Point2D.Double(dx,dy);

            // 通过坐标变换，获取实际的坐标地址

            try {
//                initialTransform.transform(pointo, 0, pointo, 0, 1);
//                initialTransform.transform(point, point2);
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
            x2 = (int) pointo[0];
            y2 = (int) pointo[1];
            x2 += data.getDScaleX() * nw ;//*handle.getCanvas().getZoomManager().getCurrentScale();
            y2 += data.getDScaleY() * nh ;//*handle.getCanvas().getZoomManager().getCurrentScale();
            
            if(handle.getCanvas().getMapInfo(data.getStrLineID()) != null)
                modifyLineBySymbol(data.getElement(), x2, y2, data.getStrPointID());
        }
        }
        else if(element.getNodeName().equals("use"))
        {
            String strID = element.getAttribute("id");
            Set<LinkPointManager.LineData> sets = lpManager.getLineData(strID);
            if (sets == null || sets.size() == 0)
            {
                    return;
            }

            
            Element parent = (Element) element.getParentNode();
            while(parent.getParentNode().getNodeName().equals("g"))
            {
                parent =  (Element) parent.getParentNode();
            }
            
            int x2 = 0, y2 = 0;
            Rectangle2D bound = handle.getSvgElementsManager().getSensitiveBounds(parent);
            if(bound  == null)
                return;
            double nw = bound.getWidth();
            double nh = bound.getHeight();
            double x = bound.getMinX();
            double y = bound.getMinY();
            for (LinkPointManager.LineData data : sets) {
            x2 = (int)(x + data.getDScaleX() * nw) ;
            y2 = (int)(y+  data.getDScaleY() * nh) ;
            if(handle.getCanvas().getMapInfo(data.getStrLineID()) != null)
            {
                if(data.getElement().getNodeName().equals("metadata"))
                    modifyLineBySymbol((Element)data.getElement().getParentNode(), x2, y2, data.getStrPointID());
                else
                    modifyLineBySymbol(data.getElement(), x2, y2, data.getStrPointID());
            }
            }
        }
        else if(element.getNodeName().equals("g"))
        {
            String strID = element.getAttribute("id");
            Set<LinkPointManager.LineData> sets = lpManager.getLineData(strID);
            if (sets == null || sets.size() == 0)
                return;

            
            int x2 = 0, y2 = 0;
            Rectangle2D bound = handle.getSvgElementsManager().getSensitiveBounds(element);
           Element useelement = (Element)element.getElementsByTagName("use").item(0);
//            System.out.println(bound);
            if(bound  == null)
                return;
//            double nw = bound.getWidth();
//            double nh = bound.getHeight();
            double x = bound.getMinX();
            double y = bound.getMinY();
//            double nw = new Double(useelement.getAttribute("width")).doubleValue();
//            double nh = new Double(useelement.getAttribute("height")).doubleValue();
//            double x = new Double(useelement.getAttribute("x")).doubleValue();
//            double y = new Double(useelement.getAttribute("y")).doubleValue();
            for (LinkPointManager.LineData data : sets) {
                double nw = data.getDsw();
                double nh = data.getDsh();
            x2 = (int)(x + data.getDScaleX() * nw);//*handle.getCanvas().getZoomManager().getCurrentScale()) ;
            y2 = (int)(y + data.getDScaleY() * nh);//*handle.getCanvas().getZoomManager().getCurrentScale()) ;
            if(handle.getCanvas().getMapInfo(data.getStrLineID()) != null)
            {
                if(data.getElement().getNodeName().equals("metadata"))
                    modifyLineBySymbol((Element)data.getElement().getParentNode(), x2, y2, data.getStrPointID());
                else
                    modifyLineBySymbol(data.getElement(), x2, y2, data.getStrPointID());
            }
            }
        }
        return;
    }

    private void modifyPath(SVGHandle handle, Element element) {
        Element metadata = (Element) element.getElementsByTagName(Constants.NCI_SVG_METADATA)
                .item(0);
        Document doc = handle.getCanvas().getDocument();
        if (metadata == null) {
            return;
        }

        // 首先删除以前所有的线连接关系
        Element nciLink = (Element) element
                .getElementsByTagName("PSR:Nci_Link").item(0);

        if (nciLink == null)
            return;
        String strLineID = element.getAttribute("id");

        int k = 0;
        String strID = "";
        while (1 == 1) {
            strID = nciLink.getAttribute("Pin0InfoVect" + k + "LinkObjId");
            if (strID == null || strID.length() == 0)
                break;
            nciLink.removeAttribute("Pin0InfoVect" + k + "LinkObjId");
            k++;

            strID = strID.substring(0, strID.length() - 2);
            handle.getCanvas().removeLinkPoint(strID, strLineID);

        }

        k = 0;
        while (1 == 1) {
            strID = nciLink.getAttribute("Pin1InfoVect" + k + "LinkObjId");
            if (strID == null || strID.length() == 0)
                break;
            nciLink.removeAttribute("Pin1InfoVect" + k + "LinkObjId");
            k++;

            strID = strID.substring(0, strID.length() - 2);
            handle.getCanvas().removeLinkPoint(strID, strLineID);

        }
        
        //根据两点重新构建连接关系
        String dPath = element.getAttribute(dAtt);
        Path path = new Path(dPath);
        int nCount = path.getSegmentsNumber();

        Segment seg = path.getSegment();
        Point2D bpoint = seg.getEndPoint();
        for (int i = 0; i < nCount - 1; i++) {
            seg = seg.getNextSegment();
        }
        Point2D epoint = seg.getEndPoint();
        
        String strRelaFormat = "Pin%dInfoVect%dLinkObjId";

        Set<Element> sets = handle.getSvgElementsManager().getNodeAt(doc.getDocumentElement(), bpoint,
                null,1);
        Iterator iterator2 = sets.iterator();
        nCount = sets.size();
        int i = 0;
        String strTemp, strRela;
        while (iterator2.hasNext()) {
            Element el = (Element) iterator2.next();
            String strNodeName = el.getNodeName();
            String type = el.getAttribute("type");
            if(type == null || !type.equals("solidified"))
            {
            while (strNodeName.equals("g")) {
                NodeList nodeList = el.getChildNodes();
                for (int j = 0; j < nodeList.getLength(); j++) {
                    if (nodeList.item(j) instanceof Element) {
                        el = (Element) el.getChildNodes().item(j);
                        strNodeName = el.getNodeName();
                        break;
                    }
                }
            }
            }
            strID = el.getAttribute("id");
            if (!strID.equals(strLineID) && strID.length() >0 ) {
                strTemp = new String().format(strRelaFormat, 0, i);
                strRela = strID + "_0";
                if (strRela != null && strRela.length() > 0) {
                    nciLink.setAttribute(strTemp, strRela);
                    handle.getCanvas().addLinkPoint(strID, strLineID, element, "p0", bpoint);
                    i++;
                }
            }
        }

        Set<Element> sets2 =  handle.getSvgElementsManager().getNodeAt(doc.getDocumentElement(), epoint,
                null,1);
        nCount = sets2.size();
        iterator2 = sets2.iterator();
        i = 0;
        while (iterator2.hasNext()) {
            Element el = (Element) iterator2.next();
            String strNodeName = el.getNodeName();
            String type = el.getAttribute("type");
            if(type == null || !type.equals("solidified"))
            {
            while (strNodeName.equals("g")) {
                NodeList nodeList = el.getChildNodes();
                for (int j = 0; j < nodeList.getLength(); j++) {
                    if (nodeList.item(j) instanceof Element) {
                        el = (Element) el.getChildNodes().item(j);
                        strNodeName = el.getNodeName();
                        break;
                    }
                }
            }
            }
            strID = el.getAttribute("id");
            if (!strID.equals(strLineID) && strID.length() >0 ) {
                strTemp = new String().format(strRelaFormat, 1, i);
                strRela = strID + "_0";
                if (strRela != null && strRela.length() > 0) {
                    nciLink.setAttribute(strTemp, strRela);
                    handle.getCanvas().addLinkPoint(strID, strLineID, element, "p1", epoint);
                    i++;
                }
            }
        }
        return;
    }

    private Point getSymbolPoint(SVGHandle handle, Element element,
            String strPointID) {
        Point point = null;
        NodeList ncipoints = getNciPointList(element);
        if (ncipoints == null)
            return point;

        double dx = new Double(element.getAttribute("x")).doubleValue();
        double dy = new Double(element.getAttribute("y")).doubleValue();
        double sw = new Double(element.getAttribute("sw")).doubleValue();
        double sh = new Double(element.getAttribute("sh")).doubleValue();
        double nw = new Double(element.getAttribute("width")).doubleValue();
        double nh = new Double(element.getAttribute("height")).doubleValue();
        if (sw == 0)
            sw = nw;

        if (sh == 0)
            sh = nh;
        int x1 = 0, x2 = 0, y1 = 0, y2 = 0;
        final AffineTransform initialTransform = handle.getSvgElementsManager()
                .getTransform(element);
        for (int i = 0; i < ncipoints.getLength(); i++) {
            Element mdelement = (Element) ncipoints.item(i);
            if (mdelement.getAttribute("name").equals(strPointID)) {
                x1 = new Integer(mdelement.getAttribute("x")).intValue();
                y1 = new Integer(mdelement.getAttribute("y")).intValue();
                double[] pointo = { dx + x1 * nw / sw, dy + y1 * nh / sh };

                // 通过坐标变换，获取实际的坐标地址

                initialTransform.transform(pointo, 0, pointo, 0, 1);
                x2 = (int) pointo[0];
                y2 = (int) pointo[1];
                point = new Point();
                point.setLocation(x2, y2);
                return point;
            }
        }
        return point;
    }

    /**
     * 基于图元删除，删除线关联，主要用于新建后采用ctrl+z取消的情况
     * 
     * @param handle：图层句柄
     * @param element：节点
     */
    private void removeSymbol(SVGHandle handle, Element element) {
        NodeList ncipoints = getNciPointList(element);

        if (ncipoints == null)
            return;

        for (int i = 0; i < ncipoints.getLength(); i++) {
            Element mdelement = (Element) ncipoints.item(i);
            String strTemp = mdelement.getAttribute(lineCount);

            if (strTemp == null || strTemp.length() == 0)
                continue;
            int nLineCount = new Integer(strTemp).intValue();
            if (nLineCount == 0)
                continue;

            String strLineID = null;
            for (int j = 0; j < nLineCount; j++) {
                strTemp = String.format("line%d", j);
                strLineID = mdelement.getAttribute(strTemp);
                String[] strTmp = strLineID.split(":");

                Element lElement = getLineElement(handle, strTmp[0]);
                if (lElement != null) {
                    removeSymbolFromLine(lElement, strTmp[1]);
                }
            }
        }
        return;
    }

    /**
     * 基于线删除，删除图元关联，主要用于新建后采用ctrl+z取消的情况
     * 
     * @param handle：图层句柄
     * @param element：节点
     */
    private void removePath(SVGHandle handle, Element element) {
        String strP0 = element.getAttribute(lP0);
        String strP1 = element.getAttribute(lP1);
        String strLineID = element.getAttribute("id");

        if (strP0 != null && strP0.length() != 0) {
            String[] strSymbol = strP0.split(":");
            Element eSymbol = getSymbolElement(handle, strSymbol[0]);
            if (eSymbol != null) {
                removeLineFromSymbol(eSymbol, strLineID + ":" + lP0,
                        strSymbol[1]);
            }
        }

        if (strP1 != null && strP1.length() != 0) {
            String[] strSymbol = strP1.split(":");
            Element eSymbol = getSymbolElement(handle, strSymbol[0]);
            if (eSymbol != null) {
                removeLineFromSymbol(eSymbol, strLineID + ":" + lP1,
                        strSymbol[1]);
            }
        }

        return;
    }

    /**
     * 根据图元id，获取该图元节点
     * 
     * @param handle：图层句柄
     * @param strSymbolID：图元id
     * @return：如存在则返回该图元节点，不存在则返回null
     */
    private Element getSymbolElement(SVGHandle handle, String strSymbolID) {
        Element element = null;
        Element doc = handle.getCanvas().getDocument().getDocumentElement();
        NodeList nList = doc.getElementsByTagName("image");
        String strid = null;
        for (int i = 0; i < nList.getLength(); i++) {
            element = (Element) nList.item(i);
            strid = element.getAttribute("id");
            if (strid.equals(strSymbolID)) {
                return element;
            }
        }
        return null;

    }

    /**
     * 根据线id，获取该线节点
     * 
     * @param handle：图层句柄
     * @param strLineID：线id
     * @return：如存在则返回该线节点，不存在则返回null
     */
    private Element getLineElement(SVGHandle handle, String strLineID) {
        Element element = null;
        Element doc = handle.getCanvas().getDocument().getDocumentElement();
        NodeList nList = doc.getElementsByTagName("path");
        String strid = null;
        for (int i = 0; i < nList.getLength(); i++) {
            element = (Element) nList.item(i);
            strid = element.getAttribute("id");
            if (strid.equals(strLineID)) {
                return element;
            }
        }
        return null;

    }

    /**
     * 根据输入的线id和图元连接点名称，把该线关联从图元节点中删除
     * 
     * @param element：图元节点
     * @param strLineID：线id
     * @param strPointID：图元连接点名称
     */
    private void removeLineFromSymbol(Element element, String strLineID,
            String strPointID) {
        NodeList ncipoints = getNciPointList(element);
        if (ncipoints == null)
            return;

        for (int i = 0; i < ncipoints.getLength(); i++) {
            Element mdelement = (Element) ncipoints.item(i);
            if (mdelement.getAttribute("name").equalsIgnoreCase(strPointID)) {
                String strTemp = mdelement.getAttribute(lineCount);

                if (strTemp == null || strTemp.length() == 0)
                    return;
                int nLineCount = new Integer(strTemp).intValue();
                if (nLineCount == 0)
                    return;

                String strLineInfo = null;
                for (int j = 0; j < nLineCount; j++) {
                    strTemp = String.format("line%d", j);
                    strLineInfo = mdelement.getAttribute(strTemp);
                    if (strLineInfo.equals(strLineID)) {
                        if (j == nLineCount - 1)
                            mdelement.removeAttribute(strTemp);
                        else {
                            String strTmp = String.format("line%d",
                                    nLineCount - 1);
                            mdelement.setAttribute(strTemp, mdelement
                                    .getAttribute(strTmp));
                            mdelement.removeAttribute(strTmp);
                        }
                        break;
                    }
                }
                nLineCount--;
                strTemp = new Integer(nLineCount).toString();
                mdelement.setAttribute(lineCount, strTemp);
            }
        }
        return;
    }

    /**
     * 根据输入的线连接点名称，把该关联从线节点中删除
     * 
     * @param element：线节点
     * @param strPointID：线连接点名称
     */
    private void removeSymbolFromLine(Element element, String strPointID) {
        element.setAttribute(strPointID, "");
        return;
    }

    /**
     * 根据输入的图元信息和线连接点名称，把该图元关联新增至线节点
     * 
     * @param element：线节点
     * @param strPointID：线连接点名称
     * @param strSymbolInfo：图元信息
     */
    public void addSymbolToLine(Element element, String strPointID,
            String strSymbolInfo) {
        element.setAttribute(strPointID, strSymbolInfo);
        return;
    }

    /**
     * 根据输入的线信息和图元连接点名称，把该线关联新增至图元节点
     * 
     * @param element：图元节点
     * @param strLineInfo：线信息
     * @param strPointID：图元连接点名称
     */
    private void addLineToSymbol(Element element, String strPointID,
            String strLineInfo) {

        NodeList ncipoints = getNciPointList(element);
        if (ncipoints == null)
            return;

        for (int i = 0; i < ncipoints.getLength(); i++) {
            Element mdelement = (Element) ncipoints.item(i);
            if (mdelement.getAttribute("name").equalsIgnoreCase(strPointID)) {
                String strTemp = mdelement.getAttribute(lineCount);

                if (strTemp == null || strTemp.length() == 0)
                    strTemp = "0";
                int nLineCount = new Integer(strTemp).intValue();

                strTemp = String.format("line%d", nLineCount);
                mdelement.setAttribute(strTemp, strLineInfo);

                nLineCount++;
                strTemp = new Integer(nLineCount).toString();
                mdelement.setAttribute(lineCount, strTemp);
            }
        }

        return;
    }

    /**
     * 根据输入的新坐标x，y和线连接点名称，修改线节点中相应的坐标
     * 
     * @param element：线节点
     * @param x:新连接点横坐标
     * @param y：新连接点纵坐标
     * @param strPointID：线连接点名称
     */
    private void modifyLineBySymbol(Element element, int x, int y,
            String strPointID) {

        element.getNodeName();
        String sPath = element.getAttribute(dAtt);
        String oPath = sPath;
        Path path = new Path(sPath);
        int nCount = path.getSegmentsNumber();
        Segment se = path.getSegment();
        if (strPointID.equals(lP0)) {

            StringBuffer buffer = new StringBuffer();
            sPath = String.format("M%d %d", x, y);
            buffer.append(sPath);
            for (int i = 0; i < nCount - 1; i++) {
                if (se == null)
                    break;
                se = se.getNextSegment();
                sPath = String.format("L%d %d", (int) se.getEndPoint().getX(),
                        (int) se.getEndPoint().getY());
                buffer.append(sPath);
            }
            sPath = buffer.toString();
        } else {
            StringBuffer buffer = new StringBuffer();
            sPath = String.format("M%d %d", (int) se.getEndPoint().getX(),
                    (int) se.getEndPoint().getY());
            buffer.append(sPath);
            for (int i = 0; i < nCount - 2; i++) {
                if (se == null)
                    break;
                se = se.getNextSegment();
                sPath = String.format("L%d %d", (int) se.getEndPoint().getX(),
                        (int) se.getEndPoint().getY());
                buffer.append(sPath);
            }
            sPath = String.format("L%d %d", x, y);
            buffer.append(sPath);
            sPath = buffer.toString();
        }

        element.setAttribute(dAtt, sPath);
        NciTextPathModule module = new NciTextPathModule(svgHandle.getEditor());
        module.correctTextPos(svgHandle, element, oPath, sPath);
        return;
    }

    public NodeList getNciPointList(Element element) {
        Element terminal = (Element) element.getElementsByTagName("terminal")
                .item(0);

        if (terminal == null)
            return null;
        return terminal.getElementsByTagName("nci:POINT");
    }

}
