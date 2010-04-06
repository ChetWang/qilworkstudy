/**
 * 类名：com.nci.svg.module
 * 创建人:yx.nci
 * 创建日期：2008-6-18
 * 类作用:TODO
 * 修改日志：
 */
package com.nci.svg.module;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JToggleButton;

import org.apache.batik.ext.awt.geom.ExtendedGeneralPath;
import org.w3c.dom.Element;

import fr.itris.glips.library.geom.path.Path;
import fr.itris.glips.library.geom.path.segment.Segment;
import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.ModuleAdapter;
import fr.itris.glips.svgeditor.actions.popup.PopupItem;
import fr.itris.glips.svgeditor.actions.popup.PopupSubMenu;
import fr.itris.glips.svgeditor.display.handle.HandlesListener;
import fr.itris.glips.svgeditor.display.handle.HandlesManager;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.display.selection.Selection;
import fr.itris.glips.svgeditor.display.selection.SelectionChangedListener;
import fr.itris.glips.svgeditor.display.undoredo.UndoRedoAction;
import fr.itris.glips.svgeditor.display.undoredo.UndoRedoActionList;
import fr.itris.glips.svgeditor.resources.ResourcesManager;
import fr.itris.glips.svgeditor.shape.ShapeToolkit;

import com.nci.svg.util.*;

/**
 * @author yx.nci
 *
 */
public class DrawLineModifyModule extends ModuleAdapter {
    /**
     * 模型标识
     */
    protected static final String id = "DrawLineModifyModule";

    /**
     * 菜单图标
     */
    protected Icon menuIcon;

    /**
     * 菜单标签
     */
    protected String menuLabel = "";

    /**
     * 菜单选项
     */
    protected JMenu optionsMenu;

    /**
     * 各菜单选项标识
     */
    protected String[] ids = { "ParallelModeModify", "VerticalModeModify" };

    /**
     * the icons for the modes
     */
    protected Icon[] optionsIcons, optionsDisabledIcons;

    /**
     * the label for the menu and tool item
     */
    protected String[] optionsItemLabels, optionsOnItemToolTips,
            optionsOffItemToolTips;

    /**
     * the menu items that are displayed in the menu bar for the options
     */
    //  protected JCheckBoxMenuItem[] optionsMenuItems;
    protected JMenuItem[] optionsMenuItems;

    /**
     * the tool items that are displayed in the toolbar for the options
     */
    protected JToggleButton[] optionsToolItems;
    
    protected String undoRedoLabel="";

    protected int curAction = -1;

    /**
     * @return the curAction
     */
    public int getCurAction() {
        return curAction;
    }

    /**
     * @param curAction the curAction to set
     */
    public void setCurAction(int curAction) {
        this.curAction = curAction;
    }

    /**
     * the constructor of the class
     * 
     * @param editor
     *            the editor
     */
    public DrawLineModifyModule(Editor editor) {
    	super(editor);
        createMenuAndToolItems();
    }

