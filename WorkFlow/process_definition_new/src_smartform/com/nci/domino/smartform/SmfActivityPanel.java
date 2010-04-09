package com.nci.domino.smartform;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.jidesoft.swing.JidePopupMenu;
import com.jidesoft.swing.JideScrollPane;
import com.jidesoft.swing.JideTabbedPane;
import com.jidesoft.swing.PartialGradientLineBorder;
import com.jidesoft.swing.PartialSide;
import com.jidesoft.swing.TitledSeparator;
import com.nci.domino.GlobalConstants;
import com.nci.domino.beans.WofoActions;
import com.nci.domino.beans.WofoNetBean;
import com.nci.domino.beans.desyer.WofoActivityBean;
import com.nci.domino.beans.desyer.WofoProcessBean;
import com.nci.domino.beans.plugin.smartform.ActivityFormConfigBean;
import com.nci.domino.beans.plugin.smartform.FormBOAttributeBean;
import com.nci.domino.beans.plugin.smartform.FormBizObjectBean;
import com.nci.domino.beans.plugin.smartform.ProcessFormConfigBean;
import com.nci.domino.components.WfOverlayable;
import com.nci.domino.components.dialog.WfDialog;
import com.nci.domino.components.dialog.WfSetValueListener;
import com.nci.domino.components.dialog.activity.WfNewActivityHumanDialog;
import com.nci.domino.components.table.TableTextPositionRenderer;
import com.nci.domino.components.table.tablesorter.DefaultSimpleTableSortor;
import com.nci.domino.components.tree.WfBusyTextOverlayable;
import com.nci.domino.concurrent.WfSwingWorker;
import com.nci.domino.help.Functions;

/**
 * 活动表单输入输出对象
 * 
 * @author Qil.Wong
 * 
 */
public class SmfActivityPanel extends SmfPluginPanel {

	private JLabel formLabel;

	private JTextField formField;

	private JTabbedPane tab;

	private ActivityFormConfigBean activityForm;

	private WfOverlayable overlay;

	/**
	 * 放到WfDialog对象下的自定义第三方组件都需要一个带参（WfDialog对象）的构造函数
	 * 
	 * @param dialog
	 */
	public SmfActivityPanel(WfDialog dialog) {
		super(dialog);
	}

	/**
	 * 初始化对话框
	 */
	protected void init() {
		formLabel = new JLabel("业务表单：");
		formField = new JTextField();
		tab = new JideTabbedPane();
		formField.setEditable(false);
		overlay = new WfBusyTextOverlayable(tab, "{\n正在获取业务活动表单数据:f:gray}");
		overlay.setOpaque(false);
		setLayout(new GridBagLayout());
		GridBagConstraints cons = new GridBagConstraints();
		cons.fill = GridBagConstraints.BOTH;
		cons.insets = new Insets(10, 5, 5, 5);
		cons.anchor = GridBagConstraints.WEST;
		cons.gridx = 0;
		cons.gridy = 0;
		add(formLabel, cons);
		cons.gridx = 1;
		cons.weightx = 1;
		add(formField, cons);
		cons.insets = new Insets(5, 5, 2, 5);
		cons.gridx = 0;
		cons.gridy = 1;
		cons.gridwidth = 2;
		TitledSeparator sep = new TitledSeparator(new JLabel("业务对象"),
				new PartialGradientLineBorder(new Color[] { Color.LIGHT_GRAY,
						Color.white }, 1, PartialSide.SOUTH),
				GlobalConstants.LABEL_ALIGH_POSITION);
		add(sep, cons);
		cons.insets = new Insets(3, 5, 5, 5);
		cons.gridy = 2;
		cons.weighty = 1;
		add(overlay, cons);
		dialog.addSetValueListener(new WfSetValueListener() {

			public void beforeValueSet(Serializable value) {
				WofoProcessBean process = getProcessBean();
				ProcessFormConfigBean processForm = (ProcessFormConfigBean) process
						.getPluginProps().get(
								ProcessFormConfigBean.class.getName());
				boolean flag = processForm == null
						|| processForm.getFormId() == null
						|| processForm.getFormId().equals("");
				setActivityModelEnabled(flag);
			}

			public void afterValueSet(Serializable value) {
				// DO NOTHING
			}
		});
	}

