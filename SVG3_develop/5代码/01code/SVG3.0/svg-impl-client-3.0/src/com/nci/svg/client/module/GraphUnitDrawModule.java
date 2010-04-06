package com.nci.svg.client.module;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.function.ModuleAdapter;
import com.nci.svg.sdk.mode.EditorModeListener;

import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * 绘图时所需的图元拖拽及相关操作及区域显示的模块，即左侧的图元选择区域
 * 
 * @author Qil.Wong
 * 
 */
public class GraphUnitDrawModule extends ModuleAdapter {

	public final static String MODULE_ID = "45f3757e-4f9a-49c9-931f-c9520304975f";

	private String[] ids = { "nci_graphunit_find" };

	private JMenuItem[] menuItems = new JMenuItem[1];

	public GraphUnitDrawModule(final EditorAdapter editor) {
		super(editor);
		moduleUUID = MODULE_ID;
		initMenuItems();
		editor.getModeManager().addModeListener(new EditorModeListener() {
			@Override
			public void modeChanged() {
				if (!editor.getModeManager().isOutlookPaneCreate()) {
					for (int i = 0; i < ids.length; i++) {
						menuItems[i].setVisible(false);
						if (i == 0) {
							menuItems[i].setAccelerator(null);
						}
					}
				} else {
					for (int i = 0; i < ids.length; i++) {
						menuItems[i].setVisible(true);
						if (i == 0) {
							menuItems[i].setAccelerator(KeyStroke.getKeyStroke(
									KeyEvent.VK_F, InputEvent.CTRL_MASK
											| InputEvent.SHIFT_MASK));
						}
					}
				}

			}

		});
	}

	/**
	 * 初始化模块所需的菜单项
	 */
	private void initMenuItems() {
		for (int i = 0; i < ids.length; i++) {
			menuItems[i] = new JMenuItem(ResourcesManager.bundle
					.getString(ids[i]));
			if (i == 0) {
				menuItems[i].setAccelerator(KeyStroke.getKeyStroke(
						KeyEvent.VK_F, InputEvent.CTRL_MASK
								| InputEvent.SHIFT_MASK));
			}
			final int index = i;
			menuItems[i].addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					doAction(index);

				}

			});
		}
	}

	@Override
	public HashMap<String, JMenuItem> getMenuItems() {
		HashMap<String, JMenuItem> map = new HashMap<String, JMenuItem>();
		for (int i = 0; i < ids.length; i++) {
			map.put(ids[i], this.menuItems[i]);
		}
		return map;
	}

	private void doAction(int index) {
		switch (index) {
		case 0:
			editor.getOutlookPanel().getSearchPanel().setVisible(true);
			break;
		default:
			break;
		}
	}

}
