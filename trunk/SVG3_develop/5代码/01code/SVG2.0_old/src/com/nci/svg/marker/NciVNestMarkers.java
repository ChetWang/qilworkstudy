package com.nci.svg.marker;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class NciVNestMarkers extends NciMarkers {

    /* (non-Javadoc)
     * @see com.nci.svg.marker.NciMarkers#appendMarkers(org.w3c.dom.Document)
     */
    @Override
    public void appendMarkers(Document doc) {
        Element defsElement = getDefsElement(doc);
        
        //marker不能使用g组合方式包含
        defsElement.appendChild(appendBeginMarker(doc));
        defsElement.appendChild(appendEndMarker(doc));
        return;
    }
    
    protected Element appendBeginMarker(Document doc)
    {
        Element markerElement = appendMarker(doc);
        markerElement.setAttribute("id", "beginMarker");
        markerElement.setAttribute("refX", "0");
        markerElement.setAttribute("refY", "10");
        Element pathElement = doc.createElementNS(doc.getDocumentElement().getNamespaceURI(), "path");
        pathElement.setAttribute("d", "M 20 0 L 00 10 L 20 20 z");
        pathElement.setAttribute("fill", "purple");
        pathElement.setAttribute("stroke", "black");
        markerElement.appendChild(pathElement);
        return markerElement;
    }
    
    protected Element appendEndMarker(Document doc)
    {
        Element markerElement = appendMarker(doc);
        markerElement.setAttribute("id", "endMarker");
        markerElement.setAttribute("refX", "20");
        markerElement.setAttribute("refY", "10");
        Element pathElement = doc.createElementNS(doc.getDocumentElement().getNamespaceURI(), "path");
        pathElement.setAttribute("d", "M 0 0 L 20 10 L 0 20 z");
        pathElement.setAttribute("fill", "purple");
        pathElement.setAttribute("stroke", "black");
        markerElement.appendChild(pathElement);
        return markerElement;
    }
    
    protected Element appendMarker(Document doc)
    {
        Element markerElement = doc.createElementNS(doc.getDocumentElement().getNamespaceURI(), "marker");
        markerElement.setAttribute("viewBox", "0 0 20 20");
        markerElement.setAttribute("markerUnits", "strokeWidth");
        markerElement.setAttribute("markerWidth", "10");
        markerElement.setAttribute("markerHeight", "30");
        markerElement.setAttribute("orient", "auto");
        return markerElement;
    }

}
