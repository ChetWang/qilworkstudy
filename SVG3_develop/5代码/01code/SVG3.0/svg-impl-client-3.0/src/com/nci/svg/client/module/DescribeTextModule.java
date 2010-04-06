package com.nci.svg.client.module;

import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.function.ModuleAdapter;
import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.client.util.EditorToolkit;
import com.nci.svg.sdk.client.util.Utilities;
import com.nci.svg.sdk.module.ModuleStopedException;

import fr.itris.glips.library.Toolkit;
import fr.itris.glips.svgeditor.ColorManager;
import fr.itris.glips.svgeditor.actions.popup.PopupItem;
import fr.itris.glips.svgeditor.actions.popup.PopupSubMenu;
import fr.itris.glips.svgeditor.display.canvas.dom.SVGDOMNormalizer;
import fr.itris.glips.svgeditor.display.handle.HandlesListener;
import fr.itris.glips.svgeditor.display.handle.HandlesManager;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.display.selection.SelectionChangedListener;
import fr.itris.glips.svgeditor.display.undoredo.UndoRedoAction;
import fr.itris.glips.svgeditor.display.undoredo.UndoRedoActionList;
import fr.itris.glips.svgeditor.resources.ResourcesManager;
import fr.itris.glips.svgeditor.shape.ShapeToolkit;
import fr.itris.glips.svgeditor.shape.text.TextDialog;

/**
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author yx.nci
 * @时间：2008-12-8
 * @功能：描述功能类 描述的格式 <g id="g_节点id" type=""> <element id="节点id"/>需要描述的节点 <text
 *           id="节点id_describe"/> </g>
 * 
 */
public class DescribeTextModule extends ModuleAdapter {
	/**
	 * 模型标识
	 */
	protected static final String id = "DescribeTextModule";
	/**
	 * 菜单图标
	 */
	protected Icon menuIcon;

	/**
	 * 菜单标签
	 */
	protected String menuLabel = "";

	/**
	 * 菜单选项
	 */
	protected JMenu optionsMenu;

	/**
	 * 各菜单选项标识
	 */
	protected String[] ids = { "AppendDescribeTextModule",
			"RemoveDescribeTextModule" };

	/**
	 * the icons for the modes
	 */
	protected Icon[] optionsIcons, optionsDisabledIcons;

	/**
	 * the label for the menu and tool item
	 */
	protected String[] optionsItemLabels;

	/**
	 * the menu items that are displayed in the menu bar for the options
	 */
	protected JMenuItem[] optionsMenuItems;
	/**
	 * the dialog used to select a text
	 */
	private TextDialog textDialog;

	/**
	 * 描述组合g节点类型
	 */
	private String gType = "g_describe";
	/**
	 * 当前操作的节点
	 */
	private Element curElement = null;

	public DescribeTextModule(EditorAdapter editor) {
		super(editor);
		Utilities.executeRunnable(new Runnable() {
			public void run() {
				initModule();
			}
		});
	}

	private void initModule() {
		createMenuAndToolItems();
		if (editor.getParent() instanceof JDialog) {

			textDialog = new TextDialog(this, (JDialog) editor.getParent(),
					editor);

		} else if (editor.getParent() instanceof Frame) {

			textDialog = new TextDialog(this, (Frame) editor.getParent(),
					editor);
		}
	}

	@Override
	public HashMap<String, JMenuItem> getMenuItems() {
		HashMap<String, JMenuItem> map = new HashMap<String, JMenuItem>();
		map.put(id, optionsMenu);

		return map;
	}

