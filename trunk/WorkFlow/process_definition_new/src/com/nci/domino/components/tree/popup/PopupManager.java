package com.nci.domino.components.tree.popup;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.tree.DefaultMutableTreeNode;

import com.nci.domino.WfEditor;
import com.nci.domino.beans.WofoSimpleSet;
import com.nci.domino.beans.desyer.WofoActivityBean;
import com.nci.domino.beans.desyer.WofoPackageBean;
import com.nci.domino.beans.desyer.WofoProcessBean;
import com.nci.domino.beans.desyer.WofoProcessMasterBean;
import com.nci.domino.components.tree.WfTree;
import com.nci.domino.concurrent.WfRunnable;
import com.nci.domino.concurrent.WfStartupEndException;
import com.nci.domino.help.Functions;

/**
 * 右键菜单管理器
 * 
 * @author Qil.Wong
 * 
 */
public class PopupManager {

	private WfEditor editor;

	/**
	 * 弹出菜单集合，对WofoSimleFolderBean对象，根据类型分类，其它子节点按classname分类
	 */
	private Map<String, AbstractPopupMenu> popups = new HashMap<String, AbstractPopupMenu>();

	/**
	 * 复制或拷贝的对象
	 */
	private DefaultMutableTreeNode copyedNode;

	/**
	 * 剪切标记
	 */
	private boolean cut = false;

	public PopupManager(WfEditor editor) {
		this.editor = editor;
		WfRunnable run = new WfRunnable("正在初始化菜单") {
			public void run() {
				// 原先放在同一个流程树下的处理方式
				// WofoSimpleSetBean[] wss = new WofoSimpleSetBean[6];
				// for (int i = 0; i < wss.length; i++) {
				// wss[i] = new WofoSimpleSetBean(i, "");
				// getPopup(wss[i]);
				// }

				Object[] other = new Object[] {
						// new WofoActivityBean(Functions.getUID()),
						new WofoProcessBean(Functions.getUID()),
						new WofoPackageBean(Functions.getUID()),
						new WofoProcessMasterBean(Functions.getUID()) };
				for (int i = 0; i < other.length; i++) {
					getPopup(other[i], PopupManager.this.editor
							.getOperationArea().getWfTree());
				}
			}
		};
		try {
			editor.getBackgroundManager().enqueueStartupQueue(run);
		} catch (WfStartupEndException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据节点类型，获取弹出菜单
	 * 
	 * @param nodeObject
	 * @return
	 */
	public AbstractPopupMenu getPopup(Object nodeObject, WfTree tree) {
		AbstractPopupMenu popup = null;

		if (nodeObject instanceof WofoSimpleSet) {// 文件夹性质的节点
			WofoSimpleSet bean = (WofoSimpleSet) nodeObject;
			int type = bean.getType();
			popup = popups.get(WofoSimpleSet.class.getName()
					+ String.valueOf(type));
			if (popup == null) {
				popup = generateSimpleSetPopup(type, tree);
				popups.put(WofoSimpleSet.class.getName()
						+ String.valueOf(type), popup);
			}
		} else {
			popup = popups.get(String.valueOf(nodeObject.getClass().getName()));
			if (popup == null) {
				if (nodeObject instanceof WofoActivityBean) {
					popup = new ActivityDefinitionPopupMenu(tree);
				} else if (nodeObject instanceof WofoProcessBean) {
					popup = new WofoProcessPopupMenu(tree);
				} else if (nodeObject instanceof WofoPackageBean) {
					popup = new WofoPackagePopupMenu(tree);
				} else if (nodeObject instanceof WofoProcessMasterBean) {
					popup = new WofoResourceSetPopupMenu(tree);
				} else {
					Logger.getLogger(PopupManager.class.getName()).log(
							Level.SEVERE,
							"该类弹出菜单还未实现   " + nodeObject.getClass().getName());
				}
				if (popup != null) {
					popups.put(nodeObject.getClass().getName(), popup);
				}
			}
		}
		return popup;
	}

	/**
	 * 生成集合类菜单
	 * 
	 * @param type
	 * @return
	 */
	private AbstractPopupMenu generateSimpleSetPopup(int type, WfTree tree) {
		AbstractPopupMenu popup = null;
		switch (type) {
		case WofoSimpleSet.BUSINESS_ITEM_TYPE:
			popup = new ActivityDefinitionSetBusinessPopupMenu(tree);
			break;
		case WofoSimpleSet.BUSINESS_WORKFLOW_TYPE:
			popup = new WofoDefinitionSetPopupMenu(tree);
			break;
		case WofoSimpleSet.WORKFLOW_CONDITION_TYPE:
			popup = new WofoConditionSetPopupMenu(tree);
			break;
		case WofoSimpleSet.WORKFLOW_EVENT_TYPE:
			popup = new WofoEventSetPopupMenu(tree);
			break;
		case WofoSimpleSet.WORKFLOW_MESSAGE_TYPE:
			popup = new WofoMessageSetPopupMenu(tree);
			break;
		case WofoSimpleSet.WORKFLOW_PARAMETER_TYPE:
			popup = new WofoParamSetPopupMenu(tree);
			break;
		case WofoSimpleSet.WORKFLOW_ITEM_TYPE:
			popup = new ActivityDefinitionSetWofoPopupMenu(tree);
			break;
		default:
			break;
		}
		return popup;
	}

	public DefaultMutableTreeNode getCopyedNode() {
		return copyedNode;
	}

	public void setCopyedNode(DefaultMutableTreeNode copyedNode) {
		this.copyedNode = copyedNode;
	}

	/**
	 * 设置是否是剪切
	 * @param cut
	 */
	public void setCut(boolean cut) {
		this.cut = cut;
	}

	/**
	 * 判断是否是剪切
	 * @return
	 */
	public boolean isCut() {
		return cut;
	}

}
