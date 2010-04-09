package com.nci.domino.shape.activity.topicon;

import javax.swing.JOptionPane;

import com.nci.domino.PaintBoardBasic;
import com.nci.domino.shape.basic.WfActivityBasic;

/**
 * 附件图标的双击触发事件，demo 测试
 * @author Qil.Wong
 *
 */
public class WfAttachmentsIconAction implements WfActivityIconActinListener {

	public void onClick(PaintBoardBasic board, WfActivityBasic activity) {
		JOptionPane.showConfirmDialog(board, "Bye, Java World!", "INFO",
				JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
	}

}
