/*
 * Created on 25 mars 2004
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
package fr.itris.glips.svgeditor.display.handle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingUtilities;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.nci.svg.sdk.bean.ModelBean;
import com.nci.svg.sdk.bean.ModelPropertyBean;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.client.util.Utilities;
import com.nci.svg.sdk.display.canvas.OperationArea;
import com.nci.svg.sdk.graphunit.NCIEquipSymbolBean;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.shape.ShapeAutoAjuster;

import fr.itris.glips.svgeditor.display.canvas.SVGCanvas;
import fr.itris.glips.svgeditor.display.canvas.SVGFrame;
import fr.itris.glips.svgeditor.display.canvas.SVGScrollPane;
import fr.itris.glips.svgeditor.display.canvas.dom.SVGDOMListenerManager;
import fr.itris.glips.svgeditor.display.canvas.dom.SVGDOMNormalizer;
import fr.itris.glips.svgeditor.display.handle.manager.SVGElementsManager;
import fr.itris.glips.svgeditor.display.handle.manager.SVGResourcesManager;
import fr.itris.glips.svgeditor.display.handle.manager.TransformsManager;
import fr.itris.glips.svgeditor.display.selection.Selection;
import fr.itris.glips.svgeditor.display.undoredo.UndoRedo;

/**
 * the class handling all information and data for the svg file display
 * 
 * @author ITRIS, Jordi SUC
 */
public class SVGHandle {

	/**
	 * the name of this svg handle
	 */
	private String name;

	/**
	 * 远程服务器上svg图像的名称
	 */
	private String remoteName;

	/**
	 * the internal frame that will be displayed in the desktop
	 */
	private SVGFrame svgFrame;

	/**
	 * the boolean set to true if the SVG picture has been modified
	 */
	private boolean modified = false;

	/**
	 * the undo/redo
	 */
	private UndoRedo undoRedo;

	/**
	 * the selection manager
	 */
	private Selection selection;

	/**
	 * the manager of the svg resources
	 */
	private SVGResourcesManager svgResourcesManager = new SVGResourcesManager(
			this);

	/**
	 * the svg dom normalizer
	 */
	private SVGDOMNormalizer svgDOMNormalizer = new SVGDOMNormalizer(this);

	/**
	 * the svg dom listeners manager
	 */
	private SVGDOMListenerManager svgDOMListenerManager = new SVGDOMListenerManager(
			this);

	/**
	 * the manager of the svg elements
	 */
	private SVGElementsManager svgElementsManager = new SVGElementsManager(this);

	/**
	 * the object provided methods to handle scale changes
	 */
	private TransformsManager handleScaleManager = new TransformsManager(this);

	private int handleType = 0;

	/**
	 * 图形所在的厂站名称
	 */
	private String substationName = "";
	/**
	 * svg图形存放在数据库中的唯一ID,对于图元来说是图元ID
	 */
	private String mapID = "";
	/**
	 * SVGHandle类型，HANDLE_TYPE_SVG值为0，表示是SVG图形编辑器Handle类型
	 */
	public static final int HANDLE_TYPE_SVG = 0;
	/**
	 * SVGHandle类型，HANDLE_TYPE_GRAPH_UNIT值为1，表示是图元编辑Handle类型,并且该图元是基本图元(老式的处理)
	 * 
	 * @deprecated
	 */
	public static final int HANDLE_TYPE_GRAPH_UNIT_OLD = 1;

	/**
	 * SVGHandle类型，HANDLE_TYPE_GRAPH_UNIT值为3，表示是图元编辑Handle类型,并且该图元是基本图元
	 */
	public static final int HANDLE_TYPE_SYMBOL_GRAPH_UNIT_NORMAL = 3;

	/**
	 * SVGHandle类型，HANDLE_TYPE_GRAPH_UNIT值为2，表示是图元编辑Handle类型，并且该图元是模板图元
	 */
	public static final int HANDLE_TYPE_SYMBOL_TEMPLATE = 2;

	private NCIEquipSymbolBean symbolBean = null;

	public int nFileType = 0;

	public String strNciClass = "";
	public String strNciName = "";
	/**
	 * 图元模板绘制或编辑时对应的模型对象
	 */
	private ModelBean symbolModelObj = null;
	/**
	 * 图元模板绘制或编辑时对应的模型对象的状态
	 */
	private String symbolStatus = null;

