/*
 * Created on 18 juil. 2004
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

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JMenuItem;

import org.w3c.dom.Element;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.function.ModuleAdapter;
import com.nci.svg.sdk.ui.about.SVGAboutPanel;

import fr.itris.glips.svgeditor.actions.popup.PopupItem;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * the class used to display the "about" dialog
 * 
 * @author ITRIS, Jordi SUC
 * 
 */
public class AboutModule extends ModuleAdapter {

	/**
	 * the id
	 */
	private String idAbout = "About";

	/**
	 * the labels
	 */
	private String labelAbout = "";

	/**
	 * the menu item
	 */
	private final JMenuItem about = new JMenuItem();

	/**
	 * the resource bundle
	 */
	private ResourceBundle bundle;

	/**
	 * the listener to the about menu items
	 */
	private ActionListener aboutListener;
	
	
	private  AboutDialog aboutDialog = null;

	/**
	 * the constructor of the class
	 * 
	 * @param editor
	 *            the editor
	 */
	public AboutModule(final EditorAdapter editor) {

		super(editor);

		// the resource bundle
		bundle = ResourcesManager.bundle;

		if (bundle != null) {

			try {
				labelAbout = bundle.getString("labelabout");
			} catch (Exception ex) {
			}
		}
		initAbout();
		
	}
	
	private void initAbout(){
		// getting the icons
		ImageIcon aboutIcon = ResourcesManager.getIcon("About", false), daboutIcon = ResourcesManager
				.getIcon("About", true);

		// the menuitem
		about.setText(labelAbout);
		about.setIcon(aboutIcon);
		about.setDisabledIcon(daboutIcon);
		

		// creating the listener to the menu item
		aboutListener = new ActionListener() {

			public void actionPerformed(ActionEvent evt) {
				showAbout();
			}
		};

		about.addActionListener(aboutListener);
	}
	
	private void showAbout(){
		if(aboutDialog == null){
			aboutDialog = new AboutDialog(editor.findParentFrame());
		}
		aboutDialog.setLocationRelativeTo(editor.getParent());
		aboutDialog.setVisible(true);
	}

	@Override
	public HashMap<String, JMenuItem> getMenuItems() {

		HashMap<String, JMenuItem> menuItems = new HashMap<String, JMenuItem>();
		menuItems.put(idAbout, about);

		return menuItems;
	}

	@Override
	public Collection<PopupItem> getPopupItems() {

		LinkedList<PopupItem> popupItems = new LinkedList<PopupItem>();

		// creating the about popup item
		PopupItem item = new PopupItem(getSVGEditor(), idAbout, labelAbout,
				"About") {

			@Override
			public JMenuItem getPopupItem(LinkedList<Element> nodes) {
				menuItem.setEnabled(false);
				if (nodes.size() <= 0) {
					// adds the action listeners
					menuItem.addActionListener(aboutListener);
					menuItem.setEnabled(true);
				}
				return super.getPopupItem(nodes);
			}
		};

		popupItems.add(item);

		return popupItems;
	}

	/**
	 * @return the editor
	 */
	public EditorAdapter getSVGEditor() {
		return editor;
	}

	/**
	 * the dialog that will be shown
	 * 
	 * @author ITRIS, Jordi SUC
	 */
	protected class AboutDialog extends JDialog {

		/**
		 * ¹¹Ôìº¯Êý
		 */
		protected AboutDialog(Frame f) {
			super(f,false);
			setTitle(labelAbout);			
			getContentPane().add(new SVGAboutPanel(this));
			pack();
		}
	}
}
