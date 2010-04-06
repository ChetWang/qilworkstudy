/**
 * ������com.nci.svg.filter
 * ������:yx.nci
 * �������ڣ�2008-7-28
 * ������:TODO
 * �޸���־��
 */
package com.nci.svg.filter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author yx.nci
 *
 */
public class NciFilters {
    public void appendFilters(Document doc)
    {
        
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
