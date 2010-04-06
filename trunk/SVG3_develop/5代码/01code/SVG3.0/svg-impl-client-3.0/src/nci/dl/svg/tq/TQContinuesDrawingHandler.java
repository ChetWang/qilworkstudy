package nci.dl.svg.tq;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.apache.batik.ext.awt.geom.ExtendedGeneralPath;
import org.w3c.dom.Element;

import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.display.selection.SelectionItem;
import fr.itris.glips.svgeditor.shape.AbstractShape;
import fr.itris.glips.svgeditor.shape.path.ActionPoint;
import fr.itris.glips.svgeditor.shape.path.DrawingHandler;
import fr.itris.glips.svgeditor.shape.path.PathToConnect;
import fr.itris.glips.svgeditor.shape.path.segments.MoveToSeg;
import fr.itris.glips.svgeditor.shape.path.segments.QuadToSeg;

public class TQContinuesDrawingHandler extends DrawingHandler {

	/**
	 * 每个线路片段的起始点
	 */
	private Point2D segBeginPoint = null;

	private double DEFAULT_OFFSET = 80;
	/**
	 * add by yux,2009-2-24 图元间偏移量
	 */
	private double curOffset = 80;

	/**
	 * add by yux,2009-2-24 起始点绘制标记，true表示需要绘制
	 */
	private boolean bStart = true;
	/**
	 * add by yux,2009-2-24 图元轨迹，Rect为矩形，Circle为圆形
	 */
	// private String pathType = "Circle";
	/**
	 * add by yux,2009-2-24 折点补齐标记，如为true则在折点也设置图元
	 */
	private boolean bPatch = false;
	/**
	 * add by yux,2009-2-24 过渡变量，标注当前上一段线路的最后点
	 */
	private Point2D lastPoint = null;

	private Point2D curPoint = null;

	/**
	 * 计算片段时往上取整
	 */
	private boolean upperContinue = false;

	private PathTypeEnum pathType;

	/**
	 * 绘制的圆的半径
	 */
	private static final int CIRCLE_RADIUS = 10;

	/**
	 * 绘制的矩形的宽度
	 */
	private static final int RECT_WIDTH = 10;
	/**
	 * 绘制的矩形的高度
	 */
	private static final int RECT_HEIGHT = 10;

	public enum PathTypeEnum {
		Line, Circle, Rect
	}

	public TQContinuesDrawingHandler(TQShape tqShape) {
		super(tqShape);
	}

	/**
	 * 简单直线的绘制
	 * 
	 * @param handle
	 * @param point
	 */
	public void simpleMouseMoved(SVGHandle handle, Point2D point) {
		super.mouseMoved(handle, point);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.itris.glips.svgeditor.shape.path.DrawingHandler#mouseMoved(fr.itris.glips.svgeditor.display.handle.SVGHandle,
	 *      java.awt.geom.Point2D)
	 */
	@Override
	public void mouseMoved(SVGHandle handle, Point2D point) {
		// initializing the drawing shape

		initializeDrawingPath();

		// removing the last point of the points list of its a dragged action
		// point
		if (points.size() > 0
				&& points.getLast().getActionType() == AbstractShape.DRAWING_MOUSE_MOVED) {

			points.removeLast();
		}

		if (points.size() == 0 && segments.size() > 0) {

			if (pathShapeModule.getEditor().getConstraintLinesModeManager()
					.constraintLines()) {

				// computing the point so that the line is constrained
				point = computeLinePointWhenLineConstraintModeActive(segments
						.getLast().getEndPoint(), point);
			}

			// the action is "cubicTo" step 1/6 or lineTo step 1/3,
			points
					.add(new ActionPoint(AbstractShape.DRAWING_MOUSE_MOVED,
							point));

			// drawing the line
			drawPath(handle, point, curOffset);
			curPoint = point;

		} else if (points.size() == 1
				&& points.getLast().getActionType() == AbstractShape.DRAWING_MOUSE_RELEASED) {

			// the action is "quadTo", step 3/4
			points
					.add(new ActionPoint(AbstractShape.DRAWING_MOUSE_MOVED,
							point));

			drawQuadToSegment();
		}

		// painting the shape
		paintShape(handle);
	}

