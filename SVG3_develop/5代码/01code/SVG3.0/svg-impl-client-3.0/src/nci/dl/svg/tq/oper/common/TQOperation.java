package nci.dl.svg.tq.oper.common;

import java.awt.geom.Point2D;
import java.util.Set;

import javax.swing.JMenuItem;
import javax.xml.xpath.XPathExpressionException;

import nci.dl.svg.tq.TQShape;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.nci.svg.sdk.client.selection.ModeSelectionEvent;
import com.nci.svg.sdk.client.util.EditorToolkit;
import com.nci.svg.sdk.client.util.Utilities;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.shape.SelectionFilterIF;

import fr.itris.glips.svgeditor.display.canvas.SVGCanvas;

/**
 * 基本的台区图操作
 * 
 * @author qi
 * 
 */
public abstract class TQOperation {

	protected TQShape tqModule;

	protected JMenuItem menuItem;

	protected SelectionFilterIF selectionFilter = new SelectionFilterIF() {

		@Override
		public boolean filterElement(Element shapeElement) {
			if (shapeElement.getNodeName().equals("path"))
				return false;
			return true;
		}

	};

	public TQOperation(TQShape tqModule) {
		this.tqModule = tqModule;
		initOperation();
	}

	/**
	 * 绘制或业务执行前的鼠标变换，但不包括绘制过程中的鼠标变换
	 * 
	 * @param elementAtPoint
	 * @param canvas
	 */
	public abstract void cursorChange(Element elementAtPoint, SVGCanvas canvas);

	public void selectionChange(Set<Element> selectedEles) {

	}

	/**
	 * 操作的初始化工作，将在构造函数中执行
	 */
	protected void initOperation() {

	}

	/**
	 * 菜单响应事件，出了通用的设置处理已经在TQShape中执行外，可以增加不同的操作独有的功能
	 */
	public void doMenuAction() {
		tqModule.getEditor().getHandlesManager().getCurrentHandle()
				.getSelection().setSelectionFilter(selectionFilter);
	}

	/**
	 * 鼠标点击操作，一般适用在只需点击一次的操作情况下，如增加删除设备，或线路绘制时各阶段的偏移量存储
	 * 
	 * @param evt
	 */
	public void mousePress(ModeSelectionEvent evt) {

	}

	/**
	 * 将菜单设置为最初始状态
	 */
	public void resetMenuToDefault() {
		// 默认是enabled，无需重设
		menuItem.setEnabled(true);
	}

	/**
	 * 获取指定图元的连接点名称对应的连接点的位置
	 * 
	 * @param connectPointName
	 *            连接点名称
	 * @param useEle
	 *            指定图元
	 * @return Point2D位置
	 */
	protected Point2D getConnectedPoint(String connectPointName, Element useEle) {
		try {
			if (useEle == null) {
				tqModule.getEditor().getLogger().log(tqModule,
						LoggerAdapter.INFO, "指定的连接点对象为null");
				return null;
			}
			String href = useEle.getAttributeNS(EditorToolkit.xmlnsXLinkNS,
					"href").substring(1);
			Element symbolEle = (Element) Utilities.findNode("//*[@id='" + href
					+ "']", useEle);
			NodeList connectedPoints = symbolEle.getElementsByTagName("use");
			for (int i = 0; i < connectedPoints.getLength(); i++) {
				Element pointEle = (Element) connectedPoints.item(i);
				String name = pointEle.getAttribute("name");
				if (name.equals(connectPointName)) {
					String x = pointEle.getAttribute("x");
					String y = pointEle.getAttribute("y");
					String viewBox = symbolEle.getAttribute("viewBox");
					String[] viewBoxData = viewBox.split(" ");
					String vwidth = viewBoxData[2];
					double scale = Double.valueOf(useEle.getAttribute("width"))
							/ Double.valueOf(vwidth);
					Point2D realConnPoint = new Point2D.Double(scale
							* Double.valueOf(x)
							+ Double.valueOf(useEle.getAttribute("x")), scale
							* Double.valueOf(y)
							+ Double.valueOf(useEle.getAttribute("y")));
					return realConnPoint;
				}
			}
		} catch (DOMException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 重置操作模块
	 */
	public void reset() {
		tqModule.getEditor().getPropertyModelInteractor().getGraphModel()
				.initTreeModel(
						tqModule.getEditor().getHandlesManager()
								.getCurrentHandle());
		if (!tqModule.getEditor().getRemanentModeManager().isRemanentMode()) {
			tqModule.getEditor().getHandlesManager().getCurrentHandle()
					.getSelection().setSelectionFilter(null);
		}
	}

	/**
	 * 获取台区图模块
	 * 
	 * @return
	 */
	public TQShape getTQModule() {
		return tqModule;
	}

	/**
	 * 获取操作模块对应的菜单JMenuItem
	 * 
	 * @return
	 */
	public JMenuItem getMenuItem() {
		return menuItem;
	}

	/**
	 * 设置操作模块对应的菜单JMenuItem
	 * 
	 * @param menuItem
	 */
	public void setMenuItem(JMenuItem menuItem) {
		this.menuItem = menuItem;
	}

	/**
	 * 判断绘图时鼠标光标是否变化
	 * 
	 * @return
	 */
	public boolean isCursorChangeWhenDrawing() {
		return false;
	}

}
