package nci.dl.svg.tq.oper;

import java.awt.Cursor;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.Set;

import nci.dl.svg.tq.TQContinuesDrawingHandler;
import nci.dl.svg.tq.TQShape;
import nci.dl.svg.tq.TQToolkit;
import nci.dl.svg.tq.TQContinuesDrawingHandler.PathTypeEnum;
import nci.dl.svg.tq.oper.common.TQDrawOperation;

import org.apache.batik.ext.awt.geom.ExtendedGeneralPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nci.svg.module.TerminalModule;
import com.nci.svg.sdk.client.selection.GraphicTranslationListener;
import com.nci.svg.sdk.client.selection.ModeSelectionEvent;
import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.client.util.EditorToolkit;
import com.nci.svg.sdk.client.util.Utilities;

import fr.itris.glips.library.geom.path.Path;
import fr.itris.glips.library.geom.path.segment.Segment;
import fr.itris.glips.svgeditor.display.canvas.SVGCanvas;
import fr.itris.glips.svgeditor.display.handle.HandlesListener;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;

/**
 * ̨��ͼ������·�������������Ƹ��ߡ�֧��
 * 
 * @author qi
 * 
 */
public class TQDrawLineOperation extends TQDrawOperation {

	public enum LinesDraw {
		���Ƹ���, ����֧��, ���ƽӻ�
	}

	public TQDrawLineOperation(TQShape tqModule) {
		super(tqModule);
	}

	public void initOperation() {
		// ת�Ǹ��Զ���������
		final GraphicTranslationListener zjPoleLis = new GraphicTranslationListener() {
			@Override
			public void graphicsTranslated(Set<Element> elementsTraslated,Point2D firstPoint, Point2D scaledPoint) {
				zjPoleChange(elementsTraslated);
			}
			@Override
			public void graphicsTranslating(Set<Element> elementsTraslated,Point2D firstPoint, Point2D scaledPoint) {
			}
		};
		tqModule.getEditor().getHandlesManager().addHandlesListener(
				new HandlesListener() {
					public void handleCreated(SVGHandle currentHandle) {
						currentHandle.getSelection()
								.addGraphicTranslationListener(zjPoleLis);
					}
				});
	}

	/**
	 * ת�Ǹ˼��
	 * 
	 * @param evt
	 */
	private void zjPoleChange(Set<Element> elementsTraslated) {
		if (!elementsTraslated.isEmpty()) {
			Iterator<Element> it = elementsTraslated.iterator();
			while (it.hasNext()) {
				Element e = it.next();
				if (TQToolkit.isPole(e)) {
					// FIXME ת���ת�Ǹ��п���Ҳ��仯
				}
			}
		}
	}

