package com.nci.domino.components.dialog.activity;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.jidesoft.swing.JideScrollPane;
import com.jidesoft.swing.JideTabbedPane;
import com.jidesoft.swing.PartialGradientLineBorder;
import com.jidesoft.swing.PartialSide;
import com.jidesoft.swing.TitledSeparator;
import com.nci.domino.GlobalConstants;
import com.nci.domino.WfEditor;
import com.nci.domino.beans.desyer.WofoActivityBean;
import com.nci.domino.beans.desyer.WofoParticipantScopeBean;
import com.nci.domino.beans.org.WofoRoleBean;
import com.nci.domino.beans.org.WofoUnitBean;
import com.nci.domino.beans.org.WofoUserBean;
import com.nci.domino.beans.org.WofoVirtualRoleBean;
import com.nci.domino.components.WfBannerPanel;
import com.nci.domino.components.WfComboBox;
import com.nci.domino.components.WfDepartmentChooser;
import com.nci.domino.components.WfInputPanel;
import com.nci.domino.components.WfModelPanel;
import com.nci.domino.components.WfNameCodeArea;
import com.nci.domino.components.WfOperControllerButtonPanel;
import com.nci.domino.components.WfParticipantChooser;
import com.nci.domino.components.WfTextDocument;
import com.nci.domino.components.WfComboBox.WfComboBean;
import com.nci.domino.components.dialog.IllegalInputTypeException;
import com.nci.domino.help.Functions;
import com.nci.domino.help.WofoResources;

/**
 * �»��ҵ�񻷽ڣ��ĶԻ���
 * 
 * @author Qil.Wong
 * 
 */
public class WfNewActivityHumanDialog extends WfActivityDialog {

	private static final long serialVersionUID = 2421835120328951891L;

	private BasicContentPanel basic;

	private ParticipantPanel participantPanel;

	public WfNewActivityHumanDialog(WfEditor editor, String title, boolean modal) {
		super(editor, title, modal);
		// 700, 520,
		defaultWidth = 700;
		defaultHeight = 540;
	}

	protected String checkInput() {
		if (basic.nameCodeArea.getCode().equals("")) {
			return "Ӣ�����Ʋ���Ϊ��";
		}
		if (basic.nameCodeArea.getName().equals("")) {
			return "�������Ʋ���Ϊ��";
		}

		String duplicateActivityCodeResult = basic.nameCodeArea
				.checkDuplicateActivityCode();
		if (duplicateActivityCodeResult != null
				&& !duplicateActivityCodeResult.trim().equals("")) {
			return duplicateActivityCodeResult;
		}
		return super.checkInput();
	}

	@Override
	public void clearContents() {
		super.clearContents();
		((JideTabbedPane) getContentPanel()).setSelectedIndex(0);
		participantPanel.reset();
		basic.reset();
	}

	@Override
	public Serializable getInputValues() {
		activity = (WofoActivityBean) super.getInputValues();
		basic.applyValues(activity);
		participantPanel.applyValues(activity);
		return activity;
	}

	@Override
	public void setInputValues(WofoActivityBean activity) {
		basic.setValues(activity);
		participantPanel.setValues(activity);
	}

	@Override
	public JComponent createBannerPanel() {
		WfBannerPanel headerPanel1 = new WfBannerPanel("ҵ�����̻",
				"ҵ�����̻��һ���˹������Ҫ��Ա������Ĳ���", null);
		return headerPanel1.getGlassBanner();
	}

	@Override
	public JComponent createContentPanel() {
		basic = new BasicContentPanel();
		contentTab.addTab("����", null, basic, null);
		participantPanel = new ParticipantPanel();
		contentTab.addTab("������", null, participantPanel, null);
		conditionCombos.add(basic.joinConditionCombo);
		conditionCombos.add(basic.splitConditionCombo);
		return contentTab;
	}

	private class BasicContentPanel extends WfInputPanel {

		JLabel typeLabel = new JLabel("���ͣ�");
		JTextField typeField = new JTextField();
		JLabel workflowLabel = new JLabel("ҵ�����̣�");
		JLabel iconLabel = new JLabel("ͼ�꣺");
		JTextField workflowField = new JTextField();
		WfComboBox iconCombo = new WfComboBox(false, true);
		WfDepartmentChooser departChooser;

