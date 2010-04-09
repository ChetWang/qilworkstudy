package com.nci.domino.components.tree.popup;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import com.nci.domino.components.tree.WfTree;

/**
 * 流程树右键菜单,流程参数集合
 * 
 * @author Qil.Wong
 * 
 */
public class WofoParamSetPopupMenu extends AbstractPopupMenu {

	private enum Command {
		add_process_params, delete_all
	}

	public WofoParamSetPopupMenu(WfTree tree) {
		super(tree);
	}

	public void actionPerformed(ActionEvent e) {
		Command commandEnum = Command.valueOf(e.getActionCommand());
		switch (commandEnum) {
		case add_process_params:
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
