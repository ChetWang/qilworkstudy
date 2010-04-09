package com.nci.domino.components.operation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.w3c.dom.Document;

import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JidePopupMenu;
import com.jidesoft.swing.JideSwingUtilities;
import com.jidesoft.swing.JideTabbedPane;
import com.nci.domino.PaintBoard;
import com.nci.domino.PaintBoardBasic;
import com.nci.domino.PaintBoardScroll;
import com.nci.domino.WfEditor;
import com.nci.domino.WfEditor.WfEditorPackedListener;
import com.nci.domino.beans.WofoSimpleSet;
import com.nci.domino.beans.desyer.WofoPackageBean;
import com.nci.domino.beans.desyer.WofoProcessBean;
import com.nci.domino.components.autocomplete.AutoCompletionField;
import com.nci.domino.components.tree.WfTree;
import com.nci.domino.concurrent.WfRunnable;
import com.nci.domino.help.BoardOpenListener;
import com.nci.domino.help.BoardSwitchListener;
import com.nci.domino.help.Functions;
import com.nci.domino.help.WofoResources;

/**
 * 操作区，包括操作树和绘图区
 * 
 * @author Qil.Wong
 */
public class WfOperationArea extends JSplitPane {

	private static final long serialVersionUID = 8581042750655007502L;

	private WfEditor editor = null;

	private WfTree wfTree;

	private JideTabbedPane operationTab;

	private CloseTabPopup closeTabPopup = null;

	// 活动组
	private WfOperationSimpleSetArea activityArea;

	// 条件组
	private WfOperationSimpleSetArea conditionArea;

	private Map<String, PaintBoard> boards = new LinkedHashMap<String, PaintBoard>();

	private List<BoardSwitchListener> switchListeners = new Vector<BoardSwitchListener>();

	private List<BoardOpenListener> openListeners = new Vector<BoardOpenListener>();

	private AutoCompletionField filterField = new AutoCompletionField();

	private JideButton refreshTreeBtn = new JideButton(Functions
			.getImageIcon("refresh.gif"));

	public WfOperationArea(WfEditor wfEditor) {
		this.editor = wfEditor;
		init();
	}

