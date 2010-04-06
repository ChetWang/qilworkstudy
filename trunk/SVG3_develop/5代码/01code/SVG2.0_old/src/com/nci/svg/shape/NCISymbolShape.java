package com.nci.svg.shape;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Element;

import com.nci.svg.other.UpholdElementRela;

import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.display.canvas.CanvasPainter;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.display.selection.Selection;
import fr.itris.glips.svgeditor.display.selection.SelectionItem;
import fr.itris.glips.svgeditor.display.undoredo.UndoRedoAction;
import fr.itris.glips.svgeditor.shape.RectangularShape;
import fr.itris.glips.svgeditor.shape.ShapeToolkit;

public class NCISymbolShape extends RectangularShape{

	/* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.shape.AbstractShape#getRotationSkewCenterPoint(fr.itris.glips.svgeditor.display.handle.SVGHandle, org.w3c.dom.Element)
     */
    @Override
    public Point2D getRotationSkewCenterPoint(SVGHandle handle, Element element) {
        // TODO Auto-generated method stub
        Rectangle2D bounds = getTransformedShape(handle, element, false).getBounds2D();

        return getRotationSkewCenterPoint(handle, bounds);
    }

    /* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.shape.AbstractShape#getTransformedShape(fr.itris.glips.svgeditor.display.handle.SVGHandle, org.w3c.dom.Element, boolean)
     */
    @Override
    public Shape getTransformedShape(SVGHandle handle, Element element,
            boolean isOutline) {
        Shape shape = getShape(handle, element, isOutline);

        if (shape != null) {

            // getting the transformation of this element
            AffineTransform af = handle.getSvgElementsManager().parseTransform(element);
            if(af != null)
                af = new AffineTransform(af);
            if (af == null) {

                af = new AffineTransform();
            }

            // transforming this shape
            if (!af.isIdentity()) {

//                if(!element.getNodeName().equals("use"))
                     shape = af.createTransformedShape(shape);
            }
        }

        return shape;
    }

    /* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.shape.RectangularShape#showTranslateAction(fr.itris.glips.svgeditor.display.handle.SVGHandle, java.util.Set, java.awt.geom.Point2D, java.awt.geom.Point2D)
     */
    @Override
    public CanvasPainter showTranslateAction(SVGHandle handle,
            Set<Element> elementSet, Point2D firstPoint, Point2D currentPoint) {
        CanvasPainter canvasPainter=null;
        
        //getting the element that will undergo the action
        Element element=elementSet.iterator().next();
        
        //getting the rectangle shape
        Shape shape=getShape(handle, element, true);

        //getting the translation factors
        Point2D translationCoefficients=
            new Point2D.Double(currentPoint.getX()-firstPoint.getX(), 
                    currentPoint.getY()-firstPoint.getY());
        
        //getting the shape transform
        AffineTransform transform=
            handle.getSvgElementsManager().parseTransform(element);
        
        //creating the translate transform
        AffineTransform translateTransform=AffineTransform.getTranslateInstance(
                translationCoefficients.getX(), translationCoefficients.getY());
        
        //concatenating the transforms
        transform.preConcatenate(translateTransform);
        
        //computing the screen scaled shape
        shape=handle.getTransformsManager().
            getScaledShape(shape, false, transform);
        
        final Shape fshape=shape;
        
        //creating the set of the clips
        final Set<Rectangle2D> fclips=new HashSet<Rectangle2D>();
        fclips.add(shape.getBounds2D());

        canvasPainter=new CanvasPainter(){

            @Override
            public void paintToBeDone(Graphics2D g) {
                
                g=(Graphics2D)g.create();
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g.setColor(strokeColor);
                g.draw(fshape);
                g.dispose();
            }
            
            @Override
            public Set<Rectangle2D> getClip() {

                return fclips;
            }
        };
        
        return canvasPainter;
    }

    /* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.shape.RectangularShape#modifyRadius(fr.itris.glips.svgeditor.display.handle.SVGHandle, org.w3c.dom.Element, java.awt.geom.Point2D)
     */
    @Override
    public UndoRedoAction modifyRadius(SVGHandle handle, Element element,
            Point2D radius) {
        // TODO Auto-generated method stub
        return super.modifyRadius(handle, element, radius);
    }

