package nci.dl.svg.tq.oper;

import java.awt.Cursor;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import nci.dl.svg.tq.TQContinuesDrawingHandler;
import nci.dl.svg.tq.TQShape;
import nci.dl.svg.tq.TQToolkit;
import nci.dl.svg.tq.TQContinuesDrawingHandler.PathTypeEnum;
import nci.dl.svg.tq.oper.common.TQDrawOperation;

import org.apache.batik.ext.awt.geom.ExtendedGeneralPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.nci.svg.district.DistrictTopology;
import com.nci.svg.module.TerminalModule;
import com.nci.svg.sdk.client.selection.ModeSelectionEvent;
import com.nci.svg.sdk.client.selection.ModeSelectionListener;
import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.client.util.EditorToolkit;

import fr.itris.glips.library.geom.path.Path;
import fr.itris.glips.library.geom.path.segment.Segment;
import fr.itris.glips.svgeditor.display.canvas.SVGCanvas;
import fr.itris.glips.svgeditor.display.handle.HandlesListener;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.display.undoredo.UndoRedoAction;
import fr.itris.glips.svgeditor.display.undoredo.UndoRedoActionList;
import fr.itris.glips.svgeditor.shape.ShapeToolkit;

/**
 * ����ͬ����·����
 * 
 * @author qi
 * 
 */
public class TQDrawTonghuiOperation extends TQDrawOperation {

	/**
	 * ͬ����ʼ����
	 */
	private Element thStartEle;
	/**
	 * ͬ�ؽ�������
	 */
	private Element thEndEle;

	/**
	 * �����ͨ������·���ԣ�ƫ�����Ƿ�䶯��
	 */
	private boolean offsetChanged = false;

	/**
	 * �����������С��Χ
	 */
	private static double acceptRange = 0.01;

	/**
	 * ƫ�����ݼ��ķ�Χ
	 */
	private static double acceptPlusOffset = 0.01;

	/**
	 * û����ͬ�ظ���ƥ��ǰ��ƫ����
	 */
	private double previousOffset;

	/**
	 * ͬ�ص���ʼsegment���
	 */
	private int thSegStartIndex = -1;
	/**
	 * ͬ�صĽ�βsegment���
	 */
	private int thSegEndIndex = -1;

	public TQDrawTonghuiOperation(TQShape tqModule) {
		super(tqModule);
	}

