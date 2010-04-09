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
 * UNDOREDO�Ķ�������
 * 
 * @author Qil.Wong
 * 
 */
public class ActionGroup extends AbstractUndoableEdit {

	private static final long serialVersionUID = 7808366326088474211L;

	/**
	 * ͼ�����Ӷ���
	 */
	public static final int ADD = 0;

	/**
	 * ͼ���Ƴ�����
	 */
	public static final int DELETE = 1;

	/**
	 * ͼ���޸Ķ���
	 */
	public static final int MODIFY = 3;

	// ͼ�β�������
	private int action = ADD;

	// ��ǰ��Ӱ���ͼ��
	List<AbstractShape> effectShapes = new ArrayList<AbstractShape>();

	// �϶���������ʼ��shape������ʼ�˿�ʼ��¡
	List<AbstractShape> ghostShapes = new ArrayList<AbstractShape>();

	// ������ͼ��
	PaintBoard board;

	/**
	 * ��һ��
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
	 * ����һ��
	 * 
	 * @param sh
	 */
	public ActionGroup(AbstractShape sh, PaintBoard board) {
		effectShapes.add(sh);
		this.board = board;
	}

	/**
	 * ����һ��
	 * 
	 * @param shs
	 */
	public ActionGroup(List<AbstractShape> shs, PaintBoard board) {
		this(shs, ADD, board);
	}

	/**
	 * ����һ��
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
	 * ����������������˷�ʱ��������Ժ�ÿ��һ�ش��룬�������ֶ���1,^_^
	 */
	private int waste_times_here = 3;// -_-!

	/**
	 * �϶����undoredo
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