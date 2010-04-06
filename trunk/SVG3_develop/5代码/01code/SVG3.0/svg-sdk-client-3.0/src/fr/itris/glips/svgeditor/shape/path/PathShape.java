package fr.itris.glips.svgeditor.shape.path;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.AbstractButton;
import javax.swing.JMenuItem;

import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.ext.awt.geom.ExtendedGeneralPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.function.ModuleAdapter;
import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.client.util.Utilities;
import com.nci.svg.sdk.other.NciTextPathModule;
import com.nci.svg.sdk.shape.vhpath.VHPathShape;

import fr.itris.glips.library.geom.path.Path;
import fr.itris.glips.library.geom.path.segment.ArcToSegment;
import fr.itris.glips.library.geom.path.segment.CubicToSegment;
import fr.itris.glips.library.geom.path.segment.LineToSegment;
import fr.itris.glips.library.geom.path.segment.MoveToSegment;
import fr.itris.glips.library.geom.path.segment.QuadraticToSegment;
import fr.itris.glips.svgeditor.display.canvas.CanvasPainter;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.display.selection.Selection;
import fr.itris.glips.svgeditor.display.selection.SelectionItem;
import fr.itris.glips.svgeditor.display.undoredo.UndoRedoAction;
import fr.itris.glips.svgeditor.resources.ResourcesManager;
import fr.itris.glips.svgeditor.shape.AbstractShape;
import fr.itris.glips.svgeditor.shape.ShapeToolkit;

/**
 * the superclass of all the module that handle paths
 * 
 * @author ITRIS, Jordi SUC
 */
public class PathShape extends AbstractShape {

	protected String currentAction = "LineShape";
	/**
	 * the creation mode for the module
	 */
	protected static final int CREATION_MODE = 0;

	/**
	 * the add mode for the module
	 */
	protected static final int ADD_MODE = 1;

	/**
	 * the remove mode for the module
	 */
	protected static final int REMOVE_MODE = 2;

	public static final String PATH_STYLE = "path_style";

	/**
	 * the id for the add point action
	 */

	protected static String lineActionId = "LineShape";// 直线

	protected static String curveActionId = "CurveShape";// 曲线

	protected static String foldlineActionId = "FoldLineShape";// 曲线

	protected static String addPointActionId = "AddPointPathShape";

	/**
	 * the id for the remove point action
	 */
	protected static String removePointActionId = "RemovePointPathShape";

	/**
	 * the element attributes names
	 */
	public static String dAtt = "d", transformAtt = "transform";

	/**
	 * the drawing handler
	 */
	public DrawingHandler drawingHandler = null;

	/**
	 * the undo/redo labels
	 */
	public String modifyPointUndoRedoLabel = "";

	/**
	 * the undo/redo labels
	 */
	public String addPointUndoRedoLabel = "", removePointUndoRedoLabel = "";

	/**
	 * the mode for the module
	 */
	public int moduleMode = CREATION_MODE;

	/**
	 * the items handler
	 */
	protected PathShapeItemsHandler itemsHandler;

	/**
	 * 是否是连接线
	 */
	protected boolean isConnectedPath = false;

	public static final String MODULE_ID = "948b6e74-b677-4054-b8d3-9a729f059f83";

	/**
	 * @return the currentAction
	 */
	public String getCurrentAction() {
		return currentAction;
	}

	/**
	 * @param currentAction
	 *            the currentAction to set
	 */
	public void setCurrentAction(String currentAction) {
		this.currentAction = currentAction;
	}

	/**
	 * the constructor of the class
	 * 
	 * @param editor
	 *            the editor
	 */
	public PathShape(EditorAdapter editor) {

		super(editor);
		if (this.getClass().getSuperclass().getName().equals(
				AbstractShape.class.getName())) {
			moduleUUID = MODULE_ID;
			final PathShape pathshape = this;
			drawingHandler = new DrawingHandler(this);
			Utilities.executeRunnable(new Runnable() {
				public void run() {
					shapeModuleId = "PathShape";
					handledElementTagName = "path";
					retrieveLabels();

					// creating the menu and tool handlers
					itemsHandler = new PathShapeItemsHandler(pathshape);
				}
			});
		}
	}

