/**
 * 类名：com.nci.svg.module.ModuleAdapter
 * 创建人:yx.nci
 * 创建日期：2008-6-18
 * 类作用:单线图提取，保存及自动成图
 * 修改日志：
 */
package com.nci.svg.module;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.w3c.dom.Document;

import com.nci.svg.other.MongLineData;
import com.nci.svg.util.NCIGlobal;

import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.ModuleAdapter;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.display.selection.Selection;
import fr.itris.glips.svgeditor.resources.ResourcesManager;
import fr.itris.glips.svgeditor.shape.EllipseShape;

public class MongLineDrawingModule extends ModuleAdapter {
    /**
     * 模型标识
     */
    protected static final String id = "MongLineDrawingModule";

    /**
     * 菜单图标
     */
    protected Icon menuIcon;
    
    private SVGHandle handle = null;

    /**
     * 菜单标签
     */
    protected String menuLabel = "";
    
    protected JMenuItem menuItem;
    private Document doc;

    
    public MongLineDrawingModule(Editor editor)
    {
        super(editor);
        createMenuAndToolItems();
    }
    
    /* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.ModuleAdapter#getMenuItems()
     */
    @Override
    public HashMap<String, JMenuItem> getMenuItems() {
        HashMap<String, JMenuItem> map = new HashMap<String, JMenuItem>();
        map.put(id, menuItem);

        return map;
    }

    protected void createMenuAndToolItems() {
        menuLabel = ResourcesManager.bundle.getString(id + "ItemLabel");

        // getting the menu icons
        menuIcon = ResourcesManager.getIcon(id, false);

        // creating the menu
        menuItem = new JMenuItem(menuLabel,menuIcon);
        menuItem.addActionListener(new  ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    doAction();
                }
            });
    }
    
    protected void doAction() {
        openMLFile("单线图");
        
    }
    
    public void openMLFile(String strMLName) {
        System.out.println("单线图");
        MongLineData data = new MongLineData();
        data.readData("c:\\work\\王城4455线.xml");
        handle =
            editor.getHandlesManager().
                createSVGHandle("王城4455线_单线图");

        handle.setNFileType("10");
        if(handle!=null){
            
            handle.getScrollPane().getSVGCanvas().
                newDocument((String)editor.getGCParam("newDocumentWidth"), (String)editor.getGCParam("newDocumentHeight"));
            this.doc = handle.getScrollPane().getSVGCanvas().getDocument();
        }
       
    }
    
    protected void DrawStation(int x,int y)
    {
    }
}
