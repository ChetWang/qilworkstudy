package fr.itris.glips.svgeditor.shape.text;

import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JDialog;

import org.apache.batik.dom.svg.SVGOMDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.client.util.EditorToolkit;
import com.nci.svg.sdk.client.util.Utilities;

import fr.itris.glips.svgeditor.ColorManager;
import fr.itris.glips.svgeditor.display.canvas.CanvasPainter;
import fr.itris.glips.svgeditor.display.canvas.dom.SVGDOMNormalizer;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.display.selection.Selection;
import fr.itris.glips.svgeditor.display.selection.SelectionItem;
import fr.itris.glips.svgeditor.display.undoredo.UndoRedoAction;
import fr.itris.glips.svgeditor.shape.AbstractShape;
import fr.itris.glips.svgeditor.shape.ShapeToolkit;

/**
 * the class of the module that handle text shapes
 * 
 * @author ITRIS, Jordi SUC
 */
public class TextShape extends AbstractShape {

	/**
	 * the element attributes names
	 */
	protected static String xAtt = "x", yAtt = "y";

	/**
	 * the dialog used to select a text
	 */
	private TextDialog textDialog;

	/**
	 * the point selected for the drawing action
	 */
	private Point2D drawingPoint;

	public static final int FONT_SIZE = 12;

	/**
	 * the constructor of the class
	 * 
	 * @param editor
	 *            the editor
	 */
	public TextShape(EditorAdapter editor) {

		super(editor);
		Utilities.executeRunnable(new Runnable() {
			public void run() {
				initTextShape();
			}
		});
	}

	private void initTextShape() {
		shapeModuleId = "TextShape";
		handledElementTagName = "text";
		retrieveLabels();
		createMenuAndToolItems();

		// creating the text dialog
		if (editor.getParent() instanceof JDialog) {

			textDialog = new TextDialog(this, (JDialog) editor.getParent(),
					editor);

		} else if (editor.getParent() instanceof Frame) {

			textDialog = new TextDialog(this, (Frame) editor.getParent(),
					editor);
		}
	}

	@Override
	public int getLevelCount() {

		return 1;
	}

	@Override
	public void refresh(Element element) {
	}

	@Override
	public void notifyDrawingAction(SVGHandle handle, Point2D point,
			int modifier, int type) {

		// according to the type of the event for the drawing action
		switch (type) {
		case DRAWING_MOUSE_RELEASED: {

			// scaling the two points to fit a 1.1 zoom factor
			Point2D scaledPoint = handle.getTransformsManager().getScaledPoint(
					point, true);
			this.drawingPoint = scaledPoint;
			handle.getSelection().setBTextDialog(true);
			textDialog.showDialog(handle.getSVGFrame(), handle);
			resetDrawing();
			editor.getSvgSession().refreshCurrentHandleImediately();
			break;
		}
		}
	}

