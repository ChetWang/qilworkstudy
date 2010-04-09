package com.nci.domino.components.dialog.process;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import com.nci.domino.beans.desyer.WofoArgumentsBean;
import com.nci.domino.beans.desyer.WofoProcessBean;
import com.nci.domino.components.WfArgumentsPanel;
import com.nci.domino.components.dialog.WfDialog;
import com.nci.domino.domain.WofoBaseDomain;
import com.nci.domino.help.Functions;

public class WfProcessArgumentsPanel extends WfArgumentsPanel {

	public WfProcessArgumentsPanel(WfDialog dialog) {
		super(dialog);
	}

	@Override
	public Serializable applyValues(Serializable value) {
		WofoProcessBean bean = (WofoProcessBean) value;
		bean.getArguments().clear();
		bean.getArguments().addAll(getArguments(value));
		return bean;
	}

	public List<WofoArgumentsBean> getArguments(Serializable value) {
		Functions.stopEditingCells(argumentsTable);
		WofoProcessBean process = (WofoProcessBean) value;
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
			if (process != null) {
				bean.setProcessObjId(process.getID());
				bean.setProcessObjType(WofoBaseDomain.OBJ_TYPE_PROCESS);
			}
			args.add(bean);
		}
		return args;
	}

	@Override
	public void setValues(Serializable value) {
		WofoProcessBean bean = (WofoProcessBean) value;
		//已有参数
		List<WofoArgumentsBean> args = bean.getArguments();
		//将参数赋值到table内
		DefaultTableModel model = (DefaultTableModel) argumentsTable.getModel();
		for (WofoArgumentsBean b : args) {
			model.addRow(new Object[] { model.getRowCount() + 1,
					b.getArgName(), b.getArgType(), b.getDefaultValue(),
					b.getFollowField(), b.getSqlRead(), b.getSqlWrite(), b });
		}
	}

}
