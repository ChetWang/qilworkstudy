package fr.itris.glips.svgeditor.shape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.nci.svg.other.UpholdElementRela;
import com.nci.svg.shape.GraphUnitImageShape;

import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.EditorToolkit;
import fr.itris.glips.svgeditor.display.canvas.CanvasPainter;
import fr.itris.glips.svgeditor.display.canvas.SVGCanvas;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.display.selection.Selection;
import fr.itris.glips.svgeditor.display.selection.SelectionItem;
import fr.itris.glips.svgeditor.display.undoredo.UndoRedoAction;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * the superclass of all the module that handle rectangular shapes
 * 抽象的“矩形”形状图形，包括圆、椭圆、长方形（正方形）、外部图像等。这里的“矩形”指在画布上逻辑处理的概念，
 * 说明无论展现形式是什么形状，这些图像实际上是占据了一个矩形的空间。
 * @author ITRIS, Jordi SUC
 */
public abstract class RectangularShape extends AbstractShape {
	
	/**
	 * the element attributes names，各个变量名称
	 */
	protected static String xAtt="x", yAtt="y", wAtt="width", 
		hAtt="height", rxAtt="rx", ryAtt="ry";

	/**
	 * the first point of the drawing action，绘图的第一个点
	 */
	protected Point2D firstDrawingPoint;
	
	/**
	 * the painter drawing ghost rectangles on the canvas，绘制虚图的绘制器,指从面板中拖拽过来的基本图元或自定义图元在未完全绘制在画布前的形状。
	 */
	protected GhostShapeCanvasPainter ghostCanvasPainter=new GhostShapeCanvasPainter();
	
	/**
	 * the label for the undo/redo action of the modification
	 * of the radius attribute of a rectangle.
	 * 修改后的标记
	 */
	protected String radiusModificationUndoRedoLabel="";
	
	/**
	 * the constructor of the class
	 * @param editor the editor
	 */
	public RectangularShape(Editor editor) {
		
		super(editor);
	}
	
	@Override
	public int getLevelCount() {
		
		return 1;
	}

	@Override
	public void notifyDrawingAction(
			SVGHandle handle, Point2D point, int modifier, int type) {

		//getting the canvas
		SVGCanvas canvas=handle.getScrollPane().getSVGCanvas();
		
		//according to type of the event for the drawing action
		switch (type) {
			
			case DRAWING_MOUSE_PRESSED :
				
				if(firstDrawingPoint==null) {
					
					firstDrawingPoint=point;
				}

				break;
				
			case DRAWING_MOUSE_RELEASED :
				
				// 图元绘制情况下，不允许另外的图元
//				if (this  instanceof GraphUnitImageShape
//						&& handle.getHandleType() != SVGHandle.HANDLE_TYPE_SVG) {
//					JOptionPane.showConfirmDialog(handle.getCanvas(),
//							ResourcesManager.bundle
//							.getString("nci_graphunit_no_graphunit_inside"), ResourcesManager.bundle
//									.getString("nci_optionpane_infomation_title"), JOptionPane.CLOSED_OPTION,
//							JOptionPane.INFORMATION_MESSAGE);
//					canvas.removePaintListener(ghostCanvasPainter, true);
//					editor.getSelectionManager().setToRegularMode();
//					break;
//				}
				//
				//computing the rectangle corresponding the first and current clicked points
				Rectangle2D rect=null;
				
				if(editor.getSquareModeManager().isSquareMode()){
					
					rect=EditorToolkit.getComputedSquare(firstDrawingPoint, point);
					
				}else{
					
					rect=EditorToolkit.getComputedRectangle(firstDrawingPoint, point);
				}

				//computing the base scaled rectangle 
				Rectangle2D rect2D=handle.getTransformsManager().
					getScaledRectangle(rect,  true);
			
			    //added by wangql
				if(rect2D.getHeight()==0 || rect2D.getWidth()==0){
					rect2D.setRect(rect2D.getX(), rect2D.getY(), 32, 32);
				}
				//creating the svg rectangle shape
				createElement(handle, rect2D);
				
				//reinitializing the data
				firstDrawingPoint=null;
				canvas.removePaintListener(ghostCanvasPainter, true);
				ghostCanvasPainter.reinitialize();
				resetDrawing();
				handleShapeBusiness();
				break;
				
			case DRAWING_MOUSE_DRAGGED :
				
				//removing the ghost canvas painter
				if(ghostCanvasPainter.getClip()!=null) {
					
					canvas.removePaintListener(ghostCanvasPainter, false);
				}

				//computing the rectangle corresponding the first and current clicked points
				if(editor.getSquareModeManager().isSquareMode()){
					
					rect=EditorToolkit.getComputedSquare(firstDrawingPoint, point);
					
				}else{
					
					rect=EditorToolkit.getComputedRectangle(firstDrawingPoint, point);
				}

				//setting the new rectangle to the ghost painter
				ghostCanvasPainter.setShape(rect);
				canvas.addLayerPaintListener(SVGCanvas.DRAW_LAYER, ghostCanvasPainter, true);
				break;
		}
	}

