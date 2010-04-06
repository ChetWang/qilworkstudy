/**
 * 类名：com.nci.svg.shape
 * 创建人:yx
 * 创建日期：2008-5-8
 * 类作用:TODO
 * 修改日志：
 */
package com.nci.svg.shape;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nci.svg.util.NCISymbolSession;
import com.nci.svg.util.Utilities;

import fr.itris.glips.library.display.ArrowIcon;
import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.display.canvas.CanvasPainter;
import fr.itris.glips.svgeditor.display.handle.HandlesListener;
import fr.itris.glips.svgeditor.display.handle.HandlesManager;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.display.selection.Selection;
import fr.itris.glips.svgeditor.display.selection.SelectionItem;
import fr.itris.glips.svgeditor.display.undoredo.UndoRedoAction;
import fr.itris.glips.svgeditor.resources.ResourcesManager;
import fr.itris.glips.svgeditor.selection.SelectionInfoManager;
import fr.itris.glips.svgeditor.shape.AbstractShape;
import fr.itris.glips.svgeditor.shape.path.PathShape;

/**
 * @author yx
 *
 */
public class GraphUnitPointShape extends AbstractShape {

    public int ADD_MODE = 0;
    public int REMOVE_MODE = 1;
    protected static String addGraphUnitPointId = "AddGraphUnitPointShape";// 图元连接点
    protected static String removeGraphUnitPointId = "RemoveGraphUnitPointShape";// 图元连接点
    protected JMenuItem addPointMenuItem,removePointMenuItem;
    protected ImageIcon addPointIcon, 
    addPointDisabledIcon, removePointIcon, removePointDisabledIcon;
    
    protected String addPointLabel, addPointTooltip, 
    removePointLabel, removePointTooltip;
    
    protected JToggleButton addPointButton,removePointButton;
    
    protected Document symbolDoc = null;
    protected boolean bAddPointFlag = false;
    
