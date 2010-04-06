package nci.dl.svg.tq.oper;

import java.awt.Cursor;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import nci.dl.svg.tq.TQContinuesDrawingHandler;
import nci.dl.svg.tq.TQShape;
import nci.dl.svg.tq.TQToolkit;
import nci.dl.svg.tq.TQContinuesDrawingHandler.PathTypeEnum;
import nci.dl.svg.tq.oper.common.TQDrawOperation;

import org.apache.batik.ext.awt.geom.ExtendedGeneralPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nci.svg.module.TerminalModule;
import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.client.util.EditorToolkit;

import fr.itris.glips.library.geom.path.Path;
import fr.itris.glips.library.geom.path.segment.Segment;
import fr.itris.glips.svgeditor.display.canvas.SVGCanvas;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;

/**
 * 台区图绘制绘制接户线操作
 * 
 * @author qi
 * 
 */
public class TQJiehuOperation extends TQDrawOperation {

	public TQJiehuOperation(TQShape tqModule) {
		super(tqModule);
	}

	@Override
	public Element createDrawedElement(SVGHandle handle, Shape continuesShape,
			String lineID) {
		String symbolName = null;

		String symbolStatus = null;

		Document doc = handle.getScrollPane().getSVGCanvas().getDocument();
		// g打包
		Element gElement = null;
		gElement = doc.createElementNS(doc.getDocumentElement()
				.getNamespaceURI(), "g");
		gElement.setAttribute("id", lineID);
		gElement.setAttribute(TQToolkit.MODEL_ATTR, TQToolkit.MODEL_JIEHU_LINE);
		symbolName = TQToolkit.JILIANGBIAO_NAME;
		symbolStatus = "正常";
		Path path = null;
		// 首先根据continuesShape绘制图元
		if (continuesShape != null
				&& continuesShape instanceof ExtendedGeneralPath) {

			// creating the path object that computes the string
			// representation of the path shape
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
						- Constants.NCI_SVG_DEFAULT_GRAPHICS_WIDTH / 2;
				rect.y = beginPoint.getY()
						- Constants.NCI_SVG_DEFAULT_GRAPHICS_HEIGHT / 2;
				rect.width = Constants.NCI_SVG_DEFAULT_GRAPHICS_WIDTH;
				rect.height = Constants.NCI_SVG_DEFAULT_GRAPHICS_HEIGHT;
				if (seg.getSegmentIndex() == 1) {
					afterID = tqModule.getPseudoStartElement().getAttribute(
							"id");
					afterPoint = EditorToolkit.getCenterPoint(tqModule
							.getPseudoStartElement());

					TQToolkit.addMetadata(tqModule.getPseudoStartElement(),
							TQToolkit.TQ_METADATA_JIEHU,
							TQToolkit.TQ_METADATA_LINE_ID, lineID, false);
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
						TQToolkit.setToTerminalJiliangbiao(otherEle, lineID);
					}
					afterID = otherEle.getAttribute("id");
					Element pathEle = EditorToolkit.insertPathElement(handle,
							gElement, firstElement, beforePoint, afterPoint,
							beforeID, afterID,
							TerminalModule.CENTER_TERMINAL_NAME,
							TerminalModule.CENTER_TERMINAL_NAME);
					// 设置为基本线路
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
		if (elementAtPoint != null
				&& TQToolkit.isJieHuLineDrawable(elementAtPoint)) {
			canvas.setCursor(Cursor
					.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
			tqModule.setDrawable(true);
		} else {
			tqModule.setDrawable(false);
			canvas.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		}
	}

	@Override
	public void doMenuAction() {
		super.doMenuAction();
		tqModule.setDrawable(true);
		((TQContinuesDrawingHandler) tqModule.getDrawingHandler())
				.setPathType(PathTypeEnum.Rect);
	}

}
