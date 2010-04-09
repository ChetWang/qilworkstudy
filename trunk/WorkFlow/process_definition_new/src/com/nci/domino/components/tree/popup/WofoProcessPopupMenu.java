package com.nci.domino.components.tree.popup;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;

import com.jidesoft.popup.JidePopup;
import com.jidesoft.swing.JideScrollPane;
import com.nci.domino.PaintBoardBasic;
import com.nci.domino.beans.WofoActions;
import com.nci.domino.beans.WofoNetBean;
import com.nci.domino.beans.WofoVerifyResult;
import com.nci.domino.beans.desyer.WofoProcessBean;
import com.nci.domino.components.dialog.WfDialog;
import com.nci.domino.components.dialog.importexport.LocalExportChooserDialog;
import com.nci.domino.components.dialog.process.WfNewProcessDialog;
import com.nci.domino.components.tree.WfBusyTextOverlayable;
import com.nci.domino.components.tree.WfTree;
import com.nci.domino.concurrent.WfSwingWorker;
import com.nci.domino.help.Functions;
import com.nci.domino.help.WofoResources;
import com.sun.org.apache.xml.internal.serializer.Version;

/**
 * �������Ҽ��˵�,ҵ������
 * 
 * @author Qil.Wong
 * 
 */
public class WofoProcessPopupMenu extends AbstractPopupMenu {

	private enum Command {
		export_edit, SEPARATOR_2, copy, delete, SEPARATOR, up, down, SEPARATOR_1, version, properties
	}

