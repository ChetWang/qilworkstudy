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
 * ��������������
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
	 * �ŵ�WfDialog�����µ��Զ���������������Ҫһ�����Σ�WfDialog���󣩵Ĺ��캯��
	 * 
	 * @param dialog
	 */
	public SmfActivityPanel(WfDialog dialog) {
		super(dialog);
	}

	/**
	 * ��ʼ���Ի���
	 */
	protected void init() {
		formLabel = new JLabel("ҵ�����");
		formField = new JTextField();
		tab = new JideTabbedPane();
		formField.setEditable(false);
		overlay = new WfBusyTextOverlayable(tab, "{\n���ڻ�ȡҵ��������:f:gray}");
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
		TitledSeparator sep = new TitledSeparator(new JLabel("ҵ�����"),
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
		// ���ж�����activityForm�д�����˲������´�������ֱ���޸�activityForm�Ϳ�
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

				// ��δȡ����ǰҪ��ģ��disable
				setActivityModelEnabled(false);

				final WfSwingWorker<ActivityFormConfigBean, Object> worker = new WfSwingWorker<ActivityFormConfigBean, Object>(
						"���ڻ�ȡҵ��\"" + actBean.getActivityName() + "\"������") {

					@Override
					protected ActivityFormConfigBean doInBackground()
							throws Exception {

						WofoProcessBean process = getProcessBean();
						ProcessFormConfigBean processForm = (ProcessFormConfigBean) process
								.getPluginProps().get(
										ProcessFormConfigBean.class.getName());
						// processForm�����ڣ�����Ҫ��ȡ��processForm
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
						// processFormȡ��ֵ�󣬸���processForm��ȡ���̻��
						if (processForm != null) {
							WofoNetBean netBean = new WofoNetBean(
									WofoActions.GET_ACTIVITY_FORM, dialog
											.getEditor().getUserID(), param);
							// �����������processForm
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
	 * ��Ի����ģ����Ч������
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
	 * ������ActivityFormConfigBean������ڣ����и�ֵ
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
	 * ���tab
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
	 * �������JTable
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
			JMenuItem selectAll = new JMenuItem("ȫѡ");
			JMenuItem deselectAll = new JMenuItem("ȫ��ѡ");
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
					"", "������", "�ɼ�", "�ɱ༭" }) {
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
		 * ȫѡ��ȡ��ȫѡ
		 */
		private void selectAllRows(boolean flag) {
			DefaultTableModel model = (DefaultTableModel) getModel();
			for (int i = 0; i < model.getRowCount(); i++) {
				model.setValueAt(flag, i, colIndex);
			}
		}

	}

}
