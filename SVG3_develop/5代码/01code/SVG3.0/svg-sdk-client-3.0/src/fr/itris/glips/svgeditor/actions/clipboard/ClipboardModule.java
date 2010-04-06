/*
 * Created on 15 avr. 2004
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
package fr.itris.glips.svgeditor.actions.clipboard;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.action.ElementDeleteListener;
import com.nci.svg.sdk.client.function.ModuleAdapter;
import com.nci.svg.sdk.client.util.EditorToolkit;
import com.nci.svg.sdk.client.util.Utilities;
import com.nci.svg.sdk.other.ComputeID;
import com.nci.svg.sdk.shape.ElementDeleteFilter;
import com.nci.svg.sdk.shape.GraphUnitImageShape;

import fr.itris.glips.library.Toolkit;
import fr.itris.glips.library.geom.path.Path;
import fr.itris.glips.library.geom.path.segment.Segment;
import fr.itris.glips.svgeditor.NodeIterator;
import fr.itris.glips.svgeditor.actions.popup.PopupItem;
import fr.itris.glips.svgeditor.display.handle.HandlesListener;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.display.selection.SelectionChangedListener;
import fr.itris.glips.svgeditor.display.undoredo.UndoRedoAction;
import fr.itris.glips.svgeditor.display.undoredo.UndoRedoActionList;
import fr.itris.glips.svgeditor.resources.ResourcesManager;
import fr.itris.glips.svgeditor.shape.AbstractShape;
import fr.itris.glips.svgeditor.shape.ShapeToolkit;

/**
 * @author ITRIS, Jordi SUC the class managing all the copy, paste, cut, delete
 *         actions
 */
public class ClipboardModule extends ModuleAdapter {

	/**
	 * the ids of the module
	 */
	private static final String idClipboard = "Clipboard", idCopy = "Copy",
			idPaste = "Paste", idPastePos = "PastePos", idCut = "Cut",
			idDelete = "Delete";

	/**
	 * the labels
	 */
	private String copyLabel = "", pasteLabel = "", pastePosLabel = "",
			cutLabel = "", deleteLabel = "";

	/**
	 * the undo/redo labels
	 */
	private String undoRedoPasteLabel = "", undoRedoCutLabel = "",
			undoRedoDeleteLabel = "";

	/**
	 * the menu items that will be added to the menubar
	 */
	private JMenuItem copyMenuItem, pasteMenuItem, pastePosMenuItem,
			cutMenuItem, deleteMenuItem;

	/**
	 * the listener the the copy, paste, cut and delete menu items
	 */
	private ActionListener copyListener, pasteListener, pastePosListener,
			cutListener, deleteListener;

	/**
	 * the clipboard manager
	 */
	private ClipboardManager clipboardManager;

	/**
	 * the nodes that are currently selected
	 */
	private final Set<Element> selectedNodes = new HashSet<Element>();

	/**
	 * defs复制历史
	 */
	private ArrayList<String> listHis = new ArrayList<String>();

	/**
	 * defs复制历史纪录中所使用的分隔符号
	 */
	private String separator = "-@$nci$@-";

	private Vector<ElementDeleteListener> deletedListeners = new Vector<ElementDeleteListener>();

	public static final String MODULE_ID = "11d56122-619f-46fc-b06a-24d8bf5ff441";
	
	protected ElementDeleteFilter deleteFilter;
	
	/**
	 * the constructor of the class
	 * 
	 * @param editor
	 *            the editor
	 */
	public ClipboardModule(final EditorAdapter editor) {
		super(editor);
		moduleUUID = MODULE_ID;
		Utilities.executeRunnable(new Runnable() {
			public void run() {
				initClipBoard();
			}
		});
	}