	public WofoProcessPopupMenu(WfTree tree) {
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
		case export_edit:
			export();
			break;
		case copy:
			tree.getEditor().getPopupManager().setCopyedNode(
					tree.getSelectedNode());
			tree.getEditor().getPopupManager().setCut(false);
			break;
		case delete:
			WofoProcessBean processBean = (WofoProcessBean) tree.getEditor()
					.getOperationArea().getCurrentProcess();
			int result = JOptionPane.showConfirmDialog(tree.getEditor(),
					"ȷ��ɾ������\"" + processBean.toString() + "\"?", "����",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			if (result == JOptionPane.OK_OPTION) {
				DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) tree
						.getSelectedNode().getParent();
				parentNode.remove(tree.getSelectedNode());
				PaintBoardBasic board = tree.getEditor().getOperationArea()
						.getBoards().get(processBean.getID());
				tree.getEditor().getOperationArea()
						.removePaintBoardWithoutSave(board);
				tree.updateUI();
			}
			break;
		case up:
			updown(true);
			break;
		case down:
			updown(false);
			break;
		case version:
			showVersion(e);
			break;
		case properties:
			showProperties(e);
			break;
		default:
			break;

		}
	}

	/**
	 * �����ļ�
	 */
	private void export() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				LocalExportChooserDialog dialog = (LocalExportChooserDialog) tree
						.getEditor().getDialogManager().getDialog(
								LocalExportChooserDialog.class, "������������", true);
				dialog.getFileChooser().getFilterSelector().setSelectedIndex(0);
				dialog.showWfDialog(null);
				if (dialog.getDialogResult() == WfDialog.RESULT_AFFIRMED) {
					String storedFile = (String) dialog.getInputValues();
					dialog.getCurrentFilter().export(storedFile);
				}
			}
		});
	}

	/**
	 * ��������
	 * 
	 * @param flag
	 *            trueΪ���ƣ�falseΪ����
	 */
	private void updown(boolean flag) {
		try {
			DefaultMutableTreeNode selectNode = tree.getSelectedNode();
			if (selectNode == null) {
				return;
			}
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectNode
					.getParent();
			if (selectNode.getUserObject() instanceof WofoProcessBean == false) {
				return;
			}
			if (flag && parent.getIndex(selectNode) == 0) {
				return;
			}
			if (!flag
					&& parent.getIndex(selectNode) == parent.getChildCount() - 1) {
				return;
			}

			WofoProcessBean processBean = (WofoProcessBean) selectNode
					.getUserObject();
			DefaultMutableTreeNode previous = selectNode.getPreviousSibling();
			DefaultMutableTreeNode next = selectNode.getNextSibling();
			int currentDisplayOrder = processBean.getDisplayOrder();
			if (flag) { // ��������һ��package����displayorder
				int previousDisplayorder = ((WofoProcessBean) previous
						.getUserObject()).getDisplayOrder();
				processBean.setDisplayOrder(previousDisplayorder);
				((WofoProcessBean) previous.getUserObject())
						.setDisplayOrder(currentDisplayOrder);
			} else {// ��������һ��package����displayorder
				int nextDisplayorder = ((WofoProcessBean) next.getUserObject())
						.getDisplayOrder();
				processBean.setDisplayOrder(nextDisplayorder);
				((WofoProcessBean) next.getUserObject())
						.setDisplayOrder(currentDisplayOrder);
			}
			int index = parent.getIndex(selectNode);
			parent.insert(selectNode, flag ? index - 1 : index + 1);
			// removeChildrenPaintBoard(selectNode);
		} finally {
			tree.updateUI();
		}
	}

	private SimpleDateFormat versionDateFmt = new SimpleDateFormat(
			"yyyy.MM.dd HH:mm");

	/**
	 * ��ʾ�汾��Ϣ
	 */
	private void showVersion(ActionEvent e) {
		MouseEvent clickEvt = (MouseEvent) getClientProperty("MouseEvent");
		JidePopup popup = new JidePopup();
		popup.setPreferredSize(new Dimension(250, 250));
		popup.setMovable(true);
		final JTable table = new JTable();
		JideScrollPane scroll = createVersionTable(table);
		String busiText = "���ڻ�ȡ�汾��Ϣ";
		final WfBusyTextOverlayable overLay = new WfBusyTextOverlayable(scroll,
				busiText);
		overLay.setOverlayVisible(true);
		popup.getContentPane().setLayout(new BorderLayout());
		popup.getContentPane().add(overLay, BorderLayout.CENTER);

		// �������˰汾
		WfSwingWorker<List<WofoProcessBean>, Object> worker = new WfSwingWorker<List<WofoProcessBean>, Object>(
				busiText) {

			@Override
			protected List<WofoProcessBean> doInBackground() throws Exception {
				WofoNetBean netBean = new WofoNetBean(WofoActions.LOAD_VERSION,
						tree.getEditor().getUserID(), tree.getEditor()
								.getOperationArea().getCurrentProcess());
				WofoNetBean result = Functions.getReturnNetBean(tree
						.getEditor().getServletPath(), netBean);
				return (List<WofoProcessBean>) result.getParam();
			}

			@Override
			public void wfDone() {
				try {
					List<WofoProcessBean> versions = get();
					DefaultTableModel model = (DefaultTableModel) table
							.getModel();
					// ��ʾ3�У��ܹ����У����������أ����ڴ�������汾����
					for (int i = 0; i < versions.size(); i++) {
						WofoProcessBean processBean = versions.get(i);
						//�汾����Ҫ��ԭʼ�������ֿ������Ըı������ֺ�id
						processBean.setProcessName(processBean.getProcessName()
								+ " v1." + processBean.getProcessVersion());
						processBean.setID(processBean.getID()
								+ processBean.getProcessVersion());
						model.addRow(new Object[] {
								"1." + versions.get(i).getProcessVersion(),
								versionDateFmt.format(versions.get(i)
										.getCreateDate()),
								versions.get(i).getCreatorId(), processBean });
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					overLay.setOverlayVisible(false);
				}
			}
		};

		tree.getEditor().getBackgroundManager().enqueueOpertimeQueue(worker);

		popup.showPopup((int) clickEvt.getLocationOnScreen().getX(),
				(int) clickEvt.getLocationOnScreen().getY(), tree);
	}

	/**
	 * �����汾��ʾ���
	 * 
	 * @return
	 */
	private JideScrollPane createVersionTable(final JTable table) {
		JideScrollPane scroll = new JideScrollPane(table);
		table.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		table.setModel(new javax.swing.table.DefaultTableModel(null,
				new String[] { "�汾", "�޸�����", "�޸���", "" }) {
			Class[] types = new Class[] { String.class, String.class,
					String.class };

			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return false;
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(25);
		table.getColumnModel().getColumn(1).setPreferredWidth(120);
		table.getColumnModel().getColumn(2).setPreferredWidth(50);
		Functions.hideColumn(table, 3);
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2
						&& e.getButton() == MouseEvent.BUTTON1) {
					DefaultTableModel model = (DefaultTableModel) table
							.getModel();
					int index = table.getSelectedRow();
					if (index >= 0) {
						WofoProcessBean processBean = (WofoProcessBean) model
								.getValueAt(index, 3);
//						processBean.setProcessName(processBean.getProcessName()
//								+ " v1." + processBean.getProcessVersion());
//						processBean.setID(processBean.getID()
//								+ processBean.getProcessVersion());
						tree.getEditor().getOperationArea().openProcess(
								processBean, null);
					}
				}
			}
		});
		return scroll;
	}

	/**
	 * ��ʾ��������
	 * 
	 * @param e
	 */
	private void showProperties(ActionEvent e) {
		WfDialog dialog = tree.getEditor().getDialogManager().getDialog(
				WfNewProcessDialog.class,
				WofoResources.getValueByKey(e.getActionCommand()), true);
		WofoProcessBean processBean = tree.getEditor().getOperationArea()
				.getCurrentProcess();
		dialog.showWfDialog(processBean);
		if (dialog.getDialogResult() == WfDialog.RESULT_AFFIRMED) {
			WofoProcessBean treeObject = (WofoProcessBean) tree
					.getSelectedNode().getUserObject();
			dialog.getInputValues();
			// ���������������ù�������Ϊ�ڵ��չ����ʽ��processName��
			// cloned�Ľڵ�process���ָı��ԴprocessBeanҲҪ���������������޷�������ʾ
			treeObject.setProcessName(processBean.getProcessName());
			tree.updateUI();
			PaintBoardBasic board = tree.getEditor().getOperationArea()
					.getBoards().get(processBean.getID());
			if (board != null) {
				int index = tree.getEditor().getOperationArea()
						.getOperationTab().indexOfComponent(
								board.getPaintBoardScroll());
				// ����tab����
				tree.getEditor().getOperationArea().getOperationTab()
						.setTitleAt(index, processBean.getProcessName());
			}
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
		Component[] c = this.getComponents();
		DefaultMutableTreeNode selectNode = tree.getSelectedNode();
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectNode
				.getParent();
		if (parent != null) {
			int index = parent.getIndex(selectNode);
			// ��10��Ԫ���ǡ����ơ��˵�
			c[5].setEnabled(index != 0);
			c[6].setEnabled(index != parent.getChildCount() - 1);
		}
	}

}
