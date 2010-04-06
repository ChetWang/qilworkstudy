/**
 * 类名：com.nci.svg.module.NCISvgSearchModule
 * 创建人:yx.nci
 * 创建日期：2008-6-30
 * 类作用:当前窗体内查找模块
 * 修改日志：
 */
package com.nci.svg.module;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.nci.svg.ui.search.NCISvgSearchDialog;
import com.nci.svg.util.Utilities;

import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.ModuleAdapter;
import fr.itris.glips.svgeditor.display.handle.HandlesListener;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * @author yx.nci
 *
 */
public class NCISvgSearchModule extends ModuleAdapter {

    /**
     * 模型标识
     */
    public static final String id = "SvgSearch";

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
    protected JMenuItem searchMenu;

    private ActionListener searchListener = null;
    
    private NCISvgSearchDialog searchDialog = null;
    
    
    public NCISvgSearchModule(Editor editor)
    {
    	super(editor);
        createMenuAndToolItems(); 
        searchDialog = new NCISvgSearchDialog(editor.findParentFrame(),true,this);
    }
    
    protected void createMenuAndToolItems() {
        menuLabel = ResourcesManager.bundle.getString(id + "ItemLabel");

        // getting the menu icons
        menuIcon = ResourcesManager.getIcon(id, false);
        
        searchListener = new ActionListener() {

            public void actionPerformed(ActionEvent actionevent) {
                search();
            }
        };
        searchMenu = new JMenuItem(menuLabel, menuIcon);
        searchMenu.setEnabled(false);
        searchMenu.addActionListener(searchListener);
        searchMenu.setAccelerator(KeyStroke.getKeyStroke("ctrl F"));
        
        final HandlesListener svgHandlesListener = new HandlesListener() {

            @Override
            public void handleChanged(SVGHandle currentHandle,
                    Set<SVGHandle> handles) {
                if(currentHandle != null)
                    searchMenu.setEnabled(true);
                else
                    searchMenu.setEnabled(false);
            }

        };
        editor.getHandlesManager().addHandlesListener(svgHandlesListener);
    }

    /* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.ModuleAdapter#getMenuItems()
     */
    @Override
    public HashMap<String, JMenuItem> getMenuItems() {
        HashMap<String, JMenuItem> map = new HashMap<String, JMenuItem>();
        map.put(id, searchMenu);

        return map;
    }
    
    /**
     * 显示寻找界面，允许寻找
     */
    public void search()
    {
    	searchDialog.setVisible(true);
        
        
    }
    
    /**
     * 显示寻找到的节点，并显示在屏幕中间位置
     * @param handle:待显示的窗口
     * @param element：待显示的节点
     */
    public void showElement(SVGHandle handle,Element element)
    {
        handle.getSelection().handleSelection(element, false, true);
        
        //将选中的节点在屏幕中间位置显示
        Dimension dimension = handle.getCanvas().getScrollPane().getScrollValues();
        dimension.height /= handle.getCanvas().getZoomManager().getCurrentScale();
        dimension.width /= handle.getCanvas().getZoomManager().getCurrentScale();
        Rectangle rect = handle.getCanvas().getScrollPane().getViewPortBounds();
        rect.height /=handle.getCanvas().getZoomManager().getCurrentScale();
        rect.width /=handle.getCanvas().getZoomManager().getCurrentScale();
        double x = new Double(element.getAttribute("x")).doubleValue();
        double y = new Double(element.getAttribute("y")).doubleValue();
        if(x < dimension.width || y < dimension.height)
        {
            dimension.width = (int)x -  rect.width/2;   
            dimension.height = (int)y -  rect.height/2; 
            if(dimension.width < 0 )
                dimension.width = 0;
            if(dimension.height < 0 )
                dimension.height = 0;
            
            dimension.height *= handle.getCanvas().getZoomManager().getCurrentScale();
            dimension.width *= handle.getCanvas().getZoomManager().getCurrentScale();
            handle.getCanvas().getScrollPane().setScrollValues(dimension);
        }
        else if(x >= dimension.width + rect.width
                || y >= dimension.height + rect.height)
        {
            dimension.width = (int)x -  rect.width/2;   
            dimension.height = (int)y -  rect.height/2; 
            if(dimension.width < 0 )
                dimension.width = 0;
            if(dimension.height < 0 )
                dimension.height = 0;
            
            dimension.height *= handle.getCanvas().getZoomManager().getCurrentScale();
            dimension.width *= handle.getCanvas().getZoomManager().getCurrentScale();
            handle.getCanvas().getScrollPane().setScrollValues(dimension);
        }
        return;
    }
    
    
    public String getName(){
    	return id;
    }
}