	private EditorAdapter editor;

	private OperationArea operArea = null;

	/**
	 * the constuctor of the class
	 * 
	 * @param name
	 *            the name that will be linked with the SVG picture
	 */
	public SVGHandle(String name, EditorAdapter editor) {
		this.editor = editor;
		undoRedo = new UndoRedo(this, editor);
		// creating the internal frame that will be displayed in the desktop
		svgFrame = new SVGFrame(this);
		handleType = HANDLE_TYPE_SVG;
		// setting the name of this handle
		setName(name);

	}

	public SVGHandle(String name, int handleType, EditorAdapter editor) {
		this.editor = editor;
		undoRedo = new UndoRedo(this, editor);
		this.handleType = handleType;
		// creating the internal frame that will be displayed in the desktop
		svgFrame = new SVGFrame(this);
		if (isSymbolHandle()) {
			operArea = new OperationArea(this);
			operArea.setStatusCompVisible(false);
			svgFrame.getContentPane().add(operArea, BorderLayout.NORTH);
		}
		// setting the name of this handle
		setName(name);

	}

	@Deprecated
	public SVGHandle(String name, int handleType, SVGFrame svgFrame,
			EditorAdapter editor) {
		this.editor = editor;
		undoRedo = new UndoRedo(this, editor);
		this.handleType = handleType;
		this.svgFrame = svgFrame;
		svgFrame.setSvgHandle(this);
		setName(name);

	}

	public int getHandleType() {
		return handleType;
	}

	public void setHandleType(int type) {
		this.handleType = type;
	}

	/**
	 * creates the selection handler on the svg canvas (can only be called by
	 * the svg canvas object)
	 */
	public void createSelection() {

		// creating the selection manager
		selection = new Selection(this);
	}

	/**
	 * @return the selection manager
	 */
	public Selection getSelection() {

		return selection;
	}

	/**
	 * @return the svg resources manager
	 */
	public SVGResourcesManager getSvgResourcesManager() {
		return svgResourcesManager;
	}

	/**
	 * @return the svg dom normalizer
	 */
	public SVGDOMNormalizer getSvgDOMNormalizer() {
		return svgDOMNormalizer;
	}

	/**
	 * @return the svg dom listeners manager
	 */
	public SVGDOMListenerManager getSvgDOMListenerManager() {
		return svgDOMListenerManager;
	}

	/**
	 * @return the svg elements manager
	 */
	public SVGElementsManager getSvgElementsManager() {
		return svgElementsManager;
	}

	/**
	 * @return the object provided methods to handle conversion of geometry
	 *         shape on the canvas
	 */
	public TransformsManager getTransformsManager() {
		return handleScaleManager;
	}

	/**
	 * @return the undo/redo manager
	 */
	public UndoRedo getUndoRedo() {

		return undoRedo;
	}

	/**
	 * @return true if the SVG picture has been modified
	 */
	public boolean isModified() {

		return modified;
	}

	/**
	 * sets that the svg document has been modified
	 * 
	 * @param modified
	 *            must be true if the svg picture has been modified
	 */
	public void setModified(boolean modified) {

		this.modified = modified;

		// SwingUtilities.invokeLater(new Runnable() {
		//
		// public void run() {
		//
		//				
		// }
		// });
		if (svgFrame != null) {

			svgFrame.setSVGFrameLabel(name);
		}
	}

	/**
	 * @return the svg frame linked to this handle
	 */
	public SVGFrame getSVGFrame() {

		return svgFrame;
	}

	/**
	 * @return the svg scrollpane linked to this handle
	 */
	public SVGScrollPane getScrollPane() {

		return svgFrame.getScrollpane();
	}

	/**
	 * @return the svg canvas linked to this handle
	 */
	public SVGCanvas getCanvas() {

		if (svgFrame != null) {

			return svgFrame.getCanvas();
		}

		return null;
	}

	/**
	 * @return the name of the handle, that is the name of the svg document
	 */
	public String getName() {

		return name;
	}

	/**
	 * sets the new name of the handle
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {

		this.name = name;
		svgFrame.setSVGFrameLabel(name);
	}

	/**
	 * @return the short name of this handle
	 */
	public String getShortName() {

		String shortName = "";

		try {
			File file = new File(name);
			shortName = file.getName();
		} catch (Exception ex) {
			shortName = null;
		}

		return shortName;
	}

