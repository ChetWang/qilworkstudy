package fr.itris.glips.svgeditor.shape;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.jdesktop.swingworker.SwingWorker;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nci.svg.logntermtask.LongtermTask;
import com.nci.svg.logntermtask.LongtermTaskManager;
import com.nci.svg.util.Constants;

import fr.itris.glips.library.Toolkit;
import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.EditorToolkit;
import fr.itris.glips.svgeditor.actions.popup.PopupItem;
import fr.itris.glips.svgeditor.display.canvas.CanvasPainter;
import fr.itris.glips.svgeditor.display.handle.HandlesListener;
import fr.itris.glips.svgeditor.display.handle.HandlesManager;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.display.selection.SelectionChangedListener;
import fr.itris.glips.svgeditor.display.selection.SelectionItem;
import fr.itris.glips.svgeditor.resources.ResourcesManager;
import fr.itris.glips.svgeditor.selection.SelectionInfoManager;

/**
 * the class of the module handling the group nodes
 * 
 * @author Jordi SUC
 */
public class GroupShape extends MultiAbstractShape {

    /**
     * the id for the unGroup action
     */
    private String unGroupId = "UnGroupShape", groupBayId = "GroupBay",
            unGroupBayId = "UnGroupBay";

    /**
     * the labels
     */
    private String unGroupLabel = "", groupBayLabel = "", unGroupBayLabel = "";

    /**
     * the tool tips
     */
    // private String unGroupTooltip="";
    /**
     * the undo/redo labels
     */
    private String undoRedoUnGroupLabel = "", undoRedoGroupBayLabel = "",
            undoRedoUnGroupBayLabel = "";

    /**
     * the UnGroup icons
     */
    private Icon unGroupIcon, unGroupDisabledIcon;

    /**
     * the menu item
     */
    private JMenuItem unGroupMenuItem, groupBayMenuItem, unGroupBayMenuItem;

    /* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.shape.MultiAbstractShape#showAction(fr.itris.glips.svgeditor.display.handle.SVGHandle, int, java.util.Set, fr.itris.glips.svgeditor.display.selection.SelectionItem, java.awt.geom.Point2D, java.awt.geom.Point2D)
     */
    @Override
    public CanvasPainter showAction(SVGHandle handle, int level,
            Set<Element> elementSet, SelectionItem item, Point2D firstPoint,
            Point2D currentPoint) {
        // TODO Auto-generated method stub
        Element element = elementSet.iterator().next();
        if(element.getAttribute("type").equals("solidified"))
        {
            return null;
        }
        return super.showAction(handle, level, elementSet, item, firstPoint,
                currentPoint);
    }

    /**
     * the constructor of the class
     * 
     * @param editor
     *            the editor
     */
    public GroupShape(Editor editor) {

        super(editor);

        shapeModuleId = "GroupShape";
        handledElementTagName = "g";
        retrieveLabels();
        createMenuAndToolItems();
    }

    @Override
    protected void retrieveLabels() {

        super.retrieveLabels();

        unGroupLabel = ResourcesManager.bundle.getString(unGroupId
                + "ItemLabel");
        undoRedoUnGroupLabel = ResourcesManager.bundle.getString(unGroupId
                + "UndoRedoLabel");
        groupBayLabel = ResourcesManager.bundle.getString(groupBayId
                + "ItemLabel");
        undoRedoGroupBayLabel = ResourcesManager.bundle.getString(groupBayId
                + "UndoRedoLabel");
        unGroupBayLabel = ResourcesManager.bundle.getString(unGroupBayId
                + "ItemLabel");
        undoRedoUnGroupBayLabel = ResourcesManager.bundle
                .getString(unGroupBayId + "UndoRedoLabel");
    }

