package com.nci.svg.sdk.shape;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.apache.batik.ext.awt.geom.ExtendedGeneralPath;

public class DrawToolkit {
	public static void reset(ExtendedGeneralPath path)
	{
		path.reset();
	}
	
	public static void drawCircle(ExtendedGeneralPath aPath, Point2D point,double rx) {

		aPath.append(new Ellipse2D.Double(point.getX() - rx, point.getY() - rx,
				2 * rx, 2 * rx), false);
	}

	public static void drawRect(ExtendedGeneralPath path, Point2D point, double width,double height)
	{
		path.append(new Rectangle2D.Double(point.getX() - (width/2),point.getY() - (height/2),
				width,height), false);
	}
	
	public static void drawEllipse(ExtendedGeneralPath aPath, Point2D point,double rx,double ry) {

		aPath.append(new Ellipse2D.Double(point.getX() - rx, point.getY() - ry,
				2 * rx, 2 * ry), false);
	}
}
