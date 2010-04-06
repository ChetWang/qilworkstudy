package fr.itris.glips.svgeditor.display.selection;

import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.w3c.dom.Element;

import com.nci.svg.sdk.other.LinkPointManager;

import fr.itris.glips.svgeditor.actions.clipboard.ClipboardModule;
import fr.itris.glips.svgeditor.display.canvas.CanvasPainter;
import fr.itris.glips.svgeditor.display.canvas.SVGCanvas;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.display.undoredo.UndoRedoAction;
import fr.itris.glips.svgeditor.display.undoredo.UndoRedoActionList;
import fr.itris.glips.svgeditor.shape.AbstractShape;
import fr.itris.glips.svgeditor.shape.GroupShape;
import fr.itris.glips.svgeditor.shape.MultiAbstractShape;
import fr.itris.glips.svgeditor.shape.ShapeToolkit;

/**
 * the class handling selections, translations and actions when only a single
 * element is selected
 * 
 * @author ITRIS, Jordi SUC
 */
public class SingleSelectionManager extends SelectionManager {

	/**
	 * the canvas painter that is currently in use
	 */
	private CanvasPainter currentCanvasPainter = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.itris.glips.svgeditor.display.selection.SelectionManager#copyElement()
	 */
	@Override
	public void copyElement(Set<Element> elements, Point2D point) {
		if (currentCanvasPainter != null) {

			selection.getSVGHandle().getScrollPane().getSVGCanvas()
					.removePaintListener(currentCanvasPainter, true);
		}
		Rectangle2D rectangle = MultiAbstractShape.getElementsBounds(editor
				.getHandlesManager().getCurrentHandle(), elements);
		double x = point.getX() - rectangle.getMinX();
		double y = point.getY() - rectangle.getMinY();
		ClipboardModule clipModule = (ClipboardModule) editor
				.getModule("Clipboard");
		clipModule.copy();
		clipModule.paste(false, true, new Point2D.Double(x, y));

	}

	/**
	 * the constructor of the class
	 * 
	 * @param selectionObject
	 *            the selection object that uses this selection manager
	 */
	public SingleSelectionManager(Selection selectionObject) {
		super(selectionObject.getSVGHandle().getEditor());
		this.selection = selectionObject;
	}

	@Override
	public int getNextSelectionLevel(Set<Element> elements) {

		// getting the selected element
		Element element = elements.iterator().next();

		// getting the current selection level
		int currentSelectionLevel = selection.getSelectionLevel();

		// getting the shape module corresponding to the element
		AbstractShape shape = ShapeToolkit.getShapeModule(element, editor);

		if (shape != null) {

			// getting the max selection level for this module
			int maxSelectionLevel = shape.getLevelCount();

			// computing the new selection level
			return ((currentSelectionLevel + 1) % (maxSelectionLevel + 1));
		}

		return 0;
	}

	@Override
	public Set<SelectionItem> getSelectionItems(SVGHandle handle,
			Set<Element> elements, int level) {

		if (selection.isSelectionLocked()) {

			return getLockedSelectionItems(handle, elements);

		} else {

			// the selection items set that will be returned
			HashSet<SelectionItem> items = new HashSet<SelectionItem>();
			Iterator<Element> it = elements.iterator();
			while (it.hasNext()) {

				Element element = it.next();

				// getting the shape module corresponding to this element
				AbstractShape shapeModule = ShapeToolkit.getShapeModule(
						element, editor);

				if (shapeModule != null) {
					Set<Element> set = new HashSet<Element>();
					set.add(element);
					items.addAll(shapeModule.getSelectionItems(handle, set,
							level));
				}
			}

			return items;
		}
	}

