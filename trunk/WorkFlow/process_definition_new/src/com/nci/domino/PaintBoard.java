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
 * @author ������ ����ͼ��
 * @since 4.0 modified Qil.Wong
 */
public class PaintBoard extends PaintBoardBasic implements Printable {

	protected static final long serialVersionUID = -5641334723748403706L;
	/**
	 * ָ�򸸴��ڵ�ָ��
	 */
	protected WfEditor editor = null;

	// undo redo ����������
	protected UndoManager undomgr = null;

	// ��ǰ��ͼ�����ڵĽڵ�
	protected DefaultMutableTreeNode flowNode;

	// ǰһ�ε�mode
	protected int prevMode;

	// �ж����ĵ��Ƿ�仯
	boolean centerPointChanged = false;

	public PaintBoard() {
		super();
	}

	/**
	 * ������
	 */
	public PaintBoard(WfEditor editor, DefaultMutableTreeNode flowNode) {
		super();
		this.editor = editor;
		this.flowNode = flowNode;
		initialize();
	}

	/**
	 * ��ʼ����panel
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
								Level.INFO, "ToolMode:" + toolMode + ",��δʵ��");
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
			 * ������������¼�
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
							Level.INFO, "ToolMode:" + toolMode + ",��δʵ��");
					break;
				}
				isCommanding = true;
			}

			/*
			 * ����������¼�
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

			// �����϶��¼�
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
	 * ע������¼�
	 */
	protected void regestorKeyBoardAction() {
		// delɾ��
		this.registerKeyboardAction(new AbstractAction("delete_shape") {
			public void actionPerformed(ActionEvent e) {
				removeSelectShapes();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0),
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		// ctrl+z����
		this.registerKeyboardAction(new AbstractAction("undo") {
			public void actionPerformed(ActionEvent e) {
				undo();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK),
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		// ctrl+y����
		this.registerKeyboardAction(new AbstractAction("redo") {
			public void actionPerformed(ActionEvent e) {
				redo();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK),
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		// ctrl+c����
		this.registerKeyboardAction(new AbstractAction("copy") {
			public void actionPerformed(ActionEvent e) {
				copy();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK),
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		// ctrl+vճ��
		this.registerKeyboardAction(new AbstractAction("paste") {
			public void actionPerformed(ActionEvent e) {
				paste();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK),
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

		// ctrl+aȫѡ
		this.registerKeyboardAction(new AbstractAction("select_all") {
			public void actionPerformed(ActionEvent e) {
				selectAll();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK),
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		// ctrl+s���浱ǰ
		this.registerKeyboardAction(new AbstractAction("save_current") {
			public void actionPerformed(ActionEvent e) {
				preSave();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK),
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	}

	/**
	 * ����ͼ��
	 */
	protected void copy() {
		editor.getCopyPaste().copyShapes(selectedShapes);
	}

	/**
	 * ճ��ͼ��
	 */
	protected void paste() {
		editor.getCopyPaste().pasteShapes(PaintBoard.this);
	}

	/**
	 * ���ñ༭״̬
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
	 * �ͼ��ѡ������������������������˵�����
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
	 * �������ͼ��
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
	 * ���ο�ѡ�� ��һ�������������ͼ�ζ���ѡ�� ��������Ļ����
	 * 
	 * @param x1
	 *            (x1,y1)��һ�����εĶ˵�
	 * @param y1
	 * @param x2
	 *            (x2,y2)�ǵ�һ���˵�ĶԽǵ�
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
	 * �Ƿ񱻱༭��
	 * 
	 * @return
	 */
	public boolean isEdited() {
		return edited;
	}

	/**
	 * ����Ҽ��϶�
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
	 * �������϶�
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
	 * ׼���رգ��ر�ǰ�����δ����ģ�����ʾ��Ҳ�����û�ȡ���رն���
	 * 
	 * @return
	 */
	public boolean isReadyToCloseWithCancel() {
		boolean flag = true;
		if (isEdited()) {
			int result = JOptionPane.showConfirmDialog(this, "����["
					+ processBean.getProcessName() + "]���޸ģ��Ƿ񱣴棿", "��ʾ",
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
	 * ׼���رգ��ر�ǰ�����δ����ģ�����ʾ�����������û�ȡ���رն�������֮���ҲҪ�ر�
	 * 
	 * @return
	 */
	public boolean isReadyToCloseWithoutCancel() {
		boolean flag = true;
		if (isEdited()) {
			int result = JOptionPane.showConfirmDialog(this, "����["
					+ processBean.getProcessName() + "]���޸ģ��Ƿ񱣴棿", "��ʾ",
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
	 * ȫѡ
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
	 * ���
	 * 
	 * @param x
	 *            ���ĵ�x����
	 * @param y
	 *            ���ĵ�y����
	 */
	public WfActivity drawActivity(double x, double y) {
		String currentActivityType = editor.getModeManager().getActivityType();
		if ((currentActivityType.equals(WofoActivityBean.ACTIVITY_TYPE_BEGIN))
				&& isExistSameClass(currentActivityType)) {
			JOptionPane.showMessageDialog(editor, "��ʼ�ֻ����һ��!");
			return null;
		}
		if (currentActivityType == null) {
			JOptionPane.showMessageDialog(editor, "��ѡ������!");
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
	 * ����Ӿ��ͼ
	 */
	protected WfPipeShape drawPipe() {
		boolean vertical = "V".equals(editor.getModeManager().getPipeType());
		int index = 0;
		double formerLength = 0;// ֮ǰ����Ӿ���ĳ��ȣ���ֱӾ����Ӧ��ȣ�ˮƽӾ����Ӧ�߶ȣ�
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
	 * ���Ʊ�ע
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
	 * ���Ʊ�ע��Ϣ������·
	 */
	protected void drawNoteLinkLine() {
		AbstractShape twoact = findShapeLoaction(wxNew, wyNew);
		WfNoteShapeBasic noteShape = (WfNoteShapeBasic) draggingShape;
		if (twoact != null) {
			noteShape.addNotedShape(twoact);
		}
	}

	/**
	 * ����Ǩ��
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
					JOptionPane.showMessageDialog(this, "��ʼ��ͽ������ֻ����һ��!");
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * �Ƿ��Ѿ�������ͬ�Ļ����
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
	 * ɾ��ͼ��
	 * 
	 * @param shape
	 */
	public void removeGraph(AbstractShape shape) {
		shape.removeShape(this);
		// graphVector.remove(shape);
	}

	/**
	 * ���������֮���ǲ����Ѿ�����һ��Ǩ����
	 * 
	 * @param oneact
	 *            ��һ���
	 * @param twoact
	 *            �ڶ����
	 * @return true��ʾ��
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
	 * �����ĸ��� ����һ����յ��ı���
	 * 
	 * @param g2
	 *            Graphics2Dʵ��
	 * @param p1
	 *            ��һ��
	 * @param p2
	 *            �ڶ���
	 * @param p3
	 *            ������
	 * @param p4
	 *            ���ĵ�
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
	 * ��������
	 * 
	 * @param g2
	 *            Graphics2Dʵ��
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
	 * ɾ������ѡ�е�ͼ�ζ���
	 */
	public void removeSelectShapes() {
		List<AbstractShape> s = new ArrayList<AbstractShape>();
		// ��ѡ�е�ͼ�ν���������ɾǨ�ƣ���ɾ�
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
	 * ɾ��һ���
	 * 
	 * @param act
	 *            WfActivityʵ��
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
	 * ������ݵ���Ч��
	 * 
	 * @return true��ʾ��Ч
	 */
	public boolean dataIsValid() {
		int startCount = 0;
		int endCount = 0;
		if (processBean == null) {
			JOptionPane.showMessageDialog(this, "��ʼ��ͽ����������ֻ����һ��!");
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
			JOptionPane.showMessageDialog(this, "��ʼ��ͽ����������ֻ����һ��!");
			return false;
		}
		if (processBean == null) {
			JOptionPane.showMessageDialog(this, "�ڿհ״��Ҽ��˵��������̰汾����!");
			return false;
		}
		return true;
	}

	// -----------------------------

	/**
	 * ����ǰһ�β���
	 */
	public void undo() {
		if (undomgr.canUndo()) {
			undomgr.undo();
		}
	}

	/**
	 * �ظ�ǰһ�γ����Ĳ���
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
	 * �ݴ棬��ͼ���ϵ����ݺ�������������ͬ��
	 * 
	 * @param toServer
	 *            �Ƿ��͵�������
	 */
	public void preSave(boolean toServer) {
		if (flowNode == null) { // flowNode==null��ʾ�ǰ汾��board�����뱣��
			return;
		}
		List<WofoBaseBean> shapeBeans = new ArrayList<WofoBaseBean>();
		// ��ǰ��δ����ʱ������ҵ�����
		List<WofoBaseBean> previousShapes = processBean.getGraphs();

		// �����id
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
		// �����°汾�ж�
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
			// ������°汾�������°汾�����и����ϰ汾����ֵ
			WofoProcessBean oldVersionProcess = (WofoProcessBean) editor
					.getOperationArea().getWfTree().getOriginalProcess(
							processBean);
			// �°汾������ID
			String processXML = null;
			try {
				processXML = CopyableUtils.toString(processBean);
				// ��Ҫ�Ƴ���operationArea�л����key-value��Ӧ��ϵ
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
			// ͼ��ҲҪȫ�������
			graphVector.clear();

		} else {
			// ���֮ǰ��������ҽ��й��汾������������һ���ֲ����б������Ҫ���ϰ汾������Ϊ��
			processBean.setOldVersionProcess(null);
		}
		setEdited(false);
		// ����¡�����ٴο�¡�󸳸��ڵ㣬��Ϊuserobject
		flowNode.setUserObject(Functions.deepClone(processBean));
		openGraphs(processBean, false);

		if (!toServer)
			editor.saveLocal(GlobalConstants.tempFile, (Serializable) editor
					.getOperationArea().getWfTree().getModel().getRoot());
	}

	/**
	 * ��ӡͼ��
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
	 * ��ȡ��ǰPaintBoard��UndoRedoManager
	 * 
	 * @return
	 */
	public UndoManager getUndomgr() {
		return undomgr;
	}

	/**
	 * ����ҵ��id��ȡͼ��
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
	 * ��ȡ������ڵ��������ڵ�
	 * 
	 * @return
	 */
	public DefaultMutableTreeNode getFlowNode() {
		return flowNode;
	}

	/**
	 * ����������ڵ��������ڵ�
	 * 
	 * @param flowNode
	 */
	public void setFlowNode(DefaultMutableTreeNode flowNode) {
		this.flowNode = flowNode;
	}

	/**
	 * Ϊ��ǰ�հ׻�ͼ�����Ĭ�ϻ����ʼ+������
	 */
	public void applyDefault() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (graphVector.size() > 0) {
					new Exception("applyDefault()ֻ������PaintBoard���������ݴ�ʱ")
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
						.setActivityName("��ʼ");
				((WofoActivityBean) startAct.getWofoBean())
						.setActivityCode("BEGIN");
				((WofoActivityBean) endAct.getWofoBean()).setActivityName("����");
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
	 * ��ȡ�����϶���ͼ��
	 * 
	 * @return
	 */
	public AbstractShape getDraggingShape() {
		return draggingShape;
	}

	/**
	 * ��ȡ����ģʽ
	 * 
	 * @return
	 */
	public int getToolMode() {
		return editor.getModeManager().getToolMode();

	}

	/**
	 * ���������϶���ͼ��
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
