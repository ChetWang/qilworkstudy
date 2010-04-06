package fr.itris.glips.svgeditor.display.selection;

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.xml.xpath.XPathExpressionException;

import org.apache.batik.dom.svg.SVGOMGElement;
import org.apache.batik.dom.svg.SVGOMPathElement;
import org.apache.batik.dom.svg.SVGOMUseElement;
import org.apache.batik.ext.awt.geom.ExtendedGeneralPath;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nci.svg.actionparser.ActionParserModule;
import com.nci.svg.sdk.bean.ModelActionBean;
import com.nci.svg.sdk.bean.ModelBean;
import com.nci.svg.sdk.bean.ModelRelaIndunormBean;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.business.BusinessSelectionIfc;
import com.nci.svg.sdk.client.selection.GraphicTranslationListener;
import com.nci.svg.sdk.client.selection.ModeSelectionEvent;
import com.nci.svg.sdk.client.selection.ModeSelectionListener;
import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.client.util.EditorToolkit;
import com.nci.svg.sdk.client.util.Utilities;
import com.nci.svg.sdk.graphunit.NCIEquipSymbolBean;
import com.nci.svg.sdk.module.ScreenCastModuleAdapter;
import com.nci.svg.sdk.other.LinkPointManager;
import com.nci.svg.sdk.shape.GroupBreakerIF;
import com.nci.svg.sdk.shape.PseudoConnectedElement;
import com.nci.svg.sdk.shape.SelectionFilterIF;
import com.nci.svg.sdk.ui.ScreenCastCursor;
import com.nci.svg.sdk.ui.clipboard.ImageTransferable;

import fr.itris.glips.library.geom.path.Path;
import fr.itris.glips.library.geom.path.segment.Segment;
import fr.itris.glips.svgeditor.display.canvas.CanvasPainter;
import fr.itris.glips.svgeditor.display.canvas.SVGCanvas;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.properties.SVGProperties;
import fr.itris.glips.svgeditor.selection.SelectionInfoManager;
import fr.itris.glips.svgeditor.shape.AbstractShape;
import fr.itris.glips.svgeditor.shape.MultiAbstractShape;
import fr.itris.glips.svgeditor.shape.ShapeToolkit;
import fr.itris.glips.svgeditor.shape.path.PathShape;

/**
 * the class recording information on the selection on the canvas
 * 
 * @author ITRIS, Jordi SUC
 */
public class Selection {

	/**
	 * the constant specifying the first level of the selection
	 */
	public static final int SELECTION_LEVEL_1 = 0;
	/**
	 * the constant specifying the second level of the selection
	 */
	public static final int SELECTION_LEVEL_2 = 1;
	/**
	 * the constant specifying the third level of the selection
	 */
	public static final int SELECTION_LEVEL_3 = 2;
	/**
	 * the constant specifying the third level of the selection
	 */
	public static final int SELECTION_LEVEL_4 = 3;
	/**
	 * the constant specifying the level of the selection while in a drawing
	 * action mode
	 */

	/**
	 * add by yux,2008.12.30 拓扑点显示模式
	 */
	public static final int SELECTION_TERMINAL = 20;

	/**
	 * add by yux,2008.12.30 连接点显示模式
	 */
	public static final int SELECTION_CONNECTNODE = 21;
	public static final int SELECTION_LEVEL_DRAWING = 10;
	/**
	 * the constant specifying the level of the selection while in selection
	 * items action mode
	 */
	public static final int SELECTION_LEVEL_ITEMS_ACTION = 11;
	/**
	 * the regular sub mode
	 */
	public static final int REGULAR_SUB_MODE = 0;
	/**
	 * the translation sub mode
	 */
	public static final int TRANSLATION_SUB_MODE = 1;
	/**
	 * the action sub mode
	 */
	public static final int ACTION_SUB_MODE = 2;
	/**
	 * the drawing mouse pressed action
	 */
	public static final int SELECTION_ZONE_MOUSE_PRESSED = 0;
	/**
	 * the drawing mouse released action
	 */
	public static final int SELECTION_ZONE_MOUSE_RELEASED = 1;
	/**
	 * the drawing mouse dragged action
	 */
	public static final int SELECTION_ZONE_MOUSE_DRAGGED = 2;

	public static final int NONE_LINE_MODE = -1;

	public static final int PARALLEL_LINE_MODE = 0;

	public static final int VERTICAL_LINE_MODE = 1;

	public static final int VERTICAL_MIDDLE_LINE_MODE = 2;

	public int nLineMode = -1;
	/**
	 * the global selection manager
	 */
	private SelectionInfoManager selectionManager = null;
	/**
	 * the svg handle this object is linked to
	 */
	private SVGHandle svgHandle;
	/**
	 * the listener to the mouse actions on the canvas
	 */
	private SelectionListener selectionListener;
	/**
	 * the listener to the keyactions on the canvas
	 */
	private KeySelectionListener keySelectionListener;
	/**
	 * the selection managers
	 */
	private SelectionManager singleSelectionManager, multiSelectionManager;

	private PointItemsCanvasPainter pointit = new PointItemsCanvasPainter();
	/**
	 * the parent element of the edited nodes
	 */
	private Element parentElement;
	/**
	 * the set of the currently selected elements
	 */
	private Set<Element> selectedElements = new HashSet<Element>();
	/**
	 * the ordered list of the currently selected elements
	 */
	private LinkedList<Element> orderedSelectedElements = new LinkedList<Element>();
	/**
	 * the set of the currently selected elements that are not locked
	 */
	private Set<Element> unLockedSelectedElements = new HashSet<Element>();
	/**
	 * the set of the locked elements
	 */
	private Set<Element> lockedElements = new HashSet<Element>();
	/**
	 * the set of the current selection items
	 */
	private Set<SelectionItem> selectionItems = new HashSet<SelectionItem>();

	/**
	 * add by yux，2008.12.30 拓扑点显示
	 */
	private Set<SelectionItem> terminalItems = new HashSet<SelectionItem>();
	/**
	 * the selection mode
	 */
	private int selectionSubMode = REGULAR_SUB_MODE;
	/**
	 * the selection level
	 */
	private int selectionLevel = SELECTION_LEVEL_1;

	/**
	 * add by yux,2008.12.30 拓扑点显示等级
	 */
	private int terminalLevel = SELECTION_LEVEL_1;
	/**
	 * the action selection manager
	 */
	private SelectionManager actionSelectionManager;
	/**
	 * the painter of the selection zone rectangle
	 */
	private GhostSelectionZoneCanvasPainter ghostSelectionZoneCanvasPainter1,
			ghostSelectionZoneCanvasPainter2, ghostSelectionZoneCanvasPainter3,
			ghostSelectionZoneCanvasPainter4;
	/**
	 * the set of the listeners that listen to the selection changes
	 */
	private Set<SelectionChangedListener> selectionChangedListeners = new HashSet<SelectionChangedListener>();
	/**
	 * the painter of the selection items
	 */
	private SelectionItemsCanvasPainter selectionItemsPainter;

	/**
	 * add by yux,2008.12.30 拓扑点显示刷子
	 */
	private TerminalItemsCanvasPainter terminalItemsPainter;
	/**
	 * whether the action and translation management should be disabled
	 */
	private boolean actionDisabled = false;
	/**
	 * the first point of an action
	 */
	private Point2D firstPoint;
	/**
	 * the selection item of the current action
	 */
	private SelectionItem actionItem;
	/**
	 * the set of the elements that are put here when they are translated
	 */
	private Set<Element> translationElements = new HashSet<Element>();

	public static int serial = 0;

	public int i = 0;

	// 绘制平行线、垂直线、垂分线的事件监听
	private SelectionChangedListener drawLineListener = null;

	private SelectionChangedListener drawLineModifyListener = null;

	private SelectionChangedListener pathshapeListener = null;

	/**
	 * add by yux,2008.12.08 绘制描述的事件监听
	 */
	private SelectionChangedListener describeTextListener = null;

	private boolean bTextDialog = false;

	private SymbolWink sWink = null;

	/**
	 * add by yux,2008.12.30
	 */
	private Element terminalElement = null;

	private SelectionFilterIF selectionFilter = null;

	/**
	 * 闪烁用定时器
	 */
	private Timer winkTimer = null;

	private List<BusinessSelectionIfc> businessSelections = null;

	private List<ModeSelectionListener> modeSelectionListeners = null;

	private List<ModeSelectionListener> mouseMoveListeners = null;

	private List<ModeSelectionListener> mouseReleasedListeners = null;

	private List<ModeSelectionListener> mouseClickedListeners = null;

	private List<ModeSelectionListener> mousePressedListeners = null;

	private List<GraphicTranslationListener> graphicTranslationListeners = null;

	/**
	 * 
	 * @return the pathshapeListener
	 */
	public SelectionChangedListener getPathshapeListener() {
		return pathshapeListener;
	}

	/**
	 * @param pathshapeListener
	 *            the pathshapeListener to set
	 */
	public void setPathshapeListener(SelectionChangedListener pathshapeListener) {
		this.pathshapeListener = pathshapeListener;
	}

	/**
	 * @return the bTextDialog
	 */
	public boolean isBTextDialog() {
		return bTextDialog;
	}

	/**
	 * @param textDialog
	 *            the bTextDialog to set
	 */
	public void setBTextDialog(boolean textDialog) {
		bTextDialog = textDialog;
	}

	/**
	 * @return the drawLineModifyListener
	 */
	public SelectionChangedListener getDrawLineModifyListener() {
		return drawLineModifyListener;
	}

	/**
	 * @param drawLineModifyListener
	 *            the drawLineModifyListener to set
	 */
	public void setDrawLineModifyListener(
			SelectionChangedListener drawLineModifyListener) {
		this.drawLineModifyListener = drawLineModifyListener;
	}

	/**
	 * @return the nLineMode
	 */
	public int getNLineMode() {
		return nLineMode;
	}

	/**
	 * @param lineMode
	 *            the nLineMode to set
	 */
	public void setNLineMode(int lineMode) {
		nLineMode = lineMode;
	}

	public void setWink(boolean bWinkFlag, int seccend) {
		if (bWinkFlag) {
			if (winkTimer == null)
				winkTimer = new Timer(true);
			beginWink(selectedElements, svgHandle.getEditor());
			sWink = new SymbolWink(selectedElements, svgHandle.getEditor());
			winkTimer.schedule(sWink, 0, seccend * 1000);
		} else {
			if (winkTimer != null) {
				winkTimer.cancel();
				winkTimer = null;
				endWink(sWink.getSets(), svgHandle.getEditor());
			}

		}
	}

	/**
	 * 闪烁前初始化
	 * 
	 * @param sets
	 *            :待初始化的节点集合
	 * @param editor
	 *            :
	 */
	public void beginWink(Set<Element> sets, EditorAdapter editor) {
		Iterator<Element> iterators = sets.iterator();
		while (iterators.hasNext()) {
			Element element = iterators.next();
			element.setAttribute("opacity", "1");
		}
		editor.getSvgSession().refreshCurrentHandleImediately();
	}

	/**
	 * 闪烁后还原
	 * 
	 * @param sets
	 *            :待还原的节点集合
	 * @param editor
	 */
	public void endWink(Set<Element> sets, EditorAdapter editor) {
		Iterator<Element> iterators = sets.iterator();
		while (iterators.hasNext()) {
			Element element = iterators.next();
			element.removeAttribute("opacity");
		}
		editor.getSvgSession().refreshCurrentHandleImediately();
	}

	class SymbolWink extends TimerTask {
		private Set<Element> sets = new HashSet<Element>();
		private EditorAdapter editor = null;

		/**
		 * @return the sets
		 */
		public Set<Element> getSets() {
			return sets;
		}

		public SymbolWink(Set<Element> sets, EditorAdapter editor) {
			this.sets.addAll(sets);
			this.editor = editor;
		}

		public void run() {
			Iterator<Element> iterators = sets.iterator();
			while (iterators.hasNext()) {
				Element element = iterators.next();
				String str = element.getAttribute("opacity");
				if (str == null || str.length() == 0)
					element.setAttribute("opacity", "0");
				else if (str.substring(0, 1).equals("0")) {
					element.setAttribute("opacity", "1");
				} else
					element.setAttribute("opacity", "0");
			}
			editor.getSvgSession().refreshCurrentHandleImediately();
		}
	}

	/**
	 * the constructor of the class
	 * 
	 * @param handle
	 *            the svg handle that is associated to the selection object
	 */
	public Selection(final SVGHandle handle) {

		this.svgHandle = handle;
		selectionManager = handle.getEditor().getSelectionManager();
		i = serial;
		serial++;

		// creating the selection managers
		singleSelectionManager = new SingleSelectionManager(this);
		multiSelectionManager = new MultiSelectionManager(this);

		// getting the parent element of the nodes that will be selected
		setParentElement(handle.getCanvas().getDocument().getDocumentElement(),
				false);

		// creating the painter of the selection items
		selectionItemsPainter = new SelectionItemsCanvasPainter();

		// add by yux,2008.12.30
		terminalItemsPainter = new TerminalItemsCanvasPainter();

		// creating the painters of the selection zone rectangle
		ghostSelectionZoneCanvasPainter1 = new GhostSelectionZoneCanvasPainter();
		ghostSelectionZoneCanvasPainter2 = new GhostSelectionZoneCanvasPainter();
		ghostSelectionZoneCanvasPainter3 = new GhostSelectionZoneCanvasPainter();
		ghostSelectionZoneCanvasPainter4 = new GhostSelectionZoneCanvasPainter();

		// creating the selection listeners
		selectionListener = new SelectionListener(this);
		keySelectionListener = new KeySelectionListener(this);
		businessSelections = new Vector<BusinessSelectionIfc>();
		modeSelectionListeners = new Vector<ModeSelectionListener>();
		mouseMoveListeners = new Vector<ModeSelectionListener>();
		mouseReleasedListeners = new Vector<ModeSelectionListener>();
		mouseClickedListeners = new Vector<ModeSelectionListener>();
		mousePressedListeners = new Vector<ModeSelectionListener>();
		graphicTranslationListeners = new Vector<GraphicTranslationListener>();
		SVGCanvas canvas = handle.getCanvas();
		JViewport viewport = canvas.getScrollPane().getViewport();

		canvas.addMouseListener(selectionListener);
		canvas.addMouseMotionListener(selectionListener);
		viewport.addMouseListener(selectionListener);
		viewport.addMouseMotionListener(selectionListener);

	}

	/**
	 * disposes the selection resources
	 */
	public void dispose() {

		selectionChangedListeners.clear();

		SVGCanvas canvas = svgHandle.getCanvas();
		JViewport viewport = canvas.getScrollPane().getViewport();

		canvas.removeMouseListener(selectionListener);
		canvas.removeMouseMotionListener(selectionListener);
		keySelectionListener.dispose();
		viewport.removeMouseListener(selectionListener);
		viewport.removeMouseMotionListener(selectionListener);

		selectionManager = null;
		svgHandle = null;
		selectionListener = null;
		keySelectionListener = null;
		singleSelectionManager = null;
		multiSelectionManager = null;
		parentElement = null;
		selectedElements = null;
		orderedSelectedElements = null;
		unLockedSelectedElements = null;
		lockedElements = null;
		selectionItems = null;
		actionSelectionManager = null;
		ghostSelectionZoneCanvasPainter1 = null;
		ghostSelectionZoneCanvasPainter2 = null;
		ghostSelectionZoneCanvasPainter3 = null;
		ghostSelectionZoneCanvasPainter4 = null;
		selectionChangedListeners = null;
		selectionItemsPainter = null;
		terminalItemsPainter = null;
		firstPoint = null;
		actionItem = null;
		translationElements = null;
		businessSelections = null;
		modeSelectionListeners = null;
		mouseMoveListeners = null;
		mouseReleasedListeners = null;
	}

	/**
	 * @return the parent element of the edited nodes
	 */
	public Element getParentElement() {

		return parentElement;
	}

