package com.nci.domino.components.operation;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;

import com.jidesoft.swing.JidePopupMenu;
import com.nci.domino.beans.WofoSimpleSet;
import com.nci.domino.beans.desyer.WofoActivityBean;
import com.nci.domino.beans.desyer.WofoConditionBean;
import com.nci.domino.beans.desyer.WofoPackageBean;
import com.nci.domino.beans.desyer.WofoProcessBean;
import com.nci.domino.components.dialog.WfDialog;
import com.nci.domino.components.dialog.condition.WfNewConditionGroupDialog;
import com.nci.domino.help.Functions;
import com.nci.domino.shape.WfActivity;

/**
 * ���������Ĳ�������
 * 
 * @author Qil.Wong
 * 
 */
public class WfConditionArea extends WfOperationSimpleSetArea {

	protected WofoConditionBean copyPasteCondition;

	public WfConditionArea(WfOperationArea operationArea,
			WofoSimpleSet simpleSetBean) {
		super(operationArea, simpleSetBean);
		init();
		simpleSetList.setCellRenderer(new ConditionListCellRenderer());
	}

	/**
	 * �����ʼ��
	 */
	private void init() {
		buttonPanel.removeDefaultListeners(buttonPanel.getAddRowBtn());
		buttonPanel.removeDefaultListeners(buttonPanel.getDelRowBtn());
		buttonPanel.getAddRowBtn().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionevent) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						addCondition();
					}
				});
			}
		});

		final JidePopupMenu popup = new JidePopupMenu();
		// ���Ӹ��ơ�ճ��
		final JMenuItem copyItem = new JMenuItem("����");
		final JMenuItem pasteItem = new JMenuItem("ճ��");
		popup.add(copyItem);
		popup.add(pasteItem);
		this.simpleSetList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3
						&& e.getClickCount() == 1 && simpleSetList.isEnabled()) {
					int index = simpleSetList.locationToIndex(e.getPoint());
					if (index != -1) {
						simpleSetList.setSelectedIndex(index);
					} else {
						simpleSetList.clearSelection();
					}
					copyItem.setEnabled(index > -1);
					pasteItem.setEnabled(copyPasteCondition != null);
					popup.show(simpleSetList, e.getX(), e.getY());
				}
			}
		});
		copyItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				copyPasteCondition = (WofoConditionBean) Functions
						.deepClone(simpleSetList.getSelectedValue());
			}
		});
		// ճ���¼�
		pasteItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<?> conditions = WfConditionArea.this.operationArea
						.getCurrentProcess().getConditions();

				WofoConditionBean clonedCopyPasteCondition = (WofoConditionBean) Functions
						.deepClone(copyPasteCondition);
				for (Object o : conditions) {
					if (o.toString().equals(copyPasteCondition.toString())) {
						clonedCopyPasteCondition.setConditionName("copy_"
								+ clonedCopyPasteCondition.getConditionName());
						break;
					}
				}
				addValue(clonedCopyPasteCondition);
				WfConditionArea.this.operationArea.getCurrentProcess()
						.getConditions().add(clonedCopyPasteCondition);
				clonedCopyPasteCondition.setID(Functions.getUID());
				clonedCopyPasteCondition
						.setPackageId(WfConditionArea.this.operationArea
								.getCurrentPackage().getID());
				clonedCopyPasteCondition
						.setProcessId(WfConditionArea.this.operationArea
								.getCurrentProcess().getID());
			}
		});
	}

	/**
	 * ��������
	 */
	private void addCondition() {
		WfDialog dialog = operationArea.getEditor().getDialogManager()
				.getDialog(WfNewConditionGroupDialog.class, "��������", true);
		WofoConditionBean conditionBean = new WofoConditionBean(Functions
				.getUID());
		conditionBean.setPackageId(operationArea.getCurrentPackage()
				.getPackageId());
		dialog.showWfDialog(conditionBean);
		if (dialog.getDialogResult() == WfDialog.RESULT_AFFIRMED) {
			WofoConditionBean condition = (WofoConditionBean) dialog
					.getInputValues();
			addValue(condition);
			operationArea.getCurrentProcess().getConditions().add(condition);
		}
	}

	@Override
	public void doubleClicked() {
		WofoConditionBean selectedObj = (WofoConditionBean) simpleSetList
				.getSelectedValue();
		if (selectedObj != null) {
			WfDialog dialog = operationArea.getEditor().getDialogManager()
					.getDialog(WfNewConditionGroupDialog.class,
							selectedObj.getConditionName() + "��������", true);
			dialog.showWfDialog(selectedObj);
			if (dialog.getDialogResult() == WfDialog.RESULT_AFFIRMED) {
				dialog.getInputValues();
			}
		}
	}

	@Override
	public void treeSelectionChanged(TreeSelectionEvent e) {
		clearContent();
		DefaultMutableTreeNode newNode = operationArea.getWfTree()
				.getSelectedNode();
		boolean disabled = newNode == null || newNode.isRoot()
				|| newNode.getUserObject() instanceof WofoProcessBean == false;
		setEnabled(!disabled);
		if (!disabled) {
			List<WofoConditionBean> conditions = new ArrayList<WofoConditionBean>();
			WofoProcessBean p = operationArea.getCurrentProcess();
			conditions.addAll(p.getConditions());
			setValues(conditions);
		}
	}

	/**
	 * ������Ⱦ������list����ʾ����
	 * 
	 * @author Qil.Wong
	 * 
	 */
	private class ConditionListCellRenderer extends DefaultListCellRenderer {

		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			DefaultListCellRenderer c = (DefaultListCellRenderer) super
					.getListCellRendererComponent(list, value, index,
							isSelected, cellHasFocus);
			c.setIcon(Functions.getImageIcon("condition.gif"));
			return c;
		}
	}

}
