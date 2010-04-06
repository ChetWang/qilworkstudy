package fr.itris.glips.svgeditor.io.managers.creation;

import java.awt.*;
import java.util.*;
import javax.swing.*;

import org.w3c.dom.Document;

import com.nci.svg.util.NCIGlobal;

import fr.itris.glips.library.util.XMLPrinter;
import fr.itris.glips.svgeditor.*;


import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.io.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class handling the file creation
 * @author Jordi SUC
 */
public class FileNew {

	/**
	 * the dialog used to specify the parameters for the new files to create
	 */
	private NewDialog newDialog;
	
	/**
	 * the labels
	 */
	private String untitledLabel="";
	
	IOManager ioManager;
	
	/**
	 * the constructor of the class
	 * @param ioManager the io manager
	 */
	public FileNew(IOManager ioManager){
		this.ioManager = ioManager;
		//gets the labels from the resources
		ResourceBundle bundle=ResourcesManager.bundle;
		
		if(bundle!=null){
		    
			try{
				untitledLabel=bundle.getString("FileNewUntitled");
			}catch (Exception ex){}
		}
		
		//creating the dialog used to specify the parameters for the new files to create
		if(ioManager.getEditor().getParent() instanceof Frame){
			
			newDialog=new NewDialog(this, (Frame)ioManager.getEditor().getParent(),ioManager.getEditor());
			
		}else if(ioManager.getEditor().getParent() instanceof JDialog){
			
			newDialog=new NewDialog(this, (JDialog)ioManager.getEditor().getParent(),ioManager.getEditor());
		}
	}
	
	/**
	 * asks the user for the parameters to create the new file
	 * @param relativeComponent the component relatively 
	 * to which the dialog should be displayed
	 */
	public void askForNewFileParameters(JComponent relativeComponent){
		//modified by wangql, 直接给出默认大小
//		newDialog.showDialog(relativeComponent);

	    JMenuItem menuItem = (JMenuItem)relativeComponent;
	   
		this.createNewDocument((String)ioManager.getEditor().getGCParam(NCIGlobal.NEW_DOCUMENT_WIDTH_Str),
		        (String)ioManager.getEditor().getGCParam(NCIGlobal.NEW_DOCUMENT_HEIGHT_Str), menuItem.getName());
	}
	
	/**
	 * creates a new document
	 * @param width the width of the new document
	 * @param height the height of the new document
	 */
	public void createNewDocument(String width, String height,String strFileType){
		SVGHandle handle=
			ioManager.getEditor().getHandlesManager().
				createSVGHandle(untitledLabel);
		
		if(handle!=null){

		    
		    handle.getScrollPane().getSVGCanvas().
		    	newDocument(width, height);
		    handle.getScrollPane().getSVGCanvas().setFileType(strFileType);
		}
	}
}
