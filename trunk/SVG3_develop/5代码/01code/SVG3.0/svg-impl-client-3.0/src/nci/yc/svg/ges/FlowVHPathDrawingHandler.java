package nci.yc.svg.ges;

import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.w3c.dom.Element;

import com.nci.svg.sdk.client.util.EditorToolkit;
import com.nci.svg.sdk.client.util.Utilities;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.shape.vhpath.VHDrawingHandler;
import com.nci.svg.sdk.shape.vhpath.VHPathShape;

import fr.itris.glips.library.geom.path.Path;
import fr.itris.glips.library.geom.path.segment.Segment;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.display.selection.SelectionItem;
import fr.itris.glips.svgeditor.shape.AbstractShape;
import fr.itris.glips.svgeditor.shape.ShapeToolkit;
import fr.itris.glips.svgeditor.shape.path.ActionPoint;
import fr.itris.glips.svgeditor.shape.path.PathShape;
import fr.itris.glips.svgeditor.shape.path.segments.CubicToSeg;
import fr.itris.glips.svgeditor.shape.path.segments.LineToSeg;
import fr.itris.glips.svgeditor.shape.path.segments.MoveToSeg;
import fr.itris.glips.svgeditor.shape.path.segments.QuadToSeg;

public class FlowVHPathDrawingHandler extends VHDrawingHandler {

	/**
	 * 连接的起始图元Element
	 */
	protected Element firstConnEle;

	protected Rectangle2D firstConnEleRect = null;
	/**
	 * 连接的结束图元Element
	 */
	protected Element lastConnEle;

	protected boolean isPreviousClickFirstConnEleClicked = false;

	private boolean isFirstClick = false;

	private FlowGraphicsModule flowGraphicsModule;

	private Point2D clickedPoint;

	private List<Point2D> cornerPoints = new ArrayList<Point2D>();

	public FlowVHPathDrawingHandler(VHPathShape pathShape,
			FlowGraphicsModule flowGraphicsModule) {
		super(pathShape);
		this.flowGraphicsModule = flowGraphicsModule;
	}

	public void reset(SVGHandle handle) {
		super.reset(handle);
		firstConnEle = null;
		firstConnEleRect = null;
		lastConnEle = null;
		cornerPoints.clear();
		isPreviousClickFirstConnEleClicked = false;
		isFirstClick = false;
	}

