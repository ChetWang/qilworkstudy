package com.nci.svg.sdk.shape;

import java.util.List;

import org.w3c.dom.Element;

/**
 * 过滤这种类型的Element是否允许删除
 * @author qi
 *
 */
public interface ElementDeleteFilter {
	public boolean filterElement(Element shapeElement,List<Element> elementsToDelete );
}
