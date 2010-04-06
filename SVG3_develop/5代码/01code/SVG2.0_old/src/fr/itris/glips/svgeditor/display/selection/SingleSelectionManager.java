package fr.itris.glips.svgeditor.display.selection;

import java.util.*;

import com.nci.svg.other.*;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.*;

import org.w3c.dom.*;

import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.actions.clipboard.ClipboardModule;
import fr.itris.glips.svgeditor.display.canvas.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.display.undoredo.*;
import fr.itris.glips.svgeditor.shape.*;
import com.nci.svg.util.Utilities;
import fr.itris.glips.svgeditor.shape.path.PathShape;

/**
 * the class handling selections, translations and actions when only a single
 * element is selected
 * 
 * @author ITRIS, Jordi SUC
 */
public class SingleSelectionManager extends SelectionManager {

    /**
     * the canvas painter that is currently in use
     */
    private CanvasPainter currentCanvasPainter = null;

    /*
     * (non-Javadoc)
     * 
     * @see fr.itris.glips.svgeditor.display.selection.SelectionManager#copyElement()
     */
    @Override
    public void copyElement(Set<Element> elements,Point2D point) {
        if (currentCanvasPainter != null) {

            selection.getSVGHandle().getScrollPane().getSVGCanvas()
                    .removePaintListener(currentCanvasPainter, true);
        }
        Rectangle2D rectangle = MultiAbstractShape.getElementsBounds(
        		editor.getHandlesManager().getCurrentHandle(),
                elements);
        double x = point.getX() - rectangle.getMinX();
        double y = point.getY() - rectangle.getMinY();
        ClipboardModule clipModule = (ClipboardModule) editor
                .getModule("Clipboard");
        clipModule.copy();
        clipModule.paste(false, true,new Point2D.Double(x,y));

    }

    /**
     * the constructor of the class
     * 
     * @param selectionObject
     *            the selection object that uses this selection manager
     */
    public SingleSelectionManager(Selection selectionObject) {
    	super(selectionObject.getSVGHandle().getEditor());
        this.selection = selectionObject;
    }

    @Override
    public int getNextSelectionLevel(Set<Element> elements) {

        // getting the selected element
        Element element = elements.iterator().next();

        // getting the current selection level
        int currentSelectionLevel = selection.getSelectionLevel();

        // getting the shape module corresponding to the element
        AbstractShape shape = ShapeToolkit.getShapeModule(element,editor);

        if (shape != null) {

            // getting the max selection level for this module
            int maxSelectionLevel = shape.getLevelCount();

            // computing the new selection level
            return ((currentSelectionLevel + 1) % (maxSelectionLevel + 1));
        }

        return 0;
    }

    @Override
    public Set<SelectionItem> getSelectionItems(SVGHandle handle,
            Set<Element> elements, int level) {

        if (selection.isSelectionLocked()) {

            return getLockedSelectionItems(handle, elements);

        } else {

            // the selection items set that will be returned
            HashSet<SelectionItem> items = new HashSet<SelectionItem>();
            Element element = elements.iterator().next();

            // getting the shape module corresponding to this element
            AbstractShape shapeModule = ShapeToolkit.getShapeModule(element,editor);

            if (shapeModule != null) {

                items.addAll(shapeModule.getSelectionItems(handle, elements,
                        level));
            }

            return items;
        }
    }

    @Override
    public void doTranslateAction(Set<Element> elements, Point2D firstPoint,
            Point2D currentPoint) {

        // getting the element that will undergo the
        // action��singleSelectionManagerֻ���𵥸�Ԫ�ص�ƽ��
        Element element = elements.iterator().next();

        // getting the shape module corresponding to this action
        AbstractShape shape = ShapeToolkit.getShapeModule(element,editor);

        if (shape != null) {

            // executing the translate action//

            // removing the previous canvas
            // painter,��Ҫ���Ƴ�һ���������������һ���������Ų����ж���߿�ͬʱ���Ƶ����
            if (currentCanvasPainter != null) {

                selection.getSVGHandle().getScrollPane().getSVGCanvas()
                        .removePaintListener(currentCanvasPainter, false);
            }

            currentCanvasPainter = shape.showTranslateAction(selection
                    .getSVGHandle(), elements, firstPoint, currentPoint);

            if (currentCanvasPainter != null) {

                // repainting the canvas,�����ƶ�ʱ���߿�
                selection.getSVGHandle().getScrollPane().getSVGCanvas()
                        .addLayerPaintListener(SVGCanvas.DRAW_LAYER,
                                currentCanvasPainter, true);
            }
        }
    }

