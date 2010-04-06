/*
 * Created on 7 juin 2004
 * 
 =============================================
                   GNU LESSER GENERAL PUBLIC LICENSE Version 2.1
 =============================================
GLIPS Graffiti Editor, a SVG Editor
Copyright (C) 2003 Jordi SUC, Philippe Gil, SARL ITRIS

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

Contact : jordi.suc@itris.fr; philippe.gil@itris.fr

 =============================================
 */
package fr.itris.glips.svgeditor;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.nci.svg.sdk.NCIClassLoader;
import com.nci.svg.sdk.bean.ModuleInfoBean;
import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.SysSetDefines;
import com.nci.svg.sdk.client.communication.CommunicationBean;
import com.nci.svg.sdk.client.function.ModuleAdapter;
import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.communication.ActionNames;
import com.nci.svg.sdk.communication.ActionParams;

import fr.itris.glips.svgeditor.actions.menubar.EditorMenuBar;
import fr.itris.glips.svgeditor.actions.popup.PopupManager;
import fr.itris.glips.svgeditor.actions.toolbar.ToolBarManager;
import fr.itris.glips.svgeditor.shape.AbstractShape;

/**
 * @author ITRIS, Jordi SUC the class loading the modules
 */
public class ModuleManager {

	/**
	 * the editor
	 */
	private EditorAdapter editor;

	/**
	 * the menu bar
	 */
	private EditorMenuBar menubar;

	/**
	 * the popup manager
	 */
	private PopupManager popupManager;

	/**
	 * the color manager
	 */
	private ColorManager colorManager;

	/**
	 * the toolBar manager
	 */
	private ToolBarManager toolBarManager;

	/**
	 * the list of the modules
	 */
	private LinkedList<ModuleAdapter> modules = new LinkedList<ModuleAdapter>();

	/**
	 * the list of the shape modules
	 */
	private Set<fr.itris.glips.svgeditor.shape.AbstractShape> shapeModules = new HashSet<fr.itris.glips.svgeditor.shape.AbstractShape>();

	/**
	 * the list of the classes of the modules
	 */
	private LinkedList<String> moduleClasses = new LinkedList<String>();

	/**
	 * the resource image manager
	 */
	private ResourceImageManager resourceImageManager;

	private boolean initFinished = false;

	/**
	 * the constructor of the class
	 * 
	 * @param editor
	 *            the editor
	 */
	public ModuleManager(EditorAdapter editor) {

		this.editor = editor;
	}

	/**
	 * initializes the object
	 */
	public void init() {

		menubar = new EditorMenuBar(editor);

		// the toolBar manager

		toolBarManager = new ToolBarManager(editor);
		// gets the module's classes

		parseXMLModules();

		// add by yux,
		// 升级业务组件，并加载
		if (editor.getGCParam(SysSetDefines.WEB_FLAG) == null
				|| ((String) editor.getGCParam(SysSetDefines.WEB_FLAG))
						.equals("1")) {
			upgradeModule(Constants.MODULE_TYPE_BUSINESS);
		}
		// creates the static modules

		createModuleObjects();

		// the color manager

		colorManager = new ColorManager(editor);

		// the resource image manager

		resourceImageManager = new ResourceImageManager(editor);

		// the popup menu manager

		popupManager = new PopupManager(editor);
		// initializes the menu bar
		// menubar.init();
		//
		// initFinished = true;

	}

	public boolean isInitFinished() {
		return initFinished;
	}

	public void hideMenuBar() {
		menubar.setVisible(false);
	}

	public void hideToolBar() {
		toolBarManager.getToolsBar().setVisible(false);
	}

	/**
	 * initializes some parts
	 */
	public void initializeParts() {
		menubar.init();

		initFinished = true;
		Collection<ModuleAdapter> mds = getModules();

		for (ModuleAdapter module : mds) {

			if (module != null) {

				module.initialize();
			}
		}

		toolBarManager.initializeParts();
		editor.getHandlesManager().initializeParts();
	}

	/**
	 * parses the XML document to get the modules
	 */
	protected void parseXMLModules() {

		Document doc = editor.getModeManager().getModuleDocument();

		if (doc == null)
			return;
		Element root = doc.getDocumentElement();
		Node current = null;
		NamedNodeMap attributes = null;
		String name = null, sclass = null;

		for (NodeIterator it = new NodeIterator(root); it.hasNext();) {

			current = it.next();

			if (current != null) {

				name = current.getNodeName();
				attributes = current.getAttributes();

				if (name != null && name.equals("module") && attributes != null) {

					// adds the string representing a class in the list linked
					// with static items
					sclass = attributes.getNamedItem("class").getNodeValue();

					if (sclass != null && !sclass.equals("")) {

						moduleClasses.add(sclass);
					}
				}
			}
		}
	}