	@Override
	public Element createDrawedElement(SVGHandle handle, Shape continuesShape,
			String lineID) {

		// ���ƵĺϷ����ж�
		if (!checkLegality(handle)) {
			return null;
		}

		String symbolName = null;

		String symbolStatus = null;

		Document doc = handle.getScrollPane().getSVGCanvas().getDocument();
		// g���
		Element gElement = null;
		gElement = doc.createElementNS(doc.getDocumentElement()
				.getNamespaceURI(), "g");
		gElement.setAttribute("id", lineID);
		if (thStartEle != null)
			gElement.setAttribute(TQToolkit.MODEL_ATTR, ((Element) thStartEle
					.getParentNode()).getAttribute(TQToolkit.MODEL_ATTR));
		else
			gElement.setAttribute(TQToolkit.MODEL_ATTR,
					TQToolkit.MODEL_MAIN_LINE);
		symbolName = TQToolkit.POLE_NAME;
		symbolStatus = "����";
		Path path = null;
		// ���ȸ���continuesShape����ͼԪ
		if (continuesShape != null
				&& continuesShape instanceof ExtendedGeneralPath) {
			path = new Path((ExtendedGeneralPath) continuesShape);
		}
		List<Element> crossedTonghuiPoles = null;
		Element thPathEle = null;
		if (path != null) {
			int num = path.getSegmentsNumber();
			Point2D beginPoint = null;
			Point2D endPoint = null;
			Segment seg = path.getSegment();
			endPoint = seg.getEndPoint();
			Point2D beforePoint = null;
			Point2D afterPoint = null;
			String beforeID = null, afterID = null;
			Element firstElement = null;
			for (int i = 0; i < num - 1; i++) {
				seg = seg.getNextSegment();
				beginPoint = endPoint;
				endPoint = seg.getEndPoint();
				double distance = Math.sqrt((beginPoint.getX() - endPoint
						.getX())
						* (beginPoint.getX() - endPoint.getX())
						+ (beginPoint.getY() - endPoint.getY())
						* (beginPoint.getY() - endPoint.getY()));
				long size = Math.round(distance / offsetList.get(i)) + 1;
				Rectangle2D.Double rect = new Rectangle2D.Double();
				rect.x = beginPoint.getX() - TQToolkit.TQ_GRAPH_DEFAULT_WIDTH
						/ 2;
				rect.y = beginPoint.getY() - TQToolkit.TQ_GRAPH_DEFAULT_HEIGHT
						/ 2;
				rect.width = TQToolkit.TQ_GRAPH_DEFAULT_WIDTH;
				rect.height = TQToolkit.TQ_GRAPH_DEFAULT_HEIGHT;
				if (seg.getSegmentIndex() == 1) {
					Element startEle = EditorToolkit.insertSymbolElement(
							handle, rect, gElement, null, symbolName,
							symbolStatus);
					// ��һ������Ϊ���е��ն˸�
					TQToolkit.setToTerminalPole(startEle, lineID);
					afterID = startEle.getAttribute("id");
					afterPoint = beginPoint;
				}

				firstElement = (Element) gElement.getFirstChild();
				if (i != thSegStartIndex) {
					for (int j = 1; j < size; j++) {
						beforePoint = afterPoint;
						beforeID = afterID;
						afterPoint = getPoint(beginPoint, endPoint, distance,
								j, offsetList.get(i));
						rect.x = afterPoint.getX()
								- TQToolkit.TQ_GRAPH_DEFAULT_WIDTH / 2;
						rect.y = afterPoint.getY()
								- TQToolkit.TQ_GRAPH_DEFAULT_HEIGHT / 2;
						rect.width = TQToolkit.TQ_GRAPH_DEFAULT_WIDTH;
						rect.height = TQToolkit.TQ_GRAPH_DEFAULT_HEIGHT;
						if (i == thSegStartIndex - 1 && j == size - 1) {
							afterPoint = EditorToolkit
									.getCenterPoint(thStartEle);
							afterID = thStartEle.getAttribute("id");
						} else {
							Element otherEle = EditorToolkit
									.insertSymbolElement(handle, rect,
											gElement, null, symbolName,
											symbolStatus);

							if (firstElement == null)
								firstElement = otherEle;
							if (j == size - 1) {
								// ���ڻ��Ƹ��߻�֧����˵�����Ľڵ����������
								if (i == num - 2)
									TQToolkit.setToTerminalPole(otherEle,
											lineID);
								// �������м�ת�۸���ת�Ǹ�
								else {
									TQToolkit.setToCornerPole(otherEle, lineID);
								}
							}
							afterID = otherEle.getAttribute("id");
						}
						if (i == thSegEndIndex && j == 1) {
							afterPoint = EditorToolkit.getCenterPoint(thEndEle);
							beforeID = thEndEle.getAttribute("id");
						}
						Element pathEle = EditorToolkit.insertPathElement(
								handle, gElement, firstElement, beforePoint,
								afterPoint, beforeID, afterID,
								TerminalModule.CENTER_TERMINAL_NAME,
								TerminalModule.CENTER_TERMINAL_NAME);
						// ����Ϊ������·
						TQToolkit.addMetadata(pathEle,
								TQToolkit.TQ_METADATA_PROPERTIES,
								TQToolkit.TQ_METADATA_LINE_TYPE,
								TQToolkit.TQ_METADATA_BASE_LINE, false);
					}
				} else {
					// ����ͬ����·path
					beforePoint = afterPoint;
					afterPoint = EditorToolkit.getCenterPoint(thEndEle);
					beforeID = thStartEle.getAttribute("id");
					afterID = thEndEle.getAttribute("id");
					thPathEle = EditorToolkit.insertPathElement(handle,
							gElement, firstElement, beforePoint, afterPoint,
							beforeID, afterID,
							TerminalModule.CENTER_TERMINAL_NAME,
							TerminalModule.CENTER_TERMINAL_NAME);
					crossedTonghuiPoles = convertTonghuiPath(thPathEle);
					TQToolkit.addMetadata(thPathEle,
							TQToolkit.TQ_METADATA_PROPERTIES,
							TQToolkit.TQ_METADATA_LINE_TYPE,
							TQToolkit.TQ_METADATA_TONGHUI_LINE, false);
				}
			}
		}

		EditorToolkit.insertCurSymbolByName(handle, symbolName);
		insertShapeElement(handle, thPathEle, gElement, crossedTonghuiPoles);
		return gElement;
	}

