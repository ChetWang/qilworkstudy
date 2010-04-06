package com.nci.svg.sdk.shape.vhpath;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.swing.AbstractButton;
import javax.swing.JMenuItem;

import org.apache.batik.ext.awt.geom.ExtendedGeneralPath;
import org.jdesktop.swingworker.SwingWorker;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.selection.GraphicTranslationListener;
import com.nci.svg.sdk.client.selection.ModeSelectionEvent;
import com.nci.svg.sdk.client.selection.ModeSelectionListener;
import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.client.util.EditorToolkit;
import com.nci.svg.sdk.client.util.Utilities;
import com.nci.svg.sdk.other.NciTextPathModule;
import com.nci.svg.sdk.svgeditor.selection.DrawingShapeChangeListener;

import fr.itris.glips.library.geom.path.Path;
import fr.itris.glips.library.geom.path.segment.Segment;
import fr.itris.glips.svgeditor.display.canvas.CanvasPainter;
import fr.itris.glips.svgeditor.display.handle.HandlesListener;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.display.selection.SelectionItem;
import fr.itris.glips.svgeditor.display.selection.SingleSelectionManager;
import fr.itris.glips.svgeditor.display.undoredo.UndoRedoAction;
import fr.itris.glips.svgeditor.display.undoredo.UndoRedoActionList;
import fr.itris.glips.svgeditor.shape.AbstractShape;
import fr.itris.glips.svgeditor.shape.ShapeToolkit;
import fr.itris.glips.svgeditor.shape.path.PathShape;

/**
 * 仅支持垂直、水平绘制的path
 * 
 * @author qi
 * 
 */
public class VHPathShape extends PathShape {

	public static final String MODULE_ID = "b2f4760f-a865-4520-b84f-f78529a5ba05";

	public static final String VH_STYPE = "nci_vh";

	public static final String GROUP_STYLE = "group_style";

	public static final String VH_MODEL = "VH连接线";

	private VHPathTranslationHandler translationHandler = new DefaultVHTranslationHandler(
			this);

	public VHPathShape(EditorAdapter editor) {
		super(editor);
		moduleUUID = MODULE_ID;
		drawingHandler = new VHDrawingHandler(this);
		shapeModuleId = "VHPathShape";
		handledElementTagName = "path";
		final GraphicTranslationListener transListener = new GraphicTranslationListener() {
			@Override
			public void graphicsTranslated(Set<Element> elementsTraslated,
					Point2D firstPoint, Point2D scaledPoint) {
				translated(elementsTraslated);
			}

			@Override
			public void graphicsTranslating(Set<Element> elementsTraslating,
					Point2D firstPoint, Point2D scaledPoint) {
				translating(elementsTraslating);
			}
		};
		final ModeSelectionListener mouseDoubleClickListener = new ModeSelectionListener() {

			@Override
			public void modeSelected(ModeSelectionEvent evt) {
				pathDoubleClicked(evt);
			}

		};
		final DrawingShapeChangeListener drawingShapeChangeListener = new DrawingShapeChangeListener() {

			@Override
			public void drawingShapeChanged(AbstractShape previousShape,
					AbstractShape currentShape) {
				drawingShapeChange(previousShape, currentShape);
			}
		};
		editor.getHandlesManager().addHandlesListener(new HandlesListener() {
			public void handleCreated(SVGHandle currentHandle) {
				currentHandle.getSelection().addGraphicTranslationListener(
						transListener);
				currentHandle.getSelection().addMouseClickedListener(
						mouseDoubleClickListener);
				currentHandle.getEditor().getSelectionManager()
						.addDrawingShapeChangeListener(
								drawingShapeChangeListener);
			}
		});
	}

	protected void drawingShapeChange(AbstractShape previousShape,
			AbstractShape currentShape) {
		SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
		drawingHandler.reset(handle);
	}

