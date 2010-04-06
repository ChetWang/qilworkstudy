/**
 * 类名：com.nci.svg.properties.SVGPropertiesMetadataWidget
 * 创建人:yx.nci
 * 创建日期：2008-7-30
 * 类作用:metadata数据域设置类
 * 修改日志：
 */
package com.nci.svg.properties;

import fr.itris.glips.svgeditor.properties.SVGPropertiesWidget;
import fr.itris.glips.svgeditor.properties.SVGPropertyItem;

/**
 * @author yx.nci
 *
 */
public class SVGPropertiesMetadataWidget extends SVGPropertiesWidget {
    public SVGPropertiesMetadataWidget(SVGPropertyItem propertyItem) {

        super(propertyItem);
        
        buildComponent();
    }
    
    protected void buildComponent(){
    
    }
}
