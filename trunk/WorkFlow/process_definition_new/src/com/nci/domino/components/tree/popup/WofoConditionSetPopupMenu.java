package com.nci.domino.components.tree.popup;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import com.nci.domino.components.tree.WfTree;

/**
 * 流程树右键菜单,流程条件集合
 * 
 * @author Qil.Wong
 * 
 */
public class WofoConditionSetPopupMenu extends AbstractPopupMenu {

	private enum Command {
		add_process_condition_group, delete_all_conditions
	}

	public WofoConditionSetPopupMenu(WfTree tree) {
		super(tree);
	}

	public void actionPerformed(ActionEvent e) {
		Command commandEnum = Command.valueOf(e.getActionCommand());
		switch (commandEnum) {
		case add_process_condition_group:
			
			break;
		case delete_all_conditions:
			// 删除所有条件:
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