	protected WofoProcessBean getProcessBean() {
		return dialog.getEditor().getOperationArea().getCurrentPaintBoard()
				.getProcessBean();
	}

	@Override
	public Serializable applyValues(Serializable value) {
		// 所有对象都在activityForm中处理，因此不需重新创建对象，直接修改activityForm就可
		int tabCount = tab.getTabCount();
		for (int i = 0; i < tabCount; i++) {
			JideScrollPane scroll = (JideScrollPane) tab.getComponentAt(i);
			JTable table = (JTable) scroll.getViewport().getView();
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			for (int x = 0; x < model.getRowCount(); x++) {
				FormBOAttributeBean attr = (FormBOAttributeBean) model
						.getValueAt(x, 1);
				attr.setDisplay((Boolean) model.getValueAt(x, 2));
				attr.setEditable((Boolean) model.getValueAt(x, 3));
			}
		}
		return value;
	}

	@Override
	public void reset() {
		formField.setText("");
		tab.removeAll();
		overlay.setOverlayVisible(false);
	}

	@Override
	public void setValues(Serializable value) {
		overlay.setOverlayVisible(true);
		try {
			final WofoActivityBean actBean = (WofoActivityBean) value;
			activityForm = (ActivityFormConfigBean) actBean.getPluginProps()
					.get(ActivityFormConfigBean.class.getName());
			if (activityForm == null) {
				activityForm = new ActivityFormConfigBean();
				activityForm.setActivityId(actBean.getActivityId());
				activityForm.setActivityName(actBean.getActivityName());
				final HashMap param = new HashMap();
				param.put(ActivityFormConfigBean.class.getName(), activityForm);

				// 在未取到表单前要将模型disable
				setActivityModelEnabled(false);

				final WfSwingWorker<ActivityFormConfigBean, Object> worker = new WfSwingWorker<ActivityFormConfigBean, Object>(
						"正在获取业务活动\"" + actBean.getActivityName() + "\"表单数据") {

					@Override
					protected ActivityFormConfigBean doInBackground()
							throws Exception {

						WofoProcessBean process = getProcessBean();
						ProcessFormConfigBean processForm = (ProcessFormConfigBean) process
								.getPluginProps().get(
										ProcessFormConfigBean.class.getName());
						// processForm不存在，则需要先取到processForm
						if (processForm == null) {
							processForm = new ProcessFormConfigBean();
							processForm.setProcessId(process.getProcessId());
							WofoNetBean netBean = new WofoNetBean(
									WofoActions.GET_PROCESS_FORM, dialog
											.getEditor().getUserID(),
									processForm);
							netBean = Functions.getReturnNetBean(dialog
									.getEditor().getServletPath(), netBean);
							if (netBean != null) {
								processForm = (ProcessFormConfigBean) netBean
										.getParam();
								if (processForm != null) {
									process.getPluginProps().put(
											ProcessFormConfigBean.class
													.getName(), processForm);
								}
							} else {
								processForm = null;
							}
						}
						// processForm取到值后，根据processForm读取流程活动表单
						if (processForm != null) {
							WofoNetBean netBean = new WofoNetBean(
									WofoActions.GET_ACTIVITY_FORM, dialog
											.getEditor().getUserID(), param);
							// 请求参数增加processForm
							param.put(ProcessFormConfigBean.class.getName(),
									processForm);
							netBean = Functions.getReturnNetBean(dialog
									.getEditor().getServletPath(), netBean);
							return (ActivityFormConfigBean) netBean.getParam();
						}
						return null;
					}

					@Override
					public void wfDone() {
						try {
							activityForm = get();
							setValueIfEixst(actBean);
							overlay.setOverlayVisible(false);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				};
				worker.setCancelListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dialog.getEditor().getStatusBar().stopShowInfo(
								worker.getTipInfo());
						overlay.setOverlayVisible(false);
					}
				});
				dialog.getEditor().getBackgroundManager().enqueueOpertimeQueue(
						worker);
				dialog.addWindowListener(new WindowAdapter() {
					public void windowClosed(WindowEvent e) {
						if (!worker.isDone()) {
							worker.cancelWorking(true);
						}
					}
				});
			} else {
				setValueIfEixst(actBean);
				overlay.setOverlayVisible(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			overlay.setOverlayVisible(false);
		}
	}

	/**
	 * 活动对话框的模型有效性设置
	 * 
	 * @param flag
	 */
	private void setActivityModelEnabled(boolean flag) {
		WfNewActivityHumanDialog actdialog = (WfNewActivityHumanDialog) dialog;
		actdialog.getBusinessPerform().setEnabled(flag);
		actdialog.getBusinessUntreat().setEnabled(flag);
		actdialog.getBusinessView().setEnabled(flag);
	}

	/**
	 * 如果活动的ActivityFormConfigBean对象存在，进行赋值
	 */
	private void setValueIfEixst(WofoActivityBean actBean) {
		ProcessFormConfigBean processForm = (ProcessFormConfigBean) getProcessBean()
				.getPluginProps().get(ProcessFormConfigBean.class.getName());
		if (processForm != null) {
			boolean flag = processForm == null
					|| processForm.getFormId() == null
					|| processForm.getFormId().equals("");
			setActivityModelEnabled(flag);
			formField.setText(processForm.getFormName());
			List businessObj = activityForm.getBusinessObjects();
			for (int i = 0; i < businessObj.size(); i++) {
				addTabComponent((FormBizObjectBean) businessObj.get(i));
			}
			actBean.getPluginProps().put(
					ActivityFormConfigBean.class.getName(), activityForm);
		} else {
			setActivityModelEnabled(true);
		}
	}

	/**
	 * 添加tab
	 * 
	 * @param bizObj
	 */
	private void addTabComponent(FormBizObjectBean bizObj) {
		SmfActFormTable table = new SmfActFormTable();
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		JideScrollPane scroll = new JideScrollPane(table);
		tab.addTab(bizObj.getBoName(), null, scroll, null);
		List attrs = bizObj.getAttributes();
		for (int i = 0; i < attrs.size(); i++) {
			FormBOAttributeBean attr = (FormBOAttributeBean) attrs.get(i);
			model.addRow(new Object[] { i + 1, attr, attr.isDisplay(),
					attr.isEditable() });
		}
	}

	/**
	 * 活动表单属性JTable
	 * 
	 * @author Qil.Wong
	 * 
	 */
	private class SmfActFormTable extends JTable {

		JidePopupMenu pop = new JidePopupMenu();

		private int colIndex;

		private SmfActFormTable() {
			init();
		}

		private void init() {
			JMenuItem selectAll = new JMenuItem("全选");
			JMenuItem deselectAll = new JMenuItem("全不选");
			selectAll.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					selectAllRows(true);
				}

			});
			deselectAll.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					selectAllRows(false);
				}
			});
			pop.add(selectAll);
			pop.add(deselectAll);

			setModel(new DefaultTableModel(new Object[][] {}, new String[] {
					"", "表单属性", "可见", "可编辑" }) {
				Class[] types = new Class[] { Integer.class, String.class,
						Boolean.class, Boolean.class };

				public Class getColumnClass(int columnIndex) {
					return types[columnIndex];
				}

				public boolean isCellEditable(int rowIndex, int columnIndex) {
					return columnIndex == 2 || columnIndex == 3;
				}
			});
			getColumnModel().getColumn(0).setPreferredWidth(20);
			getColumnModel().getColumn(1).setPreferredWidth(450);
			getColumnModel().getColumn(2).setPreferredWidth(60);
			getColumnModel().getColumn(3).setPreferredWidth(60);
			getColumnModel().getColumn(0).setCellRenderer(
					new TableTextPositionRenderer(SwingConstants.CENTER));
			new DefaultSimpleTableSortor(this).initSortHeader();
			getTableHeader().addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getButton() == 3) {
						JTableHeader header = (JTableHeader) e.getSource();
						colIndex = header.getColumnModel().getColumnIndexAtX(
								e.getX());
						if (colIndex == 2 || colIndex == 3)
							pop.show(header, e.getX(), e.getY());
					}
				}
			});

		}

		/**
		 * 全选或取消全选
		 */
		private void selectAllRows(boolean flag) {
			DefaultTableModel model = (DefaultTableModel) getModel();
			for (int i = 0; i < model.getRowCount(); i++) {
				model.setValueAt(flag, i, colIndex);
			}
		}

	}

}