		JLabel dealPeriodLabel = new JLabel("�������ޣ�");
		JTextField limitPeriodField = new JTextField();
		JLabel dealPeriodCombo = new JLabel("��");
		JLabel warnPeriodLabel = new JLabel("�澯���ޣ�");
		JTextField alarmPeriodField = new JTextField();
		JLabel alarmPeriodCombo = new JLabel("��");
		JLabel splitTypeLabel = new JLabel("��֧���ͣ�");
		JRadioButton split_andRadio;
		JRadioButton split_orRadio;
		JRadioButton split_conditionRadio;

		JLabel splitConditionLabel = new JLabel("��֧������");
		WfComboBox splitConditionCombo = new WfComboBox();

		JLabel joinTypeLabel = new JLabel("�ۺ����ͣ�");
		JRadioButton join_andRadio;
		JRadioButton join_orRadio;
		JRadioButton join_conditionRadio;

		JLabel joinConditionLabel = new JLabel("�ۺ�������");
		WfComboBox joinConditionCombo = new WfComboBox();

		JLabel descLabel = new JLabel("������");
		JTextField descField = new JTextField();

		JCheckBox canUntread = new JCheckBox("�Ƿ���˵�");// ����
		JCheckBox canUntread_to = new JCheckBox("�Ƿ�ɱ��˵�");// ������
		JCheckBox canReplevy = new JCheckBox("�Ƿ��׷��");// ׷��
		JCheckBox canReplevy_to = new JCheckBox("�Ƿ�ɱ�׷��");// ��׷��
		JCheckBox canInterrupt = new JCheckBox("�Ƿ����ֹ");// ��ֹ
		JCheckBox canNotify = new JCheckBox("����ѭ������");// ��������

		TitledSeparator businessSeparator = new TitledSeparator(new JLabel(),
				new PartialGradientLineBorder(new Color[] { Color.LIGHT_GRAY,
						Color.white }, 1, PartialSide.SOUTH),
				GlobalConstants.LABEL_ALIGH_POSITION);

		WfModelPanel businessPerform = new WfModelPanel(
				WfNewActivityHumanDialog.this);// ҵ����
		WfModelPanel businessView = new WfModelPanel(
				WfNewActivityHumanDialog.this);// ҵ��鿴
		WfModelPanel businessUntreat = new WfModelPanel(
				WfNewActivityHumanDialog.this);// ҵ�����

		ButtonGroup bg_split = new ButtonGroup();
		ButtonGroup bg_join = new ButtonGroup();
		WfNameCodeArea nameCodeArea = new WfNameCodeArea("������������", "����Ӣ������",
				WfNewActivityHumanDialog.this);

		private BasicContentPanel() {
			init();
		}

