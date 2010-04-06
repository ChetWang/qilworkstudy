/*
 * Created on 1 juil. 2004
 *
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
package fr.itris.glips.svgeditor.display.canvas.rulers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

import org.w3c.dom.Element;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.function.ModuleAdapter;
import com.nci.svg.sdk.client.util.Utilities;

import fr.itris.glips.svgeditor.actions.popup.PopupItem;
import fr.itris.glips.svgeditor.display.handle.HandlesListener;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * the class used to display the rulers
 * 
 * @author ITRIS, Jordi SUC
 */
public class RulersModule extends ModuleAdapter {

	/**
	 * the id
	 */
	private String id = "Rulers";

	/**
	 * the labels
	 */
	private String rulersHiddenLabel = "", rulersShownLabel = "",
			rulersSwitchLabel = "";

	/**
	 * the menu item used for the rulers
	 */
	private final JMenuItem displayRulers = new JMenuItem();

	private ActionListener rulersListener = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.itris.glips.svgeditor.ModuleAdapter#getPopupItems()
	 */
	@Override
	public Collection<PopupItem> getPopupItems() {
		LinkedList<PopupItem> popupItems = new LinkedList<PopupItem>();

		// creating the about popup item
		PopupItem item = new PopupItem(editor, id, rulersSwitchLabel, "Rulers") {

			@Override
			public JMenuItem getPopupItem(LinkedList<Element> nodes) {
				menuItem.setEnabled(false);
				if (nodes.size() <= 0) {
					// adds the action listeners
					menuItem.addActionListener(rulersListener);
					menuItem.setEnabled(true);
				}
				return super.getPopupItem(nodes);
			}
		};

		popupItems.add(item);

		return popupItems;
	}

	/**
	 * the constructor of the class
	 * 
	 * @param editor
	 *            the editor
	 */
	public RulersModule(final EditorAdapter editor) {
		super(editor);
		Utilities.executeRunnable(new Runnable() {
			public void run() {
				initModule();
			}
		});		
	}
	
	private void initModule() {
		// gets the labels from the resources
		ResourceBundle bundle = ResourcesManager.bundle;

		if (bundle != null) {
			try {
				rulersHiddenLabel = bundle.getString("RulersHidden");
				rulersShownLabel = bundle.getString("RulersShown");
				rulersSwitchLabel = bundle.getString("RulersSwitch");
			} catch (Exception ex) {
			}
		}

		// a listener that listens to the changes of the svg handles
		final HandlesListener svgHandlesListener = new HandlesListener() {

			@Override
			public void handleChanged(SVGHandle currentHandle,
					Set<SVGHandle> handles) {

				if (currentHandle != null) {

					// enables the menuitems
					if (editor.getModeManager().isRulerShown())
						displayRulers.setEnabled(true);
					else
						displayRulers.setEnabled(false);

				} else {
					// disables the menuitems

					displayRulers.setEnabled(false);
				}
			}
		};

		// adds the svg handles change listener
		editor.getHandlesManager().addHandlesListener(svgHandlesListener);

		// getting the icons
		ImageIcon icon = ResourcesManager.getIcon(id, false);
		ImageIcon disabledIcon = ResourcesManager.getIcon(id, true);

		// setting the properties of the menuitem
		if (editor.getHandlesManager().getRulersParametersHandler()
				.areRulersEnabled()) {
			displayRulers.setText(rulersShownLabel);
		} else {
			displayRulers.setText(rulersHiddenLabel);
		}
		displayRulers.setIcon(icon);
		displayRulers.setDisabledIcon(disabledIcon);
		displayRulers.setEnabled(false);

		// adding the listener
		rulersListener = new ActionListener() {

			public void actionPerformed(ActionEvent evt) {

				SVGHandle handle = getEditor().getHandlesManager()
						.getCurrentHandle();

				if (handle != null) {

					RulersParametersManager manager = getEditor()
							.getHandlesManager().getRulersParametersHandler();

					if (!manager.areRulersEnabled()) {

						manager.setRulersEnabled(true);
						displayRulers.setText(rulersShownLabel);

					} else {

						manager.setRulersEnabled(false);
						displayRulers.setText(rulersHiddenLabel);
					}
				}
			}
		};
		displayRulers.addActionListener(rulersListener);
	}

	@Override
	public HashMap<String, JMenuItem> getMenuItems() {

		HashMap<String, JMenuItem> menuItems = new HashMap<String, JMenuItem>();
		menuItems.put(id, displayRulers);

		return menuItems;
	}

	public String getName() {
		return id;
	}

	public JMenuItem getDisplayRulersMenuItem() {
		return displayRulers;
	}
}
