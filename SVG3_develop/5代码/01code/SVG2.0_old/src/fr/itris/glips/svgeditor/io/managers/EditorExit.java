package fr.itris.glips.svgeditor.io.managers;

import java.util.Collection;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import com.nci.svg.module.NCIHelpModule;

import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.io.IOManager;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * the manager enabling to exit the editor
 * 
 * @author Jordi SUC
 */
public class EditorExit {

	/**
	 * the labels
	 */
	private String warningTitle = "", warningMessage = "";

	/**
	 * the io manager
	 */
	private IOManager ioManager;

	/**
	 * the constructor of the class
	 * 
	 * @param ioManager
	 *            the io manager
	 */
	public EditorExit(IOManager ioManager) {

		this.ioManager = ioManager;
		warningTitle = ResourcesManager.bundle
				.getString("EditorExitWarningTitle");
		warningMessage = ResourcesManager.bundle
				.getString("EditorExitWarningMessage");
	}

	/**
	 * exits the editor
	 */
	public void exit() {

		// saving the current state of the editor
		ioManager.getEditor().getResourcesManager().saveEditorsCurrentState();

		if (ioManager.getEditor().isQuitActionDisabled()) {

			// closing all the files that are currently being edited
			ioManager.getFileCloseManager().closeAll();

			// hiding the editor's frame
			ioManager.getEditor().setVisible(false);

		} else {

			// quitting the editor
			// checks if some svg documents have been modified
			boolean displayDialog = false;
			Collection<SVGHandle> handles = new LinkedList<SVGHandle>(ioManager.getEditor().getHandlesManager().getHandles());

			if (handles.size() > 0) {

				for (SVGHandle hnd : handles) {

					if (hnd != null && hnd.isModified()) {

						displayDialog = true;
						break;
					}
				}
			}

			boolean canExitEditor = ioManager.getEditor().canExitFromJVM();

			if (displayDialog) {

				// if svg documents have not been save, an alert dialog is
				// displayed
				int returnVal = JOptionPane.showConfirmDialog(ioManager.getEditor()
						.getParent(), warningMessage, warningTitle,
						JOptionPane.YES_NO_OPTION);

				canExitEditor = (canExitEditor && returnVal == JOptionPane.YES_OPTION);
			}

			if (canExitEditor) {
				
			    try {
					((NCIHelpModule)ioManager.getEditor()
					        .getModule(NCIHelpModule.NCI_Help_ModuleID))
					        .closeHelp();
				} catch (Exception e) {
					
				}

				ioManager.getEditor().dispose();
                                System.exit(0);
//				if (!NciSvgApplet.isOpened)
//					System.exit(0);
//				else{
//					NciSvgApplet.isOpened = false;
//					NciSvgApplet.close();
//				}
			}
		}
	}

}