/**
 * 类名：com.nci.svg.module
 * 创建人:yx.nci
 * 创建日期：2008-7-1
 * 类作用:TODO
 * 修改日志：
 */
package com.nci.svg.module;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.nci.svg.util.NCIGlobal;

import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.ModuleAdapter;
import fr.itris.glips.svgeditor.display.selection.Selection;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

import java.io.IOException;
import java.lang.Runtime;
import java.util.HashMap;

/**
 * @author yx.nci
 *
 */
public class NCIHelpModule extends ModuleAdapter {
    public static final String id = "NciHelp";
    
    public static final String NCI_Help_ModuleID = "NciHelpModule";
    private Process helpProcess = null;
    /**
     * 菜单标签
     */
    protected String menuLabel = "";
    /**
     * 菜单图标
     */
    protected Icon menuIcon;
    /**
     * 菜单项
     */
    protected JMenuItem menuItem;

    /* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.ModuleAdapter#getMenuItems()
     */
    @Override
    public HashMap<String, JMenuItem> getMenuItems() {
        HashMap<String, JMenuItem> map = new HashMap<String, JMenuItem>();
        map.put(id, menuItem);

        return map;
    }

    public NCIHelpModule(Editor editor)
    {
    	super(editor);
        menuLabel = ResourcesManager.bundle.getString(id + "ItemLabel");
        
        menuIcon = ResourcesManager.getIcon(id, false);
        
        menuItem = new JMenuItem(menuLabel,menuIcon);
        menuItem.setAccelerator(KeyStroke.getKeyStroke("F1"));
        menuItem.setEnabled(true);
        
        ActionListener listener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                openHelp();
            }
        };
        menuItem.addActionListener(listener);
    }
    
    public void openHelp()
    {
        String command = (String)editor.getGCParam("strHelpCommand");
        boolean bFlag = false;
        if(helpProcess != null)
        {
            int nValue = -1;
            try
            {
                nValue = helpProcess.exitValue();
            }
            catch(IllegalThreadStateException  iex)
            {

            }
            
            if(nValue == 0)
                helpProcess = null;
        }
        
        try {
            
            if(helpProcess == null)
                helpProcess = Runtime.getRuntime().exec(command);
            else
            {
                
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
  
    }
    
    public void closeHelp()
    {
        if(helpProcess != null)
        {
            int nValue = -1;
            try
            {
                nValue = helpProcess.exitValue();
            }
            catch(IllegalThreadStateException  iex)
            {
                helpProcess.destroy();
                helpProcess = null;

            }

        }
    }
    
    public String getName() {
        return NCI_Help_ModuleID;
    }
}
