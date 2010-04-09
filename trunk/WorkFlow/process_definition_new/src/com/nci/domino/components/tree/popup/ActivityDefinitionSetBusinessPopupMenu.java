package com.nci.domino.components.tree.popup;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import com.nci.domino.components.tree.WfTree;

/**
 * 流程树右键菜单，活动定义集合，业务项
 * 
 * @author Qil.Wong
 * 
 */
public class ActivityDefinitionSetBusinessPopupMenu extends AbstractPopupMenu {

	private enum Command {
		add_business_item, SEPARATOR, paste, delete_all
	}

	public ActivityDefinitionSetBusinessPopupMenu(WfTree tree) {
		super(tree);
	}

	public void actionPerformed(ActionEvent e) {
		Command commandEnum = Command.valueOf(e.getActionCommand());
		switch (commandEnum) {
		case add_business_item:
			//新增业务项
			break;
		case paste:
			break;
		case delete_all:
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
