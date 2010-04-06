package com.nci.svg.sdk.graphmanager.property;

import java.awt.BorderLayout;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.batik.dom.svg.SVGOMGElement;
import org.apache.batik.dom.svg.SVGOMPathElement;
import org.apache.batik.dom.svg.SVGOMTextElement;
import org.apache.batik.dom.svg.SVGOMUseElement;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.nci.svg.bean.TreeNodeBean;
import com.nci.svg.sdk.bean.ModelBean;
import com.nci.svg.sdk.bean.ModelRelaIndunormBean;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.display.canvas.CanvasOperAdapter;
import com.nci.svg.sdk.graphunit.NCIEquipSymbolBean;
import com.nci.svg.sdk.topology.TopologyManagerAdapter;
import com.nci.svg.sdk.ui.EditorPanel;

import fr.itris.glips.svgeditor.display.handle.SVGHandle;

public class GraphModel extends EditorPanel {

	/**
	 * add by yux,2009-1-20 重新构建模型树
	 */
	public static final int REINIT_MODELTREE = 0;

	/**
	 * add by yux,2009-1-20 展开指定的树节点
	 */
	public static final int EXPAND_TREENODE = 1;

	/**
	 * add by yux,2009-1-20 选中树节点
	 */
	public static final int SELECT_TREENODE = 2;

	/**
	 * 模型树
	 */
	private JTree modelTree = null;

	/**
	 * add by yux,2009-1-19 节点与模型树之间的关联关系
	 */
	private HashMap<Element, DefaultMutableTreeNode> mapElement = new HashMap<Element, DefaultMutableTreeNode>();

	/**
	 * add by yux,2009-1-19 模型树根节点
	 */
	private DefaultMutableTreeNode root = null;

	/**
	 * add by yux,2009-1-19 模型树model
	 */
	private TreeModel treeModel = null;

	public GraphModel(EditorAdapter editor) {
		super(editor);
		init();
	}

