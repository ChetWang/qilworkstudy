/*
 * Created on 21 juin 2004
 * 
 =============================================
 GNU LESSER GENERAL PUBLIC LICENSE Version 2.1
 =============================================
 GLIPS Graffiti Editor, a SVG Editor
 Copyright (C) 2003 Jordi SUC, Philippe Gil, SARL ITRIS
 
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.
 
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.
 
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 
 Contact : jordi.suc@itris.fr; philippe.gil@itris.fr
 
 =============================================
 */
package com.nci.svg.sdk.client.util;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.swing.JComboBox;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.xml.xpath.XPathExpressionException;

import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.dom.svg.SVGOMElement;
import org.apache.batik.dom.svg.SVGOMEllipseElement;
import org.apache.batik.dom.svg.SVGOMGElement;
import org.apache.batik.dom.svg.SVGOMPathElement;
import org.apache.batik.dom.svg.SVGOMTextElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nci.svg.module.TerminalModule;
import com.nci.svg.sdk.bean.ModelRelaIndunormBean;
import com.nci.svg.sdk.graphunit.NCIEquipSymbolBean;
import com.nci.svg.sdk.graphunit.SymbolTypeBean;
import com.nci.svg.sdk.other.LinkPointManager;
import com.nci.svg.sdk.other.LinkPointManager.LineData;
import com.nci.svg.sdk.shape.vhpath.VHPathShape;

import fr.itris.glips.library.FormatStore;
import fr.itris.glips.library.geom.path.Path;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * a class providing utility methods
 * 
 * @author ITRIS, Jordi SUC, Maciej Wojtkiewicz
 */
public class EditorToolkit {

	/**
	 * the xml file extension
	 */
	public static final String XML_FILE_EXTENSION = ".xml";

	/**
	 * the svg file extension
	 */
	public static final String SVG_FILE_EXTENSION = ".svg";

	/**
	 * the svgz file extension
	 */
	public static final String SVGZ_FILE_EXTENSION = ".svgz";

	/**
	 * the name space for declaring namespaces
	 */
	public static final String xmlnsNS = "http://www.w3.org/2000/xmlns/";

	/**
	 * the xlink attribute namespace name
	 */
	public static final String xmlnsXLinkAttributeName = "xmlns:xlink";

	/**
	 * the xlink prefix
	 */
	public static final String xLinkprefix = "xlink:";

	/**
	 * the xlink namespace
	 */
	public static final String xmlnsXLinkNS = "http://www.w3.org/1999/xlink";

	/**
	 * the svg namespace
	 */
	public static final String svgNS = "http://www.w3.org/2000/svg";

	public static final String svgNCINS = "http://www.nci.com.cn";

	/**
	 * the jwidget tag name
	 */
	public static final String jwidgetTagName = "rtda:jwidget";

	/**
	 * the decimal formatter
	 */
	public static DecimalFormat format = null;

	/**
	 * the map associating the name of a svg element shape to its label
	 */
	protected static HashMap<String, String> svgElementLabels = new HashMap<String, String>();

	/**
	 * the set of the available svg shape elements
	 */
	public static Set<String> svgShapeElementNames = new HashSet<String>();

	/**
	 * the label for an unknow shape
	 */
	public static String unknownShapeLabel = "";

	public static String hrefAtt = "xlink:href",
			preserveAtt = "preserveAspectRatio";
	public static String xAtt = "x", yAtt = "y", wAtt = "width",
			hAtt = "height", rxAtt = "rx", ryAtt = "ry";

	static {

		// the object used to convert double values into strings
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		format = new DecimalFormat("######.#", symbols);

		svgElementLabels.put("g", ResourcesManager.bundle
				.getString("svgElementName_g"));
		svgElementLabels.put("ellipse", ResourcesManager.bundle
				.getString("svgElementName_ellipse"));
		svgElementLabels.put("image", ResourcesManager.bundle
				.getString("svgElementName_image"));
		svgElementLabels.put("path", ResourcesManager.bundle
				.getString("svgElementName_path"));
		svgElementLabels.put("rect", ResourcesManager.bundle
				.getString("svgElementName_rect"));
		svgElementLabels.put("text", ResourcesManager.bundle
				.getString("svgElementName_text"));

		unknownShapeLabel = ResourcesManager.bundle
				.getString("svgElementName_unknown");

		svgShapeElementNames.add("g");
		svgShapeElementNames.add("ellipse");
		svgShapeElementNames.add("image");
		svgShapeElementNames.add("path");
		svgShapeElementNames.add("rect");
		svgShapeElementNames.add("text");
		svgShapeElementNames.add("line");
		svgShapeElementNames.add("circle");
		// added by wangql,ʹ��ͼԪsymbol��ǰ���Ǳ�����ʶ��use
		svgShapeElementNames.add("use");
		// added by wangql,����������
		svgShapeElementNames.add("a");
		svgShapeElementNames.add("marker");
	}

	/**
	 * computes and returns the value of the given attribute, in pixels,
	 * provided that it's a numeric value
	 * 
	 * @param element
	 *            an element
	 * @param attributeName
	 *            the name of an attribute
	 * @return the value of the given attribute, provided that it's a numeric
	 *         value
	 */
	public static double getAttributeValue(Element element, String attributeName) {

		double value = 0;

		try {
			value = Double.parseDouble(element.getAttributeNS(null,
					attributeName));
		} catch (Exception ex) {
			value = Double.NaN;
		}

		return value;
	}

	/**
	 * computes and returns the value of the given attribute, in pixels,
	 * provided that it's a numeric value
	 * 
	 * @param element
	 *            an element
	 * @param attributeName
	 *            the name of an attribute
	 * @param value
	 *            the new value for the attribute
	 */
	public static void setAttributeValue(Element element, String attributeName,
			double value) {

		element.setAttributeNS(null, attributeName, FormatStore.format(value));
	}

	/**
	 * checks if the xlink namespace is defined in the given document
	 * 
	 * @param doc
	 *            a document
	 */
	public static void checkXLinkNameSpace(Document doc) {

		if (doc != null
				&& !doc.getDocumentElement().hasAttributeNS(xmlnsNS,
						xmlnsXLinkAttributeName)) {

			doc.getDocumentElement().setAttributeNS(xmlnsNS,
					xmlnsXLinkAttributeName, xmlnsXLinkNS);

		}
	}

	/**
	 * checks if the given document contains the given name space, if not, the
	 * namespace is added
	 * 
	 * @param doc
	 *            a svg document
	 * @param prefix
	 *            the name space prefix
	 * @param nameSpace
	 *            a name space
	 */
	public static void checkXmlns(Document doc, String prefix, String nameSpace) {

		if (doc != null && prefix != null && nameSpace != null) {

			Element svgRoot = doc.getDocumentElement();

			if (!svgRoot.hasAttributeNS("http://www.w3.org/2000/xmlns/",
					"xmlns:" + prefix)) {

				svgRoot.setAttributeNS("http://www.w3.org/2000/xmlns/",
						"xmlns:" + prefix, nameSpace);
			}

			if (!svgRoot.hasAttributeNS("http://www.nci.com.cn/", "xmlns:nci")) {

				svgRoot.setAttributeNS("http://www.nci.com.cn/", "xmlns:nci",
						nameSpace);
			}

			if (!svgRoot.hasAttributeNS("http://www.cim.com/", "xmlns:PSR")) {

				svgRoot.setAttributeNS("http://www.cim.cn/", "xmlns:PSR",
						nameSpace);
			}
		}
	}