	/**
	 * 双击线路，以便选择所有相关path
	 * 
	 * @param evt
	 */
	private void pathDoubleClicked(ModeSelectionEvent evt) {
		if (evt.getSource().getMouseEvent().getClickCount() == 2) {
			Element e = evt.getSource().getElementAtMousePoint();
			if (EditorToolkit.isVHPath(e)) {
				NodeList paths = e.getParentNode().getChildNodes();
				Set<Element> set = new HashSet<Element>();
				for (int i = 0; i < paths.getLength(); i++) {
					Node node = paths.item(i);
					if (node instanceof Element)
						set.add((Element) node);
				}
				evt.getSource().getHandle().getSelection()
						.handleSelectionAlwaysSingle(set, true);
			}
		}

	}

	/**
	 * 内部连接线及图元的平移，应该能使相邻的其它线路也作平移或延伸
	 * 
	 * @param elementTraslated
	 */
	public void translated(final Set<Element> elementTraslated) {
		new SwingWorker() {

			@Override
			protected Object doInBackground() throws Exception {
				if (elementTraslated != null && elementTraslated.size() > 0) {
					for (Element e : elementTraslated) {
						if (EditorToolkit.isVHPath(e)) {// 如果移动的是连接线
							Element previous = (Element) e.getPreviousSibling();
							if (previous != null
									&& !elementTraslated.contains(previous)) {
								translateCorrelate(e, previous, true);
							}
							Element next = (Element) e.getNextSibling();
							if (next != null
									&& !elementTraslated.contains(next)) {
								translateCorrelate(e, next, false);
							}
						}
					}
				}
				return null;
			}

			public void done() {
				editor.getSvgSession().refreshCurrentHandleImediately();
			}

		}.execute();

	}

	public void translating(Set<Element> elementTraslating) {
		if (elementTraslating.size() == 1) {
			Element e = elementTraslating.iterator().next();
			if (e.getNodeName().equals("use")) {// 如果移动的是和连接线相邻的图元，并且是有连接关系的图元
				SingleSelectionManager ssm = (SingleSelectionManager) editor
						.getHandlesManager().getCurrentHandle().getSelection()
						.getActionSelectionManager();
				if (ssm != null) {
					CanvasPainter cp = ssm.getCurrentCanvasPainter();
					if (cp != null) {
						Rectangle2D bounds = cp.getClip().iterator().next();
						// System.out.println(bounds);
						editor.getHandlesManager().getCurrentHandle()
								.getSelection().showTerminal(e, bounds);
						editor.getSvgSession().refreshCurrentHandleImediately();
					}
				}
			}
		}
	}

	private void translateCorrelate(Element firstTranslateEle,
			Element correlateEle, boolean previousOrNext) {
		String location = firstTranslateEle.getAttribute("d");
		Path firstElepath = new Path(location);
		Segment seg = firstElepath.getSegment();
		Point2D firstEleStartPoint = seg.getEndPoint();
		Point2D firstEleEndPoint = seg.getNextSegment().getEndPoint();

		String correlateEleLocation = correlateEle.getAttribute("d");
		Path correlateElePath = new Path(correlateEleLocation);
		seg = correlateElePath.getSegment();
		Point2D correlateEleStartPoint = seg.getEndPoint();
		Point2D correlateEleEndPoint = seg.getNextSegment().getEndPoint();

		StringBuffer sb = new StringBuffer();
		if (previousOrNext) {// 被移动的节点的起始点与受影响的前一个节点的结束点重合
			sb.append("M").append(correlateEleStartPoint.getX()).append(" ")
					.append(correlateEleStartPoint.getY()).append("L").append(
							firstEleStartPoint.getX()).append(" ").append(
							firstEleStartPoint.getY());
		} else {// 被移动的节点的结束点与受影响的前一个节点的起点重合
			sb.append("M").append(firstEleEndPoint.getX()).append(" ").append(
					firstEleEndPoint.getY()).append("L").append(
					correlateEleEndPoint.getX()).append(" ").append(
					correlateEleEndPoint.getY());
		}
		correlateEle.setAttribute("d", sb.toString());
	}

