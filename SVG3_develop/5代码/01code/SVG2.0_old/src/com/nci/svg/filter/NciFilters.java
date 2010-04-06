/**
 * 类名：com.nci.svg.filter
 * 创建人:yx.nci
 * 创建日期：2008-7-28
 * 类作用:TODO
 * 修改日志：
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
