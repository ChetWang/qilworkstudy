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
 * 标题：CreateFrameMap.java
 * </p>
 * <p>
 * 描述： 结构图文件生成类
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2009-6-7
 * @version 1.0
 */
/**
 * <p>
 * 标题：CreateFrameMap.java
 * </p>
 * <p>
 * 描述：
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2009-6-28
 * @version 1.0
 */
public class CreateFrameMap {
	/**
	 * 文件对象
	 */
	private Document doc;
	/**
	 * SVG根节点
	 */
	private Element root;
	/**
	 * 结构图数据
	 */
	private FrameNode data;
	/**
	 * 上下文路径
	 */
	private String contextPath;

	public CreateFrameMap(FrameNode data, String contextPath) {
		this.data = data;
		this.contextPath = contextPath;
	}

	/**
	 * 2009-6-7 Add by ZHM
	 * 
	 * @功能 系统结构图生成
	 * @return
	 */
	public AutoMapResultBean createSVG() {
		AutoMapResultBean result;
		// *********
		// 数据初始化
		// *********
		result = init();
		if (!result.isFlag()) {
			// 初始化失败
			return result;
		}
		// ***************
		// 生成Document对象
		// ***************
		result = createDoc();
		if (result.isFlag()) {
			this.doc = (Document) result.getMsg();
		} else {
			// 生成document对象失败
			return result;
		}

		// 创建SVG文件头
		createSvgHead();
		// 创建SVG文件引用
		// createSvgRefer();
		// 创建css
		createSvgCss();
		// 创建图元
		createSymbols();
		// 绘制结构图主体
		createBody();
		// 创建浮动提示框
		createFloatMesBox();

		// 获取SVG文件内容
		// String svgContent = XmlUtil.xmlToString(doc);
		String svgContent = XmlUtil.printNode(doc, false);
//		System.out.println(svgContent);
		result.setMsg(svgContent);
		return result;
	}

	/**
	 * 2009-6-12 Add by ZHM
	 * 
	 * @功能 初始化结构图数据
	 * @return
	 */
	private AutoMapResultBean init() {
		AutoMapResultBean result;
		// 检查数据是否合法
		CheckData checkData = new CheckData(data);
		result = checkData.check();
		if (!result.isFlag()) {
			return result;
		}
		// 节点坐标计算
		CoordinateCalculate calculate = new CoordinateCalculate(data);
		result = calculate.calculate();
		return result;
	}

	/**
	 * 构造Document对象
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
	 * 创建SVG头
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

		// 增加文件描述对象
		Element desc = doc.createElement("desc");
		attrs = new String[][] { { "nci-filetype", "flow" } };
		XmlUtil.setElementAttributes(desc, attrs);
		root.appendChild(desc);

		// 增加背景对象
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
	 * @功能 创建SVG文件的外部引用
	 */
	private void createSvgRefer() {
		Element eJs = doc.createElement("script");
		String[][] attrs = { { "xlink:href", "tool_tip.js" },
				{ "type", "text/javascript" } };
		XmlUtil.setElementAttributes(eJs, attrs);
		root.appendChild(eJs);
	}

