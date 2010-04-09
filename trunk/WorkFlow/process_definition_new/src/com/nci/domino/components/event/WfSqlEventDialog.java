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
 * ���⣺WfSqlEventDialog.java
 * </p>
 * <p>
 * ������ SQL�ű��¼�����Ի�������
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2010-3-26
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
			return "�¼����Ʋ�����Ϊ�գ�";
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
		WfBannerPanel headerPanel1 = new WfBannerPanel("SQL�ű��¼�����",
				"��������SQL�ű��¼�", Functions.getImageIcon("new_process_set.gif"));
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
		 * �¼����������
		 */
		private WfOverlayableTextField nameTextField;
		/**
		 * ��������ѡ���
		 */
		private WfComboBox monitorTypeCombobox;
		/**
		 * ִ��ѡ����
		 */
		private ButtonGroup excuteGrop;
		/**
		 * ͬ��ִ��
		 */
		private JRadioButton syncTypeRadio;
		/**
		 * �첽ִ��
		 */
		private JRadioButton asyncTypeRadio;
		/**
		 * ��ע�����
		 */
		private JTextArea remarkTextArea;
		private JScrollPane remarkScrollPane;
		/**
		 * SQL�ű������
		 */
		private JTextArea sqlTextArea;
		private JScrollPane sqlScrollPane;
		/**
		 * ����������ť
		 */
		private JButton paramBtn;
		protected JidePopup argSelectPopup;

		public WfSqlEventPanel(WfDialog dialog) {
			super(dialog);
			panelName = "Sql�ű��¼�����";
			initComponents();
			initDatas();
		}

		/**
		 * @���� ��ʼ������
		 * 
		 * @Add by ZHM 2010-3-29
		 */
		@SuppressWarnings("unchecked")
		private void initDatas() {
			// // ��ȡ�����е��¼�����
			// categoryBeans = (ArrayList<WofoEventCategoryBean>) editor
			// .getCache().nowaitWhileNullGet(
			// WofoActions.GET_EVENT_CATEGORY);
			// if (categoryBeans != null) {
			// // ��ӵ��¼������б���
			// DefaultComboBoxModel model = (DefaultComboBoxModel) typeComboBox
			// .getModel();
			// for (WofoEventCategoryBean bean : categoryBeans) {
			// // ͨ��modelʵ�����ݱ䶯
			// model.addElement(bean);
			// }
			// }
		}

		/**
		 * @���� ��ʼ������
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
			// add(new JLabel("�¼����ࣺ"), gridBagConstraints);
			// typeComboBox = new WfComboBox();
			// gridBagConstraints.insets = new Insets(3, 3, 3, 3);
			// gridBagConstraints.gridx = 1;
			// gridBagConstraints.gridy = rowCount;
			// gridBagConstraints.weightx = 1;
			// add(typeComboBox, gridBagConstraints);
			// typeAddBtn = new JButton("����");
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
			add(new JLabel("�¼����ƣ�"), gridBagConstraints);
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
			add(new JLabel("�������ͣ�"), gridBagConstraints);
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
			add(new JLabel("ִ�з�ʽ��"), gridBagConstraints);
			excuteGrop = new ButtonGroup();
			syncTypeRadio = new JRadioButton("ͬ��");
			asyncTypeRadio = new JRadioButton("�첽");
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
			add(new JLabel("��ע��"), gridBagConstraints);
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
			add(new JLabel("SQL�ű���"), gridBagConstraints);
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
			paramBtn = new WfSplitBtn("����");
			combineArgsTextComp(paramBtn, sqlTextArea);
			add(paramBtn, gridBagConstraints);
		}

		/**
		 * @���� ������ѡ��ť���ı���������������Ӳ�����ѡ��Ĳ������Զ���װĳ���ʽ��ӵ��ı������
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
