package com.nci.svg.sdk.shape;

import java.awt.Shape;

import org.w3c.dom.Element;

/**
 * ͼԪ�Զ�����
 * @author qi
 *
 */
public interface ShapeAutoAjuster {
	
	public void autoAjustWhenCreated(Element useEle);
	
	public void autoAjustWhenResized(Element useEle,Shape oldShape, Shape newShape);

}
