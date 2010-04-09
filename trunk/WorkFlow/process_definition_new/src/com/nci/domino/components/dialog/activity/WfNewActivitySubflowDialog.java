package com.nci.domino.components.dialog.activity;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.Serializable;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;

import com.jidesoft.swing.JideScrollPane;
import com.nci.domino.WfEditor;
import com.nci.domino.beans.desyer.WofoActivityBean;
import com.nci.domino.beans.desyer.WofoProcessBean;
import com.nci.domino.components.WfBannerPanel;
import com.nci.domino.components.WfInputPanel;
import com.nci.domino.components.WfNameCodeArea;
import com.nci.domino.components.WfSubflowChooser;
import com.nci.domino.components.WfTextDocument;
import com.nci.domino.help.WofoResources;

/**
 * �����̻�Ի���
 * 
 * @author Qil.Wong
 * 
 */
public class WfNewActivitySubflowDialog extends WfActivityDialog {

	private static final long serialVersionUID = -3870971699871905246L;

	protected BasicContentPanel basic = null;

	public WfNewActivitySubflowDialog(WfEditor editor, String title,
			boolean modal) {
		super(editor, title, modal);
		defaultWidth = 520;
		defaultHeight = 380;
	}

	@Override
	public void setInputValues(WofoActivityBean bean) {
		basic.setValues(bean);
	}

	@Override
	protected String checkInput() {
		if (basic.nameCodeArea.getCode().equals("")) {
			return "Ӣ�����Ʋ���Ϊ��";
		}
		if (basic.nameCodeArea.getName().equals("")) {
			return "�������Ʋ���Ϊ��";
		}
		return super.checkInput();
	}

	@Override
	public void clearContents() {
		super.clearContents();
		basic.reset();
	}

	@Override
	public Serializable getInputValues() {
		activity = (WofoActivityBean) super.getInputValues();
		basic.applyValues(activity);
		return activity;
	}

	@Override
	public JComponent createBannerPanel() {
		WfBannerPanel headerPanel1 = new WfBannerPanel("�����̻",
				"�����̻��һ����׼������ʵ�֣����Ը�����ĳ���ϼ�����", null);
		return headerPanel1.getGlassBanner();
	}

	@Override
	public JComponent createContentPanel() {
		basic = new BasicContentPanel();
		contentTab.addTab("����", null, basic, null);
		return contentTab;
	}

	protected class BasicContentPanel extends WfInputPanel {

		private static final long serialVersionUID = -4607027968061076841L;

		JLabel typeLabel = new JLabel("���ͣ�");
		JTextField typeField = new JTextField();
		JLabel workflowLabel = new JLabel("ҵ�����̣�");
		JTextField workflowField = new JTextField();
		WfNameCodeArea nameCodeArea = new WfNameCodeArea("������������", "����Ӣ������",
				WfNewActivitySubflowDialog.this);

		WfSubflowChooser subflowChooser;

		JLabel descLabel = new JLabel("������");
		JTextArea descField = new JTextArea();

		private BasicContentPanel() {
			init();
		}

		private void init() {
			setLayout(new GridBagLayout());
			typeField.setEnabled(false);
			descField.setAutoscrolls(true);
			workflowField.setEnabled(false);

			WfTextDocument descDoc = new WfTextDocument(200);
			descDoc.setWfDialog(WfNewActivitySubflowDialog.this);
			descField.setDocument(descDoc);
			subflowChooser = new WfSubflowChooser(editor, this);
			GridBagConstraints leftCons = new GridBagConstraints();
			int inputAreaGridWidth = 3;
			leftCons.fill = GridBagConstraints.HORIZONTAL;
			leftCons.insets = new Insets(3, 5, 2, 5);
			leftCons.gridx = 0;
			leftCons.gridy = 0;
			leftCons.gridwidth = 1;
			add(typeLabel, leftCons);
			leftCons.gridx = 1;
			leftCons.gridwidth = inputAreaGridWidth;
			leftCons.weightx = 1;
			add(typeField, leftCons);

			leftCons.gridx = 0;
			leftCons.gridy = 1;
			leftCons.gridwidth = 1;
			leftCons.weightx = 0;
			add(workflowLabel, leftCons);
			leftCons.gridx = 1;
			leftCons.gridwidth = inputAreaGridWidth;
			add(workflowField, leftCons);

			nameCodeArea.init(0, 2, inputAreaGridWidth, this, leftCons);

			subflowChooser.init(0, 4, inputAreaGridWidth);

			// ����
			leftCons.gridx = 0;
			leftCons.gridy = 5;
			leftCons.gridwidth = 1;
			add(descLabel, leftCons);
			leftCons.gridx = 1;
			leftCons.gridwidth = inputAreaGridWidth;
			leftCons.fill = GridBagConstraints.BOTH;
			leftCons.weighty = 1;
			JideScrollPane descScroll = new JideScrollPane(descField);
			add(descScroll, leftCons);
		}

		public void reset() {
			typeField.setText("");
			workflowField.setText("");
			nameCodeArea.reset();
			descField.setText("");
		}

		@Override
		public Serializable applyValues(Serializable value) {
			WofoActivityBean activity = (WofoActivityBean) value;
			activity.setActivityCode(nameCodeArea.getCode());
			activity.setActivityName(nameCodeArea.getName());
			activity.setActivityStatus(WofoActivityBean.ACTIVITY_STATUS_ONLINE);

			// activity.setActivityVersion(activityVersion)

			activity.setDescription(this.descField.getText().trim());
			// activity.setDisplayOrder(displayOrder)
			DefaultMutableTreeNode selectedSubNode = subflowChooser
					.getSelectedSubNode();
			if (selectedSubNode != null
					&& !workflowField.getText().trim().equals("")) {
				activity.setSubProcessId(((WofoProcessBean) selectedSubNode
						.getUserObject()).getID());
			}
			return activity;
		}

		@Override
		public void setValues(Serializable value) {
			WofoActivityBean activity = (WofoActivityBean) value;
			nameCodeArea.setNameCode(activity.getActivityName(), activity
					.getActivityCode());
			this.descField.setText(activity.getDescription());
			this.typeField.setText(WofoResources
					.getValueByKey("workflow_activity_"
							+ activity.getActivityType()));
			this.workflowField.setText(editor.getOperationArea()
					.getCurrentPaintBoard().getProcessBean().getProcessName());

		}
	}

	public boolean needArguments() {
		return false;
	}

}
