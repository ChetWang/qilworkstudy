package com.nci.domino.components.dialog.activity;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import com.jidesoft.popup.JidePopup;
import com.jidesoft.swing.CheckBoxList;
import com.jidesoft.swing.JideScrollPane;
import com.jidesoft.swing.JideSplitButton;
import com.nci.domino.PaintBoard;
import com.nci.domino.beans.desyer.WofoActivityBean;
import com.nci.domino.beans.desyer.WofoArgumentsBean;
import com.nci.domino.components.WfArgumentsPanel;
import com.nci.domino.components.dialog.WfDialog;
import com.nci.domino.domain.WofoBaseDomain;
import com.nci.domino.help.Functions;
import com.nci.domino.help.WofoResources;

/**
 * 活动参数面板
 * 
 * @author Qil.Wong
 * 
 */
public class WfActivityArgumentsPanel extends WfArgumentsPanel {

	protected void initImport(final JideSplitButton importBtn) {
		super.initImport(importBtn);
		ActionListener l = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showProcessArgsImporter(importBtn);
			}
		};
		JMenuItem item = new JMenuItem("导入流程参数");
		item.addActionListener(l);
		importBtn.add(item);
		importBtn.addActionListener(l);
	}

	/**
	 * 显示流程参数导入菜单
	 */
	private void showProcessArgsImporter(JComponent relative) {
		PaintBoard board = dialog.getEditor().getOperationArea()
				.getCurrentPaintBoard();
		List<WofoArgumentsBean> processArgs = board.getProcessBean()
				.getArguments();
		final JidePopup popup = new JidePopup();
		popup.setMovable(true);
		popup.setPreferredSize(new Dimension(150, 220));
		popup.getContentPane().setLayout(new BorderLayout());
		Vector v = new Vector();
		v.addAll(processArgs);
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
					for (Object oo : selectedArguments) {
						WofoArgumentsBean arg = (WofoArgumentsBean) oo;
						model.addRow(new Object[] { model.getRowCount() + 1,
								arg.getArgName(), arg.getArgType(),
								arg.getDefaultValue(), arg.getFollowField(),
								arg.getSqlRead(), arg.getSqlWrite(), arg });
					}
				}
			}
		});
		popup.getContentPane().add(btnPanel, BorderLayout.SOUTH);
		popup.showPopup(relative);
	}

	public List<WofoArgumentsBean> getArguments(Serializable value) {
		Functions.stopEditingCells(argumentsTable);
		WofoActivityBean act = (WofoActivityBean) value;
		List<WofoArgumentsBean> args = new ArrayList<WofoArgumentsBean>();
		DefaultTableModel model = (DefaultTableModel) argumentsTable.getModel();
		for (int i = 0; i < model.getRowCount(); i++) {
			WofoArgumentsBean bean = (WofoArgumentsBean) model.getValueAt(i, 7);
			bean.setArgName((String) model.getValueAt(i, 1));
			bean.setArgType((String) model.getValueAt(i, 2));
			bean.setDefaultValue((String) model.getValueAt(i, 3));
			bean.setFollowField((String) model.getValueAt(i, 4));
			bean.setSqlRead((String) model.getValueAt(i, 5));
			bean.setSqlWrite((String) model.getValueAt(i, 6));
			if (act != null) {
				bean.setProcessObjId(act.getID());
				bean.setProcessObjType(WofoBaseDomain.OBJ_TYPE_ACTIVITY);
			}
			args.add(bean);
		}
		return args;
	}

	public WfActivityArgumentsPanel(WfDialog dialog) {
		super(dialog);
	}

	@Override
	public Serializable applyValues(Serializable value) {
		WofoActivityBean bean = (WofoActivityBean) value;
		bean.getArguments().clear();
		bean.getArguments().addAll(getArguments(value));
		return bean;
	}

	@Override
	public void setValues(Serializable value) {
		WofoActivityBean bean = (WofoActivityBean) value;
		List<WofoArgumentsBean> args = bean.getArguments();
		DefaultTableModel model = (DefaultTableModel) argumentsTable.getModel();
		for (WofoArgumentsBean b : args) {
			model.addRow(new Object[] { model.getRowCount() + 1,
					b.getArgName(), b.getArgType(), b.getDefaultValue(),
					b.getFollowField(), b.getSqlRead(), b.getSqlWrite(), b });
		}
	}

}
