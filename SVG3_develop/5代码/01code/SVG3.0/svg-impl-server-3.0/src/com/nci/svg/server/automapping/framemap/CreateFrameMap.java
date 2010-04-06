package com.nci.svg.server.automapping.framemap;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.nci.svg.sdk.server.util.ServerUtilities;
import com.nci.svg.server.automapping.comm.AutoMapResultBean;
import com.nci.svg.server.automapping.comm.Point;
import com.nci.svg.server.util.XmlUtil;

/**
 * <p>
 * ���⣺CreateFrameMap.java
 * </p>
 * <p>
 * ������ �ṹͼ�ļ�������
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2009-6-7
 * @version 1.0
 */
/**
 * <p>
 * ���⣺CreateFrameMap.java
 * </p>
 * <p>
 * ������
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2009-6-28
 * @version 1.0
 */
public class CreateFrameMap {
	/**
	 * �ļ�����
	 */
	private Document doc;
	/**
	 * SVG���ڵ�
	 */
	private Element root;
	/**
	 * �ṹͼ����
	 */
	private FrameNode data;
	/**
	 * ������·��
	 */
	private String contextPath;

	public CreateFrameMap(FrameNode data, String contextPath) {
		this.data = data;
		this.contextPath = contextPath;
	}

	/**
	 * 2009-6-7 Add by ZHM
	 * 
	 * @���� ϵͳ�ṹͼ����
	 * @return
	 */
	public AutoMapResultBean createSVG() {
		AutoMapResultBean result;
		// *********
		// ���ݳ�ʼ��
		// *********
		result = init();
		if (!result.isFlag()) {
			// ��ʼ��ʧ��
			return result;
		}
		// ***************
		// ����Document����
		// ***************
		result = createDoc();
		if (result.isFlag()) {
			this.doc = (Document) result.getMsg();
		} else {
			// ����document����ʧ��
			return result;
		}

		// ����SVG�ļ�ͷ
		createSvgHead();
		// ����SVG�ļ�����
		// createSvgRefer();
		// ����css
		createSvgCss();
		// ����ͼԪ
		createSymbols();
		// ���ƽṹͼ����
		createBody();
		// ����������ʾ��
		createFloatMesBox();

		// ��ȡSVG�ļ�����
		// String svgContent = XmlUtil.xmlToString(doc);
		String svgContent = XmlUtil.printNode(doc, false);
//		System.out.println(svgContent);
		result.setMsg(svgContent);
		return result;
	}

	/**
	 * 2009-6-12 Add by ZHM
	 * 
	 * @���� ��ʼ���ṹͼ����
	 * @return
	 */
	private AutoMapResultBean init() {
		AutoMapResultBean result;
		// ��������Ƿ�Ϸ�
		CheckData checkData = new CheckData(data);
		result = checkData.check();
		if (!result.isFlag()) {
			return result;
		}
		// �ڵ��������
		CoordinateCalculate calculate = new CoordinateCalculate(data);
		result = calculate.calculate();
		return result;
	}

	/**
	 * ����Document����
	 * 
	 * @return
	 */
	private AutoMapResultBean createDoc() {
		AutoMapResultBean resultBean = new AutoMapResultBean();
		Document document = XmlUtil.createDoc();
		resultBean.setMsg(document);
		return resultBean;
	}

