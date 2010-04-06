package nci.yc.svg.ges;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.swing.text.Position;

import org.apache.batik.ext.awt.geom.ExtendedGeneralPath;
import org.w3c.dom.Element;

import com.nci.svg.module.TerminalModule;
import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.client.util.EditorToolkit;
import com.nci.svg.sdk.other.NciTextPathModule;
import com.nci.svg.sdk.shape.PseudoConnectedElement;
import com.nci.svg.sdk.shape.vhpath.DefaultVHTranslationHandler;
import com.nci.svg.sdk.shape.vhpath.VHPathShape;

import fr.itris.glips.library.geom.path.Path;
import fr.itris.glips.svgeditor.display.canvas.CanvasPainter;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.display.selection.SelectionItem;
import fr.itris.glips.svgeditor.display.undoredo.UndoRedoAction;
import fr.itris.glips.svgeditor.shape.ShapeToolkit;

public class FlowPathTranslationHandler extends DefaultVHTranslationHandler {

	private FlowGraphicsModule flowGraphicsModule;

	public static final int EAST = 0;

	public static final int WEST = 1;

	public static final int SOUTH = 2;

	public static final int NORTH = 3;

	private boolean containMiddlePoint = false;

	public FlowPathTranslationHandler(VHPathShape vhPathShape,
			FlowGraphicsModule flowGraphicsModule) {
		super(vhPathShape);
		this.flowGraphicsModule = flowGraphicsModule;
	}

