package com.nci.domino.components.tree.popup;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

import com.nci.domino.PaintBoardBasic;
import com.nci.domino.beans.desyer.WofoPackageBean;
import com.nci.domino.beans.desyer.WofoProcessBean;
import com.nci.domino.components.dialog.WfDialog;
import com.nci.domino.components.dialog.WfNewPackageDialog;
import com.nci.domino.components.dialog.importexport.LocalImportChooserDialog;
import com.nci.domino.components.dialog.process.WfNewProcessDialog;
import com.nci.domino.components.tree.WfTree;
import com.nci.domino.concurrent.WfSwingWorker;
import com.nci.domino.help.Functions;
import com.nci.domino.help.WofoResources;
import com.nci.domino.importexport.WofoProcessImportExport;
import com.nci.domino.utils.CopyableUtils;

/**
 * 流程树右键菜单
 * 
 * @author Qil.Wong
 * 
 */
public class WofoPackagePopupMenu extends AbstractPopupMenu {

	private static final long serialVersionUID = -9201365595964093888L;

	private enum Command {
		new_package, new_process,
		// new_process_set,
		SEPARATOR_1, import_edit, SEPARATOR_2, cut, copy, paste, delete, SEPARATOR_3, up, down, SEPARATOR_4, properties
	}

	public WofoPackagePopupMenu(WfTree tree) {
		super(tree);
		tree.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.isControlDown()) {
					if (e.getKeyCode() == KeyEvent.VK_U) {
						updown(true);
					} else if (e.getKeyCode() == KeyEvent.VK_D) {
						updown(false);
					}
				}
			}
		});
	}

	public void actionPerformed(ActionEvent e) {
		Command commandEnum = Command.valueOf(e.getActionCommand());

		switch (commandEnum) {
		case new_package:
			// 新建包:
			newPackage(e);
			break;
		case new_process:
			// 新建流程定义:
			newProcess(e);
			break;
		case import_edit:
			// 导入流程
			importProcess();
			break;
		case cut:
			// 剪切，除了设置拷贝节点，还要设置cut标记
			tree.getEditor().getPopupManager().setCopyedNode(
					tree.getSelectedNode());
			tree.getEditor().getPopupManager().setCut(true);
			break;
		case copy:
			tree.getEditor().getPopupManager().setCopyedNode(
					tree.getSelectedNode());
			tree.getEditor().getPopupManager().setCut(false);
			break;
		case paste:
			// 粘贴
			paste();
			break;
		case delete:
			// 删除
			deletePackage();
			break;
		case up:
			// 上移
			updown(true);
			break;
		case down:
			// 下移
			updown(false);
			break;
		case properties:
			// 显示属性
			showProperties(e);
			break;
		default:
			break;
		}
	}

	/**
	 * 删除包
	 */
	private void deletePackage() {
		WofoPackageBean packageBean = (WofoPackageBean) tree
				.getSelectedUserObject();
		int result = JOptionPane
				.showConfirmDialog(tree.getEditor(), "所有其下流程都将被删除！"
						+ "\n确定删除流程包\"" + packageBean.toString() + "\"?", "警告",
						JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.WARNING_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) tree
					.getSelectedNode().getParent();
			removeChildrenPaintBoard(tree.getSelectedNode());
			parent.remove(tree.getSelectedNode());
			tree.updateUI();
		}
	}

	/**
	 * 移掉所有节点对应的PaintBoard，包括子节点PaintBoard
	 * 
	 * @param node
	 */
	private void removeChildrenPaintBoard(DefaultMutableTreeNode node) {
		int childCount = node.getChildCount();
		// 不是根节点，继续迭代
		if (childCount > 0) {
			for (int i = 0; i < childCount; i++) {
				DefaultMutableTreeNode child = (DefaultMutableTreeNode) node
						.getChildAt(i);
				removeChildrenPaintBoard(child);
			}
		} else {
			// 是根节点并且是流程节点，就要移除已经打开的流程
			if (node.getUserObject() instanceof WofoProcessBean) {
				WofoProcessBean processBean = (WofoProcessBean) node
						.getUserObject();
				System.out.println(processBean);
				PaintBoardBasic board = tree.getEditor().getOperationArea()
						.getBoards().get(processBean.getID());
				tree.getEditor().getOperationArea()
						.removePaintBoardWithoutSave(board);
			}
		}
	}

	/**
	 * 导入流程
	 */
	private void importProcess() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				LocalImportChooserDialog dialog = (LocalImportChooserDialog) tree
						.getEditor().getDialogManager().getDialog(
								LocalImportChooserDialog.class, "导入本地单个流程",
								true);
				dialog.getFileChooser().getFilterSelector().setSelectedIndex(0);
				dialog.getFileChooser().getFilterSelector();
				dialog.showWfDialog(null);
				if (dialog.getDialogResult() == WfDialog.RESULT_AFFIRMED) {
					String storedFile = (String) dialog.getInputValues();
					((WofoProcessImportExport) dialog.getCurrentFilter())
							.importFromLocal(storedFile);
				}
			}
		});
	}

	/**
	 * 新建子包
	 * 
	 * @param e
	 */
	private void newPackage(ActionEvent e) {
		WfDialog dialog = tree.getEditor().getDialogManager().getDialog(
				WfNewPackageDialog.class,
				WofoResources.getValueByKey(e.getActionCommand()), true);
		WofoPackageBean parent = (WofoPackageBean) tree.getSelectedUserObject();
		int childCount = tree.getSelectedNode().getChildCount();
		int displayOrder = 0;
		for (int i = 0; i < childCount; i++) {
			DefaultMutableTreeNode child = (DefaultMutableTreeNode) tree
					.getSelectedNode().getChildAt(i);
			if (child.getUserObject() instanceof WofoPackageBean) {
				displayOrder++;
			}
		}
		WofoPackageBean newPackage = new WofoPackageBean(Functions.getUID(),
				parent);
		newPackage.setDisplayOrder(displayOrder);
		dialog.showWfDialog(newPackage);
		if (dialog.getDialogResult() == WfDialog.RESULT_AFFIRMED) {
			tree.appendNode(dialog.getInputValues(), true, true);
			tree.updateUI();
		}
	}

	/**
	 * 新建流程
	 * 
	 * @param e
	 */
	private void newProcess(ActionEvent e) {
		// 新建一个WofoProcessBean对象
		WofoProcessBean p = new WofoProcessBean(Functions.getUID());
		DefaultMutableTreeNode selectNode = tree.getSelectedNode();
		WofoPackageBean pm = (WofoPackageBean) selectNode.getUserObject();
		// p.setProcessMasterName(pm.getProcessMasterName());
		int childCount = selectNode.getChildCount();
		int displayOrder = 0;
		for (int i = 0; i < childCount; i++) {
			DefaultMutableTreeNode child = (DefaultMutableTreeNode) tree
					.getSelectedNode().getChildAt(i);
			if (child.getUserObject() instanceof WofoProcessBean) {
				displayOrder++;
			}
		}
		p.setDisplayOrder(displayOrder);
		p.setPackageId(pm.getPackageId());
		// 将新的WofoProcessBean对象赋予dialog，并显示，通过dialog对象进行属性操作
		WfDialog dialog = tree.getEditor().getDialogManager().getDialog(
				WfNewProcessDialog.class,
				WofoResources.getValueByKey(e.getActionCommand()), true);
		dialog.showWfDialog(p);
		if (dialog.getDialogResult() == WfDialog.RESULT_AFFIRMED) {
			WofoProcessBean input = (WofoProcessBean) dialog.getInputValues();
			DefaultMutableTreeNode node = tree.appendNode(input, true, true);
			tree.getEditor().getOperationArea().openProcess(input, node);
			tree.getEditor().getOperationArea().getCurrentPaintBoard()
					.applyDefault();
			tree.updateUI();
		}
	}

	/**
	 * 粘贴流程或包
	 */
	private void paste() {
		final DefaultMutableTreeNode copyedNode = tree.getEditor()
				.getPopupManager().getCopyedNode();
		tree.getEditor().getPopupManager().setCopyedNode(null);
		tree.setEnabled(false);
		String busyText = "正在执行粘贴操作";
		tree.getOverlayScroll().setBusyText(busyText);
		tree.getOverlayScroll().setOverlayVisible(true);
		WfSwingWorker<DefaultMutableTreeNode, Object> worker = new WfSwingWorker<DefaultMutableTreeNode, Object>(
				busyText) {

			@Override
			protected DefaultMutableTreeNode doInBackground() throws Exception {

				WofoPackageBean currentPackage = tree.getEditor()
						.getOperationArea().getCurrentPackage();
				Object newNodeVallue = null;

				if (copyedNode.getUserObject() instanceof WofoProcessBean) {
					// 这里不做处理，流程没有定义cut

				} else if (copyedNode.getUserObject() instanceof WofoPackageBean) {
					// 除了对象粘贴，还要改掉所有的id
					WofoPackageBean packageBean = (WofoPackageBean) copyedNode
							.getUserObject();
					// newCloedObj 的id变化,cut只需改变上下级关系及所属包的id，copy则和重新生成新版本一样
					if (tree.getEditor().getPopupManager().isCut()) {
						packageBean.setParentPackageId(currentPackage.getID());
						newNodeVallue = copyedNode;
					}
				}
				// 复制的对象要重新赋予新版本信息,除了对象粘贴，还要改掉所有的id
				if (!tree.getEditor().getPopupManager().isCut()) {
					newNodeVallue = Functions.deepClone(copyedNode);
					try {
						String clonedObjXML = CopyableUtils
								.toString(newNodeVallue);
						clonedObjXML = Functions.applyNewVersion(clonedObjXML);
						newNodeVallue = CopyableUtils.toObject(clonedObjXML);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				DefaultMutableTreeNode newNode = (DefaultMutableTreeNode) newNodeVallue;

				StringBuffer copyNamesBuffer = new StringBuffer("copy_");
				if (!tree.getEditor().getPopupManager().isCut()) {
					// 包要改名
					if (newNode.getUserObject() instanceof WofoPackageBean) {
						WofoPackageBean packageBean = (WofoPackageBean) newNode
								.getUserObject();
						checkCopyedNames(copyNamesBuffer, tree
								.getSelectedNode(), packageBean
								.getPackageName());
						packageBean.setPackageName(copyNamesBuffer.toString()
								+ packageBean.getPackageName());
					}
					// 流程要改名和编码
					if (newNode.getUserObject() instanceof WofoProcessBean) {
						WofoProcessBean procssBean = (WofoProcessBean) newNode
								.getUserObject();
						checkCopyedNames(copyNamesBuffer, tree
								.getSelectedNode(), procssBean.getProcessName());
						procssBean.setProcessName(copyNamesBuffer.toString()
								+ procssBean.getProcessName());
						procssBean.setProcessCode(copyNamesBuffer.toString()
								+ procssBean.getProcessCode());
					}
				}
				return newNode;
			}

			@Override
			public void wfDone() {
				try {
					tree.setEnabled(true);
					tree.getOverlayScroll().setOverlayVisible(false);
					DefaultMutableTreeNode newNode = get();
					if (newNode.getParent() != null) {
						((DefaultMutableTreeNode) newNode.getParent())
								.remove(newNode);
					}
					tree.appendNode(tree.getSelectedNode(), newNode, true,
							false);
					// 此前生成太多的字符串，需要强制回收一下
					System.gc();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		};
		tree.getEditor().getBackgroundManager().enqueueOpertimeQueue(worker);
	}

	/**
	 * 判断新名字前要加多少个“copy_”
	 * 
	 * @param sb
	 * @param parentNode
	 * @param originalName
	 */
	private void checkCopyedNames(StringBuffer sb,
			DefaultMutableTreeNode parentNode, String originalName) {
		for (int i = 0; i < parentNode.getChildCount(); i++) {
			if (((DefaultMutableTreeNode) parentNode.getChildAt(i))
					.getUserObject().toString().equals("copy_" + originalName)) {
				sb.append("copy_");
//				originalName =;
				checkCopyedNames(sb, parentNode,  "copy_" + originalName);
			}
		}
	}

	/**
	 * 上移下移
	 * 
	 * @param flag
	 *            true为上移，false为下移
	 */
	private void updown(boolean flag) {
		try {
			DefaultMutableTreeNode selectNode = tree.getSelectedNode();
			if (selectNode == null) {
				return;
			}
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectNode
					.getParent();
			if (selectNode.getUserObject() instanceof WofoPackageBean == false) {
				return;
			}
			if (flag && parent.getIndex(selectNode) == 0) {
				return;
			}
			if (!flag
					&& parent.getIndex(selectNode) == parent.getChildCount() - 1) {
				return;
			}

			WofoPackageBean packageBean = (WofoPackageBean) selectNode
					.getUserObject();
			DefaultMutableTreeNode previous = selectNode.getPreviousSibling();
			DefaultMutableTreeNode next = selectNode.getNextSibling();
			int currentDisplayOrder = packageBean.getDisplayOrder();
			if (flag) { // 上移与上一个package交换displayorder
				int previousDisplayorder = ((WofoPackageBean) previous
						.getUserObject()).getDisplayOrder();
				packageBean.setDisplayOrder(previousDisplayorder);
				((WofoPackageBean) previous.getUserObject())
						.setDisplayOrder(currentDisplayOrder);
			} else {// 下移与下一个package交换displayorder
				int nextDisplayorder = ((WofoPackageBean) next.getUserObject())
						.getDisplayOrder();
				packageBean.setDisplayOrder(nextDisplayorder);
				((WofoPackageBean) next.getUserObject())
						.setDisplayOrder(currentDisplayOrder);
			}
			int index = parent.getIndex(selectNode);
			parent.insert(selectNode, flag ? index - 1 : index + 1);
			removeChildrenPaintBoard(selectNode);
		} finally {
			tree.updateUI();
		}
	}

	/**
	 * 显示包属性
	 * 
	 * @param e
	 */
	private void showProperties(ActionEvent e) {
		WfDialog dialog = tree.getEditor().getDialogManager().getDialog(
				WfNewPackageDialog.class,
				WofoResources.getValueByKey(e.getActionCommand()), true);
		WofoPackageBean packageBean = (WofoPackageBean) tree
				.getSelectedUserObject();
		dialog.showWfDialog(packageBean);
		if (dialog.getDialogResult() == WfDialog.RESULT_AFFIRMED) {
			packageBean = (WofoPackageBean) dialog.getInputValues();
			tree.getSelectedNode().setUserObject(packageBean);
			tree.updateUI();
		}
	}

	protected List<Enum> getCommands() {
		Command[] comm = Command.values();
		List<Enum> l = new ArrayList<Enum>();
		for (int i = 0; i < comm.length; i++) {
			l.add(comm[i]);
		}
		return l;
	}

	@Override
	public void checkMenus() {
		WofoPackageBean pb = (WofoPackageBean) tree.getSelectedUserObject();
		boolean isRoot = pb.isRootNode();
		Component[] c = this.getComponents();
		for (int i = 1; i < c.length; i++) {
			c[i].setEnabled(!isRoot);

		}
		// 第7个元素是“粘贴”菜单
		c[7]
				.setEnabled(tree.getEditor().getPopupManager().getCopyedNode() != null);
		DefaultMutableTreeNode selectNode = tree.getSelectedNode();
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectNode
				.getParent();
		if (parent != null) {
			int index = parent.getIndex(selectNode);
			// 第10个元素是“上移”菜单
			c[10].setEnabled(index != 0);
			c[11].setEnabled(index != parent.getChildCount() - 1);
		}
	}

}
