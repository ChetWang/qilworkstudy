package com.nci.domino.components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.jidesoft.swing.JideButton;
import com.nci.domino.help.Functions;

/**
 * Ϊ����б��������С�ɾ���С����ơ����ƵĹ���������
 * 
 * @author Qil.Wong
 * 
 */
public class WfOperControllerButtonPanel extends JPanel {

	/**
	 * ��Ӱ�ť
	 */
	private AbstractButton addRowBtn = new JideButton(Functions
			.getImageIcon("add_row.gif"));
	/**
	 * ɾ����ť
	 */
	private AbstractButton delRowBtn = new JideButton(Functions
			.getImageIcon("del_row.gif"));
	/**
	 * ���ư�ť
	 */
	private AbstractButton upRowBtn = new JideButton(Functions
			.getImageIcon("up.gif"));
	/**
	 * ���ư�ť
	 */
	private AbstractButton downRowBtn = new JideButton(Functions
			.getImageIcon("down.gif"));

	/**
	 * �ܿر��,�б�
	 */
	private JComponent controller;

	private boolean needReIndex;

	private int gridx;

	GridBagConstraints toolCons = new GridBagConstraints();

	private JPanel glue = new JPanel();

	public WfOperControllerButtonPanel(JComponent controller,
			boolean needReIndex) {
		this.needReIndex = needReIndex;
		this.controller = controller;
		init();
	}

