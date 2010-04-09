package com.nci.domino.shape.note;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.nci.domino.PaintBoard;
import com.nci.domino.PaintBoardBasic;
import com.nci.domino.beans.WofoBaseBean;
import com.nci.domino.beans.desyer.WofoActivityBean;
import com.nci.domino.beans.plugin.note.WofoNoteBean;
import com.nci.domino.components.dialog.WfDialog;
import com.nci.domino.edit.ToolMode;
import com.nci.domino.shape.basic.AbstractShape;
import com.nci.domino.shape.basic.WfNoteShapeBasic;

public class WfNoteShape extends WfNoteShapeBasic {

	public int showInput(PaintBoard board) {
		WfDialog dialog = board.getEditor().getDialogManager().getDialog(
				WfNoteInputDialog.class, "备注", true);
		dialog.showWfDialog(wofoBean);
		return dialog.getDialogResult();
	}

	public void mouseDragged(MouseEvent e, PaintBoardBasic board) {
		if (!resizable) {
			super.mouseDragged(e, board);
		} else {
			board.setSelectedShape(this);
			// 图形宽高的最小值是24
			if (board.getWxNew() - x < 24 / board.getTrans()[0]) {
				return;
			}
			if (board.getWyNew() - y < 24 / board.getTrans()[0]) {
				return;
			}
			width = board.getWxNew() - x;
			height = board.getWyNew() - y;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e, PaintBoardBasic board) {
		if (board.getToolMode() == ToolMode.TOOL_SELECT_OR_DRAG) {
			Rectangle2D rect = new Rectangle2D.Double(x, y, width, height);
			resizable = rect.contains(board.getWxNew(), board.getWyNew())
					&& x + width - board.getWxNew() <= 10 / board.getTrans()[0]
					&& y + height - board.getWyNew() <= 10 / board.getTrans()[0];
			if (resizable) {
				board.setCursor(Cursor
						.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
			} else {
				board.setCursor(Cursor
						.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		}
	}

	@Override
	public void mouseDoubleClicked(MouseEvent e, PaintBoardBasic b) {
		PaintBoard board = (PaintBoard) b;
		int toolMode = board.getToolMode();
		if (toolMode == ToolMode.TOOL_SELECT_OR_DRAG) {
			WfDialog dialog = board.getEditor().getDialogManager().getDialog(
					WfNoteInputDialog.class, "备注", true);
			dialog.showWfDialog(wofoBean);
		}
	}

	public void mouseReleased(MouseEvent e, PaintBoardBasic board) {
		WofoNoteBean noteBean = (WofoNoteBean) wofoBean;
		noteBean.setPosX(x);
		noteBean.setPosY(y);
	}

	public void mouseClicked(MouseEvent e, PaintBoardBasic board) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			JPopupMenu popup = new JPopupMenu();
			JMenu removeMenu = new JMenu("移除注释关联");
			popup.add(removeMenu);
			for (final AbstractShape s : notedShapes) {
				JMenuItem item = new JMenuItem();
				item.setText(s.getWofoBean().toString());
				item.putClientProperty("NotedShape", s);
				removeMenu.add(item);
				item.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						notedShapes.remove(s);

					}
				});
			}
			popup.show(board, e.getX(), e.getY());
		}
	}

	@Override
	public void saveShape(List<WofoBaseBean> shapeBeans, PaintBoardBasic board) {
		WofoNoteBean noteBean = (WofoNoteBean) wofoBean;
		noteBean.setPosX(x);
		noteBean.setPosY(y);
		noteBean.setWidth(width);
		noteBean.setHeight(height);
		shapeBeans.add(noteBean);
	}
}