	public Element createElement(SVGHandle handle, Shape pathShape,
			String lineID) {

		return ((VHDrawingHandler) drawingHandler).createPathElement(handle,
				pathShape, lineID);
	}

	public boolean isElementTypeSupported(Element element) {
		return (element != null
				&& handledElementTagName.equals(element.getNodeName())
				&& element.getParentNode() != null && ((Element) element
				.getParentNode()).getAttribute(GROUP_STYLE).equals(VH_STYPE));
	}

	public void insertShapeElement(final SVGHandle handle,
			final Element shapeElement) {

		if (shapeElement != null) {

			// 设置唯一的id
			String id = shapeElement.getAttribute("id");
			if (id == null || id.length() == 0) {
				id = UUID.randomUUID().toString();
				shapeElement.setAttribute("id", id);
			}
			// getting the current parent element of all the edited nodes
			final Element parentElement = handle.getSelection()
					.getParentElement();

			// the execute runnable
			Runnable executeRunnable = new Runnable() {

				public void run() {
					if (editor.getPositionHandler() == null) {
						parentElement.appendChild(shapeElement);
					} else {
						Set<Element> eles = new HashSet<Element>();
						eles.add(shapeElement);
						editor.getPositionHandler().handlePosition(eles,
								parentElement);
					}
					handle.getSelection().clearSelection();
					Set<Element> children = new HashSet<Element>();
					NodeList childrenNodes = shapeElement.getChildNodes();
					for (int i = 0; i < childrenNodes.getLength(); i++) {
						children.add((Element) childrenNodes.item(i));
					}
					handle.getSelection().handleSelection(children, false,
							false);
					String strID = shapeElement.getAttribute("id");
					if (strID != null && strID.length() > 0) {
						handle.getCanvas().appendElement(strID, shapeElement);
					}
					Set<Element> sets = new HashSet<Element>();
					sets.add(shapeElement);
					if (editor.getModeManager().isPropertyPaneCreate()) {
						handle.getEditor().getPropertyModelInteractor()
								.getGraphProperty().setElement(sets);
					}
				}
			};

			// the undo runnable
			Runnable undoRunnable = new Runnable() {

				public void run() {

					String strID = shapeElement.getAttribute("id");
					if (strID != null && strID.length() > 0) {
						handle.getCanvas().deleteElement(strID, shapeElement);
					}
					parentElement.removeChild(shapeElement);
					handle.getEditor().getPropertyModelInteractor()
							.getGraphProperty().setElement(null);
				}
			};

			// executing the action and creating the undo/redo action
			HashSet<Element> elements = new HashSet<Element>();
			elements.add(shapeElement);
			UndoRedoAction undoRedoAction = ShapeToolkit.getUndoRedoAction(
					undoRedoLabel, executeRunnable, undoRunnable, elements);

			UndoRedoActionList actionlist = new UndoRedoActionList(
					undoRedoLabel, false);
			actionlist.add(undoRedoAction);
			handle.getUndoRedo().addActionList(actionlist, false);
		}
	}

	public Point2D computeVHCurrentPoint(Shape initialShape,
			Point2D firstPoint, Point2D currentPoint, boolean flag) {
		if (flag) {
			if (initialShape.getBounds2D().getWidth() == 0) {// 水平坐标不变
				currentPoint = new Point2D.Double(currentPoint.getX(),
						firstPoint.getY());
			} else if (initialShape.getBounds2D().getHeight() == 0) {// 垂直坐标不变
				currentPoint = new Point2D.Double(firstPoint.getX(),
						currentPoint.getY());
			}
		} else {
			if (initialShape.getBounds2D().getWidth() == 0) {// 水平坐标不变
				currentPoint = new Point2D.Double(firstPoint.getX(),
						currentPoint.getY());
			} else if (initialShape.getBounds2D().getHeight() == 0) {// 垂直坐标不变

				currentPoint = new Point2D.Double(currentPoint.getX(),
						firstPoint.getY());
			}
		}
		return currentPoint;
	}