	/**
	 * creates the objects corresponding to the modules
	 */
	// protected void createModuleObjects() {
	// add(new fr.itris.glips.svgeditor.io.module.IOModule(editor));
	// add(new fr.itris.glips.svgeditor.actions.clipboard.ClipboardModule(
	// editor));
	// add(new fr.itris.glips.svgeditor.undoredo.UndoRedoModule(editor));
	// add(new fr.itris.glips.svgeditor.options.OptionsModule(editor));
	// add(new fr.itris.glips.svgeditor.shape.RectangleShape(editor));
	// add(new fr.itris.glips.svgeditor.shape.EllipseShape(editor));
	// add(new fr.itris.glips.svgeditor.shape.path.PathShape(editor));
	// add(new fr.itris.glips.svgeditor.shape.ImageShape(editor));
	// add(new fr.itris.glips.svgeditor.shape.GroupShape(editor));
	// add(new fr.itris.glips.svgeditor.shape.text.TextShape(editor));
	// add(new com.nci.svg.sdk.shape.GraphUnitImageShape(editor));
	// add(new fr.itris.glips.svgeditor.selection.SelectionModule(editor));
	// add(new fr.itris.glips.svgeditor.actions.dom.AlignModule(editor));
	// add(new fr.itris.glips.svgeditor.actions.dom.CenterOnCanvasModule(
	// editor));
	// add(new fr.itris.glips.svgeditor.actions.dom.DistributeModule(editor));
	// add(new fr.itris.glips.svgeditor.actions.dom.FlipModule(editor));
	// add(new fr.itris.glips.svgeditor.actions.dom.RotateModule(editor));
	// add(new fr.itris.glips.svgeditor.actions.dom.SameDimensionsModule(
	// editor));
	// add(new fr.itris.glips.svgeditor.actions.dom.OrderModule(editor));
	// add(new fr.itris.glips.svgeditor.actions.dom.PathOperationsModule(
	// editor));
	// add(new fr.itris.glips.svgeditor.actions.dom.CanvasSizeModule(editor));
	// add(new fr.itris.glips.svgeditor.AboutModule(editor));
	// add(new fr.itris.glips.svgeditor.DOMViewerModule(editor));
	// add(new fr.itris.glips.svgeditor.display.canvas.zoom.ZoomModule(editor));
	// add(new fr.itris.glips.svgeditor.display.canvas.rulers.RulersModule(
	// editor));
	// add(new fr.itris.glips.svgeditor.display.canvas.grid.GridModule(editor));
	// add(new fr.itris.glips.svgeditor.properties.SVGProperties(editor));
	//
	// add(new com.nci.svg.shape.NCISymbolShape(editor));
	//
	// add(new com.nci.svg.sdk.display.background.EditorBackgroundModule(
	// editor));
	//
	// add(new com.nci.svg.module.SymbolRefreshModule(editor));
	// add(new com.nci.svg.module.SymbolSwtichModule(editor));
	// add(new com.nci.svg.module.TerminalModule(editor));
	//
	// }
	
//	public void add(Object obj) {
//		if (obj instanceof AbstractShape) {
//
//			shapeModules.add((AbstractShape) obj);
//		}
//
//		modules.add((ModuleAdapter) obj);
//	}

	/**
	 * creates the objects corresponding to the modules
	 */
	protected void createModuleObjects() {
		Object obj = null;
		Class<?>[] classargs = { EditorAdapter.class };
		Object[] args = { editor };
		for (String current : moduleClasses) {

			if (current != null && !current.equals("")) {

				try {

					// editor.getLogger().log(editor, LoggerAdapter.DEBUG,
					// current);
					// creates instances of each static module
					obj = Class.forName(current).getConstructor(classargs)
							.newInstance(args);
					System.out.println(current);
					// if it is a shape module, it is added to the list of the
					// shape module
					if (obj instanceof AbstractShape) {

						shapeModules.add((AbstractShape) obj);
					}

					modules.add((ModuleAdapter) obj);
				} catch (Exception ex) {
					ex.printStackTrace();
					System.out.println(current);
				}
			}

		}
	}

	/**
	 * gets the module given its name
	 * 
	 * @param name
	 *            the module's name
	 * @return a module
	 */
	public Object getModule(String name) {

		String cname = null;

		for (ModuleAdapter module : modules) {

			try {
				cname = (String) module.getClass().getMethod("getName",
						(Class[]) null).invoke(module, (Object[]) null);
			} catch (Exception e) {
				cname = null;
			}

			if (cname != null && cname.equals(name)) {

				return module;
			}
		}
		return null;
	}

