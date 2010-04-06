package com.nci.svg.marker;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class NciMarkers {
    public void appendMarkers(Document doc) {
        
    }
    /**获取defs定义节点
     * @param doc:待加入的doc
     * @return:新增节点
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
