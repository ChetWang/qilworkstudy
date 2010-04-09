package com.nci.domino.components.toolbar;

import java.util.ArrayList;
import java.util.List;

import com.nci.domino.PaintBoard;
import com.nci.domino.WfEditor;
import com.nci.domino.edit.ActionGroup;
import com.nci.domino.shape.basic.AbstractShape;
import com.nci.domino.shape.basic.SimpleLocationShape;

public class WfAlignManager {

	private WfEditor editor;

	public WfAlignManager(WfEditor editor) {
		this.editor = editor;
	}

	/**
	 * ¶¥¶ÔÆë
	 */
	public void alignTop() {
		PaintBoard board = editor.getOperationArea().getCurrentPaintBoard();
		List<AbstractShape> selectedShapes = board.getSelectedShapes();
		List<AbstractShape> ghostShapes = new ArrayList<AbstractShape>();
		double minY = Double.MAX_VALUE;
		SimpleLocationShape topShape = null;
		for (AbstractShape s : selectedShapes) {
			if (s instanceof SimpleLocationShape) {
				ghostShapes.add(s.cloneShape());
				if (s.getMinYPos() < minY) {
					minY = s.getMinYPos();
					topShape = (SimpleLocationShape) s;
				}
			}
		}
		for (AbstractShape s : selectedShapes) {
			if (s instanceof SimpleLocationShape) {
				((SimpleLocationShape) s).setY(topShape.getY());
			}
		}
		board.getUndomgr().addEdit(
				new ActionGroup(ghostShapes, selectedShapes, board));
		board.repaint();
	}

	/**
	 * µ×¶ÔÆë
	 */
	public void alignBottom() {
		PaintBoard board = editor.getOperationArea().getCurrentPaintBoard();
		List<AbstractShape> selectedShapes = board.getSelectedShapes();
		List<AbstractShape> ghostShapes = new ArrayList<AbstractShape>();
		double maxY = Double.MIN_VALUE;
		SimpleLocationShape bottomShape = null;
		for (AbstractShape s : selectedShapes) {
			if (s instanceof SimpleLocationShape) {
				ghostShapes.add(s.cloneShape());
				if (s.getMaxYPos() > maxY) {
					maxY = s.getMaxYPos();
					bottomShape = (SimpleLocationShape) s;
				}
			}
		}
		for (AbstractShape s : selectedShapes) {
			if (s instanceof SimpleLocationShape) {
				((SimpleLocationShape) s).setY(bottomShape.getY());
			}
		}
		board.getUndomgr().addEdit(
				new ActionGroup(ghostShapes, selectedShapes, board));
		board.repaint();
	}

	/**
	 * ×ó¶ÔÆë
	 */
	public void alignLeft() {
		PaintBoard board = editor.getOperationArea().getCurrentPaintBoard();
		List<AbstractShape> selectedShapes = board.getSelectedShapes();
		List<AbstractShape> ghostShapes = new ArrayList<AbstractShape>();
		double minX = Double.MAX_VALUE;
		SimpleLocationShape bottomShape = null;
		for (AbstractShape s : selectedShapes) {
			if (s instanceof SimpleLocationShape) {
				ghostShapes.add(s.cloneShape());
				if (s.getMinXPos() < minX) {
					minX = s.getMinXPos();
					bottomShape = (SimpleLocationShape) s;
				}
			}
		}
		for (AbstractShape s : selectedShapes) {
			if (s instanceof SimpleLocationShape) {
				((SimpleLocationShape) s).setX(bottomShape.getX());
			}
		}
		board.getUndomgr().addEdit(
				new ActionGroup(ghostShapes, selectedShapes, board));
		board.repaint();
	}

	/**
	 * ÓÒ¶ÔÆë
	 */
	public void alignRight() {
		PaintBoard board = editor.getOperationArea().getCurrentPaintBoard();
		List<AbstractShape> selectedShapes = board.getSelectedShapes();
		List<AbstractShape> ghostShapes = new ArrayList<AbstractShape>();
		double maxX = Double.MIN_VALUE;
		SimpleLocationShape bottomShape = null;
		for (AbstractShape s : selectedShapes) {
			if (s instanceof SimpleLocationShape) {
				ghostShapes.add(s.cloneShape());
				if (s.getMaxXPos() > maxX) {
					maxX = s.getMaxXPos();
					bottomShape = (SimpleLocationShape) s;
				}
			}
		}
		for (AbstractShape s : selectedShapes) {
			if (s instanceof SimpleLocationShape) {
				((SimpleLocationShape) s).setX(bottomShape.getX());
			}
		}
		board.getUndomgr().addEdit(
				new ActionGroup(ghostShapes, selectedShapes, board));
		board.repaint();
	}

}