	private void init() {
		setDividerLocation(180);
		setResizeWeight(0.14);
		setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		WofoPackageBean rootPackage = new WofoPackageBean(Functions.getUID());
		rootPackage.setPackageName("所有流程");
		wfTree = new WfTree(this, rootPackage);
		WofoSimpleSet ssb = new WofoSimpleSet(WofoSimpleSet.WORKFLOW_ITEM_TYPE,
				WofoResources.getValueByKey("workflow_item"));
		activityArea = new WfTemplateActivityArea(this, ssb);
		activityArea.setEnabled(false);
		ssb = new WofoSimpleSet(WofoSimpleSet.WORKFLOW_CONDITION_TYPE,
				WofoResources.getValueByKey("workflow_condition"));
		conditionArea = new WfConditionArea(this, ssb);
		conditionArea.setEnabled(false);
		final JSplitPane leftSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		JPanel treePanel = new JPanel(new BorderLayout());
		JPanel filterPanel = new JPanel(new GridBagLayout());
		refreshTreeBtn.setToolTipText("刷新流程树，从服务端重新获取数据");
		refreshTreeBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				refresh();
			}
		});
		filterField.setToolTipText("输入条件，定位包或流程");
		GridBagConstraints cons = new GridBagConstraints();
		cons.insets = new Insets(3, 3, 3, 3);
		cons.gridx = 0;
		cons.gridy = 0;
		cons.weightx = 1;
		cons.fill = GridBagConstraints.BOTH;
		filterPanel.add(filterField, cons);
		filterField.getPopup().addListSelectionListener(
				new ListSelectionListener() {

					public void valueChanged(ListSelectionEvent e) {
						searchSelectionNode();
					}
				});
		filterField.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				searchSelectionNode();

			}
		});
		cons.gridx = 1;
		cons.weightx = 0;
		filterPanel.add(refreshTreeBtn, cons);
		treePanel.add(filterPanel, BorderLayout.NORTH);
		treePanel.add(wfTree.getOverlayScroll(), BorderLayout.CENTER);
		leftSplit.setTopComponent(treePanel);
		JideTabbedPane tab = new JideTabbedPane();
		tab.setBoldActiveTab(true);
		tab.addTab("流程环节", null, activityArea, null);
		tab.addTab("流程条件", null, conditionArea, null);
		leftSplit.setBottomComponent(tab);
		leftSplit.setOneTouchExpandable(true);
		setTopComponent(leftSplit);
		editor.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				int h = leftSplit.getHeight();
				// 让tab只占三分之一的空间
				leftSplit.setDividerLocation(2 * h / 3);
			}
		});
		editor.addEditorPackedListener(new WfEditorPackedListener() {

			public void uiPacked(WfEditor editor) {
				int h = leftSplit.getHeight();
				// 让tab只占三分之一的空间
				leftSplit.setDividerLocation(2 * h / 3);
			}
		});
		operationTab = new GradientTabPane();
		operationTab.setTabPlacement(JideTabbedPane.TOP);
		operationTab.setBoldActiveTab(true);
		operationTab.setCloseAction(null);
		operationTab.setCloseTabOnMouseMiddleButton(true);
		operationTab.setScrollSelectedTabOnWheel(true);
		operationTab.setShowCloseButton(true);
		operationTab.setShowCloseButtonOnTab(true);
		operationTab.setShowCloseButtonOnSelectedTab(true);
		operationTab.setCloseAction(new TabCloseAction());
		operationTab.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				if (evt.getButton() == MouseEvent.BUTTON3) {
					rightClickTab(evt);
				}
			}
		});
		setBottomComponent(operationTab);
		wfTree.getSelectionModel().addTreeSelectionListener(
				new TreeSelectionListener() {
					public void valueChanged(TreeSelectionEvent e) {
						wfTreeSelectionChanged(e);
					}
				});
		addBoardSwitchListener(new BoardSwitchListener() {

			public void boardSwitched(PaintBoard currentBoard) {
				if (currentBoard == null) {
					wfTree.clearSelection();
				} else {
					if (currentBoard.getFlowNode() != null) {
						wfTree.setSelectionPath(new TreePath(currentBoard
								.getFlowNode().getPath()));
					}
				}

				if (currentBoard != null) {
					currentBoard.setCursor(Functions.getCursor(editor
							.getModeManager().getToolMode()));
				}
				editor.getToolBar().getButtons().get("image_export")
						.setEnabled(currentBoard != null);
				editor.getToolBar().getButtons().get("print").setEnabled(
						currentBoard != null);
				editor.getToolBar().getButtons().get("zoomIn").setEnabled(
						currentBoard != null);
				editor.getToolBar().getButtons().get("zoomOut").setEnabled(
						currentBoard != null);
				editor.getToolBar().getButtons().get("undo").setEnabled(
						currentBoard != null);
				editor.getToolBar().getButtons().get("redo").setEnabled(
						currentBoard != null);
				editor.getToolBar().getButtons().get("restore_ui").setEnabled(
						currentBoard != null);
				// editor.getToolBar().getButtons().get("fit_screen").setEnabled(
				// currentBoard != null);
				if (currentBoard != null) {
					// 选择状态下的对齐菜单变化
					currentBoard.shapesSelected(currentBoard
							.getSelectedShapes());
				}
			}
		});
	}

	private void searchSelectionNode() {
		Object o = filterField.getPopup().getSelectedValue();
		DefaultMutableTreeNode node = wfTree.getNodeByValue(o);
		TreePath path = new TreePath(node.getPath());
		wfTree.scrollPathToVisible(path);
		wfTree.setSelectionPath(path);
		wfTree.updateUI();
	}

	/**
	 * 不保存就移除面板
	 * 
	 * @param paintBoard
	 *            被移除的面板
	 */
	public void removePaintBoardWithoutSave(PaintBoardBasic paintBoard) {
		if (paintBoard != null) {
			getBoards().remove(paintBoard.getProcessBean().getID());
			getOperationTab().remove(paintBoard.getPaintBoardScroll());
		}
	}

	/**
	 * @return the editor
	 */
	public WfEditor getEditor() {
		return editor;
	}

	private void refresh() {
		int result = JOptionPane.showConfirmDialog(editor,
				"刷新流程树将强制关闭已打开的绘图工作区，并丢失所有未保存流程的修改数据！\n确定刷新？", "警告",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			closeAllTabsWithoutSave();
			wfTree.getInitData();
			editor.getStatusBar().showGlassInfo("刷新流程数据，重新获取服务器数据样本");
		}
	}

	/**
	 * 流程树节点选择变化
	 * 
	 * @param e
	 */
	private void wfTreeSelectionChanged(TreeSelectionEvent e) {
		Object o = wfTree.getSelectedUserObject();
		if (o instanceof WofoProcessBean) {// 双击流程节点，打开绘图面板
			WofoProcessBean wpb = (WofoProcessBean) o;
			if (boards.get(wpb.getID()) != null) {
				setSelectedPaintBoard(boards.get(wpb.getID()));
			}

		}
		conditionArea.treeSelectionChanged(e);
		activityArea.treeSelectionChanged(e);
	}

	/**
	 * tab标签上的鼠标右键
	 * 
	 * @param evt
	 */
	private void rightClickTab(MouseEvent evt) {
		if (operationTab.getTabCount() > 0) {
			int index = operationTab.getTabAtLocation(evt.getX(), evt.getY());
			operationTab.setSelectedIndex(index);
			if (closeTabPopup == null) {
				closeTabPopup = new CloseTabPopup();
			}
			if (operationTab.getTabCount() == 1) {
				closeTabPopup.getCloseOthersMenuItem().setEnabled(false);
			} else {
				closeTabPopup.getCloseOthersMenuItem().setEnabled(true);
			}

			closeTabPopup.show(operationTab, evt.getX(), evt.getY());
		}
	}

	/**
	 * 关闭当前绘图区
	 */
	private void closeCurrentTab() {
		PaintBoard board = getCurrentPaintBoard();
		if (board.isReadyToCloseWithCancel()) {
			operationTab.remove(board.getPaintBoardScroll());
			boards.remove(board.getProcessBean().getID());
			editor.getStatusBar().showGlassInfo(
					"关闭了\"" + board.getName() + "\"绘图区");
		}
	}

	/**
	 * 关闭其它绘图区
	 */
	private void closeOtherTabs() {
		PaintBoard currentBoard = getCurrentPaintBoard();
		int compCount = operationTab.getTabCount();
		boolean[] flags = new boolean[compCount];
		boolean cancel = false;
		for (int i = compCount - 1; i >= 0; i--) {
			PaintBoard board = ((PaintBoardScroll) operationTab
					.getComponentAt(i)).getBoard();
			if (board != currentBoard) {
				flags[i] = board.isReadyToCloseWithCancel();
				if (!flags[i]) {
					cancel = true;
					break;
				}
			}
		}
		// 如果是确定关闭，则要移除掉
		if (!cancel) {
			for (int i = compCount - 1; i >= 0; i--) {
				PaintBoard board = ((PaintBoardScroll) operationTab
						.getComponentAt(i)).getBoard();
				if (board != currentBoard) {
					operationTab.remove(operationTab.getComponentAt(i));
					boards.remove(board.getProcessBean().getID());
				}
			}
			editor.getStatusBar().showGlassInfo(
					"关闭了\"" + currentBoard.getName() + "\"外其它绘图区");
		}
	}

	/**
	 * 打开流程编辑
	 * 
	 * @param wpb
	 *            WofoProcessBean流程对象
	 */
	@SuppressWarnings( { "unchecked" })
	public void openProcess(WofoProcessBean processBean,
			DefaultMutableTreeNode node) {
		// 读取配置

		PaintBoard board = boards.get(processBean.getID());
		if (board == null) {
			board = new PaintBoard(editor, node);
			WofoProcessBean clonedProcess = (WofoProcessBean) Functions
					.deepClone(processBean);
			board.setProcessBean(clonedProcess);
			operationTab.addTab(processBean.toString(), new PaintBoardScroll(
					board));
			boards.put(processBean.getID(), board);
			// 开始解析图形
			board.openGraphs(clonedProcess, true);
			fireBoardOpen(board);
		}
		if (board.getPaintBoardScroll() != operationTab.getSelectedComponent())
			setSelectedPaintBoard(board);
	}

	/**
	 * 获取当前的绘图面板
	 * 
	 * @return
	 */
	public PaintBoard getCurrentPaintBoard() {
		PaintBoardScroll parent = (PaintBoardScroll) operationTab
				.getSelectedComponent();
		if (parent == null)
			return null;
		return (PaintBoard) parent.getBoard();
	}

	/**
	 * 设置当前选中的绘图区
	 * 
	 * @param board
	 */
	public void setSelectedPaintBoard(PaintBoard board) {
		if (board != null) {
			operationTab.setSelectedComponent(board.getPaintBoardScroll());
		}
	}

	/**
	 * 添加绘图面板切换监听
	 * 
	 * @param l
	 */
	public void addBoardSwitchListener(BoardSwitchListener l) {
		switchListeners.add(l);
	}

	private void fireBoardSwitch() {
		PaintBoard p = getCurrentPaintBoard();
		for (BoardSwitchListener l : switchListeners) {
			try {
				l.boardSwitched(p);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 添加面板打开事件
	 * 
	 * @param l
	 */
	public void addBoardOpenListener(BoardOpenListener l) {
		openListeners.add(l);
	}

	private void fireBoardOpen(PaintBoardBasic paintBoard) {
		for (BoardOpenListener l : openListeners) {
			l.boardOpen(paintBoard);
		}
	}

	/**
	 * 关闭所有绘图区
	 */
	public void closeAllTabs() {
		int compCount = operationTab.getTabCount();
		boolean[] flags = new boolean[compCount];
		boolean cancel = false;
		for (int i = compCount - 1; i >= 0; i--) {
			PaintBoard board = ((PaintBoardScroll) operationTab
					.getComponentAt(i)).getBoard();
			flags[i] = board.isReadyToCloseWithCancel();
			if (!flags[i]) {
				cancel = true;
				break;
			}
		}
		if (!cancel) {
			for (int i = compCount - 1; i >= 0; i--) {
				PaintBoard board = ((PaintBoardScroll) operationTab
						.getComponentAt(i)).getBoard();
				operationTab.remove(operationTab.getComponentAt(i));
				boards.remove(board.getProcessBean().getID());
			}
			editor.getStatusBar().showGlassInfo("关闭了所有绘图区");
		}
	}

	/**
	 * 关闭所有绘图区,不允许取消，只能选择是否保存，最终全部关闭
	 */
	public void closeAllTabsWithoutCancel() {
		int compCount = operationTab.getTabCount();
		boolean[] flags = new boolean[compCount];
		boolean cancel = false;
		for (int i = compCount - 1; i >= 0; i--) {
			PaintBoard board = ((PaintBoardScroll) operationTab
					.getComponentAt(i)).getBoard();
			flags[i] = board.isReadyToCloseWithoutCancel();
			if (!flags[i]) {
				cancel = true;
				break;
			}
		}
		if (!cancel) {
			for (int i = compCount - 1; i >= 0; i--) {
				PaintBoard board = ((PaintBoardScroll) operationTab
						.getComponentAt(i)).getBoard();
				operationTab.remove(operationTab.getComponentAt(i));
				boards.remove(board.getProcessBean().getID());
			}
			editor.getStatusBar().showGlassInfo("关闭了所有绘图区");
		}
	}

	/**
	 * 在不进行任何提示的情况下，关闭所有绘图区
	 */
	public void closeAllTabsWithoutSave() {
		int compCount = operationTab.getTabCount();
		for (int i = compCount - 1; i >= 0; i--) {
			PaintBoard board = ((PaintBoardScroll) operationTab
					.getComponentAt(i)).getBoard();
			operationTab.remove(operationTab.getComponentAt(i));
			boards.remove(board.getProcessBean().getID());
		}
	}

	/**
	 * tab标签上的弹出菜单对应的事件
	 * 
	 * @author Qil.Wong
	 * 
	 */
	private class CloseTabMenuItemActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand() == CloseTabPopup.CLOSE_CURRENT_COMMAND) {
				closeCurrentTab();
			} else if (e.getActionCommand() == CloseTabPopup.CLOSE_OTHERS_COMMAND) {
				closeOtherTabs();
			} else if (e.getActionCommand() == CloseTabPopup.CLOSE_ALL_COMMAND) {
				closeAllTabs();
			}
			fireBoardSwitch();
		}

	}

	/**
	 * tab标签弹出菜单类，共有三个菜单
	 * 
	 * @author Qil.Wong
	 * 
	 */
	private class CloseTabPopup extends JidePopupMenu {
		private static final long serialVersionUID = 6205636512483936485L;

		private static final String CLOSE_CURRENT_COMMAND = "关闭当前面板";
		private static final String CLOSE_OTHERS_COMMAND = "关闭其它面板";
		private static final String CLOSE_ALL_COMMAND = "关闭所有面板";

		JMenuItem closeCurrentItem = new JMenuItem(CLOSE_CURRENT_COMMAND);
		JMenuItem closeOtherItem = new JMenuItem(CLOSE_OTHERS_COMMAND);
		JMenuItem closeAllItem = new JMenuItem(CLOSE_ALL_COMMAND);

		private CloseTabPopup() {
			super();
			add(closeCurrentItem);
			add(closeOtherItem);
			add(closeAllItem);
			CloseTabMenuItemActionListener closeAction = new CloseTabMenuItemActionListener();
			closeCurrentItem.addActionListener(closeAction);
			closeOtherItem.addActionListener(closeAction);
			closeAllItem.addActionListener(closeAction);
		}

		private JMenuItem getCloseOthersMenuItem() {
			return closeOtherItem;
		}
	}

	private class TabCloseAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			PaintBoard paintBoard = ((PaintBoardScroll) e.getSource())
					.getBoard();
			if (paintBoard.isReadyToCloseWithCancel()) {
				editor.getStatusBar().showGlassInfo(
						"关闭了\"" + paintBoard.getName() + "\"绘图区");
				operationTab.remove(paintBoard.getPaintBoardScroll());
				boards.remove(paintBoard.getProcessBean().getID());
			}
			fireBoardSwitch();
		}
	}

	private class GradientTabPane extends JideTabbedPane {

		private static final long serialVersionUID = 1524093729017006316L;

		private Color formColor = new Color(235, 235, 235);

		private Color toColor = new Color(195, 195, 195);

		public void paintComponent(Graphics g) {
			Rectangle rect = new Rectangle(0, 0, getWidth(), getHeight());
			JideSwingUtilities.fillGradient((Graphics2D) g, rect, formColor,
					toColor, false);
		}

		public void setSelectedIndex(int index) {
			super.setSelectedIndex(index);
			// 面板切换了，触发事件
			fireBoardSwitch();
		}

		public void processMouseSelection(int tabIndex, MouseEvent e) {

		}
	}

	/**
	 * 获取当前工作所在的包
	 * 
	 * @return
	 */
	public WofoPackageBean getCurrentPackage() {
		DefaultMutableTreeNode selectNode = wfTree.getSelectedNode();
		TreeNode[] pathNodes = selectNode.getPath();
		for (int i = pathNodes.length - 1; i >= 0; i--) {
			Object o = ((DefaultMutableTreeNode) pathNodes[i]).getUserObject();
			if (o instanceof WofoPackageBean) {
				return (WofoPackageBean) o;

			}
		}
		return null;
	}

	/**
	 * 获取当前所在的流程,如果有打开的，则调用打开流程PaintBoard的processBean副本；如果没有打开的，
	 * 则直接调用节点的userobject
	 * 
	 * @return
	 */
	public WofoProcessBean getCurrentProcess() {
		DefaultMutableTreeNode selectNode = wfTree.getSelectedNode();
		if (selectNode != null
				&& selectNode.getUserObject() instanceof WofoProcessBean) {
			WofoProcessBean process = (WofoProcessBean) selectNode
					.getUserObject();
			if (boards.get(process.getID()) == null) {
				// 没有打开，返回userobject
				return process;
			} else {
				// 已经打开，返回副本
				return boards.get(process.getID()).getProcessBean();
			}
		}
		return null;
	}

	/**
	 * 刷新重载
	 * 
	 * @param model
	 */
	public void load(DefaultTreeModel model) {
		int result = JOptionPane.showConfirmDialog(editor,
				"加载当前流程树外的数据将强制关闭已打开的绘图工作区，并丢失所有未保存流程的修改数据！\n确定加载？", "警告",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			closeAllTabsWithoutSave();
			wfTree.setWfTreeModel(model);
			wfTree.initAutoCompleteFilter();
			updateUI();
		}
	}

	/**
	 * 获取流程树
	 * 
	 * @return
	 */
	public WfTree getWfTree() {
		return wfTree;
	}

	/**
	 * 获取图形面板集合的tab，该tab上放置所有图形面板
	 * 
	 * @return
	 */
	public JideTabbedPane getOperationTab() {
		return operationTab;
	}

	/**
	 * 获取关闭面板的弹出菜单
	 * 
	 * @return
	 */
	public CloseTabPopup getCloseTabPopup() {
		return closeTabPopup;
	}

	/**
	 * 获取所有打开的面板
	 * 
	 * @return
	 */
	public Map<String, PaintBoard> getBoards() {
		return boards;
	}

	/**
	 * 获取活动集合区域，该区域显示当前指定的流程下的所有活动
	 * 
	 * @return
	 */
	public WfOperationSimpleSetArea getActivityArea() {
		return activityArea;
	}

	/**
	 * 获取条件集合区域，该区域显示当前指定流程下的所有条件
	 * 
	 * @return
	 */
	public WfOperationSimpleSetArea getConditionArea() {
		return conditionArea;
	}

	/**
	 * 获取自动补齐的Field
	 * 
	 * @return
	 */
	public AutoCompletionField getFilterField() {
		return filterField;
	}
}