	/**
	 * ��svghandle���������½���ͼ�Σ��Լ�������ص�ͬ����Ϣ
	 * 
	 * @param handle
	 * @param thPathEle
	 * @param shapeElement
	 * @param crossedTonghuiPoles
	 */
	private void insertShapeElement(final SVGHandle handle,
			final Element thPathEle, final Element shapeElement,
			final List<Element> crossedTonghuiPoles) {
		final Element parentElement = handle.getSelection().getParentElement();

		// the execute runnable
		Runnable executeRunnable = new Runnable() {

			public void run() {

				parentElement.appendChild(shapeElement);
				handle.getSelection().clearSelection();
				handle.getSelection().handleSelection(shapeElement, false,
						false);
				String strID = shapeElement.getAttribute("id");
				if (strID != null && strID.length() > 0) {
					handle.getCanvas().appendElement(strID, shapeElement);
				}
				for (Element e : crossedTonghuiPoles) {
					TQToolkit.addMetadata(e,
							TQToolkit.TQ_METADATA_TONGHUI_LINE,
							TQToolkit.TQ_METADATA_LINE_ID, shapeElement
									.getAttribute("id"), false);
				}
				Set<Element> sets = new HashSet<Element>();
				sets.add(shapeElement);
				handle.getEditor().getPropertyModelInteractor()
						.getGraphProperty().setElement(sets);
			}

		};

		// the undo runnable
		Runnable undoRunnable = new Runnable() {

			public void run() {

				String strID = shapeElement.getAttribute("id");
				if (strID != null && strID.length() > 0) {
					handle.getCanvas().deleteElement(strID, shapeElement);
				}
				for (Element e : crossedTonghuiPoles) {
					TQToolkit.removeMetadata(e,
							TQToolkit.TQ_METADATA_TONGHUI_LINE,
							TQToolkit.TQ_METADATA_LINE_ID, shapeElement
									.getAttribute("id"));
				}
				parentElement.removeChild(shapeElement);
				handle.getEditor().getPropertyModelInteractor()
						.getGraphProperty().setElement(null);
			}
		};

		// executing the action and creating the undo/redo action
		HashSet<Element> elements = new HashSet<Element>();
		elements.add(shapeElement);
		UndoRedoAction undoRedoAction = ShapeToolkit.getUndoRedoAction(tqModule
				.getCurrentAction(), executeRunnable, undoRunnable, elements);

		UndoRedoActionList actionlist = new UndoRedoActionList(tqModule
				.getCurrentAction(), false);
		actionlist.add(undoRedoAction);
		handle.getUndoRedo().addActionList(actionlist, false);
	}

	/**
	 * �ж�ͬ�ػ��ƵĺϷ��ԣ���������ϣ��Ͳ��������ͬ��
	 * 
	 * @param handle
	 * @return
	 */
	private boolean checkLegality(SVGHandle handle) {
		boolean flag = true;
		if (thStartEle == null || thEndEle == null) {
			JOptionPane.showMessageDialog(handle.getCanvas(), "ͬ����·���뾭����������!",
					"��ʾ", JOptionPane.INFORMATION_MESSAGE);
			flag = false;
		} else if (thStartEle.getParentNode() != thEndEle.getParentNode()) {
			JOptionPane.showMessageDialog(handle.getCanvas(),
					"ͬ����·��������������������ͬһ��·��!", "��ʾ",
					JOptionPane.INFORMATION_MESSAGE);
			flag = false;
		}
		return flag;
	}

	@Override
	public void cursorChange(Element elementAtPoint, SVGCanvas canvas) {
		canvas.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		tqModule.setDrawable(true);
		pointNeedCentralByDrawing = false;
	}

	@Override
	public void doMenuAction() {
		super.doMenuAction();
		tqModule.setDrawable(true);
		((TQContinuesDrawingHandler) tqModule.getDrawingHandler())
				.setPathType(PathTypeEnum.Circle);
	}

	@Override
	protected void initOperation() {
		// ����ģʽ�£�ֻ������ƶ�ʱ�������״�仯,�Լ�������ͼ�λ�ҵ����
		final ModeSelectionListener cursorLis = new ModeSelectionListener() {
			@Override
			public void modeSelected(ModeSelectionEvent evt) {
				changeWhenDrawing(evt);
			}
		};
		tqModule.getEditor().getHandlesManager().addHandlesListener(
				new HandlesListener() {
					@Override
					public void handleCreated(SVGHandle currentHandle) {
						currentHandle.getSelection().addMouseMoveListener(
								cursorLis);
					}
				});
	}

	/**
	 * ���ƹ���������ƶ��еĶ�����Ӧ��ͼ����Ӧ
	 * 
	 * @param evt
	 */
	private void changeWhenDrawing(ModeSelectionEvent evt) {
		if (tqModule.getCurrentAction() == null
				|| tqModule.getCurrentAction().equals("")) {
			return;
		}
		if (tqModule.getCurrentTQOperation() instanceof TQDrawTonghuiOperation) {
			// ��ʼ��ʱ���������������������ĸ����������Ǹ��߻�֧�ߣ�
			cursorChangeWhenDrawing(evt);
		}
	}