	/**
	 * 创建css
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
	 * 创建图元
	 */
	private void createSymbols() {
		Element defs = doc.createElement("defs");
		String[][] attrs;
		// ****************
		// 增加展开和收起图元
		// ****************
		Element g = doc.createElement("g");
		attrs = new String[][] { { "id", "openRclose" } };
		XmlUtil.setElementAttributes(g, attrs);

		// 展开图元
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

		// 收起图元
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
		// 添加渐进定义
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
	 * @功能 创建SVG浮动提示框
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
	 * 获取系统图范围
	 * 
	 * @return 数组中分别是区域的宽、高
	 */
	private long[] getBound() {
		long[] bound = new long[4];
		bound[0] = Math.round(data.getRight());
		bound[1] = Math.round(data.getButtom());
		// 加上节点本身大小
		bound[0] += FrameGlobal.HORIZONTAL_INTERVAL;
		bound[1] += FrameGlobal.VERTICAL_INTERVAL;
		return bound;
	}

	/**
	 * 2009-6-13 Add by ZHM
	 * 
	 * @功能 绘制结构图主体
	 */
	private void createBody() {
		// 绘制整体图形
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
	 * @功能 绘制子节点
	 * @param wholeG:Element:whole节点
	 * @param parentG:Element:父节点
	 * @param node:FrameNode:结构图数据
	 * @param layerNum:int:当前数据所处层数
	 */
	private void drawChildNode(Element wholeG, Element parentG, FrameNode node,
			int layerNum) {
		String[][] attrs; // 元素属性数组
		String id = node.getId(); // 节点ID
		Element g = doc.createElement("g");
		attrs = new String[][] { { "id", id }, { "group_style", "flow_part" } };
		XmlUtil.setElementAttributes(g, attrs);
		// 绘制节点矩形
		Element box = drawBox(node, layerNum);
		g.appendChild(box);
		// 绘制节点文本
		Element text = drawText(node);
		g.appendChild(text);

		// 绘制连接线
		if (node.getParentNode() != null) {// 父节点存在
			drawRelationPath(node, wholeG, parentG, g);
			// g.appendChild(relaPath);
		}

		wholeG.appendChild(g);

		ArrayList children = node.getChildNodes();
		int childSize = children.size();
		if (childSize > 0) {
			layerNum++;
			// // 绘制展开图标
			// Element open = drawOpenFlag(node);
			// g.appendChild(open);
			// 递归绘制子节点
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
	 * @功能 绘制节点上的图标
	 * @param node
	 * @return
	 */
	private Element drawImage(FrameNode node) {
		String[][] attrs; // 元素属性数组
		String id = node.getId(); // 节点ID
		Point coord = node.getCoordinate(); // 节点坐标
		String x;// X轴坐标
		String y;// Y轴坐标

		Element g = doc.createElement("g");
		attrs = new String[][] { { "id", id + "image" } };
		XmlUtil.setElementAttributes(g, attrs);

		// *************
		// 责任书图标添加
		// *************
		if (node.getModeType() == FrameGlobal.HORIZONTAL_MODE) {
			// 横排时图标坐标
			x = Double.toString(coord.getX() - 0.5
					* FrameGlobal.HORIZONTAL_NODE_WIDTH);
			y = Double
					.toString(coord.getY()
							+ (FrameGlobal.HORIZONTAL_NODE_HEIGHT - FrameGlobal.IMAGE_RANGE)
							/ 2);
		} else {
			// 竖排时图标坐标
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
		String hideMes = node.getName() + "  任务书";
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
		// 危险点图标添加
		// **************
//		if (node.getModeType() == FrameGlobal.HORIZONTAL_MODE) {
//			// 横排时图标坐标
//			x = Double.toString(coord.getX() + 0.5
//					* FrameGlobal.HORIZONTAL_NODE_WIDTH
//					- FrameGlobal.IMAGE_RANGE);
//			y = Double
//					.toString(coord.getY()
//							+ (FrameGlobal.HORIZONTAL_NODE_HEIGHT - FrameGlobal.IMAGE_RANGE)
//							/ 2);
//		} else {
//			// 竖排时图标坐标
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
//		hideMes = node.getName() + "  危险点";
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
	 * @功能 绘制指定节点的文本
	 * @param node
	 */
	private Element drawText(FrameNode node) {
		String[][] attrs; // 元素属性数组
		String id = node.getId(); // 节点ID
		Element g = doc.createElement("g");
		attrs = new String[][] { { "id", id + "_g" } };
		XmlUtil.setElementAttributes(g, attrs);

		Point coord = node.getCoordinate(); // 节点坐标
		String name = node.getName(); // 节点名称
		name = name.trim(); // 去除字符串两端空格

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

		// 根据节点样式定制文字排列方向
		if (node.getModeType() == FrameGlobal.HORIZONTAL_MODE) {
			attrs = new String[][] { { "writing-mode", "lr_tb" } };
		} else {
			attrs = new String[][] { { "writing-mode", "tb" } };
		}
		XmlUtil.setElementAttributes(text, attrs);

		if (node.getModeType() == FrameGlobal.HORIZONTAL_MODE) {
			// ***********
			// 横排文字绘制
			// ***********
			// *******************************
			// 名称字符串中存在"\n"表示多行显示名称
			// *******************************
			String[] names = name.split("\n");
			int rowCount = names.length;
			double blankY = 1.0 * (FrameGlobal.HORIZONTAL_NODE_HEIGHT - rowCount
					* FrameGlobal.FONT_SIZE) / 2;
			for (int i = 0; i < rowCount; i++) {
				// 当前文字长度
				int wordCount = names[i].length();
				if (wordCount > 6) {
					// 如果文字多于6个则截断
					wordCount = 6;
					names[i] = names[i].substring(0, 5) + "…";
				}
				// 当前文字起始坐标
				double x = coord.getX() - wordCount * 7.5;
				if (contextPath != null && contextPath.length() > 0) {
					// 存在图标时文字坐标点起始位置
					double imageX = node.getCoordinate().getX() - 0.5
							* FrameGlobal.HORIZONTAL_NODE_WIDTH
							+ FrameGlobal.IMAGE_RANGE;
					if (x < imageX) {
						// 如果文字被图标覆盖则将文字X轴坐标紧跟图标
						x = imageX;
					}
				}
				if (x < coord.getX() - 0.5 * FrameGlobal.HORIZONTAL_NODE_WIDTH) {
					// 保证文字不会突出到框左边
					x = coord.getX() - 0.5 * FrameGlobal.HORIZONTAL_NODE_WIDTH;
				}
				double y = coord.getY() + blankY + FrameGlobal.FONT_SIZE
						* (i + 1);
				// 字间间隔
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
			// 竖排文字绘制
			// ************
			String[] names = name.split("\n");
			int rowCount = names.length;
			double blankX = 1.0 * (FrameGlobal.VERTICAL_NODE_WIDTH - rowCount
					* FrameGlobal.FONT_SIZE) / 2;
			for (int i = 0; i < rowCount; i++) {
				// 当前文字长度
				int wordCount = names[i].length();
				if (wordCount > 6) {
					// 如果文字多于6个则截断
					wordCount = 6;
					names[i] = names[i].substring(0, 5) + "…";
				}
				// 当前文字起始坐标
				double x = coord.getX() - 0.5 * FrameGlobal.VERTICAL_NODE_WIDTH
						+ blankX + FrameGlobal.FONT_SIZE * (i + 0.5);
				double y = coord.getY() + FrameGlobal.FONT_SIZE;
				if (contextPath != null && contextPath.length() > 0) {
					// 存在图标时文字坐标点起始位置
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
		// 增加节点图标
		FrameNode parNode = node.getParentNode();
		if (parNode != null && contextPath != null && contextPath.length() > 0) {
			g.appendChild(drawImage(node));
		}
		return g;
	}

	/**
	 * 2009-6-13 Add by ZHM
	 * 
	 * @功能 绘制节点矩形
	 * @param node
	 * @return
	 */
	private Element drawBox(FrameNode node, int layerNum) {
		String[][] attrs; // 元素属性数组
		String id = node.getId(); // 节点ID
		Point coord = node.getCoordinate(); // 节点坐标
		// String message = node.getMessage(); // 节点隐式信息
		int colorIndex = 0;
		if (layerNum > 0)
			colorIndex = 6;

		// 根据节点样式获取节点X轴、Y轴、宽、高
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
				// 暂时取消提示框功能
				{ "onmouseover", "ShowTooltip(evt, '" + node.getName() + "')" },
				{ "onmousemove", "ShowTooltip(evt, '" + node.getName() + "')" },
				{ "onmouseout", "HideTooltip(evt)" } };
		XmlUtil.setElementAttributes(rect, attrs);

		// // 绘制节点文本
		// Element text = drawText(node);
		//
		// rect.appendChild(text);

		return rect;
	}

	/**
	 * 2009-6-13 Add by ZHM
	 * 
	 * @功能 绘制展开图标
	 * @param node
	 * @return
	 */
	private Element drawOpenFlag(FrameNode node) {
		String[][] attrs; // 元素属性数组
		String id = node.getId(); // 节点ID
		Point coord = node.getCoordinate(); // 节点坐标
		double x = coord.getX() - 6; // 展开标志X轴位置
		double y = coord.getY() - 6; // 展开标准Y轴位置
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
		// 暂时取消子节点展开/合起功能
		// { "onclick", "switchChild('" + id + "')" }
		};
		XmlUtil.setElementAttributes(use, attrs);

		return use;
	}

	/**
	 * 2009-6-13 Add by ZHM
	 * 
	 * @功能 绘制节点间连接线
	 * @param node:FrameNode:结构图数据
	 * @param wholeG:Element:whole节点
	 * @param parentG:Element:父节点
	 * @param ChildG:Element:本节点
	 * @return
	 */
	private void drawRelationPath(FrameNode node, Element wholeG,
			Element parentG, Element childG) {
		String[][] attrs; // 元素属性数组
		String id = node.getId(); // 节点ID

		Element g = doc.createElement("g");
		attrs = new String[][] { { "id", id + "_path" }, { "model", "VH连接线" },
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
				// 增加父节点连接
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
		// // 父子连接点绘制后，连接线需要下移一段
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
	 * @功能 为指定节点添加连接线路编号
	 * @param childG:Element:本节点
	 * @param pathID:String:线路编号
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

		// 获取指定节点下的rect元素
		NodeList rects = childG.getElementsByTagName("rect");
		Object rectO = rects.item(0);
		if (rectO instanceof Element) {
			Element rect = (Element) rectO;
			rect.appendChild(connPath);
		}
	}
}