	/**
	 * creates a svg element by specifiying its parameters
	 * 
	 * @param handle
	 *            the current svg handle
	 * @param text
	 *            the text for the new element
	 * @return the created svg element
	 */
	public Element createElement(SVGHandle handle, String text) {

		// the edited document
		Document doc = handle.getScrollPane().getSVGCanvas().getDocument();

		// creating the text element
		final Element element = doc.createElementNS(doc.getDocumentElement()
				.getNamespaceURI(), handledElementTagName);

		// getting the last color that has been used by the user
		String colorString = editor.getColorChooser().getColorString(
				ColorManager.getCurrentColor());
		// element.setAttributeNS(null,"fill", colorString);
		// element.setAttributeNS(null, "style", "fill:" + colorString
		// + ";stroke:none;");
		String style = "font-size:" + FONT_SIZE + "pt;fill:" + colorString
				+ ";";
		element.setAttributeNS(null, "style", style);

//		EditorToolkit.setAttributeValue(element, xAtt, drawingPoint.getX());
//		EditorToolkit.setAttributeValue(element, yAtt, drawingPoint.getY());

		String[] s = text.split("\n");
		// creating the text node
		if (s.length == -1) {
			Text textValue = doc.createTextNode(text);
			element.appendChild(textValue);
		} else {
			int startY = (int) drawingPoint.getY();
			for (int i = 0; i < s.length; i++) {
				Element tspan = doc.createElementNS(doc.getDocumentElement()
						.getNamespaceURI(), SVGDOMNormalizer.tspanTagName);

				// Element tspan =
				// doc.createElement(SVGDOMNormalizer.tspanTagName);
				tspan.setAttribute("x", String.valueOf(drawingPoint.getX()));
				tspan.setAttribute("y", String.valueOf(startY));
				tspan.setAttributeNS(null, "style", style);
				Text textValue = doc.createTextNode(s[i]);
				tspan.appendChild(textValue);
				element.appendChild(tspan);
				startY = startY + 17;
			}
		}

		// inserting the element in the document and handling the undo/redo
		// support
//		if (handle.getHandleType() == SVGHandle.HANDLE_TYPE_SYMBOL_GRAPH_UNIT_NORMAL
//				|| handle.getHandleType() == SVGHandle.HANDLE_TYPE_SYMBOL_TEMPLATE) {
//			insertShapeElement(handle, element);
//		} else if (handle.getHandleType() == SVGHandle.HANDLE_TYPE_SVG) {
//			Element layerElement = ((SVGOMDocument) doc)
//					.getElementById(Constants.LAYER_TEXT);
//			if (layerElement == null) {
//				layerElement = ((SVGOMDocument) doc).createElementNS(doc
//						.getDocumentElement().getNamespaceURI(), "g");
//				layerElement.setAttribute("id", Constants.LAYER_TEXT);
//				doc.getDocumentElement().appendChild(layerElement);
//			}
////			insertShapeElement(handle, layerElement, element);
//		}
		insertShapeElement(handle,element);
		handle.getSelection().handleSelection(element, false, true);
		// Set<Area> areas = new HashSet<Area>();
		// Area a = new
		// Area(handle.getSvgElementsManager().getNodeBounds(element));
		// areas.add(a);
		// handle.getCanvas().refreshCanvasContent(false, true, areas);
		return element;
	}

	@Override
	public Set<SelectionItem> getSelectionItems(SVGHandle handle,
			Set<Element> elements, int level) {

		// clearing the stored values
		rotationSkewCenterPoint = null;

		// getting the first element of this set
		Element element = elements.iterator().next();

		// the set of the items that will be returned
		Set<SelectionItem> items = new HashSet<SelectionItem>();

		// getting the bounds of the element
		Rectangle2D bounds = getTransformedShape(handle, element, false)
				.getBounds2D();

		// scaling the bounds in the canvas space
		Rectangle2D scaledWholeBounds = handle.getTransformsManager()
				.getScaledRectangle(bounds, false);

		// getting the selection items according to the level type
		switch (level) {

		case Selection.SELECTION_LEVEL_DRAWING:
		case Selection.SELECTION_LEVEL_1:

			items.addAll(getResizeSelectionItems(handle, elements,
					scaledWholeBounds));
			break;

		case Selection.SELECTION_LEVEL_2:

			items.addAll(getRotateSelectionItems(handle, elements,
					scaledWholeBounds));
			break;
		}

		return items;
	}

	@Override
	public UndoRedoAction translate(final SVGHandle handle,
			Set<Element> elementsSet, Point2D translationFactors,
			boolean refresh) {

		// getting the translation transform
		AffineTransform transform = AffineTransform.getTranslateInstance(
				translationFactors.getX(), translationFactors.getY());

		return applyTransform(handle, elementsSet.iterator().next(), transform,
				translateUndoRedoLabel, refresh);
	}

	@Override
	public UndoRedoAction resize(SVGHandle handle, Set<Element> elementsSet,
			AffineTransform transform) {

		return applyTransform(handle, elementsSet.iterator().next(), transform,
				resizeUndoRedoLabel, true);
	}

	@Override
	public UndoRedoAction rotate(SVGHandle handle, Set<Element> elementsSet,
			Point2D centerPoint, double angle) {

		// getting the rotation affine transform
		AffineTransform actionTransform = AffineTransform.getRotateInstance(
				angle, centerPoint.getX(), centerPoint.getY());

		return applyTransform(handle, elementsSet.iterator().next(),
				actionTransform, rotateUndoRedoLabel, true);
	}

	@Override
	public UndoRedoAction skew(SVGHandle handle, Set<Element> elementsSet,
			Point2D centerPoint, double skewFactor, boolean isHorizontal) {

		Element element = elementsSet.iterator().next();

		// getting the skew affine transform
		AffineTransform actionTransform = ShapeToolkit.getSkewAffineTransform(
				handle, element, centerPoint, skewFactor, isHorizontal);

		return applyTransform(handle, element, actionTransform,
				skewUndoRedoLabel, true);
	}

