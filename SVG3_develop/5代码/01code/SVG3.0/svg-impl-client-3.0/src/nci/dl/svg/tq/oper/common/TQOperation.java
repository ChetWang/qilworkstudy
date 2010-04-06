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
 * ������̨��ͼ����
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
	 * ���ƻ�ҵ��ִ��ǰ�����任�������������ƹ����е����任
	 * 
	 * @param elementAtPoint
	 * @param canvas
	 */
	public abstract void cursorChange(Element elementAtPoint, SVGCanvas canvas);

	public void selectionChange(Set<Element> selectedEles) {

	}

	/**
	 * �����ĳ�ʼ�����������ڹ��캯����ִ��
	 */
	protected void initOperation() {

	}

	/**
	 * �˵���Ӧ�¼�������ͨ�õ����ô����Ѿ���TQShape��ִ���⣬�������Ӳ�ͬ�Ĳ������еĹ���
	 */
	public void doMenuAction() {
		tqModule.getEditor().getHandlesManager().getCurrentHandle()
				.getSelection().setSelectionFilter(selectionFilter);
	}

	/**
	 * �����������һ��������ֻ����һ�εĲ�������£�������ɾ���豸������·����ʱ���׶ε�ƫ�����洢
	 * 
	 * @param evt
	 */
	public void mousePress(ModeSelectionEvent evt) {

	}

	/**
	 * ���˵�����Ϊ���ʼ״̬
	 */
	public void resetMenuToDefault() {
		// Ĭ����enabled����������
		menuItem.setEnabled(true);
	}

	/**
	 * ��ȡָ��ͼԪ�����ӵ����ƶ�Ӧ�����ӵ��λ��
	 * 
	 * @param connectPointName
	 *            ���ӵ�����
	 * @param useEle
	 *            ָ��ͼԪ
	 * @return Point2Dλ��
	 */
	protected Point2D getConnectedPoint(String connectPointName, Element useEle) {
		try {
			if (useEle == null) {
				tqModule.getEditor().getLogger().log(tqModule,
						LoggerAdapter.INFO, "ָ�������ӵ����Ϊnull");
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
	 * ���ò���ģ��
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
	 * ��ȡ̨��ͼģ��
	 * 
	 * @return
	 */
	public TQShape getTQModule() {
		return tqModule;
	}

	/**
	 * ��ȡ����ģ���Ӧ�Ĳ˵�JMenuItem
	 * 
	 * @return
	 */
	public JMenuItem getMenuItem() {
		return menuItem;
	}

	/**
	 * ���ò���ģ���Ӧ�Ĳ˵�JMenuItem
	 * 
	 * @param menuItem
	 */
	public void setMenuItem(JMenuItem menuItem) {
		this.menuItem = menuItem;
	}

	/**
	 * �жϻ�ͼʱ������Ƿ�仯
	 * 
	 * @return
	 */
	public boolean isCursorChangeWhenDrawing() {
		return false;
	}

}