    @Override
    protected void createMenuAndToolItems() {

        // creating the listener to the menu and tool items
        ActionListener listener = new ActionListener() {

            public void actionPerformed(ActionEvent evt) {

                if (evt.getModifiers() == InputEvent.BUTTON1_DOWN_MASK
                        || evt.getModifiers() == InputEvent.BUTTON1_MASK
                        || editor.getHandlesManager()
                                .keyStrokeActsOnSVGFrame()) {

                    if (evt.getSource().equals(shapeCreatorMenuItem)) {

                        group(false);

                    } else if (evt.getSource().equals(unGroupMenuItem)) {

                        ungroup(false);
                    } else if (evt.getSource().equals(groupBayMenuItem)) {

                        group(true);

                    } else if (evt.getSource().equals(unGroupBayMenuItem)) {

                        ungroup(true);
                    }
                }
            }
        };

        // getting the icons for the items
        shapeCreatorIcon = ResourcesManager.getIcon(shapeModuleId, false);
        shapeCreatorDisabledIcon = ResourcesManager
                .getIcon(shapeModuleId, true);
        unGroupIcon = ResourcesManager.getIcon(unGroupId, false);
        unGroupDisabledIcon = ResourcesManager.getIcon(unGroupId, true);

        // creating the menu items
        shapeCreatorMenuItem = new JMenuItem(itemLabel, shapeCreatorIcon);
        shapeCreatorMenuItem.setDisabledIcon(shapeCreatorDisabledIcon);
        shapeCreatorMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_G, java.awt.Toolkit.getDefaultToolkit()
                        .getMenuShortcutKeyMask(), false));
        shapeCreatorMenuItem.addActionListener(listener);
        shapeCreatorMenuItem.setEnabled(false);

        unGroupMenuItem = new JMenuItem(unGroupLabel, unGroupIcon);
        unGroupMenuItem.setDisabledIcon(unGroupDisabledIcon);
        unGroupMenuItem.addActionListener(listener);
        unGroupMenuItem.setEnabled(false);

        groupBayMenuItem = new JMenuItem(groupBayLabel, shapeCreatorIcon);
        groupBayMenuItem.setDisabledIcon(shapeCreatorDisabledIcon);
        groupBayMenuItem.addActionListener(listener);
        groupBayMenuItem.setEnabled(false);

        unGroupBayMenuItem = new JMenuItem(unGroupBayLabel, unGroupIcon);
        unGroupBayMenuItem.setDisabledIcon(unGroupDisabledIcon);
        unGroupBayMenuItem.addActionListener(listener);
        unGroupBayMenuItem.setEnabled(false);

        // adding the listener to the switches between the svg handles
        final HandlesManager svgHandleManager = editor
                .getHandlesManager();

        svgHandleManager.addHandlesListener(new GroupHandlesListener());
    }

    @Override
    public HashMap<String, JMenuItem> getMenuItems() {

        HashMap<String, JMenuItem> menuItems = new HashMap<String, JMenuItem>();

        if (shapeCreatorMenuItem != null) {

            menuItems.put(shapeModuleId, shapeCreatorMenuItem);
            menuItems.put(unGroupId, unGroupMenuItem);
            menuItems.put(groupBayId, groupBayMenuItem);
            menuItems.put(unGroupBayId, unGroupBayMenuItem);
        }

        return menuItems;
    }

    @Override
    public Collection<PopupItem> getPopupItems() {

        Set<PopupItem> popupItems = new HashSet<PopupItem>();

        // creating the group popup item
        PopupItem groupItem = new PopupItem(editor, shapeModuleId,
                itemLabel, shapeModuleId) {

            @Override
            public JMenuItem getPopupItem(LinkedList<Element> nodes) {

                menuItem.setEnabled(false);

                // getting the current handle
                SVGHandle currentHandle = editor
                        .getHandlesManager().getCurrentHandle();

                if (currentHandle != null) {

                    // getting the set of the selected elements
                    Set<Element> selectedElements = currentHandle
                            .getSelection().getSelectedElements();

                    if (selectedElements.size() > 0) {

                        menuItem.setEnabled(true);
                        menuItem.addActionListener(new ActionListener() {

                            public void actionPerformed(ActionEvent e) {
//                                SwingWorker worker = new SwingWorker() {
//
//                                    @Override
//                                    protected Object doInBackground()
//                                            throws Exception {
//                                        Editor.getEditor().getHandlesManager()
//                                                .getCurrentHandle()
//                                                .getSelection().lock();
//                                        group(false);
//                                        Editor.getEditor().getHandlesManager()
//                                                .getCurrentHandle()
//                                                .getSelection().unLock();
//
//                                        return null;
//                                    }
//
//                                };
//                                LongtermTask lt = new LongtermTask("正在组合图形...",
//                                        worker);
//                                LongtermTaskManager.getInstance()
//                                        .addAndStartLongtermTask(lt);
                                
                                group(false);

                            }
                        });
                    }
                }

                return super.getPopupItem(nodes);
            }
        };

        popupItems.add(groupItem);

        // creating the group popup item
        PopupItem unGroupItem = new PopupItem(editor, unGroupId,
                unGroupLabel, unGroupId) {

            @Override
            public JMenuItem getPopupItem(LinkedList<Element> nodes) {

                menuItem.setEnabled(false);

                // getting the current handle
                SVGHandle currentHandle = editor
                        .getHandlesManager().getCurrentHandle();

                if (currentHandle != null) {

                    // getting the set of the selected elements
                    Set<Element> selectedElements = currentHandle
                            .getSelection().getSelectedElements();

                    if (selectedElements.size() > 0) {

                        boolean unGroupEnabled = true;

                        for (Element element : selectedElements) {

                            if (!element.getNodeName().equals(
                                    handledElementTagName)) {

                                unGroupEnabled = false;
                                break;
                            }
                            else 
                            {
                                if(element.getAttribute("type").equals("solidified"))
                                {
                                    unGroupEnabled = false;
                                    break;
                                }
                            }
                        }

                        if (unGroupEnabled) {

                            menuItem.setEnabled(true);

                            menuItem.addActionListener(new ActionListener() {

                                public void actionPerformed(ActionEvent e) {
//                                    SwingWorker worker = new SwingWorker() {
//
//                                        @Override
//                                        protected Object doInBackground()
//                                                throws Exception {
//                                            Editor.getEditor()
//                                                    .getHandlesManager()
//                                                    .getCurrentHandle()
//                                                    .getSelection().lock();
//                                            ungroup(false);
//                                            Editor.getEditor()
//                                                    .getHandlesManager()
//                                                    .getCurrentHandle()
//                                                    .getSelection().unLock();
//                                            return null;
//                                        }
//
//                                    };
//                                    LongtermTask lt = new LongtermTask(
//                                            "正在分解图形...", worker);
//                                    LongtermTaskManager.getInstance()
//                                            .addAndStartLongtermTask(lt);
                                    ungroup(false);

                                }
                            });
                        }
                    }
                }

                return super.getPopupItem(nodes);
            }
        };

        popupItems.add(unGroupItem);

        return popupItems;
    }

    @Override
    public Set<Element> getElements(Set<Element> elements) {

        // creating the set of the children of the element that
        // is found in the provided set
        Element groupElement = elements.iterator().next();
        Set<Element> newElements = new HashSet<Element>();
        NodeList childNodes = groupElement.getChildNodes();
        Node node = null;

        for (int i = 0; i < childNodes.getLength(); i++) {

            node = childNodes.item(i);

            if (node != null && node instanceof Element) {

                if (!node.getNodeName().equals(Constants.NCI_SVG_METADATA))
                    newElements.add((Element) node);
            }
        }

        return newElements;
    }

    @Override
    public Rectangle2D getBounds(SVGHandle handle, Set<Element> elements) {

        return handle.getSvgElementsManager().getNodeGeometryBounds(
                elements.iterator().next());
    }

    /**
     * sets whether the items should be enabled or not
     * 
     * @param enable
     *            whether the items should be enabled or not
     */
    protected void setEnabled(boolean enable) {

        setGroupEnabled(enable);
        setUnGroupEnabled(enable);
        setUnGroupBayEnabled(enable);
    }

    /**
     * sets whether the group items should be enabled or not
     * 
     * @param enable
     *            whether the group items should be enabled or not
     */
    protected void setGroupEnabled(boolean enable) {

        shapeCreatorMenuItem.setEnabled(enable);
        groupBayMenuItem.setEnabled(enable);
    }

    /**
     * sets whether the ungroup items should be enabled or not
     * 
     * @param enable
     *            whether the ungroup items should be enabled or not
     */
    protected void setUnGroupEnabled(boolean enable) {

        unGroupMenuItem.setEnabled(enable);
    }

    protected void setUnGroupBayEnabled(boolean enable) {

        unGroupBayMenuItem.setEnabled(enable);
    }

    /**
     * groups the currently selected elements in the currently selected svg
     * handle
     */
    protected void group(boolean bBayFlag) {

        // getting the current handle
        final SVGHandle currentHandle = editor.getHandlesManager()
                .getCurrentHandle();

        // getting the selected elements
        final Set<Element> selectedElements = new HashSet<Element>(
                currentHandle.getSelection().getSelectedElements());

        // getting the parent node
        final Element parentNode = currentHandle.getSelection()
                .getParentElement();

        // creating the list of the child nodes of the parent element,
        // in the order in which they are found in the document
        final LinkedList<Element> orderedElements = Toolkit
                .getChildrenElements(parentNode);

        // creating the new group node
        Document doc = parentNode.getOwnerDocument();
        final Element groupElement = doc.createElementNS(doc
                .getDocumentElement().getNamespaceURI(), handledElementTagName);

        // creating the execute runnable
        Runnable executeRunnable = new Runnable() {

            public void run() {

                // appending the child nodes to the group element
                for (Element element : orderedElements) {

                    if (selectedElements.contains(element)) {

                        parentNode.removeChild(element);
                        groupElement.appendChild(element);
                    }
                }

                // appending the group element to the parent node
                parentNode.appendChild(groupElement);

                currentHandle.getSelection().clearSelection();
                currentHandle.getSelection().handleSelection(groupElement,
                        false, true);
            }
        };

        // creating the undo runnable
        Runnable undoRunnable = new Runnable() {

            public void run() {

                // removing the child nodes from the group element
                Element previousElement = null;

                for (Element element : orderedElements) {

                    if (selectedElements.contains(element)) {

                        groupElement.removeChild(element);

                        if (previousElement != null) {

                            parentNode.insertBefore(element, previousElement
                                    .getNextSibling());

                        } else {

                            parentNode.insertBefore(element, parentNode
                                    .getFirstChild());
                        }
                    }

                    previousElement = element;
                }

                // removing the group element from the parent node
                parentNode.removeChild(groupElement);
            }
        };

        // creating the set of the elements that are modified
        Set<Element> elements = new HashSet<Element>(selectedElements);
        elements.add(groupElement);
        if (bBayFlag) {
            groupElement.setAttribute("nci_type", "bay");
        }

        // creating the undo/redo action
        ShapeToolkit.addUndoRedoAction(currentHandle, undoRedoLabel,
                executeRunnable, undoRunnable, elements);
    }

    /**
     * ungroups the currently selected elements in the currently selected svg
     * handle
     */
    protected void ungroup(boolean bBayFlag) {

        // getting the current handle
        final SVGHandle currentHandle = editor.getHandlesManager()
                .getCurrentHandle();

        // getting the selected elements
        final Set<Element> selectedElements = currentHandle.getSelection()
                .getSelectedElements();
        
        Element gelement = selectedElements.iterator().next();
        if(gelement.getAttribute("type").equals("solidified"))
        {
            return;
        }

        // getting the parent node
        final Element parentNode = currentHandle.getSelection()
                .getParentElement();

        // creating the set of the elements that will be modified
        Set<Element> elements = new HashSet<Element>(selectedElements);

        // creating the list of the child nodes of the parent element,
        // in the order in which they are found in the document
        final LinkedList<Element> orderedElements = Toolkit
                .getChildrenElements(parentNode);

        final Map<Element, Element> groupNodesSuccessor = new HashMap<Element, Element>();

        for (Element element : orderedElements) {

            groupNodesSuccessor.put(element, EditorToolkit
                    .getNextElementSibling(element));
        }

        // creating the map associating a group node to its child nodes
        final Map<Element, LinkedList<Element>> groupNodes = new HashMap<Element, LinkedList<Element>>();
        LinkedList<Element> childNodesList = null;
        NodeList groupChildNodes = null;
        Node node = null;

        final Map<Element, Element> mapMetadata = new HashMap<Element, Element>();

        for (Element groupElement : selectedElements) {

            // getting the child nodes
            elements.add(groupElement);
            groupChildNodes = groupElement.getChildNodes();
            childNodesList = new LinkedList<Element>();
            groupNodes.put(groupElement, childNodesList);

            // filling the set of the child nodes
            for (int i = 0; i < groupChildNodes.getLength(); i++) {

                node = groupChildNodes.item(i);

                if (node != null && node instanceof Element) {

                    if (!node.getNodeName().equals(Constants.NCI_SVG_METADATA)) {
                        childNodesList.add((Element) node);
                        elements.add((Element) node);
                    }
                }
            }
            NodeList mdList = groupElement.getElementsByTagName(Constants.NCI_SVG_METADATA);

            for (int i = 0; i < mdList.getLength(); i++) {
                if (mdList.item(i).getParentNode() == groupElement) {
                    mapMetadata.put(groupElement, (Element) mdList.item(i));
                    break;
                }
            }

        }

        // creating the execute runnable
        Runnable executeRunnable = new Runnable() {

            public void run() {

                // for each group node
                LinkedList<Element> groupChildNodesList = null;

                for (Element groupElement : groupNodes.keySet()) {

                    // getting the list of the child nodes of the group element
                    groupChildNodesList = groupNodes.get(groupElement);
                    Element metadata = mapMetadata.get(groupElement);

                    for (Element childElement : groupChildNodesList) {

                        // appending the child element of the group node to the
                        // parent node
                        if (childElement != metadata)
                            parentNode.insertBefore(childElement, groupElement);
                    }

                    // removing the group element
                    if (metadata != null)
                        groupElement.removeChild(metadata);
                    parentNode.removeChild(groupElement);
                }

                currentHandle.getSelection().clearSelection();
            }
        };

        // creating the undo runnable
        Runnable undoRunnable = new Runnable() {

            public void run() {

                // for each group node
                LinkedList<Element> groupChildNodesList = null;

                for (Element groupElement : groupNodes.keySet()) {

                    // getting the list of the child nodes of the group element
                    groupChildNodesList = groupNodes.get(groupElement);
                    Element metadata = mapMetadata.get(groupElement);

                    for (Element childElement : groupChildNodesList) {

                        // appending the child element of the group node to the
                        // parent node
                        parentNode.removeChild(childElement);
                        groupElement.appendChild(childElement);
                    }
                    if (metadata != null)
                        groupElement.appendChild(metadata);

                    // appending the group element to the parent node
                    Element nextSibling = groupNodesSuccessor.get(groupElement);
                    parentNode.insertBefore(groupElement, nextSibling);
                }
            }
        };

        // creating the undo/redo action
        ShapeToolkit.addUndoRedoAction(currentHandle, undoRedoUnGroupLabel,
                executeRunnable, undoRunnable, elements);
    }

    /**
     * the class of the listener to svg handle modification
     * 
     * @author Jordi SUC
     */
    protected class GroupHandlesListener extends HandlesListener {

        /**
         * the selection listener for the current handle
         */
        private SelectionChangedListener selectionChangedListener;

        /**
         * the last handle
         */
        private SVGHandle lastHandle;

        @Override
        public void handleChanged(SVGHandle currentHandle,
                Set<SVGHandle> handles) {

            if (currentHandle != null) {

                // removing the last selection changed listener
                if (selectionChangedListener != null && lastHandle != null
                        && lastHandle.getSelection() != null) {

                    lastHandle.getSelection().removeSelectionChangedListener(
                            selectionChangedListener);
                }

                selectionChanged(currentHandle);

                // adding a new selection changed listener
                selectionChangedListener = new SelectionChangedListener() {

                    @Override
                    public void selectionChanged(Set<Element> selectedElements) {

                        GroupHandlesListener.this.selectionChanged(editor.getHandlesManager()
                                .getCurrentHandle());
                    }
                };

                currentHandle.getSelection().addSelectionChangedListener(
                        selectionChangedListener);

            } else {

                selectionChangedListener = null;
                setEnabled(false);
            }

            this.lastHandle = currentHandle;
        }

        /**
         * called when the selection of the provided handle has changed
         * 
         * @param currentHandle
         *            the current svg handle
         */
        protected void selectionChanged(SVGHandle currentHandle) {

            if (currentHandle != null) {

                // getting the set of the selected elements
                Set<Element> selectedElements = currentHandle.getSelection()
                        .getSelectedElements();

                setEnabled(false);

                if (selectedElements.size() > 0) {

                    setGroupEnabled(true);
                    boolean unGroupEnabled = true;
                    boolean unGroupBayEnabled = false;

                    for (Element element : selectedElements) {

                        if (!element.getNodeName()
                                .equals(handledElementTagName)) {

                            unGroupEnabled = false;
                            break;
                        }
                        else
                        {
                            if(element.getAttribute("type").equals("solidified"))
                            {
                                unGroupEnabled = false;
                                break;
                            }
                        }
                    }

                    if (unGroupEnabled) {
                        unGroupBayEnabled = true;
                        for (Element element : selectedElements) {

                            if (element.getAttribute("nci_type") == null
                                    || element.getAttribute("nci_type")
                                            .length() == 0) {

                                unGroupBayEnabled = false;
                                break;
                            }
                            else
                            {
                                if(element.getAttribute("type").equals("solidified"))
                                {
                                    unGroupEnabled = false;
                                    break;
                                }
                            }
                        }
                    }
                    setUnGroupEnabled(unGroupEnabled);
                    setUnGroupBayEnabled(unGroupBayEnabled);
                }
            }
        }
    }
}