	/**
	 * ����ʱ������ƶ�������Ӱ��Ĺ��仯
	 * 
	 * @param evt
	 *            ModeSelectionEvent�¼�
	 * @param elementAtPoint
	 *            ��굱ǰλ�ö�Ӧ��svg element����
	 */
	private void cursorChangeWhenDrawing(ModeSelectionEvent evt) {
		Element elementAtPoint = evt.getSource().getElementAtMousePoint();
		if (elementAtPoint != null && tqModule.isDrawingStarted()
				&& TQToolkit.isBranchLineDrawable(elementAtPoint)) {

			pointNeedCentralByDrawing = true;
			if (thStartEle == null) {
				offsetChangeWhenDrawing(evt);
			} else if (thStartEle != null && thEndEle == null) {
				endTHPoleDrawing(evt);
			}
			if (thStartEle == null || thEndEle == null) {
				evt.getSource().getHandle().getCanvas().setCursor(
						Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
			}
		} else {
			restorePreviousOffset();
			evt.getSource().getHandle().getCanvas().setCursor(
					Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			pointNeedCentralByDrawing = false;
		}

	}

	/**
	 * �ָ��仯ǰ��ƫ����
	 */
	private void restorePreviousOffset() {
		TQContinuesDrawingHandler drawingHandler = (TQContinuesDrawingHandler) tqModule
				.getDrawingHandler();
		if (offsetChanged) {
			drawingHandler.setCurrentOffset(previousOffset);
			offsetChanged = !offsetChanged;
		}
		drawingHandler.setUpperContinue(false);
	}

	/**
	 * ��������ʼͬ�ظ���������Ƶ�ͬ�ظ���ʱ�������߶�Ҫ���¼���ƫ�����Ա�����ȷ�Ļ���ͬ����·
	 * 
	 * @param evt
	 */
	private void offsetChangeWhenDrawing(ModeSelectionEvent evt) {
		TQContinuesDrawingHandler drawingHandler = (TQContinuesDrawingHandler) tqModule
				.getDrawingHandler();
		if (!offsetChanged) {
			double currentOffset = drawingHandler.getCurrentOffset();
			previousOffset = currentOffset;
			offsetChanged = !offsetChanged;
		}
		Element elementAtPoint = evt.getSource().getElementAtMousePoint();
		Point2D endPoint = EditorToolkit.getCenterPoint(elementAtPoint);
		Point2D beginPoint = drawingHandler.getSegBeginPoint();
		endPoint = evt.getSource().getHandle().getTransformsManager()
				.getScaledPoint(endPoint, false);

		double distance = Math.sqrt((beginPoint.getX() - endPoint.getX())
				* (beginPoint.getX() - endPoint.getX())
				+ (beginPoint.getY() - endPoint.getY())
				* (beginPoint.getY() - endPoint.getY()));
		double scale = evt.getSource().getHandle().getCanvas().getZoomManager()
				.getCurrentScale();
		double scaledPlusRange = acceptPlusOffset * scale;
		while ((distance / (drawingHandler.getCurrentOffset() * scale)) % 1 > acceptRange) {
			drawingHandler.setCurrentOffset(drawingHandler.getCurrentOffset()
					- scaledPlusRange);
		}
		drawingHandler.setUpperContinue(true);
		drawingHandler.drawPath(evt.getSource().getHandle(), endPoint,
				drawingHandler.getCurrentOffset());
	}

	/**
	 * �ն�ͬ�ظ�������
	 * 
	 * @param evt
	 */
	private void endTHPoleDrawing(ModeSelectionEvent evt) {
		TQContinuesDrawingHandler drawingHandler = (TQContinuesDrawingHandler) tqModule
				.getDrawingHandler();
		Element elementAtPoint = evt.getSource().getElementAtMousePoint();
		Point2D endPoint = EditorToolkit.getCenterPoint(elementAtPoint);
		endPoint = evt.getSource().getHandle().getTransformsManager()
				.getScaledPoint(endPoint, false);
		// ����ȡ��
		drawingHandler.setUpperContinue(true);
		drawingHandler.drawPath(evt.getSource().getHandle(), endPoint,
				drawingHandler.getCurrentOffset());
	}

	@Override
	public void mousePress(ModeSelectionEvent evt) {
		super.mousePress(evt);
		Element tempEle = evt.getSource().getElementAtMousePoint();
		if (tqModule.isDrawingStarted() && tempEle != null) {
			if (thStartEle == null) {
				if (TQToolkit.isBranchLineDrawable(tempEle)) {
					thStartEle = tempEle;
					thSegStartIndex = offsetList.size();
					((TQContinuesDrawingHandler) tqModule.getDrawingHandler())
							.setPathType(PathTypeEnum.Line);
				}
			} else {
				if (thEndEle == null) {
					if (TQToolkit.isBranchLineDrawable(tempEle)) {
						thEndEle = tempEle;
						thSegEndIndex = offsetList.size();
					}
				}
				((TQContinuesDrawingHandler) tqModule.getDrawingHandler())
						.setPathType(PathTypeEnum.Circle);
			}
		}

	}

	@Override
	public void reset() {
		super.reset();
		thStartEle = null;
		thEndEle = null;
		offsetChanged = false;
		previousOffset = 0;
		thSegStartIndex = -1;
		thSegEndIndex = -1;
		((TQContinuesDrawingHandler) tqModule.getDrawingHandler())
				.setPathType(PathTypeEnum.Circle);
	}

	private static double VERTICAL_THLINE_OFFSET = Constants.NCI_SVG_DEFAULT_GRAPHICS_HEIGHT / 2;

	/**
	 * ת��ͬ����·��Ϣ�������ر�ͬ�صĸ�������
	 * 
	 * @param thPathEle
	 * @return
	 */
	private List<Element> convertTonghuiPath(Element thPathEle) {
		List<Element> crossedTonghuiPoles = new ArrayList<Element>();
		SVGHandle handle = tqModule.getEditor().getHandlesManager()
				.getCurrentHandle();
		DistrictTopology topo = (DistrictTopology) handle.getCanvas()
				.getCanvasOper().getTopologyManager().getTopologyObject();
		LinkedList<String> subList = topo.getSubList(((Element) thStartEle
				.getParentNode()).getAttribute("id"), thStartEle
				.getAttribute("id"), thEndEle.getAttribute("id"));
		for (String id : subList) {
			Element crossPole = thPathEle.getOwnerDocument().getElementById(id);
			crossedTonghuiPoles.add(crossPole);
			TQToolkit.addMetadata(crossPole,
					TQToolkit.TQ_METADATA_TONGHUI_LINE,
					TQToolkit.TQ_METADATA_LINE_ID, ((Element) thPathEle
							.getParentNode()).getAttribute("id"), false);
		}
		// �ж���ʼ�����ж�����ͬ����·, �����ݼ���ż������
		int thCounts = 0;
		try {
			thCounts = ((Element) thStartEle.getElementsByTagName(
					Constants.NCI_SVG_METADATA).item(0)).getElementsByTagName(
					TQToolkit.TQ_METADATA_TONGHUI_LINE).getLength();
		} catch (Exception e) {
			if (e instanceof NullPointerException == false)
				e.printStackTrace();
		}
		List<Point2D> cornerPoints = new ArrayList<Point2D>();
		Point2D startPoint = EditorToolkit.getCenterPoint(thStartEle);
		cornerPoints.add(startPoint);
		Point2D endPoint = EditorToolkit.getCenterPoint(thEndEle);
		StringBuffer pathValue = new StringBuffer();
		Point2D.Double newStartPoint = new Point2D.Double();
		Point2D.Double newEndPoint = new Point2D.Double();
		double corner = Math.atan2(endPoint.getY() - startPoint.getY(),
				endPoint.getX() - startPoint.getX());
		if (thCounts % 2 == 0) {
			corner = corner + Math.PI / 2;
		} else {
			corner = corner - Math.PI / 2;
		}
		// ʵ��ƫ����Ҫ��ͬ����·���Ӷ�����
		double offset = (thCounts + 1) / 2 * VERTICAL_THLINE_OFFSET;
		newStartPoint.x = startPoint.getX() + offset * Math.cos(corner);
		newStartPoint.y = startPoint.getY() + offset * Math.sin(corner);
		newEndPoint.x = endPoint.getX() + offset * Math.cos(corner);
		newEndPoint.y = endPoint.getY() + offset * Math.sin(corner);

		pathValue.append("M").append(startPoint.getX()).append(" ").append(
				startPoint.getY());
		pathValue.append("L").append(newStartPoint.getX()).append(" ").append(
				newStartPoint.getY());
		pathValue.append("L").append(newEndPoint.getX()).append(" ").append(
				newEndPoint.getY());
		pathValue.append("L").append(endPoint.getX()).append(" ").append(
				endPoint.getY());
		thPathEle.setAttribute("d", pathValue.toString());
		return crossedTonghuiPoles;
	}

}
