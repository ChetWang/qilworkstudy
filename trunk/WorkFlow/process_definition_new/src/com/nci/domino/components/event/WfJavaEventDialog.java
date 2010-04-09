package com.nci.domino.components.event;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.jidesoft.popup.JidePopup;
import com.jidesoft.swing.PartialGradientLineBorder;
import com.jidesoft.swing.PartialSide;
import com.jidesoft.swing.TitledSeparator;
import com.nci.domino.GlobalConstants;
import com.nci.domino.WfEditor;
import com.nci.domino.beans.desyer.event.WofoEventJavaHandlerBean;
import com.nci.domino.components.WfBannerPanel;
import com.nci.domino.components.WfComboBox;
import com.nci.domino.components.WfOperControllerButtonPanel;
import com.nci.domino.components.WfOverlayableTextField;
import com.nci.domino.components.WfTextDocument;
import com.nci.domino.components.dialog.WfDialog;
import com.nci.domino.components.dialog.WfDialogPluginPanel;
import com.nci.domino.help.Functions;

/**
 * <p>
 * 标题：WfJavaEventDialog.java
 * </p>
 * <p>
 * 描述： Java程序事件定义窗口类
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2010-3-26
 * @version 1.0
 */
public class WfJavaEventDialog extends WfDialog {
	private static final long serialVersionUID = 1716421774232021895L;
	private WfJavaEventPanel contentPanel;
	private WfDialog parentDialog;

	public WfJavaEventDialog(WfEditor editor, String title, boolean modal,
			WfDialog parentDialog) {
		super(editor, title, modal);
		this.parentDialog = parentDialog;
		defaultWidth = 450;
		defaultHeight = 380;
	}

	protected String checkInput() {
		if (this.contentPanel.nameTextField.getText().length() <= 0) {
			return "事件名称不可以为空！";
		}
		if (this.contentPanel.javaTextField.getText().length() <= 0) {
			return "Java类名不可以为空！";
		}
		if (this.contentPanel.methodTextField.getText().length() <= 0) {
			return "调用方法不可以为空！";
		}
		return super.checkInput();
	}

	public Serializable getInputValues() {
		Serializable result = super.getInputValues();
		return contentPanel.applyValues(result);
	}

	public void clearContents() {
		super.clearContents();
		contentPanel.reset();
	}

	public void setInputValues(Serializable value) {
		super.setInputValues(value);
		contentPanel.setValues(value);
	}

	@Override
	public JComponent createBannerPanel() {
		WfBannerPanel headerPanel1 = new WfBannerPanel("Java程序事件定义",
				"用来定义Java程序事件", Functions.getImageIcon("new_process_set.gif"));
		return headerPanel1.getGlassBanner();
	}

	@Override
	public JComponent createContentPanel() {
		contentPanel = new WfJavaEventPanel(this);
		// customPanels.add(contentPanel);
		return contentPanel;
	}

	private class WfJavaEventPanel extends WfDialogPluginPanel {
		private static final long serialVersionUID = -4821520098245293066L;
		// /**
		// * 事件分类选择框
		// */
		// private WfComboBox typeComboBox;
		// /**
		// * 事件分类新增按钮
		// */
		// private JButton typeAddBtn;
		/**
		 * 事件名称输入框
		 */
		private WfOverlayableTextField nameTextField;
		/**
		 * 监听类型选择框
		 */
		private WfComboBox monitorTypeCombobox;
		/**
		 * 执行选择组
		 */
		private ButtonGroup excuteGrop;
		/**
		 * 同步执行
		 */
		private JRadioButton syncTypeRadio;
		/**
		 * 异步执行
		 */
		private JRadioButton asyncTypeRadio;
		/**
		 * 备注输入框
		 */
		private JTextArea remarkTextArea;
		private JScrollPane remarkScrollPane;
		/**
		 * Java类输入框
		 */
		private WfOverlayableTextField javaTextField;
		/**
		 * 调用方法输入框
		 */
		private WfOverlayableTextField methodTextField;
		/**
		 * 参数列表框
		 */
		private JList paramsList;
		private JScrollPane paramsScrollPane;
		/**
		 * 参数操作按钮
		 */
		private WfOperControllerButtonPanel paramsBtns;
		/**
		 * 参数弹出菜单
		 */
		protected JidePopup argSelectPopup;

		public WfJavaEventPanel(WfDialog dialog) {
			super(dialog);
			panelName = "Java程序事件定义";
			initComponents();
		}

