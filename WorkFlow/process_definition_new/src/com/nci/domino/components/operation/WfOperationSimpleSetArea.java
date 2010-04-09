package com.nci.domino.components.operation;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;

import com.jidesoft.swing.JideScrollPane;
import com.nci.domino.beans.WofoSimpleSet;
import com.nci.domino.beans.desyer.WofoConditionBean;
import com.nci.domino.beans.desyer.WofoPackageBean;
import com.nci.domino.beans.desyer.WofoProcessBean;
import com.nci.domino.components.WfOperControllerButtonPanel;
import com.nci.domino.components.list.WfListCellRenderer;

/**
 * �������Լ��ϵĲ�������������ʾ�Ͳ����Զ���ͨ�����������������
 * 
 * @author Qil.Wong
 * 
 */
public abstract class WfOperationSimpleSetArea extends JPanel {

	protected JList simpleSetList;

	protected WfOperationArea operationArea;

	protected WfOperControllerButtonPanel buttonPanel;
	
	protected JideScrollPane scroll ;

	public WfOperationSimpleSetArea(WfOperationArea operationArea,
			WofoSimpleSet simpleSetBean) {
		this.operationArea = operationArea;
		init(simpleSetBean);
	}

	/**
	 * ��ʼ��
	 * 
	 * @param simpleSetBean
	 */
	private void init(WofoSimpleSet simpleSetBean) {
		simpleSetList = new JList(new DefaultListModel());
		simpleSetList.setCellRenderer(new WfListCellRenderer());
		buttonPanel = new WfOperControllerButtonPanel(simpleSetList, false);
		buttonPanel.getUpRowBtn().setVisible(false);
		buttonPanel.getDownRowBtn().setVisible(false);
		scroll = new JideScrollPane(simpleSetList);
		// simpleSetList.getSelectionModel().setSelectionMode(
		// ListSelectionModel.SINGLE_SELECTION);
		setLayout(new BorderLayout());
		add(scroll, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.NORTH);
		simpleSetList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1
						&& e.getClickCount() == 2) {
					doubleClicked();
				}
			}
		});
		buttonPanel.getDelRowBtn().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionevent) {
				int index = simpleSetList.getSelectedIndex();
				if (index != -1) {
					((DefaultListModel) simpleSetList.getModel()).remove(index);
				}
			}
		});
	}

	/**
	 * ˫���б�
	 */
	public void doubleClicked() {

	}

	/**
	 * ��ȡ������б�
	 * 
	 * @return
	 */
	public JList getWfList() {
		return simpleSetList;
	}

	public void setEnabled(boolean flag) {
		super.setEnabled(flag);
		simpleSetList.setEnabled(flag);
		buttonPanel.setEnabled(flag);
	}

	/**
	 * ��������ӽڵ�
	 */
	public void clearContent() {
		((DefaultListModel) simpleSetList.getModel()).removeAllElements();
		simpleSetList.updateUI();
	}

	/**
	 * �������ݽڵ�
	 * 
	 * @param values
	 */
	public void setValues(List<? extends Object> values) {
		DefaultListModel model = (DefaultListModel) simpleSetList.getModel();
		model.removeAllElements();
		for (int i = 0; i < values.size(); i++) {
			model.addElement(values.get(i));
		}
		simpleSetList.updateUI();
	}

	/**
	 * ���һ������
	 * 
	 * @param value
	 */
	public void addValue(Serializable value) {
		DefaultListModel model = (DefaultListModel) simpleSetList.getModel();
		model.addElement(value);
		simpleSetList.updateUI();
	}

	/**
	 * ��ȡ�����б��еĶ���
	 * 
	 * @return
	 */
	public List getValues() {
		List values = new ArrayList();
		DefaultListModel model = (DefaultListModel) simpleSetList.getModel();
		int size = model.getSize();
		for (int i = 0; i < size; i++) {
			values.add(model.getElementAt(i));
		}
		return values;
	}

	public WfOperControllerButtonPanel getButtonPanel() {
		return buttonPanel;
	}

	protected String getNodeBelongedProcess(DefaultMutableTreeNode node) {
		if (node != null) {
			Object o = node.getUserObject();
			if (o instanceof WofoProcessBean) {
				// return ((WofoProcessBean) o).getPackageId();
				return ((WofoProcessBean) o).getProcessId();
			}
		}
		return null;
	}

	protected String getNodeBelongedPackage(DefaultMutableTreeNode node) {
		if (node != null) {
			Object o = node.getUserObject();
			if (o instanceof WofoPackageBean) {
				return ((WofoPackageBean) o).getPackageId();
				// return "";
			} else if (o instanceof WofoProcessBean) {
				return ((WofoProcessBean) o).getPackageId();
				// return ((WofoProcessBean) o).getProcessId();
			}
		}
		return null;
	}

	public abstract void treeSelectionChanged(TreeSelectionEvent e);
	
}
