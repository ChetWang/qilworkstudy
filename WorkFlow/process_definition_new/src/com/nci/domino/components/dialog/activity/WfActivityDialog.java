package com.nci.domino.components.dialog.activity;

import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTabbedPane;

import com.nci.domino.PaintBoard;
import com.nci.domino.WfEditor;
import com.nci.domino.beans.desyer.WofoActivityBean;
import com.nci.domino.beans.desyer.WofoConditionBean;
import com.nci.domino.components.WfArgumentsPanel;
import com.nci.domino.components.WfComboBox;
import com.nci.domino.components.WfComboBox.WfComboBean;
import com.nci.domino.components.dialog.IllegalInputTypeException;
import com.nci.domino.components.dialog.WfDialog;

public abstract class WfActivityDialog extends WfDialog {

	protected WofoActivityBean activity = null;

	protected List<WfComboBox> conditionCombos = new ArrayList<WfComboBox>();

	protected WfArgumentsPanel argumentsPanel;

	public WfActivityDialog(WfEditor editor, String title, boolean modal) {
		super(editor, title, modal);
		argumentsPanel = new WfActivityArgumentsPanel(this);
	}

	/**
	 * 一旦确定后，不管是否修改过，都认为编辑区已编辑
	 */
	public void okActionPerformed(ActionEvent e) {
		PaintBoard board = editor.getOperationArea().getCurrentPaintBoard();
		if (board != null) {
			board.setEdited(true);
		}
		
	}

	public void clearContents() {
		super.clearContents();
		activity = null;
		defalutValue = null;
	}

	public void initCustomComponents() {
		if (needArguments()) {
			customPanels.add(argumentsPanel);
		}
	}

	/**
	 * 检测传入对象类型的准确性，只检测对象的class是否符合当前对话框的需要
	 * 
	 * @param value
	 * @return
	 */
	public boolean checkValueClassIllegal(Serializable value) {
		if (value instanceof WofoActivityBean == false) {
			new IllegalInputTypeException(WofoActivityBean.class, value
					.getClass()).printStackTrace();
		}
		return value instanceof WofoActivityBean;
	}

	public void setInputValues(Serializable value) {
		super.setInputValues(value);
		if (checkValueClassIllegal(value)) {
			activity = (WofoActivityBean) value;
			setInputValues(activity);
		}
	}

	public void showWfDialog(int wdith, int height, Serializable defaultValue) {
		initConditions();
		super.showWfDialog(wdith, height, defaultValue);
	}

	/**
	 * 初始化条件值
	 * 
	 * @param conditionCombo
	 */
	protected void initConditions() {
		List conditions = editor.getOperationArea().getConditionArea()
				.getValues();
		List<WfComboBean> conditionComboBeans = new ArrayList<WfComboBean>();
		for (int i = 0; i < conditions.size(); i++) {
			WofoConditionBean c = (WofoConditionBean) conditions.get(i);
			WfComboBean cb = new WfComboBean(c.getConditionId().toString(), c
					.getConditionName());
			conditionComboBeans.add(cb);
		}
		for (WfComboBox c : conditionCombos) {
			c.resetElements(conditionComboBeans, true);
		}
	}

	public abstract void setInputValues(WofoActivityBean bean);

	/**
	 * 判断该活动是否需要参数
	 * 
	 * @return
	 */
	public boolean needArguments() {
		return true;
	}

	public WfArgumentsPanel getArgumentsPanel() {
		return argumentsPanel;
	}
}