	/**
	 * ����SVGͷ
	 */
	private void createSvgHead() {
		// long[] bound = getBound();
		// String width = Long.toString(bound[0]);
		// String height = Long.toString(bound[1]);
		root = doc.createElement("svg");
		String[][] attrs = {
				{ "xmlns", "http://www.w3.org/2000/svg" },
				// { "width", width },
				// { "height", height },
				// {
				// "viewBox",
				// "0 0 " + FrameGlobal.MAP_WIDTH + " "
				// + FrameGlobal.MAP_HEIGHT },
				{ "width", "100%" }, { "height", "100%" },
				{ "onload", "initializeSvg(evt);" },
				{ "onmousedown", "mouseDown(evt)" },
				{ "onmousemove", "mouseMove(evt)" },
				{ "onmouseup", "mouseUp(evt)" },
				{ "xmlns:xlink", "http://www.w3.org/1999/xlink" },
				{ "xmlns:nci", "http://www.nci.com.cn" },
				{ "onzoom", "ZoomControl()" } };
		XmlUtil.setElementAttributes(root, attrs);
		// attrs = new String[][] {
		// { "http://www.w3.org/2000/svg", "xmlns:xlink",
		// "http://www.w3.org/1999/xlink" },
		// { "http://www.w3.org/2000/svg", "xmlns:nci",
		// "http://www.nci.com.cn" } };
		// XmlUtil.setElementAttributesNS(root, ns);

		// �����ļ���������
		Element desc = doc.createElement("desc");
		attrs = new String[][] { { "nci-filetype", "flow" } };
		XmlUtil.setElementAttributes(desc, attrs);
		root.appendChild(desc);

		// ���ӱ�������
		Element rect = doc.createElement("rect");
		attrs = new String[][] { { "id", "BackDrop" }, { "x", "-10%" },
				{ "y", "-10%" }, { "width", "110%" }, { "height", "110%" },
				{ "fill", "none" }, { "pointer-events", "all" } };
		XmlUtil.setElementAttributes(rect, attrs);
		root.appendChild(rect);

		doc.appendChild(root);
	}

	/**
	 * 2009-6-8 Add by ZHM
	 * 
	 * @���� ����SVG�ļ����ⲿ����
	 */
	private void createSvgRefer() {
		Element eJs = doc.createElement("script");
		String[][] attrs = { { "xlink:href", "tool_tip.js" },
				{ "type", "text/javascript" } };
		XmlUtil.setElementAttributes(eJs, attrs);
		root.appendChild(eJs);
	}

	/**
	 * ����css
	 */
	private void createSvgCss() {
		StringBuffer css = new StringBuffer();
		css.append("\n.css1{stroke:black;fill:black}\n").append(
				".path1{stroke:black;fill:none;stroke-width:1}\n").append(
				".path2{stroke:#C4C4C4;fill:none;stroke-width:1}\n").append(
				".box0{stroke:#58AF44;fill:none;stroke-width:1}\n").append(
				".box1{stroke:#4B7FA6;fill:none;stroke-width:1}\n").append(
				".box2{stroke:#BB9964;fill:none;stroke-width:1}\n").append(
				".box3{stroke:#AB7ABB;fill:none;stroke-width:1}\n").append(
				".box4{stroke:#A2BB7A;fill:none;stroke-width:1}\n").append(
				".box5{stroke:#8FC9DA;fill:none;stroke-width:1}\n").append(
				".box6{stroke:#C1C1C1;fill:none;stroke-width:1}\n");
		Node cssNode = doc.createCDATASection(css.toString());
		Element style = doc.createElement("style");
		style.setAttribute("type", "text/css");
		style.appendChild(cssNode);
		root.appendChild(style);
	}

