package nci.dl.svg.tq.oper;

import java.awt.Cursor;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Set;

import nci.dl.svg.tq.TQContinuesDrawingHandler;
import nci.dl.svg.tq.TQShape;
import nci.dl.svg.tq.TQToolkit;
import nci.dl.svg.tq.TQContinuesDrawingHandler.PathTypeEnum;
import nci.dl.svg.tq.oper.common.TQDrawOperation;

import org.apache.batik.ext.awt.geom.ExtendedGeneralPath;
import org.w3c.dom.Element;

import com.nci.svg.sdk.client.util.EditorToolkit;

import fr.itris.glips.library.geom.path.Path;
import fr.itris.glips.library.geom.path.segment.Segment;
import fr.itris.glips.svgeditor.display.canvas.SVGCanvas;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.shape.ShapeToolkit;

/**
 * 台区图绘制拉线操作
 * 
 * @author qi
 * 
 */
public class TQPullLineOperation extends TQDrawOperation {

	public TQPullLineOperation(TQShape tqModule) {
		super(tqModule);
	}

	@Override
	public Element createDrawedElement(final SVGHandle handle,
			Shape continuesShape, String lineID) {
		final Element pathEle = handle.getCanvas().getDocument()
				.createElementNS(
						handle.getCanvas().getDocument().getDocumentElement()
								.getNamespaceURI(), "path");
		Path path = new Path((ExtendedGeneralPath) continuesShape);
		final Segment seg = path.getSegment();
		final Element elementAtPoint = tqModule.getPseudoStartElement();

		Runnable executeRunnable = new Runnable() {
			public void run() {
				Point2D startPoint = seg.getEndPoint();
				System.out.println("startPoint:" + startPoint);
				Point2D endPoint = seg.getNextSegment().getEndPoint();
				System.out.println("endPoint:" + endPoint);
				EditorToolkit.insertPathElement(handle, elementAtPoint
						.getParentNode(), pathEle, elementAtPoint
						.getParentNode().getFirstChild(), startPoint, endPoint,
						elementAtPoint.getAttribute("id"), null, "nci_c", null);

				TQToolkit.addMetadata(pathEle, TQToolkit.TQ_METADATA_PULL_LINE,
						TQToolkit.TQ_METADATA_POLE_ID, elementAtPoint
								.getAttribute("id"), false);
				EditorToolkit.appendMarker(elementAtPoint.getOwnerDocument());
				pathEle.setAttribute("marker-end", "url(#"
						+ EditorToolkit.MARKER_ID + ")");
				TQToolkit.addMetadata(pathEle,
						TQToolkit.TQ_METADATA_PROPERTIES,
						TQToolkit.TQ_METADATA_LINE_TYPE,
						TQToolkit.TQ_METADATA_PULL_LINE, false);
			}
		};
		Runnable undoRunnable = new Runnable() {
			public void run() {
				pathEle.getParentNode().removeChild(pathEle);
			}
		};
		Set<Element> elements = new HashSet<Element>();
		elements.add(pathEle);
		ShapeToolkit.addUndoRedoAction(handle, tqModule.getCurrentAction(),
				executeRunnable, undoRunnable, elements);
		return pathEle;
	}

	@Override
	public void cursorChange(Element elementAtPoint, SVGCanvas canvas) {
		// 转角杆和终端杆可拉线
		if (elementAtPoint != null
				&& (TQToolkit.isCornerPole(elementAtPoint) || TQToolkit
						.isTerminalPole(elementAtPoint))) {

			canvas.setCursor(Cursor
					.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
			tqModule.setDrawable(true);

		} else {
			canvas.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			tqModule.setDrawable(false);
		}
	}

	@Override
	public void doMenuAction() {
		super.doMenuAction();
		tqModule.setDrawable(true);
		((TQContinuesDrawingHandler) tqModule.getDrawingHandler())
				.setPathType(PathTypeEnum.Line);
	}

}