	@Override
	public Collection<PopupItem> getPopupItems() {
		LinkedList<PopupItem> popupItems = new LinkedList<PopupItem>();
		String iconName = null;
		// 创建描述菜单
		PopupSubMenu subMenu = new PopupSubMenu(editor, id, menuLabel, id);
		// 创建线操作下所有的二级菜单
		for (int i = 0; i < ids.length; i++) {
			final int index = i;
			PopupItem item = new PopupItem(editor, ids[i],
					ResourcesManager.bundle.getString(ids[i] + "ItemLabel"),
					iconName) {
				@Override
				public JMenuItem getPopupItem(LinkedList<Element> nodes) {
					menuItem.setEnabled(false);
					if (nodes.size() == 1) {// 选中只有一个节点
						Set<Element> sets = editor.getHandlesManager()
								.getCurrentHandle().getSelection()
								.getSelectedElements();
						final Element element = (Element) sets.iterator()
								.next();
						String id = element.getAttribute("id");
						String type = element.getAttribute("type");
						if (type != null && type.equals(gType)
								&& element.getNodeName().equals("g")) {
							if (index == 1) {
								menuItem
										.addActionListener(new ActionListener() {
											public void actionPerformed(
													ActionEvent e) {
												doAction(editor
														.getHandlesManager()
														.getCurrentHandle(),
														element, index);
											}
										});
								menuItem.setEnabled(true);
							}

						} else {
							if (index == 0) {
								menuItem
										.addActionListener(new ActionListener() {
											public void actionPerformed(
													ActionEvent e) {
												doAction(editor
														.getHandlesManager()
														.getCurrentHandle(),
														element, index);
											}
										});
								menuItem.setEnabled(true);
							}
						}
					}
					return super.getPopupItem(nodes);
				}
			};
			subMenu.addPopupItem(item);
		}

		popupItems.add(subMenu);
		return popupItems;
	}

	/**
	 * 创建菜单项
	 */
	protected void createMenuAndToolItems() {
		// creating the menu//
		// getting the menu label
		menuLabel = ResourcesManager.bundle.getString(id + "ItemLabel");

		// getting the menu icons
		menuIcon = ResourcesManager.getIcon(id, false);

		// creating the menu
		optionsMenu = new JMenu(menuLabel);
		optionsMenu.setIcon(menuIcon);

		// creating the options item//

		// getting the resources for the items
		optionsItemLabels = new String[ids.length];
		optionsIcons = new Icon[ids.length];
		optionsDisabledIcons = new Icon[ids.length];
		optionsMenuItems = new JMenuItem[ids.length];
		ActionListener listener = null;
		ButtonGroup bgMenu = new ButtonGroup();
		ButtonGroup bgTool = new ButtonGroup();
		for (int i = 0; i < ids.length; i++) {
			// getting the labels
			final int index = i;
			optionsItemLabels[i] = ResourcesManager.bundle.getString(ids[i]
					+ "ItemLabel");

			// getting the icons
			optionsIcons[i] = ResourcesManager.getIcon(ids[i], false);
			optionsDisabledIcons[i] = ResourcesManager.getIcon(ids[i], true);

			// creating the menu items
			optionsMenuItems[i] = new JMenuItem(optionsItemLabels[i]);
			optionsMenuItems[i].setIcon(optionsIcons[i]);
			optionsMenuItems[i].setDisabledIcon(optionsDisabledIcons[i]);
			optionsMenuItems[i].setEnabled(false);

			// creating the listener
			listener = new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					Set<Element> sets = editor.getHandlesManager()
							.getCurrentHandle().getSelection()
							.getSelectedElements();
					Element element = sets.iterator().next();
					doAction(editor.getHandlesManager().getCurrentHandle(),
							element, index);
				}
			};

			optionsMenuItems[i].addActionListener(listener);

