package com.nci.svg.sdk.shape;

import java.awt.Shape;

import org.w3c.dom.Element;

/**
 * 图元自动调整
 * @author qi
 *
 */
public interface ShapeAutoAjuster {
	
	public void autoAjustWhenCreated(Element useEle);
	
	public void autoAjustWhenResized(Element useEle,Shape oldShape, Shape newShape);

}