	/**
	 * @return creates and returns a combo box enabling to choose the units
	 */
	public JComboBox getUnitsComboBoxChooser() {

		// creating the items
		String[] items = new String[] { "px", "pt", "pc", "mm", "cm", "in" };

		JComboBox combo = new JComboBox(items);
		combo.setSelectedIndex(0);

		return combo;
	}

	/**
	 * returns the previous element sibling of the given element
	 * 
	 * @param element
	 *            an element
	 * @return the previous element sibling of the given element
	 */
	public static Element getPreviousElementSibling(Element element) {

		Element previousSibling = null;

		if (element != null) {

			Node cur = null;

			for (cur = element.getPreviousSibling(); cur != null; cur = cur
					.getPreviousSibling()) {

				if (cur instanceof Element) {

					previousSibling = (Element) cur;
					break;
				}
			}
		}

		return previousSibling;
	}

	/**
	 * returns the next element sibling of the given element
	 * 
	 * @param element
	 *            an element
	 * @return the next element sibling of the given element
	 */
	public static Element getNextElementSibling(Element element) {

		Element nextSibling = null;

		if (element != null) {

			Node cur = null;

			for (cur = element.getNextSibling(); cur != null; cur = cur
					.getNextSibling()) {

				if (cur instanceof Element) {

					nextSibling = (Element) cur;
					break;
				}
			}
		}

		return nextSibling;
	}

	/**
	 * computes the number corresponding to this string in pixel
	 * 
	 * @param str
	 * @return the number corresponding to this string in pixel
	 */
	public static double getPixelledNumber(String str) {

		double i = 0;

		if (str != null && !str.equals("")) {

			str = str.trim();

			if (!Character.isDigit(str.charAt(str.length() - 1))) {

				String unit = str.substring(str.length() - 2, str.length());
				String nb = str.substring(0, str.length() - 2);

				try {
					i = Double.parseDouble(nb);
				} catch (Exception ex) {
				}

				if (unit.equals("pt")) {

					i = i * 1.25;

				} else if (unit.equals("pc")) {

					i = i * 15;

				} else if (unit.equals("mm")) {

					i = i * 3.543307;

				} else if (unit.equals("cm")) {

					i = i * 35.43307;

				} else if (unit.equals("in")) {

					i = i * 90;
				}

			} else {

				try {
					i = Double.parseDouble(str);
				} catch (Exception ex) {
				}
			}
		}

		return i;
	}

	/**
	 * converts the given pixelled value into the given units value
	 * 
	 * @param value
	 *            the pixelled value
	 * @param unit
	 *            the new unit
	 * @return the value in the given units
	 */
	public static double convertFromPixelToUnit(double value, String unit) {

		double i = value;

		if (unit != null && !unit.equals("")) {

			unit = unit.trim();

			if (unit.equals("pt")) {

				i = value / 1.25;

			} else if (unit.equals("pc")) {

				i = value / 15;

			} else if (unit.equals("mm")) {

				i = value / 3.543307;

			} else if (unit.equals("cm")) {

				i = value / 35.43307;

			} else if (unit.equals("in")) {

				i = value / 90;
			}
		}

		return i;
	}

	/**
	 * computes a rectangle given the coordinates of two points
	 * 
	 * @param point1
	 *            the first point
	 * @param point2
	 *            the second point
	 * @return the correct rectangle
	 */
	public static Rectangle2D getComputedRectangle(Point2D point1,
			Point2D point2) {

		if (point1 != null && point2 != null) {

			double width = point2.getX() - point1.getX(), height = point2
					.getY()
					- point1.getY(), x = point1.getX(), y = point1.getY();

			if (point1.getX() > point2.getX() && point1.getY() > point2.getY()) {

				x = point2.getX();
				y = point2.getY();
				width = point1.getX() - point2.getX();
				height = point1.getY() - point2.getY();

			} else if (point1.getX() > point2.getX()
					&& point1.getY() < point2.getY()) {

				width = point1.getX() - point2.getX();
				height = point2.getY() - point1.getY();
				x = point2.getX();
				y = point1.getY();

			} else if (point1.getX() < point2.getX()
					&& point1.getY() > point2.getY()) {

				width = point2.getX() - point1.getX();
				height = point1.getY() - point2.getY();
				x = point1.getX();
				y = point2.getY();
			}
			// this "if" is added by wangql,����ͼԪ����ʱ����ȳ����޶�
			// if (Editor.getEditor().getSelectionManager().getDrawingShape()
			// instanceof GraphUnitImageShape) {
			// return new Rectangle2D.Double(x, y, 48, 48);
			// }

			return new Rectangle2D.Double(x, y, width, height);
		}

		return new Rectangle2D.Double(0, 0, 0, 0);
	}

	/**
	 * computes a square given the coordinates of two points
	 * 
	 * @param point1
	 *            the first point
	 * @param point2
	 *            the second point
	 * @return the correct square
	 */
	public static Rectangle2D getComputedSquare(Point2D point1, Point2D point2) {

		if (point1 != null && point2 != null) {

			double width = point2.getX() - point1.getX(), height = point2
					.getY()
					- point1.getY(), x = point1.getX(), y = point1.getY();

			if (point1.getX() > point2.getX() && point1.getY() > point2.getY()) {

				x = point2.getX();
				y = point2.getY();
				width = point1.getX() - point2.getX();
				height = point1.getY() - point2.getY();

				if (width < height) {

					y = point2.getY() + (height - width);
					height = width;

				} else {

					x = point2.getX() + (width - height);
					width = height;
				}

			} else if (point1.getX() > point2.getX()
					&& point1.getY() <= point2.getY()) {

				width = point1.getX() - point2.getX();
				height = point2.getY() - point1.getY();
				x = point2.getX();
				y = point1.getY();

				if (width < height) {

					height = width;

				} else {

					x = point2.getX() + (width - height);
					width = height;
				}

			} else if (point1.getX() <= point2.getX()
					&& point1.getY() > point2.getY()) {

				width = point2.getX() - point1.getX();
				height = point1.getY() - point2.getY();
				x = point1.getX();
				y = point2.getY();

				if (width < height) {

					y = point2.getY() + (height - width);
					height = width;

				} else {

					width = height;
				}

			} else if (point1.getX() <= point2.getX()
					&& point1.getY() <= point2.getY()) {

				if (width < height) {

					height = width;

				} else {

					width = height;
				}
			}
			// this "if" is added by wangql������ͼԪ����ʱ����ȳ����޶�
			// if (Editor.getEditor().getSelectionManager().getDrawingShape()
			// instanceof GraphUnitImageShape) {
			// return new Rectangle2D.Double(x, y, 48, 48);
			// }

			return new Rectangle2D.Double(x, y, width, height);
		}

		return new Rectangle2D.Double(0, 0, 0, 0);
	}

