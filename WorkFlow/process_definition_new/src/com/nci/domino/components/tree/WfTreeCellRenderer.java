package com.nci.domino.components.tree;

import java.awt.Component;
import java.awt.Image;

import javax.swing.GrayFilter;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.nci.domino.beans.WofoSimpleSet;
import com.nci.domino.beans.desyer.WofoPackageBean;
import com.nci.domino.beans.desyer.WofoProcessBean;
import com.nci.domino.beans.desyer.WofoProcessMasterBean;
import com.nci.domino.beans.org.WofoUnitBean;
import com.nci.domino.beans.org.WofoUserBean;
import com.nci.domino.help.Functions;

public class WfTreeCellRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = -1069976438478926819L;

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		String stringValue = tree.convertValueToText(value, sel, expanded,
				leaf, row, hasFocus);
		this.hasFocus = hasFocus;
		setText(stringValue);
		setForeground(sel ? getTextSelectionColor()
				: getTextNonSelectionColor());
		setIcon(getIcon((DefaultMutableTreeNode) value, expanded));
		setComponentOrientation(tree.getComponentOrientation());
		selected = sel;
		return this;
	}

	protected ImageIcon getIcon(DefaultMutableTreeNode value, boolean expanded) {
		Object userObject = value.getUserObject();
		if (userObject instanceof WofoPackageBean) {
			WofoPackageBean p = (WofoPackageBean) userObject;
			ImageIcon icon = null;
			if (p.isRootNode()) {
				if (expanded) {
					icon = Functions.getImageIcon("package_root.gif");
				} else {
					icon = Functions.getImageIcon("package_root_closed.gif");
				}
			} else {
				if (value.isLeaf()) {
					icon = Functions.getImageIcon("package_sub_empty.gif");
				} else
					icon = Functions.getImageIcon("package_sub.gif");
			}
			if (p.isDeleted()) {
				icon = new ImageIcon(GrayFilter.createDisabledImage(icon
						.getImage()));
			}
			return icon;
		} else if (userObject instanceof WofoProcessBean) {
			WofoProcessBean p = (WofoProcessBean) userObject;
			ImageIcon icon = Functions.getImageIcon("new_process.gif");
			if (p.isDeleted()) {
				Image disabledIcon = GrayFilter.createDisabledImage(icon
						.getImage());
				return new ImageIcon(disabledIcon);
			}
			return icon;
		} else if (userObject instanceof WofoProcessMasterBean) {
			return Functions.getImageIcon("new_process_set.gif");
		} else if (userObject instanceof WofoSimpleSet) {
			WofoSimpleSet setBean = (WofoSimpleSet) userObject;
			switch (setBean.getType()) {
			case WofoSimpleSet.BUSINESS_ITEM_TYPE:
				break;
			case WofoSimpleSet.BUSINESS_WORKFLOW_TYPE:
				break;
			case WofoSimpleSet.WORKFLOW_CONDITION_TYPE:
				break;
			case WofoSimpleSet.WORKFLOW_EVENT_TYPE:
				break;
			case WofoSimpleSet.WORKFLOW_MESSAGE_TYPE:
				break;
			case WofoSimpleSet.WORKFLOW_PARAMETER_TYPE:
				break;
			case WofoSimpleSet.WORKFLOW_ITEM_TYPE:
				break;
			}
		} else if (userObject instanceof WofoUnitBean) {
			return Functions.getImageIcon("department.gif");
		} else if (userObject instanceof WofoUserBean) {
			return Functions.getImageIcon("human.gif");
		}
		return null;
	}
}