	@Override
	protected void retrieveLabels() {

		super.retrieveLabels();

		// getting labels
		modifyPointUndoRedoLabel = ResourcesManager.bundle
				.getString(shapeModuleId + "ModifyPointUndoRedoLabel");
	}

	@Override
	public HashMap<String, JMenuItem> getMenuItems() {

		return itemsHandler.getMenuItems();
	}

	@Override
	public HashMap<String, AbstractButton> getToolItems() {

		return itemsHandler.getTools();
	}

	@Override
	public int getLevelCount() {

		return 2;
	}

	/**
	 * sets the new module mode
	 * 
	 * @param moduleMode
	 *            a module mode
	 */
	public void setModuleMode(int moduleMode) {

		this.moduleMode = moduleMode;
	}

	@Override
	public void notifyDrawingAction(SVGHandle handle, Point2D point,
			int modifier, int type) {

		// according to the type of the event for the drawing action
		switch (type) {

		case DRAWING_MOUSE_PRESSED:

			drawingHandler.mousePressed(handle, point);
			break;

		case DRAWING_MOUSE_RELEASED:

			drawingHandler.mouseReleased(handle, point);
			break;

		case DRAWING_MOUSE_DRAGGED:

			if (currentAction.equals("CurveShape")
					|| currentAction.equals("LineShape"))
				drawingHandler.mouseDragged(handle, point);
			break;

		case DRAWING_MOUSE_MOVED:

			drawingHandler.mouseMoved(handle, point);
			break;

		case DRAWING_MOUSE_DOUBLE_CLICK:

			drawingHandler.mouseDoubleClicked(handle, point);
			resetDrawing();
			// handleShapeBusiness();
			break;

		case DRAWING_END:

			drawingHandler.reset(handle);
			break;
		}
	}

	@Override
	public void notifyItemsAction(final SVGHandle handle, SelectionItem item) {

		// getting the element whose point should be modified
		final Element element = item.getElements().iterator().next();

		// getting the initial shape
		Path initialShape = (Path) getShape(handle, element, false);

		// getting the shape that will be modified
		Path modifiedShape = new Path(initialShape);

		// the runnables used for the undo/redo action
		Runnable executeRunnable = null;
		Runnable undoRunnable = null;

		// modifying the path
		if (moduleMode == ADD_MODE) {

			modifiedShape.addSegment(item.getIndex());

		} else if (moduleMode == REMOVE_MODE) {

			modifiedShape.removeSegment(item.getIndex());
		}

		// getting the string representation of the both paths
		final String initDValue = initialShape.toString();
		final String modifiedDValue = modifiedShape.toString();

		// setting the new translation factors
		executeRunnable = new Runnable() {

			public void run() {

				element.setAttribute(dAtt, modifiedDValue);
				NciTextPathModule module = new NciTextPathModule(editor);
				module.correctTextPos(handle, element, initDValue,
						modifiedDValue);
			}
		};

		// the undo runnable
		undoRunnable = new Runnable() {

			public void run() {

				element.setAttribute(dAtt, initDValue);
				NciTextPathModule module = new NciTextPathModule(editor);
				module.correctTextPos(handle, element, modifiedDValue,
						initDValue);
			}
		};

		// creating the undo/redo action, and adding it to the undo/redo stack
		Set<Element> elements = new HashSet<Element>();
		elements.add(element);

		ShapeToolkit.addUndoRedoAction(handle, modifyPointUndoRedoLabel,
				executeRunnable, undoRunnable, elements);

		// resetDrawing();
	}

