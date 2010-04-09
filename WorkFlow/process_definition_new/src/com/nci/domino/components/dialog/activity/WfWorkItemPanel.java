package com.nci.domino.components.dialog.activity;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.plaf.multi.MultiInternalFrameUI;

import com.jidesoft.popup.JidePopup;
import com.jidesoft.swing.CheckBoxList;
import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JideScrollPane;
import com.jidesoft.swing.PartialGradientLineBorder;
import com.jidesoft.swing.PartialSide;
import com.jidesoft.swing.TitledSeparator;
import com.nci.domino.GlobalConstants;
import com.nci.domino.beans.desyer.WofoActivityBean;
import com.nci.domino.beans.desyer.WofoArgumentsBean;
import com.nci.domino.beans.desyer.WofoProcessBean;
import com.nci.domino.beans.desyer.WofoWorkitemRuleBean;
import com.nci.domino.components.WfArgumentsPanel;
import com.nci.domino.components.WfTextDocument;
import com.nci.domino.components.dialog.WfDialog;
import com.nci.domino.components.dialog.WfDialogPluginPanel;
import com.nci.domino.components.dialog.process.WfNewProcessDialog;
import com.nci.domino.domain.WofoBaseDomain;
import com.nci.domino.help.Functions;
import com.nci.domino.help.WofoResources;

/**
 * 活动工作项输入输出界面
 * 
 * @author Qil.Wong
 * 
 */
public class WfWorkItemPanel extends WfDialogPluginPanel {

	private JCheckBox useProcessRules = new JCheckBox("使用流程定义的工作单生成规则");

	private RulePanel codePanel = new RulePanel("业务编号：", false);

	private RulePanel contentPanel = new RulePanel("业务内容：", true);

	private RulePanel descPanel = new RulePanel("业务描述：", true);

	private RulePanel statusPanel = new RulePanel("业务状态：", false);

	private JidePopup argPopup;

	// JLabel multipleLabel = new JLabel("为每一个环节参与者分配一个工作单实例");
	private JCheckBox multipleParticipantCheck = new JCheckBox(
			"为每一个环节参与者分配一个工作单实例");
	private JCheckBox multipleActivityCheck = new JCheckBox("为每一个环节参与者分配一个环节实例");
	private JLabel participantTypeLabel = new JLabel("参与方式：");
	private JRadioButton participant_sametime_radio = new JRadioButton("同时参与");
	private JRadioButton participant_sequence_radio = new JRadioButton("顺序参与");
	private JLabel completeTypeLabel = new JLabel("完成类型：");
	JRadioButton complete_all_radio = new JRadioButton("全部完成");
	JRadioButton complete_counts_radio = new JRadioButton("完成数");
	JRadioButton complete_percent_radio = new JRadioButton("完成率");
	JLabel completeConditionLabel = new JLabel("完成条件：");
	JTextField count_percentField = new JTextField();
	JLabel completePercentLabel = new JLabel("%");
	private ButtonGroup participantGroup = new ButtonGroup();
	ButtonGroup completeGroup = new ButtonGroup();

	private JPanel multiItemLeftPanel = new JPanel(new GridBagLayout());
	private JPanel multiItemRightPanel = new JPanel(new GridBagLayout());