	/**
	 * ����ͼԪ
	 */
	private void createSymbols() {
		Element defs = doc.createElement("defs");
		String[][] attrs;
		// ****************
		// ����չ��������ͼԪ
		// ****************
		Element g = doc.createElement("g");
		attrs = new String[][] { { "id", "openRclose" } };
		XmlUtil.setElementAttributes(g, attrs);

		// չ��ͼԪ
		Element symbol = doc.createElement("symbol");
		attrs = new String[][] { { "id", "openRclose_open" },
				{ "preserveAspectRatio", "xMidYMid meet" } };
		XmlUtil.setElementAttributes(symbol, attrs);
		Element rect = doc.createElement("rect");
		attrs = new String[][] { { "x", "0" }, { "y", "0" },
				{ "height", "12" }, { "width", "12" },
				{ "style", "fill:white;stroke-width:3;fill-opacity:0" } };
		XmlUtil.setElementAttributes(rect, attrs);
		symbol.appendChild(rect);
		g.appendChild(symbol);

		// ����ͼԪ
		symbol = doc.createElement("symbol");
		attrs = new String[][] { { "id", "openRclose_close" },
				{ "preserveAspectRatio", "xMidYMid meet" } };
		XmlUtil.setElementAttributes(symbol, attrs);
		rect = doc.createElement("rect");
		attrs = new String[][] { { "x", "0" }, { "y", "0" },
				{ "height", "12" }, { "width", "12" },
				{ "style", "fill:white;stroke-width:3;fill-opacity:0" } };
		XmlUtil.setElementAttributes(rect, attrs);
		symbol.appendChild(rect);
		Element line = doc.createElement("line");
		attrs = new String[][] { { "style", "stroke-width:2;" }, { "x1", "6" },
				{ "y1", "0" }, { "x2", "6" }, { "y2", "12" } };
		XmlUtil.setElementAttributes(line, attrs);
		symbol.appendChild(line);
		g.appendChild(symbol);
		defs.appendChild(g);

		// ***********
		// ��ӽ�������
		// ***********
		String[][] linearColors = new String[][] {
				{ "#DDFFD8", "#99D96A", "#DDFFD8" },
				{ "#CBE6F9", "#81B5DD", "#CBE6F9" },
				{ "#FDECCF", "#CDAA6E", "#FDECCF" },
				{ "#F7E0FE", "#D1A1DE", "#F7E0FE" },
				{ "#F2FEE0", "#C6DFA1", "#F2FEE0" },
				{ "#E7F9FF", "#ADE0F3", "#E7F9FF" },
				{ "#F2F2F2", "#CFCDC0", "#F2F2F2" } };
		for (int i = 0; i < FrameGlobal.LINEAR_NUM; i++) {
			Element linearGradient = doc.createElement("linearGradient");
			attrs = new String[][] { { "id", "lg" + i }, { "x1", "0%" },
					{ "y1", "0%" }, { "x2", "0%" }, { "y2", "100%" } };
			XmlUtil.setElementAttributes(linearGradient, attrs);
			for (int j = 0; j < 3; j++) {
				Element stop = doc.createElement("stop");
				attrs = new String[][] {
						{ "offset", j * 50 + "%" },
						{
								"style",
								"stop-color:" + linearColors[i][j]
										+ ";stop-opacity:1" } };
				XmlUtil.setElementAttributes(stop, attrs);
				linearGradient.appendChild(stop);
			}
			defs.appendChild(linearGradient);
		}

		root.appendChild(defs);
	}

	/**
	 * 2009-6-8 Add by ZHM
	 * 
	 * @���� ����SVG������ʾ��
	 */
	private void createFloatMesBox() {
		Element eBox = doc.createElement("g");
		String[][] attrs = new String[][] { { "id", "tooltip" },
				{ "style", "pointer-events:none" } };
		XmlUtil.setElementAttributes(eBox, attrs);

		Element eRect = doc.createElement("rect");
		attrs = new String[][] { { "id", "ttr" }, { "x", "0" }, { "y", "0" },
				{ "rx", "5" }, { "ry", "5" }, { "width", "100" },
				{ "height", "16" }, { "style", "visibility: hidden" } };
		XmlUtil.setElementAttributes(eRect, attrs);
		eBox.appendChild(eRect);

		Element eText = doc.createElement("text");
		attrs = new String[][] { { "id", "ttt" }, { "x", "0" }, { "y", "0" },
				{ "style", "visibility:hidden" }, { "font-family", "SimSun" } };
		Text textValue = doc.createTextNode("Text");
		XmlUtil.setElementAttributes(eText, attrs);
		eText.appendChild(textValue);
		eBox.appendChild(eText);

		root.appendChild(eBox);
	}