    @Override
    public void validateTranslateAction(Set<Element> elements,
            Point2D firstPoint, Point2D currentPoint) {

        // removing the current canvas painter
        if (currentCanvasPainter != null) {

            selection.getSVGHandle().getScrollPane().getSVGCanvas()
                    .removePaintListener(currentCanvasPainter, true);
        }
        Point2D fPoint;
        if ((int) firstPoint.getX() == -1 && (int) firstPoint.getY() == -1) {
            fPoint = new Point(0, 0);
        } else {
            // add by yuxiang
            // ���϶��������巶Χ���򱾴��϶���Ч
            Point2D currentSize = selection.getSVGHandle().getCanvas()
                    .getGeometryCanvasSize();
            fPoint = firstPoint;
            if (currentPoint.getX() < 0 || currentPoint.getY() < 0)
                return;
            else if (currentPoint.getX() > currentSize.getX()
                    || currentPoint.getY() > currentSize.getY())
                return;
        }

        // getting the element that will undergo the action
        Element element = elements.iterator().next();

        // getting the shape module corresponding to this action
        AbstractShape shape = ShapeToolkit.getShapeModule(element,editor);

        if (shape != null) {

            // validating the translate action
            UndoRedoAction undoRedoAction = shape.validateTranslateAction(
                    selection.getSVGHandle(), elements, fPoint, currentPoint);

            /*
             * add by yuxiang ��קʱ�Ĺ�����ϵ�任
             */

            if (undoRedoAction != null) {

                // adding the undo/redo action
                UndoRedoActionList actionlist = new UndoRedoActionList(
                        undoRedoAction.getName(), false);
                actionlist.add(undoRedoAction);
                selection.getSVGHandle().getUndoRedo().addActionList(
                        actionlist, true);
            }
        }
    }

    @Override
    public void doAction(Set<Element> elements, Point2D firstPoint,
            Point2D currentPoint, SelectionItem item) {

        // getting the element that will undergo the action
        Element element = elements.iterator().next();

        // getting the shape module corresponding to this action
        AbstractShape shape = ShapeToolkit.getShapeModule(element,editor);

        // executing the action
        if (shape != null) {
            if(shape instanceof GroupShape)
            {
                if(element.getAttribute("type").equals("solidified"))
                {
                    return;
                }
            }

            // removing the previous canvas painter
            if (currentCanvasPainter != null) {

                selection.getSVGHandle().getScrollPane().getSVGCanvas()
                        .removePaintListener(currentCanvasPainter, false);
            }

            currentCanvasPainter = shape.showAction(selection.getSVGHandle(),
                    selection.getSelectionLevel(), elements, item, firstPoint,
                    currentPoint);

            if (currentCanvasPainter != null) {

                // repainting the canvas
                selection.getSVGHandle().getScrollPane().getSVGCanvas()
                        .addLayerPaintListener(SVGCanvas.DRAW_LAYER,
                                currentCanvasPainter, true);
            }

            // if(element.getNodeName().equals("path"))
            // {
            // selection.redrawNciPoints(currentPoint);
            //			}
        }
    }

    @Override
    public void validateAction(Set<Element> elements, Point2D firstPoint,
            Point2D currentPoint, SelectionItem item) {

        //removing the current canvas painter
        if (currentCanvasPainter != null) {

            selection.getSVGHandle().getScrollPane().getSVGCanvas()
                    .removePaintListener(currentCanvasPainter, true);
        }

        //getting the element that will undergo the action
        Element element = elements.iterator().next();

        //getting the shape module corresponding to this action
        AbstractShape shape = ShapeToolkit.getShapeModule(element,editor);

        if (shape != null) {
            if(shape instanceof GroupShape)
            {
                if(element.getAttribute("type").equals("solidified"))
                {
                    return;
                }
            }

            //validating the action
            UndoRedoAction undoRedoAction = shape.validateAction(selection
                    .getSVGHandle(), selection.getSelectionLevel(), elements,
                    item, firstPoint, currentPoint);

            /*add by yuxiang
             * ���ڵ���ͼԪ��������С�ߴ�ͽǶȣ���ʱ�޸���Ϣ
             */
            if (undoRedoAction != null) {

                //adding the undo/redo action
                UndoRedoActionList actionlist = new UndoRedoActionList(
                        undoRedoAction.getName(), false);
                actionlist.add(undoRedoAction);
                selection.getSVGHandle().getUndoRedo().addActionList(
                        actionlist, true);
            }
        }
    }
}
