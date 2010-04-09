package com.nci.domino;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.JViewport;

import com.jidesoft.swing.JideScrollPane;

/**
 * »æÍ¼Ãæ°æµÄscrollÇøÓò
 * 
 * @author Qil.Wong
 * 
 */
public class PaintBoardScroll extends JideScrollPane {

	PaintBoard board;

	public PaintBoardScroll(PaintBoard board) {
		this.board = board;
		JPanel p = new JPanel(new BorderLayout());
		p.add(board, BorderLayout.CENTER);
		board.setPaintBoardScroll(this);
		getVerticalScrollBar().setUnitIncrement(20);
		getHorizontalScrollBar().setUnitIncrement(20);
		setViewportView(p);
		getViewport().setScrollMode(
				JViewport.BACKINGSTORE_SCROLL_MODE);
	}

	public PaintBoard getBoard() {
		return board;
	}

	protected void processKeyEvent(KeyEvent evt) {
		return;
	}
}
