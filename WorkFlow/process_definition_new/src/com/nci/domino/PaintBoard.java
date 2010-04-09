package com.nci.domino;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.UndoManager;

import com.nci.domino.beans.WofoBaseBean;
import com.nci.domino.beans.desyer.WofoActivityBean;
import com.nci.domino.beans.desyer.WofoConditionBean;
import com.nci.domino.beans.desyer.WofoConditionMemberBean;
import com.nci.domino.beans.desyer.WofoProcessBean;
import com.nci.domino.components.WfConditionMenuItem;
import com.nci.domino.components.dialog.WfDialog;
import com.nci.domino.edit.ActionGroup;
import com.nci.domino.edit.ToolMode;
import com.nci.domino.help.Functions;
import com.nci.domino.help.WfMath;
import com.nci.domino.shape.WfActivity;
import com.nci.domino.shape.WfTransition;
import com.nci.domino.shape.basic.AbstractShape;
import com.nci.domino.shape.basic.BasicShape;
import com.nci.domino.shape.basic.PaintBoardShapeSelectionListener;
import com.nci.domino.shape.basic.SimpleLocationShape;
import com.nci.domino.shape.basic.WfNoteShapeBasic;
import com.nci.domino.shape.note.WfNoteShape;
import com.nci.domino.shape.pipe.WfPipeShape;
import com.nci.domino.utils.CopyableUtils;

/**
 * @author 陈新中 主绘图区
 * @since 4.0 modified Qil.Wong
 */
public class PaintBoard extends PaintBoardBasic implements Printable {

	protected static final long serialVersionUID = -5641334723748403706L;
	/**
	 * 指向父窗口的指针
	 */
	protected WfEditor editor = null;

	// undo redo 管理器对象
	protected UndoManager undomgr = null;

	// 当前绘图区所在的节点
	protected DefaultMutableTreeNode flowNode;

	// 前一次的mode
	protected int prevMode;

	// 判断中心点是否变化
	boolean centerPointChanged = false;

	public PaintBoard() {
		super();
	}

	/**
	 * 构造器
	 */
	public PaintBoard(WfEditor editor, DefaultMutableTreeNode flowNode) {
		super();
		this.editor = editor;
		this.flowNode = flowNode;
		initialize();
	}

