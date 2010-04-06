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
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author yx.nci
 * @ʱ�䣺2008-12-9
 * @���ܣ�ͼ����Ӧ������ʵ����
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
		// TODO ��ʱ��ʵ�֣�Ŀǰ�޴�ҵ��������
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
		// ����λ�ã����ƶ���������Ӧ��λ��
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

		// ��ȡ���ϸ�ͼԪ��ŵĽڵ�
		Node node = getNodeBySymbolID(handle, symbolID);
		if (node == null || !(node instanceof SVGOMUseElement))
			return OPER_ERROR;
		// ������Ч�Ľڵ����״̬�л�
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
			// ����ͼԪ��Ż�ȡ�ڵ�
			Node node = getNodeBySymbolID(handle, symbolID[i]);
			if (node == null || !(node instanceof SVGOMUseElement))
				continue;
			Element element = (Element) node;

			if (cssRemark == null || cssRemark.length < i
					|| cssRemark[i] == null || cssRemark[i].length() == 0)
				element.removeAttribute("class");
			else {
				// TODO��Ŀǰ��ʱȱ�ٶ�cssУ��
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
			// ����ҵ���Ż�ȡ�ڵ�
			Node node = getNodeByBussID(handle, bussID[i]);
			if (node == null || !(node instanceof SVGOMUseElement))
				continue;
			Element element = (Element) node;

			// ���������������������ҵ����ԭ����ɫ��Ⱦ
			if (cssRemark == null || cssRemark.length < i
					|| cssRemark[i] == null || cssRemark[i].length() == 0)
				element.removeAttribute("class");
			else {
				// TODO��Ŀǰ��ʱȱ�ٶ�cssУ��
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
			// ��ȡ���ϸ�ҵ���ŵĽڵ�
			Node node = getNodeByBussID(handle, bussID[i]);
			if (node == null || !(node instanceof Element))
				continue;
			Element element = (Element) node;
			// �����ת��Ҫ��������ת��
			if (matrix != null && matrix.length >= i) {
				String matrixText = matrix[i];
				if (matrixText != null && matrixText.length() > 0) {
					element.setAttribute("transform", matrixText);
				} else
					element.removeAttribute("transform");
			} else {
				element.removeAttribute("transform");
			}

			// �����λ��ת������������λ��
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
			// ��ȡ���ϸ�ҵ���ŵĽڵ�
			Node node = getNodeByBussID(handle, bussID[i]);
			if (node == null || !(node instanceof SVGOMUseElement))
				continue;

			if (symbolStatus == null || symbolStatus.length < i)
				continue;
			// ������Ч�Ľڵ����״̬�л�
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

			// �������ɫ����Ҫ����������ɫ
			if (rgb != null && rgb.length >= i) {
				String rgbText = rgb[i];
				if (rgbText != null && rgbText.length() > 0) {
					editor.getSVGToolkit().setStyleProperty(element, "fill",
							rgbText);

				}
			}

			// �����content����Ҫ������������
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
	 * �ڴ���Ľ����л�ȡ����ͼԪ��ŵĽڵ�
	 * 
	 * @param handle:����
	 * @param symbolID:ͼԪ���
	 * @return:�ڵ㣬�����򷵻ؽڵ���󣬲������򷵻�null
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
	 * �ڴ���Ľ����л�ȡ����ҵ���ŵĽڵ�
	 * 
	 * @param handle:����
	 * @param bussID:ҵ����
	 * @return:�ڵ㣬�����򷵻ؽڵ���󣬲������򷵻�null
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

		//��˳��λ��ʼ���ط���ҵ����������ΪSVG��׼�ڵ�Ľڵ�
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
	 * �������ֽڵ����������
	 * 
	 * @param handle����ά����svghandle
	 * @param node���ڵ�
	 * @param newValue������������
	 */
	protected void resetTextContent(SVGHandle handle, Node node, String newValue) {
		String[] tspanString = newValue.split("\n");
		Document svgDoc = handle.getCanvas().getDocument();
		
		if (tspanString.length > 1) {

			SVGOMTextElement textEle = (SVGOMTextElement) node;
			//���������С�����ö��м�࣬Ĭ��Ϊ16
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
	 * �л�ͼԪ״̬
	 * 
	 * @param handle
	 * @param node��ͼԪ�ڵ�
	 * @param symbolStatus��Ŀ��״̬
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
			// ��ȡ�ڵ����õ�ͼԪ��Ϣ
			String href = element.getHref().getBaseVal();
			String symbolID = href.substring(1);
			String[] symbolInfo = symbolID.split(Constants.SYMBOL_STATUS_SEP);

			//���Ϲ�˾�淶��ͼԪ�������ת��
			if (symbolInfo != null && symbolInfo.length == 2) {
				symbolID = symbolInfo[0] + Constants.SYMBOL_STATUS_SEP + symbolStatus;
				String expr = "//*[@id='" + symbolID + "']";
				//У���ת����Ŀ��״̬�Ƿ���ڣ�������ת��
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
