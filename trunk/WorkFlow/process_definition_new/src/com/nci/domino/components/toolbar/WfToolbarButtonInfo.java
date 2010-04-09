package com.nci.domino.components.toolbar;

import java.awt.event.ActionListener;

/**
 * 工具条组件信息对象
 * @author Qil.Wong
 *
 */
public class WfToolbarButtonInfo {

	//组件名称
	private String actionCommand;
	//组件显示的文本
	private String text;
	//组件的提示信息
	private String tooltip;
	//组件对应的图标
	private String icon;
	
	private ActionListener actionListener;
	
	public WfToolbarButtonInfo(){
		
	}

	public String getActionCommand() {
		return actionCommand;
	}

	public void setActionCommand(String actionCommand) {
		this.actionCommand = actionCommand;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public ActionListener getActionListener() {
		return actionListener;
	}

	public void setActionListener(ActionListener actionListener) {
		this.actionListener = actionListener;
	}
	
	
}