    protected int currentMode=ADD_MODE;
    protected JMenuItem addPointMenuItemTool,removePointMenuItemTool;
    
    
    /* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.shape.AbstractShape#getToolItems()
     */
    @Override
    public HashMap<String, AbstractButton> getToolItems() {
        // TODO Auto-generated method stub
        HashMap<String, AbstractButton> map=new HashMap<String, AbstractButton>();
        map.put(addGraphUnitPointId, addPointButton);
        map.put(removeGraphUnitPointId, removePointButton);
 
        return map;
    }
    /**
     * @return the symbolDoc
     */
    public Document getSymbolDoc() {
        return symbolDoc;
    }
    /**
     * @param symbolDoc the symbolDoc to set
     */
    public void setSymbolDoc(Document symbolDoc) {
        this.symbolDoc = symbolDoc;
    }
    protected void createItems(){
        addPointIcon=ResourcesManager.getIcon("AddPointPath", false);
        addPointDisabledIcon=ResourcesManager.getIcon("AddPointPath", true);
        removePointIcon=ResourcesManager.getIcon("RemovePointPath", false);
        removePointDisabledIcon=ResourcesManager.getIcon("RemovePointPath", true);
        
        //getting the labels
        addPointLabel=ResourcesManager.bundle.getString(shapeModuleId+"AddPointLabel");
        addPointTooltip=ResourcesManager.bundle.getString(shapeModuleId+"AddPointToolTip");
        removePointLabel=ResourcesManager.bundle.getString(shapeModuleId+"RemovePointLabel");
        removePointTooltip=ResourcesManager.bundle.getString(shapeModuleId+"RemovePointToolTip");
    
        //creating the menu items for the menu bar
        addPointMenuItem=new JMenuItem(addPointLabel, addPointIcon);
        addPointMenuItem.setDisabledIcon(addPointDisabledIcon);
        addPointMenuItem.setEnabled(false);
        
        removePointMenuItem=new JMenuItem(removePointLabel, removePointIcon);
        removePointMenuItem.setDisabledIcon(removePointDisabledIcon);
        removePointMenuItem.setEnabled(false);
        
        addPointMenuItemTool=new JMenuItem(addPointLabel, addPointIcon);
        addPointMenuItemTool.setDisabledIcon(addPointDisabledIcon);
        addPointMenuItemTool.setToolTipText(addPointTooltip);
        
        removePointMenuItemTool=new JMenuItem(removePointLabel, removePointIcon);
        removePointMenuItemTool.setDisabledIcon(removePointDisabledIcon);
        removePointMenuItemTool.setToolTipText(removePointTooltip);
        
        addPointButton=new JToggleButton(addPointIcon);
        addPointButton.setDisabledIcon(addPointDisabledIcon);
        addPointButton.setToolTipText(addPointTooltip);
        addPointButton.setEnabled(false);
        
        removePointButton=new JToggleButton(removePointIcon);
        removePointButton.setDisabledIcon(removePointDisabledIcon);
        removePointButton.setToolTipText(removePointTooltip);
        removePointButton.setEnabled(false);
        
         //creating the listener to the menu and tool items
        ActionListener listener=new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                
                if(e.getSource().equals(addPointMenuItem)){
                    
                    notifyDrawingMode();  
                    addPointButton.setSelected(true);
                    currentMode = ADD_MODE;
                }else if(e.getSource().equals(removePointMenuItem)){
                    
                    notifyDrawingMode();
                    removePointButton.setSelected(true);
                    currentMode = REMOVE_MODE;
                    
                }else if(e.getSource().equals(addPointButton)){
                    
                    notifyDrawingMode();  
                    addPointButton.removeActionListener(this);
                    addPointButton.setSelected(true);
                    addPointButton.addActionListener(this);
                    currentMode = ADD_MODE;
                }else if(e.getSource().equals(removePointButton)){
                    
                    notifyDrawingMode();
                    removePointButton.removeActionListener(this);
                    removePointButton.setSelected(true);
                    removePointButton.addActionListener(this);
                    currentMode = REMOVE_MODE;
               
                }
                
                
            }
        };
        
        //adding the listener to the tool and menu items
        addPointMenuItem.addActionListener(listener);
        removePointMenuItem.addActionListener(listener);
        addPointButton.addActionListener(listener);
        removePointButton.addActionListener(listener);
        
        
  
        
        //adding the listener to the switches between the svg handles
        final HandlesManager svgHandleManager=
        	editor.getHandlesManager();
        
        svgHandleManager.addHandlesListener(new HandlesListener(){
            
            @Override
            public void handleChanged(SVGHandle currentHandle, Set<SVGHandle> handles) {
                boolean isDrawingEnabled = isDrawingEnabled(currentHandle);
                if (isDrawingEnabled
                        && currentHandle.getHandleType() == SVGHandle.HANDLE_TYPE_GRAPH_UNIT_OLD) {

                    addPointMenuItem.setEnabled(true);
                    removePointMenuItem.setEnabled(true);
                    addPointButton.setEnabled(true);
                    removePointButton.setEnabled(true);
                } else {
                    addPointMenuItem.setEnabled(false);
                    removePointMenuItem.setEnabled(false);
                    addPointButton.setEnabled(false);
                    removePointButton.setEnabled(false);
                }
                SelectionInfoManager selectionManager = editor
                        .getSelectionManager();

                if (selectionManager.getDrawingShape() != null
                        && selectionManager.getDrawingShape().equals(
                                GraphUnitPointShape.this)) {

                    selectionManager.setToRegularMode();
                }
            }           
        });
    }
    
    /* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.shape.AbstractShape#getMenuItems()
     */
    @Override
    public HashMap<String, JMenuItem> getMenuItems() {
        HashMap<String, JMenuItem> map=new HashMap<String, JMenuItem>();
        map.put(addGraphUnitPointId, addPointMenuItem);
        map.put(removeGraphUnitPointId, removePointMenuItem);
        return map;
    }
    public GraphUnitPointShape(Editor editor)
    {
        super(editor);

        shapeModuleId = "GraphUnitPointShape";
        createItems();
        return;
    }
    /* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.shape.AbstractShape#getLevelCount()
     */
    @Override
    public int getLevelCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.shape.AbstractShape#getSelectionItems(fr.itris.glips.svgeditor.display.handle.SVGHandle, java.util.Set, int)
     */
    @Override
    public Set<SelectionItem> getSelectionItems(SVGHandle handle,
            Set<Element> elements, int level) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.shape.AbstractShape#getShape(fr.itris.glips.svgeditor.display.handle.SVGHandle, org.w3c.dom.Element, boolean)
     */
    @Override
    public Shape getShape(SVGHandle handle, Element element, boolean isOutline) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.shape.AbstractShape#notifyDrawingAction(fr.itris.glips.svgeditor.display.handle.SVGHandle, java.awt.geom.Point2D, int, int)
     */
    @Override
    public void notifyDrawingAction(SVGHandle handle, Point2D point,
            int modifier, int type) {
        // TODO Auto-generated method stub
        switch (type) {

        case DRAWING_MOUSE_MOVED:
        {
          //显示变化的新增点
            Set<Element> elements = new HashSet<Element>();
            Set<SelectionItem> items = new HashSet<SelectionItem>();
            items.add(new SelectionItem(handle, elements, point, SelectionItem.POINT,
                    SelectionItem.POINT_STYLE, 0, false, null));
            Selection selection = handle.getSelection();
            selection.refreshSelectElement(items);
            break;
        }
        case DRAWING_MOUSE_PRESSED:
        {
            setSymbolDoc(handle.getCanvas().getDocument());
            //将新增点加入doc
            if(symbolDoc != null && !bAddPointFlag && currentMode == ADD_MODE)
            {
                bAddPointFlag = true;
                addNciPoint(handle,point);

            }
            else if(symbolDoc != null && !bAddPointFlag && currentMode == REMOVE_MODE)
            {
                bAddPointFlag = true; 
                removeNciPoint(handle,point);
            }
            break;
        }
        case DRAWING_MOUSE_RELEASED:
        {
            if(symbolDoc != null && bAddPointFlag)
            {
                bAddPointFlag = false;
                resetDrawing();
            }
            break;
        }
        }
    }
    
    
    /**删除nci连接点，如该坐标存在连接点则删除
     * @param handle
     * @param point：待删除的连接点坐标
     */
    public void removeNciPoint(SVGHandle handle, Point2D point)
    {
        Element eRoot = symbolDoc.getDocumentElement();
        int ngCount = eRoot.getElementsByTagName("g").getLength();
        Element gElement = null;
        if(ngCount == 0)
        {
            return;
        }   
        int i = 0;
        for(i = 0;i < ngCount;i++)
        {
            gElement = (Element)eRoot.getElementsByTagName("g").item(i);
            String strid = gElement.getAttribute("id");
            if(strid != null && strid.equals("nci:terminal"))
            {
                break;
            }
            gElement = null;
        }
        
        if(gElement == null)
            return;
        
        int nPointCount = gElement.getElementsByTagName("ellipse").getLength();
        for(i = 0;i < nPointCount;i++)
        {
            Element element = (Element)gElement.getElementsByTagName("ellipse").item(i);
            int x = new Integer(element.getAttribute("cx")).intValue();
            int y = new Integer(element.getAttribute("cy")).intValue();
            
            if(point.distance(x, y) <=2)
            {
                gElement.removeChild(element);
                editor.getSvgSession().refreshHandle();
                return;
            }
        }
        return;
    }
    
    /**新增nci连接点
     * @param handle
     * @param point:增加的坐标点
     */
    public void addNciPoint(SVGHandle handle, Point2D point)
    {
        Element eRoot = symbolDoc.getDocumentElement();
        int ngCount = eRoot.getElementsByTagName("g").getLength();
        Element gElement = null;
        boolean bCreateFlag = false;
        int nPointCount = 0;
        if(ngCount == 0)
        {
            bCreateFlag = true;
        }
        else
        {
            for(int i = 0;i < ngCount;i++)
            {
                gElement = (Element)eRoot.getElementsByTagName("g").item(i);
                String strid = gElement.getAttribute("id");
                if(strid != null && strid.equals("nci:terminal"))
                {
                    nPointCount = new Integer(gElement.getAttribute("pmax")).intValue();
                    break;
                }
                gElement = null;
            }
            
            if(gElement == null)
                bCreateFlag = true;
            
        }
        if(bCreateFlag)
        {
            gElement = symbolDoc.createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI,"g") ;    
            gElement.setAttribute("id", "nci:terminal");
            eRoot.appendChild(gElement);
        }
        
          
        Element tElement = symbolDoc.createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI,"ellipse") ;
        tElement.setAttribute("name",  new String().format("t%d", nPointCount));
        tElement.setAttribute("cx", new String().format("%d", (int)point.getX()));
        tElement.setAttribute("cy", new String().format("%d", (int)point.getY()));
        tElement.setAttribute("rx", "1");
        tElement.setAttribute("ry", "1");
        tElement.setAttribute("style","stroke-width:1.0;stroke:#ff0000;fill:#ff0000;");
        nPointCount++;
        gElement.setAttribute("pmax",  new String().format("%d", nPointCount));
        insertShapeElement(handle,gElement,tElement);
        handle.getSelection().lock(tElement);

        return;
    }

    /* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.shape.AbstractShape#resize(fr.itris.glips.svgeditor.display.handle.SVGHandle, java.util.Set, java.awt.geom.AffineTransform)
     */
    @Override
    public UndoRedoAction resize(SVGHandle handle, Set<Element> elementSet,
            AffineTransform transform) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.shape.AbstractShape#rotate(fr.itris.glips.svgeditor.display.handle.SVGHandle, java.util.Set, java.awt.geom.Point2D, double)
     */
    @Override
    public UndoRedoAction rotate(SVGHandle handle, Set<Element> elementSet,
            Point2D centerPoint, double angle) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.shape.AbstractShape#setShape(fr.itris.glips.svgeditor.display.handle.SVGHandle, org.w3c.dom.Element, java.awt.Shape)
     */
    @Override
    public void setShape(SVGHandle handle, Element element, Shape shape) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.shape.AbstractShape#showAction(fr.itris.glips.svgeditor.display.handle.SVGHandle, int, java.util.Set, fr.itris.glips.svgeditor.display.selection.SelectionItem, java.awt.geom.Point2D, java.awt.geom.Point2D)
     */
    @Override
    public CanvasPainter showAction(SVGHandle handle, int level,
            Set<Element> elementSet, SelectionItem item, Point2D firstPoint,
            Point2D currentPoint) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.shape.AbstractShape#showTranslateAction(fr.itris.glips.svgeditor.display.handle.SVGHandle, java.util.Set, java.awt.geom.Point2D, java.awt.geom.Point2D)
     */
    @Override
    public CanvasPainter showTranslateAction(SVGHandle handle,
            Set<Element> elementSet, Point2D firstPoint, Point2D currentPoint) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.shape.AbstractShape#skew(fr.itris.glips.svgeditor.display.handle.SVGHandle, java.util.Set, java.awt.geom.Point2D, double, boolean)
     */
    @Override
    public UndoRedoAction skew(SVGHandle handle, Set<Element> elementSet,
            Point2D centerPoint, double skewFactor, boolean isHorizontal) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.shape.AbstractShape#translate(fr.itris.glips.svgeditor.display.handle.SVGHandle, java.util.Set, java.awt.geom.Point2D)
     */
    @Override
    public UndoRedoAction translate(SVGHandle handle, Set<Element> elementSet,
            Point2D translationFactors) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.shape.AbstractShape#validateAction(fr.itris.glips.svgeditor.display.handle.SVGHandle, int, java.util.Set, fr.itris.glips.svgeditor.display.selection.SelectionItem, java.awt.geom.Point2D, java.awt.geom.Point2D)
     */
    @Override
    public UndoRedoAction validateAction(SVGHandle handle, int level,
            Set<Element> elementSet, SelectionItem item, Point2D firstPoint,
            Point2D lastPoint) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.shape.AbstractShape#validateTranslateAction(fr.itris.glips.svgeditor.display.handle.SVGHandle, java.util.Set, java.awt.geom.Point2D, java.awt.geom.Point2D)
     */
    @Override
    public UndoRedoAction validateTranslateAction(SVGHandle handle,
            Set<Element> elementSet, Point2D firstPoint, Point2D currentPoint) {
        // TODO Auto-generated method stub
        return null;
    }

}