	/**
	 * sets the new parent element
	 * 
	 * @param parentElement
	 *            the new parent element
	 * @param notify
	 *            whether the parent modification event should be propagated
	 */
	public void setParentElement(Element parentElement, boolean notify) {

		this.parentElement = parentElement;

		if (notify) {

			svgHandle.getCanvas().notifyParentElementChanged();
			clearSelection();
		}
	}

	/**
	 * @return the selection submode
	 */
	public int getSelectionSubMode() {

		return selectionSubMode;
	}

	/**
	 * @return the selection manager corresponding to the current selection
	 */
	public SelectionManager getSelectionManager() {

		if (selectedElements.size() == 1) {

			return singleSelectionManager;

		} else if (selectedElements.size() > 1) {
			boolean single = true;
			Iterator<Element> it = selectedElements.iterator();
			while (it.hasNext()) {
				Element e = it.next();
				if (EditorToolkit.isElementsSingleSelect(e) == false) {
					single = false;
					break;
				}
			}
			if (single)
				return singleSelectionManager;

			return multiSelectionManager;
		}

		return null;
	}

	/**
	 * sets the new selection mode
	 * 
	 * @param selectionSubMode
	 *            the new selection sub mode
	 */
	public void setSelectionSubMode(int selectionSubMode) {

		this.selectionSubMode = selectionSubMode;

		if (svgHandle.getEditor().getSelectionManager().getSelectionMode() == SelectionInfoManager.ITEMS_ACTION_MODE) {

			// refreshing the selection
			selectionLevel = SELECTION_LEVEL_ITEMS_ACTION;
			refreshSelection(true);

		} else if (svgHandle.getEditor().getSelectionManager()
				.getSelectionMode() == SelectionInfoManager.DRAWING_MODE) {

			// refreshing the selection
			selectionLevel = SELECTION_LEVEL_DRAWING;
			refreshSelection(true);

		} else if (selectionLevel == SELECTION_LEVEL_ITEMS_ACTION
				|| selectionLevel == SELECTION_LEVEL_DRAWING) {

			// refreshing the selection
			selectionLevel = SELECTION_LEVEL_1;
			refreshSelection(true);
		}
	}

	/**
	 * notifies that the selection mode has changed. 触发选择模式变化的事件
	 */
	public void selectionModeChanged() {

		// getting the set of all the shape modules
		Set<AbstractShape> modules = svgHandle.getEditor().getShapeModules();

		// notifying all the shape modules that the selection mode has changed
		for (AbstractShape shapeModule : modules) {

			if (shapeModule != null) {

				shapeModule.notifyDrawingAction(svgHandle, null, 0,
						AbstractShape.DRAWING_END);
			}
		}
	}

	/**
	 * sets whether the selection painters should be blocked
	 * 
	 * @param blockSelectionItemsPaint
	 *            whether the selection painters should be blocked
	 */
	public void setBlockSelectionItemsPaint(boolean blockSelectionItemsPaint) {

		if (blockSelectionItemsPaint) {

			selectionItemsPainter.clean();

		} else {

			selectionItemsPainter.reinitialize();
		}
	}

	/**
	 * handles the selection of the element that can be found at the given point
	 * 
	 * @param currentPoint
	 *            a mouse point
	 * @param isMultiSelection
	 *            whether the multi selection has been activated or not
	 */
	@SuppressWarnings("deprecation")
	public void setSelection(Point2D currentPoint, boolean isMultiSelection) {

		// converting the given point into the user space units
		Point2D scaledPoint = svgHandle.getTransformsManager().getScaledPoint(
				currentPoint, true);
		// getting the element that can be found at the given location
		Element element = svgHandle.getSvgElementsManager().getNodeAt(
				parentElement, scaledPoint, selectionFilter, groupBreaker);// ,false,null,isMultiSelection);
		boolean keepSelectionLevel = false;

		if (element != null) {
			String str = element.getNodeName();
			if (str.equals("g")) {
				String strID = element.getAttribute("id");
				if (strID != null && strID.equals("nci:terminal")) {
					return;
				}

				// NodeList gChildren = element.getElementsByTagName("g");
				// if(gChildren.getLength()==0){
				// NodeList children = element.getChildNodes();
				// for(int i=0;i<children.getLength();i++){
				// Node child = children.item(i);
				// if(child instanceof Element){
				// if(!((Element)child).getNodeName().equalsIgnoreCase("metadata"
				// )
				// ){
				// // Utilities.printNode(child);
				// element = (Element)child;
				// break;
				// }
				// }
				// }
				// }
			}
			if (str.equals("a")) {

				// svgHandle.getEditor().getSvgSession().openRemoteSVGFileOld("",
				// Utilities.getOneAttributeValueWithColone(element,
				// "xlink:href"));

			}
			if (str.equals("path") || str.equals("use")) {
				keepSelectionLevel = true;
			}
		}
		// Utilities.printNode(element);
		// handling the selection
		handleSelection(element, isMultiSelection, keepSelectionLevel);

		// enabling the actions and translations support
		actionDisabled = false;
	}

	public void handleSelection(String strSymbolID) {

		Element element = svgHandle.getCanvas().getMapInfo(strSymbolID);
		if (element != null)
			handleSelection(element, false, false);
	}

	public void handleSelection(ArrayList<String> list) {

		for (int i = 0; i < list.size(); i++) {
			String strSymbolID = list.get(i);
			Element element = null;
			try {
				element = (Element) Utilities.findNode("//*[*='" + strSymbolID
						+ "']", svgHandle.getCanvas().getDocument()
						.getDocumentElement());
			} catch (XPathExpressionException e) {

				e.printStackTrace();
			}
			if (element != null)
				handleSelection(element, true, false);
		}
	}

	/**
	 * handles the selection of the given element
	 * 
	 * @param element
	 *            an element
	 * @param isMultiSelection
	 *            whether the multi selection has been activated or not
	 * @param keepSelectionLevel
	 *            whether the selection level should be kept or not
	 */
	public void handleSelection(Element element, boolean isMultiSelection,
			boolean keepSelectionLevel) {

		if (element == null) {

			// clearing the selection
			selectedElements.clear();
			orderedSelectedElements.clear();

		} else {

			if (isMultiSelection) {

				// if the multi selection mode is enabled//
				if (selectedElements.contains(element)) {

					// as the element was already selected, it is removed from
					// the selection
					selectedElements.remove(element);
					orderedSelectedElements.remove(element);

				} else {

					// the element is added to the selection
					selectedElements.add(element);
					orderedSelectedElements.add(element);
				}

			} else {

				// checking if the given element is already selected, if then
				// the selection level is incremented
				if (selectedElements.contains(element)) {
					// getting the selection manager corresponding to the
					// current selection
					SelectionManager selManager = getSelectionManager();

					// getting the next selection level for the current
					// selection
					if (!keepSelectionLevel) {

						selectionLevel = selManager
								.getNextSelectionLevel(selectedElements);
					}

				} else {

					// clearing the selection
					selectedElements.clear();
					orderedSelectedElements.clear();

					// adding the new element to the selection
					selectedElements.add(element);
					orderedSelectedElements.add(element);

					// resetting the selection level
					if (!keepSelectionLevel) {

						selectionLevel = 0;
					}
				}
			}
		}

		refreshSelection(true);

	}

	/**
	 * handles the selection of the given elements
	 * 
	 * @param elements
	 *            a set of elements
	 * @param isMultiSelection
	 *            whether the multi selection has been activated or not
	 * @param propagate
	 *            whether to propagate the event of the refresh selection action
	 *            or not
	 */
	public void handleSelection(Set<Element> elements,
			boolean isMultiSelection, boolean propagate) {

		if (elements == null || elements.size() == 0) {

			// clearing the selection
			selectedElements.clear();
			orderedSelectedElements.clear();

		} else {

			if (!isMultiSelection) {

				// clearing the selection
				selectedElements.clear();
				orderedSelectedElements.clear();
			}

			for (Element element : elements) {

				if (isMultiSelection && selectedElements.contains(element)) {

					// as when are in the multiselection mode and the element
					// was
					// already selected, it is removed from the selection
					selectedElements.remove(element);
					orderedSelectedElements.remove(element);

				} else {

					// the element is added to the selection
					selectedElements.add(element);
					orderedSelectedElements.add(element);
				}
			}
		}

		refreshSelection(propagate);
		// fireBusinessSelection();
	}

	public void handleSelectionAlwaysSingle(Set<Element> elements,
			boolean propagate) {

		selectedElements.clear();
		orderedSelectedElements.clear();
		if (elements != null || elements.size() > 0) {

			for (Element element : elements) {

				if (selectedElements.contains(element)) {

					// as when are in the multiselection mode and the element
					// was
					// already selected, it is removed from the selection
					selectedElements.remove(element);
					orderedSelectedElements.remove(element);

				} else {

					// the element is added to the selection
					selectedElements.add(element);
					orderedSelectedElements.add(element);
				}
			}
		}

		refreshSelection(propagate);
	}

	/**
	 * selects all the elements that can be found in the given rectangle
	 * 
	 * @param rectangle
	 *            an area on the canvas
	 * @param isMultiSelectionEnabled
	 *            whether the multi selection is enabled
	 */
	public void handleSelection(Rectangle2D rectangle,
			boolean isMultiSelectionEnabled) {

		// reinitializing the selection level
		selectionLevel = SELECTION_LEVEL_1;

		// getting the corresponding rectangle in user space
		Rectangle2D scaledRectangle = svgHandle.getTransformsManager()
				.getScaledRectangle(rectangle, true);

		// getting all the elements that can be found in the given rectangle in
		// the canvas
		Rectangle2D r2 = null;

		// the set of all the elements that can be found in the given area
		Set<Element> elements = new HashSet<Element>();
		Element element = null;

		for (Node current = parentElement.getFirstChild(); current != null; current = current
				.getNextSibling()) {

			if (current instanceof Element) {

				element = (Element) current;
				if (element.getNodeName().equals("g")
						&& (element.getAttribute("id").equals("allshape") || element
								.getAttribute("id").toLowerCase().indexOf(
										"layer") > -1)) {
					handleChildSelection(element, elements, scaledRectangle);
					// for (Node cur = element.getFirstChild(); cur != null; cur
					// = cur
					// .getNextSibling()) {
					// if (cur instanceof Element) {
					// Element gElement = (Element) cur;
					// if (gElement.getNodeName().equals("g")) {
					// if (gElement.getAttribute("id").toLowerCase()
					// .equals("head_layer")) {
					// continue;
					// } else if (gElement.getAttribute("id")
					// .toLowerCase().indexOf("layer") > -1) {
					// handleChildSelection(gElement, elements,
					// scaledRectangle);
					//
					// }
					// }
					// }
					// }
				} else if (EditorToolkit.isElementAShape(element)) {

					r2 = svgHandle.getSvgElementsManager()
							.getNodeGeometryBounds(element);

					if (r2 != null) {

						Rectangle2D r3 = new Rectangle2D.Double(r2.getX(), r2
								.getY(), r2.getWidth() + 1, r2.getHeight() + 1);

						// if the node is contained in the rectangle, it is
						// selected
						if (scaledRectangle.contains(r3)) {

							elements.add(element);
						}
					}
				}
			}
		}

		handleSelection(elements, isMultiSelectionEnabled, true);

		if (!svgHandle.getEditor().getRemanentModeManager().isRemanentMode()) {

			svgHandle.getEditor().getSelectionManager().setToRegularMode();
		}
	}

	private void handleChildSelection(Element element, Set<Element> elements,
			Rectangle2D scaledRectangle) {
		Rectangle2D r2 = null;

		for (Node current = element.getFirstChild(); current != null; current = current
				.getNextSibling()) {

			if (current instanceof Element) {

				element = (Element) current;

				if (EditorToolkit.isElementAShape(element)) {

					r2 = svgHandle.getSvgElementsManager()
							.getNodeGeometryBounds(element);

					if (r2 != null) {

						Rectangle2D r3 = new Rectangle2D.Double(r2.getX(), r2
								.getY(), r2.getWidth() + 1, r2.getHeight() + 1);

						// if the node is contained in the rectangle, it is
						// selected
						if (scaledRectangle.contains(r3)) {

							elements.add(element);
						}
					}
				}
			}
		}
	}

	/**
	 * selects all the elements that could be selected
	 * 
	 */
	public void selectAllElements() {

		// clearing the selected elements
		selectedElements.clear();
		orderedSelectedElements.clear();

		Set<Element> elementsToSelect = new HashSet<Element>();

		// computing the set of the elements to be selected
		for (Node node = parentElement.getFirstChild(); node != null; node = node
				.getNextSibling()) {

			if (node instanceof Element
					&& EditorToolkit.isElementAShape((Element) node)) {

				elementsToSelect.add((Element) node);
			}
		}

		handleSelection(elementsToSelect, true, true);
	}

	/**
	 * removes all the selected items from the selection
	 */
	public void clearSelection() {
		selectedElements.clear();
		orderedSelectedElements.clear();
		refreshSelection(true);
	}

	/**
	 * refreshes the selection items
	 * 
	 * @param propagate
	 *            whether to propagate the event of the refresh selection action
	 *            or not
	 */
	public void refreshSelection(boolean propagate) {

		selectionItemsPainter.clear();
		selectionItems.clear();
		unLockedSelectedElements.clear();

		// checking if all the elements whose selected items
		// should be handle, have not be deleted
		for (Element element : new HashSet<Element>(selectedElements)) {

			if (element.getParentNode() == null) {

				selectedElements.remove(element);
				orderedSelectedElements.remove(element);
			}
		}

		// checking if all the locked elements have not been deleted
		for (Element element : new HashSet<Element>(lockedElements)) {

			if (element.getParentNode() == null) {

				lockedElements.remove(element);
			}
		}

		if (selectedElements.size() > 0) {

			// creating the set of the unlocked elements
			for (Element element : selectedElements) {

				if (!lockedElements.contains(element)) {

					unLockedSelectedElements.add(element);
				}
			}

			// 计算出有多少个边角需要显示 //mark
			Set<SelectionItem> items = getSelectionManager().getSelectionItems(
					svgHandle, new HashSet<Element>(selectedElements),
					selectionLevel);

			if (items != null) {
				// 需要显示的边角
				selectionItems.addAll(items);
			}
		}

		// reinitializing the selection items painter
		selectionItemsPainter.reinitialize();
		// svgHandle.getCanvas().refreshCanvasContent(true, false, null);

		if (propagate) {
			// notifies all the selection changed listeners that the selection
			// has changed
			notifySelectionChanged();
		}
	}

	public void refreshSelectElement(Set<SelectionItem> items) {
		selectionItemsPainter.clear();
		selectionItems.clear();
		unLockedSelectedElements.clear();
		if (items != null) {
			// 需要显示的边角
			selectionItems.addAll(items);
		}

		selectionItemsPainter.reinitialize();
	}

	/**
	 * adds a new selection changed listener
	 * 
	 * @param listener
	 *            a selection changed listener
	 */
	public void addSelectionChangedListener(SelectionChangedListener listener) {

		if (selectionChangedListeners != null) {

			selectionChangedListeners.add(listener);
		}
	}

	/**
	 * removes a selection changed listener
	 * 
	 * @param listener
	 *            a selection changed listener
	 */
	public void removeSelectionChangedListener(SelectionChangedListener listener) {

		if (selectionChangedListeners != null) {

			selectionChangedListeners.remove(listener);
		}
	}

	/**
	 * notifies all the selection changed listeners that the selection has
	 * changed
	 */
	public void notifySelectionChanged() {

		for (SelectionChangedListener listener : selectionChangedListeners) {

			listener.selectionChanged(new HashSet<Element>(selectedElements));
		}
	}

	/**
	 * notifies that the mouse button has been pressed while in the drawing mode
	 * 
	 * @param point
	 *            a mouse point
	 * @param modifier
	 *            a key modifier
	 * @param type
	 *            the type of the drawing action
	 */
	public void drawingAction(Point2D point, int modifier, int type) {

		// getting the current drawing shape
		fr.itris.glips.svgeditor.shape.AbstractShape drawingShape = selectionManager
				.getDrawingShape();
		if (drawingShape != null) {

			drawingShape.notifyDrawingAction(svgHandle, point, modifier, type);
		}

	}

