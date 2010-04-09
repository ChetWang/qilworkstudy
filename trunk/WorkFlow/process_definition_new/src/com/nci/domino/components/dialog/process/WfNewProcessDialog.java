package com.nci.domino.components.dialog.process;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.tree.DefaultMutableTreeNode;

import com.jidesoft.spinner.DateSpinner;
import com.jidesoft.swing.JideScrollPane;
import com.jidesoft.swing.JideTabbedPane;
import com.jidesoft.swing.PartialGradientLineBorder;
import com.jidesoft.swing.PartialSide;
import com.jidesoft.swing.TitledSeparator;
import com.nci.domino.GlobalConstants;
import com.nci.domino.WfEditor;
import com.nci.domino.beans.desyer.WofoPackageBean;
import com.nci.domino.beans.desyer.WofoProcessBean;
import com.nci.domino.components.WfBannerPanel;
import com.nci.domino.components.WfDepartmentChooser;
import com.nci.domino.components.WfInputPanel;
import com.nci.domino.components.WfModelPanel;
import com.nci.domino.components.WfNameCodeArea;
import com.nci.domino.components.dialog.IllegalInputTypeException;
import com.nci.domino.components.dialog.WfDialog;
import com.nci.domino.components.dialog.WfSetValueListener;
import com.nci.domino.help.Functions;

/**
 * 流程定义新建和属性的对话框
 * 
 * @author Qil.Wong
 * 
 */
public class WfNewProcessDialog extends WfDialog {

	private static final long serialVersionUID = 4956788872828827323L;

	private String validTimeFormat = "yyyy/MM/dd HH:mm";

	public WfNewProcessDialog(WfEditor editor, String title, boolean modal) {
		super(editor, title, modal);
		// 650, 470,
		defaultWidth = 520;
		defaultHeight = 390;
	}

