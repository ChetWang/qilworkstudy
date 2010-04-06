/**
 * 类名：com.nci.svg.module
 * 创建人:yx.nci
 * 创建日期：2008-9-2
 * 类作用:TODO
 * 修改日志：
 */
package com.nci.svg.module;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.JMenuItem;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.nci.svg.bean.NestOperBean;
import com.nci.svg.util.Constants;
import com.nci.svg.util.Utilities;

import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.ModuleAdapter;
import fr.itris.glips.svgeditor.actions.popup.PopupItem;
import fr.itris.glips.svgeditor.actions.popup.PopupSubMenu;

/**
 * @author yx.nci
 *
 */
public class NestOperModule extends ModuleAdapter {
    private ArrayList list = null;
    public NestOperModule(Editor editor)
    {
        super(editor);
        getNestOper();
    }

    /* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.ModuleAdapter#getPopupItems()
     */
    @Override
    public Collection<PopupItem> getPopupItems() {
        LinkedList<PopupItem> popupItems = new LinkedList<PopupItem>();
        if(list == null ||list.size() == 0)
            return popupItems;
        String iconName = null;
        NestOperBean bean = (NestOperBean)list.get(0);
        PopupSubMenu subMenu = new PopupSubMenu(editor, bean.getOperID(),
                bean.getOperName(), bean.getOperID());
        // creating the about popup item
        for (int i = 1; i < list.size(); i++) {
            
            bean = (NestOperBean)list.get(i);
            final int nIndex = i;
            PopupItem item = new PopupItem(editor, bean.getOperID(),
                    bean.getOperName(), iconName) {

                @Override
                public JMenuItem getPopupItem(LinkedList<Element> nodes) {
                    menuItem.setEnabled(false);
                    if (nodes.size() == 1) {//0表示没选中任何图形，1表示图形选中一个图元，2及以上表示选中多个图元
                        // adds the action listeners
                        final Element element = Utilities
                                .parseSelectedElement(nodes.get(0));
                        boolean bEnable = false;
                        // Node metadata = element.getNextSibling();
                        Node metadata = Utilities.getSingleChildElement(
                                Constants.NCI_SVG_METADATA, element);
                        if(metadata == null)
                        {
                            metadata = Utilities.getSingleSibingElement(
                                    Constants.NCI_SVG_METADATA, element);
                        }
                        String nciTypeValue = element
                                .getAttribute(Constants.NCI_SVG_Type_Attr);
                        // 如果是nciType的设备类型，或者是有metadata的非text类型节点，
                        // 或者是包含metadata的g节点（含多个子设备），都当作是一个设备
                        // if (metadata != null) {

                        
                        try {
                            if ((nciTypeValue != null && Constants.nciGraphTypes
                                    .contains(nciTypeValue))
                                    || (!element.getNodeName()
                                            .equalsIgnoreCase("text") && metadata
                                            .getNodeName().equalsIgnoreCase(
                                                    Constants.NCI_SVG_METADATA))
                                    || (element.getNodeName().equalsIgnoreCase(
                                            "g") && (element
                                            .getElementsByTagName(
                                                    Constants.NCI_SVG_METADATA)
                                            .getLength() > 0))) {
                                bEnable = true;
                            }

                                if(bEnable)
                                {
                                menuItem
                                        .addActionListener(new ActionListener() {

                                            public void actionPerformed(
                                                    ActionEvent e) {

                                                doAction(nIndex, element);
                                            }
                                        });
                                menuItem.setEnabled(true);
                            }
                        } catch (NullPointerException e) {

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
    private void getNestOper()
    {
        final StringBuffer baseUrl = new StringBuffer().append(
                (String) editor.getGCParam("appRoot")).append(
                (String) editor.getGCParam("servletPath")).append(
                "?action=get_NestOper");
        
        try {
            list = (ArrayList) Utilities.communicateWithURL(
                    baseUrl.toString(), null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            list = null;
        }
        return;
    }
    private void doAction(int index,Element element)
    {
        NestOperBean bean = (NestOperBean)list.get(index);
        StringBuffer baseUrl = new StringBuffer().append(
                (String) editor.getGCParam("appRoot")).append(
                (String) editor.getGCParam("servletPath")).append(
                "?action=handleOperUri");
        String[][] param = new String[4][2];
        param[0][0] = "operType";
        param[0][1] = bean.getOperID();
        if(editor.getMetaDataManager().getPSMSID(element).length() > 0)
        {
            param[1][0] = "codeType";
            param[1][1] = "psms";
            param[2][0] = "code";
            param[2][1] = editor.getMetaDataManager().getPSMSID(element);
        }
        else
        {
            param[1][0] = "codeType";
            param[1][1] = "scada";
            param[2][0] = "code";
            param[2][1] = editor.getMetaDataManager().getScadaID(element);
        }
        param[3][0] = "appRoot";
        param[3][1] = (String)editor.getGCParam("approot");
        String strUrl = "";
        try {
            strUrl = (String) Utilities.communicateWithURL(
                    baseUrl.toString(), param);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(strUrl.length() > 0)
            Utilities.gotoWebSite(strUrl, bean.getOperName());
    }
}
