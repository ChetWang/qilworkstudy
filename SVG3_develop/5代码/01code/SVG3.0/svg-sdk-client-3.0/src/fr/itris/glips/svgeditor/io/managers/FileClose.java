package fr.itris.glips.svgeditor.io.managers;

import java.util.*;

import javax.swing.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.io.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class handling the closing of files
 * @author Jordi SUC
 */
public class FileClose {

	/**
	 * the io manager
	 */
	private IOManager ioManager;
	
	/**
	 * the labels
	 */
	private String warningTitle="", warningMessage="";
	
	
	/**
	 * add by yux,2009-4-22
	 * 远程文件关闭提示
	 */
	private String remoteWarningTitle="", remoteWarningMessage="";
	
	/**
	 * the constructor of the class
	 * @param ioManager the io manager
	 */
	public FileClose(IOManager ioManager){
		
		this.ioManager=ioManager;
		
		warningTitle=ResourcesManager.bundle.getString("FileCloseWarningTitle");
		warningMessage=ResourcesManager.bundle.getString("FileCloseWarningMessage");
		remoteWarningTitle=ResourcesManager.bundle.getString("RemoteFileCloseWarningTitle");
		remoteWarningMessage=ResourcesManager.bundle.getString("RemoteFileCloseWarningMessage");
	}
	
	/**
	 * closes the provided handle, some verifications are 
	 * done before closing so that no modifications are lost
	 * @param handle a svg handle
	 * @param relativeComponent the component relatively 
	 * to which the save dialog will be shown
	 */
	public void closeHandle(SVGHandle handle, JComponent relativeComponent){
		
		if(handle.getCanvas().getGraphFileBean() != null 
				&& handle.getCanvas().getGraphFileBean().getID() != null
				&& handle.getCanvas().getGraphFileBean().getID().length() > 0)
		{
			//远程获取的图形
			int returnValue=JOptionPane.showConfirmDialog(
					ioManager.getEditor().getParent(), remoteWarningMessage, remoteWarningTitle, 
						JOptionPane.YES_NO_CANCEL_OPTION);
			
			if(returnValue==JOptionPane.YES_OPTION){
				ioManager.getFileSaveManager().saveHandleDocumentToServer(handle, relativeComponent);
				handle.setModified(false);
			}
		}
		if(handle.isModified()){
			
			//checking whether the file should be saved, or the close action cancelled
			int returnValue=JOptionPane.showConfirmDialog(
					ioManager.getEditor().getParent(), warningMessage, warningTitle, 
						JOptionPane.YES_NO_CANCEL_OPTION);
			
			if(returnValue==JOptionPane.YES_OPTION){
				
				boolean saveActionLaunched=
					ioManager.getFileSaveManager().saveHandleDocument(
							handle, false, relativeComponent);
				
				if(saveActionLaunched){
					
					close(handle);
				}
				
			}else if(returnValue==JOptionPane.NO_OPTION){
				
				close(handle);
			}

		}else{
			
			close(handle);
		}
	}
	
	/**
	 * closes all the handles
	 */
	public void closeAll(){
		
		//getting the collection of the svg handles
		Collection<SVGHandle> handles=
			ioManager.getEditor().getHandlesManager().getHandles();
		
		for(SVGHandle handle : handles){
			
			if(handle!=null){
				
				closeHandle(handle, null);
			}
		}
	}
	
	/**
	 * closes the provided handle
	 * @param handle the handle to close
	 */
	public void close(SVGHandle handle){
		
		ioManager.getEditor().getHandlesManager().
        	removeHandle(handle.getName());
	}
}