	@Override
	protected String checkInput() {
		if (this.processContent.nameCodeArea.getCode().equals("")) {
			return "流程编码不能为空";
		}
		if (this.processContent.nameCodeArea.getName().equals("")) {
			return "流程名称不能为空";
		}
		if (processContent.validTimeCheck.isSelected()) {
			SimpleDateFormat sdf = new SimpleDateFormat(this.validTimeFormat);
			try {
				if (sdf.parse(
						processContent.validTimeSpinner._timeEditor
								.getTextField().getText()).after(
						sdf.parse(processContent.invalidTimeSpinner._timeEditor
								.getTextField().getText()))) {
					return "有效时间不能在无效时间之后";
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return super.checkInput();
	}

	@Override
	protected void clearContents() {
		super.clearContents();
		((JideTabbedPane) getContentPanel()).setSelectedIndex(0);
		defalutValue = null;
		processContent.reset();
		modelPanel.reset();
	}

	@Override
	public Serializable getInputValues() {
		WofoProcessBean processBean = (WofoProcessBean) super.getInputValues();
		if (processBean == null) {
			processBean = new WofoProcessBean(Functions.getUID());
		}
		processContent.applyValues(processBean);
		String[] name_type_url = (String[]) modelPanel.applyValues(processBean);
		processBean.setAppViewName(name_type_url[0]);
		processBean.setAppViewType(name_type_url[1]);
		processBean.setAppViewUrl(name_type_url[2]);
		// ......
		processBean.setDisplayOrder(editor.getOperationArea().getWfTree()
				.getSelectedNode().getChildCount() + 1);
		return processBean;

	}

	@Override
	public void setInputValues(Serializable value) {
		if (value instanceof WofoProcessBean) {
			super.setInputValues(value);
			defalutValue = value;
			processContent.setValues(value);
			WofoProcessBean processBean = (WofoProcessBean) value;
			modelPanel
					.setValues(new String[] { processBean.getAppViewName(),
							processBean.getAppViewType(),
							processBean.getAppViewUrl() });
		} else {
			new IllegalInputTypeException(WofoProcessBean.class, value
					.getClass()).printStackTrace();
		}
	}

	@Override
	public JComponent createBannerPanel() {
		WfBannerPanel headerPanel1 = new WfBannerPanel("流程定义",
				"流程定义是定义一个业务流程，包括各类活动环节定义，环节之间的关系和条件", Functions
						.getImageIcon("new_process_set.gif"));
		return headerPanel1.getGlassBanner();
	}

	private WfModelPanel modelPanel;

	private NewProcessContentPanel processContent;

	@Override
	public JComponent createContentPanel() {
		processContent = new NewProcessContentPanel();
		contentTab.addTab("属性", null, processContent, null);
		modelPanel = new WfModelPanel(this);
		contentTab.addTab("模型", null, modelPanel, null);
		contentTab.addTab("事件", null, new JPanel(), null);
		return contentTab;
	}

	/**
	 * 新流程内容的添加面板
	 * 
	 * @author Qil.Wong
	 * 
	 */
	private class NewProcessContentPanel extends WfInputPanel {

		private static final long serialVersionUID = -181014247976375962L;

		private JTextField departmentField;
		private JLabel fromLabel;
		private DateSpinner invalidTimeSpinner;
		private JScrollPane descScroll;
		private JTextField packageNameField;
		private JLabel masterProcessNameLabel;

		private JTextArea processDescArea;
		private JLabel processDescLabel;
		private WfNameCodeArea nameCodeArea = new WfNameCodeArea("名称：", "编码：",
				WfNewProcessDialog.this);
		private JLabel toLabel;
		private JCheckBox validTimeCheck;
		private DateSpinner validTimeSpinner;
		private TitledSeparator validTimeSep;
		private WfDepartmentChooser departChooser;

		public NewProcessContentPanel() {
			panelName = "属性";
			initComponents();
		}

		public void reset() {
			packageNameField.setText("");
			nameCodeArea.reset();
			processDescArea.setText("");
			validTimeCheck.setSelected(false);
			departChooser.clearContents();
			hideValidTimeSetting(true);
			((SpinnerDateModel) validTimeSpinner.getModel())
					.setValue(new Date());
			((SpinnerDateModel) invalidTimeSpinner.getModel())
					.setValue(new Date());
		}

		private void initComponents() {
			GridBagConstraints gridBagConstraints;
			masterProcessNameLabel = new JLabel();
			packageNameField = new JTextField();
			packageNameField.setEditable(false);

			processDescLabel = new JLabel();
			descScroll = new JideScrollPane();
			processDescArea = new JTextArea();
			processDescArea.setAutoscrolls(true);
			departmentField = new JTextField();
			departmentField.setEditable(false);
			validTimeCheck = new JCheckBox();
			fromLabel = new JLabel();

			validTimeSep = new TitledSeparator(validTimeCheck,
					new PartialGradientLineBorder(new Color[] {
							Color.LIGHT_GRAY, Color.white }, 2,
							PartialSide.SOUTH),
					GlobalConstants.LABEL_ALIGH_POSITION);
			validTimeSpinner = new DateSpinner(validTimeFormat);
			toLabel = new JLabel();
			invalidTimeSpinner = new DateSpinner(validTimeFormat);

			setLayout(new GridBagLayout());

			masterProcessNameLabel.setText("所属流程：");
			masterProcessNameLabel
					.setHorizontalTextPosition(GlobalConstants.LABEL_ALIGH_POSITION);
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.insets = new Insets(5, 5, 5, 5);
			// wangql remove掉
			// add(masterProcessNameLabel, gridBagConstraints);
			// gridBagConstraints = new GridBagConstraints();
			// gridBagConstraints.gridx = 1;
			// gridBagConstraints.gridy = 0;
			// gridBagConstraints.gridwidth = 7;
			// gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			// gridBagConstraints.weightx = 1.0;
			// gridBagConstraints.insets = new Insets(5, 5, 5, 5);
			// add(packageNameField, gridBagConstraints);

			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.insets = new Insets(5, 5, 5, 5);
			nameCodeArea.init(0, 1, 7, this, gridBagConstraints);
			processDescLabel.setText("流程描述：");
			processDescLabel
					.setHorizontalTextPosition(GlobalConstants.LABEL_ALIGH_POSITION);
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 3;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
			gridBagConstraints.insets = new Insets(5, 5, 5, 5);
			add(processDescLabel, gridBagConstraints);
			processDescArea.setLineWrap(true);
			descScroll.setViewportView(processDescArea);

			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 3;
			gridBagConstraints.gridwidth = 7;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.insets = new Insets(5, 5, 5, 5);
			add(descScroll, gridBagConstraints);
			departChooser = new WfDepartmentChooser(editor, this);
			departChooser.init(0, 4, 5);
			validTimeCheck.setText("时间有效性");
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 7;
			gridBagConstraints.gridwidth = 6;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints.weightx = 1.0;
			add(validTimeSep, gridBagConstraints);
			fromLabel.setText("从");
			fromLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			toLabel.setText("至");
			toLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			JPanel validTimeSettingPanel = new JPanel(new FlowLayout());
			validTimeSettingPanel.add(fromLabel);
			validTimeSettingPanel.add(validTimeSpinner);
			validTimeSettingPanel.add(toLabel);
			validTimeSettingPanel.add(invalidTimeSpinner);
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 8;
			// gridBagConstraints.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			add(validTimeSettingPanel, gridBagConstraints);

			validTimeCheck.setToolTipText("选择该选项进行有效时间的设置，不选则在任何时间有效");
			validTimeCheck.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					hideValidTimeSetting(!validTimeCheck.isSelected());
				}
			});
			// 初始化时隐藏时间有效性设置
			hideValidTimeSetting(true);
		}

		@Override
		public Serializable applyValues(Serializable value) {
			WofoProcessBean p = (WofoProcessBean) value;

			if (validTimeCheck.isSelected()) {
				SimpleDateFormat sdf = new SimpleDateFormat(validTimeFormat);
				try {
					p.setActiveTime(sdf.parse(validTimeSpinner._timeEditor
							.getTextField().getText()));
					p.setDisableTime((sdf.parse(invalidTimeSpinner._timeEditor
							.getTextField().getText())));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

			// p.setAppViewUrl(appViewUrl)
			p.setCreateDate(new Date());
			p.setCreatorId(editor.getUserID());
			// p.setCreatorUnitId(creatorUnitId)
			// p.setDeployStatus(deployStatus)
			p.setDescription(processDescArea.getText().trim());
			// p.setDisplayOrder(displayOrder)
			p.setEffectDepartmentId(departChooser.getDepartmentID());
			p.setEffectUnitId(departChooser.getCompanyID());

			p.setProcessCode(nameCodeArea.getCode());
			// p.setProcessfile(processfile)
			// p.setProcessMasterId(packageBean.getProcessMasterId());
			p.setProcessName(nameCodeArea.getName());
			// p.setProcessscope(processscope)
			// p.setProcessStatus(processStatus)
			// p.setProcessVersion(processVersion)
			// p.setProcessVersionDefault(processVersionDefault)
			// p.setProcessVersionDesc(processVersionDesc)
			// p.setUpdateDate(updateDate)
			return p;
		}

		@Override
		public void setValues(Serializable value) {
			WofoProcessBean p = (WofoProcessBean) value;
			Object packageBean = ((DefaultMutableTreeNode) editor
					.getOperationArea().getWfTree().getSelectedNode())
					.getUserObject();
			if (packageBean instanceof WofoProcessBean) {
				packageBean = ((DefaultMutableTreeNode) editor
						.getOperationArea().getWfTree().getSelectedNode()
						.getParent()).getUserObject();
			}
			this.packageNameField.setText(((WofoPackageBean) packageBean)
					.getPackageName());
			nameCodeArea.setNameCode(p.getProcessName(), p.getProcessCode());
			this.processDescArea.setText(p.getDescription());
			this.departChooser.setDepartmentValue(p.getEffectUnitId(), p
					.getEffectDepartmentId());
		}

		private void hideValidTimeSetting(boolean hide) {
			validTimeSpinner.setEnabled(!hide);
			invalidTimeSpinner.setEnabled(!hide);
		}

	}

	public WfModelPanel getModelPanel() {
		return modelPanel;
	}

}