	/**
	 * returns whether the given element is a shape node or not
	 * 
	 * @param element
	 * @return whether the given element is a shape node or not
	 */
	public static boolean isElementAShape(Element element) {

		if (element != null) {
			return svgShapeElementNames.contains(element.getNodeName());
		}

		return false;
	}

	/**
	 * �Ƿ���һ��ʵ�ʵ�������״��������"g","a". added by wangql
	 * 
	 * @param element
	 * @return
	 */
	public static boolean isElementAnActualShape(Element element) {
		if (element != null) {
			if (!element.getNodeName().equalsIgnoreCase("g")
					&& !element.getNodeName().equalsIgnoreCase("a"))
				return svgShapeElementNames.contains(element.getNodeName());
		}

		return false;
	}

	/**
	 * �жϸýڵ��Ƿ�ɼ�
	 * 
	 * @param element
	 * @return
	 */
	public static boolean isElementVisible(Element element) {
		String style = element.getAttribute("style");
		int visiIndex = style.indexOf("visibility");
		if (visiIndex >= 0) {
			String visiSubString = style.substring(style.indexOf("visibility"));
			if (visiSubString.indexOf("hidden") >= 0) {
				return false;
			}
		} else {
			return true;
		}
		return true;
	}

	/**
	 * returns the label corresponding to the given element
	 * 
	 * @param element
	 *            an element
	 * @return the label corresponding to the given element
	 */
	public static String getElementLabel(Element element) {

		if (element != null
				&& svgElementLabels.containsKey(element.getTagName())) {

			return svgElementLabels.get(element.getTagName());
		}

		return unknownShapeLabel;
	}

	/**
	 * force a refresh of the current selection
	 */
	public static void forceReselection(SVGHandle handle) {

		if (handle != null) {

			handle.getSelection().refreshSelection(true);
		}
	}

	/**
	 * converts a string to a double that is a percentage
	 * 
	 * @param str
	 *            a string
	 * @param isPercentage
	 *            the boolean telling if the string describes a percentage value
	 * @return the corresponding value of the given string
	 */
	public double getDoubleValue(String str, boolean isPercentage) {

		if (str == null) {

			str = "";
		}

		str = str.replaceAll("\\s+", "");

		double val = 0;
		boolean hasPercentSign = (str.indexOf("%") != -1);

		try {
			if (isPercentage) {

				if (hasPercentSign) {

					str = str.replaceAll("%", "");
					val = Double.parseDouble(str);

				} else {

					val = Double.parseDouble(str);
					val = val * 100;
				}

			} else {

				val = Double.parseDouble(str);
			}
		} catch (Exception ex) {
		}

		return val;
	}

	/**
	 * picks the color at the given point on the screen
	 * 
	 * @param point
	 *            a point in the screen coordinates
	 * @return the color corresponding to the given point
	 */
	public Color pickColor(Point point) {

		Color color = new Color(255, 255, 255);

		if (point != null) {

			try {
				// getting the color at this point
				Robot robot = new Robot();
				color = robot.getPixelColor(point.x, point.y);
			} catch (Exception ex) {
			}
		}

		return color;
	}

