package com.nci.domino.components;

import java.awt.LayoutManager;
import java.io.Serializable;

import javax.swing.JPanel;

public abstract class WfInputPanel extends JPanel {
	
	protected String panelName;

	public WfInputPanel() {
		super();
	}

	public WfInputPanel(LayoutManager layout) {
		super(layout);
	}

	/**
	 *  获取面板在tab上显示的名称
	 * @return
	 */
	public String getPanelName() {
		return panelName;
	}
	
	/**
	 * 设置面板在tab上显示的名称
	 * @param panelName
	 */
	public void setPanelName(String panelName){
		this.panelName = panelName;
	}

	/**
	 * 合法性校验，无错误返回null
	 * @return
	 */
	public String check() {
		return null;
	}

	/**
	 * 重置面板上的值
	 */
	public abstract void reset();

	/**
	 * 将传入的值显示在面板上
	 * 
	 * @param value
	 */
	public abstract void setValues(Serializable value);

	/**
	 * 给定一个对象，将当前面板上的值都赋予该对象; 但有时给定的对象无法满足需要，可以返回一个对象在外部进行重新赋值
	 * 
	 * @param value
	 */
	public abstract Serializable applyValues(Serializable value);

}
