package com.nci.domino.shape.activity.topicon;

import com.nci.domino.PaintBoardBasic;
import com.nci.domino.shape.basic.WfActivityBasic;

public class WfMessageIconAction implements WfActivityIconActinListener {

	public void onClick(PaintBoardBasic board, WfActivityBasic activity) {
		System.out.println("message");
	}

}
