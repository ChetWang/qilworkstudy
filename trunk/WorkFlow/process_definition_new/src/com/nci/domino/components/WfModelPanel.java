package com.nci.domino.components;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.Serializable;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.nci.domino.GlobalConstants;
import com.nci.domino.beans.desyer.WofoActivityBean;
import com.nci.domino.components.dialog.WfDialog;

/**
 * 模型基本面板
 * 
 * @author Qil.Wong
 * 
 */
public class WfModelPanel extends WfInputPanel {

	private JRadioButton functionModelRadio;

	private JLabel modelTypeLabel;

	private JTextField urlField;
	private JLabel urlLabel;
	private JLabel modelNameLabel;
	private JTextField modelNameField;
	private JRadioButton urlRadio;

	private WfDialog dialog;

	public WfModelPanel(WfDialog dialog) {
		this.dialog = dialog;
		init();
	}

	public void reset() {
		functionModelRadio.setSelected(true);
		urlRadio.setSelected(false);
		urlField.setText("");
		modelNameField.setText("");
	}

	/**
	 * 初始化
	 */
	private void init() {
		JPanel north = new JPanel(new GridBagLayout());
		setLayout(new BorderLayout());
		add(north, BorderLayout.NORTH);
		modelTypeLabel = new JLabel("模块类型：");
		functionModelRadio = new JRadioButton("功能模型");
		urlRadio = new JRadioButton("URL");
		urlLabel = new JLabel("入口地址：");
		urlField = new JTextField();
		WfTextDocument urlDoc = new WfTextDocument(250);
		urlDoc.setWfDialog(dialog);
		WfTextDocument modelNameDoc = new WfTextDocument(38);
		modelNameDoc.setWfDialog(dialog);
		modelNameLabel = new JLabel("模块名称：");
		modelNameField = new JTextField();
		urlField.setDocument(urlDoc);
		modelNameField.setDocument(modelNameDoc);

		modelTypeLabel
				.setHorizontalTextPosition(GlobalConstants.LABEL_ALIGH_POSITION);
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;

		gridBagConstraints.insets = new Insets(3, 5, 0, 0);

		north.add(modelTypeLabel, gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(5, 0, 0, 5);
		north.add(functionModelRadio, gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(5, 0, 0, 5);
		gridBagConstraints.weighty = 0;
		north.add(urlRadio, gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(3, 5, 0, 0);
		gridBagConstraints.weighty = 0;
		north.add(modelNameLabel, gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(5, 0, 0, 5);
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		north.add(modelNameField, gridBagConstraints);

		urlLabel
				.setHorizontalTextPosition(GlobalConstants.LABEL_ALIGH_POSITION);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(0, 5, 0, 0);
		north.add(urlLabel, gridBagConstraints);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(5, 0, 5, 5);
		north.add(urlField, gridBagConstraints);

		ButtonGroup bg = new ButtonGroup();
		bg.add(urlRadio);
		bg.add(functionModelRadio);

		functionModelRadio.setSelected(true);
	}

	/**
	 * 获取模型名称
	 * 
	 * @return
	 */
	public String getModelName() {
		return modelNameField.getText().trim();
	}

	/**
	 * 获取模型URL
	 * 
	 * @return
	 */
	public String getModelURL() {
		return urlField.getText().trim();
	}

	/**
	 * 获取模型类型
	 * 
	 * @return
	 */
	public String getModelType() {
		return functionModelRadio.isSelected() ? WofoActivityBean.APP_TYPE_JFW
				: WofoActivityBean.APP_TYPE_URL;
	}

	@Override
	public Serializable applyValues(Serializable value) {
		String[] name_type_urlArr = new String[3];
		name_type_urlArr[0] = this.getModelName();
		name_type_urlArr[1] = this.getModelType();
		name_type_urlArr[2] = this.getModelURL();

		return name_type_urlArr;
	}

	@Override
	public void setValues(Serializable name_type_url) {
		// 是一个按name，type，url排量的数组
		String[] name_type_urlArr = (String[]) name_type_url;
		this.functionModelRadio.setSelected(WofoActivityBean.APP_TYPE_JFW
				.equals(name_type_urlArr[1]));
		this.urlRadio.setSelected(WofoActivityBean.APP_TYPE_URL
				.equals(name_type_urlArr[1]));
		this.urlField.setText(name_type_urlArr[2]);
		this.modelNameField.setText(name_type_urlArr[0]);
	}

	public void setEnabled(boolean flag) {
		super.setEnabled(flag);
		functionModelRadio.setEnabled(flag);
		urlField.setEnabled(flag);
		modelNameField.setEnabled(flag);
		urlRadio.setEnabled(flag);
	}

}