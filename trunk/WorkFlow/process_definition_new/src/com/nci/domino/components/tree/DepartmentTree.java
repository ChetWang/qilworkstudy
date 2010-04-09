package com.nci.domino.components.tree;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.jidesoft.swing.CheckBoxTree;
import com.jidesoft.swing.JideScrollPane;
import com.nci.domino.WfEditor;
import com.nci.domino.concurrent.WfSwingWorker;

/**
 * 机构部门树
 * 
 * @author Qil.Wong
 * 
 */
public class DepartmentTree extends CheckBoxTree {

	private static final long serialVersionUID = -8447387513561843871L;

	// // 悬浮提示对象
	// protected WfOverlayable overlayScroll;

	JScrollPane treeScroll;

	// 工作流树根节点
	protected DefaultMutableTreeNode rootNode;

	protected boolean initialized = false;

	protected WfEditor editor;

	protected String busyText = "正在初始化数据";

	public DepartmentTree(WfEditor editor) {
		super();
		this.editor = editor;
		treeScroll = new JideScrollPane(this);
		treeScroll.setBackground(Color.black);
		setBackground(Color.white);
		rootNode = new DefaultMutableTreeNode("所有机构");
		DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
		setModel(treeModel);
		setCellRenderer(new WfTreeCellRenderer());
	}
	
	public void reset(){
		clearSelection();
		getCheckBoxTreeSelectionModel().clearSelection();
	}

	public void initTree() {
		setEnabled(false);
		WfSwingWorker<Object, DefaultMutableTreeNode> worker = new WfSwingWorker<Object, DefaultMutableTreeNode>(
				busyText) {

			@Override
			protected Object doInBackground() throws Exception {

				return null;
			}

			public void wfDone() {
				setEnabled(true);
				initialized = true;
				// overlayScroll.setOverlayVisible(false);
			}

		};
		this.editor.getBackgroundManager().enqueueOpertimeQueue(worker);
	}

	/**
	 * 获取tree所在的overlay组件，包含overlay，tree，scrollpane
	 * 
	 * @return
	 */
	public JComponent getOverlayScroll() {
		return treeScroll;
	}

	/**
	 * 是否已经初始化
	 * 
	 * @return
	 */
	public boolean isInitialized() {
		return initialized;
	}

	public String getBusyText() {
		return busyText;
	}

	public void setBusyText(String busyText) {
		this.busyText = busyText;
	}

	public DefaultMutableTreeNode getRootNode() {
		return rootNode;
	}

	public void setRootNode(DefaultMutableTreeNode rootNode) {
		// ((DefaultTreeModel) getModel()).removeNodeFromParent(this.rootNode);
		((DefaultTreeModel) getModel()).setRoot(rootNode);
		this.rootNode = rootNode;
	}

	public JScrollPane getTreeScroll() {
		return treeScroll;
	}

}