	public double getDistance(Point2D beginPoint, Point2D endPoint) {
		double distance = Math.sqrt((beginPoint.getX() - endPoint.getX())
				* (beginPoint.getX() - endPoint.getX())
				+ (beginPoint.getY() - endPoint.getY())
				* (beginPoint.getY() - endPoint.getY()));
		return distance;
	}

	public double getCurrentOffset(double distance, int pos) {
		double offset = distance / pos;
		return offset;
	}

	public int getSize(double distance) {
		int size = (int) Math.round(distance / curOffset);
		return size;
	}

	public void drawPath(SVGHandle handle, Point2D endPoint, double offset) {
		Point2D beginPoint = segBeginPoint;
		double scale = handle.getCanvas().getZoomManager().getCurrentScale();
		double scaleOffset = offset * scale;
		drawnPath.reset();
		// 计算两点间距离
		double distance = Math.sqrt((beginPoint.getX() - endPoint.getX())
				* (beginPoint.getX() - endPoint.getX())
				+ (beginPoint.getY() - endPoint.getY())
				* (beginPoint.getY() - endPoint.getY()));

		long size = Math.round(distance / scaleOffset);
		if (upperContinue)
			size++;
		if (size < 0) {
			return;
		}
		if (bStart) {
			drawnPath.moveTo((float) beginPoint.getX(), (float) beginPoint
					.getY());
			if (pathType.equals(PathTypeEnum.Circle)) {
				drawCircle(drawnPath, beginPoint, CIRCLE_RADIUS);
				// drawCircle(wholePath, beginPoint, 10);
			} else if (pathType.equals(PathTypeEnum.Rect)) {
				drawRect(drawnPath, beginPoint, RECT_WIDTH, RECT_HEIGHT);
			}
			drawnPath.moveTo((float) beginPoint.getX(), (float) beginPoint
					.getY());
		}
		for (int i = 1; i < size; i++) {
			Point2D point = getPoint(beginPoint, endPoint, distance, i,
					scaleOffset);
			drawnPath.lineTo((float) point.getX(), (float) point.getY());

			if (pathType.equals(PathTypeEnum.Circle)) {
				drawCircle(drawnPath, point, CIRCLE_RADIUS);
				// drawCircle(wholePath, beginPoint, 10);
			} else if (pathType.equals(PathTypeEnum.Rect)) {
				drawRect(drawnPath, point, RECT_WIDTH, RECT_HEIGHT);
			}
			drawnPath.moveTo((float) point.getX(), (float) point.getY());
			lastPoint = point;
		}
		curOffset = offset;
	}

	private Point2D getPoint(Point2D beginPoint, Point2D endPoint,
			double distance, int pos, double offset) {
		Point2D.Double point = new Point2D.Double();
		point.x = beginPoint.getX() + (endPoint.getX() - beginPoint.getX())
				* offset * pos / distance;
		point.y = beginPoint.getY() + (endPoint.getY() - beginPoint.getY())
				* offset * pos / distance;
		return point;
	}

	public void drawCircle(ExtendedGeneralPath path, Point2D point, int rx) {
		path.append(new Ellipse2D.Double(point.getX() - rx, point.getY() - rx,
				2 * rx, 2 * rx), false);
	}

	public void drawRect(ExtendedGeneralPath path, Point2D point, int width,
			int height) {
		path.append(new Rectangle2D.Double(point.getX() - (width / 2), point
				.getY()
				- (height / 2), width, height), false);
	}

	public void simpleMousePressed(SVGHandle handle, Point2D point) {
		super.mousePressed(handle, point);
	}

