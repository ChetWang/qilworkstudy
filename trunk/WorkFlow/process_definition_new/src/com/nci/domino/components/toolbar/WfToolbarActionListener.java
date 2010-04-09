package com.nci.domino.components.toolbar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.nci.domino.PaintBoard;
import com.nci.domino.beans.WofoActions;
import com.nci.domino.beans.WofoNetBean;
import com.nci.domino.beans.desyer.WofoProcessBean;
import com.nci.domino.components.dialog.WfDialog;
import com.nci.domino.components.dialog.importexport.LocalExportChooserDialog;
import com.nci.domino.components.dialog.importexport.LocalImportChooserDialog;
import com.nci.domino.components.dialog.importexport.PngExportChooserDialog;
import com.nci.domino.concurrent.WfSwingWorker;
import com.nci.domino.edit.ToolMode;
import com.nci.domino.help.Functions;
import com.nci.domino.help.WofoResources;
import com.nci.domino.importexport.WofoImportExport;

/**
 * 工具条的响应事件
 * 
 * @author Qil.Wong
 * 
 */
public class WfToolbarActionListener implements ActionListener {

	/**
	 * 编辑器工具条
	 */
	protected WfToolBar toolbar;

	/**
	 * 图形对齐管理器
	 */
	private WfAlignManager align;

	BusiDialog busiDialog;

	public WfToolbarActionListener(WfToolBar toolbar) {
		this.toolbar = toolbar;
		align = new WfAlignManager(toolbar.getEditor());
		busiDialog = new BusiDialog();
	}

	public void actionPerformed(ActionEvent e) {
		// 命令通过actionCommand来传递
		String action = e.getActionCommand();
		toolbar.getEditor().getStatusBar().showGlassInfo(
				WofoResources.getValueByKey("choose")
						+ WofoResources.getValueByKey(action));
		AbstractButton btn = (AbstractButton) e.getSource();
		if (action.equals("save") || action.equals("save_all")) {
			saveAll();
		} else if (action.equals("save_current")) {
			saveCurrent();
		} else if (action.equals("print")) {
			print();
		} else if (action.equals("import_edit")) {
			importFromXML();
		} else if (action.equals("export_edit")) {
			exportXml();
		} else if (action.equals("image_export")) {
			exportImage();
		} else if (action.equals("cut")) {

		} else if (action.equals("copy")) {
			copy();
		} else if (action.equals("paste")) {
			paste();
		} else if (action.equals("delete")) {
			delete();
		} else if (action.equals("undo")) {
			undo();
		} else if (action.equals("redo")) {
			redo();
		} else if (action.equals("align")) {

		} else if (action.equals("align_top")) {
			align.alignTop();
		} else if (action.equals("align_bottom")) {
			align.alignBottom();
		} else if (action.equals("align_left")) {
			align.alignLeft();
		} else if (action.equals("align_right")) {
			align.alignRight();
		} else if (action.equals("align_vertical")) {

		} else if (action.equals("align_horizontal")) {

		} else if (action.equals("restore_ui")) {
			restore();
		} else if (action.equals("fit_screen")) {
			fit();
		} else if (action.startsWith("workflow_activity")) {
			addNewActivity(action);
		} else if (action.equals("new_process_set")) {

		} else if (action.equals("add_business_item")) {
			createBusinessActivity();
		} else if (action.equals("new_transition")) {
			toolbar.getEditor().getModeManager().setToolMode(
					ToolMode.TOOL_LINKLINE);
		} else if (action.equals("new_note")) {
			toolbar.getEditor().getModeManager().setToolMode(
					ToolMode.Tool_DRAW_NOTES);
		} else if (action.equals("move_all")) {
			toolbar.getEditor().getModeManager().setToolMode(
					ToolMode.TOOL_TRANSLATE);
		} else if (action.equals("zoomIn")) {// 放大
			toolbar.getEditor().getOperationArea().getCurrentPaintBoard()
					.zoomSteady(1.2);
		} else if (action.equals("zoomOut")) {
			toolbar.getEditor().getOperationArea().getCurrentPaintBoard()
					.zoomSteady(1.0 / 1.2);
		} else if (action.equals("regular")) {// 正常操作模式
			toolbar.getEditor().getModeManager().setToolMode(
					ToolMode.TOOL_SELECT_OR_DRAG);
		} else if (action.equals("remanent_oper")) {// 重复操作模式
			toolbar.getEditor().getModeManager().setRemanentOperation(
					btn.isSelected());
		}
	}

