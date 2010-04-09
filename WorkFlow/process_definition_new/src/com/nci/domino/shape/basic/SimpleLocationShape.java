package com.nci.domino.shape.basic;

import java.awt.event.MouseEvent;

import com.nci.domino.PaintBoardBasic;
import com.nci.domino.edit.ToolMode;

/**
 * 简单的图形对象，只是单个图形，并且对应x，y坐标进行绘制； 此类对象支持拖动，对齐等操作
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
					// 多选情况下要联动
					if (sh != this) {
						sh.trans(transX, transY);
					}
				}
			} else {
				board.setSelectedShape(this);
			}
			// 拖动undoredo
			if (!board.isDragging()) { // 第一次拖动
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
