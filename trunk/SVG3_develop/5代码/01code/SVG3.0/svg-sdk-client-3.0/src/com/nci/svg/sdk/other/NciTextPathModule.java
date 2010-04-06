package com.nci.svg.sdk.other;

import java.awt.geom.Point2D;

import org.apache.batik.dom.svg.SVGOMTextElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.util.EditorToolkit;

import fr.itris.glips.library.geom.path.Path;
import fr.itris.glips.library.geom.path.segment.Segment;
import fr.itris.glips.svgeditor.display.canvas.dom.SVGDOMNormalizer;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;

public class NciTextPathModule {
    private String strTextContent = null;
    private String strTextID = null;
    private EditorAdapter editor = null;
    private int nRowNum = 0;
    private boolean bCreateFlag = false;
    public NciTextPathModule(EditorAdapter editor)
    {
        this.editor = editor;
    }
    
    public void initElement(Element element)
    {
        setStrTextContent(element.getAttribute("nci-text"));
        setStrTextID(element.getAttribute("nci-textid"));
    }

    /**
     * @return the strTextContent
     */
    public String getStrTextContent() {
        return strTextContent;
    }
    /**
     * @param strTextContent the strTextContent to set
     */
    public void setStrTextContent(String strTextContent) {
        this.strTextContent = strTextContent;
    }
    /**
     * @return the strTextID
     */
    public String getStrTextID() {
        return strTextID;
    }
    /**
     * @param strTextID the strTextID to set
     */
    public void setStrTextID(String strTextID) {
        this.strTextID = strTextID;
    }
    
    public Point2D calOffset(int x,int y,String initdValue,String dValue)
    {
        double dx = 0,dy = 0;
        Path path = new Path(initdValue);
        Segment se = path.getSegment();
        Point2D bPoint1 = se.getEndPoint();
        se = se.getNextSegment();
        Point2D ePoint1 = se.getEndPoint();
        path = new Path(dValue);
        se = path.getSegment();
        Point2D bPoint2 = se.getEndPoint();
        se = se.getNextSegment();
        Point2D ePoint2 = se.getEndPoint();
        dx = x - (int)((bPoint1.getX() + ePoint1.getX())/2) + (int)((bPoint2.getX() + ePoint2.getX())/2);
        dy = y - (int)((bPoint1.getY() + ePoint1.getY())/2) + (int)((bPoint2.getY() + ePoint2.getY())/2);
        Point2D point = new Point2D.Double(dx,dy);
        return point;
    }
    public void correctTextPos(SVGHandle handle,Element pathElement,String initdValue,String dValue)
    {
        String strTextID = pathElement.getAttribute("nci-textid");
        Element textElement = handle.getCanvas().getMapInfo(strTextID);
        if(textElement == null)
            return;
        
        String x = textElement.getAttribute("x");
        String y = textElement.getAttribute("y");
        int textX = (int) Double.parseDouble(x);
        int textY = (int) Double.parseDouble(y);
        Point2D point =calOffset(textX,textY,initdValue,dValue); 
        EditorToolkit.setAttributeValue(textElement, "x", point.getX());
        EditorToolkit.setAttributeValue(textElement, "y", point.getY());
    }
    
    public Element createTextNode(Document doc,Element pathElement)
    {
        if(strTextContent == null || strTextContent.length() == 0)
            return null;
        String sPath = pathElement.getAttribute("d");
        Path path = new Path(sPath);
        Segment se = path.getSegment();
        Point2D bPoint = se.getEndPoint();
        se = se.getNextSegment();
        Point2D ePoint = se.getEndPoint();
        Element element =  doc.createElementNS(doc.getDocumentElement()
                .getNamespaceURI(), "text");
        EditorToolkit.setAttributeValue(element, "x", (int)((bPoint.getX() + ePoint.getX())/2));
        EditorToolkit.setAttributeValue(element, "y", (int)((bPoint.getY() + ePoint.getY())/2));
        String[] tspanString = strTextContent.split("\n");
        nRowNum = tspanString.length;
        if (tspanString.length > 1) {

            SVGOMTextElement textEle = (SVGOMTextElement) element;
            NodeList children = textEle.getChildNodes();
            for (int i = children.getLength(); i > 0; i--) {
                textEle.removeChild(children.item(i - 1));
            }
            String x = textEle.getAttribute("x");
            String y = textEle.getAttribute("y");
            int startY = (int) Double.parseDouble(y);

            for (int i = 0; i < tspanString.length; i++) {

                Element tspan = doc.createElementNS(
                        doc.getDocumentElement()
                                .getNamespaceURI(),
                        SVGDOMNormalizer.tspanTagName);
                tspan.setAttribute("x", x);
                tspan.setAttribute("y", String.valueOf(startY));
                Text textValue = doc
                        .createTextNode(tspanString[i]);
                tspan.appendChild(textValue);
                textEle.appendChild(tspan);
                startY = startY + 17;
            }

        } else if (tspanString.length <= 1) {
            SVGOMTextElement textEle = (SVGOMTextElement) element;
            NodeList children = textEle.getChildNodes();
            for (int i = children.getLength(); i > 0; i--) {
                textEle.removeChild(children.item(i - 1));
            }
            if (tspanString.length == 1) {
                Text textValue = doc
                        .createTextNode(tspanString[0]);
                textEle.appendChild(textValue);
            }
        }
        element.setAttribute("id", ComputeID.getTextID());
        element.setAttribute("nci-textpath", pathElement.getAttribute("id"));
        bCreateFlag = true;
        return element;
    }
    
    public void modiTextNode(Document doc,Element pathElement,Element textElement)
    {
        if(strTextContent == null || strTextContent.length() == 0)
        {
            pathElement.removeAttribute("nci-textid");
            doc.getDocumentElement().removeChild(textElement);
            return;
        }
        Element element =  textElement;
        String[] tspanString = strTextContent.split("\n");
        if (tspanString.length > 1) {

            SVGOMTextElement textEle = (SVGOMTextElement) element;
            NodeList children = textEle.getChildNodes();
            for (int i = children.getLength(); i > 0; i--) {
                textEle.removeChild(children.item(i - 1));
            }
            String x = textEle.getAttribute("x");
            String y = textEle.getAttribute("y");
            int startY = (int) Double.parseDouble(y);

            for (int i = 0; i < tspanString.length; i++) {

                Element tspan = doc.createElementNS(
                        doc.getDocumentElement()
                                .getNamespaceURI(),
                        SVGDOMNormalizer.tspanTagName);
                tspan.setAttribute("x", x);
                tspan.setAttribute("y", String.valueOf(startY));
                Text textValue = doc
                        .createTextNode(tspanString[i]);
                tspan.appendChild(textValue);
                textEle.appendChild(tspan);
                startY = startY + 17;
            }

        } else if (tspanString.length <= 1) {
            SVGOMTextElement textEle = (SVGOMTextElement) element;
            NodeList children = textEle.getChildNodes();
            for (int i = children.getLength(); i > 0; i--) {
                textEle.removeChild(children.item(i - 1));
            }
            if (tspanString.length == 1) {
                Text textValue = doc
                        .createTextNode(tspanString[0]);
                textEle.appendChild(textValue);
            }
        }
        return ;
    }
}
