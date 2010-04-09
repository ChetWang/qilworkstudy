package com.nci.domino.components.tree;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.jidesoft.swing.JideScrollPane;
import com.nci.domino.WfEditor;
import com.nci.domino.beans.WofoActions;
import com.nci.domino.beans.WofoNetBean;
import com.nci.domino.beans.desyer.WofoPackageBean;
import com.nci.domino.beans.desyer.WofoProcessBean;
import com.nci.domino.components.WfOverlayable;
import com.nci.domino.components.autocomplete.DefaultCompletionFilter;
import com.nci.domino.components.operation.WfOperationArea;
import com.nci.domino.concurrent.WfRunnable;
import com.nci.domino.help.Functions;

public class WfTree extends JTree {

	private static final long serialVersionUID = 2407724341024380255L;

	private WfOperationArea area;

	// 悬浮提示对象
	private WfBusyTextOverlayable overlayScroll;

	// 工作流树根节点
	private DefaultMutableTreeNode rootNode;

	// 从服务端获取来的最原始的副本
	private DefaultMutableTreeNode originalNode;

	// 工作流树模型
	private DefaultTreeModel treeModel;

	private JScrollPane treeScroll;

	/**
	 * 原始节点临时对象，用于存储获取原始节点的值
	 */
	private DefaultMutableTreeNode tempOriginalNode = null;

	public WfTree(WfOperationArea area, Object rootObject) {
		this.area = area;
		rootNode = new DefaultMutableTreeNode(rootObject);
		treeModel = new DefaultTreeModel(rootNode);

		WfTreeMouseAdapter treeMouseListener = new WfTreeMouseAdapter(this);
		addMouseListener(treeMouseListener);
		this.setModel(treeModel);
		treeScroll = new JideScrollPane(this);
		// treeScroll.setBackground(Color.black);
		setBackground(Color.white);
		overlayScroll = new WfBusyTextOverlayable(treeScroll,
				"{\n正在获取流程数据:f:gray}");
		overlayScroll.setOverlayVisible(true);
		getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		setCellRenderer(new WfTreeCellRenderer());
		getInitData();
	}

