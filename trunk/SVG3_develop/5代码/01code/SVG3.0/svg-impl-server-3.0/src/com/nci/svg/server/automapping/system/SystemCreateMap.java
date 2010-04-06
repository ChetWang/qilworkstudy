package com.nci.svg.server.automapping.system;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import com.nci.svg.server.automapping.comm.AutoMapResultBean;
import com.nci.svg.server.automapping.comm.Point;
import com.nci.svg.server.util.XmlUtil;

/**
 * <p>
 * 标题：SystemCreateMap.java
 * </p>
 * <p>
 * 描述： 系统图自动成图，图形文件生成类
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2009-7-17
 * @version 1.0
 */
public class SystemCreateMap {
	/**
	 * 文件对象
	 */
	private Document doc;
	/**
	 * SVG根节点
	 */
	private Element root;
	/**
	 * 稀疏矩阵
	 */
	private ArrayList stationMatrix;
	/**
	 * 厂站编号队列
	 */
	private ArrayList stationIds;
	/**
	 * 厂站集
	 */
	private HashMap stationMap;
	/**
	 * 线路集
	 */
	private ArrayList lineList;
	/**
	 * 图形中心点
	 */
	private Point centerPoint;

	/**
	 * @功能 构造函数
	 * @param stationIds:厂站编号队列
	 * @param stationMap:HashMap:厂站集
	 * @param linelist:ArrayList:线路集
	 * @param stationMatrix:ArrayList:稀疏矩阵
	 */
	public SystemCreateMap(ArrayList stationIds, HashMap stationMap,
			ArrayList linelist, ArrayList stationMatrix) {
		this.stationIds = stationIds;
		this.stationMap = stationMap;
		this.lineList = linelist;
		this.stationMatrix = stationMatrix;

		// 获取中心点
		String centerId = (String) stationIds.get(0);
		SystemSubstation centerSub = (SystemSubstation) stationMap
				.get(centerId);
		centerPoint = centerSub.getMapCoord();
	}

	/**
	 * 2009-7-17 Add by ZHM
	 * 
	 * @功能 生成SVG文件
	 * @return
	 */
	public AutoMapResultBean createSVG() {
		AutoMapResultBean result;
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
		// 创建css
		createSvgCss();
		// 创建图元
		createSymbols();
		// 绘制结构图主体
		createBody();

		// 获取SVG文件内容
		String svgContent = XmlUtil.printNode(doc, false);
		result.setMsg(svgContent);
		return result;
	}

	/**
	 * 2009-6-13 Add by ZHM
	 * 
	 * @功能 绘制结构图主体
	 */
	private void createBody() {
		// 绘制圆环
		createCircle();
		// 绘制厂站
		createStations();
		// 绘制线路
		createLines();
	}

	/**
	 * 2009-7-18 Add by ZHM
	 * 
	 * @功能 绘制线路
	 */
	private void createLines() {
		// TODO
	}

	/**
	 * 2009-7-17 Add by ZHM
	 * 
	 * @功能 绘制厂站
	 */
	private void createStations() {
		// 厂站个数
		int size = stationIds.size();
		for (int i = 0; i < size; i++) {
			String id = (String) stationIds.get(i);
			SystemSubstation sub = (SystemSubstation) stationMap.get(id);
			// 名称
			String name = (String) sub.getProperty("Name");
			// 电压等级
			String voltage = (String) sub.getProperty("VoltageLevel");
			// 厂站类型
			String subType = (String) sub.getProperty("substationType");
			// 发电站类型
			String type = (String) sub.getProperty("Type");
			// 坐标点
			Point point = sub.getMapCoord();

			Element g = doc.createElement("g");
			String[][] attrs = new String[][] { { "id", name + "_" + id } };
			XmlUtil.setElementAttributes(g, attrs);
			root.appendChild(g);

			Element text = doc.createElement("text");
			attrs = new String[][] {
					{ "x", Double.toString(point.getX()) },
					{ "y", Double.toString(point.getY() + 24) },
					{ "style", "font-size:12pt;fill:#6464ff;font-family:Simsun" } };
			XmlUtil.setElementAttributes(text, attrs);
			Text textValue = doc.createTextNode(name);
			text.appendChild(textValue);
			g.appendChild(text);

			Element use = doc.createElement("use");
			attrs = new String[][] {
					{ "x", Double.toString(point.getX() - 16) },
					{ "y", Double.toString(point.getY() - 16) },
					{ "width", "24" }, { "height", "24" }, { "class", voltage } };
			XmlUtil.setElementAttributes(use, attrs);
			g.appendChild(use);
			if ("Substation".equals(subType)) {
				// 变电站
				attrs = new String[][] { { "xlink:href", "#变电所" } };
			} else {
				// 电厂
				attrs = new String[][] { { "xlink:href", "#" + type } };
			}
			XmlUtil.setElementAttributes(use, attrs);
		}
	}