	/**
	 * notifies that a selection items action has occured
	 * 
	 * @param point
	 *            a mouse point
	 */
	public void itemsAction(Point2D point) {

		// getting the selection item corresponding to this point
		SelectionItem item = getSelectionItem(point);

		if (item != null) {

			// notifying the shape that handles the element
			// corresponding to the item that an items action has occured//

			// getting the element of the selection item
			Element element = item.getElements().iterator().next();

			// getting the shape module corresponding to the element
			AbstractShape shape = ShapeToolkit.getShapeModule(element,
					svgHandle.getEditor());

			if (shape != null) {

				// notifying the shape module that an item action occured
				shape.notifyItemsAction(svgHandle, item);
			}

			// refreshing the selection
			refreshSelection(true);
		}
	}

	/**
	 * draws a selection zone
	 * 
	 * @param currentPoint
	 *            the current point
	 * @param type
	 *            the type of the action
	 */
	public void handleZoomZone(Point2D currentPoint, int type) {

		// getting the canvas
		SVGCanvas canvas = svgHandle.getScrollPane().getSVGCanvas();

		switch (type) {

		case SELECTION_ZONE_MOUSE_PRESSED:

			// initializing the action
			firstPoint = currentPoint;
			break;

		case SELECTION_ZONE_MOUSE_RELEASED:

			// computing the rectangle corresponding the first and current
			// clicked points
			Rectangle2D rect = EditorToolkit.getComputedRectangle(firstPoint,
					currentPoint);
			rect = svgHandle.getTransformsManager().getScaledRectangle(rect,
					true);

			// setting the current scale for the canvas
			canvas.getZoomManager().scaleTo(rect);

			// reinitializing the data
			firstPoint = null;
			canvas.removePaintListener(ghostSelectionZoneCanvasPainter1, true);
			canvas.removePaintListener(ghostSelectionZoneCanvasPainter2, true);
			canvas.removePaintListener(ghostSelectionZoneCanvasPainter3, true);
			canvas.removePaintListener(ghostSelectionZoneCanvasPainter4, true);
			ghostSelectionZoneCanvasPainter1.reinitialize();
			ghostSelectionZoneCanvasPainter2.reinitialize();
			ghostSelectionZoneCanvasPainter3.reinitialize();
			ghostSelectionZoneCanvasPainter4.reinitialize();

			if (!svgHandle.getEditor().getRemanentModeManager()
					.isRemanentMode()) {

				svgHandle.getEditor().getSelectionManager().setToRegularMode();
			}

			break;

		case SELECTION_ZONE_MOUSE_DRAGGED:

			// computing the current rectangle
			rect = EditorToolkit.getComputedRectangle(firstPoint, currentPoint);

			// drawing the new zone ghost rectangle//
			canvas.removePaintListener(ghostSelectionZoneCanvasPainter1, false);
			canvas.removePaintListener(ghostSelectionZoneCanvasPainter2, false);
			canvas.removePaintListener(ghostSelectionZoneCanvasPainter3, false);
			canvas.removePaintListener(ghostSelectionZoneCanvasPainter4, false);

			// computing the rectangle corresponding the first and current
			// clicked points
			rect = EditorToolkit.getComputedRectangle(firstPoint, currentPoint);

			// setting the new rectangle to the ghost painter
			ghostSelectionZoneCanvasPainter1.setPoints(new Point2D.Double(rect
					.getX(), rect.getY()), new Point2D.Double(
					(rect.getX() + rect.getWidth()), rect.getY()));
			ghostSelectionZoneCanvasPainter2.setPoints(new Point2D.Double(rect
					.getX()
					+ rect.getWidth(), rect.getY()), new Point2D.Double(rect
					.getX()
					+ rect.getWidth(), rect.getY() + rect.getHeight()));
			ghostSelectionZoneCanvasPainter3.setPoints(new Point2D.Double(rect
					.getX(), rect.getY() + rect.getHeight()),
					new Point2D.Double(rect.getX() + rect.getWidth(), rect
							.getY()
							+ rect.getHeight()));
			ghostSelectionZoneCanvasPainter4.setPoints(new Point2D.Double(rect
					.getX(), rect.getY()), new Point2D.Double(rect.getX(), rect
					.getY()
					+ rect.getHeight()));

			canvas.addLayerPaintListener(SVGCanvas.DRAW_LAYER,
					ghostSelectionZoneCanvasPainter1, true);
			canvas.addLayerPaintListener(SVGCanvas.DRAW_LAYER,
					ghostSelectionZoneCanvasPainter2, true);
			canvas.addLayerPaintListener(SVGCanvas.DRAW_LAYER,
					ghostSelectionZoneCanvasPainter3, true);
			canvas.addLayerPaintListener(SVGCanvas.DRAW_LAYER,
					ghostSelectionZoneCanvasPainter4, true);
			break;
		}
	}

	public void clearPoints() {
		pointit.clean();
	}

	/**
	 * draws a selection zone
	 * 
	 * @param currentPoint
	 *            the current point
	 * @param type
	 *            the type of the action
	 * @param isMultiSelectionEnabled
	 *            whether the multi selection is enabled
	 */
	public void handleSelectionZone(Point2D currentPoint, int type,
			boolean isMultiSelectionEnabled) {

		// getting the canvas
		SVGCanvas canvas = svgHandle.getScrollPane().getSVGCanvas();

		switch (type) {

		case SELECTION_ZONE_MOUSE_PRESSED:

			// initializing the action
			firstPoint = currentPoint;
			break;

		case SELECTION_ZONE_MOUSE_RELEASED:

			// computing the rectangle corresponding the first and current
			// clicked points 计算点击的位置
			Rectangle2D rect = EditorToolkit.getComputedRectangle(firstPoint,
					currentPoint);

			if (svgHandle.getEditor().getSelectionManager().getSelectionMode() != SelectionInfoManager.SCREEN_CAST_MODE) {

				// creating the svg rectangle shape，设置选择的图元
				handleSelection(rect, isMultiSelectionEnabled);

			} else {
				// 截图
				if (rect.getBounds().width > 0 && rect.getBounds().height > 0) {
					Point svgFrameScreenPoint = getSVGHandle().getCanvas()
							.getLocationOnScreen();
					// 坐标和长度都增减2个像素是为了防止框址时留下的虚线被截进去
					Rectangle originCastRect = new Rectangle((int) rect.getX()
							+ svgFrameScreenPoint.x + 2, (int) rect.getY()
							+ svgFrameScreenPoint.y + 2,
							(int) rect.getWidth() - 2,
							(int) rect.getHeight() - 2);
					Rectangle scroolBounds = svgHandle.getScrollPane()
							.getInnerScrollpane().getBounds();
					Point pScrool = svgHandle.getScrollPane()
							.getInnerScrollpane().getLocationOnScreen();
					scroolBounds = new Rectangle(pScrool.x, pScrool.y,
							scroolBounds.width, scroolBounds.height);
					Rectangle canvasBounds = svgHandle.getCanvas().getBounds();
					Point pCanvas = svgHandle.getCanvas().getLocationOnScreen();
					canvasBounds = new Rectangle(pCanvas.x, pCanvas.y,
							canvasBounds.width, canvasBounds.height);
					// scroolpane , svgcanvas和选定的rectangle区域三者相交形成真正可以截取的区域
					Rectangle rectCanbeCast = scroolBounds
							.intersection(canvasBounds);
					originCastRect = rectCanbeCast.intersection(originCastRect);
					BufferedImage bi = Utilities.getRobot()
							.createScreenCapture(originCastRect);
					ImageTransferable clipContent = new ImageTransferable(bi);
					Toolkit.getDefaultToolkit().getSystemClipboard()
							.setContents(clipContent, clipContent);
					((ScreenCastModuleAdapter) svgHandle.getEditor().getModule(
							ScreenCastModuleAdapter.ScreenCastModuleID))
							.showPromptInfo();
				}

			}

			// reinitializing the data
			firstPoint = null;
			canvas.removePaintListener(ghostSelectionZoneCanvasPainter1, true);
			canvas.removePaintListener(ghostSelectionZoneCanvasPainter2, true);
			canvas.removePaintListener(ghostSelectionZoneCanvasPainter3, true);
			canvas.removePaintListener(ghostSelectionZoneCanvasPainter4, true);
			ghostSelectionZoneCanvasPainter1.reinitialize();
			ghostSelectionZoneCanvasPainter2.reinitialize();
			ghostSelectionZoneCanvasPainter3.reinitialize();
			ghostSelectionZoneCanvasPainter4.reinitialize();

			break;

		case SELECTION_ZONE_MOUSE_DRAGGED:

			// computing the current rectangle
			rect = EditorToolkit.getComputedRectangle(firstPoint, currentPoint);

			// drawing the new zone ghost rectangle//
			canvas.removePaintListener(ghostSelectionZoneCanvasPainter1, false);
			canvas.removePaintListener(ghostSelectionZoneCanvasPainter2, false);
			canvas.removePaintListener(ghostSelectionZoneCanvasPainter3, false);
			canvas.removePaintListener(ghostSelectionZoneCanvasPainter4, false);

			// computing the rectangle corresponding the first and current
			// clicked points
			rect = EditorToolkit.getComputedRectangle(firstPoint, currentPoint);

			// setting the new rectangle to the ghost painter
			ghostSelectionZoneCanvasPainter1.setPoints(new Point2D.Double(rect
					.getX(), rect.getY()), new Point2D.Double(
					(rect.getX() + rect.getWidth()), rect.getY()));
			ghostSelectionZoneCanvasPainter2.setPoints(new Point2D.Double(rect
					.getX()
					+ rect.getWidth(), rect.getY()), new Point2D.Double(rect
					.getX()
					+ rect.getWidth(), rect.getY() + rect.getHeight()));
			ghostSelectionZoneCanvasPainter3.setPoints(new Point2D.Double(rect
					.getX(), rect.getY() + rect.getHeight()),
					new Point2D.Double(rect.getX() + rect.getWidth(), rect
							.getY()
							+ rect.getHeight()));
			ghostSelectionZoneCanvasPainter4.setPoints(new Point2D.Double(rect
					.getX(), rect.getY()), new Point2D.Double(rect.getX(), rect
					.getY()
					+ rect.getHeight()));

			canvas.addLayerPaintListener(SVGCanvas.DRAW_LAYER,
					ghostSelectionZoneCanvasPainter1, true);
			canvas.addLayerPaintListener(SVGCanvas.DRAW_LAYER,
					ghostSelectionZoneCanvasPainter2, true);
			canvas.addLayerPaintListener(SVGCanvas.DRAW_LAYER,
					ghostSelectionZoneCanvasPainter3, true);
			canvas.addLayerPaintListener(SVGCanvas.DRAW_LAYER,
					ghostSelectionZoneCanvasPainter4, true);
			break;
		}

	}

	/**
	 * executes an action according to the item that was selected by the user
	 * and the point where the mouse can be found
	 * 
	 * @param currentPoint
	 *            the current position of the mouse
	 */
	public void doAction(Point2D currentPoint) {

		if (!actionDisabled && !isSelectionLocked()) {

			// converting the given point into the user space units
			Point2D scaledPoint = svgHandle.getTransformsManager()
					.getScaledPoint(currentPoint, true);

			if (firstPoint == null) {

				// initializing the action//

				// storing the first point
				firstPoint = scaledPoint;

				// checking if the given point matches a selection item
				actionItem = getSelectionItem(currentPoint);

				// checking if the given point is included in a shape//
				Element translationElement = null;

				if (actionItem == null) {

					// getting the element that can be found at the given
					// location
					translationElement = svgHandle.getSvgElementsManager()
							.getNodeAt(parentElement, scaledPoint);
					// fireGraphicTranslation(elementsTraslated)
					if (translationElement != null) {
						String str = translationElement.getNodeName();
						if (str.equals("g")) {
							String strID = translationElement
									.getAttribute("id");
							if (strID != null && strID.equals("nci:terminal")) {
								return;
							}
						}
					}

					if (translationElement != null
							&& !lockedElements.contains(translationElement)) {

						// checking if this element is already selected,
						// then all the selected elements will be translated
						if (selectedElements.contains(translationElement)
								|| selectedElements
										.contains((Element) translationElement
												.getParentNode())) {

							translationElements
									.addAll(unLockedSelectedElements);

						} else {

							// otherwise, all the selected elements will be
							// deselected,
							// and the found element will be translated
							selectedElements.clear();
							orderedSelectedElements.clear();
							refreshSelection(true);
							translationElements.add(translationElement);
						}
					}
				}

				// checking if a selection item or an element matches the point,
				// if not, exits the method
				if (actionItem == null && translationElements.size() == 0) {

					// reinitializes the data and returns
					firstPoint = null;
					setSelectionSubMode(REGULAR_SUB_MODE);
					actionDisabled = true;
					return;
				}

				if (selectionSubMode == REGULAR_SUB_MODE) {

					// computing the new type of the action
					if (actionItem != null) {

						setSelectionSubMode(ACTION_SUB_MODE);

					} else {

						setSelectionSubMode(TRANSLATION_SUB_MODE);
					}
				}

				// getting the selection manager
				switch (selectionSubMode) {

				case TRANSLATION_SUB_MODE:

					actionSelectionManager = (translationElements.size() > 1 ? multiSelectionManager
							: singleSelectionManager);
					// actionSelectionManager = singleSelectionManager;
					break;

				case ACTION_SUB_MODE:

					actionSelectionManager = getSelectionManager();
					break;
				}
			}

			// checking if the selection items should be still painted or not
			setBlockSelectionItemsPaint(!(actionItem != null && actionItem
					.isPersistent()));

			// handling the action for the current point of the drag action
			switch (selectionSubMode) {

			case TRANSLATION_SUB_MODE: {

				// translating the elements

				actionSelectionManager.doTranslateAction(translationElements,
						firstPoint, scaledPoint);
				// // ADDED by wangql
				fireGraphicTranslating(translationElements, firstPoint,
						scaledPoint);

				if (translationElements.size() == 1) {
					Set<SelectionItem> sets = new HashSet<SelectionItem>();
					Element element = translationElements.iterator().next();
					if (element != null
							&& element.getNodeName().equals("path")
							&& element.getAttribute("d").toLowerCase().indexOf(
									'z') == -1) {
						// 计算偏移量
						int xoffset = (int) (scaledPoint.getX() - firstPoint
								.getX());
						int yoffset = (int) (scaledPoint.getY() - firstPoint
								.getY());
						// 线拖动时
						Path path = new Path(element.getAttribute("d"));
						int size = path.getSegmentsNumber();
						Segment segment = path.getSegment();
						Point2D beginPoint = new Point2D.Double(segment
								.getEndPoint().getX()
								+ xoffset, segment.getEndPoint().getY()
								+ yoffset);
						for (int i = 0; i < size - 1; i++) {
							segment = segment.getNextSegment();
						}
						Point2D endPoint = new Point2D.Double(segment
								.getEndPoint().getX()
								+ xoffset, segment.getEndPoint().getY()
								+ yoffset);

						Set<SelectionItem> showSets = null;
						showSets = getConnectNode(beginPoint);
						if (showSets == null || showSets.size() == 0) {
							showSets = getTerminal(beginPoint);
						}
						if (showSets != null && showSets.size() > 0)
							sets.addAll(showSets);
						showSets = getConnectNode(endPoint);
						if (showSets == null || showSets.size() == 0) {
							showSets = getTerminal(endPoint);
						}
						if (showSets != null && showSets.size() > 0)
							sets.addAll(showSets);

					}
					terminalItemsPainter.clear();
					terminalItems.clear();
					terminalItems.addAll(sets);
					terminalItemsPainter.reinitialize();
				}
				break;
			}

			case ACTION_SUB_MODE: {

				// doing the action
				AbstractShape shape = actionSelectionManager.doAction(
						new HashSet<Element>(selectedElements), firstPoint,
						scaledPoint, actionItem);
				if (shape != null && shape.isNodeConnectable()) {
					Element element = shape.getActionElements(selectedElements,
							actionItem).iterator().next();
					if (element != null
							&& element.getNodeName().equals("path")
							&& element.getAttribute("d").toLowerCase().indexOf(
									'z') == -1 && actionItem != null) {
						// 只移动线头时
						Point2D scalePoint = svgHandle.getTransformsManager()
								.getScaledPoint(currentPoint, true);
						int index = actionItem.getIndex() / 10;
						Path path = new Path(element.getAttribute("d"));
						int size = path.getSegmentsNumber();
						if (index == 0 || index == size - 1) {
							Set<SelectionItem> sets = new HashSet<SelectionItem>();
							Set<SelectionItem> showSets = null;
							showSets = getConnectNode(scalePoint);
							if (showSets == null || showSets.size() == 0) {
								showSets = getTerminal(scalePoint);
							}
							if (showSets != null && showSets.size() > 0) {
								sets.addAll(showSets);
							}

							terminalItemsPainter.clear();
							terminalItems.clear();
							terminalItems.addAll(sets);
							terminalItemsPainter.reinitialize();
						}
					}
				}
				break;
			}
			}
		}
	}

