package com.nci.svg.sdk.shape;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.apache.batik.ext.awt.geom.ExtendedGeneralPath;
import org.w3c.dom.Element;

import com.nci.svg.sdk.module.BusinessModuleAdapter;

import fr.itris.glips.library.geom.path.segment.Segment;
import fr.itris.glips.svgeditor.display.canvas.CanvasPainter;
import fr.itris.glips.svgeditor.display.canvas.SVGCanvas;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.shape.AbstractShape;
import fr.itris.glips.svgeditor.shape.path.ActionPoint;
import fr.itris.glips.svgeditor.shape.path.PathToConnect;
import fr.itris.glips.svgeditor.shape.path.segments.ActionSeg;
import fr.itris.glips.svgeditor.shape.path.segments.CubicToSeg;
import fr.itris.glips.svgeditor.shape.path.segments.LineToSeg;
import fr.itris.glips.svgeditor.shape.path.segments.MoveToSeg;
import fr.itris.glips.svgeditor.shape.path.segments.QuadToSeg;

/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2009-3-6
 * @功能：业务绘图控制类
 *
 */
public class BusinessDrawingHandler {

	/**
	 * the ghost painter
	 */
	protected DefaultGhostShapeCanvasPainter ghostPainter = new DefaultGhostShapeCanvasPainter();

	/**
	 * the path shape module
	 */
	protected BusinessModuleAdapter pathShapeModule;

	/**
	 * the path that should be used to create the svg element
	 */
	protected ExtendedGeneralPath path = new ExtendedGeneralPath();
	protected LinkedList<ExtendedGeneralPath> paths = new LinkedList<ExtendedGeneralPath>();
	/**
	 * the path that contains all the segments before the previous action
	 */
	protected ExtendedGeneralPath wholePath = new ExtendedGeneralPath();
	
	protected LinkedList<ExtendedGeneralPath> wholePaths = new LinkedList<ExtendedGeneralPath>();
	
	protected LinkedList<StoreData> dataList = new LinkedList<StoreData>();

	/**
	 * the path used for the drawing action
	 */
	protected ExtendedGeneralPath drawnPath = new ExtendedGeneralPath();

	/**
	 * the path used to draw control point lines of the current segment
	 */
	protected ExtendedGeneralPath controlPointsPath = new ExtendedGeneralPath();

	/**
	 * the list of the points that correspond to an event
	 */
	protected LinkedList<ActionPoint> points = new LinkedList<ActionPoint>();

	/**
	 * the list of the segments
	 */
	protected LinkedList<ActionSeg> segments = new LinkedList<ActionSeg>();

	/**
	 * the object storing information on the potential path that could be
	 * connected to the path created in the drawing mode
	 */
	protected PathToConnect drawingPathToConnect = null;
	
	protected Point2D beginPoint = null;
	protected Point2D curPoint = null;
	protected int index = 0;
	protected StoreData curData = null;
	