	/**
	 * 初始化主panel
	 */
	protected void initialize() {
		undomgr = new UndoManager();
		setAutoscrolls(true);
		this.addMouseListener(new MouseAdapter() {

			public void mouseReleased(MouseEvent e) {
				pressedMouseButton = -1;
				if (e.getClickCount() == 2 || e.getButton() == 2) {
					return;
				}
				pxNew = e.getX();
				pyNew = e.getY();
				double[] ret = WfMath.getWorldCoord(pxNew, pyNew, trans);
				wxNew = ret[0];
				wyNew = ret[1];
				int toolMode = getToolMode();
				if (draggingShape != null) {
					for (AbstractShape s : selectedShapes) {
						s.mouseReleased(e, PaintBoard.this);
					}
				}
				if (e.getButton() == 3 && toolMode == ToolMode.TOOL_LINKLINE) {
					if (draggingShape instanceof WfActivity) {
						drawTransition();
					} else if (draggingShape instanceof WfNoteShapeBasic) {
						drawNoteLinkLine();
					}
					editor.getModeManager().setToolMode(prevMode);
				} else {
					switch (toolMode) {
					case ToolMode.TOOL_SELECT_OR_DRAG:
						releaseMouseToSetShape(e);
						break;
					case ToolMode.TOOL_LINKLINE:
						drawTransition();
						break;
					case ToolMode.TOOL_DRAW_ACTIVITY:
						drawActivity(wxNew, wyNew);
						break;
					case ToolMode.TOOL_TRANSLATE:
						trans[1] = txOld + (pxNew - pxOld) / trans[0];
						trans[2] = tyOld + (pyNew - pyOld) / trans[0];
						break;
					case ToolMode.Tool_DRAW_PIPE:
						drawPipe();
						break;
					case ToolMode.Tool_DRAW_NOTES:
						drawNote();
						break;
					default:
						Logger.getLogger(PaintBoard.class.getName()).log(
								Level.INFO, "ToolMode:" + toolMode + ",还未实现");
						break;
					}
				}
				dragging = false;
				isCommanding = false;
				if (!editor.getModeManager().isRemanentOperation()) {
					editor.getModeManager().setToolMode(
							ToolMode.TOOL_SELECT_OR_DRAG);
					editor.getToolBar().getButtons().get("regular").doClick();
				}
				computePreferedSize();
				repaint();
			}

			/*
			 * 处理鼠标落下事件
			 */
			public void mousePressed(MouseEvent e) {
				PaintBoard.this.requestFocus();
				pressedMouseButton = e.getButton();
				if (e.getClickCount() == 2) {
					return;
				}
				pxOld = e.getX();
				pyOld = e.getY();
				double[] ret = WfMath.getWorldCoord(pxOld, pyOld, trans);
				wxOld = ret[0];
				wyOld = ret[1];
				int toolMode = editor.getModeManager().getToolMode();
				switch (toolMode) {
				case ToolMode.TOOL_SELECT_OR_DRAG: {
					draggingShape = findShapeLoaction(wxOld, wyOld);
					if (draggingShape != null) {
						draggingShape.mousePressed(e, PaintBoard.this);
					}
				}
					break;
				case ToolMode.TOOL_LINKLINE: {
					draggingShape = findActLoaction(wxOld, wyOld);
				}
					break;
				case ToolMode.TOOL_TRANSLATE: {
					txOld = trans[1];
					tyOld = trans[2];
				}
					break;
				default:
					Logger.getLogger(PaintBoard.class.getName()).log(
							Level.INFO, "ToolMode:" + toolMode + ",还未实现");
					break;
				}
				isCommanding = true;
			}

			/*
			 * 处理鼠标点击事件
			 */
			public void mouseClicked(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				double[] wxy = WfMath.getWorldCoord((double) x, (double) y,
						trans);
				AbstractShape sh = findShapeLoaction(wxy[0], wxy[1]);
				if (e.getClickCount() == 2) {
					if (sh != null) {
						sh.mouseDoubleClicked(e, PaintBoard.this);
						repaint();
					}
				} else if (e.getClickCount() == 1) {
					if (sh != null) {
						sh.mouseClicked(e, PaintBoard.this);
						repaint();
					}
				}
			}
		});
		this.addMouseMotionListener(new MouseMotionAdapter() {

			// 处理拖动事件
			public void mouseDragged(MouseEvent e) {
				if (pressedMouseButton == MouseEvent.BUTTON1) {
					leftDrag(e);
				} else if (pressedMouseButton == MouseEvent.BUTTON3) {
					rightDrag(e);
				}
				setEdited(true);
			}
		});
		regestorKeyBoardAction();
		addPaintBoardShapeSelectionListener(new PaintBoardShapeSelectionListener() {
			public void shapeSelectionStateChanged(
					List<AbstractShape> currentSelectedShapes,
					List<AbstractShape> changedShapes) {
				shapesSelected(currentSelectedShapes);
			}
		});
	}

	protected void releaseMouseToSetShape(MouseEvent e) {

		if (Math.abs(pxNew - pxOld) < WfMath.edfbl
				|| Math.abs(pyNew - pyOld) < WfMath.edfbl) {
			super.releaseMouseToSetShape(e);
			draggingShape = null;
		} else {
			if (draggingShape == null) {
				selectObjsFromRect(pxOld, pyOld, pxNew, pyNew);
			} else {
				if (draggingShape != null) {
					draggingShape = null;
				}
				if (dragging) {
					undomgr.addEdit(new ActionGroup(ghostDraggedShapes,
							selectedShapes, PaintBoard.this));
				}
			}
		}

	}

