package fr.itris.glips.svgeditor.io.managers;

import java.awt.Frame;
import java.io.File;
import java.net.URI;
import java.util.concurrent.ExecutionException;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.jdesktop.swingworker.SwingWorker;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

import com.nci.svg.sdk.bean.GraphFileBean;
import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.client.communication.CommunicationBean;
import com.nci.svg.sdk.client.util.EditorToolkit;
import com.nci.svg.sdk.communication.ActionNames;
import com.nci.svg.sdk.communication.ActionParams;
import com.nci.svg.sdk.module.GraphUnitModuleAdapter;
import com.nci.svg.sdk.ui.layer.LayerDocSaveDialog;
import com.nci.svg.svgeditor.io.managers.monitor.NCISaveToServerMonitor;

import fr.itris.glips.library.monitor.Monitor;
import fr.itris.glips.library.util.XMLPrinter;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.io.IOManager;
import fr.itris.glips.svgeditor.io.managers.dialog.FileChooserDialog;
import fr.itris.glips.svgeditor.io.managers.dialog.SVGFileFilter;
import fr.itris.glips.svgeditor.io.managers.monitor.SaveMonitor;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * the class handling the saving of files
 * 
 * @author Jordi SUC
 */
public class FileSave {

	/**
	 * the io manager
	 */
	private IOManager ioManager;

	/**
	 * the file chooser dialog
	 */
	private FileChooserDialog fileChooserDialog;

	/**
	 * the constructor of the class
	 * 
	 * @param ioManager
	 *            the io manager
	 */
	public FileSave(IOManager ioManager) {

		this.ioManager = ioManager;

		// creating the file chooser dialog
		if (ioManager.getEditor().getParent() instanceof Frame) {

			fileChooserDialog = new FileChooserDialog((Frame) ioManager
					.getEditor().getParent(), FileChooserDialog.SAVE_FILE_MODE,
					ioManager.getEditor());

		} else if (ioManager.getEditor().getParent() instanceof JDialog) {

			fileChooserDialog = new FileChooserDialog((JDialog) ioManager
					.getEditor().getParent(), FileChooserDialog.SAVE_FILE_MODE,
					ioManager.getEditor());
		}

		// setting the file filter
		fileChooserDialog.setFileFilter(new SVGFileFilter());
		
	}

	/**
	 * saves the document denoted by the handle into the file the user has
	 * chosen, or the file that was previously chosen or opened
	 * 
	 * @param handle
	 *            a svg handle
	 * @param saveAs
	 *            whether the action is a saveAs one
	 * @param relativeComponent
	 *            the component relatively to which the dialog will be shown
	 * @return whether the save action has been launched
	 */
	public boolean saveHandleDocument(SVGHandle handle, boolean saveAs,
			JComponent relativeComponent) {

		// getting the file corresponding to the current handle
		File handleFile = getFile(handle);

		if (handleFile == null || saveAs) {

			// getting the file where to save the document
			boolean bFlag = false;
			while (bFlag == false) {
				// fileChooserDialog.set
				fileChooserDialog.showDialog(relativeComponent);

				// getting the selected file
				File selectedFile = fileChooserDialog.getSelectedFile();

				if (selectedFile != null) {

					String fileWillBeErasedWarningLabel = ResourcesManager.bundle
							.getString("FileSaveWillBeErasedWarning");
					if (selectedFile.exists()) {
						int returnVal = JOptionPane.showConfirmDialog(ioManager
								.getEditor().getParent(),
								fileWillBeErasedWarningLabel,
								fileWillBeErasedWarningLabel,
								JOptionPane.YES_NO_OPTION);

						if (returnVal == JOptionPane.NO_OPTION)
							continue;
					}
					bFlag = true;

					selectedFile = checkFileExtension(selectedFile);

					// creating the monitor
					Monitor monitor = new SaveMonitor(ioManager.getEditor()
							.getParent(), relativeComponent, 0,
							getNodesCount(handle.getCanvas().getDocument()));

					// saving the svg document into the selected file
					saveDocument(selectedFile, handle, monitor, true);
					return true;
				} else
					bFlag = true;
			}

		} else {
			if (handle.getCanvas().getSelectedLayerMap().size() > 0) {
				LayerDocSaveDialog layerSave = new LayerDocSaveDialog(ioManager
						.getEditor().findParentFrame(), true);
				layerSave.setVisible(true);
				if (layerSave.getSaveType() == LayerDocSaveDialog.SAVETYPE_FILTERED_SVG) {
					ioManager.getEditor().getLayerSelectionManager()
							.removeEffectAttrib(
									handle.getCanvas().getDocument());
					handle.getCanvas().getSelectedLayerMap().clear();
					saveHandleDocument(handle, true, relativeComponent);
				} else if (layerSave.getSaveType() == LayerDocSaveDialog.SAVETYPE_CANCEL) {
					return false;
				}
			}
			// creating the monitor
			Monitor monitor = new SaveMonitor(
					ioManager.getEditor().getParent(), relativeComponent, 0,
					getNodesCount(handle.getCanvas().getDocument()));

			// saving the svg document into the found file
			saveDocument(handleFile, handle, monitor, false);
			return true;
		}

		return false;
	}

