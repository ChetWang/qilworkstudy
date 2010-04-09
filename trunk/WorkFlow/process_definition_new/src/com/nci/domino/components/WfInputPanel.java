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
	 *  ��ȡ�����tab����ʾ������
	 * @return
	 */
	public String getPanelName() {
		return panelName;
	}
	
	/**
	 * ���������tab����ʾ������
	 * @param panelName
	 */
	public void setPanelName(String panelName){
		this.panelName = panelName;
	}

	/**
	 * �Ϸ���У�飬�޴��󷵻�null
	 * @return
	 */
	public String check() {
		return null;
	}

	/**
	 * ��������ϵ�ֵ
	 */
	public abstract void reset();

	/**
	 * �������ֵ��ʾ�������
	 * 
	 * @param value
	 */
	public abstract void setValues(Serializable value);

	/**
	 * ����һ�����󣬽���ǰ����ϵ�ֵ������ö���; ����ʱ�����Ķ����޷�������Ҫ�����Է���һ���������ⲿ�������¸�ֵ
	 * 
	 * @param value
	 */
	public abstract Serializable applyValues(Serializable value);

}