		private void init() {
			setLayout(new GridBagLayout());
			JTabbedPane businessTab = new JTabbedPane();
			JPanel leftPanel = new JPanel(new GridBagLayout());
			JPanel rightPanel = new JPanel(new GridBagLayout());
			typeField.setEnabled(false);
			workflowField.setEnabled(false);
			String tip = "��������ֻ��������";
			WfTextDocument numberDoc = new WfTextDocument(-1, "[0-9]*", tip);
			numberDoc.setWfDialog(WfNewActivityHumanDialog.this);

			limitPeriodField.setDocument(numberDoc);

			numberDoc = new WfTextDocument(-1, "[0-9]*", tip);
			numberDoc.setWfDialog(WfNewActivityHumanDialog.this);
			alarmPeriodField.setDocument(numberDoc);

			ActionListener raidoListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					splitConditionCombo
							.setEnabled(e.getSource() == split_conditionRadio);
				}
			};
			split_andRadio = createRadioBtn("��", bg_split, raidoListener);
			split_andRadio.setActionCommand(WofoActivityBean.SPLIT_MODE_AND);
			split_orRadio = createRadioBtn("��", bg_split, raidoListener);
			split_orRadio.setActionCommand(WofoActivityBean.SPLIT_MODE_OR);
			split_conditionRadio = createRadioBtn("����", bg_split, raidoListener);
			split_conditionRadio
					.setActionCommand(WofoActivityBean.SPLIT_MODE_CONDITION);
			raidoListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					joinConditionCombo
							.setEnabled(e.getSource() == join_conditionRadio);
				}
			};
			join_andRadio = createRadioBtn("��", bg_join, raidoListener);
			join_andRadio.setActionCommand(WofoActivityBean.JOIN_MODE_AND);
			join_orRadio = createRadioBtn("��", bg_join, raidoListener);
			join_orRadio.setActionCommand(WofoActivityBean.JOIN_MODE_OR);
			join_conditionRadio = createRadioBtn("����", bg_join, raidoListener);
			join_conditionRadio
					.setActionCommand(WofoActivityBean.JOIN_MODE_CONDITION);
			WfTextDocument descDoc = new WfTextDocument(200);
			descDoc.setWfDialog(WfNewActivityHumanDialog.this);
			descField.setDocument(descDoc);

			initIconCombo();

			// ��ʼ����
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
			rootCons = new GridBagConstraints();
			rootCons.gridx = 0;
			rootCons.gridy = 1;
			rootCons.gridwidth = 2;
			rootCons.insets = new Insets(1, 5, 1, 5);
			rootCons.fill = GridBagConstraints.BOTH;
			add(businessSeparator, rootCons);
			rootCons.gridy = 2;
			add(businessTab, rootCons);

			GridBagConstraints leftCons = new GridBagConstraints();
			int inputAreaGridWidth = 2;
			leftCons.fill = GridBagConstraints.HORIZONTAL;
			leftCons.insets = new Insets(3, 5, 2, 5);
			leftCons.gridx = 0;
			leftCons.gridy = 0;
			leftCons.gridwidth = 1;
			leftPanel.add(typeLabel, leftCons);
			leftCons.gridx = 1;
			leftCons.gridwidth = inputAreaGridWidth;
			leftPanel.add(typeField, leftCons);

			leftCons.gridx = 0;
			leftCons.gridy = 1;
			leftCons.gridwidth = 1;
			leftPanel.add(workflowLabel, leftCons);
			leftCons.gridx = 1;
			leftCons.gridwidth = inputAreaGridWidth;
			leftPanel.add(workflowField, leftCons);

			nameCodeArea.init(0, 2, inputAreaGridWidth, leftPanel, leftCons);

			leftCons.gridx = 0;
			leftCons.gridy = 4;
			leftCons.gridwidth = 1;
			leftPanel.add(iconLabel, leftCons);
			leftCons.gridx = 1;
			leftCons.gridwidth = inputAreaGridWidth;
			leftPanel.add(iconCombo, leftCons);

			departChooser = new WfDepartmentChooser(editor, leftPanel);
			departChooser.init(0, 5, inputAreaGridWidth);

			GridBagConstraints rightCons = new GridBagConstraints();
			rightCons.gridx = 0;
			rightCons.gridy = 0;
			rightCons.anchor = GridBagConstraints.WEST;
			rightCons.fill = GridBagConstraints.HORIZONTAL;
			rightCons.insets = new Insets(3, 5, 2, 5);
			rightPanel.add(dealPeriodLabel, rightCons);
			rightCons.gridx = 1;
			limitPeriodField.setColumns(4);
			rightPanel.add(limitPeriodField, rightCons);
			rightCons.gridx = 2;
			rightPanel.add(dealPeriodCombo, rightCons);
			rightCons.gridx = 3;
			rightPanel.add(warnPeriodLabel, rightCons);
			rightCons.gridx = 4;
			alarmPeriodField.setColumns(4);
			rightPanel.add(alarmPeriodField, rightCons);
			rightCons.gridx = 5;
			rightPanel.add(alarmPeriodCombo, rightCons);

			// ��֧

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
			rightCons.gridx = 4;
			JLabel glue = new JLabel();// �����ſ�warnPeriodField
			glue.setBorder(BorderFactory.createEmptyBorder(2, 18, 2, 18));
			rightPanel.add(glue, rightCons);

			rightCons.gridx = 0;
			rightCons.gridy = 2;
			rightPanel.add(splitConditionLabel, rightCons);
			rightCons.gridx = 1;
			rightCons.gridwidth = 5;

			rightPanel.add(splitConditionCombo, rightCons);

			// �ۺ�
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
			rightCons.gridwidth = 5;
			rightPanel.add(joinConditionCombo, rightCons);

			// ����
			rightCons.gridx = 0;
			rightCons.gridy = 5;
			rightCons.gridwidth = 1;
			rightPanel.add(descLabel, rightCons);
			rightCons.gridx = 1;
			rightCons.gridwidth = 5;
			rightPanel.add(descField, rightCons);

			// ��ѡ��
			JPanel checkPanel = new JPanel(new GridBagLayout());
			checkPanel.setBorder(new EtchedBorder());
			GridBagConstraints checkCons = new GridBagConstraints();
			checkCons.insets = new Insets(3, 10, 3, 10);
			checkCons.fill = GridBagConstraints.BOTH;
			checkCons.gridx = 0;
			checkCons.gridy = 0;
			checkPanel.add(canUntread, checkCons);
			checkCons.gridx = 1;
			checkPanel.add(canUntread_to, checkCons);
			checkCons.gridx = 0;
			checkCons.gridy = 1;
			checkPanel.add(canReplevy, checkCons);
			checkCons.gridx = 1;
			checkPanel.add(canReplevy_to, checkCons);
			checkCons.gridx = 0;
			checkCons.gridy = 2;
			checkPanel.add(canInterrupt, checkCons);
			checkCons.gridx = 1;
			checkPanel.add(canNotify, checkCons);

			rightCons.gridx = 0;
			rightCons.gridy = 6;
			rightCons.gridwidth = 6;
			rightCons.weighty = 1;
			rightCons.fill = GridBagConstraints.BOTH;
			rightCons.insets = new Insets(5, 0, 5, 5);
			rightPanel.add(checkPanel, rightCons);

			businessTab.addTab("ҵ����", null, businessPerform, null);
			businessTab.addTab("ҵ��鿴", null, businessView, null);
			businessTab.addTab("ҵ�����", null, businessUntreat, null);
		}

		private Map<String, String> iconMap = new HashMap<String, String>();

		private int defaultInconIndex = 0;

		/**
		 * ��ʼ��ͼ��
		 */
		private void initIconCombo() {
			Document doc = Functions.getXMLDocument(getClass().getResource(
					"/resources/icons_activity_human.xml"));
			NodeList icons = doc.getElementsByTagName("icon");
			List<WfComboBean> values = new ArrayList<WfComboBean>();
			for (int i = 0; i < icons.getLength(); i++) {
				Element e = (Element) icons.item(i);
				WfComboBean child = new WfComboBean(e.getAttribute("icon38"), e
						.getAttribute("name"));
				WfComboBean b = new WfComboBean(e.getAttribute("icon16"), child);
				iconMap.put(e.getAttribute("icon38"), e.getAttribute("icon16"));
				if (e.getAttribute("default").equals("true")) {
					defaultInconIndex = i;
				}
				values.add(b);
			}
			iconCombo.resetElements(values, false);
		}

		public void reset() {
			typeField.setText("");
			workflowField.setText("");
			nameCodeArea.reset();
			iconCombo.setSelectedIndex(defaultInconIndex);
			departChooser.clearContents();
			limitPeriodField.setText("");
			alarmPeriodField.setText("");
			split_andRadio.setSelected(false);
			split_orRadio.setSelected(true);
			split_conditionRadio.setSelected(false);
			splitConditionCombo.setSelectedIndex(0);
			join_andRadio.setSelected(false);
			join_orRadio.setSelected(true);
			join_conditionRadio.setSelected(false);
			joinConditionCombo.setSelectedIndex(0);
			descField.setText("");
			canUntread.setSelected(true);
			canInterrupt.setSelected(true);
			canNotify.setSelected(false);
			canReplevy.setSelected(true);
			canReplevy_to.setSelected(true);
			canUntread_to.setSelected(true);
			splitConditionCombo.setEnabled(false);
			joinConditionCombo.setEnabled(false);

		}

		/**
		 * �򵥵Ĵ���radiobutton
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
			String[] perform_name_type_url = (String[]) businessPerform
					.applyValues(value);
			activity.setAppPerformName(perform_name_type_url[0]);
			activity.setAppPerformType(perform_name_type_url[1]);
			activity.setAppPerformUrl(perform_name_type_url[2]);
			String[] view_name_type_url = (String[]) businessView
					.applyValues(value);
			activity.setAppViewName(view_name_type_url[0]);
			activity.setAppViewType(view_name_type_url[1]);
			activity.setAppViewUrl(view_name_type_url[2]);
			String[] unthread_name_type_url = (String[]) businessUntreat
					.applyValues(value);
			activity.setAppUntreadName(unthread_name_type_url[0]);
			activity.setAppUntreadType(unthread_name_type_url[1]);
			activity.setAppUntreadUrl(unthread_name_type_url[2]);
			activity.setActivityCode(this.nameCodeArea.getCode().trim());
			activity
					.setActivityIcon(((WfComboBean) ((WfComboBean) this.iconCombo
							.getSelectedItem()).value).id);
			activity.setActivityName(this.nameCodeArea.getName().trim());
			activity.setActivityStatus(WofoActivityBean.ACTIVITY_STATUS_ONLINE);
			// activity.setActivityType(WofoActivityBean.ACTIVITY_TYPE_HUMAN);
			// activity.setActivityVersion(activityVersion)
			// activity.setPackageId(editor.getOperationArea().getCurrentPackage().getPackageId());

			String alarm = this.alarmPeriodField.getText().trim();
			if (!alarm.equals("")) {
				activity.setAlarmTime(Integer.parseInt(alarm));
			}
			activity
					.setCanInterrupt(this.canInterrupt.isSelected() ? WofoActivityBean.CAN
							: WofoActivityBean.CAN_NOT);
			activity
					.setCanReplevy(this.canReplevy.isSelected() ? WofoActivityBean.CAN
							: WofoActivityBean.CAN_NOT);
			activity
					.setCanReplevyTo(this.canReplevy_to.isSelected() ? WofoActivityBean.CAN
							: WofoActivityBean.CAN_NOT);
			activity
					.setCanUntread(this.canUntread.isSelected() ? WofoActivityBean.CAN
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
			activity
					.setEffectDepartmentId(this.departChooser.getDepartmentID());
			activity.setEffectUnitId(this.departChooser.getCompanyID());
			activity
					.setExpireLoopRemind(this.canNotify.isSelected() ? WofoActivityBean.CAN
							: WofoActivityBean.CAN_NOT);
			activity.setJoinMode(bg_join.getSelection().getActionCommand());
			String limit = this.limitPeriodField.getText().trim();
			if (!limit.equals("")) {
				activity.setLimitTime(Integer.parseInt(limit));
			}
			activity.setSplitMode(bg_split.getSelection().getActionCommand());
			return activity;
		}

		@Override
		public void setValues(Serializable value) {
			if (value instanceof WofoActivityBean) {
				WofoActivityBean activity = (WofoActivityBean) value;

				this.canInterrupt.setSelected(WofoActivityBean.CAN
						.equals(activity.getCanInterrupt()));
				this.canNotify.setSelected(WofoActivityBean.CAN.equals(activity
						.getExpireLoopRemind()));
				this.canReplevy.setSelected(WofoActivityBean.CAN
						.equals(activity.getCanReplevy()));
				this.canReplevy_to.setSelected(WofoActivityBean.CAN
						.equals(activity.getCanReplevyTo()));
				this.canUntread.setSelected(WofoActivityBean.CAN
						.equals(activity.getCanUntread()));
				this.canUntread_to.setSelected(WofoActivityBean.CAN
						.equals(activity.getCanUntreadTo()));
				nameCodeArea.setNameCode(activity.getActivityName(), activity
						.getActivityCode());
				this.limitPeriodField.setText(activity.getLimitTime() == 0 ? ""
						: String.valueOf(activity.getLimitTime()));
				this.departChooser.setDepartmentValue(activity
						.getEffectUnitId(), activity.getEffectDepartmentId());
				this.descField.setText(activity.getDescription());

				String iconID = iconMap.get(activity.getActivityIcon());
				if (iconID != null && !iconID.trim().equals("")) {
					this.iconCombo.setSelectedItemByID(iconID);
				} else {
					this.iconCombo.setSelectedIndex(defaultInconIndex);
				}

				this.join_andRadio.setSelected(WofoActivityBean.JOIN_MODE_AND
						.equals(activity.getJoinMode()));
				this.join_conditionRadio
						.setSelected(WofoActivityBean.JOIN_MODE_CONDITION
								.equals(activity.getJoinMode()));
				this.join_orRadio.setSelected(WofoActivityBean.JOIN_MODE_OR
						.equals(activity.getJoinMode()));
				// �ж��Ƿ�ѡ��ۺ�����
				boolean joinConditionEnabbled = join_conditionRadio
						.isSelected();
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
				// �ж��Ƿ�ѡ���֧����
				boolean splitConditionEnabbled = split_conditionRadio
						.isSelected();
				this.splitConditionCombo.setEnabled(splitConditionEnabbled);
				if (splitConditionEnabbled) {
					this.splitConditionCombo.setSelectedItemByID(activity
							.getSplitConditionId());
				} else {
					this.splitConditionCombo.setSelectedIndex(0);
				}

				this.typeField.setText(WofoResources
						.getValueByKey("workflow_activity_"
								+ activity.getActivityType().toLowerCase()));
				this.alarmPeriodField.setText(activity.getAlarmTime() == 0 ? ""
						: String.valueOf(activity.getAlarmTime()));
				this.workflowField.setText(editor.getOperationArea()
						.getCurrentPaintBoard().getProcessBean()
						.getProcessName());

				businessPerform.setValues(new String[] {
						activity.getAppPerformName(),
						activity.getAppPerformType(),
						activity.getAppPerformUrl() });
				businessView.setValues(new String[] {
						activity.getAppViewName(), activity.getAppViewType(),
						activity.getAppViewUrl() });
				businessUntreat.setValues(new String[] {
						activity.getAppUntreadName(),
						activity.getAppUntreadType(),
						activity.getAppUntreadUrl() });
			} else {
				new IllegalInputTypeException(WofoActivityBean.class, value
						.getClass());
			}
		}
	}

	private class ParticipantPanel extends WfInputPanel {

		JTable participantTable = new JTable();
		WfParticipantChooser particiPantChooser = new WfParticipantChooser(
				editor);

		public ParticipantPanel() {
			init();
		}

		@Override
		public void reset() {

			clearTable();
		}

		private void init() {

			particiPantChooser.getOkButton().addActionListener(
					new ActionListener() {

						public void actionPerformed(ActionEvent e) {
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									addSelectedParticipants();
								}
							});

						}
					});

			TitledSeparator sep = new TitledSeparator(new JLabel(),
					new PartialGradientLineBorder(new Color[] {
							Color.LIGHT_GRAY, Color.white }, 1,
							PartialSide.SOUTH),
					GlobalConstants.LABEL_ALIGH_POSITION);
			WfOperControllerButtonPanel buttonPanel = new WfOperControllerButtonPanel(
					participantTable, false) {
				@Override
				public Object[] getOneRowEmptyData() {
					return new Object[] { null, null, null };
				}
			};
			buttonPanel.getAddRowBtn().removeActionListener(
					buttonPanel.getAddRowBtn().getActionListeners()[0]);
			buttonPanel.getAddRowBtn().addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					particiPantChooser.showPopupChooser((JComponent) e
							.getSource(), 200, 280);

				}
			});
			participantTable.setModel(new javax.swing.table.DefaultTableModel(
					new Object[][] {}, new String[] { "����������", "������", "��Χ" }) {
				Class[] types = new Class[] { java.lang.String.class,
						java.lang.String.class, JComboBox.class };

				public Class getColumnClass(int columnIndex) {
					return types[columnIndex];
				}

				public boolean isCellEditable(int rowIndex, int columnIndex) {
					if (columnIndex != 2) {
						return false;
					}
					WofoParticipantScopeBean scope = (WofoParticipantScopeBean) getValueAt(
							rowIndex, 1);
					// ֻ�����ɫ�������ɫ�ɽ��б༭
					String participantType = scope.getParticipantType() == null ? ""
							: scope.getParticipantType();
					if (participantType
							.equals(WofoParticipantScopeBean.PARTICIPANT_TYPE_ROLE)
							|| participantType
									.equals(WofoParticipantScopeBean.PARTICIPANT_TYPE_VIRTUAL)) {
						return true;
					}
					return false;
				}
			});
			// ���������͸�ѡ��
			JComboBox participantScopeCombo = new JComboBox(
					new Object[] {
							new WfComboBean(
									WofoParticipantScopeBean.ROLE_EFFECT_SCOPE_UNIT,
									WofoResources
											.getValueByKey("participant_scope_"
													+ WofoParticipantScopeBean.ROLE_EFFECT_SCOPE_UNIT
															.toLowerCase())),
							new WfComboBean(
									WofoParticipantScopeBean.ROLE_EFFECT_SCOPE_DEPT,
									WofoResources
											.getValueByKey("participant_scope_"
													+ WofoParticipantScopeBean.ROLE_EFFECT_SCOPE_DEPT
															.toLowerCase())) });
			participantTable.getColumnModel().getColumn(2).setCellEditor(
					new DefaultCellEditor(participantScopeCombo));

			participantTable.getSelectionModel().setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
			participantTable.getColumnModel().getColumn(0)
					.setPreferredWidth(60);
			participantTable.getColumnModel().getColumn(1).setPreferredWidth(
					380);
			participantTable.getColumnModel().getColumn(2).setPreferredWidth(
					100);
			JideScrollPane scrollPane = new JideScrollPane(participantTable);
			scrollPane.setBorder(new EtchedBorder());
			setLayout(new GridBagLayout());
			GridBagConstraints rootCons = new GridBagConstraints();
			rootCons.insets = new Insets(3, 5, 3, 5);
			rootCons.gridx = 0;
			rootCons.gridy = 0;
			// add(left, rootCons);
			// rootCons.gridx = 1;
			// add(right, rootCons);
			// rootCons.gridx = 0;
			rootCons.gridy = 1;
			rootCons.weightx = 1;
			rootCons.gridwidth = 2;
			rootCons.fill = GridBagConstraints.HORIZONTAL;
			// add(sep, rootCons);
			// rootCons.gridx = 0;
			// rootCons.gridy = 2;
			rootCons.insets = new Insets(2, 5, 2, 5);
			add(buttonPanel, rootCons);
			rootCons.gridx = 0;
			rootCons.gridy = 3;
			rootCons.weightx = 1;
			rootCons.weighty = 1;
			rootCons.fill = GridBagConstraints.BOTH;
			add(scrollPane, rootCons);

		}

		/**
		 * ���������Ӳ�����
		 */
		private void addSelectedParticipants() {
			DefaultTableModel model = (DefaultTableModel) participantTable
					.getModel();
			// ��Ҫ�����������
			if (model.getRowCount() > 0) {
				for (int i = model.getRowCount(); i > 0; i--) {
					model.removeRow(i - 1);
				}
			}
			List<WofoUnitBean> departs = particiPantChooser
					.getSelectedDepartments();
			List<WofoUserBean> users = particiPantChooser.getSelectedStaffs();
			List<WofoRoleBean> roles = particiPantChooser.getSelectedRoles();
			List<WofoVirtualRoleBean> vRoles = particiPantChooser
					.getSelectedVRoles();
			WofoParticipantScopeBean p = null;

			// ��ѡ��Ĳ�����Ϣ�ӵ������
			for (WofoUnitBean unit : departs) {
				p = new WofoParticipantScopeBean(Functions.getUID());
				p.setActivityId(activity.getActivityId());
				p.setParticipantCode(unit.getUnitCode());
				p.setParticipantId(unit.getUnitId());
				p
						.setParticipantType(WofoParticipantScopeBean.PARTICIPANT_TYPE_UNIT);
				p.setParticipantName(unit.getUnitName());
				// other setters
				// ........
				model.addRow(new Object[] {
						WofoResources.getValueByKey("participant_scope_"
								+ p.getParticipantType().toLowerCase()), p,
						new WfComboBean("", "") });
			}
			// ��ѡ�����Ա��Ϣ�ӵ������
			for (WofoUserBean user : users) {
				p = new WofoParticipantScopeBean(Functions.getUID());
				p.setActivityId(activity.getActivityId());
				p.setParticipantCode(user.getUserCode());
				p.setParticipantId(user.getUserId());
				p
						.setParticipantType(WofoParticipantScopeBean.PARTICIPANT_TYPE_USER);
				p.setParticipantName(user.getUserName());
				model.addRow(new Object[] {
						WofoResources.getValueByKey("participant_scope_"
								+ p.getParticipantType().toLowerCase()), p,
						new WfComboBean("", "") });
			}
			// ��ѡ��Ľ�ɫ��Ϣ�ӵ������
			for (WofoRoleBean r : roles) {
				p = new WofoParticipantScopeBean(Functions.getUID());
				p.setActivityId(activity.getActivityId());
				p.setParticipantCode(r.getRoleCode());
				p.setParticipantId(r.getRoleId());
				p
						.setParticipantType(WofoParticipantScopeBean.PARTICIPANT_TYPE_ROLE);
				p.setParticipantName(r.getRoleName());
				WfComboBean defaultEffectScope = new WfComboBean(
						WofoParticipantScopeBean.ROLE_EFFECT_SCOPE_UNIT,
						WofoResources
								.getValueByKey("participant_scope_"
										+ WofoParticipantScopeBean.ROLE_EFFECT_SCOPE_UNIT));
				model.addRow(new Object[] {
						WofoResources.getValueByKey("participant_scope_"
								+ p.getParticipantType().toLowerCase()), p,
						defaultEffectScope });
			}
			// FIXME �����ɫ
			for (WofoVirtualRoleBean r : vRoles) {
				p = new WofoParticipantScopeBean(Functions.getUID());
				p.setActivityId(activity.getActivityId());
				p.setParticipantCode(r.getRoleCode());
				p.setParticipantId(r.getRoleId());
				p
						.setParticipantType(WofoParticipantScopeBean.PARTICIPANT_TYPE_VIRTUAL);
				p.setParticipantName(r.getRoleName());
				WfComboBean defaultEffectScope = new WfComboBean(
						WofoParticipantScopeBean.ROLE_EFFECT_SCOPE_UNIT,
						WofoResources
								.getValueByKey("participant_scope_"
										+ WofoParticipantScopeBean.ROLE_EFFECT_SCOPE_UNIT));
				model.addRow(new Object[] {
						WofoResources.getValueByKey("participant_scope_"
								+ p.getParticipantType().toLowerCase()), p,
						defaultEffectScope });
			}
		}

		/**
		 * ��ȡ�����߼���
		 * 
		 * @return
		 */
		private List<WofoParticipantScopeBean> getParticipantScopes() {
			DefaultTableModel model = (DefaultTableModel) participantTable
					.getModel();
			List<WofoParticipantScopeBean> scopes = new ArrayList<WofoParticipantScopeBean>();
			int size = model.getRowCount();
			WofoParticipantScopeBean scope;
			for (int i = 0; i < size; i++) {
				String effect = "";
				scope = (WofoParticipantScopeBean) model.getValueAt(i, 1);
				Object o = model.getValueAt(i, 2);
				if (o instanceof WfComboBean) {
					effect = ((WfComboBean) o).id;
				}
				scope.setParticipateOrder(i);
				scope.setRoleEffectScope(effect);
				scopes.add(scope);
			}
			return scopes;
		}

		/**
		 * ��ձ�������
		 */
		private void clearTable() {
			DefaultTableModel model = (DefaultTableModel) participantTable
					.getModel();
			int rows = model.getRowCount();
			for (int i = rows - 1; i >= 0; i--) {
				model.removeRow(i);
			}
		}

		@Override
		public Serializable applyValues(Serializable value) {

			activity.setParticipantScopes(getParticipantScopes());
			return activity;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void setValues(Serializable value) {
			WofoActivityBean activity = (WofoActivityBean) value;
			List<WofoParticipantScopeBean> scopes = activity
					.getParticipantScopes();
			if (scopes != null) {
				DefaultTableModel model = (DefaultTableModel) participantTable
						.getModel();
				for (WofoParticipantScopeBean scope : scopes) {
					String particiType = WofoResources
							.getValueByKey("participant_scope_"
									+ scope.getParticipantType().toLowerCase());
					String effectScopeTitle = "";
					String ecffectScope = scope.getRoleEffectScope() == null ? ""
							: scope.getRoleEffectScope().trim();
					if (!"".equals(ecffectScope)) {
						effectScopeTitle = WofoResources
								.getValueByKey("participant_scope_"
										+ ecffectScope.toLowerCase());
					}
					model.addRow(new Object[] { particiType, scope,
							new WfComboBean(ecffectScope, effectScopeTitle) });
				}
			}
		}
	}

	public WfModelPanel getBusinessPerform() {
		return basic.businessPerform;
	}

	public WfModelPanel getBusinessUntreat() {
		return basic.businessUntreat;
	}

	public WfModelPanel getBusinessView() {
		return basic.businessView;
	}
}