	public synchronized Object getModuleByID(String moduleID) {
		String cname = null;

		for (ModuleAdapter module : modules) {
			try {
				cname = (String) module.getClass().getMethod("getModuleID",
						(Class[]) null).invoke(module, (Object[]) null);
			} catch (Exception e) {
				cname = null;
			}
			if (cname != null && cname.equals(moduleID)) {

				return module;
			}
		}
		return null;
	}

	/**
	 * @return the collection of the objects corresponding to the modules
	 */
	public Collection<ModuleAdapter> getModules() {
		return modules;
	}

	/**
	 * returns a shape module given its id
	 * 
	 * @param moduleId
	 *            the id of a module
	 * @return a shape module given its id
	 */
	public AbstractShape getShapeModule(String moduleId) {

		for (AbstractShape shape : shapeModules) {

			if (shape.getId().equals(moduleId)) {

				return shape;
			}
		}

		return null;
	}

	/**
	 * @return the collection of the objects corresponding to the shape modules
	 */
	public Set<AbstractShape> getShapeModules() {
		return shapeModules;
	}

	/**
	 * @return Returns the color manager.
	 */
	public ColorManager getColorManager() {
		return colorManager;
	}

	/**
	 * @return the menubar
	 */
	public synchronized EditorMenuBar getMenuBar() {
		// System.out.println(menubar);
		while (menubar == null) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
			System.out.println("waiting menubar...");
		}
		return menubar;
	}

	/**
	 * @return the tool bar manager
	 */
	public synchronized ToolBarManager getToolBarManager() {
		while (toolBarManager == null) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
			System.out.println("waiting toolbar manager...");
		}
		return toolBarManager;
	}

	/**
	 * @return the popup manager
	 */
	public PopupManager getPopupManager() {
		return popupManager;
	}

	/**
	 * @return Returns the resourceImageManager.
	 */
	public ResourceImageManager getResourceImageManager() {
		return resourceImageManager;
	}

	/**
	 * 根据组件类型，获取组件升级清单
	 * 
	 * @param moduleType：组件类型，参见Constants
	 * @return 更新清单结果
	 */
	public boolean upgradeModule(String moduleType) {
		String[][] params = new String[1][2];
		params[0][0] = ActionParams.MODULE_TYPE;
		params[0][1] = moduleType;
		ResultBean bean = editor.getCommunicator().communicate(
				new CommunicationBean(ActionNames.GET_UPGRADE_MODULE, params));
		if (bean == null || bean.getReturnFlag() == ResultBean.RETURN_ERROR) {
			return false;
		}

		return downloadModules((ArrayList<ModuleInfoBean>) bean.getReturnObj());
	}

	/**
	 * add by yux,2009-1-9 待下载的组件列表
	 * 
	 * @param Modulelist
	 * @return
	 */
	public boolean downloadModules(ArrayList<ModuleInfoBean> moduleList) {
		for (ModuleInfoBean bean : moduleList) {
			downloadModule(bean);
		}
		return false;
	}

	/**
	 * add by yux,2009-1-14 根据组件信息下载组件模块,并加载至jvm内存中
	 * 
	 * @param bean:模块信息
	 * @return:下载及加载结果
	 */
	public boolean downloadModule(ModuleInfoBean bean) {
		String[][] params = new String[1][2];
		params[0][0] = ActionParams.MODULE_SHORT_NAME;
		params[0][1] = bean.getModuleShortName();
		ResultBean resultBean = editor.getCommunicator().communicate(
				new CommunicationBean(ActionNames.GET_UPGRADE_MODULE, params));
		if (resultBean == null
				|| resultBean.getReturnFlag() == ResultBean.RETURN_ERROR) {
			return false;
		}

		byte[] content = (byte[]) resultBean.getReturnObj();
		if (content != null) {
			String jarName = Constants.NCI_SVG_DOWNLOADS_CACHE_DIR
					+ bean.getModuleShortName() + bean.getEdition() + ".jar";
			try {
				FileOutputStream fos = new FileOutputStream(new File(jarName));
				fos.write(content, 0, content.length);
				fos.close();
				File localJarFile = new File(jarName);
				if (localJarFile.exists()) {
					NCIClassLoader.addClassPath(localJarFile);
					moduleClasses.add(bean.getClassName());
				} else {
					return false;
				}

			} catch (Exception e) {
			}
		}
		return true;
	}

}