	/**
	 * ��ȡϵͳͼ��Χ
	 * 
	 * @return �����зֱ�������Ŀ���
	 */
	private long[] getBound() {
		long[] bound = new long[4];
		bound[0] = Math.round(data.getRight());
		bound[1] = Math.round(data.getButtom());
		// ���Ͻڵ㱾���С
		bound[0] += FrameGlobal.HORIZONTAL_INTERVAL;
		bound[1] += FrameGlobal.VERTICAL_INTERVAL;
		return bound;
	}

	/**
	 * 2009-6-13 Add by ZHM
	 * 
	 * @���� ���ƽṹͼ����
	 */
	private void createBody() {
		// ��������ͼ��
		Element g = doc.createElement("g");
		String[][] attrs = new String[][] { { "id", "flow_whole" },
				{ "group_style", "flow_whole" } };
		XmlUtil.setElementAttributes(g, attrs);

		drawChildNode(g, null, data, 0);

		root.appendChild(g);
	}

	/**
	 * 2009-6-13 Add by ZHM
	 * 
	 * @���� �����ӽڵ�
	 * @param wholeG:Element:whole�ڵ�
	 * @param parentG:Element:���ڵ�
	 * @param node:FrameNode:�ṹͼ����
	 * @param layerNum:int:��ǰ������������
	 */
	private void drawChildNode(Element wholeG, Element parentG, FrameNode node,
			int layerNum) {
		String[][] attrs; // Ԫ����������
		String id = node.getId(); // �ڵ�ID
		Element g = doc.createElement("g");
		attrs = new String[][] { { "id", id }, { "group_style", "flow_part" } };
		XmlUtil.setElementAttributes(g, attrs);
		// ���ƽڵ����
		Element box = drawBox(node, layerNum);
		g.appendChild(box);
		// ���ƽڵ��ı�
		Element text = drawText(node);
		g.appendChild(text);

		// ����������
		if (node.getParentNode() != null) {// ���ڵ����
			drawRelationPath(node, wholeG, parentG, g);
			// g.appendChild(relaPath);
		}

		wholeG.appendChild(g);

		ArrayList children = node.getChildNodes();
		int childSize = children.size();
		if (childSize > 0) {
			layerNum++;
			// // ����չ��ͼ��
			// Element open = drawOpenFlag(node);
			// g.appendChild(open);
			// �ݹ�����ӽڵ�
			for (int i = 0; i < childSize; i++) {
				FrameNode child = (FrameNode) children.get(i);
				drawChildNode(wholeG, g, child, layerNum);
				// wholeG.appendChild(childG);
			}
		}
	}