	@Override
	public void doTranslateAction(Set<Element> elements, Point2D firstPoint,
			Point2D currentPoint) {

		// getting the element that will undergo the
		// action，singleSelectionManager只负责单个元素的平移
		Element element = elements.iterator().next();

		// getting the shape module corresponding to this action
		AbstractShape shape = ShapeToolkit.getShapeModule(element, editor);

		if (shape != null) {

			// executing the translate action//

			// removing the previous canvas
			// painter,需要先移除一个监听器，再添加一个，这样才不会有多个边框同时绘制的情况
			if (currentCanvasPainter != null) {

				selection.getSVGHandle().getScrollPane().getSVGCanvas()
						.removePaintListener(currentCanvasPainter, false);
			}

			currentCanvasPainter = shape.showTranslateAction(selection
					.getSVGHandle(), elements, firstPoint, currentPoint);

			if (currentCanvasPainter != null) {

				// repainting the canvas,画出移动时的线框
				selection.getSVGHandle().getScrollPane().getSVGCanvas()
						.addLayerPaintListener(SVGCanvas.DRAW_LAYER,
								currentCanvasPainter, true);
			}
		}
	}

	@Override
	public void validateTranslateAction(Set<Element> elements,
			Point2D firstPoint, Point2D currentPoint) {

		// removing the current canvas painter
		if (currentCanvasPainter != null) {

			selection.getSVGHandle().getScrollPane().getSVGCanvas()
					.removePaintListener(currentCanvasPainter, true);
		}
		Point2D fPoint;

		// getting the element that will undergo the action
		Element element = elements.iterator().next();

		// getting the shape module corresponding to this action
		AbstractShape shape = ShapeToolkit.getShapeModule(element, editor);
		Shape sh = shape.getShape(selection.getSVGHandle(), element, true);
		// if(sh == null)
		// return;
		if ((int) firstPoint.getX() == -1 && (int) firstPoint.getY() == -1) {
			fPoint = new Point(0, 0);
		}
		// add by yuxiang
		// 当拖动超出画板范围，则本次拖动无效
		Point2D currentSize = selection.getSVGHandle().getCanvas()
				.getGeometryCanvasSize();
		fPoint = firstPoint;
		// if (currentPoint.getX() < 0 || currentPoint.getY() < 0)
		// return;
		// else if (currentPoint.getX() > currentSize.getX()
		// || currentPoint.getY() > currentSize.getY())
		// return;
		// modified by wangql
		// if (sh != null) {
		Rectangle2D bound = null;
		if (sh != null)
			bound = sh.getBounds2D();
		else if (shape instanceof GroupShape) {
			bound = ((GroupShape) shape).getBounds(selection.getSVGHandle(),
					elements);
		}
		if (bound != null) {
			double xOffset = currentPoint.getX() - firstPoint.getX();
			double yOffset = currentPoint.getY() - firstPoint.getY();
			if (bound.getX() + xOffset < 0
					|| bound.getY() + yOffset < 0
					|| bound.getX() + bound.getWidth() + xOffset > currentSize
							.getX()
					|| bound.getY() + bound.getHeight() + yOffset > currentSize
							.getY()) {
				currentPoint.setLocation(firstPoint);
				return;
			}
		}
		if (shape != null) {

			// add by yux,2009.1.5
			Set<UndoRedoAction> undoRedoActionSet = new HashSet<UndoRedoAction>();
			// validating the translate action
			UndoRedoAction undoRedoAction = shape.validateTranslateAction(
					selection.getSVGHandle(), elements, fPoint, currentPoint);
			// selection.fireGraphicTranslated(elements,firstPoint,
			// currentPoint);
			if (undoRedoAction != null) {
				undoRedoActionSet.add(undoRedoAction);

				undoRedoAction = getUndoRedoAction(selection.getSVGHandle(),
						undoRedoActionSet, elements, undoRedoAction.getName());
			}

			/*
			 * add by yuxiang 拖拽时的关联关系变换
			 */

			if (undoRedoAction != null) {

				// adding the undo/redo action
				UndoRedoActionList actionlist = new UndoRedoActionList(
						undoRedoAction.getName(), false);
				actionlist.add(undoRedoAction);
				selection.getSVGHandle().getUndoRedo().addActionList(
						actionlist, true);
			}
		}
	}