	/**
	 * 2009-7-17 Add by ZHM
	 * 
	 * @功能 创建圆环
	 */
	private void createCircle() {
		// 获取绘图最短界限
		int min = (SystemGlobal.CANVAS_HEIGHT < SystemGlobal.CANVAS_WIDTH) ? SystemGlobal.CANVAS_HEIGHT
				: SystemGlobal.CANVAS_WIDTH;
		// 环的个数
		int raSize = stationMatrix.size();
		// 步进长度
		double stepRadius = 1.0 * min / raSize;
		// 中心点X坐标
		String x = Double.toString(centerPoint.getX());
		// 中心点Y坐标
		String y = Double.toString(centerPoint.getY());

		Element g = doc.createElement("g");
		String[][] attrs = new String[][] { { "id", "systemCircle" } };
		XmlUtil.setElementAttributes(g, attrs);
		root.appendChild(g);
		for (int i = 0; i < raSize; i++) {
			double r = stepRadius * (i + 1);
			Element circle = doc.createElement("circle");
			attrs = new String[][] {
					{ "cx", x },
					{ "cy", y },
					{ "r", Double.toString(r) },
					{ "style",
							"stroke:#000000;fill:none;stroke-dasharray:5 10;fill-opacity:0.8;" } };
			XmlUtil.setElementAttributes(circle, attrs);
			g.appendChild(circle);
		}
	}

	/**
	 * 创建图元
	 */
	private void createSymbols() {
		Element defs = doc.createElement("defs");
		root.appendChild(defs);
		String[][] attrs;

		// 变电所
		Element symbol = doc.createElement("symbol");
		attrs = new String[][] { { "preserveAspectRatio", "xMidYMid meet" },
				{ "id", "变电所" }, { "viewBox", "0 0 24 24" } };
		XmlUtil.setElementAttributes(symbol, attrs);

		Element ellipse = doc.createElement("ellipse");
		attrs = new String[][] { { "rx", "10" }, { "ry", "10" },
				{ "style", "stroke-width:2.0;fill:none;" }, { "cx", "11" },
				{ "cy", "12" } };
		XmlUtil.setElementAttributes(ellipse, attrs);
		symbol.appendChild(ellipse);

		ellipse = doc.createElement("ellipse");
		attrs = new String[][] { { "rx", "5" }, { "ry", "5" },
				{ "style", "stroke-width:2.0;fill:none;" }, { "cx", "11" },
				{ "cy", "12" } };
		XmlUtil.setElementAttributes(ellipse, attrs);
		symbol.appendChild(ellipse);
		defs.appendChild(symbol);

		// 水电厂
		symbol = doc.createElement("symbol");
		attrs = new String[][] { { "preserveAspectRatio", "xMidYMid meet" },
				{ "id", "水电厂" }, { "viewBox", "0 0 34 34" } };
		XmlUtil.setElementAttributes(symbol, attrs);

		Element path = doc.createElement("path");
		String d = "M7.742 13.042Q11.807 1.449 21.263 13.483";
		attrs = new String[][] { { "d", d },
				{ "style", "stroke-width:2.0;fill:none;" } };
		XmlUtil.setElementAttributes(path, attrs);
		symbol.appendChild(path);

		path = doc.createElement("path");
		d = "M20.351 12.269Q26.572 21.874 33.872 12.710";
		attrs = new String[][] { { "d", d },
				{ "style", "stroke-width:2.0;fill:none;" } };
		XmlUtil.setElementAttributes(path, attrs);
		symbol.appendChild(path);

		Element rect = doc.createElement("rect");
		attrs = new String[][] { { "x", "5.171" }, { "y", "1.559" },
				{ "width", "29.209" }, { "height", "22.523" },
				{ "style", "stroke-width:2.0;fill:none;" } };
		XmlUtil.setElementAttributes(rect, attrs);
		symbol.appendChild(rect);
		defs.appendChild(symbol);

		// 火电厂
		symbol = doc.createElement("symbol");
		attrs = new String[][] { { "preserveAspectRatio", "xMidYMid meet" },
				{ "id", "火电厂" }, { "viewBox", "0 0 34 34" } };
		XmlUtil.setElementAttributes(symbol, attrs);

		path = doc.createElement("path");
		d = "M6.901 18.853Q10.285 8.549 18.157 19.246";
		attrs = new String[][] { { "d", d },
				{ "style", "stroke-width:2.0;fill:none;" } };
		XmlUtil.setElementAttributes(path, attrs);
		symbol.appendChild(path);

		path = doc.createElement("path");
		d = "M17.398 18.166Q22.577 26.704 28.653 18.559";
		attrs = new String[][] { { "d", d },
				{ "style", "stroke-width:2.0;fill:none;" } };
		XmlUtil.setElementAttributes(path, attrs);
		symbol.appendChild(path);

		ellipse = doc.createElement("ellipse");
		attrs = new String[][] { { "rx", "15.84" }, { "ry", "15.84" },
				{ "style", "stroke-width:2.0;fill:none;" }, { "cx", "17.33" },
				{ "cy", "17.82" } };
		XmlUtil.setElementAttributes(ellipse, attrs);
		symbol.appendChild(ellipse);
		defs.appendChild(symbol);

		// 发电厂
		symbol = doc.createElement("symbol");
		attrs = new String[][] { { "preserveAspectRatio", "xMidYMid meet" },
				{ "id", "发电厂" }, { "viewBox", "0 0 34 34" } };
		XmlUtil.setElementAttributes(symbol, attrs);

		path = doc.createElement("path");
		d = "M6.901 18.853Q10.285 8.549 18.157 19.246";
		attrs = new String[][] { { "d", d },
				{ "style", "stroke-width:2.0;fill:none;" } };
		XmlUtil.setElementAttributes(path, attrs);
		symbol.appendChild(path);

		path = doc.createElement("path");
		d = "M17.398 18.166Q22.577 26.704 28.653 18.559";
		attrs = new String[][] { { "d", d },
				{ "style", "stroke-width:2.0;fill:none;" } };
		XmlUtil.setElementAttributes(path, attrs);
		symbol.appendChild(path);

		ellipse = doc.createElement("ellipse");
		attrs = new String[][] { { "rx", "15.84" }, { "ry", "15.84" },
				{ "style", "stroke-width:2.0;fill:none;" }, { "cx", "17.33" },
				{ "cy", "17.82" } };
		XmlUtil.setElementAttributes(ellipse, attrs);
		symbol.appendChild(ellipse);
		defs.appendChild(symbol);
	}

