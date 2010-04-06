package com.nci.svg.server.automapping.area;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import com.nci.svg.server.automapping.comm.AutoMapResultBean;

/**
 * <p>
 * ���⣺CreateAreaMap.java
 * </p>
 * <p>
 * ����������SVG�ļ�
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2009-03-07
 * @version 1.0
 */
public class CreateAreaMap {
	/**
	 * ̨������
	 */
	private Area area;
	/**
	 * �ļ�����
	 */
	private Document doc;
	/**
	 * SVG���ڵ�
	 */
	private Element root;
	/**
	 * ̨����ѹ�ȼ�
	 */
	private String voltage;
	/**
	 * ��ѹ�ȼ���css��Ӧ��
	 */
	private HashMap voltages;
	/**
	 * ����ƽ��
	 */
	private String translate;

	/**
	 * ���캯��
	 * 
	 * @param area
	 */
	public CreateAreaMap(Area area) {
		this.area = area;
		// ��ȡ̨����ѹ�ȼ�
		AreaTransformer tf = area.getTransformer();
		this.voltage = (String) tf.getProperty("voltage");
		// ��ʼ����ѹ�ȼ���css��Ӧ��
		voltages = new HashMap();
		voltages.put("550kV", "KV550");
		voltages.put("330kV", "KV330");
		voltages.put("220kV", "KV220");
		voltages.put("110kV", "KV110");
		voltages.put("35kV", "KV350");
		voltages.put("20kV", "KV20");
		voltages.put("10kV", "KV10");
		voltages.put("18kV", "KV18");
		voltages.put("6kV", "KV6");
		voltages.put("380V", "V380");
		voltages.put("���Ե�", "KV���Ե�");
	}

	/**
	 * ����̨��ͼ��ͼ���ļ�
	 * 
	 * @return
	 */
	public AutoMapResultBean createSVG() {
		AutoMapResultBean resultBean = new AutoMapResultBean();

		// ***************
		// ����Document����
		// ***************
		resultBean = createDoc();
		if (resultBean.isFlag())
			this.doc = (Document) resultBean.getMsg();
		else
			return resultBean;

		// ************
		// ����SVG���ڵ�
		// ************
		createSvgHead();
		// ����css�ڵ�
		createSvgCss();
		// ����ͼԪ�ڵ�
		createSymbols();
		// �������
		createTransformer();
		// ���Ƹ���
		createPoles();
		// ������·
		createLines();

		return resultBean;
	}

