package com.nci.domino.edit;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.undo.AbstractUndoableEdit;

import com.nci.domino.PaintBoard;
import com.nci.domino.shape.WfActivity;
import com.nci.domino.shape.WfTransition;
import com.nci.domino.shape.basic.AbstractShape;
import com.nci.domino.shape.basic.WfTextShapeBasic;

/**
 * UNDOREDO的动作对象
 * 
 * @author Qil.Wong
 * 
 */
public class ActionGroup extends AbstractUndoableEdit {

	private static final long serialVersionUID = 7808366326088474211L;

	/**
	 * 图形增加动作
	 */
	public static final int ADD = 0;

	/**
	 * 图形移除动作
	 */
	public static final int DELETE = 1;

	/**
	 * 图形修改动作
	 */
	public static final int MODIFY = 3;

	// 图形操作动作
	private int action = ADD;

	// 当前受影响的图形
	List<AbstractShape> effectShapes = new ArrayList<AbstractShape>();

	// 拖动过程中起始的shape，从起始端开始克隆
	List<AbstractShape> ghostShapes = new ArrayList<AbstractShape>();

	// 操作绘图区
	PaintBoard board;

	/**
	 * 改一个
	 * 
	 * @param sh
	 */
	public ActionGroup(List<AbstractShape> ghostShapes,
			List<AbstractShape> newShapes, PaintBoard board) {
		this.effectShapes.addAll(newShapes);
		this.ghostShapes.addAll(ghostShapes);
		this.board = board;
		this.action = MODIFY;
	}

	/**
	 * 加入一个
	 * 
	 * @param sh
	 */
	public ActionGroup(AbstractShape sh, PaintBoard board) {
		effectShapes.add(sh);
		this.board = board;
	}

	/**
	 * 加入一组
	 * 
	 * @param shs
	 */
	public ActionGroup(List<AbstractShape> shs, PaintBoard board) {
		this(shs, ADD, board);
	}

	/**
	 * 操作一组
	 * 
	 * @param shs
	 * @param action
	 * @param board
	 */
	public ActionGroup(List<AbstractShape> shs, int action, PaintBoard board) {
		this.effectShapes.addAll(shs);
		this.action = action;
		this.board = board;
	}

	public void undo() {

		switch (action) {
		case ADD:
			deleteShapes(effectShapes);
			break;
		case DELETE:
			addShapes(effectShapes);
			break;
		case MODIFY:
			undoRedoDrag();
			break;
		default:
			break;
		}
		board.repaint();
	}

	public void redo() {
		switch (action) {
		case ADD:
			addShapes(effectShapes);
			break;
		case DELETE:
			deleteShapes(effectShapes);
			break;
		case MODIFY:
			undoRedoDrag();
			break;
		default:
			break;
		}
		board.repaint();
	}

	private void addShapes(List<AbstractShape> s) {
		for (AbstractShape a : s) {
			board.addGraph(a);
		}
	}

	private void deleteShapes(List<AbstractShape> s) {
		for (AbstractShape a : s) {
			board.removeGraph(a);
		}
	}

	/**
	 * 在下面这个方法上浪费时间次数，以后每改一回代码，请主动手动加1,^_^
	 */
	private int waste_times_here = 3;// -_-!

	/**
	 * 拖动后的undoredo
	 */
	private void undoRedoDrag() {
		try {
			List<AbstractShape> temp = new ArrayList<AbstractShape>();
			for (int i = 0; i < ghostShapes.size(); i++) {
				AbstractShape s = ghostShapes.get(i);
				if (s instanceof WfActivity) {
					WfActivity appliedAct = (WfActivity) effectShapes.get(i);
					temp.add(appliedAct.cloneShape());
					appliedAct.setX(((WfActivity) s).getX());
					appliedAct.setY(((WfActivity) s).getY());
					appliedAct.setWofoBean(((WfActivity) s).getWofoBean());
				} else if (s instanceof WfTransition) {
					WfTransition appliedTrans = (WfTransition) effectShapes
							.get(i);
					temp.add(appliedTrans.cloneShape());
					appliedTrans.setTransitionBean((((WfTransition) s)
							.getTransitionBean()));
				} else if (s instanceof WfTextShapeBasic) {
					WfTextShapeBasic appliedString = (WfTextShapeBasic) effectShapes
							.get(i);
					temp.add(appliedString.cloneShape());
					// appliedString.x = ((WfTextShape) s).x;
					appliedString.setX(((WfTextShapeBasic) s).getX());
					// appliedString.y = ((WfTextShape) s).y;
					appliedString.setY(((WfTextShapeBasic) s).getY());
				}
			}
			ghostShapes = temp;
		} catch (Exception e) {
			Logger.getLogger(ActionGroup.class.getName()).log(
					Level.SEVERE,
					e.getClass().getName() + ":" + e.getMessage() + ", "
							+ e.getStackTrace()[0]);
		}
	}

}