	/**
	 * creates a svg element by specifiying its bounds
	 * 
	 * @param handle
	 *            the current svg handle
	 * @param pathShape
	 *            the path shape describing the element
	 * @return the created svg element
	 */
	public Element createElement(SVGHandle handle, Shape pathShape,
			String lineID) {

		// the edited document
		Document doc = handle.getScrollPane().getSVGCanvas().getDocument();

		// creating the path
		final Element element = doc.createElementNS(doc.getDocumentElement()
				.getNamespaceURI(), handledElementTagName);
		setShape(handle, element, pathShape);
		// changed the stroke color by wangql
		// element.setAttributeNS(null, "style",
		// "fill:none;stroke:#000000;");
		element.setAttributeNS(null, "style", "fill:none;stroke:"
				+ Constants.NCI_DEFAULT_STROKE_COLOR + ";");
		// added by wangql
		if (isConnectedPath) {
			element.setAttributeNS(null, Constants.NCI_SVG_Type_Attr,
					Constants.NCI_SVG_Type_ConnectedLine_Value);
			element.setAttributeNS(null, Constants.NCI_SVG_EquipType_Name,
					Constants.NCI_SVG_Type_ConnectedLine_Name);
		}
		// added end
		// inserting the element in the document and handling the undo/redo
		// support
		element.setAttribute("id", lineID);
		if (handle.isSymbolHandle()) {
			insertShapeElement(handle, element);
		} else if (handle.getHandleType() == SVGHandle.HANDLE_TYPE_SVG) {
			Element layerElement = ((SVGOMDocument) doc)
					.getElementById(Constants.LAYER_PATH);
			if (layerElement == null) {
				layerElement = ((SVGOMDocument) doc).createElementNS(doc
						.getDocumentElement().getNamespaceURI(), "g");
				layerElement.setAttribute("id", Constants.LAYER_PATH);
				doc.getDocumentElement().appendChild(layerElement);
			}
			insertShapeElement(handle, layerElement, element);
		}

		return element;
	}

	@Override
	public void refresh(Element element) {
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

		// getting the bounds of the element.所选图元的边界
		Rectangle2D bounds = handle.getSvgElementsManager().getOutline(element)
				.getBounds2D();

		// scaling the bounds in the canvas space
		Rectangle2D scaledWholeBounds = handle.getTransformsManager()
				.getScaledRectangle(bounds, false);

		// getting the selection items according to the level type
		switch (level) {

		case Selection.SELECTION_LEVEL_1:

			items.addAll(getModifyPointsSelectionItems(handle, elements, true,
					false));
			break;

		case Selection.SELECTION_LEVEL_2:

			items.addAll(getResizeSelectionItems(handle, elements,
					scaledWholeBounds));
			break;

		case Selection.SELECTION_LEVEL_3:

			items.addAll(getRotateSelectionItems(handle, elements,
					scaledWholeBounds));
			break;

		case Selection.SELECTION_LEVEL_ITEMS_ACTION:

			items.addAll(getModifyPointsSelectionItems(handle, elements, false,
					false));
			break;

		case Selection.SELECTION_LEVEL_DRAWING:

			items.addAll(getModifyPointsSelectionItems(handle, elements, false,
					true));
			break;
		}

		return items;
	}

	@Override
	public UndoRedoAction translate(final SVGHandle handle,
			Set<Element> elementsSet, Point2D translationFactors,
			boolean refresh) {

		// getting the affine transform for the translation
		AffineTransform actionTransform = AffineTransform.getTranslateInstance(
				translationFactors.getX(), translationFactors.getY());

		return applyTransform(handle, elementsSet.iterator().next(),
				actionTransform, translateUndoRedoLabel);
	}

	@Override
	public UndoRedoAction resize(SVGHandle handle, Set<Element> elementsSet,
			AffineTransform transform) {

		Element element = elementsSet.iterator().next();

		return applyTransform(handle, element, transform, resizeUndoRedoLabel);
	}

	@Override
	public UndoRedoAction rotate(SVGHandle handle, Set<Element> elementsSet,
			Point2D centerPoint, double angle) {

		Element element = elementsSet.iterator().next();

		// getting the rotation affine transform
		AffineTransform actionTransform = AffineTransform.getRotateInstance(
				angle, centerPoint.getX(), centerPoint.getY());

		return applyTransform(handle, element, actionTransform,
				rotateUndoRedoLabel);
	}

	@Override
	public UndoRedoAction skew(SVGHandle handle, Set<Element> elementsSet,
			Point2D centerPoint, double skewFactor, boolean isHorizontal) {

		Element element = elementsSet.iterator().next();

		// getting the skew affine transform
		AffineTransform actionTransform = ShapeToolkit.getSkewAffineTransform(
				handle, element, centerPoint, skewFactor, isHorizontal);

		return applyTransform(handle, element, actionTransform,
				skewUndoRedoLabel);
	}