	/**
	 * creates a rectangular element by specifiying its bounds。
	 * 创建一个类“矩形”框架
	 * @param handle the current svg handle
	 * @param bounds the bounds of the element
	 * @return the created svg element
	 */
	public Element createElement(SVGHandle handle, Rectangle2D bounds){
		
		return null;
	}

	@Override
	public Set<SelectionItem> getSelectionItems(
			SVGHandle handle, Set<Element> elements, int level) {

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
	
	/**根据传入的节点信息，遍历连接点信息，如存在则在相应的位置绘制连接点，支持图元缩放，转换等功能
	 * @param handle：绘制句柄
	 * @param element：图元节点
	 * @return：连接点绘制信息
	 * by yuxiang
	 */
	public Set<SelectionItem> getPointSelectionItems(SVGHandle handle,
	        Set<Element> elements) {
	    Element element=elements.iterator().next();
        Set<SelectionItem> items = new HashSet<SelectionItem>();
        Element terminal = (Element)element.getElementsByTagName("terminal").item(0);
        if(terminal == null)
            return null;
        
        NodeList ncipoints = terminal.getElementsByTagName("nci:POINT");
        if(ncipoints == null)
            return null;
        
        int nSize = ncipoints.getLength();
        int x1=0,x2=0,y1=0,y2=0;
        double dx = new Double(element.getAttribute("x")).doubleValue();
        double dy = new Double(element.getAttribute("y")).doubleValue();
        double sw = new Double(element.getAttribute("sw")).doubleValue();
        double sh = new Double(element.getAttribute("sh")).doubleValue();
        double nw = new Double(element.getAttribute("width")).doubleValue();
        double nh = new Double(element.getAttribute("height")).doubleValue();
        if(sw == 0)
            sw = nw;
        
        if(sh == 0)
            sh = nh;
        final AffineTransform initialTransform=
            handle.getSvgElementsManager().getTransform(element);
        if(nSize > 0){
            for(int i = 0;i< nSize;i++)
            {
                Element mdelement = (Element)ncipoints.item(i);
                x1 = new Integer(mdelement.getAttribute("x")).intValue();
                y1 = new Integer(mdelement.getAttribute("y")).intValue();
                double[] pointo = {dx+x1*nw/sw,dy+y1*nh/sh};
                
                //通过坐标变换，获取实际的坐标地址
                
                initialTransform.transform(pointo, 0, pointo, 0, 1);
                double dx2 = pointo[0];
                double dy2 = pointo[1];
                Point pp = new  Point((int) dx2, (int) dy2);
                
                
                items.add(new SelectionItem(handle, elements, handle.getTransformsManager().getScaledPoint(pp, false), SelectionItem.POINT,
                             SelectionItem.POINT_STYLE, 0, false, null));
           
            }
        }
        return items;
    }
	
	@Override
	public UndoRedoAction translate(//TODO
		final SVGHandle handle, Set<Element> elementSet, Point2D translationFactors) {

		UndoRedoAction undoRedoAction=null;
		final Element element=elementSet.iterator().next();
		
		//getting the initial shape
		final java.awt.geom.RectangularShape initialShape=
			(java.awt.geom.RectangularShape)getShape(handle, element, false);

		if(initialShape!=null){
			
			//getting the translation factors
			Point2D translationCoefficients=
				ShapeToolkit.getTranslationFactors(handle, element, 
						translationFactors.getX(), translationFactors.getY());
			
			//creating the new shape
			final java.awt.geom.RectangularShape newShape=
				new Rectangle2D.Double(
					initialShape.getX()+translationCoefficients.getX(),
					initialShape.getY()+translationCoefficients.getY(),
					initialShape.getWidth(), initialShape.getHeight());
			
            final boolean bSingleFlag = (handle.getSelection().getSelectedElements().size() > 1?false:true);			
			//setting the new x and y attributes for the elements
			Runnable executeRunnable=new Runnable() {

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
			Runnable undoRunnable=new Runnable() {

				public void run() {

					setShape(handle, element, initialShape);
					if (bSingleFlag && (element.getNodeName().equals("image")|| element.getNodeName().equals("use")))
					{
					UpholdElementRela rela = new UpholdElementRela(handle.getCanvas().getLpManager());
	                rela.upholdElement(handle, element, UpholdElementRela.MODIFY_TYPE);
					}
					editor.getSvgSession().refreshHandle();
				}
			};
			
			//executing the action and creating the undo/redo action
			HashSet<Element> elements=new HashSet<Element>();
			elements.add(element);
			undoRedoAction=ShapeToolkit.getUndoRedoAction(
					translateUndoRedoLabel, executeRunnable, undoRunnable, elements);
		}
		
		return undoRedoAction;
	}
	
	@Override
	public UndoRedoAction resize(
			final SVGHandle handle, Set<Element> elementSet, final AffineTransform transform) {

		UndoRedoAction undoRedoAction=null;
		Runnable executeRunnable=null, undoRunnable=null;
		final Element element=elementSet.iterator().next();
		//getting the transform of the current node
		final AffineTransform initialTransform=
			handle.getSvgElementsManager().getTransform(element);
		
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
	
	@Override
	public UndoRedoAction rotate(
			final SVGHandle handle, Set<Element> elementSet, 
				Point2D centerPoint, double angle) {
		
		final Element element=elementSet.iterator().next();

		//getting the rotation affine transform
		AffineTransform actionTransform=
			AffineTransform.getRotateInstance(angle, centerPoint.getX(), centerPoint.getY());
		
		//getting the element's transform
		final AffineTransform previousElementTransform=
			handle.getSvgElementsManager().getTransform(element);
		
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
	
	@Override
	public UndoRedoAction skew(
			final SVGHandle handle, Set<Element> elementSet, Point2D centerPoint, 
				double skewFactor, boolean isHorizontal) {
		
		final Element element=elementSet.iterator().next();

		//getting the skew affine transform
		AffineTransform actionTransform=ShapeToolkit.getSkewAffineTransform(
				handle, element, centerPoint, skewFactor, isHorizontal);
		
		//getting the element's transform
		final AffineTransform previousElementTransform=
			handle.getSvgElementsManager().getTransform(element);
		
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
	
	/**
	 * returns a undo/redo action used to modify the radius 
	 * attribute for the element
	 * @param handle the svg handle of the canvas into which the action has been done
	 * @param element the element to be handled
	 * @param radius the new radius for the element
	 * @return a undo/redo action
	 */
	public UndoRedoAction modifyRadius(final SVGHandle handle, 
			final Element element, final Point2D radius){
		
		//getting the current radius attributes values
		final String xRadiusAttValue=element.getAttribute(rxAtt);
		final String yRadiusAttValue=element.getAttribute(ryAtt);
		final boolean bSingleFlag = (handle.getSelection().getSelectedElements().size() > 1?false:true);
		//setting the new values for the element
		Runnable executeRunnable=new Runnable() {

			public void run() {

				EditorToolkit.setAttributeValue(element, rxAtt, radius.getX());
				EditorToolkit.setAttributeValue(element, ryAtt, radius.getY());
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

				element.setAttribute(rxAtt, xRadiusAttValue);
				element.setAttribute(ryAtt, yRadiusAttValue);
				if (bSingleFlag && (element.getNodeName().equals("image") || element.getNodeName().equals("use")))
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
				radiusModificationUndoRedoLabel, executeRunnable, undoRunnable, elements);

		return undoRedoAction;
	}

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
			handle.getSvgElementsManager().getTransform(element);
		
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
	
	@Override
	public UndoRedoAction validateTranslateAction(SVGHandle handle, Set<Element> elementSet, 
			Point2D firstPoint, Point2D currentPoint) {

		return translate(handle, elementSet, new Point2D.Double(
				currentPoint.getX()-firstPoint.getX(), currentPoint.getY()-firstPoint.getY()));
	}
	
	@Override
	public CanvasPainter showAction(
			SVGHandle handle, int level, Set<Element> elementSet, SelectionItem item, 
			Point2D firstPoint, Point2D currentPoint) {
		
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
					handle.getSvgElementsManager().getTransform(element);
				
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
					handle.getSvgElementsManager().getTransform(element);
				
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
	
	@Override
	public UndoRedoAction validateAction(
			SVGHandle handle,  int level, Set<Element> elementSet,
			SelectionItem item, Point2D firstPoint, Point2D lastPoint) {
		
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
	
	@Override
	public Shape getShape(SVGHandle handle, Element element, boolean isOutline) {

		RoundRectangle2D rectangle=null;
		
		//getting the bounds of the initial rectangle
		double initX=EditorToolkit.getAttributeValue(element, xAtt);
		double initY=EditorToolkit.getAttributeValue(element, yAtt);
		double initW=EditorToolkit.getAttributeValue(element, wAtt);
		double initH=EditorToolkit.getAttributeValue(element, hAtt);
		double initRX=EditorToolkit.getAttributeValue(element, rxAtt);
		double initRY=EditorToolkit.getAttributeValue(element, ryAtt);
		
		if(Double.isNaN(initRX)){
			
			initRX=0;
		}
		
		if(Double.isNaN(initRY)){
			
			initRY=0;
		}
		
		if(! Double.isNaN(initX) && ! Double.isNaN(initY) &&
			! Double.isNaN(initW) && ! Double.isNaN(initH)){
			
			//creating the rectangle
			rectangle=new RoundRectangle2D.Double(initX, initY, initW, initH, 2*initRX, 2*initRY);
		}
		
		return rectangle;
	}
	
	@Override
	public void setShape(SVGHandle handle, Element element, Shape shape) {

		if(shape!=null && shape instanceof RoundRectangle2D){
			
			RoundRectangle2D rect=(RoundRectangle2D.Double)shape;
			
			EditorToolkit.setAttributeValue(element, xAtt, rect.getX());
			EditorToolkit.setAttributeValue(element, yAtt, rect.getY());
			EditorToolkit.setAttributeValue(element, wAtt, rect.getWidth());
			EditorToolkit.setAttributeValue(element, hAtt, rect.getHeight());
			EditorToolkit.setAttributeValue(element, rxAtt, rect.getArcWidth()/2);
			EditorToolkit.setAttributeValue(element, ryAtt, rect.getArcHeight()/2);
			
		}else if(shape!=null && shape instanceof java.awt.geom.RectangularShape){
			
			java.awt.geom.RectangularShape rect=(java.awt.geom.RectangularShape)shape;
			
			EditorToolkit.setAttributeValue(element, xAtt, rect.getX());
			EditorToolkit.setAttributeValue(element, yAtt, rect.getY());
			EditorToolkit.setAttributeValue(element, wAtt, rect.getWidth());
			EditorToolkit.setAttributeValue(element, hAtt, rect.getHeight());
		}
	}
	
	/**
	 * returns the new radius values for the rectangle
	 * @param handle a svg handle
	 * @param element the element
	 * @param currentPoint the currently clicked point
	 * @param item a selection item
	 * @return the new radius values for the rectangle
	 */
	protected Point2D getRadius(SVGHandle handle, Element element, 
			Point2D currentPoint, SelectionItem item){
		
		//getting the untransformed shape corresponding to the element
		RoundRectangle2D rect=(RoundRectangle2D)getShape(handle, element, false);
		
		//getting the previous factors
		Point2D.Double factors=new Point2D.Double(rect.getArcWidth()/2, rect.getArcHeight()/2);
		
		//transforming the provided point with the inverse transform of the element, 
		//so that the point that is found is untransformed 
		AffineTransform elementAf=
			handle.getSvgElementsManager().getTransform(element);
		
		if(elementAf!=null && ! elementAf.isIdentity()){
			
			try{
				//creating the inverse transform
				AffineTransform inverseTransform=elementAf.createInverse();
				
				if(inverseTransform!=null){
					
					//transforming the points
					currentPoint=inverseTransform.transform(currentPoint, null);
				}
			}catch (Exception ex){}
		}
		
		//getting the new factor for the axis defined by the provided selection item
		double factor=0;
		
		switch(item.getIndex()){
		
			case 0 :
				
				//computing the factor
				factor=-(currentPoint.getX()-(rect.getX()+rect.getWidth()));
				
				//correcting the factor
				if(factor<0){
					
					factor=0;
				}
				
				if(factor>rect.getWidth()/2){
					
					factor=rect.getWidth()/2;
				}
				
				//creating the point that will be returned
				factors=new Point2D.Double(
						factor, factors.getY()==0?factor:factors.getY());
				break;
				
			case 1 :
				
				//computing the factor
				factor=currentPoint.getY()-rect.getY();
				
				//correcting the factor
				if(factor<0){
					
					factor=0;
				}
				
				if(factor>rect.getHeight()/2){
					
					factor=rect.getHeight()/2;
				}
				
				//creating the point that will be returned
				factors=new Point2D.Double(
						factors.getX()==0?factor:factors.getX(), factor);
				
				break;
		}

		return factors;
	}
	
	/**
	 * returns the modified shape for the provided element, 
	 * without its element's transform applied
	 * with its radius factors modified.
	 * 获取已知Element元素修改后的Shape对象，不包括Element元素变换后的特征，但包含改变后的半径(radius)
	 * @param handle a svg handle
	 * @param element an element
	 * @param newFactors the new radius factors for the rectangle
	 * @return the transformed shape for the provided element
	 * with its radius factors modified
	 */
	protected Shape getModifiedRadiusShape(
			SVGHandle handle, Element element, Point2D newFactors){

		//getting the untransformed shape corresponding to the element
		RoundRectangle2D rect=(RoundRectangle2D)getShape(handle, element, false);
		
		//setting the new radius factors for the rectangle
		rect.setRoundRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(), 
				newFactors.getX()*2, newFactors.getY()*2);

		return rect;
	}
	
	/**
	 * returns the set of the selection items corresponding to a radius modification
	 * selection level and for the elements and the covered area
	 * @param handle a svg handle
	 * @param elements a set of elements
	 * @param area the union of the bounds of the elements
	 * @return the set of the selection items corresponding to a radius modification
	 * selection level and for the elements and the covered area
	 */
	public Set<SelectionItem> getRadiusSelectionItems(
			SVGHandle handle, Set<Element> elements, Rectangle2D area){
		
		//the set of the selection items that will be returned
		Set<SelectionItem> items=new HashSet<SelectionItem>();
		
		//getting the current element
		Element element=elements.iterator().next();
		
		//getting the untransformed shape of the element
		RoundRectangle2D rect=(RoundRectangle2D)getShape(handle, element, false);
		
		//computing the two points used to choose the radius//
		//the x-axis point
		double x=rect.getX()+rect.getWidth()-rect.getArcWidth()/2;
		double y=rect.getY();
		
		//correcting the factor
		if(x<rect.getX()+rect.getWidth()/2){
			
			x=rect.getX()+rect.getWidth()/2;
		}
		
		if(x>rect.getX()+rect.getWidth()){
			
			x=rect.getX()+rect.getWidth();
		}
		
		Point2D xRadiusPoint=new Point2D.Double(x, y);
		
		//the y-axis point
		x=rect.getX()+rect.getWidth();
		y=rect.getY()+rect.getArcHeight()/2;
		
		//correcting the factor
		if(y<rect.getY()){
			
			y=rect.getY();
		}
		
		if(y>rect.getY()+rect.getHeight()/2){
			
			y=rect.getY()+rect.getHeight()/2;
		}
		
		Point2D yRadiusPoint=new Point2D.Double(x, y);
		
		//transforming the points with the element's transform, 
		//and so that they fit the canvas
		AffineTransform transform=
			handle.getSvgElementsManager().getTransform(element);
		
		//scaling each point
		xRadiusPoint=handle.getTransformsManager().
			getScaledPoint(xRadiusPoint, false, transform);
		yRadiusPoint=handle.getTransformsManager().
			getScaledPoint(yRadiusPoint, false, transform);
			
		items.add(new SelectionItem(handle, elements, xRadiusPoint,
				SelectionItem.POINT, SelectionItem.CENTER_POINT_STYLE, 0, false, null));
		
		items.add(new SelectionItem(handle, elements, yRadiusPoint,
				SelectionItem.POINT, SelectionItem.CENTER_POINT_STYLE, 1, false, null));
		
		return items;
	}
	
	/**
	 * the painter used to draw ghost shapes on a canvas
	 * @author ITRIS, Jordi SUC
	 */
	protected class GhostShapeCanvasPainter extends CanvasPainter{
		
		/**
		 * the shape to draw
		 */
		private java.awt.geom.RectangularShape shape;
		
		/**
		 * the set of the clip rectangles
		 */
		private Set<Rectangle2D> clips=new HashSet<Rectangle2D>();
		
		@Override
		public void paintToBeDone(Graphics2D g) {
			
			if(shape!=null) {

				g=(Graphics2D)g.create();
				//wangql changed the color from black to blue
				g.setColor(Color.blue);
//				g.setColor(Color.black);
				//g.setXORMode(Color.white);
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
						RenderingHints.VALUE_ANTIALIAS_ON);
				
				if(shape instanceof RoundRectangle2D){
					
					RoundRectangle2D roundRect=(RoundRectangle2D)shape;
					
					g.drawRoundRect((int)roundRect.getX(), (int)roundRect.getY(), 
						(int)roundRect.getWidth(), (int)roundRect.getHeight(), 
							(int)roundRect.getArcWidth(), (int)roundRect.getArcHeight());
					
				}else if(shape instanceof Ellipse2D){
					
					g.drawOval((int)shape.getX(), (int)shape.getY(), 
							(int)shape.getWidth(), (int)shape.getHeight());
					
				}else{
					
					g.drawRect((int)shape.getX(), (int)shape.getY(), 
							(int)shape.getWidth(), (int)shape.getHeight());
				}

				g.dispose();
			}
		}

		@Override
		public Set<Rectangle2D> getClip() {

			return clips;
		}
		
		/**
		 * sets the shape to paint
		 * @param shape a shape
		 */
		public void setShape(
				java.awt.geom.RectangularShape shape) {
			
			this.shape=shape;
			clips.clear();
			
			if(shape!=null) {
				
				clips.add(shape.getBounds2D());
			}
		}
		
		/**
		 * reinitializing the painter
		 */
		public void reinitialize() {
			
			shape=null;
		}
	}
}
