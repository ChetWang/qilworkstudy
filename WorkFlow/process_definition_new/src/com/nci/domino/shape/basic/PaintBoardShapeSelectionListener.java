package com.nci.domino.shape.basic;

import java.util.List;

/**
 * ͼ��ѡ���¼�
 * 
 * @author Qil.Wong
 * 
 */
public interface PaintBoardShapeSelectionListener {

	/**
	 * ͼ��ѡ��仯����
	 * 
	 * @param currentSelectedShapes
	 *            ��ǰѡ���ͼ��
	 * @param changedShapes
	 *            �仯��ͼ��
	 */
	public void shapeSelectionStateChanged(
			List<AbstractShape> currentSelectedShapes,
			List<AbstractShape> changedShapes);

}