	private void initClipBoard() {
		this.clipboardManager = editor.getClipboardManager();

		// gets the labels from the resources
		ResourceBundle bundle = ResourcesManager.bundle;
		deleteFilter = new ElementDeleteFilter(){
			@Override
			public boolean filterElement(Element shapeElement,List<Element> elementsToDelete) {
				return true;
			}			
		};
		copyLabel = bundle.getString("Copy");
		pasteLabel = bundle.getString("Paste");
		pastePosLabel = bundle.getString("PastePos");
		cutLabel = bundle.getString("Cut");
		deleteLabel = bundle.getString("Delete");
		undoRedoPasteLabel = bundle.getString("UndoRedoPaste");
		undoRedoCutLabel = bundle.getString("UndoRedoCut");
		undoRedoDeleteLabel = bundle.getString("UndoRedoDelete");

		// a listener that listens to the changes of the svg handles
		final HandlesListener svgHandleListener = new HandlesListener() {

			/**
			 * a listener on the selection changes
			 */
			private SelectionChangedListener selectionListener;

			/**
			 * the last handle
			 */
			private SVGHandle lastHandle = null;

			@Override
			public void handleChanged(final SVGHandle currentHandle,
					Set<SVGHandle> handles) {

				if (lastHandle != null && selectionListener != null
						&& lastHandle.getSelection() != null) {

					// if a selection listener is already registered on a
					// selection module, it is removed
					lastHandle.getSelection().removeSelectionChangedListener(
							selectionListener);
					selectionListener = null;
				}

				// clearing the selected nodes
				selectedNodes.clear();

				// disables the menuitems
				copyMenuItem.setEnabled(false);
				cutMenuItem.setEnabled(false);
				deleteMenuItem.setEnabled(false);
				pasteMenuItem.setEnabled(false);
				pastePosMenuItem.setEnabled(false);
				selectionListener = null;

				if (currentHandle != null) {

					if (clipboardManager.getClipboardContent().size() > 0) {

						pasteMenuItem.setEnabled(true);
						pastePosMenuItem.setEnabled(true);
					}

					manageSelection(currentHandle, new HashSet<Element>(
							currentHandle.getSelection().getSelectedElements()));

					// the listener of the selection changes
					selectionListener = new SelectionChangedListener() {

						@Override
						public void selectionChanged(
								Set<Element> selectedElements) {

							manageSelection(currentHandle, selectedElements);
						}
					};

					// adds the selection listener
					if (selectionListener != null) {

						currentHandle.getSelection()
								.addSelectionChangedListener(selectionListener);
					}
				}

				lastHandle = currentHandle;
			}

			/**
			 * updates the selected items and the state of the menu items
			 * 
			 * @param handle
			 *            the current handle
			 * @param selectedElements
			 *            the currently selected elements
			 */
			protected void manageSelection(SVGHandle handle,
					Set<Element> selectedElements) {

				// disabling the menuitems
				copyMenuItem.setEnabled(false);
				cutMenuItem.setEnabled(false);
				deleteMenuItem.setEnabled(false);

				selectedNodes.clear();

				// refreshing the selected nodes list
				if (selectedElements != null) {

					selectedNodes.addAll(selectedElements);
				}

				if (selectedNodes.size() > 0) {

					copyMenuItem.setEnabled(true);

					if (!handle.getSelection().isSelectionLocked()) {

						cutMenuItem.setEnabled(true);
						deleteMenuItem.setEnabled(true);
					}
				}
			}
		};

		// adds the svg handles change listener
		editor.getHandlesManager().addHandlesListener(svgHandleListener);

		// getting the icons
		ImageIcon copyIcon = ResourcesManager.getIcon("Copy", false);
		ImageIcon dcopyIcon = ResourcesManager.getIcon("Copy", true);
		ImageIcon pasteIcon = ResourcesManager.getIcon("Paste", false);
		ImageIcon dpasteIcon = ResourcesManager.getIcon("Paste", true);
		ImageIcon pastePosIcon = ResourcesManager.getIcon("PastePos", false);
		ImageIcon dpastePosIcon = ResourcesManager.getIcon("PastePos", true);
		ImageIcon cutIcon = ResourcesManager.getIcon("Cut", false);
		ImageIcon dcutIcon = ResourcesManager.getIcon("Cut", true);
		ImageIcon deleteIcon = ResourcesManager.getIcon("Delete", false);
		ImageIcon ddeleteIcon = ResourcesManager.getIcon("Delete", true);

		// initializing the menuitems, the popup items and adds the listeners on
		// them//

		copyMenuItem = new JMenuItem(copyLabel, copyIcon);
		copyMenuItem.setDisabledIcon(dcopyIcon);
		copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
				java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(),
				false));
		copyMenuItem.setEnabled(false);
		copyListener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (e.getModifiers() == InputEvent.BUTTON1_DOWN_MASK
						|| e.getModifiers() == InputEvent.BUTTON1_MASK
						|| editor.getHandlesManager().keyStrokeActsOnSVGFrame()) {

					copy();
					pasteMenuItem.setEnabled(true);
					pastePosMenuItem.setEnabled(true);
				}
			}
		};

		copyMenuItem.addActionListener(copyListener);

		pasteMenuItem = new JMenuItem(pasteLabel, pasteIcon);
		pasteMenuItem.setDisabledIcon(dpasteIcon);
		pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
				java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(),
				false));
		pasteMenuItem.setEnabled(false);

		pasteListener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (e.getModifiers() == InputEvent.BUTTON1_DOWN_MASK
						|| e.getModifiers() == InputEvent.BUTTON1_MASK
						|| editor.getHandlesManager().keyStrokeActsOnSVGFrame()) {

					paste(
							false,
							e.getModifiers() != InputEvent.BUTTON1_DOWN_MASK
									&& e.getModifiers() != InputEvent.BUTTON1_MASK,
							new Point(0, 0));
				}
			}
		};

		pasteMenuItem.addActionListener(pasteListener);

		pastePosMenuItem = new JMenuItem(pastePosLabel, pastePosIcon);
		pastePosMenuItem.setDisabledIcon(dpastePosIcon);
		pastePosMenuItem.setEnabled(false);

		pastePosListener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (e.getModifiers() == InputEvent.BUTTON1_DOWN_MASK
						|| e.getModifiers() == InputEvent.BUTTON1_MASK
						|| editor.getHandlesManager().keyStrokeActsOnSVGFrame()) {

					paste(true, false, new Point(0, 0));
				}
			}
		};

		pastePosMenuItem.addActionListener(pastePosListener);

		cutMenuItem = new JMenuItem(cutLabel, cutIcon);
		cutMenuItem.setDisabledIcon(dcutIcon);
		cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(),
				false));
		cutMenuItem.setEnabled(false);

		cutListener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (e.getModifiers() == InputEvent.BUTTON1_DOWN_MASK
						|| e.getModifiers() == InputEvent.BUTTON1_MASK
						|| editor.getHandlesManager().keyStrokeActsOnSVGFrame()) {

					cut();
					pasteMenuItem.setEnabled(true);
				}
			}
		};

		cutMenuItem.addActionListener(cutListener);

		deleteMenuItem = new JMenuItem(deleteLabel, deleteIcon);
		deleteMenuItem.setDisabledIcon(ddeleteIcon);
		deleteMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_DELETE, 0, false));
		deleteMenuItem.setEnabled(false);

		deleteListener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (e.getModifiers() == InputEvent.BUTTON1_DOWN_MASK
						|| e.getModifiers() == InputEvent.BUTTON1_MASK
						|| editor.getHandlesManager().keyStrokeActsOnSVGFrame()) {

					delete(true);
				}
			}
		};

		deleteMenuItem.addActionListener(deleteListener);
	}

	/**
	 * @return the editor
	 */
	public EditorAdapter getSVGEditor() {

		return editor;
	}

	@Override
	public HashMap<String, JMenuItem> getMenuItems() {

		HashMap<String, JMenuItem> menuItems = new HashMap<String, JMenuItem>();
		menuItems.put(idCopy, copyMenuItem);
		menuItems.put(idPaste, pasteMenuItem);
		menuItems.put(idPastePos, pastePosMenuItem);
		menuItems.put(idCut, cutMenuItem);
		menuItems.put(idDelete, deleteMenuItem);

		return menuItems;
	}

	@Override
	public Collection<PopupItem> getPopupItems() {

		LinkedList<PopupItem> popupItems = new LinkedList<PopupItem>();

		// creating the copy popup item
		PopupItem item = new PopupItem(getSVGEditor(), idCopy, copyLabel,
				"Copy") {

			@Override
			public JMenuItem getPopupItem(LinkedList<Element> nodes) {

				if (nodes != null && nodes.size() > 0) {

					menuItem.setEnabled(true);

					// adds the action listeners
					menuItem.addActionListener(copyListener);

				} else {

					menuItem.setEnabled(false);
				}

				return super.getPopupItem(nodes);
			}
		};

		popupItems.add(item);

		// creating the paste popup item
		item = new PopupItem(getSVGEditor(), idPaste, pasteLabel, "Paste") {

			@Override
			public JMenuItem getPopupItem(LinkedList<Element> nodes) {

				if (clipboardManager.getClipboardContent().size() > 0) {

					menuItem.setEnabled(true);

					// adds the action listeners
					menuItem.addActionListener(pasteListener);

				} else {

					menuItem.setEnabled(false);
				}

				return super.getPopupItem(nodes);
			}
		};

		popupItems.add(item);

		// creating the paste to same location popup item
		item = new PopupItem(getSVGEditor(), idPastePos, pastePosLabel,
				"PastePos") {

			@Override
			public JMenuItem getPopupItem(LinkedList<Element> nodes) {

				if (clipboardManager.getClipboardContent().size() > 0) {

					menuItem.setEnabled(true);

					// adds the action listeners
					menuItem.addActionListener(pastePosListener);

				} else {

					menuItem.setEnabled(false);
				}

				return super.getPopupItem(nodes);
			}
		};

		popupItems.add(item);

		// creating the cut popup item
		item = new PopupItem(getSVGEditor(), idCut, cutLabel, "Cut") {

			@Override
			public JMenuItem getPopupItem(LinkedList<Element> nodes) {

				menuItem.setEnabled(false);

				if (nodes != null && nodes.size() > 0) {

					// getting the current svg handle
					SVGHandle handle = editor.getHandlesManager()
							.getCurrentHandle();

					if (handle != null
							&& !handle.getSelection().isSelectionLocked()) {

						menuItem.setEnabled(true);

						// adds the action listeners
						menuItem.addActionListener(cutListener);
					}
				}

				return super.getPopupItem(nodes);
			}
		};

		popupItems.add(item);

		// creating the delete popup item
		item = new PopupItem(getSVGEditor(), idDelete, deleteLabel, "Delete") {

			@Override
			public JMenuItem getPopupItem(LinkedList<Element> nodes) {

				menuItem.setEnabled(false);

				if (nodes != null && nodes.size() > 0) {

					// getting the current svg handle
					SVGHandle handle = editor.getHandlesManager()
							.getCurrentHandle();

					if (handle != null
							&& !handle.getSelection().isSelectionLocked()) {

						menuItem.setEnabled(true);

						// adds the action listeners
						menuItem.addActionListener(deleteListener);
					}
				}

				return super.getPopupItem(nodes);
			}
		};

		popupItems.add(item);

		return popupItems;
	}

	/**
	 * copies the selected nodes of the current handle into the clipboard
	 */
	public void copy() {

		clipboardManager.clearClipboard();
		Node clonedNode = null;
		Rectangle2D bounds = null;

		// getting the current handle
		SVGHandle handle = editor.getHandlesManager().getCurrentHandle();

		if (handle != null) {

			// the map associating a cloned element to its bounds
			LinkedHashMap<Element, Rectangle2D> elementToBounds = new LinkedHashMap<Element, Rectangle2D>();

			// creating the linkedlist of the elements to be handled
			final LinkedList<Element> elementsList = new LinkedList<Element>();

			elementsList.addAll(selectedNodes);
			// cloning the nodes in the list and storing their bounds
			for (Element cur : elementsList) {

				if (cur != null) {

					bounds = handle.getSvgElementsManager()
							.getNodeGeometryBounds(cur);
					clonedNode = getClonedNodeWithoutUseNodes(cur);

					if (clonedNode != null) {

						elementToBounds.put((Element) clonedNode, bounds);
					}
				}
			}

			clipboardManager.addToClipboard(handle, elementToBounds);
		}
	}

	int pasteIndex = 0;

	/**
	 * pastes the copied nodes
	 * 
	 * @param samePosition
	 *            whether to paste on the same position as the copied node
	 * @param mousePosition
	 *            whether to paste at the mouse position
	 */
	public void paste(boolean samePosition, boolean mousePosition,
			Point2D offsetPoint) {

		// getting the current handle
		final SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
		if (clipboardManager.getClipboardContent().size() > 0 && handle != null) {

			final Element parent = handle.getSelection().getParentElement();
			final Document doc = handle.getScrollPane().getSVGCanvas()
					.getDocument();

			// the list of the nodes to paste
			final LinkedList<Element> nodesToPaste = new LinkedList<Element>();

			// modifying the id of the nodes
			final LinkedList<Element> sourceNodesToPaste = new LinkedList<Element>(
					clipboardManager.getClipboardContent());
			Set<Element> alreadyHandledNodes = new HashSet<Element>();
			Set<Element> nodesToRemove = new HashSet<Element>();
			final SVGHandle clipboardHandle = clipboardManager
					.getSourceHandle();

			for (Element current : sourceNodesToPaste) {

				if (current != null) {

					if (((clipboardHandle != null && clipboardHandle
							.equals(handle)) || !Toolkit
							.hasJWidgetChildElement(current))) {

						// sid = handle.getSvgElementsManager().getId("",
						// alreadyHandledNodes);
						// current.setAttribute("id", sid);
						alreadyHandledNodes.add(current);

					} else {

						nodesToRemove.add(current);
					}
				}
			}

			// removing all the nodes that should not be pasted
			sourceNodesToPaste.removeAll(nodesToRemove);

			if (nodesToRemove.size() > 0) {

				// showing a message that some nodes cannot be
				// pasted because they are rtda nodes

				// getting the labels
				String warningLabel = ResourcesManager.bundle
						.getString("WarningLabel");
				String messageLabel = "";

				if (sourceNodesToPaste.size() == 0) {

					messageLabel = ResourcesManager.bundle
							.getString("NoRightNodeToPaste");

				} else {

					messageLabel = ResourcesManager.bundle
							.getString("WrongNodesExistInCopy");
				}

				// showing the information dialog notifying that not all
				// the nodes or no node has been pasted
				JOptionPane.showMessageDialog(editor.getParent(), messageLabel,
						warningLabel, JOptionPane.WARNING_MESSAGE);
			}

			if (sourceNodesToPaste.size() > 0) {

				// the defs node
				final Element defs = handle.getSvgResourcesManager()
						.getDefsElement();

				// the list of the resources used by the pasted nodes
				final Set<Element> resNodes = new HashSet<Element>();

				// the list of the resources imported into the current document
				LinkedList<Element> resourceNodes = null;
				Element clonedElement = null;
				AbstractShape shapeModule = null;
				Point2D canvasSize = null;
				Rectangle2D canvasBounds = null;
				Point2D.Double translationFactors = null;
				Rectangle2D shapeBounds = null;
				UndoRedoAction translateAction = null;
				Set<Element> set;

				Point2D.Double basePoint = null;
				int nIndex = 0;
				HashSet<Element> textPathNodes = new HashSet<Element>(
						sourceNodesToPaste);
				HashMap<Element, Element> textPathMap = new HashMap<Element, Element>();
				for (Element current : textPathNodes) {
					if (current.getNodeName().equals("path")) {
						String strTextID = current.getAttribute("nci-textid");
						if (strTextID != null && strTextID.length() > 0) {
							Element textElement = handle.getCanvas()
									.getMapInfo(strTextID);
							if (textElement != null) {
								textPathMap.put(current, textElement);
								sourceNodesToPaste.remove(current);
								sourceNodesToPaste.remove(textElement);
							}
						}
					}

				}
				for (Element current : sourceNodesToPaste) {

					if (current != null) {
						if (nIndex == 0) {
							nIndex = 1;
							basePoint = calBasePoint(current);
						}

						// getting all the resource nodes used by this node
						resourceNodes = getResourcesUsedByNode(current, true);

						// if the copied node does not belong to this svg
						// document, it is imported
						if (!current.getOwnerDocument().equals(doc)) {

							// for each resource node, check if it is contained
							// in the list
							// of the resources used by the copied nodes
							for (Element res : resourceNodes) {

								if (res != null && !resNodes.contains(res)) {

									resNodes.add(res);
								}
							}
						}

						// cloning the node
						clonedElement = (Element) doc.importNode(current, true);
						handleElement(current, clonedElement);
						nodesToPaste.add(clonedElement);

						// removing all the rtda elements if the current handle
						// is not the one of the clipboard
						if (clipboardHandle == null
								|| !clipboardHandle.equals(handle)) {

							removeRtdaElements(clonedElement);
						}

						// handling the position of the node
						if (!samePosition) {

							// getting the shape manager for this node
							shapeModule = ShapeToolkit.getShapeModule(
									clonedElement, editor);

							if (shapeModule != null) {

								shapeBounds = clipboardManager
										.getClipboardContentMap().get(current);

								if (shapeBounds != null) {

									// translating the shape if it is necessary
									// computing the translation factors
									translationFactors = new Point2D.Double();

									// getting the size of the svg file
									canvasSize = handle.getCanvas()
											.getGeometryCanvasSize();
									canvasBounds = new Rectangle2D.Double(0, 0,
											canvasSize.getX(), canvasSize
													.getY());

									if (mousePosition) {

										// getting the mouse position
										Point mousePoint = handle.getCanvas()
												.getMousePosition();

										if (mousePoint != null) {

											translationFactors.x = -shapeBounds
													.getX()
													+ mousePoint.getX();
											translationFactors.y = -shapeBounds
													.getY()
													+ mousePoint.getY();
										}

									} else if (!canvasBounds
											.intersects(shapeBounds)) {

										translationFactors.x = -shapeBounds
												.getX()
												+ canvasSize.getX()
												/ 2
												- shapeBounds.getWidth() / 2;

										translationFactors.y = -shapeBounds
												.getY()
												+ canvasSize.getY()
												/ 2
												- shapeBounds.getHeight() / 2;

									} else {

										translationFactors.x = 50;
										translationFactors.y = 50;
									}

									if (translationFactors.getX() != 0
											|| translationFactors.getY() != 0) {

										// translating the node
										Point mousePoint = handle.getCanvas()
												.getMousePosition();
										if (mousePoint == null) { // menubar的菜单操作，mousePoint是空值
											mousePoint = new Point(5, 5);
										}
										Point2D scaledPoint = handle
												.getTransformsManager()
												.getScaledPoint(mousePoint,
														true);
										translationFactors = calPointOffset(
												current, basePoint,
												scaledPoint, offsetPoint);
										set = new HashSet<Element>();
										set.add(clonedElement);
										translateAction = shapeModule
												.translate(handle, set,
														translationFactors,
														true);
										translateAction.execute();
									}
								}
							}
						}
					}
				}
				if (textPathMap.size() > 0) {
					nodesToPaste
							.addAll(getTextPath(doc, textPathMap, samePosition,
									mousePosition, offsetPoint, basePoint));
				}

				// checking if the sub tree of the pasted nodes requires a
				// specific name space
				for (Element current : nodesToPaste) {

					if (current != null) {

						checkNamespace(current);
					}
				}

				// the runnable that will be executed
				final Runnable executeRunnable = new Runnable() {

					public void run() {

						handle.getSelection().clearSelection();

						// appending the resources
						for (Element current : resNodes) {// usedResourceNodes)
							// {

							if (current != null) {

								if (!current.getNodeName().equalsIgnoreCase(
										"style")) {
									defs.appendChild(current);

								}
							}
						}

						// add by yux，2008.12.05
						parseDefsSymbol(clipboardHandle, handle);

						// appending the children
						for (Element current : nodesToPaste) {

							if (current != null) {
								// remove by yuxiang
								// parseUseSymbol(handle, defs, current,
								// parent);
								parent.appendChild(current);
								// registering the current node to the used
								// resources map if it uses a resource
								handle.getSvgResourcesManager()
										.registerUsedResource(current);
							}
						}

						// refreshing the properties and the resources handle
						handle.getSelection().handleSelection(
								new HashSet<Element>(nodesToPaste), true, true);
						getSVGEditor().getHandlesManager().handleChanged();
					}
				};

				// the runnable used for undoing
				final Runnable undoRunnable = new Runnable() {

					public void run() {

						// removing the added children from the parent node
						for (Element current : nodesToPaste) {

							if (current != null) {

								parent.removeChild(current);

								// unregister the current node to the used
								// resources
								// map if it uses a resource
								handle.getSvgResourcesManager()
										.unregisterAllUsedResource(current);
							}
						}

						// removing the added resources
						for (Element current : resNodes) {// usedResourceNodes)
							// {

							if (current != null) {
								defs.removeChild(current);
							}
						}

						// refreshing the properties and the resources handle
						handle.getSelection().refreshSelection(true);
						getSVGEditor().getHandlesManager().handleChanged();
					}
				};

				// adding the undo/redo action list
				UndoRedoAction action = new UndoRedoAction(undoRedoPasteLabel,
						executeRunnable, undoRunnable, executeRunnable,
						new HashSet<Element>(nodesToPaste));

				// creating the undo/redo list and adds the action to it
				UndoRedoActionList actionlist = new UndoRedoActionList(action
						.getName(), false);
				actionlist.add(action);
				handle.getUndoRedo().addActionList(actionlist, true);

				editor.getSvgSession().refreshCurrentHandleLater();
				// if (pasteIndex%2==0) {
				// Set<Element> selectedEles = handle.getSelection()
				// .getSelectedElements();
				// handle.getSelection().clearSelection();
				// Iterator<Element> it = selectedEles.iterator();
				// while (it.hasNext()) {
				// Element e = it.next();
				// e.getParentNode().removeChild(e);
				// }
				// }else{
				// paste(samePosition, mousePosition, offsetPoint);
				// }
				// pasteIndex++;
			}
		}
	}

	/**
	 * add by yux,2008.12.05 当目标定义域发生变化时，重新维护复制纪录，将该目标定义域做为源定义域时的复制纪录
	 * 
	 * @param oldName
	 *            ：原名
	 * @param newName
	 *            ：新名
	 */
	public void resetDefsHis(String oldName, String newName) {
		String info = null;
		int size = listHis.size();

		for (int j = size - 1; j >= 0; j--) {
			info = listHis.get(j);
			if (info.indexOf(oldName + separator) == 0) {
				listHis.remove(j);
				info.replaceFirst(oldName, newName);
				listHis.set(j, info);
			} else if (info.lastIndexOf(separator + oldName) == info.length()
					- oldName.length()) {
				listHis.remove(j);
				info.replaceFirst(separator + oldName, separator + newName);
				listHis.set(j, info);
			}
		}
		return;
	}

	/**
	 * add by yux,2008.12.08 当目标定义域发生变化时，重新维护复制纪录，将该目标定义域做为源定义域时的复制纪录清除
	 * 
	 * @param destHandle
	 *            ：目标定义域对象
	 */
	public void removeDefsHis(SVGHandle handle) {
		String destName = handle.getName();
		String info = null;
		int size = listHis.size();

		for (int j = size - 1; j >= 0; j--) {
			info = listHis.get(j);
			if (info.indexOf(destName + separator) == 0) {
				listHis.remove(j);
			} else if (info.lastIndexOf(separator + destName) == info.length()
					- destName.length()) {
				listHis.remove(j);
			}
		}
		return;
	}

	/**
	 * add by yux，2008.12.04 将源文档中所有symbol定义
	 * 
	 * @param sourceHandle
	 * @param destHandle
	 */
	private void parseDefsSymbol(SVGHandle sourceHandle, SVGHandle destHandle) {
		// 目标与源有一个不存在则返回
		if (sourceHandle == null || destHandle == null) {
			return;
		}

		// 目标与源相同则返回
		if (sourceHandle == destHandle)
			return;

		String sourceName = sourceHandle.getName();
		String destName = destHandle.getName();
		// 目标与源相同则返回
		if (sourceName.equals(destName))
			return;
		Document destDoc = destHandle.getCanvas().getDocument();
		String info = sourceName + separator + destName;

		// 如果历史纪录包含defs复制，则无需复制defs数据
		if (listHis.contains(info))
			return;
		listHis.add(info);
		Element defsSource = (Element) sourceHandle.getCanvas().getDocument()
				.getDocumentElement().getElementsByTagName("defs").item(0);
		Element defsDest = (Element) destDoc.getDocumentElement()
				.getElementsByTagName("defs").item(0);
		// 如果源定义域不存在，则无需复制
		if (defsSource == null) {
			return;
		}

		// 如目标定义域不存在，则新建一个定义域
		if (defsDest == null) {
			defsDest = destHandle.getCanvas().getDocument().createElement(
					"defs");
			defsDest = (Element) destHandle.getCanvas().getDocument()
					.getDocumentElement().appendChild(defsDest);
		}

		// 遍历源定义域，当目标定义域无对应的定义，则复制
		NodeList childSource = defsSource.getChildNodes();
		int length = childSource.getLength();
		String id = null;
		Element element = null;
		String xPathExpr = null;
		Node destNode = null;
		int nCount = 0;// 目标定义域被更新次数
		for (int i = 0; i < length; i++) {
			if (childSource.item(i) instanceof Element) {
				element = (Element) childSource.item(i);
				id = element.getAttribute("id");
				xPathExpr = "/*[@id='" + id + "']";
				try {
					destNode = Utilities.findNode(xPathExpr, defsDest);
				} catch (XPathExpressionException e) {

					e.printStackTrace();
					destNode = null;
				}
				if (destNode == null) {
					element = (Element) destDoc.importNode(element, true);
					defsDest.appendChild(element);
					nCount++;
				}
			}
		}

		// 当目标定义域有更新的情况，则将该目标定义域做为复制源定义域以往的复制纪录清除
		if (nCount > 0) {
			int size = listHis.size();

			for (int j = size - 1; j >= 0; j--) {
				info = listHis.get(j);
				if (info.indexOf(destName + separator) == 0) {
					listHis.remove(j);
				}
			}
		}

		return;
	}

	/**
	 * 粘贴use元素
	 * 
	 * @param handle
	 *            SVGHandle对象
	 * @param defsEle
	 *            handle中的defs元素
	 * @param pastedEle
	 *            待粘贴的元素
	 * @param parentEle
	 *            被粘贴的元素
	 */
	private void parseUseSymbol(SVGHandle handle, Element defsEle,
			Element pastedEle, Element parentEle) {
		Element symbolEle = null;
		if (pastedEle.getNodeName().equalsIgnoreCase("use")) {
			String xlink_href = pastedEle.getAttributeNS(
					"http://www.w3.org/1999/xlink", "href");
			String symbolID = xlink_href.substring(1);
			// String content =
			// editor.getGraphUnitManager().getSymbolMap_useID().get(symbolID).getContent();
			// 图元文件中的symbol元素
			// Element originalSymbolEle =
			// (Element)Utilities.getXMLDocumentByString(content).getDocumentElement();

			// TODO 获取图元element ，deprecated by wangql @2008.12.11-16:54,
			// 将图元、模板、thumbnail在统一的hashmap中管理
			// Element originalSymbolEle = editor.getGraphUnitManager()
			// .getThumbnailMap().get(symbolID).getDocument()
			// .getDocumentElement();

			Element originalSymbolEle = null;
			symbolEle = (Element) handle.getCanvas().getDocument().importNode(
					originalSymbolEle, true);

			new GraphUnitImageShape(editor).addSymbolElement(handle.getCanvas()
					.getDocument(), defsEle, symbolEle, "", "");
		} else if (pastedEle.getNodeName().equalsIgnoreCase("g")) {
			NodeList children = pastedEle.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				if (children.item(i).getNodeName().equalsIgnoreCase("use")
						|| children.item(i).getNodeName().equalsIgnoreCase("g")) {
					pastedEle = (Element) children.item(i);
					parseUseSymbol(handle, defsEle, pastedEle, parentEle);
				}
			}
		}
	}

	public String handleElement(Element resElement, Element cloneElement) {
		String resId = null;
		try {
			resId = resElement.getAttribute("id");
		} catch (Exception ex) {
			resId = null;
		}

		String newId = null;
		if (resElement.getNodeName().equals("g")) {
			String type = resElement.getAttribute("type");
			newId = ComputeID.getSymbolID();
			if (resId != null && resId.length() > 0)
				cloneElement.setAttribute("id", newId);
			if (type != null && type.equals("solidified")) {
				try {
					Element target_center = (Element) Utilities.findNode(
							"*[@nci_type='nci_target_pointer']", cloneElement);
					NodeList list = Utilities.findNodes(
							"*[@nci_type='target']", cloneElement);
					for (int i = 0; i < list.getLength(); i++) {
						if (list.item(i) instanceof Element) {
							Element element = (Element) list.item(i);
							element.removeAttribute("transform");
							double x = Double.parseDouble(target_center
									.getAttribute("cx"))
									- Double.parseDouble(element
											.getAttribute("width"));
							element.setAttribute("x", String.valueOf(x));
							double y = Double.parseDouble(target_center
									.getAttribute("cy"))
									- ((int) Double.parseDouble(element
											.getAttribute("height"))) / 2;
							element.setAttribute("y", String.valueOf(y));
						}
					}
				} catch (XPathExpressionException e) {

					e.printStackTrace();
				}
			} else {
				newId = ComputeID.getSymbolID();
				if (resId != null && resId.length() > 0)
					cloneElement.setAttribute("id", newId);

				NodeList childList = cloneElement.getChildNodes();
				int iIndex = 0;
				for (int i = 0; i < childList.getLength(); i++) {
					if (childList.item(i) instanceof Element) {
						Element el = (Element) childList.item(i);
						handleChildElement(el, new String().format("%s%d",
								newId, iIndex));
						iIndex++;
					}
				}
			}
		} else {
			// add by yuxiang

			if (resElement.getNodeName().equals("path")) {
				newId = ComputeID.getLineID();
				clearLineInfo(cloneElement);

			} else if (resElement.getNodeName().equals("text")) {
				newId = ComputeID.getTextID();
			} else
				newId = ComputeID.getSymbolID();

			// modifying the id of the resource
			cloneElement.setAttribute("id", newId);

		}

		return newId;

	}

	/**
	 * removes all the rtda elements that could be found under this element
	 * 
	 * @param element
	 *            a rtda element
	 */
	protected void removeRtdaElements(Element element) {

		if (element != null && element.hasChildNodes()) {

			NodeList children = element.getChildNodes();
			LinkedList<Element> childrenList = Toolkit.getLinkedList(children);

			for (Element el : childrenList) {

				if (el.getNodeName().startsWith(Toolkit.rtdaPrefix)) {

					// if the node is a rtda node, it is removed
					element.removeChild(el);

				} else {

					// if the node is not a rtda node, its subtree is checked
					removeRtdaElements(el);
				}
			}
		}
	}

	/**
	 * modifies the referenced resource id in the elements properties and their
	 * child nodes
	 * 
	 * @param handle
	 *            a svg handle
	 * @param elements
	 *            a set of elements
	 * @param oldId
	 *            the old id of a resource
	 * @param newId
	 *            the new id of a resource
	 */
	protected void modifyReferencedResourceId(SVGHandle handle,
			Set<Element> elements, String oldId, String newId) {

		// for each pasted node, modifying the name of the resource it uses
		String style = "";
		Node node = null;
		Element element = null;

		for (Element current : elements) {

			if (current != null) {

				style = current.getAttribute("style");

				if (style != null && style.indexOf("#".concat(oldId)) != -1) {

					style = style.replaceAll("#".concat(oldId) + "[)]", "#"
							.concat(newId)
							+ ")");
					current.setAttribute("style", style);
					handle.getSvgResourcesManager().addNodeUsingResource(newId,
							current);
				}

				// modifying the nodes in the subtree under the pasted node
				for (NodeIterator nodeIt = new NodeIterator(current); nodeIt
						.hasNext();) {

					node = nodeIt.next();

					if (node != null && !node.equals(current)
							&& node instanceof Element
							&& ((Element) node).hasAttribute("style")) {

						element = (Element) node;
						style = element.getAttribute("style");

						if (style != null
								&& style.indexOf("#".concat(oldId)) != -1) {

							style = style.replaceAll("#".concat(oldId) + "[)]",
									"#".concat(newId) + ")");
							element.setAttribute("style", style);
							handle.getSvgResourcesManager()
									.addNodeUsingResource(newId, node);
						}
					}
				}
			}
		}
	}

	/**
	 * checking if the sub tree of the node requires a specific name space
	 * 
	 * @param element
	 *            an element
	 */
	protected void checkNamespace(Element element) {

		Map<String, String> nameSpaces = new HashMap<String, String>(editor
				.getRequiredNameSpaces());

		NodeIterator nit = new NodeIterator(element);
		Node cur;
		String prefix = "", nsp = "";
		int pos = 0;

		// checking if the nodes under the pasted nodes use a specific name
		// space
		do {

			cur = nit.hasNext() ? nit.next() : element;

			if (cur != null && cur instanceof Element) {

				pos = cur.getNodeName().indexOf(":");

				if (pos != -1) {

					prefix = cur.getNodeName().substring(0, pos);
					nsp = nameSpaces.get(prefix);

					if (nsp != null) {

						EditorToolkit.checkXmlns(cur.getOwnerDocument(),
								prefix, nsp);
						nameSpaces.remove(prefix);
					}
				}
			}

		} while (nit.hasNext());
	}

	/**
	 * cuts the current selection
	 */
	public void cut() {

		// copies the nodes
		copy();

		// removes the nodes
		delete(false);
	}

	/**
	 * deletes the selected nodes
	 * 
	 * @param isDelete
	 *            whether it is used in the delete action or in the cut action
	 */
	public void delete(boolean isDelete) {

		final SVGHandle handle = editor.getHandlesManager().getCurrentHandle();

		if (selectedNodes.size() > 0) {

			// cloning the selected nodes set
			Set<Element> clonedSelectedNodesSet = new LinkedHashSet<Element>(
					selectedNodes);

			for (Element element : selectedNodes) {
				if (element.getNodeName().equals("path")) {
					String strTextID = element.getAttribute("nci-textid");
					if (strTextID != null && strTextID.length() > 0) {
						Element textElement = handle.getCanvas().getMapInfo(
								strTextID);
						if (textElement != null
								&& !selectedNodes.contains(textElement)) {
							clonedSelectedNodesSet.add(textElement);
						}
					}
				} else if (element.getNodeName().equals("text")) {
					String strPathID = element.getAttribute("nci-textpath");
					if (strPathID != null && strPathID.length() > 0) {
						Element pathElement = handle.getCanvas().getMapInfo(
								strPathID);
						if (pathElement != null
								&& !selectedNodes.contains(pathElement)) {
							clonedSelectedNodesSet.add(pathElement);
						}
					}
				}

			}

			// creating the ordered list of the selected nodes
			// (the list order is the one that is found in the parent)
			final HashMap<Element, Element> parentNodesMap = new HashMap<Element, Element>();
			final List<Element> elementsToDelete = new ArrayList<Element>(
					clonedSelectedNodesSet.size());

			// creating the map associating an element to its next element
			final Map<Element, Element> nextNodesMap = new HashMap<Element, Element>();

			// LinkedList<Element> childNodesList = Toolkit
			// .getChildrenElements((Element) parent);
			// int i = 0;
			//
			// for (Element el : childNodesList) {
			//
			// if (clonedSelectedNodesSet.contains(el)) {
			//
			// elementsToDelete.add(el);
			// nextNodesMap.put(el,
			// (((i + 1) < childNodesList.size()) ? childNodesList
			// .get(i + 1) : null));
			// }
			//
			// i++;
			// }

			for (Element el : clonedSelectedNodesSet) {
				Node nextNode = el.getNextSibling();

				nextNodesMap.put(el, (Element) nextNode);
				elementsToDelete.add(el);
				parentNodesMap.put(el, (Element) el.getParentNode());

			}

			// reverting the list of the elements to delete
			// Collections.reverse(elementsToDelete);

			// the runnable that will be executed
			final Runnable executeRunnable = new Runnable() {

				public void run() {

					// removing the children from the parent element
					for (Element element : elementsToDelete) {

						if (element != null && deleteFilter.filterElement(element,elementsToDelete)) {

							Element parentElement = parentNodesMap.get(element);
							parentElement.removeChild(element);
							
							// register the current node to the used
							// resources map if it uses a resource
							handle.getSvgResourcesManager()
									.unregisterAllUsedResource(element);
						}
					}
					fireElementDeleted(parentNodesMap, elementsToDelete);
					// modified by wangql @2009-1-19
					handle.getSelection().clearSelection();
					// handle.getSelection().refreshSelection(true);
				}
			};

			// the runnable used for the undo/redo action
			final Runnable undoRunnable = new Runnable() {

				public void run() {

					try {
						// re-adding the children to the parent element
						Element nextElement = null;
						Element parentElement = null;
						// Collections.reverse(elementsToDelete);
						for (int i = 0; i < elementsToDelete.size(); i++) {
							Element element = elementsToDelete.get(i);
							if (element != null && deleteFilter.filterElement(element,elementsToDelete)) {

								// getting the next element of the current
								// element
								nextElement = nextNodesMap.get(element);
								parentElement = parentNodesMap.get(element);
								// inserting the element
								if (nextElement != null) {

									parentElement.insertBefore(element,
											nextElement);

								} else {

									parentElement.appendChild(element);
								}

								// register the current node to the used
								// resources map if it uses a resource
								handle.getSvgResourcesManager()
										.registerUsedResource(element);
							}
						}
						fireUndoElementDeleted(parentNodesMap, elementsToDelete);
						handle.getSelection().refreshSelection(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};

			// adding the undo/redo action list
			UndoRedoAction action = new UndoRedoAction(
					isDelete ? undoRedoDeleteLabel : undoRedoCutLabel,
					executeRunnable, undoRunnable, executeRunnable,
					new HashSet<Element>(elementsToDelete));

			// creates the undo/redo list and adds the action to it
			UndoRedoActionList actionlist = new UndoRedoActionList(action
					.getName(), false);
			actionlist.add(action);
			handle.getUndoRedo().addActionList(actionlist, true);
			editor.getSvgSession().refreshCurrentHandleImediately();
			editor.getPropertyModelInteractor().getGraphProperty().setElement(
					null);
			editor.getPropertyModelInteractor().getGraphBusiProperty()
					.setElement(null);
		}
	}

	/**
	 * returns the cloned node of the given node whose use nodes have been
	 * removed
	 * 
	 * @param node
	 *            a node
	 * @return the cloned node of the given node whose use nodes have been
	 *         removed
	 */
	public Node getClonedNodeWithoutUseNodes(Node node) {

		Node clonedNode = null;

		if (node != null) {

			clonedNode = node.cloneNode(true);

			if (!clonedNode.getNodeName().equals("use")) {

				// removes the use nodes from the subtree of the cloned node
				removeUseNodes(clonedNode);

			} else {

				clonedNode = null;
			}
		}

		return clonedNode;
	}

	/**
	 * removes the use nodes in the given nodes
	 * 
	 * @param node
	 *            a node
	 */
	protected void removeUseNodes(Node node) {

		if (node != null && node.hasChildNodes()) {

			NodeList children = node.getChildNodes();
			LinkedList<Element> childrenList = Toolkit.getLinkedList(children);

			for (Element element : childrenList) {

				if (element.getNodeName().equals("use")) {

					// if the node is a use node, it is removed
					// node.removeChild(element);//use问题已经解决

				} else {

					// if the node is not a use node, its subtree is checked
					removeUseNodes(element);
				}
			}
		}
	}

	/**
	 * creates the list of the resources used by the given node and returns it
	 * 
	 * @param element
	 *            an element
	 * @param deep
	 *            true if the children of the given node should be inspected
	 * @return the list of the resources used by the given node
	 */
	public LinkedList<Element> getResourcesUsedByNode(Element element,
			boolean deep) {

		LinkedList<Element> resources = new LinkedList<Element>();

		if (element != null) {

			// getting the defs element
			Element root = element.getOwnerDocument().getDocumentElement();
			Node cur = null;
			Element defs = null;

			for (cur = root.getFirstChild(); cur != null; cur = cur
					.getNextSibling()) {

				if (cur instanceof Element && cur.getNodeName().equals("defs")) {

					defs = (Element) cur;
				}
			}

			// the string containing the ids of the resources needed
			String style = element.getAttribute("style");

			if (deep) {

				for (NodeIterator it = new NodeIterator(element); it.hasNext();) {

					cur = it.next();

					if (cur instanceof Element) {

						style = style.concat(((Element) cur)
								.getAttribute("style"));
					}
				}
			}

			if (defs != null && style != null && !style.equals("")) {

				String id = "";
				Element el = null;

				// for each child of the "defs" element, adds it to the list if
				// it is used by the given element
				for (cur = defs.getFirstChild(); cur != null; cur = cur
						.getNextSibling()) {

					if (cur instanceof Element) {

						el = (Element) cur;
						id = el.getAttribute("id");

						// if the id of the resource is contained in the style
						// attribute
						if (id != null && style.indexOf("#".concat(id)) != -1) {

							resources.add(el);
						}
					}
				}
			}
		}

		return resources;
	}

	/**
	 * gets the name of the module
	 * 
	 * @return the name of the module
	 */
	public String getName() {

		return idClipboard;
	}

	private Point2D.Double calBasePoint(Element element) {
		Point2D.Double bpoint = null;
		if (element.getNodeName().equals("path")) {
			String strPath = element.getAttribute("d");
			Path path = new Path(strPath);

			Segment seg = path.getSegment();
			bpoint = (Point2D.Double) seg.getEndPoint();
		} else if (element.getNodeName().equals("ellipse")) {
			double x = 0, y = 0;
			x = new Double(element.getAttribute("cx")).doubleValue();
			y = new Double(element.getAttribute("cy")).doubleValue();
			bpoint = new Point2D.Double(x, y);

			AffineTransform initialTransform = editor.getHandlesManager()
					.getCurrentHandle().getSvgElementsManager().getTransform(
							element);
			initialTransform.transform(bpoint, bpoint);
		} else if (element.getNodeName().equals("g")) {
			NodeList childList = element.getChildNodes();
			for (int i = 0; i < childList.getLength(); i++) {
				if (childList.item(i) instanceof Element) {
					Element el = (Element) childList.item(i);
					el.getNodeName();
					return calBasePoint(el);
				}
			}
		} else {
			double x = 0, y = 0;
			x = new Double(element.getAttribute("x")).doubleValue();
			y = new Double(element.getAttribute("y")).doubleValue();
			bpoint = new Point2D.Double(x, y);

			AffineTransform initialTransform = editor.getHandlesManager()
					.getCurrentHandle().getSvgElementsManager().getTransform(
							element);
			initialTransform.transform(bpoint, bpoint);
		}
		return bpoint;
	}

	private Point2D.Double calPointOffset(Element element,
			Point2D.Double basePoint, Point2D curPoint, Point2D offsetPoint) {
		double x = curPoint.getX() - basePoint.getX() - offsetPoint.getX();
		double y = curPoint.getY() - basePoint.getY() - offsetPoint.getY();
		Point2D.Double resPoint = new Point2D.Double(x, y);
		return resPoint;
	}

	private void clearLineInfo(Element cloneElement) {
		if (cloneElement.getNodeName().equals("path")) {
			Element nciLink = (Element) cloneElement.getElementsByTagName(
					"PSR:Nci_Link").item(0);
			if (nciLink != null) {
				int k = 0;
				String strID = "";
				while (1 == 1) {
					strID = nciLink.getAttribute("Pin0InfoVect" + k
							+ "LinkObjId");
					if (strID == null || strID.length() == 0)
						break;
					nciLink.removeAttribute("Pin0InfoVect" + k + "LinkObjId");
					k++;

				}

				k = 0;
				while (1 == 1) {
					strID = nciLink.getAttribute("Pin1InfoVect" + k
							+ "LinkObjId");
					if (strID == null || strID.length() == 0)
						break;
					nciLink.removeAttribute("Pin1InfoVect" + k + "LinkObjId");
					k++;
				}
			}
		}
	}

	private void handleChildElement(Element element, String strID) {
		if (element.getNodeName().equals("g")) {

			NodeList childList = element.getChildNodes();
			int iIndex = 0;
			for (int i = 0; i < childList.getLength(); i++) {
				if (childList.item(i) instanceof Element) {
					Element el = (Element) childList.item(i);
					handleChildElement(el, new String().format("%s%d", strID,
							iIndex));
					iIndex++;
				}
			}
		} else {
			element.setAttribute("id", strID);
			clearLineInfo(element);
		}
		return;
	}

	public Set<Element> getTextPath(Document doc,
			HashMap<Element, Element> elementMap, boolean samePosition,
			boolean mousePosition, Point2D offsetPoint, Point2D.Double basePoint) {
		Element clonedPathElement = null;
		Element clonedTextElement = null;
		Set<Element> sets = new HashSet<Element>();
		Point2D.Double curbasePoint = basePoint;
		Element textElement = null;
		for (Element current : elementMap.keySet()) {
			if (current != null) {
				textElement = elementMap.get(current);
				if (curbasePoint == null) {
					curbasePoint = calBasePoint(current);
				}

				// cloning the node
				clonedPathElement = (Element) doc.importNode(current, true);

				String strPathID = handleElement(current, clonedPathElement);
				sets.add(clonedPathElement);

				clonedTextElement = (Element) doc.importNode(textElement, true);
				String strTextID = handleElement(textElement, clonedTextElement);

				sets.add(clonedTextElement);
				clonedTextElement.setAttribute("nci-textpath", strPathID);
				clonedPathElement.setAttribute("nci-textid", strTextID);
				tranElement(current, clonedPathElement, samePosition,
						mousePosition, offsetPoint, curbasePoint);
				// tranElement(textElement,clonedTextElement, samePosition,
				// mousePosition, offsetPoint,curbasePoint);
				// NciTextPathModule module = new NciTextPathModule(editor);
				// module.correctTextPos(editor.getHandlesManager().getCurrentHandle(),
				// clonedPathElement,
				// current.getAttribute("d"),
				// clonedPathElement.getAttribute("d"));
			}

		}
		return sets;
	}

	public void tranElement(Element current, Element clonedElement,
			boolean samePosition, boolean mousePosition, Point2D offsetPoint,
			Point2D.Double basePoint) {
		AbstractShape shapeModule = null;
		Point2D canvasSize = null;
		Rectangle2D canvasBounds = null;
		Point2D.Double translationFactors = null;
		Rectangle2D shapeBounds = null;
		UndoRedoAction translateAction = null;
		SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
		Set<Element> set;
		// handling the position of the node
		if (!samePosition) {

			// getting the shape manager for this node
			shapeModule = ShapeToolkit.getShapeModule(clonedElement, editor);

			if (shapeModule != null) {

				shapeBounds = clipboardManager.getClipboardContentMap().get(
						current);

				if (shapeBounds != null) {

					// translating the shape if it is necessary
					// computing the translation factors
					translationFactors = new Point2D.Double();

					// getting the size of the svg file
					canvasSize = handle.getCanvas().getGeometryCanvasSize();
					canvasBounds = new Rectangle2D.Double(0, 0, canvasSize
							.getX(), canvasSize.getY());

					if (mousePosition) {

						// getting the mouse position
						Point mousePoint = handle.getCanvas()
								.getMousePosition();

						if (mousePoint != null) {

							translationFactors.x = -shapeBounds.getX()
									+ mousePoint.getX();
							translationFactors.y = -shapeBounds.getY()
									+ mousePoint.getY();
						}

					} else if (!canvasBounds.intersects(shapeBounds)) {

						translationFactors.x = -shapeBounds.getX()
								+ canvasSize.getX() / 2
								- shapeBounds.getWidth() / 2;

						translationFactors.y = -shapeBounds.getY()
								+ canvasSize.getY() / 2
								- shapeBounds.getHeight() / 2;

					} else {

						translationFactors.x = 50;
						translationFactors.y = 50;
					}

					if (translationFactors.getX() != 0
							|| translationFactors.getY() != 0) {

						// translating the node
						Point mousePoint = handle.getCanvas()
								.getMousePosition();
						Point2D scaledPoint = handle.getTransformsManager()
								.getScaledPoint(mousePoint, true);
						translationFactors = calPointOffset(current, basePoint,
								scaledPoint, offsetPoint);
						set = new HashSet<Element>();
						set.add(clonedElement);
						translateAction = shapeModule.translate(handle, set,
								translationFactors, true);
						translateAction.execute();
					}
				}
			}
		}
	}

	public void addElementDeletedListener(ElementDeleteListener lis) {
		deletedListeners.add(lis);
	}

	public void removeElementDeletedListener(ElementDeleteListener lis) {
		deletedListeners.remove(lis);
	}

	public void fireElementDeleted(HashMap<Element, Element> parentNodesMap,
			List<Element> elementsToDelete) {
		for (ElementDeleteListener lis : deletedListeners) {
			lis.elementDeleted(parentNodesMap, elementsToDelete);
		}
	}

	public void fireUndoElementDeleted(
			HashMap<Element, Element> parentNodesMap,
			List<Element> elementsToDelete) {
		for (ElementDeleteListener lis : deletedListeners) {
			lis.undoDelete(parentNodesMap, elementsToDelete);
		}
	}

	public ElementDeleteFilter getDeleteFilter() {
		return deleteFilter;
	}

	public void setDeleteFilter(ElementDeleteFilter deleteFilter) {
		this.deleteFilter = deleteFilter;
	}

}