	public void mousePressed(SVGHandle handle, Point2D point) {
		clickedPoint = point;
		/*
		 * 根据落点坐标信息，读取节点信息，分析是否与该图元连接点相符
		 */
		if (segments.size() == 0 && points.size() == 0) {

			isFirstClick = true;
			// handling the path to connect//
			// getting the selection item corresponding to the point
			SelectionItem item = handle.getSelection().getSelectionItem(point);
			Point2D scaledPoint = handle.getTransformsManager().getScaledPoint(
					point, true);

			if (item != null && item.getElements().size() == 1) {

				// getting the point
				point = item.getPoint();

			}
			if (firstConnEle == null)
				firstConnEle = handle.getSvgElementsManager().getNodeAt(
						handle.getCanvas().getDocument().getDocumentElement(),
						scaledPoint,
						handle.getSelection().getSelectionFilter(),
						handle.getSelection().getGroupBreaker());
			if (!EditorToolkit.isVHPathConnectable(firstConnEle)) {
				firstConnEle = null;
			}
			if (firstConnEle != null) {
				scaledPoint = EditorToolkit.getCenterPoint(firstConnEle);
				point = handle.getTransformsManager().getScaledPoint(
						scaledPoint, false);
				clickedPoint = point;
				isPreviousClickFirstConnEleClicked = true;
			}
			// Utilities.printNode(e, true);
			// creating a moveTo segment, step 1/1
			segments.add(new MoveToSeg(point));
			wholePath.reset();
			wholePath.moveTo((float) point.getX(), (float) point.getY());

			// getting the base scaled point value

			// elementPath.reset();
			// elementPath.moveTo((float) scaledPoint.getX(), (float)
			// scaledPoint
			// .getY());
			lineShapeDraged = false;

		} else if (points.size() == 1
				&& points.getLast().getActionType() == AbstractShape.DRAWING_MOUSE_MOVED) {

			if (pathShapeModule.getEditor().getConstraintLinesModeManager()
					.constraintLines()) {

				// computing the point so that the line is constrained
				point = computeLinePointWhenLineConstraintModeActive(segments
						.getLast().getEndPoint(), point);
			}
			isPreviousClickFirstConnEleClicked = false;
			isFirstClick = false;
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
			isPreviousClickFirstConnEleClicked = false;
			isFirstClick = false;
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

	/**
	 * notifies that the mouse has been double clicked
	 * 
	 * @param handle
	 *            a svg handle
	 * @param point
	 *            the where action occured
	 */
	public void mouseDoubleClicked(SVGHandle handle, Point2D point) {
		Point2D scaledPoint = handle.getTransformsManager().getScaledPoint(
				point, true);
		flowGraphicsModule.editTextClick = false;
		flowGraphicsModule.doubleClickTimer.start();
		if (lastConnEle == null) {
			lastConnEle = handle.getSvgElementsManager().getNodeAt(
					handle.getCanvas().getDocument().getDocumentElement(),
					scaledPoint, handle.getSelection().getSelectionFilter(),
					handle.getSelection().getGroupBreaker());
		}
		// scaledPoint = EditorToolkit.getCenterPoint(lastConnEle);
		// point = handle.getTransformsManager()
		// .getScaledPoint(scaledPoint, false);
		if (!EditorToolkit.isVHPathConnectable(lastConnEle)) {
			lastConnEle = null;
		}
		if (segments.size() > 0) {

			Element element = null;
			drawingPathToConnect = null;

			if (drawingPathToConnect != null) {

				// getting the element that should be modified
				final Element modifiedElement = drawingPathToConnect
						.getElement();
				element = modifiedElement;

				// getting the path to modified
				Path connectedPath = drawingPathToConnect.getPath();

				// duplicating the path so that it can be modified
				Path modifiedPath = new Path(connectedPath);

				// modifying the path
				if (drawingPathToConnect.isInsertBeforeAction()) {

					modifiedPath.insertBefore(elementPath);

				} else {

					modifiedPath.insertAfter(elementPath);
				}

				// checking if the path should be closed
				if (pathShapeModule.getEditor().getClosePathModeManager()
						.shouldClosePath()) {

					modifiedPath.closePath();
				}

				// applying the modifications
				// getting the string representation of the both paths
				final String initDValue = connectedPath.toString();
				final String modifiedDValue = modifiedPath.toString();

				// setting the new translation factors
				Runnable executeRunnable = new Runnable() {

					public void run() {

						modifiedElement.setAttribute(PathShape.dAtt,
								modifiedDValue);
						// resetConnEle();
					}
				};

				// the undo runnable
				Runnable undoRunnable = new Runnable() {

					public void run() {

						modifiedElement.setAttribute("d", initDValue);
					}
				};

				// creating the undo/redo action, and adding it to the undo/redo
				// stack
				Set<Element> elements = new HashSet<Element>();
				elements.add(modifiedElement);
				ShapeToolkit.addUndoRedoAction(handle,
						pathShapeModule.modifyPointUndoRedoLabel,
						executeRunnable, undoRunnable, elements);

			} else {

				if (lineID == null) {
					lineID = UUID.randomUUID().toString();
				}

				element = pathShapeModule.createElement(handle, elementPath,
						lineID);

				pathShapeModule.resetDrawing();
			}
		}

		reset(handle);
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
		// if (isLinkFlag()) {
		// Element element = handle.getSvgElementsManager().getNodeAt(
		// handle.getCanvas().getDocument().getDocumentElement(),
		// handle.getTransformsManager().getScaledPoint(point, true),
		// "");
		// if (element != null && element.getNodeName().equals("use")) {
		// // 图元，则读取图元信息
		// Point2D pp = handle.getTransformsManager().getScaledPoint(
		// point, true);
		// handle.getSelection().showTerminal(element);
		// connectionFlag = handle.getSelection().showConnectNode(element,
		// pp.getX(), pp.getY());
		// } else if (element != null
		// && element.getNodeName().equals("g")
		// && element.getAttribute("symbol_status") != null
		// && element.getAttribute("symbol_status").equals(
		// NCIEquipSymbolBean.SYMBOL_TYPE_TEMPLATE)) {
		// Point2D pp = handle.getTransformsManager().getScaledPoint(
		// point, true);
		// handle.getSelection().showTerminal(element);
		// connectionFlag = handle.getSelection().showConnectNode(element,
		// pp.getX(), pp.getY());
		// } else {
		// handle.getSelection().showTerminal(null);
		// connectionFlag = false;
		// }
		// }
		if (points.size() == 0 && segments.size() > 0) {

			// point = computeVHLinePoint(segments.getLast().getEndPoint(),
			// point);
			// if(point.getX()-previousPoint.getX()<ignoreCap){
			// point.setLocation(previousPoint.getX(), point.getY())
			// }
			previousPoint = point;
			// the action is "cubicTo" step 1/6 or lineTo step 1/3,
			points
					.add(new ActionPoint(AbstractShape.DRAWING_MOUSE_MOVED,
							point));

			// drawing the line
			drawnPath.reset();

			generateCornerPoints(handle, point);
			// drawnPath.moveTo((float) clickedPoint.getX(), (float)
			// clickedPoint
			// .getY());
			for (int i = 0; i < cornerPoints.size(); i++) {
				Point2D p = cornerPoints.get(i);
				if (i == 0) {
					drawnPath.moveTo((float) p.getX(), (float) p.getY());
				} else {
					drawnPath.lineTo((float) p.getX(), (float) p.getY());
				}
			}
			// drawnPath.lineTo((float) point.getX(), (float) point.getY());
			// if(point.getX()>500)
			// drawnPath.reset();
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
			// elementPath.reset();
			for (int i = 0; i < cornerPoints.size(); i++) {
				Point2D p = cornerPoints.get(i);
				if (i == 0) {
					wholePath.moveTo((float) p.getX(), (float) p.getY());
					Point2D scEndPoint = handle.getTransformsManager()
							.getScaledPoint(p, true);
					elementPath.moveTo((float) scEndPoint.getX(),
							(float) scEndPoint.getY());
				} else {
					wholePath.lineTo((float) p.getX(), (float) p.getY());
					Point2D scEndPoint = handle.getTransformsManager()
							.getScaledPoint(p, true);
					elementPath.lineTo((float) scEndPoint.getX(),
							(float) scEndPoint.getY());
				}

				// System.out.println(p);
			}
			// wholePath.lineTo((float) seg.getEndPoint().getX(), (float) seg
			// .getEndPoint().getY());
			//
			// // getting the base scaled points value
			// Point2D scEndPoint =
			// handle.getTransformsManager().getScaledPoint(
			// seg.getEndPoint(), true);
			// elementPath.lineTo((float) scEndPoint.getX(), (float) scEndPoint
			// .getY());

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
		if (!isFirstClick) {
			Point2D scaledPoint = handle.getTransformsManager().getScaledPoint(
					point, true);
			lastConnEle = handle.getSvgElementsManager().getNodeAt(
					handle.getCanvas().getDocument().getDocumentElement(),
					scaledPoint, handle.getSelection().getSelectionFilter(),
					handle.getSelection().getGroupBreaker());
			if (lastConnEle != null) {
				this.mouseDoubleClicked(handle, point);
				return;
			}
		}
	}

	public static final int DIM_EAST = 0;
	public static final int DIM_WEST = 1;
	public static final int DIM_NORTH = 2;
	public static final int DIM_SOUTH = 3;

	private int generateCornerPointsWithFirstConnEle(SVGHandle handle,
			Rectangle2D firstConnEleRect, Point2D currentPoint,
			Point2D scaledPoint) {
		double middleMinY = firstConnEleRect.getCenterY()
				- firstConnEleRect.getHeight() / 2;
		double middleMaxY = firstConnEleRect.getCenterY()
				+ firstConnEleRect.getHeight() / 2;
		double middleMinX = firstConnEleRect.getCenterX()
				- firstConnEleRect.getWidth() / 2;
		double middleMaxX = firstConnEleRect.getCenterX()
				+ firstConnEleRect.getWidth() / 2;
		double outerDistance = -1;
		Point2D scaledClickPoint = handle.getTransformsManager()
				.getScaledPoint(clickedPoint, true);
		int flag = -1;
		if (scaledPoint.getX() >= middleMaxX
				&& (scaledPoint.getY() <= middleMaxY && scaledPoint.getY() >= middleMinY)) {
			outerDistance = scaledPoint.getX() - middleMaxX;
			outerDistance = outerDistance > USE_CAP ? USE_CAP : outerDistance;
			if (currentPoint.getY() != clickedPoint.getY()) {
				Point2D middle = new Point2D.Double(middleMaxX + outerDistance,
						scaledClickPoint.getY());
				cornerPoints.add(handle.getTransformsManager().getScaledPoint(
						middle, false));
				if (scaledPoint.getX() != middleMaxX + outerDistance) {
					middle = new Point2D.Double(middleMaxX + outerDistance,
							scaledPoint.getY());
					cornerPoints.add(handle.getTransformsManager()
							.getScaledPoint(middle, false));
				}
			}
			flag = DIM_EAST;
		} else if (scaledPoint.getY() >= middleMaxY) {

			outerDistance = scaledPoint.getY() - middleMaxY;
			outerDistance = outerDistance > USE_CAP ? USE_CAP : outerDistance;
			if (currentPoint.getY() != clickedPoint.getY()) {
				Point2D middle = new Point2D.Double(scaledClickPoint.getX(),
						middleMaxY + outerDistance);
				cornerPoints.add(handle.getTransformsManager().getScaledPoint(
						middle, false));
				if (scaledPoint.getY() != middleMaxY + outerDistance) {
					middle = new Point2D.Double(scaledPoint.getX(), middleMaxY
							+ outerDistance);
					cornerPoints.add(handle.getTransformsManager()
							.getScaledPoint(middle, false));
				}
			}
			flag = DIM_SOUTH;
		} else if (scaledPoint.getX() < middleMinX
				&& (scaledPoint.getY() < middleMaxY && scaledPoint.getY() > middleMinY)) {
			outerDistance = middleMinX - scaledPoint.getX();
			outerDistance = outerDistance > USE_CAP ? USE_CAP : outerDistance;
			if (currentPoint.getY() != clickedPoint.getY()) {
				Point2D middle = new Point2D.Double(middleMinX - outerDistance,
						scaledClickPoint.getY());
				cornerPoints.add(handle.getTransformsManager().getScaledPoint(
						middle, false));
				if (scaledPoint.getX() != middleMaxX + outerDistance) {
					middle = new Point2D.Double(middleMinX - outerDistance,
							scaledPoint.getY());
					cornerPoints.add(handle.getTransformsManager()
							.getScaledPoint(middle, false));
				}
			}
			flag = DIM_WEST;
		} else if (scaledPoint.getY() <= middleMinY) {
			outerDistance = middleMinY - scaledPoint.getY();
			outerDistance = outerDistance > USE_CAP ? USE_CAP : outerDistance;
			if (currentPoint.getY() != clickedPoint.getY()) {
				Point2D middle = new Point2D.Double(scaledClickPoint.getX(),
						middleMinY - outerDistance);
				cornerPoints.add(handle.getTransformsManager().getScaledPoint(
						middle, false));
				if (scaledPoint.getY() != middleMaxY + outerDistance) {
					middle = new Point2D.Double(scaledPoint.getX(), middleMinY
							- outerDistance);
					cornerPoints.add(handle.getTransformsManager()
							.getScaledPoint(middle, false));
				}
			}
			flag = DIM_NORTH;
		}
		return flag;
	}

	/**
	 * 鼠标移动过程中，自动计算的拐点位置
	 * 
	 * @return
	 */
	private void generateCornerPoints(SVGHandle handle, Point2D currentPoint) {
		// MyPoint2D clickedPoint = new MyPoint2D(clickedPoint);
		cornerPoints.clear();
		Point2D tempClickPoint = new Point2D.Double();
		tempClickPoint.setLocation(clickedPoint);

		cornerPoints.add(tempClickPoint);
		Point2D scaledPoint = handle.getTransformsManager().getScaledPoint(
				currentPoint, true);
		Element elementAtPoint = handle.getSvgElementsManager().getNodeAt(
				handle.getCanvas().getDocument().getDocumentElement(),
				scaledPoint, handle.getSelection().getSelectionFilter(),
				handle.getSelection().getGroupBreaker());
		if (!EditorToolkit.isVHPathConnectable(elementAtPoint)) {
			elementAtPoint = null;
		}
		if (elementAtPoint != null
				&& EditorToolkit.isVHPathConnectable(elementAtPoint)) {
			scaledPoint = EditorToolkit.getCenterPoint(elementAtPoint);
		}
		currentPoint = handle.getTransformsManager().getScaledPoint(
				scaledPoint, false);
		firstConnEleRect = null;
		if (firstConnEle != null) {
			//
			firstConnEleRect = flowGraphicsModule.getElementShape(firstConnEle)
					.getBounds2D();

			if (EditorToolkit.contiansPoint(firstConnEleRect, scaledPoint)) {// 在内部时不做反应
				cornerPoints.clear();
				return;
			}
			if (isPreviousClickFirstConnEleClicked) {
				int position = generateCornerPointsWithFirstConnEle(handle,
						firstConnEleRect, currentPoint, scaledPoint);
			}
		}

		if (!isPreviousClickFirstConnEleClicked) {
			Point2D middle = new Point2D.Double(currentPoint.getX(),
					clickedPoint.getY());
			cornerPoints.add(middle);
		}
		// if (currentPoint.getY() == clickedPoint.getY()) {
		// // FIXME 直接一条水平线，水平绘制，两个图元的中心点在同一Y值
		// }
		cornerPoints.add(currentPoint);
		// 如果最后第二个在图元内部，需要重新调整
		Rectangle2D elementAtPointRect = null;
		boolean regenerate = false;
		if (elementAtPoint != null) {
			if (cornerPoints.size() >= 2) {
				Point2D last2ndPoint = cornerPoints
						.get(cornerPoints.size() - 2);
				elementAtPointRect = flowGraphicsModule.getElementShape(
						elementAtPoint).getBounds2D();
				Point2D last2ndPointScaled = handle.getTransformsManager()
						.getScaledPoint(last2ndPoint, true);

				// 图元框包含了前一个节点

				if (EditorToolkit.contiansPoint(elementAtPointRect,
						last2ndPointScaled)) {
					USE_CAP = USE_CAP / 2;
					// elementPath.
					if (USE_CAP >= 1) {
						generateCornerPoints(handle, currentPoint);
					} else {
						System.out.println("真的没办法");
					}
					regenerate = true;
					// generateConnerPoints2ndConnEle(handle,
					// elementAtPointRect,
					// last2ndPointScaled, last3rdPointScaled, currentPoint);
				}
				USE_CAP = CAP;
			}
		}
		if (!regenerate)
			reduceInsectPath(handle, firstConnEleRect, elementAtPointRect);

	}

	/**
	 * 第一个点和第二个点之间，应去除图元内部部分; 最后一个点和最后第二个点之间，应去除图元内部部分
	 * 
	 * @param handle
	 * @param firstConnEleRect
	 */
	private void reduceInsectPath(SVGHandle handle,
			Rectangle2D firstConnEleRect, Rectangle2D elementAtPointRect) {
		// 第一个点和第二个点之间，应去除图元内部部分
		if (firstConnEle != null && isPreviousClickFirstConnEleClicked) {
			if (cornerPoints.size() > 1) {
				Point2D firstPoint = cornerPoints.get(0);
				Point2D secondPoint = cornerPoints.get(1);
				Point2D scaledFirstPoint = handle.getTransformsManager()
						.getScaledPoint(firstPoint, true);
				Point2D scaledSecondPoint = handle.getTransformsManager()
						.getScaledPoint(secondPoint, true);
				// 由于浮点数会产生误差，这里不能用==来判断位置的水平或垂直
				if (Math
						.abs(scaledFirstPoint.getX() - scaledSecondPoint.getX()) < 0.01) {// 垂直的
					double offset = scaledFirstPoint.getY() > scaledSecondPoint
							.getY() ? -firstConnEleRect.getHeight() / 2
							: firstConnEleRect.getHeight() / 2;
					scaledFirstPoint.setLocation(scaledFirstPoint.getX(),
							scaledFirstPoint.getY() + offset);
				} else if (Math.abs(scaledFirstPoint.getY()
						- scaledSecondPoint.getY()) < 0.01) {// 水平的
					double offset = scaledFirstPoint.getX() > scaledSecondPoint
							.getX() ? -firstConnEleRect.getWidth() / 2
							: firstConnEleRect.getWidth() / 2;
					scaledFirstPoint.setLocation(scaledFirstPoint.getX()
							+ offset, scaledFirstPoint.getY());
				}
				Point2D temp = handle.getTransformsManager().getScaledPoint(
						scaledFirstPoint, false);
				firstPoint.setLocation(temp);
			}

		}
		if (elementAtPointRect != null) {
			Point2D lastPoint = cornerPoints.get(cornerPoints.size() - 1);
			Point2D last2ndPoint = cornerPoints.get(cornerPoints.size() - 2);
			Point2D scaledLastPoint = handle.getTransformsManager()
					.getScaledPoint(lastPoint, true);
			Point2D scaledLast2ndPoint = handle.getTransformsManager()
					.getScaledPoint(last2ndPoint, true);
			if (Math.abs(scaledLastPoint.getX() - scaledLast2ndPoint.getX()) < 0.01) {// 垂直的
				double offset = scaledLastPoint.getY() > scaledLast2ndPoint
						.getY() ? -elementAtPointRect.getHeight() / 2
						: elementAtPointRect.getHeight() / 2;
				scaledLastPoint.setLocation(scaledLastPoint.getX(),
						scaledLastPoint.getY() + offset);

			} else if (Math.abs(scaledLastPoint.getY()
					- scaledLast2ndPoint.getY()) < 0.01) {// 水平的
				double offset = scaledLastPoint.getX() > scaledLast2ndPoint
						.getX() ? -elementAtPointRect.getWidth() / 2
						: elementAtPointRect.getWidth() / 2;
				scaledLastPoint.setLocation(scaledLastPoint.getX() + offset,
						scaledLastPoint.getY());
			}
			Point2D temp = handle.getTransformsManager().getScaledPoint(
					scaledLastPoint, false);
			lastPoint.setLocation(temp);
		}
	}

	@Override
	public Element createPathElement(SVGHandle handle, Shape pathShape,
			String lineID) {
		Element e = null;
		try {
			e = super.createPathElement(handle, pathShape, lineID);
			Element e1 = firstConnEle;
			Element e2 = lastConnEle;
			flowGraphicsModule.fillVHPath(e);
			flowGraphicsModule.createRelation(e, e1, e2);
		} catch (Exception ex) {
			handle.getEditor().getLogger().log(flowGraphicsModule,
					LoggerAdapter.ERROR, ex);
		} finally {
			reset(handle);
		}
		return e;
	}

}
