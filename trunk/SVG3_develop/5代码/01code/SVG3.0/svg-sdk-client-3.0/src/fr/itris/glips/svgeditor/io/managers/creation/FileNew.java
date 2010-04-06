package fr.itris.glips.svgeditor.io.managers.creation;

import java.util.ResourceBundle;

import javax.swing.JComponent;
import javax.swing.JMenuItem;

import com.nci.svg.sdk.client.SysSetDefines;
import com.nci.svg.sdk.client.business.BusinessInfoLocator;
import com.nci.svg.sdk.client.business.ShapeInfoLocator;

import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.io.IOManager;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * the class handling the file creation
 * 
 * @author Jordi SUC
 */
public class FileNew {

	/**
	 * the labels
	 */
	private String untitledLabel = "";

	IOManager ioManager;

	/**
	 * the constructor of the class
	 * 
	 * @param ioManager
	 *            the io manager
	 */
	public FileNew(IOManager ioManager) {
		this.ioManager = ioManager;
		// gets the labels from the resources
		ResourceBundle bundle = ResourcesManager.bundle;

		if (bundle != null) {

			try {
				untitledLabel = bundle.getString("FileNewUntitled");
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * asks the user for the parameters to create the new file
	 * 
	 * @param relativeComponent
	 *            the component relatively to which the dialog should be
	 *            displayed
	 */
	public void askForNewFileParameters(JComponent relativeComponent) {
		// modified by wangql, 直接给出默认大小
		// newDialog.showDialog(relativeComponent);

		JMenuItem menuItem = (JMenuItem) relativeComponent;

		this.createNewDocument((String) ioManager.getEditor().getGCParam(
				SysSetDefines.NEW_DOCUMENT_WIDTH), (String) ioManager
				.getEditor().getGCParam(SysSetDefines.NEW_DOCUMENT_HEIGHT),
				menuItem.getName());
	}

	/**
	 * creates a new document
	 * 
	 * @param width
	 *            the width of the new document
	 * @param height
	 *            the height of the new document
	 */
	public void createNewDocument(String width, String height,
			String strFileType) {
		SVGHandle handle = ioManager.getEditor().getHandlesManager()
				.createSVGHandle(untitledLabel);

		if (handle != null) {

			handle.getScrollPane().getSVGCanvas().newDocument(width, height);

     		handle.getScrollPane().getSVGCanvas().setFileType(strFileType,true);
     		handle.getScrollPane().getSVGCanvas().initLoad();
			// added by wangql
			if (handle.getCanvas().isNCISvgType()) {
				handle.getSelection().addBusinessSelection(
						new BusinessInfoLocator(handle));
				handle.getSelection().addBusinessSelection(
						new ShapeInfoLocator(handle));
			}

    	}
	}
}