	/**
	 * 创建css
	 */
	private void createSvgCss() {
		StringBuffer css = new StringBuffer();
		css
				.append(".KV1000{stroke:rgb(0,0,225);fill:rgb(0,0,225)}\n")
				.append(".KV800{stroke:rgb(0,0,225);fill:rgb(0,0,225)}\n")
				.append(".KV750{stroke:rgb(250,128,10);fill:rgb(250,128,10)}\n")
				.append(".KV660{stroke:rgb(250,128,10);fill:rgb(250,128,10)}\n")
				.append(".KV500{stroke:rgb(255,0,0);fill:rgb(255,0,0)}\n")
				.append(".KV330{stroke:rgb(30,144,255);fill:rgb(30,144,255)}\n")
				.append(".KV220{stroke:rgb(128,0,128);fill:rgb(128,0,128)}\n")
				.append(".KV110{stroke:rgb(240,65,85);fill:rgb(240,65,85)}\n")
				.append(".KV66{stroke:rgb(255,204,0);fill:rgb(255,204,0)}\n")
				.append(".KV35{stroke:rgb(255,255,0);fill:rgb(255,255,0)}\n")
				.append(".KV20{stroke:rgb(226,172,6);fill:rgb(226,172,6)}\n")
				.append(".KV15.75{stroke:rgb(0,128,0);fill:rgb(0,128,0)}\n")
				.append(".KV13.8{stroke:rgb(0,210,0);fill:rgb(0,210,0)}\n")
				.append(".KV10{stroke:rgb(185,72,66);fill:rgb(185,72,66)}\n")
				.append(".中性点kV{stroke:rgb(110,0,0);fill:rgb(110,0,0)}\n");
		Node cssNode = doc.createCDATASection(css.toString());
		Element style = doc.createElement("style");
		style.setAttribute("type", "text/css");
		style.appendChild(cssNode);
		root.appendChild(style);
	}

	/**
	 * 创建SVG头
	 */
	private void createSvgHead() {
		root = doc.createElement("svg");
		String[][] attrs = { { "xmlns", "http://www.w3.org/2000/svg" },
				{ "width", "100%" }, { "height", "100%" },
				{ "xmlns:xlink", "http://www.w3.org/1999/xlink" },
				{ "xmlns:nci", "http://www.nci.com.cn" } };
		XmlUtil.setElementAttributes(root, attrs);

		// 增加文件描述对象
		Element desc = doc.createElement("desc");
		attrs = new String[][] { { "nci-filetype", "system" } };
		XmlUtil.setElementAttributes(desc, attrs);
		root.appendChild(desc);

		doc.appendChild(root);
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

}
