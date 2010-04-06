/**
 * 类名：com.nci.svg.module.ResetSymbolModule
 * 创建人:yx.nci
 * 创建日期：2008-7-7
 * 类作用:图元置位
 * 修改日志：
 */
package com.nci.svg.module;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.w3c.dom.Element;

import com.nci.svg.graphunit.NCIEquipSymbolBean;
import com.nci.svg.util.Constants;

import fr.itris.glips.library.Toolkit;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.actions.dom.DomActionsModule;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

public class ResetSymbolModule extends DomActionsModule {
    public ResetSymbolModule(Editor editor)
    {
        super(editor);
        moduleId="ResetSymbol";


        createMyItems();
    }
    
    protected void createMyItems() {

        // getting the menu resources
        moduleLabel = ResourcesManager.bundle.getString(moduleId + "ItemLabel");
        moduleIcon = ResourcesManager.getIcon(moduleId, false);

        // creating the menu
        moduleMenu = new JMenu(moduleLabel);
        moduleMenu.setIcon(moduleIcon);

        // creating the listener to the menu items
        ActionListener listener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                for (int i = 0; i < actionsMenuItems.length; i++) {

                    if (e.getSource().equals(actionsMenuItems[i])) {

                        // getting the current handle
                        SVGHandle handle = editor.getHandlesManager()
                                .getCurrentHandle();

                        if (handle != null) {

                            doAction(handle, new HashSet<Element>(handle
                                    .getSelection().getSelectedElements()),
                                    i, e);
                            editor.getSvgSession().refreshCurrentHandleLater();
                        }

                        break;
                    }
                }
            }
        };


        // creating the arrays
        if(!((String) editor.getGCParam("webflag")).equals("1")){
            actionsIds = new String[0];
            return;
        }
        int length = editor.getGraphUnitManager().getSymbolsStatus().size();
        actionsIds = new String[length];
        actionsLabels = new String[length];
        undoRedoActionsLabels = new String[length];
        actionsIcons = new Icon[length];
        actionsDisabledIcons = new Icon[length];
        actionsMenuItems = new JMenuItem[length];

        // filling the arrays
        for (int i = 0; i < length; i++) {

            // getting the labels
            actionsIds[i] = editor.getGraphUnitManager().getSymbolsStatus().get(i);
            actionsLabels[i] = actionsIds[i];
            undoRedoActionsLabels[i] = moduleLabel + ":" + actionsIds[i];

            // getting the icons
            actionsIcons[i] = ResourcesManager.getIcon(actionsIds[i], false);
            actionsDisabledIcons[i] = ResourcesManager.getIcon(actionsIds[i],
                    true);

            // creating the menu item
            actionsMenuItems[i] = new JMenuItem(actionsLabels[i]);
            actionsMenuItems[i].setIcon(actionsIcons[i]);
            actionsMenuItems[i].setDisabledIcon(actionsDisabledIcons[i]);
            actionsMenuItems[i].setEnabled(false);

            // adding the listener
            actionsMenuItems[i].addActionListener(listener);

            // adding the menu item to the menu
            moduleMenu.add(actionsMenuItems[i]);
        }
    }

    /* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.actions.dom.DomActionsModule#doAction(fr.itris.glips.svgeditor.display.handle.SVGHandle, java.util.Set, int, java.awt.event.ActionEvent)
     */
    @Override
    protected void doAction(SVGHandle handle, Set<Element> elements, int index,
            ActionEvent evt) {
        // TODO Auto-generated method stub
        if(elements == null || elements.size() != 1 )
            return;
        
        Element element = elements.iterator().next();
        if(element.getNodeName().equals("image"))
        {
            String strType = element.getAttribute("nciType");
            if(strType != null && strType.equals("GraphUnit"))
            {
                
                strType = element.getAttribute("graphUnitIdentifier");
                NCIEquipSymbolBean bean = editor.getGraphUnitManager().getOriginalSymbolMap().get(strType);
                if(bean == null)
                    return;
                String strStatus = bean.getGraphUnitStatus();
                if(strStatus == null || strStatus.length() == 0 || strStatus.equals("无"))
                    return;
                
                String strGroup = bean.getGraphUnitGroup();
                String strSymbolName = editor.getGraphUnitManager().getStatusMap().get(
                        strGroup)
                        .getStatusSymbol(index);
                if(strSymbolName.length() == 0) return;
                bean = editor.getGraphUnitManager().getOriginalSymbolMap().get(strSymbolName);
                element.setAttribute("graphUnitIdentifier",strSymbolName);
                String strPath = new File(Constants.NCI_SVG_SYMBOL_CACHE_DIR + bean.getGraphUnitType()+"/"+bean.getGraphUnitName()
                        + Constants.NCI_SVG_EXTENDSION).toURI()
                        .toString();
                element.setAttributeNS("http://www.w3.org/1999/xlink","xlink:href",strPath);
                editor.getSvgSession().refreshCurrentHandleImediately();
                
            }
        }
    }

    /* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.actions.dom.DomActionsModule#selectionCorrect(int, java.util.Set)
     */
    @Override
    protected boolean selectionCorrect(int index, Set<Element> elements) {
        // TODO Auto-generated method stub
        if(elements == null || elements.size() != 1 )
            return false;
        
        Element element = elements.iterator().next();
        if(element.getNodeName().equals("image"))
        {
            String strType = element.getAttribute("nciType");
            if(strType != null && strType.equals("GraphUnit"))
            {
                strType = element.getAttribute("graphUnitIdentifier");
                NCIEquipSymbolBean bean = editor.getGraphUnitManager().getOriginalSymbolMap().get(strType);
                if(bean == null)
                    return false;
                String strStatus = bean.getGraphUnitStatus();
                if(strStatus == null || strStatus.length() == 0 || strStatus.equals("无"))
                    return false;
                
                String strGroup = bean.getGraphUnitGroup();
                String strSymbolName = editor.getGraphUnitManager().getStatusMap().get(
                        strGroup)
                        .getStatusSymbol(index);
                if(strSymbolName.length() == 0) return false;
                return true;
            }
        }
        return false;
    }
    
    
}