	/**
	 * 注册键盘事件
	 */
	protected void regestorKeyBoardAction() {
		// del删除
		this.registerKeyboardAction(new AbstractAction("delete_shape") {
			public void actionPerformed(ActionEvent e) {
				removeSelectShapes();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0),
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		// ctrl+z撤销
		this.registerKeyboardAction(new AbstractAction("undo") {
			public void actionPerformed(ActionEvent e) {
				undo();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK),
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		// ctrl+y回退
		this.registerKeyboardAction(new AbstractAction("redo") {
			public void actionPerformed(ActionEvent e) {
				redo();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK),
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		// ctrl+c复制
		this.registerKeyboardAction(new AbstractAction("copy") {
			public void actionPerformed(ActionEvent e) {
				copy();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK),
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		// ctrl+v粘贴
		this.registerKeyboardAction(new AbstractAction("paste") {
			public void actionPerformed(ActionEvent e) {
				paste();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK),
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

		// ctrl+a全选
		this.registerKeyboardAction(new AbstractAction("select_all") {
			public void actionPerformed(ActionEvent e) {
				selectAll();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK),
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		// ctrl+s保存当前
		this.registerKeyboardAction(new AbstractAction("save_current") {
			public void actionPerformed(ActionEvent e) {
				preSave();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK),
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	}

	/**
	 * 复制图形
	 */
	protected void copy() {
		editor.getCopyPaste().copyShapes(selectedShapes);
	}

	/**
	 * 粘贴图形
	 */
	protected void paste() {
		editor.getCopyPaste().pasteShapes(PaintBoard.this);
	}

	/**
	 * 设置编辑状态
	 * 
	 * @param edited
	 */
	public void setEdited(boolean edited) {
		if (getToolMode() != ToolMode.TOOL_VIEW) {
			int index = editor.getOperationArea().getOperationTab()
					.indexOfComponent(paintBoardScroll);
			if (!isEdited() && edited) {
				editor.getOperationArea().getOperationTab().setTitleAt(index,
						"*" + processBean.getProcessName());
			} else if (!edited) {
				editor.getOperationArea().getOperationTab().setTitleAt(index,
						processBean.getProcessName());
			}
			this.edited = edited;
		}
	}

	public WfEditor getEditor() {
		return editor;
	}

	/**
	 * 活动图形选择动作，如果多余两个，则对齐菜单可用
	 */
	public void shapesSelected(List<AbstractShape> selectedShapes) {
		int actCount = 0;
		for (AbstractShape s : selectedShapes) {
			if (s instanceof SimpleLocationShape) {
				++actCount;
				if (actCount > 1)
					break;
			}
		}
		editor.getToolBar().setAlignButtonsEnabled(actCount > 1);
	}

	/**
	 * 清除所有图形
	 */
	public void clearAllShapes() {
		List<AbstractShape> s = new ArrayList<AbstractShape>();
		for (int i = 0; i < graphVector.size(); i++) {
			s.add(graphVector.get(i));
		}
		graphVector.clear();
		undomgr.addEdit(new ActionGroup(s, ActionGroup.DELETE, this));
		toOriginLoaction();
	}

	/**
	 * 矩形框选择 把一个框包含的所有图形对象都选中 坐标是屏幕坐标
	 * 
	 * @param x1
	 *            (x1,y1)是一个矩形的端点
	 * @param y1
	 * @param x2
	 *            (x2,y2)是第一个端点的对角点
	 * @param y2
	 */
	protected void selectObjsFromRect(double x1, double y1, double x2, double y2) {
		double[] p1 = WfMath.getWorldCoord(x1, y1, trans);
		double[] p2 = WfMath.getWorldCoord(x2, y1, trans);
		double[] p3 = WfMath.getWorldCoord(x2, y2, trans);
		double[] p4 = WfMath.getWorldCoord(x1, y2, trans);
		setAllUnselected();
		for (int i = 0; i < graphVector.size(); i++) {
			AbstractShape sh = graphVector.get(i);
			if (!sh.isSelected() && sh.isInRect(p1, p2, p3, p4)) {
				addSelectdShape(sh);
			}
		}
		repaint();
	}

	/**
	 * 是否被编辑过
	 * 
	 * @return
	 */
	public boolean isEdited() {
		return edited;
	}

	/**
	 * 鼠标右键拖动
	 * 
	 * @param e
	 */
	protected void rightDrag(MouseEvent e) {
		pxNew = e.getX();
		pyNew = e.getY();
		double[] ret = WfMath.getWorldCoord(pxNew, pyNew, trans);
		wxNew = ret[0];
		wyNew = ret[1];
		if (!dragging) {
			prevMode = editor.getModeManager().getToolMode();
			editor.getModeManager().setToolMode(ToolMode.TOOL_LINKLINE);
			dragging = true;
		}
		repaint();
	}

	/**
	 * 鼠标左键拖动
	 * 
	 * @param e
	 */
	protected void leftDrag(MouseEvent e) {
		pxNew = e.getX();
		pyNew = e.getY();
		double[] ret = WfMath.getWorldCoord(pxNew, pyNew, trans);
		wxNew = ret[0];
		wyNew = ret[1];
		int toolMode = editor.getModeManager().getToolMode();
		switch (toolMode) {
		case ToolMode.TOOL_LINKLINE:
			break;
		case ToolMode.TOOL_SELECT_OR_DRAG:
			if (draggingShape != null) {
				draggingShape.mouseDragged(e, this);
			}
			break;
		case ToolMode.TOOL_TRANSLATE:
			trans[1] = txOld + (pxNew - pxOld) / trans[0];
			trans[2] = tyOld + (pyNew - pyOld) / trans[0];
			break;
		default:
			break;
		}
		scrollRectToVisible(new Rectangle(e.getX(), e.getY(), 38, 38));
		repaint();
	}

	/**
	 * 准备关闭，关闭前如果有未保存的，会提示，也允许用户取消关闭动作
	 * 
	 * @return
	 */
	public boolean isReadyToCloseWithCancel() {
		boolean flag = true;
		if (isEdited()) {
			int result = JOptionPane.showConfirmDialog(this, "流程["
					+ processBean.getProcessName() + "]已修改，是否保存？", "提示",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.INFORMATION_MESSAGE);
			if (result == JOptionPane.YES_OPTION) {
				this.preSave();
				close();
			} else if (result == JOptionPane.CANCEL_OPTION) {
				flag = false;
			}
		}
		return flag;
	}

	/**
	 * 准备关闭，关闭前如果有未保存的，会提示，但不允许用户取消关闭动作，总之最后也要关闭
	 * 
	 * @return
	 */
	public boolean isReadyToCloseWithoutCancel() {
		boolean flag = true;
		if (isEdited()) {
			int result = JOptionPane.showConfirmDialog(this, "流程["
					+ processBean.getProcessName() + "]已修改，是否保存？", "提示",
					JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if (result == JOptionPane.YES_OPTION) {
				this.preSave();
			}
		}
		close();
		return flag;
	}

	protected void close() {
		this.boardShapeSelectionListeners.clear();
		this.boardShapeSelectionListeners = null;
		this.area = null;
		this.draggingShape = null;
		this.flowNode = null;
		this.ghostDraggedShapes.clear();
		this.ghostDraggedShapes = null;
		this.graphVector.clear();
		this.graphVector = null;
		this.processBean = null;
		this.selectedShapes = null;
		this.tranRouter.clear();
		this.tranRouter = null;
		this.underMouseShape = null;
	}

	/**
	 * 全选
	 */
	public void selectAll() {
		setAllUnselected();
		for (AbstractShape shape : graphVector) {
			shape.setSelected(true);
			selectedShapes.add(shape);
		}
		firePaintBoardShapeSelectionListeners(graphVector);
		repaint();
	}

	/**
	 * 画活动
	 * 
	 * @param x
	 *            中心的x坐标
	 * @param y
	 *            中心的y坐标
	 */
	public WfActivity drawActivity(double x, double y) {
		String currentActivityType = editor.getModeManager().getActivityType();
		if ((currentActivityType.equals(WofoActivityBean.ACTIVITY_TYPE_BEGIN))
				&& isExistSameClass(currentActivityType)) {
			JOptionPane.showMessageDialog(editor, "开始活动只能有一个!");
			return null;
		}
		if (currentActivityType == null) {
			JOptionPane.showMessageDialog(editor, "请选择活动类型!");
			return null;
		}
		WfActivity one = new WfActivity(x, y, currentActivityType);
		one.updatePaste(this);
		addGraph(one);
		repaint();
		int result = one.showInput(this);
		if (result != WfDialog.RESULT_AFFIRMED) {
			removeGraph(one);
			repaint();
		} else {
			List<AbstractShape> set = new ArrayList<AbstractShape>();
			set.add(one);
			undomgr.addEdit(new ActionGroup(set, this));
		}
		return one;
	}

	/**
	 * 绘制泳道图
	 */
	protected WfPipeShape drawPipe() {
		boolean vertical = "V".equals(editor.getModeManager().getPipeType());
		int index = 0;
		double formerLength = 0;// 之前所有泳道的长度（垂直泳道对应宽度，水平泳道对应高度）
		for (AbstractShape s : graphVector) {
			if (s instanceof WfPipeShape) {
				if (((WfPipeShape) s).isVertical() == vertical) {
					index++;
					if (vertical) {
						formerLength = formerLength
								+ ((WfPipeShape) s).getWidth();
					} else {
						formerLength = formerLength
								+ ((WfPipeShape) s).getHeight();
					}
				}
			}
		}
		WfPipeShape pipeShape = new WfPipeShape(index, formerLength, vertical);
		addGraph(pipeShape);
		repaint();
		int result = pipeShape.showInput();
		if (result != WfDialog.RESULT_AFFIRMED) {
			removeGraph(pipeShape);
			repaint();
		} else {
			List<AbstractShape> set = new ArrayList<AbstractShape>();
			set.add(pipeShape);
			undomgr.addEdit(new ActionGroup(set, this));
		}
		return pipeShape;
	}

	/**
	 * 绘制备注
	 */
	protected void drawNote() {
		WfNoteShape noteShape = new WfNoteShape();
		noteShape.setX(wxNew);
		noteShape.setY(wyNew);
		addGraph(noteShape);
		repaint();
		int result = noteShape.showInput(this);
		if (result != WfDialog.RESULT_AFFIRMED) {
			removeGraph(noteShape);
			repaint();
		} else {
			List<AbstractShape> set = new ArrayList<AbstractShape>();
			set.add(noteShape);
			undomgr.addEdit(new ActionGroup(set, this));
		}
	}

	/**
	 * 绘制备注信息连接线路
	 */
	protected void drawNoteLinkLine() {
		AbstractShape twoact = findShapeLoaction(wxNew, wyNew);
		WfNoteShapeBasic noteShape = (WfNoteShapeBasic) draggingShape;
		if (twoact != null) {
			noteShape.addNotedShape(twoact);
		}
	}

	/**
	 * 绘制迁移
	 */
	protected void drawTransition() {
		WfActivity twoact = findActLoaction(wxNew, wyNew);
		if (draggingShape != null && draggingShape instanceof WfActivity
				&& twoact != null && draggingShape != twoact) {
			WfActivity oneact = (WfActivity) draggingShape;
			if (isExistsTrans(oneact, twoact)) {
				return;
			}
			if (((WofoActivityBean) oneact.getWofoBean()).getActivityType()
					.equals(WofoActivityBean.ACTIVITY_TYPE_END)) {
				return;
			}
			if (((WofoActivityBean) twoact.getWofoBean()).getActivityType()
					.equals(WofoActivityBean.ACTIVITY_TYPE_BEGIN)) {
				return;
			}
			WfTransition one = new WfTransition(oneact, twoact);
			one.updatePaste(this);
			addGraph(one);
			String splitConditionid = ((WofoActivityBean) oneact.getWofoBean())
					.getSplitConditionId();
			if (splitConditionid != null && !splitConditionid.equals("")) {
				List conditions = editor.getOperationArea().getConditionArea()
						.getValues();
				JPopupMenu popup = new JPopupMenu();
				for (int i = 0; i < conditions.size(); i++) {
					WofoConditionBean c = (WofoConditionBean) conditions.get(i);
					if (c.getConditionId().equals(splitConditionid)) {
						List members = c.getMembers();
						for (int x = 0; x < members.size(); x++) {
							WofoConditionMemberBean memberBean = (WofoConditionMemberBean) members
									.get(x);
							JMenuItem splitItem = new WfConditionMenuItem(
									memberBean, one, this);
							popup.add(splitItem);
						}

					}
				}
				popup.show(this, (int) twoact.getX(), (int) twoact.getY());
			}
			undomgr.addEdit(new ActionGroup(one, this));
			repaint();
			twoact = null;
			draggingShape = null;
		}
	}

	public boolean checkPastedActivity(List<AbstractShape> shapes) {
		for (AbstractShape s : shapes) {
			if (s instanceof WfActivity) {
				WfActivity act = (WfActivity) s;
				String currentActivityType = ((WofoActivityBean) act
						.getWofoBean()).getActivityType();
				if ((currentActivityType
						.equals(WofoActivityBean.ACTIVITY_TYPE_BEGIN) || currentActivityType
						.equals(WofoActivityBean.ACTIVITY_TYPE_END))
						&& isExistSameClass(currentActivityType)) {
					JOptionPane.showMessageDialog(this, "开始活动和结束活动都只能有一个!");
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 是否已经存在相同的活动类型
	 * 
	 * @param activityType
	 * @return
	 */
	protected boolean isExistSameClass(String activityType) {
		for (int i = 0; i < graphVector.size(); i++) {
			AbstractShape oneShape = graphVector.get(i);
			if (oneShape instanceof WfActivity
					&& ((WofoActivityBean) oneShape.getWofoBean())
							.getActivityType().equals(activityType)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 删除图形
	 * 
	 * @param shape
	 */
	public void removeGraph(AbstractShape shape) {
		shape.removeShape(this);
		// graphVector.remove(shape);
	}

	/**
	 * 在这两个活动之间是不是已经存在一条迁移线
	 * 
	 * @param oneact
	 *            第一个活动
	 * @param twoact
	 *            第二个活动
	 * @return true表示有
	 */
	protected boolean isExistsTrans(WfActivity oneact, WfActivity twoact) {
		for (int i = 0; i < graphVector.size(); i++) {
			BasicShape oneshape = graphVector.get(i);
			if (oneshape instanceof WfTransition) {
				WfTransition sinLine = (WfTransition) oneshape;
				if (sinLine.previousActivity == oneact
						&& sinLine.nextActivity == twoact) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 给出四个点 画出一个封闭的四边形
	 * 
	 * @param g2
	 *            Graphics2D实例
	 * @param p1
	 *            第一点
	 * @param p2
	 *            第二点
	 * @param p3
	 *            第三点
	 * @param p4
	 *            第四点
	 */
	protected void drawSlantingRect(Graphics2D g2, double[] p1, double[] p2,
			double[] p3, double[] p4) {
		GeneralPath path = new GeneralPath();
		path.moveTo((float) p1[0], (float) p1[1]);
		path.lineTo((float) p2[0], (float) p2[1]);
		path.lineTo((float) p3[0], (float) p3[1]);
		path.lineTo((float) p4[0], (float) p4[1]);
		path.lineTo((float) p1[0], (float) p1[1]);
		g2.draw(path);
	}

	/**
	 * 画辅助线
	 * 
	 * @param g2
	 *            Graphics2D实例
	 */
	@Override
	protected void drawHelpLine(Graphics2D g2) {
		g2.setColor(helpLineColor);
		g2.setStroke(GlobalConstants.SMALL_LINE_STROKE);
		if (isCommanding) {
			int toolMode = editor.getModeManager().getToolMode();
			switch (toolMode) {
			case ToolMode.TOOL_LINKLINE: {
				WfMath.drawArrow(g2, wxOld, wyOld, wxNew, wyNew);
			}
				break;

			case ToolMode.TOOL_SELECT_OR_DRAG: {
				if (draggingShape == null) {
					drawSlantingRect(g2, WfMath.getWorldCoord(pxOld, pyOld,
							trans), WfMath.getWorldCoord(pxNew, pyOld, trans),
							WfMath.getWorldCoord(pxNew, pyNew, trans), WfMath
									.getWorldCoord(pxOld, pyNew, trans));
				}
			}
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 删除所有选中的图形对象
	 */
	public void removeSelectShapes() {
		List<AbstractShape> s = new ArrayList<AbstractShape>();
		// 对选中的图形进行排序，先删迁移，后删活动
		for (int i = graphVector.size() - 1; i >= 0; i--) {
			AbstractShape sh = graphVector.get(i);
			if (sh.isSelected() && sh instanceof WfActivity == false
					&& sh.isDeletable()) {
				s.add(sh);
			}
		}
		for (int i = graphVector.size() - 1; i >= 0; i--) {
			AbstractShape sh = graphVector.get(i);
			if (sh.isSelected() && sh instanceof WfActivity) {
				ArrayList<AbstractShape> ret = removeActivity((WfActivity) sh);
				for (int j = 0; j < ret.size(); j++) {
					if (!s.contains(ret.get(j))) {
						s.add(ret.get(j));
					}
				}
			}
		}
		for (AbstractShape shape : s) {
			removeGraph(shape);
		}
		if (s.size() > 0) {
			undomgr.addEdit(new ActionGroup(s, ActionGroup.DELETE, this));
			repaint();
		}
		setEdited(true);
	}

	/**
	 * 删除一个活动
	 * 
	 * @param act
	 *            WfActivity实例
	 */
	protected ArrayList<AbstractShape> removeActivity(WfActivity act) {
		ArrayList<AbstractShape> ret = new ArrayList<AbstractShape>();
		if (act == null) {
			return ret;
		}
		String activityID = act.getWofoBean().getID();
		for (int i = graphVector.size() - 1; i >= 0; i--) {
			if (graphVector.get(i) instanceof WfTransition) {
				WfTransition tran = (WfTransition) graphVector.get(i);
				if (tran.previousActivity.getWofoBean().getID().equals(
						activityID)
						|| tran.nextActivity.getWofoBean().getID().equals(
								activityID)) {
					ret.add(tran);
				}
			}
		}
		ret.add(act);
		return ret;
	}

	/**
	 * 检查数据的有效性
	 * 
	 * @return true表示有效
	 */
	public boolean dataIsValid() {
		int startCount = 0;
		int endCount = 0;
		if (processBean == null) {
			JOptionPane.showMessageDialog(this, "开始活动和结束活动都有且只能有一个!");
		}
		for (int i = 0; i < graphVector.size(); i++) {
			if (graphVector.get(i) instanceof WfActivity) {
				WfActivity o = (WfActivity) graphVector.get(i);
				String type = ((WofoActivityBean) o.getWofoBean())
						.getActivityType();
				if (type.equals(WofoActivityBean.ACTIVITY_TYPE_BEGIN)) {
					startCount++;
				}
				if (type.equals(WofoActivityBean.ACTIVITY_TYPE_END)) {
					endCount++;
				}
			}
		}
		if (startCount != 1 || endCount != 1) {
			JOptionPane.showMessageDialog(this, "开始活动和结束活动都有且只能有一个!");
			return false;
		}
		if (processBean == null) {
			JOptionPane.showMessageDialog(this, "在空白处右键菜单设置流程版本属性!");
			return false;
		}
		return true;
	}

	// -----------------------------

	/**
	 * 撤销前一次操作
	 */
	public void undo() {
		if (undomgr.canUndo()) {
			undomgr.undo();
		}
	}

	/**
	 * 重复前一次撤销的操作
	 */
	public void redo() {
		try {
			undomgr.redo();
		} catch (CannotRedoException e) {
			System.out.println("Can't redo");
		}
	}

	@SuppressWarnings("unchecked")
	public void preSave() {
		preSave(false);
	}

	/**
	 * 暂存，将图形上的数据和流程树的数据同步
	 * 
	 * @param toServer
	 *            是否发送到服务器
	 */
	public void preSave(boolean toServer) {
		if (flowNode == null) { // flowNode==null表示是版本的board，无须保存
			return;
		}
		List<WofoBaseBean> shapeBeans = new ArrayList<WofoBaseBean>();
		// 当前在未保存时的流程业务对象
		List<WofoBaseBean> previousShapes = processBean.getGraphs();

		// 缓存的id
		List<String> previousActvityIDList = (List<String>) editor.getCache()
				.nowaitWhileNullGet(
						"PROCESS_ACTIVITY_IDs_" + processBean.getProcessId());
		List<String> currentActvityIDList = new ArrayList<String>();
		if (previousActvityIDList == null) {
			previousActvityIDList = new ArrayList<String>();
			for (int i = 0; i < previousShapes.size(); i++) {
				if (previousShapes.get(i) instanceof WofoActivityBean) {
					previousActvityIDList
							.add(((WofoActivityBean) previousShapes.get(i))
									.getActivityId());
				}
			}
			editor.getCache().cache(
					"PROCESS_ACTIVITY_IDs_" + processBean.getProcessId(),
					previousActvityIDList);
		}
		// 进行新版本判断
		for (AbstractShape s : graphVector) {
			s.saveShape(shapeBeans, this);
			if (s instanceof WfActivity) {
				WfActivity act = (WfActivity) s;
				currentActvityIDList.add(act.getWofoBean().getID());
				if (previousShapes != null
						&& !previousActvityIDList.contains(act.getWofoBean()
								.getID())) {
					processBean.setNextVersion(true);
				}
			}
		}
		if (previousShapes == null && graphVector != null
				&& graphVector.size() > 0) {
			processBean.setNextVersion(true);
		}
		for (String id : previousActvityIDList) {
			if (!currentActvityIDList.contains(id)) {
				processBean.setNextVersion(true);
				break;
			}
		}
		processBean.setGraphs(shapeBeans);
		if (processBean.isNextVersion()) {
			// 如果是新版本，则在新版本对象中赋予老版本对象值
			WofoProcessBean oldVersionProcess = (WofoProcessBean) editor
					.getOperationArea().getWfTree().getOriginalProcess(
							processBean);
			// 新版本赋予新ID
			String processXML = null;
			try {
				processXML = CopyableUtils.toString(processBean);
				// 先要移除掉operationArea中缓存的key-value对应关系
				editor.getOperationArea().getBoards().remove(
						processBean.getID());
				processXML = Functions.applyNewVersion(processXML);
				processBean = (WofoProcessBean) CopyableUtils
						.toObject(processXML);
				editor.getOperationArea().getBoards().put(processBean.getID(),
						this);
				processBean.setOldVersionProcess(oldVersionProcess);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 图形也要全部变更掉
			graphVector.clear();

		} else {
			// 如果之前保存过并且进行过版本变更，但最后又一次又不进行变更，则要把老版本对象置为空
			processBean.setOldVersionProcess(null);
		}
		setEdited(false);
		// 将克隆对象再次克隆后赋给节点，作为userobject
		flowNode.setUserObject(Functions.deepClone(processBean));
		openGraphs(processBean, false);

		if (!toServer)
			editor.saveLocal(GlobalConstants.tempFile, (Serializable) editor
					.getOperationArea().getWfTree().getModel().getRoot());
	}

	/**
	 * 打印图形
	 * 
	 * @param g
	 * @param pf
	 * @param page
	 * @return
	 * @throws PrinterException
	 */
	public int print(Graphics g, PageFormat pf, int page)
			throws PrinterException {
		setAllUnselected();
		if (page >= 1)
			return Printable.NO_SUCH_PAGE;
		Graphics2D g2 = (Graphics2D) g;
		setGraphProperty(g2);
		g2.setPaint(Color.black);
		g2.translate(pf.getImageableX(), pf.getImageableY());
		g2.draw(new Rectangle2D.Double(0, 0, pf.getImageableWidth(), pf
				.getImageableHeight()));
		this.paint(g2);
		return Printable.PAGE_EXISTS;
	}

	/**
	 * 获取当前PaintBoard的UndoRedoManager
	 * 
	 * @return
	 */
	public UndoManager getUndomgr() {
		return undomgr;
	}

	/**
	 * 根据业务id获取图形
	 * 
	 * @param id
	 * @return
	 */
	public AbstractShape getShapeById(String id) {
		for (AbstractShape s : graphVector) {
			WofoBaseBean bean = s.getWofoBean();
			if (bean != null && id.equals(bean.getID())) {
				return s;
			}
		}
		return null;
	}

	/**
	 * 获取面板所在的流程树节点
	 * 
	 * @return
	 */
	public DefaultMutableTreeNode getFlowNode() {
		return flowNode;
	}

	/**
	 * 设置面板所在的流程树节点
	 * 
	 * @param flowNode
	 */
	public void setFlowNode(DefaultMutableTreeNode flowNode) {
		this.flowNode = flowNode;
	}

	/**
	 * 为当前空白绘图区添加默认活动（开始+结束）
	 */
	public void applyDefault() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (graphVector.size() > 0) {
					new Exception("applyDefault()只能用在PaintBoard初次无数据打开时")
							.printStackTrace();
					return;
				}
				Dimension size = PaintBoard.this.getParent().getSize();
				WfActivity startAct = new WfActivity(size.getWidth() / 2 - 80,
						size.getHeight() / 2 - 80,
						WofoActivityBean.ACTIVITY_TYPE_BEGIN);
				startAct.updatePaste(PaintBoard.this);
				WfActivity endAct = new WfActivity(size.getWidth() / 2 + 20,
						size.getHeight() / 2 + 20,
						WofoActivityBean.ACTIVITY_TYPE_END);
				endAct.updatePaste(PaintBoard.this);
				((WofoActivityBean) startAct.getWofoBean())
						.setActivityName("开始");
				((WofoActivityBean) startAct.getWofoBean())
						.setActivityCode("BEGIN");
				((WofoActivityBean) endAct.getWofoBean()).setActivityName("结束");
				((WofoActivityBean) endAct.getWofoBean())
						.setActivityCode("END");
				WfTransition tra = new WfTransition(startAct, endAct);
				tra.updatePaste(PaintBoard.this);
				graphVector.add(startAct);
				graphVector.add(endAct);
				graphVector.add(tra);
				PaintBoard.this.repaint();
			}
		});

	}

	/**
	 * 获取正在拖动的图形
	 * 
	 * @return
	 */
	public AbstractShape getDraggingShape() {
		return draggingShape;
	}

	/**
	 * 获取操作模式
	 * 
	 * @return
	 */
	public int getToolMode() {
		return editor.getModeManager().getToolMode();

	}

	/**
	 * 设置正在拖动的图形
	 * 
	 * @param draggingShape
	 */
	public void setDraggingShape(AbstractShape draggingShape) {
		this.draggingShape = draggingShape;
	}

	@Override
	public String getActivityShapeStyle() {
		return editor.getWfActivityType();
	}

	public void openGraphs(WofoProcessBean process, boolean revalidate) {
		super.openGraphs(process, revalidate);
		editor.getOperationArea().getActivityArea().treeSelectionChanged(null);
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		if (!editor.isEnabled()) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					0.5f));
		}
		super.paint(g2);
	}

}