	/**
	 * 初始化数据
	 */
	public void getInitData() {
		setEnabled(false);
		final String tip = "正在获取流程数据";
		overlayScroll.setBusyText(tip);
		overlayScroll.setOverlayVisible(true);
		WfRunnable run = new WfRunnable(tip) {
			WofoNetBean result;

			public void run() {
				try {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							area.getEditor().getStatusBar().startShowBusyInfo(
									tip);
						}
					});
					WofoNetBean netBean = new WofoNetBean(WofoActions.LOAD_ALL,
							area.getEditor().getUserID(), null);
					try {
						result = Functions.getReturnNetBean(area.getEditor()
								.getServletPath(), netBean);
					} catch (Exception e) {
						e.printStackTrace();
						result = null;
					}
					if (result != null) {
						final Object o = result.getParam();
						if (result != null) {
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									overlayScroll.setOverlayVisible(false);
									if (o != null) {
										rootNode = (DefaultMutableTreeNode) o;
										treeModel.setRoot(rootNode);
										setOriginalNode((DefaultMutableTreeNode) Functions
												.deepClone(rootNode));
										initAutoCompleteFilter();
									}
								}
							});
						}
					}
				} finally {
					if (result == null) {
						Logger.getLogger(WfTree.class.getName()).log(
								Level.SEVERE, "获取流程数据失败，未得到任何数据");
						JOptionPane.showConfirmDialog(area.getEditor(),
								"获取流程数据失败!\n无法连接到服务器："
										+ area.getEditor().getServletPath(),
								"错误", JOptionPane.CLOSED_OPTION,
								JOptionPane.ERROR_MESSAGE);
					}
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							WfTree.this.setEnabled(true);
							area.getEditor().getStatusBar().stopShowInfo(tip);
						}
					});

				}
			}
		};
		new Thread(run).start();
	}

	/**
	 * 初始化过滤器
	 */
	public void initAutoCompleteFilter() {
		Vector<DefaultMutableTreeNode> v = new Vector<DefaultMutableTreeNode>();
		parseAutoCompleteFilter(rootNode, v);
		area.getFilterField().setFilter(new DefaultCompletionFilter(v));
	}

	public WfEditor getEditor() {
		return area.getEditor();
	}

	/**
	 * 将树状节点数据转换成列表格式数据
	 * 
	 * @param o
	 * @param v
	 */
	private void parseAutoCompleteFilter(DefaultMutableTreeNode o,
			Vector<DefaultMutableTreeNode> v) {
		Object obj = o.getUserObject();
		if (obj instanceof WofoPackageBean) {
			String name = ((WofoPackageBean) obj).getPackageName();
			if (name != null && !name.trim().equals("")) {
				v.add(o);
			}
		} else if (obj instanceof WofoProcessBean) {
			String name = ((WofoProcessBean) obj).getProcessName();
			if (name != null && !name.trim().equals("")) {
				v.add(o);
			}
		}
		for (int i = 0; i < o.getChildCount(); i++) {
			parseAutoCompleteFilter((DefaultMutableTreeNode) o.getChildAt(i), v);
		}
	}

	/**
	 * 获取Overlay组件
	 * 
	 * @return
	 */
	public WfBusyTextOverlayable getOverlayScroll() {
		return overlayScroll;
	}

	/**
	 * 获取流程树根节点
	 * 
	 * @return
	 */
	public DefaultMutableTreeNode getRootNode() {
		return rootNode;
	}

	/**
	 * 获取流程树的treemodel
	 * 
	 * @return
	 */
	public DefaultTreeModel getWfTreeModel() {
		return treeModel;
	}

	public void setWfTreeModel(DefaultTreeModel model) {
		super.setModel(model);
		this.treeModel = model;
		rootNode = (DefaultMutableTreeNode) model.getRoot();
	}

	public JScrollPane getTreeScroll() {
		return treeScroll;
	}

	/**
	 * 获取选择的节点代表的对象
	 * 
	 * @return 节点代表的对象
	 */
	public Object getSelectedUserObject() {
		DefaultMutableTreeNode node = getSelectedNode();

		return node == null ? null : node.getUserObject();
	}

	/**
	 * 获取选择的节点
	 * 
	 * @return 节点
	 */
	public DefaultMutableTreeNode getSelectedNode() {
		TreePath path = getSelectionPath();
		return path == null ? null : (DefaultMutableTreeNode) this
				.getSelectionPath().getLastPathComponent();
	}

	/**
	 * 往树上堆加节点，如果有选中的节点，则添加在选中的节点上，如果没有选中的节点，则添加在根节点上
	 * 
	 * @param object
	 *            添加数据的userobject
	 * @param expand
	 *            是否展开
	 * @param select
	 *            是否选中
	 * @return 新添加的节点
	 */
	public DefaultMutableTreeNode appendNode(Serializable object,
			boolean expand, boolean select) {
		DefaultMutableTreeNode selectNode = getSelectedNode();
		if (selectNode == null) {
			selectNode = rootNode;
		}
		return appendNode(selectNode, object, expand, select);
	}

	/**
	 * 往树上堆加节点
	 * 
	 * @param parent
	 *            父节点
	 * @param object
	 *            节点的userobject
	 * @param expand
	 *            是否展开
	 * @param select
	 *            是否选中
	 * @return 新增加的节点
	 */
	public DefaultMutableTreeNode appendNode(DefaultMutableTreeNode parent,
			Serializable object, boolean expand, boolean select) {
		return appendNode(parent, new DefaultMutableTreeNode(object), expand,
				select);
	}

	/**
	 * 往树上堆加节点
	 * 
	 * @param parent
	 * @param objectNode
	 * @param expand
	 * @param select
	 * @return
	 */
	public DefaultMutableTreeNode appendNode(DefaultMutableTreeNode parent,
			DefaultMutableTreeNode objectNode, boolean expand, boolean select) {

		DefaultMutableTreeNode node = objectNode;
		DefaultMutableTreeNode selectNode = getSelectedNode();
		if (selectNode == null) {
			selectNode = rootNode;
		}
		selectNode.add(node);
		if (expand) {
			// this.expandPath(new TreePath(node.getPath()));
			getSelectionModel().setSelectionPath(new TreePath(node.getPath()));
		}
		if (!select) {
			getSelectionModel().setSelectionPath(
					new TreePath(((DefaultMutableTreeNode) node.getParent())
							.getPath()));
		}
		updateUI();
		return objectNode;
	}

	private boolean iterStart = false;

	private void getNodeByValue(Object userObject,
			DefaultMutableTreeNode currentNode,
			List<DefaultMutableTreeNode> iteratored) {
		if (iterStart) {
			iteratored.add(currentNode);
			if (userObject != currentNode.getUserObject()) {
				for (int i = 0; i < currentNode.getChildCount(); i++) {
					DefaultMutableTreeNode child = (DefaultMutableTreeNode) currentNode
							.getChildAt(i);
					getNodeByValue(userObject, child, iteratored);
				}
			} else {
				iterStart = false;
				return;
			}
		}

	}

	/**
	 * 根据节点的UserObject获取DefaultMutableTreeNode节点对象
	 * 
	 * @param userObject
	 * @return
	 */
	public DefaultMutableTreeNode getNodeByValue(Object userObject) {
		iterStart = true;
		List<DefaultMutableTreeNode> iteratored = new ArrayList<DefaultMutableTreeNode>();
		getNodeByValue(userObject, rootNode, iteratored);
		return iteratored.size() > 0 ? iteratored.get(iteratored.size() - 1)
				: null;
	}

	public void paint(Graphics g) {
		if (!isEnabled()) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					0.5f));
		}
		super.paint(g);
	}

	public void updateUI() {
		super.updateUI();
		if (overlayScroll != null) {
			overlayScroll.updateUI();
		}
	}

	/**
	 * 获取原始对象副本节点
	 * 
	 * @return
	 */
	public DefaultMutableTreeNode getOriginalNode() {
		return originalNode;
	}

	/**
	 * 设置原始对象副本节点
	 * 
	 * @param originalNode
	 */
	public void setOriginalNode(DefaultMutableTreeNode originalNode) {
		this.originalNode = originalNode;
	}

	/**
	 * 根据当前操作的流程对象获取最初始流程对象的副本
	 * 
	 * @param currentProcess
	 * @return
	 */
	public WofoProcessBean getOriginalProcess(WofoProcessBean currentProcess) {
		if (originalNode == null) {
			return null;
		}
		DefaultMutableTreeNode processNode = getOriginalProcessNode(
				originalNode, currentProcess);
		if (processNode == null)
			return null;
		return (WofoProcessBean) processNode.getUserObject();
	}

	/**
	 * 根据当前流程，获取副本流程对应的节点
	 * 
	 * @param currentProcess
	 * @return
	 */
	public DefaultMutableTreeNode getOriginalProcessNode(
			WofoProcessBean currentProcess) {

		return getOriginalProcessNode(originalNode, currentProcess);
	}

	/**
	 * 根据当前操作的流程对象获取最初始流程对象的副本
	 * 
	 * @param currentProcess
	 * @return
	 */
	private DefaultMutableTreeNode getOriginalProcessNode(
			DefaultMutableTreeNode original, WofoProcessBean currentProcess) {
		tempOriginalNode = null;
		int count = original.getChildCount();
		DefaultMutableTreeNode node = null;
		for (int i = 0; i < count; i++) {
			node = (DefaultMutableTreeNode) original.getChildAt(i);
			Object o = node.getUserObject();
			if (node.getUserObject() instanceof WofoProcessBean) {
				WofoProcessBean pro = (WofoProcessBean) o;
				if (pro.getID().equals(currentProcess.getID())) {
					tempOriginalNode = node;
					break;
				}
			}
			if (tempOriginalNode == null) {
				node = getOriginalProcessNode(node, currentProcess);
			}
		}
		return tempOriginalNode;
	}

}