	/**
	 * 根据已选中的图元，计算混合矩形信息
	 * 
	 * @return:矩形信息
	 */
	public Rectangle2D getBounds() {

		return MultiAbstractShape
				.getElementsBounds(svgHandle, selectedElements);
	}

	/**
	 * executes an action according to the item that was selected by the user
	 * and the point where the mouse can be found
	 * 
	 * @param currentPoint
	 *            the current position of the mouse
	 */
	public void validateAction(Point2D currentPoint) {

		if (!isSelectionLocked()) {

			// converting the given point into the user space units
			final Point2D scaledPoint = svgHandle.getTransformsManager()
					.getScaledPoint(currentPoint, true);

			if (actionSelectionManager != null) {

				// validating the action
				switch (selectionSubMode) {

				case TRANSLATION_SUB_MODE:

					// translating the elements
					if (selectionListener.isBControl()) {
						actionSelectionManager.copyElement(translationElements,
								firstPoint);
					} else {

						actionSelectionManager.validateTranslateAction(
								translationElements, firstPoint, scaledPoint);
						svgHandle.getCanvas().modifyElements(
								translationElements);
						// SwingUtilities.invokeLater(new Runnable(){
						// public void run(){
						// fireGraphicTranslated(translationElements,firstPoint,
						// scaledPoint);
						// }
						// });
						fireGraphicTranslated(translationElements, firstPoint,
								scaledPoint);
						// add by yux,2009.1.5
						// 加上线拖动捕捉拓扑点
						Element element = null;
						if (translationElements.size() == 1) {
							element = translationElements.iterator().next();
							if (element != null
									&& element instanceof SVGOMPathElement
									&& element.getAttribute("d").toLowerCase()
											.indexOf('z') == -1) {
								// 计算偏移量
								int xoffset = (int) (scaledPoint.getX() - firstPoint
										.getX());
								int yoffset = (int) (scaledPoint.getY() - firstPoint
										.getY());
								// 线拖动时
								Path path = new Path(element.getAttribute("d"));
								int size = path.getSegmentsNumber();
								Segment segment = path.getSegment();
								Point2D beginPoint = new Point2D.Double(segment
										.getEndPoint().getX()
										+ xoffset, segment.getEndPoint().getY()
										+ yoffset);
								for (int i = 0; i < size - 1; i++) {
									segment = segment.getNextSegment();
								}
								Point2D endPoint = new Point2D.Double(segment
										.getEndPoint().getX()
										+ xoffset, segment.getEndPoint().getY()
										+ yoffset);
								TerminalInfo info = getConnectName(beginPoint);
								if (info != null) {
									svgHandle
											.getCanvas()
											.getLpManager()
											.addLinkPoint(
													element.getAttribute("id"),
													info.getElement()
															.getAttribute("id"),
													info.getTerminalName(),
													LinkPointManager.BEGIN_LINE_POINT);
									element.setAttribute(
											LinkPointManager.BEGIN_LINE_POINT,
											info.getElement()
													.getAttribute("id"));
									element
											.setAttribute(
													LinkPointManager.BEGIN_LINE_TERMINAL,
													info.getTerminalName());

								} else {
									svgHandle
											.getCanvas()
											.getLpManager()
											.removeLinkPoint(
													element.getAttribute("id"),
													LinkPointManager.BEGIN_LINE_POINT);
								}

								info = getConnectName(endPoint);
								if (info != null) {
									svgHandle
											.getCanvas()
											.getLpManager()
											.addLinkPoint(
													element.getAttribute("id"),
													info.getElement()
															.getAttribute("id"),
													info.getTerminalName(),
													LinkPointManager.END_LINE_POINT);
									element.setAttribute(
											LinkPointManager.END_LINE_POINT,
											info.getElement()
													.getAttribute("id"));
									element.setAttribute(
											LinkPointManager.END_LINE_TERMINAL,
											info.getTerminalName());
								} else {
									svgHandle
											.getCanvas()
											.getLpManager()
											.removeLinkPoint(
													element.getAttribute("id"),
													LinkPointManager.END_LINE_POINT);
								}
								break;
							}
						}
					}
					break;

				case ACTION_SUB_MODE:

					// doing the action
					actionSelectionManager.validateAction(new HashSet<Element>(
							selectedElements), firstPoint, scaledPoint,
							actionItem);
					svgHandle.getCanvas().modifyElements(selectedElements);
					// add by yux,2009.1.5
					// 加上线移动捕捉拓扑点
					if (selectedElements.size() == 1) {
						Element element = selectedElements.iterator().next();
						if (element != null
								&& element instanceof SVGOMPathElement
								&& element.getAttribute("d").toLowerCase()
										.indexOf('z') == -1
								&& actionItem != null) {
							// 只移动线头时
							int index = actionItem.getIndex();
							Path path = new Path(element.getAttribute("d"));
							int size = path.getSegmentsNumber();
							if (index == 0) {

								TerminalInfo info = getConnectName(scaledPoint);
								if (info != null) {
									svgHandle
											.getCanvas()
											.getLpManager()
											.addLinkPoint(
													element.getAttribute("id"),
													info.getElement()
															.getAttribute("id"),
													info.getTerminalName(),
													LinkPointManager.BEGIN_LINE_POINT);
									element.setAttribute(
											LinkPointManager.BEGIN_LINE_POINT,
											info.getElement()
													.getAttribute("id"));
									element
											.setAttribute(
													LinkPointManager.BEGIN_LINE_TERMINAL,
													info.getTerminalName());

								} else {
									svgHandle
											.getCanvas()
											.getLpManager()
											.removeLinkPoint(
													element.getAttribute("id"),
													LinkPointManager.BEGIN_LINE_POINT);
								}
							} else if (index == size - 1) {
								TerminalInfo info = getConnectName(scaledPoint);
								if (info != null) {
									svgHandle
											.getCanvas()
											.getLpManager()
											.addLinkPoint(
													element.getAttribute("id"),
													info.getElement()
															.getAttribute("id"),
													info.getTerminalName(),
													LinkPointManager.END_LINE_POINT);
									element.setAttribute(
											LinkPointManager.END_LINE_POINT,
											info.getElement()
													.getAttribute("id"));
									element.setAttribute(
											LinkPointManager.END_LINE_TERMINAL,
											info.getTerminalName());

								} else {
									svgHandle
											.getCanvas()
											.getLpManager()
											.removeLinkPoint(
													element.getAttribute("id"),
													LinkPointManager.END_LINE_POINT);
								}
							}
							break;
						}
					}

					break;
				}
			}

			// reinitializing the data
			firstPoint = null;
			actionItem = null;
			actionSelectionManager = null;
			translationElements.clear();
			setSelectionSubMode(REGULAR_SUB_MODE);
		}
	}