    /**
     * creates the menu and tool items
     */
    protected void createMenuAndToolItems() {

        // creating the menu//
        // getting the menu label
        undoRedoLabel = ResourcesManager.bundle.getString(id + "ItemLabel");
        menuLabel = ResourcesManager.bundle.getString(id + "ItemLabel");

        // getting the menu icons
        menuIcon = ResourcesManager.getIcon(id, false);

        // creating the menu
        optionsMenu = new JMenu(menuLabel);
        optionsMenu.setIcon(menuIcon);

        // creating the options item//

        // getting the resources for the items
        optionsItemLabels = new String[ids.length];
        optionsOnItemToolTips = new String[ids.length];
        optionsOffItemToolTips = new String[ids.length];
        optionsIcons = new Icon[ids.length];
        optionsDisabledIcons = new Icon[ids.length];
        optionsMenuItems = new JMenuItem[ids.length];
        optionsToolItems = new JToggleButton[ids.length];
        ActionListener listener = null;
        ButtonGroup bgMenu = new ButtonGroup();
        ButtonGroup bgTool = new ButtonGroup();
        for (int i = 0; i < ids.length; i++) {
            // getting the labels
            optionsItemLabels[i] = ResourcesManager.bundle.getString(ids[i]
                    + "ItemLabel");

            optionsOnItemToolTips[i] = ResourcesManager.bundle.getString(ids[i]
                    + "OnItemToolTip");

            optionsOffItemToolTips[i] = ResourcesManager.bundle
                    .getString(ids[i] + "OffItemToolTip");

            // getting the icons
            optionsIcons[i] = ResourcesManager.getIcon(ids[i], false);
            optionsDisabledIcons[i] = ResourcesManager.getIcon(ids[i], true);

            // creating the menu items
            optionsMenuItems[i] = new JMenuItem(optionsItemLabels[i]);
            optionsMenuItems[i].setIcon(optionsIcons[i]);
            optionsMenuItems[i].setDisabledIcon(optionsDisabledIcons[i]);
            optionsMenuItems[i].setEnabled(false);

            // creating the tool items
            optionsToolItems[i] = new JToggleButton();
            optionsToolItems[i].setIcon(optionsIcons[i]);
            optionsToolItems[i].setDisabledIcon(optionsDisabledIcons[i]);
            optionsToolItems[i].setEnabled(false);

            // creating the listener
            listener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (e.getSource().equals(optionsToolItems[0])
                            || e.getSource().equals(optionsMenuItems[0])) {
                        setCurAction(Selection.PARALLEL_LINE_MODE);
                    } else if (e.getSource().equals(optionsToolItems[1])
                            || e.getSource().equals(optionsMenuItems[1])) {
                        setCurAction(Selection.VERTICAL_LINE_MODE);
                    } 
                    doAction();
                    handleToolsState();
                }
            };

            optionsMenuItems[i].addActionListener(listener);
            optionsToolItems[i].addActionListener(listener);