	public void reinit() {
		svgFrame.setVisible(false);

		// disposing the internal frame
		if (svgFrame != null) {

			svgFrame.disposeFrame();
		}

		svgFrame = new SVGFrame(this);
	}

	/**
	 * disposes this handle
	 */
	public void dispose() {

		svgFrame.setVisible(false);

		// removing this handle from the handle manager
		editor.getHandlesManager().removeHandleWithoutDisposing(getName());

		// disposing items from the handles disposer
		HandlesManager.handleDisposer.disposeHandle(this);

		svgDOMListenerManager.dispose();
		svgResourcesManager.dispose();

		// clearing the undo/redo manager
		undoRedo.dispose();

		// disposing the selection manager
		if (selection != null) {

			selection.dispose();
		}

		// disposing the internal frame
		if (svgFrame != null) {

			svgFrame.disposeFrame();
		}

		name = null;
		svgFrame = null;
		undoRedo = null;
		selection = null;
		svgResourcesManager = null;
		svgDOMNormalizer = null;
		svgDOMListenerManager = null;
		svgElementsManager = null;
		handleScaleManager = null;
	}

	public void disposeGraphUnit() {
		// svgFrame.setVisible(false);
		// removing this handle from the handle manager
		editor.getHandlesManager().removeHandleWithoutDisposingGUnit(getName());

		// disposing items from the handles disposer
		HandlesManager.handleDisposer.disposeHandle(this);

		svgDOMListenerManager.dispose();
		svgResourcesManager.dispose();

		// clearing the undo/redo manager
		undoRedo.dispose();

		// disposing the selection manager
		if (selection != null) {

			selection.dispose();
		}

		// //disposing the internal frame
		// if(svgFrame!=null){
		//			
		// svgFrame.disposeFrame();
		// }

		name = null;
		// svgFrame=null;
		undoRedo = null;
		selection = null;
		svgResourcesManager = null;
		svgDOMNormalizer = null;
		svgDOMListenerManager = null;
		svgElementsManager = null;
		handleScaleManager = null;
	}

	/**
	 * closes the svg handle
	 */
	public void close() {

		editor.getIOManager().getFileCloseManager().closeHandle(this,
				getSVGFrame());
	}

	/**
	 * @return whether this handle is the current handle or not
	 */
	public boolean isSelected() {

		SVGHandle currentHandle = editor.getHandlesManager().getCurrentHandle();

		if (currentHandle != null && currentHandle.equals(this)) {

			return true;
		}

		return false;
	}

	/**
	 * enqueues the given runnable
	 * 
	 * @param runnable
	 *            a runnable
	 * @param elements
	 *            the set of the elements that are modified
	 * @param inAWTThread
	 *            whether the runnable should be invoked in the AWT thread
	 */
	public void enqueue(final Runnable runnable, final Set<Element> elements,
			boolean inAWTThread) {

		if (runnable != null) {

			if (inAWTThread && !SwingUtilities.isEventDispatchThread()) {

				SwingUtilities.invokeLater(new Runnable() {

					public void run() {

						enqueue(runnable, elements);
					}
				});

			} else {

				enqueue(runnable, elements);
			}
		}
	}

	/**
	 * enqueues the given runnable
	 * 
	 * @param runnable
	 *            a runnable
	 * @param elements
	 *            the set of the elements that are modified
	 */
	private void enqueue(final Runnable runnable, final Set<Element> elements) {

		// computing the bounds of each element and merging them
		Area oldBoundsArea = null;
		Rectangle2D bounds = null, bounds2 = null;

		for (Element element : elements) {

			// getting the bounds of the element
			bounds = getSvgElementsManager().getSensitiveBounds(element);
			bounds2 = getSvgElementsManager().getNodeGeometryBounds(element);

			if (bounds != null && bounds.getWidth() > 0
					&& bounds.getHeight() > 0 && bounds2 != null
					&& bounds2.getWidth() > 0 && bounds2.getHeight() > 0) {

				bounds.add(bounds2);
				bounds = getNormalizedRectangle(bounds);

				if (oldBoundsArea == null) {

					oldBoundsArea = new Area(bounds);

				} else {

					oldBoundsArea.add(new Area(bounds));
				}
			}
		}

		runnable.run();

		// computing the new bounds of each element and merging them
		Area newBoundsArea = null;

		for (Element element : elements) {

			// getting the bounds of the element
			bounds = getSvgElementsManager().getSensitiveBounds(element);
			bounds2 = getSvgElementsManager().getNodeGeometryBounds(element);

			if (bounds != null && bounds.getWidth() > 0
					&& bounds.getHeight() > 0 && bounds2 != null
					&& bounds2.getWidth() > 0 && bounds2.getHeight() > 0) {

				bounds.add(bounds2);
				bounds = getNormalizedRectangle(bounds);

				if (newBoundsArea == null) {

					newBoundsArea = new Area(bounds);

				} else {

					newBoundsArea.add(new Area(bounds));
				}
			}
		}

		// merging the old and new bounds areas
		Area allArea = null;

		if (oldBoundsArea != null) {

			allArea = new Area(oldBoundsArea);

			if (newBoundsArea != null) {

				allArea.add(newBoundsArea);
			}

		} else if (newBoundsArea != null) {

			allArea = new Area(newBoundsArea);
		}

		if (allArea != null) {

			svgFrame.getCanvas().requestUpdateContent(allArea);

			if (elements.size() > 0) {

				selection.setBlockSelectionItemsPaint(false);
			}
		}
	}

