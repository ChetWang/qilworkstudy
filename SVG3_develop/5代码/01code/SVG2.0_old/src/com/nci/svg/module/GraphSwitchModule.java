/**
 * 类名：com.nci.svg.module.GraphSwitchModule
 * 创建人:yx.nci
 * 创建日期：2008-8-29
 * 类作用:图元切换类
 * 修改日志：
 */
package com.nci.svg.module;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.JMenuItem;

import org.w3c.dom.Element;
import com.nci.svg.util.Utilities;

import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.ModuleAdapter;
import fr.itris.glips.svgeditor.actions.popup.PopupItem;
import fr.itris.glips.svgeditor.resources.ResourcesManager;


/**
 * @author yx.nci
 *
 */
public class GraphSwitchModule extends ModuleAdapter {

    private String strGraphSwitchID = "GraphSwitchModule";

    public GraphSwitchModule(Editor editor) {
        super(editor);
    }

    /* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.ModuleAdapter#getPopupItems()
     */
    @Override
    public Collection<PopupItem> getPopupItems() {
        // TODO Auto-generated method stub
        LinkedList<PopupItem> popupItems = new LinkedList<PopupItem>();
        String iconName = null;
        PopupItem item = new PopupItem(editor, strGraphSwitchID,
                strGraphSwitchID, iconName) {

            @Override
            public JMenuItem getPopupItem(LinkedList<Element> nodes) {
                menuItem.setEnabled(false);
                if (nodes.size() == 1) {//0表示没选中任何图形，1表示图形选中一个图元，2及以上表示选中多个图元
                    // adds the action listeners
                    final Element element = Utilities
                            .parseSelectedElement(nodes.get(0));
                    boolean bEnable = false;

                    //根据第一个图元信息，决定显示的文字和是否显示
                    String strType = analyseElement(element);
                    if ( strType != null) {
                        bEnable = true;
                        if(strType.equals("substation"))
                        {
                            menuItem.setText(ResourcesManager.bundle.getString("GraphSwitch_ycjxt"));
                        }
                        else if(strType.equals("link") || strType.equals("bus"))
                        {
                            menuItem.setText(ResourcesManager.bundle.getString("GraphSwitch_dxt"));
                        }
                    }
                    if (bEnable) {
                        menuItem.addActionListener(new ActionListener() {

                            public void actionPerformed(ActionEvent e) {

                                doAction(element);
                            }
                        });
                    }
                    menuItem.setEnabled(bEnable);
                }

                return super.getPopupItem(nodes);
            }
        };

        popupItems.add(item);

        return popupItems;
    }

    private String analyseElement(Element element) {
        Element metadata = (Element)element.getElementsByTagName("metadata").item(0);
        if(metadata == null)
            return null;
        
        Element cimClass = (Element)metadata.getElementsByTagName("PSR:CimClass").item(0);
        if(cimClass == null)
            return null;
        
        String strType = cimClass.getAttribute("CimType");
        if(strType.toLowerCase().equals("substation") || strType.toLowerCase().equals("link")
                || strType.toLowerCase().equals("bus"))
        {
            return strType.toLowerCase();
        }
        return null;
    }

    public void doAction(Element element) {

        String strType = analyseElement(element);
        if(strType == null)
            return;
        Element objref = (Element)element.getElementsByTagName("PSR:ObjRef").item(0);
        if(objref == null)
            return;
        
        //先提取psmsid
        String strcode = objref.getAttribute("AppCode");
        if(strcode.length() != 0)
        {
            editor.getSvgSession().openRemoteSVGFileOldByCode("psms", strcode);
            return;
        }
        
        //psmsid不存在，提取scadaid
        strcode = objref.getAttribute("ScadaID");
        if(strcode.length() != 0)
        {
            editor.getSvgSession().openRemoteSVGFileOldByCode("scada", strcode);
            return;
        }
        
        //scadaid不存在，提取misid
        strcode = objref.getAttribute("MisID");
        if(strcode.length() != 0)
        {
            editor.getSvgSession().openRemoteSVGFileOldByCode("mis", strcode);
            return;
        }
        return ;
        
    }

}
