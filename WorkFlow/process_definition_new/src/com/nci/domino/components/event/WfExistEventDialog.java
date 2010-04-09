package com.nci.domino.components.event;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.jidesoft.swing.PartialGradientLineBorder;
import com.jidesoft.swing.PartialSide;
import com.jidesoft.swing.TitledSeparator;
import com.nci.domino.GlobalConstants;
import com.nci.domino.WfEditor;
import com.nci.domino.components.WfBannerPanel;
import com.nci.domino.components.dialog.WfDialog;
import com.nci.domino.components.dialog.WfDialogPluginPanel;
import com.nci.domino.help.Functions;

/**
 * <p>
 * ���⣺WfExistEventDialog.java
 * </p>
 * <p>
 * ������ �����¼�ѡ�񴰿�
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2010-3-29
 * @version 1.0
 */
public class WfExistEventDialog extends WfDialog {
	private static final long serialVersionUID = -3419209052710906466L;
	private WfExistEventPanel contentPanel;

	public WfExistEventDialog(WfEditor editor, String title, boolean modal) {
		super(editor, title, modal);
		defaultWidth = 450;
		defaultHeight = 420;
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
		WfBannerPanel headerPanel1 = new WfBannerPanel("�����¼�ѡ��", "��ѯ��ѡ�������¼�",
				Functions.getImageIcon("new_process_set.gif"));
		return headerPanel1.getGlassBanner();
	}

	@Override
	public JComponent createContentPanel() {
		contentPanel = new WfExistEventPanel(this);
		return contentPanel;
	}

	private class WfExistEventPanel extends WfDialogPluginPanel {
		private static final long serialVersionUID = 5322273667986014450L;
		/**
		 * �¼�����ѡ���
		 */
		private JComboBox typeComboBox;
		/**
		 * �¼����������
		 */
		private JTextField nameTextField;
		/**
		 * ��ѯ��ť
		 */
		private JButton searchBt;
		/**
		 * �����б��
		 */
		private JList paramsList;
		private JScrollPane paramsScrollPane;

		public WfExistEventPanel(WfDialog dialog) {
			super(dialog);
			panelName = "�����¼�ѡ��";
			initComponents();
		}

		private void initComponents() {
			int rowCount = 0;
			setLayout(new GridBagLayout());
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;

			gridBagConstraints.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = rowCount;
			add(new JLabel("�¼����ͣ�"), gridBagConstraints);
			typeComboBox = new JComboBox();
			gridBagConstraints.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = rowCount;
			gridBagConstraints.weightx = 1;
			add(typeComboBox, gridBagConstraints);

			rowCount++;
			gridBagConstraints.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = rowCount;
			gridBagConstraints.weightx = 0;
			add(new JLabel("�¼����ƣ�"), gridBagConstraints);
			nameTextField = new JTextField();
			gridBagConstraints.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = rowCount;
			gridBagConstraints.weightx = 1;
			add(nameTextField, gridBagConstraints);

			rowCount++;
			searchBt = new JButton("��ѯ");
			gridBagConstraints.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = rowCount;
			gridBagConstraints.weightx = 0;
//			gridBagConstraints.gridwidth = 3;
			add(searchBt, gridBagConstraints);

//			rowCount++;
//			TitledSeparator businessSeparator = new TitledSeparator(
//					new JLabel(), new PartialGradientLineBorder(new Color[] {
//							Color.LIGHT_GRAY, Color.white }, 1,
//							PartialSide.SOUTH),
//					GlobalConstants.LABEL_ALIGH_POSITION);
//			gridBagConstraints.insets = new Insets(3, 1, 1, 1);
//			gridBagConstraints.gridx = 0;
//			gridBagConstraints.gridy = rowCount;
//			gridBagConstraints.weightx = 1;
//			gridBagConstraints.gridwidth = 4;
//			add(businessSeparator, gridBagConstraints);

			rowCount++;
			paramsScrollPane = new JScrollPane(paramsList,
					JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			gridBagConstraints.insets = new Insets(3, 3, 3, 3);
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = rowCount;
			gridBagConstraints.weightx = 1;
			gridBagConstraints.weighty = 1;
//			gridBagConstraints.gridheight = 3;
			gridBagConstraints.gridwidth = 1;
			gridBagConstraints.gridwidth = 4;
			add(paramsScrollPane, gridBagConstraints);

		}

		@Override
		public Serializable applyValues(Serializable value) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void reset() {
			// TODO Auto-generated method stub

		}

		@Override
		public void setValues(Serializable value) {
			// TODO Auto-generated method stub

		}

	}
}
