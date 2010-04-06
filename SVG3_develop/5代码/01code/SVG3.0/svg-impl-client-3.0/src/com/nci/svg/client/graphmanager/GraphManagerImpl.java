package com.nci.svg.client.graphmanager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.xml.xpath.XPathExpressionException;

import org.apache.batik.dom.svg.SVGOMElement;
import org.apache.batik.dom.svg.SVGOMMetadataElement;
import org.apache.batik.dom.svg.SVGOMTextElement;
import org.apache.batik.dom.svg.SVGOMUseElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.GraphManagerAdapter;
import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.client.util.EditorToolkit;
import com.nci.svg.sdk.client.util.Utilities;

import fr.itris.glips.svgeditor.display.canvas.dom.SVGDOMNormalizer;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;

/**
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author yx.nci
 * @时间：2008-12-9
 * @功能：图形响应管理类实现类
 * 
 */
public class GraphManagerImpl extends GraphManagerAdapter {


	public GraphManagerImpl(EditorAdapter editor) {
		super(editor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.client.GraphManagerAdapter#animate(java.lang.String)
	 */
	@Override
	public int animate(String xml) {
		// TODO 暂时不实现，目前无此业务需求定义
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.client.GraphManagerAdapter#moveCanvas(double,
	 *      double)
	 */
	@Override
	public int moveCanvas(double x, double y) {
		// 
		SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
		if (handle == null)
			return OPER_ERROR;
		// 计算位置，并移动画布到相应的位置
		Dimension dimension = handle.getCanvas().getScrollPane()
				.getScrollValues();
		dimension.height /= handle.getCanvas().getZoomManager()
				.getCurrentScale();
		dimension.width /= handle.getCanvas().getZoomManager()
				.getCurrentScale();
		Rectangle rect = handle.getCanvas().getScrollPane().getViewPortBounds();
		rect.height /= handle.getCanvas().getZoomManager().getCurrentScale();
		rect.width /= handle.getCanvas().getZoomManager().getCurrentScale();
		if (x < dimension.width || y < dimension.height) {
			dimension.width = (int) x;
			dimension.height = (int) y;
			if (dimension.width < 0)
				dimension.width = 0;
			if (dimension.height < 0)
				dimension.height = 0;

			dimension.height *= handle.getCanvas().getZoomManager()
					.getCurrentScale();
			dimension.width *= handle.getCanvas().getZoomManager()
					.getCurrentScale();
			handle.getCanvas().getScrollPane().setScrollValues(dimension);
		} else if (x >= dimension.width + rect.width
				|| y >= dimension.height + rect.height) {
			dimension.width = (int) x;
			dimension.height = (int) y;
			if (dimension.width < 0)
				dimension.width = 0;
			if (dimension.height < 0)
				dimension.height = 0;

			dimension.height *= handle.getCanvas().getZoomManager()
					.getCurrentScale();
			dimension.width *= handle.getCanvas().getZoomManager()
					.getCurrentScale();
			handle.getCanvas().getScrollPane().setScrollValues(dimension);
		}
		return OPER_SUCCESS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.client.GraphManagerAdapter#resetCanvasBg(int)
	 */
	@Override
	public int resetCanvasBg(int rgbColor) {
		// 
		Color color = null;
		SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
		if (handle == null)
			return OPER_ERROR;
		try {
			color = new Color(rgbColor);
			if (color == null)
				return OPER_ERROR;
			editor.getHandlesManager().getCurrentHandle().getCanvas()
					.setBackground(color, false);
		} catch (Exception ex) {
			color = null;
			return OPER_ERROR;
		}

		return OPER_SUCCESS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.client.GraphManagerAdapter#resetSymbolStatus(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public int resetSymbolStatus(String symbolID, String symbolStatus) {
		// 
		SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
		if (handle == null)
			return OPER_ERROR;

		// 获取符合该图元编号的节点
		Node node = getNodeBySymbolID(handle, symbolID);
		if (node == null || !(node instanceof SVGOMUseElement))
			return OPER_ERROR;
		// 对于有效的节点进行状态切换
		resetSymbolStatus_Node(handle, node, symbolStatus);
		editor.getSvgSession().refreshCurrentHandleImediately();
		return OPER_SUCCESS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.client.GraphManagerAdapter#resetSymbolsColor(java.lang.String[],
	 *      java.lang.String[])
	 */
	@Override
	public int resetSymbolsColor(String[] symbolID, String[] cssRemark) {
		// 
		SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
		if (handle == null || symbolID == null)
			return OPER_ERROR;

		int length = symbolID.length;
		for (int i = 0; i < length; i++) {
			// 根据图元编号获取节点
			Node node = getNodeBySymbolID(handle, symbolID[i]);
			if (node == null || !(node instanceof SVGOMUseElement))
				continue;
			Element element = (Element) node;

			if (cssRemark == null || cssRemark.length < i
					|| cssRemark[i] == null || cssRemark[i].length() == 0)
				element.removeAttribute("class");
			else {
				// TODO：目前暂时缺少对css校验
				element.setAttribute("class", cssRemark[i]);
			}
		}
		handle.getEditor().getSvgSession().refreshCurrentHandleImediately();
		return OPER_SUCCESS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.client.GraphManagerAdapter#resetSymbolsColor_Buss(java.lang.String[],
	 *      java.lang.String[])
	 */
	@Override
	public int resetSymbolsColor_Buss(String[] bussID, String[] cssRemark) {
		// 
		SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
		if (handle == null || bussID == null)
			return OPER_ERROR;

		int length = bussID.length;
		for (int i = 0; i < length; i++) {
			// 根据业务编号获取节点
			Node node = getNodeByBussID(handle, bussID[i]);
			if (node == null || !(node instanceof SVGOMUseElement))
				continue;
			Element element = (Element) node;

			// 如描述数组有误，则清除该业务编号原有颜色渲染
			if (cssRemark == null || cssRemark.length < i
					|| cssRemark[i] == null || cssRemark[i].length() == 0)
				element.removeAttribute("class");
			else {
				// TODO：目前暂时缺少对css校验
				element.setAttribute("class", cssRemark[i]);
			}
		}
		handle.getEditor().getSvgSession().refreshCurrentHandleImediately();
		return OPER_SUCCESS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.client.GraphManagerAdapter#resetSymbolsMatrix(java.lang.String[],
	 *      java.lang.String[], int[], int[])
	 */
	@Override
	public int resetSymbolsMatrix(String[] bussID, String[] matrix, int[] x,
			int[] y) {
		// 
		SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
		if (handle == null)
			return OPER_ERROR;

		int size = bussID.length;
		for (int i = 0; i < size; i++) {
			// 获取符合该业务编号的节点
			Node node = getNodeByBussID(handle, bussID[i]);
			if (node == null || !(node instanceof Element))
				continue;
			Element element = (Element) node;
			// 如存在转换要求，则设置转换
			if (matrix != null && matrix.length >= i) {
				String matrixText = matrix[i];
				if (matrixText != null && matrixText.length() > 0) {
					element.setAttribute("transform", matrixText);
				} else
					element.removeAttribute("transform");
			} else {
				element.removeAttribute("transform");
			}

			// 如存在位置转换需求，则设置位置
			if (x != null && x.length >= i && y != null && y.length > i) {
				element.setAttribute("x", new String().valueOf(x[i]));
				element.setAttribute("y", new String().valueOf(y[i]));
			}
		}
		editor.getSvgSession().refreshCurrentHandleImediately();
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.client.GraphManagerAdapter#resetSymbolsStatus_Buss(java.lang.String[],
	 *      java.lang.String[])
	 */
	@Override
	public int resetSymbolsStatus_Buss(String[] bussID, String[] symbolStatus) {
		// 
		SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
		if (handle == null)
			return OPER_ERROR;

		int size = bussID.length;
		for (int i = 0; i < size; i++) {
			// 获取符合该业务编号的节点
			Node node = getNodeByBussID(handle, bussID[i]);
			if (node == null || !(node instanceof SVGOMUseElement))
				continue;

			if (symbolStatus == null || symbolStatus.length < i)
				continue;
			// 对于有效的节点进行状态切换
			resetSymbolStatus_Node(handle, node, symbolStatus[i]);
		}
		editor.getSvgSession().refreshCurrentHandleImediately();
		return OPER_SUCCESS;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.client.GraphManagerAdapter#resetTexts(java.lang.String[],
	 *      int[], java.lang.String[])
	 */
	@Override
	public int resetTexts(String[] bussID, String[] rgb, String[] content) {
		// 
		SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
		if (handle == null || bussID == null)
			return OPER_ERROR;

		int length = bussID.length;
		for (int i = 0; i < length; i++) {
			Node node = getNodeByBussID(handle, bussID[i]);
			if (node == null || !(node instanceof SVGOMTextElement))
				continue;
			Element element = (Element) node;

			// 如存在颜色设置要求，则设置颜色
			if (rgb != null && rgb.length >= i) {
				String rgbText = rgb[i];
				if (rgbText != null && rgbText.length() > 0) {
					editor.getSVGToolkit().setStyleProperty(element, "fill",
							rgbText);

				}
			}

			// 如存在content设置要求则设置文字
			if (content != null && content.length >= i) {
				String textContent = content[i];
				if (textContent != null && textContent.length() > 0)
					resetTextContent(handle, node, textContent);
			}
		}
		editor.getSvgSession().refreshCurrentHandleImediately();
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.client.GraphManagerAdapter#scaleCanvas(double)
	 */
	@Override
	public int scaleCanvas(double scale) {
		// 
		if (scale > 20 || scale < 0.01)
			return OPER_ERROR;
		SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
		if (handle == null)
			return OPER_ERROR;
		handle.getCanvas().getZoomManager().scaleTo(scale);
		return OPER_SUCCESS;
	}

	/**
	 * 在传入的进程中获取符合图元编号的节点
	 * 
	 * @param handle:进程
	 * @param symbolID:图元编号
	 * @return:节点，存在则返回节点对象，不存在则返回null
	 */
	protected Node getNodeBySymbolID(SVGHandle handle, String symbolID) {
		Document doc = handle.getCanvas().getDocument();
		String expr = "//*[@id='" + symbolID + "']";
		Node node = null;
		try {
			node = Utilities.findNode(expr, doc.getDocumentElement());
		} catch (XPathExpressionException e) {
			node = null;
		}
		return node;
	}

	/**
	 * 在传入的进程中获取符合业务编号的节点
	 * 
	 * @param handle:进程
	 * @param bussID:业务编号
	 * @return:节点，存在则返回节点对象，不存在则返回null
	 */
	protected Node getNodeByBussID(SVGHandle handle, String bussID) {
		Document doc = handle.getCanvas().getDocument();
		String expr = "//*[@*='" + bussID + "']";
		NodeList list = null;
		try {
			list = Utilities.findNodes(expr, doc.getDocumentElement());
		} catch (XPathExpressionException e) {
			return null;
		}
		if (list == null || list.getLength() == 0)
			return null;

		//从顺序位开始返回符合业务编号条件且为SVG标准节点的节点
		int size = list.getLength();
		int i = 0;
		for(i = 0;i < size;i++)
		{
			if(list.item(i).getParentNode() instanceof SVGOMMetadataElement
					&& list.item(i).getParentNode().getParentNode() instanceof SVGOMElement)
			{
				return list.item(i).getParentNode().getParentNode();
			}
		}
		return null;
	}

	/**
	 * 重置文字节点的文字内容
	 * 
	 * @param handle：待维护的svghandle
	 * @param node：节点
	 * @param newValue：新文字内容
	 */
	protected void resetTextContent(SVGHandle handle, Node node, String newValue) {
		String[] tspanString = newValue.split("\n");
		Document svgDoc = handle.getCanvas().getDocument();
		
		if (tspanString.length > 1) {

			SVGOMTextElement textEle = (SVGOMTextElement) node;
			//根据字体大小，设置多行间距，默认为16
			String fontSize = editor.getSVGToolkit().getStyleProperty(textEle, "font-size");
			if(fontSize == null || fontSize.length() == 0)
			{
				fontSize = textEle.getAttribute("font-size");
			}
			if(fontSize == null || fontSize.length() == 0)
			{
				fontSize = "16";
			}
			int font_size = 16;
			try
			{
			font_size = new Integer(fontSize);
			}
			catch(Exception ex)
			{
				font_size = 16;
			}
			font_size += 2;
			NodeList children = textEle.getChildNodes();
			for (int i = children.getLength(); i > 0; i--) {
				textEle.removeChild(children.item(i - 1));
			}
			String x = textEle.getAttribute("x");
			String y = textEle.getAttribute("y");
			int startY = (int) Double.parseDouble(y);

			for (int i = 0; i < tspanString.length; i++) {

				Element tspan = svgDoc.createElementNS(svgDoc
						.getDocumentElement().getNamespaceURI(),
						SVGDOMNormalizer.tspanTagName);
				tspan.setAttribute("x", x);
				tspan.setAttribute("y", String.valueOf(startY));
				Text textValue = svgDoc.createTextNode(tspanString[i]);
				tspan.appendChild(textValue);
				textEle.appendChild(tspan);
				startY = startY + font_size;
			}

		} else if (tspanString.length <= 1) {
			SVGOMTextElement textEle = (SVGOMTextElement) node;
			NodeList children = textEle.getChildNodes();
			for (int i = children.getLength(); i > 0; i--) {
				textEle.removeChild(children.item(i - 1));
			}
			if (tspanString.length == 1) {
				Text textValue = svgDoc.createTextNode(tspanString[0]);
				textEle.appendChild(textValue);
			}
		}
	}

	/**
	 * 切换图元状态
	 * 
	 * @param handle
	 * @param node：图元节点
	 * @param symbolStatus：目标状态
	 */
	protected void resetSymbolStatus_Node(SVGHandle handle, Node node,
			String symbolStatus) {
		if (handle == null || node == null || symbolStatus == null
				|| symbolStatus.length() == 0) {
			return;
		}

		if (node instanceof SVGOMUseElement) {
			SVGOMUseElement element = (SVGOMUseElement) node;
			editor.getSvgSession().refreshHandle(element);
			Document doc = handle.getCanvas().getDocument();
			// 获取节点引用的图元信息
			String href = element.getHref().getBaseVal();
			String symbolID = href.substring(1);
			String[] symbolInfo = symbolID.split(Constants.SYMBOL_STATUS_SEP);

			//符合公司规范的图元，则进行转换
			if (symbolInfo != null && symbolInfo.length == 2) {
				symbolID = symbolInfo[0] + Constants.SYMBOL_STATUS_SEP + symbolStatus;
				String expr = "//*[@id='" + symbolID + "']";
				//校验待转换的目标状态是否存在，存在则转换
				Node symbolNode = null;
				try {
					symbolNode = Utilities.findNode(expr, doc
							.getDocumentElement());
				} catch (XPathExpressionException e) {
					symbolNode = null;
				}
				if(symbolNode != null) {
					element.setAttributeNS(EditorToolkit.xmlnsXLinkNS, "xlink:href",
			                "#" +symbolID);
					editor.getSvgSession().refreshHandle(element);
				}
			}

		}
        return;
	}
}