		private void initComponents() {
			int rowCount = 0;
			setLayout(new GridBagLayout());
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;

			// gridBagConstraints.insets = new Insets(3, 3, 3, 3);
			// gridBagConstraints.gridx = 0;
			// gridBagConstraints.gridy = rowCount;
			// add(new JLabel("事件分类："), gridBagConstraints);
			// typeComboBox = new WfComboBox();
			// gridBagConstraints.insets = new Insets(3, 3, 3, 3);
			// gridBagConstraints.gridx = 1;
			// gridBagConstraints.gridy = rowCount;
			// gridBagConstraints.weightx = 1;
			// add(typeComboBox, gridBagConstraints);
			// typeAddBtn = new JButton("新增");
			// gridBagConstraints.insets = new Insets(3, 3, 3, 3);
			// gridBagConstraints.gridx = 2;
			// gridBagConstraints.gridy = rowCount;
			// gridBagConstraints.weightx = 0;
			// add(typeAddBtn, gridBagConstraints);

			rowCount++;
			gridBagConstraints.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = rowCount;
			gridBagConstraints.weightx = 0;
			add(new JLabel("事件名称："), gridBagConstraints);
			WfTextDocument nameCodeDoc = new WfTextDocument(64);
			nameCodeDoc.setWfDialog(dialog);
			nameTextField = new WfOverlayableTextField(nameCodeDoc);
			gridBagConstraints.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = rowCount;
			gridBagConstraints.weightx = 1;
			add(nameTextField.getOverlayableTextField(), gridBagConstraints);

			rowCount++;
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = rowCount;
			gridBagConstraints.weightx = 0;
			gridBagConstraints.gridwidth = 1;
			add(new JLabel("监听类型："), gridBagConstraints);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = rowCount;
			gridBagConstraints.weightx = 1;
			gridBagConstraints.gridwidth = 2;
			monitorTypeCombobox = new WfComboBox();
			add(monitorTypeCombobox, gridBagConstraints);

			rowCount++;
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = rowCount;
			gridBagConstraints.weightx = 0;
			gridBagConstraints.gridwidth = 1;
			add(new JLabel("执行方式："), gridBagConstraints);
			excuteGrop = new ButtonGroup();
			syncTypeRadio = new JRadioButton("同步");
			asyncTypeRadio = new JRadioButton("异步");
			excuteGrop.add(syncTypeRadio);
			excuteGrop.add(asyncTypeRadio);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = rowCount;
			gridBagConstraints.gridwidth = 1;
			gridBagConstraints.weightx = 0;
			asyncTypeRadio.setSelected(true);
			add(asyncTypeRadio, gridBagConstraints);
			gridBagConstraints.gridx = 2;
			gridBagConstraints.gridy = 2;
			gridBagConstraints.gridwidth = 1;
			add(syncTypeRadio, gridBagConstraints);

			rowCount++;
			gridBagConstraints.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = rowCount;
			gridBagConstraints.weightx = 0;
			add(new JLabel("备注："), gridBagConstraints);
			remarkTextArea = new JTextArea();
			remarkTextArea.setAutoscrolls(true);
			remarkTextArea.setLineWrap(true);
			remarkScrollPane = new JScrollPane();
			remarkScrollPane.setViewportView(remarkTextArea);
			gridBagConstraints.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = rowCount;
			gridBagConstraints.weighty = 0.3;
			gridBagConstraints.weightx = 1;
			gridBagConstraints.gridwidth = 1;
			gridBagConstraints.gridheight = 2;
			add(remarkScrollPane, gridBagConstraints);

			rowCount += 2;
			gridBagConstraints.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = rowCount;
			gridBagConstraints.weightx = 0;
			gridBagConstraints.weighty = 0;
			gridBagConstraints.gridheight = 1;
			add(new JLabel("Java类："), gridBagConstraints);
			WfTextDocument javaCodeDoc = new WfTextDocument(256);
			javaCodeDoc.setWfDialog(dialog);
			javaTextField = new WfOverlayableTextField(javaCodeDoc);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = rowCount;
			gridBagConstraints.weightx = 0;
			gridBagConstraints.weighty = 0;
			gridBagConstraints.gridheight = 1;
			add(javaTextField.getOverlayableTextField(), gridBagConstraints);

			rowCount++;
			gridBagConstraints.insets = new Insets(3, 3, 1, 3);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = rowCount;
			gridBagConstraints.weightx = 0;
			gridBagConstraints.weighty = 0;
			gridBagConstraints.gridheight = 1;
			add(new JLabel("调用方法："), gridBagConstraints);
			WfTextDocument methodCodeDoc = new WfTextDocument(256);
			methodCodeDoc.setWfDialog(dialog);
			methodTextField = new WfOverlayableTextField(methodCodeDoc);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = rowCount;
			gridBagConstraints.weightx = 0;
			gridBagConstraints.weighty = 0;
			gridBagConstraints.gridheight = 1;
			add(methodTextField.getOverlayableTextField(), gridBagConstraints);

			rowCount++;
			TitledSeparator businessSeparator = new TitledSeparator(
					new JLabel(), new PartialGradientLineBorder(new Color[] {
							Color.LIGHT_GRAY, Color.white }, 1,
							PartialSide.SOUTH),
					GlobalConstants.LABEL_ALIGH_POSITION);
			gridBagConstraints.insets = new Insets(3, 1, 1, 1);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = rowCount;
			gridBagConstraints.weightx = 0;
			gridBagConstraints.gridwidth = 4;
			add(businessSeparator, gridBagConstraints);

			rowCount++;
			paramsList = new JList(new DefaultListModel());
			paramsBtns = new WfOperControllerButtonPanel(paramsList, false);
			gridBagConstraints.insets = new Insets(1, 1, 1, 1);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = rowCount;
			gridBagConstraints.weightx = 1;
			gridBagConstraints.weighty = 0;
			gridBagConstraints.gridwidth = 1;
			gridBagConstraints.gridheight = 1;
			add(paramsBtns, gridBagConstraints);

			rowCount++;
			gridBagConstraints.insets = new Insets(1, 3, 3, 3);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = rowCount;
			gridBagConstraints.weightx = 0;
			gridBagConstraints.weighty = 0;
			gridBagConstraints.gridwidth = 1;
			gridBagConstraints.gridheight = 1;
			add(new JLabel("传入参数："), gridBagConstraints);
			paramsScrollPane = new JScrollPane(paramsList,
					JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			gridBagConstraints.insets = new Insets(1, 3, 3, 3);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = rowCount;
			gridBagConstraints.weightx = 1;
			gridBagConstraints.weighty = 1;
			gridBagConstraints.gridheight = 3;
			gridBagConstraints.gridwidth = 1;
			add(paramsScrollPane, gridBagConstraints);

			paramsBtns.removeDefaultListeners(paramsBtns.getAddRowBtn());
			paramsBtns.getAddRowBtn().addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					argSelectPopup = new JidePopup();
					WfPopupParamsPanel pPanel = new WfPopupParamsPanel(
							argSelectPopup, paramsList, true, parentDialog);
					argSelectPopup.setPreferredSize(new Dimension(150, 200));
					argSelectPopup.getContentPane().setLayout(
							new BorderLayout());
					argSelectPopup.getContentPane().add(pPanel,
							BorderLayout.CENTER);
					argSelectPopup.showPopup(paramsBtns);
				}
			});
		}

		@Override
		public Serializable applyValues(Serializable value) {
			WofoEventJavaHandlerBean bean = (WofoEventJavaHandlerBean) value;
			// bean.setEventCat(((WfComboBean)
			// typeComboBox.getSelectedItem()).id);
			bean.setEventName(nameTextField.getText());
			bean.setDescription(remarkTextArea.getText());
			bean.setClassName(javaTextField.getText());
			bean.setMethodName(methodTextField.getText());

			ArrayList<Object> paramList = new ArrayList<Object>();
			DefaultListModel listModel = (DefaultListModel) paramsList
					.getModel();
			for (int i = 0; i < listModel.getSize(); i++) {
				paramList.add(listModel.get(i));
			}
			bean.setParams(paramList);
			return bean;
		}

		@Override
		public void reset() {
			// typeComboBox.setSelectedIndex(0);
			nameTextField.setText("");
			remarkTextArea.setText("");
			javaTextField.setText("");
			methodTextField.setText("");

			DefaultListModel listModel = (DefaultListModel) paramsList
					.getModel();
			listModel.clear();
		}

		@Override
		@SuppressWarnings("unchecked")
		public void setValues(Serializable value) {
			WofoEventJavaHandlerBean bean = (WofoEventJavaHandlerBean) value;
			// typeComboBox.setSelectedItemByID(bean.getEventCat());
			nameTextField.setText(bean.getEventName());
			remarkTextArea.setText(bean.getDescription());
			javaTextField.setText(bean.getClassName());
			methodTextField.setText(bean.getMethodName());

			ArrayList<Object> paramList = (ArrayList<Object>) bean.getParams();
			if (paramList != null && paramList.size() > 0) {
				DefaultListModel listModel = (DefaultListModel) paramsList
						.getModel();
				listModel.clear();
				for (Object o : paramList) {
					listModel.addElement(o);
				}
			}
		}
	}
}
