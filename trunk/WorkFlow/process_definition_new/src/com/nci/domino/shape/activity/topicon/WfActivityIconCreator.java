package com.nci.domino.shape.activity.topicon;

import java.awt.Graphics2D;

import com.nci.domino.shape.basic.WfActivityBasic;

/**
 * 活动右上角图标生成接口
 * 
 * @author Qil.Wong
 * 
 */
public interface WfActivityIconCreator {

	/**
	 * 绘制图形
	 * 
	 * @param g
	 *            源Paindboard对象的Graphics2D
	 * @param activity
	 *            指定的活动对象
	 */
	public void paint(Graphics2D g, WfActivityBasic activity);
}
