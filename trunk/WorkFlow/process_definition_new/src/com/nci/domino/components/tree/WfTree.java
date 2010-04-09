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

	// ������ʾ����
	private WfBusyTextOverlayable overlayScroll;

	// �����������ڵ�
	private DefaultMutableTreeNode rootNode;

	// �ӷ���˻�ȡ������ԭʼ�ĸ���
	private DefaultMutableTreeNode originalNode;

	// ��������ģ��
	private DefaultTreeModel treeModel;

	private JScrollPane treeScroll;

	/**
	 * ԭʼ�ڵ���ʱ�������ڴ洢��ȡԭʼ�ڵ��ֵ
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
				"{\n���ڻ�ȡ��������:f:gray}");
		overlayScroll.setOverlayVisible(true);
		getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		setCellRenderer(new WfTreeCellRenderer());
		getInitData();
	}

	/**
	 * ��ʼ������
	 */
	public void getInitData() {
		setEnabled(false);
		final String tip = "���ڻ�ȡ��������";
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
								Level.SEVERE, "��ȡ��������ʧ�ܣ�δ�õ��κ�����");
						JOptionPane.showConfirmDialog(area.getEditor(),
								"��ȡ��������ʧ��!\n�޷����ӵ���������"
										+ area.getEditor().getServletPath(),
								"����", JOptionPane.CLOSED_OPTION,
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
	 * ��ʼ��������
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
	 * ����״�ڵ�����ת�����б��ʽ����
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
	 * ��ȡOverlay���
	 * 
	 * @return
	 */
	public WfBusyTextOverlayable getOverlayScroll() {
		return overlayScroll;
	}

	/**
	 * ��ȡ���������ڵ�
	 * 
	 * @return
	 */
	public DefaultMutableTreeNode getRootNode() {
		return rootNode;
	}

	/**
	 * ��ȡ��������treemodel
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
	 * ��ȡѡ��Ľڵ����Ķ���
	 * 
	 * @return �ڵ����Ķ���
	 */
	public Object getSelectedUserObject() {
		DefaultMutableTreeNode node = getSelectedNode();

		return node == null ? null : node.getUserObject();
	}

	/**
	 * ��ȡѡ��Ľڵ�
	 * 
	 * @return �ڵ�
	 */
	public DefaultMutableTreeNode getSelectedNode() {
		TreePath path = getSelectionPath();
		return path == null ? null : (DefaultMutableTreeNode) this
				.getSelectionPath().getLastPathComponent();
	}

	/**
	 * �����϶Ѽӽڵ㣬�����ѡ�еĽڵ㣬�������ѡ�еĽڵ��ϣ����û��ѡ�еĽڵ㣬������ڸ��ڵ���
	 * 
	 * @param object
	 *            ������ݵ�userobject
	 * @param expand
	 *            �Ƿ�չ��
	 * @param select
	 *            �Ƿ�ѡ��
	 * @return ����ӵĽڵ�
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
	 * �����϶Ѽӽڵ�
	 * 
	 * @param parent
	 *            ���ڵ�
	 * @param object
	 *            �ڵ��userobject
	 * @param expand
	 *            �Ƿ�չ��
	 * @param select
	 *            �Ƿ�ѡ��
	 * @return �����ӵĽڵ�
	 */
	public DefaultMutableTreeNode appendNode(DefaultMutableTreeNode parent,
			Serializable object, boolean expand, boolean select) {
		return appendNode(parent, new DefaultMutableTreeNode(object), expand,
				select);
	}

	/**
	 * �����϶Ѽӽڵ�
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
	 * ���ݽڵ��UserObject��ȡDefaultMutableTreeNode�ڵ����
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
	 * ��ȡԭʼ���󸱱��ڵ�
	 * 
	 * @return
	 */
	public DefaultMutableTreeNode getOriginalNode() {
		return originalNode;
	}

	/**
	 * ����ԭʼ���󸱱��ڵ�
	 * 
	 * @param originalNode
	 */
	public void setOriginalNode(DefaultMutableTreeNode originalNode) {
		this.originalNode = originalNode;
	}

	/**
	 * ���ݵ�ǰ���������̶����ȡ���ʼ���̶���ĸ���
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
	 * ���ݵ�ǰ���̣���ȡ�������̶�Ӧ�Ľڵ�
	 * 
	 * @param currentProcess
	 * @return
	 */
	public DefaultMutableTreeNode getOriginalProcessNode(
			WofoProcessBean currentProcess) {

		return getOriginalProcessNode(originalNode, currentProcess);
	}

	/**
	 * ���ݵ�ǰ���������̶����ȡ���ʼ���̶���ĸ���
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
