package com.nci.domino.shape.pipe;

import java.awt.event.ActionEvent;

import com.nci.domino.components.toolbar.WfToolBar;
import com.nci.domino.components.toolbar.WfToolbarActionListener;
import com.nci.domino.edit.ToolMode;

/**
 * 管道按钮的事件
 * @author Qil.Wong
 *
 */
public class WfToolbarPipeActionListener extends WfToolbarActionListener {

	public WfToolbarPipeActionListener(WfToolBar toolbar) {
		super(toolbar);
	}

	public void actionPerformed(ActionEvent e) {
		toolbar.getEditor().getModeManager().setToolMode(
				ToolMode.Tool_DRAW_PIPE);
		String action = e.getActionCommand();
		if (action.indexOf("hori") >= 0) {
			toolbar.getEditor().getModeManager().setPipeType("H");
		} else {
			toolbar.getEditor().getModeManager().setPipeType("V");
		}
	}

}
