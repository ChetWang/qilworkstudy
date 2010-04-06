package com.nci.svg.sdk.client.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;
import javax.swing.border.BevelBorder;

import com.jidesoft.swing.JideSplitButton;
import com.nci.svg.sdk.ui.graphunit.NCIThumbnailPanel;

public class NCISymbolSession {

	private NCIThumbnailPanel selectedThumbnail = null;

	private ButtonGroup bg = null;
	
	private String[] pathDirectDrawNames = {"导线"};

	public NCISymbolSession() {
	}
	
	/**
	 * 是否是直接画线，而不是图元
	 * @param symbolName
	 * @return
	 */
	public boolean isDirectDrawPath(String symbolName){
		for(String s:pathDirectDrawNames){
			if(symbolName.equals(s)){
				return true;
			}
		}
		return false;
	}

	/**
	 * 设置选择的缩略图元
	 * 
	 * @param thumbnail
	 */
	public void setSelectedThumbnail(NCIThumbnailPanel thumbnail) {
		clearSelectedBtnGroup();
		if (thumbnail == null) {
			throw new NullPointerException("Thumbnail could not be null!");
		}
		if (selectedThumbnail != null)
			selectedThumbnail.setBorder(new BevelBorder(BevelBorder.RAISED));
		selectedThumbnail = thumbnail;
		selectedThumbnail.setBorder(new BevelBorder(BevelBorder.LOWERED));
	}

	/**
	 * 清空当前选择的操作按钮
	 */
	public void clearSelectedBtnGroup() {
		Enumeration<AbstractButton> enu = bg.getElements();
		while (enu.hasMoreElements()) {
			AbstractButton jtBtn = enu.nextElement();
			if (bg.isSelected(jtBtn.getModel())) {
				bg.remove(jtBtn);
				jtBtn.setSelected(false);
				bg.add(jtBtn);
			}
			if (jtBtn instanceof JideSplitButton) {
				((JideSplitButton) jtBtn).setButtonSelected(false);
			}
		}
		clearSelectedThumbnail();
	}

	/**
	 * 清空选择的缩略图元
	 */
	public void clearSelectedThumbnail() {
		if (selectedThumbnail != null) {
			selectedThumbnail.setBorder(new BevelBorder(BevelBorder.RAISED));
		}
		selectedThumbnail = null;
	}

	/**
	 * 获取选择的缩略图元
	 * 
	 * @return 选择的缩略图元
	 */
	public NCIThumbnailPanel getSelectedThumbnail() {
		return selectedThumbnail;
	}

	/**
	 * 设置工具条的选择及绘制ButtonGroup工具组
	 * 
	 * @param bg1
	 */
	public void setToolbarGraphUnitButtonGroup(ButtonGroup bg1) {
		bg = bg1;
		/**
		 * 是否已经将图元和绘图按钮建立关联，一旦有关联，则工具栏绘图按钮点击后，图元将处于不选择状态
		 */
		Enumeration<AbstractButton> enu = bg.getElements();
		while (enu.hasMoreElements()) {
			final AbstractButton jtBtn = enu.nextElement();
			if (jtBtn instanceof JideSplitButton == false) {
				jtBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {

						clearSelectedThumbnail();
						Enumeration<AbstractButton> enu2 = bg.getElements();
						while (enu2.hasMoreElements()) {
							AbstractButton jtBtn2 = enu2.nextElement();
							if (jtBtn2 instanceof JideSplitButton
									&& jtBtn != jtBtn2) {
								((JideSplitButton) jtBtn2)
										.setButtonSelected(false);
							}
						}
					}
				});
			}
		}
	}

	/**
	 * 返回工具条的第一组图元ButtonGroup
	 * 
	 * @return
	 */
	public ButtonGroup getToolBarButtonGroup() {
		return bg;
	}

}