	public WfWorkItemPanel(WfDialog dialog) {
		super(dialog);
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		participant_sametime_radio
				.setActionCommand(WofoActivityBean.PARTICIPANT_TYPE_SAMETIME);
		participant_sequence_radio
				.setActionCommand(WofoActivityBean.PARTICIPANT_TYPE_SEQUENCE);
		participantGroup.add(participant_sametime_radio);
		participantGroup.add(participant_sequence_radio);
		participant_sametime_radio.setEnabled(false);
		participant_sequence_radio.setEnabled(false);
		complete_all_radio.setActionCommand(WofoActivityBean.COMPLETE_TYPE_ALL);
		complete_counts_radio
				.setActionCommand(WofoActivityBean.COMPLETE_TYPE_COUNT);
		complete_percent_radio
				.setActionCommand(WofoActivityBean.COMPLETE_TYPE_PERCENT);
		completeGroup.add(complete_all_radio);
		completeGroup.add(complete_counts_radio);
		completeGroup.add(complete_percent_radio);
		ActionListener multipleParticipantAction = new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				checkMultiParticipant();
			}
		};
		participant_sequence_radio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean flag = participant_sametime_radio.isSelected();
				checkSequenceParticipant(flag);
			}
		});
		participant_sametime_radio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean flag = participant_sametime_radio.isSelected();
				checkSequenceParticipant(flag);
			}
		});
		multipleActivityCheck.setEnabled(false);
		multipleParticipantCheck.addActionListener(multipleParticipantAction);
		ActionListener completeRadioAction = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				completePercentLabel
						.setVisible(e.getSource() == complete_percent_radio);

			}
		};
		complete_all_radio.addActionListener(completeRadioAction);
		complete_counts_radio.addActionListener(completeRadioAction);
		complete_percent_radio.addActionListener(completeRadioAction);
		WfTextDocument numDoc = new WfTextDocument(-1, "[0-9]*", "只能是整数");
		numDoc.setWfDialog(dialog);
		count_percentField.setDocument(numDoc);

		setLayout(new GridBagLayout());
		GridBagConstraints cons = new GridBagConstraints();
		// cons.anchor = GridBagConstraints.NORTH;
		cons.insets = new Insets(4, 4, 0, 4);
		cons.gridx = 0;
		cons.gridy = 0;
		cons.fill = GridBagConstraints.BOTH;
		cons.weightx = 1;
		cons.gridwidth = 3;
		add(useProcessRules, cons);
		TitledSeparator sep = new TitledSeparator(new JLabel(),
				new PartialGradientLineBorder(new Color[] { Color.LIGHT_GRAY,
						Color.white }, 1, PartialSide.SOUTH),
				GlobalConstants.LABEL_ALIGH_POSITION);
		cons.gridy = 1;
		cons.insets = new Insets(0, 4, 2, 4);
		add(sep, cons);
		cons.gridy = 2;
		cons.insets = new Insets(2, 4, 2, 4);
		add(codePanel, cons);
		cons.gridy = 3;
		add(contentPanel, cons);
		cons.gridy = 4;
		add(descPanel, cons);
		cons.gridy = 5;
		add(statusPanel, cons);

		TitledSeparator sep2 = new TitledSeparator(new JLabel(),
				new PartialGradientLineBorder(new Color[] { Color.LIGHT_GRAY,
						Color.white }, 1, PartialSide.SOUTH),
				GlobalConstants.LABEL_ALIGH_POSITION);

		TitledSeparator sep3 = new TitledSeparator(new JLabel("多工作单支持"),
				new PartialGradientLineBorder(new Color[] { Color.LIGHT_GRAY,
						Color.white }, 1, PartialSide.SOUTH),
				GlobalConstants.LABEL_ALIGH_POSITION);

		cons.gridy = 6;
		// add(sep3, cons);
		// cons.gridy = 7;
		cons.weightx = 0;
		add(multiItemLeftPanel, cons);
		cons.gridy = 8;
		// add(sep2, cons);
		// cons.gridy = 9;
		add(multiItemRightPanel, cons);

		cons.gridy = 10;
		cons.weighty = 1;
		add(new JPanel(), cons);

		GridBagConstraints leftCons = new GridBagConstraints();
		leftCons.insets = new Insets(3, 5, 3, 5);
		leftCons.gridx = 0;
		leftCons.gridy = 0;
		leftCons.fill = GridBagConstraints.BOTH;
		// multiItemLeftPanel.add(multipleLabel, leftCons);
		// leftCons.gridx = 1;
		multiItemLeftPanel.add(sep3, leftCons);
		leftCons.gridy = 1;
		multiItemLeftPanel.add(multipleParticipantCheck, leftCons);
		leftCons.gridy = 2;
		multiItemLeftPanel.add(multipleActivityCheck, leftCons);
		leftCons.gridy = 3;
		multiItemLeftPanel.add(sep2, leftCons);

		leftCons.gridx = 3;
		leftCons.weightx = 1;
		multiItemLeftPanel.add(new JPanel(), leftCons);

		GridBagConstraints rightCons = new GridBagConstraints();
		rightCons.insets = new Insets(3, 5, 3, 5);
		rightCons.gridx = 0;
		rightCons.gridy = 0;
		rightCons.fill = GridBagConstraints.BOTH;

		multiItemRightPanel.add(participantTypeLabel, rightCons);
		rightCons.gridx = 1;
		multiItemRightPanel.add(participant_sametime_radio, rightCons);
		rightCons.gridx = 2;
		multiItemRightPanel.add(participant_sequence_radio, rightCons);

		rightCons.gridy = 1;
		rightCons.gridx = 0;
		multiItemRightPanel.add(completeTypeLabel, rightCons);
		rightCons.gridx = 1;
		multiItemRightPanel.add(complete_all_radio, rightCons);
		rightCons.gridx = 2;
		multiItemRightPanel.add(complete_counts_radio, rightCons);
		rightCons.gridx = 3;
		multiItemRightPanel.add(complete_percent_radio, rightCons);
		rightCons.gridx = 0;
		rightCons.gridy = 2;
		multiItemRightPanel.add(completeConditionLabel, rightCons);
		rightCons.gridx = 1;

		multiItemRightPanel.add(count_percentField, rightCons);
		rightCons.gridx = 2;
		multiItemRightPanel.add(completePercentLabel, rightCons);

		rightCons.gridx = 3;
		rightCons.weightx = 1;
		multiItemRightPanel.add(new JPanel(), leftCons);

		useProcessRules.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				useProcessRulesSelected();
			}
		});
		if (dialog instanceof WfNewProcessDialog) {
			useProcessRules.setSelected(true);
			useProcessRules.setVisible(false);
			sep.setVisible(false);
		}
	}

	private void useProcessRulesSelected() {
		if (dialog instanceof WfNewActivityHumanDialog) {
			codePanel.setEnabled(!useProcessRules.isSelected());
			contentPanel.setEnabled(!useProcessRules.isSelected());
			descPanel.setEnabled(!useProcessRules.isSelected());
			statusPanel.setEnabled(!useProcessRules.isSelected());
		}
	}

	@Override
	public Serializable applyValues(Serializable value) {
		WofoWorkitemRuleBean workItem = null;
		if (value instanceof WofoActivityBean) {
			WofoActivityBean activity = (WofoActivityBean) value;
			workItem = activity.getWorkitemRule();
			if (useProcessRules.isSelected()) {
				workItem = null;
				activity.setWorkitemRule(null);
			} else if (workItem == null) {
				workItem = new WofoWorkitemRuleBean(Functions.getUID());
				workItem.setProcessObjId(((WofoActivityBean) value)
						.getProcessId());
				workItem.setProcessObjType(WofoBaseDomain.OBJ_TYPE_ACTIVITY);
				activity.setWorkitemRule(workItem);
			}

			// 多单工作时的数据
			activity.setCompleteCond(this.count_percentField.getText());
			activity.setMultiParticipant(this.multipleParticipantCheck
					.isSelected() ? WofoActivityBean.YES : WofoActivityBean.NO);
			activity.setParticipateType("");
			activity.setCompleteType("");
			activity.setCompleteCond("");
			if (multipleParticipantCheck.isSelected()) {
				activity.setParticipateType(this.participantGroup
						.getSelection().getActionCommand());
				if (activity.getParticipateType().equals(
						WofoActivityBean.PARTICIPANT_TYPE_SAMETIME)) {
					activity.setCompleteType(this.completeGroup.getSelection()
							.getActionCommand());
					activity.setCompleteCond(this.count_percentField.getText()
							.trim());
				}
				activity
						.setMultiInstance(multipleActivityCheck.isSelected() ? WofoBaseDomain.YES
								: WofoBaseDomain.NO);

			}
		} else if (value instanceof WofoProcessBean) {
			workItem = ((WofoProcessBean) value).getWorkitemRule();
			if (workItem == null) {
				workItem = new WofoWorkitemRuleBean(Functions.getUID());
				workItem.setProcessObjId(((WofoProcessBean) value)
						.getProcessId());
				workItem.setProcessObjType(WofoBaseDomain.OBJ_TYPE_PROCESS);
				((WofoProcessBean) value).setWorkitemRule(workItem);
			}
		} else {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					"当前对象" + value.getClass() + "还未关联工作单！");
		}
		if (workItem != null) {
			workItem.setBizContentRule(contentPanel.f.getText().trim());
			workItem.setBizDescriptionRule(descPanel.f.getText().trim());
			workItem.setBizNumberRule(codePanel.f.getText().trim());
			workItem.setBizStateRule(statusPanel.f.getText().trim());
		}

		return value;
	}

	@Override
	public void reset() {
		useProcessRules.setSelected(false);
		useProcessRulesSelected();
		codePanel.f.setText("");
		contentPanel.f.setText("");
		descPanel.f.setText("");
		statusPanel.f.setText("");

		multipleParticipantCheck.setSelected(false);
		participant_sametime_radio.setSelected(true);
		participant_sequence_radio.setSelected(false);
		participant_sametime_radio.setEnabled(false);
		participant_sequence_radio.setEnabled(false);
		complete_all_radio.setSelected(true);
		complete_counts_radio.setSelected(false);
		complete_percent_radio.setSelected(false);
		count_percentField.setText("");
		completePercentLabel.setVisible(false);
	}

	/**
	 * 顺序参与时的按钮选择变化
	 * 
	 * @param flag
	 */
	private void checkSequenceParticipant(boolean flag) {
		complete_all_radio.setEnabled(flag);
		complete_counts_radio.setEnabled(flag);
		complete_percent_radio.setEnabled(flag);
		count_percentField.setEnabled(flag);
	}

	/**
	 * 多人参与时的变化
	 */
	private void checkMultiParticipant() {
		multipleActivityCheck.setEnabled(multipleParticipantCheck.isSelected());
		if (!multipleParticipantCheck.isSelected()) {
			multipleActivityCheck.setSelected(false);
		}

		boolean flag = multipleParticipantCheck.isSelected();
		participant_sametime_radio.setEnabled(flag);
		participant_sequence_radio.setEnabled(flag);
		checkSequenceParticipant(participant_sametime_radio.isSelected()
				& participant_sametime_radio.isEnabled());
	}

	@Override
	public void setValues(Serializable value) {
		multiItemLeftPanel.setVisible(value instanceof WofoActivityBean);
		multiItemRightPanel.setVisible(value instanceof WofoActivityBean);

		WofoWorkitemRuleBean workItem = null;
		if (value instanceof WofoActivityBean) {
			WofoActivityBean activity = ((WofoActivityBean) value);
			workItem = activity.getWorkitemRule();
			this.multipleParticipantCheck.setSelected(WofoActivityBean.YES
					.equals(activity.getMultiParticipant()));
			this.complete_all_radio
					.setSelected(WofoActivityBean.COMPLETE_TYPE_ALL
							.equals(activity.getCompleteType()));
			this.complete_counts_radio
					.setSelected(WofoActivityBean.COMPLETE_TYPE_COUNT
							.equals(activity.getCompleteType()));
			this.complete_percent_radio
					.setSelected(WofoActivityBean.COMPLETE_TYPE_PERCENT
							.equals(activity.getCompleteType()));
			this.count_percentField.setText(activity.getCompleteCond());
			this.participant_sametime_radio
					.setSelected(WofoActivityBean.PARTICIPANT_TYPE_SAMETIME
							.equals(activity.getParticipateType()));
			this.participant_sequence_radio
					.setSelected(WofoActivityBean.PARTICIPANT_TYPE_SEQUENCE
							.equals(activity.getParticipateType()));
			this.multipleActivityCheck.setSelected(WofoActivityBean.YES
					.equals(activity.getMultiInstance()));
			checkMultiParticipant();
		} else if (value instanceof WofoProcessBean) {
			workItem = ((WofoProcessBean) value).getWorkitemRule();
		} else {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					"当前对象" + value.getClass() + "还未关联工作单！");
		}
		if (workItem != null) {
			String content = workItem.getBizContentRule() == null ? ""
					: workItem.getBizContentRule();
			String desc = workItem.getBizDescriptionRule() == null ? ""
					: workItem.getBizDescriptionRule();
			String codeNumber = workItem.getBizNumberRule() == null ? ""
					: workItem.getBizNumberRule();
			String status = workItem.getBizStateRule() == null ? "" : workItem
					.getBizStateRule();
			contentPanel.f.setText(content);
			descPanel.f.setText(desc);
			codePanel.f.setText(codeNumber);
			statusPanel.f.setText(status);
		} else {
			useProcessRules.setSelected(true);
		}
		useProcessRulesSelected();
	}

	/**
	 * 工作项规则选择区
	 * 
	 * @author Qil.Wong
	 * 
	 */
	private class RulePanel extends JPanel {
		private JLabel l = new JLabel();
		// 工作单文本
		private JTextField f = new JTextField();
		// 工作单参数选择按钮
		private JideButton b = new JideButton("参数");
		// 参数是否允许多选
		private boolean multiple;

		public RulePanel(String label, boolean multiple) {
			l.setText(label);
			b.setHorizontalTextPosition(AbstractButton.LEFT);
			b.setIcon(Functions.getImageIcon("splitbtn.gif"));
			b.setFocusable(false);
			this.multiple = multiple;
			initRulePanel();
		}

		private void initRulePanel() {
			setLayout(new GridBagLayout());
			GridBagConstraints cons = new GridBagConstraints();
			cons.gridx = 0;
			cons.gridy = 0;
			cons.insets = new Insets(3, 3, 3, 3);
			cons.fill = GridBagConstraints.BOTH;
			add(l, cons);
			cons.gridx = 1;
			cons.weightx = 1;
			add(f, cons);
			cons.gridx = 2;
			cons.weightx = 0;
			add(b, cons);
			final PopupPanel pPanel = new PopupPanel(this, multiple);
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					argPopup = new JidePopup();
					argPopup.setPreferredSize(new Dimension(150, 200));
					argPopup.getContentPane().setLayout(new BorderLayout());
					argPopup.getContentPane().add(pPanel, BorderLayout.CENTER);
					WfArgumentsPanel argPanel = null;
					// 取到参数面板
					for (JPanel p : dialog.getCustomPanels()) {
						if (p instanceof WfArgumentsPanel) {
							argPanel = (WfArgumentsPanel) p;
							break;
						}
					}
					if (argPanel != null) {
						List<WofoArgumentsBean> args = argPanel
								.getArguments(null);
						pPanel.model.clear();
						for (WofoArgumentsBean arg : args) {
							pPanel.model.addElement(arg);
						}
					}
					argPopup.showPopup((JComponent) e.getSource());
				}
			});
		}

		public void setEnabled(boolean flag) {
			super.setEnabled(flag);
			f.setEnabled(flag);
			b.setEnabled(flag);
		}

	}

	/**
	 * 选择弹出面板
	 * 
	 * @author Qil.Wong
	 * 
	 */
	private class PopupPanel extends JPanel {
		private CheckBoxList argList = new CheckBoxList();
		private JButton okBtn = new JButton(WofoResources.getValueByKey("ok"));
		DefaultListModel model = new DefaultListModel();

		public PopupPanel(final RulePanel rule, final boolean multiple) {
			argList.setModel(model);
			if (!multiple) {
				argList.getCheckBoxListSelectionModel().setSelectionMode(
						ListSelectionModel.SINGLE_SELECTION);
			}
			JideScrollPane scroll = new JideScrollPane(argList);
			setLayout(new BorderLayout());
			add(scroll, BorderLayout.CENTER);
			JPanel btnPanel = new JPanel(new BorderLayout());
			btnPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
			btnPanel.add(okBtn, BorderLayout.EAST);
			okBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					argPopup.hidePopup();
					StringBuffer paramValue = new StringBuffer();
					Object[] os = argList.getCheckBoxListSelectedValues();
					for (int i = 0; i < os.length; i++) {
						paramValue.append("{").append(os[i].toString()).append(
								"}");
					}
					if (!multiple) {
						rule.f.setText(paramValue.toString());
					} else {
						rule.f
								.setText(rule.f.getText()
										+ paramValue.toString());
					}
				}
			});
			add(btnPanel, BorderLayout.SOUTH);
		}
	}

}