	@Override
	public Element createDrawedElement(SVGHandle handle, Shape continuesShape,
			String lineID) {
		String symbolName = null;

		String symbolStatus = null;

		Document doc = handle.getScrollPane().getSVGCanvas().getDocument();
		// the edited document
		String currentAction = tqModule.getCurrentAction();
		// g���
		Element gElement = null;
		gElement = doc.createElementNS(doc.getDocumentElement()
				.getNamespaceURI(), "g");
		gElement.setAttribute("id", lineID);

		// �жϻ���ʲô��·
		if (currentAction.equals(LinesDraw.���Ƹ���.name())) {
			gElement.setAttribute(TQToolkit.MODEL_ATTR,
					TQToolkit.MODEL_MAIN_LINE);
			symbolName = TQToolkit.POLE_NAME;
			symbolStatus = "����";
		} else if (currentAction.equals(LinesDraw.����֧��.name())) {
			gElement.setAttribute(TQToolkit.MODEL_ATTR,
					TQToolkit.MODEL_BRANCH_LINE);
			symbolName = TQToolkit.POLE_NAME;
			symbolStatus = "����";
		}
		Path path = null;
		// ���ȸ���continuesShape����ͼԪ
		if (continuesShape != null
				&& continuesShape instanceof ExtendedGeneralPath) {
			path = new Path((ExtendedGeneralPath) continuesShape);
		}

		if (path != null) {
			int num = path.getSegmentsNumber();
			Point2D beginPoint = null;
			Point2D endPoint = null;
			Segment seg = path.getSegment();
			endPoint = seg.getEndPoint();
			Point2D beforePoint = null;
			Point2D afterPoint = null;
			String beforeID = null, afterID = null;
			// Double offset = tqModule.getDrawingHandler().getOffset();
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
				rect.x = beginPoint.getX()
						-TQToolkit.TQ_GRAPH_DEFAULT_WIDTH/ 2;
				rect.y = beginPoint.getY()
						- TQToolkit.TQ_GRAPH_DEFAULT_HEIGHT / 2;
				rect.width = TQToolkit.TQ_GRAPH_DEFAULT_WIDTH;
				rect.height = TQToolkit.TQ_GRAPH_DEFAULT_HEIGHT;
				if (seg.getSegmentIndex() == 1) {
					if (currentAction.equals(LinesDraw.���Ƹ���.name())) {
						Element startEle = EditorToolkit.insertSymbolElement(
								handle, rect, gElement, null, symbolName,
								symbolStatus);
						// ��һ������Ϊ���е��ն˸�
						TQToolkit.setToTerminalPole(startEle, lineID);
						afterID = startEle.getAttribute("id");
						afterPoint = beginPoint;
					} else if (currentAction.equals(LinesDraw.����֧��.name())) {
						afterID = tqModule.getPseudoStartElement()
								.getAttribute("id");
						Utilities.printNode(tqModule.getPseudoStartElement(),true);
						afterPoint = EditorToolkit.getCenterPoint(tqModule
								.getPseudoStartElement());
						TQToolkit.setToTerminalPole(tqModule
								.getPseudoStartElement(), lineID);
						TQToolkit.addMetadata(tqModule.getPseudoStartElement(),
								TQToolkit.TQ_METADATA_BRANCH,
								TQToolkit.TQ_METADATA_LINE_ID, lineID, false);
					}

				}
				firstElement = (Element) gElement.getFirstChild();
				for (int j = 1; j < size; j++) {
					beforePoint = afterPoint;
					beforeID = afterID;
					afterPoint = getPoint(beginPoint, endPoint, distance, j,
							offsetList.get(i));
					rect.x = afterPoint.getX()
							- TQToolkit.TQ_GRAPH_DEFAULT_WIDTH / 2;
					rect.y = afterPoint.getY()
							- TQToolkit.TQ_GRAPH_DEFAULT_HEIGHT / 2;
					rect.width = TQToolkit.TQ_GRAPH_DEFAULT_WIDTH;
					rect.height = TQToolkit.TQ_GRAPH_DEFAULT_HEIGHT;
					Element otherEle = EditorToolkit.insertSymbolElement(
							handle, rect, gElement, null, symbolName,
							symbolStatus);
					if (firstElement == null)
						firstElement = otherEle;
					if (j == size - 1) {
						// ���ڻ��Ƹ��߻�֧����˵�����Ľڵ����������
						if (currentAction.equals(LinesDraw.����֧��.name())
								|| currentAction.equals(LinesDraw.���Ƹ���.name())) {
							// ���һ��Ҳ���ն˸�
							if (i == num - 2)
								TQToolkit.setToTerminalPole(otherEle, lineID);
							// �������м�ת�۸���ת�Ǹ�
							else {
								TQToolkit.setToCornerPole(otherEle, lineID);
							}
						}

					}
					afterID = otherEle.getAttribute("id");
					Element pathEle = EditorToolkit.insertPathElement(handle,
							gElement, firstElement, beforePoint, afterPoint,
							beforeID, afterID,
							TerminalModule.CENTER_TERMINAL_NAME,
							TerminalModule.CENTER_TERMINAL_NAME);
					// ����Ϊ������·
					TQToolkit.addMetadata(pathEle,
							TQToolkit.TQ_METADATA_PROPERTIES,
							TQToolkit.TQ_METADATA_LINE_TYPE,
							TQToolkit.TQ_METADATA_BASE_LINE, false);
				}

			}
		}
		EditorToolkit.insertCurSymbolByName(handle, symbolName);
		tqModule.insertShapeElement(handle, gElement);		
		return gElement;
	}

	@Override
	public void cursorChange(Element elementAtPoint, SVGCanvas canvas) {
		if (elementAtPoint == null) {
			tqModule.setCurrentAction(LinesDraw.���Ƹ���.name());
			tqModule.setDrawable(true);
			tqModule.setPseudoStartElement(null);
			pointNeedCentralByDrawing = false;
		} else if (TQToolkit.isBranchLineDrawable(elementAtPoint)) {
			canvas.setCursor(Cursor
					.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
			tqModule.setDrawable(true);
			tqModule.setCurrentAction(LinesDraw.����֧��.name());
			tqModule.setPseudoStartElement(elementAtPoint);
			pointNeedCentralByDrawing = true;
		} else {
			canvas.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
			tqModule.setDrawable(false);
			tqModule.setPseudoStartElement(null);
			pointNeedCentralByDrawing = false;
		}
	}

	@Override
	public void doMenuAction() {
		super.doMenuAction();
		tqModule.setDrawable(true);
		((TQContinuesDrawingHandler) tqModule.getDrawingHandler())
				.setPathType(PathTypeEnum.Circle);
	}

}