	@Override
	public CanvasPainter showTranslateAction(SVGHandle handle,
			Set<Element> elementSet, Point2D firstPoint, Point2D currentPoint) {

		return translationHandler.showTranslateAction(handle, elementSet,
				firstPoint, currentPoint);
	}

	@Override
	public UndoRedoAction validateTranslateAction(SVGHandle handle,
			Set<Element> elementSet, Point2D firstPoint, Point2D currentPoint) {

		return translationHandler.validateTranslateAction(handle, elementSet,
				firstPoint, currentPoint);
	}

	public Shape getModifiedPointShape(SVGHandle svgHandle, Element element,
			Point2D firstPoint, Point2D currentPoint, SelectionItem item) {

		// the shape that will be returned
		Shape shape = null;

		// the shapes
		Path initialShape = (Path) getShape(svgHandle, element, false);
		// initialShape.getPathType()
		// 对firstpoint的处理是为了防止拖动是的微妙偏移
		if (initialShape.getBounds2D().getHeight() == 0) {
			// firstPoint.setLocation(initialShape.getBounds2D().getX()+initialShape.getBounds2D().getWidth(),
			// initialShape.getBounds2D().getY());
			firstPoint.setLocation(initialShape.getBounds2D().getX()
					+ initialShape.getBounds2D().getWidth(), initialShape
					.getBounds2D().getY());
		} else if (initialShape.getBounds2D().getWidth() == 0) {
			firstPoint.setLocation(initialShape.getBounds2D().getX(),
					initialShape.getBounds2D().getY()
							+ initialShape.getBounds2D().getHeight());
			// firstPoint.setLocation(currentPoint.getX(),
			// initialShape.getBounds2D().getY()+initialShape.getBounds2D().getHeight());

		}
		// getting the element's transform
		AffineTransform transform = svgHandle.getSvgElementsManager()
				.getTransform(element);

		// the new point that will replace the former one at the
		// index given by the selection item

		currentPoint = computeVHCurrentPoint(initialShape, firstPoint,
				currentPoint, false);
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
		// initialShape.modifyPoint(newPoint, 0);
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

	public static final int VERTICAL = 0;

	public static final int HORINZONAL = 1;

	/**
	 * 判断是垂直线还是水平线
	 * 
	 * @param initialShape
	 * @return
	 */
	public static int getVHPathType(Path initialShape) {
		if (initialShape.getBounds2D().getHeight() == 0)
			return HORINZONAL;
		if (initialShape.getBounds2D().getWidth() == 0)
			return VERTICAL;
		return -1;
	}

	@Override
	public CanvasPainter showAction(SVGHandle handle, int level,
			Set<Element> elementSet, SelectionItem item, Point2D firstPoint,
			Point2D currentPoint) {
		return translationHandler.showAction(handle, level, elementSet, item,
				firstPoint, currentPoint);

	}

	public UndoRedoAction modifyPoint(final SVGHandle handle,
			final Element element, SelectionItem item, Point2D point) {

		return translationHandler.modifyPoint(handle, element, item, point);
	}

	/**
	 * 判断指定的节点是否是连接线g节点
	 * 
	 * @param gPathElement
	 * @return
	 */
	public static boolean isVHPathGroup(Element gPathElement) {
		if (gPathElement.getNodeName().equals("g")) {
			if (gPathElement.getAttribute(GROUP_STYLE).equals(VH_STYPE)) {
				return true;
			}
		}
		return false;
	}

	public void initialize() {
	}

	@Override
	public HashMap<String, JMenuItem> getMenuItems() {
		return null;
	}

	@Override
	public HashMap<String, AbstractButton> getToolItems() {
		return null;
	}

	public VHPathTranslationHandler getTranslationHandler() {
		return translationHandler;
	}

	public void setTranslationHandler(
			VHPathTranslationHandler translationHandler) {
		this.translationHandler = translationHandler;
	}

	public Set<Element> getActionElements(Set<Element> selectedEle,
			SelectionItem item) {
		return item.getElements();
	}

}
