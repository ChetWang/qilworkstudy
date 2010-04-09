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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import com.jidesoft.swing.JideScrollPane;
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
 * 聚合\分支活动的父对话框
 * 
 * @author Qil.Wong
 * 
 */
public abstract class WfNewActivityJoinSplit extends WfActivityDialog {

	private static final long serialVersionUID = -3870971699871905246L;

	protected BasicContentPanel basic = null;

	public WfNewActivityJoinSplit(WfEditor editor, String title, boolean modal) {
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
		WfBannerPanel headerPanel1 = new WfBannerPanel("聚合活动",
				"聚合活动是——DongXF补充", null);
		return headerPanel1.getGlassBanner();
	}

	@Override
	public JComponent createContentPanel() {
		basic = new BasicContentPanel();
		contentTab.addTab("属性", null, basic, null);
//		contentTab.addTab("参数", null, new JPanel(), null);
		conditionCombos.add(basic.join_splitConditionCombo);
		return contentTab;
	}

	protected class BasicContentPanel extends WfInputPanel {

		private static final long serialVersionUID = -4607027968061076841L;

		JLabel typeLabel = new JLabel("类型：");
		JTextField typeField = new JTextField();
		JLabel workflowLabel = new JLabel("业务流程：");
		JTextField workflowField = new JTextField();
		WfNameCodeArea nameCodeArea = new WfNameCodeArea("环节中文名：", "环节英文名：",
				WfNewActivityJoinSplit.this);

		JLabel joinTypeLabel = new JLabel("聚合类型：");
		JRadioButton andRadio;
		JRadioButton orRadio;
		JRadioButton conditionRadio;
		JLabel joinConditionLabel = new JLabel("聚合条件：");
		WfComboBox join_splitConditionCombo = new WfComboBox();
		JLabel descLabel = new JLabel("描述：");
		JTextArea descField = new JTextArea();
		JCheckBox canUntread_to = new JCheckBox("是否可被退单");// 被回退
		JCheckBox canReplevy_to = new JCheckBox("是否可被追回");// 被追回

		ButtonGroup bg_join = new ButtonGroup();

		private BasicContentPanel() {
			init();
		}

		private void init() {
			setLayout(new GridBagLayout());
			typeField.setEnabled(false);
			workflowField.setEnabled(false);
			descField.setAutoscrolls(true);

			ActionListener raidoListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					join_splitConditionCombo
							.setEnabled(e.getSource() == conditionRadio);
				}
			};
			andRadio = createRadioBtn("与", bg_join, raidoListener);
			andRadio.setActionCommand(getCurrentNeedAndMode());
			orRadio = createRadioBtn("或", bg_join, raidoListener);
			orRadio.setActionCommand(getCurrentNeedOrMode());
			conditionRadio = createRadioBtn("条件", bg_join, raidoListener);
			conditionRadio.setActionCommand(getCurrentNeedConditionMode());
			WfTextDocument descDoc = new WfTextDocument(200);
			descDoc.setWfDialog(WfNewActivityJoinSplit.this);
			descField.setDocument(descDoc);

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

			leftCons.gridx = 0;
			leftCons.gridy = 2;
			leftCons.gridwidth = 1;
		

			nameCodeArea.init(0, 2, inputAreaGridWidth, this, leftCons);
			// 聚合

			leftCons.gridx = 0;
			leftCons.gridy = 4;
			leftCons.gridwidth = 1;
			add(joinTypeLabel, leftCons);
			leftCons.gridx = 1;
			add(andRadio, leftCons);
			leftCons.gridx = 2;
			add(orRadio, leftCons);
			leftCons.gridx = 3;
			add(conditionRadio, leftCons);

			leftCons.gridx = 0;
			leftCons.gridy = 5;
			add(joinConditionLabel, leftCons);
			leftCons.gridx = 1;
			leftCons.gridwidth = inputAreaGridWidth;
			add(join_splitConditionCombo, leftCons);

