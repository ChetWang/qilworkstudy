package com.nci.domino.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import com.nci.domino.PaintBoard;
import com.nci.domino.beans.desyer.WofoConditionMemberBean;
import com.nci.domino.shape.WfTransition;

public class WfConditionMenuItem extends JMenuItem {

	public WfConditionMenuItem(final Object o, final WfTransition trans,
			final PaintBoard board) {
		super(o.toString());
		final WofoConditionMemberBean member = (WofoConditionMemberBean)o;
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				trans.setTransitionName(o.toString());
				trans.getTransitionBean().setConditionMemberId(member.getMemberId());
//				trans.getTransitionBean().setAnchors(anchors)
				board.repaint();
			}
		});
	}

}
