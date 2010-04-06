package fr.itris.glips.svgeditor.shape.path;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.apache.batik.ext.awt.geom.ExtendedGeneralPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.nci.svg.util.Constants;

import fr.itris.glips.library.geom.path.Path;
import fr.itris.glips.library.geom.path.segment.Segment;
import fr.itris.glips.svgeditor.display.canvas.CanvasPainter;
import fr.itris.glips.svgeditor.display.canvas.SVGCanvas;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.display.selection.Selection;
import fr.itris.glips.svgeditor.display.selection.SelectionItem;
import fr.itris.glips.svgeditor.shape.AbstractShape;
import fr.itris.glips.svgeditor.shape.ShapeToolkit;
import fr.itris.glips.svgeditor.shape.path.segments.ActionSeg;
import fr.itris.glips.svgeditor.shape.path.segments.CubicToSeg;
import fr.itris.glips.svgeditor.shape.path.segments.LineToSeg;
import fr.itris.glips.svgeditor.shape.path.segments.MoveToSeg;
import fr.itris.glips.svgeditor.shape.path.segments.QuadToSeg;

/**
 * the class that handles the creation of path elements
 * 
 * Drawing steps : QuadTo : dragged -> released -> moved -> pressed / CubicTo :
 * moved -> pressed -> dragged -> released / LineTo : moved -> pressed ->
 * released / MoveTo : pressed, when no segment has been created yet and no
 * point has been registered yet
 * 
 * @author Jordi SUC
 */
public class DrawingHandler {

    /**
     * the ghost painter
     */
    private DefaultGhostShapeCanvasPainter ghostPainter = new DefaultGhostShapeCanvasPainter();

    /**
     * the path shape module
     */
    private PathShape pathShapeModule;

    /**
     * the path that should be used to create the svg element
     */
    private ExtendedGeneralPath path = new ExtendedGeneralPath();

    /**
     * the path that contains all the segments before the previous action
     */
    private ExtendedGeneralPath wholePath = new ExtendedGeneralPath();

    /**
     * the path used for the drawing action
     */
    private ExtendedGeneralPath drawnPath = new ExtendedGeneralPath();

    /**
     * the path used to draw control point lines of the current segment
     */
    private ExtendedGeneralPath controlPointsPath = new ExtendedGeneralPath();

    /**
     * the list of the points that correspond to an event
     */
    private LinkedList<ActionPoint> points = new LinkedList<ActionPoint>();

    /**
     * the list of the segments
     */
    private LinkedList<ActionSeg> segments = new LinkedList<ActionSeg>();

    /**
     * the object storing information on the potential path that could be
     * connected to the path created in the drawing mode
     */
    private PathToConnect drawingPathToConnect = null;

    private boolean lineShapeDraged = false;

    private boolean linkFlag = true;

    /**
     * @return the linkFlag
     */
    public boolean isLinkFlag() {
        return linkFlag;
    }

    /**
     * @param linkFlag
     *            the linkFlag to set
     */
    public void setLinkFlag(boolean linkFlag) {
        this.linkFlag = linkFlag;
    }

