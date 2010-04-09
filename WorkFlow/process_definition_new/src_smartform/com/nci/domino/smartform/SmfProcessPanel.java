package com.nci.domino.smartform;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.AbstractButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JideScrollPane;
import com.jidesoft.swing.PartialGradientLineBorder;
import com.jidesoft.swing.PartialSide;
import com.jidesoft.swing.TitledSeparator;
import com.nci.domino.GlobalConstants;
import com.nci.domino.beans.WofoActions;
import com.nci.domino.beans.WofoNetBean;
import com.nci.domino.beans.desyer.WofoActivityBean;
import com.nci.domino.beans.desyer.WofoArgumentsBean;
import com.nci.domino.beans.desyer.WofoProcessBean;
import com.nci.domino.beans.org.WofoRoleBean;
import com.nci.domino.beans.plugin.smartform.ActivityFormConfigBean;
import com.nci.domino.beans.plugin.smartform.FormBOAttributeBean;
import com.nci.domino.beans.plugin.smartform.FormBizObjectBean;
import com.nci.domino.beans.plugin.smartform.ProcessFormConfigBean;
import com.nci.domino.beans.plugin.smartform.SmartFormBean;
import com.nci.domino.components.WfOperControllerButtonPanel;
import com.nci.domino.components.WfParticipantChooser;
import com.nci.domino.components.dialog.WfDialog;
import com.nci.domino.components.dialog.WfSetValueListener;
import com.nci.domino.components.dialog.process.WfNewProcessDialog;
import com.nci.domino.components.table.TableTextPositionRenderer;
import com.nci.domino.components.table.tablesorter.DefaultSimpleTableSortor;
import com.nci.domino.concurrent.WfSwingWorker;
import com.nci.domino.domain.WofoBaseDomain;
import com.nci.domino.help.Functions;

/**
 * 智能表单在流程定义对话框下的自定义组件面板
 * 
 * @author Qil.Wong
 * 
 */
public class SmfProcessPanel extends SmfPluginPanel {

	private JLabel formLabel;
	private JTextField formField;
	private AbstractButton formSelectBtn;
	private WfParticipantChooser roleChooser;
	private SmfFormChooser formChooser;
	private JTable roleTable;
	private JideButton formCancelBtn;

	// 后台请求进程
	private WfSwingWorker<ProcessFormConfigBean, Object> worker;

	private ProcessFormConfigBean processFormBean;

	private List<WofoRoleBean> selectedRoles;

	private WofoProcessBean process;

	private String defaultURL;

	/**
	 * 放到WfDialog对象下的自定义第三方组件都需要一个带参（WfDialog对象）的构造函数
	 * 
	 * @param dialog
	 */
	public SmfProcessPanel(WfDialog dialog) {
		super(dialog);
	}

