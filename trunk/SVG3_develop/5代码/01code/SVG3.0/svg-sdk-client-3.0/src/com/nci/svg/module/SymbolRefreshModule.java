package com.nci.svg.module;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.xml.xpath.XPathExpressionException;

import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.dom.svg.SVGOMUseElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.function.ModuleAdapter;
import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.client.util.EditorToolkit;
import com.nci.svg.sdk.client.util.Utilities;
import com.nci.svg.sdk.graphunit.NCIEquipSymbolBean;
import com.nci.svg.sdk.graphunit.SymbolTypeBean;
import com.nci.svg.sdk.other.LinkPointManager;

import fr.itris.glips.svgeditor.actions.popup.PopupItem;
import fr.itris.glips.svgeditor.display.handle.HandlesListener;
import fr.itris.glips.svgeditor.display.handle.HandlesManager;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.resources.ResourcesManager;

/**
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author yx.nci
 * @ʱ�䣺2009-1-5
 * @���ܣ�ͼ����ͼԪ����ˢ��
 * 
 */
public class SymbolRefreshModule extends ModuleAdapter {
	public final static String MODULE_ID = "6cfa746e-e4ff-42eb-8c78-51db73c8780f";
	/**
	 * ģ�ͱ�ʶ
	 */
	protected static final String id = "SymbolRefreshModule";
	/**
	 * �˵�ͼ��
	 */
	protected Icon menuIcon;

	/**
	 * �˵���ǩ
	 */
	protected String menuLabel = "";

	/**
	 * �˵�ѡ��
	 */
	protected JMenuItem optionsMenu;

	private HashMap<String, String> mapSymbol = new HashMap<String, String>();
	private ArrayList<String> listSymbol = new ArrayList<String>();

	public SymbolRefreshModule(EditorAdapter editor) {
		super(editor);
		createMenuItems();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.client.function.ModuleAdapter#getMenuItems()
	 */
	@Override
	public HashMap<String, JMenuItem> getMenuItems() {
		HashMap<String, JMenuItem> map = new HashMap<String, JMenuItem>();
		map.put(id, optionsMenu);

		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.client.function.ModuleAdapter#getPopupItems()
	 */
	@Override
	public Collection<PopupItem> getPopupItems() {
		// TODO Auto-generated method stub
		return super.getPopupItems();
	}

	/**
	 * ����ˢ�¹���˵���
	 */
	protected void createMenuItems() {
		// creating the menu//
		// getting the menu label
		menuLabel = ResourcesManager.bundle.getString(id + "ItemLabel");

		// getting the menu icons
		menuIcon = ResourcesManager.getIcon(id, false);

		// creating the menu
		optionsMenu = new JMenuItem(menuLabel);
		optionsMenu.setIcon(menuIcon);
		// ���Ӵ����¼�
		ActionListener listener = null;
		listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Utilities.executeRunnable(new Runnable() {
					public void run() {
						doAction();
					}
				});
			}
		};
		optionsMenu.addActionListener(listener);
		optionsMenu.setEnabled(false);

		// adding the listener to the switches between the svg handles
		final HandlesManager svgHandleManager = editor.getHandlesManager();

		svgHandleManager.addHandlesListener(new HandlesListener() {

			@Override
			public void handleChanged(SVGHandle currentHandle,
					Set<SVGHandle> handles) {
				if (currentHandle == null) {
					optionsMenu.setEnabled(false);
				} else {
					if (currentHandle.getHandleType() == SVGHandle.HANDLE_TYPE_SYMBOL_GRAPH_UNIT_NORMAL
							|| currentHandle.getHandleType() == SVGHandle.HANDLE_TYPE_SYMBOL_TEMPLATE)
						optionsMenu.setEnabled(false);
					else
						optionsMenu.setEnabled(true);
				}

			}
		});

	}

