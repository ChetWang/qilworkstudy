package nci.yc.svg.ges;

import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.swing.Timer;
import javax.xml.xpath.XPathExpressionException;

import org.apache.batik.dom.svg.SVGOMTSpanElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jidesoft.utils.SwingWorker;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.action.ElementDeleteListener;
import com.nci.svg.sdk.client.function.ModuleAdapter;
import com.nci.svg.sdk.client.selection.GraphicTranslationListener;
import com.nci.svg.sdk.client.selection.ModeSelectionEvent;
import com.nci.svg.sdk.client.selection.ModeSelectionListener;
import com.nci.svg.sdk.client.util.EditorToolkit;
import com.nci.svg.sdk.client.util.Utilities;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.shape.ElementDeleteFilter;
import com.nci.svg.sdk.shape.GroupBreakerIF;
import com.nci.svg.sdk.shape.SelectionFilterIF;
import com.nci.svg.sdk.shape.ShapeAutoAjuster;
import com.nci.svg.sdk.shape.ShapePositionHandler;
import com.nci.svg.sdk.shape.vhpath.VHPathShape;
import com.nci.svg.sdk.shape.vhpath.VHPathTranslationHandler;

import fr.itris.glips.library.geom.path.Path;
import fr.itris.glips.library.geom.path.segment.Segment;
import fr.itris.glips.svgeditor.actions.clipboard.ClipboardModule;
import fr.itris.glips.svgeditor.display.handle.HandlesListener;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.display.undoredo.UndoRedoAction;
import fr.itris.glips.svgeditor.display.undoredo.UndoRedoActionList;
import fr.itris.glips.svgeditor.shape.ShapeToolkit;
import fr.itris.glips.svgeditor.shape.path.DrawingHandler;
import fr.itris.glips.svgeditor.shape.text.TextShape;

public class FlowGraphicsModule extends ModuleAdapter {

	public static final String FLOW_FILE_TYPE = "flow";

	public static final String DOCUMENT_WHOLE_TYPE = "flow_whole";

	public static final String ELEMENT_GROUP_PART_TYPE = "flow_part";

	public static final String CONN_PATH_ELEMENT_NAME = "connpath";

	public static final String CONN_PATH_ELEMENT_ID = "pathid";

	/**
	 * �ײ�����
	 */
	public static final int TOP_CONN = 1;
	/**
	 * β������
	 */
	public static final int BOTTON_CONN = 0;

	/**
	 * β�������ɵ�������������꣬������Դ�����ߵ���һ��·����
	 */
	public static final int BOTTON_CONVERSE_RIGHT = 3;
	/**
	 * β�������ɸ�������������꣬������Դ�����ߵ���һ��·����
	 */
	public static final int BOTTON_CONVERSE_LEFT = 2;
	/**
	 * �ײ������ɸ�������������꣬������Դ�����ߵ���һ��·����
	 */
	public static final int TOP_CONVERSE_LEFT = 4;
	/**
	 * �ײ������ɵ�������������꣬������Դ�����ߵ���һ��·����
	 */
	public static final int TOP_CONVERSE_RIGHT = 5;

	VHPathShape vhpath;

	FlowVHPathDrawingHandler flowDrawingHandler = new FlowVHPathDrawingHandler(
			vhpath, this);

	public FlowGraphicsModule(EditorAdapter editor) {
		super(editor);
	}

	public int init() {
		try {
			final EditorAdapter editorAd = editor;
			final GraphicTranslationListener transListener = new GraphicTranslationListener() {
				@Override
				public void graphicsTranslated(
						final Set<Element> elementsTraslated,
						final Point2D firstPoint, final Point2D scaledPoint) {

					translated(elementsTraslated, firstPoint, scaledPoint);
				}

				@Override
				public void graphicsTranslating(
						Set<Element> elementsTraslating, Point2D firstPoint,
						Point2D scaledPoint) {
					// translating(elementsTraslating);
				}
			};
			final GroupBreakerIF groupBreaker = new GroupBreakerIF() {

				@Override
				public boolean breakGroup(Element groupElement) {
					return groupBreak(groupElement);
				}

			};
			final ModeSelectionListener doubleClickLis = new ModeSelectionListener() {

				@Override
				public void modeSelected(final ModeSelectionEvent evt) {
					SwingWorker worker = new SwingWorker() {

						@Override
						protected Object doInBackground() throws Exception {
							editText(evt);
							return null;
						}

						public void done() {
							editorAd.getSvgSession()
									.refreshCurrentHandleImediately();
						}

					};
					worker.execute();

				}

			};
			final SelectionFilterIF selectionFilter = new SelectionFilterIF() {

				@Override
				public boolean filterElement(Element shapeElement) {
					return filterFlowShape(shapeElement);
				}

			};
			final ShapeAutoAjuster autoAjuster = new ShapeAutoAjuster() {

				@Override
				public void autoAjustWhenCreated(Element useEle) {
					autoAjustCreatedUseShape(useEle);
				}

				@Override
				public void autoAjustWhenResized(Element useEle,
						Shape oldShape, Shape newShape) {
					autoAjustResizedUseShape(useEle, oldShape, newShape);

				}

			};

			vhpath = (VHPathShape) editor.getModuleByID(VHPathShape.MODULE_ID);
			// final VHPathShape vh = vhpath;
			final ClipboardModule clipModule = (ClipboardModule) editor
					.getModuleByID(ClipboardModule.MODULE_ID);
			final ElementDeleteListener edl = new ElementDeleteListener() {

				@Override
				public void elementDeleted(
						final HashMap<Element, Element> parentNodesMap,
						final List<Element> elementsToDelete) {
					new SwingWorker() {

						@Override
						protected Object doInBackground() throws Exception {
							deleteElement(parentNodesMap, elementsToDelete);
							return null;
						}

					}.execute();

				}

				@Override
				public void undoDelete(
						final HashMap<Element, Element> parentNodesMap,
						final List<Element> elementsToDelete) {
					new SwingWorker() {

						@Override
						protected Object doInBackground() throws Exception {
							undoElementDelete(parentNodesMap, elementsToDelete);
							return null;
						}

					}.execute();

				}

			};
			final ElementDeleteFilter flowDeleteFilter = new ElementDeleteFilter() {

				@Override
				public boolean filterElement(Element shapeElement,
						List<Element> elementsToDelte) {

					return checkElementDeleteable(shapeElement, elementsToDelte);
				}

			};
			final ElementDeleteFilter defauDeleteltFilter = clipModule
					.getDeleteFilter();
			final DrawingHandler defaultVHHandler = vhpath.getDrawingHandler();

			final VHPathTranslationHandler defaultTranslationHandler = vhpath
					.getTranslationHandler();
			final VHPathTranslationHandler flowTranslationHandler = new FlowPathTranslationHandler(
					vhpath, this);
			final ShapePositionHandler positionHandler = new ShapePositionHandler() {
				@Override
				public void handlePosition(Set<Element> createdElements,
						Element parentElement) {
					handlePathElementPosition(createdElements, parentElement);
				}
			};

			editor.getHandlesManager().addHandlesListener(
					new HandlesListener() {
						public void handleCreated(SVGHandle currentHandle) {
							if (isFlowSupportFileType(currentHandle.getCanvas()
									.getFileType())) {
								currentHandle.getSelection()
										.addGraphicTranslationListener(
												transListener);
								currentHandle.getSelection().setGroupBreaker(
										groupBreaker);
								currentHandle
										.getSelection()
										.addMouseClickedListener(doubleClickLis);
								currentHandle.getSelection()
										.setSelectionFilter(selectionFilter);
								currentHandle.setAutoAjuster(autoAjuster);
								clipModule.addElementDeletedListener(edl);
								clipModule.setDeleteFilter(flowDeleteFilter);
							}
						}

						public void handleChanged(SVGHandle currentHandle,
								Set<SVGHandle> handles) {
							if (currentHandle != null) {
								String fileType = currentHandle.getCanvas()
										.getFileType();
								boolean isFlowSupport = isFlowSupportFileType(fileType);
								if (isFlowSupport
										&& vhpath.getDrawingHandler() != flowDrawingHandler) {
									vhpath
											.setDrawingHandler(flowDrawingHandler);
								} else if (!isFlowSupport
										&& vhpath.getDrawingHandler() == flowDrawingHandler) {
									vhpath.setDrawingHandler(defaultVHHandler);
								}
								if (isFlowSupport
										&& editor.getPositionHandler() == null) {
									editor.setPositionHandler(positionHandler);
								} else if (!isFlowSupport
										&& editor.getPositionHandler() == positionHandler) {
									editor.setPositionHandler(null);
								}
								if (isFlowSupport
										&& vhpath.getTranslationHandler() != flowTranslationHandler) {
									vhpath
											.setTranslationHandler(flowTranslationHandler);
								} else if (!isFlowSupport
										&& vhpath.getTranslationHandler() == flowTranslationHandler) {
									vhpath
											.setTranslationHandler(defaultTranslationHandler);
								}
								if (isFlowSupport
										&& clipModule.getDeleteFilter() != flowDeleteFilter) {
									clipModule
											.setDeleteFilter(flowDeleteFilter);
								} else if (!isFlowSupport
										&& clipModule.getDeleteFilter() == flowDeleteFilter) {
									clipModule
											.setDeleteFilter(defauDeleteltFilter);
								}
							}
						}
					});
		} catch (Exception e) {
			editor.getLogger().log(this, LoggerAdapter.ERROR, e);
			return MODULE_INITIALIZE_FAILED;
		}
		return MODULE_INITIALIZE_COMPLETE;
	}

	protected boolean groupBreak(Element groupElement) {
		if (groupElement.getAttribute(VHPathShape.GROUP_STYLE).equals(
				DOCUMENT_WHOLE_TYPE))
			return true;
		if (groupElement.getAttribute(VHPathShape.GROUP_STYLE).equals(
				ELEMENT_GROUP_PART_TYPE))
			return true;
		return false;
	}