    /* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.shape.RectangularShape#resize(fr.itris.glips.svgeditor.display.handle.SVGHandle, java.util.Set, java.awt.geom.AffineTransform)
     */
    @Override
    public UndoRedoAction resize(final SVGHandle handle, Set<Element> elementSet,
            AffineTransform transform) {
        UndoRedoAction undoRedoAction=null;
        Runnable executeRunnable=null, undoRunnable=null;
        final Element element=elementSet.iterator().next();
        //getting the transform of the current node
        final AffineTransform initialTransform=
            handle.getSvgElementsManager().parseTransform(element);
        
        if(initialTransform.isIdentity()){
            
            //getting the current element bounds
            final java.awt.geom.RectangularShape shape=
                (java.awt.geom.RectangularShape)getShape(handle, element, false);
            
            //transforming the rectangle and getting its new bounds
            final java.awt.geom.RectangularShape newShape=
                transform.createTransformedShape(shape).getBounds2D();
            final boolean bSingleFlag = (handle.getSelection().getSelectedElements().size() > 1?false:true);
            
            executeRunnable=new Runnable() {

                public void run() {

                    setShape(handle, element, newShape);
                    if (bSingleFlag && (element.getNodeName().equals("image")|| element.getNodeName().equals("use")))
                    {
                        UpholdElementRela rela = new UpholdElementRela(handle.getCanvas().getLpManager());
                        rela.upholdElement(handle, element, UpholdElementRela.MODIFY_TYPE);
                    }
                    refresh(element);
                    editor.getSvgSession().refreshHandle();
                }
            };
            
            //the undo runnable
            undoRunnable=new Runnable() {

                public void run() {

                    setShape(handle, element, shape);
                    if (bSingleFlag && (element.getNodeName().equals("image") || element.getNodeName().equals("use")))
                    {
                        UpholdElementRela rela = new UpholdElementRela(handle.getCanvas().getLpManager());
                        rela.upholdElement(handle, element, UpholdElementRela.MODIFY_TYPE);
                    }
                    refresh(element);
                    editor.getSvgSession().refreshHandle();
                }
            };
            
        }else{
            
            //computing the new affine transform for the element
            final AffineTransform newTransform=new AffineTransform(initialTransform);
            newTransform.preConcatenate(transform);
            
            final boolean bSingleFlag = (handle.getSelection().getSelectedElements().size() > 1?false:true);
            //setting the transform attribute
            executeRunnable=new Runnable() {

                public void run() {

                    handle.getSvgElementsManager().
                        setTransform(element, newTransform);
                    if (bSingleFlag && (element.getNodeName().equals("image")|| element.getNodeName().equals("use")))
                    {
                        UpholdElementRela rela = new UpholdElementRela(handle.getCanvas().getLpManager());
                        rela.upholdElement(handle, element, UpholdElementRela.MODIFY_TYPE);
                    }
                    refresh(element);
                    editor.getSvgSession().refreshHandle();
                }
            };
            
            //the undo runnable
            undoRunnable=new Runnable() {

                public void run() {
                    
                    handle.getSvgElementsManager().
                        setTransform(element, initialTransform);
                    if (bSingleFlag && (element.getNodeName().equals("image")|| element.getNodeName().equals("use")))
                    {
                        UpholdElementRela rela = new UpholdElementRela(handle.getCanvas().getLpManager());
                        rela.upholdElement(handle, element, UpholdElementRela.MODIFY_TYPE);
                    }
                    refresh(element);
                    editor.getSvgSession().refreshHandle();
                }
            };
        }
        
        //executing the action and creating the undo/redo action
        HashSet<Element> elements=new HashSet<Element>();
        elements.add(element);
        undoRedoAction=ShapeToolkit.getUndoRedoAction(
                resizeUndoRedoLabel, executeRunnable, undoRunnable, elements);

        return undoRedoAction;
    }