	@Override
	public AbstractShape doAction(Set<Element> elements, Point2D firstPoint,
			Point2D currentPoint, SelectionItem item) {

		// getting the element that will undergo the action
		Element element = elements.iterator().next();

		// getting the shape module corresponding to this action
		AbstractShape shape = ShapeToolkit.getShapeModule(element, editor);

		// executing the action
		if (shape != null) {
			if (shape instanceof GroupShape) {
				if (element.getAttribute("type").equals("solidified")) {
					return null;
				}
			}

			// removing the previous canvas painter
			if (currentCanvasPainter != null) {

				selection.getSVGHandle().getScrollPane().getSVGCanvas()
						.removePaintListener(currentCanvasPainter, false);
			}

			currentCanvasPainter = shape.showAction(selection.getSVGHandle(),
					selection.getSelectionLevel(), elements, item, firstPoint,
					currentPoint);

			if (currentCanvasPainter != null) {

				// repainting the canvas
				selection.getSVGHandle().getScrollPane().getSVGCanvas()
						.addLayerPaintListener(SVGCanvas.DRAW_LAYER,
								currentCanvasPainter, true);
			}

		}
		return shape;
	}

	@Override
	public void validateAction(Set<Element> elements, Point2D firstPoint,
			Point2D currentPoint, SelectionItem item) {

		// removing the current canvas painter
		if (currentCanvasPainter != null) {

			selection.getSVGHandle().getScrollPane().getSVGCanvas()
					.removePaintListener(currentCanvasPainter, true);
		}

		// getting the element that will undergo the action
		Element element = elements.iterator().next();

		// getting the shape module corresponding to this action
		AbstractShape shape = ShapeToolkit.getShapeModule(element, editor);

		if (shape != null) {
			if (shape instanceof GroupShape) {
				if (element.getAttribute("type").equals("solidified")) {
					return;
				}
			}

			// add by yux,2009.1.5
			Set<UndoRedoAction> undoRedoActionSet = new HashSet<UndoRedoAction>();

			// validating the action
			UndoRedoAction undoRedoAction = shape.validateAction(selection
					.getSVGHandle(), selection.getSelectionLevel(), elements,
					item, firstPoint, currentPoint);
			if (undoRedoAction != null) {
				undoRedoActionSet.add(undoRedoAction);

				undoRedoAction = getUndoRedoAction(selection.getSVGHandle(),
						undoRedoActionSet, elements, undoRedoAction.getName());
			}
			/*
			 * add by yuxiang 对于单个图元或线条大小尺寸和角度，及时修改信息
			 */
			if (undoRedoAction != null) {

				// adding the undo/redo action
				UndoRedoActionList actionlist = new UndoRedoActionList(
						undoRedoAction.getName(), false);
				actionlist.add(undoRedoAction);
				selection.getSVGHandle().getUndoRedo().addActionList(
						actionlist, true);
			}
		}
	}

	/**
	 * returns the undo/redo action used for merging the provided undo/redo
	 * action set
	 * 
	 * @param undoRedoActionSet
	 *            the undo/redo action set
	 * @param label
	 *            the label for the undo/redo action
	 * @param elementsSet
	 *            the set of the elements that will be modified
	 * @return the undo/redo action used for merging the provided undo/redo
	 *         action set
	 */
	protected UndoRedoAction getUndoRedoAction(SVGHandle handle,
			final Set<UndoRedoAction> undoRedoActionSet,
			Set<Element> elementsSet, String label) {
		final Set<Element> elements = new HashSet<Element>(elementsSet);
		final UndoRedoAction undoRedoAction = selection.getSVGHandle()
				.getCanvas().getLpManager().validateAction(
						LinkPointManager.SYMBOL_ACTION_MODIFY, elements);
		// the execute runnable
		Runnable executeRunnable = new Runnable() {

			public void run() {

				for (UndoRedoAction theAction : undoRedoActionSet) {

					theAction.execute();
				}
				if (undoRedoAction != null)
					undoRedoAction.execute();
			}
		};

		// the undo runnable
		Runnable undoRunnable = new Runnable() {

			public void run() {

				for (UndoRedoAction theAction : undoRedoActionSet) {

					theAction.undo();
				}
				if (undoRedoAction != null)
					undoRedoAction.undo();
			}
		};

		// executing the action and creating the undo/redo action
		return ShapeToolkit.getUndoRedoAction(label, executeRunnable,
				undoRunnable, elementsSet);
	}

	public CanvasPainter getCurrentCanvasPainter() {
		return currentCanvasPainter;
	}
}
