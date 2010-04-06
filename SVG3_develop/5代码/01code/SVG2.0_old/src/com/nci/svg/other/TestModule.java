package com.nci.svg.other;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nci.svg.util.Utilities;

import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.EditorToolkit;
import fr.itris.glips.svgeditor.ModuleAdapter;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

public class TestModule extends ModuleAdapter {
    private double pos = 0;
    public static final String id = "NciTest";
    
    public static final String NCI_Test_ModuleID = "NciTestModule";
    /**
     * ²Ëµ¥±êÇ©
     */
    protected String menuLabel = "";
    /**
     * ²Ëµ¥Í¼±ê
     */
    protected Icon menuIcon;
    /**
     * ²Ëµ¥Ïî
     */
    protected JMenuItem menuItem;

    public TestModule(Editor editor)
    {
        super(editor);
        menuLabel = "²âÊÔ";
        
        menuIcon = ResourcesManager.getIcon(id, false);
        
        menuItem = new JMenuItem(menuLabel,menuIcon);
        menuItem.setEnabled(true);
        
        ActionListener listener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                testFun();
            }
        };
        menuItem.addActionListener(listener);
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

    public void testFun()
    {
        SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
        Element element = handle.getSelection().getSelectedElements().iterator().next();
        element = (Element)element.getElementsByTagName("use").item(0);
        element.setAttributeNS(EditorToolkit.xmlnsXLinkNS, "xlink:href",
                "#target_order0");
        handle.getEditor().getSvgSession().refreshCurrentHandleImediately();
//        handle.getSvgElementsManager().
//        setTransform(element, newElementTransform);
    }
}
