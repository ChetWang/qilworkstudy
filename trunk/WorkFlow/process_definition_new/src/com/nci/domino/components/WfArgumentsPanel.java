package com.nci.domino.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.jidesoft.popup.JidePopup;
import com.jidesoft.swing.CheckBoxList;
import com.jidesoft.swing.JideScrollPane;
import com.jidesoft.swing.JideSplitButton;
import com.nci.domino.PaintBoard;
import com.nci.domino.beans.WofoActions;
import com.nci.domino.beans.WofoCodeBean;
import com.nci.domino.beans.WofoNetBean;
import com.nci.domino.beans.desyer.WofoArgumentsBean;
import com.nci.domino.cache.WfCacheManager;
import com.nci.domino.components.dialog.WfDialog;
import com.nci.domino.components.dialog.WfDialogPluginPanel;
import com.nci.domino.components.table.TableTextPositionRenderer;
import com.nci.domino.components.table.tablesorter.DefaultSimpleTableSortor;
import com.nci.domino.help.Functions;
import com.nci.domino.help.WofoResources;

/**
 * 流程和活动的参数界面
 * 
 * @author Qil.Wong
 * 
 */
public abstract class WfArgumentsPanel extends WfDialogPluginPanel {

	protected JTable argumentsTable;

	protected JideSplitButton importBtn;

	protected WfOperControllerButtonPanel tableOperTool;

	/**
	 * 参数面板构造函数
	 * 
	 * @param dialog
	 */
	public WfArgumentsPanel(WfDialog dialog) {
		super(dialog);
		init();
		panelName = "参数";
	}

	private void init() {
		initTable();
		tableOperTool = new WfOperControllerButtonPanel(argumentsTable, true) {
			@Override
			public Object[] getOneRowEmptyData() {
				WofoArgumentsBean a = new WofoArgumentsBean();
				a.setArgId(Functions.getUID());

				Object[] data = new Object[] { null, null, null, null, null,
						null, null, a };
				return data;
			}
		};
		importBtn = new JideSplitButton(Functions
				.getImageIcon("import_edit.gif"));
		importBtn.setToolTipText("从别处导入参数");
		importBtn.setFocusable(false);
		initImport(importBtn);
		tableOperTool.addControlComp(importBtn);
		setLayout(new GridBagLayout());
		GridBagConstraints cons = new GridBagConstraints();
		cons.fill = GridBagConstraints.BOTH;
		cons.insets = new Insets(5, 5, 3, 5);
		cons.gridx = 0;
		cons.gridy = 0;
		add(tableOperTool, cons);
		cons.gridy = 1;
		cons.weightx = 1;
		cons.weighty = 1;
		cons.insets = new Insets(5, 5, 5, 5);
		add(new JideScrollPane(argumentsTable), cons);

		addComponentListener(new ComponentAdapter() {
			public void componentHidden(ComponentEvent e) {
				Functions.stopEditingCells(argumentsTable);
			}
		});

	}