	protected ArrayList<TextShowData> textList = new ArrayList<TextShowData>();
	
	
	/**
	 * the constructor of the class
	 * 
	 * @param pathShape
	 *            the path shape module
	 */
	public BusinessDrawingHandler(BusinessModuleAdapter pathShape) {

		this.pathShapeModule = pathShape;

		// adding a listener to the keys entered by the user
		AWTEventListener keyListener = new AWTEventListener() {

			public void eventDispatched(AWTEvent evt) {

				// getting the key event
				KeyEvent event = (KeyEvent) evt;

				if (event != null && event.isControlDown()
						&& event.getKeyCode() == KeyEvent.VK_Z
						&& event.getID() == KeyEvent.KEY_PRESSED
						&& (segments.size() > 0 || points.size() > 0)) {

					event.consume();

					// getting the current handle
					SVGHandle handle = pathShapeModule.getEditor()
							.getHandlesManager().getCurrentHandle();

					if (handle != null) {

						// whether a modification occured
						boolean modified = false;

						if (points.size() > 1) {

							points.clear();

							// clearing the drawing paths
							drawnPath.reset();
							controlPointsPath.reset();

							modified = true;

						} else if (segments.size() > 1) {

							points.clear();

							// clearing the drawing paths
							drawnPath.reset();
							controlPointsPath.reset();

							// removing the last segment
							segments.removeLast();

							// rebuilding the paths
							wholePath.reset();
							path.reset();
							Point2D pt = null, pt1 = null, pt2 = null;

							for (ActionSeg seg : segments) {

								if (seg instanceof MoveToSeg) {

									pt = seg.getEndPoint();
									wholePath.moveTo((float) pt.getX(),
											(float) pt.getY());

									pt = handle.getTransformsManager()
											.getScaledPoint(pt, true);
									path.moveTo((float) pt.getX(), (float) pt
											.getY());

								} else if (seg instanceof LineToSeg) {

									pt = seg.getEndPoint();
									wholePath.lineTo((float) pt.getX(),
											(float) pt.getY());

									pt = handle.getTransformsManager()
											.getScaledPoint(pt, true);
									path.lineTo((float) pt.getX(), (float) pt
											.getY());

								} else if (seg instanceof QuadToSeg) {

									pt = seg.getEndPoint();
									pt1 = seg.getControlPoint();
									wholePath.quadTo((float) pt1.getX(),
											(float) pt1.getY(), (float) pt
													.getX(), (float) pt.getY());

									pt = handle.getTransformsManager()
											.getScaledPoint(pt, true);
									pt1 = handle.getTransformsManager()
											.getScaledPoint(pt1, true);
									path.quadTo((float) pt1.getX(), (float) pt1
											.getY(), (float) pt.getX(),
											(float) pt.getY());

								} else if (seg instanceof CubicToSeg) {

									pt = seg.getEndPoint();
									pt1 = ((CubicToSeg) seg)
											.getFirstControlPoint();
									pt2 = seg.getControlPoint();
									wholePath.curveTo((float) pt1.getX(),
											(float) pt1.getY(), (float) pt2
													.getX(),
											(float) pt2.getY(), (float) pt
													.getX(), (float) pt.getY());

									pt = handle.getTransformsManager()
											.getScaledPoint(pt, true);
									pt1 = handle.getTransformsManager()
											.getScaledPoint(pt1, true);
									pt2 = handle.getTransformsManager()
											.getScaledPoint(pt2, true);
									path.curveTo((float) pt1.getX(),
											(float) pt1.getY(), (float) pt2
													.getX(),
											(float) pt2.getY(), (float) pt
													.getX(), (float) pt.getY());
								}
							}

							modified = true;
						}

						// repainting the shapes
						if (modified) {

							paintShape(handle);
						}
					}
				}
			}
		};

		Toolkit.getDefaultToolkit().addAWTEventListener(keyListener,
				AWTEvent.KEY_EVENT_MASK);
	}

	/**
	 * @return the path that should be used to create the svg element
	 */
	public ExtendedGeneralPath getPath() {
		return wholePath;
	}

	/**
	 * @return the path that is drawn
	 */
	public ExtendedGeneralPath getDrawnPath() {
		return drawnPath;
	}



	/**
	 * notifies that the mouse has been pressed
	 * 
	 * @param handle
	 *            a svg handle
	 * @param point
	 *            the where action occured
	 */
	public void mousePressed(SVGHandle handle, Point2D point,int modifier) {

		if (segments.size() == 0 && points.size() == 0) {

      		// creating a moveTo segment, step 1/1
			segments.add(new MoveToSeg(point));
			wholePath.reset();
			wholePath.moveTo((float) point.getX(), (float) point.getY());

			// getting the base scaled point value
			Point2D scaledPoint = handle.getTransformsManager().getScaledPoint(
					point, true);
			path.reset();
			path.moveTo((float) scaledPoint.getX(), (float) scaledPoint.getY());
            beginPoint = point;

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
			beginPoint = curPoint;
			points.add(new ActionPoint(AbstractShape.DRAWING_MOUSE_PRESSED,
					point));


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
			path.quadTo((float) scControlPoint.getX(), (float) scControlPoint
					.getY(), (float) scEndPoint.getX(), (float) scEndPoint
					.getY());
		}
	}

