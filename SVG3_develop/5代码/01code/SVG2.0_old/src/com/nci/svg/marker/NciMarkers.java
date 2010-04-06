package com.nci.svg.marker;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class NciMarkers {
    public void appendMarkers(Document doc) {
        
    }
    /**��ȡdefs����ڵ�
     * @param doc:�������doc
     * @return:�����ڵ�
     */
    protected Element getDefsElement(Document doc) {
        Element defsElement = (Element) doc.getDocumentElement()
                .getElementsByTagName("defs").item(0);
        if (defsElement == null) {
            defsElement = doc.createElementNS(doc.getNamespaceURI(), "defs");
            doc.getDocumentElement().appendChild(defsElement);
        }
        return defsElement;
    }
}
