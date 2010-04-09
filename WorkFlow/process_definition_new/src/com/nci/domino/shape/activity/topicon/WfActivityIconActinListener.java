package com.nci.domino.shape.activity.topicon;

import com.nci.domino.PaintBoardBasic;
import com.nci.domino.shape.basic.WfActivityBasic;

/**
 * 活动右上角图标事件监听
 * 
 * @author Qil.Wong
 * 
 */
public interface WfActivityIconActinListener {

	/**
	 * 双击事件
	 * 
	 * @param board
	 * @param activity
	 */
	public void onClick(PaintBoardBasic board, WfActivityBasic activity);
}