	private void init() {
		JScrollPane scrollPane = new JScrollPane();
		modelTree = new JTree();
		modelTree.setModel(null);
		scrollPane.setViewportView(modelTree);
		BorderLayout borderLayout = new BorderLayout(3, 3);
		this.setLayout(borderLayout);
		this.add(scrollPane, BorderLayout.CENTER);
		modelTree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		modelTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				nodeSelected(e);
			}
		});
	}

	protected MouseListener treeMouseListener = null;

	public void initTreeModel(SVGHandle handle) {
		try {
			CanvasOperAdapter canvasOper = handle.getCanvas().getCanvasOper();
			if(canvasOper == null) return;
			TopologyManagerAdapter topologyManager = canvasOper
					.getTopologyManager();
			if (topologyManager == null) {
				treeModel = null;
				modelTree.setModel(treeModel);
			} else {
				treeModel = topologyManager.getTreeModel();
				modelTree.setModel(treeModel);
			}
			if (topologyManager == null
					|| topologyManager.getTreeCellRenderer() == null) {
				modelTree.setCellRenderer(null);
			} else {
				modelTree
						.setCellRenderer(topologyManager.getTreeCellRenderer());
			}
			if (treeMouseListener != null) {
				modelTree.removeMouseListener(treeMouseListener);
			}
			if (topologyManager != null)
				treeMouseListener = topologyManager.getTreeMouseListener();
			if (treeMouseListener != null)
				modelTree.addMouseListener(treeMouseListener);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 树节点选择更改
	 * 
	 * @param e
	 */
	private void nodeSelected(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) modelTree
				.getLastSelectedPathComponent();
		if (node == null) {
			return;
		}
		Object o = node.getUserObject();
		if (o != null && o instanceof TreeNodeBean) {
			TreeNodeBean bean = (TreeNodeBean) o;
			Element element = bean.getElement();
			editor.getHandlesManager().getCurrentHandle().getSelection()
					.handleSelection(element, false, false);
			notifyRightPanel(SELECT_TREENODE, node);
		}
	}

	/**
	 * add by yux,2009-1-19 重新根据当前显示svg图构建模型树
	 */
	public void reinitTree() {
		SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
		modelTree.setModel(null);
		if (handle == null
				|| handle.getHandleType() != SVGHandle.HANDLE_TYPE_SVG) {

			return;
		}
		modelTree.setModel(treeModel);
	}

	public void expandNode(DefaultMutableTreeNode node) {
		modelTree.updateUI();
		if (node != null) {
			TreePath path = new TreePath(((DefaultTreeModel) modelTree
					.getModel()).getPathToRoot(node));
			modelTree.setSelectionPath(path);
		}
	}

	public void selectNode(DefaultMutableTreeNode node) {
		if (node != null) {
			TreePath path = new TreePath(((DefaultTreeModel) modelTree
					.getModel()).getPathToRoot(node));
			modelTree.setSelectionPath(path);
		} else {
			modelTree.clearSelection();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.client.EditorAdapter#notifyRightPanel(int)
	 */
	public void notifyRightPanel(int type, Object obj) {
		if (type == REINIT_MODELTREE)
			reinitTree();
		else if (type == EXPAND_TREENODE)
			expandNode((DefaultMutableTreeNode) obj);
		else if (type == SELECT_TREENODE) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj;
			selectNode(node);
			if (node != null) {
				TreeNodeBean bean = (TreeNodeBean) node.getUserObject();
				Set<Element> sets = new HashSet<Element>();
				sets.add(bean.getElement());
				editor.getPropertyModelInteractor().getGraphBusiProperty()
						.setElement(sets);
				editor.getPropertyModelInteractor().getGraphProperty()
						.setElement(sets);
			} else
				editor.getPropertyModelInteractor().getGraphBusiProperty()
						.setElement(null);
		}

	}

	/**
	 * add by yux,2009-1-21 增加树节点，用于图元新加入图中
	 * 
	 * @param parentElement：父文档节点
	 * @param name：图元名称
	 * @param element：节点
	 */
	public void addTreeNode(Element parentElement, String name, Element element) {
		DefaultMutableTreeNode parentNode = mapElement.get(parentElement);
		if (parentNode == null)
			return;
		ModelBean bean = editor.getSvgSession().getModelBeanBySymbolName(name);
		TreeNodeBean nodeBean = null;
		if (bean == null)
			nodeBean = new TreeNodeBean(name, element);
		else
			nodeBean = new TreeNodeBean(bean.getTypeBean().getName(), element);
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(nodeBean);

		parentNode.add(node);
		mapElement.put(element, node);
		addChildTreeNode(node, element);
		notifyRightPanel(EXPAND_TREENODE, node);
	}

	/**
	 * add by yux,2009-1-21 根据文档节点删除树节点
	 * 
	 * @param element：文档节点
	 */
	public void delTreeNode(Element element) {

		DefaultMutableTreeNode node = mapElement.get(element);
		if (node != null) {
			node.removeFromParent();
		}
		notifyRightPanel(EXPAND_TREENODE, null);
	}

	/**
	 * add by yux,2009-1-21 在模型树中按指定的树节点和文档节点，增加子树节点
	 * 
	 * @param parentNode：树父节点
	 * @param parentElement：文档父节点
	 */
	private void addChildTreeNode(DefaultMutableTreeNode parentNode,
			Element parentElement) {
		NodeList childList = parentElement.getChildNodes();
		int size = childList.getLength();
		for (int i = 0; i < size; i++) {
			if (childList.item(i) instanceof Element) {
				Element element = (Element) childList.item(i);
				if (element instanceof SVGOMUseElement) {
					// 图元
					String href = ((SVGOMUseElement) element).getHref()
							.getBaseVal().toString().substring(1);
					String symbolName = href.substring(0, href
							.indexOf(Constants.SYMBOL_STATUS_SEP));
					ModelBean bean = editor.getSvgSession()
							.getModelBeanBySymbolName(symbolName);
					if (bean != null) {
						ModelRelaIndunormBean relaBean = editor.getSvgSession()
								.getModelIDPropertyBySymbolID(symbolName);
						String id = getElementBussID(element, relaBean);
						String name = bean.getTypeBean().getName() + "-" + id;
						TreeNodeBean nodeBean = new TreeNodeBean(name, element);
						DefaultMutableTreeNode node = new DefaultMutableTreeNode(
								nodeBean);
						parentNode.add(node);
						mapElement.put(element, node);
					}
				} else if (element instanceof SVGOMGElement) {
					// 组合：描述，模板。。
					if (element.getAttribute(Constants.SYMBOL_TYPE) != null
							&& element
									.getAttribute(Constants.SYMBOL_TYPE)
									.equals(
											NCIEquipSymbolBean.SYMBOL_TYPE_TEMPLATE)) {
						// 模板
						String symbolName = element
								.getAttribute(Constants.SYMBOL_ID);
						ModelBean bean = editor.getSvgSession()
								.getModelBeanBySymbolName(symbolName);
						if (bean != null) {
							ModelRelaIndunormBean relaBean = editor
									.getSvgSession()
									.getModelIDPropertyBySymbolID(symbolName);
							String id = getElementBussID(element, relaBean);
							String name = bean.getTypeBean().getName() + "-"
									+ id;
							TreeNodeBean nodeBean = new TreeNodeBean(name,
									element);
							DefaultMutableTreeNode node = new DefaultMutableTreeNode(
									nodeBean);

							parentNode.add(node);
							mapElement.put(element, node);
							addChildTreeNode(node, element);
						}
					} else {

					}
				} else if (element instanceof SVGOMPathElement) {
					// 连接线
				} else if (element instanceof SVGOMTextElement) {
					// 文字
				}
			}
		}
	}

	/**
	 * add by yux,2009-1-21 根据节点，申请刷新树节点
	 * 
	 * @param element:文档节点
	 */
	public void refreshTreeNode(Element element) {
		DefaultMutableTreeNode node = mapElement.get(element);
		if (node == null) {
			return;
		}

		if (element instanceof SVGOMUseElement) {
			// 图元
			String href = ((SVGOMUseElement) element).getHref().getBaseVal()
					.toString().substring(1);
			String symbolName = href.substring(0, href
					.indexOf(Constants.SYMBOL_STATUS_SEP));
			ModelBean bean = editor.getSvgSession().getModelBeanBySymbolName(
					symbolName);
			ModelRelaIndunormBean relaBean = editor.getSvgSession()
					.getModelIDPropertyBySymbolID(symbolName);
			String id = getElementBussID(element, relaBean);
			String name = bean.getTypeBean().getName() + "-" + id;
			TreeNodeBean nodeBean = (TreeNodeBean) node.getUserObject();
			nodeBean.setText(name);
		} else if (element instanceof SVGOMGElement) {
			// 组合：描述，模板。。
			if (element.getAttribute(Constants.SYMBOL_TYPE) != null
					&& element.getAttribute(Constants.SYMBOL_TYPE).equals(
							NCIEquipSymbolBean.SYMBOL_TYPE_TEMPLATE)) {
				// 模板
				String symbolName = element.getAttribute(Constants.SYMBOL_ID);
				ModelBean bean = editor.getSvgSession()
						.getModelBeanBySymbolName(symbolName);
				ModelRelaIndunormBean relaBean = editor.getSvgSession()
						.getModelIDPropertyBySymbolID(symbolName);
				String id = getElementBussID(element, relaBean);
				String name = bean.getTypeBean().getName() + "-" + id;
				TreeNodeBean nodeBean = (TreeNodeBean) node.getUserObject();
				nodeBean.setText(name);
			} else {

			}
		}
		notifyRightPanel(EXPAND_TREENODE, node);
	}

	/**
	 * add by yux,2009-1-21 根据节点和模型规范关联数据获取业务唯一标识
	 * 
	 * @param element：节点
	 * @param bean：模型关联关联数据
	 * @return：如存在则返回业务唯一标识，不存在则返回null
	 */
	private String getElementBussID(Element element, ModelRelaIndunormBean bean) {
		if (element == null || bean == null)
			return null;
		String id = null;
		Element metadata = (Element) element.getElementsByTagName("metadata")
				.item(0);
		if (metadata == null)
			return null;

		Element el = (Element) element
				.getElementsByTagName(
						bean.getIndunormShortName() + ":"
								+ bean.getMetadataShortName()).item(0);
		if (el == null)
			return null;

		id = el.getAttribute(bean.getFieldShortName());
		return id;
	}

	/**
	 * 设置模型树
	 * 
	 * @param treeModel
	 *            the treeModel to set
	 */
	public void setTreeModel(TreeModel treeModel) {
		this.treeModel = treeModel;
	}

	/**
	 * 返回模型树
	 * 
	 * @return the treeModel
	 */
	public TreeModel getTreeModel() {
		return treeModel;
	}

	/**
	 * 返回
	 * 
	 * @return the modelTree
	 */
	public JTree getModelTree() {
		return modelTree;
	}

	public HashMap<Element, DefaultMutableTreeNode> getMapElement() {
		return mapElement;
	}
}
