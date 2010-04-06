/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nci.svg.module;

import static com.nci.svg.util.Constants.EDITOR_SPLIT_DIVIDERSIZE_NORMAL;
import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.ModuleAdapter;
import fr.itris.glips.svgeditor.resources.ResourcesManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JToggleButton;

import com.nci.svg.util.Constants;

/**
 * 
 * @author Qil.Wong
 */
public class NCICustomGraphModule extends ModuleAdapter {

	private JToggleButton customUnitButton = new JToggleButton();;

	private String customUnitId = "NciSvgCustomUnit";

	private Icon customUnitIcon;

	/**
	 * 构造函数，创建自定义图元模块
	 * @param editor 编辑器Editor对象
	 */
	public NCICustomGraphModule(Editor editor) {
		super(editor);
		customUnitIcon = ResourcesManager.getIcon("NCICustomUnit", false);
		customUnitButton.setToolTipText(ResourcesManager.bundle
				.getString("nci_cusomunit_tooltip_hide"));
		customUnitButton.setIcon(customUnitIcon);
		createMenuAndToolItems();
	}

	@Override
	public HashMap<String, AbstractButton> getToolItems() {
		HashMap<String, AbstractButton> toolItems = new HashMap<String, AbstractButton>();
		toolItems.put(customUnitId, customUnitButton);
		return toolItems;
	}

	/**
	 * 创建菜单和工具栏对应的控件,以及相应的事件处理
	 */
	protected void createMenuAndToolItems() {
		
		customUnitButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (customUnitButton.isSelected()) {
					hidePlatte();
				} else {
					showPlatte();
				}
			}
		});
	}
	
	public void hidePlatte(){
		customUnitButton.setSelected(true);
		editor.getSplitPane().getLeftComponent().setVisible(false);
		editor.getSplitPane().setDividerSize(
				Constants.EDITOR_SPLIT_DIVIDERSIZE_MIN);
		customUnitButton.setToolTipText(ResourcesManager.bundle
				.getString("nci_cusomunit_tooltip_show"));
	}
	
	public void showPlatte(){
		customUnitButton.setSelected(false);
		editor.getSplitPane().getLeftComponent().setVisible(true);
		editor.getSplitPane().setDividerLocation(
				Constants.EDITOR_SPLIT_DIVIDERLOCATION);
		editor.getSplitPane().setDividerSize(
				EDITOR_SPLIT_DIVIDERSIZE_NORMAL);
		customUnitButton.setToolTipText(ResourcesManager.bundle
				.getString("nci_cusomunit_tooltip_hide"));
	}
	
	public String getName(){
		return customUnitId;
	}

	public JToggleButton getCustomUnitButton() {
		return customUnitButton;
	}
}
