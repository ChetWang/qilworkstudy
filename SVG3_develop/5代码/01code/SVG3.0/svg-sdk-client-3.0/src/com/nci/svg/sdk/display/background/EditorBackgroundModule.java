/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nci.svg.sdk.display.background;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
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
 * 
 * @author Qil.Wong
 */
public class EditorBackgroundModule extends ModuleAdapter {

	private String idBackground = "nci_svgcanvas_background";
	private JMenuItem backgroundMenuItem = null;
	private ImageIcon backgroundIcon = null;
	private ActionListener backgroundMenuListener;
	private String backgroundLabel = "";

	/* (non-Javadoc)
     * @see fr.itris.glips.svgeditor.ModuleAdapter#getPopupItems()
     */
    @Override
    public Collection<PopupItem> getPopupItems() {
        LinkedList<PopupItem> popupItems = new LinkedList<PopupItem>();

        // creating the about popup item
        PopupItem item = new PopupItem(editor, idBackground, backgroundLabel,
                idBackground) {

            @Override
            public JMenuItem getPopupItem(LinkedList<Element> nodes) {
                menuItem.setEnabled(false);
                if (nodes.size() <= 0) {
                    // adds the action listeners
                    menuItem.addActionListener(backgroundMenuListener);
                    menuItem.setEnabled(true);
                }
                return super.getPopupItem(nodes);
            }
        };

        popupItems.add(item);

        return popupItems;
    }


    public EditorBackgroundModule(EditorAdapter editor) {
		super(editor);
		Utilities.executeRunnable(new Runnable() {
			public void run() {
				initModule();
			}
		});
	}
    
    private void initModule(){
    	ResourceBundle bundle = ResourcesManager.bundle;
		backgroundLabel = bundle.getString(idBackground);
		backgroundMenuListener = new ActionListener() {

			public void actionPerformed(ActionEvent actionevent) {
				showBackgroundSetting();
			}
		};
		backgroundMenuItem = new JMenuItem(backgroundLabel, backgroundIcon);
		backgroundMenuItem.setEnabled(false);
		backgroundMenuItem.addActionListener(backgroundMenuListener);

		final HandlesListener svgHandlesListener = new HandlesListener() {

			@Override
			public void handleChanged(SVGHandle currentHandle,
					Set<SVGHandle> handles) {
				backgroundMenuItem.setEnabled(currentHandle != null);
				if (currentHandle != null) {
					backgroundMenuItem.setEnabled(true);
				} else {
					backgroundMenuItem.setEnabled(false);
				}
			}

		};
		editor.getHandlesManager().addHandlesListener(svgHandlesListener);
    }

	
	private void showBackgroundSetting() {
		Color background = JColorChooser.showDialog(editor.getSplitPane(),
		        backgroundLabel, editor.getHandlesManager()
						.getCurrentHandle().getCanvas().getBackground());
		//modify by yuxiang 
		//背景色修改应用于单个窗体
		if (background != null) {
//			Utilities.refreshAllHandles(background);

	        SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
	            if (background != null && handle != null)
	            {
	                handle.getCanvas().setBackground(background, true);
	            // 目的是为了达到updateUI一样的效果，这里由于暂时无法找到全部更新图元的方法（拖动过图元后，留下的轨迹不会被下一个背景覆盖），所以只能以缩放窗口实现。
	            handle.getSVGFrame().setSize(
	                    handle.getSVGFrame().getWidth() + 1,
	                    handle.getSVGFrame().getHeight() + 1);
	            // handle.getCanvas().updateUI();
	        }
		}
	}

	@Override
	public HashMap<String, JMenuItem> getMenuItems() {
		HashMap<String, JMenuItem> menuItems = new HashMap<String, JMenuItem>();
		menuItems.put(idBackground, backgroundMenuItem);
		return menuItems;
	}

}
