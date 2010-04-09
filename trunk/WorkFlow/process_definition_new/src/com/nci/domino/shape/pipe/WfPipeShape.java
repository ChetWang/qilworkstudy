package com.nci.domino.shape.pipe;

import java.awt.event.MouseEvent;

import com.nci.domino.PaintBoard;
import com.nci.domino.PaintBoardBasic;
import com.nci.domino.beans.plugin.pipe.WofoPipeBaseBean;
import com.nci.domino.components.dialog.WfDialog;
import com.nci.domino.edit.ToolMode;
import com.nci.domino.shape.basic.WfPipeShapeBasic;

/**
 * 管道图对象
 * 
 * @author Qil.Wong
 * 
 */
public class WfPipeShape extends WfPipeShapeBasic {

	private Class<WfDialog> inputDialogClass;

	public WfPipeShape(int index, double formerLength, boolean vertical) {
		super(index, formerLength, vertical);
		inputDialogClass = vertical ? WfPipeConfig.verticalPipeInput
				: WfPipeConfig.horizontalPipeInput;
	}

	public int showInput() {
		return WfDialog.RESULT_AFFIRMED;
	}

	public void mouseDoubleClicked(MouseEvent e, PaintBoardBasic b) {
		// 双击显示属性
		if (b.getToolMode() == ToolMode.TOOL_SELECT_OR_DRAG) {
			WfDialog dialog = ((PaintBoard) b).getEditor().getDialogManager()
					.getDialog(inputDialogClass,
							((WofoPipeBaseBean) wofoBean).getShowText(), true);
			dialog.showWfDialog(wofoBean);
			if (dialog.getDialogResult() == WfDialog.RESULT_AFFIRMED) {
				dialog.getInputValues();
			}
		}
	}

}
