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
 * ���⣺WfJavaEventDialog.java
 * </p>
 * <p>
 * ������ Java�����¼����崰����
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
			return "�¼����Ʋ�����Ϊ�գ�";
		}
		if (this.contentPanel.javaTextField.getText().length() <= 0) {
			return "Java����������Ϊ�գ�";
		}
		if (this.contentPanel.methodTextField.getText().length() <= 0) {
			return "���÷���������Ϊ�գ�";
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
		WfBannerPanel headerPanel1 = new WfBannerPanel("Java�����¼�����",
				"��������Java�����¼�", Functions.getImageIcon("new_process_set.gif"));
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
		// * �¼�����ѡ���
		// */
		// private WfComboBox typeComboBox;
		// /**
		// * �¼�����������ť
		// */
		// private JButton typeAddBtn;
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
		 * Java�������
		 */
		private WfOverlayableTextField javaTextField;
		/**
		 * ���÷��������
		 */
		private WfOverlayableTextField methodTextField;
		/**
		 * �����б��
		 */
		private JList paramsList;
		private JScrollPane paramsScrollPane;
		/**
		 * ����������ť
		 */
		private WfOperControllerButtonPanel paramsBtns;
		/**
		 * ���������˵�
		 */
		protected JidePopup argSelectPopup;

		public WfJavaEventPanel(WfDialog dialog) {
			super(dialog);
			panelName = "Java�����¼�����";
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
			gridBagConstraints.gridheight = 2;
			add(remarkScrollPane, gridBagConstraints);

			rowCount += 2;
			gridBagConstraints.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = rowCount;
			gridBagConstraints.weightx = 0;
			gridBagConstraints.weighty = 0;
			gridBagConstraints.gridheight = 1;
			add(new JLabel("Java�ࣺ"), gridBagConstraints);
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
			add(new JLabel("���÷�����"), gridBagConstraints);
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
			add(new JLabel("���������"), gridBagConstraints);
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