    /* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.shape.RectangularShape#rotate(fr.itris.glips.svgeditor.display.handle.SVGHandle, java.util.Set, java.awt.geom.Point2D, double)
     */
    @Override
    public UndoRedoAction rotate(final SVGHandle handle, Set<Element> elementSet,
            Point2D centerPoint, double angle) {

        final Element element=elementSet.iterator().next();

        //getting the rotation affine transform
        AffineTransform actionTransform=
            AffineTransform.getRotateInstance(angle, centerPoint.getX(), centerPoint.getY());
        
        //getting the element's transform
        final AffineTransform previousElementTransform=
            handle.getSvgElementsManager().parseTransform(element);
        
        //concatenating the two transforms
        final AffineTransform newElementTransform=
            new AffineTransform(previousElementTransform);
        newElementTransform.preConcatenate(actionTransform);

        final boolean bSingleFlag = (handle.getSelection().getSelectedElements().size() > 1?false:true);
        //setting the new transform for the element
        Runnable executeRunnable=new Runnable() {

            public void run() {

                handle.getSvgElementsManager().
                    setTransform(element, newElementTransform);
                if (bSingleFlag && (element.getNodeName().equals("image")|| element.getNodeName().equals("use")))
                {
                    UpholdElementRela rela = new UpholdElementRela(handle.getCanvas().getLpManager());
                    rela.upholdElement(handle, element, UpholdElementRela.MODIFY_TYPE);
                }
                refresh(element);
                editor.getSvgSession().refreshHandle();
            }
        };
        
        //the undo runnable
        Runnable undoRunnable=new Runnable() {

            public void run() {

                handle.getSvgElementsManager().
                    setTransform(element, previousElementTransform);
                if (bSingleFlag && (element.getNodeName().equals("image")|| element.getNodeName().equals("use")))
                {
                    UpholdElementRela rela = new UpholdElementRela(handle.getCanvas().getLpManager());
                    rela.upholdElement(handle, element, UpholdElementRela.MODIFY_TYPE);
                }
                refresh(element);
                editor.getSvgSession().refreshHandle();
            }
        };
        
        //executing the action and creating the undo/redo action
        HashSet<Element> elements=new HashSet<Element>();
        elements.add(element);
        UndoRedoAction undoRedoAction=ShapeToolkit.getUndoRedoAction(
                rotateUndoRedoLabel, executeRunnable, undoRunnable, elements);

        return undoRedoAction;
    }

    /* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.shape.RectangularShape#skew(fr.itris.glips.svgeditor.display.handle.SVGHandle, java.util.Set, java.awt.geom.Point2D, double, boolean)
     */
    @Override
    public UndoRedoAction skew(final SVGHandle handle, Set<Element> elementSet,
            Point2D centerPoint, double skewFactor, boolean isHorizontal) {

        final Element element=elementSet.iterator().next();

        //getting the skew affine transform
        AffineTransform actionTransform=ShapeToolkit.getSkewAffineTransform(
                handle, element, centerPoint, skewFactor, isHorizontal);
        
        //getting the element's transform
        final AffineTransform previousElementTransform=
            handle.getSvgElementsManager().parseTransform(element);
        
        //concatenating the two transforms
        final AffineTransform newElementTransform=
            new AffineTransform(previousElementTransform);
        newElementTransform.preConcatenate(actionTransform);
        final boolean bSingleFlag = (handle.getSelection().getSelectedElements().size() > 1?false:true);
        //setting the new transform for the element
        Runnable executeRunnable=new Runnable() {

            public void run() {

                handle.getSvgElementsManager().
                    setTransform(element, newElementTransform);
                if (bSingleFlag && (element.getNodeName().equals("image")|| element.getNodeName().equals("use")))
                {
                    UpholdElementRela rela = new UpholdElementRela(handle.getCanvas().getLpManager());
                    rela.upholdElement(handle, element, UpholdElementRela.MODIFY_TYPE);
                }
                refresh(element);
                editor.getSvgSession().refreshHandle();
            }
        };
        
        //the undo runnable
        Runnable undoRunnable=new Runnable() {

            public void run() {

                handle.getSvgElementsManager().
                    setTransform(element, previousElementTransform);
                if (bSingleFlag && (element.getNodeName().equals("image")|| element.getNodeName().equals("use")))
                {
                    UpholdElementRela rela = new UpholdElementRela(handle.getCanvas().getLpManager());
                    rela.upholdElement(handle, element, UpholdElementRela.MODIFY_TYPE);
                }
                refresh(element);
                editor.getSvgSession().refreshHandle();
            }
        };
        
        //executing the action and creating the undo/redo action
        HashSet<Element> elements=new HashSet<Element>();
        elements.add(element);
        UndoRedoAction undoRedoAction=ShapeToolkit.getUndoRedoAction(
                skewUndoRedoLabel, executeRunnable, undoRunnable, elements);

        return undoRedoAction;
    }

    /* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.shape.RectangularShape#translate(fr.itris.glips.svgeditor.display.handle.SVGHandle, java.util.Set, java.awt.geom.Point2D)
     */
    @Override
    public UndoRedoAction translate(SVGHandle handle, Set<Element> elementSet,
            Point2D translationFactors) {
        // TODO Auto-generated method stub
        return super.translate(handle, elementSet, translationFactors);
    }

