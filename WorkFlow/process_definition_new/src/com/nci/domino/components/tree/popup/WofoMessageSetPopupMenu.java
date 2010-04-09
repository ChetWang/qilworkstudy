package com.nci.domino.components.tree.popup;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import com.nci.domino.components.tree.WfTree;

/**
 * 流程树右键菜单,业务流程
 * 
 * @author Qil.Wong
 * 
 */
public class WofoMessageSetPopupMenu extends AbstractPopupMenu {

	private enum Command {
		copy, paste, SEPARATOR, properties
	}

	public WofoMessageSetPopupMenu(WfTree tree) {
		super(tree);
	}

	public void actionPerformed(ActionEvent e) {
		Command commandEnum = Command.valueOf(e.getActionCommand());
		switch (commandEnum) {
		case copy:
			break;
		case paste:
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
