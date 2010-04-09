package com.nci.domino.shape.basic;

import java.awt.event.MouseEvent;

import com.nci.domino.PaintBoardBasic;
import com.nci.domino.edit.ToolMode;

/**
 * �򵥵�ͼ�ζ���ֻ�ǵ���ͼ�Σ����Ҷ�Ӧx��y������л��ƣ� �������֧���϶�������Ȳ���
 * 
 * @author Qil.Wong
 * 
 */
public abstract class SimpleLocationShape extends AbstractShape {

	protected double x;

	protected double y;

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void mouseDragged(MouseEvent e, PaintBoardBasic board) {
		if (board.getToolMode() == ToolMode.TOOL_SELECT_OR_DRAG) {
			double transX = board.getxCentOld() + board.getWxNew()
					- board.getWxOld() - x;
			double transY = board.getyCentOld() + board.getWyNew()
					- board.getWyOld() - y;
			setX(board.getxCentOld() + board.getWxNew() - board.getWxOld());

			setY(board.getyCentOld() + board.getWyNew() - board.getWyOld());

			if (board.getSelectedShapes().contains(this)) {
				for (AbstractShape sh : board.getSelectedShapes()) {
					// ��ѡ�����Ҫ����
					if (sh != this) {
						sh.trans(transX, transY);
					}
				}
			} else {
				board.setSelectedShape(this);
			}
			// �϶�undoredo
			if (!board.isDragging()) { // ��һ���϶�
				board.getGhostDraggedShapes().clear();
				for (AbstractShape s : board.getSelectedShapes()) {
					board.getGhostDraggedShapes().add(s.cloneShape());
				}
				board.setDragging(true);
			}
		}
	}

	public void mousePressed(MouseEvent e, PaintBoardBasic board) {
		board.setxCentOld(x);
		board.setyCentOld(y);
	}

}
