package fr.itris.glips.svgeditor.display.selection;

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JViewport;
import javax.xml.xpath.XPathExpressionException;

import org.apache.batik.ext.awt.geom.ExtendedGeneralPath;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nci.svg.module.NCIScreenCastModule;
import com.nci.svg.ui.clipboard.ImageTransferable;
import com.nci.svg.util.Utilities;

import fr.itris.glips.library.geom.path.Path;
import fr.itris.glips.library.geom.path.segment.Segment;
import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.EditorToolkit;
import fr.itris.glips.svgeditor.display.canvas.CanvasPainter;
import fr.itris.glips.svgeditor.display.canvas.SVGCanvas;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.properties.SVGProperties;
import fr.itris.glips.svgeditor.resources.ResourcesManager;
import fr.itris.glips.svgeditor.selection.SelectionInfoManager;
import fr.itris.glips.svgeditor.shape.AbstractShape;
import fr.itris.glips.svgeditor.shape.MultiAbstractShape;
import fr.itris.glips.svgeditor.shape.ShapeToolkit;
import fr.itris.glips.svgeditor.shape.path.PathShape;

/**
 * the class recording information on the selection on the canvas
 * 
 * @author ITRIS, Jordi SUC
 */
public class Selection {

    /**
     * the constant specifying the first level of the selection
     */
    public static final int SELECTION_LEVEL_1 = 0;
    /**
     * the constant specifying the second level of the selection
     */
    public static final int SELECTION_LEVEL_2 = 1;
    /**
     * the constant specifying the third level of the selection
     */
    public static final int SELECTION_LEVEL_3 = 2;
    /**
     * the constant specifying the third level of the selection
     */
    public static final int SELECTION_LEVEL_4 = 3;
    /**
     * the constant specifying the level of the selection while in a drawing
     * action mode
     */
    public static final int SELECTION_LEVEL_DRAWING = 10;
    /**
     * the constant specifying the level of the selection while in selection
     * items action mode
     */
    public static final int SELECTION_LEVEL_ITEMS_ACTION = 11;
    /**
     * the regular sub mode
     */
    public static final int REGULAR_SUB_MODE = 0;
    /**
     * the translation sub mode
     */
    public static final int TRANSLATION_SUB_MODE = 1;
    /**
     * the action sub mode
     */
    public static final int ACTION_SUB_MODE = 2;
    /**
     * the drawing mouse pressed action
     */
    public static final int SELECTION_ZONE_MOUSE_PRESSED = 0;
    /**
     * the drawing mouse released action
     */
    public static final int SELECTION_ZONE_MOUSE_RELEASED = 1;
    /**
     * the drawing mouse dragged action
     */
    public static final int SELECTION_ZONE_MOUSE_DRAGGED = 2;

    public static final int NONE_LINE_MODE = -1;

    public static final int PARALLEL_LINE_MODE = 0;

    public static final int VERTICAL_LINE_MODE = 1;

    public static final int VERTICAL_MIDDLE_LINE_MODE = 2;

    public int nLineMode = -1;
    /**
     * the global selection manager
     */
    private SelectionInfoManager selectionManager = null;
    /**
     * the svg handle this object is linked to
     */
    private SVGHandle svgHandle;
    /**
     * the listener to the mouse actions on the canvas
     */
    private SelectionListener selectionListener;
    /**
     * the listener to the keyactions on the canvas
     */
    private KeySelectionListener keySelectionListener;
    /**
     * the selection managers
     */
    private SelectionManager singleSelectionManager, multiSelectionManager;

    private PointItemsCanvasPainter pointit = new PointItemsCanvasPainter();
    /**
     * the parent element of the edited nodes
     */
    private Element parentElement;
    /**
     * the set of the currently selected elements
     */
    private Set<Element> selectedElements = new HashSet<Element>();
    /**
     * the ordered list of the currently selected elements
     */
    private LinkedList<Element> orderedSelectedElements = new LinkedList<Element>();
    /**
     * the set of the currently selected elements that are not locked
     */
    private Set<Element> unLockedSelectedElements = new HashSet<Element>();
    /**
     * the set of the locked elements
     */
    private Set<Element> lockedElements = new HashSet<Element>();
    /**
     * the set of the current selection items
     */
    private Set<SelectionItem> selectionItems = new HashSet<SelectionItem>();

    /**
     * the selection mode
     */
    private int selectionSubMode = REGULAR_SUB_MODE;
    /**
     * the selection level
     */
    private int selectionLevel = SELECTION_LEVEL_1;
    /**
     * the action selection manager
     */
    private SelectionManager actionSelectionManager;
    /**
     * the painter of the selection zone rectangle
     */
    private GhostSelectionZoneCanvasPainter ghostSelectionZoneCanvasPainter1,
            ghostSelectionZoneCanvasPainter2, ghostSelectionZoneCanvasPainter3,
            ghostSelectionZoneCanvasPainter4;
    /**
     * the set of the listeners that listen to the selection changes
     */
    private Set<SelectionChangedListener> selectionChangedListeners = new HashSet<SelectionChangedListener>();
    /**
     * the painter of the selection items
     */
    private SelectionItemsCanvasPainter selectionItemsPainter;
    /**
     * whether the action and translation management should be disabled
     */
    private boolean actionDisabled = false;
    /**
     * the first point of an action
     */
    private Point2D firstPoint;
    /**
     * the selection item of the current action
     */
    private SelectionItem actionItem;
    /**
     * the set of the elements that are put here when they are translated
     */
    private Set<Element> translationElements = new HashSet<Element>();

    public static int serial = 0;

    public int i = 0;

    // ����ƽ���ߡ���ֱ�ߡ������ߵ��¼�����
    private SelectionChangedListener drawLineListener = null;

    private SelectionChangedListener drawLineModifyListener = null;

    private SelectionChangedListener pathshapeListener = null;

    private boolean bTextDialog = false;
    private SymbolWink sWink = null;

    /**
     * ��˸�ö�ʱ��
     */
    private Timer winkTimer = null;

    /**
     * >>>>>>> 1.63
     * 
     * @return the pathshapeListener
     */
    public SelectionChangedListener getPathshapeListener() {
        return pathshapeListener;
    }

    /**
     * @param pathshapeListener
     *            the pathshapeListener to set
     */
    public void setPathshapeListener(SelectionChangedListener pathshapeListener) {
        this.pathshapeListener = pathshapeListener;
    }

    /**
     * @return the bTextDialog
     */
    public boolean isBTextDialog() {
        return bTextDialog;
    }

    /**
     * @param textDialog
     *            the bTextDialog to set
     */
    public void setBTextDialog(boolean textDialog) {
        bTextDialog = textDialog;
    }

    /**
     * @return the drawLineModifyListener
     */
    public SelectionChangedListener getDrawLineModifyListener() {
        return drawLineModifyListener;
    }

    /**
     * @param drawLineModifyListener
     *            the drawLineModifyListener to set
     */
    public void setDrawLineModifyListener(
            SelectionChangedListener drawLineModifyListener) {
        this.drawLineModifyListener = drawLineModifyListener;
    }

    /**
     * @return the nLineMode
     */
    public int getNLineMode() {
        return nLineMode;
    }

    /**
     * @param lineMode
     *            the nLineMode to set
     */
    public void setNLineMode(int lineMode) {
        nLineMode = lineMode;
    }

    public void setWink(boolean bWinkFlag, int seccend) {
        if (bWinkFlag) {
            if (winkTimer == null)
                winkTimer = new Timer(true);
            beginWink(selectedElements, svgHandle.getEditor());
            sWink = new SymbolWink(selectedElements, svgHandle.getEditor());
            winkTimer.schedule(sWink, 0, seccend * 1000);
        } else {
            if (winkTimer != null) {
                winkTimer.cancel();
                winkTimer = null;
                endWink(sWink.getSets(), svgHandle.getEditor());
            }

        }
    }

    /**
     * ��˸ǰ��ʼ��
     * 
     * @param sets:����ʼ���Ľڵ㼯��
     * @param editor:
     */
    public void beginWink(Set<Element> sets, Editor editor) {
        Iterator<Element> iterators = sets.iterator();
        while (iterators.hasNext()) {
            Element element = iterators.next();
            element.setAttribute("opacity", "1");
        }
        editor.getSvgSession().refreshCurrentHandleImediately();
    }

    /**
     * ��˸��ԭ
     * 
     * @param sets:����ԭ�Ľڵ㼯��
     * @param editor
     */
    public void endWink(Set<Element> sets, Editor editor) {
        Iterator<Element> iterators = sets.iterator();
        while (iterators.hasNext()) {
            Element element = iterators.next();
            element.removeAttribute("opacity");
        }
        editor.getSvgSession().refreshCurrentHandleImediately();
    }

    class SymbolWink extends TimerTask {
        private Set<Element> sets = new HashSet<Element>();
        private Editor editor = null;

        /**
         * @return the sets
         */
        public Set<Element> getSets() {
            return sets;
        }

        public SymbolWink(Set<Element> sets, Editor editor) {
            this.sets.addAll(sets);
            this.editor = editor;
        }

        public void run() {
            Iterator<Element> iterators = sets.iterator();
            while (iterators.hasNext()) {
                Element element = iterators.next();
                String str = element.getAttribute("opacity");
                if (str == null || str.length() == 0)
                    element.setAttribute("opacity", "0");
                else if (str.substring(0, 1).equals("0")) {
                    element.setAttribute("opacity", "1");
                } else
                    element.setAttribute("opacity", "0");
            }
            editor.getSvgSession().refreshCurrentHandleImediately();
        }
    }

