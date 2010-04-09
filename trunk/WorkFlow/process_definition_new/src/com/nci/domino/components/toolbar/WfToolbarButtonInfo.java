package com.nci.domino.components.toolbar;

import java.awt.event.ActionListener;

/**
 * �����������Ϣ����
 * @author Qil.Wong
 *
 */
public class WfToolbarButtonInfo {

	//�������
	private String actionCommand;
	//�����ʾ���ı�
	private String text;
	//�������ʾ��Ϣ
	private String tooltip;
	//�����Ӧ��ͼ��
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
