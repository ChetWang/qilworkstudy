
package com.nci.svg.swing.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.EventListener;
import java.util.Vector;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * 这部分代码从jdk6剥离，为满足servlet和client同时能序列化和反序列化
 * @author Qil.Wong
 *
 */
public class NCITreeModel implements Serializable, TreeModel {
	private static final long serialVersionUID = 1812153440196569079L;
	public NCITreeModel(TreeNode treenode) {
		this(treenode, false);
	}

	public NCITreeModel(TreeNode treenode, boolean flag) {
		listenerList = new EventListenerList();
		root = treenode;
		asksAllowsChildren = flag;
	}

	public void setAsksAllowsChildren(boolean flag) {
		asksAllowsChildren = flag;
	}

	public boolean asksAllowsChildren() {
		return asksAllowsChildren;
	}

	public void setRoot(TreeNode treenode) {
		TreeNode treenode1 = root;
		root = treenode;
		if (treenode == null && treenode1 != null)
			fireTreeStructureChanged(this, null);
		else
			nodeStructureChanged(treenode);
	}

	public Object getRoot() {
		return root;
	}

	public int getIndexOfChild(Object obj, Object obj1) {
		if (obj == null || obj1 == null)
			return -1;
		else
			return ((TreeNode) obj).getIndex((TreeNode) obj1);
	}

	public Object getChild(Object obj, int i) {
		return ((TreeNode) obj).getChildAt(i);
	}

	public int getChildCount(Object obj) {
		return ((TreeNode) obj).getChildCount();
	}

	public boolean isLeaf(Object obj) {
		if (asksAllowsChildren)
			return !((TreeNode) obj).getAllowsChildren();
		else
			return ((TreeNode) obj).isLeaf();
	}

	public void reload() {
		reload(root);
	}

	public void valueForPathChanged(TreePath treepath, Object obj) {
		MutableTreeNode mutabletreenode = (MutableTreeNode) treepath
				.getLastPathComponent();
		mutabletreenode.setUserObject(obj);
		nodeChanged(mutabletreenode);
	}

	public void insertNodeInto(MutableTreeNode mutabletreenode,
			MutableTreeNode mutabletreenode1, int i) {
		mutabletreenode1.insert(mutabletreenode, i);
		int ai[] = new int[1];
		ai[0] = i;
		nodesWereInserted(mutabletreenode1, ai);
	}

	public void removeNodeFromParent(MutableTreeNode mutabletreenode) {
		MutableTreeNode mutabletreenode1 = (MutableTreeNode) mutabletreenode
				.getParent();
		if (mutabletreenode1 == null) {
			throw new IllegalArgumentException("node does not have a parent.");
		} else {
			int ai[] = new int[1];
			Object aobj[] = new Object[1];
			ai[0] = mutabletreenode1.getIndex(mutabletreenode);
			mutabletreenode1.remove(ai[0]);
			aobj[0] = mutabletreenode;
			nodesWereRemoved(mutabletreenode1, ai, aobj);
			return;
		}
	}

	public void nodeChanged(TreeNode treenode) {
		if (listenerList != null && treenode != null) {
			TreeNode treenode1 = treenode.getParent();
			if (treenode1 != null) {
				int i = treenode1.getIndex(treenode);
				if (i != -1) {
					int ai[] = new int[1];
					ai[0] = i;
					nodesChanged(treenode1, ai);
				}
			} else if (treenode == getRoot())
				nodesChanged(treenode, null);
		}
	}

	public void reload(TreeNode treenode) {
		if (treenode != null)
			fireTreeStructureChanged(this, getPathToRoot(treenode), null, null);
	}

	public void nodesWereInserted(TreeNode treenode, int ai[]) {
		if (listenerList != null && treenode != null && ai != null
				&& ai.length > 0) {
			int i = ai.length;
			Object aobj[] = new Object[i];
			for (int j = 0; j < i; j++)
				aobj[j] = treenode.getChildAt(ai[j]);

			fireTreeNodesInserted(this, getPathToRoot(treenode), ai, aobj);
		}
	}

	public void nodesWereRemoved(TreeNode treenode, int ai[], Object aobj[]) {
		if (treenode != null && ai != null)
			fireTreeNodesRemoved(this, getPathToRoot(treenode), ai, aobj);
	}

	public void nodesChanged(TreeNode treenode, int ai[]) {
		if (treenode != null)
			if (ai != null) {
				int i = ai.length;
				if (i > 0) {
					Object aobj[] = new Object[i];
					for (int j = 0; j < i; j++)
						aobj[j] = treenode.getChildAt(ai[j]);

					fireTreeNodesChanged(this, getPathToRoot(treenode), ai,
							aobj);
				}
			} else if (treenode == getRoot())
				fireTreeNodesChanged(this, getPathToRoot(treenode), null, null);
	}

	public void nodeStructureChanged(TreeNode treenode) {
		if (treenode != null)
			fireTreeStructureChanged(this, getPathToRoot(treenode), null, null);
	}

