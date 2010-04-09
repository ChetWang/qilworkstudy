package com.nci.domino.shape.basic;

import java.util.List;

/**
 * 图形选择事件
 * 
 * @author Qil.Wong
 * 
 */
public interface PaintBoardShapeSelectionListener {

	/**
	 * 图形选择变化动作
	 * 
	 * @param currentSelectedShapes
	 *            当前选择的图形
	 * @param changedShapes
	 *            变化的图形
	 */
	public void shapeSelectionStateChanged(
			List<AbstractShape> currentSelectedShapes,
			List<AbstractShape> changedShapes);

}