	private int parsePosition(String position) {
		if (position.equals(TerminalModule.EAST_TERMINAL_NAME))
			return EAST;
		if (position.equals(TerminalModule.WEST_TERMINAL_NAME))
			return WEST;
		if (position.equals(TerminalModule.SOUTH_TERMINAL_NAME))
			return SOUTH;
		if (position.equals(TerminalModule.NORTH_TERMINAL_NAME))
			return NORTH;
		return -1;
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
			// currentPoint = vhPathShape.computeVHCurrentPoint(initialShape,
			// firstPoint, currentPoint, true);
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


	@Override
	public CanvasPainter showAction(SVGHandle handle, int level,
			Set<Element> elementSet, SelectionItem item, Point2D firstPoint,
			Point2D currentPoint) {
		// getting the element that will undergo the action
		Element element = null;
		// if(elementSet.size()==1){
		// showTransPathEle = elementSet.iterator().next();
		// }
		// if (showTransPathEle == null) {
		// for (Element pathEle : elementSet) {
		// Path path = new Path(pathEle.getAttribute("d"));
		// if (EditorToolkit.contiansPoint(path.getBounds2D(), firstPoint)) {
		// showTransPathEle = pathEle;
		// break;
		// }
		// }
		// }
		element = item.getElements().iterator().next();
		// the canvas painter that should be returned
		CanvasPainter painter = null;

		// whether the shape should be painted
		boolean canPaintShape = true;

		// the shape that will be painted
		Shape shape = getModifiedPointShape(handle, element, firstPoint,
				currentPoint, item);

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
	public UndoRedoAction modifyPoint(final SVGHandle handle,
			final Element elementIt, final SelectionItem item, Point2D point) {
		// getting the initial shape
		showPath.reset();
		// 这个值和参数中的element不同，这里比较特殊，是在单选模式下选中的多个path，参数element是从selectedElements中第一个迭带值
		final Element transEle = item.getElements().iterator().next();
		if (transEle.getPreviousSibling() != null
				&& transEle.getNextSibling() != null)
			return null;

		PseudoConnectedElement pseudoEle = handle.getSelection()
				.getPseudoConnectedElement();
		int position = -1;
		if (pseudoEle != null)
			position = parsePosition(pseudoEle.getPosition());
		final Path initialShape = (Path) vhPathShape.getShape(handle, transEle,
				false);
		final String initdValue = initialShape.toString();
		final boolean isFirst = transEle.getNextSibling() == null ? true
				: false;
		Point2D trueStartPoint = null;
		final List<Element> createdEles = new ArrayList<Element>();
		String originD = "";
		for (int i = 0; i < cornerPoints.size(); i++) {
			Point2D p = cornerPoints.get(i);
			if (i == 0) {
				trueStartPoint = p;
				continue;
			} else if (p instanceof MiddlePoint && containMiddlePoint) {
				trueStartPoint = p;
				continue;
			} else if (((initialShape.getBounds2D().getWidth() == 0 && (position == EAST || position == WEST)) || (initialShape
					.getBounds2D().getHeight() == 0 && (position == SOUTH || position == NORTH)))
					&& i == 1) {

				trueStartPoint = p;
				continue;
			} else if (i == 1 && position == -1
					&& initialShape.getBounds2D().getWidth() == 0) {
				trueStartPoint = p;
				continue;
			} else {
				String d = isFirst ? EditorToolkit.getPathDAttrString(
						trueStartPoint, p) : EditorToolkit.getPathDAttrString(
						p, trueStartPoint);
				trueStartPoint = p;
				if (i == cornerPoints.size() - 1) {
					originD = d;
				} else {
					Element e = transEle.getOwnerDocument().createElementNS(
							transEle.getOwnerDocument().getDocumentElement()
									.getNamespaceURI(), "path");
					e.setAttributeNS(null, "style", "fill:none;stroke:"
							+ Constants.NCI_DEFAULT_STROKE_COLOR + ";");
					e.setAttribute("id", UUID.randomUUID().toString());
					e.setAttribute("d", d);
					createdEles.add(e);
				}
				//

			}
		}
		final String originD_f = originD;

		Runnable executeRunnable = new Runnable() {

			public void run() {

				transEle.setAttribute(VHPathShape.dAtt, originD_f);

				if (isFirst) {
					Element before = transEle;
					for (int i = createdEles.size() - 1; i >= 0; i--) {
						Element e = createdEles.get(i);
						transEle.getParentNode().insertBefore(e, before);
						before = e;
					}
				} else {
					Element before = (Element) transEle.getNextSibling();
					for (int i = 0; i < createdEles.size(); i++) {
						Element e = createdEles.get(i);
						transEle.getParentNode().insertBefore(e, before);
						before = e;
					}
				}
				Set<Element> trans = new HashSet<Element>();
				trans.add(transEle);
				for (Element e : createdEles) {
					trans.add(e);
				}
				//新加入的线路要移动一下，使视觉上未变化的线路也跟着变化
				vhPathShape.translated(trans);
				if (handle.getSelection().getPseudoConnectedElement() != null) {
//					flowGraphicsModule.brokeConn(trans);
					flowGraphicsModule
							.appendConnRelation(handle.getSelection()
									.getPseudoConnectedElement().getConnEle(),
									transEle);
				}
			}
		};

		// the undo runnable
		Runnable undoRunnable = new Runnable() {

			public void run() {

				transEle.setAttribute(VHPathShape.dAtt, initdValue);
				// NciTextPathModule module = new NciTextPathModule(vhPathShape
				// .getEditor());
				// module.correctTextPos(handle, element, dValue, initdValue);
				vhPathShape.refresh(transEle);

			}
		};

		// creating the undo/redo action, and adding it to the undo/redo stack
		Set<Element> elements = new HashSet<Element>();
		elements.add(transEle);

		UndoRedoAction undoRedoAction = ShapeToolkit.getUndoRedoAction(
				vhPathShape.modifyPointUndoRedoLabel, executeRunnable,
				undoRunnable, elements);

		return undoRedoAction;
	}

	private List<Point2D> cornerPoints = new ArrayList<Point2D>();

	protected ExtendedGeneralPath showPath = new ExtendedGeneralPath();

	public Shape getModifiedPointShape(SVGHandle svgHandle, Element pathEle,
			Point2D firstPoint, Point2D currentPoint, SelectionItem item) {
		try {
			cornerPoints.clear();
			flowGraphicsModule.brokeConn(pathEle);
			// the shape that will be returned
			Shape shape = null;

			Path intialPath = new Path(pathEle.getAttribute("d"));
			// 算起始点，终点
			boolean isFirst = pathEle.getNextSibling() == null ? true : false;
			Point2D start = null;
			if (pathEle.getPreviousSibling() == null) {
				start = intialPath.getSegment().getNextSegment().getEndPoint();
			} else if (pathEle.getNextSibling() == null) {
				start = intialPath.getSegment().getEndPoint();
			}
			if (start == null)
				return null;
			cornerPoints.add(start);
			if (svgHandle.getSelection().getPseudoConnectedElement() == null
					&& start != null) {
				Point2D middle = new Point2D.Double(currentPoint.getX(), start
						.getY());
				cornerPoints.add(middle);
			} else if (svgHandle.getSelection().getPseudoConnectedElement() != null
					&& start != null) {
				boolean inner = false;

				String position = svgHandle.getSelection()
						.getPseudoConnectedElement().getPosition();
				int positionFlag = parsePosition(position);
				Rectangle2D rect = flowGraphicsModule.getElementShape(
						svgHandle.getSelection().getPseudoConnectedElement()
								.getConnEle()).getBounds2D();
				fitCurrentPoint(currentPoint, positionFlag, rect);
				boolean south_north = position
						.equals(TerminalModule.EAST_TERMINAL_NAME)
						|| position.equals(TerminalModule.WEST_TERMINAL_NAME) ? false
						: true;
				if (!south_north) {
					if (position.equals(TerminalModule.EAST_TERMINAL_NAME)) {
						if (start.getX() > currentPoint.getX()
								&& start.getX() - currentPoint.getX() < FlowVHPathDrawingHandler.USE_CAP) {
							inner = true;
						}
					} else if (currentPoint.getX() > start.getX()
							&& currentPoint.getX() - start.getX() < FlowVHPathDrawingHandler.USE_CAP) {
						inner = true;
					}
				} else {
					if (position.equals(TerminalModule.SOUTH_TERMINAL_NAME)) {
						if (start.getY() > currentPoint.getY()
								&& start.getY() - currentPoint.getY() < FlowVHPathDrawingHandler.USE_CAP) {
							inner = true;
						}
					} else if (currentPoint.getY() > start.getY()
							&& currentPoint.getY() - start.getY() < FlowVHPathDrawingHandler.USE_CAP) {
						inner = true;
					}
				}

				// vhPathShape.translated(svgHandle.getSelection().getSelectedElements());
				Point2D middle = null;
				if (inner) {
					// 下面两种情况要增加一个拐点，以确保每个连接线都是水平──垂直顺序
					if (intialPath.getBounds2D().getWidth() == 0 && south_north) {
						cornerPoints.add(start);
					} else if (intialPath.getBounds2D().getHeight() == 0
							&& !south_north) {
						cornerPoints.add(start);
					}
					// 增加middle节点
					middle = new Point2D.Double(currentPoint.getX(), start
							.getY());
					cornerPoints.add(middle);

				} else {
					Point2D capPoint = getCapPoint(currentPoint, positionFlag);
					middle = getMiddlePoint(capPoint, start, positionFlag);

					cornerPoints.add(middle);
					cornerPoints.add(capPoint);
				}
				Element nextEle = isFirst ? (Element) pathEle
						.getPreviousSibling() : (Element) pathEle
						.getNextSibling();
				Path nextPathShape = new Path(nextEle.getAttribute("d"));
				containMiddlePoint = EditorToolkit.contiansPoint(nextPathShape
						.getBounds2D(), middle);

			}
			cornerPoints.add(currentPoint);
			showPath.reset();
			showPath.moveTo((float) start.getX(), (float) start.getY());
			for (Point2D p : cornerPoints) {
				showPath.lineTo((float) p.getX(), (float) p.getY());
			}
			shape = svgHandle.getTransformsManager().getScaledShape(showPath,
					false);

			return shape;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

	private void fitCurrentPoint(Point2D currentPoint, int positionFlag,
			Rectangle2D pesudoEleRect) {

		switch (positionFlag) {
		case EAST:
			currentPoint.setLocation(pesudoEleRect.getCenterX()
					+ pesudoEleRect.getWidth() / 2, pesudoEleRect.getCenterY());
			break;
		case WEST:
			currentPoint.setLocation(pesudoEleRect.getCenterX()
					- pesudoEleRect.getWidth() / 2, pesudoEleRect.getCenterY());
			break;
		case SOUTH:
			currentPoint.setLocation(pesudoEleRect.getCenterX(), pesudoEleRect
					.getCenterY()
					+ pesudoEleRect.getHeight() / 2);
			break;
		case NORTH:
			currentPoint.setLocation(pesudoEleRect.getCenterX(), pesudoEleRect
					.getCenterY()
					- pesudoEleRect.getHeight() / 2);
			break;
		default:
			break;
		}

	}

	private Point2D getCapPoint(Point2D currentPoint, int positionFlag) {
		Point2D p = new Point2D.Double();
		switch (positionFlag) {
		case EAST:
			p.setLocation(currentPoint.getX()
					+ FlowVHPathDrawingHandler.USE_CAP, currentPoint.getY());
			break;
		case WEST:
			p.setLocation(currentPoint.getX()
					- FlowVHPathDrawingHandler.USE_CAP, currentPoint.getY());
			break;
		case SOUTH:
			p.setLocation(currentPoint.getX(), currentPoint.getY()
					+ FlowVHPathDrawingHandler.USE_CAP);
			break;
		case NORTH:
			p.setLocation(currentPoint.getX(), currentPoint.getY()
					- FlowVHPathDrawingHandler.USE_CAP);
		default:
			break;
		}
		return p;
	}

	private Point2D getMiddlePoint(Point2D capPoint, Point2D startPoint,
			int positionFlag) {
		Point2D p = new MiddlePoint();
		switch (positionFlag) {
		case EAST:
			p.setLocation(capPoint.getX(), startPoint.getY());
			break;
		case WEST:
			p.setLocation(capPoint.getX(), startPoint.getY());
			break;
		case SOUTH:
			p.setLocation(startPoint.getX(), capPoint.getY());
			break;
		case NORTH:
			p.setLocation(startPoint.getX(), capPoint.getY());
			break;
		default:
			break;
		}
		return p;
	}

	private class MiddlePoint extends Point2D.Double {

	}
}
