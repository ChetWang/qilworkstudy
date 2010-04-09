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
 * �Ҽ��˵�������
 * 
 * @author Qil.Wong
 * 
 */
public class PopupManager {

	private WfEditor editor;

	/**
	 * �����˵����ϣ���WofoSimleFolderBean���󣬸������ͷ��࣬�����ӽڵ㰴classname����
	 */
	private Map<String, AbstractPopupMenu> popups = new HashMap<String, AbstractPopupMenu>();

	/**
	 * ���ƻ򿽱��Ķ���
	 */
	private DefaultMutableTreeNode copyedNode;

	/**
	 * ���б��
	 */
	private boolean cut = false;

	public PopupManager(WfEditor editor) {
		this.editor = editor;
		WfRunnable run = new WfRunnable("���ڳ�ʼ���˵�") {
			public void run() {
				// ԭ�ȷ���ͬһ���������µĴ���ʽ
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
	 * ���ݽڵ����ͣ���ȡ�����˵�
	 * 
	 * @param nodeObject
	 * @return
	 */
	public AbstractPopupMenu getPopup(Object nodeObject, WfTree tree) {
		AbstractPopupMenu popup = null;

		if (nodeObject instanceof WofoSimpleSet) {// �ļ������ʵĽڵ�
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
							"���൯���˵���δʵ��   " + nodeObject.getClass().getName());
				}
				if (popup != null) {
					popups.put(nodeObject.getClass().getName(), popup);
				}
			}
		}
		return popup;
	}

	/**
	 * ���ɼ�����˵�
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
	 * �����Ƿ��Ǽ���
	 * @param cut
	 */
	public void setCut(boolean cut) {
		this.cut = cut;
	}

	/**
	 * �ж��Ƿ��Ǽ���
	 * @return
	 */
	public boolean isCut() {
		return cut;
	}

}
