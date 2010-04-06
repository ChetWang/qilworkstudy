package com.nci.svg.sdk.shape.vhpath;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Element;

import com.nci.svg.sdk.other.NciTextPathModule;

import fr.itris.glips.library.geom.path.Path;
import fr.itris.glips.svgeditor.display.canvas.CanvasPainter;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.display.selection.SelectionItem;
import fr.itris.glips.svgeditor.display.undoredo.UndoRedoAction;
import fr.itris.glips.svgeditor.shape.ShapeToolkit;

public class DefaultVHTranslationHandler implements VHPathTranslationHandler {

	protected VHPathShape vhPathShape;

	public DefaultVHTranslationHandler(VHPathShape vhPathShape) {
		this.vhPathShape = vhPathShape;
	}

	@Override
	public UndoRedoAction modifyPoint(final SVGHandle handle,
			final Element element, final SelectionItem item, Point2D point) {
		// getting the initial shape
		final Path initialShape = (Path) vhPathShape.getShape(handle, element,
				false);
		if (initialShape.getBounds2D().getWidth() == 0) {
			point.setLocation(initialShape.getBounds2D().getX(), point.getY());
		} else if (initialShape.getBounds2D().getHeight() == 0) {
			point.setLocation(point.getX(), initialShape.getBounds2D().getY());
		}
		// getting the element's transform
		AffineTransform transform = handle.getSvgElementsManager()
				.getTransform(element);

		if (!transform.isIdentity()) {

			if (initialShape.canBeAppliedTransform()) {

				initialShape.applyTransform(transform);
			}

			// transforming the point so that the point
			// can directy modify a path point that has a transform applied to
			// it
			try {
				point = transform.createInverse().transform(point, null);
			} catch (Exception ex) {
			}
		}

		// getting the shape that will be modified
		final Path transformedShape = new Path(initialShape);
		transformedShape.modifyPoint(point, item.getIndex());

		// getting the value of the path attribute
		final String initdValue = initialShape.toString();
		final String dValue = transformedShape.toString();

		// setting the new shape
		Runnable executeRunnable = new Runnable() {

			public void run() {

				element.setAttribute(VHPathShape.dAtt, dValue);

				if (initialShape.canBeAppliedTransform()) {

					handle.getSvgElementsManager().setTransform(element, null);
				}
				NciTextPathModule module = new NciTextPathModule(vhPathShape
						.getEditor());
				module.correctTextPos(handle, element, initdValue, dValue);
				vhPathShape.refresh(element);
			}
		};

		// the undo runnable
		Runnable undoRunnable = new Runnable() {

			public void run() {

				element.setAttribute(VHPathShape.dAtt, initdValue);
				NciTextPathModule module = new NciTextPathModule(vhPathShape
						.getEditor());
				module.correctTextPos(handle, element, dValue, initdValue);
				vhPathShape.refresh(element);
			}
		};

		// creating the undo/redo action, and adding it to the undo/redo stack
		Set<Element> elements = new HashSet<Element>();
		elements.add(element);

		UndoRedoAction undoRedoAction = ShapeToolkit.getUndoRedoAction(
				vhPathShape.modifyPointUndoRedoLabel, executeRunnable,
				undoRunnable, elements);

		return undoRedoAction;
	}

	@Override
	public CanvasPainter showAction(SVGHandle handle, int level,
			Set<Element> elementSet, SelectionItem item, Point2D firstPoint,
			Point2D currentPoint) {
		// getting the element that will undergo the action
		Element element = elementSet.iterator().next();

		// the canvas painter that should be returned
		CanvasPainter painter = null;

		// whether the shape should be painted
		boolean canPaintShape = true;

		// the shape that will be painted
		Shape shape = vhPathShape.getModifiedPointShape(handle, element,
				firstPoint, currentPoint, item);

		if (canPaintShape && shape != null) {

			final Shape fshape = shape;

			// creating the set of the clips
			final HashSet<Rectangle2D> fclips = new HashSet<Rectangle2D>();
			fclips.add(fshape.getBounds2D());

			painter = new CanvasPainter() {

				@Override
				public void paintToBeDone(Graphics2D g) {

					g = (Graphics2D) g.create();
					g.setColor(VHPathShape.strokeColor);
					g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
							RenderingHints.VALUE_ANTIALIAS_ON);
					g.draw(fshape);
					g.dispose();
				}

				@Override
				public Set<Rectangle2D> getClip() {

					return fclips;
				}
			};

		}

		return painter;
	}

	@Override
	public CanvasPainter showTranslateAction(SVGHandle handle,
			Set<Element> elementSet, Point2D firstPoint, Point2D currentPoint) {
		CanvasPainter canvasPainter = null;

		// getting the element that will undergo the action
		Element element = elementSet.iterator().next();

		// getting the initial shape
		Path initialShape = (Path) vhPathShape.getShape(handle, element, true);

		if (initialShape != null) {
			currentPoint = vhPathShape.computeVHCurrentPoint(initialShape,
					firstPoint, currentPoint, true);
			// getting the translation coefficients
			Point2D translationCoefficients = new Point2D.Double(currentPoint
					.getX()
					- firstPoint.getX(), currentPoint.getY()
					- firstPoint.getY());

			// creating the transform corresponding to this translation
			AffineTransform translationTransform = AffineTransform
					.getTranslateInstance(translationCoefficients.getX(),
							translationCoefficients.getY());

			// getting the transformed shape
			Shape shape = translationTransform
					.createTransformedShape(initialShape);

			// computing the screen scaled shape
			final Shape endShape = handle.getTransformsManager()
					.getScaledShape(shape, false);

			// creating the set of the clips
			final Set<Rectangle2D> fclips = new HashSet<Rectangle2D>();
			fclips.add(endShape.getBounds2D());

			canvasPainter = new CanvasPainter() {

				@Override
				public void paintToBeDone(Graphics2D g) {

					g = (Graphics2D) g.create();
					g.setColor(VHPathShape.strokeColor);
					g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
							RenderingHints.VALUE_ANTIALIAS_ON);
					g.draw(endShape);
					g.dispose();
				}

				@Override
				public Set<Rectangle2D> getClip() {

					return fclips;
				}
			};
		}

		return canvasPainter;
	}

	public VHPathShape getVhPathShape() {
		return vhPathShape;
	}

	@Override
	public UndoRedoAction validateTranslateAction(SVGHandle handle,
			Set<Element> elementSet, Point2D firstPoint, Point2D currentPoint) {
		Element e = elementSet.iterator().next();
		Path initialShape = (Path) vhPathShape.getShape(handle, e, true);
		currentPoint = vhPathShape.computeVHCurrentPoint(initialShape,
				firstPoint, currentPoint, true);
		return vhPathShape.translate(handle, elementSet, new Point2D.Double(
				currentPoint.getX() - firstPoint.getX(), currentPoint.getY()
						- firstPoint.getY()), true);
	}

}
