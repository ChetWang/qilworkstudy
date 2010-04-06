package com.nci.svg.sdk.svgeditor.selection;

import fr.itris.glips.svgeditor.shape.AbstractShape;

/**
 * »æÍ¼Í¼Ôª±ä»¯¼àÌýÆ÷
 * @author qi
 *
 */
public interface DrawingShapeChangeListener {

	public void drawingShapeChanged(AbstractShape previousShape,
			AbstractShape currentShape);

}
