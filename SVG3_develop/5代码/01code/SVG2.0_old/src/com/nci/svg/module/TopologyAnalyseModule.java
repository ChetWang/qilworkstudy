/**
 * ������com.nci.svg.module.TopologyAnalyseModule
 * ������:yx
 * �������ڣ�2008-5-12
 * ������:TODO
 * �޸���־��
 */
package com.nci.svg.module;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

import org.w3c.dom.Document;

import com.nci.svg.other.TopologyAnalyse;

import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.ModuleAdapter;
import fr.itris.glips.svgeditor.display.handle.HandlesListener;
import fr.itris.glips.svgeditor.display.handle.HandlesManager;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * @author yx
 *
 */
public class TopologyAnalyseModule extends ModuleAdapter {

    protected static String topologyAnalyseId = "TopologyAnalyse";// ͼԪ���ӵ�
    protected JMenuItem taMenuItem;
    protected ImageIcon taIcon,taDisabledIcon;
    protected String taLabel;

    private TopologyAnalyse taHandle = null;
    
    public TopologyAnalyseModule(Editor editor)
    {
    	super(editor);
        taIcon=ResourcesManager.getIcon("AddPointPath", false);
        taDisabledIcon=ResourcesManager.getIcon("AddPointPath", true);
        
        taLabel=ResourcesManager.bundle.getString(topologyAnalyseId+"Label");
        
        taMenuItem=new JMenuItem(taLabel, taIcon);
        taMenuItem.setDisabledIcon(taDisabledIcon);
        taMenuItem.setEnabled(false);
        
        ActionListener listener=new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                topologyAnalyse();
            }
        };
        
        taMenuItem.addActionListener(listener);
        
        final HandlesManager svgHandleManager=
        	editor.getHandlesManager();
        
        svgHandleManager.addHandlesListener(new HandlesListener(){
            
            @Override
            public void handleChanged(SVGHandle currentHandle, Set<SVGHandle> handles) {
                if(currentHandle != null && currentHandle.getHandleType() == SVGHandle.HANDLE_TYPE_SVG)
                {
                    taMenuItem.setEnabled(true);
                }
                else
                {
                    taMenuItem.setEnabled(false);
                }
            }           
        }); 
        
    }
    /* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.ModuleAdapter#getMenuItems()
     */
    @Override
    public HashMap<String, JMenuItem> getMenuItems() {
        // TODO Auto-generated method stub

        HashMap<String, JMenuItem> map=new HashMap<String, JMenuItem>();
        map.put(topologyAnalyseId, taMenuItem);
        return map;
    }
    
    public void topologyAnalyse()
    {
        if(taHandle == null)
            taHandle = new TopologyAnalyse(editor);
        
        HandlesManager svgHandleManager=
        	editor.getHandlesManager();
        Document doc = svgHandleManager.getCurrentHandle().getCanvas().getDocument();
        if(doc!= null)
            taHandle.splitRela(doc);
    }


}
