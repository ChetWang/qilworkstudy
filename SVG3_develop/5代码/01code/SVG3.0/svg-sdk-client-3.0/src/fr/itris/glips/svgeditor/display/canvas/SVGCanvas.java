/**
 * Created on 23 mars 2004
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
package fr.itris.glips.svgeditor.display.canvas;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.swing.JPanel;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathExpressionException;

import libraries.URIEncoderDecoder;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.DynamicGVTBuilder;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.gvt.CanvasGraphicsNode;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.RootGraphicsNode;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;

import com.nci.svg.actionparser.ActionParserModule;
import com.nci.svg.bean.OperLogBean;
import com.nci.svg.sdk.CodeConstants;
import com.nci.svg.sdk.bean.CodeInfoBean;
import com.nci.svg.sdk.bean.GraphFileBean;
import com.nci.svg.sdk.bean.GraphFileParamsBean;
import com.nci.svg.sdk.bean.ModelActionBean;
import com.nci.svg.sdk.bean.ModelBean;
import com.nci.svg.sdk.bean.RelaIndunormDescBean;
import com.nci.svg.sdk.bean.RelaIndunormGraphBean;
import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.bean.SimpleCodeBean;
import com.nci.svg.sdk.bean.TopologyBean;
import com.nci.svg.sdk.client.DataManageAdapter;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.SysSetDefines;
import com.nci.svg.sdk.client.business.ShapeInfoLocator;
import com.nci.svg.sdk.client.communication.CommunicationBean;
import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.client.util.EditorToolkit;
import com.nci.svg.sdk.client.util.RemoteUtilities;
import com.nci.svg.sdk.client.util.Utilities;
import com.nci.svg.sdk.communication.ActionNames;
import com.nci.svg.sdk.communication.ActionParams;
import com.nci.svg.sdk.display.canvas.CanvasOperAdapter;
import com.nci.svg.sdk.display.canvas.DefaultCanvasOperImpl;
import com.nci.svg.sdk.graphmanager.property.GraphModel;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.other.LinkPointManager;
import com.nci.svg.sdk.ui.ComboPanel;
import com.nci.svg.sdk.ui.DoubleCompPanel;
import com.nci.svg.sdk.ui.EditPanel;
import com.nci.svg.sdk.ui.NCIButtonPanel;
import com.nci.svg.sdk.ui.NciCustomDialog;

import fr.itris.glips.library.FormatStore;
import fr.itris.glips.library.PreferencesStore;
import fr.itris.glips.library.Toolkit;
import fr.itris.glips.library.monitor.Monitor;
import fr.itris.glips.library.util.XMLPrinter;
import fr.itris.glips.svgeditor.display.canvas.grid.Grid;
import fr.itris.glips.svgeditor.display.canvas.zoom.Zoom;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * @author ITRIS, Jordi SUC the class of the canvas of a SVG
 *         file,用来显示svg图形的画布（canvas）
 */
public class SVGCanvas extends JPanel {

	/**
	 * the constant for the grid layer，网格层
	 */
	public static final int GRID_LAYER = 0;
	/**
	 * the constant for the bottom layer，最底层
	 */
	public static final int BOTTOM_LAYER = 1;
	/**
	 * the constant for the selection layer，选择层
	 */
	public static final int SELECTION_LAYER = 2;
	/**
	 * the constant for the draw layer，绘图层
	 */
	public static final int DRAW_LAYER = 3;

	/**
	 * add by yux,2009-1-4 拓扑点层
	 */
	public static final int TERMINAL_LAYER = 5;
	/**
	 * the constant for the top layer,最顶层
	 */
	public static final int TOP_LAYER = 4;
	/**
	 * the array of the layers，各层的集合
	 */
	protected static int[] layers = { GRID_LAYER, BOTTOM_LAYER,
			SELECTION_LAYER, DRAW_LAYER, TOP_LAYER, TERMINAL_LAYER };
	/**
	 * the color used for drawing the area that is outside the current parent
	 * bounds
	 */
	protected static final Color parentVeilColor = new Color(255, 255, 255, 150);
	/**
	 * the canvas uri，打开的svg图的uri
	 */
	private String uri = "";

	/**
	 * 临时svg图的uri
	 */
	private String strTmpUri = "";
	/**
	 * the canvas document，svg图形的xml Document
	 */
	private Document document;
	/**
	 * the builder，apache batik的gvt构造器
	 */
	private GVTBuilder builder;
	/**
	 * the bridge context，apache batik的桥环境，用于连接xml的DOM元素和gvt节点之间的关系
	 */
	private BridgeContext ctx;
	/**
	 * the root graphics node，GVT树的最顶层图形节点
	 */
	private RootGraphicsNode gvtRoot;
	/**
	 * the offscreen image of the canvas
	 */
	private BufferedImage canvasOffscreenImage;
	/**
	 * the rendered rectangle
	 */
	private Rectangle renderedRectangle = new Rectangle(0, 0, 1, 1),
			tmpRectangle = new Rectangle(0, 0, 0, 0);
	/**
	 * the scrollpane that contains the canvas
	 */
	private SVGScrollPane scrollpane;
	/**
	 * the canvas zoom manager
	 */
	private Zoom zoomManager;
	/**
	 * the grid manager
	 */
	protected Grid gridManager;
	/**
	 * the file of the project this canvas is associated with
	 */
	private File projectFile;
	/**
	 * the map associating an id integer to the list of the paint listeners for
	 * a layer
	 */
	private Map<Integer, Set<CanvasPainter>> paintListeners = new ConcurrentHashMap<Integer, Set<CanvasPainter>>();
	/**
	 * the zone that should be repainted with the paint listeners when the
	 * rectangle is not null
	 */
	private Rectangle2D repaintZone;
	/**
	 * the svg handle this canvas is associated to
	 */
	private SVGHandle svgHandle;
	/**
	 * the repaint manager，重绘管理器，用于图形导入或变动后的重绘工作
	 */
	protected RepaintManager repaintManager = RepaintManager
			.currentManager(this);

	private static String BACKGROUND_COLOR_ID = "CanvasBackground";
	private Color background = Color.white;

	private LinkPointManager lpManager = null;

	private HashMap<String, ArrayList<String>> allSelectedLayerMap = new HashMap<String, ArrayList<String>>();

	/**
	 * add by yux,2008.12.26 文件属性框
	 */
	private NciCustomDialog fileProperties = null;
	/**
	 * add by yux,2008.12.26 文件类型
	 */
	private String fileType = null;

	/**
	 * add by yux,2008.12.26 图文件类型属性bean
	 */
	private GraphFileParamsBean graphFileParamsBean = null;

	/**
	 * add by yux,2008.12.30 文件属性对象
	 */
	private GraphFileBean graphFileBean = null;

	/**
	 * add by yux,2009-1-9 操作日志记录
	 */
	private HashMap<String, OperLogBean> operLogs = new HashMap<String, OperLogBean>();

	private LinkedList<String> linkedLogs = new LinkedList<String>();

	/**
	 * add by yux,2009-1-9 日志记录标记
	 */
	private boolean bLog = false;

	/**
	 * add by yux,2009-1-9 新建文件标记
	 */
	private boolean bCreate = false;

	private ArrayList<String> graphIndunormTypeList = new ArrayList<String>();

	/**
	 * add by yux,2009-1-22 初始化加载所需队列
	 */
	private ArrayList<Runnable> initList = new ArrayList<Runnable>();

	/**
	 * add by yux,2009-3-5 图形操作接口类
	 */
	private CanvasOperAdapter canvasOper = null;

	/**
	 * @return the strTmpUri
	 */
	public String getStrTmpUri() {
		return strTmpUri;
	}

	/**
	 * @param strTmpUri
	 *            the strTmpUri to set
	 */
	public void setStrTmpUri(String strTmpUri) {
		this.strTmpUri = this.uri;
		this.uri = strTmpUri;
	}

