package com.nci.svg.sdk.shape.vhpath;

import java.awt.Shape;
import java.awt.geom.Point2D;
import java.util.UUID;

import org.apache.batik.ext.awt.geom.ExtendedGeneralPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.client.util.EditorToolkit;
import com.nci.svg.sdk.graphunit.NCIEquipSymbolBean;
import com.nci.svg.sdk.other.LinkPointManager;

import fr.itris.glips.library.geom.path.Path;
import fr.itris.glips.library.geom.path.segment.Segment;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.display.selection.SelectionItem;
import fr.itris.glips.svgeditor.shape.AbstractShape;
import fr.itris.glips.svgeditor.shape.path.ActionPoint;
import fr.itris.glips.svgeditor.shape.path.DrawingHandler;
import fr.itris.glips.svgeditor.shape.path.segments.CubicToSeg;
import fr.itris.glips.svgeditor.shape.path.segments.LineToSeg;
import fr.itris.glips.svgeditor.shape.path.segments.MoveToSeg;
import fr.itris.glips.svgeditor.shape.path.segments.QuadToSeg;

public class VHDrawingHandler extends DrawingHandler {

	protected Point2D previousPoint = null;
	// 图元下面都固定的线距
	public static int USE_CAP = 50;

	public static final int CAP = 50;
	/**
	 * 在此范围内忽略的差距
	 */
	public static final int ignoreCap = 10;

	public VHDrawingHandler(VHPathShape pathShape) {
		super(pathShape);
	}

