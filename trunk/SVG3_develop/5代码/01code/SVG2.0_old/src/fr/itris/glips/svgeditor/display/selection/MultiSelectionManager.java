package fr.itris.glips.svgeditor.display.selection;

import java.util.*;

import org.w3c.dom.*;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.*;

import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.actions.clipboard.ClipboardModule;
import fr.itris.glips.svgeditor.display.canvas.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.display.undoredo.*;
import fr.itris.glips.svgeditor.shape.*;

/**
 * the class handling selections, translations and actions when several elements are selected
 * @author ITRIS, Jordi SUC
 */
public class MultiSelectionManager extends SelectionManager{
	
	/**
	 * the shape handler that deals with all the selections and actions that can be done on a multiple selection
	 */
	private ManyShape manyShape;
	
	/**
	 * the canvas painter that is currently in use
	 */
	private CanvasPainter currentCanvasPainter=null;
	
	/* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.display.selection.SelectionManager#copyElement()
     */
    @Override
    public void copyElement(Set<Element> elements,Point2D point) {
        if (currentCanvasPainter != null) {

            selection.getSVGHandle().getScrollPane().getSVGCanvas()
                    .removePaintListener(currentCanvasPainter, true);
        }
        Rectangle2D rectangle = MultiAbstractShape.getElementsBounds(
        		selection.getSVGHandle(),
                elements);
        double x = point.getX() - rectangle.getMinX();
        double y = point.getY() - rectangle.getMinY();
        ClipboardModule clipModule = (ClipboardModule) selection.getSVGHandle().getEditor()
                .getModule("Clipboard");
        clipModule.copy();
        clipModule.paste(false, true,new Point2D.Double(x,y));
        
    }

    /**
	 * the constructor of the class
	 * @param selectionObject the selection object that uses this selection manager
	 */
	public MultiSelectionManager(Selection selectionObject){
		super(selectionObject.getSVGHandle().getEditor());
		this.selection=selectionObject;
		manyShape=new ManyShape(selection.getSVGHandle().getEditor());
	}
	
	@Override
	public int getNextSelectionLevel(Set<Element> elements) {

		//getting the current selection level
		int currentSelectionLevel=selection.getSelectionLevel();
		
		//getting the max selection level for this module
		int maxSelectionLevel=manyShape.getLevelCount();
		
		//computing the new selection level
		return ((currentSelectionLevel+1)%(maxSelectionLevel+1));
	}

	@Override
	public Set<SelectionItem> getSelectionItems(
			SVGHandle handle, Set<Element> elements, int level){
		
		if(selection.isSelectionLocked()){
			
			return getLockedSelectionItems(handle, elements);
			
		}else{
			
			return manyShape.getSelectionItems(handle, elements, level);
		}
	}
	
	@Override
	public void doTranslateAction(Set<Element> elements, 
			Point2D firstPoint, Point2D currentPoint) {

		//executing the translate  action//
		
		//removing the previous canvas painter
		if(currentCanvasPainter!=null){
			
			selection.getSVGHandle().getScrollPane().getSVGCanvas().
				removePaintListener(currentCanvasPainter, false);
		}
		
		currentCanvasPainter=manyShape.showTranslateAction(
				selection.getSVGHandle(), elements, firstPoint, currentPoint);
		
		if(currentCanvasPainter!=null){
			
			//repainting the canvas
			selection.getSVGHandle().getScrollPane().getSVGCanvas().
				addLayerPaintListener(SVGCanvas.DRAW_LAYER, currentCanvasPainter, true);
		}
	}

	@Override
	public void validateTranslateAction(Set<Element> elements, 
			Point2D firstPoint, Point2D currentPoint) {

		//removing the current canvas painter
		if(currentCanvasPainter!=null){
			
			selection.getSVGHandle().getScrollPane().getSVGCanvas().
				removePaintListener(currentCanvasPainter, true);
		}
		//add by yuxiang
        //当拖动超出画板范围，则本次拖动无效
		Point2D fPoint;
        if ((int) firstPoint.getX() == -1 && (int) firstPoint.getY() == -1) {
            fPoint = new Point(0, 0);
        } else {
            // add by yuxiang
            // 当拖动超出画板范围，则本次拖动无效
            Point2D currentSize = selection.getSVGHandle().getCanvas()
                    .getGeometryCanvasSize();
            fPoint = firstPoint;
            if (currentPoint.getX() < 0 || currentPoint.getY() < 0)
                return;
            else if (currentPoint.getX() > currentSize.getX()
                    || currentPoint.getY() > currentSize.getY())
                return;
        }
		//validating the translate action
		UndoRedoAction undoRedoAction=manyShape.validateTranslateAction(
				selection.getSVGHandle(), elements, fPoint, currentPoint);	
		
		if(undoRedoAction!=null){
			
			//adding the undo/redo action
			UndoRedoActionList actionlist=new UndoRedoActionList(undoRedoAction.getName(), false);
			actionlist.add(undoRedoAction);
			selection.getSVGHandle().getUndoRedo().addActionList(actionlist, true);
		}
	}
	
	@Override
	public void doAction(Set<Element> elements, Point2D firstPoint, 
			Point2D currentPoint, SelectionItem item) {
		
		//executing the action//
		
		//removing the previous canvas painter
		if(currentCanvasPainter!=null){
			
			selection.getSVGHandle().getScrollPane().getSVGCanvas().
				removePaintListener(currentCanvasPainter, false);
		}
		
		currentCanvasPainter=manyShape.showAction(
			selection.getSVGHandle(), selection.getSelectionLevel(), 
				elements, item, firstPoint, currentPoint);
		
		if(currentCanvasPainter!=null){
			
			//repainting the canvas
			selection.getSVGHandle().getScrollPane().getSVGCanvas().
				addLayerPaintListener(SVGCanvas.DRAW_LAYER, currentCanvasPainter, true);
		}
	}
	
	@Override
	public void validateAction(Set<Element> elements, Point2D firstPoint, 
			Point2D currentPoint, SelectionItem item) {
		
		//removing the current canvas painter
		if(currentCanvasPainter!=null){
			
			selection.getSVGHandle().getScrollPane().getSVGCanvas().
				removePaintListener(currentCanvasPainter, true);
		}
		
		//validating the action
		UndoRedoAction undoRedoAction=manyShape.validateAction(
				selection.getSVGHandle(), selection.getSelectionLevel(), 
					elements, item, firstPoint, currentPoint);
		
		if(undoRedoAction!=null){
			
			//adding the undo/redo action
			UndoRedoActionList actionlist=new UndoRedoActionList(undoRedoAction.getName(), false);
			actionlist.add(undoRedoAction);
			selection.getSVGHandle().getUndoRedo().addActionList(actionlist, true);
		}
	}
}