	/**
	 * enqueues the given runnable and waits until it's finished.
	 * 执行预定义的runnable线程。
	 * 
	 * @param runnable
	 *            a runnable
	 * @param elements
	 *            the set of the elements that are modified
	 */
	public void enqueueAndWait(final Runnable runnable,
			final Set<Element> elements) {

		if (runnable != null) {

			try {
				SwingUtilities.invokeAndWait(new Runnable() {

					public void run() {

						enqueue(runnable, elements);
					}
				});
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * modify by yux,2008.12.05 从protected修改为public computes the rectangle used
	 * for computing the bounds for redrawing shapes
	 * 
	 * @param rect
	 *            a rectangle
	 * @return the rectangle used for computing the bounds for redrawing shapes
	 */
	public Rectangle getNormalizedRectangle(Rectangle2D rect) {

		int x0 = (int) Math.floor(rect.getX());
		int y0 = (int) Math.floor(rect.getY());
		int x1 = (int) Math.ceil(rect.getX() + rect.getWidth());
		int y1 = (int) Math.ceil(rect.getY() + rect.getHeight());

		return new Rectangle(x0 - 2, y0 - 2, x1 - x0 + 5, y1 - y0 + 5);
	}

	/**
	 * picks the color at the given point on a canvas. 获取当前所在点的颜色信息
	 * 
	 * @param point
	 *            a point
	 * @return the color corresponding to the given point
	 */
	public Color pickColor(Point point) {

		Color color = new Color(255, 255, 255);

		if (point != null) {

			// getting the offscreen image of the canvas
			BufferedImage image = getScrollPane().getSVGCanvas().getOffscreen();

			int pos = image.getRGB(point.x, point.y);
			ColorModel model = image.getColorModel();

			if (pos != 0) {

				color = new Color(model.getRed(pos), model.getGreen(pos), model
						.getBlue(pos));
			}
		}

		return color;
	}

	/**
	 * 获取远程图形名称
	 * 
	 * @return
	 */
	public String getRemoteName() {
		return remoteName;
	}

	/**
	 * 设置远程图形名称
	 * 
	 * @param remoteName
	 */
	public void setRemoteName(String remoteName) {
		this.remoteName = remoteName;
	}

	/**
	 * 获取当前SVGHandle图形厂站名称
	 * 
	 * @return
	 */
	public String getSubstationName() {
		return substationName;
	}

	/**
	 * 设置当前图形的厂站名称
	 * 
	 * @param substationName
	 */
	public void setSubstationName(String substationName) {
		this.substationName = substationName;
	}

	/**
	 * @return the nFileType
	 */
	public int getNFileType() {
		return nFileType;
	}

	/**
	 * @param fileType
	 *            the nFileType to set
	 */
	public void setNFileType(String strfileType) {
		if (strfileType == null || strfileType.length() == 0)
			nFileType = 0;
		else
			nFileType = new Integer(strfileType).intValue();
	}

	/**
	 * @return the strNciClass
	 */
	public String getStrNciClass() {
		return strNciClass;
	}

	/**
	 * @param strNciClass
	 *            the strNciClass to set
	 */
	public void setStrNciClass(String strNciClass) {
		this.strNciClass = strNciClass;
	}

	/**
	 * @return the strNciName
	 */
	public String getStrNciName() {
		return strNciName;
	}

	/**
	 * @param strNciName
	 *            the strNciName to set
	 */
	public void setStrNciName(String strNciName) {
		this.strNciName = strNciName;
	}

	/**
	 * 获取svg图形对应的mapID
	 * 
	 * @return
	 */
	public String getMapID() {
		return mapID;
	}

	public void setMapID(String mapID) {
		this.mapID = mapID;
	}

	/**
	 * 获取SVGHandle的NCIEquipSymbolBean对象
	 * 
	 * @return SVGHandle的NCIEquipSymbolBean对象
	 */
	public NCIEquipSymbolBean getSymbolBean() {
		return symbolBean;
	}

	/**
	 * 设置SVGHandle的NCIEquipSymbolBean对象
	 * 
	 * @param symbolBean
	 *            NCIEquipSymbolBean对象
	 */
	public void setSymbolBean(NCIEquipSymbolBean symbolBean) {
		this.symbolBean = symbolBean;
		this.setName(symbolBean.getName());
	}

	public EditorAdapter getEditor() {
		return editor;
	}

	/**
	 * 获取图元模板对应的模型对象, 只有在图元或模板新建的时候可以获取到该对象
	 * 
	 * @return 图元模板对应的模型对象
	 */
	@Deprecated
	public ModelBean getSymbolModelObj() {
		return symbolModelObj;
	}

	/**
	 * 设置图元模板对应的模型对象
	 * 
	 * @param symbolModelObj
	 *            图元模板对应的模型对象
	 */
	public void setSymbolModelObj(ModelBean symbolModelObj) {
		if (handleType != HANDLE_TYPE_SYMBOL_GRAPH_UNIT_NORMAL
				&& handleType != HANDLE_TYPE_SYMBOL_TEMPLATE) {
			editor.getLogger().log(editor, LoggerAdapter.ERROR,
					new Exception("非图元/模板类型的SVG图不能设置SymbolModelBean"));
			return;
		}
		this.symbolModelObj = symbolModelObj;
		if (symbolModelObj == null)
			return;
		this.getSymbolBean().setModelID(symbolModelObj.getTypeBean().getId());
		//不同状态的图元经过模型切换后需要做的动作
		Document svgDoc = svgFrame.getCanvas().getDocument();
		Element svgEle = svgDoc.getDocumentElement();
		if (handleType == HANDLE_TYPE_SYMBOL_GRAPH_UNIT_NORMAL) {			
			HashMap<String,ModelPropertyBean> statusmap = (HashMap<String,ModelPropertyBean>)symbolModelObj.getProperties().get(ModelPropertyBean.TYPE_STATUS);
			try {
				NodeList statusNodes = Utilities.findNodes("//*[@"
						+ Constants.SYMBOL_STATUS + "]", svgEle);
				ArrayList<String> existedStatus = new ArrayList<String>(
						statusNodes.getLength());
				
				for (int i = 0; i < statusNodes.getLength(); i++) {
					Element e = (Element) statusNodes.item(i);
					String oldStatus = e.getAttribute(Constants.SYMBOL_STATUS);
					existedStatus.add(oldStatus);
					Iterator<ModelPropertyBean> iterator = statusmap.values().iterator();
					boolean bEqual = false;
					while(iterator.hasNext())
					{
						ModelPropertyBean bean= (ModelPropertyBean)iterator.next();
						if(bean.getName().equals(oldStatus))
						{
							bEqual = true;
							break;
						}
					}
					if(!bEqual){//新状态没有的状态节点应该删除掉！！！
						svgEle.removeChild(e);
					}
				}
				if (statusmap != null && statusmap.size() > 0) {
					Iterator<ModelPropertyBean> iterator = statusmap.values().iterator();
					boolean bEqual = false;
					while(iterator.hasNext())
					{
						ModelPropertyBean bean= (ModelPropertyBean)iterator.next();
						String status = bean.getName();
						if (existedStatus.contains(status)) {// 说明新模型和旧模型有相同的状态存在
							// do nothing
						} else {// 是新状态,则增加节点
							Element statusGroup = svgDoc.createElementNS(svgDoc
									.getDocumentElement().getNamespaceURI(),
									"g");
							statusGroup.setAttribute(Constants.SYMBOL_TYPE,
									Utilities.getHandleSymbolType(handleType));
							statusGroup.setAttribute(Constants.SYMBOL_STATUS,
									status);
							statusGroup.setAttribute("id", UUID.randomUUID()
									.toString());
							svgEle.appendChild(statusGroup);
						}
					}
				}
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
		}else if(handleType == HANDLE_TYPE_SYMBOL_TEMPLATE){
			Element typeGroup = svgDoc.createElementNS(svgDoc
					.getDocumentElement().getNamespaceURI(),
					"g");
			typeGroup.setAttribute(Constants.SYMBOL_TYPE,
					Utilities.getHandleSymbolType(handleType));
			typeGroup.setAttribute("id", UUID.randomUUID()
					.toString());
			svgEle.appendChild(typeGroup);
			getSelection().setParentElement(typeGroup, false);
		}

	}

	/**
	 * 获取图元模板绘制或编辑时对应的模型对象的状态
	 * 
	 * @return 图元模板绘制或编辑时对应的模型对象的状态
	 */
	public String getSymbolStatus() {
		return symbolStatus;
	}

	/**
	 * 
	 * 设置图元模板绘制或编辑时对应的模型对象的状态
	 * 
	 * @param symbolStatus
	 *            图元模板绘制或编辑时对应的模型对象的状态
	 */
	public void setSymbolStatus(String symbolStatus,
			boolean notifyParentElementChange) {
		if (handleType != HANDLE_TYPE_SYMBOL_GRAPH_UNIT_NORMAL
				&& handleType != HANDLE_TYPE_SYMBOL_TEMPLATE) {
			editor.getLogger().log(editor, LoggerAdapter.ERROR,
					new Exception("非图元/模板类型的SVG图不能设置SymbolModelBean"));
			return;
		}
		this.symbolStatus = symbolStatus;
		if (symbolStatus == null){//没有状态
			
			return;
		}			
		Element svgEle = (Element) svgFrame.getCanvas().getDocument()
				.getDocumentElement();
		String xpathExpr = "//*[@" + Constants.SYMBOL_STATUS + "='"
				+ symbolStatus + "']";
		Element element = null;
		try {
			element = (Element) Utilities.findNode(xpathExpr, svgEle);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			element = null;
		}
		if (element != null) {
			// 设置绘制母节点，所有的绘制将在指定节点下进行
			selection.setParentElement(element, notifyParentElementChange);
		}
		showSymbolStatusArea();
	}

	/**
	 * 显示状态区域，如果没有状态标记，就没必要显示
	 * 
	 * @param doc
	 */
	private void showSymbolStatusArea() {
		try {
			if (!isSymbolHandle()) {
				return;
			}
			// 说明已经显示过了
			if (operArea.getStatusComboBox().getModel().getSize() > 0) {
				return;
			}
			// 找出是否有状态标记
			NodeList statusNodes = Utilities.findNodes("//*[@"
					+ Constants.SYMBOL_STATUS + "]", getSVGFrame().getCanvas()
					.getDocument().getDocumentElement());
			operArea.getStatusComboBox().removeAllItems();
			if (statusNodes != null && statusNodes.getLength() > 0) {
				for (int i = 0; i < statusNodes.getLength(); i++)
					((DefaultComboBoxModel) operArea.getStatusComboBox()
							.getModel()).addElement(((Element) statusNodes
							.item(i)).getAttribute(Constants.SYMBOL_STATUS));
				operArea.setStatusCompVisible(true);
				operArea.setDefaultSymbolStatus(symbolStatus, false);
			} else {
				operArea.setStatusCompVisible(false);
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
	
	private ShapeAutoAjuster autoAjuster;

	/**
	 * 判断当前Handle是否是图元或模板类型
	 * 
	 * @param symbolType
	 * @return
	 */
	public boolean isSymbolHandle() {
		if (handleType == SVGHandle.HANDLE_TYPE_SYMBOL_GRAPH_UNIT_NORMAL
				|| handleType == SVGHandle.HANDLE_TYPE_SYMBOL_TEMPLATE) {
			return true;
		}
		return false;
	}

	public OperationArea getStatusArea() {
		return operArea;
	}

	public ShapeAutoAjuster getAutoAjuster() {
		return autoAjuster;
	}

	public void setAutoAjuster(ShapeAutoAjuster autoAjuster) {
		this.autoAjuster = autoAjuster;
	}
}
