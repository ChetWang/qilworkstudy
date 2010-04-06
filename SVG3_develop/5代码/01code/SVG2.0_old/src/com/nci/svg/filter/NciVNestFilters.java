package com.nci.svg.filter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class NciVNestFilters extends NciFilters {

    /* (non-Javadoc)
     * @see com.nci.svg.filter.NciFilters#appendFilters(org.w3c.dom.Document)
     */
    @Override
    public void appendFilters(Document doc) {
        Element defsElement = getDefsElement(doc);
        
        Element gElement = doc.createElement("g");
        defsElement.appendChild(gElement);
        gElement.setAttribute("id", "nci_filters");
        appendFeComponentTransferFilter(doc,gElement,"KV500_Image","discrete","0 1","0 0.57","0 0");
        appendFeComponentTransferFilter(doc,gElement,"KV330_Image","discrete","0 1","0 1","0 0.39");
        appendFeComponentTransferFilter(doc,gElement,"KV220_Image","discrete","0 0.89","0 0","0 0.89");
        appendFeComponentTransferFilter(doc,gElement,"KV110_Image","discrete","0 1","0 0","0 0");
        appendFeComponentTransferFilter(doc,gElement,"KV35_Image","discrete","0 1","0 1","0 0");
        appendFeComponentTransferFilter(doc,gElement,"KV10_Image","discrete","0 0.38","0 0.496","0 0.898");
        appendFeComponentTransferFilter(doc,gElement,"KV6_Image","discrete","0 0.707","0 0","0 0");
        appendFeComponentTransferFilter(doc,gElement,"KVÖÐÐÔµã_Image","discrete","0 0.433","0 0","0 0");
        appendFeComponentTransferFilter(doc,gElement,"V380_Image","discrete","0 1","0 0.57","0 0");
        appendFeComponentTransferFilter(doc,gElement,"KV18_Image","discrete","0 0","0 0.668","0 0");
        appendFeComponentTransferFilter(doc,gElement,"KV20_Image","discrete","0 0.707","0 1","0 0.707");
        
        return;
    }
    
    protected void appendFeComponentTransferFilter(Document doc,Element element,String strID,String type,String valuesR,
            String valuesG,String valuesB)
    {
        String[] types = {type,type,type};
        String[] values = {valuesR,valuesG,valuesB};
        element.appendChild(appendFeComponentTransferFilter(doc,strID,types,values));
        return;
    }
    
    protected Element appendFeComponentTransferFilter(Document doc,String strID,String[] types,String[] values)
    {
        Element filterElement = doc.createElementNS(doc.getDocumentElement().getNamespaceURI(), "filter");
        filterElement.setAttribute("id", strID);
        filterElement.setAttribute("filterUnits", "userSpaceOnUse");
//        filterElement.setAttributeNS(doc.getNamespaceURI(), "xlink:type", "simple");
//        filterElement.setAttributeNS(doc.getNamespaceURI(), "xlink:actuate", "onLoad");
//        filterElement.setAttributeNS(doc.getNamespaceURI(), "xlink:show", "other");
//        filterElement.setAttributeNS(doc.getNamespaceURI(), "xmlns:xlink", "http://www.w3.org/1999/xlink");
        Element transfer = doc.createElementNS(doc.getDocumentElement().getNamespaceURI(), "feComponentTransfer"); 
        filterElement.appendChild(transfer);
        Element element = doc.createElementNS(doc.getDocumentElement().getNamespaceURI(), "feFuncR");
        element.setAttribute("type", types[0]);
        element.setAttribute("tableValues", values[0]);
        transfer.appendChild(element);
        element = doc.createElementNS(doc.getDocumentElement().getNamespaceURI(), "feFuncG");
        element.setAttribute("type", types[1]);
        element.setAttribute("tableValues", values[1]);
        transfer.appendChild(element);
        element = doc.createElementNS(doc.getDocumentElement().getNamespaceURI(), "feFuncB");
        element.setAttribute("type", types[2]);
        element.setAttribute("tableValues", values[2]);
        transfer.appendChild(element);
        String localName = filterElement.getLocalName();
        String namespaceURI = filterElement.getNamespaceURI();
        return filterElement;   
    }

}