	/**
	 * translates the currently selected points by the provided factors
	 * 
	 * @param delta
	 *            the translation factors
	 */
	public void translate(Point2D delta) {

		if (!isSelectionLocked()) {

			// getting the scaled translation factors
			final Point2D scaledDelta = svgHandle.getTransformsManager()
					.getScaledPoint(delta, true);

			// getting the current action manager
			actionSelectionManager = getSelectionManager();

			if (actionSelectionManager != null) {

				Iterator<Element> it = selectedElements.iterator();
				HashMap<Element, String> filterEleMap = new HashMap<Element, String>();
				while (it.hasNext()) {
					Element ele = it.next();
					String filter = ele.getAttribute("filter");
					if (filter != null && !filter.equals("")) {
						filterEleMap.put(ele, filter);
						ele.removeAttribute("filter");
					}
				}
				// translating the elements
				actionSelectionManager.validateTranslateAction(
						new HashSet<Element>(selectedElements),
						new Point(0, 0), scaledDelta);
				final Set<Element> temp = selectedElements;
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						fireGraphicTranslated(temp, new Point(0, 0),
								scaledDelta);

					}
				});
				// fireGraphicTranslated(temp,new Point(0,0), scaledDelta);
				Iterator<Element> filterEleIt = filterEleMap.keySet()
						.iterator();
				while (filterEleIt.hasNext()) {
					Element e = filterEleIt.next();
					String filter = filterEleMap.get(e);
					e.setAttributeNS(null, "filter", filter);
				}
			}
		}
	}

	/**
	 * refreshes the labels displaying the current position of the mouse on the
	 * canvas
	 * 
	 * @param currentMousePoint
	 *            the current mouse point
	 */
	public void refreshSVGFrame(Point currentMousePoint) {

		// converting the given point into the user space units
		Point2D scaledPoint = svgHandle.getTransformsManager().getScaledPoint(
				currentMousePoint, true);

		// refreshing the labels displaying the current position of the mouse on
		// the canvas
		svgHandle.getSVGFrame().getStateBar().setMousePosition(scaledPoint);
	}

	/**
	 * 添加业务选择处理
	 * 
	 * @param busiSel
	 */
	public void addBusinessSelection(BusinessSelectionIfc busiSel) {
		businessSelections.add(busiSel);
	}

	/**
	 * 注册选择事件
	 * 
	 * @param listener
	 */
	public void addModeSelectionListener(ModeSelectionListener listener) {
		modeSelectionListeners.add(listener);
	}

	/**
	 * 添加鼠标变化监听
	 * 
	 * @param listener
	 */
	public void addMouseMoveListener(ModeSelectionListener listener) {
		mouseMoveListeners.add(listener);
	}

	public void addMouseReleasedListener(
			ModeSelectionListener modeSelectionListener) {
		mouseReleasedListeners.add(modeSelectionListener);
	}

	public void addMouseClickedListener(
			ModeSelectionListener modeSelectionListener) {
		mouseClickedListeners.add(modeSelectionListener);
	}

	public void addMousePressedListener(
			ModeSelectionListener modeSelectionListener) {
		mousePressedListeners.add(modeSelectionListener);
	}

	public void addGraphicTranslationListener(GraphicTranslationListener lis) {
		graphicTranslationListeners.add(lis);
	}

	/**
	 * 触发所有当前注册的选择事件
	 * 
	 * @param evt
	 */
	public synchronized void fireModeSelection(ModeSelectionEvent evt) {
		for (ModeSelectionListener listener : modeSelectionListeners) {
			listener.modeSelected(evt);
		}
	}

	/**
	 * 触发鼠标移动时的光标变化监听
	 * 
	 * @param evt
	 */
	public synchronized void fireMouseMove(ModeSelectionEvent evt) {
		for (ModeSelectionListener listener : mouseMoveListeners) {
			listener.modeSelected(evt);
		}
	}

	public synchronized void fireMouseClicked(ModeSelectionEvent evt) {
		for (ModeSelectionListener lis : mouseClickedListeners) {
			lis.modeSelected(evt);
		}
	}

	public synchronized void fireMousePressed(ModeSelectionEvent evt) {
		for (ModeSelectionListener lis : mousePressedListeners) {
			lis.modeSelected(evt);
		}
	}

	/**
	 * mouse release触发
	 * 
	 * @param evt
	 */
	public synchronized void fireMouseReleased(ModeSelectionEvent evt) {
		for (ModeSelectionListener listener : mouseReleasedListeners) {
			listener.modeSelected(evt);
		}
	}

	/**
	 * 触发图形平移事件
	 * 
	 * @param elementsTraslated
	 *            平移的图形元素
	 */
	public synchronized void fireGraphicTranslated(
			Set<Element> elementsTraslated, Point2D firstPoint,
			Point2D scaledPoint) {
		for (GraphicTranslationListener l : graphicTranslationListeners) {
			l.graphicsTranslated(elementsTraslated, firstPoint, scaledPoint);
		}
	}

	public synchronized void fireGraphicTranslating(
			Set<Element> elementsTraslating, Point2D firstPoint,
			Point2D scaledPoint) {
		for (GraphicTranslationListener l : graphicTranslationListeners) {
			l.graphicsTranslating(elementsTraslating, firstPoint, scaledPoint);
		}
	}

	public void removeModeSelectionListener(ModeSelectionListener listener) {
		modeSelectionListeners.remove(listener);
	}

	/**
	 * 移除业务选择处理
	 * 
	 * @param busiSel
	 */
	public void removeBusinessSelection(BusinessSelectionIfc busiSel) {
		businessSelections.remove(busiSel);
	}

	/**
	 * 业务选择激发
	 */
	public synchronized void fireBusinessSelection() {

		// svgHandle.getEditor().getLogger().log(null, LoggerAdapter.DEBUG,
		// "fireBusinessSelection");
		for (BusinessSelectionIfc bs : businessSelections) {
			bs.handleSelection();
		}
	}

	/**
	 * 根据输入的线节点，及拖动形成的偏移量，绘制连接点或耦合点
	 * 
	 * @param element
	 *            ：线节点
	 * @param xoffset
	 *            ：横坐标偏移量
	 * @param yoffset
	 *            ：纵坐标偏移量
	 */
	// public void redrawNciPoints(Element element, int xoffset, int yoffset) {
	// if (!element.getNodeName().equals("path"))
	// return;
	//
	// String dPath = element.getAttribute("d");
	// Path path = new Path(dPath);
	// int nCount = path.getSegmentsNumber();
	// Segment seg = path.getSegment();
	// // bpoint为线节点起始点坐标，epoint为线节点终止点坐标
	// Point2D bpoint = seg.getEndPoint();
	// for (int i = 0; i < nCount - 1; i++)
	// seg = seg.getNextSegment();
	// Point2D epoint = seg.getEndPoint();
	//
	// bpoint.setLocation(bpoint.getX() + xoffset, bpoint.getY() + yoffset);
	// epoint.setLocation(epoint.getX() + xoffset, epoint.getY() + yoffset);
	//
	// Set<SelectionItem> items = new HashSet<SelectionItem>();
	// Set<SelectionItem> itemTemp = getPointItems(bpoint);
	// if (itemTemp != null)
	// items.addAll(itemTemp);
	//
	// itemTemp = getPointItems(epoint);
	// if (itemTemp != null)
	// items.addAll(itemTemp);
	//
	// pointit.reinitialize(items, SVGCanvas.TOP_LAYER);
	// return;
	// }
	/**
	 * 根据输入的坐标点，绘制图元连接点或耦合点
	 * 
	 * @param currentMousePoint
	 *            ：坐标点
	 */
	// public void redrawNciPoints(Point2D currentMousePoint) {
	//
	// Set<SelectionItem> items = getPointItems(currentMousePoint);
	//
	// pointit.reinitialize(items, SVGCanvas.TOP_LAYER);
	// return;
	// }
	//
	// public void redrawNciPoints(Set<SelectionItem> items, int nLayer) {
	//
	// pointit.reinitialize(items, nLayer);
	// return;
	// }
	// public Set<SelectionItem> getPointItems(Point2D currentPoint) {
	// Set<SelectionItem> items = null;
	// // items = getNciPointItems(currentPoint);
	// if (items == null) {
	// items = getLinkPointItems(currentPoint);
	// }
	// return items;
	// }
	/**
	 * 根据输入的坐标点，生成需要绘制的图元连接点或耦合点
	 * 
	 * @param currentPoint
	 *            ：输入的坐标点
	 * @return：如果有符合该点要求的图元连接点或耦合点则返回set，失败或异常返回null
	 */
	// public Set<SelectionItem> getNciPointItems(Point2D currentPoint) {
	// Element element = svgHandle.getSvgElementsManager().getNodeAt(
	// svgHandle.getCanvas().getDocument().getDocumentElement(),
	// currentPoint, "image");
	//
	// Set<SelectionItem> items = null;
	// if (element == null) {
	// return null;
	// }
	//
	// if (element.getNodeName().equals("image")) {
	// Set<Element> elements = new HashSet<Element>();
	// elements.add(element);
	// items = new HashSet<SelectionItem>();
	// Element terminal = (Element) element.getElementsByTagName(
	// "terminal").item(0);
	// if (terminal == null) {
	// return null;
	// }
	//
	// NodeList ncipoints = terminal.getElementsByTagName("nci:POINT");
	// if (ncipoints == null) {
	// return null;
	// }
	//
	// int nSize = ncipoints.getLength();
	// int x1 = 0, x2 = 0, y1 = 0, y2 = 0;
	// double dx = new Double(element.getAttribute("x")).doubleValue();
	// double dy = new Double(element.getAttribute("y")).doubleValue();
	// double sw = new Double(element.getAttribute("sw")).doubleValue();
	// double sh = new Double(element.getAttribute("sh")).doubleValue();
	// double nw = new Double(element.getAttribute("width")).doubleValue();
	// double nh = new Double(element.getAttribute("height"))
	// .doubleValue();
	// if (sw == 0)
	// sw = nw;
	//
	// if (sh == 0)
	// sh = nh;
	// final AffineTransform initialTransform = svgHandle
	// .getSvgElementsManager().getTransform(element);
	// if (nSize > 0) {
	// for (int i = 0; i < nSize; i++) {
	// Element mdelement = (Element) ncipoints.item(i);
	// x1 = new Integer(mdelement.getAttribute("x")).intValue();
	// y1 = new Integer(mdelement.getAttribute("y")).intValue();
	// double[] pointo = { dx + x1 * nw / sw, dy + y1 * nh / sh };
	//
	// // 通过坐标变换，获取实际的坐标地址
	//
	// initialTransform.transform(pointo, 0, pointo, 0, 1);
	// double dx2 = pointo[0];
	// double dy2 = pointo[1];
	// Point pp = new Point((int) dx2, (int) dy2);
	//
	// // 判断输入的坐标点与查询得到的连接点距离，如果小于3则认为两点耦合，设备与线相连
	// if (currentPoint.distance(dx2, dy2) > 3) {
	// items.add(new SelectionItem(svgHandle, elements,
	// svgHandle.getTransformsManager()
	// .getScaledPoint(pp, false),
	// SelectionItem.POINT, SelectionItem.POINT_STYLE,
	// 0, false, null));
	// } else {
	// // 坐标点与连接点距离小于3，则两点耦合，清空连接点信息，返回耦合点
	// items.clear();
	// items.add(new SelectionItem(svgHandle, elements,
	// svgHandle.getTransformsManager()
	// .getScaledPoint(pp, false),
	// SelectionItem.POINT,
	// SelectionItem.CENTER_POINT_STYLE, 0, false,
	// null));
	// break;
	// }
	//
	// }
	// }
	// }
	// return items;
	// }
	/**
	 * modi by yux,2008.12.11 双击热点操作
	 * 
	 * @param currentPoint
	 */
	public void hyperLink(Point2D currentPoint) {
		Point2D scaledPoint = svgHandle.getTransformsManager().getScaledPoint(
				currentPoint, true);
		Element element = svgHandle.getSvgElementsManager().getNodeAt(
				svgHandle.getCanvas().getDocument().getDocumentElement(),
				scaledPoint, "");
		if (element == null)
			return;

		return;
	}

	// public Set<SelectionItem> getLinkPointItems(Point2D currentPoint) {
	// Set<Element> elements = svgHandle.getSvgElementsManager().getNodeAt(
	// svgHandle.getCanvas().getDocument().getDocumentElement(),
	// currentPoint, null, 0);
	//
	// Set<SelectionItem> items = null;
	// if (elements == null || elements.size() == 0) {
	// return null;
	// }
	// if (selectedElements.iterator().next().getNodeName().equals("path")) {
	// String strPathID = ((Element) selectedElements.iterator().next())
	// .getAttribute("id");
	//
	// for (Element element : elements) {
	//
	// if (isLineShape(element)
	// || (element.getNodeName().equals("use") || element
	// .getElementsByTagName("use").getLength() > 0)
	// || (element.getNodeName().equals("image"))) {
	// String strSelectID = element.getAttribute("id");
	// if (strSelectID == null || !strSelectID.equals(strPathID)) {
	// Set<Element> selectedElements = new HashSet<Element>();
	// selectedElements.add(element);
	// items = new HashSet<SelectionItem>();
	// items.add(new SelectionItem(svgHandle,
	// selectedElements, svgHandle
	// .getTransformsManager().getScaledPoint(
	// currentPoint, false),
	// SelectionItem.POINT,
	// SelectionItem.CENTER_POINT_STYLE, 0, false,
	// null));
	// break;
	// }
	// }
	// }
	// }
	// return items;
	// }

	public boolean isLineShape(Element element) {
		if (element.getNodeName().equals("path")
				|| element.getElementsByTagName("path").getLength() > 0)
			return true;

		if (element.getNodeName().equals("line")
				|| element.getElementsByTagName("line").getLength() > 0)
			return true;

		if (element.getNodeName().equals("polyline")
				|| element.getElementsByTagName("polyline").getLength() > 0)
			return true;
		return false;
	}

	/**
	 * add by yux,2008-12-03 针对平移操作，设定手型鼠标及取消
	 * 
	 * @param showFlag
	 *            ：设定标志，true标识设置手型鼠标，false标识还原成普通鼠标
	 */
	public void handleHandCursor(boolean showFlag) {
		Cursor newCursor = null;
		if (showFlag)
			newCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
		else
			newCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

		svgHandle.getScrollPane().getSVGCanvas().setSVGCursor(newCursor);
		svgHandle.getScrollPane().getViewport().setCursor(newCursor);
	}

	/**
	 * handles the cursor for the given point. 处理鼠标的形状。
	 * 
	 * @param currentMousePoint
	 */
	public void handleCursor(Point currentMousePoint) {

		// converting the given point into the user space units
		Point2D scaledPoint = svgHandle.getTransformsManager().getScaledPoint(
				currentMousePoint, true);

		// getting the selection item corresponding to this point, if one exists
		SelectionItem currentItem = null;

		for (SelectionItem item : new HashSet<SelectionItem>(selectionItems)) {

			if (item.intersectsItemForMouseMove(currentMousePoint)) {

				currentItem = item;
				break;
			}
		}

		// computing the point alignedwith rulers point
		Point2D point = svgHandle.getTransformsManager()
				.getAlignedWithRulersPoint(currentMousePoint, true);

		// refreshing the labels displaying the current position of the mouse on
		// the canvas
		svgHandle.getSVGFrame().getStateBar().setMousePosition(point);
		// //added by wangql
		// if (NCIBoxPane.mouseStatus != Constants.MOUSE_PRESSED) {
		if (currentItem != null) {
			// setting the cursor associated with the item for the canvas
			svgHandle.getScrollPane().getSVGCanvas().setSVGCursor(
					currentItem.getCursor());
			svgHandle.getScrollPane().getViewport().setCursor(
					currentItem.getCursor());

		} else {
			Cursor screenCastCursor = null;
			int cursorType = Cursor.DEFAULT_CURSOR;
			int selectionMode = svgHandle.getEditor().getSelectionManager()
					.getSelectionMode();

			if (selectionMode == SelectionInfoManager.DRAWING_MODE
					|| selectionMode == SelectionInfoManager.ZONE_MODE
					|| selectionMode == SelectionInfoManager.ZOOM_MODE) {

				cursorType = Cursor.CROSSHAIR_CURSOR;

			} else if (selectionMode == SelectionInfoManager.SCREEN_CAST_MODE) {
				try {
					screenCastCursor = ScreenCastCursor.getScreenCastCursor();
				} catch (HeadlessException e) {

					e.printStackTrace();
				} catch (AWTException e) {

					e.printStackTrace();
				}
			} else if (!isSelectionLocked()) {

				// getting the svg element corresponding to this point
				Element element = svgHandle.getSvgElementsManager().getNodeAt(
						parentElement, scaledPoint);

				if (element != null && terminalElement != element
						&& element.getNodeName().equals("use")) {
					showTerminal(element);
					terminalElement = element;
					terminalLevel = SELECTION_TERMINAL;
				}

				if (element != null) {
					if (terminalElement != element
							&& element.getNodeName().equals("g")
							&& element.getAttribute("symbol_status") != null
							&& element.getAttribute("symbol_status").equals(
									NCIEquipSymbolBean.SYMBOL_TYPE_TEMPLATE)) {
						showTerminal(element);
						terminalElement = element;
						terminalLevel = SELECTION_CONNECTNODE;
					}
				}

				if (element == null
						&& (terminalLevel == SELECTION_TERMINAL || terminalLevel == SELECTION_CONNECTNODE)) {
					showTerminal(null);
					terminalElement = null;
					terminalLevel = SELECTION_LEVEL_1;
				}
				// if (element != null) {
				// String str = element.getNodeName();
				// if (str.equals("g")) {
				// String strID = element.getAttribute("id");
				// if (strID != null && strID.equals("nci:terminal")) {
				// return;
				// }
				// }
				// }
				//
				// if (element != null) {
				// String str = element.getNodeName();
				// if (str.equals("g")) {
				// String strID = element.getAttribute("id");
				// if (strID != null && strID.equals("nci:terminal")) {
				// return;
				// }
				// }
				// }

				if (element != null && !isLocked(element)) {
					// System.out.println("element:" + element + "."
					// + this.getClass().getName());
					cursorType = Cursor.MOVE_CURSOR;
					if (element.getNodeName().equals("a")) {
						// 超链接以手形展示
						cursorType = Cursor.HAND_CURSOR;
					}
				}
				// FIXME:显示属性，待完成
				// svgHandle.getEditor().getToolTipManager()
				// .handleToolTip(element);
			}
			Cursor newCursor = null;
			if (selectionMode != SelectionInfoManager.SCREEN_CAST_MODE)
				newCursor = Cursor.getPredefinedCursor(cursorType);
			else
				newCursor = screenCastCursor;

			svgHandle.getScrollPane().getSVGCanvas().setSVGCursor(newCursor);
			svgHandle.getScrollPane().getViewport().setCursor(newCursor);
			// }
		}
	}

	/**
	 * returns the selection item (if it exists) that can be found at the given
	 * point
	 * 
	 * @param point
	 *            a mouse point
	 * @return the selection item (if it exists) that can be found at the given
	 *         point
	 */
	public SelectionItem getSelectionItem(Point2D point) {

		SelectionItem currentItem = null;

		for (SelectionItem item : new HashSet<SelectionItem>(selectionItems)) {

			if (item.intersectsItem(point)) {

				currentItem = item;
				break;
			}
		}

		return currentItem;
	}

	/**
	 * @return the svg handle that is associated to this object
	 */
	public SVGHandle getSVGHandle() {
		return svgHandle;
	}

	/**
	 * @return the set of the selected elements
	 */
	public Set<Element> getSelectedElements() {
		return selectedElements;
	}

	/**
	 * @return the linkedlist of the selected elements, the order is the one
	 *         used by the user to select the elements
	 */
	public LinkedList<Element> getOrderedSelectedElements() {
		return orderedSelectedElements;
	}

	/**
	 * @return selectionLevel.
	 */
	public int getSelectionLevel() {
		return selectionLevel;
	}

	/**
	 * returns whether the provided element can be a parent element or not
	 * 
	 * @param element
	 *            an element
	 * @return whether the provided element can be a parent element or not
	 */
	public static boolean isParentElementEligible(Element element) {

		return element != null && element.getNodeName().equals("g");
	}

	/**
	 * @return whether one or more locked elements can be found in the selected
	 *         elements
	 */
	public boolean isSelectionLocked() {

		return !unLockedSelectedElements.containsAll(selectedElements);
	}

	/**
	 * @return whether all the selected elements are locked
	 */
	public boolean isAllSelectionLocked() {

		return unLockedSelectedElements.isEmpty()
				&& selectedElements.size() > 0;
	}

	/**
	 * returns whether the given element is locked or not
	 * 
	 * @param element
	 *            an element
	 * @return whether the given element is locked or not
	 */
	public boolean isLocked(Element element) {

		return lockedElements.contains(element);
	}

	/**
	 * locks the currently selected elements
	 */
	public void lock() {

		lockedElements.addAll(selectedElements);
		refreshSelection(true);
	}

	/**
	 * 单节点加入锁定
	 * 
	 * @param element
	 */
	public void lock(Element element) {
		lockedElements.add(element);
		refreshSelection(true);
	}

	/**
	 * unlocks the currently selected elements
	 */
	public void unLock() {

		lockedElements.removeAll(selectedElements);
		refreshSelection(true);
	}

	/**
	 * @return the locked elements
	 */
	public Set<Element> getLockedElements() {

		return lockedElements;
	}

	// add by yuxiang
	public void openSVGProperties() {
		SVGProperties module = (SVGProperties) (svgHandle.getEditor()
				.getModule("Properties"));
		module.openProperties();
		return;
	}

	/**
	 * add by yux,2009-1-2 根据传入的坐标，获取选中节点并显示
	 * 
	 * @param point
	 *            ：坐标位置
	 */
	public void showTerminalByPoint(Point point) {
		Element element = svgHandle.getSvgElementsManager().getNodeAt(
				svgHandle.getCanvas().getDocument().getDocumentElement(),
				svgHandle.getTransformsManager().getScaledPoint(point, true),
				"path");
		if (element != null) {

			showTerminal(element);
		}
	}

	/**
	 * add by yux,2009-1-4 根据鼠标点获取拓扑点显示集合
	 * 
	 * @param point
	 *            ：鼠标位置
	 * @return：如存在则返回集合，失败则返回null
	 */
	public Set<SelectionItem> getTerminal(Point2D point) {
		ArrayList<String> list = new ArrayList<String>();
		list.add("path");
		Element element = svgHandle.getSvgElementsManager().getNodeAt(
				svgHandle.getCanvas().getDocument().getDocumentElement(),
				point, false, list, true);// false,null,true);

		if (element == null)
			return null;

		// Iterator<Element> iterator = elements.iterator();
		Set<SelectionItem> items = new HashSet<SelectionItem>();
		// while (iterator.hasNext()) {
		// Element element = iterator.next();

		if (element instanceof SVGOMUseElement
				|| element instanceof SVGOMGElement) {
			Set<SelectionItem> showItems = getTerminal(element);
			if (showItems != null && showItems.size() > 0) {
				items.addAll(showItems);
			}
		}
		// }

		return items;
	}

	/**
	 * add by yux,2009-1-4 根据节点获取拓扑点显示结合
	 * 
	 * @param element
	 *            ：节点
	 * @return：如存在则返回集合，失败返回null
	 */
	public Set<SelectionItem> getTerminal(Element element) {
		if (element == null)
			return null;
		Set<SelectionItem> items = null;
		if (element != null && element.getNodeName().equals("use")) {
			// 图元，则读取图元信息
			SVGOMUseElement useElement = (SVGOMUseElement) element;
			// AffineTransform initialTransform = svgHandle
			// .getSvgElementsManager().getTransform(useElement);
			Rectangle2D bounds = svgHandle.getSvgElementsManager()
					.getSensitiveBounds(element);
			double ex = bounds.getMinX();
			double ey = bounds.getMinY();
			double nw = bounds.getWidth();
			double nh = bounds.getHeight();
			// double ex = Double.valueOf(useElement.getAttribute("x"));
			// double ey = Double.valueOf(useElement.getAttribute("y"));
			// double nw = Double.valueOf(useElement.getAttribute("width"));
			// double nh = Double.valueOf(useElement.getAttribute("height"));
			String href = useElement.getHref().getBaseVal();
			if (href == null || href.length() > 0) {
				href = href.substring(1);

				Element symbol = svgHandle.getCanvas().getDocument()
						.getElementById(href);
				Set<Element> elements = new HashSet<Element>();
				elements.add(element);
				if (symbol != null) {
					String viewBox = symbol.getAttribute("viewBox");
					String[] sep = viewBox.split(" ");
					double sw = Double.valueOf(sep[2]);
					double sh = Double.valueOf(sep[3]);
					NodeList list = symbol.getElementsByTagName("use");
					items = new HashSet<SelectionItem>();
					for (int i = 0; i < list.getLength(); i++) {
						if (((SVGOMUseElement) list.item(i)).getHref()
								.getBaseVal().equals("#terminal")) {
							Element e = (Element) list.item(i);
							double dx = Double.valueOf(e.getAttribute("x"));
							double dy = Double.valueOf(e.getAttribute("y"));
							double[] pointo = { ex + dx * nw / sw,
									ey + dy * nh / sh };

							// 通过坐标变换，获取实际的坐标地址

							// try {
							// initialTransform.inverseTransform(pointo, 0,
							// pointo, 0, 1);
							// } catch (NoninvertibleTransformException e1) {
							// // TODO Auto-generated catch block
							// e1.printStackTrace();
							// }
							Point pp = new Point((int) pointo[0],
									(int) pointo[1]);
							items.add(new SelectionItem(svgHandle, elements,
									svgHandle.getTransformsManager()
											.getScaledPoint(pp, false),
									SelectionItem.TERMINAL_POINT,
									SelectionItem.TERMINAL_POINT_STYLE, 0,
									false, null));

						}
					}
				}
				terminalItems.addAll(items);
			}
		} else if (element != null
				&& element.getNodeName().equals("g")
				&& element.getAttribute("symbol_status") != null
				&& element.getAttribute("symbol_status").equals(
						NCIEquipSymbolBean.SYMBOL_TYPE_TEMPLATE)) {
			NodeList list = element.getElementsByTagName("use");
			Set<Element> elements = new HashSet<Element>();
			elements.add(element);
			items = new HashSet<SelectionItem>();
			for (int i = 0; i < list.getLength(); i++) {
				if (((SVGOMUseElement) list.item(i)).getHref().getBaseVal()
						.equals("#terminal")) {
					Element e = (Element) list.item(i);
					double dx = Double.valueOf(e.getAttribute("x"));
					double dy = Double.valueOf(e.getAttribute("y"));
					Point pp = new Point((int) dx, (int) dy);
					items
							.add(new SelectionItem(svgHandle, elements,
									svgHandle.getTransformsManager()
											.getScaledPoint(pp, false),
									SelectionItem.TERMINAL_POINT,
									SelectionItem.TERMINAL_POINT_STYLE, 0,
									false, null));
				}
			}

		}

		return items;
	}

	/**
	 * 鼠标在连接线移过去的时候断定可以连接的图元
	 */
	protected PseudoConnectedElement pseudoConnectedElement = null;

	/**
	 * add by yux,2009-1-4 根据鼠标坐标获取连接点显示集合
	 * 
	 * @param point
	 *            ：鼠标坐标
	 * @return：连接点显示集合
	 */
	public Set<SelectionItem> getConnectNode(Point2D point) {

		// ArrayList<String> list = new ArrayList<String>();
		// list.add("path");
		// Element element = svgHandle.getSvgElementsManager().getNodeAt(
		// svgHandle.getCanvas().getDocument().getDocumentElement(),
		// point, false, list, true, 5,
		// svgHandle.getSelection().getGroupBreaker());// false,null,true);

		// modified by wangql, 上述方式无法取到需要的element
		pseudoConnectedElement = null;
		SelectionFilterIF selectionFilter = new SelectionFilterIF() {

			@Override
			public boolean filterElement(Element shapeElement) {
				if (shapeElement.getNodeName().equals("use"))
					return true;
				return false;
			}

		};
		Element element = svgHandle.getSvgElementsManager().getNodeAt(
				svgHandle.getCanvas().getDocument().getDocumentElement(),
				point, selectionFilter,
				svgHandle.getSelection().getGroupBreaker());// false,null,true);

		if (element == null)
			return null;

		// Iterator<Element> iterator = elements.iterator();
		Set<SelectionItem> items = new HashSet<SelectionItem>();
		// while (iterator.hasNext()) {
		// Element element = iterator.next();

		if (element instanceof SVGOMUseElement
				|| element instanceof SVGOMGElement) {
			Set<SelectionItem> showItems = getConnectNode(element,
					point.getX(), point.getY());
			if (showItems != null && showItems.size() > 0) {
				items.addAll(showItems);
				// pseudoConnectedElement = element;
				// Utilities.printNode(psudoConnectedElement, true);
			}
		}
		// }

		return items;
	}

	/**
	 * add by yux,2009-1-4 根据节点和显示位置连接点显示集合
	 * 
	 * @param element
	 *            :节点
	 * @param px
	 *            ：x轴坐标
	 * @param py
	 *            ：y轴坐标
	 * @return：连接点集合
	 */
	public Set<SelectionItem> getConnectNode(Element element, double px,
			double py) {
		pseudoConnectedElement = null;
		if (element == null)
			return null;
		Set<SelectionItem> items = null;
		if (element != null && element.getNodeName().equals("use")) {
			// 图元，则读取图元信息
			SVGOMUseElement useElement = (SVGOMUseElement) element;
			// AffineTransform initialTransform = svgHandle
			// .getSvgElementsManager().getTransform(useElement);
			Rectangle2D bounds = svgHandle.getSvgElementsManager()
					.getSensitiveBounds(element);
			double ex = bounds.getMinX();
			double ey = bounds.getMinY();
			double nw = bounds.getWidth();
			double nh = bounds.getHeight();
			// double ex = Double.valueOf(useElement.getAttribute("x"));
			// double ey = Double.valueOf(useElement.getAttribute("y"));
			// double nw = Double.valueOf(useElement.getAttribute("width"));
			// double nh = Double.valueOf(useElement.getAttribute("height"));
			String href = useElement.getHref().getBaseVal();
			if (href == null || href.length() > 0) {
				href = href.substring(1);

				Element symbol = svgHandle.getCanvas().getDocument()
						.getElementById(href);
				Set<Element> elements = new HashSet<Element>();
				elements.add(element);
				items = new HashSet<SelectionItem>();
				if (symbol != null) {
					String viewBox = symbol.getAttribute("viewBox");
					String[] sep = viewBox.split(" ");
					double sw = Double.valueOf(sep[2]);
					double sh = Double.valueOf(sep[3]);
					NodeList list = symbol.getElementsByTagName("use");
					for (int i = 0; i < list.getLength(); i++) {
						if (((SVGOMUseElement) list.item(i)).getHref()
								.getBaseVal().equals("#terminal")) {
							Element e = (Element) list.item(i);
							double dx = Double.valueOf(e.getAttribute("x"));
							double dy = Double.valueOf(e.getAttribute("y"));
							double[] pointo = { ex + dx * nw / sw,
									ey + dy * nh / sh };

							// 通过坐标变换，获取实际的坐标地址

							// try {
							// initialTransform.inverseTransform(pointo, 0,
							// pointo, 0, 1);
							// } catch (NoninvertibleTransformException e1) {
							// // TODO Auto-generated catch block
							// e1.printStackTrace();
							// }
							Point pp = new Point((int) pointo[0],
									(int) pointo[1]);
							if (pp.distance(px, py) <= 5) {
								items.add(new SelectionItem(svgHandle,
										elements, svgHandle
												.getTransformsManager()
												.getScaledPoint(pp, false),
										SelectionItem.CONNECTIVE_NODE_POINT,
										SelectionItem.CONNECTIVE_NODE_STYLE, 0,
										false, null));
								pseudoConnectedElement = new PseudoConnectedElement(
										element, e.getAttribute("name"));
								break;
							}

						}
					}
				}

			}
		} else if (element != null
				&& element.getNodeName().equals("g")
				&& element.getAttribute("symbol_status") != null
				&& element.getAttribute("symbol_status").equals(
						NCIEquipSymbolBean.SYMBOL_TYPE_TEMPLATE)) {
			NodeList list = element.getElementsByTagName("use");
			Set<Element> elements = new HashSet<Element>();
			elements.add(element);
			items = new HashSet<SelectionItem>();
			for (int i = 0; i < list.getLength(); i++) {
				if (((SVGOMUseElement) list.item(i)).getHref().getBaseVal()
						.equals("#terminal")) {
					Element e = (Element) list.item(i);
					double dx = Double.valueOf(e.getAttribute("x"));
					double dy = Double.valueOf(e.getAttribute("y"));
					Point pp = new Point((int) dx, (int) dy);
					if (pp.distance(px, py) <= 5) {
						items.add(new SelectionItem(svgHandle, elements,
								svgHandle.getTransformsManager()
										.getScaledPoint(pp, false),
								SelectionItem.CONNECTIVE_NODE_POINT,
								SelectionItem.CONNECTIVE_NODE_STYLE, 0, false,
								null));
						break;
					}
				}
			}

		}

		return items;
	}

	public void showTerminal(Element element, Rectangle2D showBounds) {
		terminalItemsPainter.clear();
		terminalItems.clear();

		if (element != null && element.getNodeName().equals("use")) {
			// 图元，则读取图元信息
			SVGOMUseElement useElement = (SVGOMUseElement) element;
			double ex = showBounds.getMinX();
			double ey = showBounds.getMinY();
			double nw = showBounds.getWidth();
			double nh = showBounds.getHeight();
			String href = useElement.getHref().getBaseVal();
			if (href == null || href.length() > 0) {
				href = href.substring(1);

				Element symbol = svgHandle.getCanvas().getDocument()
						.getElementById(href);
				Set<Element> elements = new HashSet<Element>();
				elements.add(element);
				Set<SelectionItem> items = new HashSet<SelectionItem>();
				if (symbol != null) {
					String viewBox = symbol.getAttribute("viewBox");
					if (viewBox == null && viewBox.length() == 0)
						return;
					String[] sep = viewBox.split(" ");
					if (sep.length < 4 || sep[2] == null || sep[3] == null
							|| sep[2].length() == 0 || sep[3].length() == 0)
						return;
					double sw = Double.valueOf(sep[2]);
					double sh = Double.valueOf(sep[3]);
					NodeList list = symbol.getElementsByTagName("use");
					for (int i = 0; i < list.getLength(); i++) {
						if (((SVGOMUseElement) list.item(i)).getHref()
								.getBaseVal().equals("#terminal")) {
							Element e = (Element) list.item(i);
							double dx = Double.valueOf(e.getAttribute("x"));
							double dy = Double.valueOf(e.getAttribute("y"));
							double[] pointo = { ex + dx * nw / sw,
									ey + dy * nh / sh };

							// 通过坐标变换，获取实际的坐标地址

							// try {
							// initialTransform.transform(pointo, 0,
							// pointo, 0, 1);
							// } catch (Exception e1) {
							//
							// }
							Point pp = new Point((int) pointo[0],
									(int) pointo[1]);
							items.add(new SelectionItem(svgHandle, elements,
									svgHandle.getTransformsManager()
											.getScaledPoint(pp, false),
									SelectionItem.TERMINAL_POINT,
									SelectionItem.TERMINAL_POINT_STYLE, 0,
									false, null));

						}
					}
				}
				terminalItems.addAll(items);
			}
		} else if (element != null
				&& element.getNodeName().equals("g")
				&& element.getAttribute("symbol_status") != null
				&& element.getAttribute("symbol_status").equals(
						NCIEquipSymbolBean.SYMBOL_TYPE_TEMPLATE)) {
			NodeList list = element.getElementsByTagName("use");
			Set<Element> elements = new HashSet<Element>();
			elements.add(element);
			Set<SelectionItem> items = new HashSet<SelectionItem>();
			for (int i = 0; i < list.getLength(); i++) {
				if (((SVGOMUseElement) list.item(i)).getHref().getBaseVal()
						.equals("#terminal")) {
					Element e = (Element) list.item(i);
					double dx = Double.valueOf(e.getAttribute("x"));
					double dy = Double.valueOf(e.getAttribute("y"));
					Point pp = new Point((int) dx, (int) dy);
					items
							.add(new SelectionItem(svgHandle, elements,
									svgHandle.getTransformsManager()
											.getScaledPoint(pp, false),
									SelectionItem.TERMINAL_POINT,
									SelectionItem.TERMINAL_POINT_STYLE, 0,
									false, null));
				}
			}
			terminalItems.addAll(items);
		}
		terminalItemsPainter.reinitialize();
	}

	/**
	 * add by yux,2008.12.29 显示当前选中图元或模板的拓扑点，如传入为null则删除当前显示所有拓扑点
	 * 
	 * @param element
	 *            :待显示的图元或模板节点
	 */
	public void showTerminal(Element element) {

		Rectangle2D bounds = svgHandle.getSvgElementsManager()
				.getSensitiveBounds(element);
		showTerminal(element, bounds);

	}

	public boolean showConnectNodeByPoint(Point point) {
		Element element = svgHandle.getSvgElementsManager().getNodeAt(
				svgHandle.getCanvas().getDocument().getDocumentElement(),
				svgHandle.getTransformsManager().getScaledPoint(point, true),
				"path");
		if (element != null) {

			return showConnectNode(element, point.getX(), point.getY());
		}
		return false;
	}

	/**
	 * add by yux,2009-1-4 根据输入的节点和拓扑点名称，获取该拓扑点坐标
	 * 
	 * @param element
	 *            ：节点
	 * @param name
	 *            ：拓扑点名称
	 * @return：如存在则返回，不存在则返回null
	 */
	public Point getPointByTerminalName(Element element, String name) {
		Point point = null;
		if (element != null && element.getNodeName().equals("use")) {
			// 图元，则读取图元信息
			SVGOMUseElement useElement = (SVGOMUseElement) element;
			AffineTransform initialTransform = svgHandle
					.getSvgElementsManager().getTransform(useElement);
			Rectangle2D bounds = svgHandle.getSvgElementsManager()
					.getSensitiveBounds(element);
			double ex = bounds.getMinX();
			double ey = bounds.getMinY();
			double nw = bounds.getWidth();
			double nh = bounds.getHeight();
			// double ex = Double.valueOf(useElement.getAttribute("x"));
			// double ey = Double.valueOf(useElement.getAttribute("y"));
			// double nw = Double.valueOf(useElement.getAttribute("width"));
			// double nh = Double.valueOf(useElement.getAttribute("height"));
			String href = useElement.getHref().getBaseVal();
			if (href == null || href.length() > 0) {
				href = href.substring(1);

				Element symbol = svgHandle.getCanvas().getDocument()
						.getElementById(href);
				Set<Element> elements = new HashSet<Element>();
				elements.add(element);
				Set<SelectionItem> items = new HashSet<SelectionItem>();
				if (symbol != null) {
					String viewBox = symbol.getAttribute("viewBox");
					String[] sep = viewBox.split(" ");
					double sw = Double.valueOf(sep[2]);
					double sh = Double.valueOf(sep[3]);
					NodeList list = symbol.getElementsByTagName("use");
					for (int i = 0; i < list.getLength(); i++) {
						if (((SVGOMUseElement) list.item(i)).getHref()
								.getBaseVal().equals("#terminal")) {
							Element e = (Element) list.item(i);
							if (name.equals(e.getAttribute("name"))) {
								double dx = Double.valueOf(e.getAttribute("x"));
								double dy = Double.valueOf(e.getAttribute("y"));
								double[] pointo = { ex + dx * nw / sw,
										ey + dy * nh / sh };
								// try {
								// initialTransform.inverseTransform(pointo, 0,
								// pointo, 0, 1);
								// } catch (Exception e1) {
								//
								// }
								point = new Point((int) pointo[0],
										(int) pointo[1]);
								break;
							}
						}
					}
				}

			}
		} else if (element != null
				&& element.getNodeName().equals("g")
				&& element.getAttribute("symbol_status") != null
				&& element.getAttribute("symbol_status").equals(
						NCIEquipSymbolBean.SYMBOL_TYPE_TEMPLATE)) {
			NodeList list = element.getElementsByTagName("use");
			Set<Element> elements = new HashSet<Element>();
			elements.add(element);
			Set<SelectionItem> items = new HashSet<SelectionItem>();
			for (int i = 0; i < list.getLength(); i++) {
				if (((SVGOMUseElement) list.item(i)).getHref().getBaseVal()
						.equals("#terminal")) {
					Element e = (Element) list.item(i);
					if (name.equals(e.getAttribute("name"))) {
						double dx = Double.valueOf(e.getAttribute("x"));
						double dy = Double.valueOf(e.getAttribute("y"));
						point = new Point((int) dx, (int) dy);
						break;

					}
				}
			}

		}

		return point;
	}

	/**
	 * add by yux,2008.12.29 显示连接点标记，如传入为null则删除当前显示的连接点
	 * 
	 * @param element
	 *            :待显示的连接点
	 * @param px
	 *            :当前鼠标x轴位置
	 * @param py
	 *            :当前鼠标y轴位置
	 * @return 存在连接点则返回true，不存在则返回false
	 */
	public boolean showConnectNode(Element element, double px, double py) {
		boolean connectFlag = false;

		if (element != null && element.getNodeName().equals("use")) {
			// 图元，则读取图元信息
			SVGOMUseElement useElement = (SVGOMUseElement) element;
			Rectangle2D bounds = svgHandle.getSvgElementsManager()
					.getSensitiveBounds(element);
			double ex = bounds.getMinX();
			double ey = bounds.getMinY();
			double nw = bounds.getWidth();
			double nh = bounds.getHeight();
			// double ex = Double.valueOf(useElement.getAttribute("x"));
			// double ey = Double.valueOf(useElement.getAttribute("y"));
			// double nw = Double.valueOf(useElement.getAttribute("width"));
			// double nh = Double.valueOf(useElement.getAttribute("height"));
			String href = useElement.getHref().getBaseVal();
			if (href == null || href.length() > 0) {
				href = href.substring(1);

				Element symbol = svgHandle.getCanvas().getDocument()
						.getElementById(href);
				Set<Element> elements = new HashSet<Element>();
				elements.add(element);
				Set<SelectionItem> items = new HashSet<SelectionItem>();
				if (symbol != null) {
					String viewBox = symbol.getAttribute("viewBox");
					if (viewBox == null && viewBox.length() == 0)
						return false;
					String[] sep = viewBox.split(" ");
					if (sep.length < 4 || sep[2] == null || sep[3] == null
							|| sep[2].length() == 0 || sep[3].length() == 0)
						return false;
					double sw = Double.valueOf(sep[2]);
					double sh = Double.valueOf(sep[3]);
					NodeList list = symbol.getElementsByTagName("use");
					for (int i = 0; i < list.getLength(); i++) {
						if (((SVGOMUseElement) list.item(i)).getHref()
								.getBaseVal().equals("#terminal")) {
							Element e = (Element) list.item(i);
							double dx = Double.valueOf(e.getAttribute("x"));
							double dy = Double.valueOf(e.getAttribute("y"));
							double[] pointo = { ex + dx * nw / sw,
									ey + dy * nh / sh };

							// 通过坐标变换，获取实际的坐标地址

							// try {
							// initialTransform.inverseTransform(pointo, 0,
							// pointo, 0, 1);
							// } catch (NoninvertibleTransformException e1) {
							// // TODO Auto-generated catch block
							// e1.printStackTrace();
							// }
							Point pp = new Point((int) pointo[0],
									(int) pointo[1]);
							if (pp.distance(px, py) <= 5) {
								terminalItemsPainter.clear();
								terminalItems.clear();
								items.add(new SelectionItem(svgHandle,
										elements, svgHandle
												.getTransformsManager()
												.getScaledPoint(pp, false),
										SelectionItem.CONNECTIVE_NODE_POINT,
										SelectionItem.CONNECTIVE_NODE_STYLE, 0,
										false, null));
								terminalItems.addAll(items);
								terminalItemsPainter.reinitialize();
								connectFlag = true;
								break;
							}

						}
					}
				}

			}
		} else if (element != null
				&& element.getNodeName().equals("g")
				&& element.getAttribute("symbol_status") != null
				&& element.getAttribute("symbol_status").equals(
						NCIEquipSymbolBean.SYMBOL_TYPE_TEMPLATE)) {
			NodeList list = element.getElementsByTagName("use");
			Set<Element> elements = new HashSet<Element>();
			elements.add(element);
			Set<SelectionItem> items = new HashSet<SelectionItem>();
			for (int i = 0; i < list.getLength(); i++) {
				if (((SVGOMUseElement) list.item(i)).getHref().getBaseVal()
						.equals("#terminal")) {
					Element e = (Element) list.item(i);
					double dx = Double.valueOf(e.getAttribute("x"));
					double dy = Double.valueOf(e.getAttribute("y"));
					Point pp = new Point((int) dx, (int) dy);
					if (pp.distance(px, py) <= 5) {
						terminalItemsPainter.clear();
						terminalItems.clear();
						items.add(new SelectionItem(svgHandle, elements,
								svgHandle.getTransformsManager()
										.getScaledPoint(pp, false),
								SelectionItem.CONNECTIVE_NODE_POINT,
								SelectionItem.CONNECTIVE_NODE_STYLE, 0, false,
								null));
						terminalItems.addAll(items);
						terminalItemsPainter.reinitialize();
						connectFlag = true;
						break;
					}
				}
			}

		}

		return connectFlag;
	}

	public TerminalInfo getConnectName(Point2D point) {
		TerminalInfo info = null;
		Set<Element> elements = svgHandle.getSvgElementsManager().getNodeAt(
				svgHandle.getCanvas().getDocument().getDocumentElement(),
				svgHandle.getTransformsManager().getScaledPoint(point, true),
				null, 5);
		if (elements == null || elements.size() == 0)
			return info;
		String name = null;

		Iterator<Element> iterator = elements.iterator();
		Set<SelectionItem> items = new HashSet<SelectionItem>();
		while (iterator.hasNext()) {
			Element element = iterator.next();

			if (element instanceof SVGOMUseElement
					|| element instanceof SVGOMGElement) {
				name = getConnectName(element, point.getX(), point.getY());
				if (name != null && name.length() > 0) {
					info = new TerminalInfo(element, name);

					break;
				}

			}
		}

		return info;
	}

	/**
	 * add by yux,2009-1-21 在指定点显示模型动作菜单
	 * 
	 * @param point
	 */
	public void showActionsPopupMenu(Point point) {
		Point2D scaledPoint = svgHandle.getTransformsManager().getScaledPoint(
				point, true);
		final Element element = svgHandle.getSvgElementsManager().getNodeAt(
				svgHandle.getCanvas().getDocument().getDocumentElement(),
				scaledPoint, "");
		if (element == null)
			return;

		String symbolName = svgHandle.getEditor().getSvgSession()
				.getSymbolName(element);
		if (symbolName == null)
			return;
		ModelBean bean = svgHandle.getEditor().getSvgSession()
				.getModelBeanBySymbolName(symbolName);
		if (bean == null)
			return;
		ModelRelaIndunormBean relaBean = svgHandle.getEditor().getSvgSession()
				.getModelIDPropertyBySymbolID(symbolName);
		String bussID = svgHandle.getEditor().getSvgSession().getElementBussID(
				element, relaBean);

		boolean show = false;

		JPopupMenu popupMenu = new JPopupMenu();
		if (bussID != null && bussID.length() > 0) {
			if (appendBussSubMenu(bean, popupMenu,
					ModelActionBean.TYPE_RELAINSTANCE, element))
				show = true;
			if (appendBussSubMenu(bean, popupMenu,
					ModelActionBean.TYPE_MOUSE_RDOWM, element))
				show = true;
		} else {
			if (appendBussSubMenu(bean, popupMenu,
					ModelActionBean.TYPE_NEWINSTANCE, element))
				show = true;
			if (appendBussSubMenu(bean, popupMenu,
					ModelActionBean.TYPE_RELAINSTANCE, element))
				show = true;
		}

		if (show)
			popupMenu.show(svgHandle.getScrollPane().getSVGCanvas(),
					(int) point.getX(), (int) point.getY());
	}

	private boolean appendBussSubMenu(ModelBean bean, JPopupMenu popupMenu,
			String actionType, final Element element) {
		HashMap<String, ModelActionBean> map = (HashMap<String, ModelActionBean>) bean
				.getActions().get(actionType);
		if (map == null)
			return false;
		Iterator<ModelActionBean> iterator = map.values().iterator();
		while (iterator.hasNext()) {
			final ModelActionBean actionBean = iterator.next();
			JMenuItem menuItem = new JMenuItem();
			menuItem.setName(actionBean.getId());
			menuItem.setText(actionBean.getName());
			menuItem.setBackground(Constants.getColor(actionType));
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ActionParserModule parser = new ActionParserModule(
							svgHandle.getEditor(), svgHandle, element,
							actionBean.getContent());

				}

			});
			popupMenu.add(menuItem);
		}
		return true;
	}

	/**
	 * add by yux,2008.12.29 显示连接点标记，如传入为null则删除当前显示的连接点
	 * 
	 * @param element
	 *            :待显示的连接点
	 * @param px
	 *            :当前鼠标x轴位置
	 * @param py
	 *            :当前鼠标y轴位置
	 * @return：当前连接上的图元拓扑点名称
	 */
	public String getConnectName(Element element, double px, double py) {
		String name = null;

		if (element != null && element.getNodeName().equals("use")) {
			// 图元，则读取图元信息
			SVGOMUseElement useElement = (SVGOMUseElement) element;
			AffineTransform initialTransform = svgHandle
					.getSvgElementsManager().getTransform(useElement);
			Rectangle2D bounds = svgHandle.getSvgElementsManager()
					.getSensitiveBounds(element);
			double ex = bounds.getMinX();
			double ey = bounds.getMinY();
			double nw = bounds.getWidth();
			double nh = bounds.getHeight();
			// double ex = Double.valueOf(useElement.getAttribute("x"));
			// double ey = Double.valueOf(useElement.getAttribute("y"));
			// double nw = Double.valueOf(useElement.getAttribute("width"));
			// double nh = Double.valueOf(useElement.getAttribute("height"));
			String href = useElement.getHref().getBaseVal();
			if (href == null || href.length() > 0) {
				href = href.substring(1);

				Element symbol = svgHandle.getCanvas().getDocument()
						.getElementById(href);
				Set<Element> elements = new HashSet<Element>();
				elements.add(element);
				Set<SelectionItem> items = new HashSet<SelectionItem>();
				if (symbol != null) {
					String viewBox = symbol.getAttribute("viewBox");
					String[] sep = viewBox.split(" ");
					double sw = Double.valueOf(sep[2]);
					double sh = Double.valueOf(sep[3]);
					NodeList list = symbol.getElementsByTagName("use");
					for (int i = 0; i < list.getLength(); i++) {
						if (((SVGOMUseElement) list.item(i)).getHref()
								.getBaseVal().equals("#terminal")) {
							Element e = (Element) list.item(i);
							double dx = Double.valueOf(e.getAttribute("x"));
							double dy = Double.valueOf(e.getAttribute("y"));
							double[] pointo = { ex + dx * nw / sw,
									ey + dy * nh / sh };

							// 通过坐标变换，获取实际的坐标地址

							// try {
							// initialTransform.inverseTransform(pointo, 0,
							// pointo, 0, 1);
							// } catch (NoninvertibleTransformException e1) {
							// // TODO Auto-generated catch block
							// e1.printStackTrace();
							// }
							Point pp = new Point((int) pointo[0],
									(int) pointo[1]);
							if (pp.distance(px, py) <= 5) {
								name = e.getAttribute("name");
								break;
							}

						}
					}
				}

			}
		} else if (element != null
				&& element.getNodeName().equals("g")
				&& element.getAttribute("symbol_status") != null
				&& element.getAttribute("symbol_status").equals(
						NCIEquipSymbolBean.SYMBOL_TYPE_TEMPLATE)) {
			NodeList list = element.getElementsByTagName("use");
			Set<Element> elements = new HashSet<Element>();
			elements.add(element);
			Set<SelectionItem> items = new HashSet<SelectionItem>();
			for (int i = 0; i < list.getLength(); i++) {
				if (((SVGOMUseElement) list.item(i)).getHref().getBaseVal()
						.equals("#terminal")) {
					Element e = (Element) list.item(i);
					double dx = Double.valueOf(e.getAttribute("x"));
					double dy = Double.valueOf(e.getAttribute("y"));
					Point pp = new Point((int) dx, (int) dy);
					if (pp.distance(px, py) <= 5) {
						name = e.getAttribute("name");
						break;
					}
				}
			}

		}

		return name;
	}

	/**
	 * the class used to paint the selection items on the canvas
	 * 
	 * @author ITRIS, Jordi SUC
	 */
	protected class TerminalItemsCanvasPainter extends CanvasPainter {

		/**
		 * the current clip of this canvas painter
		 */
		private Set<Rectangle2D> clips = new HashSet<Rectangle2D>();

		@Override
		public void paintToBeDone(Graphics2D g) {

			// painting the selection items
			Set<SelectionItem> items = new HashSet<SelectionItem>(terminalItems);

			for (SelectionItem item : items) {

				item.paint(g);
			}
		}

		/**
		 * clears this painter
		 */
		protected void clear() {

			// recomputing the clip
			clips.clear();

			if (terminalItems.size() > 0) {

				// adding all the new clips
				for (SelectionItem item : terminalItems) {

					clips.add(new Rectangle(item.getShapeBounds()));
				}
			}
		}

		/**
		 * stops displaying the selection items and cleans the canvas
		 */
		protected void clean() {

			SVGCanvas canvas = svgHandle.getScrollPane().getSVGCanvas();

			if (clips.size() > 0) {

				canvas.removePaintListener(this, true);
			}
		}

		/**
		 * reinitializes all the selection items
		 */
		protected void reinitialize() {

			final SVGCanvas canvas = svgHandle.getScrollPane().getSVGCanvas();

			if (clips.size() > 0) {

				canvas.removePaintListener(this, true);
				clips.clear();
			}

			if (terminalItems.size() > 0) {

				// adding all the new clips
				for (SelectionItem item : terminalItems) {
					// 增加边角箭头对应的矩形
					clips.add(new Rectangle(item.getShapeBounds()));
				}

				final TerminalItemsCanvasPainter s = this;
				canvas.addLayerPaintListener(SVGCanvas.TERMINAL_LAYER, s, true);
				// SwingWorker worker = new SwingWorker() {
				//
				// @Override
				// protected Object doInBackground() throws Exception {
				// canvas.addLayerPaintListener(SVGCanvas.DRAW_LAYER, s,
				// true);
				// return null;
				// }
				// };
				// worker.execute();
			}
		}

		@Override
		public Set<Rectangle2D> getClip() {

			return clips;
		}
	}

	public class TerminalInfo {
		private String terminalName;
		private Element element;

		public TerminalInfo(Element element, String terminalName) {
			this.element = element;
			this.terminalName = terminalName;
		}

		public TerminalInfo() {

		}

		/**
		 * 返回
		 * 
		 * @return the terminalName
		 */
		public String getTerminalName() {
			return terminalName;
		}

		/**
		 * 设置
		 * 
		 * @param terminalName
		 *            the terminalName to set
		 */
		public void setTerminalName(String terminalName) {
			this.terminalName = terminalName;
		}

		/**
		 * 返回
		 * 
		 * @return the element
		 */
		public Element getElement() {
			return element;
		}

		/**
		 * 设置
		 * 
		 * @param element
		 *            the element to set
		 */
		public void setElement(Element element) {
			this.element = element;
		}

	}

	/**
	 * the class used to paint the selection items on the canvas
	 * 
	 * @author ITRIS, Jordi SUC
	 */
	protected class SelectionItemsCanvasPainter extends CanvasPainter {

		/**
		 * the current clip of this canvas painter
		 */
		private Set<Rectangle2D> clips = new HashSet<Rectangle2D>();

		@Override
		public void paintToBeDone(Graphics2D g) {

			// painting the selection items
			Set<SelectionItem> items = new HashSet<SelectionItem>(
					selectionItems);

			for (SelectionItem item : items) {

				item.paint(g);
			}
		}

		/**
		 * clears this painter
		 */
		protected void clear() {

			// recomputing the clip
			clips.clear();

			if (selectionItems.size() > 0) {

				// adding all the new clips
				for (SelectionItem item : selectionItems) {

					clips.add(new Rectangle(item.getShapeBounds()));
				}
			}
		}

		/**
		 * stops displaying the selection items and cleans the canvas
		 */
		protected void clean() {

			SVGCanvas canvas = svgHandle.getScrollPane().getSVGCanvas();

			if (clips.size() > 0) {

				canvas.removePaintListener(this, true);
			}
		}

		/**
		 * reinitializes all the selection items
		 */
		protected void reinitialize() {

			final SVGCanvas canvas = svgHandle.getScrollPane().getSVGCanvas();

			if (clips.size() > 0) {

				canvas.removePaintListener(this, true);
				clips.clear();
			}

			if (selectionItems.size() > 0) {

				// adding all the new clips
				for (SelectionItem item : selectionItems) {
					// 增加边角箭头对应的矩形
					clips.add(new Rectangle(item.getShapeBounds()));
				}

				final SelectionItemsCanvasPainter s = this;
				canvas.addLayerPaintListener(SVGCanvas.DRAW_LAYER, s, true);
				// SwingWorker worker = new SwingWorker() {
				//
				// @Override
				// protected Object doInBackground() throws Exception {
				// canvas.addLayerPaintListener(SVGCanvas.DRAW_LAYER, s,
				// true);
				// return null;
				// }
				// };
				// worker.execute();
			}
		}

		@Override
		public Set<Rectangle2D> getClip() {

			return clips;
		}
	}

	/**
	 * the painter used to draw ghost selection zones on a canvas
	 * 
	 * @author ITRIS, Jordi SUC
	 */
	protected static class GhostSelectionZoneCanvasPainter extends
			CanvasPainter {

		/**
		 * the stroke for the ghost
		 */
		private static Stroke stroke = new BasicStroke(1,
				BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1, new float[] {
						5, 5 }, 0);
		/**
		 * the points of the line to draw
		 */
		private Point2D point1, point2;
		/**
		 * the set of the clip rectangles
		 */
		private Set<Rectangle2D> clips = new HashSet<Rectangle2D>();

		@Override
		public void paintToBeDone(Graphics2D g) {

			if (point1 != null && point2 != null) {

				g = (Graphics2D) g.create();
				g.setColor(Color.black);
				g.setStroke(stroke);
				g.setXORMode(Color.white);
				g.drawLine((int) point1.getX(), (int) point1.getY(),
						(int) point2.getX(), (int) point2.getY());
				g.dispose();
			}
		}

		@Override
		public Set<Rectangle2D> getClip() {

			return clips;
		}

		/**
		 * sets the points for the line
		 * 
		 * @param point1
		 *            the first point of the line
		 * @param point2
		 *            the second point of the line
		 */
		public void setPoints(Point2D point1, Point2D point2) {

			this.point1 = point1;
			this.point2 = point2;
			clips.clear();

			if (point1 != null && point2 != null) {

				clips.add(new Rectangle2D.Double(point1.getX(), point1.getY(),
						point2.getX() - point1.getX() + 1, point2.getY()
								- point1.getY() + 1));
			}
		}

		/**
		 * reinitializing the painter
		 */
		public void reinitialize() {

			point1 = null;
			point2 = null;
		}
	}

	/**
	 * 绘制连接点或耦合点的画布刷子
	 * 
	 * @author yx
	 * 
	 */
	protected class PointItemsCanvasPainter extends CanvasPainter {

		/**
		 * the current clip of this canvas painter
		 */
		private Set<Rectangle2D> clips = new HashSet<Rectangle2D>();
		private Set<SelectionItem> pointitems = null;

		@Override
		public void paintToBeDone(Graphics2D g) {

			// painting the selection items
			// Set<SelectionItem> items = new HashSet<SelectionItem>(
			// selectionItems);
			if (pointitems == null)
				return;

			for (SelectionItem item : pointitems) {

				item.paint(g);
			}
		}

		/**
		 * clears this painter
		 */
		protected void clear() {

			// recomputing the clip
			clips.clear();

			if (pointitems != null && pointitems.size() > 0) {

				// adding all the new clips
				for (SelectionItem item : pointitems) {

					clips.add(new Rectangle(item.getShapeBounds()));
				}
			}
		}

		/**
		 * stops displaying the selection items and cleans the canvas
		 */
		protected void clean() {

			SVGCanvas canvas = svgHandle.getScrollPane().getSVGCanvas();

			if (clips.size() > 0) {

				canvas.removePaintListener(this, true);
			}
		}

		/**
		 * reinitializes all the selection items
		 */
		protected void reinitialize(Set<SelectionItem> Items, int nLayer) {

			final SVGCanvas canvas = svgHandle.getScrollPane().getSVGCanvas();

			pointitems = Items;
			if (clips.size() > 0) {

				canvas.removePaintListener(this, true);
				clips.clear();
			}

			if (pointitems != null && pointitems.size() > 0) {

				// adding all the new clips
				for (SelectionItem item : pointitems) {
					// 增加边角箭头对应的矩形
					clips.add(new Rectangle(item.getShapeBounds()));
				}

				final PointItemsCanvasPainter s = this;
				canvas.addLayerPaintListener(nLayer, s, true);
				// SwingWorker worker = new SwingWorker() {
				//
				// @Override
				// protected Object doInBackground() throws Exception {
				// canvas.addLayerPaintListener(SVGCanvas.DRAW_LAYER, s,
				// true);
				// return null;
				// }
				// };
				// worker.execute();
			}
		}

		@Override
		public Set<Rectangle2D> getClip() {

			return clips;
		}
	}

	public SelectionChangedListener getDrawLineListener() {
		return drawLineListener;
	}

	public void setDrawLineListener(SelectionChangedListener drawLineListener) {
		this.drawLineListener = drawLineListener;
	}

	public void drawLineAtPoint(Point2D point) {
		point = svgHandle.getTransformsManager().getScaledPoint(point, true);
		if (selectedElements == null || selectedElements.size() != 1)
			return;
		Element element = selectedElements.iterator().next();

		if (element == null || !element.getNodeName().equals("path"))
			return;

		String sPath = element.getAttribute("d");
		Path path = new Path(sPath);
		int nCount = path.getSegmentsNumber();
		if (nCount != 2)
			return;

		Segment se = path.getSegment();
		Point2D bPoint = se.getEndPoint();
		se = se.getNextSegment();
		Point2D ePoint = se.getEndPoint();
		ExtendedGeneralPath egPath = null;// = new ExtendedGeneralPath();
		if (nLineMode == PARALLEL_LINE_MODE) { // 绘制平行线
			egPath = calParallelLine(bPoint, ePoint, point);
		} else if (nLineMode == VERTICAL_LINE_MODE) { // 绘制垂直线
			egPath = calVerticalLine(bPoint, ePoint, point);
		} else if (nLineMode == VERTICAL_MIDDLE_LINE_MODE) { // 绘制垂分线
			egPath = calVerticalMiddleLine(bPoint, ePoint, point);
		}
		PathShape shape = (PathShape) svgHandle.getEditor().getModuleByID(
				PathShape.MODULE_ID);
		String lineID = UUID.randomUUID().toString();
		Element el = shape.createElement(svgHandle, egPath, lineID);

		nLineMode = NONE_LINE_MODE;
		shape.resetDrawing();
		return;
	}

	/**
	 * 绘制平行线
	 * 
	 * @param beginPoint
	 *            起始点
	 * @param endPoint
	 *            终止点
	 * @param curPoint
	 *            点击点
	 * @return
	 */
	public ExtendedGeneralPath calParallelLine(Point2D beginPoint,
			Point2D endPoint, Point2D curPoint) {
		ExtendedGeneralPath egPath = new ExtendedGeneralPath();
		if (beginPoint.getX() == endPoint.getX()) {
			egPath.moveTo((float) curPoint.getX(), (float) beginPoint.getY());
			egPath.lineTo((float) curPoint.getX(), (float) endPoint.getY());

		} else if (beginPoint.getY() == endPoint.getY()) {
			egPath.moveTo((float) beginPoint.getX(), (float) curPoint.getY());
			egPath.lineTo((float) endPoint.getX(), (float) curPoint.getY());
		} else {
			double dSlope = (beginPoint.getX() - endPoint.getX())
					/ (beginPoint.getY() - endPoint.getY());
			if (Math.abs(dSlope) < 1) {
				double x1 = (endPoint.getY() - curPoint.getY()) * dSlope
						+ curPoint.getX();
				double x2 = (beginPoint.getY() - curPoint.getY()) * dSlope
						+ curPoint.getX();
				egPath.moveTo((float) x1, (float) endPoint.getY());
				egPath.lineTo((float) x2, (float) beginPoint.getY());
			} else {
				double y1 = (endPoint.getX() - curPoint.getX()) / dSlope
						+ curPoint.getY();
				double y2 = (beginPoint.getX() - curPoint.getX()) / dSlope
						+ curPoint.getY();
				egPath.moveTo((float) endPoint.getX(), (float) y1);
				egPath.lineTo((float) beginPoint.getX(), (float) y2);
			}
		}
		return egPath;
	}

	/**
	 * 绘制垂直线
	 * 
	 * @param beginPoint
	 *            起始点
	 * @param endPoint
	 *            终止点
	 * @param curPoint
	 *            点击点
	 * @return
	 */
	public ExtendedGeneralPath calVerticalLine(Point2D beginPoint,
			Point2D endPoint, Point2D curPoint) {
		ExtendedGeneralPath egPath = new ExtendedGeneralPath();
		if (beginPoint.getX() == endPoint.getX()) {
			egPath.moveTo((float) curPoint.getX(), (float) curPoint.getY());
			egPath.lineTo((float) beginPoint.getX(), (float) curPoint.getY());
		} else if (beginPoint.getY() == endPoint.getY()) {
			egPath.moveTo((float) curPoint.getX(), (float) curPoint.getY());
			egPath.lineTo((float) curPoint.getX(), (float) beginPoint.getY());
		} else {
			double dSlope = (beginPoint.getX() - endPoint.getX())
					/ (beginPoint.getY() - endPoint.getY());
			double y = (dSlope * endPoint.getY() + curPoint.getY() / dSlope
					+ curPoint.getX() - endPoint.getX())
					/ (dSlope + 1 / dSlope);
			double x = dSlope * (y - endPoint.getY()) + endPoint.getX();
			egPath.moveTo((float) (curPoint.getX()), (float) curPoint.getY());
			egPath.lineTo((float) (x), (float) y);
		}
		return egPath;
	}

	/**
	 * 绘制垂分线
	 * 
	 * @param beginPoint
	 *            起始点
	 * @param endPoint
	 *            终止点
	 * @param curPoint
	 *            点击点
	 * @return
	 */
	public ExtendedGeneralPath calVerticalMiddleLine(Point2D beginPoint,
			Point2D endPoint, Point2D curPoint) {
		ExtendedGeneralPath egPath = new ExtendedGeneralPath();
		if (beginPoint.getX() == endPoint.getX()) {
			egPath.moveTo((float) curPoint.getX(),
					(float) (beginPoint.getY() + endPoint.getY()) / 2);
			egPath.lineTo((float) beginPoint.getX(),
					(float) (beginPoint.getY() + endPoint.getY()) / 2);
		} else if (beginPoint.getY() == endPoint.getY()) {
			egPath.moveTo((float) (beginPoint.getX() + endPoint.getX()) / 2,
					(float) curPoint.getY());
			egPath.lineTo((float) (beginPoint.getX() + endPoint.getX()) / 2,
					(float) beginPoint.getY());
		} else {
			double dSlope = (beginPoint.getX() - endPoint.getX())
					/ (beginPoint.getY() - endPoint.getY());
			// double y =( dSlope*endPoint.getY() + (beginPoint.getY() +
			// endPoint.getY())/2/dSlope + (beginPoint.getX() +
			// endPoint.getX())/2 - endPoint.getX())/(dSlope + 1 /dSlope);
			int offset = 50;
			if ((curPoint.getX() - beginPoint.getX()) / dSlope
					+ beginPoint.getY() >= curPoint.getY())
				offset = -50;
			double y = (beginPoint.getY() + endPoint.getY()) / 2 + offset;
			double x = (beginPoint.getX() + endPoint.getX()) / 2 - offset
					/ dSlope;
			egPath.moveTo((float) ((beginPoint.getX() + endPoint.getX()) / 2),
					(float) (beginPoint.getY() + endPoint.getY()) / 2);
			egPath.lineTo((float) (x), (float) y);
		}
		return egPath;
	}

	/**
	 * add by yuxiang,2008.12.03 平移画布操作
	 * 
	 * @param initPoint
	 *            ：平移起始点
	 * @param currentPoint
	 *            ：平移当前点
	 */
	public void moveCanvas(Point2D initPoint, Point2D currentPoint) {
		int x = svgHandle.getCanvas().getScrollPane().getInnerScrollpane()
				.getHorizontalScrollBar().getValue();
		int y = svgHandle.getCanvas().getScrollPane().getInnerScrollpane()
				.getVerticalScrollBar().getValue();
		svgHandle.getCanvas().getScrollPane().getInnerScrollpane()
				.getHorizontalScrollBar().setValue(
						x - (int) (currentPoint.getX() - initPoint.getX()));
		svgHandle.getCanvas().getScrollPane().getInnerScrollpane()
				.getVerticalScrollBar().setValue(
						y - (int) (currentPoint.getY() - initPoint.getY()));
	}

	public SelectionChangedListener getDescribeTextListener() {
		return describeTextListener;
	}

	public void setDescribeTextListener(
			SelectionChangedListener describeTextListener) {
		this.describeTextListener = describeTextListener;
	}

	public void addTerminalPoint(Point2D point) {
		Point2D scaledPoint = svgHandle.getTransformsManager().getScaledPoint(
				point, true);
		svgHandle.getEditor().getSelectionManager().getTerminalmodule()
				.manualAddTerminal(scaledPoint);
	}

	public SelectionFilterIF getSelectionFilter() {
		return selectionFilter;
	}

	public void setSelectionFilter(SelectionFilterIF selectionFilter) {
		this.selectionFilter = selectionFilter;
	}

	public SelectionManager getActionSelectionManager() {
		return actionSelectionManager;
	}

	private GroupBreakerIF groupBreaker = null;

	public GroupBreakerIF getGroupBreaker() {
		return groupBreaker;
	}

	public void setGroupBreaker(GroupBreakerIF groupBreaker) {
		if (this.groupBreaker != null) {
			System.err
					.println("Warning: GroupBreaker in a SVGHandle Object is recommended to be set ONCE!! If you are not set this value manualy, this warning may happened when multiple listener is added during SVGHandle creation.");
		}
		this.groupBreaker = groupBreaker;
	}

	/**
	 * 获取鼠标在连接线移过去的时候断定可以连接的图元
	 * 
	 * @return
	 */
	public PseudoConnectedElement getPseudoConnectedElement() {
		return pseudoConnectedElement;
	}

}