	public TreeNode[] getPathToRoot(TreeNode treenode) {
		return getPathToRoot(treenode, 0);
	}

	protected TreeNode[] getPathToRoot(TreeNode treenode, int i) {
		TreeNode atreenode[];
		if (treenode == null) {
			if (i == 0)
				return null;
			atreenode = new TreeNode[i];
		} else {
			i++;
			if (treenode == root)
				atreenode = new TreeNode[i];
			else
				atreenode = getPathToRoot(treenode.getParent(), i);
			atreenode[atreenode.length - i] = treenode;
		}
		return atreenode;
	}

	public void addTreeModelListener(TreeModelListener treemodellistener) {
		listenerList.add(javax.swing.event.TreeModelListener.class,
				treemodellistener);
	}

	public void removeTreeModelListener(TreeModelListener treemodellistener) {
		listenerList.remove(javax.swing.event.TreeModelListener.class,
				treemodellistener);
	}

	public TreeModelListener[] getTreeModelListeners() {
		return (TreeModelListener[]) (TreeModelListener[]) listenerList
				.getListeners(javax.swing.event.TreeModelListener.class);
	}

	protected void fireTreeNodesChanged(Object obj, Object aobj[], int ai[],
			Object aobj1[]) {
		Object aobj2[] = listenerList.getListenerList();
		TreeModelEvent treemodelevent = null;
		for (int i = aobj2.length - 2; i >= 0; i -= 2) {
			if (aobj2[i] != javax.swing.event.TreeModelListener.class)
				continue;
			if (treemodelevent == null)
				treemodelevent = new TreeModelEvent(obj, aobj, ai, aobj1);
			((TreeModelListener) aobj2[i + 1]).treeNodesChanged(treemodelevent);
		}

	}

	protected void fireTreeNodesInserted(Object obj, Object aobj[], int ai[],
			Object aobj1[]) {
		Object aobj2[] = listenerList.getListenerList();
		TreeModelEvent treemodelevent = null;
		for (int i = aobj2.length - 2; i >= 0; i -= 2) {
			if (aobj2[i] != javax.swing.event.TreeModelListener.class)
				continue;
			if (treemodelevent == null)
				treemodelevent = new TreeModelEvent(obj, aobj, ai, aobj1);
			((TreeModelListener) aobj2[i + 1])
					.treeNodesInserted(treemodelevent);
		}

	}

	protected void fireTreeNodesRemoved(Object obj, Object aobj[], int ai[],
			Object aobj1[]) {
		Object aobj2[] = listenerList.getListenerList();
		TreeModelEvent treemodelevent = null;
		for (int i = aobj2.length - 2; i >= 0; i -= 2) {
			if (aobj2[i] != javax.swing.event.TreeModelListener.class)
				continue;
			if (treemodelevent == null)
				treemodelevent = new TreeModelEvent(obj, aobj, ai, aobj1);
			((TreeModelListener) aobj2[i + 1]).treeNodesRemoved(treemodelevent);
		}

	}

	protected void fireTreeStructureChanged(Object obj, Object aobj[],
			int ai[], Object aobj1[]) {
		Object aobj2[] = listenerList.getListenerList();
		TreeModelEvent treemodelevent = null;
		for (int i = aobj2.length - 2; i >= 0; i -= 2) {
			if (aobj2[i] != javax.swing.event.TreeModelListener.class)
				continue;
			if (treemodelevent == null)
				treemodelevent = new TreeModelEvent(obj, aobj, ai, aobj1);
			((TreeModelListener) aobj2[i + 1])
					.treeStructureChanged(treemodelevent);
		}

	}

	private void fireTreeStructureChanged(Object obj, TreePath treepath) {
		Object aobj[] = listenerList.getListenerList();
		TreeModelEvent treemodelevent = null;
		for (int i = aobj.length - 2; i >= 0; i -= 2) {
			if (aobj[i] != javax.swing.event.TreeModelListener.class)
				continue;
			if (treemodelevent == null)
				treemodelevent = new TreeModelEvent(obj, treepath);
			((TreeModelListener) aobj[i + 1])
					.treeStructureChanged(treemodelevent);
		}

	}

	public EventListener[] getListeners(Class class1) {
		return listenerList.getListeners(class1);
	}

	private void writeObject(ObjectOutputStream objectoutputstream)
			throws IOException {
		Vector vector = new Vector();
		objectoutputstream.defaultWriteObject();
		if (root != null && (root instanceof Serializable)) {
			vector.addElement("root");
			vector.addElement(root);
		}
		objectoutputstream.writeObject(vector);
	}

	private void readObject(ObjectInputStream objectinputstream)
			throws IOException, ClassNotFoundException {
		objectinputstream.defaultReadObject();
		Vector vector = (Vector) objectinputstream.readObject();
		int i = 0;
		int j = vector.size();
		if (i < j && vector.elementAt(i).equals("root")) {
			root = (TreeNode) vector.elementAt(++i);
			i++;
		}
	}

	protected TreeNode root;
	protected EventListenerList listenerList;
	protected boolean asksAllowsChildren;
}
