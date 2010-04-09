package com.nci.domino.components.tree.popup;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import com.nci.domino.components.tree.WfTree;

/**
 * 流程树右键菜单，流程资源集合
 * 
 * @author Qil.Wong
 * 
 */
public class WofoResourceSetPopupMenu extends AbstractPopupMenu {

	private enum Command {
		export_edit, SEPARATOR, cut, copy, paste, delete, SEPARATOR_1, up, down, SEPARATOR_2, properties
	}

	public WofoResourceSetPopupMenu(WfTree tree) {
		super(tree);
	}


	public void actionPerformed(ActionEvent e) {
		Command commandEnum = Command.valueOf(e.getActionCommand());
		switch (commandEnum) {
		case export_edit:
			break;
		case cut:
			break;
		case copy:
			break;
		case paste:
			break;
		case delete:
			break;
		case up:
			break;
		case down:
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