	/**
	 * 2009-6-25 Add by ZHM
	 * 
	 * @���� ���ƽڵ��ϵ�ͼ��
	 * @param node
	 * @return
	 */
	private Element drawImage(FrameNode node) {
		String[][] attrs; // Ԫ����������
		String id = node.getId(); // �ڵ�ID
		Point coord = node.getCoordinate(); // �ڵ�����
		String x;// X������
		String y;// Y������

		Element g = doc.createElement("g");
		attrs = new String[][] { { "id", id + "image" } };
		XmlUtil.setElementAttributes(g, attrs);

		// *************
		// ������ͼ�����
		// *************
		if (node.getModeType() == FrameGlobal.HORIZONTAL_MODE) {
			// ����ʱͼ������
			x = Double.toString(coord.getX() - 0.5
					* FrameGlobal.HORIZONTAL_NODE_WIDTH);
			y = Double
					.toString(coord.getY()
							+ (FrameGlobal.HORIZONTAL_NODE_HEIGHT - FrameGlobal.IMAGE_RANGE)
							/ 2);
		} else {
			// ����ʱͼ������
			x = Double.toString(coord.getX() - 0.5 * FrameGlobal.IMAGE_RANGE);
			y = Double.toString(coord.getY());
		}
		Element image = doc.createElement("image");
		String imagePath;
		if (contextPath != null && contextPath.length() > 0) {
			imagePath = contextPath + "/standard/images/formula.gif";
		} else {
			imagePath = "../images/formula.gif";
		}
		String hideMes = node.getName() + "  ������";
		attrs = new String[][] { { "id", id + "image0" },
				{ "width", Integer.toString(FrameGlobal.IMAGE_RANGE) },
				{ "height", Integer.toString(FrameGlobal.IMAGE_RANGE) },
				{ "xlink:href", imagePath },
				{ "onclick", "showMessageDetail('" + id + "')" }, { "x", x },
				{ "y", y },
				{ "onmouseover", "ShowTooltip(evt, '" + hideMes + "')" },
				{ "onmousemove", "ShowTooltip(evt, '" + hideMes + "')" },
				{ "onmouseout", "HideTooltip(evt)" } };
		XmlUtil.setElementAttributes(image, attrs);
		g.appendChild(image);
		// **************
		// Σ�յ�ͼ�����
		// **************
//		if (node.getModeType() == FrameGlobal.HORIZONTAL_MODE) {
//			// ����ʱͼ������
//			x = Double.toString(coord.getX() + 0.5
//					* FrameGlobal.HORIZONTAL_NODE_WIDTH
//					- FrameGlobal.IMAGE_RANGE);
//			y = Double
//					.toString(coord.getY()
//							+ (FrameGlobal.HORIZONTAL_NODE_HEIGHT - FrameGlobal.IMAGE_RANGE)
//							/ 2);
//		} else {
//			// ����ʱͼ������
//			x = Double.toString(coord.getX() - 0.5 * FrameGlobal.IMAGE_RANGE);
//			y = Double.toString(coord.getY() + FrameGlobal.VERTICAL_NODE_HEIGHT
//					- FrameGlobal.IMAGE_RANGE);
//		}
//		image = doc.createElement("image");
//		if (contextPath != null && contextPath.length() > 0) {
//			imagePath = contextPath + "/standard/images/Warning.gif";
//		} else {
//			imagePath = "../images/Warning.gif";
//		}
//		hideMes = node.getName() + "  Σ�յ�";
//		attrs = new String[][] { { "id", id + "image1" },
//				{ "width", Integer.toString(FrameGlobal.IMAGE_RANGE) },
//				{ "height", Integer.toString(FrameGlobal.IMAGE_RANGE) },
//				{ "xlink:href", imagePath },
//				{ "onclick", "showDangerousMessage('" + id + "')" },
//				{ "x", x }, { "y", y },
//				{ "onmouseover", "ShowTooltip(evt, '" + hideMes + "')" },
//				{ "onmousemove", "ShowTooltip(evt, '" + hideMes + "')" },
//				{ "onmouseout", "HideTooltip(evt)" } };
//		XmlUtil.setElementAttributes(image, attrs);
//		g.appendChild(image);
		
		return g;
	}