	protected void initImport(final JideSplitButton importBtn) {
		ActionListener l = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showSystemArgsImporter(importBtn);
			}
		};
		JMenuItem item = new JMenuItem("导入系统参数");
		item.addActionListener(l);
		importBtn.add(item);
		importBtn.addActionListener(l);
	}

	/**
	 * 系统参数导入
	 * 
	 * @param importBtn2
	 */
	protected void showSystemArgsImporter(JideSplitButton importBtn) {
		PaintBoard board = dialog.getEditor().getOperationArea()
				.getCurrentPaintBoard();
		List<WofoArgumentsBean> systemArgs = (List<WofoArgumentsBean>) dialog
				.getEditor().getCache().nowaitWhileNullGet(
						WfCacheManager.SYSTEM_ARGUMENTS);
		if (systemArgs == null) {
			WofoNetBean netBean = new WofoNetBean(
					WofoActions.GET_SYSTEM_PROPERTY, dialog.getEditor()
							.getUserID(), null);
			systemArgs = (List<WofoArgumentsBean>) Functions.getReturnNetBean(
					dialog.getEditor().getServletPath(), netBean).getParam();
			dialog.getEditor().getCache().cache(
					WfCacheManager.SYSTEM_ARGUMENTS, systemArgs);
		}
		final JidePopup popup = new JidePopup();
		popup.setMovable(true);
		popup.setPreferredSize(new Dimension(150, 220));
		popup.getContentPane().setLayout(new BorderLayout());
		Vector v = new Vector();
		v.addAll(systemArgs);
		final CheckBoxList list = new CheckBoxList(v);
		popup.getContentPane().add(new JideScrollPane(list),
				BorderLayout.CENTER);
		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new BorderLayout());
		btnPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		btnPanel.add(new JPanel(), BorderLayout.CENTER);
		JButton okBtn = new JButton(WofoResources.getValueByKey("ok"));
		btnPanel.add(okBtn, BorderLayout.EAST);
		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				popup.hidePopup(false);
				Object[] selectedArguments = list
						.getCheckBoxListSelectedValues();
				if (selectedArguments != null && selectedArguments.length > 0) {
					DefaultTableModel model = (DefaultTableModel) argumentsTable
							.getModel();
					String packageID = dialog.getEditor().getOperationArea().getCurrentPackage().getID();
					for (Object oo : selectedArguments) {
						WofoCodeBean code = (WofoCodeBean) oo;
						WofoArgumentsBean arg = new WofoArgumentsBean(Functions.getUID());
						arg.setArgName(code.getName());
						arg.setArgType(WofoArgumentsBean.ARG_TYPE_STRING);
//						arg.setDefaultValue(code.getValue());
						arg.setEditable(false);
						arg.setFollowField("");
						arg.setSqlRead("System");
						arg.setSqlWrite("System");
						arg.setPackageId(packageID);
//						arg.setProcessObjId(processObjId)
						model.addRow(new Object[] { model.getRowCount() + 1,
								arg.getArgName(), arg.getArgType(),
								arg.getDefaultValue(), arg.getFollowField(),
								arg.getSqlRead(), arg.getSqlWrite(), arg });
					}
				}
			}
		});
		popup.getContentPane().add(btnPanel, BorderLayout.SOUTH);
		popup.showPopup(importBtn);
	}

	public String check() {
		Functions.stopEditingCells(argumentsTable);
		DefaultTableModel model = (DefaultTableModel) argumentsTable.getModel();
		Set<WofoArgumentsBean> duplicateArgs = new HashSet<WofoArgumentsBean>();
		// getArguments作用是将表格中的数据赋给隐藏的WofoArgumentBean对象值
		// getArguments(null);
		// 先重置所有参数为不重复
		for (int i = 0; i < model.getRowCount(); i++) {
			WofoArgumentsBean arg = (WofoArgumentsBean) model.getValueAt(i, 7);
			arg.setDuplicate(false);
		}
		// 接下来开始判断是否重复
		for (int i = 0; i < model.getRowCount(); i++) {
			WofoArgumentsBean arg = (WofoArgumentsBean) model.getValueAt(i, 7);
			String argName = (String) model.getValueAt(i, 1);
			if (argName == null || argName.trim().equals("")) {
				return "参数名称不能为空";
			}
			for (int k = i + 1; k < model.getRowCount(); k++) {

				WofoArgumentsBean another = (WofoArgumentsBean) model
						.getValueAt(k, 7);
				String anotherName = (String) model.getValueAt(k, 1);
				if (argName != null && !argName.equals("")
						&& argName.equals(anotherName)) {
					duplicateArgs.add(arg);
					duplicateArgs.add(another);
					arg.setDuplicate(true);
					another.setDuplicate(true);
				}
			}
		}
		if (duplicateArgs.size() > 0) {
			return "重复名称的参数存在，请更正";
		}
		return null;
	}

	/**
	 * 初始化表格
	 */
	private void initTable() {
		argumentsTable = new JTable();
		argumentsTable.setModel(new javax.swing.table.DefaultTableModel(
				new Object[][] { {} }, new String[] { "", "参数名称", "参数类型",
						"默认值", "参数描述", "输入接口", "输出接口", "" }) {
			Class[] types = new Class[] { Integer.class, String.class,
					JComboBox.class, String.class, String.class, String.class,
					String.class, WofoArgumentsBean.class };

			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				if (columnIndex == 0) {
					return false;
				}
				WofoArgumentsBean arg = (WofoArgumentsBean) getValueAt(
						rowIndex, 7);
				return arg.isEditable();
			}
		});
		argumentsTable.getColumnModel().getColumn(0).setPreferredWidth(25);
		argumentsTable.getColumnModel().getColumn(1).setPreferredWidth(160);
		argumentsTable.getColumnModel().getColumn(2).setPreferredWidth(120);
		argumentsTable.getColumnModel().getColumn(3).setPreferredWidth(120);
		argumentsTable.getColumnModel().getColumn(4).setPreferredWidth(190);
		argumentsTable.getColumnModel().getColumn(5).setPreferredWidth(160);
		argumentsTable.getColumnModel().getColumn(6).setPreferredWidth(160);
		Functions.hideColumn(argumentsTable, 7);
		JComboBox combo = new JComboBox();
		DefaultComboBoxModel model = (DefaultComboBoxModel) combo.getModel();
		model.addElement(WofoArgumentsBean.ARG_TYPE_STRING);
		model.addElement(WofoArgumentsBean.ARG_TYPE_NUMBER);
		model.addElement(WofoArgumentsBean.ARG_TYPE_BOOLEAN);
		model.addElement(WofoArgumentsBean.ARG_TYPE_DATE);
		argumentsTable.getColumnModel().getColumn(2).setCellEditor(
				new DefaultCellEditor(combo));
		argumentsTable.getColumnModel().getColumn(0).setCellRenderer(
				new TableTextPositionRenderer(SwingConstants.CENTER));
		new DefaultSimpleTableSortor(argumentsTable).initSortHeader();
		argumentsTable.getColumnModel().getColumn(1).setCellRenderer(
				new MyCellRenderer());
		argumentsTable.getColumnModel().getColumn(1).setCellEditor(
				new DefaultCellEditor(new JTextField()));
		argumentsTable.getColumnModel().getColumn(1).getCellEditor()
				.addCellEditorListener(new CellEditorListener() {

					public void editingStopped(ChangeEvent e) {
						check();
					}

					public void editingCanceled(ChangeEvent e) {

					}
				});
	}

	/**
	 * 获取输入的参数
	 * 
	 * @return
	 */
	public abstract List<WofoArgumentsBean> getArguments(Serializable value);

	@Override
	public void reset() {
		Functions.stopEditingCells(argumentsTable);
		DefaultTableModel model = (DefaultTableModel) argumentsTable.getModel();
		for (int i = model.getRowCount(); i > 0; i--) {
			model.removeRow(i - 1);
		}
	}

	public JideSplitButton getImportBtn() {
		return importBtn;
	}

	public JTable getArgumentsTable() {
		return argumentsTable;
	}

	public WfOperControllerButtonPanel getTableOperTool() {
		return tableOperTool;
	}

	private class MyCellRenderer extends DefaultTableCellRenderer {
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			WofoArgumentsBean arg = (WofoArgumentsBean) model
					.getValueAt(row, 7);

			DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) super
					.getTableCellRendererComponent(table, value, isSelected,
							hasFocus, row, column);
			renderer.setForeground(arg.isDuplicate() ? Color.red : Color.black);
			return renderer;
		}
	}

}