	/**
	 * computes the new coordinates of the element according to the transform a
	 * returns an undo/redo action
	 * 
	 * @param handle
	 *            a svg handle
	 * @param element
	 *            the element that will be transformed
	 * @param transform
	 *            the transform to apply
	 * @param actionUndoRedoLabel
	 *            the action undo/redo label
	 * @return an undo/redo action
	 */
	protected UndoRedoAction applyTransform(final SVGHandle handle,
			final Element element, AffineTransform transform,
			String actionUndoRedoLabel, final boolean refresh) {

		// getting the initial transform
		final AffineTransform initialTransform = handle.getSvgElementsManager()
				.getTransform(element);

		// getting the new transform
		final AffineTransform newTransform = new AffineTransform(
				initialTransform);
		newTransform.preConcatenate(transform);

		// setting the new x and y attributes for the elements
		Runnable executeRunnable = new Runnable() {

			public void run() {
				if (refresh)
					editor.getSvgSession().refreshHandle(element);
				handle.getSvgElementsManager().setTransform(element,
						newTransform);
				if (refresh)
					editor.getSvgSession().refreshHandle(element);
			}
		};

		// the undo runnable
		Runnable undoRunnable = new Runnable() {

			public void run() {
				if (refresh)
					editor.getSvgSession().refreshHandle(element);
				handle.getSvgElementsManager().setTransform(element,
						initialTransform);
				if (refresh)
					editor.getSvgSession().refreshHandle(element);
			}
		};

		// executing the action and creating the undo/redo action
		HashSet<Element> elements = new HashSet<Element>();
		elements.add(element);
		UndoRedoAction undoRedoAction = ShapeToolkit.getUndoRedoAction(
				actionUndoRedoLabel, executeRunnable, undoRunnable, elements);

		return undoRedoAction;
	}

