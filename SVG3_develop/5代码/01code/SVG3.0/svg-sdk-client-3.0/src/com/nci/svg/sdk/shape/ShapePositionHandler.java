package com.nci.svg.sdk.shape;

import java.util.Set;

import org.w3c.dom.Element;

/**
 * 新生成的元素的位置适配器，同过此接口可以定义新生成的元素在SVG中的位置，而不是一味的在文档末逐个添加
 * 
 * @author qi
 * 
 */
public interface ShapePositionHandler {

	public void handlePosition(Set<Element> createdElements,
			Element parentElement);
}
