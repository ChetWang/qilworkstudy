package com.nci.domino.components.tree.popup;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import com.nci.domino.components.tree.WfTree;

/**
 * �������Ҽ��˵�,�����¼�����
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
			// �鿴����
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