	@Override
	public CanvasPainter showTranslateAction(SVGHandle handle,
			Set<Element> elementSet, Point2D firstPoint, Point2D currentPoint) {

		CanvasPainter canvasPainter = null;

		// getting the element that will undergo the action
		Element element = elementSet.iterator().next();

		// getting the initial line
		Shape shape = getTransformedShape(handle, element, true);

		if (shape != null) {

			Point2D translationCoefficients = new Point2D.Double(currentPoint
					.getX()
					- firstPoint.getX(), currentPoint.getY()
					- firstPoint.getY());

			AffineTransform transform = AffineTransform.getTranslateInstance(
					translationCoefficients.getX(), translationCoefficients
							.getY());

			// computing the screen scaled shape
			final Shape fshape = handle.getTransformsManager().getScaledShape(
					shape, false, transform);

			// creating the set of the clips
			final Set<Rectangle2D> fclips = new HashSet<Rectangle2D>();
			fclips.add(fshape.getBounds2D());

			canvasPainter = new CanvasPainter() {

				@Override
				public void paintToBeDone(Graphics2D g) {

					g = (Graphics2D) g.create();
					g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
							RenderingHints.VALUE_ANTIALIAS_ON);
					g.setColor(strokeColor);
					g.draw(fshape);
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

	@Override
	public UndoRedoAction validateTranslateAction(SVGHandle handle,
			Set<Element> elementSet, Point2D firstPoint, Point2D currentPoint) {

		return translate(handle, elementSet, new Point2D.Double(currentPoint
				.getX()
				- firstPoint.getX(), currentPoint.getY() - firstPoint.getY()),
				true);
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
		Shape shape = null;

		// getting the action transform
		AffineTransform actionTransform = null;

		switch (level) {

		case 0:

			// getting the resize transform
			actionTransform = getResizeTransform(handle, element, item,
					firstPoint, currentPoint);
			break;

		case 1:

			if (item.getType() == SelectionItem.CENTER) {

				// storing the center point for the rotate action
				rotationSkewSelectionItemCenterPoint = currentPoint;
				item.setPoint(currentPoint);
				canPaintShape = false;

			} else if (item.getType() == SelectionItem.NORTH_WEST
					|| item.getType() == SelectionItem.NORTH_EAST
					|| item.getType() == SelectionItem.SOUTH_EAST
					|| item.getType() == SelectionItem.SOUTH_WEST) {

				// getting the rotation transform
				actionTransform = getRotationTransform(handle, element,
						firstPoint, currentPoint);

			} else {

				// getting the skew transform
				actionTransform = getSkewTransform(handle, element, firstPoint,
						currentPoint, item);
			}

			break;
		}

		if (actionTransform != null) {

			// getting the initial shape
			shape = getShape(handle, element, true);

			// getting the element's transform
			AffineTransform transform = handle.getSvgElementsManager()
					.getTransform(element);

			// concatenating the action transform to the element's transform
			transform.preConcatenate(actionTransform);

			// computing the screen scaled shape
			shape = handle.getTransformsManager().getScaledShape(shape, false,
					transform);
		}

		if (canPaintShape && shape != null) {

			final Shape fshape = shape;

			// creating the set of the clips
			final HashSet<Rectangle2D> fclips = new HashSet<Rectangle2D>();
			fclips.add(fshape.getBounds2D());

			painter = new CanvasPainter() {

				@Override
				public void paintToBeDone(Graphics2D g) {

					g = (Graphics2D) g.create();
					g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
							RenderingHints.VALUE_ANTIALIAS_ON);
					g.setColor(strokeColor);
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
	public UndoRedoAction validateAction(SVGHandle handle, int level,
			Set<Element> elementSet, SelectionItem item, Point2D firstPoint,
			Point2D lastPoint) {

		// the undo/redo action that will be returned
		UndoRedoAction undoRedoAction = null;

		// getting the element that will undergo the action
		Element element = elementSet.iterator().next();

		// executing the accurate action
		switch (level) {

		case 0:

			// getting the resize transform
			AffineTransform resizeTransform = getResizeTransform(handle,
					element, item, firstPoint, lastPoint);

			// executing the resize action
			undoRedoAction = resize(handle, elementSet, resizeTransform);
			break;

		case 1:

			if (item.getType() != SelectionItem.CENTER) {

				// getting the center point
				Point2D centerPoint = getRotationSkewCenterPoint(handle,
						element);

				if (item.getType() == SelectionItem.NORTH_WEST
						|| item.getType() == SelectionItem.NORTH_EAST
						|| item.getType() == SelectionItem.SOUTH_EAST
						|| item.getType() == SelectionItem.SOUTH_WEST) {

					// getting the angle for the rotation
					double angle = ShapeToolkit.getRotationAngle(centerPoint,
							firstPoint, lastPoint);

					// executing the rotation action
					undoRedoAction = rotate(handle, elementSet, centerPoint,
							angle);

				} else {

					// getting the skew factor and whether it's horizontal or
					// not
					boolean isHorizontal = (item.getType() == SelectionItem.NORTH || item
							.getType() == SelectionItem.SOUTH);
					double skewFactor = 0;

					if (isHorizontal) {

						skewFactor = lastPoint.getX() - firstPoint.getX();

					} else {

						skewFactor = lastPoint.getY() - firstPoint.getY();
					}

					// executing the skew action
					undoRedoAction = skew(handle, elementSet, centerPoint,
							skewFactor, isHorizontal);
				}

				rotationSkewSelectionItemCenterPoint = null;

			} else {

				rotationSkewCenterPoint = rotationSkewSelectionItemCenterPoint;
				rotationSkewSelectionItemCenterPoint = null;
			}

			break;
		}

		return undoRedoAction;
	}

	@Override
	public Shape getShape(SVGHandle handle, Element element, boolean isOutline) {

		Shape shape = handle.getSvgElementsManager()
				.getGeometryOutline(element);

		if (shape == null || shape.getBounds().getWidth() == 0
				|| shape.getBounds().getHeight() == 0) {

			// getting the location of the text
			double x = EditorToolkit.getAttributeValue(element, xAtt);
			double y = EditorToolkit.getAttributeValue(element, yAtt);

			shape = new Rectangle2D.Double(x, y, 1, 1);
		}

		return shape;
	}

	@Override
	public void setShape(SVGHandle handle, Element element, Shape shape) {
	}

	public TextDialog getTextDialog() {
		return textDialog;
	}

	@Override
	public Shape getTransformedShape(SVGHandle handle, Element element,
			boolean isOutline) {

		Shape shape = getShape(handle, element, isOutline);

		if (shape != null) {

			// getting the transformed points
			AffineTransform af = handle.getSvgElementsManager().getTransform(
					element);

			if (af != null && !af.isIdentity()) {

				shape = af.createTransformedShape(shape);
			}
		}

		return shape;
	}

	public String getName() {
		return shapeModuleId;
	}

	public Point2D getDrawingPoint() {
		return drawingPoint;
	}

	public void setDrawingPoint(Point2D drawingPoint) {
		this.drawingPoint = drawingPoint;
	}
}
