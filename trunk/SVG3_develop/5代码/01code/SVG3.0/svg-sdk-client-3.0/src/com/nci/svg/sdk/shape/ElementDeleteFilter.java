package com.nci.svg.sdk.shape;

import java.util.List;

import org.w3c.dom.Element;

/**
 * �����������͵�Element�Ƿ�����ɾ��
 * @author qi
 *
 */
public interface ElementDeleteFilter {
	public boolean filterElement(Element shapeElement,List<Element> elementsToDelete );
}
