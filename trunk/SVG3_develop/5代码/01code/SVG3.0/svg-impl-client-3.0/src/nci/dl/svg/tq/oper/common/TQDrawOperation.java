package nci.dl.svg.tq.oper.common;

import java.awt.Shape;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import nci.dl.svg.tq.TQShape;

import org.w3c.dom.Element;

import com.nci.svg.sdk.client.selection.ModeSelectionEvent;
import com.nci.svg.sdk.logger.LoggerAdapter;

import fr.itris.glips.svgeditor.display.handle.SVGHandle;

/**
 * 设计绘图的操作
 * 
 * @author qi
 * 
 */
public abstract class TQDrawOperation extends TQOperation {

	public TQDrawOperation(TQShape tqModule) {
		super(tqModule);
	}

	private int clickIndex = 0;

	protected List<Double> offsetList = new Vector<Double>();

	/**
	 * 起始位置是否是需转换过的图元的中点
	 */
	protected boolean pointNeedCentralByDrawing = true;

	/**
	 * 根据既定的偏移量计算出应该所在的位置
	 * 
	 * @param beginPoint
	 * @param endPoint
	 * @param distance
	 * @param pos
	 * @param offset
	 * @return
	 */
	protected Point2D getPoint(Point2D beginPoint, Point2D endPoint,
			double distance, int pos, double offset) {
		Point2D.Double point = new Point2D.Double();
		point.x = beginPoint.getX() + (endPoint.getX() - beginPoint.getX())
				* offset * pos / distance;
		point.y = beginPoint.getY() + (endPoint.getY() - beginPoint.getY())
				* offset * pos / distance;
		return point;
	}

	public Element createElement(SVGHandle handle, Shape continuesShape,
			String lineID) {
		try {
			Element e = createDrawedElement(handle, continuesShape, lineID);
			return e;
		} catch (Exception e) {
			tqModule.getEditor().getLogger().log(tqModule, LoggerAdapter.ERROR,
					e);
			return null;
		} finally {
			reset();
		}
	}

	/**
	 * 生成需要的Element对象，并组合
	 * 
	 * @param handle
	 * @param continuesShape
	 * @param lineID
	 * @return
	 */
	public abstract Element createDrawedElement(SVGHandle handle,
			Shape continuesShape, String lineID);

	@Override
	public void mousePress(ModeSelectionEvent evt) {
		if (tqModule.isDrawable()) {
			if (clickIndex > 0) {
				offsetList.add(tqModule.getDrawingHandler()
						.getCurrentOffset());
			}
			clickIndex++;
		}
	}

	/**
	 * 绘图时，判断起始位置是否是需转换过的图元的中点
	 * 
	 * @return
	 */
	public boolean isPointNeedCentralByDrawing() {
		return pointNeedCentralByDrawing;
	}

	public void reset() {
		super.reset();
		clickIndex = 0;
		offsetList.clear();
	}
}
