package fr.itris.glips.svgeditor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JApplet;
import javax.swing.SwingUtilities;

import org.w3c.dom.Document;


import com.nci.svg.util.NCIGlobal;
import com.nci.svg.util.Utilities;

import fr.itris.glips.library.util.XMLPrinter;

/**
 * the class of the applet for the editor
 * 
 * @author Jordi SUC
 */
public class EditorApplet extends JApplet {

    /**
     * the editor
     */
    private Editor editor = null;
    private NCIGlobal initConfig = new NCIGlobal();

    @Override
    public void init() {

        try {
            SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {

                    createGUI();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * creates the GUI
     */
    private void createGUI() {

        iniParam();

        // creating the editor object
        editor = new Editor();

        // intializing the editor
        editor.init(this, "", false, false, true, false, null);
        OpenSvgFile("系统图.svg");
    }

    private void iniParam() {
        if (getParameter("SERVER_URL") != null)
            initConfig.setParam("appRoot",getParameter("SERVER_URL"));
    }

    @Override
    public void destroy() {

        if (editor != null) {

            editor.dispose();
        }

        super.destroy();
    }
    
    
    
    public void OpenSvgFile(String filename){
    	System.out.println("filename:" + filename);
    	String svgContent = "";
    	
    	// 获取远程请求到的SVG文件字符串
    	StringBuffer baseURL = new StringBuffer();
        baseURL.append((String)initConfig.getParam("appRoot")).append((String)initConfig.getParam("servletPath")).append(
                "?action=getonesvgfile");
        String[][] param = new String[1][2];
        param[0][0] = "filename";
        param[0][1] = filename;
		try {
                        svgContent = (String) Utilities.communicateWithURL(baseURL.toString(), param);
			System.out.println(svgContent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//        SVGDocument doc = (SVGDocument) Utilities.getXMLDocumentByString(svgContent);
		Document doc = Utilities.getXMLDocumentByString(svgContent);
		XMLPrinter.printXML(doc, new File("c:/1.svg"), null);
		editor.getIOManager().getFileOpenManager().open(new File("c:/1.svg"), null);
//    	NCIMultiXMLGraphUnitManager nciUM = new NCIMultiXMLGraphUnitManager();
//    	SVGDocument doc = (SVGDocument)nciUM.createSVGDocument(svgContent);
//    	System.out.println("success get doc");
////    	editor.getHandlesManager().getCurrentHandle().getCanvas().setDocument(doc, null, false);
//    	HandlesManager hm = editor.getHandlesManager();
//    	System.out.println("success get HandlesManager:"+hm.toString());
//    	SVGHandle svgH = hm.getCurrentHandle();
//    	System.out.println("success get SVGHandle:"+svgH.toString());
//    	SVGCanvas svgC = svgH.getCanvas();
//    	System.out.println("success get SVGCanvas:"+svgC.toString());
//    	svgC.setDocument(doc, null, false);
    }

}
