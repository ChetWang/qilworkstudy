package com.nci.domino.components.list;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;

import com.nci.domino.beans.desyer.WofoActivityBean;
import com.nci.domino.beans.desyer.WofoConditionBean;
import com.nci.domino.beans.desyer.WofoPackageBean;
import com.nci.domino.beans.desyer.WofoProcessBean;
import com.nci.domino.help.Functions;

/**
 * 工作流定义器JList列表渲染器
 * 
 * @author Qil.Wong
 * 
 */
public class WfListCellRenderer extends DefaultListCellRenderer {
	public Component getListCellRendererComponent(JList jlist, Object obj,
			int i, boolean selected, boolean foucused) {
		setComponentOrientation(jlist.getComponentOrientation());
		if (selected) {
			setBackground(jlist.getSelectionBackground());
			setForeground(jlist.getSelectionForeground());
		} else {
			setBackground(jlist.getBackground());
			setForeground(jlist.getForeground());
		}
		setText(obj != null ? obj.toString() : "");
		setEnabled(jlist.isEnabled());
		setFont(jlist.getFont());
		setIcon(getIcon(obj));
		return this;
	}

	private ImageIcon getIcon(Object o) {
		if (o instanceof WofoConditionBean) {
			WofoConditionBean condition = (WofoConditionBean) o;
			return Functions.getImageIcon("condition_"
					+ condition.getConditionType() + ".gif");
		} else if (o instanceof WofoActivityBean) {
			WofoActivityBean activity = (WofoActivityBean) o;
			return Functions.getImageIcon("workflow_activity_"
					+ activity.getActivityType() + "_16.gif");
		} else if(o instanceof WofoPackageBean){
			return Functions.getImageIcon("package_sub.gif");
		}else if(o instanceof WofoProcessBean){
			return Functions.getImageIcon("new_process.gif");
		}
		return null;
	}
}
