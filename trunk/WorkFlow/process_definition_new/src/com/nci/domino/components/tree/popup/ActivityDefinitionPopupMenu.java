package com.nci.domino.components.tree.popup;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import com.nci.domino.components.tree.WfTree;

/**
 * 流程树右键菜单,活动定义
 * 
 * @author Qil.Wong
 * 
 */
public class ActivityDefinitionPopupMenu extends AbstractPopupMenu {

	private enum Command {
		copy, delete, SEPARATOR, view_reference, properties
	}

	public ActivityDefinitionPopupMenu(WfTree tree) {
		super(tree);
	}


	public void actionPerformed(ActionEvent e) {
		Command commandEnum = Command.valueOf(e.getActionCommand());
		switch (commandEnum) {
		case copy:
			break;
		case delete:
			break;
		case view_reference:
			break;
		case properties:
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