	/**
	 * notifies that the mouse has been released
	 * 
	 * @param handle
	 *            a svg handle
	 * @param point
	 *            the where action occured
	 */
	public void mouseReleased(SVGHandle handle, Point2D point,int modifier) {


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
//			wholePath.lineTo((float) curPoint.getX(),(float)curPoint.getY());
			ExtendedGeneralPath tempPath = new ExtendedGeneralPath(drawnPath);
			wholePaths.add(tempPath);
			wholePath.reset();
			wholePath.moveTo((float) curPoint.getX(),(float)curPoint.getY());

			// getting the base scaled points value
			Point2D scEndPoint = handle.getTransformsManager().getScaledPoint(
					curPoint, true);
			path.lineTo((float) scEndPoint.getX(), (float) scEndPoint.getY());
			tempPath = new ExtendedGeneralPath(path);
			paths.add(tempPath);
			path.reset();
			path.moveTo((float) scEndPoint.getX(), (float) scEndPoint.getY());
			
			dataList.add(curData);
			curData = null;
			

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
			path.curveTo((float) scControlPoint1.getX(),
					(float) scControlPoint1.getY(), (float) scControlPoint2
							.getX(), (float) scControlPoint2.getY(),
					(float) scEndPoint.getX(), (float) scEndPoint.getY());
		}

	}

	/**
	 * notifies that the mouse has been dragged
	 * 
	 * @param handle
	 *            a svg handle
	 * @param point
	 *            the where action occured
	 */
	public void mouseDragged(SVGHandle handle, Point2D point,int modifier) {

		// removing the last point of the points list of its a dragged action
		// point
		if (points.size() > 0
				&& points.getLast().getActionType() == AbstractShape.DRAWING_MOUSE_DRAGGED) {

			points.removeLast();
		}

		// initializing the drawing shape
		initializeDrawingPath();

		if (points.size() == 0 && segments.size() > 0) {

			// recording the point
			points.add(new ActionPoint(AbstractShape.DRAWING_MOUSE_DRAGGED,
					point));

			// the action is "quadTo", step 1/4
			drawQuadToSegment();

		} else if (points.size() == 1
				&& points.getLast().getActionType() == AbstractShape.DRAWING_MOUSE_PRESSED) {

			// recording the point
			points.add(new ActionPoint(AbstractShape.DRAWING_MOUSE_DRAGGED,
					point));

			// the action is "cubicTo", step 3/4
			drawCubicToSegment();
		}

		// painting the shape
		paintShape(handle);
	}

	/**
	 * modify by yux,2008.12.29 重新修改了拓扑点的显示，及连接点的出现 notifies that the mouse has
	 * been moved
	 * 
	 * @param handle
	 *            a svg handle
	 * @param point
	 *            the where action occured
	 */
	public void mouseMoved(SVGHandle handle, Point2D point,int modifier) {

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

			 curData = pathShapeModule.draw(paths.size(), pathShapeModule.MOUSE_MOVE,drawnPath, beginPoint, point,modifier);
			 textList.clear();
			 if(curData != null)
			 {
			    curPoint = curData.getPoint();
			    textList.addAll(curData.getTextList());
			 }
			// drawing the line

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

	/**
	 * notifies that the mouse has been double clicked
	 * 
	 * @param handle
	 *            a svg handle
	 * @param point
	 *            the where action occured
	 */
	public void mouseDoubleClicked(SVGHandle handle, Point2D point,int modifier) {

		if (segments.size() > 0) {

			Element element = null;
	
				element = pathShapeModule.createElement(handle,index, paths,dataList);
				
				pathShapeModule.resetDrawing();


			// selecting the element
			handle.getSelection().handleSelection(element, false, true);
		}

		reset(handle);
	}



	/**
	 * resets the drawing action
	 * 
	 * @param handle
	 *            the current svg handle
	 */
	public void reset(SVGHandle handle) {

		if (segments.size() > 0) {

			path.reset();
			paths.clear();
			wholePath.reset();
			wholePaths.clear();
			drawnPath.reset();
			controlPointsPath.reset();
			points.clear();
			segments.clear();
			drawingPathToConnect = null;
			index = -1;
			beginPoint = null;
			curPoint = null;
			textList.clear();

			// removing the ghost canvas painter
			if (ghostPainter.getClip() != null) {

				handle.getCanvas().removePaintListener(ghostPainter, true);
			}
		}
	}

	/**
	 * initializes the drawing path
	 */
	protected void initializeDrawingPath() {

		drawnPath.reset();
		controlPointsPath.reset();
		textList.clear();

		// getting the endPoint of the segment
		Point2D endPoint = getEndPoint();

		if (endPoint != null) {

			// adding the moveTo command
			drawnPath.moveTo((float) endPoint.getX(), (float) endPoint.getY());
			controlPointsPath.moveTo((float) endPoint.getX(), (float) endPoint
					.getY());
		}
	}

	/**
	 * @return the end point of the last segment
	 */
	protected Point2D getEndPoint() {

		if (segments.size() > 0) {

			// getting the end point of the last added segment
			return segments.getLast().getEndPoint();
		}

		return null;
	}

	/**
	 * draws the current quadTo segment
	 */
	protected void drawQuadToSegment() {

		if (points.size() == 1) {

			Point2D point = points.getLast().getActionPoint();
			controlPointsPath
					.lineTo((float) point.getX(), (float) point.getY());
			DrawToolkit.drawCircle(controlPointsPath, point,2);

		} else if (points.size() == 2) {

			// drawing the curve
			Point2D pt1 = points.get(0).getActionPoint();
			Point2D pt2 = points.get(1).getActionPoint();

			drawnPath.quadTo((float) pt1.getX(), (float) pt1.getY(),
					(float) pt2.getX(), (float) pt2.getY());
			controlPointsPath.lineTo((float) pt1.getX(), (float) pt1.getY());
			DrawToolkit.drawCircle(controlPointsPath, pt1,2);
		}
	}

	/**
	 * draws the current cubicTo segment
	 */
	protected void drawCubicToSegment() {

		if (points.size() == 2) {

			// getting the points
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

			drawnPath.curveTo((float) pt2.getX(), (float) pt2.getY(),
					(float) pt3.getX(), (float) pt3.getY(), (float) pt1.getX(),
					(float) pt1.getY());
			controlPointsPath.lineTo((float) pt2.getX(), (float) pt2.getY());
			DrawToolkit.drawCircle(controlPointsPath, pt2,2);

			controlPointsPath.moveTo((float) pt1.getX(), (float) pt1.getY());
			controlPointsPath.lineTo((float) pt3.getX(), (float) pt3.getY());
			DrawToolkit.drawCircle(controlPointsPath, pt3,2);
		}
	}

	/**
	 * paints the drawing shape
	 * 
	 * @param handle
	 *            a svg handle
	 */
	protected void paintShape(SVGHandle handle) {

		// removing the ghost canvas painter
		if (ghostPainter.getClip() != null) {

			handle.getCanvas().removePaintListener(ghostPainter, false);
		}

		// setting the new shapes to the ghost painter
		ghostPainter.reinitialize();
//		ghostPainter.setPathShape(wholePath);
		ghostPainter.setPathShapes(wholePaths);
		ghostPainter.setCurrentSegmentShape(drawnPath);
		ghostPainter.setControlPointsShape(controlPointsPath);
		ghostPainter.setTextList(textList);
		handle.getCanvas().addLayerPaintListener(SVGCanvas.DRAW_LAYER,
				ghostPainter, true);
	}

	/**
	 * draws a circle in the provided path whose center is the provided point
	 * 
	 * @param aPath
	 *            a path
	 * @param point
	 *            the center of the circle
	 */

	

	

	/**
	 * computes the last point of a line given the current point of the mouse
	 * when the line constraint mode is active
	 * 
	 * @param startPoint
	 *            the start point for the segment
	 * @param basePoint
	 *            the current's mouse point
	 * @return the computed point
	 */
	protected Point2D computeLinePointWhenLineConstraintModeActive(
			Point2D startPoint, Point2D basePoint) {

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
			if (pt1.x <= pt2.x && pt1.y >= pt2.y) {

				if (cosinus <= 1 && cosinus > Math.cos(Math.PI / 8)) {

					pt.x = (int) (pt1.x + xDistance);
					pt.y = pt1.y;

				} else if (cosinus <= Math.cos(Math.PI / 8)
						&& cosinus > Math.cos(3 * Math.PI / 8)) {

					pt.x = (int) (pt1.x + xDistance);
					pt.y = (int) (pt1.y - xDistance);

				} else if (cosinus <= Math.cos(3 * Math.PI / 8) && cosinus > 0) {

					pt.x = pt1.x;
					pt.y = (int) (pt1.y - yDistance);
				}

			} else if (pt1.x > pt2.x && pt1.y >= pt2.y) {

				if (cosinus <= 0 && cosinus > Math.cos(5 * Math.PI / 8)) {

					pt.x = pt1.x;
					pt.y = (int) (pt1.y - yDistance);

				} else if (cosinus <= Math.cos(5 * Math.PI / 8)
						&& cosinus >= Math.cos(7 * Math.PI / 8)) {

					pt.x = (int) (pt1.x - xDistance);
					pt.y = (int) (pt1.y - xDistance);

				} else if (cosinus <= Math.cos(7 * Math.PI / 8)
						&& cosinus >= -1) {

					pt.x = (int) (pt1.x - xDistance);
					pt.y = pt1.y;
				}

			} else if (pt1.x >= pt2.x && pt1.y < pt2.y) {

				if (cosinus >= -1 && cosinus < Math.cos(7 * Math.PI / 8)) {

					pt.x = (int) (pt1.x - xDistance);
					pt.y = pt1.y;

				} else if (cosinus >= Math.cos(7 * Math.PI / 8)
						&& cosinus < Math.cos(5 * Math.PI / 8)) {

					pt.x = (int) (pt1.x - xDistance);
					pt.y = (int) (pt1.y + xDistance);

				} else if (cosinus >= Math.cos(5 * Math.PI / 8) && cosinus < 0) {

					pt.x = pt1.x;
					pt.y = (int) (pt1.y + yDistance);
				}

			} else if (pt1.x <= pt2.x && pt1.y <= pt2.y) {

				if (cosinus >= 0 && cosinus < Math.cos(3 * Math.PI / 8)) {

					pt.x = pt1.x;
					pt.y = (int) (pt1.y + yDistance);

				} else if (cosinus >= Math.cos(3 * Math.PI / 8)
						&& cosinus < Math.cos(Math.PI / 8)) {

					pt.x = (int) (pt1.x + xDistance);
					pt.y = (int) (pt1.y + xDistance);

				} else if (cosinus >= Math.cos(Math.PI / 8) && cosinus < 1) {

					pt.x = (int) (pt1.x + xDistance);
					pt.y = pt1.y;
				}
			}
		}

		return pt;
	}
	
	public StoreData newStoreData()
	{
		return new StoreData();
	}
	
	public TextShowData newTextShowData()
	{
		return new TextShowData();
	}
	/**
	 * add by yux,2009.2.25
	 * 鼠标滚动条上滚
	 * @param handle：
	 */
	public void mouseWheelUp(SVGHandle handle, Point2D point,int modifier)
	{
		pathShapeModule.draw(paths.size(), pathShapeModule.MOUSE_WHEEL_UP,drawnPath, beginPoint, curPoint,modifier);
	}
	
	/**
	 * add by yux,2009.2.25
	 * 鼠标滚动条下滚
	 * @param handle
	 */
	public void mouseWheelDown(SVGHandle handle, Point2D point,int modifier)
	{
		pathShapeModule.draw(paths.size(), pathShapeModule.MOUSE_WHEEL_DOWN,drawnPath, beginPoint, curPoint,modifier);
	}
	
	/**
	 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
	 * 
	 * @author yx.nci
	 * @时间：2009-3-11
	 * @功能：数据存储类
	 *
	 */
	public class StoreData
	{
		/**
		 * add by yux,2009-3-9
		 * 业务所需自定义对象
		 */
		private Object obj = null;
		/**
		 * add by yux,2009-3-12
		 * 文字显示
		 */
		private ArrayList<TextShowData> textList = new ArrayList<TextShowData>();
		/**
		 * add by yux,2009-3-9
		 * 当前操作最后的处理坐标
		 */
		private Point2D point = null;
		/**
		 * 返回业务所需对象
		 * @return the obj
		 */
		public Object getObj() {
			return obj;
		}
		/**
		 * 设置业务所需对象
		 * @param obj the obj to set
		 */
		public void setObj(Object obj) {
			this.obj = obj;
		}
		/**
		 * 返回当前操作最后的处理坐标
		 * @return the point
		 */
		public Point2D getPoint() {
			return point;
		}
		/**
		 * 设置当前操作最后的处理坐标
		 * @param point the point to set
		 */
		public void setPoint(Point2D point) {
			this.point = point;
		}
		/**
		 * 返回
		 * @return the list
		 */
		public ArrayList<TextShowData> getTextList() {
			return textList;
		}
		/**
		 * 设置
		 * @param list the list to set
		 */
		public void setTextList(ArrayList<TextShowData> textList) {
			this.textList.addAll(textList);
		}
		
	}
	
	public class TextShowData
	{
		/**
		 * add by yux,2009-3-12
		 * 待显示的文字
		 */
		private String text;
		/**
		 * add by yux,2009-3-12
		 * 待显示的字体
		 */
		private Font font;
		/**
		 * add by yux,2009-3-12
		 * 显示文字的位置
		 */
		private Point2D point;
		/**
		 * 返回待显示的文字
		 * @return the text
		 */
		public String getText() {
			return text;
		}
		/**
		 * 设置待显示的文字
		 * @param text the text to set
		 */
		public void setText(String text) {
			this.text = text;
		}
		/**
		 * 返回显示文字的位置
		 * @return the point
		 */
		public Point2D getPoint() {
			return point;
		}
		/**
		 * 设置显示文字的位置
		 * @param point the point to set
		 */
		public void setPoint(Point2D point) {
			this.point = point;
		}
		/**
		 * 返回
		 * @return the font
		 */
		public Font getFont() {
			return font;
		}
		/**
		 * 设置
		 * @param font the font to set
		 */
		public void setFont(Font font) {
			this.font = font;
		}
		
	}
	/**
	 * the painter used to draw ghost shapes on a canvas
	 * 
	 * @author ITRIS, Jordi SUC
	 */
	public class DefaultGhostShapeCanvasPainter extends CanvasPainter {

		/**
		 * the path shape
		 */
		private Shape pathShape;
		private LinkedList<Shape> pathShapes = new LinkedList<Shape>();

		/**
		 * the current segment shape
		 */
		private Shape currentSegmentShape;

		/**
		 * the control points of the current segment shape
		 */
		private Shape controlPointsShape;

		/**
		 * the set of the clip rectangles
		 */
		private Set<Rectangle2D> clips = new HashSet<Rectangle2D>();

		private ArrayList<TextShowData> textList = new ArrayList<TextShowData>();
		@Override
		public void paintToBeDone(Graphics2D g) {

			g = (Graphics2D) g.create();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

//			if (pathShape != null) {
//
//				g.setColor(Color.blue);
//				g.draw(pathShape);
//			}
			
			for(Shape shape:pathShapes)
			{
				g.setColor(Color.blue);
				g.draw(shape);
			}

			if (currentSegmentShape != null) {

				g.setColor(Color.green);
				g.draw(currentSegmentShape);
			}

			if (controlPointsShape != null) {

				g.setColor(Color.gray);
				g.draw(controlPointsShape);
			}

			for(TextShowData data:textList)
			{
				g.setFont(data.getFont());
				g.setColor(Color.red);
				g.drawString(data.getText(), (float)data.getPoint().getX(), (float)data.getPoint().getY());
			}
			g.dispose();
		}

		@Override
		public Set<Rectangle2D> getClip() {

			return clips;
		}

		/**
		 * sets the path shape
		 * 
		 * @param pathShape
		 *            the path shape
		 */
		public void setPathShape(Shape pathShape) {

			this.pathShape = pathShape;
			clips.add(pathShape.getBounds2D());
		}

		/**
		 * sets the current segment shape
		 * 
		 * @param currentSegmentShape
		 *            the current segment shape
		 */
		public void setCurrentSegmentShape(Shape currentSegmentShape) {

			this.currentSegmentShape = currentSegmentShape;
			clips.add(currentSegmentShape.getBounds2D());
		}

		/**
		 * sets the control points shape
		 * 
		 * @param controlPointsShape
		 *            the control points shape
		 */
		public void setControlPointsShape(Shape controlPointsShape) {

			this.controlPointsShape = controlPointsShape;
			clips.add(controlPointsShape.getBounds2D());
		}

		/**
		 * reinitializing the painter
		 */
		public void reinitialize() {

			clips.clear();
		}

		/**
		 * 设置
		 * @param pathShapes the pathShapes to set
		 */
		public void setPathShapes(LinkedList<ExtendedGeneralPath> pathShapes) {
			this.pathShapes.clear();
			this.pathShapes.addAll(pathShapes);
			for(Shape shape:pathShapes)
			{
				clips.add(shape.getBounds2D());
			}
		}

		/**
		 * 设置
		 * @param textList the textList to set
		 */
		public void setTextList(ArrayList<TextShowData> textList) {
			this.textList.clear();
			this.textList.addAll(textList);
			
			for(TextShowData data :textList)
			{
				Rectangle2D rect = data.getFont().getStringBounds(data.getText(), new FontRenderContext(null,true,true) );
				clips.add(new Rectangle2D.Double(data.getPoint().getX(),data.getPoint().getY(),rect.getWidth(),rect.getHeight()));
			}
		}
		
		
	}

	/**
	 * 设置起始点
	 * @param beginPoint the beginPoint to set
	 */
	public void setBeginPoint(Point2D beginPoint) {
		this.beginPoint = beginPoint;
	}

	/**
	 * 设置
	 * @param curPoint the curPoint to set
	 */
	public void setCurPoint(Point2D curPoint) {
		this.curPoint = curPoint;
	}

	/**
	 * 设置
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	
}
