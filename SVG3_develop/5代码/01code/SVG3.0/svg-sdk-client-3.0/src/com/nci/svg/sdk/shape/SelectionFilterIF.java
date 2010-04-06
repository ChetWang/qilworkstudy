package com.nci.svg.sdk.shape;

import org.w3c.dom.Element;

public interface SelectionFilterIF {

	/**
	 * 选择节点时，在位置重叠下，svg默认是选择xml结构中的最上层的元素，若需要选择非默认的节点，需要进行过滤判断。
	 * 
	 * @param shapeElement
	 *            被判断的节点
	 * @return true为符合条件，确定为选择的节点；false为不符合，会继续迭代判断
	 */
	public boolean filterElement(Element shapeElement);

}