	/**
	 * ��λVHPath��SVG�е�λ��
	 * 
	 * @param createdEles
	 * @param parentElement
	 */
	protected void handlePathElementPosition(Set<Element> createdEles,
			Element parentElement) {
		for (Element shapeEle : createdEles) {
			if (shapeEle.getNodeName().equals("path")
					|| VHPathShape.isVHPathGroup(shapeEle)) {
				parentElement.appendChild(shapeEle);
			} else {
				NodeList nodes = parentElement.getChildNodes();
				Node previousNode = null;
				for (int i = 0; i < nodes.getLength(); i++) {
					Node node = nodes.item(i);
					if (node instanceof Element) {
						Element e = (Element) node;
						if (e.getNodeName().equals("path")
								|| VHPathShape.isVHPathGroup(e)) {
							previousNode = e;
							break;
						}
					}
				}
				parentElement.insertBefore(shapeEle, previousNode);
			}
		}
	}

	/**
	 * ����ѡ��ʱ���˵Ľڵ�
	 * 
	 * @param shapeElement
	 * @return
	 */
	protected boolean filterFlowShape(Element shapeElement) {
		if (shapeElement.getNodeName().equals("text")) {
			Element parent = (Element) shapeElement.getParentNode();
			if (parent.getAttribute(VHPathShape.GROUP_STYLE).equals(
					ELEMENT_GROUP_PART_TYPE)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * ˫���༭����
	 * 
	 * @param evt
	 */
	protected void editText(ModeSelectionEvent evt) {
		if (this.editTextClick) {
			MouseEvent mouseEvent = evt.getSource().getMouseEvent();
			if (mouseEvent.getClickCount() == 2
					&& mouseEvent.getButton() == MouseEvent.BUTTON1) {
				Element useEle = evt.getSource().getElementAtMousePoint();
				// packElement(useEle);
				if (useEle == null || !useEle.getNodeName().equals("use")) {
					return;
				}
				TextShape textShape = (TextShape) editor.getModule("TextShape");
				SVGHandle handle = evt.getSource().getHandle();
				Node oldTextNode = useEle.getNextSibling();

				StringBuffer existText = new StringBuffer();
				if (oldTextNode != null
						&& isFlowPartGroup((Element) useEle.getParentNode())) {

					if (!oldTextNode.getNodeName().equals("text")) {
						System.err
								.println("Warning:��ʽ���ܴ������⣬use�ڵ���һ�������Ų�����text");
						return;
					}
					NodeList oldTspans = oldTextNode.getChildNodes();
					for (int i = 0; i < oldTspans.getLength(); i++) {
						SVGOMTSpanElement tspan = (SVGOMTSpanElement) oldTspans
								.item(i);
						Node text = tspan.getChildNodes().item(0);
						if (text == null) {
							System.err
									.println("Warning:��ʽ���ܴ������⣬text�ڵ���tspanû����������");
						}
						existText.append(text.getNodeValue());
						if (i != oldTspans.getLength() - 1)
							existText.append("\n");
					}
				} else {
					oldTextNode = null;
				}
				Rectangle2D rect = editor.getHandlesManager()
						.getCurrentHandle().getSvgElementsManager()
						.getNodeGeometryBounds(useEle);
				textShape.setDrawingPoint(new Point2D.Double(rect.getCenterX(),
						rect.getCenterY() + TextShape.FONT_SIZE / 2));
				handle.getSelection().clearSelection();
				handle.getSelection().setBTextDialog(true);
				textShape.getTextDialog().showDialog(handle.getCanvas(),
						handle, existText.toString());
				handle.getSelection().setBTextDialog(false);
				Set<Element> selectEle = handle.getSelection()
						.getSelectedElements();
				if (selectEle.size() == 1) {
					Element textEle = selectEle.iterator().next();
					if (!textEle.getNodeName().equals("text"))
						return;
					if (oldTextNode != null)
						oldTextNode.getParentNode().removeChild(oldTextNode);
					textEle.getParentNode().removeChild(textEle);
					useEle.getParentNode().appendChild(textEle);

					centerTextOnUseRect(textEle, rect);
					packElement(useEle, textEle);
				}
				handle.getSelection().clearSelection();

			}
		}
	}

	/**
	 * �������Զ�������ͼԪ����
	 * 
	 * @param useEle
	 */
	public void centerTextOnUseRect(Element useEle) {
		Element textEle = null;
		if (isFlowPartGroup((Element) useEle.getParentNode())) {
			textEle = (Element) useEle.getNextSibling();
		}
		centerTextOnUseRect(textEle, editor.getHandlesManager()
				.getCurrentHandle().getSvgElementsManager()
				.getNodeGeometryBounds(useEle));
	}

	/**
	 * �����ַ�����ͼԪ����
	 * 
	 * @param textEle
	 * @param useRect
	 */
	public void centerTextOnUseRect(Element textEle, Rectangle2D useRect) {
		if (textEle == null)
			return;
		Rectangle2D textRect = editor.getHandlesManager().getCurrentHandle()
				.getSvgElementsManager().getNodeGeometryBounds(textEle);
		NodeList tspans = textEle.getChildNodes();
		textEle.removeAttribute("transform");
		double verticalPad = textRect.getHeight() / tspans.getLength();
		double bottonY = useRect.getCenterY() - textRect.getHeight() / 2;
		for (int i = 0; i < tspans.getLength(); i++) {
			Node node = tspans.item(i);
			if (node.getNodeName().equals("tspan")) {
				Element tspanEle = (Element) node;
				EditorToolkit.setAttributeValue(tspanEle, "x", useRect
						.getCenterX()
						- textRect.getWidth() / 2);
				EditorToolkit.setAttributeValue(tspanEle, "y", verticalPad * i
						+ bottonY + TextShape.FONT_SIZE);
			}
		}
	}

	protected void packElement(Element clickedEle, Element textEle) {
		Element parent = (Element) clickedEle.getParentNode();
		// �Ѿ������ְ�Χ��������gԪ��
		if (parent != null
				&& parent.getNodeName().equals("g")
				&& parent.getAttribute(VHPathShape.GROUP_STYLE).equals(
						ELEMENT_GROUP_PART_TYPE)) {
			return;
		} else { // δ���ְ�Χ��û��gԪ��
			Element newGroup = clickedEle.getOwnerDocument().createElementNS(
					clickedEle.getOwnerDocument().getDocumentElement()
							.getNamespaceURI(), "g");
			newGroup.setAttribute(VHPathShape.GROUP_STYLE,
					ELEMENT_GROUP_PART_TYPE);
			parent.appendChild(newGroup);
			// parent.removeChild(clickedEle);
			newGroup.appendChild(clickedEle);
			newGroup.appendChild(textEle);
		}
	}

	protected void reunion(Element selectEle, Point2D firstPoint,
			Point2D scaledPoint) {
		SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
		// String fileType = handle.getCanvas().getFileType();
		Rectangle2D selectBounds = handle.getSvgElementsManager()
				.getNodeBounds(selectEle);
		Rectangle2D useRec = this.getElementShape(selectEle).getBounds2D();
		if (hasConnPath(selectEle)) {
			translateConnPath(selectEle, useRec, firstPoint, scaledPoint);
		}
		// else
		// translateConnPath(selectEle, firstPoint, scaledPoint);
		else if (selectEle != null && selectEle.getNodeName().equals("use")) {
			try {

				// �ҳ������ߵļ���
				NodeList vhPaths = Utilities.findNodes("//*[@model='"
						+ VHPathShape.VH_MODEL + "']", selectEle
						.getOwnerDocument());
				if (vhPaths != null) {
					for (int i = 0; i < vhPaths.getLength(); i++) {
						Node node = vhPaths.item(i);
						if (node.getNodeName().equals("g")) {
							NodeList childPaths = node.getChildNodes();
							for (int k = 0; k < childPaths.getLength(); k++) {
								Node path = childPaths.item(k);
								if (path.getNodeName().equals("path")) {
									Rectangle2D pathRect = handle
											.getSvgElementsManager()
											.getNodeBounds((Element) path);
									// Path pathShape = new Path(((Element)
									// path)
									// .getAttribute("d"));
									// Rectangle2D pathRect = pathShape
									// .getBounds2D();
									if (selectBounds.intersects(pathRect)) {// ����ཻ���ж��Ǻ����������ӣ����ײ�������β��
										int position = getPosition(
												(Element) path, selectEle);
										// �жϸ�λ���Ƿ��Ѿ������ӵ�ͼԪ����
										if (!isSamePositionExist(
												(Element) path, position)) {

											reunion(position, (Element) path,
													selectEle, selectBounds);

											break;
										}
									}
								}
							}
						}
					}
				}
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * �ж��Ƿ���ͬһλ���Ѿ��������ϵ�ͼԪ����
	 * 
	 * @param path
	 *            ָ���жϵ�������
	 * @param currentPosition
	 *            ��ǰ��ͼԪ���������ϵ�λ��
	 * @return
	 */
	private boolean isSamePositionExist(Element path, int currentPosition) {
		NodeList connUses = getConnElement(path.getAttribute("id"));
		int existPosition = -1;
		if (connUses.getLength() > 0) {

			if (path.getPreviousSibling() == null) {
				existPosition = BOTTON_CONN;
			} else if (path.getNextSibling() == null) {
				existPosition = TOP_CONN;
			}
		}
		return (existPosition == currentPosition);
	}

	/**
	 * ��ȡָ��pathid��Ӧ������ͼԪ
	 * 
	 * @param pathId
	 * @return
	 */
	private NodeList getConnElement(String pathId) {
		Document currentDoc = editor.getHandlesManager().getCurrentHandle()
				.getCanvas().getDocument();
		NodeList nodes = null;

		try {
			nodes = Utilities.findNodes("//*/" + CONN_PATH_ELEMENT_NAME + "[@"
					+ CONN_PATH_ELEMENT_ID + "='" + pathId + "']", currentDoc);

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return nodes;
	}

	protected Set<Element> getCrossedLines(NodeList connPaths, Element use,
			Rectangle2D useRec, double xOffset, double yOffset) {
		Set<Element> crossLines = new HashSet<Element>();
		for (int i = 0; i < connPaths.getLength(); i++) {
			String id = ((Element) connPaths.item(i))
					.getAttribute(FlowGraphicsModule.CONN_PATH_ELEMENT_ID);
			Element path = use.getOwnerDocument().getElementById(id);
			Path pathShape = new Path(path.getAttribute("d"));

			Point2D crossPoint = null;

			crossPoint = path.getPreviousSibling() == null ? pathShape
					.getSegment().getNextSegment().getEndPoint() : pathShape
					.getSegment().getEndPoint();
			boolean flag = false;
			if (pathShape.getBounds2D().getWidth() == 0) {

				// ˮƽ�����������ڵڶ�����·��Y��Ϊ��׼���ж��Ƿ�ͼԪ�͵ڶ�����·�������ӳ��ߣ��ཻ
				if (useRec.getCenterY() + useRec.getBounds2D().getHeight() / 2 >= crossPoint
						.getY()
						&& useRec.getCenterY()
								- useRec.getBounds2D().getHeight() / 2 <= crossPoint
								.getY()) {
					flag = true;
				}
			} else if (pathShape.getBounds2D().getHeight() == 0) {

				// ��ֱ�����������ڵڶ�����·��X��Ϊ��׼���ж��Ƿ�ͼԪ�͵ڶ�����·�������ӳ��ߣ��ཻ
				if (useRec.getCenterX() + useRec.getBounds2D().getWidth() / 2 >= crossPoint
						.getX()
						&& useRec.getCenterX()
								- useRec.getBounds2D().getWidth() / 2 <= crossPoint
								.getX()) {
					flag = true;
				}
			}
			if (flag) {
				crossLines.add(path);
			}
		}
		return crossLines;
	}

	/**
	 * ��ͼԪ�ƶ������У�ͼԪ�������ߵĵڶ������ڵ��ߣ������ӳ��ߣ��ཻʱ��Ҫ��������
	 * 
	 * @param path
	 * @param use
	 * @param useRec
	 * @param pathShape
	 * @return true��ʶ�ཻ�ˣ�false��ʶδ�ཻ
	 */
	protected boolean reunionCrossNextLine(final Set<Element> crossLines,
			final Element use, double xOffset, double yOffset) {
		// int flag = -1 ;//���ƶ�
		boolean flag = false;
		// List
		if (crossLines.size() == 1) {
			// flag = false;
			Element path = crossLines.iterator().next();
			Element next2ndEle = null;
			try {
				next2ndEle = path.getNextSibling() == null ? (Element) path
						.getPreviousSibling() : (Element) path.getNextSibling();
			} catch (Exception e) {
			}
			Rectangle2D useRec = this.getElementShape(use).getBounds2D();
			path.getParentNode().removeChild(path);

			NodeList connPaths = use.getChildNodes();
			String pathIdTobeRemove = ((Element) path).getAttribute("id");
			for (int i = 0; i < connPaths.getLength(); i++) {
				Node node = connPaths.item(i);
				if (node instanceof Element
						&& node.getNodeName().equals(
								FlowGraphicsModule.CONN_PATH_ELEMENT_NAME)) {
					Element connEle = (Element) node;
					if (connEle.getAttribute(
							FlowGraphicsModule.CONN_PATH_ELEMENT_ID).equals(
							pathIdTobeRemove)) {
						connEle.getParentNode().removeChild(connEle);
					}
				}
			}

			if (next2ndEle == null)
				return false;
			int position = getPosition(next2ndEle, use);
			// Point2D firstP = EditorToolkit.getCenterPoint(use);
			reunion(position, next2ndEle, use, useRec);
			if (next2ndEle.getNextSibling() == null) {
				EditorToolkit.appendMarker(next2ndEle);
			}

			// //��ֻ��1����·�������ߣ�Ҫ����Ϊ3��
			if (next2ndEle.getNextSibling() == null
					&& next2ndEle.getPreviousSibling() == null) {
				fillVHPath((Element) next2ndEle.getParentNode());
				// �ײ�ͼԪ������idҪ����µ�id
				Element idChangedEle = null;
				String oldid = next2ndEle.getAttribute("id");
				Element newEle = (Element) next2ndEle.getParentNode()
						.getChildNodes().item(
								next2ndEle.getParentNode().getChildNodes()
										.getLength() - 1);
				String newid = newEle.getAttribute("id");
				if (position == BOTTON_CONN) { // �ƶ�����β����ͼԪ
					try {
						NodeList nodes = Utilities.findNodes("//*[@pathid='"
								+ oldid + "']", use.getOwnerDocument());
						for (int n = 0; n < nodes.getLength(); n++) {
							Node node = nodes.item(n);
							if (node.getParentNode() != use) {
								idChangedEle = (Element) node.getParentNode();
								break;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (position == TOP_CONN) { // �ƶ������ײ���ͼԪ
					idChangedEle = use;
				}
				if (idChangedEle != null) {
					NodeList nodes = idChangedEle
							.getElementsByTagName(FlowGraphicsModule.CONN_PATH_ELEMENT_NAME);
					for (int n = 0; n < nodes.getLength(); n++) {
						Element connPath = (Element) nodes.item(n);
						if (connPath.getAttribute(
								FlowGraphicsModule.CONN_PATH_ELEMENT_ID)
								.equals(oldid)) {
							connPath.setAttribute(
									FlowGraphicsModule.CONN_PATH_ELEMENT_ID,
									newid);
						}
					}
				}
			}
			flag = true;

		}

		return flag;
	}

	// private List<Point2D> generateCornerPoints(Point2D pinPoint){
	// List<Point2D> points = new ArrayList<Point2D>();
	//		
	// return points;
	// }

	/**
	 * �������ͼԪ����·�Ĺ�ϵ
	 * 
	 * @param flag
	 *            ����·��ϵ�λ�ñ�ǣ�0��ʶβ��path��1��ʶ�ײ�path��-1����Ӧ
	 * @param path
	 * @param use
	 */
	protected void reunion(int flag, Element path, final Element use,
			Rectangle2D useRec) {
		final SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
		Runnable executeRunnable = null;
		Runnable undoRunnable = null;
		String attVal = path.getAttribute("d");

		// creating the path object corresponding to this attribute value
		Path pathShape = new Path(attVal);
		// Point2D end = pathShape.getSegment().getNextSegment().getEndPoint();
		final RoundRectangle2D useShape = getElementShape(use);
		final Element connPathEle = use.getOwnerDocument().createElement(
				CONN_PATH_ELEMENT_NAME);
		// //��ֻ��1����·�������ߣ�Ҫ����Ϊ3��
		// if(path.getNextSibling()==null && path.getPreviousSibling()==null){
		// Element e1 = (Element)path.cloneNode(false);
		// e1.setAttribute("d", EditorToolkit.getPathDAttrString(end, end));
		// Element e2 = (Element)e1.cloneNode(false);
		// path.removeAttribute("marker-end");
		// e2.setAttribute("marker-end", "url(#"
		// + EditorToolkit.MARKER_ID + ")");
		// path.getParentNode().appendChild(e1);
		// path.getParentNode().appendChild(e2);
		// e1.setAttribute("id", UUID.randomUUID().toString());
		// e2.setAttribute("id", UUID.randomUUID().toString());
		// path=e2;
		// }
		connPathEle.setAttribute(CONN_PATH_ELEMENT_ID, path.getAttribute("id"));
		double newX = -1;
		double newY = -1;

		switch (flag) {
		case -1:
			return;
		case BOTTON_CONN:
			if (pathShape.getBounds2D().getWidth() == 0) {// ��ֱ�ߣ�Ӧ������useͼԪ��south��������
				newX = pathShape.getBounds2D().getX()
						- useShape.getBounds2D().getWidth() / 2;
				if (isActivePath(pathShape)) {
					newY = pathShape.getBounds2D().getY()
							- useShape.getBounds2D().getHeight();
				} else {
					newY = pathShape.getBounds2D().getY()
							+ pathShape.getBounds2D().getHeight();
				}
			} else if (pathShape.getBounds2D().getHeight() == 0) {// ˮƽ�ߣ�Ӧ������useͼԪ��east��������
				if (isActivePath(pathShape)) {
					newX = pathShape.getBounds2D().getX()
							- useShape.getBounds2D().getWidth();
				} else {
					newX = pathShape.getBounds2D().getX()
							+ pathShape.getBounds2D().getWidth();
				}

				newY = pathShape.getBounds2D().getY()
						- useShape.getBounds2D().getHeight() / 2;
			}
			break;
		case TOP_CONN:
			if (pathShape.getBounds2D().getWidth() == 0) {// ��ֱ�ߣ�Ӧ������useͼԪ��south��������
				newX = pathShape.getBounds2D().getX()
						- useShape.getBounds2D().getWidth() / 2;
				if (isActivePath(pathShape)) {
					newY = pathShape.getBounds2D().getY()
							+ pathShape.getBounds2D().getHeight();

				} else {
					newY = pathShape.getBounds2D().getY()

					- useShape.getBounds2D().getHeight();
				}
			} else if (pathShape.getBounds2D().getHeight() == 0) {// ˮƽ�ߣ�Ӧ������useͼԪ��east��������
				if (isActivePath(pathShape)) {
					newX = pathShape.getBounds2D().getX()
							+ pathShape.getBounds2D().getWidth();
				} else {
					newX = pathShape.getBounds2D().getX()
							- useShape.getBounds2D().getWidth();
				}

				newY = pathShape.getBounds2D().getY()
						- useShape.getBounds2D().getHeight() / 2;
			}

			break;
		default:
			return;
		}
		final double newX_f = newX;
		final double newY_f = newY;
		final double padx = newX - useShape.getBounds2D().getX();
		final double pady = newY - useShape.getBounds2D().getY();
		// final Element textEle_f = textEle;
		final boolean containConnPath = containsConnPath(use, connPathEle);
		executeRunnable = new Runnable() {
			public void run() {

				EditorToolkit.setAttributeValue(use, "x", newX_f);
				EditorToolkit.setAttributeValue(use, "y", newY_f);

				if (!containConnPath)
					use.appendChild(connPathEle);
				centerTextOnUseRect(use);

				handle.getSelection().clearSelection();
			}
		};
		undoRunnable = new Runnable() {
			public void run() {
				if (!containConnPath)
					use.removeChild(connPathEle);

				EditorToolkit.setAttributeValue(use, "x", java.lang.Double
						.valueOf(use.getAttribute("x"))
						- padx);
				EditorToolkit.setAttributeValue(use, "y", java.lang.Double
						.valueOf(use.getAttribute("y"))
						- pady);

				centerTextOnUseRect(use);

			}
		};
		HashSet<Element> elements = new HashSet<Element>();
		elements.add(path);
		elements.add(use);
		UndoRedoAction undoRedoAction = ShapeToolkit.getUndoRedoAction("��·����",
				executeRunnable, undoRunnable, elements);

		UndoRedoActionList actionlist = new UndoRedoActionList("��·����", false);
		actionlist.add(undoRedoAction);
		editor.getHandlesManager().getCurrentHandle().getUndoRedo()
				.addActionList(actionlist, false);
	}

	private void translated(Element elementTrans, Point2D firstPoint,
			Point2D scaledPoint) {
		if (elementTrans.getNodeName().equals("use")
				|| elementTrans.getNodeName().equals("path")
				|| isFlowPartGroup(elementTrans)) {

			if (elementTrans.getNodeName().equals("use")) {
				reunion(elementTrans, firstPoint, scaledPoint);
			} else if (isFlowPartGroup(elementTrans)) {
				reunion((Element) elementTrans.getChildNodes().item(0),
						firstPoint, scaledPoint);
			} else if (EditorToolkit.isVHPath(elementTrans)) {
				brokeConn(elementTrans);
			}
		}
	}

	private void translated(final Set<Element> elementsTraslated,
			final Point2D firstPoint, final Point2D scaledPoint) {

		if (elementsTraslated.size() == 1) {

			final Element ele = elementsTraslated.iterator().next();
			translated(ele, firstPoint, scaledPoint);
		} else if (elementsTraslated.size() > 1) {
			multipleTranslate(elementsTraslated, firstPoint, scaledPoint);
		}
	}

	private boolean translateConnPath(Element useEle, Element pathEle,
			Point2D firstPoint, Point2D scaledPoint) {
		if (firstPoint == null || scaledPoint == null)
			return false;
		final double xOffset = firstPoint.getX() - scaledPoint.getX();
		final double yOffset = firstPoint.getY() - scaledPoint.getY();
		NodeList connPaths = useEle
				.getElementsByTagName(CONN_PATH_ELEMENT_NAME);
		// ��ȡ�ཻ��������Ŀ
		Rectangle2D useRec = getElementShape(useEle).getBounds2D();
		Set<Element> crossedLines = getCrossedLines(connPaths, useEle, useRec,
				xOffset, yOffset);
		// ֻ����1��ͼԪ����1��������ʱ���ſ��Խ����ཻ�߶ε�ƫ��
		boolean cross_transable = false;
		if (connPaths.getLength() > 1) {
			cross_transable = false;
		} else {
			cross_transable = reunionCrossNextLine(crossedLines, useEle,
					xOffset, yOffset);
		}
		// ��ͼԪ�����Ķ���������ཻʱ�����账��
		if (!cross_transable && crossedLines.size() > 0) {
			editor.getHandlesManager().getCurrentHandle().getUndoRedo()
					.undoLastAction();

		}
		if (crossedLines.size() == 0) {
			Element corelateEle = null;
			if (pathEle.getPreviousSibling() == null) {// ��һ��
				corelateEle = (Element) pathEle.getNextSibling();
			} else if (pathEle.getNextSibling() == null) {// ���һ��
				corelateEle = (Element) pathEle.getPreviousSibling();
			}
			Path path = new Path(pathEle.getAttribute("d"));
			Point2D start = path.getSegment().getEndPoint();
			Point2D end = path.getSegment().getNextSegment().getEndPoint();
			Rectangle2D useRect = getElementShape(useEle).getBounds2D();
			boolean isActivePath = isActivePath(pathEle);
			int flag = getPosition(isActivePath, pathEle, path, firstPoint,
					scaledPoint);
			if (path.getBounds2D().getWidth() == 0) { // ��ֱ

				switch (flag) {
				case BOTTON_CONN:
					end.setLocation(end.getX() - xOffset, end.getY());
					start.setLocation(start.getX() - xOffset, start.getY()
							- yOffset);
					break;
				case TOP_CONN:
					start.setLocation(start.getX() - xOffset, start.getY());
					end.setLocation(end.getX() - xOffset, end.getY() - yOffset);
					break;
				case TOP_CONVERSE_LEFT:
					start.setLocation(start.getX() - xOffset, start.getY());
					end.setLocation(end.getX() - xOffset, end.getY() - yOffset
							+ useRect.getHeight());
					break;
				case TOP_CONVERSE_RIGHT:
					start.setLocation(start.getX() - xOffset, start.getY());
					end.setLocation(end.getX() - xOffset, end.getY() - yOffset
							- useRect.getHeight());
					break;
				case BOTTON_CONVERSE_LEFT:
					start.setLocation(start.getX() - xOffset, start.getY()
							- yOffset + useRect.getHeight());
					end.setLocation(end.getX() - xOffset, end.getY());
					break;
				case BOTTON_CONVERSE_RIGHT:
					start.setLocation(start.getX() - xOffset, start.getY()
							- yOffset - useRect.getHeight());
					end.setLocation(end.getX() - xOffset, end.getY());
					break;
				default:
					editor.getLogger().log(this, LoggerAdapter.WARN, "δ�����ģʽ");
					break;
				}
			} else if (path.getBounds2D().getHeight() == 0) {// ˮƽ
				switch (flag) {
				case BOTTON_CONN:
					end.setLocation(end.getX(), end.getY() - yOffset);
					start.setLocation(start.getX() - xOffset, start.getY()
							- yOffset);
					break;
				case TOP_CONN:
					start.setLocation(start.getX(), start.getY() - yOffset);
					end.setLocation(end.getX() - xOffset, end.getY() - yOffset);
					break;
				case TOP_CONVERSE_LEFT:
					start.setLocation(start.getX(), start.getY() - yOffset);
					end.setLocation(end.getX() - xOffset + useRect.getWidth(),
							end.getY() - yOffset);
					break;
				case TOP_CONVERSE_RIGHT:
					start.setLocation(start.getX(), start.getY() - yOffset);
					end.setLocation(end.getX() - xOffset - useRect.getWidth(),
							end.getY() - yOffset);
					break;
				case BOTTON_CONVERSE_LEFT:
					start.setLocation(start.getX() - xOffset
							+ useRect.getWidth(), start.getY() - yOffset);
					end.setLocation(end.getX(), end.getY() - yOffset);
					break;
				case BOTTON_CONVERSE_RIGHT:
					start.setLocation(start.getX() - xOffset
							- useRect.getWidth(), start.getY() - yOffset);
					end.setLocation(end.getX(), end.getY() - yOffset);
					break;
				default:
					editor.getLogger().log(this, LoggerAdapter.WARN, "δ�����ģʽ");
					break;
				}
			}
			if (corelateEle != null) {
				Path corelatePath = new Path(corelateEle.getAttribute("d"));
				Point2D corelateStart = corelatePath.getSegment().getEndPoint();
				Point2D corelateEnd = corelatePath.getSegment()
						.getNextSegment().getEndPoint();
				Point2D changedPoint = null;
				if (isActivePath) {
					switch (flag) {
					case BOTTON_CONN:
						changedPoint = corelateStart;
						break;
					case TOP_CONN:
						changedPoint = corelateEnd;
						break;
					case TOP_CONVERSE_LEFT:
						changedPoint = corelateEnd;
						break;
					case TOP_CONVERSE_RIGHT:
						changedPoint = corelateEnd;
						break;
					case BOTTON_CONVERSE_LEFT:
						changedPoint = corelateStart;
						break;
					case BOTTON_CONVERSE_RIGHT:
						changedPoint = corelateStart;
						break;
					default:
						editor.getLogger().log(this, LoggerAdapter.WARN,
								"δ�����ģʽ");
						break;
					}

				} else {
					switch (flag) {
					case BOTTON_CONN:
						changedPoint = corelateStart;
						break;
					case TOP_CONN:
						changedPoint = corelateEnd;
						break;
					case TOP_CONVERSE_LEFT:
						changedPoint = corelateEnd;
						break;
					case TOP_CONVERSE_RIGHT:
						changedPoint = corelateEnd;
						break;
					case BOTTON_CONVERSE_LEFT:
						changedPoint = corelateStart;
						break;
					case BOTTON_CONVERSE_RIGHT:
						changedPoint = corelateStart;
						break;
					default:
						editor.getLogger().log(this, LoggerAdapter.WARN,
								"δ�����ģʽ");
						break;
					}
				}
				if (changedPoint != null) {
					if (path.getBounds2D().getWidth() == 0) {
						changedPoint.setLocation(changedPoint.getX() - xOffset,
								changedPoint.getY());
					} else if (path.getBounds2D().getHeight() == 0) {
						changedPoint.setLocation(changedPoint.getX(),
								changedPoint.getY() - yOffset);
					}
					corelateEle.setAttribute("d", EditorToolkit
							.getPathDAttrString(corelateStart, corelateEnd));
				}
			}

			pathEle.setAttribute("d", EditorToolkit.getPathDAttrString(start,
					end));

			Runnable executeRunnable = new Runnable() {
				public void run() {

				}
			};
			Runnable undoRunnable = new Runnable() {

				public void run() {

				}
			};
			HashSet<Element> elements = new HashSet<Element>();
			elements.add(useEle);
			UndoRedoAction undoRedoAction = ShapeToolkit.getUndoRedoAction(
					"�ƶ�������", executeRunnable, undoRunnable, elements);

			UndoRedoActionList actionlist = new UndoRedoActionList("�ƶ�������",
					false);
			actionlist.add(undoRedoAction);
			editor.getHandlesManager().getCurrentHandle().getUndoRedo()
					.addActionList(actionlist, false);

		}
		return cross_transable;
	}

	private void translateConnPath(Element useEle, Rectangle2D useRec,
			Point2D firstPoint, Point2D scaledPoint) {
		if (firstPoint == null || scaledPoint == null)
			return;
		final double xOffset = firstPoint.getX() - scaledPoint.getX();
		final double yOffset = firstPoint.getY() - scaledPoint.getY();
		NodeList connPaths = useEle
				.getElementsByTagName(CONN_PATH_ELEMENT_NAME);
		// ��ȡ�ཻ��������Ŀ
		Set<Element> crossedLines = getCrossedLines(connPaths, useEle, useRec,
				xOffset, yOffset);
		// ֻ����1��ͼԪ����1��������ʱ���ſ��Խ����ཻ�߶ε�ƫ��
		boolean cross_transable = false;
		if (connPaths.getLength() > 1) {
			cross_transable = false;
		} else {
			cross_transable = reunionCrossNextLine(crossedLines, useEle,
					xOffset, yOffset);
		}
		// ��ͼԪ�����Ķ���������ཻʱ�����账��
		if (!cross_transable && crossedLines.size() > 0) {
			// editor.getHandlesManager().getCurrentHandle().getUndoRedo()
			// .undoLastAction();

			redrawConnLiens(crossedLines, connPaths, useEle, useRec, xOffset,
					yOffset, firstPoint, scaledPoint);
		}

		if (crossedLines.size() == 0) {
			for (int i = 0; i < connPaths.getLength(); i++) {
				String id = ((Element) connPaths.item(i))
						.getAttribute(FlowGraphicsModule.CONN_PATH_ELEMENT_ID);
				Element pathEle = useEle.getOwnerDocument().getElementById(id);
				if (pathEle == null)
					continue;

				// if (crossedLines.contains(pathEle)) {
				// continue;
				// }
				Element corelateEle = null;
				if (pathEle.getPreviousSibling() == null) {// ��һ��
					corelateEle = (Element) pathEle.getNextSibling();
				} else if (pathEle.getNextSibling() == null) {// ���һ��
					corelateEle = (Element) pathEle.getPreviousSibling();
				}
				Path path = new Path(pathEle.getAttribute("d"));
				Point2D start = path.getSegment().getEndPoint();
				Point2D end = path.getSegment().getNextSegment().getEndPoint();
				Rectangle2D useRect = getElementShape(useEle).getBounds2D();
				boolean isActivePath = isActivePath(pathEle);
				int flag = getPosition(isActivePath, pathEle, path, firstPoint,
						scaledPoint);
				if (path.getBounds2D().getWidth() == 0) { // ��ֱ

					switch (flag) {
					case BOTTON_CONN:
						end.setLocation(end.getX() - xOffset, end.getY());
						start.setLocation(start.getX() - xOffset, start.getY()
								- yOffset);
						break;
					case TOP_CONN:
						start.setLocation(start.getX() - xOffset, start.getY());
						end.setLocation(end.getX() - xOffset, end.getY()
								- yOffset);
						break;
					case TOP_CONVERSE_LEFT:
						start.setLocation(start.getX() - xOffset, start.getY());
						end.setLocation(end.getX() - xOffset, end.getY()
								- yOffset + useRect.getHeight());
						break;
					case TOP_CONVERSE_RIGHT:
						start.setLocation(start.getX() - xOffset, start.getY());
						end.setLocation(end.getX() - xOffset, end.getY()
								- yOffset - useRect.getHeight());
						break;
					case BOTTON_CONVERSE_LEFT:
						start.setLocation(start.getX() - xOffset, start.getY()
								- yOffset + useRect.getHeight());
						end.setLocation(end.getX() - xOffset, end.getY());
						break;
					case BOTTON_CONVERSE_RIGHT:
						start.setLocation(start.getX() - xOffset, start.getY()
								- yOffset - useRect.getHeight());
						end.setLocation(end.getX() - xOffset, end.getY());
						break;
					default:
						editor.getLogger().log(this, LoggerAdapter.WARN,
								"δ�����ģʽ");
						break;
					}
				} else if (path.getBounds2D().getHeight() == 0) {// ˮƽ
					switch (flag) {
					case BOTTON_CONN:
						end.setLocation(end.getX(), end.getY() - yOffset);
						start.setLocation(start.getX() - xOffset, start.getY()
								- yOffset);
						break;
					case TOP_CONN:
						start.setLocation(start.getX(), start.getY() - yOffset);
						end.setLocation(end.getX() - xOffset, end.getY()
								- yOffset);
						break;
					case TOP_CONVERSE_LEFT:
						start.setLocation(start.getX(), start.getY() - yOffset);
						end.setLocation(end.getX() - xOffset
								+ useRect.getWidth(), end.getY() - yOffset);
						break;
					case TOP_CONVERSE_RIGHT:
						start.setLocation(start.getX(), start.getY() - yOffset);
						end.setLocation(end.getX() - xOffset
								- useRect.getWidth(), end.getY() - yOffset);
						break;
					case BOTTON_CONVERSE_LEFT:
						start.setLocation(start.getX() - xOffset
								+ useRect.getWidth(), start.getY() - yOffset);
						end.setLocation(end.getX(), end.getY() - yOffset);
						break;
					case BOTTON_CONVERSE_RIGHT:
						start.setLocation(start.getX() - xOffset
								- useRect.getWidth(), start.getY() - yOffset);
						end.setLocation(end.getX(), end.getY() - yOffset);
						break;
					default:
						editor.getLogger().log(this, LoggerAdapter.WARN,
								"δ�����ģʽ");
						break;
					}
				}
				if (corelateEle != null) {
					// vhpath.translated(elementTraslated)
					Path corelatePath = new Path(corelateEle.getAttribute("d"));
					Point2D corelateStart = corelatePath.getSegment()
							.getEndPoint();
					Point2D corelateEnd = corelatePath.getSegment()
							.getNextSegment().getEndPoint();
					Point2D changedPoint = null;
					if (isActivePath) {
						switch (flag) {
						case BOTTON_CONN:
							changedPoint = corelateStart;
							break;
						case TOP_CONN:
							changedPoint = corelateEnd;
							break;
						case TOP_CONVERSE_LEFT:
							changedPoint = corelateEnd;
							break;
						case TOP_CONVERSE_RIGHT:
							changedPoint = corelateEnd;
							break;
						case BOTTON_CONVERSE_LEFT:
							changedPoint = corelateStart;
							break;
						case BOTTON_CONVERSE_RIGHT:
							changedPoint = corelateStart;
							break;
						default:
							editor.getLogger().log(this, LoggerAdapter.WARN,
									"δ�����ģʽ");
							break;
						}

					} else {
						switch (flag) {
						case BOTTON_CONN:
							changedPoint = corelateStart;
							break;
						case TOP_CONN:
							changedPoint = corelateEnd;
							break;
						case TOP_CONVERSE_LEFT:
							changedPoint = corelateEnd;
							break;
						case TOP_CONVERSE_RIGHT:
							changedPoint = corelateEnd;
							break;
						case BOTTON_CONVERSE_LEFT:
							changedPoint = corelateStart;
							break;
						case BOTTON_CONVERSE_RIGHT:
							changedPoint = corelateStart;
							break;
						default:
							editor.getLogger().log(this, LoggerAdapter.WARN,
									"δ�����ģʽ");
							break;
						}
					}
					if (changedPoint != null) {
						if (path.getBounds2D().getWidth() == 0) {
							changedPoint.setLocation(changedPoint.getX()
									- xOffset, changedPoint.getY());
						} else if (path.getBounds2D().getHeight() == 0) {
							changedPoint.setLocation(changedPoint.getX(),
									changedPoint.getY() - yOffset);
						}
						corelateEle
								.setAttribute("d", EditorToolkit
										.getPathDAttrString(corelateStart,
												corelateEnd));
					}
				}

				pathEle.setAttribute("d", EditorToolkit.getPathDAttrString(
						start, end));

				Runnable executeRunnable = new Runnable() {
					public void run() {

					}
				};
				Runnable undoRunnable = new Runnable() {

					public void run() {

					}
				};
				HashSet<Element> elements = new HashSet<Element>();
				elements.add(useEle);
				UndoRedoAction undoRedoAction = ShapeToolkit.getUndoRedoAction(
						"�ƶ�������", executeRunnable, undoRunnable, elements);

				UndoRedoActionList actionlist = new UndoRedoActionList("�ƶ�������",
						false);
				actionlist.add(undoRedoAction);
				editor.getHandlesManager().getCurrentHandle().getUndoRedo()
						.addActionList(actionlist, false);

			}
		}
	}

	/**
	 * �ػ�������·
	 * 
	 * @param connPaths
	 * @param useEle
	 */
	private void redrawConnLiens(Set<Element> crossedLines, NodeList connPaths,
			Element useEle, Rectangle2D useRec, double xOffset, double yOffset,
			Point2D firstPoint, Point2D scaledPoint) {

		List<String> connIds = new ArrayList<String>();
		Set<Element> sameGroupEles = new HashSet<Element>();
		for (int i = 0; i < connPaths.getLength(); i++) {
			Element connPath = (Element) connPaths.item(i);
			String id = connPath
					.getAttribute(FlowGraphicsModule.CONN_PATH_ELEMENT_ID);
			connIds.add(id);
		}
		for (int i = 0; i < connIds.size(); i++) {
			String id = connIds.get(i);
			Element connPathEle = useEle.getOwnerDocument().getElementById(id);

			Element anotherSideConnPathEle = connPathEle.getPreviousSibling() == null ? (Element) connPathEle
					.getParentNode().getLastChild()
					: (Element) connPathEle.getParentNode().getFirstChild();
			// �ж��Ƿ���ͬһͼԪ������������ͬһgroup����·
			if (anotherSideConnPathEle != null
					&& connIds.contains(anotherSideConnPathEle
							.getAttribute("id"))) {
				sameGroupEles.add((Element) connPathEle.getParentNode());
				continue;
			}
			Path anotherSizeConnPathEle_Path = new Path(anotherSideConnPathEle
					.getAttribute("d"));

			NodeList anotherConnUses = getConnElement(anotherSideConnPathEle
					.getAttribute("id"));

			// �Ƿ��ǵ�һ����g�е������߽ڵ�
			boolean firstInVHGroup = connPathEle.getPreviousSibling() == null ? true
					: false;

			// �����ͼԪ�������ߣ��޹���ͼԪʱ�����ڵĵ�
			Point2D end = firstInVHGroup ? anotherSizeConnPathEle_Path
					.getSegment().getNextSegment().getEndPoint()
					: anotherSizeConnPathEle_Path.getSegment().getEndPoint();

			Element anotherConnUse = null;
			Rectangle2D anotherConnUseRect = null;
			if (anotherConnUses.getLength() > 0) {
				anotherConnUse = (Element) anotherConnUses.item(0)
						.getParentNode();
				anotherConnUseRect = getElementShape(anotherConnUse)
						.getBounds2D();
			} else {
				anotherConnUseRect = new Rectangle2D.Double(0, 0, 0, 0);
			}

			/*************************************************************/
			Point2D start = new Point2D.Double(useRec.getCenterX(), useRec
					.getCenterY());
			if (anotherConnUse != null)
				end = new Point2D.Double(anotherConnUseRect.getCenterX(),
						anotherConnUseRect.getCenterY());
			if (!firstInVHGroup) {// ��ʼλ��Ҫ����
				Point2D temp = start;
				start = end;
				end = temp;
			}
			SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
			Point2D scaledStart = handle.getTransformsManager().getScaledPoint(
					start, false);
			Point2D scaledEnd = handle.getTransformsManager().getScaledPoint(
					end, false);
			// ��ԭ�����ӹ�ϵɾ��
			brokeConn(connPathEle);
			brokeConn(anotherSideConnPathEle);
			// ��ԭ��������·ȫ��ɾ��
			Node parent = connPathEle.getParentNode();
			parent.getParentNode().removeChild(parent);
			if (firstInVHGroup) {
				flowDrawingHandler.firstConnEle = useEle;
			} else {
				flowDrawingHandler.firstConnEle = anotherConnUse;
			}
			flowDrawingHandler.mousePressed(handle, scaledStart);
			flowDrawingHandler.mouseReleased(handle, scaledStart);
			flowDrawingHandler.mouseMoved(handle, scaledEnd);
			flowDrawingHandler.mousePressed(handle, scaledEnd);

			flowDrawingHandler.mouseReleased(handle, scaledEnd);
			if (anotherConnUse == null) {
				flowDrawingHandler.mouseDoubleClicked(handle, scaledPoint);
			}
			// flowDrawingHandler.reset(handle);
			// List<Point2D> cornerPoints = new ArrayList<Point2D>();
			// boolean verticalStart = connPathEle_Path.getBounds2D().getWidth()
			// == 0 ? true
			// : false;
			// int anotherUsePosition = -1;
			// if (anotherConnUse != null) {
			// anotherUsePosition = getPosition(anotherSideConnPathEle,
			// anotherConnUse);
			// }
			// boolean reverse = generateRedrawCornerPoints(cornerPoints, start,
			// end, isActive, firstInVHGroup, verticalStart,
			// connPathEle_Path, anotherSizeConnPathEle_Path,
			// anotherConnUseRect, anotherUsePosition);
			//
			// redrawElements(cornerPoints, connPathEle, anotherSideConnPathEle,
			// reverse);
		}
		redrawSameGroup(sameGroupEles, xOffset, yOffset);
	}

	/**
	 * �ػ�����ͬһgroup��·ָ��ͬһͼԪ�����
	 * 
	 * @param sameGroup
	 * @param xOffset
	 * @param yOffset
	 */
	protected void redrawSameGroup(Set<Element> sameGroup, double xOffset,
			double yOffset) {
		SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
		for (Element group : sameGroup) {
			NodeList nodes = group.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				Element e = (Element) nodes.item(i);
				EditorToolkit.modifyBounds(handle, e, -xOffset, -yOffset);
			}
		}
	}

	/**
	 * �ж�ѡ���ƶ�Ԫ�غ��Ƿ���Ҫ�ƻ����ӹ�ϵ
	 * 
	 * @param translateElements
	 * @return
	 */
	private boolean isTranslatedBroken(Set<Element> translateElements) {
		boolean flag = false;
		for (Element e : translateElements) {
			if (!e.getNodeName().equals("use")) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	protected void multipleTranslate(Set<Element> translateElements,
			Point2D firstPoint, Point2D scaledPoint) {
		SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
		Document doc = handle.getCanvas().getDocument();
//		final double xOffset = firstPoint.getX() - scaledPoint.getX();
//		final double yOffset = firstPoint.getY() - scaledPoint.getY();
		
//		boolean isTranslatedBroken = isTranslatedBroken(translateElements);
		for (Element e : translateElements) {
			NodeList connPaths = e
					.getElementsByTagName(FlowGraphicsModule.CONN_PATH_ELEMENT_NAME);
			if (e.getNodeName().equals("use")) {
				for (int i = connPaths.getLength() - 1; i >= 0; i--) {
					Element node = (Element) connPaths.item(i);
					String id = node
							.getAttribute(FlowGraphicsModule.CONN_PATH_ELEMENT_ID);
					Element connPath = doc.getElementById(id);
					// ������ŵ�ͼԪ����·һ���ƶ����Ͳ��Ƶ����ӹ�ϵ���������ӹ�ϵ��������
					
					if (!translateElements.contains(connPath)
							&& !translateElements.contains(connPath
									.getParentNode())) {
						 e.removeChild(connPaths.item(i));
						//�ƶ�û��ѡ�����������·
						// Set<Element> pathRelateTrans = new
						// HashSet<Element>();
						// pathRelateTrans.add(connPath);
						// vhpath.translate(handle, pathRelateTrans,
						// new Point2D.Double(xOffset, yOffset), true);
					}
				}
			}
			// else if (VHPathShape.isVHPathGroup(e)) {
			// NodeList nodes = e.getChildNodes();
			// for (int i = 0; i < nodes.getLength(); i++) {
			// Element connPath = (Element) nodes.item(i);
			// NodeList connUses = getConnElement(connPath
			// .getAttribute("id"));
			// for (int n = 0; n < connUses.getLength(); n++) {
			// Node node = connUses.item(n);
			// Element useEle = (Element) node.getParentNode();
			// if (!translateElements.contains(useEle)) {
			// useEle.removeChild(node);
			// }
			// }
			// }
			// }
		}
	}

	/**
	 * �ƶ�����ʱ����Ҫ����������ͼԪ�����ӹ�ϵ
	 * 
	 * @param vhpathEle
	 */
	protected void brokeConn(final Element vhpathEle) {

		final NodeList connectedUses = getConnElement(vhpathEle
				.getAttribute("id"));
		for (int i = 0; i < connectedUses.getLength(); i++) {
			Node node = connectedUses.item(i);
			node.getParentNode().removeChild(node);
		}

		// Runnable executeRunnable = new Runnable() {
		// public void run() {
		//				
		// }
		// };
		// Runnable undoRunnable = new Runnable() {
		//
		// public void run() {
		// for (int i = 0; i < connectedUses.getLength(); i++) {
		// Node node = connectedUses.item(i);
		// node.getParentNode().appendChild(node);
		// }
		// }
		// };
		// HashSet<Element> elements = new HashSet<Element>();
		// elements.add(vhpathEle);
		// UndoRedoAction undoRedoAction = ShapeToolkit.getUndoRedoAction(
		// "�������ӹ�ϵ", executeRunnable, undoRunnable, elements);
		//
		// UndoRedoActionList actionlist = new UndoRedoActionList("�������ӹ�ϵ",
		// false);
		// actionlist.add(undoRedoAction);
		// editor.getHandlesManager().getCurrentHandle().getUndoRedo()
		// .addActionList(actionlist, false);
	}

	/**
	 * �ж��Ƿ��ǿɱ����ӵĲ���
	 * 
	 * @param groupElement
	 * @return
	 */
	public static boolean isFlowPartGroup(Element groupElement) {
		if (groupElement.getNodeName().equals("g")) {
			if (groupElement.getAttribute(VHPathShape.GROUP_STYLE).equals(
					ELEMENT_GROUP_PART_TYPE)) {
				return true;
			}
		}
		return false;
	}

	public RoundRectangle2D getElementShape(Element element) {
		if (element == null)
			return null;
		if (element.getNodeName().equals("use")) {
			RoundRectangle2D rectangle = null;

			// getting the bounds of the initial rectangle
			double initX = EditorToolkit.getAttributeValue(element, "x");
			double initY = EditorToolkit.getAttributeValue(element, "y");
			double initW = EditorToolkit.getAttributeValue(element, "width");
			double initH = EditorToolkit.getAttributeValue(element, "height");
			double initRX = EditorToolkit.getAttributeValue(element, "rx");
			double initRY = EditorToolkit.getAttributeValue(element, "ry");

			if (java.lang.Double.isNaN(initRX)) {

				initRX = 0;
			}

			if (java.lang.Double.isNaN(initRY)) {

				initRY = 0;
			}

			if (!java.lang.Double.isNaN(initX)
					&& !java.lang.Double.isNaN(initY)
					&& !java.lang.Double.isNaN(initW)
					&& !java.lang.Double.isNaN(initH)) {

				// creating the rectangle
				rectangle = new RoundRectangle2D.Double(initX, initY, initW,
						initH, 2 * initRX, 2 * initRY);
			}

			return rectangle;
		} else {
			editor.getLogger().log(this, LoggerAdapter.DEBUG,
					"getElementShape������" + element.getNodeName() + "�ڵ㻹δ����");
			return null;
		}
	}

	/**
	 * �ж���·������ƻ��Ƿ�����ƣ���������Ǵ������£��������ң���������Ǵ������ϣ���������
	 * 
	 * @param path
	 * @return
	 */
	protected boolean isActivePath(Path path) {
		Segment seg = path.getSegment();
		Point2D p0 = seg.getEndPoint();
		Point2D p1 = seg.getNextSegment().getEndPoint();
		if (p1.getX() - p0.getX() > 0 || p1.getY() - p0.getY() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * �ж���·������ƻ��Ƿ�����ƣ���������Ǵ������£��������ң���������Ǵ������ϣ���������
	 * 
	 * @param pathEle
	 * @return
	 */
	protected boolean isActivePath(Element pathEle) {
		String attVal = pathEle.getAttribute("d");
		Path pathShape = new Path(attVal);
		return isActivePath(pathShape);
	}

	/**
	 * �Զ����봹ֱˮƽ����·����֤���������߲�����ͬ����
	 * 
	 * @param gPathEle
	 */
	protected void fillVHPath(Element gPathEle) {
		if (VHPathShape.isVHPathGroup(gPathEle)) {
			NodeList nodeList = gPathEle.getChildNodes();
			if (nodeList.getLength() == 0)
				return;
			if (nodeList.getLength() == 1) {// Ҫ�������
				Element firstEle = (Element) nodeList.item(0);
				Path firstPath = new Path(firstEle.getAttribute("d"));
				Point2D firstStart = firstPath.getSegment().getEndPoint();
				Point2D lastEnd = firstPath.getSegment().getNextSegment()
						.getEndPoint();
				Element lastEle = (Element) firstEle.cloneNode(true);
				lastEle.setAttribute("id", UUID.randomUUID().toString());
				Element middleEle = (Element) firstEle.cloneNode(true);
				middleEle.setAttribute("id", UUID.randomUUID().toString());
				Point2D centerPoint = new Point2D.Double(firstPath
						.getBounds2D().getCenterX(), firstPath.getBounds2D()
						.getCenterY());
				middleEle.setAttribute("d", EditorToolkit.getPathDAttrString(
						centerPoint, centerPoint));
				firstEle.setAttribute("d", EditorToolkit.getPathDAttrString(
						firstStart, centerPoint));
				lastEle.setAttribute("d", EditorToolkit.getPathDAttrString(
						centerPoint, lastEnd));
				EditorToolkit.appendMarker(lastEle);
				firstEle.removeAttribute("marker-end");
				middleEle.removeAttribute("marker-end");
				gPathEle.appendChild(middleEle);
				gPathEle.appendChild(lastEle);
			} else {
				Element firstEle = (Element) nodeList.item(0);
				Path previousPath = new Path(firstEle.getAttribute("d"));
				List<Element> eles = new ArrayList<Element>();
				for (int i = 1; i < nodeList.getLength(); i++) {
					eles.add((Element) nodeList.item(i));
				}
				for (Element next : eles) {// ����������·���ͬ����ҲҪ����ڵ�
					Path nextPath = new Path(next.getAttribute("d"));
					if ((previousPath.getBounds2D().getWidth() == 0 && nextPath
							.getBounds2D().getWidth() == 0)
							&& (previousPath.getBounds2D().getHeight() == 0 && nextPath
									.getBounds2D().getHeight() == 0)) {// ����һ���㣬û�п�Ⱥ͸߶�
						next.getParentNode().removeChild(next);
						continue;
					}
					// ���ڶ��Ǵ�ֱ�� or ˮƽ��
					else if ((previousPath.getBounds2D().getWidth() == 0 && nextPath
							.getBounds2D().getWidth() == 0)
							|| (previousPath.getBounds2D().getHeight() == 0 && nextPath
									.getBounds2D().getHeight() == 0)) {
						Element newEle = (Element) firstEle.cloneNode(true);
						newEle.setAttribute("id", UUID.randomUUID().toString());
						newEle.setAttribute("d", EditorToolkit
								.getPathDAttrString(nextPath.getSegment()
										.getEndPoint(), nextPath.getSegment()
										.getEndPoint()));
						gPathEle.insertBefore(newEle, next);
					}
					previousPath = nextPath;
				}
			}
		} else {
			editor.getLogger().log(
					this,
					LoggerAdapter.WARN,
					"������Ҫ���group path element:"
							+ Utilities.printNode(gPathEle, false));
		}
	}

	/**
	 * ���½�������·��ͼԪ�������ӹ�ϵ
	 * 
	 * @param gPathEle
	 *            �½����������߼���
	 * @param firstConnElement
	 *            ��һ������ͼԪ
	 * @param lastConnElement
	 *            ���һ������ͼԪ
	 */
	protected void createRelation(Element gPathEle, Element firstConnElement,
			Element lastConnElement) {
		NodeList childPaths = gPathEle.getChildNodes();
		if (childPaths.getLength() > 0) {
			if (firstConnElement != null) {
				Element connPath = (Element) childPaths.item(0);
				createRelation(connPath.getAttribute("id"), firstConnElement);
			}
			if (lastConnElement != null) {
				Element connPath = (Element) childPaths.item(childPaths
						.getLength() - 1);
				createRelation(connPath.getAttribute("id"), lastConnElement);
			}
		}
	}

	/**
	 * ���½�������·��ͼԪ�������ӹ�ϵ
	 * 
	 * @param pathId
	 *            ������·��id
	 * @param connElement
	 *            �����ӵ�ͼԪ
	 */
	private void createRelation(String pathId, Element connElement) {
		Element connPathEle = connElement.getOwnerDocument().createElement(
				FlowGraphicsModule.CONN_PATH_ELEMENT_NAME);
		connPathEle.setAttribute(FlowGraphicsModule.CONN_PATH_ELEMENT_ID,
				pathId);
		connElement.appendChild(connPathEle);
	}

	/**
	 * �ж��Ƿ��Ѿ��������ӹ�ϵ
	 * 
	 * @param parentEle
	 * @param connPathEle
	 * @return
	 */
	private boolean containsConnPath(Element parentEle, Element connPathEle) {
		String connPathId = connPathEle
				.getAttribute(FlowGraphicsModule.CONN_PATH_ELEMENT_ID);
		NodeList nodes = parentEle
				.getElementsByTagName(FlowGraphicsModule.CONN_PATH_ELEMENT_NAME);
		for (int i = 0; i < nodes.getLength(); i++) {
			Element e = (Element) nodes.item(i);
			if (connPathId.equals(e
					.getAttribute(FlowGraphicsModule.CONN_PATH_ELEMENT_ID)))
				return true;
		}
		return false;
	}

	/**
	 * �ж��Ƿ���������߹���
	 * 
	 * @param parentEle
	 * @return
	 */
	protected boolean hasConnPath(Element parentEle) {
		return parentEle.getElementsByTagName(
				FlowGraphicsModule.CONN_PATH_ELEMENT_NAME).getLength() > 0 ? true
				: false;
	}

	/**
	 * ����ͼԪʱ�Զ�ƥ��
	 * 
	 * @param useEle
	 */
	private void autoAjustCreatedUseShape(Element useEle) {
		if (useEle.getNodeName().equals("use")) {
			reunion(useEle, null, null);
		}
	}

	/**
	 * ��ȡͼԪ������·���ڵ�λ��
	 * 
	 * @param handle
	 * @param path
	 * @param selectBounds
	 * @param vhChildPaths
	 * @return
	 */
	public int getPosition(Element path, Element use) {
		int position = -1; // paths at middle
		if (path.getPreviousSibling() == null && path.getNextSibling() == null) {
			Path pathShape = new Path(path.getAttribute("d"));
			Point2D start = pathShape.getSegment().getEndPoint();
			Point2D end = pathShape.getSegment().getNextSegment().getEndPoint();
			Point2D useCenterPoint = EditorToolkit.getCenterPoint(use);
			boolean startNexter = start.distance(useCenterPoint) <= end
					.distance(useCenterPoint);
			position = startNexter ? BOTTON_CONN : TOP_CONN;
			// if(isActivePath(pathShape)){
			//				
			// }else{
			// position = startNexter?TOP_CONN:BOTTON_CONN;
			// }
		} else if (path.getPreviousSibling() == null) {
			position = BOTTON_CONN;// β��
		} else if (path.getNextSibling() == null) {
			position = TOP_CONN;// �ײ�
		}
		return position;
	}

	/**
	 * ��ȡͼԪ����·��λ��
	 * 
	 * @param isActivePath
	 * @param path
	 * @param firstPoint
	 * @param scaledPoint
	 * @return
	 */
	private int getPosition(boolean isActivePath, Element pathEle, Path path,
			Point2D firstPoint, Point2D scaledPoint) {
		int flag = -1;// 0��ʶβ��������1��ʶ�ײ�����
		boolean firstPath = pathEle.getPreviousSibling() == null;
		if (isActivePath) {
			if (path.getBounds2D().getWidth() == 0) {// ��ֱ
				double verticalOffset = scaledPoint.getY() - firstPoint.getY();
				if (firstPath) {
					if (verticalOffset > path.getBounds2D().getHeight()) {
						flag = BOTTON_CONVERSE_RIGHT;
					} else {
						flag = BOTTON_CONN;
					}
				} else {
					if (-verticalOffset > path.getBounds2D().getHeight()) {
						flag = TOP_CONVERSE_LEFT;
					} else {
						flag = TOP_CONN;
					}
				}
			} else if (path.getBounds2D().getHeight() == 0) {// ˮƽ
				double horizonOffset = scaledPoint.getX() - firstPoint.getX();
				if (firstPath) {
					if (horizonOffset > path.getBounds2D().getWidth()) {
						flag = BOTTON_CONVERSE_RIGHT;
					} else {
						flag = BOTTON_CONN;
					}
				} else {
					if (-horizonOffset > path.getBounds2D().getWidth()) {
						flag = TOP_CONVERSE_LEFT;
					} else {
						flag = TOP_CONN;
					}
				}
			}
			/*************************************/
			// if (firstStartLonger == scaledStartLonger)
			// flag = firstStartLonger ? TOP_CONN : BOTTON_CONN;
			// else
			// flag = firstStartLonger ? TOP_CONVERSE_LEFT
			// : BOTTON_CONVERSE_RIGHT;
		} else {
			if (path.getBounds2D().getWidth() == 0) {// ��ֱ
				double verticalOffset = scaledPoint.getY() - firstPoint.getY();
				if (firstPath) {
					if (-verticalOffset > path.getBounds2D().getHeight()) {
						flag = BOTTON_CONVERSE_LEFT;
					} else {
						flag = BOTTON_CONN;
					}
				} else {
					if (verticalOffset > path.getBounds2D().getHeight()) {
						flag = TOP_CONVERSE_RIGHT;
					} else {
						flag = TOP_CONN;
					}
				}
			} else if (path.getBounds2D().getHeight() == 0) {// ˮƽ
				double horizonOffset = scaledPoint.getX() - firstPoint.getX();
				if (firstPath) {
					if (-horizonOffset > path.getBounds2D().getWidth()) {
						flag = BOTTON_CONVERSE_LEFT;
					} else {
						flag = BOTTON_CONN;
					}
				} else {
					if (horizonOffset > path.getBounds2D().getWidth()) {
						flag = TOP_CONVERSE_RIGHT;
					} else {
						flag = TOP_CONN;
					}
				}
			}
			/*************************************/
			// if (firstStartLonger == scaledStartLonger)
			// flag = firstStartLonger ? TOP_CONN : BOTTON_CONN;
			// else
			// flag = firstStartLonger ? TOP_CONVERSE_RIGHT
			// : BOTTON_CONVERSE_LEFT;
		}
		return flag;
	}

	/**
	 * ����ͼԪʱ�Զ�ƥ��
	 * 
	 * @param useEle
	 */
	private void autoAjustResizedUseShape(Element useEle, Shape oldShape,
			Shape newShape) {
		if (useEle.getNodeName().equals("use")) {

			Point2D firstPoint = new Point2D.Double(oldShape.getBounds2D()
					.getCenterX(), oldShape.getBounds2D().getCenterY());
			Point2D scaledPoint = new Point2D.Double(newShape.getBounds2D()
					.getCenterX(), newShape.getBounds2D().getCenterY());
			Rectangle2D newRect = newShape.getBounds2D();
			Rectangle2D oldRect = oldShape.getBounds2D();
			NodeList connPaths = useEle
					.getElementsByTagName(CONN_PATH_ELEMENT_NAME);
			for (int i = 0; i < connPaths.getLength(); i++) {
				String id = ((Element) connPaths.item(i))
						.getAttribute(FlowGraphicsModule.CONN_PATH_ELEMENT_ID);
				Element pathEle = useEle.getOwnerDocument().getElementById(id);
				if (pathEle == null)
					continue;
				boolean isActivePath = isActivePath(pathEle);
				Path path = new Path(pathEle.getAttribute("d"));
				int flag = getPosition(isActivePath, pathEle, path, firstPoint,
						scaledPoint);
				Point2D temp = new Point2D.Double();
				double x = newRect.getCenterX(), y = newRect.getCenterY();
				if (isActivePath) {
					if (path.getBounds2D().getWidth() == 0) { // ��ֱ
						switch (flag) {
						case BOTTON_CONN:
							y = newRect.getCenterY() + newRect.getHeight() / 2
									- oldRect.getHeight() / 2;
							break;
						case TOP_CONN:
							y = newRect.getCenterY() - newRect.getHeight() / 2
									+ oldRect.getHeight() / 2;
							break;
						case TOP_CONVERSE_LEFT:

							break;
						case TOP_CONVERSE_RIGHT:

							break;
						case BOTTON_CONVERSE_LEFT:

							break;
						case BOTTON_CONVERSE_RIGHT:

							break;
						default:
							editor.getLogger().log(this, LoggerAdapter.WARN,
									"δ�����ģʽ");
							break;
						}

					} else if (path.getBounds2D().getHeight() == 0) { // ˮƽ
						switch (flag) {
						case BOTTON_CONN:
							x = newRect.getCenterX() + newRect.getWidth() / 2
									- oldRect.getWidth() / 2;
							break;
						case TOP_CONN:
							x = newRect.getCenterX() - newRect.getWidth() / 2
									+ oldRect.getWidth() / 2;
							break;
						case TOP_CONVERSE_LEFT:

							break;
						case TOP_CONVERSE_RIGHT:

							break;
						case BOTTON_CONVERSE_LEFT:

							break;
						case BOTTON_CONVERSE_RIGHT:

							break;
						default:
							editor.getLogger().log(this, LoggerAdapter.WARN,
									"δ�����ģʽ");
							break;
						}

					}
				} else {
					if (path.getBounds2D().getWidth() == 0) { // ��ֱ
						switch (flag) {
						case BOTTON_CONN:
							y = newRect.getCenterY() - newRect.getHeight() / 2
									+ oldRect.getHeight() / 2;
							break;
						case TOP_CONN:
							y = newRect.getCenterY() + newRect.getHeight() / 2
									- oldRect.getHeight() / 2;
							break;
						case TOP_CONVERSE_LEFT:

							break;
						case TOP_CONVERSE_RIGHT:

							break;
						case BOTTON_CONVERSE_LEFT:

							break;
						case BOTTON_CONVERSE_RIGHT:

							break;
						default:
							editor.getLogger().log(this, LoggerAdapter.WARN,
									"δ�����ģʽ");
							break;
						}

					} else if (path.getBounds2D().getHeight() == 0) { // ˮƽ
						switch (flag) {
						case BOTTON_CONN:
							x = newRect.getCenterX() - newRect.getWidth() / 2
									+ oldRect.getWidth() / 2;
							break;
						case TOP_CONN:
							x = newRect.getCenterX() + newRect.getWidth() / 2
									- oldRect.getWidth() / 2;
							break;
						case TOP_CONVERSE_LEFT:

							break;
						case TOP_CONVERSE_RIGHT:

							break;
						case BOTTON_CONVERSE_LEFT:

							break;
						case BOTTON_CONVERSE_RIGHT:

							break;
						default:
							editor.getLogger().log(this, LoggerAdapter.WARN,
									"δ�����ģʽ");
							break;
						}

					}
				}
				temp.setLocation(x, y);
				boolean transable = translateConnPath(useEle, pathEle,
						firstPoint, temp);
			}
		}
		centerTextOnUseRect(useEle);
	}

	/**
	 * Ԫ��ɾ����Ӱ��
	 * 
	 * @param parentNodesMap
	 * @param elementsToDelete
	 */
	private void deleteElement(final HashMap<Element, Element> parentNodesMap,
			final List<Element> elementsToDelete) {

		if (isFlowSupportFileType(editor.getHandlesManager().getCurrentHandle()
				.getCanvas().getFileType())) {
			for (Element e : elementsToDelete) {
				if (e.getNodeName().equals("path")) {
					String id = e.getAttribute("id");
					try {
						NodeList connectedEle = Utilities.findNodes("//*[@"
								+ CONN_PATH_ELEMENT_ID + "='" + id + "']", e
								.getOwnerDocument());
						for (int i = 0; i < connectedEle.getLength(); i++) {
							connectedEle.item(i).getParentNode().removeChild(
									connectedEle.item(i));
						}
					} catch (XPathExpressionException e1) {
						e1.printStackTrace();
					}
					if (parentNodesMap.get(e).getNodeName().equals("g")
							&& parentNodesMap.get(e).getChildNodes()
									.getLength() == 0) {
						try {
							parentNodesMap.get(e).getParentNode().removeChild(
									parentNodesMap.get(e));
						} catch (Exception ex) {
						}
					}
				} else if (e.getNodeName().equals("g")) {
					List<Element> children = new ArrayList<Element>();
					HashMap<Element, Element> subParentMap = new HashMap<Element, Element>();
					NodeList nodes = e.getChildNodes();
					for (int i = 0; i < nodes.getLength(); i++) {
						children.add((Element) nodes.item(i));
						subParentMap.put((Element) nodes.item(i), e);
						deleteElement(subParentMap, children);
					}
				}
			}
		}

	}

	/**
	 * ȡ��ɾ����Ӱ��
	 * 
	 * @param parentNodesMap
	 * @param elementsToDelete
	 */
	private void undoElementDelete(HashMap<Element, Element> parentNodesMap,
			List<Element> elementsToDelete) {
		// FIXME
		editor.getLogger().log(this, LoggerAdapter.INFO,
				"UNDO not implemented yet!");
	}

	private boolean isFlowSupportFileType(String fileType) {
		// FIXME
		return true;
	}

	private boolean checkElementDeleteable(Element ele,
			List<Element> elementsToDelete) {
		boolean flag = true;
		// Element gEle = (Element) ele.getParentNode();
		// if (VHPathShape.isVHPathGroup(gEle)) {
		// if (elementsToDelete.size() == 1
		// && gEle.getChildNodes().getLength() > 1) {
		// flag = false;
		// } else {
		// for (Element e : elementsToDelete) {
		// if (!e.getParentNode().equals(gEle) ){
		// // Utilities.printNode(e.getParentNode(),true);
		// // Utilities.printNode(gEle, true);
		// flag = false;
		// break;
		// }
		// }
		// }
		// }
		return flag;
	}

	/**
	 * �Ƿ��Ǳ༭�ı���˫��
	 */
	protected boolean editTextClick = true;

	final protected Timer doubleClickTimer = new Timer(500,
			new DoubleClickAvoider());

	/**
	 * Ϊ������ƽ�����˫���ͱ༭�ı���˫������������ö�ʱ�Զ��ı�ķ�ʽ�����
	 * 
	 * @author qi
	 * 
	 */
	private class DoubleClickAvoider implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			editTextClick = true;
			doubleClickTimer.stop();
		}

	}

	public void appendConnRelation(Element useEle, Element vhPathEle) {
		Element connPathEle = useEle.getOwnerDocument().createElement(
				CONN_PATH_ELEMENT_NAME);
		connPathEle.setAttribute(FlowGraphicsModule.CONN_PATH_ELEMENT_ID,
				vhPathEle.getAttribute("id"));
		useEle.appendChild(connPathEle);
	}

}