	/**
	 * ִ��ȫͼͼԪˢ��
	 */
	protected void doAction() {
		mapSymbol.clear();
		listSymbol.clear();
		SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
		if (handle == null)
			return;

		Document doc = handle.getCanvas().getDocument();
		Element defs = (Element) doc.getDocumentElement().getElementsByTagName(
				"defs").item(0);
		if (defs == null)
			return;

		NodeList symbolList = defs.getElementsByTagName("symbol");
		int length = symbolList.getLength();
		String id = null;
		Map<SymbolTypeBean, Map<String, NCIEquipSymbolBean>> symbolMap = editor
				.getSymbolManager().getAllSymbols();
		Map<String, NCIEquipSymbolBean> mapSymbol = new HashMap<String, NCIEquipSymbolBean>();
		Iterator<Map<String, NCIEquipSymbolBean>> it1 = symbolMap.values()
				.iterator();
		while (it1.hasNext()) {
			Iterator<NCIEquipSymbolBean> beanIterator = it1.next().values()
					.iterator();
			while (beanIterator.hasNext()) {
				NCIEquipSymbolBean bean = beanIterator.next();
				mapSymbol.put(bean.getName(), bean);
			}
		}
		Element element = null;
		// �������е�ͼԪ������
		for (int i = 0; i < length; i++) {
			Element e = (Element) symbolList.item(i);
			if (e == null)
				continue;
			id = e.getAttribute("id");
			if (id == null || id.length() == 0)
				continue;

			String[] name = id.split(Constants.SYMBOL_STATUS_SEP);

			String symbolName = name[0];

			if (name.length == 1) {
				element = e;
			} else {
				element = (Element) e.getParentNode();
			}

			defs.removeChild(element);

			NCIEquipSymbolBean bean = mapSymbol.get(symbolName);
			if (bean != null) {
				Document symbolDoc = null;
				try {
					symbolDoc = Utilities.getSVGDocumentByContent(bean);
				} catch (Exception e1) {
					e1.printStackTrace();
					symbolDoc = null;
				}

				if (symbolDoc != null) {
					addGraphUnitElements(handle, doc, defs, symbolDoc, bean
							.getName());

				}
			}

		}
		NodeList list = doc.getDocumentElement().getElementsByTagName("use");
		length = list.getLength();
		Set<Element> sets = new HashSet<Element>();
		for (int i = 0; i < length; i++) {
			SVGOMUseElement useElement = (SVGOMUseElement) list.item(i);
			String baseVal = useElement.getHref().getBaseVal().toString();
			if (baseVal.equals("#terminal")) {
				continue;
			}
			Element parentElement = (Element) useElement.getParentNode();
			Element nextElement = (Element) useElement.getNextSibling();
			parentElement.removeChild(useElement);
			baseVal = baseVal.substring(1);
			// ��ԭ״̬�Ѳ����ڣ����Զ��л��ɵ�ǰͼԪ��Ĭ��״̬
			if (!listSymbol.contains(baseVal)) {
				String str[] = baseVal.split(Constants.SYMBOL_STATUS_SEP);
				element.setAttributeNS(EditorToolkit.xmlnsXLinkNS,
						"xlink:href", "#" + mapSymbol.get(str[0]));
			}
			parentElement.insertBefore(useElement, nextElement);
			sets.add(useElement);
		}
		handle.getCanvas().getLpManager().symbolAction(
				LinkPointManager.SYMBOL_ACTION_MODIFY, sets);
		editor.getSvgSession().refreshCurrentHandleImediately();
		mapSymbol.clear();
		listSymbol.clear();
		editor.getSvgSession().showMessageBox("ͼԪˢ��", "ͼԪˢ�����");
	}

	/**
	 * add by yux,2009.1.5 ������ͨͼԪ�ڵ�
	 * 
	 * @param handle����ǰhandle
	 * @param doc��Ŀ��doc
	 * @param defs:������ڵ�
	 * @param symboldoc��ͼԪԴdoc
	 * @param name��ͼԪ����
	 * @return������href������ʧ���򷵻�null
	 */
	private String addGraphUnitElements(SVGHandle handle, Document doc,
			Element defs, Document symboldoc, String name) {
		String href = null;
		// ��ȡĿ���ĵ��ж�����ڵ㣬û��������
		NodeList gList = null;
		try {
			gList = Utilities.findNodes("//*[@" + Constants.SYMBOL_TYPE + "='"
					+ NCIEquipSymbolBean.SYMBOL_TYPE_GRAPHUNIT + "']",
					symboldoc.getDocumentElement());
		} catch (XPathExpressionException e) {
			gList = null;
		}
		// boolean insertFlag = true;
		// if (gList == null || gList.getLength() == 0)
		// insertFlag = false;

		int length = gList.getLength();
		// �½�һ������������ͨͼԪ����״̬��g�ڵ�
		Element gElement = ((SVGOMDocument) doc).getElementById(name);
		if (gElement == null) {
			gElement = ((SVGOMDocument) doc).createElementNS(doc
					.getDocumentElement().getNamespaceURI(), "g");
			gElement.setAttribute("id", name);
			defs.appendChild(gElement);
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
				listSymbol.add(symbolID);

				if (((SVGOMDocument) doc).getElementById(symbolID) == null) {
					Element symbolElement = doc.createElementNS(doc
							.getDocumentElement().getNamespaceURI(), "symbol");

					symbolElement.setAttribute("viewBox", symboldoc
							.getDocumentElement().getAttribute("viewBox"));
					symbolElement.setAttribute("id", symbolID);
					symbolElement.setAttribute("preserveAspectRatio",
							"xMidYMid meet");
					gElement.appendChild(symbolElement);
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
					appendTerminalElement(doc, symboldoc, symbolElement);
				}
				// ������ʱ����ʾ��״̬Ϊ��״̬������href����
				String visibility = EditorToolkit.getStyleProperty(element,
						"visibility");
				if (visibility == null || visibility.length() == 0) {

					href = symbolID;
					mapSymbol.put(name, href);
				}
			}
		}