			// adding to the menu
			optionsMenu.add(optionsMenuItems[i]);
			bgMenu.add(optionsMenuItems[i]);
		}

		// adding the listener to the switches between the svg handles
		final HandlesManager svgHandleManager = editor.getHandlesManager();

		svgHandleManager.addHandlesListener(new HandlesListener() {

			@Override
			public void handleChanged(SVGHandle currentHandle,
					Set<SVGHandle> handles) {

				if (currentHandle != null) {
					if (currentHandle.getSelection().getDescribeTextListener() != null)
						currentHandle.getSelection()
								.removeSelectionChangedListener(
										currentHandle.getSelection()
												.getDescribeTextListener());
					SelectionChangedListener listener = new SelectionChangedListener() {

						@Override
						public void selectionChanged(
								Set<Element> selectedElements) {
							resetMenuItemsStatus(selectedElements);
						}
					};
					currentHandle.getSelection().addSelectionChangedListener(
							listener);
					currentHandle.getSelection().setDescribeTextListener(
							listener);

					Set<Element> sets = currentHandle.getSelection()
							.getSelectedElements();
					resetMenuItemsStatus(sets);
				}
			}
		});

	}

	/**
	 * 根据传入的svghandle、节点和操作类型进行描述的新增和删除操作
	 * 
	 * @param handle:svghandle，当前操作的
	 * @param element；需要维护的节点
	 * @param index:操作类型
	 */
	public void doAction(SVGHandle handle, Element element, int index) {

		if (handle == null || element == null)
			return;

		if (index == 0) {
			// 新增描述
			appendDescribeText(handle, element);
		} else if (index == 1) {
			// 删除描述
			removeDescribeText(handle, element);
		} else
			return;
	}

	/**
	 * 新增文字描述
	 * 
	 * @param handle:待操作的svghandle
	 * @param element；节点
	 */
	protected void appendDescribeText(SVGHandle handle, Element element) {
		curElement = element;
		handle.getSelection().setBTextDialog(true);
		textDialog.showDialog(handle.getSVGFrame(), handle);
		if (!editor.getRemanentModeManager().isRemanentMode()) {

			editor.getSelectionManager().setToRegularMode();
		}
		editor.getSvgSession().refreshCurrentHandleImediately();
	}

	protected void removeDescribeText(SVGHandle handle, Element gElement) {
		if (handle == null || gElement == null)
			return;

		String type = gElement.getAttribute("type");
		String id = gElement.getAttribute("id");
		if (type != null && type.equals("g_describe")
				&& gElement.getNodeName().equals("g")) {
			id = id.substring(2);
			id += "_describe";
			final String describeID = id;
			// getting the current handle
			final SVGHandle currentHandle = editor.getHandlesManager()
					.getCurrentHandle();

			// getting the selected elements
			final Set<Element> selectedElements = currentHandle.getSelection()
					.getSelectedElements();

			// getting the parent node
			final Element parentNode = currentHandle.getSelection()
					.getParentElement();

			// creating the set of the elements that will be modified
			Set<Element> elements = new HashSet<Element>(selectedElements);

			// creating the list of the child nodes of the parent element,
			// in the order in which they are found in the document
			final LinkedList<Element> orderedElements = Toolkit
					.getChildrenElements(parentNode);

			final Map<Element, Element> groupNodesSuccessor = new HashMap<Element, Element>();

			for (Element element : orderedElements) {

				groupNodesSuccessor.put(element, EditorToolkit
						.getNextElementSibling(element));
			}

			// creating the map associating a group node to its child nodes
			final Map<Element, LinkedList<Element>> groupNodes = new HashMap<Element, LinkedList<Element>>();
			LinkedList<Element> childNodesList = null;
			NodeList groupChildNodes = null;
			Node node = null;

			final Map<Element, Element> mapMetadata = new HashMap<Element, Element>();

			for (Element groupElement : selectedElements) {

				// getting the child nodes
				elements.add(groupElement);
				groupChildNodes = groupElement.getChildNodes();
				childNodesList = new LinkedList<Element>();
				groupNodes.put(groupElement, childNodesList);

				// filling the set of the child nodes
				for (int i = 0; i < groupChildNodes.getLength(); i++) {

					node = groupChildNodes.item(i);

					if (node != null && node instanceof Element) {

						if (!node.getNodeName().equals(
								Constants.NCI_SVG_METADATA)) {
							childNodesList.add((Element) node);
							elements.add((Element) node);
						}
					}
				}
				NodeList mdList = groupElement
						.getElementsByTagName(Constants.NCI_SVG_METADATA);

				for (int i = 0; i < mdList.getLength(); i++) {
					if (mdList.item(i).getParentNode() == groupElement) {
						mapMetadata.put(groupElement, (Element) mdList.item(i));
						break;
					}
				}

			}

			// creating the execute runnable
			Runnable executeRunnable = new Runnable() {

				public void run() {

					// for each group node
					LinkedList<Element> groupChildNodesList = null;

					for (Element groupElement : groupNodes.keySet()) {

						// getting the list of the child nodes of the group
						// element
						groupChildNodesList = groupNodes.get(groupElement);
						Element metadata = mapMetadata.get(groupElement);

						for (Element childElement : groupChildNodesList) {

							// appending the child element of the group node to
							// the
							// parent node
							String childid = childElement.getAttribute("id");
							if (childElement != metadata
									&& (childid != null && !childid
											.equals(describeID)))
								parentNode.insertBefore(childElement,
										groupElement);
						}

						// removing the group element
						if (metadata != null)
							groupElement.removeChild(metadata);
						parentNode.removeChild(groupElement);
					}

					currentHandle.getSelection().clearSelection();
				}
			};

			// creating the undo runnable
			Runnable undoRunnable = new Runnable() {

				public void run() {

					// for each group node
					LinkedList<Element> groupChildNodesList = null;

					for (Element groupElement : groupNodes.keySet()) {

						// getting the list of the child nodes of the group
						// element
						groupChildNodesList = groupNodes.get(groupElement);
						Element metadata = mapMetadata.get(groupElement);

						for (Element childElement : groupChildNodesList) {

							// appending the child element of the group node to
							// the
							// parent node
							String childid = childElement.getAttribute("id");
							if (childid != null && !childid.equals(describeID))
								parentNode.removeChild(childElement);
							groupElement.appendChild(childElement);
						}
						if (metadata != null)
							groupElement.appendChild(metadata);

						// appending the group element to the parent node
						Element nextSibling = groupNodesSuccessor
								.get(groupElement);
						parentNode.insertBefore(groupElement, nextSibling);
					}
				}
			};

			// creating the undo/redo action
			ShapeToolkit.addUndoRedoAction(currentHandle, optionsItemLabels[1],
					executeRunnable, undoRunnable, elements);

		}

	}

	/**
	 * 根据传入的图形节点集重置菜单项状态
	 * 
	 * @param elements:当前选中的图形节点集
	 */
	public void resetMenuItemsStatus(Set<Element> elements) {
		for (int i = 0; i < optionsMenuItems.length; i++) {

			optionsMenuItems[i].setEnabled(false);
		}
		if (elements == null || elements.size() != 1) {

		} else {
			Element element = (Element) elements.iterator().next();
			String id = element.getAttribute("id");
			String type = element.getAttribute("type");
			if (type != null && type.equals("g_describe")
					&& element.getNodeName().equals("g")) {
				optionsMenuItems[0].setEnabled(false);
				optionsMenuItems[1].setEnabled(true);
			} else {
				if (id == null || id.length() == 0) {
					for (int i = 0; i < optionsMenuItems.length; i++) {

						optionsMenuItems[i].setEnabled(false);
					}
				} else {
					String expr = "//*[@id='" + id + "_describe']";
					Node node = null;
					try {
						node = Utilities.findNode(expr, element
								.getOwnerDocument().getDocumentElement());

					} catch (XPathExpressionException e) {

						node = null;
					}
					if (node != null) {
						optionsMenuItems[0].setEnabled(false);
						optionsMenuItems[1].setEnabled(true);
					} else {
						optionsMenuItems[0].setEnabled(true);
						optionsMenuItems[1].setEnabled(false);
					}
				}
			}
		}
	}

	// @Override
	// public Object handleOper(int index, Object obj)
	// throws ModuleStopedException {
	// if (index == 0) {
	// // 新增描述节点
	// String text = (String) obj;
	// Element element = createElement(editor.getHandlesManager()
	// .getCurrentHandle(), text);
	// }
	// return super.handleOper(index, obj);
	// }

	@Override
	public ResultBean handleOper(String action, Map params) {
		if (action == "0") {
			// 新增描述节点
			String text = (String) params.get("text");
			Element element = createElement(editor.getHandlesManager()
					.getCurrentHandle(), text);
		}
		return super.handleOper(action, params);
	}

	/**
	 * 新增描述节点 将原节点和描述节点组合成一个g节点，
	 * 
	 * @param handle
	 * @param text
	 * @return
	 */
	public Element createElement(SVGHandle handle, String text) {
		// the edited document
		Document doc = handle.getScrollPane().getSVGCanvas().getDocument();
		String handledElementTagName = "text";
		String xAtt = "x", yAtt = "y";
		Point drawingPoint = new Point();
		Rectangle2D bounds = null;
		bounds = handle.getSvgElementsManager().getSensitiveBounds(curElement);
		drawingPoint.setLocation(bounds.getMinX(), bounds.getMaxY() + 5);
		// creating the text element
		final Element element = doc.createElementNS(doc.getDocumentElement()
				.getNamespaceURI(), handledElementTagName);
		element.setAttribute("id", curElement.getAttribute("id") + "_describe");

		// getting the last color that has been used by the user
		String colorString = editor.getColorChooser().getColorString(
				ColorManager.getCurrentColor());
		// element.setAttributeNS(null,"fill", colorString);
		// element.setAttributeNS(null, "style", "fill:" + colorString
		// + ";stroke:none;");
		element.setAttributeNS(null, "style", "font-size:48pt;fill:"
				+ colorString + ";");

		EditorToolkit.setAttributeValue(element, xAtt, drawingPoint.getX());
		EditorToolkit.setAttributeValue(element, yAtt, drawingPoint.getY());

		String[] s = text.split("\n");
		// creating the text node
		if (s.length == 1) {
			Text textValue = doc.createTextNode(text);
			element.appendChild(textValue);
		} else {
			int startY = (int) drawingPoint.getY();
			for (int i = 0; i < s.length; i++) {
				Element tspan = doc.createElementNS(doc.getDocumentElement()
						.getNamespaceURI(), SVGDOMNormalizer.tspanTagName);

				tspan.setAttribute("x", String.valueOf(drawingPoint.getX()));
				tspan.setAttribute("y", String.valueOf(startY));
				Text textValue = doc.createTextNode(s[i]);
				tspan.appendChild(textValue);
				element.appendChild(tspan);
				startY = startY + 17;
			}
		}

		Element gElement = doc.createElementNS(doc.getDocumentElement()
				.getNamespaceURI(), "g");
		gElement.setAttribute("id", "g_" + curElement.getAttribute("id"));
		gElement.setAttribute("type", gType);
		Element parentElement = (Element) curElement.getParentNode();
		parentElement.appendChild(gElement);
		gElement.appendChild(curElement);
		gElement.appendChild(element);

		handle.getSelection().handleSelection(gElement, false, false);
		// insertShapeElement(handle, gElement, curElement);
		return gElement;
	}

	/**
	 * 插入节点
	 * 
	 * @param handle
	 * @param shapeElement
	 * @param childElement
	 */
	protected void insertShapeElement(final SVGHandle handle,
			final Element shapeElement, final Element childElement) {

		if (shapeElement != null) {

			// getting the current parent element of all the edited nodes
			final Element parentElement = handle.getSelection()
					.getParentElement();

			// the execute runnable
			Runnable executeRunnable = new Runnable() {

				public void run() {

					parentElement.appendChild(shapeElement);
					handle.getSelection().clearSelection();
					handle.getSelection().handleSelection(shapeElement, false,
							false);

				}
			};

			// the undo runnable
			Runnable undoRunnable = new Runnable() {

				public void run() {

					parentElement.insertBefore(childElement, shapeElement);
					parentElement.removeChild(shapeElement);

				}
			};

			// executing the action and creating the undo/redo action
			HashSet<Element> elements = new HashSet<Element>();
			elements.add(shapeElement);
			UndoRedoAction undoRedoAction = ShapeToolkit.getUndoRedoAction(
					optionsItemLabels[0], executeRunnable, undoRunnable,
					elements);

			UndoRedoActionList actionlist = new UndoRedoActionList(
					optionsItemLabels[0], false);
			actionlist.add(undoRedoAction);
			handle.getUndoRedo().addActionList(actionlist, false);
		}
	}

}