			// 描述
			leftCons.gridx = 0;
			leftCons.gridy = 6;
			leftCons.gridwidth = 1;
			add(descLabel, leftCons);
			leftCons.gridx = 1;
			leftCons.gridwidth = inputAreaGridWidth;
			leftCons.fill = GridBagConstraints.BOTH;
			leftCons.weighty = 1;
			JideScrollPane descScroll = new JideScrollPane(descField);
			add(descScroll, leftCons);

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

			leftCons.gridx = 0;
			leftCons.gridy = 7;
			leftCons.weighty = 0;
			leftCons.gridwidth = inputAreaGridWidth + 1;
			leftCons.fill = GridBagConstraints.BOTH;
			add(checkPanel, leftCons);

		}

		public void reset() {
			typeField.setText("");
			workflowField.setText("");
			nameCodeArea.reset();
			andRadio.setSelected(false);
			orRadio.setSelected(true);
			conditionRadio.setSelected(false);
			join_splitConditionCombo.setSelectedIndex(0);
			descField.setText("");
			canReplevy_to.setSelected(true);
			canUntread_to.setSelected(true);

			join_splitConditionCombo.setEnabled(false);
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
			activity.setActivityName(nameCodeArea.getName());
			activity.setActivityStatus(WofoActivityBean.ACTIVITY_STATUS_ONLINE);
			// activity.setActivityVersion(activityVersion)

			activity
					.setCanReplevyTo(this.canReplevy_to.isSelected() ? WofoActivityBean.CAN
							: WofoActivityBean.CAN_NOT);

			activity
					.setCanUntreadTo(this.canUntread_to.isSelected() ? WofoActivityBean.CAN
							: WofoActivityBean.CAN_NOT);

			setConditionID(((WfComboBean) this.join_splitConditionCombo
					.getSelectedItem()).id, activity);

			activity.setDescription(this.descField.getText().trim());

			setJoinSplitMode(activity, bg_join.getSelection()
					.getActionCommand());

			return activity;
		}

		@Override
		public void setValues(Serializable value) {
			WofoActivityBean activity = (WofoActivityBean) value;

			this.canReplevy_to.setSelected(WofoActivityBean.CAN.equals(activity
					.getCanReplevyTo()));
			this.canUntread_to.setSelected(WofoActivityBean.CAN.equals(activity
					.getCanUntreadTo()));
			nameCodeArea.setNameCode(activity.getActivityName(),activity.getActivityCode());
			this.descField.setText(activity.getDescription());
			this.andRadio.setSelected(getCurrentNeedAndMode().equals(
					getJoinSplitMode(activity)));
			this.conditionRadio.setSelected(getCurrentNeedConditionMode()
					.equals(getJoinSplitMode(activity)));
			this.orRadio.setSelected(getCurrentNeedOrMode().equals(
					getJoinSplitMode(activity)));

			this.join_splitConditionCombo.setEnabled(conditionRadio
					.isSelected());
			this.join_splitConditionCombo
					.setSelectedItemByID(getConditionID(activity));
			this.typeField.setText(WofoResources
					.getValueByKey("workflow_activity_"
							+ activity.getActivityType()));
			this.workflowField.setText(editor.getOperationArea()
					.getCurrentPaintBoard().getProcessBean().getProcessName());

		}
	}

	/**
	 * 获取当前的条件模式
	 * 
	 * @param activity
	 * @return
	 */
	public abstract String getJoinSplitMode(WofoActivityBean activity);

	public abstract void setJoinSplitMode(WofoActivityBean activity, String mode);

	/**
	 * 当前模式下的“与”类型字符串值
	 * 
	 * @return
	 */
	public abstract String getCurrentNeedAndMode();

	/**
	 * 当前模式下的“或”类型字符串值
	 * 
	 * @return
	 */
	public abstract String getCurrentNeedOrMode();

	/**
	 * 当前模式下的“条件”类型字符串值
	 * 
	 * @return
	 */
	public abstract String getCurrentNeedConditionMode();

	/**
	 * 获取分支或聚合条件的id
	 * 
	 * @param activity
	 * @return
	 */
	public abstract String getConditionID(WofoActivityBean activity);

	public abstract void setConditionID(String conditionID,
			WofoActivityBean activity);

}