            // adding to the menu
            optionsMenu.add(optionsMenuItems[i]);
            bgMenu.add(optionsMenuItems[i]);
            bgTool.add(optionsToolItems[i]);
        }


        // adding the listener to the switches between the svg handles
        final HandlesManager svgHandleManager = editor
                .getHandlesManager();

        svgHandleManager.addHandlesListener(new HandlesListener() {

            @Override
            public void handleChanged(SVGHandle currentHandle,
                    Set<SVGHandle> handles) {

                if (currentHandle != null) {
                    boolean bSelect = true;
                    if (currentHandle.getSelection().getDrawLineModifyListener() != null)
                        currentHandle.getSelection()
                                .removeSelectionChangedListener(
                                        currentHandle.getSelection()
                                                .getDrawLineModifyListener());
                    SelectionChangedListener listener = new SelectionChangedListener() {

                        @Override
                        public void selectionChanged(
                                Set<Element> selectedElements) {

                            boolean bSelect = true;
                            List<Element> eList = editor.getClipboardManager().getClipboardContent();
                            if(eList.size() != 1)
                                bSelect = false;
                            else
                            {
                                Element listElement = eList.get(0);
                                if(!listElement.getNodeName().equals("path"))
                                    bSelect = false;
                                else
                                {
                                
                            if (selectedElements == null
                                    || selectedElements.size() != 1)
                                bSelect = false;
                            else {
                                Element element = (Element) selectedElements
                                        .iterator().next();
                                if (!element.getNodeName().equals("path") || 
                                        listElement.equals(element)
                                        ||!listElement.getOwnerDocument().equals(element.getOwnerDocument())) {
                                    bSelect = false;
                                }
                                else
                                {
                                     Path path = new Path(listElement.getAttribute("d")); 
                                     if(path.getSegmentsNumber() > 2)
                                         bSelect = false;
                                     path = new Path(element.getAttribute("d")); 
                                     if(path.getSegmentsNumber() > 2)
                                         bSelect = false;
                                }
                            }
                                }
                            }
                            for (int i = 0; i < optionsMenuItems.length; i++) {

                                optionsMenuItems[i].setEnabled(bSelect);
                                optionsToolItems[i].setEnabled(bSelect);
//                              optionsMenuItems[i].setSelected(false);
                                optionsToolItems[i].setSelected(false);
                            }
                        }
                    };
                    currentHandle.getSelection().addSelectionChangedListener(
                            listener);
                    currentHandle.getSelection().setDrawLineModifyListener(listener);

                    Set<Element> sets = currentHandle.getSelection()
                            .getSelectedElements();
                    List<Element> eList = editor.getClipboardManager().getClipboardContent();
                    if (sets.size() != 1 || eList.size() != 1) {
                        bSelect = false;
                    } else {
                        Element element = (Element) sets.iterator().next();
                        Element listElement = eList.get(0);
                        if(!listElement.getNodeName().equals("path") || 
                                listElement.equals(element)
                                || !listElement.getOwnerDocument().equals(element.getOwnerDocument()))
                            bSelect = false;
                        else
                        {
                             Path path = new Path(listElement.getAttribute("d")); 
                             if(path.getSegmentsNumber() > 2)
                                 bSelect = false;
                             if (!element.getNodeName().equals("path")) {
                                 bSelect = false;
                             }
                             else
                             {
                                 path = new Path(element.getAttribute("d")); 
                                 if(path.getSegmentsNumber() > 2)
                                     bSelect = false;
                             }
                             
                        }
                    }
                    for (int i = 0; i < optionsMenuItems.length; i++) {

                        optionsMenuItems[i].setEnabled(bSelect);
                        optionsToolItems[i].setEnabled(bSelect);
                    }

                } else {
                    for (int i = 0; i < optionsMenuItems.length; i++) {

                        optionsMenuItems[i].setEnabled(false);
                        optionsToolItems[i].setEnabled(false);
                    }
                }
            }
        });

        handleToolsState();
    }

    /**
     * 控制工具栏按钮状态
     */
    protected void handleToolsState() {

        for (int i = 0; i < ids.length; i++) {
            optionsToolItems[i]
                    .setToolTipText(optionsToolItems[i].isSelected() ? optionsOnItemToolTips[i]
                            : optionsOffItemToolTips[i]);
        }
    }

    @Override
    public HashMap<String, JMenuItem> getMenuItems() {

        HashMap<String, JMenuItem> map = new HashMap<String, JMenuItem>();
        map.put(id, optionsMenu);

        return map;
    }

    @Override
    public HashMap<String, AbstractButton> getToolItems() {

        HashMap<String, AbstractButton> map = new HashMap<String, AbstractButton>();

        for (int i = 0; i < ids.length; i++) {

            map.put(ids[i], optionsToolItems[i]);
        }

        return map;
    }

    /**
     * executes the action corresponding to the provided index
     * 
     * @param index
     *            the index of an option manager
     * @param isSelected
     *            whether the related item is selected
     */
    protected void doAction() {
        Element selectElement = editor.getHandlesManager()
                .getCurrentHandle().getSelection().getSelectedElements()
                .iterator().next();
        Element copyElement = editor.getClipboardManager()
                .getClipboardContent().get(0);
        String sPath = copyElement.getAttribute("d");
        Path path = new Path(sPath);
        int nCount = path.getSegmentsNumber();
        if (nCount != 2)
            return;

        Segment se = path.getSegment();
        Point2D bPointcopy = se.getEndPoint();
        se = se.getNextSegment();
        Point2D ePointcopy = se.getEndPoint();
        sPath = selectElement.getAttribute("d");
        path = new Path(sPath);
        nCount = path.getSegmentsNumber();
        if (nCount != 2)
            return;

        final String strBeforeMoved = path.toString();
        se = path.getSegment();
        Point2D bPointselect = se.getEndPoint();
        se = se.getNextSegment();
        Point2D ePointselect = se.getEndPoint();
        ExtendedGeneralPath egPath = null;
        Point2D curPoint = new Point2D.Double((bPointselect.getX()+ePointselect.getX())/2, 
                (bPointselect.getY()+ePointselect.getY())/2);
        if (getCurAction() == Selection.PARALLEL_LINE_MODE) {
            egPath =  editor.getHandlesManager()
            .getCurrentHandle().getSelection().calParallelLine(bPointcopy, ePointcopy, curPoint);
        } else if (getCurAction() == Selection.VERTICAL_LINE_MODE) {
            egPath =  editor.getHandlesManager()
            .getCurrentHandle().getSelection().calVerticalLine(bPointcopy, ePointcopy, curPoint);
        }
        
        if(egPath != null)
        {
            path = new Path(egPath);
            selectElement.setAttribute("d",path.toString());
        }
        final String strMoved = path.toString();
        
        final Element shapeElement = selectElement;

        // the execute runnable
        Runnable executeRunnable = new Runnable() {

            public void run() {

                shapeElement.setAttribute("d", strMoved);
                editor.getHandlesManager()
                .getCurrentHandle().getSelection().refreshSelection(true);
                editor.getSvgSession().refreshCurrentHandleImediately();
            }
        };

        // the undo runnable
        Runnable undoRunnable = new Runnable() {

            public void run() {

                shapeElement.setAttribute("d", strBeforeMoved);
                editor.getHandlesManager()
                .getCurrentHandle().getSelection().refreshSelection(true);
                editor.getSvgSession().refreshCurrentHandleImediately();
            }
        };

        // executing the action and creating the undo/redo action
        HashSet<Element> elements = new HashSet<Element>();
        elements.add(shapeElement);
        UndoRedoAction undoRedoAction = ShapeToolkit.getUndoRedoAction(
                undoRedoLabel, executeRunnable, undoRunnable, elements);

        UndoRedoActionList actionlist = new UndoRedoActionList(
                undoRedoLabel, false);
        actionlist.add(undoRedoAction);
        editor.getHandlesManager()
             .getCurrentHandle().getUndoRedo().addActionList(actionlist, false);
    }

	@Override
	public Collection<PopupItem> getPopupItems() {
		// TODO Auto-generated method stub
        LinkedList<PopupItem> popupItems = new LinkedList<PopupItem>();
       
		String iconName = null;
		PopupSubMenu subMenu = new PopupSubMenu(editor, id,
				menuLabel, id);

        for (int i = 0; i < ids.length; i++) {
			final int index = i;
			PopupItem item = new PopupItem(editor, ids[i],
					ResourcesManager.bundle.getString(ids[i]), iconName) {
				@Override
				public JMenuItem getPopupItem(LinkedList<Element> nodes) {
					menuItem.setEnabled(false);
					List<Element> eList = editor
							.getClipboardManager().getClipboardContent();
					if(eList.size() != 1)
					    return null;
					Element listElement = eList.get(0);
					if (nodes.size() == 1) {// 0表示没选中任何图形，1表示图形选中一个图元，2及以上表示选中多个图元
						final Element element = Utilities
								.parseSelectedElement(nodes.get(0));
						boolean isSelectPath = listElement.getOwnerDocument().equals(element.getOwnerDocument());
						if (element.getNodeName().equals("path")) {
							menuItem.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									if (ids[index].equals("ParallelModeModify")) {	// 平行线模式
				                        setCurAction(Selection.PARALLEL_LINE_MODE);
				                    } else if (ids[index].equals("VerticalModeModify")) {	// 垂直线模式
				                        setCurAction(Selection.VERTICAL_LINE_MODE);
				                    } 
									doAction();
									handleToolsState();
								}
							});
							menuItem.setEnabled(true);
						}
					}
					return super.getPopupItem(nodes);
				}
			};
			subMenu.addPopupItem(item);
        }

		popupItems.add(subMenu);
        return popupItems;
	}
}