	/**
	 * ��ʼ��
	 */
	private void init() {
		setLayout(new GridBagLayout());
		gridx = 0;
		toolCons.insets = new Insets(0, 1, 0, 0);
		toolCons.gridx = gridx;
		toolCons.gridy = 0;
		toolCons.anchor = GridBagConstraints.WEST;
		toolCons.fill = GridBagConstraints.HORIZONTAL;
		add(addRowBtn, toolCons);
		gridx++;
		toolCons.gridx = gridx;
		gridx++;
		add(delRowBtn, toolCons);
		toolCons.gridx = gridx;
		gridx++;
		add(upRowBtn, toolCons);
		toolCons.gridx = gridx;
		gridx++;
		add(downRowBtn, toolCons);
		toolCons.gridx = 4;
		toolCons.weightx = 1;
		add(glue, toolCons);
		ActionListener lis = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				oper((AbstractButton) e.getSource());
			}
		};
		addRowBtn.addActionListener(lis);
		delRowBtn.addActionListener(lis);
		upRowBtn.addActionListener(lis);
		downRowBtn.addActionListener(lis);
		addRowBtn.setFocusable(false);
		delRowBtn.setFocusable(false);
		upRowBtn.setFocusable(false);
		downRowBtn.setFocusable(false);
		addRowBtn.setToolTipText("������");
		delRowBtn.setToolTipText("ɾ����");
		upRowBtn.setToolTipText("����");
		downRowBtn.setToolTipText("����");
	}

	private void oper(AbstractButton source) {
		if (controller instanceof JTable) {
			operTable((JTable) controller, source);
		} else if (controller instanceof JList) {
			operList((JList) controller, source);
		}
	}

	public void setEnabled(boolean flag) {
		addRowBtn.setEnabled(flag);
		delRowBtn.setEnabled(flag);
		upRowBtn.setEnabled(flag);
		downRowBtn.setEnabled(flag);
	}

	/**
	 * �Ƴ�Ĭ�ϵļ���
	 */
	public void removeDefaultOperListeners() {
		addRowBtn.removeActionListener(addRowBtn.getActionListeners()[0]);
		delRowBtn.removeActionListener(delRowBtn.getActionListeners()[0]);
		upRowBtn.removeActionListener(upRowBtn.getActionListeners()[0]);
		downRowBtn.removeActionListener(downRowBtn.getActionListeners()[0]);
	}

	/**
	 * �Ƴ�Ĭ�ϵļ���
	 * 
	 * @param panelButton
	 *            ָ����������������Ĭ����Ӧ����������ȥ
	 */
	public void removeDefaultListeners(AbstractButton panelButton) {
		panelButton.removeActionListener(panelButton.getActionListeners()[0]);
	}

	/**
	 * �б��¼��ɷ�
	 * 
	 * @param controller2
	 * @param source
	 */
	private void operList(JList list, AbstractButton source) {
		DefaultListModel model = (DefaultListModel) list.getModel();
		int index = list.getSelectedIndex();
		if (source == addRowBtn) {
			//
		} else if (index >= 0) {
			if (source == delRowBtn) {
				model.remove(index);
			} else if (source == upRowBtn) {
				if (index > 0) {
					Object o = model.getElementAt(index);
					model.remove(index);
					model.add(index - 1, o);
					list.setSelectedIndex(index - 1);
				}
			} else if (source == downRowBtn) {
				if (index < model.getSize() - 1) {
					Object o = model.getElementAt(index);
					model.remove(index);
					model.add(index + 1, o);
					list.setSelectedIndex(index + 1);
				}
			}
		}
		list.updateUI();
	}

	/**
	 * ����¼��ɷ�
	 * 
	 * @param source
	 */
	private void operTable(JTable table, AbstractButton source) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		Functions.stopEditingCells(table);
		int selectIndex = table.getSelectedRow();
		int columnCount = table.getColumnModel().getColumnCount();
		if (source == addRowBtn) {
			if (selectIndex == -1) {
				selectIndex = model.getRowCount() - 1;
			}
			if (model.getRowCount() == 0) {
				selectIndex = -1;
			}

			model.insertRow(selectIndex + 1, getOneRowEmptyData());
			table.getSelectionModel().setSelectionInterval(selectIndex,
					selectIndex);
		} else if (source == delRowBtn) {
			if (model.getRowCount() > 0 && selectIndex != -1) {
				int[] rows = table.getSelectedRows();
				for (int i = rows.length - 1; i >= 0; i--) {
					model.removeRow(rows[i]);
				}
				if (model.getRowCount() == 0
						|| selectIndex == model.getRowCount()) {
					table.clearSelection();
				} else {
					table.getSelectionModel().setSelectionInterval(selectIndex,
							selectIndex);
				}
			}

		} else if (source == upRowBtn) {
			if (selectIndex != -1 && selectIndex != 0) {
				Object[] o = new Object[columnCount];
				for (int i = 0; i < columnCount; i++) {
					o[i] = model.getValueAt(selectIndex, i);
				}
				model.removeRow(selectIndex);
				model.insertRow(selectIndex - 1, o);
				table.getSelectionModel().setSelectionInterval(selectIndex - 1,
						selectIndex - 1);
			}
		} else if (source == downRowBtn) {
			if (selectIndex != -1 && selectIndex != model.getRowCount() - 1) {
				Object[] o = new Object[columnCount];
				for (int i = 0; i < columnCount; i++) {
					o[i] = model.getValueAt(selectIndex, i);
				}
				model.removeRow(selectIndex);
				model.insertRow(selectIndex + 1, o);
				table.getSelectionModel().setSelectionInterval(selectIndex + 1,
						selectIndex + 1);
			}
		}
		if (needReIndex) {
			reIndex(table);
		}
	}

	// private void

	/**
	 * ���¸���һ�и�ֵ
	 */
	private void reIndex(JTable table) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		int rows = model.getRowCount();
		for (int i = 1; i < rows + 1; i++) {
			model.setValueAt(i, i - 1, 0);
		}
	}

	/**
	 * Ĭ�Ͽ��е�����
	 * 
	 * @return
	 */
	public Object[] getOneRowEmptyData() {
		return null;
	}

	public AbstractButton getAddRowBtn() {
		return addRowBtn;
	}

	public AbstractButton getDelRowBtn() {
		return delRowBtn;
	}

	public AbstractButton getUpRowBtn() {
		return upRowBtn;
	}

	public AbstractButton getDownRowBtn() {
		return downRowBtn;
	}

	public JComponent getController() {
		return controller;
	}

	public void addControlComp(JComponent c) {
		toolCons.gridx = gridx;
		toolCons.weightx = 0;
		gridx++;
		add(c, toolCons);
		toolCons.gridx = gridx;
		toolCons.weightx = 1;
		add(glue, toolCons);
	}

}