	/**
	 * 2009-6-13 Add by ZHM
	 * 
	 * @���� ����ָ���ڵ���ı�
	 * @param node
	 */
	private Element drawText(FrameNode node) {
		String[][] attrs; // Ԫ����������
		String id = node.getId(); // �ڵ�ID
		Element g = doc.createElement("g");
		attrs = new String[][] { { "id", id + "_g" } };
		XmlUtil.setElementAttributes(g, attrs);

		Point coord = node.getCoordinate(); // �ڵ�����
		String name = node.getName(); // �ڵ�����
		name = name.trim(); // ȥ���ַ������˿ո�

		Element text = doc.createElement("text");
		attrs = new String[][] {
				{ "id", id + "_text" },
				{ "font-family", "SimSun" },
				{ "font-weight", "bold" },
				// { "writing-mode", "lr_tb" },
				{ "font-size", Integer.toString(FrameGlobal.FONT_SIZE) },
				{ "onmouseover", "ShowTooltip(evt, '" + node.getName() + "')" },
				{ "onmousemove", "ShowTooltip(evt, '" + node.getName() + "')" },
				{ "onmouseout", "HideTooltip(evt)" } };
		XmlUtil.setElementAttributes(text, attrs);
		g.appendChild(text);

		// ���ݽڵ���ʽ�����������з���
		if (node.getModeType() == FrameGlobal.HORIZONTAL_MODE) {
			attrs = new String[][] { { "writing-mode", "lr_tb" } };
		} else {
			attrs = new String[][] { { "writing-mode", "tb" } };
		}
		XmlUtil.setElementAttributes(text, attrs);

		if (node.getModeType() == FrameGlobal.HORIZONTAL_MODE) {
			// ***********
			// �������ֻ���
			// ***********
			// *******************************
			// �����ַ����д���"\n"��ʾ������ʾ����
			// *******************************
			String[] names = name.split("\n");
			int rowCount = names.length;
			double blankY = 1.0 * (FrameGlobal.HORIZONTAL_NODE_HEIGHT - rowCount
					* FrameGlobal.FONT_SIZE) / 2;
			for (int i = 0; i < rowCount; i++) {
				// ��ǰ���ֳ���
				int wordCount = names[i].length();
				if (wordCount > 6) {
					// ������ֶ���6����ض�
					wordCount = 6;
					names[i] = names[i].substring(0, 5) + "��";
				}
				// ��ǰ������ʼ����
				double x = coord.getX() - wordCount * 7.5;
				if (contextPath != null && contextPath.length() > 0) {
					// ����ͼ��ʱ�����������ʼλ��
					double imageX = node.getCoordinate().getX() - 0.5
							* FrameGlobal.HORIZONTAL_NODE_WIDTH
							+ FrameGlobal.IMAGE_RANGE;
					if (x < imageX) {
						// ������ֱ�ͼ�긲��������X���������ͼ��
						x = imageX;
					}
				}
				if (x < coord.getX() - 0.5 * FrameGlobal.HORIZONTAL_NODE_WIDTH) {
					// ��֤���ֲ���ͻ���������
					x = coord.getX() - 0.5 * FrameGlobal.HORIZONTAL_NODE_WIDTH;
				}
				double y = coord.getY() + blankY + FrameGlobal.FONT_SIZE
						* (i + 1);
				// �ּ���
				StringBuffer strDy = new StringBuffer();
				for (int k = 0; k < wordCount; k++) {
					if (k == wordCount - 1) {
						strDy.append("1");
					} else {
						strDy.append("1,");
					}
				}

				Element tspan = doc.createElement("tspan");
				attrs = new String[][] { { "id", id + "_tspan" },
						{ "x", Double.toString(x) },
						{ "y", Double.toString(y) }, { "dx", strDy.toString() } };
				XmlUtil.setElementAttributes(tspan, attrs);

				Text textValue = doc.createTextNode(names[i]);
				tspan.appendChild(textValue);
				text.appendChild(tspan);
			}
		} else {
			// ************
			// �������ֻ���
			// ************
			String[] names = name.split("\n");
			int rowCount = names.length;
			double blankX = 1.0 * (FrameGlobal.VERTICAL_NODE_WIDTH - rowCount
					* FrameGlobal.FONT_SIZE) / 2;
			for (int i = 0; i < rowCount; i++) {
				// ��ǰ���ֳ���
				int wordCount = names[i].length();
				if (wordCount > 6) {
					// ������ֶ���6����ض�
					wordCount = 6;
					names[i] = names[i].substring(0, 5) + "��";
				}
				// ��ǰ������ʼ����
				double x = coord.getX() - 0.5 * FrameGlobal.VERTICAL_NODE_WIDTH
						+ blankX + FrameGlobal.FONT_SIZE * (i + 0.5);
				double y = coord.getY() + FrameGlobal.FONT_SIZE;
				if (contextPath != null && contextPath.length() > 0) {
					// ����ͼ��ʱ�����������ʼλ��
					y += FrameGlobal.IMAGE_RANGE;
				}

				Element tspan = doc.createElement("tspan");
				attrs = new String[][] { { "id", id + "_tspan" },
						{ "x", Double.toString(x) },
						{ "y", Double.toString(y) } };
				XmlUtil.setElementAttributes(tspan, attrs);

				Text textValue = doc.createTextNode(names[i]);
				tspan.appendChild(textValue);
				text.appendChild(tspan);
			}
		}
		// ���ӽڵ�ͼ��
		FrameNode parNode = node.getParentNode();
		if (parNode != null && contextPath != null && contextPath.length() > 0) {
			g.appendChild(drawImage(node));
		}
		return g;
	}

