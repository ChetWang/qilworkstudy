package com.nci.svg.util;

import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;
import javax.swing.border.BevelBorder;

import com.nci.svg.ui.graphunit.NCIThumbnailPanel;

public class NCISymbolSession {
	
	private NCIThumbnailPanel SELECTED_THUMBNAIL = null;
	
	private ButtonGroup bg = null;
	
	public NCISymbolSession(){}
	
	/**
	 * 设置选择的缩略图元
	 * @param thumbnail
	 */
	public void setSelectedThumbnail(NCIThumbnailPanel thumbnail){
		if(thumbnail == null){
			throw new NullPointerException("Thumbnail could not be null!");
		}
		if(SELECTED_THUMBNAIL!=null)
			SELECTED_THUMBNAIL.setBorder(new BevelBorder(BevelBorder.RAISED));
		SELECTED_THUMBNAIL = thumbnail;
		SELECTED_THUMBNAIL.setBorder(new BevelBorder(BevelBorder.LOWERED));
		
		Enumeration<AbstractButton> enu = bg.getElements();
		while(enu.hasMoreElements()){
			JToggleButton jtBtn = (JToggleButton)enu.nextElement();
			if(bg.isSelected(jtBtn.getModel())){
				bg.remove(jtBtn);
				jtBtn.setSelected(false);
                bg.add(jtBtn);

			}
		}
	}
	
	/**
	 * 清空选择的缩略图元
	 */
	public void clearSelectedThumbnail(){
		if(SELECTED_THUMBNAIL!=null){
			SELECTED_THUMBNAIL.setBorder(new BevelBorder(BevelBorder.RAISED));
		}
		SELECTED_THUMBNAIL = null;
	}
	
	/**
	 * 获取选择的缩略图元
	 * @return 选择的缩略图元
	 */
	public NCIThumbnailPanel getSelectedThumbnail(){
		return SELECTED_THUMBNAIL;
	}
	/**
	 * 设置工具条的选择及绘制ButtonGroup工具组
	 * @param bg1
	 */
	public void setToolbarGraphUnitButtonGroup(ButtonGroup bg1){
		bg = bg1;
	}
	
	/**
	 * 返回工具条的第一组图元ButtonGroup
	 * @return
	 */
	public ButtonGroup getToolBarButtonGroup(){
		return bg;
	}

}
