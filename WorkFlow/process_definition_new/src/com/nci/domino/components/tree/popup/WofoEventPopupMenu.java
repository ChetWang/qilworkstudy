package com.nci.domino.components.tree.popup;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import com.nci.domino.components.tree.WfTree;

/**
 * 流程树右键菜单,流程事件集合
 * 
 * @author Qil.Wong
 * 
 */
public class WofoEventPopupMenu extends AbstractPopupMenu {

	private enum Command {
		delete, view_reference
	}

	public WofoEventPopupMenu(WfTree tree) {
		super(tree);
	}

	public void actionPerformed(ActionEvent e) {
		Command commandEnum = Command.valueOf(e.getActionCommand());
		switch (commandEnum) {
		case delete:
			break;
		case view_reference:
			// 查看引用
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