	public void mousePressed(SVGHandle handle, Point2D point) {

		/*
		 * 根据落点坐标信息，读取节点信息，分析是否与该图元连接点相符
		 */
		if (segments.size() == 0 && points.size() == 0) {

			// handling the path to connect//
			// getting the selection item corresponding to the point
			SelectionItem item = handle.getSelection().getSelectionItem(point);
			Point2D scaledPoint = handle.getTransformsManager().getScaledPoint(
					point, true);

			if (item != null && item.getElements().size() == 1) {

				// getting the point
				point = item.getPoint();

				// getting the element
				// Element element = item.getElements().iterator().next();

				// if (element != null
				// && pathShapeModule.isElementTypeSupported(element)) {
				//
				// // creating the object handling the path to connect
				// drawingPathToConnect = new PathToConnect(element, item
				// .getIndex());
				//
				// if (!drawingPathToConnect.isCorrectPath()) {
				//
				// // the new path could not be connect to this path
				// drawingPathToConnect = null;
				// }
				// }
			}
			// Element e = handle.getSvgElementsManager().getNodeAt(
			// handle.getCanvas().getDocument().getDocumentElement(),
			// scaledPoint, handle.getSelection().getSelectionFilter(),
			// handle.getSelection().getGroupBreaker());
			// Utilities.printNode(e, true);
			// creating a moveTo segment, step 1/1
			segments.add(new MoveToSeg(point));
			wholePath.reset();
			wholePath.moveTo((float) point.getX(), (float) point.getY());

			// getting the base scaled point value

			elementPath.reset();
			elementPath.moveTo((float) scaledPoint.getX(), (float) scaledPoint
					.getY());
			lineShapeDraged = false;

			if (connectionFlag) {
				Element element = handle.getSvgElementsManager().getNodeAt(
						handle.getCanvas().getDocument().getDocumentElement(),
						handle.getTransformsManager().getScaledPoint(point,
								true), "");
				String name = handle.getSelection().getConnectName(element,
						scaledPoint.getX(), scaledPoint.getY());
				if (name != null) {
					lineID = UUID.randomUUID().toString();
					beginData = handle.getCanvas().getLpManager()
							.createLineData(lineID, element.getAttribute("id"),
									name, LinkPointManager.BEGIN_LINE_POINT);
				}

			}

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
			// points.add(new ActionPoint(AbstractShape.DRAWING_MOUSE_PRESSED,
			// point));
			points.add(new ActionPoint(AbstractShape.DRAWING_MOUSE_PRESSED,
					previousPoint));
			if (pathShapeModule.getCurrentAction().equals("LineShape")) {
				mouseReleased(handle, point);
				mouseDoubleClicked(handle, point);
				pathShapeModule.resetDrawing();
			}

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

	public void mouseReleased(SVGHandle handle, Point2D point) {

		if (pathShapeModule.getCurrentAction().equals("LineShape")
				&& lineShapeDraged) {
			if (pathShapeModule.getEditor().getConstraintLinesModeManager()
					.constraintLines()) {

				// computing the point so that the line is constrained
				point = computeLinePointWhenLineConstraintModeActive(segments
						.getLast().getEndPoint(), point);
			}

			// the action is "cubicTo" step 2/6 or lineTo step 2/3,
			points.clear();
			points.add(new ActionPoint(AbstractShape.DRAWING_MOUSE_PRESSED,
					point));
		}

		if (points.size() == 1
				&& points.getLast().getActionType() == AbstractShape.DRAWING_MOUSE_DRAGGED) {

			// the action is "quadTo" , step 2/4
			points.clear();
			points.add(new ActionPoint(AbstractShape.DRAWING_MOUSE_RELEASED,
					point));

		} else if (points.size() == 1
				&& points.getLast().getActionType() == AbstractShape.DRAWING_MOUSE_PRESSED) {

			// the action is "lineTo" , step 3/3
			LineToSeg seg = new LineToSeg(points.getFirst().getActionPoint());
			segments.add(seg);
			points.clear();

			// drawing the line
			wholePath.lineTo((float) seg.getEndPoint().getX(), (float) seg
					.getEndPoint().getY());

			// getting the base scaled points value
			Point2D scEndPoint = handle.getTransformsManager().getScaledPoint(
					seg.getEndPoint(), true);
			elementPath.lineTo((float) scEndPoint.getX(), (float) scEndPoint
					.getY());

		} else if (points.size() == 2
				&& points.getLast().getActionType() == AbstractShape.DRAWING_MOUSE_DRAGGED) {

			// the action is "cubicTo", step 4/4
			points.removeLast();

			// storing the current point
			points.add(new ActionPoint(AbstractShape.DRAWING_MOUSE_RELEASED,
					point));

			// computing the points of the segment
			Point2D endPoint = getEndPoint();
			Point2D pt1, pt2, pt3;

			pt1 = points.get(0).getActionPoint();

			if (segments.getLast() instanceof CubicToSeg
					|| segments.getLast() instanceof QuadToSeg) {

				pt2 = Segment.computeSymetric(segments.getLast()
						.getControlPoint(), segments.getLast().getEndPoint());
				pt3 = points.get(1).getActionPoint();

			} else {

				pt2 = points.get(1).getActionPoint();
				pt3 = Segment.computeSymetricRelCenter(pt2, endPoint, pt1);
			}

			// creating the cubicTo segment
			CubicToSeg seg = new CubicToSeg(pt2, pt3, pt1);
			segments.add(seg);
			points.clear();

			// adding the segment to the path
			wholePath.curveTo((float) pt2.getX(), (float) pt2.getY(),
					(float) pt3.getX(), (float) pt3.getY(), (float) pt1.getX(),
					(float) pt1.getY());

			// getting the base scaled points value
			Point2D scControlPoint1 = handle.getTransformsManager()
					.getScaledPoint(seg.getFirstControlPoint(), true);
			Point2D scControlPoint2 = handle.getTransformsManager()
					.getScaledPoint(seg.getControlPoint(), true);
			Point2D scEndPoint = handle.getTransformsManager().getScaledPoint(
					seg.getEndPoint(), true);
			elementPath.curveTo((float) scControlPoint1.getX(),
					(float) scControlPoint1.getY(), (float) scControlPoint2
							.getX(), (float) scControlPoint2.getY(),
					(float) scEndPoint.getX(), (float) scEndPoint.getY());
		}

		if (pathShapeModule.getCurrentAction().equals("LineShape")
				&& lineShapeDraged) {
			mouseDoubleClicked(handle, point);
			pathShapeModule.resetDrawing();
		}
	}

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
		/*
		 * 根据移动点坐标分析，如果移动至图元上方，则显示图元的连接点图标位置
		 */
		if (isLinkFlag()) {
			Element element = handle.getSvgElementsManager().getNodeAt(
					handle.getCanvas().getDocument().getDocumentElement(),
					handle.getTransformsManager().getScaledPoint(point, true),
					"");
			if (element != null && element.getNodeName().equals("use")) {
				// 图元，则读取图元信息
				Point2D pp = handle.getTransformsManager().getScaledPoint(
						point, true);
				handle.getSelection().showTerminal(element);
				connectionFlag = handle.getSelection().showConnectNode(element,
						pp.getX(), pp.getY());
			} else if (element != null
					&& element.getNodeName().equals("g")
					&& element.getAttribute("symbol_status") != null
					&& element.getAttribute("symbol_status").equals(
							NCIEquipSymbolBean.SYMBOL_TYPE_TEMPLATE)) {
				Point2D pp = handle.getTransformsManager().getScaledPoint(
						point, true);
				handle.getSelection().showTerminal(element);
				connectionFlag = handle.getSelection().showConnectNode(element,
						pp.getX(), pp.getY());
			} else {
				handle.getSelection().showTerminal(null);
				connectionFlag = false;
			}
		}

		if (points.size() == 0 && segments.size() > 0) {

			point = computeVHLinePoint(segments.getLast().getEndPoint(), point);
			previousPoint = point;
			// the action is "cubicTo" step 1/6 or lineTo step 1/3,
			points
					.add(new ActionPoint(AbstractShape.DRAWING_MOUSE_MOVED,
							point));

			// drawing the line
			drawnPath.lineTo((float) point.getX(), (float) point.getY());

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

	protected Point2D computeVHLinePoint(Point2D startPoint, Point2D basePoint) {

		Point2D.Double pt = new Point2D.Double();

		if (startPoint != null && basePoint != null) {

			pt.x = basePoint.getX();
			pt.y = basePoint.getY();

			Point2D.Double pt1 = new Point2D.Double(startPoint.getX(),
					startPoint.getY());
			Point2D.Double pt2 = new Point2D.Double(basePoint.getX(), basePoint
					.getY());

			// the norme
			double n = Math.sqrt(Math.pow((pt2.x - pt1.x), 2)
					+ Math.pow((pt2.y - pt1.y), 2));

			// the x-distance and the y-distance
			double xDistance = Math.abs(pt2.x - pt1.x), yDistance = Math
					.abs(pt2.y - pt1.y);

			// the angle
			double cosinus = (pt2.x - pt1.x) / n;

			// computing the new point
			if (pt1.x <= pt2.x && pt1.y >= pt2.y) {// 右上

				if (cosinus <= 1 && cosinus > Math.cos(Math.PI / 8)) {

					pt.x = (int) (pt1.x + xDistance);
					pt.y = pt1.y;

				} else if (cosinus <= Math.cos(Math.PI / 8)
						&& cosinus > Math.cos(3 * Math.PI / 8)) {
					pt.x = (int) (pt1.x + xDistance);
					pt.y = pt1.y;
					// pt.x = (int) (pt1.x + xDistance);
					// pt.y = (int) (pt1.y - xDistance);

				} else if (cosinus <= Math.cos(3 * Math.PI / 8) && cosinus > 0) {

					pt.x = pt1.x;
					pt.y = (int) (pt1.y - yDistance);
				}

			} else if (pt1.x > pt2.x && pt1.y >= pt2.y) {// 左上

				if (cosinus <= 0 && cosinus > Math.cos(5 * Math.PI / 8)) {

					pt.x = pt1.x;
					pt.y = (int) (pt1.y - yDistance);

				} else if (cosinus <= Math.cos(5 * Math.PI / 8)
						&& cosinus >= Math.cos(7 * Math.PI / 8)) {
					pt.x = pt1.x;
					pt.y = (int) (pt1.y - yDistance);
					// pt.x = (int) (pt1.x - xDistance);
					// pt.y = (int) (pt1.y - xDistance);

				} else if (cosinus <= Math.cos(7 * Math.PI / 8)
						&& cosinus >= -1) {

					pt.x = (int) (pt1.x - xDistance);
					pt.y = pt1.y;
				}

			} else if (pt1.x >= pt2.x && pt1.y < pt2.y) {// 左下

				if (cosinus >= -1 && cosinus < Math.cos(7 * Math.PI / 8)) {

					pt.x = (int) (pt1.x - xDistance);
					pt.y = pt1.y;

				} else if (cosinus >= Math.cos(7 * Math.PI / 8)
						&& cosinus < Math.cos(5 * Math.PI / 8)) {
					pt.x = (int) (pt1.x - xDistance);
					pt.y = pt1.y;
					// pt.x = (int) (pt1.x - xDistance);
					// pt.y = (int) (pt1.y + xDistance);

				} else if (cosinus >= Math.cos(5 * Math.PI / 8) && cosinus < 0) {

					pt.x = pt1.x;
					pt.y = (int) (pt1.y + yDistance);
				}

			} else if (pt1.x <= pt2.x && pt1.y <= pt2.y) {// 右下

				if (cosinus >= 0 && cosinus < Math.cos(3 * Math.PI / 8)) {

					pt.x = pt1.x;
					pt.y = (int) (pt1.y + yDistance);

				} else if (cosinus >= Math.cos(3 * Math.PI / 8)
						&& cosinus < Math.cos(Math.PI / 8)) {
					pt.x = pt1.x;
					pt.y = (int) (pt1.y + yDistance);
					// pt.x = (int) (pt1.x + xDistance);
					// pt.y = (int) (pt1.y + xDistance);

				} else if (cosinus >= Math.cos(Math.PI / 8) && cosinus < 1) {

					pt.x = (int) (pt1.x + xDistance);
					pt.y = pt1.y;
				}
			}
		}

		return pt;
	}

	public Element createPathElement(SVGHandle handle, Shape pathShape,
			String lineID) {
		// the edited document
		Document doc = handle.getScrollPane().getSVGCanvas().getDocument();

		// creating the path
		final Element gElement = doc.createElementNS(doc.getDocumentElement()
				.getNamespaceURI(), "g");
		gElement.setAttribute("model", VHPathShape.VH_MODEL);
		gElement.setAttribute(VHPathShape.GROUP_STYLE, VHPathShape.VH_STYPE);
		Path path = new Path((ExtendedGeneralPath) pathShape);
		Point2D beginPoint = null;
		Point2D endPoint = null;
		Segment seg = path.getSegment();
		endPoint = seg.getEndPoint();
		int segCounts = path.getSegmentsNumber();
		for (int i = 1; i <= segCounts; i++) {
			seg = seg.getNextSegment();
			if (seg == null)
				continue;
			beginPoint = endPoint;
			endPoint = seg.getEndPoint();
			if (EditorToolkit.equalPoint(beginPoint, endPoint))
				continue;
			StringBuffer pathString = new StringBuffer();
			pathString.append("M").append(beginPoint.getX()).append(" ")
					.append(beginPoint.getY()).append(" ").append("L").append(
							endPoint.getX()).append(" ")
					.append(endPoint.getY());
			Element createdPathElement = doc.createElementNS(doc
					.getDocumentElement().getNamespaceURI(), "path");

			// 设置线属性
			createdPathElement.setAttribute("d", pathString.toString());
			createdPathElement.setAttributeNS(null, "style",
					"fill:none;stroke:" + Constants.NCI_DEFAULT_STROKE_COLOR
							+ ";");
			// 生成线编号，并赋予节点
			String id = UUID.randomUUID().toString();
			if (i == 1)
				id = lineID;
			createdPathElement.setAttribute("id", id);
			if (i == segCounts - 1) {
				EditorToolkit.appendMarker(createdPathElement
						.getOwnerDocument());
				EditorToolkit.appendMarker(createdPathElement);
			}
			gElement.appendChild(createdPathElement);

		}
		pathShapeModule.insertShapeElement(handle, gElement);
		return gElement;
	}

	public void reset(SVGHandle handle) {
		super.reset(handle);
		previousPoint = null;
		USE_CAP = CAP;
	}
}