    /* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.shape.RectangularShape#validateAction(fr.itris.glips.svgeditor.display.handle.SVGHandle, int, java.util.Set, fr.itris.glips.svgeditor.display.selection.SelectionItem, java.awt.geom.Point2D, java.awt.geom.Point2D)
     */
    @Override
    public UndoRedoAction validateAction(SVGHandle handle, int level,
            Set<Element> elementSet, SelectionItem item, Point2D firstPoint,
            Point2D lastPoint) {
        //the undo/redo action that will be returned
        UndoRedoAction undoRedoAction=null;

        //getting the element that will undergo the action
        Element element=elementSet.iterator().next();
        
        //executing the accurate action
        switch (level){
        
            case 0 :
                
                //getting the resize transform for this action
                AffineTransform resizeTransform=
                    getResizeTransform(handle, element, item, firstPoint, lastPoint);
                
                //executing the resize action
                undoRedoAction=resize(handle, elementSet, resizeTransform);
                break;
                
            case 1 :
                
                if(item.getType()!=SelectionItem.CENTER){

                    //getting the center point
                    Point2D centerPoint=getRotationSkewCenterPoint(handle, element);

                    if(item.getType()==SelectionItem.NORTH_WEST || 
                        item.getType()==SelectionItem.NORTH_EAST ||
                        item.getType()==SelectionItem.SOUTH_EAST ||
                        item.getType()==SelectionItem.SOUTH_WEST){
                        
                        //getting the angle for the rotation
                        double angle=ShapeToolkit.getRotationAngle(
                                centerPoint, firstPoint, lastPoint);
                        
                        //executing the rotation action
                        undoRedoAction=rotate(handle, elementSet, centerPoint, angle);

                    }else{

                        //getting the skew factor and whether it's horizontal or not
                        boolean isHorizontal=(item.getType()==SelectionItem.NORTH || 
                                item.getType()==SelectionItem.SOUTH);
                        double skewFactor=0;
                        
                        if(isHorizontal){
                            
                            skewFactor=lastPoint.getX()-firstPoint.getX();
                            
                        }else{
                            
                            skewFactor=lastPoint.getY()-firstPoint.getY();
                        }
                        
                        //executing the skew action
                        undoRedoAction=skew(handle, elementSet, centerPoint, skewFactor, isHorizontal);
                    }

                    rotationSkewSelectionItemCenterPoint=null;
                    
                }else{
                    
                    rotationSkewCenterPoint=rotationSkewSelectionItemCenterPoint;
                    rotationSkewSelectionItemCenterPoint=null;
                }

                break;
                
            case 2 :
                
                //getting the new radius for the shape
                Point2D newRx=getRadius(handle, element, lastPoint, item);
                undoRedoAction=modifyRadius(handle, element, newRx);
                break;
        }

        return undoRedoAction;
    }