	/**
	 * the constructor of the class
	 * 
	 * @param scrollpane
	 *            the scrollpane into which the canvas will be inserted
	 */
	public SVGCanvas(SVGScrollPane scrollpane) {

		this.scrollpane = scrollpane;
		this.svgHandle = scrollpane.getSVGHandle();
		this.zoomManager = new Zoom(this);
		setDoubleBuffered(true);
		try {
			String value = PreferencesStore.getPreference(null,
					BACKGROUND_COLOR_ID);
			if (value != null && !value.equals(""))
				background = new Color(Integer.parseInt(value));

			setBackground(background);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// creating the paint listeners map structure
		paintListeners
				.put(GRID_LAYER, new CopyOnWriteArraySet<CanvasPainter>());
		paintListeners.put(BOTTOM_LAYER,
				new CopyOnWriteArraySet<CanvasPainter>());
		paintListeners.put(SELECTION_LAYER,
				new CopyOnWriteArraySet<CanvasPainter>());
		paintListeners
				.put(DRAW_LAYER, new CopyOnWriteArraySet<CanvasPainter>());
		paintListeners.put(TERMINAL_LAYER,
				new CopyOnWriteArraySet<CanvasPainter>());
		paintListeners.put(TOP_LAYER, new CopyOnWriteArraySet<CanvasPainter>());
		lpManager = new LinkPointManager(this.svgHandle);
	}

	public void saveTmpFile(Document doc, Monitor monitor) {
		String strPath = Constants.NCI_SVG_CACHE_DIR + "temp\\"
				+ System.currentTimeMillis();

		String strFileName = "";
		strFileName = strPath + "\\" + svgHandle.getSVGFrame().getTitle()
				+ ".svg";
		File file = new File(strPath);
		file.mkdirs();
		file = new File(strFileName);
		if (file != null) {
			XMLPrinter.printXML(doc, file, null);
			setURI(file.toURI().toString(), monitor);
			setStrTmpUri("");
			return;
		}
	}

	private boolean nciSvgType = false;

	public boolean isNCISvgType() {
		return nciSvgType;
	}

	/**
	 * 根据传入的类型设置文件头和描述信息
	 * 
	 * @param strFileType
	 *            :文件类型
	 */
	public void setFileType(String strFileType, boolean isCreated) {
		this.fileType = strFileType;
		if (!isCreated) {
			return;
		}
		Element desc = (Element) document.getDocumentElement()
				.getElementsByTagName("desc").item(0);
		desc.setAttribute("nci-filetype", strFileType);
		nciSvgType = true;
		ArrayList<RelaIndunormGraphBean> graphList = svgHandle.getEditor()
				.getIndunormManager().getIndunormGraphRela(strFileType);
		ArrayList<RelaIndunormDescBean> descList = svgHandle.getEditor()
				.getIndunormManager().getIndunormDescRela(strFileType);

		// 添加至文件头节点
		if (graphList != null && graphList.size() > 0) {
			String shortName, quote, defs;
			for (int i = 0; i < graphList.size(); i++) {
				shortName = graphList.get(i).getTypeBean().getShortName();
				quote = graphList.get(i).getTypeBean().getQuote();
				defs = graphList.get(i).getDefs();
				if (shortName != null) {
					document.getDocumentElement().setAttribute(
							"xmlns:" + shortName, quote);
					graphIndunormTypeList.add(shortName);
				}

				if (defs != null) {
					Element defsElement = (Element) document
							.getDocumentElement().getElementsByTagName("defs")
							.item(0);
					DocumentBuilderFactory docBuildFactory = DocumentBuilderFactory
							.newInstance();
					Document symbolDoc = null;
					try {
						symbolDoc = docBuildFactory.newDocumentBuilder().parse(
								new ByteArrayInputStream(defs.getBytes()));
					} catch (Exception e) {
						symbolDoc = null;
					}
					if (symbolDoc != null) {
						Element e = (Element) document.importNode(symbolDoc
								.getDocumentElement(), true);
						defsElement.appendChild(e);
					}
				}
			}
		}

		// 添加至描述节点中
		if (descList != null && descList.size() > 0) {
			String shortName;
			for (int i = 0; i < descList.size(); i++) {
				shortName = descList.get(i).getDescBean().getShortName();
				if (shortName != null) {
					desc.setAttribute(shortName, "");
				}
			}
		}
		packFilePropertiesDialog();
		fileProperties.setLocationRelativeTo(svgHandle.getEditor()
				.findParentFrame());
		fileProperties.setVisible(true);

		createCanvasOper();
	}

	/**
	 * add by yux,2008.12.26 从服务器获取本文件类型的配置参数信息，生成属于本文件的保存界面
	 */
	private void packFilePropertiesDialog() {
		graphFileParamsBean = getGraphFileParams();
		fileProperties = new NciCustomDialog(svgHandle.getEditor()
				.findParentFrame(), true);
		if (graphFileParamsBean == null)
			return;
		EditPanel namePanel = new EditPanel();
		namePanel.getShowText().setText("文件名称");
		namePanel.getEditText().setText(svgHandle.getSVGFrame().getTitle());
		fileProperties.addComponent(namePanel);
		ComboPanel fileType = new ComboPanel();
		fileType.getShowText().setText("文件类型");
		ResultBean resultBean = svgHandle.getEditor().getDataManage().getData(
				DataManageAdapter.KIND_CODES, CodeConstants.SVG_GRAPH_TYPE,
				null);
		fileType.setSonComboData((HashMap<String, CodeInfoBean>) resultBean
				.getReturnObj());
		fileProperties.addComponent(fileType);
		String desc = null, type = null, nullFlag = null;
		for (int i = 0; i < 10; i++) {
			desc = graphFileParamsBean.getBean(i).getDesc();
			if (desc == null || desc.length() == 0)
				continue;
			type = graphFileParamsBean.getBean(i).getType();
			if (type == null || type.length() == 0) {
				// 文本输入窗口
				EditPanel edit = new EditPanel();
				edit.getShowText().setText(desc);
				edit.getEditText().setText("");
				fileProperties.addComponent(edit);
			} else {

				ResultBean result = svgHandle.getEditor().getDataManage()
						.getData(DataManageAdapter.KIND_CODES, type, null);
				if (result.getReturnFlag() == ResultBean.RETURN_SUCCESS) {
					ComboPanel combo = new ComboPanel();
					combo.getShowText().setText(desc);
					HashMap<String, CodeInfoBean> map = (HashMap<String, CodeInfoBean>) result
							.getReturnObj();
					combo.setSonComboData(map);
					fileProperties.addComponent(combo);
				}
			}

		}

		NCIButtonPanel button = new NCIButtonPanel();
		// 确定按钮
		button.getButtonOK().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EditPanel namePanel = (EditPanel) fileProperties
						.getContentPane().getComponent(0);
				String textName = namePanel.getEditText().getText();
				if (!svgHandle.getName().equals(textName) && textName != null
						&& textName.length() > 0) {
					svgHandle.setName(textName);
					graphFileBean.setFileName(textName);
				} else if (textName == null || textName.length() == 0) {
					svgHandle.getEditor().getSvgSession().showMessageBox(
							"文件名称不能为空", "文件名称不能为空");
					namePanel.getEditText().requestFocus();
					return;
				}
				ComboPanel typePanel = (ComboPanel) fileProperties
						.getContentPane().getComponent(1);
				if (typePanel.getSonCombo().getSelectedIndex() == -1) {
					svgHandle.getEditor().getSvgSession().showMessageBox(
							"文件类型不能为空", "文件类型不能为空");
					typePanel.getSonCombo().requestFocus();
					return;
				} else {
					SimpleCodeBean codeBean = (SimpleCodeBean) typePanel
							.getSonCombo().getSelectedItem();
					if (codeBean != null)
						graphFileBean.setFileType(codeBean.getCode());
				}
				String desc = null, type = null, nullflag = null;
				// 属性输入框起始位置
				int index = 1;
				for (int i = 0; i < 10; i++) {
					desc = graphFileParamsBean.getBean(i).getDesc();
					if (desc == null || desc.length() == 0)
						continue;
					index++;
					type = graphFileParamsBean.getBean(i).getType();
					nullflag = graphFileParamsBean.getBean(i).getNullFlag();
					if (type == null || type.length() == 0) {
						// 文本输入窗口
						EditPanel edit = (EditPanel) fileProperties
								.getContentPane().getComponent(index);
						if (nullflag != null && nullflag.equals("0")
								&& edit.getEditText().getText().length() == 0) {
							svgHandle.getEditor().getSvgSession()
									.showMessageBox("属性不能为空", desc + "不能为空");
							edit.getEditText().requestFocus();
							return;
						} else {
							graphFileBean.setParams(i, edit.getEditText()
									.getText());
						}
					} else {

						ComboPanel combo = (ComboPanel) fileProperties
								.getContentPane().getComponent(index);
						if (nullflag != null && nullflag.equals("0")
								&& combo.getSonCombo().getSelectedIndex() == -1) {
							svgHandle.getEditor().getSvgSession()
									.showMessageBox("属性不能为空", desc + "不能为空");
							combo.getSonCombo().requestFocus();
							return;
						} else {
							SimpleCodeBean codeBean = (SimpleCodeBean) combo
									.getSonCombo().getSelectedItem();
							if (codeBean != null)
								graphFileBean.setParams(i, codeBean.getCode());
						}
					}

				}

				fileProperties.setVisible(false);
			}
		});

		// 取消按钮
		button.getButtonCancel().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileProperties.setVisible(false);
			}
		});
		button.setPreferredSize(new Dimension(DoubleCompPanel.compSize.width
				+ DoubleCompPanel.labelSize.width,
				DoubleCompPanel.labelSize.height
						+ DoubleCompPanel.compSize.height));
		fileProperties.addComponent(button);
		fileProperties.pack();
		fileProperties.setTitle("文件属性");

	}

	public void showFileProperties() {
		if (graphFileBean == null) {
			this.graphFileBean = new GraphFileBean();
			final NciCustomDialog dialog = new NciCustomDialog(svgHandle
					.getEditor().findParentFrame(), true);
			ComboPanel combo = new ComboPanel();
			combo.getShowText().setText("业务图类型");
			ResultBean resultBean = svgHandle.getEditor().getDataManage()
					.getData(DataManageAdapter.KIND_CODES,
							CodeConstants.SVG_BUSINESS_GRAPH_TYPE, null);
			combo.setSonComboData((HashMap<String, CodeInfoBean>) resultBean
					.getReturnObj());
			dialog.addComponent(combo);
			NCIButtonPanel button = new NCIButtonPanel();
			// 确定按钮
			button.getButtonOK().addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ComboPanel combo = (ComboPanel) dialog.getContentPane()
							.getComponent(0);
					if (combo.getSonCombo().getSelectedIndex() == -1) {
						svgHandle.getEditor().getSvgSession().showMessageBox(
								"文件类型不能为空", "文件类型不能为空");
						return;
					}

					SimpleCodeBean codeBean = (SimpleCodeBean) combo
							.getSonCombo().getSelectedItem();
					fileType = codeBean.getCode();
					packFilePropertiesDialog();
					fileProperties.setLocationRelativeTo(svgHandle.getEditor()
							.findParentFrame());
					fileProperties.setVisible(true);
				}
			});

			button.getButtonOK().addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					graphFileBean = null;
					dialog.setVisible(false);
				}
			});
			dialog.addComponent(button);
			dialog.setTitle("请选择图业务类型");
			dialog.setLocationRelativeTo(svgHandle.getEditor()
					.findParentFrame());
			dialog.setVisible(true);
		} else {
			fileProperties.setLocationRelativeTo(svgHandle.getEditor()
					.findParentFrame());
			fileProperties.setVisible(true);
		}
	}

	/**
	 * 属性输入校验
	 * 
	 * @return：校验合法返回0，失败返回－1
	 */
	public int checkInput() {
		if (graphFileParamsBean == null)
			return -1;
		String desc = null, type = null, nullFlag = null;
		int index = -1;
		for (int i = 0; i < 10; i++) {
			desc = graphFileParamsBean.getBean(i).getDesc();
			if (desc == null || desc.length() == 0)
				continue;
			index++;
			type = graphFileParamsBean.getBean(i).getType();
			nullFlag = graphFileParamsBean.getBean(i).getNullFlag();
			if (nullFlag.equals("0"))
				continue;
			if (type == null || type.length() == 0) {
				// 文本输入窗口
				EditPanel edit = (EditPanel) fileProperties.getContentPane()
						.getComponent(index);
				if (edit.getEditText().getText() == null
						|| edit.getEditText().getText().length() == 0)
					return -1;
			} else {
				ComboPanel combo = (ComboPanel) fileProperties.getContentPane()
						.getComponent(index);
				if (combo.getSonCombo().getSelectedIndex() == -1)
					return -1;
			}
		}

		return 0;
	}

	/**
	 * add by yux,2008.12.26 根据业务系统编号和图类型编号获取图类型参数属性集合
	 * 
	 * @return：图类型参数描述bean
	 */
	private GraphFileParamsBean getGraphFileParams() {

		ArrayList<GraphFileParamsBean> list = RemoteUtilities
				.getGraphFileParams(svgHandle.getEditor(), fileType);
		if (list == null || list.size() == 0)
			return null;
		return list.get(0);
	}

	/**
	 * creates a new svg document
	 * 
	 * @param width
	 *            the width of the new document
	 * @param height
	 *            the height of the new document
	 */
	public void newDocument(String width, String height) {

		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
		SVGDocument doc = (SVGDocument) impl.createDocument(svgNS, "svg", null);
		doc.getDocumentElement().getNamespaceURI();
		// gets the root element (the svg element)
		Element svgRoot = doc.getDocumentElement();
		// svgRoot.setAttribute(Constants.NCI_SVG_PRODUCER_ATTR,
		// Constants.NCI_SVG_PRODUCER_VALUE);
		svgRoot.setAttribute(Constants.NCI_SVG_XMLNS,
				Constants.NCI_SVG_XMLNS_VALUE);
		// svgRoot.setAttribute(Constants.PSR_SVG_XMLNS,
		// Constants.PSR_SVG_XMLNS_VALUE);
		// set the width and height attribute on the root svg element
		svgRoot.setAttributeNS(null, "width", width);
		svgRoot.setAttributeNS(null, "height", height);
		svgRoot.setAttribute("viewBox", "0 0 "
				+ EditorToolkit.getPixelledNumber(width) + " "
				+ EditorToolkit.getPixelledNumber(height));
		appendDesc(doc);
		Toolkit.checkRtdaXmlns(doc);
		if (svgHandle.getHandleType() == SVGHandle.HANDLE_TYPE_SVG) {
			graphFileBean = new GraphFileBean();
		}

		bCreate = true;
		setDocument(doc, null);

	}

	/**
	 * 图元管理的Document创建
	 * 
	 * @param width
	 *            默认宽度
	 * @param height
	 *            默认高度
	 * @deprecated
	 */
	public void newSVGUnitDocument() {
		// DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		// String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
		SVGDocument doc = svgHandle.getEditor().getSymbolManager()
				.createEmptyGraphUnitDocument();
		// initializing the canvas
		Toolkit.checkRtdaXmlns(doc);
		this.document = doc;
		initializeCanvas(null);
	}

	/**
	 * sets the uri for the canvas (should not be invoked in the AWT thread)
	 * 
	 * @param uri
	 *            the uri of the svg file to be loaded
	 * @param monitor
	 *            the object monitoring the loading of the svg file
	 */
	public void setURI(final String uri, Monitor monitor) {

		synchronized (this) {
			this.uri = uri;
		}

		if (EditorAdapter.isRtdaAnimationsVersion) {

			projectFile = svgHandle.getEditor().getColorChooser()
					.getProjectFile(uri);
		}

		try {
			if (monitor != null) {

				monitor.start();
				monitor.setProgress(0);
			}

			// creating the svg document corresponding to this uri
			SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory("");
			SVGDocument doc = factory.createSVGDocument(uri);

			if (doc != null) {

				// normalizing the document
				if (svgHandle.getHandleType() == SVGHandle.HANDLE_TYPE_GRAPH_UNIT_OLD)
					reloadNciPoint(doc);
				// // 更新设备关联的显示，如果没有进行关联，则该设备将以特殊的形式展现（如虚线）
				// Utilities.updateRelationUI(doc);
				// Utilities.updateNCIGraphUnitXLink(doc,
				// svgHandle.getEditor());
				setDocument(doc, monitor);
				initLoad(doc);
			}
		} catch (Exception ex) {

			ex.printStackTrace();
			getSVGHandle().dispose();

			if (monitor != null) {

				String messageLabel = ResourcesManager.bundle
						.getString("canvasLoadingFailedMessage");
				messageLabel = URIEncoderDecoder.HTMLEntityEncode(messageLabel);

				String returnedErrorMessage = URIEncoderDecoder
						.HTMLEntityEncode(ex.getMessage());

				messageLabel = "<html><body>" + messageLabel + "<br><i>"
						+ returnedErrorMessage + "</i></body></html>";
				monitor.setErrorMessage(messageLabel);
			}
		}
	}

	public void setStringToDocument(String strContent) {
		if (strContent == null || strContent.equals("")) {
			return;
		}
		if (strContent.indexOf("height=\"495.0py\"") >= 0) {
			svgHandle.close();
			return;
		}
		Document doc = Utilities.getXMLDocumentByString(strContent);
		String width = doc.getDocumentElement().getAttribute("width");
		if (width.indexOf("py") >= 0) {
			svgHandle.close();
			return;
		}
		String height = doc.getDocumentElement().getAttribute("height");
		if (height.indexOf("py") >= 0) {
			svgHandle.close();
			return;
		}
		File file = new File(Constants.NCI_SVG_CACHE_CLIP_TEMP_CACHE_DIR
				+ "temp.svg");

		// deprecated by wanql, 2008.12.11--11:35
		// if (strContent.indexOf("common.css") >= 0
		// && strContent.indexOf("symbol.xml") >= 0) {
		// // 这个图是qsj版本转过来的图，fill属性没有，symbol没有,这里要将这两个改掉
		// // parseQSJXX(doc);
		//
		// }
		XMLPrinter.printStringToFile(Utilities.printNode(doc
				.getDocumentElement(), false), file);

		System.out.println("printStringToFile ok");
		// try {
		// SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory("");
		// SVGDocument doc = factory
		// .createSVGDocument(file.toURI().toString());
		// updateDocument(doc);
		// } catch (Exception ex) {
		// ex.printStackTrace();
		// }
		setURI(file.toURI().toString(), null);
		System.out.println("setStringToDocument ok");
	}

	// /**
	// * 转换qsj版本的图,这个图是qsj版本转过来的图，fill属性没有，symbol没有,这里要将这两个改掉
	// *
	// * @param oldDoc
	// * qsj版本的document对象
	// */
	// private void parseQSJXX(Document oldDoc) {
	// // 填充fill属性，要将没有fill属性的明确表示fill:none,并加上css
	// this.appendCSS(oldDoc);
	// Set<String> shapeNames = EditorToolkit.getSvgShapeElementNames();
	// Iterator<String> it = shapeNames.iterator();
	// while (it.hasNext()) {
	// String shapeName = it.next();
	// if (shapeName.equals("a") || shapeName.equals("g")
	// || shapeName.equals("image")) {
	// continue;
	// }
	// NodeList shapes = oldDoc.getDocumentElement().getElementsByTagName(
	// shapeName);
	// Element shapeEle = null;
	// String style = null;
	// String fill = null;
	// for (int i = 0; i < shapes.getLength(); i++) {
	// shapeEle = (Element) shapes.item(i);
	// if (shapeName.equals("use")) {
	// String voltageLevel = shapeEle.getAttribute("class");
	// if(voltageLevel.equals("")){
	// if(shapeEle.getAttribute("xlink:href").indexOf("_1")>=0 ||
	// shapeEle.getAttribute("xlink:href").indexOf("_闭")>=0){
	// shapeEle.setAttribute("class", "fill");
	// }else{
	// shapeEle.setAttribute("class", "fillnone");
	// }
	//						
	// }
	// } else {
	// style = shapeEle.getAttribute("style");
	// fill = shapeEle.getAttribute("fill");
	// if (fill.equals("")) {
	// if (style.indexOf("fill") < 0) {
	// EditorToolkit.setStyleProperty(shapeEle, "fill",
	// "none");
	// }
	// }
	// }
	// }
	// }
	// // symbol填充
	// NodeList existSymbols = oldDoc.getDocumentElement()
	// .getElementsByTagName("use");
	// Element useEle = null;
	// NCIEquipSymbolBean symbolBean;
	// Document symbolDoc;
	// for (int i = 0; i < existSymbols.getLength(); i++) {
	// useEle = (Element) existSymbols.item(i);
	// // 这里只是普通的document element对象，不需要namespace
	// String id = useEle.getAttribute("xlink:href").substring(1);
	// symbolBean = svgHandle.getEditor().getGraphUnitManager()
	// .getSymbolMap_useID().get(id);
	// symbolDoc = Utilities.getXMLDocumentByString(symbolBean
	// .getContent());
	// svgHandle.getEditor().getSvgSession().iteratorUseElementInserting(
	// oldDoc, symbolDoc.getDocumentElement());
	// }
	// }

	protected Element appendDefsElement(Document doc) {
		Element defsElement = (Element) doc.getDocumentElement()
				.getElementsByTagName("defs").item(0);
		if (defsElement == null) {
			defsElement = doc.createElementNS(doc.getNamespaceURI(), "defs");
			doc.getDocumentElement().appendChild(defsElement);

			Element e = doc.createElementNS(doc.getDocumentElement()
					.getNamespaceURI(), "symbol");
			e.setAttribute("id", "terminal");
			e.setAttribute("preserveAspectRatio", "xMidYMid meet");
			defsElement.appendChild(e);

			Element circle = doc.createElement("circle");
			circle.setAttribute("fill", "yellow");
			circle.setAttribute("stroke", "yellow");
			circle.setAttribute("r", "1");
			circle.setAttribute("cx", "0");
			circle.setAttribute("cy", "0");
			circle.setAttribute("stroke-width", "1");
			e.appendChild(circle);
		}
		return defsElement;
	}

	/**
	 * add by yux,2008.12.10 将服务器端配置的关联定义包加载到本文件中
	 */
	protected void insertDefsStanard() {
		// TODO:
		return;
	}

	protected void appendDesc(Document doc) {
		if (doc.getDocumentElement().getElementsByTagName("desc") == null
				|| doc.getDocumentElement().getElementsByTagName("desc")
						.getLength() == 0) {
			Element element = doc.createElement("desc");
			doc.getDocumentElement().appendChild(element);

			// if (svgHandle.getHandleType() ==
			// SVGHandle.HANDLE_TYPE_SYMBOL_GRAPH_UNIT_NORMAL) {
			// element.setAttribute("type",
			// NCIEquipSymbolBean.SYMBOL_TYPE_GRAPHUNIT);
			// element.setAttribute("name", svgHandle.getName());
			// } else if (svgHandle.getHandleType() ==
			// SVGHandle.HANDLE_TYPE_SYMBOL_TEMPLATE) {
			// element.setAttribute("type",
			// NCIEquipSymbolBean.SYMBOL_TYPE_TEMPLATE);
			// element.setAttribute("name", svgHandle.getName());
			// }
		}
	}

	private void tranGraphUnitDoc(Document doc) {
		NodeList list = doc.getDocumentElement().getElementsByTagName("title");
		if (list != null && list.getLength() > 0) {
			Element title = (Element) list.item(0);
			String strSymbolID = title.getAttribute("name");

			list = doc.getDocumentElement().getChildNodes();
			Element useElement = null;
			for (int i = 0; i < list.getLength(); i++) {
				if (list.item(i) instanceof Element
						&& list.item(i).getNodeName().equals("use")) {
					useElement = (Element) list.item(i);
				}
			}

			Element symbolElement = doc.getElementById(strSymbolID);
			if (symbolElement == null)
				return;

			list = symbolElement.getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				if (list.item(i) instanceof Element) {
					Element element = (Element) doc.importNode(list.item(i),
							true);
					doc.getDocumentElement().insertBefore(element, useElement);
					if (svgHandle.getEditor().getSVGToolkit().getStyleProperty(
							element, "stroke").length() == 0
							&& element.getAttribute("stroke").length() == 0)
						element.setAttribute("stroke", "#000000");
				}
			}

			doc.getDocumentElement().removeChild(useElement);
		}

	}

	/**
	 * 去除doc中多余的无效结点
	 * 
	 * @param doc
	 */
	private void removeUnwantedElement(Document doc) {
		// 删除
		NodeList list = doc.getDocumentElement().getElementsByTagName("script");
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i) instanceof Element) {
				Element element = (Element) list.item(i);
				String strJSName = element.getAttributeNS(
						EditorToolkit.xmlnsXLinkNS, "href");
				if (strJSName.equals("../scripts/xl.js")) {
					element.getParentNode().removeChild(element);
					i--;
				}
			}
		}

		list = doc.getDocumentElement().getElementsByTagName("image");
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i) instanceof Element) {
				Element element = (Element) list.item(i);
				String strImageName = element.getAttributeNS(
						EditorToolkit.xmlnsXLinkNS, "href");
				if (strImageName.equals("Concave.bm")
						|| strImageName.length() == 0) {
					element.getParentNode().removeChild(element);
					i--;
				}
			}
		}

		list = doc.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i).getNodeName().equals("xml-stylesheet")) {
				doc.removeChild(list.item(i));
				i--;
			}
		}

		try {
			list = Utilities.findNodes("//*[@class]", doc.getDocumentElement());
			for (int i = 0; i < list.getLength(); i++) {
				if (list.item(i) instanceof Element) {
					Element element = (Element) list.item(i);
					element.removeAttribute("fill");
					element.removeAttribute("stroke");
					EditorToolkit.setStyleProperty(element, "fill", "");
					EditorToolkit.setStyleProperty(element, "stroke", "");
				}
			}
		} catch (XPathExpressionException e) {

			e.printStackTrace();
		}

		Element rectElement = doc.getElementById("fourline");
		if (rectElement != null) {
			rectElement.getParentNode().removeChild(rectElement);
		}

	}

	/**
	 * 设置显示的内容
	 * 
	 * @param doc
	 *            SVGDocument对象，即显示的Document
	 * @param monitor
	 * @param flag
	 *            true为图元编辑时显示，false为图形显示
	 * 
	 */
	public void setDocument(Document doc, Monitor monitor) {

		NodeList descNode = doc.getDocumentElement().getElementsByTagName(
				"desc");
		if (descNode.getLength() > 0) {
			setFileType(((Element) descNode.item(0))
					.getAttribute("nci-filetype"), false);
		}
		appendDesc(doc);
		appendDefsElement(doc);

		if (svgHandle.getHandleType() == SVGHandle.HANDLE_TYPE_SYMBOL_GRAPH_UNIT_NORMAL) {
			tranGraphUnitDoc(doc);
		}

		removeUnwantedElement(doc);
		synchronized (this) {
			this.document = doc;
		}
		Dimension scaledCanvasSize = getScaledCanvasSize(doc
				.getDocumentElement());
		svgHandle.getSvgDOMNormalizer().normalize(doc, scaledCanvasSize);
		Toolkit.checkRtdaXmlns(doc);

		if (monitor != null && monitor.isCancelled()) {

			monitor.stop();
			getSVGHandle().dispose();
			return;
		}

		initializeCanvas(monitor);

		// add by yuxiang
		// 加载图形、设备和节点映射关系

		// 加载连接线信息
		loadLinkInfo();

		if (svgHandle.getNFileType() == SVGHandle.HANDLE_TYPE_SVG
				|| bCreate == false) {
			bLog = true;
		}

	}

	public void reloadNciPoint(Document doc) {
		Element eRoot = doc.getDocumentElement();
		Element terminal = (Element) eRoot.getElementsByTagName("terminal")
				.item(0);
		if (terminal == null) {
			return;
		}
		int nPointCount = terminal.getElementsByTagName("nci:POINT")
				.getLength();
		if (nPointCount == 0) {
			return;
		}

		int x = 0, y = 0;
		int ngCount = eRoot.getElementsByTagName("g").getLength();
		int i = 0;
		for (i = 0; i < ngCount; i++) {
			Element gElement = (Element) eRoot.getElementsByTagName("g")
					.item(i);
			if (gElement.getAttribute("id").equals("nci:terminal")) {
				eRoot.removeChild(gElement);
				break;
			}
		}

		String strName = null;
		Element gElement = doc.createElementNS(
				SVGDOMImplementation.SVG_NAMESPACE_URI, "g");
		gElement = (Element) doc.importNode(gElement, true);

		gElement.setAttribute("id", "nci:terminal");
		int nPointMax = 0;
		for (i = 0; i < nPointCount; i++) {
			Element element = (Element) terminal.getElementsByTagName(
					"nci:POINT").item(i);
			x = new Integer(element.getAttribute("x")).intValue();
			y = new Integer(element.getAttribute("y")).intValue();
			strName = element.getAttribute("name");

			Element egPoint = doc.createElementNS(
					SVGDOMImplementation.SVG_NAMESPACE_URI, "ellipse");

			egPoint.setAttribute("name", strName);
			egPoint.setAttribute("cx", String.format("%d", x));
			egPoint.setAttribute("cy", String.format("%d", y));
			egPoint.setAttribute("rx", "1");
			egPoint.setAttribute("ry", "1");
			egPoint.setAttribute("style",
					"stroke-width:1.0;stroke:#ff0000;fill:#ff0000;");
			gElement.appendChild(egPoint);

			strName = strName.substring(1);
			if (nPointMax < new Integer(strName).intValue()) {
				nPointMax = new Integer(strName).intValue();
			}
		}
		eRoot.appendChild(gElement);
		gElement.setAttribute("pmax", String.format("%d", nPointMax));
		eRoot.removeChild(terminal);

		return;
	}

	/**
	 * initializes the canvas.
	 * 
	 * @param monitor
	 *            the object monitoring the loading of the svg file
	 */
	public void initializeCanvas(final Monitor monitor) {// wangql将protected修饰符改为public

		// creating the selection handler
		svgHandle.createSelection();

		if (monitor != null) {

			monitor.setProgress(40);
		}
		// creating the graphics node
		try {
			UserAgentAdapter userAgent = new UserAgentAdapter();
			ctx = new BridgeContext(userAgent, null, new DocumentLoader(
					userAgent));
			builder = new DynamicGVTBuilder();
			ctx.setDynamicState(BridgeContext.DYNAMIC);
			GraphicsNode gvt = builder.build(ctx, document);

			if (gvt != null) {

				gvtRoot = gvt.getRoot();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		if (monitor != null) {

			monitor.setProgress(80);
		}

		if (monitor == null || !monitor.isCancelled()) {

			SwingUtilities.invokeLater(new Runnable() {

				public void run() {

					// setting the size of the canvas
					Dimension scaledCanvasSize = getScaledCanvasSize();
					setCanvasPreferredSize(scaledCanvasSize);

					svgHandle.getSVGFrame().displayFrame(scaledCanvasSize);

					svgHandle.getEditor().getHandlesManager().handleChanged();

					if (monitor != null) {

						monitor.stop();
					}

					// creating the grid manager
					gridManager = new Grid(SVGCanvas.this);

					// add by yuxiang
					// 新增或加载文件均最大化显示
					try {
						// svgHandle.getSVGFrame().setMaximum(false);
						svgHandle.getSVGFrame().setSelected(true);
						// svgHandle.getSVGFrame().setMaximum(true);
						String width = document.getDocumentElement()
								.getAttribute("width");
						String height = document.getDocumentElement()
								.getAttribute("height");
						if (!width.equals("") && !height.equals("")) {
							if (Double.parseDouble(width) > scrollpane
									.getViewport().getBounds().width
									|| Double.parseDouble(height) > scrollpane
											.getViewport().getBounds().height)
								zoomManager.fitImageToViewport();
						}

					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			});

		} else {

			monitor.stop();
			getSVGHandle().dispose();
		}

		//
		// this.setBackground((Color)svgHandle.getEditor().getGCParam(
		// "initBackGroudColor"));
	}

	/**
	 * disposes this canvas
	 */
	public void dispose() {

		// disposing all the elements of the canvas
		removeAllPaintListeners();
		paintListeners.clear();

		if (ctx != null) {

			ctx.dispose();
		}

		repaintManager.removeInvalidComponent(this);
		removeAll();

		uri = null;
		document = null;
		builder = null;
		ctx = null;
		gvtRoot = null;
		canvasOffscreenImage = null;
		renderedRectangle = null;
		tmpRectangle = null;
		scrollpane = null;
		zoomManager = null;
		gridManager = null;
		projectFile = null;
		paintListeners = null;
		repaintZone = null;
		svgHandle = null;
		repaintManager = null;
	}

	/**
	 * sets the new uri for the canvas
	 * 
	 * @param uri
	 *            a uri
	 */
	public void setNewURI(String uri) {

		this.uri = uri;

		// getting the uri object
		URI theURI = null;

		try {
			theURI = new URI(uri);
		} catch (Exception ex) {
		}

		if (theURI != null) {

			try {
				((SVGOMDocument) getDocument()).setURLObject(theURI.toURL());
			} catch (Exception ex) {
			}
		}

		svgHandle.getEditor().getHandlesManager().handleChanged();
	}

	/**
	 * @return the uri of the canvas
	 */
	public String getURI() {

		return uri;
	}

	/**
	 * @return the viewing transform
	 */
	public AffineTransform getViewingTransform() {

		CanvasGraphicsNode canvasGraphicsNode = getCanvasGraphicsNode();

		if (canvasGraphicsNode != null) {

			return canvasGraphicsNode.getViewingTransform();
		}

		return null;
	}

	/**
	 * @return the rendering transform
	 */
	public AffineTransform getRenderingTransform() {

		double scale = zoomManager.getCurrentScale();
		return AffineTransform.getScaleInstance(scale, scale);
	}

	/**
	 * @return the offscreen image
	 */
	public BufferedImage getOffscreen() {

		return canvasOffscreenImage;
	}

	/**
	 * @return the document of the canvas
	 */
	public Document getDocument() {

		return document;
	}

	/**
	 * @return the grid manager
	 */
	public Grid getGridManager() {
		return gridManager;
	}

	/**
	 * @return the zoom manager
	 */
	public Zoom getZoomManager() {
		return zoomManager;
	}

	/**
	 * setting the preferred size for this canvas
	 * 
	 * @param size
	 *            the preferred size
	 */
	public void setCanvasPreferredSize(Dimension size) {

		setPreferredSize(size);
		setSize(size);
	}

	/**
	 * @return the bridge context
	 */
	public BridgeContext getBridgeContext() {

		return ctx;
	}

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);

		if (canvasOffscreenImage != null) {

			// getting the sub image that should be painted
			Rectangle clip = g.getClipBounds();

			Rectangle rendRect = new Rectangle(renderedRectangle.x,
					renderedRectangle.y, canvasOffscreenImage.getWidth(),
					canvasOffscreenImage.getHeight());

			if (clip.width != 0 && clip.height != 0) {

				if (clip.contains(rendRect)) {

					((Graphics2D) g).drawRenderedImage(canvasOffscreenImage,
							AffineTransform.getTranslateInstance(rendRect.x,
									rendRect.y));

				} else if (clip.intersects(rendRect)) {

					// getting the sub image to draw
					Rectangle rZone = clip.intersection(rendRect);

					if (rZone.width != 0 && rZone.height != 0) {

						BufferedImage subImage = canvasOffscreenImage
								.getSubimage(rZone.x - rendRect.x, rZone.y
										- rendRect.y, rZone.width, rZone.height);

						((Graphics2D) g).drawRenderedImage(subImage,
								AffineTransform.getTranslateInstance(rZone.x,
										rZone.y));
					}
				}
			}
		}

		drawPainters((Graphics2D) g);
	}

	/**
	 * add by yux,2008.12.05 根据获取最大显示范围
	 * 
	 * @param elements
	 *            :节点集
	 * @return:显示范围
	 */
	public Area getAreaByElements(Set<Element> elements) {
		// 获取节点集最大显示范围
		Rectangle2D bounds = null, bounds2 = null;
		Area BoundsArea = null;
		for (Element element : elements) {
			// getting the bounds of the element
			bounds = svgHandle.getSvgElementsManager().getSensitiveBounds(
					element);
			bounds2 = svgHandle.getSvgElementsManager().getNodeGeometryBounds(
					element);

			if (bounds != null && bounds.getWidth() > 0
					&& bounds.getHeight() > 0 && bounds2 != null
					&& bounds2.getWidth() > 0 && bounds2.getHeight() > 0) {

				bounds.add(bounds2);
				bounds = svgHandle.getNormalizedRectangle(bounds);

				if (BoundsArea == null) {

					BoundsArea = new Area(bounds);

				} else {

					BoundsArea.add(new Area(bounds));
				}
			}
		}
		return BoundsArea;
	}

	/**
	 * refreshing the svg canvas content
	 * 
	 * @param repaintSVGContent
	 *            whether the whole svg image should be redrawn
	 * @param updateSVGContent
	 *            whether one a part of the svg image should be redrawn
	 * @param dirtyAreas
	 *            the areas to redraw
	 */
	public void refreshCanvasContent(boolean repaintSVGContent,
			boolean updateSVGContent, Set<Area> dirtyAreas) {
		long start = System.nanoTime();
		// drawing the offscreen image and painting it
		if (repaintSVGContent) {

			BufferedImage tempCanvasOffscreenImage = canvasOffscreenImage;

			// getting the gvt root
			GraphicsNode root = gvtRoot;

			if (root != null) {

				Rectangle usedRectangle = null;
				boolean isScrollAction = false;
				int scrollX = 0, scrollY = 0;

				if (tmpRectangle != null) {

					usedRectangle = new Rectangle(tmpRectangle);
					scrollX = usedRectangle.x - renderedRectangle.x;
					scrollY = usedRectangle.y - renderedRectangle.y;
				}

				// checking if the rendered rectangle has been changed owing to
				// a scroll action
				if (usedRectangle != null
						&& usedRectangle.width == renderedRectangle.width
						&& usedRectangle.height == renderedRectangle.height
						&& Math.abs(scrollX) < usedRectangle.width
						&& Math.abs(scrollY) < usedRectangle.height) {

					renderedRectangle.x = usedRectangle.x;
					renderedRectangle.y = usedRectangle.y;

					BufferedImage image = new BufferedImage(
							renderedRectangle.width, renderedRectangle.height,
							BufferedImage.TYPE_INT_ARGB);
					Graphics2D g2 = GraphicsUtil.createGraphics(image);

					g2.setColor(background);
					BufferedImage tmpImage = null;

					if (scrollY > 0) {

						tmpImage = tempCanvasOffscreenImage.getSubimage(0,
								scrollY, renderedRectangle.width,
								renderedRectangle.height - scrollY);
						g2.drawImage(tmpImage, 0, 0, tmpImage.getWidth(),
								tmpImage.getHeight(), null);

					} else if (scrollY < 0) {

						tmpImage = tempCanvasOffscreenImage.getSubimage(0, 0,
								renderedRectangle.width,
								renderedRectangle.height + scrollY);
						g2
								.drawImage(tmpImage, 0, -scrollY, tmpImage
										.getWidth(), tmpImage.getHeight(), null);
					}

					if (scrollX > 0) {

						tmpImage = tempCanvasOffscreenImage.getSubimage(
								scrollX, 0, renderedRectangle.width - scrollX,
								renderedRectangle.height);
						g2.drawImage(tmpImage, 0, 0, tmpImage.getWidth(),
								tmpImage.getHeight(), null);

					} else if (scrollX < 0) {

						tmpImage = tempCanvasOffscreenImage.getSubimage(0, 0,
								renderedRectangle.width + scrollX,
								renderedRectangle.height);
						g2
								.drawImage(tmpImage, -scrollX, 0, tmpImage
										.getWidth(), tmpImage.getHeight(), null);
					}

					g2.dispose();
					tempCanvasOffscreenImage = image;
					isScrollAction = true;

				} else if (!updateSVGContent) {

					if (usedRectangle != null) {

						renderedRectangle.x = usedRectangle.x;
						renderedRectangle.y = usedRectangle.y;
						renderedRectangle.width = usedRectangle.width;
						renderedRectangle.height = usedRectangle.height;
					}

					// creating the new offscreen image
					tempCanvasOffscreenImage = new BufferedImage(
							renderedRectangle.width, renderedRectangle.height,
							BufferedImage.TYPE_INT_ARGB);
				}

				Graphics2D g2 = null;

				if (isScrollAction) {

					// getting the canvas scale
					double scale = zoomManager.getCurrentScale();

					// computing the transform
					AffineTransform af = AffineTransform.getScaleInstance(
							scale, scale);
					af.preConcatenate(AffineTransform.getTranslateInstance(
							-renderedRectangle.x, -renderedRectangle.y));

					// computing the rendered rectangle in the base coordinates
					Rectangle2D baseRectangle = getSVGHandle()
							.getTransformsManager().getScaledRectangle(
									renderedRectangle, true);

					// computing the image dimensions in the base coordinates
					Rectangle2D rect = getSVGHandle()
							.getTransformsManager()
							.getScaledRectangle(
									new Rectangle2D.Double(
											0,
											0,
											tempCanvasOffscreenImage.getWidth(),
											tempCanvasOffscreenImage
													.getHeight()), true);
					Point2D basedImageSize = new Point2D.Double(
							rect.getWidth(), rect.getHeight());

					// computing the scrolling values in the base coordinates
					Point2D baseScrollPoint = getSVGHandle()
							.getTransformsManager().getScaledPoint(
									new Point2D.Double(scrollX, scrollY), true);
					double baseScrollX = baseScrollPoint.getX();
					double baseScrollY = baseScrollPoint.getY();

					g2 = GraphicsUtil.createGraphics(tempCanvasOffscreenImage);
					g2.setTransform(af);

					// clearing the image
					g2.setColor(getBackground());

					Rectangle2D svgRectangle = null;

					if (baseScrollY > 0) {

						svgRectangle = new Rectangle2D.Double(baseRectangle
								.getX(), baseRectangle.getY()
								+ basedImageSize.getY() - baseScrollY,
								basedImageSize.getX(), baseScrollY);

					} else if (baseScrollY < 0) {

						svgRectangle = new Rectangle2D.Double(baseRectangle
								.getX(), baseRectangle.getY(), basedImageSize
								.getX(), -baseScrollY);
					}

					if (baseScrollX > 0) {

						svgRectangle = new Rectangle2D.Double(baseRectangle
								.getX()
								+ basedImageSize.getX() - baseScrollX,
								baseRectangle.getY(), baseScrollX,
								basedImageSize.getY());

					} else if (baseScrollX < 0) {

						svgRectangle = new Rectangle2D.Double(baseRectangle
								.getX(), baseRectangle.getY(), -baseScrollX,
								basedImageSize.getY());
					}

					if (svgRectangle != null) {

						g2.clip(svgRectangle);

						// painting the background
						g2.setColor(background);
						g2.fill(svgRectangle);

						setRenderingHints(g2);
					}

				} else {

					// getting the canvas scale
					double scale = zoomManager.getCurrentScale();

					// computing the transform
					AffineTransform af = AffineTransform.getScaleInstance(
							scale, scale);
					af.preConcatenate(AffineTransform.getTranslateInstance(
							-renderedRectangle.x, -renderedRectangle.y));

					// root.setTransform(af);
					g2 = GraphicsUtil.createGraphics(tempCanvasOffscreenImage);
					g2.setTransform(af);
					setRenderingHints(g2);

					if (updateSVGContent) {

						if (dirtyAreas != null) {

							// computing the clip rectangle
							Area clip = null;

							for (Area area : dirtyAreas) {

								if (area != null) {

									if (clip == null) {

										clip = area;

									} else {

										clip.add(area);
									}
								}
							}

							if (clip != null) {

								g2.clip(clip);

								// painting the background
								g2.setColor(background);
								g2.fill(clip);

							} else {

								// clearing the background
								g2.setColor(background);
								g2.fillRect(0, 0, tempCanvasOffscreenImage
										.getWidth(), tempCanvasOffscreenImage
										.getHeight());
							}
						}
					}
				}

				// painting the image,耗时操作
				paintRoot(root, g2);

				Rectangle2D clipBounds = g2.getClipBounds();

				if (g2.getClip() != null) {

					clipBounds = svgHandle.getTransformsManager()
							.getScaledRectangle(clipBounds, false);
				}

				SwingUtilities.invokeLater(new Runnable() {

					public void run() {
						// long t1 = System.nanoTime();

						if (renderedRectangle != null) {

							repaintManager.addDirtyRegion(SVGCanvas.this,
									renderedRectangle.x, renderedRectangle.y,
									renderedRectangle.width,
									renderedRectangle.height);

						} else {

							repaintManager.markCompletelyDirty(SVGCanvas.this);
						}
						// System.err.println("repaintmanager
						// 耗时："+(System.nanoTime()-t1)+"ns");
					}
				});

				g2.dispose();
			}

			canvasOffscreenImage = tempCanvasOffscreenImage;
			tmpRectangle = null;
		}
		System.out.println("一个漫长的刷新..." + ((System.nanoTime() - start))
				/ 1000000 + " ms");
	}

	private void paintRoot(GraphicsNode root, Graphics2D g2) {

		root.primitivePaint(g2);

	}

	/**
	 * sets the rendering hints
	 * 
	 * @param g
	 *            a graphics object
	 */
	protected void setRenderingHints(Graphics2D g) {

		// setting the rendering hints
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
				RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
				RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_DITHERING,
				RenderingHints.VALUE_DITHER_DISABLE);
		g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
				RenderingHints.VALUE_STROKE_NORMALIZE);
	}

	/**
	 * setting the new canvas size
	 * 
	 * @param newSize
	 *            the new size
	 */
	public void setCanvasSize(Point2D newSize) {

		CanvasGraphicsNode canvasGraphicsNode = getCanvasGraphicsNode();

		if (canvasGraphicsNode != null
				&& canvasGraphicsNode.getPositionTransform() == null) {

			canvasGraphicsNode.setPositionTransform(new AffineTransform());
		}

		Element root = document.getDocumentElement();
		String width = FormatStore.format(newSize.getX());
		String height = FormatStore.format(newSize.getY());

		root.setAttribute("width", width);
		root.setAttribute("height", height);
		root.setAttribute("viewBox", "0 0 " + width + " " + height);

		setCanvasPreferredSize(getScaledCanvasSize());

		scrollpane.revalidate();
		scrollpane.refreshRulers();
		setRenderedRectangle(new Rectangle(scrollpane.getViewport()
				.getViewRect()), true, true);
	}

	/**
	 * @return SVGScrollPane the scrollpane that contains the canvas
	 */
	public SVGScrollPane getScrollPane() {
		return scrollpane;
	}

	/**
	 * @return the svg handle linked to this canvas
	 */
	public SVGHandle getSVGHandle() {
		return svgHandle;
	}

	/**
	 * @return the rendering rectangle
	 */
	public Rectangle getRenderedRectangle() {
		return renderedRectangle;
	}

	/**
	 * sets the new rendered rectangle
	 * 
	 * @param rect
	 *            a rendered rectangle
	 * @param reinitialize
	 *            whether the stored information about the rendered picture
	 *            should be erased or not
	 * @param forceReinitialize
	 *            whether the stored information about the rendered picture
	 *            should be erased or not, forcing it if necessary
	 */
	public void setRenderedRectangle(Rectangle rect, boolean reinitialize,
			boolean forceReinitialize) {

		if (forceReinitialize) {

			renderedRectangle = new Rectangle(0, 0, 1, 1);
			tmpRectangle = rect;
			if (svgHandle.getEditor().getSvgSession().getRefreshCountsOnce() == 0)
				refreshCanvasContent(true, false, null);

		} else if (reinitialize) {

			if (rect != null && !renderedRectangle.equals(rect) && reinitialize) {

				renderedRectangle = new Rectangle(0, 0, 1, 1);
				tmpRectangle = rect;
				if (svgHandle.getEditor().getSvgSession()
						.getRefreshCountsOnce() == 0) {
					refreshCanvasContent(true, false, null);
				}
			}

		} else if (rect != null && !renderedRectangle.equals(rect)
				&& renderedRectangle.width > 0 && renderedRectangle.height > 0) {

			tmpRectangle = rect;
			if (svgHandle.getEditor().getSvgSession().getRefreshCountsOnce() == 0)
				refreshCanvasContent(true, false, null);
		}
	}

	/**
	 * requests that the svg content should be repainted
	 */
	public void requestRepaintContent() {
		if (svgHandle.getEditor().getSvgSession().getRefreshCountsOnce() == 0)
			refreshCanvasContent(true, false, null);
	}

	/**
	 * requests that the svg content should be updated
	 * 
	 * @param repaintArea
	 *            the area to repaint
	 */
	public void requestUpdateContent(Area repaintArea) {

		if (repaintArea != null) {

			Set<Area> dirtyAreas = new HashSet<Area>();
			dirtyAreas.add(repaintArea);
			if (svgHandle.getEditor().getSvgSession().getRefreshCountsOnce() == 0)
				refreshCanvasContent(true, true, dirtyAreas);
		}
	}

	/**
	 * @return the project file linked to the canvas
	 */
	public File getProjectFile() {
		return projectFile;
	}

	/**
	 * sets the current cursor
	 * 
	 * @param cursor
	 *            the current cursor
	 */
	public void setSVGCursor(Cursor cursor) {

		if (cursor != null) {

			setCursor(cursor);
		}
	}

	/**
	 * notifies that the parent element has changed
	 */
	public void notifyParentElementChanged() {

		repaintManager.markCompletelyDirty(this);
	}

	/**
	 * @return the canvas' size
	 */
	public Point2D getGeometryCanvasSize() {

		return getGeometryCanvasSize(document);
	}

	/**
	 * returns the size of the svg denoted by the provided document
	 * 
	 * @param aDocument
	 *            a svg document
	 * @return the size of the svg denoted by the provided document
	 */
	public static Point2D getGeometryCanvasSize(Document aDocument) {

		if (aDocument != null) {

			// getting the root element
			Element root = aDocument.getDocumentElement();

			if (root != null) {

				double w = EditorToolkit.getPixelledNumber(root.getAttributeNS(
						null, "width"));
				double h = EditorToolkit.getPixelledNumber(root.getAttributeNS(
						null, "height"));

				return new Point2D.Double(w, h);
			}
		}

		return new Point2D.Double(0, 0);
	}

	private Dimension symbolSize = new Dimension(
			Constants.GRAPH_UNIT_WIDTH_IntValue,
			Constants.GRAPH_UNIT_HEIGHT_IntValue);

	/**
	 * @param root
	 *            the root element
	 * @return the scaled canvas' size
	 */
	public Dimension getScaledCanvasSize(Element root) {
		Dimension scaledSize = new Dimension(0, 0);

		if (root != null) {

			double w = 0, h = 0;

			try {
				// getting the scale factor of the canvas
				double scale = zoomManager.getCurrentScale();

				w = EditorToolkit.getPixelledNumber(root.getAttributeNS(null,
						"width"))
						* scale;
				h = EditorToolkit.getPixelledNumber(root.getAttributeNS(null,
						"height"))
						* scale;
				scaledSize.width = (int) w;
				scaledSize.height = (int) h;
			} catch (Exception ex) {
			}
		}

		return scaledSize;
	}

	/**
	 * @return the scaled canvas' size
	 */
	public Dimension getScaledCanvasSize() {

		Dimension scaledSize = new Dimension(0, 0);

		// gets the root element
		if (document != null) {

			Element root = document.getDocumentElement();

			if (root != null) {

				double w = 0, h = 0;

				try {
					// getting the scale factor of the canvas
					double scale = zoomManager.getCurrentScale();

					w = EditorToolkit.getPixelledNumber(root.getAttributeNS(
							null, "width"))
							* scale;
					h = EditorToolkit.getPixelledNumber(root.getAttributeNS(
							null, "height"))
							* scale;
					scaledSize.width = (int) w;
					scaledSize.height = (int) h;
				} catch (Exception ex) {
				}
			}
		}

		return scaledSize;
	}

	/**
	 * handles the repaint of the canvas
	 * 
	 * @param clipZone
	 *            the clip zone or null to repaint the whole canvas
	 */
	protected void handleRepaint(Rectangle2D clipZone) {

		// getting the repaint zone
		if (clipZone != null) {

			clipZone = new Rectangle2D.Double(clipZone.getX(), clipZone.getY(),
					clipZone.getWidth(), clipZone.getHeight());
		}

		Rectangle2D clip = null;

		if (repaintZone != null) {

			Rectangle2D rZone = new Rectangle2D.Double(repaintZone.getX(),
					repaintZone.getY(), repaintZone.getWidth(), repaintZone
							.getHeight());

			if (clipZone != null) {

				rZone = rZone.createUnion(clipZone);
			}

			rZone = rZone.createIntersection(renderedRectangle);

			double x0 = rZone.getX();
			double y0 = rZone.getY();
			double x1 = rZone.getX() + rZone.getWidth();
			double y1 = rZone.getY() + rZone.getHeight();

			clip = new Rectangle2D.Double(x0 - 2, y0 - 2, x1 - x0 + 5, y1 - y0
					+ 5);
			repaintZone = null;

		} else if (clipZone != null) {

			clipZone = clipZone.createIntersection(renderedRectangle);

			double x0 = clipZone.getX();
			double y0 = clipZone.getY();
			double x1 = clipZone.getX() + clipZone.getWidth();
			double y1 = clipZone.getY() + clipZone.getHeight();

			clip = new Rectangle2D.Double(x0 - 2, y0 - 2, x1 - x0 + 5, y1 - y0
					+ 5);

		}

		// repainting
		if (clip == null) {

			// repaint();
			repaintManager.markCompletelyDirty(this);

		} else {

			repaintManager.addDirtyRegion(this, clip.getBounds().x, clip
					.getBounds().y, clip.getBounds().width,
					clip.getBounds().height);

		}
	}

	/**
	 * adds a grid paint listener
	 * 
	 * @param type
	 *            the integer representing the layer at which the painting
	 *            should be done
	 * @param l
	 *            the grid paint listener to be added
	 * @param makeRepaint
	 *            whether to make a repaint after the paint listener was added
	 *            or not
	 */
	public void addLayerPaintListener(int type, CanvasPainter l,
			boolean makeRepaint) {

		if (l != null) {

			Set<CanvasPainter> set = paintListeners.get(type);

			if (set != null) {

				set.add(l);
			}

			// handle the clips of the painter to compute the new clipping zone

			handleClips(l.getClip(), makeRepaint);
		}
	}

	/**
	 * removes a paint listener
	 * 
	 * @param l
	 *            the paint listener to be removed
	 * @param makeRepaint
	 *            whether to make a repaint after the paint listener was removed
	 *            or not
	 */
	public void removePaintListener(CanvasPainter l, boolean makeRepaint) {

		try {
			paintListeners.get(GRID_LAYER).remove(l);
			paintListeners.get(BOTTOM_LAYER).remove(l);
			paintListeners.get(SELECTION_LAYER).remove(l);
			paintListeners.get(DRAW_LAYER).remove(l);
			paintListeners.get(TERMINAL_LAYER).remove(l);
			paintListeners.get(TOP_LAYER).remove(l);
		} catch (Exception ex) {
		}

		if (l != null) {

			// handle the clips of the painter to compute the new clipping zone
			handleClips(l.getClip(), makeRepaint);
		}
	}

	/**
	 * handles the clipping given the set of clips. 对选中图元的边裁（clip）进行绘制
	 * 
	 * @param clips
	 *            a set of clip rectangles
	 * @param makeRepaint
	 *            whether the repaint action should be done
	 */
	public void handleClips(Set<Rectangle2D> clips, boolean makeRepaint) {

		if (clips != null && clips.size() > 0) {

			if (makeRepaint) {

				for (Rectangle2D clip : clips) {
					// System.out.println(clip);
					handleRepaint(clip);
				}

			} else {

				for (Rectangle2D clip : clips) {

					if (repaintZone != null) {

						repaintZone.add(clip);

					} else {

						repaintZone = new Rectangle2D.Double();
						repaintZone.setRect(clip);
					}
				}
			}

		} else if (makeRepaint) {

			handleRepaint(null);
		}
	}

	/**
	 * notifies the paint listeners when a paint action is done
	 * 
	 * @param g2
	 *            the graphics
	 */
	protected void drawPainters(Graphics2D g2) {

		g2 = (Graphics2D) g2.create();

		// setting the rendering hints
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
				RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
		g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
				RenderingHints.VALUE_COLOR_RENDER_SPEED);
		g2.setRenderingHint(RenderingHints.KEY_DITHERING,
				RenderingHints.VALUE_DITHER_DISABLE);
		g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_SPEED);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
				RenderingHints.VALUE_STROKE_NORMALIZE);

		Set<CanvasPainter> set = null;

		for (int layer : layers) {

			set = paintListeners.get(layer);

			for (CanvasPainter listener : set) {

				listener.paintToBeDone(g2);
			}
		}

		// painting the current parent bounds
		Element parentElement = getSVGHandle().getSelection()
				.getParentElement();
		if (getSVGHandle().getHandleType() == SVGHandle.HANDLE_TYPE_SVG) {
			if (parentElement != null
					&& !parentElement.equals(document.getDocumentElement())) {

				Rectangle2D parentElementBounds = getSVGHandle()
						.getSvgElementsManager().getNodeBounds(parentElement);

				// getting the shape that should be drawn
				Area area = new Area(renderedRectangle);
				try {
					area.subtract(new Area(parentElementBounds));

					// painting the area
					g2.setColor(parentVeilColor);
					g2.fill(area);
					g2.setColor(Color.lightGray);
					g2.draw(parentElementBounds);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		g2.dispose();
	}

	/**
	 * removes all paint listeners
	 */
	public void removeAllPaintListeners() {

		paintListeners.get(GRID_LAYER).clear();
		paintListeners.get(BOTTOM_LAYER).clear();
		paintListeners.get(SELECTION_LAYER).clear();
		paintListeners.get(DRAW_LAYER).clear();
		paintListeners.get(TERMINAL_LAYER).clear();
		paintListeners.get(TOP_LAYER).clear();
	}

	/**
	 * handles the repaint of the canvas
	 * 
	 * @param clipZone
	 *            the clip zone or null the repaint all the canvas
	 */
	public void doRepaint(Rectangle clipZone) {

		handleRepaint(clipZone);
	}

	/**
	 * @return the gvt root
	 */
	public GraphicsNode getGraphicsNode() {
		return getCanvasGraphicsNode();
	}

	/**
	 * @return the canvas graphics node for this canvas
	 */
	private CanvasGraphicsNode getCanvasGraphicsNode() {

		java.util.List<?> children = gvtRoot.getChildren();

		if (children.size() == 0) {

			return null;
		}

		GraphicsNode gn = (GraphicsNode) children.get(0);

		if (!(gn instanceof CanvasGraphicsNode)) {

			return null;
		}

		return (CanvasGraphicsNode) gn;
	}

	public void setBackground(Color bg, boolean store) {
		super.setBackground(bg);
		// background = bg;
		// this.updateUI();
		if (store) {
			PreferencesStore.setPreference(null, BACKGROUND_COLOR_ID, String
					.valueOf(bg.getRGB()));
			this.background = bg;
			scrollpane.getSVGHandle().getSVGFrame().reshape(
					scrollpane.getSVGHandle().getSVGFrame().getX(),
					scrollpane.getSVGHandle().getSVGFrame().getY(),
					scrollpane.getSVGHandle().getSVGFrame().getWidth(),
					scrollpane.getSVGHandle().getSVGFrame().getHeight());
		}
	}

	/**
	 * clears the cache
	 */
	public void clearCache() {

		if (ctx != null) {

			ctx.getDocumentLoader().dispose();
		}
	}

	public void setSvgHandle(SVGHandle handle) {
		this.svgHandle = handle;
	}

	/**
	 * add by yux,2009-1-2 文档启动前加载图元连接信息
	 */
	public void loadLinkInfo() {
		NodeList pathList = document.getDocumentElement().getElementsByTagName(
				"path");
		int length = pathList.getLength();
		for (int i = 0; i < length; i++) {
			if (pathList.item(i) instanceof Element) {
				Element element = (Element) pathList.item(i);
				String id = element.getAttribute("id");
				String p0 = element
						.getAttribute(LinkPointManager.BEGIN_LINE_POINT);
				String p1 = element
						.getAttribute(LinkPointManager.END_LINE_POINT);
				if (p0 != null && p0.length() > 0) {
					String t0 = element
							.getAttribute(LinkPointManager.BEGIN_LINE_TERMINAL);
					lpManager.addLinkPoint(id, p0, t0,
							LinkPointManager.BEGIN_LINE_POINT);
				}

				if (p1 != null && p1.length() > 0) {
					String t1 = element
							.getAttribute(LinkPointManager.END_LINE_TERMINAL);
					lpManager.addLinkPoint(id, p1, t1,
							LinkPointManager.END_LINE_POINT);
				}
			}
		}
	}

	public Element getFirstElement(Element parentElement) {
		Element element = null;

		NodeList nodeList = parentElement.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i) instanceof Element) {
				element = (Element) nodeList.item(i);
				break;
			}
		}
		return element;
	}

	public Point getBeginPoint(Element parentElement) {
		Point point = null;
		if (parentElement.getNodeName().equals("path")) {
			if (parentElement.getAttribute("d").toUpperCase().indexOf("Z") > -1)
				return point;
			String[] str = parentElement.getAttribute("d").split(" ");
			if (str[0].length() > 1) {
				double x = new Double(str[0].replace('M', ' ')
						.replace('m', ' ')).doubleValue();
				double y = 0;
				if (str[1].indexOf('L') > -1 || str[1].indexOf('l') > -1) {
					y = new Double(str[1].substring(0,
							str[1].toLowerCase().indexOf('l'))
							.replace('l', ' ').replace('L', ' ')).doubleValue();
				} else
					y = new Double(str[1]).doubleValue();
				point = new Point((int) x, (int) y);
			} else {
				double x = new Double(str[1]).doubleValue();
				double y = new Double(str[2]).doubleValue();
				point = new Point((int) x, (int) y);
			}
		}
		if (parentElement.getNodeName().indexOf("line") > -1) {
			double x = new Double(parentElement.getAttribute("x1"))
					.doubleValue();
			double y = new Double(parentElement.getAttribute("y1"))
					.doubleValue();
			point = new Point((int) x, (int) y);
		}
		return point;
	}

	public Point getEndPoint(Element parentElement) {
		Point point = null;
		if (parentElement.getNodeName().equals("path")) {
			if (parentElement.getAttribute("d").toUpperCase().indexOf("Z") > -1)
				return point;
			String[] str = parentElement.getAttribute("d").split(" ");
			int nLength = str.length;
			if (nLength < 3)
				return null;
			if (str[nLength - 2].indexOf("L") > -1
					|| str[nLength - 2].indexOf("l") > -1) {
				double x = new Double(
						str[nLength - 2].substring(str[nLength - 2]
								.toLowerCase().indexOf('l') + 1)).doubleValue();
				double y = new Double(str[nLength - 1]).doubleValue();
				point = new Point((int) x, (int) y);
			} else {
				double x = new Double(str[nLength - 2]).doubleValue();
				double y = new Double(str[nLength - 1]).doubleValue();
				point = new Point((int) x, (int) y);
			}
		}
		if (parentElement.getNodeName().indexOf("line") > -1) {
			double x = new Double(parentElement.getAttribute("x2"))
					.doubleValue();
			double y = new Double(parentElement.getAttribute("y2"))
					.doubleValue();
			point = new Point((int) x, (int) y);
		}
		return point;
	}

	public Element getMapInfo(String elementID) {
		return document.getElementById(elementID);
	}

	/**
	 * @return the lpManager
	 */
	public LinkPointManager getLpManager() {
		return lpManager;
	}

	public HashMap<String, ArrayList<String>> getSelectedLayerMap() {
		return allSelectedLayerMap;
	}

	/**
	 * 返回
	 * 
	 * @return the graphFileBean
	 */
	public GraphFileBean getGraphFileBean() {
		return graphFileBean;
	}

	/**
	 * 设置
	 * 
	 * @param graphFileBean
	 *            the graphFileBean to set
	 * @param packFlag
	 *            : 构建属性框标记
	 */

	public void setGraphFileBean(GraphFileBean graphFileBean, boolean packFlag) {
		if (graphFileBean == null) {

		} else {

			this.graphFileBean = graphFileBean;
			if (packFlag) {
				this.fileType = graphFileBean.getBusiType();
				packFilePropertiesDialog();

				unPackGraphFileBean();
			}

		}
	}

	/**
	 * add by yux,2008-12-31 将GraphFileBean数据解压至fileProperties对话框
	 */
	private void unPackGraphFileBean() {
		EditPanel namePanel = (EditPanel) fileProperties.getContentPane()
				.getComponent(0);
		namePanel.getEditText().setText(graphFileBean.getFileName());
		ComboPanel fileType = (ComboPanel) fileProperties.getContentPane()
				.getComponent(1);
		fileType.setSelectValue(graphFileBean.getFileType());

		String desc, type;
		int index = 1;
		for (int i = 0; i < 10; i++) {
			desc = graphFileParamsBean.getBean(i).getDesc();
			if (desc == null || desc.length() == 0)
				continue;
			index++;
			type = graphFileParamsBean.getBean(i).getType();
			if (type == null || type.length() == 0) {
				// 文本输入窗口
				EditPanel edit = (EditPanel) fileProperties.getContentPane()
						.getComponent(index);
				edit.getEditText().setText(graphFileBean.getParams(i));
			} else {
				ComboPanel combo = (ComboPanel) fileProperties.getContentPane()
						.getComponent(index);
				combo.setSelectValue(graphFileBean.getParams(i));
			}
			this.fileType = graphFileBean.getBusiType();
			// packFilePropertiesDialog();

		}
	}

	/**
	 * add by yux,2008-12-31 将fileProperties对话框数据打包 至GraphFileBean中
	 */
	private boolean packGraphFileBean() {
		return false;
	}

	/**
	 * 校验文件属性是否齐全
	 * 
	 * @return:属性合理则返回true，无效则返回false
	 */
	public boolean checkGraphFileBean() {
		String desc = null, nullflag = null;
		for (int i = 0; i < 10; i++) {
			desc = graphFileParamsBean.getBean(i).getDesc();
			if (desc == null || desc.length() == 0)
				continue;

			nullflag = graphFileParamsBean.getBean(i).getNullFlag();

			if (nullflag != null && nullflag.equals("0")) {
				if (graphFileBean.getParams(i) == null
						|| graphFileBean.getParams(i).length() == 0)
					return false;
			}

		}
		if (graphFileBean.getFileName() == null
				|| graphFileBean.getFileName().length() == 0) {
			graphFileBean.setFileName(svgHandle.getSVGFrame().getTitle());
		}
		graphFileBean.setContent(Utilities.printNode(document
				.getDocumentElement(), false));
		graphFileBean.setBusiType(this.fileType);
		graphFileBean.setFileFormat(Constants.FILE_FORMAT_SVG);
		return true;
	}

	/**
	 * 返回
	 * 
	 * @return the fileType
	 */
	public String getFileType() {
		int count = 0;
		while (fileType == null && count < 10) {
			try {
				Thread.sleep(40);
				System.err.println("waiting file type");
				count++;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return fileType;
	}

	/**
	 * add by yux,2009-1-7 拓扑提取
	 * 
	 * @return:拓扑关系列表
	 */
	public ArrayList<TopologyBean> TopologyAnalyse() {
		return lpManager.TopologyAnalyse();
	}

	/**
	 * 返回
	 * 
	 * @return the bLog
	 */
	public boolean isBLog() {
		return bLog;
	}

	/**
	 * 设置
	 * 
	 * @param log
	 *            the bLog to set
	 */
	public void setBLog(boolean log) {
		bLog = log;
	}

	/**
	 * 设置
	 * 
	 * @param create
	 *            the bCreate to set
	 */
	public void setBCreate(boolean create) {
		bCreate = create;
		if (!bCreate && svgHandle.getNFileType() == SVGHandle.HANDLE_TYPE_SVG)
			bLog = true;
	}

	/**
	 * add by yux,2009-1-9 记录操作日志
	 * 
	 * @param id
	 *            ：节点编号
	 * @param element
	 *            ：节点
	 * @param operType
	 *            ：操作类型
	 */
	public void log(String id, Element element, int operType) {
		if (bLog) {

			int index = linkedLogs.indexOf(id);
			switch (operType) {
			case OperLogBean.APPEND_OPER: {
				if (index != -1) {
					OperLogBean existBean = operLogs.get(id);
					if (existBean.getOperType() == OperLogBean.DELETE_OPER) {
						operLogs.remove(id);
						linkedLogs.remove(index);
					}
				} else {
					operLogs.put(id, new OperLogBean(id, element, operType));
					linkedLogs.add(id);
				}
				break;
			}
			case OperLogBean.MODIFY_OPER: {
				if (index != -1) {
				} else {
					operLogs.put(id, new OperLogBean(id, element, operType));
					linkedLogs.add(id);
				}
				break;
			}
			case OperLogBean.DELETE_OPER: {
				if (index != -1) {
					OperLogBean existBean = operLogs.get(id);
					if (existBean.getOperType() == OperLogBean.APPEND_OPER) {
						operLogs.remove(id);
						linkedLogs.remove(index);
					} else if (existBean.getOperType() == OperLogBean.MODIFY_OPER) {
						operLogs.remove(id);
						linkedLogs.remove(index);
						operLogs
								.put(id, new OperLogBean(id, element, operType));
						linkedLogs.add(id);
					}

				} else {
					operLogs.put(id, new OperLogBean(id, element, operType));
					linkedLogs.add(id);
				}
				break;
			}
			}

		}
	}

	/**
	 * add by yux,2009-1-9 增加节点
	 * 
	 * @param id
	 * @param element
	 */
	public void appendElement(String id, Element element) {
		log(id, element, OperLogBean.APPEND_OPER);
	}

	/**
	 * add by yux,2009-1-9 删除节点
	 * 
	 * @param id
	 * @param element
	 */
	public void deleteElement(String id, Element element) {
		log(id, element, OperLogBean.DELETE_OPER);
	}

	/**
	 * add by yux,2009-1-9 修改节点
	 * 
	 * @param id
	 * @param element
	 */
	public void modifyElement(String id, Element element) {
		log(id, element, OperLogBean.MODIFY_OPER);
	}

	/**
	 * add by yux,2009-1-9 修改节点集合
	 * 
	 * @param elements
	 *            :修改的节点集合
	 */
	public void modifyElements(Set<Element> elements) {
		String id = null;
		for (Element element : elements) {
			id = element.getAttribute("id");
			if (id != null && id.length() != 0) {
				log(id, element, OperLogBean.MODIFY_OPER);
			}
		}
		return;

	}

	/**
	 * add by yux,2009-1-9 获取本次操作日志信息
	 * 
	 * @return 注意事项：需要将父子关系分开，如父节点本次被操作了，则需要删除子节点的记录
	 */
	public String getLogs() {
		int size = linkedLogs.size();
		String parentID = null;
		LinkedList<String> saveList = new LinkedList<String>();
		boolean b = false;
		for (int i = 0; i < size; i++) {
			b = false;
			String id = linkedLogs.get(i);
			Element element = document.getElementById(id);
			if (element == null)
				continue;
			Node parentNode = element.getParentNode();
			while (!parentNode.equals(document.getDocumentElement())) {
				parentID = ((Element) parentNode).getAttribute("id");
				parentNode = parentNode.getParentNode();
				if (parentID != null && linkedLogs.indexOf(parentID) != -1) {
					b = true;
					break;
				}
			}
			if (!b) {
				saveList.add(id);
			}
		}
		return null;
	}

	/**
	 * add by yux,2009-1-9 清除本次操作日志
	 */
	public void clearLogs() {
		linkedLogs.clear();
		operLogs.clear();
		return;
	}

	/**
	 * <<<<<<< SVGCanvas.java add by yux,2009-1-20 确认输入的规范短名在本文件已加载的规范队列中
	 * 
	 * ======= add by yux,2009-1-22 根据有效编号和初始化动作，生成初始化
	 * 
	 * @param id
	 *            ：业务编号
	 * @param bean
	 *            ：模型对象
	 * @param element
	 *            ：文档节点
	 */
	private void appendInitRunnable(String id, ModelBean bean, Element element) {
		if (id != null && id.length() > 0
				&& bean.getActions().get(ModelActionBean.TYPE_INITLOAD) != null) {
			HashMap<String, ModelActionBean> map = (HashMap<String, ModelActionBean>) bean
					.getActions().get(ModelActionBean.TYPE_INITLOAD);
			Iterator<ModelActionBean> iterator = map.values().iterator();
			Runnable runnable = null;
			while (iterator.hasNext()) {
				ActionParserModule parser = new ActionParserModule(svgHandle
						.getEditor(), svgHandle, element, iterator.next()
						.getContent());
				runnable = parser.getAction();
				initList.add(runnable);
			}
		}
	}

	/**
	 * add by yux,2009-1-20 确认输入的规范短名在本文件已加载的规范队列中
	 * 
	 * >>>>>>> 1.56
	 * 
	 * @param shortName
	 *            :规范短名
	 * @return:存在则返回true，不存在则返回false
	 */
	public boolean containIndunormType(String shortName) {
		return graphIndunormTypeList.contains(shortName);
	}

	/**
	 * add by yux,2009-1-22 业务初始化，将所有需要初始化的动作按顺序执行一次
	 */
	private void execInitActions() {
		for (Runnable runnable : initList) {
			runnable.run();
		}

		initList.clear();
	}

	/**
	 * 返回
	 * 
	 * @return the canvasOper
	 */
	public CanvasOperAdapter getCanvasOper() {
		return canvasOper;
	}

	private void createCanvasOper() {
		String[][] params = new String[1][2];
		params[0][0] = ActionParams.BUSINESS_TYPE;
		params[0][1] = fileType;
		boolean needDefault = false;
		ResultBean resultBean = getSVGHandle().getEditor().getCommunicator()
				.communicate(
						new CommunicationBean(
								ActionNames.GET_GRAPHBUSINESSTYPE_CANVASOPER,
								params));
		if (resultBean == null
				|| resultBean.getReturnFlag() == ResultBean.RETURN_ERROR) {
			needDefault = true;
		} else {
			String className = (String) resultBean.getReturnObj();
			Object obj = null;
			try {
				Class<?>[] classargs = { EditorAdapter.class, SVGHandle.class };
				Object[] args = { svgHandle.getEditor(), svgHandle };
				obj = Class.forName(className).getConstructor(classargs)
						.newInstance(args);
			} catch (Exception ex) {
				svgHandle.getEditor().getLogger().log(svgHandle.getEditor(),
						LoggerAdapter.ERROR,
						"启动" + className + "类，失败！将使用DefaultCanvasOperImpl");
				svgHandle.getEditor().getLogger().log(svgHandle.getEditor(),
						LoggerAdapter.ERROR, ex);
				needDefault = true;
			}
			if (!needDefault)
				canvasOper = (CanvasOperAdapter) obj;
			else {
				canvasOper = new DefaultCanvasOperImpl(svgHandle.getEditor(),
						svgHandle);
			}
			svgHandle.getEditor().getLogger().log(svgHandle.getEditor(),
					LoggerAdapter.INFO,
					"启用CanvasOper：" + canvasOper.getClass().getName());
		}
	}

	public void initLoad() {
		initLoad(document);
	}

	/**
	 * 初始化加载
	 */
	protected void initLoad(Document doc) {
		if (svgHandle.isSymbolHandle()) {
			String defaultStatus = doc.getDocumentElement().getAttributeNS(
					null, Constants.SYMBOL_DEFAULT_STATUS);
			if (!defaultStatus.equals("")) {
				svgHandle.setSymbolStatus(defaultStatus, false);
			}
		}
		if (svgHandle.getHandleType() == SVGHandle.HANDLE_TYPE_SVG) {
			if (graphIndunormTypeList.size() == 0) {
				// 分析svg图形文件的引用规范
				Element element = document.getDocumentElement();
				NamedNodeMap map = element.getAttributes();
				int size = map.getLength();
				for (int i = 0; i < size; i++) {
					Node node = map.item(i);
					String name = node.getNodeName();
					if (name.indexOf("xmlns:") > -1) {
						name = name.substring(6);
						graphIndunormTypeList.add(name);
					}
				}
			}
			

			if (svgHandle.getEditor().getModeManager().isPropertyPaneCreate()) {
				svgHandle.getEditor().getPropertyModelInteractor()
						.getGraphModel().initTreeModel(svgHandle);
				svgHandle.getEditor().getPropertyModelInteractor()
						.getGraphModel().notifyRightPanel(
								GraphModel.REINIT_MODELTREE, null);

				// 根据模型设定，初始化
				execInitActions();
			}
			svgHandle.getSelection().addBusinessSelection(
					new ShapeInfoLocator(svgHandle));
			svgHandle.getEditor().getHandlesManager().handleCreated();

		}
	}

}
