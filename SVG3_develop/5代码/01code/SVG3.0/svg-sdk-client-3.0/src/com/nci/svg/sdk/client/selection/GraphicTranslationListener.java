package com.nci.svg.sdk.client.selection;

import java.awt.geom.Point2D;
import java.util.Set;

import org.w3c.dom.Element;

public interface GraphicTranslationListener {
	
	public void graphicsTranslated(Set<Element> elementsTraslated,Point2D firstPoint, Point2D scaledPoint);

	public void graphicsTranslating(Set<Element> elementsTraslated,Point2D firstPoint, Point2D scaledPoint);
	
}