    /* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.shape.RectangularShape#showAction(fr.itris.glips.svgeditor.display.handle.SVGHandle, int, java.util.Set, fr.itris.glips.svgeditor.display.selection.SelectionItem, java.awt.geom.Point2D, java.awt.geom.Point2D)
     */
    @Override
    public CanvasPainter showAction(SVGHandle handle, int level,
            Set<Element> elementSet, SelectionItem item, Point2D firstPoint,
            Point2D currentPoint) {
        //getting the element that will undergo the action
        Element element=elementSet.iterator().next();

        //the canvas painter that should be returned
        CanvasPainter painter=null;
        
        //whether the shape should be painted
        boolean canPaintShape=true;
        
        //whether a transform is computed for this action
        boolean useTranform=true;
        
        //the new rx values for the rectangle
        Point2D newRx=null;

        //getting the action transform
        AffineTransform actionTransform=null;
        
        switch (level){
        
            case 0 :
                
                //getting the resize transform
                actionTransform=getResizeTransform(
                        handle, element, item, firstPoint, currentPoint);
                break;
                
            case 1 :
                
                if(item.getType()==SelectionItem.CENTER){

                    //storing the center point for the rotate action
                    rotationSkewSelectionItemCenterPoint=currentPoint;
                    item.setPoint(currentPoint);
                    canPaintShape=false;
                    
                }else if(item.getType()==SelectionItem.NORTH_WEST || 
                                item.getType()==SelectionItem.NORTH_EAST ||
                                item.getType()==SelectionItem.SOUTH_EAST ||
                                item.getType()==SelectionItem.SOUTH_WEST){
                    
                    //getting the rotation transform
                    actionTransform=getRotationTransform(handle, element, firstPoint, currentPoint);
                    
                }else{
                    
                    //getting the skew transform
                    actionTransform=getSkewTransform(handle, element, firstPoint, currentPoint, item);
                }
                
                break;
                
            case 2 :
                
                //getting the new radius for the shape
                newRx=getRadius(handle, element, currentPoint, item);
                useTranform=false;
                break;
        }
        
        if(canPaintShape){
            
            Shape shape=null;
            
            if(useTranform && actionTransform!=null){
                
                //creating the shape that will be painted
                shape=getShape(handle, element, true);
                
                //creating the transform of this element
                AffineTransform transform=
                    handle.getSvgElementsManager().parseTransform(element);
                
                //concatenating the action transform to the element's transform
                transform.preConcatenate(actionTransform);
                
                //scaling the shape to fit the canvas
                shape=handle.getTransformsManager().
                    getScaledShape(shape, false, transform);
                
            }else if(newRx!=null){
                
                //getting the shape that will be painted
                shape=getModifiedRadiusShape(handle, element, newRx);

                //getting the element's transform
                AffineTransform transform=
                    handle.getSvgElementsManager().parseTransform(element);
                
                //scaling the shape to fit the canvas
                shape=handle.getTransformsManager().
                    getScaledShape(shape, false, transform);
            }

            final Shape fshape=shape;
            
            //creating the set of the clips
            final HashSet<Rectangle2D> fclips=new HashSet<Rectangle2D>();
            fclips.add(fshape.getBounds2D());
            
            painter=new CanvasPainter(){

                @Override
                public void paintToBeDone(Graphics2D g) {
                    
                    g=(Graphics2D)g.create();
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                            RenderingHints.VALUE_ANTIALIAS_ON);
                    g.setColor(strokeColor);
                    g.draw(fshape);
                    g.dispose();
                }
                
                @Override
                public Set<Rectangle2D> getClip() {

                    return fclips;
                }
            };
        }
        
        return painter;
    }

    public NCISymbolShape(Editor editor) {
		super(editor);
		shapeModuleId="NCISymbolShape";
		handledElementTagName="use";
	}
	
	@Override
	public int getLevelCount() {

		return 2;
	}
	
	@Override
	public Element createElement(SVGHandle handle, Rectangle2D bounds) {
		return null;
	}

    /* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.shape.RectangularShape#getSelectionItems(fr.itris.glips.svgeditor.display.handle.SVGHandle, java.util.Set, int)
     */
    @Override
    public Set<SelectionItem> getSelectionItems(SVGHandle handle,
            Set<Element> elements, int level) {
        //clearing the stored values
        rotationSkewCenterPoint=null;

        // getting the first element of this set
        Element element=elements.iterator().next();
        
        // the set of the items that will be returned
        Set<SelectionItem> items=new HashSet<SelectionItem>();
        
        // getting the bounds of the element
        Rectangle2D bounds=getTransformedShape(handle, element, false).getBounds2D();
    
        // scaling the bounds in the canvas space
        Rectangle2D scaledWholeBounds=handle.getTransformsManager().
            getScaledRectangle(bounds, false);
        
        // getting the selection items according to the level type
        //点选图元引用后，显示连接点的位置
        switch (level){
            
            case Selection.SELECTION_LEVEL_DRAWING :
            case Selection.SELECTION_LEVEL_1 :
                
                items.addAll(getResizeSelectionItems(handle, elements,
                    scaledWholeBounds));
            //*
            if (elements.size() == 1) {
                // 增加连接点位置显示
                String strTagName = element.getTagName();
                if (strTagName.equals("image")) {
                    // 图元引用
                    Set<SelectionItem> itemset = getPointSelectionItems(handle, elements);
                    if(itemset != null)
                        items.addAll(itemset);

                }
            }
            //*/
            break;
                
            case Selection.SELECTION_LEVEL_2 :
                
                items.addAll(getRotateSelectionItems(handle, elements, scaledWholeBounds));
                if(elements.size() ==1)
                {
                 // 增加连接点位置显示
                    String strTagName = element.getTagName();
                    if (strTagName.equals("image")) {
                        // 图元引用
                        Set<SelectionItem> itemset = getPointSelectionItems(handle, elements);
                        if(itemset != null)
                            items.addAll(itemset);

                    }
                }
                break;
                
            case Selection.SELECTION_LEVEL_3 :
                
                items.addAll(getRadiusSelectionItems(handle, elements, scaledWholeBounds));
                break;
        }
        
        return items;
    }



}
