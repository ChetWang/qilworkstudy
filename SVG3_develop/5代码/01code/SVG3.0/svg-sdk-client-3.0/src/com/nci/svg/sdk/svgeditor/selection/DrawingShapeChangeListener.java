package com.nci.svg.sdk.svgeditor.selection;

import fr.itris.glips.svgeditor.shape.AbstractShape;

/**
 * ��ͼͼԪ�仯������
 * @author qi
 *
 */
public interface DrawingShapeChangeListener {

	public void drawingShapeChanged(AbstractShape previousShape,
			AbstractShape currentShape);

}
