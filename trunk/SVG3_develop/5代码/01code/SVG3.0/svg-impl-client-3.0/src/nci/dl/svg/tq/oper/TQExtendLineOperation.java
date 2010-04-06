package nci.dl.svg.tq.oper;

import java.awt.Cursor;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Set;

import nci.dl.svg.tq.TQContinuesDrawingHandler;
import nci.dl.svg.tq.TQShape;
import nci.dl.svg.tq.TQToolkit;
import nci.dl.svg.tq.TQContinuesDrawingHandler.PathTypeEnum;
import nci.dl.svg.tq.TQShape.LineExtend;
import nci.dl.svg.tq.TQShape.LinesDraw;
import nci.dl.svg.tq.TQShape.Operations;
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
 * 线路延伸操作
 * @author qi
 *
 */
public class TQExtendLineOperation extends TQDrawOperation {

	public TQExtendLineOperation(TQShape tqModule) {
		super(tqModule);
	}

	@Override
	public Element createDrawedElement(SVGHandle handle, Shape continuesShape,
			String lineID) {
		Element pseudoStartElement = tqModule.getPseudoStartElement();
		String symbolName = null;

		String symbolStatus = null;
		// the edited document
		String currentAction = tqModule.getCurrentAction();
		// g打包
		Element gElement = null;
		// 重新生成在已有g元素内部,一般都是对原有线路修改，如线路延伸
		gElement = (Element) pseudoStartElement.getParentNode();
		lineID = gElement.getAttribute("id");

		// 判断绘制什么线路
		if (currentAction.equals(LineExtend.延伸干支线.name())) {
			symbolName = TQToolkit.POLE_NAME;
			symbolStatus = "正常";
		} else if (currentAction.equals(LineExtend.延伸接户线.name())) {
			symbolName = TQToolkit.JILIANGBIAO_NAME;
			symbolStatus = "正常";
		}
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
//			Double offset = tqModule.getDrawingHandler().getOffset();
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
						- TQToolkit.TQ_GRAPH_DEFAULT_WIDTH / 2;
				rect.y = beginPoint.getY()
						- TQToolkit.TQ_GRAPH_DEFAULT_HEIGHT / 2;
				rect.width = TQToolkit.TQ_GRAPH_DEFAULT_WIDTH;
				rect.height = TQToolkit.TQ_GRAPH_DEFAULT_HEIGHT;
				if (seg.getSegmentIndex() == 1) {
					if (currentAction.equals(LinesDraw.绘制干线.name())) {
						Element startEle = EditorToolkit.insertSymbolElement(
								handle, rect, gElement, null, symbolName,
								symbolStatus);
						// 第一个设置为其中的终端杆
						TQToolkit.setToTerminalPole(startEle, lineID);
						afterID = startEle.getAttribute("id");
						afterPoint = beginPoint;
					} else if (currentAction.equals(LinesDraw.绘制支线.name())) {
						afterID = pseudoStartElement.getAttribute("id");
						afterPoint = EditorToolkit
								.getCenterPoint(pseudoStartElement);
						TQToolkit.setToTerminalPole(pseudoStartElement, lineID);
						TQToolkit.addMetadata(pseudoStartElement,
								TQToolkit.TQ_METADATA_BRANCH,
								TQToolkit.TQ_METADATA_LINE_ID, lineID, false);
					} else if (currentAction.equals(Operations.绘制接户.name())) {
						afterID = pseudoStartElement.getAttribute("id");
						afterPoint = EditorToolkit
								.getCenterPoint(pseudoStartElement);

						TQToolkit.addMetadata(pseudoStartElement,
								TQToolkit.TQ_METADATA_JIEHU,
								TQToolkit.TQ_METADATA_LINE_ID, lineID, false);
					} else if (currentAction.equals(LineExtend.延伸干支线.name())) {
						afterID = pseudoStartElement.getAttribute("id");
						afterPoint = EditorToolkit
								.getCenterPoint(pseudoStartElement);
						TQToolkit.removeMetadata(pseudoStartElement,
								TQToolkit.TQ_METADATA_TERMINAL_POLE);
					} else if (currentAction.equals(LineExtend.延伸接户线.name())) {
						afterID = pseudoStartElement.getAttribute("id");
						afterPoint = EditorToolkit
								.getCenterPoint(pseudoStartElement);
						TQToolkit.removeMetadata(pseudoStartElement,
								TQToolkit.TQ_METADATA_TERMINAL_JILIANGBIAO);
					}
				}
				firstElement = (Element) gElement.getFirstChild();
				for (int j = 1; j < size; j++) {
					beforePoint = afterPoint;
					beforeID = afterID;
					afterPoint = getPoint(beginPoint, endPoint, distance, j,
							offsetList.get(i));
					rect.x = afterPoint.getX()
							- Constants.NCI_SVG_DEFAULT_GRAPHICS_WIDTH / 2;
					rect.y = afterPoint.getY()
							- Constants.NCI_SVG_DEFAULT_GRAPHICS_HEIGHT / 2;
					rect.width = Constants.NCI_SVG_DEFAULT_GRAPHICS_WIDTH;
					rect.height = Constants.NCI_SVG_DEFAULT_GRAPHICS_HEIGHT;
					Element otherEle = EditorToolkit.insertSymbolElement(
							handle, rect, gElement, null, symbolName,
							symbolStatus);
					if (firstElement == null)
						firstElement = otherEle;
					if (j == size - 1) {
						// 对于绘制干线或支线来说，最后的节点是有意义的
						if (currentAction.equals(LinesDraw.绘制支线.name())
								|| currentAction.equals(LinesDraw.绘制干线.name())
								|| currentAction
										.equals(LineExtend.延伸干支线.name())) {
							// 最后一个也是终端杆
							if (i == num - 2)
								TQToolkit.setToTerminalPole(otherEle, lineID);
							// 其它的中间转折杆是转角杆
							else {
								TQToolkit.setToCornerPole(otherEle, lineID);
							}
						}

						// 最后一个计量表也要表明是终端表
						if ((currentAction.equals(Operations.绘制接户.name()) || currentAction
								.equals(LineExtend.延伸接户线.name()))
								&& i == num - 2) {
							TQToolkit
									.setToTerminalJiliangbiao(otherEle, lineID);
						}

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
		// FIXME undoredo需要修改
		tqModule.insertShapeElement(handle, gElement);
		return gElement;
	}

	@Override
	public void cursorChange(Element elementAtPoint, SVGCanvas canvas) {
		// 只有在终端杆才能延伸操作
		if (elementAtPoint != null) {
			if (TQToolkit.isTerminalPole(elementAtPoint)) {
				canvas.setCursor(Cursor
						.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
				tqModule.setCurrentAction(LineExtend.延伸干支线.name());
				((TQContinuesDrawingHandler) tqModule.getDrawingHandler())
						.setPathType(PathTypeEnum.Circle);
				tqModule.setDrawable(true);
			} else if (TQToolkit.isTerminalJiliangbiao(elementAtPoint)) {
				canvas.setCursor(Cursor
						.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
				tqModule.setCurrentAction(LineExtend.延伸接户线.name());
				((TQContinuesDrawingHandler) tqModule.getDrawingHandler())
						.setPathType(PathTypeEnum.Rect);
				tqModule.setDrawable(true);
			} else {
				tqModule.setCurrentAction(Operations.线路延伸.name());
				canvas
						.setCursor(Cursor
								.getPredefinedCursor(Cursor.WAIT_CURSOR));
				tqModule.setDrawable(false);
			}
		} else {
			tqModule.setCurrentAction(Operations.线路延伸.name());
			canvas.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			tqModule.setDrawable(false);
		}

	}

	@Override
	public void resetMenuToDefault() {
		menuItem.setEnabled(false);
	}

	@Override
	public void selectionChange(Set<Element> selectedEles) {
		if (selectedEles.size() == 1) {
			Element ele = selectedEles.iterator().next();
			// 判断是否能画线路延伸
			if (TQToolkit.isMainLine(ele) || TQToolkit.isBranchLine(ele)
					|| TQToolkit.isJieHuLine(ele)) {
				menuItem.setEnabled(true);
			}
		} else {
			menuItem.setEnabled(false);
		}
	}

}