	/**
	 * 调整到适合尺寸
	 */
	private void fit() {
		PaintBoard board = toolbar.getEditor().getOperationArea()
				.getCurrentPaintBoard();
		if (board != null) {
			board.adjustBounds();
		}
	}

	/**
	 * 复原
	 */
	private void restore() {
		PaintBoard board = toolbar.getEditor().getOperationArea()
				.getCurrentPaintBoard();
		if (board != null) {
			board.toOriginLoaction();
		}
	}

	/**
	 * 导入文件，或打开文件
	 */
	private void importFromXML() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				LocalImportChooserDialog dialog = (LocalImportChooserDialog) toolbar
						.getEditor().getDialogManager().getDialog(
								LocalImportChooserDialog.class, "打开本地流程", true);
				dialog.getFileChooser().getFilterSelector().setSelectedIndex(1);
				dialog.showWfDialog(null);
				if (dialog.getDialogResult() == WfDialog.RESULT_AFFIRMED) {
					String storedFile = (String) dialog.getInputValues();
					((WofoImportExport) dialog.getCurrentFilter())
							.importFromLocal(storedFile);
				}
			}
		});
	}

	/**
	 * 导出文件
	 */
	private void exportXml() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				LocalExportChooserDialog dialog = (LocalExportChooserDialog) toolbar
						.getEditor().getDialogManager().getDialog(
								LocalExportChooserDialog.class, "保存完整流程", true);
				dialog.getFileChooser().getFilterSelector().setSelectedIndex(1);
				dialog.showWfDialog(null);
				if (dialog.getDialogResult() == WfDialog.RESULT_AFFIRMED) {
					String storedFile = (String) dialog.getInputValues();
					dialog.getCurrentFilter().export(storedFile);
				}
			}
		});
	}

	/**
	 * 回退
	 */
	private void undo() {
		PaintBoard board = toolbar.getEditor().getOperationArea()
				.getCurrentPaintBoard();
		board.undo();
	}

	/**
	 * 重做
	 */
	private void redo() {
		PaintBoard board = toolbar.getEditor().getOperationArea()
				.getCurrentPaintBoard();
		board.redo();
	}

	/**
	 * 复制
	 */
	private void copy() {
		PaintBoard board = toolbar.getEditor().getOperationArea()
				.getCurrentPaintBoard();
		if (board != null) {
			toolbar.getEditor().getCopyPaste().copyShapes(
					board.getSelectedShapes());
		}
	}

	/**
	 * 粘贴
	 */
	private void paste() {
		PaintBoard board = toolbar.getEditor().getOperationArea()
				.getCurrentPaintBoard();
		if (board != null) {
			toolbar.getEditor().getCopyPaste().pasteShapes(board);
		}
	}

	/**
	 * 删除
	 */
	private void delete() {
		PaintBoard board = toolbar.getEditor().getOperationArea()
				.getCurrentPaintBoard();
		if (board != null) {
			board.removeSelectShapes();
		}
	}

	/**
	 * 保存当前的流程
	 */
	private void saveCurrent() {
		PaintBoard currentBoard = toolbar.getEditor().getOperationArea()
				.getCurrentPaintBoard();
		if (currentBoard != null)
			currentBoard.preSave();
		final WofoProcessBean currentProcss = toolbar.getEditor()
				.getOperationArea().getCurrentProcess();
		if (currentProcss == null) {
			JOptionPane.showConfirmDialog(toolbar.getEditor(), "必须选上一个流程进行保存",
					"结果", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
			return;
		}
		toolbar.getEditor().setEnabled(false);
		WfSwingWorker<WofoNetBean, String> worker = new WfSwingWorker<WofoNetBean, String>(
				"正在将当前流程保存至服务器") {

			@Override
			protected WofoNetBean doInBackground() throws Exception {
				WofoNetBean netBean = new WofoNetBean();
				netBean.setActionName(WofoActions.SAVE_CURRENT);
				netBean.setParam(new Serializable[] {
						toolbar.getEditor().getOperationArea().getWfTree()
								.getOriginalProcess(currentProcss),
						currentProcss });
				netBean.setUser(toolbar.getEditor().getUserID());
				netBean = (WofoNetBean) Functions.getReturnObj(toolbar
						.getEditor().getServletPath(), netBean);
				return netBean;
			}

			public void wfDone() {
				try {
					WofoNetBean result = get();
					String resultInfo = "";
					if (result != null && result.getParam() != null
							&& result.getParam().toString().trim().equals("OK")) {
						resultInfo = "保存成功！";
						toolbar.getEditor().getStatusBar().showGlassInfo(
								resultInfo);

						/*************** 保存成功后，初始节点要被重新赋值过，新节点的next versioin标签也就重新置位 *************************/
						DefaultMutableTreeNode processNode = (DefaultMutableTreeNode) toolbar
								.getEditor().getOperationArea().getWfTree()
								.getSelectedNode();
						WofoProcessBean process = toolbar.getEditor()
								.getOperationArea().getCurrentProcess();
						process.setNextVersion(false);
						processNode.setUserObject(Functions.deepClone(process));

						// 副本节点
						DefaultMutableTreeNode originProcessNode = toolbar
								.getEditor().getOperationArea().getWfTree()
								.getOriginalProcessNode(process);
						originProcessNode.setUserObject(Functions
								.deepClone(process));
					} else {
						resultInfo = "保存失败！\n" + result.getParam().toString();
						toolbar.getEditor().getStatusBar().showGlassInfo(
								resultInfo);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				} finally {
					toolbar.getEditor().setEnabled(true);
					busiDialog.dispose();
				}
			}
		};
		toolbar.getEditor().getBackgroundManager().enqueueOpertimeQueue(worker);
		busiDialog.shown();
	}

	/**
	 * 保存所有流程
	 */
	private void saveAll() {
		toolbar.getEditor().setEnabled(false);
		WfSwingWorker<WofoNetBean, String> worker = new WfSwingWorker<WofoNetBean, String>(
				"正在将所有流程保存至服务器") {

			@Override
			protected WofoNetBean doInBackground() throws Exception {
				Map<String, PaintBoard> boardMap = toolbar.getEditor()
						.getOperationArea().getBoards();
				Iterator<PaintBoard> boards = boardMap.values().iterator();
				while (boards.hasNext()) {
					boards.next().preSave(true);
				}
				DefaultTreeModel model = toolbar.getEditor().getOperationArea()
						.getWfTree().getWfTreeModel();
				WofoNetBean netBean = new WofoNetBean();
				netBean.setActionName(WofoActions.SAVE_ALL);
				netBean.setParam(new Serializable[] {
						toolbar.getEditor().getOperationArea().getWfTree()
								.getOriginalNode(),
						(Serializable) model.getRoot() });
				netBean.setUser(toolbar.getEditor().getUserID());
				netBean = (WofoNetBean) Functions.getReturnObj(toolbar
						.getEditor().getServletPath(), netBean);
				return netBean;
			}

			public void wfDone() {
				try {
					WofoNetBean result = get();
					String resultInfo = "";
					if (result != null && result.getParam() != null
							&& result.getParam().toString().trim().equals("OK")) {
						resultInfo = "保存成功！";
						toolbar.getEditor().getStatusBar().showGlassInfo(
								resultInfo);
						// JOptionPane.showConfirmDialog(toolbar.getEditor(),
						// resultInfo, "结果", JOptionPane.CLOSED_OPTION,
						// JOptionPane.INFORMATION_MESSAGE);

						/*************** 保存成功后，初始节点要被重新赋值过，新节点的next versioin标签也就重新置位 *************************/
						DefaultTreeModel model = toolbar.getEditor()
								.getOperationArea().getWfTree()
								.getWfTreeModel();
						DefaultMutableTreeNode root = (DefaultMutableTreeNode) model
								.getRoot();
						convertNextVersionNodeAfterSave(root);
						toolbar.getEditor().getOperationArea().getWfTree()
								.setOriginalNode(
										(DefaultMutableTreeNode) Functions
												.deepClone(root));
					} else {
						resultInfo = "保存失败！\n" + result.getParam().toString();
						toolbar.getEditor().getStatusBar().showGlassInfo(
								resultInfo);

					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				} finally {
					toolbar.getEditor().setEnabled(true);
					busiDialog.dispose();
				}
			}
		};
		toolbar.getEditor().getBackgroundManager().enqueueOpertimeQueue(worker);
		busiDialog.shown();
	}

	/**
	 * 将节点从保存成功后的新版本节点重新赋值为非新版本
	 * 
	 * @param node
	 */
	private void convertNextVersionNodeAfterSave(DefaultMutableTreeNode node) {
		if (node.getUserObject() instanceof WofoProcessBean) {
			((WofoProcessBean) node.getUserObject()).setNextVersion(false);
		} else {
			for (int i = 0; i < node.getChildCount(); i++) {
				convertNextVersionNodeAfterSave((DefaultMutableTreeNode) node
						.getChildAt(i));
			}
		}
	}

	/**
	 * 创建流程业务项
	 */
	private void createBusinessActivity() {
		addNewActivity("workflow_activity_human");
	}

	/**
	 * 打印工作流图像
	 */
	private void print() {
		PaintBoard currentBoard = toolbar.getEditor().getOperationArea()
				.getCurrentPaintBoard();

		PrinterJob printJob = PrinterJob.getPrinterJob();
		PageFormat pageFormat = printJob.defaultPage();
		printJob.setPrintable(currentBoard, pageFormat);
		if (printJob.printDialog()) {
			try {
				printJob.print();
			} catch (PrinterException exception) {
				toolbar.getEditor().getStatusBar().showGlassInfo("打印失败");
			}
		}
	}

	/**
	 * 导出图片
	 */
	private void exportImage() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				PngExportChooserDialog dialog = (PngExportChooserDialog) toolbar
						.getEditor().getDialogManager().getDialog(
								PngExportChooserDialog.class, "导出图像", true);
				dialog.showWfDialog(null);
				if (dialog.getDialogResult() == WfDialog.RESULT_AFFIRMED) {
					String storedFile = (String) dialog.getInputValues();
					dialog.getCurrentFilter().export(storedFile);
				}
			}
		});
	}

	private void addNewActivity(String command) {
		PaintBoard current = toolbar.getEditor().getOperationArea()
				.getCurrentPaintBoard();
		if (current == null) {
			Logger.getLogger(this.getClass().getName()).log(Level.INFO,
					"当前绘图面板为空", "");
		} else {
			String type = command.substring("workflow_activity_".length());
			toolbar.getEditor().getModeManager().setActivityType(type);
			toolbar.getEditor().getModeManager().setToolMode(
					ToolMode.TOOL_DRAW_ACTIVITY);

		}
	}

	/**
	 * 
	 * @author Qil.Wong
	 * 
	 */
	private class BusiDialog extends JDialog {

		private JLabel infoLabel;

		public BusiDialog() {
			super(toolbar.getEditor().findParentFrame(), true);
			setUndecorated(true);
//			setLocationByPlatform(true);
			init();
			setInfo("正在保存......");
			setSize(200, 68);
		}

		private void init() {
			infoLabel = new JLabel();
			infoLabel.setHorizontalAlignment(JLabel.CENTER);
			JPanel root = new JPanel(new BorderLayout());
			getContentPane().setLayout(new BorderLayout());
			getContentPane().add(root, BorderLayout.CENTER);
			root.setBorder(BorderFactory.createLineBorder(Color.black, 1));
			root.add(infoLabel, BorderLayout.CENTER);
		}

		public void setInfo(String info) {
			infoLabel.setText(info);
		}

		public void shown() {
			setLocationRelativeTo(toolbar.getEditor());
			this.setVisible(true);
		}

	}

}