	/**
	 * 2009-6-13 Add by ZHM
	 * 
	 * @���� ���ƽڵ����
	 * @param node
	 * @return
	 */
	private Element drawBox(FrameNode node, int layerNum) {
		String[][] attrs; // Ԫ����������
		String id = node.getId(); // �ڵ�ID
		Point coord = node.getCoordinate(); // �ڵ�����
		// String message = node.getMessage(); // �ڵ���ʽ��Ϣ
		int colorIndex = 0;
		if (layerNum > 0)
			colorIndex = 6;

		// ���ݽڵ���ʽ��ȡ�ڵ�X�ᡢY�ᡢ����
		double x;
		double y;
		int width;
		int height;
		int modeType = node.getModeType();
		if (modeType == FrameGlobal.HORIZONTAL_MODE) {
			width = FrameGlobal.HORIZONTAL_NODE_WIDTH;
			height = FrameGlobal.HORIZONTAL_NODE_HEIGHT;
		} else {
			width = FrameGlobal.VERTICAL_NODE_WIDTH;
			height = FrameGlobal.VERTICAL_NODE_HEIGHT;
		}
		x = coord.getX() - 0.5 * width;
		y = coord.getY();

		Element rect = doc.createElement("rect");
		attrs = new String[][] {
				{ "id", id + "_rect" },
				{ "x", Double.toString(x) },
				{ "y", Double.toString(y) },
				{ "width", Long.toString(width) },
				{ "height", Long.toString(height) },
				{ "style", "fill:url(#lg" + colorIndex + ")" },
				{ "class", "box" + colorIndex },
				// { "onclick", "showMessageDetail('" + id + "')" }
				// ��ʱȡ����ʾ����
				{ "onmouseover", "ShowTooltip(evt, '" + node.getName() + "')" },
				{ "onmousemove", "ShowTooltip(evt, '" + node.getName() + "')" },
				{ "onmouseout", "HideTooltip(evt)" } };
		XmlUtil.setElementAttributes(rect, attrs);

		// // ���ƽڵ��ı�
		// Element text = drawText(node);
		//
		// rect.appendChild(text);

		return rect;
	}

	/**
	 * 2009-6-13 Add by ZHM
	 * 
	 * @���� ����չ��ͼ��
	 * @param node
	 * @return
	 */
	private Element drawOpenFlag(FrameNode node) {
		String[][] attrs; // Ԫ����������
		String id = node.getId(); // �ڵ�ID
		Point coord = node.getCoordinate(); // �ڵ�����
		double x = coord.getX() - 6; // չ����־X��λ��
		double y = coord.getY() - 6; // չ����׼Y��λ��
		if (node.getModeType() == FrameGlobal.HORIZONTAL_MODE) {
			x += 0.5 * FrameGlobal.HORIZONTAL_NODE_WIDTH;
			y += 0.5 * FrameGlobal.HORIZONTAL_NODE_HEIGHT;
		} else {
			x += 0.5 * FrameGlobal.VERTICAL_NODE_WIDTH;
			y += 0.5 * FrameGlobal.VERTICAL_NODE_HEIGHT;
		}
		Element use = doc.createElement("use");
		attrs = new String[][] { { "id", id + "_use" },
				{ "x", Double.toString(x) }, { "y", Double.toString(y) },
				{ "width", "12" }, { "height", "12" },
				{ "xlink:href", "#openRclose_open" }, { "class", "css1" },
		// ��ʱȡ���ӽڵ�չ��/������
		// { "onclick", "switchChild('" + id + "')" }
		};
		XmlUtil.setElementAttributes(use, attrs);

		return use;
	}