	/**
	 * returns an icon displaying the given color
	 * 
	 * @param color
	 *            a color
	 * @param size
	 *            the size of the image to be returned
	 * @return an icon displaying the given color
	 */
	public static Image getImageFromColor(Color color, Dimension size) {

		if (color != null && size != null && size.width > 0 && size.height > 0) {

			BufferedImage image = new BufferedImage(size.width, size.height,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g = (Graphics2D) image.getGraphics();

			g.setColor(color);
			g.fillRect(0, 0, size.width, size.height);

			g.setColor(MetalLookAndFeel.getSeparatorForeground());
			g.drawRect(0, 0, size.width - 1, size.height - 1);

			return image;
		}

		return new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
	}

	/**
	 * creates a cursor containing the given color and returns it
	 * 
	 * @param color
	 *            a color
	 * @return a cursor containing the given color
	 */
	public static Cursor createCursorImageFromColor(Color color) {

		Cursor cursor = null;

		if (color != null) {

			// tells which size is better for the cursor images or if the cutom
			// cursors option can't be used
			Dimension bestSize = java.awt.Toolkit.getDefaultToolkit()
					.getBestCursorSize(22, 22);

			try {
				cursor = java.awt.Toolkit.getDefaultToolkit()
						.createCustomCursor(getImageFromColor(color, bestSize),
								new Point(0, 0), "color");
			} catch (Exception ex) {
			}
		}

		return cursor;
	}

	/**
	 * creates a cursor given an image
	 * 
	 * @param image
	 *            an image
	 * @return a cursor containing the image
	 */
	public static Cursor createCursorFromImage(Image image) {

		Cursor cursor = null;

		if (image != null) {

			try {
				cursor = java.awt.Toolkit.getDefaultToolkit()
						.createCustomCursor(image, new Point(0, 0), "resource");
			} catch (Exception ex) {
			}
		}

		return cursor;
	}

	/**
	 * returns the value of a style property
	 * 
	 * @param element
	 *            an element
	 * @param name
	 *            the name of a style property
	 * @return the value of a style property
	 */
	public static String getStyleProperty(Element element, String name) {

		String value = "";

		if (element != null && name != null && !name.equals("")) {

			// gets the value of the style attribute
			String styleValue = element.getAttribute("style");
			styleValue = styleValue.replaceAll("\\s*[;]\\s*", ";");
			styleValue = styleValue.replaceAll("\\s*[:]\\s*", ":");

			int rg = styleValue.indexOf(";".concat(name.concat(":")));

			if (rg != -1) {

				rg++;
			}

			if (rg == -1) {

				rg = styleValue.indexOf(name.concat(":"));

				if (rg != 0) {

					rg = -1;
				}
			}

			// if the value of the style attribute contains the property
			if (!styleValue.equals("") && rg != -1) {

				// computes the value of the property
				value = styleValue.substring(rg + name.length() + 1, styleValue
						.length());
				rg = value.indexOf(";");
				value = value.substring(0, rg == -1 ? value.length() : rg);
			}
		}

		return value;
	}

	/**
	 * setting the value of the given style element for the given node
	 * 
	 * @param element
	 *            an element
	 * @param name
	 *            the name of a style element
	 * @param value
	 *            the value for this style element
	 */
	public static void setStyleProperty(Element element, String name,
			String value) {

		if (element != null && name != null && !name.equals("")) {

			if (value == null) {

				value = "";
			}

			// the separators
			String valuesSep = ";", nameToValueSep = ":";

			// the map associating the name of a property to its value
			HashMap<String, String> values = new HashMap<String, String>();

			// getting the value of the style attribute
			String styleValue = element.getAttribute("style");
			styleValue = styleValue.replaceAll("\\s*[;]\\s*", ";");
			styleValue = styleValue.replaceAll("\\s*[:]\\s*", ":");

			// filling the map associating a property to its value
			String[] splitValues = styleValue.split(valuesSep);
			int pos = -1;
			String sname = "", svalue = "";

			for (int i = 0; i < splitValues.length; i++) {

				if (splitValues[i] != null && !splitValues[i].equals("")) {

					pos = splitValues[i].indexOf(nameToValueSep);

					sname = splitValues[i].substring(0, pos);
					svalue = splitValues[i].substring(pos
							+ nameToValueSep.length(), splitValues[i].length());

					if (!sname.equals("") && !svalue.equals("")) {

						values.put(sname, svalue);
					}
				}
			}

			// adding the new value
			if (value.equals("")) {

				values.remove(name);

			} else {

				values.put(name, value);
			}

			// computing the new style value
			styleValue = "";

			for (String newName : values.keySet()) {

				styleValue += newName + nameToValueSep + values.get(newName)
						+ valuesSep;
			}

			// sets the value of the style attribute
			if (!styleValue.equals("")) {

				element.setAttribute("style", styleValue);

			} else {

				element.removeAttribute("style");
			}
		}
	}

	/**
	 * recursively removes all the attributes that are not necessary for the
	 * given node
	 * 
	 * @param element
	 *            a node
	 */
	public void removeUselessAttributes(Element element) {

		if (element != null) {

			String nspXLink = "http://www.w3.org/1999/xlink", nspNS = "http://www.w3.org/2000/xmlns/";
			Node node = null;
			Element el = null;

			for (NodeIterator it = new NodeIterator(element); it.hasNext();) {

				node = it.next();

				if (node != null && node instanceof Element
						&& !node.getNodeName().equals("svg")) {

					el = (Element) node;

					el.removeAttributeNS(nspXLink, "xlink:show");
					el.removeAttributeNS(nspXLink, "xlink:type");
					el.removeAttributeNS(nspXLink, "xlink:actuate");
					el.removeAttributeNS(nspNS, "xlink");
				}
			}
		}
	}

	public static Set<String> getSvgShapeElementNames() {
		return svgShapeElementNames;
	}

	/**
	 * �Ƿ���nci�����ͼԪ\ģ����Ͻڵ�
	 * 
	 * @param ele
	 *            ���жϵĽڵ�
	 * @return
	 */
	public static boolean isNCISymbolGroup(Element ele) {
		if (ele.getAttribute(Constants.SYMBOL_TYPE).equals(
				NCIEquipSymbolBean.SYMBOL_TYPE_GRAPHUNIT)
				|| ele.getAttribute(Constants.SYMBOL_TYPE).equals(
						NCIEquipSymbolBean.SYMBOL_TYPE_TEMPLATE)) {
			return true;
		}
		return false;
	}

	/**
	 * ��ָ����ͼ�ζ����ڵ�ָ����������ͼԪ
	 * 
	 * @param handle
	 *            ָ����ͼ�ζ���
	 * @param rectangle
	 *            ָ������
	 * @param parentElement
	 *            ��ͼ�νڵ�
	 * @param symbolName
	 *            ͼԪ����
	 * @param symbolStatus
	 *            ͼ��״̬
	 * @return ͼԪ�����ţ���ʧ���򷵻�null
	 */
	public static Element insertSymbolElement(SVGHandle handle,
			Rectangle2D rectangle, Element parentElement, Element nextElement,
			String symbolName, String symbolStatus) {
		// У���������ݵ���Ч��
		if (handle == null || rectangle == null) {
			return null;
		}

		Document doc = handle.getCanvas().getDocument();
		// ����ͼԪ�ڵ�
		Element element = ((SVGOMDocument) doc).createElementNS(doc
				.getDocumentElement().getNamespaceURI(), "use");

		// ����ͼԪ��ţ�������ͼԪ�ڵ�
		String id = UUID.randomUUID().toString();
		element.setAttribute("id", id);

		// ����ͼԪ������
		element.setAttribute("filterUnits", "userSpaceOnUse");
		EditorToolkit.setAttributeValue(element, xAtt, rectangle.getMinX());
		EditorToolkit.setAttributeValue(element, yAtt, rectangle.getMinY());
		EditorToolkit.setAttributeValue(element, wAtt, rectangle.getWidth());
		EditorToolkit.setAttributeValue(element, hAtt, rectangle.getHeight());
		element.setAttributeNS(EditorToolkit.xmlnsXLinkNS, "xlink:href", "#"
				+ symbolName + Constants.SYMBOL_STATUS_SEP + symbolStatus);
		element.setAttributeNS(EditorToolkit.xmlnsXLinkNS, "xlink:show",
				"embed");
		element.setAttributeNS(EditorToolkit.xmlnsXLinkNS, "xlink:type",
				"simple");
		element.setAttributeNS(null, preserveAtt, "none meet");

		// �ڸ��ڵ������ӵ�ǰͼԪ�ڵ�
		// �縸�ڵ㲻���������ĵ����ڵ�����
		if (parentElement != null) {
			parentElement.insertBefore(element, nextElement);
		} else
			doc.getDocumentElement().appendChild(element);
		// �������
		fr.itris.glips.library.Toolkit.clearBatikImageCache();
		handle.getCanvas().clearCache();

		// ����źͽڵ��ύ����
		return element;
	}

	/**
	 * ��ָ����ͼ�ζ����и���ָ������ʼ�����ֹ�㣬����ֱ�߶���ڵ� ���Ը������ӹ�ϵ
	 * 
	 * @param handle
	 *            ͼ�ζ���
	 * @param parentElement
	 *            ���ڵ�
	 * 
	 * @param nextElement
	 *            ��һ���ڵ�
	 * @param beginPoint
	 *            ��ʼ��
	 * @param endPoint
	 *            ��ֹ��
	 * @param beforeID
	 *            ��ʼ�����ӵ�ͼԪ���
	 * @param nextID
	 *            ��ֹ�����ӵ�ͼԪ���
	 * @return �µ�path Element����
	 */
	public static Element insertPathElement(SVGHandle handle,
			Node parentElement, Node nextElement, Point2D beginPoint,
			Point2D endPoint, String beforeID, String nextID,
			String beforeTerminal, String nextTerminal) {
		if (beginPoint == null || endPoint == null)
			return null;
		// ������ʼ�����ֹ�㹹����·��
		StringBuffer buffer = new StringBuffer();
		buffer.append("M").append(beginPoint.getX()).append(" ").append(
				beginPoint.getY()).append(" ").append("L").append(
				endPoint.getX()).append(" ").append(endPoint.getY());

		return insertPathElement(handle, parentElement, nextElement, buffer
				.toString(), beforeID, nextID, beforeTerminal, nextTerminal);
	}

	public static Element insertPathElement(SVGHandle handle,
			Node parentElement, Element createdPathElement, Node nextElement,
			Point2D beginPoint, Point2D endPoint, String beforeID,
			String nextID, String beforeTerminal, String nextTerminal) {
		if (beginPoint == null || endPoint == null)
			return null;
		// ������ʼ�����ֹ�㹹����·��
		StringBuffer buffer = new StringBuffer();
		buffer.append("M").append(beginPoint.getX()).append(" ").append(
				beginPoint.getY()).append(" ").append("L").append(
				endPoint.getX()).append(" ").append(endPoint.getY());

		return insertPathElement(handle, parentElement, createdPathElement,
				nextElement, buffer.toString(), beforeID, nextID,
				beforeTerminal, nextTerminal);
	}

	// public static Element insertPathElement(SVGHandle handle,
	// Node parentElement, Node nextElement, Point2D beginPoint,
	// Point2D endPoint, String beforeID, String nextID,String pathColor,
	// String beforeTerminal, String nextTerminal) {
	//		
	// }

	public static Element insertPathElement(SVGHandle handle,
			Node parentElement, Element createdPathElement, Node nextElement,
			String path, String beforeID, String nextID, String beforeTerminal,
			String nextTerminal) {
		if (handle == null || path == null || path.length() == 0)
			return null;
		Document doc = handle.getCanvas().getDocument();

		// �����߽ڵ�
		if (createdPathElement == null)
			createdPathElement = doc.createElementNS(doc.getDocumentElement()
					.getNamespaceURI(), "path");

		// ����������
		createdPathElement.setAttribute("d", path);
		createdPathElement.setAttributeNS(null, "style", "fill:none;stroke:"
				+ Constants.NCI_DEFAULT_STROKE_COLOR + ";");
		// �����߱�ţ�������ڵ�
		String id = UUID.randomUUID().toString();
		createdPathElement.setAttribute("id", id);
		// �ڸ��ڵ������ӵ�ǰͼԪ�ڵ�
		// �縸�ڵ㲻���������ĵ����ڵ�����
		if (parentElement != null) {
			parentElement.insertBefore(createdPathElement, nextElement);
		} else
			doc.getDocumentElement().appendChild(createdPathElement);
		LineData lineData = null;
		// �������ʼ�������豸�����������ӹ�ϵ���߽ڵ�ͻ���
		if (beforeID != null && beforeID.length() > 0) {
			lineData = handle.getCanvas().getLpManager()
					.createLineData(id, beforeID, beforeTerminal,
							LinkPointManager.BEGIN_LINE_POINT);
			handle.getCanvas().getLpManager().addLinkPoint(
					lineData.getSymbolID(), lineData);
			createdPathElement.setAttribute(LinkPointManager.BEGIN_LINE_POINT,
					lineData.getSymbolID());
			createdPathElement.setAttribute(
					LinkPointManager.BEGIN_LINE_TERMINAL, lineData
							.getSymbolTerminalName());
		}
		// �������ֹ�������豸�����������ӹ�ϵ���߽ڵ�ͻ���
		if (nextID != null && nextID.length() > 0) {
			lineData = handle.getCanvas().getLpManager().createLineData(id,
					nextID, nextTerminal, LinkPointManager.END_LINE_POINT);
			handle.getCanvas().getLpManager().addLinkPoint(
					lineData.getSymbolID(), lineData);
			createdPathElement.setAttribute(LinkPointManager.END_LINE_POINT,
					lineData.getSymbolID());
			createdPathElement.setAttribute(LinkPointManager.END_LINE_TERMINAL,
					lineData.getSymbolTerminalName());
		}
		return createdPathElement;
	}

	/**
	 * ��ָ����ͼ�ζ����и���ָ������ʼ�����ֹ�㣬�����߶���ڵ� ���Ը������ӹ�ϵ
	 * 
	 * @param handle
	 *            ͼ�ζ���
	 * @param parentElement
	 *            ���ڵ�
	 * @param path
	 *            ��·��
	 * @param beforeID
	 *            ��ʼ�����ӵ�ͼԪ���
	 * @param nextID
	 *            ��ֹ�����ӵ�ͼԪ���
	 * @return �߽ڵ��ţ���ʧ���򷵻�null
	 */
	public static Element insertPathElement(SVGHandle handle,
			Node parentElement, Node nextElement, String path, String beforeID,
			String nextID, String beforeTerminal, String nextTerminal) {
		return insertPathElement(handle, parentElement, null, nextElement,
				path, beforeID, nextID, beforeTerminal, nextTerminal);

	}

	/**
	 * ��ָ��symbol���Ƶ�symbol�ڵ�д��ָ��doc��
	 * 
	 * @param handle
	 *            ͼ�ζ���
	 * @param symbolName
	 *            ͼԪ����
	 */
	public static void insertCurSymbolByName(SVGHandle handle, String symbolName) {
		if (handle == null || symbolName == null || symbolName.length() == 0) {
			return;
		}
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add(symbolName);
		insertCurSymbolByName(handle, arrayList);
	}

	/**
	 * ������ָ��ͼԪ���Ƶ�list��symbol�ڵ�д��ָ��doc��
	 * 
	 * @param handle
	 *            :ͼ�ζ���
	 * @param arrayList
	 *            :��д���ͼԪ����
	 */
	public static void insertCurSymbolByName(SVGHandle handle,
			ArrayList<String> arrayList) {
		if (handle == null || arrayList == null || arrayList.size() == 0) {
			return;
		}
		// ��ȡ��ǰͼԪӳ���
		Map<SymbolTypeBean, Map<String, NCIEquipSymbolBean>> symbolMap = handle
				.getEditor().getSymbolManager().getAllSymbols();
		Iterator<Map<String, NCIEquipSymbolBean>> it1 = symbolMap.values()
				.iterator();
		// ��������
		while (it1.hasNext()) {
			Iterator<NCIEquipSymbolBean> beanIterator = it1.next().values()
					.iterator();
			// ���������һͼԪ��ģ�����
			while (beanIterator.hasNext()) {
				NCIEquipSymbolBean bean = beanIterator.next();
				// ��������ȵĻ��������doc��
				if (arrayList.contains(bean.getName())) {
					// ��Ҫ�����ͼԪ�ڵ�
					Document symbolDoc = null;
					try {
						symbolDoc = Utilities.getSVGDocumentByContent(bean);
					} catch (Exception e) {
						e.printStackTrace();
						symbolDoc = null;
					}

					// ͼԪdoc��Ϊ�գ������doc��
					if (symbolDoc != null) {
						addGraphUnitElements(handle, symbolDoc, bean.getName());

					}

				}
			}
		}
	}

	/**
	 * ����ָ����ͼԪ���ƻ�ȡͼԪ���
	 * 
	 * @param handle
	 *            ��ǰ��ͼ�ζ���
	 * @param name
	 *            ͼԪ����
	 * @return ͼԪ��ţ�ʧ�ܷ���null
	 */
	public static String getSymbolIDByName(SVGHandle handle, String name) {
		if (handle == null || name == null)
			return null;
		// ��ȡ����ͼԪӳ���
		Map<SymbolTypeBean, Map<String, NCIEquipSymbolBean>> symbolMap = handle
				.getEditor().getSymbolManager().getAllSymbols();
		Iterator<Map<String, NCIEquipSymbolBean>> it1 = symbolMap.values()
				.iterator();
		// ��һ����ڷ���
		while (it1.hasNext()) {
			Iterator<NCIEquipSymbolBean> beanIterator = it1.next().values()
					.iterator();
			// ��һͼԪ��ģ�����
			while (beanIterator.hasNext()) {
				// ����������ָ��������ȵ�ͼԪ���򷵻ظ�ͼԪ�ı��
				NCIEquipSymbolBean bean = beanIterator.next();
				if (name.equals(bean.getName())) {
					return bean.getId();
				}
			}
		}
		return null;
	}

	/**
	 * ��ָ����ͼ�ζ�����ĳ���ڵ��������Ĺ淶��ģ�͹������ݣ�����metadata���ݽڵ�����
	 * 
	 * @param handle
	 *            ָ����ͼ�ζ���
	 * @param element
	 *            ָ���Ľڵ�
	 * @param list
	 *            �淶��ģ�͹�������
	 */
	public static void appendMetadataElement(SVGHandle handle, Element element,
			ArrayList<ModelRelaIndunormBean> list) {
		if (list == null || list.size() == 0 || element == null
				|| handle == null)
			return;
		// ����metadata�ڵ�
		Document doc = handle.getCanvas().getDocument();
		Element metadataElement = doc.createElement("metadata");
		element.appendChild(metadataElement);
		HashMap<String, Element> elementMap = new HashMap<String, Element>();
		for (ModelRelaIndunormBean bean : list) {
			// �����ָ��ͼ�ζ���Ĺ淶Ҫ��������
			// ������������
			if (handle.getCanvas().containIndunormType(
					bean.getIndunormShortName())
					&& bean.getMetadataShortName() != null) {
				// ����Ҫ��
				// ��ȡ������
				String nodeName = bean.getIndunormShortName() + ":"
						+ bean.getMetadataShortName();
				Element el = elementMap.get(nodeName);
				// ��������ڵ㲻���ڣ�������������ڵ�
				if (el == null) {
					el = doc.createElement(nodeName);
					metadataElement.appendChild(el);
					elementMap.put(nodeName, el);
				}
				// ��������
				el.setAttribute(bean.getFieldShortName(), "");
			}
		}
	}

	/**
	 * ��ָ����ͼ�ζ����и���ָ����ģ�ͱ�ŶԽڵ����metadata���ݽڵ�����
	 * 
	 * @param handle
	 *            ͼ�ζ���
	 * @param modelID
	 *            ģ�ͱ��
	 * @param element
	 *            �ڵ�
	 */
	public static void appendMetadataElement(SVGHandle handle, String modelID,
			Element element) {
		// ���ȸ���ģ�ͱ�Ż�ȡҵ��淶��ģ�͵Ĺ�������
		appendMetadataElement(handle, element, handle.getEditor()
				.getSvgSession().getModelRelaInfoByModelID(modelID));
	}

	/**
	 * ��ָ����ͼ�ζ����н�ָ����ͼԪ�ĵ������������ͼ�ζ����еĶ�����ڵ�
	 * 
	 * @param handle
	 *            ͼ�ζ���
	 * @param symboldoc
	 *            ͼԪ�ĵ�
	 * @param name
	 *            ͼԪ����
	 * @return ������䣬ʧ���򷵻�null
	 */
	public static String addGraphUnitElements(SVGHandle handle,
			Document symboldoc, String name) {
		if (handle == null || symboldoc == null || name == null
				|| name.length() == 0)
			return null;
		Document doc = handle.getCanvas().getDocument();
		String href = null;
		// ��ȡĿ���ĵ��ж�����ڵ㣬û��������
		Element defs = null;
		if (doc.getElementsByTagName("defs").getLength() == 0) {
			defs = doc.createElementNS(doc.getDocumentElement()
					.getNamespaceURI(), "defs");
			doc.getDocumentElement().appendChild(defs);
			handle.getCanvas().appendElement("defs", defs);

		} else
			defs = (Element) doc.getElementsByTagName("defs").item(0);

		// ��ȡͼԪ�ĵ�������״̬�ڵ�
		NodeList gList = null;
		try {
			gList = Utilities.findNodes("//*[@" + Constants.SYMBOL_TYPE + "='"
					+ NCIEquipSymbolBean.SYMBOL_TYPE_GRAPHUNIT + "']",
					symboldoc.getDocumentElement());
		} catch (XPathExpressionException e) {
			gList = null;
		}
		int length = gList.getLength();
		// �½�һ������������ͨͼԪ����״̬��g�ڵ�
		Element gElement = ((SVGOMDocument) doc).getElementById(name);
		if (gElement == null) {
			gElement = ((SVGOMDocument) doc).createElementNS(doc
					.getDocumentElement().getNamespaceURI(), "g");
			gElement.setAttribute("id", name);
			defs.appendChild(gElement);
			handle.getCanvas().appendElement(name, gElement);
		}
		String status;
		// ������ƽڵ�
		for (int i = 0; i < length; i++) {
			if (gList.item(i) instanceof Element) {
				Element element = (Element) gList.item(i);

				// ��ȡ״̬
				status = element.getAttribute(Constants.SYMBOL_STATUS);
				if (status == null)
					continue;
				String symbolID = name + Constants.SYMBOL_STATUS_SEP + status;
				// if (modifiedTime != null) {
				// symbolID = symbolID + Constants.SYMBOL_DATE_SEP
				// + modifiedTime;
				// }
				// У���Ƿ����ظ�symbolid

				if (((SVGOMDocument) doc).getElementById(symbolID) == null) {
					// ����ͼԪ����ڵ㣬��������
					Element symbolElement = doc.createElementNS(doc
							.getDocumentElement().getNamespaceURI(), "symbol");

					symbolElement.setAttribute("viewBox", symboldoc
							.getDocumentElement().getAttribute("viewBox"));
					symbolElement.setAttribute("id", symbolID);
					symbolElement.setAttribute("preserveAspectRatio",
							"xMidYMid meet");
					gElement.appendChild(symbolElement);
					handle.getCanvas().appendElement(symbolID, symbolElement);
					NodeList list = null;
					Element dElement = (Element) doc.importNode(element, true);
					list = dElement.getChildNodes();
					for (int j = 0; j < list.getLength(); j++) {
						if (list.item(j) instanceof Element
								&& !list.item(j).getNodeName().equals(
										"metadata")) {
							symbolElement.appendChild(list.item(j));
						}
					}
					// �������˵�ڵ�
					appendTerminalElements(doc, symboldoc, symbolElement);
				}
				// ������ʱ����ʾ��״̬Ϊ��״̬������href����
				String visibility = EditorToolkit.getStyleProperty(element,
						"visibility");
				if (visibility == null || visibility.length() == 0) {

					href = symbolID;

				}
			}
		}

		return href;
	}

	/**
	 * ��ָ����ͼ���ĵ��У�����ͼԪ�ĵ����壬��ͼԪ����ڵ����������˵㶨��
	 * 
	 * @param doc
	 *            ͼ���ĵ�
	 * @param symbolDoc
	 *            ͼԪ�ĵ�
	 * @param element
	 *            ͼԪ����ڵ�
	 */
	public static void appendTerminalElements(Document doc, Document symbolDoc,
			Element element) {
		if (doc == null || symbolDoc == null || element == null)
			return;
		String sWidth = symbolDoc.getDocumentElement().getAttribute("width");
		String sHeight = symbolDoc.getDocumentElement().getAttribute("height");
		double width = Double.parseDouble(sWidth);
		double height = Double.parseDouble(sHeight);
		Element metadata = null;
		// ��ͼԪ�ĵ���Ѱ�����˵㶨��
		try {
			metadata = (Element) Utilities.findNode("//*[@"
					+ Constants.NCI_TYPE + "='"
					+ TerminalModule.NCI_TYPE_TERMINAL + "']", symbolDoc
					.getDocumentElement());
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		// ������Ϊ��
		if (metadata != null) {
			NodeList nodeList = metadata
					.getElementsByTagName(TerminalModule.DEFS_TERMINIAL_NCINAME);
			int len = nodeList.getLength();
			// ��һ������˵�ڵ�
			for (int i = 0; i < len; i++) {
				if (nodeList.item(i) instanceof Element) {
					Element tElement = (Element) nodeList.item(i);
					String text = tElement
							.getAttribute(TerminalModule.DEFS_TERMINIAL_NOTE);
					int x = 0, y = 0;
					if (text.equals(TerminalModule.CENTER_POINT)) {
						appendTerminalElement(doc, element, (int) (width / 2),
								(int) (height / 2),
								TerminalModule.CENTER_TERMINAL_NAME);
					} else if (text.equals(TerminalModule.FOURWAY_POINT)) {
						appendTerminalElement(doc, element, 0,
								(int) (height / 2),
								TerminalModule.WEST_TERMINAL_NAME);
						appendTerminalElement(doc, element, width,
								(int) (height / 2),
								TerminalModule.EAST_TERMINAL_NAME);
						appendTerminalElement(doc, element, (int) (width / 2),
								0, TerminalModule.NORTH_TERMINAL_NAME);
						appendTerminalElement(doc, element, (int) (width / 2),
								height, TerminalModule.SOUTH_TERMINAL_NAME);
					} else if (text.equals(TerminalModule.LEFTANDRIGHT_POINT)) {
						appendTerminalElement(doc, element, 0,
								(int) (height / 2),
								TerminalModule.WEST_TERMINAL_NAME);
						appendTerminalElement(doc, element, width,
								(int) (height / 2),
								TerminalModule.EAST_TERMINAL_NAME);
					} else if (text.equals(TerminalModule.UPANDDOWN_POINT)) {
						appendTerminalElement(doc, element, (int) (width / 2),
								0, TerminalModule.NORTH_TERMINAL_NAME);
						appendTerminalElement(doc, element, (int) (width / 2),
								height, TerminalModule.SOUTH_TERMINAL_NAME);
					} else if (text
							.equals(TerminalModule.INCLINE_FOURWAY_POINT)) {
						appendTerminalElement(doc, element, 0, 0,
								TerminalModule.WESTNORTH_TERMINAL_NAME);
						appendTerminalElement(doc, element, 0, height,
								TerminalModule.WESTSOUTH_TERMINAL_NAME);
						appendTerminalElement(doc, element, width, 0,
								TerminalModule.EASTNORTH_TERMINAL_NAME);
						appendTerminalElement(doc, element, width, height,
								TerminalModule.EASTSOUTH_TERMINAL_NAME);
					} else if (text.equals(TerminalModule.MANUAL_TERMINAL)) {
						String name = tElement
								.getAttribute(TerminalModule.DEFS_TERMINIAL_NAME);
						double tx = Double.parseDouble(tElement
								.getAttribute("x"));
						double ty = Double.parseDouble(tElement
								.getAttribute("y"));
						appendTerminalElement(doc, element, tx, ty, name);
					}

				}
			}

		}
	}

	/**
	 * ��ͼ���ĵ�ָ��ͼԪ�ڵ��и���ָ������������˵������������˵�ڵ�
	 * 
	 * @param doc
	 *            ͼ���ĵ�
	 * @param element
	 *            ͼԪ�ڵ�
	 * @param x
	 *            x����
	 * @param y
	 *            y����
	 * @param name
	 *            ���˵�����
	 */
	public static void appendTerminalElement(Document doc, Element element,
			double x, double y, String name) {
		if (doc == null || element == null || name == null
				|| name.length() == 0)
			return;
		// �������˵�ڵ�
		Element useElement = ((SVGOMDocument) doc).createElementNS(doc
				.getDocumentElement().getNamespaceURI(), "use");

		element.appendChild(useElement);

		// �������˵�����
		EditorToolkit.setAttributeValue(useElement, xAtt, x);
		EditorToolkit.setAttributeValue(useElement, yAtt, y);
		EditorToolkit.setAttributeValue(useElement, wAtt, 0);
		EditorToolkit.setAttributeValue(useElement, hAtt, 0);
		useElement.setAttributeNS(EditorToolkit.xmlnsXLinkNS, "xlink:href",
				"#terminal");
		useElement.setAttribute("name", name);
	}

	/**
	 * ������֪�������ߣ��ҳ���Ӧ�����˵����Ӷ���
	 * 
	 * @param connectPathElement
	 * @return
	 */
	public static Element[] getConnectedElements(Element connectPathElement) {
		String p0 = connectPathElement.getAttribute("p0");
		String p1 = connectPathElement.getAttribute("p1");
		if (p0.equals("") || p1.equals("")) {
			return null;
		}
		try {
			Element p0Ele = (Element) Utilities.findNode("//*[@id='" + p0
					+ "']", connectPathElement);
			Element p1Ele = (Element) Utilities.findNode("//*[@id='" + p1
					+ "']", connectPathElement);
			Element[] e = { p0Ele, p1Ele };
			return e;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ���ָ����handle�ͽڵ����x,y��ƫ��
	 * 
	 * @param handle
	 *            ָ����handle
	 * @param el
	 *            ָ���Ľڵ�
	 * @param x
	 *            xƫ����
	 * @param y
	 *            yƫ����
	 */
	public static void modifyBounds(SVGHandle handle, Element el, double x,
			double y) {
		if (el instanceof SVGOMEllipseElement) {
			// ��Բ
			double dx = Double.parseDouble(el.getAttribute("cx"));
			double dy = Double.parseDouble(el.getAttribute("cy"));
			EditorToolkit.setAttributeValue(el, "cx", dx + x);
			EditorToolkit.setAttributeValue(el, "cy", dy + y);
		} else if (el instanceof SVGOMTextElement) {
			// ����
			final AffineTransform initialTransform = handle
					.getSvgElementsManager().getTransform(el);

			// getting the new transform
			AffineTransform transform = AffineTransform.getTranslateInstance(x,
					y);
			AffineTransform newTransform = new AffineTransform(initialTransform);
			newTransform.preConcatenate(transform);
			handle.getSvgElementsManager().setTransform(el, newTransform);
		} else if (el instanceof SVGOMPathElement) {
			// ����
			AffineTransform actionTransform = AffineTransform
					.getTranslateInstance(x, y);
			Path initialPath = new Path(el.getAttribute("d"));
			Path transformPath = new Path(initialPath);

			// getting the element's transform
			AffineTransform initialTransform = handle.getSvgElementsManager()
					.getTransform(el);

			// concatenating the transforms
			AffineTransform transform = new AffineTransform(initialTransform);
			transform.preConcatenate(actionTransform);

			// transforming the shape
			if (initialPath.canBeAppliedTransform()) {

				transformPath.applyTransform(transform);
				transform = null;
			}

			// getting the path attribute value
			String dValue = transformPath.toString();
			el.setAttribute("d", dValue);
			AffineTransform ftransform = transform;
			handle.getSvgElementsManager().setTransform(el, ftransform);
		} else if (el instanceof SVGOMGElement) {
			// ���
			NodeList list = el.getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				if (list.item(i) instanceof Element) {
					modifyBounds(handle, (Element) list.item(i), x, y);
				}
			}
		} else if (el instanceof SVGOMElement) {
			// ����SVG�ڵ�
			double dx = Double.parseDouble(el.getAttribute("x"));
			double dy = Double.parseDouble(el.getAttribute("y"));
			EditorToolkit.setAttributeValue(el, "x", dx + x);
			EditorToolkit.setAttributeValue(el, "y", dy + y);
		}
	}

	public static final String MARKER_ID = "pull-line-arrow";

	/**
	 * �������߼�ͷ
	 * 
	 * @param ownerDoc
	 */
	public static void appendMarker(Document ownerDoc) {
		try {
			Node arrowNode = Utilities.findNode("//*[@id='" + MARKER_ID + "']",
					ownerDoc.getDocumentElement());
			// Node arrowNode = Utilities.findNode("/svg",
			// ownerDoc.getDocumentElement());
			if (arrowNode == null) {
				NodeList defses = ownerDoc.getDocumentElement()
						.getElementsByTagName("defs");
				Element defsEle = null;
				if (defses.getLength() == 0) {
					defsEle = ownerDoc.createElementNS(ownerDoc
							.getDocumentElement().getNamespaceURI(), "defs");
					ownerDoc.getDocumentElement().appendChild(defsEle);
				} else {
					defsEle = (Element) defses.item(0);
				}
				Element markerEle = ownerDoc.createElementNS(ownerDoc
						.getDocumentElement().getNamespaceURI(), "marker");
				Element pathEle = ownerDoc.createElementNS(ownerDoc
						.getDocumentElement().getNamespaceURI(), "path");
				markerEle.appendChild(pathEle);
				defsEle.appendChild(markerEle);
				pathEle.setAttribute("d", "M-5 0 L5 5 L-5 10 L-5 0");
				// pathEle.setAttribute("d",
				// "M0 0 L10 5 L0 10 L0 0");viewBox="0 0 10 10"
				markerEle.setAttribute("viewBox", "0 0 10 10");
				markerEle.setAttribute("id", MARKER_ID);
				markerEle.setAttribute("refX", "3");
				markerEle.setAttribute("refY", "5");
				markerEle.setAttribute("markerUnits", "strokeWidth");
				markerEle.setAttribute("markerWidth", "20");
				markerEle.setAttribute("markerHeight", "10");
				markerEle.setAttribute("style", "fill:black;stroke:#000000");
				markerEle.setAttribute("orient", "auto");
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

	}

	public static void appendMarker(Element pathEle) {
		pathEle.setAttribute("marker-end", "url(#" + EditorToolkit.MARKER_ID
				+ ")");
	}

	/**
	 * �ж��Ƿ��ڶ�ѡ�������Ҳʹ�õ�ѡ��ʽ����Ⱦ
	 * 
	 * @param e
	 * @return
	 */
	public static boolean isElementsSingleSelect(Element e) {
		boolean flag = false;
		flag = isVHPath(e);
		return flag;
	}

	/**
	 * �ж��Ƿ���VH������
	 * 
	 * @param e
	 * @return
	 */
	public static boolean isVHPath(Element e) {
		if (e != null && e.getNodeName().equals("path")) {
			Element gElement = (Element) e.getParentNode();
			if (gElement != null
					&& gElement.getNodeName().equals("g")
					&& gElement.getAttribute("model").equals(
							VHPathShape.VH_MODEL)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * ��ȡͼԪ�Ŀ�߱�
	 * 
	 * @param doc
	 * @param href
	 * @return
	 */
	public static double getUseShapeRate(Document doc, String href) {

		Element hrefEle = doc.getElementById(href);
		String viewBox = hrefEle.getAttribute("viewBox");
		String[] viewBoxs = viewBox.split(" ");
		List<String> viewBoxValueList = new ArrayList<String>();
		for (String s : viewBoxs) {
			if (!s.trim().equals("")) {
				viewBoxValueList.add(s);
			}
		}
		double rate = (Double.valueOf(viewBoxValueList.get(2)) - Double
				.valueOf(viewBoxValueList.get(0)))
				/ (Double.valueOf(viewBoxValueList.get(3)) - Double
						.valueOf(viewBoxValueList.get(1)));
		return rate;
	}

	/**
	 * ��ȡͼԪ���е�λ��
	 * 
	 * @param element
	 *            ָ����ͼԪElement
	 * @return
	 */
	public static Point2D getCenterPoint(Element element) {
		if (element.getNodeName().equals("path")) {
			Path path = new Path(element.getAttribute("d"));
			Rectangle rect = path.getBounds();
			return new Point2D.Double(rect.getCenterX(), rect.getCenterY());
		} else {
			String x = element.getAttribute("x");
			String y = element.getAttribute("y");
			String width = element.getAttribute("width");
			String height = element.getAttribute("height");
			if ("".equals(x) || "".equals(y) || "".equals(width)
					|| "".equals(height)) {
				System.err.println("Illegal location param(s)");
				return null;
			}
			return new Point2D.Double(Double.valueOf(x) + Double.valueOf(width)
					/ 2, Double.valueOf(y) + Double.valueOf(height) / 2);
		}
	}

	public static String getPathDAttrString(Point2D start, Point2D end) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("M").append(start.getX()).append(" ")
				.append(start.getY()).append(" ").append("L")
				.append(end.getX()).append(" ").append(end.getY());
		return buffer.toString();
	}

	/**
	 * �ж��ܷ�VHPath����
	 * 
	 * @param e
	 * @return
	 */
	public static boolean isVHPathConnectable(Element e) {
		if (e == null) {
			return false;
		}
		if (e.getNodeName().equals("use")) {
			return true;
		}
		return false;
	}

	public static boolean equalPoint(Point2D p1, Point2D p2) {
		return (Math.abs(p1.getX() - p2.getX()) <= 0.01 && Math.abs(p1.getY()
				- p2.getY()) <= 0.01);
	}
	
	public static  boolean contiansPoint(Rectangle2D rect, Point2D p) {
		double x0 = rect.getX();
		double y0 = rect.getY();
		return (p.getX() >= x0 && p.getY() >= y0
				&& p.getX() <= x0 + rect.getWidth()+0.01 && p.getY() <= y0
				+ rect.getHeight()+0.01);
	}
}