	/**
	 * computes the new coordinates of the element according to the transform
	 * and returns an undo/redo action
	 * 
	 * @param handle
	 *            a svg handle
	 * @param element
	 *            the element that will be transformed
	 * @param actionTransform
	 *            the transform of the action to apply
	 * @param actionUndoRedoLabel
	 *            the action undo/redo label
	 * @return an undo/redo action
	 */
	protected UndoRedoAction applyTransform(final SVGHandle handle,
			final Element element, AffineTransform actionTransform,
			String actionUndoRedoLabel) {

		UndoRedoAction undoRedoAction = null;

		// getting the untransformed shape
		Path initialPath = (Path) getShape(handle, element, false);

		// getting the shape that should be transformed
		Path transformPath = new Path(initialPath);

		// getting the element's transform
		final AffineTransform initialTransform = handle.getSvgElementsManager()
				.getTransform(element);

		// concatenating the transforms
		AffineTransform transform = new AffineTransform(initialTransform);
		transform.preConcatenate(actionTransform);

		// transforming the shape
		if (initialPath.canBeAppliedTransform()) {

			transformPath.applyTransform(transform);
			transform = null;
		}

		// getting the path attribute value
		final String dValue = transformPath.toString();
		final String initDValue = initialPath.toString();
		final AffineTransform ftransform = transform;
		// setting the new translation factors
		Runnable executeRunnable = new Runnable() {

			public void run() {

				handle.getEditor().getSvgSession().refreshHandle(element);
				element.setAttribute(dAtt, dValue);
				handle.getSvgElementsManager()
						.setTransform(element, ftransform);
				NciTextPathModule module = new NciTextPathModule(editor);
				module.correctTextPos(handle, element, initDValue, dValue);
				handle.getEditor().getSvgSession().refreshHandle(element);
			}
		};

		// the undo runnable
		Runnable undoRunnable = new Runnable() {

			public void run() {
				handle.getEditor().getSvgSession().refreshHandle(element);
				element.setAttribute(dAtt, initDValue);
				handle.getSvgElementsManager().setTransform(element,
						initialTransform);
				NciTextPathModule module = new NciTextPathModule(editor);
				module.correctTextPos(handle, element, dValue, initDValue);
				handle.getEditor().getSvgSession().refreshHandle(element);
			}
		};

		// creating the undo/redo action, and adding it to the undo/redo stack
		Set<Element> elements = new HashSet<Element>();
		elements.add(element);

		undoRedoAction = ShapeToolkit.getUndoRedoAction(actionUndoRedoLabel,
				executeRunnable, undoRunnable, elements);

		return undoRedoAction;
	}

