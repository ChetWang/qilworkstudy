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

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.nci.svg.module.NCIViewEditModule;
import com.nci.svg.util.NCIGlobal;

import fr.itris.glips.svgeditor.actions.menubar.EditorMenuBar;
import fr.itris.glips.svgeditor.actions.popup.PopupManager;
import fr.itris.glips.svgeditor.actions.toolbar.ToolBarManager;
import fr.itris.glips.svgeditor.resources.ResourcesManager;
import fr.itris.glips.svgeditor.shape.AbstractShape;

/**
 * @author ITRIS, Jordi SUC the class loading the modules
 */
public class ModuleManager {

	/**
	 * the editor
	 */
	private Editor editor;

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
	private LinkedList<Module> modules = new LinkedList<Module>();

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
	public ModuleManager(Editor editor) {

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

		
		// creates the static modules

		createModuleObjects();


		
		// the color manager

		colorManager = new ColorManager(editor);

		// the resource image manager

		resourceImageManager = new ResourceImageManager(editor);

		// the popup menu manager

		popupManager = new PopupManager(editor);
		// initializes the menu bar
		menubar.init();	
		
		initFinished = true;
		
	}
	
	public boolean isInitFinished(){
		return initFinished;
	}
	
	public void hideMenuBar()
	{
	    menubar.setVisible(false);
	}
	
	public void hideToolBar()
	{
	    toolBarManager.getToolsBar().setVisible(false);  
	}

	/**
	 * initializes some parts
	 */
	public void initializeParts() {

		Collection<Module> mds = getModules();

		for (Module module : mds) {

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
	protected void createModuleObjects() {

		Object obj = null;

		for (String current : moduleClasses) {
//			long start = System.nanoTime();
			

			if (current != null && !current.equals("")) {

				try {
					Class<?>[] classargs = { Editor.class };
					Object[] args = { editor };

					// creates instances of each static module
					obj = Class.forName(current).getConstructor(classargs)
							.newInstance(args);
					System.out.println(current);
					// if it is a shape module, it is added to the list of the
					// shape module
					if (obj instanceof AbstractShape) {

						shapeModules.add((AbstractShape) obj);
					}

					modules.add((Module) obj);
				} catch (Exception ex) {
					ex.printStackTrace();
					System.out.println(current);
				}
			}
//			System.out.println(" ModuleManager 初始化子模块"+current+"耗时："
//					+ (System.nanoTime() - start) / 1000000l + "ms");
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

		for (Module module : modules) {

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

	/**
	 * @return the collection of the objects corresponding to the modules
	 */
	public Collection<Module> getModules() {
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
//		System.out.println(menubar);
		while(menubar==null){
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
		while(toolBarManager==null){
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
	protected ResourceImageManager getResourceImageManager() {
		return resourceImageManager;
	}

}
