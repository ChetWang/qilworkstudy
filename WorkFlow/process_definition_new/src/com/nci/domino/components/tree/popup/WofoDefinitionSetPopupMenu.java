package com.nci.domino.components.tree.popup;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import com.nci.domino.components.dialog.WfDialog;
import com.nci.domino.components.tree.WfTree;

/**
 * �������Ҽ��˵������̶��弯��
 * 
 * @author Qil.Wong
 * 
 */
@Deprecated
public class WofoDefinitionSetPopupMenu extends AbstractPopupMenu {

	private enum Command {
		new_process_definition, delete_all, paste
	}

	public WofoDefinitionSetPopupMenu(WfTree tree) {
		super(tree);
	}

	public void actionPerformed(ActionEvent e) {
		Command commandEnum = Command.valueOf(e.getActionCommand());
		WfDialog dialog = null;
		switch (commandEnum) {
		case new_process_definition:
			// �½����̶���:
//			WofoProcessBean p = new WofoProcessBean(Functions.getUID());
//			WofoProcessMasterBean pm = (WofoProcessMasterBean) ((DefaultMutableTreeNode) tree
//					.getSelectedNode().getParent()).getUserObject();
//			p.setProcessMasterName(pm.getProcessMasterName());
//			dialog = tree.getEditor().getDialogManager().getDialog(
//					WfNewProcessDialog.class,
//					WofoResources.getValueByKey(e.getActionCommand()), true);
//			dialog.showWfDialog( p);
//			if (dialog.getDialogResult() == WfDialog.RESULT_AFFIRMED) {
//				WofoProcessBean input = (WofoProcessBean) dialog
//						.getInputValues();
//				DefaultMutableTreeNode node = tree.appendNode(input, true, true);
//				tree.getEditor().getOperationArea().openProcess(input,node);
//				tree.updateUI();
//			}
			break;
		case delete_all:
			// ȫ��ɾ��:
			break;
		case paste:
			break;
		default:
			break;

		}
	}

	protected List<Enum> getCommands() {
		Command[] comm = Command.values();
		List<Enum> l = new ArrayList<Enum>();
		for (int i = 0; i < comm.length; i++) {
			l.add(comm[i]);
		}
		return l;
	}

}
