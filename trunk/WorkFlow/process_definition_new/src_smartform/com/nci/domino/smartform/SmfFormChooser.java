package com.nci.domino.smartform;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.jidesoft.popup.JidePopup;
import com.jidesoft.swing.JideScrollPane;
import com.nci.domino.WfEditor;
import com.nci.domino.beans.WofoActions;
import com.nci.domino.beans.WofoNetBean;
import com.nci.domino.beans.plugin.smartform.FormCategoryBean;
import com.nci.domino.beans.plugin.smartform.SmartFormBean;
import com.nci.domino.components.tree.WfBusyTextOverlayable;
import com.nci.domino.components.tree.WfTreeCellRenderer;
import com.nci.domino.concurrent.WfSwingWorker;
import com.nci.domino.help.Functions;
import com.nci.domino.help.WofoResources;

/**
 * 表单选择器
 * 
 * @author Qil.Wong
 * 
 */
public class SmfFormChooser {

	/**
	 * 选择表单的弹出窗口
	 */
	private JidePopup popup;

	private JideScrollPane scroll;

	/**
	 * 表单树
	 */
	private JTree formTree;

	private List<ActionListener> selectedListeners = new Vector<ActionListener>();

	private WfEditor editor;

	/**
	 * 只能选择主题目录
	 */
	public final static int SELECTED_CATEGORY = 0;

	/**
	 * 只能选择表单bean
	 */
	public final static int SELECTED_FORMBEAN = 1;

	/**
	 * 主题目录和表单bean都可以选择
	 */
	public final static int SELECTED_BOTH = 2;

	private int selectionMode = SELECTED_BOTH;

	private JPanel buttonPanel;

	private WfBusyTextOverlayable overlay;

	public SmfFormChooser(WfEditor editor) {
		this.editor = editor;
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		formTree = new JTree();
		formTree.setRootVisible(false);
		formTree.setCellRenderer(new SmfTreeCellRenderer());
		formTree.setModel(new DefaultTreeModel(null));
		formTree.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		scroll = new JideScrollPane(formTree);
		overlay = new WfBusyTextOverlayable(scroll, "{\n正在获取表单数据:f:gray}");
		Object o = editor.getCache().nowaitWhileNullGet("SMF_FORM_TREE");
		if (o == null) {
			initFormTree();
		} else {
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) o;
			DefaultTreeModel model = (DefaultTreeModel) formTree.getModel();
			model.setRoot(root);
		}

		formTree.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					checkSelection();
				}
			}
		});

		buttonPanel = new JPanel(new BorderLayout());
		JButton okBtn = new JButton(WofoResources.getValueByKey("ok"));
		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkSelection();
			}
		});
		buttonPanel.add(okBtn, BorderLayout.EAST);
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
	}

	/**
	 * 确定或双击后的检查，如果可以，就触发选择事件
	 */
	private void checkSelection() {
		DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode) formTree
				.getLastSelectedPathComponent();
		if (selectNode == null)
			return;
		Object o = selectNode.getUserObject();
		if (o instanceof FormCategoryBean && selectionMode == SELECTED_CATEGORY) {
			fireFormSelectedListeners();
		} else if (o instanceof SmartFormBean
				&& selectionMode == SELECTED_FORMBEAN) {
			fireFormSelectedListeners();
		} else if (selectionMode == SELECTED_BOTH) {
			fireFormSelectedListeners();
		}
	}

	/**
	 * 初始化表单树
	 */
	private void initFormTree() {
		overlay.setOverlayVisible(true);
		WfSwingWorker<DefaultMutableTreeNode, Object> worker = new WfSwingWorker<DefaultMutableTreeNode, Object>(
				"正在获取表单数据") {
			@Override
			protected DefaultMutableTreeNode doInBackground() throws Exception {

				WofoNetBean netBean = new WofoNetBean(
						WofoActions.GET_SMART_FORM_TREE, editor.getUserID(),
						null);
				netBean = Functions.getReturnNetBean(editor.getServletPath(),
						netBean);
				if (netBean != null) {
					return (DefaultMutableTreeNode) netBean.getParam();
				}
				return null;
			}

			@Override
			public void wfDone() {
				overlay.setOverlayVisible(false);
				DefaultTreeModel model = (DefaultTreeModel) formTree.getModel();
				try {
					DefaultMutableTreeNode root = get();
					model.setRoot(root);
					editor.getCache().cache("SMF_FORM_TREE", root);
					formTree.expandRow(0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		editor.getBackgroundManager().enqueueOpertimeQueue(worker);
	}

	/**
	 * 显示选择器
	 * 
	 * @param relateive
	 */
	public void showPopup(JComponent relateive) {
		popup = new JidePopup();
		formTree.clearSelection();

		popup.getContentPane().setLayout(new BorderLayout());
		popup.getContentPane().add(overlay, BorderLayout.CENTER);
		popup.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		popup.setMovable(true);
		popup.setPreferredPopupSize(new Dimension(200, 250));
		popup.showPopup(relateive);
	}

	protected void fireFormSelectedListeners() {
		ActionEvent evt = new ActionEvent(formTree, 0, "");
		for (ActionListener l : selectedListeners) {
			l.actionPerformed(evt);
		}
	}

	public void addFormSelectedListener(ActionListener l) {
		selectedListeners.add(l);
	}

	public void removeFormSelectedListener(ActionListener l) {
		selectedListeners.remove(l);
	}

	/**
	 * 获取选择的值
	 * 
	 * @return
	 */
	public Object getSelectedValue() {
		return ((DefaultMutableTreeNode) formTree
				.getLastSelectedPathComponent()).getUserObject();
	}

	/**
	 * 隐藏选择器
	 */
	public void hide() {
		popup.hidePopup();
	}

	/**
	 * 设置选择模式
	 * 
	 * @param selectionMode
	 *            选择模式
	 * @see SELECTED_CATEGORY,SELECTED_FORMBEAN,SELECTED_BOTH
	 */
	public void setSelectionMode(int selectionMode) {
		this.selectionMode = selectionMode;
	}

	private class SmfTreeCellRenderer extends WfTreeCellRenderer {

		protected ImageIcon getIcon(DefaultMutableTreeNode value,
				boolean expanded) {
			Object userObj = value.getUserObject();
			if (userObj instanceof FormCategoryBean) {
				return Functions.getImageIcon("smf_form_cat.gif");
			} else if (userObj instanceof SmartFormBean) {
				return Functions.getImageIcon("smf_form.gif");
			}
			return null;

		}
	}

}
