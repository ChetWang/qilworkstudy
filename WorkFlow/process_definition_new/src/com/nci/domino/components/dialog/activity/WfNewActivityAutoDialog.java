package com.nci.domino.components.dialog.activity;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import com.jidesoft.swing.JideTabbedPane;
import com.nci.domino.WfEditor;
import com.nci.domino.beans.desyer.WofoActivityBean;
import com.nci.domino.components.WfBannerPanel;
import com.nci.domino.components.WfComboBox;
import com.nci.domino.components.WfInputPanel;
import com.nci.domino.components.WfNameCodeArea;
import com.nci.domino.components.WfTextDocument;
import com.nci.domino.components.WfComboBox.WfComboBean;
import com.nci.domino.help.WofoResources;

/**
 * 自动流程对话框
 * 
 * @author Qil.Wong
 * 
 */
public class WfNewActivityAutoDialog extends WfActivityDialog {

	private static final long serialVersionUID = -865571071467091019L;

	public WfNewActivityAutoDialog(WfEditor editor, String title, boolean modal) {
		super(editor, title, modal);
		defaultWidth = 600;
		defaultHeight = 300;
	}

	@Override
	public void setInputValues(WofoActivityBean bean) {
		basic.setValues(bean);
	}

	@Override
	protected String checkInput() {
		if (basic.nameCodeArea.getCode().equals("")) {
			return "英文名称不能为空";
		}
		if (basic.nameCodeArea.getName().equals("")) {
			return "中文名称不能为空";
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
		WfBannerPanel headerPanel1 = new WfBannerPanel("自动活动",
				"自动活动是不需要进行任何人员干预的活动", null);
		return headerPanel1.getGlassBanner();
	}

	BasicContentPanel basic = null;

	@Override
	public JComponent createContentPanel() {
		
		basic = new BasicContentPanel();
		contentTab.addTab("属性", null, basic, null);
		conditionCombos.add(basic.joinConditionCombo);
		conditionCombos.add(basic.splitConditionCombo);
		return contentTab;
	}

	/**
	 * 属性面板
	 * 
	 * @author Qil.Wong
	 * 
	 */
	private class BasicContentPanel extends WfInputPanel {

		private static final long serialVersionUID = -4607027968061076841L;

		JLabel typeLabel = new JLabel("类型：");
		JTextField typeField = new JTextField();
		JLabel workflowLabel = new JLabel("业务流程：");
		JTextField workflowField = new JTextField();
		WfNameCodeArea nameCodeArea = new WfNameCodeArea("环节中文名：", "环节英文名：",
				WfNewActivityAutoDialog.this);
		JLabel iconLabel = new JLabel("图标：");
		WfComboBox iconCombo = new WfComboBox();
		JLabel splitTypeLabel = new JLabel("分支类型：");
		JRadioButton split_andRadio;
		JRadioButton split_orRadio;
		JRadioButton split_conditionRadio;
		JLabel splitConditionLabel = new JLabel("分支条件：");
		WfComboBox splitConditionCombo = new WfComboBox();
		JLabel joinTypeLabel = new JLabel("聚合类型：");
		JRadioButton join_andRadio;
		JRadioButton join_orRadio;
		JRadioButton join_conditionRadio;
		JLabel joinConditionLabel = new JLabel("聚合条件：");
		WfComboBox joinConditionCombo = new WfComboBox();
		JLabel descLabel = new JLabel("描述：");
		JTextField descField = new JTextField();
		JCheckBox canUntread_to = new JCheckBox("是否可被退单");// 被回退
		JCheckBox canReplevy_to = new JCheckBox("是否可被追回");// 被追回
		ButtonGroup bg_split = new ButtonGroup();
		ButtonGroup bg_join = new ButtonGroup();

		private BasicContentPanel() {
			init();
		}

		/**
		 * 初始化
		 */
		private void init() {
			setLayout(new GridBagLayout());
			JPanel leftPanel = new JPanel(new GridBagLayout());
			JPanel rightPanel = new JPanel(new GridBagLayout());
			typeField.setEnabled(false);
			workflowField.setEnabled(false);
			String processCodeFieldTip = "只允许英文\"a-z  A-Z\",\"-\",\"_\",及数字0-9";
			WfTextDocument codeDoc = new WfTextDocument(38, "[a-z0-9A-Z_-]*",
					processCodeFieldTip);
			codeDoc.setWfDialog(WfNewActivityAutoDialog.this);

			ActionListener raidoListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					splitConditionCombo
							.setEnabled(e.getSource() == split_conditionRadio);
				}
			};
			split_andRadio = createRadioBtn("与", bg_split, raidoListener);
			split_andRadio.setActionCommand(WofoActivityBean.SPLIT_MODE_AND);
			split_orRadio = createRadioBtn("或", bg_split, raidoListener);
			split_orRadio.setActionCommand(WofoActivityBean.SPLIT_MODE_OR);
			split_conditionRadio = createRadioBtn("条件", bg_split, raidoListener);
			split_conditionRadio
					.setActionCommand(WofoActivityBean.SPLIT_MODE_CONDITION);
			raidoListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					joinConditionCombo
							.setEnabled(e.getSource() == join_conditionRadio);
				}
			};
			join_andRadio = createRadioBtn("与", bg_join, raidoListener);
			join_andRadio.setActionCommand(WofoActivityBean.JOIN_MODE_AND);
			join_orRadio = createRadioBtn("或", bg_join, raidoListener);
			join_orRadio.setActionCommand(WofoActivityBean.JOIN_MODE_OR);
			join_conditionRadio = createRadioBtn("条件", bg_join, raidoListener);
			join_conditionRadio
					.setActionCommand(WofoActivityBean.JOIN_MODE_CONDITION);
			WfTextDocument descDoc = new WfTextDocument(200);
			descDoc.setWfDialog(WfNewActivityAutoDialog.this);
			descField.setDocument(descDoc);

			// 开始布局
			GridBagConstraints rootCons = new GridBagConstraints();
			rootCons.gridx = 0;
			rootCons.gridy = 0;
			rootCons.fill = GridBagConstraints.BOTH;
			rootCons.insets = new Insets(3, 5, 1, 5);
			rootCons.weightx = 0.5;
			rootCons.weighty = 1;
			add(leftPanel, rootCons);
			rootCons = new GridBagConstraints();
			rootCons.gridx = 1;
			rootCons.gridy = 0;
			rootCons.insets = new Insets(3, 5, 1, 5);
			rootCons.fill = GridBagConstraints.BOTH;
			add(rightPanel, rootCons);

			GridBagConstraints leftCons = new GridBagConstraints();
			int inputAreaGridWidth = 1;
			leftCons.fill = GridBagConstraints.HORIZONTAL;
			leftCons.insets = new Insets(3, 5, 2, 5);
			leftCons.gridx = 0;
			leftCons.gridy = 0;
			leftCons.gridwidth = 1;
			leftPanel.add(typeLabel, leftCons);
			leftCons.gridx = 1;
			leftCons.gridwidth = inputAreaGridWidth;
			leftCons.weightx = 1;
			leftPanel.add(typeField, leftCons);

			leftCons.gridx = 0;
			leftCons.gridy = 1;
			leftCons.gridwidth = 1;
			leftCons.weightx = 0;
			leftPanel.add(workflowLabel, leftCons);
			leftCons.gridx = 1;
			leftCons.gridwidth = inputAreaGridWidth;
			leftPanel.add(workflowField, leftCons);

			leftCons.gridx = 0;
			leftCons.gridy = 2;
			leftCons.gridwidth = 1;

			nameCodeArea.init(0, 2, inputAreaGridWidth, leftPanel, leftCons);
			leftCons.gridx = 0;
			leftCons.gridy = 4;
			leftCons.gridwidth = 1;
			leftPanel.add(iconLabel, leftCons);
			leftCons.gridx = 1;
			leftCons.gridwidth = inputAreaGridWidth;
			leftPanel.add(iconCombo, leftCons);

			// 描述
			leftCons.gridx = 0;
			leftCons.gridy = 5;
			leftCons.gridwidth = 1;
			leftPanel.add(descLabel, leftCons);
			leftCons.gridx = 1;
			leftCons.gridwidth = 5;
			leftPanel.add(descField, leftCons);

			GridBagConstraints rightCons = new GridBagConstraints();
			rightCons.gridx = 0;
			rightCons.gridy = 0;
			rightCons.anchor = GridBagConstraints.WEST;
			rightCons.fill = GridBagConstraints.HORIZONTAL;
			rightCons.insets = new Insets(3, 5, 2, 5);

			// 分支
			rightCons.fill = GridBagConstraints.HORIZONTAL;
			rightCons.gridx = 0;
			rightCons.gridy = 1;
			rightPanel.add(splitTypeLabel, rightCons);
			rightCons.gridx = 1;
			rightPanel.add(split_andRadio, rightCons);
			rightCons.gridx = 2;
			rightPanel.add(split_orRadio, rightCons);
			rightCons.gridx = 3;
			rightPanel.add(split_conditionRadio, rightCons);

			rightCons.gridx = 0;
			rightCons.gridy = 2;
			rightPanel.add(splitConditionLabel, rightCons);
			rightCons.gridx = 1;
			rightCons.gridwidth = 3;

			rightPanel.add(splitConditionCombo, rightCons);

			// 聚合
			rightCons = new GridBagConstraints();
			rightCons.insets = new Insets(3, 5, 2, 5);
			rightCons.fill = GridBagConstraints.HORIZONTAL;
			rightCons.gridx = 0;
			rightCons.gridy = 3;

			rightPanel.add(joinTypeLabel, rightCons);
			rightCons.gridx = 1;
			rightPanel.add(join_andRadio, rightCons);
			rightCons.gridx = 2;
			rightPanel.add(join_orRadio, rightCons);
			rightCons.gridx = 3;
			rightPanel.add(join_conditionRadio, rightCons);

			rightCons.gridx = 0;
			rightCons.gridy = 4;
			rightPanel.add(joinConditionLabel, rightCons);
			rightCons.gridx = 1;
			rightCons.gridwidth = 3;
			rightPanel.add(joinConditionCombo, rightCons);

			// 可选框
			JPanel checkPanel = new JPanel(new GridBagLayout());
			checkPanel.setBorder(new EtchedBorder());
			GridBagConstraints checkCons = new GridBagConstraints();
			checkCons.insets = new Insets(3, 10, 3, 10);
			checkCons.fill = GridBagConstraints.BOTH;
			checkCons.gridx = 0;
			checkCons.gridy = 0;
			checkPanel.add(canUntread_to, checkCons);
			checkCons.gridx = 1;
			checkPanel.add(canReplevy_to, checkCons);

			rightCons.gridx = 0;
			rightCons.gridy = 6;
			rightCons.gridwidth = 4;
			rightCons.weighty = 1;
			rightCons.fill = GridBagConstraints.BOTH;
			rightPanel.add(checkPanel, rightCons);
		}

		public void reset() {
			typeField.setText("");
			workflowField.setText("");
			nameCodeArea.reset();
			iconCombo.setSelectedIndex(-1);
			split_andRadio.setSelected(false);
			split_orRadio.setSelected(true);
			split_conditionRadio.setSelected(false);
			splitConditionCombo.setSelectedIndex(-1);
			join_andRadio.setSelected(false);
			join_orRadio.setSelected(true);
			join_conditionRadio.setSelected(false);
			joinConditionCombo.setSelectedIndex(-1);
			descField.setText("");
			canReplevy_to.setSelected(true);
			canUntread_to.setSelected(true);
			splitConditionCombo.setEnabled(false);
			joinConditionCombo.setEnabled(false);
		}

		/**
		 * 简单的创建radiobutton
		 * 
		 * @param txt
		 * @param bg
		 * @param lis
		 * @return
		 */
		private JRadioButton createRadioBtn(String txt, ButtonGroup bg,
				ActionListener lis) {
			JRadioButton r = new JRadioButton(txt);
			bg.add(r);
			r.addActionListener(lis);
			return r;
		}

		@Override
		public Serializable applyValues(Serializable value) {
			WofoActivityBean activity = (WofoActivityBean) value;
			activity.setActivityCode(nameCodeArea.getCode());
			activity.setActivityIcon(((WfComboBean) this.iconCombo
					.getSelectedItem()).id);
			activity.setActivityName(nameCodeArea.getName());
			activity.setActivityStatus(WofoActivityBean.ACTIVITY_STATUS_ONLINE);
			// activity.setActivityType(WofoActivityBean.ACTIVITY_TYPE_AUTO);
			// activity.setActivityVersion(activityVersion)

			activity
					.setCanReplevyTo(this.canReplevy_to.isSelected() ? WofoActivityBean.CAN
							: WofoActivityBean.CAN_NOT);

			activity
					.setCanUntreadTo(this.canUntread_to.isSelected() ? WofoActivityBean.CAN
							: WofoActivityBean.CAN_NOT);

			activity
					.setSplitConditionId(((WfComboBean) this.splitConditionCombo
							.getSelectedItem()).id);
			activity.setJoinConditionId(((WfComboBean) this.joinConditionCombo
					.getSelectedItem()).id);
			activity.setDescription(this.descField.getText().trim());
			// activity.setDisplayOrder(displayOrder)

			activity.setJoinMode(bg_join.getSelection().getActionCommand());

			activity.setSplitMode(bg_split.getSelection().getActionCommand());
			return activity;
		}

		@Override
		public void setValues(Serializable value) {
			WofoActivityBean activity = (WofoActivityBean) value;

			this.canReplevy_to.setSelected(WofoActivityBean.CAN.equals(activity
					.getCanReplevyTo()));
			this.canUntread_to.setSelected(WofoActivityBean.CAN.equals(activity
					.getCanUntreadTo()));
			this.descField.setText(activity.getDescription());
			nameCodeArea.setNameCode(activity.getActivityName(), activity
					.getActivityCode());

			this.iconCombo.setSelectedItem(activity.getActivityIcon());
			this.join_andRadio.setSelected(WofoActivityBean.JOIN_MODE_AND
					.equals(activity.getJoinMode()));
			this.join_conditionRadio
					.setSelected(WofoActivityBean.JOIN_MODE_CONDITION
							.equals(activity.getJoinMode()));
			this.join_orRadio.setSelected(WofoActivityBean.JOIN_MODE_OR
					.equals(activity.getJoinMode()));

			this.joinConditionCombo
					.setEnabled(join_conditionRadio.isSelected());
			boolean joinConditionEnabbled = join_conditionRadio.isSelected();
			this.joinConditionCombo.setEnabled(joinConditionEnabbled);
			if (joinConditionEnabbled) {
				this.joinConditionCombo.setSelectedItemByID(activity
						.getSplitConditionId());
			} else {
				this.joinConditionCombo.setSelectedIndex(0);
			}
			this.split_andRadio.setSelected(WofoActivityBean.SPLIT_MODE_AND
					.equals(activity.getSplitMode()));
			this.split_orRadio.setSelected(WofoActivityBean.SPLIT_MODE_OR
					.equals(activity.getSplitMode()));
			this.split_conditionRadio
					.setSelected(WofoActivityBean.SPLIT_MODE_CONDITION
							.equals(activity.getSplitMode()));

			boolean splitConditionEnabbled = split_conditionRadio.isSelected();
			this.splitConditionCombo.setEnabled(splitConditionEnabbled);
			if (splitConditionEnabbled) {
				this.splitConditionCombo.setSelectedItemByID(activity
						.getSplitConditionId());
			} else {
				this.splitConditionCombo.setSelectedIndex(0);
			}

			this.typeField.setText(WofoResources
					.getValueByKey("workflow_activity_"
							+ activity.getActivityType()));
			this.workflowField.setText(editor.getOperationArea()
					.getCurrentPaintBoard().getProcessBean().getProcessName());

		}
	}

}