	/**
	 * 初始化
	 */
	protected void init() {
		defaultURL = "";
		formLabel = new JLabel("表单：");
		formField = new JTextField();
		formSelectBtn = new JideButton("选择");
		formCancelBtn = new JideButton(Functions.getImageIcon("gc.gif"));
		selectedRoles = new ArrayList<WofoRoleBean>();
		formCancelBtn.setToolTipText("撤销已经关联的表单");
		formSelectBtn.setToolTipText("选择需要关联的表单");
		formCancelBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				reset();
				checkProcessModel();
			}
		});
		roleChooser = new WfParticipantChooser(dialog.getEditor(),
				new boolean[] { false, false, true, false });
		formChooser = new SmfFormChooser(dialog.getEditor());
		formChooser.setSelectionMode(SmfFormChooser.SELECTED_FORMBEAN);
		formChooser.addFormSelectedListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formSelected();
			}
		});
		setLayout(new GridBagLayout());
		formField.setEditable(false);
		formSelectBtn.setHorizontalTextPosition(AbstractButton.LEFT);
		formSelectBtn.setIcon(Functions.getImageIcon("splitbtn.gif"));
		formSelectBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formChooser.showPopup(formSelectBtn);
			}
		});
		JPanel formSelectPanel = new JPanel(new GridBagLayout());
		GridBagConstraints cons = new GridBagConstraints();
		cons.fill = GridBagConstraints.BOTH;
		cons.insets = new Insets(0, 5, 0, 5);
		cons.anchor = GridBagConstraints.WEST;
		cons.gridx = 0;
		cons.gridy = 0;
		formSelectPanel.add(formLabel, cons);
		cons.gridx = 1;
		cons.fill = GridBagConstraints.HORIZONTAL;
		cons.weightx = 1;
		formSelectPanel.add(formField, cons);
		cons.gridx = 2;
		cons.weightx = 0;
		cons.insets = new Insets(0, 3, 0, 1);
		formSelectPanel.add(formSelectBtn, cons);
		cons.insets = new Insets(0, 1, 0, 5);
		cons.gridx = 3;
		formSelectPanel.add(formCancelBtn, cons);
		GridBagConstraints rootCons = new GridBagConstraints();
		rootCons.insets = new Insets(5, 0, 0, 0);
		rootCons.fill = GridBagConstraints.BOTH;
		rootCons.gridx = 0;
		rootCons.gridy = 0;
		rootCons.weightx = 1;
		add(formSelectPanel, rootCons);

		roleTable = createRoleTable();
		final WfOperControllerButtonPanel buttonPanel = new WfOperControllerButtonPanel(
				roleTable, true);
		buttonPanel.removeDefaultListeners(buttonPanel.getAddRowBtn());
		buttonPanel.getAddRowBtn().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				roleChooser.showPopupChooser(buttonPanel.getAddRowBtn(), 200,
						280);
			}
		});
		buttonPanel.getUpRowBtn().setVisible(false);
		buttonPanel.getDownRowBtn().setVisible(false);

		rootCons.gridy = 3;
		rootCons.insets = new Insets(2, 5, 2, 5);
		add(buttonPanel, rootCons);
		/**************************************************************/

		JideScrollPane roleScrool = new JideScrollPane(roleTable);
		TitledSeparator startTitle = new TitledSeparator(new JLabel("发起角色"),
				new PartialGradientLineBorder(new Color[] { Color.LIGHT_GRAY,
						Color.white }, 1, PartialSide.SOUTH),
				GlobalConstants.LABEL_ALIGH_POSITION);
		rootCons.insets = new Insets(10, 5, 2, 5);
		rootCons.gridy = 2;
		add(startTitle, rootCons);
		rootCons.insets = new Insets(0, 5, 5, 5);
		rootCons.weighty = 1;
		rootCons.gridy = 4;
		add(roleScrool, rootCons);
		initProps();

		dialog.addSetValueListener(new WfSetValueListener() {
			public void beforeValueSet(Serializable value) {
				WfNewProcessDialog processDialog = (WfNewProcessDialog) dialog;
				processDialog.getModelPanel().setEnabled(false);
			}

			public void afterValueSet(Serializable value) {
			}
		});
	}

	private void initProps() {
		Properties p = new Properties();
		InputStream is = getClass().getResourceAsStream(
				"/resources/smartform.props");
		try {
			p.load(is);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		defaultURL = p.getProperty("defaultURL");
	}

	protected WofoProcessBean getProcessBean() {
		return process;
	}

	/**
	 * 选择form后的动作
	 */
	private void formSelected() {
		SmartFormBean selectedFormBean = (SmartFormBean) formChooser
				.getSelectedValue();
		if (processFormBean != null
				&& processFormBean.getFormId() != null
				&& !selectedFormBean.getFormId().equals(
						processFormBean.getFormId())) {
			reset();
		}
		processFormBean = new ProcessFormConfigBean();
		processFormBean.setFormId(selectedFormBean.getFormId());
		processFormBean.setFormName(selectedFormBean.getFormName());
		formField.setText(selectedFormBean.toString());
		formChooser.hide();
		checkProcessModel();
	}

	/**
	 * 创建角色表格
	 * 
	 * @return
	 */
	private JTable createRoleTable() {
		JTable roltTable = new JTable();
		roltTable.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "", "角色名称", "角色描述" }) {
			Class[] types = new Class[] { Integer.class, String.class,
					String.class };

			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return false;
			}
		});
		roleChooser.getOkButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addSelectedRoles();
			}
		});
		new DefaultSimpleTableSortor(roltTable).initSortHeader();
		roltTable.getColumnModel().getColumn(0).setPreferredWidth(30);
		roltTable.getColumnModel().getColumn(1).setPreferredWidth(100);
		roltTable.getColumnModel().getColumn(2).setPreferredWidth(500);
		roltTable.getColumnModel().getColumn(0).setCellRenderer(
				new TableTextPositionRenderer(SwingConstants.CENTER));
		return roltTable;
	}

	/**
	 * 添加选择的角色
	 */
	private void addSelectedRoles() {
		DefaultTableModel model = (DefaultTableModel) roleTable.getModel();
		for (WofoRoleBean r : roleChooser.getSelectedRoles()) {
			model.addRow(new Object[] { model.getRowCount() + 1, r,
					r.getRoleCode() });
		}
	}

	@Override
	public Serializable applyValues(Serializable value) {
		WofoProcessBean process = (WofoProcessBean) value;
		Map formProperties = process.getPluginProps();
		ProcessFormConfigBean previousFormBean = (ProcessFormConfigBean) formProperties
				.get(ProcessFormConfigBean.class.getName());
		String previousFormID = previousFormBean == null ? null
				: previousFormBean.getFormId();
		formProperties.put(ProcessFormConfigBean.class.getName(),
				processFormBean);
		selectedRoles.clear();
		DefaultTableModel model = (DefaultTableModel) roleTable.getModel();
		for (int i = 0; i < model.getRowCount(); i++) {
			selectedRoles.add((WofoRoleBean) model.getValueAt(i, 1));
		}
		if (processFormBean != null) {
			processFormBean.getRoles().clear();
			processFormBean.getRoles().addAll(selectedRoles);
			processFormBean.setProcessId(process.getProcessId());
		}
		if (formField.getText().equals("")) { // 被清除,不能设置为null，否则服务端无法识别
			processFormBean.setFormId("");
			processFormBean.setFormName("");
			processFormBean.getRoles().clear();
		}
		if (processFormBean != null && processFormBean.getFormId() != null
				&& !processFormBean.getFormId().equals("")) { // 已设置表单
			process.setAppViewName(processFormBean.getFormName());
			process.setAppViewType(WofoProcessBean.APP_TYPE_JFW);
			process.setAppViewUrl(defaultURL);
		}
		List graphs = process.getGraphs();
		for (int i = 0; i < graphs.size(); i++) {
			if (graphs.get(i) instanceof WofoActivityBean) {
				WofoActivityBean act = (WofoActivityBean) graphs.get(i);
				if (previousFormBean != processFormBean
						|| formField.getText().equals("")) {// form变化(更改或删除)，导致其流程下所有活动的表单都要变化（清除）
					act.getPluginProps().remove(
							ActivityFormConfigBean.class.getName());
				}
				if (processFormBean != null
						&& processFormBean.getFormId() != null
						&& !processFormBean.getFormId().equals("")) {// 说明关联了表单
					act.setAppPerformName(processFormBean.getFormName());
					act.setAppPerformType(WofoActivityBean.APP_TYPE_JFW);
					act.setAppPerformUrl(defaultURL);
					act.setAppUntreadName(processFormBean.getFormName());
					act.setAppUntreadType(WofoActivityBean.APP_TYPE_JFW);
					act.setAppUntreadUrl(defaultURL);
					act.setAppViewName(processFormBean.getFormName());
					act.setAppViewType(WofoActivityBean.APP_TYPE_JFW);
					act.setAppViewUrl(defaultURL);
				}
				if ((previousFormID != null && !previousFormID.equals(""))
						&& (processFormBean == null
								|| processFormBean.getFormId() == null || processFormBean
								.getFormId().equals(""))) {
					act.setAppPerformName("");
					act.setAppPerformType("");
					act.setAppPerformUrl("");
					act.setAppUntreadName("");
					act.setAppUntreadType("");
					act.setAppUntreadUrl("");
					act.setAppViewName("");
					act.setAppViewType("");
					act.setAppViewUrl("");
				}
			}
		}
		return process;
	}

	@Override
	public void reset() {
		if (worker != null) {
			worker.cancelWorking(true);
			worker = null;
		}
		DefaultTableModel model = (DefaultTableModel) roleTable.getModel();
		for (int i = model.getRowCount() - 1; i >= 0; i--) {
			model.removeRow(i);
		}
		formField.setText("");
	}

	@Override
	public void setValues(Serializable value) {
		process = (WofoProcessBean) value;
		processFormBean = (ProcessFormConfigBean) process.getPluginProps().get(
				ProcessFormConfigBean.class.getName());
		if (processFormBean == null) {
			worker = new WfSwingWorker<ProcessFormConfigBean, Object>(
					"正在获取流程\"" + process.getProcessName() + "\"表单数据") {

				@Override
				protected ProcessFormConfigBean doInBackground()
						throws Exception {
					ProcessFormConfigBean processFormBean = new ProcessFormConfigBean();
					processFormBean.setProcessId(process.getProcessId());
					WofoNetBean netBean = new WofoNetBean(
							WofoActions.GET_PROCESS_FORM, dialog.getEditor()
									.getUserID(), processFormBean);
					netBean = Functions.getReturnNetBean(dialog.getEditor()
							.getServletPath(), netBean);
					return (ProcessFormConfigBean) netBean.getParam();
				}

				public void wfDone() {
					try {
						processFormBean = (ProcessFormConfigBean) get();
						process.getPluginProps().put(
								ProcessFormConfigBean.class.getName(),
								processFormBean);
						setValueIfExist();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			worker.setCancelListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					dialog.getEditor().getStatusBar().stopShowInfo(
							worker.getTipInfo());
				}
			});
			dialog.getEditor().getBackgroundManager().enqueueOpertimeQueue(
					worker);
		} else {
			setValueIfExist();
		}
	}

	/**
	 * 在有表单时，填入值
	 */
	private void setValueIfExist() {
		this.formField.setText(processFormBean.getFormName());
		DefaultTableModel model = (DefaultTableModel) roleTable.getModel();
		selectedRoles.clear();
		selectedRoles.addAll(processFormBean.getRoles());
		for (WofoRoleBean r : selectedRoles) {
			model.addRow(new Object[] { model.getRowCount() + 1, r,
					r.getRoleCode() });
		}
		checkProcessModel();
	}

	private void checkProcessModel() {
		boolean formRelated = !formField.getText().trim().equals("");
		WfNewProcessDialog processDialog = (WfNewProcessDialog) dialog;
		processDialog.getModelPanel().setEnabled(!formRelated);
	}

}