    /**
     * the constructor of the class
     * 
     * @param pathShape
     *            the path shape module
     */
    public DrawingHandler(PathShape pathShape) {

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

    public String existSymbolPoint(SVGHandle handle, Element element,
            Point2D point) {
        boolean bflag = false;
        Element terminal = (Element) element.getElementsByTagName("terminal")
        .item(0);
        if (terminal == null)
            return null;

        NodeList ncipoints = terminal.getElementsByTagName("nci:POINT");
        if (ncipoints == null)
            return null;

        int nSize = ncipoints.getLength();
        int x1 = 0, x2 = 0, y1 = 0, y2 = 0;
        double dx = new Double(element.getAttribute("x")).doubleValue();
        double dy = new Double(element.getAttribute("y")).doubleValue();
        double sw = new Double(element.getAttribute("sw")).doubleValue();
        double sh = new Double(element.getAttribute("sh")).doubleValue();
        double nw = new Double(element.getAttribute("width")).doubleValue();
        double nh = new Double(element.getAttribute("height")).doubleValue();
        if (sw == 0)
            sw = nw;

        if (sh == 0)
            sh = nh;
        final AffineTransform initialTransform = handle.getSvgElementsManager()
        .getTransform(element);
        if (nSize > 0) {
            for (int i = 0; i < nSize; i++) {
                Element mdelement = (Element) ncipoints.item(i);
                x1 = new Integer(mdelement.getAttribute("x")).intValue();
                y1 = new Integer(mdelement.getAttribute("y")).intValue();
                double[] pointo = { dx + x1 * nw / sw, dy + y1 * nh / sh };

                // 通过坐标变换，获取实际的坐标地址

                initialTransform.transform(pointo, 0, pointo, 0, 1);
                if ((Math.abs((int) point.getX() - (int) pointo[0]) <= 2)
                        && (Math.abs((int) point.getY() - (int) pointo[1]) <= 2)) {
                    bflag = true;
                    String pointID = mdelement.getAttribute("name");
                    return pointID;
                }

            }
        }
        return null;
    }

    /**
     * notifies that the mouse has been pressed
     * 
     * @param handle
     *            a svg handle
     * @param point
     *            the where action occured
     */
    public void mousePressed(SVGHandle handle, Point2D point) {

        /*
         * 根据落点坐标信息，读取节点信息，分析是否与该图元连接点相符
         */
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
            path.reset();
            path.moveTo((float) scaledPoint.getX(), (float) scaledPoint.getY());
            lineShapeDraged = false;

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
            points.add(new ActionPoint(AbstractShape.DRAWING_MOUSE_PRESSED,
                    point));

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
            path.lineTo((float) scEndPoint.getX(), (float) scEndPoint.getY());

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

        if (pathShapeModule.getCurrentAction().equals("LineShape")
                && lineShapeDraged) {
            mouseDoubleClicked(handle, point);
            pathShapeModule.resetDrawing();
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
    public void mouseDragged(SVGHandle handle, Point2D point) {

        // removing the last point of the points list of its a dragged action
        if (pathShapeModule.getCurrentAction().equals("LineShape")) {
            lineShapeDraged = true;
            mouseMoved(handle, point);
            return;
        }
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
     * notifies that the mouse has been moved
     * 
     * @param handle
     *            a svg handle
     * @param point
     *            the where action occured
     */
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
                    null);
            if (element != null && element.getNodeName().equals("image")) {
                redrawLinkPoint(handle, element, point);
            } else if (element != null && isLineShape(element)) {
                redrawLinkPoint(handle, element, point);
            } else if (element != null
                    && (element.getNodeName().equals("use") || element
                            .getElementsByTagName("use").getLength() > 0)) {
                redrawLinkPoint(handle, element, point);
            } else {
                Selection selection = handle.getSelection();
                selection.refreshSelectElement(null);
            }
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

    /*
     * 读取结点metadata数据，提取连接点信息，并重绘连接点
     */
    public void redrawNciPoint(SVGHandle handle, Element element, Point2D point) {
        if (element.getNodeName().equals("image")) {
            Set<Element> elements = new HashSet<Element>();
            elements.add(element);
            Set<SelectionItem> items = new HashSet<SelectionItem>();
            Element terminal = (Element) element.getElementsByTagName(
            "terminal").item(0);
            if (terminal == null)
                return;

            NodeList ncipoints = terminal.getElementsByTagName("nci:POINT");
            if (ncipoints == null)
                return;

            int nSize = ncipoints.getLength();
            int x1 = 0, x2 = 0, y1 = 0, y2 = 0;
            double dx = new Double(element.getAttribute("x")).doubleValue();
            double dy = new Double(element.getAttribute("y")).doubleValue();
            double sw = new Double(element.getAttribute("sw")).doubleValue();
            double sh = new Double(element.getAttribute("sh")).doubleValue();
            double nw = new Double(element.getAttribute("width")).doubleValue();
            double nh = new Double(element.getAttribute("height"))
            .doubleValue();
            if (sw == 0)
                sw = nw;

            if (sh == 0)
                sh = nh;
            final AffineTransform initialTransform = handle
            .getSvgElementsManager().getTransform(element);
            if (nSize > 0) {
                for (int i = 0; i < nSize; i++) {
                    Element mdelement = (Element) ncipoints.item(i);
                    x1 = new Integer(mdelement.getAttribute("x")).intValue();
                    y1 = new Integer(mdelement.getAttribute("y")).intValue();
                    double[] pointo = { dx + x1 * nw / sw, dy + y1 * nh / sh };

                    // 通过坐标变换，获取实际的坐标地址

                    initialTransform.transform(pointo, 0, pointo, 0, 1);
                    double dx2 = pointo[0];
                    double dy2 = pointo[1];
                    Point pp = new Point((int) dx2, (int) dy2);

                    if (point.distance(pp) > 3) {
                        items.add(new SelectionItem(handle, elements, handle
                                .getTransformsManager().getScaledPoint(pp,
                                        false), SelectionItem.POINT,
                                        SelectionItem.POINT_STYLE, 0, false, null));
                    } else {
                        items.clear();
                        items.add(new SelectionItem(handle, elements, handle
                                .getTransformsManager().getScaledPoint(pp,
                                        false), SelectionItem.POINT,
                                        SelectionItem.CENTER_POINT_STYLE, 0, false,
                                        null));
                        break;
                    }
                    // items.add(new SelectionItem(handle, elements, new Point(
                    // (int) dx2, (int) dy2), SelectionItem.POINT,
                    // SelectionItem.POINT_STYLE, 0, false, null));

                }
            }
            Selection selection = handle.getSelection();
            selection.refreshSelectElement(items);
        }
        return;
    }

    /*
     * 读取结点metadata数据，提取连接点信息，并重绘连接点
     */
    public void redrawLinkPoint(SVGHandle handle, Element element, Point2D point) {

        Set<Element> elements = new HashSet<Element>();
        elements.add(element);
        Set<SelectionItem> items = new HashSet<SelectionItem>();
        items.add(new SelectionItem(handle, elements, point,
                SelectionItem.POINT, SelectionItem.CENTER_POINT_STYLE, 0,
                false, null));

        Selection selection = handle.getSelection();
        selection.refreshSelectElement(items);

        return;
    }

    public boolean isLineShape(Element element) {
        if (element.getNodeName().equals("path")
                || element.getElementsByTagName("path").getLength() > 0)
            return true;

        if (element.getNodeName().equals("line")
                || element.getElementsByTagName("line").getLength() > 0)
            return true;

        if (element.getNodeName().equals("polyline")
                || element.getElementsByTagName("polyline").getLength() > 0)
            return true;
        return false;
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

                    modifiedPath.insertBefore(path);

                } else {

                    modifiedPath.insertAfter(path);
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
                    }
                };

                // the undo runnable
                Runnable undoRunnable = new Runnable() {

                    public void run() {

                        modifiedElement
                        .setAttribute(PathShape.dAtt, initDValue);
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

                // creating the svg element
                /*
                 * add by yuxiang
                 * 根据线条的两端点坐标，判定是否存在于image图元内，如果存在则判定是否与该image图元连接点相符，
                 * 如相符则在image中记录本线条的编号，本线条metadata保存该图元编号
                 */

                // strP0 = AppendRelaInfo(handle, p0, strLineID, "p0");
                // strP1 = AppendRelaInfo(handle, p1, strLineID, "p1");
                element = pathShapeModule.createElement(handle, path);
                String strLineID = element.getAttribute("id");
                if (isLinkFlag()) {
                    if(handle.getHandleType() == SVGHandle.HANDLE_TYPE_SVG)
                    {
                        Path lPath = new Path(path);
                        int nCount = lPath.getSegmentsNumber();
                        Segment s0 = lPath.getSegment();
                        Point2D p0 = s0.getEndPoint();
                        for (int i = 0; i < nCount - 1; i++)
                            s0 = s0.getNextSegment();
                        Point2D p1 = s0.getEndPoint();

                        Document doc = handle.getCanvas().getDocument();
                        Element metadata = doc
                        .createElement(Constants.NCI_SVG_METADATA);
                        element.appendChild(metadata);
                        Element cimClass = doc.createElement("PSR:CimClass");
                        cimClass.setAttribute("CimType", "link");
                        Element nciLink = doc.createElement("PSR:Nci_Link");
                        nciLink.setAttribute("MaxPinNum", "2");
                        nciLink.setAttribute("Plane", "0");
                        metadata.appendChild(cimClass);
                        metadata.appendChild(nciLink);
                        AppendRelaInfo(handle, element, nciLink, p0, strLineID,
                        "p0");
                        AppendRelaInfo(handle, element, nciLink, p1, strLineID,
                        "p1");
                    }

                    // if (strP0 != null)
                    // element.setAttribute("p0", strP0);
                    // if (strP1 != null)
                    // element.setAttribute("p1", strP1);
                }
                pathShapeModule.resetDrawing();
            }

            // selecting the element
            handle.getSelection().handleSelection(element, false, true);
        }

        reset(handle);
    }

    // TODO
    public void AppendRelaInfo(SVGHandle handle, Element element,
            Element nciLink, Point2D point, String strlID, String strPointType) {
        Set<Element> elements = handle.getSvgElementsManager().getNodeAt(
                handle.getCanvas().getDocument().getDocumentElement(), point,
                null, 1);
        if (elements == null || elements.size() == 0)
            return;
        String strRelaFormat = "Pin%dInfoVect%dLinkObjId";
        int nPointType = 0;
        if (strPointType.equals("p0")) {
            nPointType = 0;
        } else
            nPointType = 1;
        String strTemp, strRela;
        int i = 0;
        for (Element el : elements) {
            String strNodeName = el.getNodeName();
            String type = el.getAttribute("type");
            if(type == null || !type.equals("solidified"))
            {
                while (strNodeName.equals("g")) {
                    NodeList nodeList = el.getChildNodes();
                    for (int j = 0; j < nodeList.getLength(); j++) {
                        if (nodeList.item(j) instanceof Element) {
                            el = (Element) el.getChildNodes().item(j);
                            strNodeName = el.getNodeName();
                            break;
                        }
                    }
                }
            }
            String strID = el.getAttribute("id");
            if (!strID.equals(strlID) && strID.length() > 0) {
                strTemp = new String().format(strRelaFormat, nPointType, i);
                strRela = strID + "_0";
                if (strRela != null && strRela.length() > 0) {
                    nciLink.setAttribute(strTemp, strRela);
                    handle.getCanvas().addLinkPoint(strID, strlID, element,
                            strPointType, point);
                    i++;
                }
            }
        }
        return;
    }

    public String AppendRelaInfo(SVGHandle handle, Point2D point,
            String strlID, String lPointType) {
        String strUrl = new String();
        Element sElement = handle.getSvgElementsManager().getNodeAt(
                handle.getCanvas().getDocument().getDocumentElement(), point,
        "image");
        if (sElement != null && sElement.getNodeName().equals("image")) {
            String sPointID = existSymbolPoint(handle, sElement, point);
            if (sPointID != null) {
                String strSymbolID = sElement.getAttribute("id");
                strUrl = strSymbolID + ":" + sPointID;
                Element terminal = (Element) sElement.getElementsByTagName(
                "terminal").item(0);

                NodeList ncipoints = terminal.getElementsByTagName("nci:POINT");
                for (int i = 0; i < ncipoints.getLength(); i++) {
                    Element mdelement = (Element) ncipoints.item(i);
                    if (mdelement.getAttribute("name").equalsIgnoreCase(
                            sPointID)) {
                        String strTemp = mdelement.getAttribute("LineCount");
                        if (strTemp == null || strTemp.length() == 0)
                            strTemp = "0";
                        int nLineCount = new Integer(strTemp).intValue();
                        nLineCount++;
                        strTemp = new Integer(nLineCount).toString();
                        mdelement.setAttribute("LineCount", strTemp);

                        strTemp = strTemp.format("line%d", nLineCount - 1);
                        mdelement.setAttribute(strTemp, strlID + ":"
                                + lPointType);
                    }
                }
            }
        }
        return strUrl;
    }

    /**
     * resets the drawing action
     * 
     * @param handle
     *            the current svg handle
     */
    protected void reset(SVGHandle handle) {

        if (segments.size() > 0) {

            path.reset();
            wholePath.reset();
            drawnPath.reset();
            controlPointsPath.reset();
            points.clear();
            segments.clear();
            drawingPathToConnect = null;

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
            drawCircle(controlPointsPath, point);

        } else if (points.size() == 2) {

            // drawing the curve
            Point2D pt1 = points.get(0).getActionPoint();
            Point2D pt2 = points.get(1).getActionPoint();

            drawnPath.quadTo((float) pt1.getX(), (float) pt1.getY(),
                    (float) pt2.getX(), (float) pt2.getY());
            controlPointsPath.lineTo((float) pt1.getX(), (float) pt1.getY());
            drawCircle(controlPointsPath, pt1);
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
            drawCircle(controlPointsPath, pt2);

            controlPointsPath.moveTo((float) pt1.getX(), (float) pt1.getY());
            controlPointsPath.lineTo((float) pt3.getX(), (float) pt3.getY());
            drawCircle(controlPointsPath, pt3);
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
        ghostPainter.setPathShape(wholePath);
        ghostPainter.setCurrentSegmentShape(drawnPath);
        ghostPainter.setControlPointsShape(controlPointsPath);
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
    protected void drawCircle(ExtendedGeneralPath aPath, Point2D point) {

        int rx = 2;
        aPath.append(new Ellipse2D.Double(point.getX() - rx, point.getY() - rx,
                2 * rx, 2 * rx), false);
    }

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

        @Override
        public void paintToBeDone(Graphics2D g) {

            g = (Graphics2D) g.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            if (pathShape != null) {

                g.setColor(Color.blue);
                g.draw(pathShape);
            }

            if (currentSegmentShape != null) {

                g.setColor(Color.green);
                g.draw(currentSegmentShape);
            }

            if (controlPointsShape != null) {

                g.setColor(Color.gray);
                g.draw(controlPointsShape);
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
    }
}