	public void mousePressed(SVGHandle handle, Point2D point) {

		if (segments.size() == 0 && points.size() == 0) {

			// handling the path to connect//
			// getting the selection item corresponding to the point
			SelectionItem item = handle.getSelection().getSelectionItem(point);

			if (item != null && item.getElements().size() == 1) {

				// getting the point
				point = item.getPoint();

				// getting the element
				Element element = item.getElements().iterator().next();

				if (element != null
						&& pathShapeModule.isElementTypeSupported(element)) {

					// creating the object handling the path to connect
					drawingPathToConnect = new PathToConnect(element, item
							.getIndex());

					if (!drawingPathToConnect.isCorrectPath()) {

						// the new path could not be connect to this path
						drawingPathToConnect = null;
					}
				}
			}

			// creating a moveTo segment, step 1/1
			segments.add(new MoveToSeg(point));
			wholePath.reset();
			wholePath.moveTo((float) point.getX(), (float) point.getY());

			// getting the base scaled point value
			Point2D scaledPoint = handle.getTransformsManager().getScaledPoint(
					point, true);
			// path.reset();
			// path.append(new
			// Ellipse2D.Double(scaledPoint.getX(),scaledPoint.getY(),10,10),
			// true);
			elementPath.moveTo((float) scaledPoint.getX(), (float) scaledPoint
					.getY());

			lineShapeDraged = false;
			segBeginPoint = point;

		} else if (points.size() == 1
				&& points.getLast().getActionType() == AbstractShape.DRAWING_MOUSE_MOVED) {

			if (pathShapeModule.getEditor().getConstraintLinesModeManager()
					.constraintLines()) {

				// computing the point so that the line is constrained
				point = computeLinePointWhenLineConstraintModeActive(segments
						.getLast().getEndPoint(), point);
			}

			// the action is "cubicTo" step 2/6 or lineTo step 2/3,
			points.clear();
			if (bPatch)
				segBeginPoint = point;
			else
				segBeginPoint = lastPoint;
			points.add(new ActionPoint(AbstractShape.DRAWING_MOUSE_PRESSED,
					segBeginPoint));

			// if (pathShapeModule.getCurrentAction().equals("LineShape")) {
			// mouseReleased(handle, point);
			// mouseDoubleClicked(handle, point);
			// pathShapeModule.resetDrawing();
			// }

		} else if (points.size() == 2
				&& points.getLast().getActionType() == AbstractShape.DRAWING_MOUSE_MOVED) {

			// the action is "quadTo" , step 4/4
			// creating the quadTo segment
			QuadToSeg seg = new QuadToSeg(points.getFirst().getActionPoint(),
					point);
			segments.add(seg);
			points.clear();

			// filling the path
			wholePath.quadTo((float) seg.getControlPoint().getX(), (float) seg
					.getControlPoint().getY(),
					(float) seg.getEndPoint().getX(), (float) seg.getEndPoint()
							.getY());

			// getting the base scaled points value
			Point2D scControlPoint = handle.getTransformsManager()
					.getScaledPoint(seg.getControlPoint(), true);
			Point2D scEndPoint = handle.getTransformsManager().getScaledPoint(
					seg.getEndPoint(), true);
			elementPath.quadTo((float) scControlPoint.getX(),
					(float) scControlPoint.getY(), (float) scEndPoint.getX(),
					(float) scEndPoint.getY());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.itris.glips.svgeditor.shape.path.DrawingHandler#mouseWheelDown(fr.itris.glips.svgeditor.display.handle.SVGHandle)
	 */
	@Override
	public void mouseWheelDown(SVGHandle handle, Point2D point) {
		curOffset++;
		drawPath(handle, curPoint, curOffset);
		paintShape(handle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.itris.glips.svgeditor.shape.path.DrawingHandler#mouseWheelUp(fr.itris.glips.svgeditor.display.handle.SVGHandle)
	 */
	@Override
	public void mouseWheelUp(SVGHandle handle, Point2D point) {
		curOffset--;
		if (curOffset < 5)
			curOffset = 5;
		drawPath(handle, curPoint, curOffset);
		paintShape(handle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.itris.glips.svgeditor.shape.path.DrawingHandler#getOffset()
	 */
	@Override
	public double getCurrentOffset() {
		return curOffset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.itris.glips.svgeditor.shape.path.DrawingHandler#setOffset()
	 */
	@Override
	public void setDefaultOffset() {
		curOffset = DEFAULT_OFFSET;
	}

	public void setCurrentOffset(double offSet) {
		curOffset = offSet;
	}

	public PathTypeEnum getPathType() {
		return pathType;
	}

	public void setPathType(PathTypeEnum pathType) {
		this.pathType = pathType;
	}

	public Point2D getSegBeginPoint() {
		return segBeginPoint;
	}

	public void setSegBeginPoint(Point2D segBeginPoint) {
		this.segBeginPoint = segBeginPoint;
	}

	public boolean isUpperContinue() {
		return upperContinue;
	}

	public void setUpperContinue(boolean upperContinue) {
		this.upperContinue = upperContinue;
	}

}