		return href;
	}

	/**
	 * add by yux,2008.12.24 ����symboldoc�еĶ˵����ã��������˶˵�ڵ�
	 * 
	 * @param doc��Ŀ��doc
	 * @param symbolDoc��ͼԪԴdoc
	 * @param element��Ŀ��ڵ�
	 */
	private void appendTerminalElement(Document doc, Document symbolDoc,
			Element element) {
		String sWidth = symbolDoc.getDocumentElement().getAttribute("width");
		String sHeight = symbolDoc.getDocumentElement().getAttribute("height");
		double width = Double.parseDouble(sWidth);
		double height = Double.parseDouble(sHeight);
		Element metadata = null;
		try {
			metadata = (Element) Utilities
					.findNode("//*[@nci-type='terminal']", symbolDoc
							.getDocumentElement());
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		// ������Ϊ��
		if (metadata != null) {
			NodeList nodeList = metadata.getElementsByTagName("nci-terminal");
			int len = nodeList.getLength();
			for (int i = 0; i < len; i++) {
				if (nodeList.item(i) instanceof Element) {
					Element tElement = (Element) nodeList.item(i);
					String text = tElement.getAttribute("terminal-note");
					int x = 0, y = 0;
					if (text.equals(TerminalModule.CENTER_POINT)) {
						appendTerminalElement(doc, element, (int) (width / 2),
								(int) (height / 2), "c");
					} else if (text.equals(TerminalModule.FOURWAY_POINT)) {
						appendTerminalElement(doc, element, 0,
								(int) (height / 2), "w");
						appendTerminalElement(doc, element, width,
								(int) (height / 2), "e");
						appendTerminalElement(doc, element, (int) (width / 2),
								0, "n");
						appendTerminalElement(doc, element, (int) (width / 2),
								height, "s");
					} else if (text.equals(TerminalModule.LEFTANDRIGHT_POINT)) {
						appendTerminalElement(doc, element, 0,
								(int) (height / 2), "w");
						appendTerminalElement(doc, element, width,
								(int) (height / 2), "e");
					} else if (text.equals(TerminalModule.UPANDDOWN_POINT)) {
						appendTerminalElement(doc, element, (int) (width / 2),
								0, "n");
						appendTerminalElement(doc, element, (int) (width / 2),
								height, "s");
					} else if (text
							.equals(TerminalModule.INCLINE_FOURWAY_POINT)) {
						appendTerminalElement(doc, element, 0, 0, "wn");
						appendTerminalElement(doc, element, 0, height, "ws");
						appendTerminalElement(doc, element, width, 0, "en");
						appendTerminalElement(doc, element, width, height, "es");
					}

				}
			}

		}
	}

	/**
	 * add by yux,2008.12.24 ���ݴ����ֵ���������˶˵�
	 * 
	 * @param doc��Ŀ��doc
	 * @param element��Ŀ��ڵ�
	 * @param x��x����
	 * @param y��y����
	 */
	private void appendTerminalElement(Document doc, Element element, double x,
			double y, String name) {
		Element useElement = ((SVGOMDocument) doc).createElementNS(doc
				.getDocumentElement().getNamespaceURI(), "use");

		element.appendChild(useElement);

		EditorToolkit.setAttributeValue(useElement, "x", x);
		EditorToolkit.setAttributeValue(useElement, "y", y);
		EditorToolkit.setAttributeValue(useElement, "width", 2);
		EditorToolkit.setAttributeValue(useElement, "height", 2);
		useElement.setAttributeNS(EditorToolkit.xmlnsXLinkNS, "xlink:href",
				"#terminal");
		useElement.setAttribute("name", name);
	}

}
