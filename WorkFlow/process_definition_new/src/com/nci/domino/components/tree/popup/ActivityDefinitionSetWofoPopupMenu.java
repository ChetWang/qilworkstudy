package com.nci.domino.components.tree.popup;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import com.nci.domino.components.tree.WfTree;

/**
 * �������Ҽ��˵�,����弯�ϣ�������
 * 
 * @author Qil.Wong
 * 
 */
public class ActivityDefinitionSetWofoPopupMenu extends AbstractPopupMenu {

	private enum Command {
		add_branch_route_activity, add_aggregation_route_activity, add_free_activity, add_subprocess_activity, delete_all_activity_definition
	}

	public ActivityDefinitionSetWofoPopupMenu(WfTree tree) {
		super(tree);
	}

	
	public void actionPerformed(ActionEvent e) {
		Command commandEnum = Command.valueOf(e.getActionCommand());
		switch (commandEnum) {
		case add_branch_route_activity:
			// ������֧·�ɻ
			break;
		case add_aggregation_route_activity:
			// �����ۺ�·�ɻ
			break;
		case add_free_activity:
			// �����Զ��
			break;
		case add_subprocess_activity:
			// ���������̻:
			break;
		case delete_all_activity_definition:
			// ɾ�����л����:
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