	/**
	 * 2009-6-13 Add by ZHM
	 * 
	 * @���� ���ƽڵ��������
	 * @param node:FrameNode:�ṹͼ����
	 * @param wholeG:Element:whole�ڵ�
	 * @param parentG:Element:���ڵ�
	 * @param ChildG:Element:���ڵ�
	 * @return
	 */
	private void drawRelationPath(FrameNode node, Element wholeG,
			Element parentG, Element childG) {
		String[][] attrs; // Ԫ����������
		String id = node.getId(); // �ڵ�ID

		Element g = doc.createElement("g");
		attrs = new String[][] { { "id", id + "_path" }, { "model", "VH������" },
				{ "group_style", "nci_vh" } };
		XmlUtil.setElementAttributes(g, attrs);

		ArrayList points = node.getPoints();
		for (int i = 0, size = points.size(); i < size - 1; i++) {
			Point point = (Point) points.get(i);
			Point nextPoint = (Point) points.get(i + 1);
			StringBuffer d = new StringBuffer();
			d.append("M ").append(point.getX()).append(" ")
					.append(point.getY()).append(" L ")
					.append(nextPoint.getX()).append(" ").append(
							nextPoint.getY());
			String uuid = ServerUtilities.getUUIDString();
			Element path = doc.createElement("path");
			attrs = new String[][] { { "id", uuid }, { "class", "path2" },
					{ "d", d.toString() } };
			XmlUtil.setElementAttributes(path, attrs);
			if (i == 0) {
				// ���Ӹ��ڵ�����
				appendConnPath(parentG, uuid);
			} else if (i == size - 2) {
				appendConnPath(childG, uuid);
			}
			g.appendChild(path);
		}
		wholeG.appendChild(g);
		// StringBuffer d = new StringBuffer();
		// d.append("M ");
		// for (int i = 0, size = points.size(); i < size; i++) {
		// Point point = (Point) points.get(i);
		// double x = point.getX();
		// double y = point.getY();
		// // �������ӵ���ƺ���������Ҫ����һ��
		// // if (i == 0) {
		// // y += 6;
		// // }
		// String dx = Double.toString(x);
		// String dy = Double.toString(y);
		// d.append(dx).append(" ").append(dy).append(" ");
		// if (i != size - 1)
		// d.append("L ");
		// }
		//
		// Element path = doc.createElement("path");
		// attrs = new String[][] { { "id", id + "_path" }, { "class", "path2"
		// },
		// { "d", d.toString() } };
		// XmlUtil.setElementAttributes(path, attrs);
		// return path;
	}

	/**
	 * 2009-6-24 Add by ZHM
	 * 
	 * @���� Ϊָ���ڵ����������·���
	 * @param childG:Element:���ڵ�
	 * @param pathID:String:��·���
	 */
	private void appendConnPath(Element childG, String pathID) {
		// String paths = childG.getAttribute("conn_path");
		// if (paths == null || paths.length() == 0) {
		// childG.setAttribute("conn_path", pathID);
		// } else {
		// paths += "," + pathID;
		// childG.setAttribute("conn_path", paths);
		// }
		Element connPath = doc.createElement("connPath");
		String[][] attrs = new String[][] { { "pathid", pathID } };
		XmlUtil.setElementAttributes(connPath, attrs);

		// ��ȡָ���ڵ��µ�rectԪ��
		NodeList rects = childG.getElementsByTagName("rect");
		Object rectO = rects.item(0);
		if (rectO instanceof Element) {
			Element rect = (Element) rectO;
			rect.appendChild(connPath);
		}
	}
}