	/**
	 * returns the modified point corresponding to the given element and
	 * parameters
	 * 
	 * @param handle
	 *            a svg handle
	 * @param element
	 *            an element
	 * @param item
	 *            a selection item
	 * @param point
	 *            the point to which the accurate should be translated
	 * @return the modified point corresponding to the given element and
	 *         parameters
	 */
	public UndoRedoAction modifyPoint(final SVGHandle handle,
			final Element element, SelectionItem item, Point2D point) {

		// getting the initial shape
		final Path initialShape = (Path) getShape(handle, element, false);

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

				element.setAttribute(dAtt, dValue);

				if (initialShape.canBeAppliedTransform()) {

					handle.getSvgElementsManager().setTransform(element, null);
				}
				NciTextPathModule module = new NciTextPathModule(editor);
				module.correctTextPos(handle, element, initdValue, dValue);
				refresh(element);
			}
		};

		// the undo runnable
		Runnable undoRunnable = new Runnable() {

			public void run() {

				element.setAttribute(dAtt, initdValue);
				NciTextPathModule module = new NciTextPathModule(editor);
				module.correctTextPos(handle, element, dValue, initdValue);
				refresh(element);
			}
		};

		// creating the undo/redo action, and adding it to the undo/redo stack
		Set<Element> elements = new HashSet<Element>();
		elements.add(element);

		UndoRedoAction undoRedoAction = ShapeToolkit.getUndoRedoAction(
				modifyPointUndoRedoLabel, executeRunnable, undoRunnable,
				elements);

		return undoRedoAction;
	}

	@Override
	public CanvasPainter showTranslateAction(SVGHandle handle,
			Set<Element> elementSet, Point2D firstPoint, Point2D currentPoint) {

		CanvasPainter canvasPainter = null;

		// getting the element that will undergo the action
		Element element = elementSet.iterator().next();

		// getting the initial shape
		Path initialShape = (Path) getShape(handle, element, true);

		if (initialShape != null) {

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
					g.setColor(strokeColor);
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

		if (level == 0) {

			// cloning the shape
			shape = getModifiedPointShape(handle, element, currentPoint, item);
			// NciTextPathModule textModule = new NciTextPathModule(editor);
			// textModule.correctTextPos(handle, element, initdValue, dValue);

		} else {

			// getting the action transform
			AffineTransform actionTransform = null;

			switch (level) {

			case 1:

				// getting the resize transform
				actionTransform = getResizeTransform(handle, element, item,
						firstPoint, currentPoint);
				break;

			case 2:

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
					actionTransform = getSkewTransform(handle, element,
							firstPoint, currentPoint, item);
				}

				break;
			}

			if (actionTransform != null) {

				// getting the initial shape
				Path path = (Path) getShape(handle, element, true);

				// transforming the shape
				shape = applyTransformToShape(handle, element, path,
						actionTransform);

				// computing the screen scaled shape
				shape = handle.getTransformsManager().getScaledShape(shape,
						false, actionTransform);
			}
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
					g.setColor(strokeColor);
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

			// executing the modify point action
			undoRedoAction = modifyPoint(handle, element, item, lastPoint);
			break;

		case 1:

			// getting the resize transform
			AffineTransform resizeTransform = getResizeTransform(handle,
					element, item, firstPoint, lastPoint);

			// executing the resize action
			undoRedoAction = resize(handle, elementSet, resizeTransform);
			break;

		case 2:

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

	/**
	 * returns the shape that has been transformed by the provided transform
	 * 
	 * @param handle
	 *            a svg handle
	 * @param element
	 *            an element
	 * @param initialPath
	 *            a path
	 * @param newTransform
	 *            the new transform
	 * @return the shape that has been transformed by the provided transform
	 */
	protected Shape applyTransformToShape(SVGHandle handle, Element element,
			Path initialPath, AffineTransform newTransform) {

		// the shape that will be returned
		Shape shape = null;

		// getting the path corresponding to the element
		Path path = new Path(initialPath);

		// getting the element's transform
		AffineTransform transform = handle.getSvgElementsManager()
				.getTransform(element);
		transform.preConcatenate(newTransform);

		if (path.canBeAppliedTransform()) {

			// applying the transform
			path.applyTransform(transform);
			shape = path;

		} else {

			// applying the transform
			shape = transform.createTransformedShape(path);
		}

		return shape;
	}

	@Override
	public void setShape(SVGHandle handle, Element element, Shape shape) {

		if (shape != null && shape instanceof ExtendedGeneralPath) {

			// creating the path object that computes the string
			// representation of the path shape
			Path path = new Path((ExtendedGeneralPath) shape);

			// checking if the path should be closed
			if (editor.getClosePathModeManager().shouldClosePath()) {

				path.closePath();
			}

			// setting the new attribute's value
			element.setAttribute(dAtt, path.toString());
		}
	}

	@Override
	public Shape getShape(SVGHandle handle, Element element, boolean isOutline) {

		// getting the value of the points attribute
		String attVal = element.getAttribute(dAtt);

		// creating the path object corresponding to this attribute value
		Path path = new Path(attVal);

		return path;
	}

	@Override
	public Shape getTransformedShape(SVGHandle handle, Element element,
			boolean isOutline) {

		Path shape = (Path) getShape(handle, element, isOutline);
		Shape theShape = shape;

		if (shape != null) {

			// getting the element's transform
			AffineTransform af = handle.getSvgElementsManager().getTransform(
					element);

			if (af != null && !af.isIdentity()) {

				if (shape.canBeAppliedTransform()) {

					shape.applyTransform(af);

				} else {

					theShape = af.createTransformedShape(shape);
				}
			}
		}

		return theShape;
	}

	/**
	 * returns the shape for which one point has been modified
	 * 
	 * @param svgHandle
	 *            a svg handle
	 * @param element
	 *            a svg element
	 * @param currentPoint
	 *            the current point of the drag action by the user
	 * @param item
	 *            the selection item
	 * @return returns the shape for which one point has been modified
	 */
	public Shape getModifiedPointShape(SVGHandle svgHandle, Element element,
			Point2D currentPoint, SelectionItem item) {

		// the shape that will be returned
		Shape shape = null;

		// the shapes
		Path initialShape = (Path) getShape(svgHandle, element, false);

		// getting the element's transform
		AffineTransform transform = svgHandle.getSvgElementsManager()
				.getTransform(element);

		// the new point that will replace the former one at the
		// index given by the selection item
		Point2D newPoint = currentPoint;

		if (initialShape.canBeAppliedTransform()) {

			// transforming the shape
			initialShape.applyTransform(transform);

		} else {

			// transforming the value for the new point
			try {
				newPoint = transform.createInverse().transform(currentPoint,
						null);
			} catch (Exception ex) {
			}
		}

		// modifying a point
		initialShape.modifyPoint(newPoint, item.getIndex());

		if (!initialShape.canBeAppliedTransform()) {

			// transforming the transformed shape with the element's transform
			shape = transform.createTransformedShape(initialShape);

		} else {

			shape = initialShape;
		}

		// transforming the shape so that it displays correctly with the
		// document transforms
		shape = svgHandle.getTransformsManager().getScaledShape(shape, false);

		return shape;
	}

	/**
	 * returns the set of the selection items corresponding to a modify points
	 * action selection level and for the elements
	 * 
	 * @param handle
	 *            a svg handle
	 * @param elements
	 *            a set of elements
	 * @param addControlPoints
	 *            whether the selection items for the control points should be
	 *            added
	 * @param onlyExtremities
	 *            whether only the extremities should appear
	 * @return the set of the selection items corresponding to a modify points
	 *         action selection level and for the elements
	 */
	public Set<SelectionItem> getModifyPointsSelectionItems(SVGHandle handle,
			Set<Element> elements, boolean addControlPoints,
			boolean onlyExtremities) {

		// the set of the selection items that will be returned
		Set<SelectionItem> items = new HashSet<SelectionItem>();

		// getting the element
		Element element = elements.iterator().next();

		// getting the shape corresponding to the element
		Path shape = (Path) getShape(handle, element, false);

		// getting the element's transform
		AffineTransform transform = handle.getSvgElementsManager()
				.getTransform(element);

		if (shape.canBeAppliedTransform()) {

			// applies the transform to the shape, if it's possible
			shape.applyTransform(transform);
			transform = null;
		}

		// creating the items
		fr.itris.glips.library.geom.path.segment.Segment segment = shape
				.getSegment();
		Point2D startPoint = null;
		Point2D endPoint = null;
		Point2D ctrlPoint1, ctrlPoint2;
		int i = 0;
		SelectionItem item;
		// 如果有“段“存在，则将”段“所在区域划成圆圈
		while (segment != null) {

			if (!onlyExtremities || i == 0
					|| (i == shape.getSegmentsNumber() - 1)) {

				if (segment instanceof MoveToSegment
						|| segment instanceof LineToSegment
						|| segment instanceof ArcToSegment) {

					endPoint = segment.getEndPoint();
					endPoint = handle.getTransformsManager().getScaledPoint(
							endPoint, false, transform);

					items.add(new SelectionItem(handle, elements, endPoint,
							SelectionItem.POINT, SelectionItem.POINT_STYLE,
							i * 10, false, null));

				} else if (segment instanceof QuadraticToSegment) {

					ctrlPoint1 = segment.getControlPoint();
					ctrlPoint1 = handle.getTransformsManager().getScaledPoint(
							ctrlPoint1, false, transform);
					endPoint = segment.getEndPoint();
					endPoint = handle.getTransformsManager().getScaledPoint(
							endPoint, false, transform);

					if (addControlPoints) {

						item = new SelectionItem(handle, elements, ctrlPoint1,
								SelectionItem.CONTROL_POINT,
								SelectionItem.CENTER_POINT_STYLE, i * 10,
								false, startPoint);
						items.add(item);
					}

					item = new SelectionItem(handle, elements, endPoint,
							SelectionItem.POINT, SelectionItem.POINT_STYLE,
							i * 10 + 1, false, null);
					items.add(item);

				} else if (segment instanceof CubicToSegment) {

					ctrlPoint1 = segment.getOtherControlPoint();
					ctrlPoint1 = handle.getTransformsManager().getScaledPoint(
							ctrlPoint1, false, transform);
					ctrlPoint2 = segment.getControlPoint();
					ctrlPoint2 = handle.getTransformsManager().getScaledPoint(
							ctrlPoint2, false, transform);
					endPoint = segment.getEndPoint();
					endPoint = handle.getTransformsManager().getScaledPoint(
							endPoint, false, transform);

					if (addControlPoints) {

						items.add(new SelectionItem(handle, elements,
								ctrlPoint1, SelectionItem.CONTROL_POINT,
								SelectionItem.CENTER_POINT_STYLE, i * 10,
								false, startPoint));
						items.add(new SelectionItem(handle, elements,
								ctrlPoint2, SelectionItem.CONTROL_POINT,
								SelectionItem.CENTER_POINT_STYLE, i * 10 + 1,
								false, endPoint));
					}

					items.add(new SelectionItem(handle, elements, endPoint,
							SelectionItem.POINT, SelectionItem.POINT_STYLE,
							i * 10 + 2, false, null));
				}
			}

			i++;
			startPoint = endPoint;
			segment = segment.getNextSegment();
		}

		return items;
	}

	/**
	 * 设置当前path是否是连接线
	 * 
	 * @param flag
	 *            true表示连接线。false表示不是连接线
	 */
	public void enableConnectedPath(boolean flag) {
		isConnectedPath = flag;
	}

	Map<ModuleAdapter, ConnectedListener> connectedListeners = new ConcurrentHashMap<ModuleAdapter, ConnectedListener>();

	public void addConnectedListener(ModuleAdapter module, ConnectedListener lis) {
		if (connectedListeners.containsKey(module)) {
			new Exception("一个模块只允许一个连接线监听").printStackTrace();
			return;
		}
		connectedListeners.put(module, lis);
	}

	public void fireConnectedListeners(Element pathele) {
		Iterator<ModuleAdapter> modules = connectedListeners.keySet()
				.iterator();

		while (modules.hasNext()) {
			ModuleAdapter module = modules.next();
			if (currentWorkingModule.equals(module)) {
				ConnectedListener lis = connectedListeners.get(module);
				lis.notifyConnected(pathele);
				break;
			}
		}
	}

	public boolean isElementTypeSupported(Element element) {
		return (element != null
				&& handledElementTagName.equals(element.getNodeName())
				&& element.getParentNode() != null && ((Element) element
				.getParentNode()).getAttribute("group_style").equals(""));
	}

	/**
	 * 当前画图所起始的模块
	 */
	private ModuleAdapter currentWorkingModule = null;

	/**
	 * 获取当前pathshape工作的模块
	 * 
	 * @return
	 */
	public ModuleAdapter getCurrentWorkingModule() {
		return currentWorkingModule;
	}
	
	public boolean isNodeConnectable(){
		return true;
	}

	/**
	 * 设置当前pathshape工作的模块
	 * 
	 * @param currentWorkingModule
	 */
	public void setCurrentWorkingModule(ModuleAdapter currentWorkingModule) {
		this.currentWorkingModule = currentWorkingModule;
	}

	public DrawingHandler getDrawingHandler() {
		return drawingHandler;
	}

	public void setDrawingHandler(DrawingHandler drawingHandler) {
		this.drawingHandler = drawingHandler;
	}
	
	

}
