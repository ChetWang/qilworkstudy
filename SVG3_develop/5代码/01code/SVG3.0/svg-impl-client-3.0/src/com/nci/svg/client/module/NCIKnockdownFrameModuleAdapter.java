/**
 * 类名：com.nci.svg.module.NCIKnockdownFrameModule
 * 创建人:yx.nci
 * 创建日期：2008-7-17
 * 类作用:组合式模块显示
 * 修改日志：
 */
package com.nci.svg.client.module;

import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.svg.SVGDocument;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.function.ModuleAdapter;

import fr.itris.glips.svgeditor.display.handle.SVGHandle;

/**
 * @author yx.nci
 *
 */
public class NCIKnockdownFrameModuleAdapter extends ModuleAdapter {
    private String strFrameLabel = "";
    private SVGHandle svgHandle = null;
    
    private final static int MIN_AREA_HEIGHT= 10;


    /**
     * @param strFrameLabel the strFrameLabel to set
     */
    public void setStrFrameLabel(String strFrameLabel) {
        this.strFrameLabel = strFrameLabel;
    }

    public NCIKnockdownFrameModuleAdapter(EditorAdapter editor)
    {
        super(editor);
        
    }
    
    private SVGDocument tranDoc(Document doc)
    {
        SVGDocument svgDoc = null;
        return svgDoc;
    }
    
    public void show(Document doc)
    {
        SVGDocument svgDoc = tranDoc(doc);
        if(editor == null || editor.getHandlesManager() == null)
            return;
        
        if(editor.getHandlesManager().getHandle(strFrameLabel) != null)
            svgHandle = editor.getHandlesManager().getHandle(strFrameLabel);
        else
            svgHandle = editor.getHandlesManager().createSVGHandle(strFrameLabel);
        if(svgHandle == null)
            return;
        
        svgHandle.getCanvas().setDocument(svgDoc, null);
        return;
    }
    
    public void show(File file)
    {
        return;
    }
    
    public void show(String strFileName)
    {
        show(new File(strFileName));
        return;
    }
    
    public void readDoc(Document doc)
    {
        
        return;
    }
    
    
}
