package com.nci.domino.components.tree.popup;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import com.nci.domino.components.tree.WfTree;

/**
 * 流程树右键菜单,活动定义集合，流程项
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
			// 新增分支路由活动
			break;
		case add_aggregation_route_activity:
			// 新增聚合路由活动
			break;
		case add_free_activity:
			// 新增自动活动
			break;
		case add_subprocess_activity:
			// 新增子流程活动:
			break;
		case delete_all_activity_definition:
			// 删除所有活动定义:
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