	/**
	 * ����Document����
	 * 
	 * @return
	 */
	private AutoMapResultBean createDoc() {
		AutoMapResultBean resultBean = new AutoMapResultBean();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			resultBean.setFlag(false);
			resultBean.setErrMsg("����̨��ͼͼ���ļ������д���Document����ʧ�ܣ�");
			e.printStackTrace();
		}
		Document document = db.newDocument();
		resultBean.setMsg(document);
		return resultBean;
	}

	/**
	 * ������·
	 */
	private void createLines() {
		LinkedHashMap lines = area.getLinesList();
		Iterator it = lines.values().iterator();
		while (it.hasNext()) {
			AreaLine line = (AreaLine) it.next();
			Element path = doc.createElement("path");
			path.setAttribute("id", (String) line.getProperty("code"));
			path.setAttribute("style", "fill:none;");
			path.setAttribute("d", calculatePathD(line));
			path.setAttribute("transform", translate);
			path.setAttribute("class", (String) voltages.get(voltage));

			Element metadata = doc.createElement("metadata");
			Element psr = doc.createElement("PSR:ObjRef");
			String[] parameterNames = line.getPropertNames();
			for (int i = 0, size = parameterNames.length; i < size; i++) {
				Object oValue = line.getProperty(parameterNames[i]);
				String pValue;
				if (oValue instanceof String) {
					pValue = (String) oValue;
					psr.setAttribute(parameterNames[i], pValue);
				}
			}
			metadata.appendChild(psr);
			path.appendChild(metadata);
			root.appendChild(path);
		}
	}

	private String calculatePathD(AreaLine line) {
		StringBuffer d = new StringBuffer();
		ArrayList poles = line.getPoles();

		double[] prePoint = new double[2];
		for (int i = 0, size = poles.size(); i < size; i++) {
			String poleCode = (String) poles.get(i);
			AreaPole pole = (AreaPole) area.getElectricPoleList().get(
					poleCode);
			double[] point = pole.getCoordinate();
			if (!line.isBranch() && line.isMiddle() && pole.isPlugin()) {
				// ������䵽�ø�����·
				Element path = doc.createElement("path");
				path.setAttribute("style", "fill:none;");
				path.setAttribute("d", "M0 0L" + point[0] + " " + point[1]);
				path.setAttribute("transform", translate);
				path.setAttribute("class", (String) voltages.get(voltage));
				root.appendChild(path);
			}
			if (i == 0) {
				d.append("M");
			} else {
				d.append("L");
				// ������·ע��
				double[] tempPoint = new double[2];
				tempPoint[0] = (prePoint[0] + point[0]) / 2;
				tempPoint[1] = (prePoint[1] + point[1]) / 2;
				double s = ((Double) pole.getProperty("distance"))
						.doubleValue();
				Element text = doc.createElement("text");
				text.setAttribute("id", "Text" + poleCode);
				text.setAttribute("x", Double.toString(tempPoint[0]));
				text.setAttribute("y", Double.toString(tempPoint[1]+7));
				text.setAttribute("font-family", "SimSun");
				text.setAttribute("transform", translate);
				text.setAttribute("font-size", "10");
				Text textValue = doc.createTextNode(Double.toString(s));
				text.appendChild(textValue);
				root.appendChild(text);
			}
			d.append(point[0]).append(" ").append(point[1]);
			prePoint = point;
		}

		return d.toString();
	}

	/**
	 * �������
	 */
	private void createTransformer() {
		int width = 35;
		int height = 26;
		AreaTransformer tsf = area.getTransformer();
		Element g = doc.createElement("g");
		g.setAttribute("id", (String) tsf.getProperty("code"));
		Element use = doc.createElement("use");
		use.setAttribute("x", Double.toString(0 - width / 2));
		use.setAttribute("y", Double.toString(0 - height / 2));
		use.setAttribute("xlink:href", "#��·���");
		use.setAttribute("class", (String) voltages.get(voltage));
		use.setAttribute("transform", translate);
		use.setAttribute("width", Integer.toString(width));
		use.setAttribute("height", Integer.toString(height));

		Element metadata = doc.createElement("metadata");
		Element psr = doc.createElement("PSR:ObjRef");
		String[] parameterNames = tsf.getPropertNames();
		for (int i = 0, size = parameterNames.length; i < size; i++) {
			Object oValue = tsf.getProperty(parameterNames[i]);
			String pValue;
			if (oValue instanceof String) {
				pValue = (String) oValue;
				psr.setAttribute(parameterNames[i], pValue);
			}
		}
		metadata.appendChild(psr);
		use.appendChild(metadata);
		g.appendChild(use);
		root.appendChild(g);
	}

	/**
	 * ���Ƹ���
	 */
	private void createPoles() {
		int range = 14; // ͼԪ��С
		LinkedHashMap poleMap = area.getElectricPoleList();
		Iterator it = poleMap.values().iterator();
		while (it.hasNext()) {
			// **********
			// �����Ӹ���
			// **********
			AreaPole pole = (AreaPole) it.next();
			if (pole.isLogic()) {
				// �߼��˲�����
				continue;
			}
			double[] tempPoint = pole.getCoordinate();
			Element g = doc.createElement("g");
			String poleCode = (String) pole.getProperty("code");
			g.setAttribute("id", poleCode);

			Element use = doc.createElement("use");
			use.setAttribute("x", Double.toString(tempPoint[0] - range / 2));
			use.setAttribute("y", Double.toString(tempPoint[1] - range / 2));
			use.setAttribute("xlink:href", "#����");
			use.setAttribute("class", (String) voltages.get(voltage));
			use.setAttribute("transform", translate);
			use.setAttribute("width", Integer.toString(range));
			use.setAttribute("height", Integer.toString(range));

			Element metadata = doc.createElement("metadata");
			Element psr = doc.createElement("PSR:ObjRef");
			String[] parameterNames = pole.getPropertNames();
			for (int i = 0, size = parameterNames.length; i < size; i++) {
				Object oValue = pole.getProperty(parameterNames[i]);
				String pValue;
				if (oValue instanceof String) {
					pValue = (String) oValue;
					psr.setAttribute(parameterNames[i], pValue);
				}
			}
			metadata.appendChild(psr);
			use.appendChild(metadata);
			g.appendChild(use);
			root.appendChild(g);

			// ***********
			// ��Ӹ���ע��
			// ***********
			String polename = (String) pole.getProperty("name");
			Element text = doc.createElement("text");
			text.setAttribute("id", "Text" + poleCode);
			text.setAttribute("x", Double.toString(tempPoint[0] - range / 2));
			text.setAttribute("y", Double.toString(tempPoint[1] - range / 2));
			text.setAttribute("font-family", "SimSun");
			text.setAttribute("transform", translate);
			text.setAttribute("font-size", "10");
			Text textValue = doc.createTextNode(polename);
			text.appendChild(textValue);
			root.appendChild(text);
		}
	}

	/**
	 * ����SVGͷ
	 */
	private void createSvgHead() {
		long[] bound = getBound();
		String width = Long.toString(bound[0]);
		String height = Long.toString(bound[1]);
		translate = "translate(" + bound[2] + "," + bound[3] + " )";
		String[][] ns = { { "xmlns:producer", "http://www.nci.com.cn/ncird" },
				{ "xmlns:xlink", "http://www.w3.org/1999/xlink" },
				{ "xmlns:nci", "http://www.nci.com.cn" },
				{ "xmlns", "http://www.w3.org/2000/svg" },
				{ "xmlns:PSR", "http://www.cim.com" },
				{ "contentScriptType", "text/ecmascript" },
				{ "zoomAndPan", "magnify" },
				{ "contentStyleType", "text/css" },
				{ "preserveAspectRatio", "xMidYMid meet" },
				{ "width", "1024" }, { "height", "768" },
				{ "viewBox", "0 0 " + width + " " + height } };
		root = doc.createElement("svg");
		for (int i = 0, size = ns.length; i < size; i++) {
			root.setAttribute(ns[i][0], ns[i][1]);
		}

		doc.appendChild(root);
	}

	/**
	 * ����ͼԪ
	 */
	private void createSymbols() {
		// �������ͼԪ
		Element transformerElement = doc.createElement("symbol");
		transformerElement.setAttribute("id", "��·���");
		transformerElement.setAttribute("preserveAspectRatio", "xMidYMid meet");
		transformerElement.setAttribute("viewBox", "0 0 35 26");
		Element ellipseElement = doc.createElement("ellipse");
		ellipseElement.setAttribute("rx", "9");
		ellipseElement.setAttribute("ry", "9");
		ellipseElement.setAttribute("style", "stroke-width:2.0;fill:none;");
		ellipseElement.setAttribute("cx", "11");
		ellipseElement.setAttribute("cy", "11");
		transformerElement.appendChild(ellipseElement);
		ellipseElement = doc.createElement("ellipse");
		ellipseElement.setAttribute("rx", "9");
		ellipseElement.setAttribute("ry", "9");
		ellipseElement.setAttribute("style", "stroke-width:2.0;fill:none;");
		ellipseElement.setAttribute("cx", "24");
		ellipseElement.setAttribute("cy", "11");
		transformerElement.appendChild(ellipseElement);
		root.appendChild(transformerElement);
		// ���Ӹ���ͼԪ
		Element poleElement = doc.createElement("symbol");
		poleElement.setAttribute("id", "����");
		poleElement.setAttribute("preserveAspectRatio", "xMidYMid meet");
		poleElement.setAttribute("viewBox", "0 0 14 14");
		ellipseElement = doc.createElement("ellipse");
		ellipseElement.setAttribute("rx", "5");
		ellipseElement.setAttribute("ry", "5");
		ellipseElement.setAttribute("style", "stroke-width:2.0;fill:none;");
		ellipseElement.setAttribute("cx", "7");
		ellipseElement.setAttribute("cy", "7");
		poleElement.appendChild(ellipseElement);
		root.appendChild(poleElement);

	}

	/**
	 * ����css
	 */
	private void createSvgCss() {
		StringBuffer css = new StringBuffer();
		css.append(".KV500{stroke:rgb(255,140,0);fill:rgb(255,140,0)}").append(
				".KV330{stroke:rgb(255,255,100);fill:rgb(255,255,100)}")
				.append(".KV220{stroke:rgb(227,0,227);fill:rgb(227,0,227)}")
				.append(".KV110{stroke:rgb(255,0,0);fill:rgb(255,0,0)}")
				.append(".KV35{stroke:rgb(255,255,0);fill:rgb(255,255,0)}")
				.append(".KV20{stroke:rgb(180,255,180);fill:rgb(180,255,180)}")
				.append(".KV10{stroke:rgb(96,126,229);fill:rgb(96,126,229)}")
				.append(".KV18{stroke:rgb(0,170,0);fill:rgb(0,170,0)}").append(
						".KV6{stroke:rgb(180,0,0);fill:rgb(180,0,0)}").append(
						".V380{stroke:rgb(255,140,0);fill:rgb(255,140,0)}")
				.append(".KV���Ե�{stroke:rgb(110,0,0);fill:rgb(110,0,0)}");
		Node cssNode = doc.createCDATASection(css.toString());
		Element style = doc.createElement("style");
		style.setAttribute("type", "text/css");
		style.appendChild(cssNode);
		root.appendChild(style);
	}

	/**
	 * ��ȡ̨������������귶Χ
	 * 
	 * @return �����зֱ�������Ŀ��ߡ�X��ƽ������Y��ƽ����
	 */
	private long[] getBound() {
		long[] bound = new long[4];

		LinkedHashMap poleMap = area.getElectricPoleList();
		Iterator it = poleMap.values().iterator();
		// �������Ͻ�����
		double[] minPoint = { Double.MAX_VALUE, Double.MAX_VALUE };
		// �������½�����
		double[] maxPoint = { -1 * Double.MAX_VALUE, -1 * Double.MAX_VALUE };
		while (it.hasNext()) {
			AreaPole pole = (AreaPole) it.next();
			double[] tempPoint = pole.getCoordinate();
			if (minPoint[0] > tempPoint[0])
				minPoint[0] = tempPoint[0];
			if (minPoint[1] > tempPoint[1])
				minPoint[1] = tempPoint[1];
			if (maxPoint[0] < tempPoint[0])
				maxPoint[0] = tempPoint[0];
			if (maxPoint[1] < tempPoint[1])
				maxPoint[1] = tempPoint[1];
		}

		// ����ƽ�ƺ�����λ��
		if (Math.abs(maxPoint[0]) > Math.abs(minPoint[0]))
			minPoint[0] = -1 * maxPoint[0];
		else
			maxPoint[0] = -1 * minPoint[0];
		if (Math.abs(maxPoint[1]) > Math.abs(minPoint[1]))
			minPoint[1] = -1 * maxPoint[1];
		else
			maxPoint[1] = -1 * minPoint[1];

		// �����
		bound[0] = Math.round(maxPoint[0] - minPoint[0]);
		// �����
		bound[1] = Math.round(maxPoint[1] - minPoint[1]);
		// X��ƫ����
		bound[2] = (long) Math.abs(minPoint[0]);
		// Y��ƽ����
		bound[3] = (long) Math.abs(minPoint[1]);
		return bound;
	}
}