	/**
	 * 将svg图形或图元保存至服务器
	 * 
	 * @param handle
	 * @param relativeComponent
	 * @return
	 */
	public boolean saveHandleDocumentToServer(final SVGHandle handle,
			JComponent relativeComponent) {
		// 和图元保存不一样，这要首先保存在本地
		// if (!saveHandleDocument(handle, false, relativeComponent))
		// return false;
		if (!handle.getCanvas().checkGraphFileBean()) {
			JOptionPane.showConfirmDialog(handle.getEditor().findParentFrame(),
					ResourcesManager.bundle
							.getString("nci_FileProperties_set_isnotnull"),
					"文件属性有未填写的，请重新设置文件属性", JOptionPane.CLOSED_OPTION,
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		final Monitor monitor = new NCISaveToServerMonitor(ioManager
				.getEditor().findParentFrame(), relativeComponent, 0, 0);
		// monitor.setIndeterminate(true);
		monitor.start();

		SwingWorker worker = new SwingWorker() {

			@Override
			protected Object doInBackground() throws Exception {
				saveSvgFileToServer(handle, handle.getCanvas()
						.getGraphFileBean());
				return null;
			}

			public void done() {
				monitor.dispose();
			}

		};
		worker.execute();
		return true;
	}

	/**
	 * 远程保存图元svg
	 * 
	 * @param handle
	 * @param relativeComponent
	 * @return
	 */
	public boolean saveSymbolDocToServer(final SVGHandle handle,
			JComponent relativeComponent) {

		final Monitor monitor = new NCISaveToServerMonitor(ioManager
				.getEditor().findParentFrame(), relativeComponent, 0, 0);
		monitor.start();
		final GraphUnitModuleAdapter m = (GraphUnitModuleAdapter) ioManager
				.getEditor()
				.getModule(GraphUnitModuleAdapter.idGraphUnitManage);
		SwingWorker worker = new SwingWorker() {

			@Override
			protected Object doInBackground() throws Exception {

				boolean result = m.saveSymbol(handle, monitor);
				return new Boolean(result);
			}

			public void done() {
				try {
					Boolean result = (Boolean) get();
					if (result.booleanValue()) {
						handle.setModified(false);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
				monitor.dispose();
			}

		};
		worker.execute();
		return true;
	}

	/**
	 * 图元的保存操作。 added by wangql
	 * 
	 * @param handle
	 * @param saveAs
	 * @param relativeComponent
	 * @return
	 */
	public boolean saveGraphUnitHandleDocument(SVGHandle handle,
			boolean saveAs, JComponent relativeComponent) {
		// if (saveAs) {
		//			
		// }
		// JOptionPane.showConfirmDialog(handle.getCanvas(), "图元保存功能还未完成!\r\n"
		// + this.getClass().getName()
		// + "下saveGraphUnitHandleDocument(...)方法中定义.", "信息",
		// JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
		// return false;
		return saveHandleDocument(handle, saveAs, relativeComponent);

	}

	/**
	 * saves the provided document into the provided file <<<<<<<
	 * FileSave.java
	 * 
	 * @param file
	 *            a file
	 * @param handle
	 *            the handle to save
	 * @param monitor
	 *            the monitor used to display the state of the save action
	 * @param handleNameChanged
	 *            whether the handle name has changed =======
	 * @param file
	 *            a file
	 * @param handle
	 *            the handle to save
	 * @param monitor
	 *            the monitor used to display the state of the save action
	 * @param handleNameChanged
	 *            whether the handle name has changed
	 */
	public void saveDocument(final File file, final SVGHandle handle,
			final Monitor monitor, boolean handleNameChanged) {

		// getting the path corresponding to the file

		// String filePath = file.toURI().toASCIIString();
		// 为显示中文，不用file.toURI().toASCIIString()
		String filePath = file.toURI().toString();
		if (handleNameChanged && !handle.isSymbolHandle()) {

			// setting the new name for the handle
			ioManager.getEditor().getHandlesManager().changeName(
					handle.getName(), filePath);
			handle.getCanvas().setNewURI(filePath);

			// storing the name of the file in the recent open files
			if (handle.getHandleType() == SVGHandle.HANDLE_TYPE_SVG) {
				ioManager.getEditor().getResourcesManager().addRecentFile(
						filePath);
				ioManager.getEditor().getResourcesManager().notifyListeners();
			}
		}

		// setting the state of the handle to no more modified
		handle.setModified(false);
		final Document doc = handle.getCanvas().getDocument();

		// add by yuxiang,图元编辑将g连接点信息转换成metadata格式
		final Document newdoc = handleGraphUnitDoc(handle, doc);

		// creating the runnable that will be added to the runnable queue
		ioManager.requestExecution(new Runnable() {

			public void run() {

				// prints the document into a file
				XMLPrinter.printXML(newdoc, file, monitor);
			}
		});
	}

	public Document handleGraphUnitDoc(final SVGHandle handle, Document doc) {
		if (handle.getHandleType() != SVGHandle.HANDLE_TYPE_GRAPH_UNIT_OLD)
			return doc;
		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
		SVGDocument newdoc = (SVGDocument) impl.createDocument(svgNS, "svg",
				null);
		Element rootOriginal = newdoc.getDocumentElement();
		newdoc.removeChild(rootOriginal);
		Element rootnew = doc.getDocumentElement();
		rootnew = (Element) newdoc.importNode(rootnew, true);
		newdoc.appendChild(rootnew);
		// Document newdoc = null;//new Document().importNode(doc, true);
		// newdoc = doc;
		Element eRoot = newdoc.getDocumentElement();

		int ngCount = eRoot.getElementsByTagName("g").getLength();
		Element gElement = null;
		int nPointCount = 0;
		int i = 0;
		if (ngCount == 0) {
			return doc;
		} else {
			for (i = 0; i < ngCount; i++) {
				gElement = (Element) eRoot.getElementsByTagName("g").item(i);
				String strid = gElement.getAttribute("id");
				if (strid != null && strid.equals("nci:terminal")) {

					break;
				}
				gElement = null;
			}

			if (gElement == null)
				return doc;

		}

		nPointCount = gElement.getElementsByTagName("ellipse").getLength();
		// 创建新metadata数据段
		Element terminal = (Element) eRoot.getElementsByTagName("terminal")
				.item(0);
		if (terminal == null) {
			terminal = newdoc.createElementNS(
					SVGDOMImplementation.SVG_NAMESPACE_URI, "terminal");
			terminal.setAttribute("id", "terminal");
			eRoot.appendChild(terminal);
		}
		for (i = 0; i < nPointCount; i++) {
			Element cElement = (Element) gElement.getElementsByTagName(
					"ellipse").item(i);
			Element npElement = newdoc.createElement("nci:POINT");

			npElement.setAttribute("name", cElement.getAttribute("name"));
			npElement.setAttribute("x", cElement.getAttribute("cx"));
			npElement.setAttribute("y", cElement.getAttribute("cy"));
			terminal.appendChild(npElement);
		}

		eRoot.removeChild(gElement);

		return newdoc;
	}

	// /**
	// * checks if the provided file has an extension. If not, the "svg"
	// extension
	// * is added to it.
	// *
	// * @param file
	// * a file
	// * @return the file whose extension has been corrected if needed >>>>>>>
	// 1.2
	// */
	// public void saveDocument(final File file, final SVGHandle handle,
	// final Monitor monitor, boolean handleNameChanged) {
	//	
	// // getting the path corresponding to the file
	// // String filePath = file.toURI().toASCIIString();
	// // 为显示中文，不用file.toURI().toASCIIString()
	// String filePath = file.toURI().toString();
	//	
	// if (handleNameChanged) {
	//	
	// // changed by wangql,为了针对不同的格式（svg图形和图元），需要有两种策略
	// // setting the new name for the handle
	// Editor.getEditor().getHandlesManager().changeName(handle.getName(),
	// filePath);
	// handle.getCanvas().setNewURI(filePath);
	//	
	// // storing the name of the file in the recent open files
	//				
	// if (handle.getHandleType() == SVGHandle.HANDLE_TYPE_SVG) {
	// Editor.getEditor().getResourcesManager()
	// .addRecentFile(filePath);
	// Editor.getEditor().getResourcesManager().notifyListeners();
	// }
	//				
	// // else if (handle.getHandleType() ==
	// // SVGHandle.HANDLE_TYPE_GRAPH_UNIT) {
	// // // Editor
	// // // .getEditor()
	// // // .getHandlesManager()
	// // // .changeName(
	// // // handle.getName(),
	// // // ResourcesManager.bundle
	// // // .getString(NCIGraphUnitModule.idGraphUnitManage)
	// // // + ":" + filePath);
	// // Editor.getEditor().getHandlesManager().changeName(
	// // handle.getName(), filePath);
	// // handle.getCanvas().setNewURI(filePath);
	// //
	// // // storing the name of the file in the recent open files
	// // // Editor.getEditor().getResourcesManager()
	// // // .addRecentFile(filePath);
	// // Editor.getEditor().getResourcesManager().notifyListeners();
	// // }
	// }
	//	
	// // setting the state of the handle to no more modified
	// handle.setModified(false);
	// final Document doc = handle.getCanvas().getDocument();
	//	
	// // creating the runnable that will be added to the runnable queue
	// ioManager.requestExecution(new Runnable() {
	//	
	// public void run() {
	//	
	// // prints the document into a file
	// XMLPrinter.printXML(doc, file, monitor);
	// }
	// });
	// }
	/**
	 * checks if the provided file has an extension. If not, the "svg" extension
	 * is added to it.
	 * 
	 * @param file
	 *            a file
	 * @return the file whose extension has been corrected if needed
	 */
	protected File checkFileExtension(File file) {

		File newFile = file;

		// getting the path of the file
		String filePath = file.toURI().toASCIIString();

		if (filePath.indexOf(".") == -1) {

			// the file has no extension, the svg one is then added
			filePath += EditorToolkit.SVG_FILE_EXTENSION;

			// creating the new file
			try {
				newFile = new File(new URI(filePath));
			} catch (Exception ex) {
			}
		}

		return newFile;
	}

	/**
	 * returns the file corresponding to this svg handle
	 * 
	 * @param handle
	 *            a svg handle
	 * @return the file corresponding to this svg handle
	 */
	protected File getFile(SVGHandle handle) {

		File file = null;

		try {
			file = new File(new URI(handle.getName()));
		} catch (Exception ex) {
		}

		if (file != null && !file.exists()) {

			file = null;
		}

		return file;
	}

	/**
	 * returns the nodes number that could be found in the provided document
	 * 
	 * @param doc
	 *            a document
	 * @return the nodes number that could be found in the provided document
	 */
	protected int getNodesCount(Document doc) {

		int nodesCount = XMLPrinter.getNodesCount(doc);
		nodesCount += nodesCount / 5;

		return nodesCount;
	}

	private boolean saveSvgFileToServer(SVGHandle handle, GraphFileBean bean) {

		if (bean == null)
			return false;
		System.out.println(bean);
		// String content = Utilities.printNode(handle.getCanvas().getDocument()
		// .getDocumentElement(), false);
		// bean.setContent(content);
		String[][] params = new String[17][2];
		params[0][0] = ActionParams.ID;
		params[0][1] = bean.getID();
		params[1][0] = ActionParams.FILE_NAME;
		params[1][1] = bean.getFileName().replace("*", "");
		params[2][0] = ActionParams.GRAPH_TYPE;
		params[2][1] = bean.getFileType();
		params[3][0] = ActionParams.BUSINESS_TYPE;
		params[3][1] = bean.getBusiType();
		params[4][0] = ActionParams.FILE_FORMAT;
		params[4][1] = bean.getFileFormat();
		for (int i = 0; i < 10; i++) {
			params[5 + i][0] = ActionParams.PARAM + i;
			params[5 + i][1] = bean.getParams(i);
		}
		params[15][0] = ActionParams.CONTENT;
		params[15][1] = bean.getContent();
		params[16][0] = ActionParams.LOGS;
		params[16][1] = handle.getCanvas().getLogs();

		ResultBean result = ioManager.getEditor().getCommunicator()
				.communicate(
						new CommunicationBean(ActionNames.SAVE_GRAPH_FILE,
								params));
		if (result == null || result.getReturnFlag() == ResultBean.RETURN_ERROR) {
			ioManager.getEditor().getSvgSession().showMessageBox("文件远程保存",
					"文件存储失败");
			return false;
		}

		ioManager.getEditor().getSvgSession()
				.showMessageBox("文件远程保存", "文件存储成功");
		handle.getCanvas().setGraphFileBean(
				(GraphFileBean) result.getReturnObj(), false);
		handle.getCanvas().setBCreate(false);
		handle.getCanvas().clearLogs();
		handle.setModified(false);
		return true;
	}
}