    /**
     * the constructor of the class
     * 
     * @param handle
     *            the svg handle that is associated to the selection object
     */
    public Selection(final SVGHandle handle) {

        this.svgHandle = handle;
        selectionManager = handle.getEditor().getSelectionManager();
        i = serial;
        serial++;

        // creating the selection managers
        singleSelectionManager = new SingleSelectionManager(this);
        multiSelectionManager = new MultiSelectionManager(this);

        // getting the parent element of the nodes that will be selected
        setParentElement(handle.getCanvas().getDocument().getDocumentElement(),
                false);

        // creating the painter of the selection items
        selectionItemsPainter = new SelectionItemsCanvasPainter();

        // creating the painters of the selection zone rectangle
        ghostSelectionZoneCanvasPainter1 = new GhostSelectionZoneCanvasPainter();
        ghostSelectionZoneCanvasPainter2 = new GhostSelectionZoneCanvasPainter();
        ghostSelectionZoneCanvasPainter3 = new GhostSelectionZoneCanvasPainter();
        ghostSelectionZoneCanvasPainter4 = new GhostSelectionZoneCanvasPainter();

        // creating the selection listeners
        selectionListener = new SelectionListener(this);
        keySelectionListener = new KeySelectionListener(this);

        SVGCanvas canvas = handle.getCanvas();
        JViewport viewport = canvas.getScrollPane().getViewport();

        canvas.addMouseListener(selectionListener);
        canvas.addMouseMotionListener(selectionListener);
        viewport.addMouseListener(selectionListener);
        viewport.addMouseMotionListener(selectionListener);
    }

    /**
     * disposes the selection resources
     */
    public void dispose() {

        selectionChangedListeners.clear();

        SVGCanvas canvas = svgHandle.getCanvas();
        JViewport viewport = canvas.getScrollPane().getViewport();

        canvas.removeMouseListener(selectionListener);
        canvas.removeMouseMotionListener(selectionListener);
        keySelectionListener.dispose();
        viewport.removeMouseListener(selectionListener);
        viewport.removeMouseMotionListener(selectionListener);

        selectionManager = null;
        svgHandle = null;
        selectionListener = null;
        keySelectionListener = null;
        singleSelectionManager = null;
        multiSelectionManager = null;
        parentElement = null;
        selectedElements = null;
        orderedSelectedElements = null;
        unLockedSelectedElements = null;
        lockedElements = null;
        selectionItems = null;
        actionSelectionManager = null;
        ghostSelectionZoneCanvasPainter1 = null;
        ghostSelectionZoneCanvasPainter2 = null;
        ghostSelectionZoneCanvasPainter3 = null;
        ghostSelectionZoneCanvasPainter4 = null;
        selectionChangedListeners = null;
        selectionItemsPainter = null;
        firstPoint = null;
        actionItem = null;
        translationElements = null;
    }

    /**
     * @return the parent element of the edited nodes
     */
    public Element getParentElement() {

        return parentElement;
    }

    /**
     * sets the new parent element
     * 
     * @param parentElement
     *            the new parent element
     * @param notify
     *            whether the parent modification event should be propagated
     */
    public void setParentElement(Element parentElement, boolean notify) {

        this.parentElement = parentElement;

        if (notify) {

            svgHandle.getCanvas().notifyParentElementChanged();
            clearSelection();
        }
    }

    /**
     * @return the selection submode
     */
    public int getSelectionSubMode() {

        return selectionSubMode;
    }

    /**
     * @return the selection manager corresponding to the current selection
     */
    public SelectionManager getSelectionManager() {

        if (selectedElements.size() == 1) {

            return singleSelectionManager;

        } else if (selectedElements.size() > 1) {

            return multiSelectionManager;
        }

        return null;
    }

    /**
     * sets the new selection mode
     * 
     * @param selectionSubMode
     *            the new selection sub mode
     */
    public void setSelectionSubMode(int selectionSubMode) {

        this.selectionSubMode = selectionSubMode;

        if (svgHandle.getEditor().getSelectionManager().getSelectionMode() == SelectionInfoManager.ITEMS_ACTION_MODE) {

            // refreshing the selection
            selectionLevel = SELECTION_LEVEL_ITEMS_ACTION;
            refreshSelection(true);

        } else if (svgHandle.getEditor().getSelectionManager()
                .getSelectionMode() == SelectionInfoManager.DRAWING_MODE) {

            // refreshing the selection
            selectionLevel = SELECTION_LEVEL_DRAWING;
            refreshSelection(true);

        } else if (selectionLevel == SELECTION_LEVEL_ITEMS_ACTION
                || selectionLevel == SELECTION_LEVEL_DRAWING) {

            // refreshing the selection
            selectionLevel = SELECTION_LEVEL_1;
            refreshSelection(true);
        }
    }

    /**
     * notifies that the selection mode has changed. ����ѡ��ģʽ�仯���¼�
     */
    public void selectionModeChanged() {

        // getting the set of all the shape modules
        Set<AbstractShape> modules = svgHandle.getEditor().getShapeModules();

        // notifying all the shape modules that the selection mode has changed
        for (AbstractShape shapeModule : modules) {

            if (shapeModule != null) {

                shapeModule.notifyDrawingAction(svgHandle, null, 0,
                        AbstractShape.DRAWING_END);
            }
        }
    }

    /**
     * sets whether the selection painters should be blocked
     * 
     * @param blockSelectionItemsPaint
     *            whether the selection painters should be blocked
     */
    public void setBlockSelectionItemsPaint(boolean blockSelectionItemsPaint) {

        if (blockSelectionItemsPaint) {

            selectionItemsPainter.clean();

        } else {

            selectionItemsPainter.reinitialize();
        }
    }

    /**
     * handles the selection of the element that can be found at the given point
     * 
     * @param currentPoint
     *            a mouse point
     * @param isMultiSelection
     *            whether the multi selection has been activated or not
     */
    @SuppressWarnings("deprecation")
    public void setSelection(Point2D currentPoint, boolean isMultiSelection) {

        // converting the given point into the user space units
        Point2D scaledPoint = svgHandle.getTransformsManager().getScaledPoint(
                currentPoint, true);

        // getting the element that can be found at the given location
        Element element = svgHandle.getSvgElementsManager().getNodeAt(
                parentElement, scaledPoint);

        if (element != null) {
            String str = element.getNodeName();
            if (str.equals("g")) {
                String strID = element.getAttribute("id");
                if (strID != null && strID.equals("nci:terminal")) {
                    return;
                }

                // NodeList gChildren = element.getElementsByTagName("g");
                // if(gChildren.getLength()==0){
                // NodeList children = element.getChildNodes();
                // for(int i=0;i<children.getLength();i++){
                // Node child = children.item(i);
                // if(child instanceof Element){
                // if(!((Element)child).getNodeName().equalsIgnoreCase("metadata"
                // )
                // ){
                // // Utilities.printNode(child);
                // element = (Element)child;
                // break;
                // }
                // }
                // }
                // }
            }
            if (str.equals("a")) {

                svgHandle.getEditor().getSvgSession().openRemoteSVGFileOld("",
                        Utilities.getOneAttributeValueWithColone(element,
                                "xlink:href"));

            }
        }
        // Utilities.printNode(element);
        // handling the selection
        handleSelection(element, isMultiSelection, false);

        // enabling the actions and translations support
        actionDisabled = false;
    }

    public void handleSelection(String strSymbolID) {

        Element element = svgHandle.getCanvas().getMapInfo(strSymbolID);
        if (element != null)
            handleSelection(element, false, false);
    }
    
