package com.nci.domino.components.event;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;

import com.jidesoft.popup.JidePopup;
import com.nci.domino.WfEditor;
import com.nci.domino.beans.desyer.event.WofoEventSQLHandlerBean;
import com.nci.domino.components.WfBannerPanel;
import com.nci.domino.components.WfComboBox;
import com.nci.domino.components.WfOverlayableTextField;
import com.nci.domino.components.WfTextDocument;
import com.nci.domino.components.WfMessagePanel.WfSplitBtn;
import com.nci.domino.components.dialog.WfDialog;
import com.nci.domino.components.dialog.WfDialogPluginPanel;
import com.nci.domino.help.Functions;

/**
 * <p>
 * 标题：WfSqlEventDialog.java
 * </p>
 * <p>
 * 描述： SQL脚本事件定义对话窗口类
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
public class WfSqlEventDialog extends WfDialog {
	private static final long serialVersionUID = -7440441782645898738L;
	private WfSqlEventPanel contentPanel;
	private WfDialog parentDialog;

	public WfSqlEventDialog(WfEditor editor, String title, boolean modal,
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
		WfBannerPanel headerPanel1 = new WfBannerPanel("SQL脚本事件定义",
				"用来定义SQL脚本事件", Functions.getImageIcon("new_process_set.gif"));
		return headerPanel1.getGlassBanner();
	}

	@Override
	public JComponent createContentPanel() {
		contentPanel = new WfSqlEventPanel(this);
		// customPanels.add(contentPanel);
		return contentPanel;
	}

	private class WfSqlEventPanel extends WfDialogPluginPanel {

		private static final long serialVersionUID = 4198682312981894993L;
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
		 * SQL脚本输入框
		 */
		private JTextArea sqlTextArea;
		private JScrollPane sqlScrollPane;
		/**
		 * 参数操作按钮
		 */
		private JButton paramBtn;
		protected JidePopup argSelectPopup;

		public WfSqlEventPanel(WfDialog dialog) {
			super(dialog);
			panelName = "Sql脚本事件定义";
			initComponents();
			initDatas();
		}

		/**
		 * @功能 初始化数据
		 * 
		 * @Add by ZHM 2010-3-29
		 */
		@SuppressWarnings("unchecked")
		private void initDatas() {
			// // 获取缓存中的事件分类
			// categoryBeans = (ArrayList<WofoEventCategoryBean>) editor
			// .getCache().nowaitWhileNullGet(
			// WofoActions.GET_EVENT_CATEGORY);
			// if (categoryBeans != null) {
			// // 添加到事件分类列表中
			// DefaultComboBoxModel model = (DefaultComboBoxModel) typeComboBox
			// .getModel();
			// for (WofoEventCategoryBean bean : categoryBeans) {
			// // 通过model实现数据变动
			// model.addElement(bean);
			// }
			// }
		}

		/**
		 * @功能 初始化界面
		 * 
		 * @Add by ZHM 2010-3-29
		 */
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
			WfTextDocument codeDoc = new WfTextDocument(64);
			codeDoc.setWfDialog(dialog);
			nameTextField = new WfOverlayableTextField(codeDoc);
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
			gridBagConstraints.gridheight = 3;
			add(remarkScrollPane, gridBagConstraints);

			rowCount += 3;
			// gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = rowCount;
			gridBagConstraints.weightx = 0;
			gridBagConstraints.weighty = 0;
			gridBagConstraints.gridheight = 1;
			add(new JLabel("SQL脚本："), gridBagConstraints);
			sqlTextArea = new JTextArea();
			sqlTextArea.setAutoscrolls(true);
			sqlTextArea.setLineWrap(true);
			sqlScrollPane = new JScrollPane();
			sqlScrollPane.setViewportView(sqlTextArea);
			gridBagConstraints.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = rowCount;
			gridBagConstraints.weightx = 1;
			gridBagConstraints.weighty = 1;
			gridBagConstraints.gridheight = 3;
			gridBagConstraints.gridwidth = 1;
			add(sqlScrollPane, gridBagConstraints);
			gridBagConstraints.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints.gridx = 2;
			gridBagConstraints.gridy = rowCount;
			gridBagConstraints.weightx = 0;
			gridBagConstraints.weighty = 0;
			gridBagConstraints.gridwidth = 1;
			gridBagConstraints.gridheight = 1;
			paramBtn = new WfSplitBtn("参数");
			combineArgsTextComp(paramBtn, sqlTextArea);
			add(paramBtn, gridBagConstraints);
		}

		/**
		 * @功能 将参数选择按钮和文本对象组件关联，从参数中选择的参数将自动安装某类格式添加到文本组件中
		 * @param btn
		 * @param textComp
		 * 
		 * @Add by ZHM 2010-3-31
		 */
		private void combineArgsTextComp(final AbstractButton btn,
				final JTextComponent textComp) {
			btn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					argSelectPopup = new JidePopup();
					WfPopupParamsPanel pPanel = new WfPopupParamsPanel(
							argSelectPopup, textComp, true, parentDialog);
					argSelectPopup.setPreferredSize(new Dimension(150, 200));
					argSelectPopup.getContentPane().setLayout(
							new BorderLayout());
					argSelectPopup.getContentPane().add(pPanel,
							BorderLayout.CENTER);
					argSelectPopup.showPopup(btn);
				}
			});
		}

		@Override
		public Serializable applyValues(Serializable value) {
			// WfComboBean
			WofoEventSQLHandlerBean bean = (WofoEventSQLHandlerBean) value;
			// bean.setEventCat(((WfComboBean)typeComboBox.getSelectedItem()).id);
			bean.setEventName(nameTextField.getText());
			bean.setDescription(remarkTextArea.getText());
			bean.setSqlText(sqlTextArea.getText());
			return bean;
		}

		@Override
		public void reset() {
			// typeComboBox.setSelectedIndex(0);
			nameTextField.setText("");
			remarkTextArea.setText("");
			sqlTextArea.setText("");
		}

		@Override
		public void setValues(Serializable value) {
			WofoEventSQLHandlerBean bean = (WofoEventSQLHandlerBean) value;
			// typeComboBox.setSelectedItemByID(bean.getEventCat());
			nameTextField.setText(bean.getEventName());
			remarkTextArea.setText(bean.getDescription());
			sqlTextArea.setText(bean.getSqlText());
		}
	}
}
