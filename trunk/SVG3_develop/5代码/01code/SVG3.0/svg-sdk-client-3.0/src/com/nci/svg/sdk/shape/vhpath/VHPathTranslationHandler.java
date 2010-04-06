package com.nci.svg.sdk.shape.vhpath;

import java.awt.geom.Point2D;
import java.util.Set;

import org.w3c.dom.Element;

import fr.itris.glips.svgeditor.display.canvas.CanvasPainter;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.display.selection.SelectionItem;
import fr.itris.glips.svgeditor.display.undoredo.UndoRedoAction;

public interface VHPathTranslationHandler {

	public UndoRedoAction modifyPoint(final SVGHandle handle,
			final Element element, SelectionItem item, Point2D point);

	public CanvasPainter showAction(SVGHandle handle, int level,
			Set<Element> elementSet, SelectionItem item, Point2D firstPoint,
			Point2D currentPoint);

	public CanvasPainter showTranslateAction(SVGHandle handle,
			Set<Element> elementSet, Point2D firstPoint, Point2D currentPoint);

	public UndoRedoAction validateTranslateAction(SVGHandle handle,
			Set<Element> elementSet, Point2D firstPoint, Point2D currentPoint);
}