    public void handleSelection(ArrayList list) {

        for(int i = 0;i < list.size();i++)
        {
            String strSymbolID = (String)list.get(i);
            Element element = null;;
            try {
                element = (Element)Utilities.findNode("//*[*='"+strSymbolID + "']",
                            svgHandle.getCanvas().getDocument().getDocumentElement());
            } catch (XPathExpressionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (element != null)
                handleSelection(element, true, false);
            }
    }

    /**
     * handles the selection of the given element
     * 
     * @param element
     *            an element
     * @param isMultiSelection
     *            whether the multi selection has been activated or not
     * @param keepSelectionLevel
     *            whether the selection level should be kept or not
     */
    public void handleSelection(Element element, boolean isMultiSelection,
            boolean keepSelectionLevel) {

        if (element == null) {

            // clearing the selection
            selectedElements.clear();
            orderedSelectedElements.clear();

        } else {

            if (isMultiSelection) {

                // if the multi selection mode is enabled//
                if (selectedElements.contains(element)) {

                    // as the element was already selected, it is removed from
                    // the selection
                    selectedElements.remove(element);
                    orderedSelectedElements.remove(element);

                } else {

                    // the element is added to the selection
                    selectedElements.add(element);
                    orderedSelectedElements.add(element);
                }

            } else {

                // checking if the given element is already selected, if then
                // the selection level is incremented
                if (selectedElements.contains(element)) {
                    // getting the selection manager corresponding to the
                    // current selection
                    SelectionManager selManager = getSelectionManager();

                    // getting the next selection level for the current
                    // selection
                    if (!keepSelectionLevel) {

                        selectionLevel = selManager
                                .getNextSelectionLevel(selectedElements);
                    }

                } else {

                    // clearing the selection
                    selectedElements.clear();
                    orderedSelectedElements.clear();

                    // adding the new element to the selection
                    selectedElements.add(element);
                    orderedSelectedElements.add(element);

                    // resetting the selection level
                    if (!keepSelectionLevel) {

                        selectionLevel = 0;
                    }
                }
            }
        }

        refreshSelection(true);
    }

    /**
     * handles the selection of the given elements
     * 
     * @param elements
     *            a set of elements
     * @param isMultiSelection
     *            whether the multi selection has been activated or not
     * @param propagate
     *            whether to propagate the event of the refresh selection action
     *            or not
     */
    public void handleSelection(Set<Element> elements,
            boolean isMultiSelection, boolean propagate) {

        if (elements == null || elements.size() == 0) {

            // clearing the selection
            selectedElements.clear();
            orderedSelectedElements.clear();

        } else {

            if (!isMultiSelection) {

                // clearing the selection
                selectedElements.clear();
                orderedSelectedElements.clear();
            }

            for (Element element : elements) {

                if (isMultiSelection && selectedElements.contains(element)) {

                    // as when are in the multiselection mode and the element
                    // was
                    // already selected, it is removed from the selection
                    selectedElements.remove(element);
                    orderedSelectedElements.remove(element);

                } else {

                    // the element is added to the selection
                    selectedElements.add(element);
                    orderedSelectedElements.add(element);
                }
            }
        }

        refreshSelection(propagate);
    }

    /**
     * selects all the elements that can be found in the given rectangle
     * 
     * @param rectangle
     *            an area on the canvas
     * @param isMultiSelectionEnabled
     *            whether the multi selection is enabled
     */
    public void handleSelection(Rectangle2D rectangle,
            boolean isMultiSelectionEnabled) {

        // reinitializing the selection level
        selectionLevel = SELECTION_LEVEL_1;

        // getting the corresponding rectangle in user space
        Rectangle2D scaledRectangle = svgHandle.getTransformsManager()
                .getScaledRectangle(rectangle, true);

        // getting all the elements that can be found in the given rectangle in
        // the canvas
        Rectangle2D r2 = null;

        // the set of all the elements that can be found in the given area
        Set<Element> elements = new HashSet<Element>();
        Element element = null;

        for (Node current = parentElement.getFirstChild(); current != null; current = current
                .getNextSibling()) {

            if (current instanceof Element) {

                element = (Element) current;

                if (element.getNodeName().equals("g")
                        && element.getAttribute("id").equals("allshape")) {
                    for (Node cur = element.getFirstChild(); cur != null; cur = cur
                            .getNextSibling()) {
                        if (cur instanceof Element) {
                            Element gElement = (Element) cur;
                            if (gElement.getNodeName().equals("g")) {
                                if (gElement.getAttribute("id").toLowerCase()
                                        .equals("head_layer")) {
                                    continue;
                                } else if (gElement.getAttribute("id")
                                        .toLowerCase().indexOf("layer") > -1) {
                                    handleChildSelection(gElement, elements,
                                            scaledRectangle);

                                }
                            }
                        }
                    }
                }
                else if (EditorToolkit.isElementAShape(element)) {

                    r2 = svgHandle.getSvgElementsManager()
                            .getNodeGeometryBounds(element);

                    if (r2 != null) {

                        Rectangle2D r3 = new Rectangle2D.Double(r2.getX(), r2
                                .getY(), r2.getWidth() + 1, r2.getHeight() + 1);

                        // if the node is contained in the rectangle, it is
                        // selected
                        if (scaledRectangle.contains(r3)) {

                            elements.add(element);
                        }
                    }
                }  
            }
        }

        handleSelection(elements, isMultiSelectionEnabled, true);

        if (!svgHandle.getEditor().getRemanentModeManager().isRemanentMode()) {

            svgHandle.getEditor().getSelectionManager().setToRegularMode();
        }
    }

    private void handleChildSelection(Element element, Set<Element> elements,
            Rectangle2D scaledRectangle) {
        Rectangle2D r2 = null;

        for (Node current = element.getFirstChild(); current != null; current = current
                .getNextSibling()) {

            if (current instanceof Element) {

                element = (Element) current;

                if (EditorToolkit.isElementAShape(element)) {

                    r2 = svgHandle.getSvgElementsManager()
                            .getNodeGeometryBounds(element);

                    if (r2 != null) {

                        Rectangle2D r3 = new Rectangle2D.Double(r2.getX(), r2
                                .getY(), r2.getWidth() + 1, r2.getHeight() + 1);

                        // if the node is contained in the rectangle, it is
                        // selected
                        if (scaledRectangle.contains(r3)) {

                            elements.add(element);
                        }
                    }
                }
            }
        }
    }

    /**
     * selects all the elements that could be selected
     * 
     */
    public void selectAllElements() {

        // clearing the selected elements
        selectedElements.clear();
        orderedSelectedElements.clear();

        Set<Element> elementsToSelect = new HashSet<Element>();

        // computing the set of the elements to be selected
        for (Node node = parentElement.getFirstChild(); node != null; node = node
                .getNextSibling()) {

            if (node instanceof Element
                    && EditorToolkit.isElementAShape((Element) node)) {

                elementsToSelect.add((Element) node);
            }
        }

        handleSelection(elementsToSelect, true, true);
    }

    /**
     * removes all the selected items from the selection
     */
    public void clearSelection() {
        selectedElements.clear();
        orderedSelectedElements.clear();
        refreshSelection(true);
    }

    /**
     * refreshes the selection items
     * 
     * @param propagate
     *            whether to propagate the event of the refresh selection action
     *            or not
     */
    public void refreshSelection(boolean propagate) {

        selectionItemsPainter.clear();
        selectionItems.clear();
        unLockedSelectedElements.clear();

        // checking if all the elements whose selected items
        // should be handle, have not be deleted
        for (Element element : new HashSet<Element>(selectedElements)) {

            if (element.getParentNode() == null) {

                selectedElements.remove(element);
                orderedSelectedElements.remove(element);
            }
        }

        // checking if all the locked elements have not been deleted
        for (Element element : new HashSet<Element>(lockedElements)) {

            if (element.getParentNode() == null) {

                lockedElements.remove(element);
            }
        }

        if (selectedElements.size() > 0) {

            // creating the set of the unlocked elements
            for (Element element : selectedElements) {

                if (!lockedElements.contains(element)) {

                    unLockedSelectedElements.add(element);
                }
            }

            // ������ж��ٸ��߽���Ҫ��ʾ
            Set<SelectionItem> items = getSelectionManager().getSelectionItems(
                    svgHandle, new HashSet<Element>(selectedElements),
                    selectionLevel);

            if (items != null) {
                // ��Ҫ��ʾ�ı߽�
                selectionItems.addAll(items);
            }
        }

        // reinitializing the selection items painter
        selectionItemsPainter.reinitialize();
        // svgHandle.getCanvas().refreshCanvasContent(true, false, null);

        if (propagate) {
            // notifies all the selection changed listeners that the selection
            // has changed
            notifySelectionChanged();
        }
    }

    public void refreshSelectElement(Set<SelectionItem> items) {
        selectionItemsPainter.clear();
        selectionItems.clear();
        unLockedSelectedElements.clear();
        if (items != null) {
            // ��Ҫ��ʾ�ı߽�
            selectionItems.addAll(items);
        }

        selectionItemsPainter.reinitialize();
    }

    /**
     * adds a new selection changed listener
     * 
     * @param listener
     *            a selection changed listener
     */
    public void addSelectionChangedListener(SelectionChangedListener listener) {

        if (selectionChangedListeners != null) {

            selectionChangedListeners.add(listener);
        }
    }

    /**
     * removes a selection changed listener
     * 
     * @param listener
     *            a selection changed listener
     */
    public void removeSelectionChangedListener(SelectionChangedListener listener) {

        if (selectionChangedListeners != null) {

            selectionChangedListeners.remove(listener);
        }
    }

    /**
     * notifies all the selection changed listeners that the selection has
     * changed
     */
    public void notifySelectionChanged() {

        for (SelectionChangedListener listener : selectionChangedListeners) {

            listener.selectionChanged(new HashSet<Element>(selectedElements));
        }
    }

    /**
     * notifies that the mouse button has been pressed while in the drawing mode
     * 
     * @param point
     *            a mouse point
     * @param modifier
     *            a key modifier
     * @param type
     *            the type of the drawing action
     */
    public void drawingAction(Point2D point, int modifier, int type) {

        // getting the current drawing shape
        fr.itris.glips.svgeditor.shape.AbstractShape drawingShape = selectionManager
                .getDrawingShape();
        if (drawingShape != null) {

            drawingShape.notifyDrawingAction(svgHandle, point, modifier, type);
        }

    }

    /**
     * notifies that a selection items action has occured
     * 
     * @param point
     *            a mouse point
     */
    public void itemsAction(Point2D point) {

        // getting the selection item corresponding to this point
        SelectionItem item = getSelectionItem(point);

        if (item != null) {

            // notifying the shape that handles the element
            // corresponding to the item that an items action has occured//

            // getting the element of the selection item
            Element element = item.getElements().iterator().next();

            // getting the shape module corresponding to the element
            AbstractShape shape = ShapeToolkit.getShapeModule(element,
                    svgHandle.getEditor());

            if (shape != null) {

                // notifying the shape module that an item action occured
                shape.notifyItemsAction(svgHandle, item);
            }

            // refreshing the selection
            refreshSelection(true);
        }
    }

    /**
     * draws a selection zone
     * 
     * @param currentPoint
     *            the current point
     * @param type
     *            the type of the action
     */
    public void handleZoomZone(Point2D currentPoint, int type) {

        // getting the canvas
        SVGCanvas canvas = svgHandle.getScrollPane().getSVGCanvas();

        switch (type) {

        case SELECTION_ZONE_MOUSE_PRESSED:

            // initializing the action
            firstPoint = currentPoint;
            break;

        case SELECTION_ZONE_MOUSE_RELEASED:

            // computing the rectangle corresponding the first and current
            // clicked points
            Rectangle2D rect = EditorToolkit.getComputedRectangle(firstPoint,
                    currentPoint);
            rect = svgHandle.getTransformsManager().getScaledRectangle(rect,
                    true);

            // setting the current scale for the canvas
            canvas.getZoomManager().scaleTo(rect);

            // reinitializing the data
            firstPoint = null;
            canvas.removePaintListener(ghostSelectionZoneCanvasPainter1, true);
            canvas.removePaintListener(ghostSelectionZoneCanvasPainter2, true);
            canvas.removePaintListener(ghostSelectionZoneCanvasPainter3, true);
            canvas.removePaintListener(ghostSelectionZoneCanvasPainter4, true);
            ghostSelectionZoneCanvasPainter1.reinitialize();
            ghostSelectionZoneCanvasPainter2.reinitialize();
            ghostSelectionZoneCanvasPainter3.reinitialize();
            ghostSelectionZoneCanvasPainter4.reinitialize();

            if (!svgHandle.getEditor().getRemanentModeManager()
                    .isRemanentMode()) {

                svgHandle.getEditor().getSelectionManager().setToRegularMode();
            }

            break;

        case SELECTION_ZONE_MOUSE_DRAGGED:

            // computing the current rectangle
            rect = EditorToolkit.getComputedRectangle(firstPoint, currentPoint);

            // drawing the new zone ghost rectangle//
            canvas.removePaintListener(ghostSelectionZoneCanvasPainter1, false);
            canvas.removePaintListener(ghostSelectionZoneCanvasPainter2, false);
            canvas.removePaintListener(ghostSelectionZoneCanvasPainter3, false);
            canvas.removePaintListener(ghostSelectionZoneCanvasPainter4, false);

            // computing the rectangle corresponding the first and current
            // clicked points
            rect = EditorToolkit.getComputedRectangle(firstPoint, currentPoint);

            // setting the new rectangle to the ghost painter
            ghostSelectionZoneCanvasPainter1.setPoints(new Point2D.Double(rect
                    .getX(), rect.getY()), new Point2D.Double(
                    (rect.getX() + rect.getWidth()), rect.getY()));
            ghostSelectionZoneCanvasPainter2.setPoints(new Point2D.Double(rect
                    .getX()
                    + rect.getWidth(), rect.getY()), new Point2D.Double(rect
                    .getX()
                    + rect.getWidth(), rect.getY() + rect.getHeight()));
            ghostSelectionZoneCanvasPainter3.setPoints(new Point2D.Double(rect
                    .getX(), rect.getY() + rect.getHeight()),
                    new Point2D.Double(rect.getX() + rect.getWidth(), rect
                            .getY()
                            + rect.getHeight()));
            ghostSelectionZoneCanvasPainter4.setPoints(new Point2D.Double(rect
                    .getX(), rect.getY()), new Point2D.Double(rect.getX(), rect
                    .getY()
                    + rect.getHeight()));

            canvas.addLayerPaintListener(SVGCanvas.DRAW_LAYER,
                    ghostSelectionZoneCanvasPainter1, true);
            canvas.addLayerPaintListener(SVGCanvas.DRAW_LAYER,
                    ghostSelectionZoneCanvasPainter2, true);
            canvas.addLayerPaintListener(SVGCanvas.DRAW_LAYER,
                    ghostSelectionZoneCanvasPainter3, true);
            canvas.addLayerPaintListener(SVGCanvas.DRAW_LAYER,
                    ghostSelectionZoneCanvasPainter4, true);
            break;
        }
    }

    public void clearPoints() {
        pointit.clean();
    }

    /**
     * draws a selection zone
     * 
     * @param currentPoint
     *            the current point
     * @param type
     *            the type of the action
     * @param isMultiSelectionEnabled
     *            whether the multi selection is enabled
     */
    public void handleSelectionZone(Point2D currentPoint, int type,
            boolean isMultiSelectionEnabled) {

        // getting the canvas
        SVGCanvas canvas = svgHandle.getScrollPane().getSVGCanvas();

        switch (type) {

        case SELECTION_ZONE_MOUSE_PRESSED:

            // initializing the action
            firstPoint = currentPoint;
            break;

        case SELECTION_ZONE_MOUSE_RELEASED:

            // computing the rectangle corresponding the first and current
            // clicked points ��������λ��
            Rectangle2D rect = EditorToolkit.getComputedRectangle(firstPoint,
                    currentPoint);

            if (svgHandle.getEditor().getSelectionManager().getSelectionMode() != SelectionInfoManager.SCREEN_CAST_MODE) {

                // creating the svg rectangle shape������ѡ���ͼԪ
                handleSelection(rect, isMultiSelectionEnabled);

            } else {
                // ��ͼ
                if (rect.getBounds().width > 0 && rect.getBounds().height > 0) {
                    Point svgFrameScreenPoint = getSVGHandle().getCanvas()
                            .getLocationOnScreen();
                    // ����ͳ��ȶ�����2��������Ϊ�˷�ֹ��ַʱ���µ����߱��ؽ�ȥ
                    Rectangle originCastRect = new Rectangle((int) rect.getX()
                            + svgFrameScreenPoint.x + 2, (int) rect.getY()
                            + svgFrameScreenPoint.y + 2,
                            (int) rect.getWidth() - 2,
                            (int) rect.getHeight() - 2);
                    Rectangle scroolBounds = svgHandle.getScrollPane()
                            .getInnerScrollpane().getBounds();
                    Point pScrool = svgHandle.getScrollPane()
                            .getInnerScrollpane().getLocationOnScreen();
                    scroolBounds = new Rectangle(pScrool.x, pScrool.y,
                            scroolBounds.width, scroolBounds.height);
                    Rectangle canvasBounds = svgHandle.getCanvas().getBounds();
                    Point pCanvas = svgHandle.getCanvas().getLocationOnScreen();
                    canvasBounds = new Rectangle(pCanvas.x, pCanvas.y,
                            canvasBounds.width, canvasBounds.height);
                    // scroolpane , svgcanvas��ѡ����rectangle���������ཻ�γ��������Խ�ȡ������
                    Rectangle rectCanbeCast = scroolBounds
                            .intersection(canvasBounds);
                    originCastRect = rectCanbeCast.intersection(originCastRect);
                    BufferedImage bi = Utilities.getRobot()
                            .createScreenCapture(originCastRect);
                    ImageTransferable clipContent = new ImageTransferable(bi);
                    Toolkit.getDefaultToolkit().getSystemClipboard()
                            .setContents(clipContent, clipContent);
                    ((NCIScreenCastModule) svgHandle.getEditor().getModule(
                            NCIScreenCastModule.ScreenCastModuleID))
                            .showPromptInfo();
                }

            }

            // reinitializing the data
            firstPoint = null;
            canvas.removePaintListener(ghostSelectionZoneCanvasPainter1, true);
            canvas.removePaintListener(ghostSelectionZoneCanvasPainter2, true);
            canvas.removePaintListener(ghostSelectionZoneCanvasPainter3, true);
            canvas.removePaintListener(ghostSelectionZoneCanvasPainter4, true);
            ghostSelectionZoneCanvasPainter1.reinitialize();
            ghostSelectionZoneCanvasPainter2.reinitialize();
            ghostSelectionZoneCanvasPainter3.reinitialize();
            ghostSelectionZoneCanvasPainter4.reinitialize();

            break;

        case SELECTION_ZONE_MOUSE_DRAGGED:

            // computing the current rectangle
            rect = EditorToolkit.getComputedRectangle(firstPoint, currentPoint);

            // drawing the new zone ghost rectangle//
            canvas.removePaintListener(ghostSelectionZoneCanvasPainter1, false);
            canvas.removePaintListener(ghostSelectionZoneCanvasPainter2, false);
            canvas.removePaintListener(ghostSelectionZoneCanvasPainter3, false);
            canvas.removePaintListener(ghostSelectionZoneCanvasPainter4, false);

            // computing the rectangle corresponding the first and current
            // clicked points
            rect = EditorToolkit.getComputedRectangle(firstPoint, currentPoint);

            // setting the new rectangle to the ghost painter
            ghostSelectionZoneCanvasPainter1.setPoints(new Point2D.Double(rect
                    .getX(), rect.getY()), new Point2D.Double(
                    (rect.getX() + rect.getWidth()), rect.getY()));
            ghostSelectionZoneCanvasPainter2.setPoints(new Point2D.Double(rect
                    .getX()
                    + rect.getWidth(), rect.getY()), new Point2D.Double(rect
                    .getX()
                    + rect.getWidth(), rect.getY() + rect.getHeight()));
            ghostSelectionZoneCanvasPainter3.setPoints(new Point2D.Double(rect
                    .getX(), rect.getY() + rect.getHeight()),
                    new Point2D.Double(rect.getX() + rect.getWidth(), rect
                            .getY()
                            + rect.getHeight()));
            ghostSelectionZoneCanvasPainter4.setPoints(new Point2D.Double(rect
                    .getX(), rect.getY()), new Point2D.Double(rect.getX(), rect
                    .getY()
                    + rect.getHeight()));

            canvas.addLayerPaintListener(SVGCanvas.DRAW_LAYER,
                    ghostSelectionZoneCanvasPainter1, true);
            canvas.addLayerPaintListener(SVGCanvas.DRAW_LAYER,
                    ghostSelectionZoneCanvasPainter2, true);
            canvas.addLayerPaintListener(SVGCanvas.DRAW_LAYER,
                    ghostSelectionZoneCanvasPainter3, true);
            canvas.addLayerPaintListener(SVGCanvas.DRAW_LAYER,
                    ghostSelectionZoneCanvasPainter4, true);
            break;
        }

    }

    /**
     * executes an action according to the item that was selected by the user
     * and the point where the mouse can be found
     * 
     * @param currentPoint
     *            the current position of the mouse
     */
    public void doAction(Point2D currentPoint) {

        if (!actionDisabled && !isSelectionLocked()) {

            // converting the given point into the user space units
            Point2D scaledPoint = svgHandle.getTransformsManager()
                    .getScaledPoint(currentPoint, true);

            if (firstPoint == null) {

                // initializing the action//

                // storing the first point
                firstPoint = scaledPoint;

                // checking if the given point matches a selection item
                actionItem = getSelectionItem(currentPoint);

                // checking if the given point is included in a shape//
                Element translationElement = null;

                if (actionItem == null) {

                    // getting the element that can be found at the given
                    // location
                    translationElement = svgHandle.getSvgElementsManager()
                            .getNodeAt(parentElement, scaledPoint);

                    if (translationElement != null) {
                        String str = translationElement.getNodeName();
                        if (str.equals("g")) {
                            String strID = translationElement
                                    .getAttribute("id");
                            if (strID != null && strID.equals("nci:terminal")) {
                                return;
                            }
                        }
                    }

                    if (translationElement != null
                            && !lockedElements.contains(translationElement)) {

                        // checking if this element is already selected,
                        // then all the selected elements will be translated
                        if (selectedElements.contains(translationElement)
                                || selectedElements
                                        .contains((Element) translationElement
                                                .getParentNode())) {

                            translationElements
                                    .addAll(unLockedSelectedElements);

                        } else {

                            // otherwise, all the selected elements will be
                            // deselected,
                            // and the found element will be translated
                            selectedElements.clear();
                            orderedSelectedElements.clear();
                            refreshSelection(true);
                            translationElements.add(translationElement);
                        }
                    }
                }

                // checking if a selection item or an element matches the point,
                // if not, exits the method
                if (actionItem == null && translationElements.size() == 0) {

                    // reinitializes the data and returns
                    firstPoint = null;
                    setSelectionSubMode(REGULAR_SUB_MODE);
                    actionDisabled = true;
                    return;
                }

                if (selectionSubMode == REGULAR_SUB_MODE) {

                    // computing the new type of the action
                    if (actionItem != null) {

                        setSelectionSubMode(ACTION_SUB_MODE);

                    } else {

                        setSelectionSubMode(TRANSLATION_SUB_MODE);
                    }
                }

                // getting the selection manager
                switch (selectionSubMode) {

                case TRANSLATION_SUB_MODE:

                    actionSelectionManager = (translationElements.size() > 1 ? multiSelectionManager
                            : singleSelectionManager);
                    break;

                case ACTION_SUB_MODE:

                    actionSelectionManager = getSelectionManager();
                    break;
                }
            }

            // checking if the selection items should be still painted or not
            setBlockSelectionItemsPaint(!(actionItem != null && actionItem
                    .isPersistent()));

            // handling the action for the current point of the drag action
            switch (selectionSubMode) {

            case TRANSLATION_SUB_MODE: {

                // translating the elements
                actionSelectionManager.doTranslateAction(translationElements,
                        firstPoint, scaledPoint);

                Element element = translationElements.iterator().next();
                if (element != null
                        && element.getNodeName().equals("path")
                        && element.getAttribute("d").toLowerCase().indexOf('z') == -1) {
                    int xoffset = (int) (scaledPoint.getX() - firstPoint.getX());
                    int yoffset = (int) (scaledPoint.getY() - firstPoint.getY());
                    if (xoffset != 0 && yoffset != 0)
                        redrawNciPoints(translationElements.iterator().next(),
                                xoffset, yoffset);
                }
                break;
            }

            case ACTION_SUB_MODE: {

                // doing the action
                actionSelectionManager.doAction(new HashSet<Element>(
                        selectedElements), firstPoint, scaledPoint, actionItem);
                Element element = selectedElements.iterator().next();
                if (element != null
                        && element.getNodeName().equals("path")
                        && element.getAttribute("d").toLowerCase().indexOf('z') == -1
                        && actionItem != null)
                    redrawNciPoints(scaledPoint);
                break;
            }
            }
        }
    }

    /**
     * ������ѡ�е�ͼԪ�������Ͼ�����Ϣ
     * 
     * @return:������Ϣ
     */
    public Rectangle2D getBounds() {

        return MultiAbstractShape
                .getElementsBounds(svgHandle, selectedElements);
    }

    /**
     * executes an action according to the item that was selected by the user
     * and the point where the mouse can be found
     * 
     * @param currentPoint
     *            the current position of the mouse
     */
    public void validateAction(Point2D currentPoint) {

        if (!isSelectionLocked()) {

            // converting the given point into the user space units
            Point2D scaledPoint = svgHandle.getTransformsManager()
                    .getScaledPoint(currentPoint, true);

            if (actionSelectionManager != null) {

                // validating the action
                switch (selectionSubMode) {

                case TRANSLATION_SUB_MODE:

                    // translating the elements
                    if (selectionListener.isBControl()) {
                        actionSelectionManager.copyElement(translationElements,
                                firstPoint);
                    } else {
                        // ����filter��Ӱ�죬��remove��filter��ƽ�����������filter
                        Iterator<Element> it = selectedElements.iterator();
                        HashMap<Element, String> filterEleMap = new HashMap<Element, String>();
                        while (it.hasNext()) {
                            Element ele = it.next();
                            String filter = ele.getAttribute("filter");
                            if (filter != null && !filter.equals("")) {
                                filterEleMap.put(ele, filter);
                                ele.removeAttribute("filter");
                            }
                        }
                        actionSelectionManager.validateTranslateAction(
                                translationElements, firstPoint, scaledPoint);
                        Iterator<Element> filterEleIt = filterEleMap.keySet()
                                .iterator();
                        while (filterEleIt.hasNext()) {
                            Element e = filterEleIt.next();
                            String filter = filterEleMap.get(e);
                            e.setAttributeNS(null, "filter", filter);
                        }
                    }
                    break;

                case ACTION_SUB_MODE:

                    // doing the action
                    actionSelectionManager.validateAction(new HashSet<Element>(
                            selectedElements), firstPoint, scaledPoint,
                            actionItem);
                    break;
                }
            }

            // reinitializing the data
            firstPoint = null;
            actionItem = null;
            actionSelectionManager = null;
            translationElements.clear();
            setSelectionSubMode(REGULAR_SUB_MODE);
        }
    }

    /**
     * translates the currently selected points by the provided factors
     * 
     * @param delta
     *            the translation factors
     */
    public void translate(Point2D delta) {

        if (!isSelectionLocked()) {

            // getting the scaled translation factors
            Point2D scaledDelta = svgHandle.getTransformsManager()
                    .getScaledPoint(delta, true);

            // getting the current action manager
            actionSelectionManager = getSelectionManager();

            if (actionSelectionManager != null) {

                Iterator<Element> it = selectedElements.iterator();
                HashMap<Element, String> filterEleMap = new HashMap<Element, String>();
                while (it.hasNext()) {
                    Element ele = it.next();
                    String filter = ele.getAttribute("filter");
                    if (filter != null && !filter.equals("")) {
                        filterEleMap.put(ele, filter);
                        ele.removeAttribute("filter");
                    }
                }
                // translating the elements
                actionSelectionManager.validateTranslateAction(
                        new HashSet<Element>(selectedElements), new Point(-1,
                                -1), scaledDelta);
                Iterator<Element> filterEleIt = filterEleMap.keySet()
                        .iterator();
                while (filterEleIt.hasNext()) {
                    Element e = filterEleIt.next();
                    String filter = filterEleMap.get(e);
                    e.setAttributeNS(null, "filter", filter);
                }
            }
        }
    }

    /**
     * refreshes the labels displaying the current position of the mouse on the
     * canvas
     * 
     * @param currentMousePoint
     *            the current mouse point
     */
    public void refreshSVGFrame(Point currentMousePoint) {

        // converting the given point into the user space units
        Point2D scaledPoint = svgHandle.getTransformsManager().getScaledPoint(
                currentMousePoint, true);

        // refreshing the labels displaying the current position of the mouse on
        // the canvas
        svgHandle.getSVGFrame().getStateBar().setMousePosition(scaledPoint);
    }

    /**
     * ����������߽ڵ㣬���϶��γɵ�ƫ�������������ӵ����ϵ�
     * 
     * @param element
     *            ���߽ڵ�
     * @param xoffset
     *            ��������ƫ����
     * @param yoffset
     *            ��������ƫ����
     */
    public void redrawNciPoints(Element element, int xoffset, int yoffset) {
        if (!element.getNodeName().equals("path"))
            return;

        String dPath = element.getAttribute("d");
        Path path = new Path(dPath);
        int nCount = path.getSegmentsNumber();
        Segment seg = path.getSegment();
        // bpointΪ�߽ڵ���ʼ�����꣬epointΪ�߽ڵ���ֹ������
        Point2D bpoint = seg.getEndPoint();
        for (int i = 0; i < nCount - 1; i++)
            seg = seg.getNextSegment();
        Point2D epoint = seg.getEndPoint();

        bpoint.setLocation(bpoint.getX() + xoffset, bpoint.getY() + yoffset);
        epoint.setLocation(epoint.getX() + xoffset, epoint.getY() + yoffset);

        Set<SelectionItem> items = new HashSet<SelectionItem>();
        Set<SelectionItem> itemTemp = getPointItems(bpoint);
        if (itemTemp != null)
            items.addAll(itemTemp);

        itemTemp = getPointItems(epoint);
        if (itemTemp != null)
            items.addAll(itemTemp);

        pointit.reinitialize(items, SVGCanvas.TOP_LAYER);
        return;
    }

    /**
     * �������������㣬����ͼԪ���ӵ����ϵ�
     * 
     * @param currentMousePoint
     *            �������
     */
    public void redrawNciPoints(Point2D currentMousePoint) {

        Set<SelectionItem> items = getPointItems(currentMousePoint);

        pointit.reinitialize(items, SVGCanvas.TOP_LAYER);
        return;
    }

    public void redrawNciPoints(Set<SelectionItem> items, int nLayer) {

        pointit.reinitialize(items, nLayer);
        return;
    }

    public Set<SelectionItem> getPointItems(Point2D currentPoint) {
        Set<SelectionItem> items = null;
        // items = getNciPointItems(currentPoint);
        if (items == null) {
            items = getLinkPointItems(currentPoint);
        }
        return items;
    }

    /**
     * �������������㣬������Ҫ���Ƶ�ͼԪ���ӵ����ϵ�
     * 
     * @param currentPoint
     *            ������������
     * @return������з��ϸõ�Ҫ���ͼԪ���ӵ����ϵ��򷵻�set��ʧ�ܻ��쳣����null
     */
    public Set<SelectionItem> getNciPointItems(Point2D currentPoint) {
        Element element = svgHandle.getSvgElementsManager().getNodeAt(
                svgHandle.getCanvas().getDocument().getDocumentElement(),
                currentPoint, "image");

        Set<SelectionItem> items = null;
        if (element == null) {
            return null;
        }

        if (element.getNodeName().equals("image")) {
            Set<Element> elements = new HashSet<Element>();
            elements.add(element);
            items = new HashSet<SelectionItem>();
            Element terminal = (Element) element.getElementsByTagName(
                    "terminal").item(0);
            if (terminal == null) {
                return null;
            }

            NodeList ncipoints = terminal.getElementsByTagName("nci:POINT");
            if (ncipoints == null) {
                return null;
            }

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
            final AffineTransform initialTransform = svgHandle
                    .getSvgElementsManager().getTransform(element);
            if (nSize > 0) {
                for (int i = 0; i < nSize; i++) {
                    Element mdelement = (Element) ncipoints.item(i);
                    x1 = new Integer(mdelement.getAttribute("x")).intValue();
                    y1 = new Integer(mdelement.getAttribute("y")).intValue();
                    double[] pointo = { dx + x1 * nw / sw, dy + y1 * nh / sh };

                    // ͨ������任����ȡʵ�ʵ������ַ

                    initialTransform.transform(pointo, 0, pointo, 0, 1);
                    double dx2 = pointo[0];
                    double dy2 = pointo[1];
                    Point pp = new Point((int) dx2, (int) dy2);

                    // �ж��������������ѯ�õ������ӵ���룬���С��3����Ϊ������ϣ��豸��������
                    if (currentPoint.distance(dx2, dy2) > 3) {
                        items.add(new SelectionItem(svgHandle, elements,
                                svgHandle.getTransformsManager()
                                        .getScaledPoint(pp, false),
                                SelectionItem.POINT, SelectionItem.POINT_STYLE,
                                0, false, null));
                    } else {
                        // ����������ӵ����С��3����������ϣ�������ӵ���Ϣ��������ϵ�
                        items.clear();
                        items.add(new SelectionItem(svgHandle, elements,
                                svgHandle.getTransformsManager()
                                        .getScaledPoint(pp, false),
                                SelectionItem.POINT,
                                SelectionItem.CENTER_POINT_STYLE, 0, false,
                                null));
                        break;
                    }

                }
            }
        }
        return items;
    }

    public void hyperLink(Point2D currentPoint) {
        Point2D scaledPoint = svgHandle.getTransformsManager().getScaledPoint(
                currentPoint, true);
        Element element = svgHandle.getSvgElementsManager().getNodeAt(
                svgHandle.getCanvas().getDocument().getDocumentElement(),
                scaledPoint, null);
        if (element == null)
            return;

        svgHandle.getEditor().getSvgSession().openRemoteSVGFileByCode(element);
        /*
        if (svgHandle.getNFileType() == 0)// ϵͳͼ
        {
            if (element.getNodeName().equals("use")
                    || element.getNodeName().equals("image")
                    || element.getNodeName().equals("rect")
                    || element.getNodeName().equals("ellipse")) {
                Element objRef = (Element) element.getElementsByTagName(
                        "PSR:ObjRef").item(0);
                if (objRef != null) {
                    String strObjectID = objRef.getAttribute("AppCode");
                    String strFieldNo = objRef.getAttribute("FieldNo");
                    if (strObjectID != null && strObjectID.length() > 0
                            && strFieldNo != null && strFieldNo.length() > 0) {
                        svgHandle
                                .getEditor()
                                .getSvgSession()
                                .openRemoteSVGFile("1", strObjectID, strFieldNo);
                    }
                }
            } else if (element.getNodeName().equals("text")) {
                String strID = element.getAttribute("nci_element");
                if (strID != null && strID.length() > 0) {
                    element = svgHandle.getCanvas().getMapInfo(strID);
                    if (element != null) {
                        if (element.getNodeName().equals("use")
                                || element.getNodeName().equals("image")
                                || element.getNodeName().equals("rect")
                                || element.getNodeName().equals("ellipse")) {
                            Element objRef = (Element) element
                                    .getElementsByTagName("PSR:ObjRef").item(0);
                            if (objRef != null) {
                                String strObjectID = objRef
                                        .getAttribute("AppCode");
                                String strFieldNo = objRef
                                        .getAttribute("FieldNo");
                                if (strObjectID != null
                                        && strObjectID.length() > 0
                                        && strFieldNo != null
                                        && strFieldNo.length() > 0) {
                                    svgHandle.getEditor().getSvgSession()
                                            .openRemoteSVGFile("1",
                                                    strObjectID, strFieldNo);
                                }
                            }
                        } else if (element.getNodeName().equals("path")) {
                            Element objRef = (Element) element
                                    .getElementsByTagName("PSR:ObjRef").item(0);
                            if (objRef != null) {
                                String strObjectID = objRef
                                        .getAttribute("AppCode");
                                String strFieldNo = objRef
                                        .getAttribute("FieldNo");
                                if (strObjectID != null
                                        && strObjectID.length() > 0
                                        && strFieldNo != null
                                        && strFieldNo.length() > 0) {
                                    svgHandle.getEditor().getSvgSession()
                                            .openRemoteSVGFile("2",
                                                    strObjectID, strFieldNo);
                                }
                            }
                        }
                    }
                }
            } else if (element.getNodeName().equals("path")) {
                Element objRef = (Element) element.getElementsByTagName(
                        "PSR:ObjRef").item(0);
                if (objRef != null) {
                    String strObjectID = objRef.getAttribute("AppCode");
                    String strFieldNo = objRef.getAttribute("FieldNo");
                    if (strObjectID != null && strObjectID.length() > 0
                            && strFieldNo != null && strFieldNo.length() > 0) {
                        svgHandle.getEditor().getSvgSession()
                                .openRemoteSVGFile(strObjectID, "2",
                                        strObjectID, strFieldNo);
                    }
                }
            }
        } else if (svgHandle.getNFileType() == 1) {
            if (element.getNodeName().equals("text")) {
                String strID = element.getAttribute("nci_element");
                if (strID != null && strID.length() > 0) {
                    svgHandle.getEditor().getSvgSession().openRemoteSVGFile(
                            strID, "2", strID, "0");
                }

            } else {
                Element cimClass = (Element) element.getElementsByTagName(
                        "PSR:CimClass").item(0);
                if (cimClass == null) {
                    Element parent = (Element) element.getParentNode();
                    cimClass = (Element) parent.getElementsByTagName(
                            "PSR:CimClass").item(0);
                    element = parent;
                }

                if (cimClass != null
                        && (cimClass.getAttribute("CimType").toLowerCase()
                                .equals("acline") || cimClass.getAttribute(
                                "CimType").toLowerCase().equals("link"))) {
                    Element ObjRef = (Element) element.getElementsByTagName(
                            "PSR:ObjRef").item(0);
                    if (ObjRef != null) {
                        String strObjectID = ObjRef.getAttribute("AppCode");
                        // ���ݱ��ر�ź�acline��Ż�ȡ��һ�˳�վ���
                        // svgHandle.getEditor().getSvgSession()
                        // .openRemoteSVGFile(
                        // strObjectID,
                        // new String().format("%d", svgHandle
                        // .getNFileType()),
                        // svgHandle.getStrNciName(),
                        // svgHandle.getStrNciClass());
                        svgHandle.getEditor().getSvgSession()
                                .openRemoteSVGFile("2", strObjectID, "0");
                    }
                }
            }
        } else if (svgHandle.getNFileType() == 2) {
            // add by yuxiang at jx
            if (element.getNodeName().equals("use")
                    || element.getNodeName().equals("image")
                    || element.getNodeName().equals("rect")
                    || element.getNodeName().equals("ellipse")) {
                Element objRef = (Element) element.getElementsByTagName(
                        "PSR:ObjRef").item(0);
                if (objRef != null) {
                    String strObjectID = objRef.getAttribute("AppCode");
                    String strFieldNo = objRef.getAttribute("FieldNo");
                    if (strObjectID != null && strObjectID.length() > 0
                            && strFieldNo != null && strFieldNo.length() > 0) {
                        svgHandle
                                .getEditor()
                                .getSvgSession()
                                .openRemoteSVGFile("1", strObjectID, strFieldNo);
                    }
                }
            }
        } else {
            String strType = element.getAttribute("type");
            if (element.getNodeName().equals("g") && strType != null
                    && strType.equals("solidified")) {
                String strUrl = element.getAttribute("nci-targeturl");
                if (strUrl != null && strUrl.length() > 0) {
                    Utilities.gotoWebSite(strUrl, "");
                }
            }
        }
        */

        return;
    }

    public Set<SelectionItem> getLinkPointItems(Point2D currentPoint) {
        Set<Element> elements = svgHandle.getSvgElementsManager().getNodeAt(
                svgHandle.getCanvas().getDocument().getDocumentElement(),
                currentPoint, null, 0);

        Set<SelectionItem> items = null;
        if (elements == null || elements.size() == 0) {
            return null;
        }
        if (selectedElements.iterator().next().getNodeName().equals("path")) {
            String strPathID = ((Element) selectedElements.iterator().next())
                    .getAttribute("id");

            for (Element element : elements) {

                if (isLineShape(element)
                        || (element.getNodeName().equals("use") || element
                                .getElementsByTagName("use").getLength() > 0)
                        || (element.getNodeName().equals("image"))) {
                    String strSelectID = element.getAttribute("id");
                    if (strSelectID == null || !strSelectID.equals(strPathID)) {
                        Set<Element> Selectelements = new HashSet<Element>();
                        Selectelements.add(element);
                        items = new HashSet<SelectionItem>();
                        items.add(new SelectionItem(svgHandle, Selectelements,
                                svgHandle.getTransformsManager()
                                        .getScaledPoint(currentPoint, false),
                                SelectionItem.POINT,
                                SelectionItem.CENTER_POINT_STYLE, 0, false,
                                null));
                        break;
                    }
                }
            }
        }
        return items;
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
     * handles the cursor for the given point. ����������״��
     * 
     * @param currentMousePoint
     */
    public void handleCursor(Point currentMousePoint) {

        // converting the given point into the user space units
        Point2D scaledPoint = svgHandle.getTransformsManager().getScaledPoint(
                currentMousePoint, true);

        // getting the selection item corresponding to this point, if one exists
        SelectionItem currentItem = null;

        for (SelectionItem item : new HashSet<SelectionItem>(selectionItems)) {

            if (item.intersectsItemForMouseMove(currentMousePoint)) {

                currentItem = item;
                break;
            }
        }

        // computing the point alignedwith rulers point
        Point2D point = svgHandle.getTransformsManager()
                .getAlignedWithRulersPoint(currentMousePoint, true);

        // refreshing the labels displaying the current position of the mouse on
        // the canvas
        svgHandle.getSVGFrame().getStateBar().setMousePosition(point);
        // //added by wangql
        // if (NCIBoxPane.mouseStatus != Constants.MOUSE_PRESSED) {
        if (currentItem != null) {
            // setting the cursor associated with the item for the canvas
            svgHandle.getScrollPane().getSVGCanvas().setSVGCursor(
                    currentItem.getCursor());
            svgHandle.getScrollPane().getViewport().setCursor(
                    currentItem.getCursor());

        } else {
            Cursor screenCastCursor = null;
            int cursorType = Cursor.DEFAULT_CURSOR;
            int selectionMode = svgHandle.getEditor().getSelectionManager()
                    .getSelectionMode();

            if (selectionMode == SelectionInfoManager.DRAWING_MODE
                    || selectionMode == SelectionInfoManager.ZONE_MODE
                    || selectionMode == SelectionInfoManager.ZOOM_MODE) {

                cursorType = Cursor.CROSSHAIR_CURSOR;

            } else if (selectionMode == SelectionInfoManager.SCREEN_CAST_MODE) {
                try {
                    screenCastCursor = ScreenCastCursor.getScreenCastCursor();
                } catch (HeadlessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (AWTException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (!isSelectionLocked()) {

                // getting the svg element corresponding to this point
                Element element = svgHandle.getSvgElementsManager().getNodeAt(
                        parentElement, scaledPoint);

                if (element != null) {
                    String str = element.getNodeName();
                    if (str.equals("g")) {
                        String strID = element.getAttribute("id");
                        if (strID != null && strID.equals("nci:terminal")) {
                            return;
                        }
                    }
                }

                if (element != null) {
                    String str = element.getNodeName();
                    if (str.equals("g")) {
                        String strID = element.getAttribute("id");
                        if (strID != null && strID.equals("nci:terminal")) {
                            return;
                        }
                    }
                }

                if (element != null && !isLocked(element)) {
                    // System.out.println("element:" + element + "."
                    // + this.getClass().getName());
                    cursorType = Cursor.MOVE_CURSOR;
                    if (element.getNodeName().equals("a")) {
                        // ������������չʾ
                        cursorType = Cursor.HAND_CURSOR;
                    }
                }
                svgHandle.getEditor().getToolTipManager()
                        .handleToolTip(element);
            }
            Cursor newCursor = null;
            if (selectionMode != SelectionInfoManager.SCREEN_CAST_MODE)
                newCursor = Cursor.getPredefinedCursor(cursorType);
            else
                newCursor = screenCastCursor;

            svgHandle.getScrollPane().getSVGCanvas().setSVGCursor(newCursor);
            svgHandle.getScrollPane().getViewport().setCursor(newCursor);
            // }
        }
    }

    /**
     * returns the selection item (if it exists) that can be found at the given
     * point
     * 
     * @param point
     *            a mouse point
     * @return the selection item (if it exists) that can be found at the given
     *         point
     */
    public SelectionItem getSelectionItem(Point2D point) {

        SelectionItem currentItem = null;

        for (SelectionItem item : new HashSet<SelectionItem>(selectionItems)) {

            if (item.intersectsItem(point)) {

                currentItem = item;
                break;
            }
        }

        return currentItem;
    }

    /**
     * @return the svg handle that is associated to this object
     */
    public SVGHandle getSVGHandle() {
        return svgHandle;
    }

    /**
     * @return the set of the selected elements
     */
    public Set<Element> getSelectedElements() {
        return selectedElements;
    }

    /**
     * @return the linkedlist of the selected elements, the order is the one
     *         used by the user to select the elements
     */
    public LinkedList<Element> getOrderedSelectedElements() {
        return orderedSelectedElements;
    }

    /**
     * @return selectionLevel.
     */
    public int getSelectionLevel() {
        return selectionLevel;
    }

    /**
     * returns whether the provided element can be a parent element or not
     * 
     * @param element
     *            an element
     * @return whether the provided element can be a parent element or not
     */
    public static boolean isParentElementEligible(Element element) {

        return element != null && element.getNodeName().equals("g");
    }

    /**
     * @return whether one or more locked elements can be found in the selected
     *         elements
     */
    public boolean isSelectionLocked() {

        return !unLockedSelectedElements.containsAll(selectedElements);
    }

    /**
     * @return whether all the selected elements are locked
     */
    public boolean isAllSelectionLocked() {

        return unLockedSelectedElements.isEmpty()
                && selectedElements.size() > 0;
    }

    /**
     * returns whether the given element is locked or not
     * 
     * @param element
     *            an element
     * @return whether the given element is locked or not
     */
    public boolean isLocked(Element element) {

        return lockedElements.contains(element);
    }

    /**
     * locks the currently selected elements
     */
    public void lock() {

        lockedElements.addAll(selectedElements);
        refreshSelection(true);
    }

    /**
     * ���ڵ��������
     * 
     * @param element
     */
    public void lock(Element element) {
        lockedElements.add(element);
        refreshSelection(true);
    }

    /**
     * unlocks the currently selected elements
     */
    public void unLock() {

        lockedElements.removeAll(selectedElements);
        refreshSelection(true);
    }

    /**
     * @return the locked elements
     */
    public Set<Element> getLockedElements() {

        return lockedElements;
    }

    // add by yuxiang
    public void openSVGProperties() {
        SVGProperties module = (SVGProperties) (svgHandle.getEditor()
                .getModule("Properties"));
        module.openProperties();
        return;
    }

    /**
     * the class used to paint the selection items on the canvas
     * 
     * @author ITRIS, Jordi SUC
     */
    protected class SelectionItemsCanvasPainter extends CanvasPainter {

        /**
         * the current clip of this canvas painter
         */
        private Set<Rectangle2D> clips = new HashSet<Rectangle2D>();

        @Override
        public void paintToBeDone(Graphics2D g) {

            // painting the selection items
            Set<SelectionItem> items = new HashSet<SelectionItem>(
                    selectionItems);

            for (SelectionItem item : items) {

                item.paint(g);
            }
        }

        /**
         * clears this painter
         */
        protected void clear() {

            // recomputing the clip
            clips.clear();

            if (selectionItems.size() > 0) {

                // adding all the new clips
                for (SelectionItem item : selectionItems) {

                    clips.add(new Rectangle(item.getShapeBounds()));
                }
            }
        }

        /**
         * stops displaying the selection items and cleans the canvas
         */
        protected void clean() {

            SVGCanvas canvas = svgHandle.getScrollPane().getSVGCanvas();

            if (clips.size() > 0) {

                canvas.removePaintListener(this, true);
            }
        }

        /**
         * reinitializes all the selection items
         */
        protected void reinitialize() {

            final SVGCanvas canvas = svgHandle.getScrollPane().getSVGCanvas();

            if (clips.size() > 0) {

                canvas.removePaintListener(this, true);
                clips.clear();
            }

            if (selectionItems.size() > 0) {

                // adding all the new clips
                for (SelectionItem item : selectionItems) {
                    // ���ӱ߽Ǽ�ͷ��Ӧ�ľ���
                    clips.add(new Rectangle(item.getShapeBounds()));
                }

                final SelectionItemsCanvasPainter s = this;
                canvas.addLayerPaintListener(SVGCanvas.DRAW_LAYER, s, true);
                // SwingWorker worker = new SwingWorker() {
                //
                // @Override
                // protected Object doInBackground() throws Exception {
                // canvas.addLayerPaintListener(SVGCanvas.DRAW_LAYER, s,
                // true);
                // return null;
                // }
                // };
                // worker.execute();
            }
        }

        @Override
        public Set<Rectangle2D> getClip() {

            return clips;
        }
    }

    /**
     * the painter used to draw ghost selection zones on a canvas
     * 
     * @author ITRIS, Jordi SUC
     */
    protected static class GhostSelectionZoneCanvasPainter extends
            CanvasPainter {

        /**
         * the stroke for the ghost
         */
        private static Stroke stroke = new BasicStroke(1,
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1, new float[] {
                        5, 5 }, 0);
        /**
         * the points of the line to draw
         */
        private Point2D point1, point2;
        /**
         * the set of the clip rectangles
         */
        private Set<Rectangle2D> clips = new HashSet<Rectangle2D>();

        @Override
        public void paintToBeDone(Graphics2D g) {

            if (point1 != null && point2 != null) {

                g = (Graphics2D) g.create();
                g.setColor(Color.black);
                g.setStroke(stroke);
                g.setXORMode(Color.white);
                g.drawLine((int) point1.getX(), (int) point1.getY(),
                        (int) point2.getX(), (int) point2.getY());
                g.dispose();
            }
        }

        @Override
        public Set<Rectangle2D> getClip() {

            return clips;
        }

        /**
         * sets the points for the line
         * 
         * @param point1
         *            the first point of the line
         * @param point2
         *            the second point of the line
         */
        public void setPoints(Point2D point1, Point2D point2) {

            this.point1 = point1;
            this.point2 = point2;
            clips.clear();

            if (point1 != null && point2 != null) {

                clips.add(new Rectangle2D.Double(point1.getX(), point1.getY(),
                        point2.getX() - point1.getX() + 1, point2.getY()
                                - point1.getY() + 1));
            }
        }

        /**
         * reinitializing the painter
         */
        public void reinitialize() {

            point1 = null;
            point2 = null;
        }
    }

    /**
     * �������ӵ����ϵ�Ļ���ˢ��
     * 
     * @author yx
     * 
     */
    protected class PointItemsCanvasPainter extends CanvasPainter {

        /**
         * the current clip of this canvas painter
         */
        private Set<Rectangle2D> clips = new HashSet<Rectangle2D>();
        private Set<SelectionItem> pointitems = null;

        @Override
        public void paintToBeDone(Graphics2D g) {

            // painting the selection items
            // Set<SelectionItem> items = new HashSet<SelectionItem>(
            // selectionItems);
            if (pointitems == null)
                return;

            for (SelectionItem item : pointitems) {

                item.paint(g);
            }
        }

        /**
         * clears this painter
         */
        protected void clear() {

            // recomputing the clip
            clips.clear();

            if (pointitems != null && pointitems.size() > 0) {

                // adding all the new clips
                for (SelectionItem item : pointitems) {

                    clips.add(new Rectangle(item.getShapeBounds()));
                }
            }
        }

        /**
         * stops displaying the selection items and cleans the canvas
         */
        protected void clean() {

            SVGCanvas canvas = svgHandle.getScrollPane().getSVGCanvas();

            if (clips.size() > 0) {

                canvas.removePaintListener(this, true);
            }
        }

        /**
         * reinitializes all the selection items
         */
        protected void reinitialize(Set<SelectionItem> Items, int nLayer) {

            final SVGCanvas canvas = svgHandle.getScrollPane().getSVGCanvas();

            pointitems = Items;
            if (clips.size() > 0) {

                canvas.removePaintListener(this, true);
                clips.clear();
            }

            if (pointitems != null && pointitems.size() > 0) {

                // adding all the new clips
                for (SelectionItem item : pointitems) {
                    // ���ӱ߽Ǽ�ͷ��Ӧ�ľ���
                    clips.add(new Rectangle(item.getShapeBounds()));
                }

                final PointItemsCanvasPainter s = this;
                canvas.addLayerPaintListener(nLayer, s, true);
                // SwingWorker worker = new SwingWorker() {
                //
                // @Override
                // protected Object doInBackground() throws Exception {
                // canvas.addLayerPaintListener(SVGCanvas.DRAW_LAYER, s,
                // true);
                // return null;
                // }
                // };
                // worker.execute();
            }
        }

        @Override
        public Set<Rectangle2D> getClip() {

            return clips;
        }
    }

    public SelectionChangedListener getDrawLineListener() {
        return drawLineListener;
    }

    public void setDrawLineListener(SelectionChangedListener drawLineListener) {
        this.drawLineListener = drawLineListener;
    }

    public void drawLineAtPoint(Point2D point) {
    	point = svgHandle.getTransformsManager().getScaledPoint(point, true);
        if (selectedElements == null || selectedElements.size() != 1)
            return;
        Element element = selectedElements.iterator().next();

        if (element == null || !element.getNodeName().equals("path"))
            return;

        String sPath = element.getAttribute("d");
        Path path = new Path(sPath);
        int nCount = path.getSegmentsNumber();
        if (nCount != 2)
            return;

        Segment se = path.getSegment();
        Point2D bPoint = se.getEndPoint();
        se = se.getNextSegment();
        Point2D ePoint = se.getEndPoint();
        ExtendedGeneralPath egPath = null;// = new ExtendedGeneralPath();
        if (nLineMode == PARALLEL_LINE_MODE) { // ����ƽ����
            egPath = calParallelLine(bPoint, ePoint, point);
        } else if (nLineMode == VERTICAL_LINE_MODE) { // ���ƴ�ֱ��
            egPath = calVerticalLine(bPoint, ePoint, point);
        } else if (nLineMode == VERTICAL_MIDDLE_LINE_MODE) { // ���ƴ�����
            egPath = calVerticalMiddleLine(bPoint, ePoint, point);
        }
        PathShape shape = new PathShape(svgHandle.getEditor());
        Element el = shape.createElement(svgHandle, egPath);

        nLineMode = NONE_LINE_MODE;
        shape.resetDrawing();
        return;
    }

    /**
     * ����ƽ����
     * 
     * @param beginPoint
     *            ��ʼ��
     * @param endPoint
     *            ��ֹ��
     * @param curPoint
     *            �����
     * @return
     */
    public ExtendedGeneralPath calParallelLine(Point2D beginPoint,
            Point2D endPoint, Point2D curPoint) {
        ExtendedGeneralPath egPath = new ExtendedGeneralPath();
        if (beginPoint.getX() == endPoint.getX()) {
            egPath.moveTo((float) curPoint.getX(), (float) beginPoint.getY());
            egPath.lineTo((float) curPoint.getX(), (float) endPoint.getY());

        } else if (beginPoint.getY() == endPoint.getY()) {
            egPath.moveTo((float) beginPoint.getX(), (float) curPoint.getY());
            egPath.lineTo((float) endPoint.getX(), (float) curPoint.getY());
        } else {
            double dSlope = (beginPoint.getX() - endPoint.getX())
                    / (beginPoint.getY() - endPoint.getY());
            if (Math.abs(dSlope) < 1) {
                double x1 = (endPoint.getY() - curPoint.getY()) * dSlope
                        + curPoint.getX();
                double x2 = (beginPoint.getY() - curPoint.getY()) * dSlope
                        + curPoint.getX();
                egPath.moveTo((float) x1, (float) endPoint.getY());
                egPath.lineTo((float) x2, (float) beginPoint.getY());
            } else {
                double y1 = (endPoint.getX() - curPoint.getX()) / dSlope
                        + curPoint.getY();
                double y2 = (beginPoint.getX() - curPoint.getX()) / dSlope
                        + curPoint.getY();
                egPath.moveTo((float) endPoint.getX(), (float) y1);
                egPath.lineTo((float) beginPoint.getX(), (float) y2);
            }
        }
        return egPath;
    }

    /**
     * ���ƴ�ֱ��
     * 
     * @param beginPoint
     *            ��ʼ��
     * @param endPoint
     *            ��ֹ��
     * @param curPoint
     *            �����
     * @return
     */
    public ExtendedGeneralPath calVerticalLine(Point2D beginPoint,
            Point2D endPoint, Point2D curPoint) {
        ExtendedGeneralPath egPath = new ExtendedGeneralPath();
        if (beginPoint.getX() == endPoint.getX()) {
            egPath.moveTo((float) curPoint.getX(), (float) curPoint.getY());
            egPath.lineTo((float) beginPoint.getX(), (float) curPoint.getY());
        } else if (beginPoint.getY() == endPoint.getY()) {
            egPath.moveTo((float) curPoint.getX(), (float) curPoint.getY());
            egPath.lineTo((float) curPoint.getX(), (float) beginPoint.getY());
        } else {
            double dSlope = (beginPoint.getX() - endPoint.getX())
                    / (beginPoint.getY() - endPoint.getY());
            double y = (dSlope * endPoint.getY() + curPoint.getY() / dSlope
                    + curPoint.getX() - endPoint.getX())
                    / (dSlope + 1 / dSlope);
            double x = dSlope * (y - endPoint.getY()) + endPoint.getX();
            egPath.moveTo((float) (curPoint.getX()), (float) curPoint.getY());
            egPath.lineTo((float) (x), (float) y);
        }
        return egPath;
    }

    /**
     * ���ƴ�����
     * 
     * @param beginPoint
     *            ��ʼ��
     * @param endPoint
     *            ��ֹ��
     * @param curPoint
     *            �����
     * @return
     */
    public ExtendedGeneralPath calVerticalMiddleLine(Point2D beginPoint,
            Point2D endPoint, Point2D curPoint) {
        ExtendedGeneralPath egPath = new ExtendedGeneralPath();
        if (beginPoint.getX() == endPoint.getX()) {
            egPath.moveTo((float) curPoint.getX(),
                    (float) (beginPoint.getY() + endPoint.getY()) / 2);
            egPath.lineTo((float) beginPoint.getX(),
                    (float) (beginPoint.getY() + endPoint.getY()) / 2);
        } else if (beginPoint.getY() == endPoint.getY()) {
            egPath.moveTo((float) (beginPoint.getX() + endPoint.getX()) / 2,
                    (float) curPoint.getY());
            egPath.lineTo((float) (beginPoint.getX() + endPoint.getX()) / 2,
                    (float) beginPoint.getY());
        } else {
            double dSlope = (beginPoint.getX() - endPoint.getX())
                    / (beginPoint.getY() - endPoint.getY());
            // double y =( dSlope*endPoint.getY() + (beginPoint.getY() +
            // endPoint.getY())/2/dSlope + (beginPoint.getX() +
            // endPoint.getX())/2 - endPoint.getX())/(dSlope + 1 /dSlope);
            int offset = 50;
            if ((curPoint.getX() - beginPoint.getX()) / dSlope
                    + beginPoint.getY() >= curPoint.getY())
                offset = -50;
            double y = (beginPoint.getY() + endPoint.getY()) / 2 + offset;
            double x = (beginPoint.getX() + endPoint.getX()) / 2 - offset
                    / dSlope;
            egPath.moveTo((float) ((beginPoint.getX() + endPoint.getX()) / 2),
                    (float) (beginPoint.getY() + endPoint.getY()) / 2);
            egPath.lineTo((float) (x), (float) y);
        }
        return egPath;
    }
}

/**
 * �Զ���Ľ�ͼ���
 * 
 * @author Qil.Wong
 * 
 */
class ScreenCastCursor extends Cursor {

    private static final long serialVersionUID = -7185789076519025650L;
    private static Image image;

    static {
        image = ResourcesManager.getIcon("nci_screencast_icon", false).getImage();
    }

    public ScreenCastCursor(int n) {
        super(n);
    }

    static public Cursor getScreenCastCursor() throws AWTException,
            HeadlessException {

        return Toolkit.getDefaultToolkit().createCustomCursor(image,
                new Point(12, 12), "screenCastCursor");
    }

}
