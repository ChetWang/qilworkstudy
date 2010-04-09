package com.nci.domino.components.tree;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.nci.domino.beans.desyer.WofoProcessBean;
import com.nci.domino.components.tree.popup.AbstractPopupMenu;

public class WfTreeMouseAdapter extends MouseAdapter {

	private WfTree tree;

	public WfTreeMouseAdapter(WfTree tree) {
		super();
		this.tree = tree;
	}

	public void mouseClicked(MouseEvent e) {
		final MouseEvent me = e;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				mouseClick(me);
			}
		});
	}

	private void mouseClick(final MouseEvent e) {
		if (!tree.isEnabled()) {
			return;
		}
		tree.repaint();
		// 右键点击，弹出菜单
		if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON3) {
			TreePath path = tree.getPathForLocation(e.getX(), e.getY());
			if (path != null) {
				tree.setSelectionPath(path);
				AbstractPopupMenu popup = tree.getEditor().getPopupManager()
						.getPopup(tree.getSelectedUserObject(), tree);
				if (popup != null) {
					popup.putClientProperty("MouseEvent", e);
					popup.show(tree, e.getX(), e.getY());
				}
			} else {
				tree.clearSelection();
			}
		} else if (e.getClickCount() == 2
				&& e.getButton() == MouseEvent.BUTTON1) {
			Object o = tree.getSelectedUserObject();
			DefaultMutableTreeNode node = tree.getSelectedNode();
			if (o instanceof WofoProcessBean) {// 双击流程节点，打开绘图面板
				WofoProcessBean wpb = (WofoProcessBean) o;
				tree.getEditor().getOperationArea().openProcess(wpb, node);
			}
		}

	}

}
