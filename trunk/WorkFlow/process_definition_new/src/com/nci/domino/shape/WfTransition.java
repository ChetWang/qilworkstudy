package com.nci.domino.shape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import com.nci.domino.PaintBoard;
import com.nci.domino.PaintBoardBasic;
import com.nci.domino.beans.WofoBaseBean;
import com.nci.domino.beans.desyer.WofoTransitionBean;
import com.nci.domino.edit.ToolMode;
import com.nci.domino.help.Functions;
import com.nci.domino.shape.basic.AbstractShape;
import com.nci.domino.shape.basic.WfActivityBasic;
import com.nci.domino.shape.basic.WfTextShapeBasic;
import com.nci.domino.shape.basic.WfTransitionBasic;

/**
 * @author ������ Ǩ������ modified by Qil.Wong
 * 
 */
public class WfTransition extends WfTransitionBasic {

	public WfTransition(WfActivityBasic firstAct, WfActivityBasic secondAct) {
		super(firstAct, secondAct);
	}

	public void updatePaste(PaintBoardBasic board) {
		WofoTransitionBean transitionBean = (WofoTransitionBean) wofoBean;
		transitionBean.setTransitionId(Functions.getUID());
		transitionBean.setProcessCode(board.getProcessBean().getProcessCode());
		transitionBean.setProcessId(board.getProcessBean().getProcessId());
		transitionBean.setPreviousActivityId(previousActivity.getWofoBean()
				.getID());
		transitionBean.setNextActivityId(nextActivity.getWofoBean().getID());
	}

	@Override
	public void saveShape(List<WofoBaseBean> shapeBeans, PaintBoardBasic board) {
		// �ı�����ʱ��ֱ��Ӱ�쵽wofobean���������Ҫ�������һ��
		getTransitionBean().setWordsPos(
				getTextShape().getX() + "," + getTextShape().getY());
		StringBuffer anchor = new StringBuffer();
		for (int i = 0; i < cornerPoints.size(); i++) {
			Point2D p = cornerPoints.get(i);
			if (i > 0) {
				anchor.append("|");
			}
			anchor.append(p.getX()).append(",").append(p.getY());
		}
		getTransitionBean().setAnchors(anchor.toString());
		shapeBeans.add(wofoBean);
	}

	@Override
	public AbstractShape cloneShape() {
		WofoTransitionBean bean = getTransitionBean().cloenTransition();
		WfTransition newTran = new WfTransition((WfActivity) previousActivity
				.cloneShape(), (WfActivity) nextActivity.cloneShape());
		newTran.selected = selected;
		newTran.setTransitionBean(bean);
		List<Point2D> clonedCornerpPoints = new ArrayList<Point2D>();
		for (Point2D p : cornerPoints) {
			Point2D newP = new Point2D.Double(p.getX(), p.getY());
			clonedCornerpPoints.add(newP);
		}
		newTran.cornerPoints = clonedCornerpPoints;
		newTran.textShape = (WfTextShapeBasic) textShape.cloneShape();
		// idҲӦ�ñ����������Ӱ�쵽���ݿ�id����
		// bean.setTransitionId(Functions.getUID());
		return newTran;
	}

	@Override
	public void mouseDragged(MouseEvent e, PaintBoardBasic b) {
		PaintBoard board = (PaintBoard) b;
		if (currentCenterPoint.isEmpty()
				|| board.getToolMode() != ToolMode.TOOL_SELECT_OR_DRAG) {
			return;
		}
		Point2D currentPoint2d = new Point2D.Double(board.getWxNew(), board
				.getWyNew());

		Point2D previous = currentCenterPoint.getCenterPoint();
		removeCornerPoint(previous);
		// ��һ����קʱ��centerpoint��������wftransiton��cornerpoints��,���ᵼ��һ˲�����ʧ
		currentCenterPoint.setCenterPoint(currentPoint2d);
		// �϶�undoredo
		if (!board.isDragging()) { // ��һ���϶�
			board.getGhostDraggedShapes().clear();
			board.getGhostDraggedShapes().add(cloneShape());
			board.setDragging(true);
		}
		if (board.isDragging()) {
			cornerPoints.add(currentCenterPoint.getCenterIndex(),
					new Point2D.Double(currentPoint2d.getX(), currentPoint2d
							.getY()));
		}
		board.setSelectedShape(this);
	}

	@Override
	protected void autoAlign(Graphics2D g2, PaintBoardBasic pb, int index) {
		boolean vh = false;// ˮƽ��ֱ
		if (((PaintBoard) pb).isDragging()) {
			boolean almostV = Math.abs(cornerPoints.get(index - 1).getX()
					- cornerPoints.get(index).getX()) < 1; // �ӽ���ֱ
			boolean almostH = Math.abs(cornerPoints.get(index - 1).getY()
					- cornerPoints.get(index).getY()) < 1;// �ӽ�ˮƽ
			if (((PaintBoard) pb).getDraggingShape() instanceof WfActivity) {
				WfActivity act = (WfActivity) ((PaintBoard) pb)
						.getDraggingShape();
				if (almostH
						&& Math
								.abs(act.getY()
										- cornerPoints.get(index).getY()) < 1) {
					act.setY(cornerPoints.get(index).getY());
				}
				if (almostV
						&& Math
								.abs(act.getX()
										- cornerPoints.get(index).getX()) < 1) {
					act.setX(cornerPoints.get(index).getX());
				}
			}
			if (almostH || almostV) {
				g2.setColor(Color.cyan);
				vh = true;
			} else {
				g2.setColor(defaultColor);
				vh = false;
			}
		}
		if (isUnderMouse) {
			if (!vh)
				g2.setColor(Color.orange);
		} else {
			if (!vh)
				g2.setColor(defaultColor);
		}
	}

}