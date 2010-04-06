package com.nci.svg.sdk.client.util;

import java.awt.Color;
import java.awt.geom.Area;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.batik.dom.svg.SVGOMGElement;
import org.apache.batik.dom.svg.SVGOMUseElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.nci.svg.sdk.bean.CodeInfoBean;
import com.nci.svg.sdk.bean.GraphFileBean;
import com.nci.svg.sdk.bean.ModelBean;
import com.nci.svg.sdk.bean.ModelPropertyBean;
import com.nci.svg.sdk.bean.ModelRelaIndunormBean;
import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.client.DataManageAdapter;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.graphunit.NCIEquipSymbolBean;

import fr.itris.glips.library.util.XMLPrinter;
import fr.itris.glips.svgeditor.display.canvas.SVGCanvas;
import fr.itris.glips.svgeditor.display.handle.SVGHandle;
import fr.itris.glips.svgeditor.display.selection.Selection;

/**
 * @author yx.nci 公共操作方法类
 */
public class NCISVGSession {

	private EditorAdapter editor;

	private ArrayList<String> javascriptCommands = new ArrayList<String>();

	/**
	 * 登录的用户
	 */
	private String user = "";

	public NCISVGSession(EditorAdapter editor) {
		this.editor = editor;
		user = System.getProperty("user.name");
	}

	boolean increaseDecrese = false;
	int resizeSize = 0;

	/**
	 * 刷新已经打开的所有SVGHandle对象
	 * 
	 * @param background
	 */
	public void refreshAllHandles(Color background) {
		increaseDecrese = !increaseDecrese;

		if (increaseDecrese) {
			resizeSize = -1;
		} else {
			resizeSize = 1;
		}
		for (SVGHandle handle : editor.getHandlesManager().getHandles()) {
			if (background != null)
				handle.getCanvas().setBackground(background, true);
			// 目的是为了达到updateUI一样的效果，这里由于暂时无法找到全部更新图元的方法（拖动过图元后，留下的轨迹不会被下一个背景覆盖），
			// 所以只能以缩放窗口实现。
			handle.getSVGFrame().setSize(
					handle.getSVGFrame().getWidth() + resizeSize,
					handle.getSVGFrame().getHeight() + resizeSize);
			// handle.getCanvas().updateUI();
		}
	}

	public byte[] lock = new byte[0];
	// 一次操作刷新的次数
	public int refreshCountsOnce = 0;

	public Set<Area> refreshAreas = new HashSet<Area>();

	public int getRefreshCountsOnce() {
		return refreshCountsOnce;
	}

	public void setRefreshCountsOnce(int refreshCountsOnce) {
		this.refreshCountsOnce = refreshCountsOnce;
	}

	/**
	 * 刷新指定svghandle对象(当前)，这里只记录次数，如果同时又多次，最后也仅需刷新一次,
	 * 最后刷的那次务必调用refreshCurrentHandle()
	 */
	public void refreshHandle() {
		synchronized (lock) {
			refreshCountsOnce++;
		}
	}

	/**
	 * 刷新指定svghandle对象(当前)，这里只记录次数，如果同时又多次，最后也仅需刷新一次,
	 * 最后刷的那次务必调用refreshCurrentHandle()
	 * 
	 * @param elements:待刷新的节点集
	 */
	public void refreshHandle(Set<Element> elements) {
		synchronized (lock) {
			refreshCountsOnce++;
			SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
			Area area = handle.getCanvas().getAreaByElements(elements);
			refreshAreas.add(area);
		}
	}

	/**
	 * 刷新指定svghandle对象(当前)，这里只记录次数，如果同时又多次，最后也仅需刷新一次,
	 * 最后刷的那次务必调用refreshCurrentHandle()
	 * 
	 * @param element:待刷新的节点
	 */
	public void refreshHandle(Element element) {
		synchronized (lock) {
			refreshCountsOnce++;
			SVGHandle handle = editor.getHandlesManager().getCurrentHandle();
			Set<Element> elements = new HashSet<Element>();
			elements.add(element);
			Area area = handle.getCanvas().getAreaByElements(elements);
			refreshAreas.add(area);
		}
	}

	/**
	 * 真正开始刷新当前的SVGHandle对象
	 */
	public void refreshCurrentHandleLater() {
		if (refreshCountsOnce > 0) {
			SwingUtilities.invokeLater(new Runnable() {

				public void run() {
					SVGHandle handle = editor.getHandlesManager()
							.getCurrentHandle();
					if (refreshAreas == null || refreshAreas.size() == 0)
						handle.getCanvas().refreshCanvasContent(true, false,
								null);
					else
						handle.getCanvas().refreshCanvasContent(true, true,
								refreshAreas);
					refreshCountsOnce = 0;
					refreshAreas.clear();
				}
			});
			// Utilities.executeRunnable(new Runnable() {
			//
			// public void run() {
			// SVGHandle handle = editor.getHandlesManager()
			// .getCurrentHandle();
			// if (refreshAreas == null || refreshAreas.size() == 0)
			// handle.getCanvas().refreshCanvasContent(true, false,
			// null);
			// else
			// handle.getCanvas().refreshCanvasContent(true, true,
			// refreshAreas);
			// refreshCountsOnce = 0;
			// refreshAreas.clear();
			// }
			// });
		}
	}

	/**
	 * 直接全屏刷新指定的handle
	 * 
	 * @param handle
	 */
	public void refreshHandleLater(final SVGHandle handle) {
		if (refreshCountsOnce > 0) {
			SwingUtilities.invokeLater(new Runnable() {

				public void run() {
					handle.getCanvas().refreshCanvasContent(true, false, null);
					refreshCountsOnce = 0;
					refreshAreas.clear();
				}
			});
		}
	}

	public void refreshCurrentHandleImediately() {
		refreshHandle();
		refreshCurrentHandleLater();
	}

	public void refreshElement(Element oldEle, Element newEle) {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				SVGHandle handle = editor.getHandlesManager()
						.getCurrentHandle();
				handle.getCanvas().refreshCanvasContent(false, false, null);
			}
		});
	}

	public void refreshElement(final Set<Element> sets) {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				SVGHandle handle = editor.getHandlesManager()
						.getCurrentHandle();
				// handle.getCanvas().refreshCanvasContent(false, true, sets);
			}
		});
	}

	public String getUser() {
		return user;
	}

	public void setUser(String loginUser) {
		user = loginUser;
	}

	public SVGHandle open(File file) {
		final String path = file.toURI().toString();
		SVGHandle handle = editor.getHandlesManager().getHandle(path);

		if (handle != null) {

			editor.getHandlesManager().removeHandle(path);

		}
		{

			// creating a new handle
			handle = editor.getHandlesManager().createSVGHandle(path, 0);

			editor.getResourcesManager().notifyListeners();
			SVGCanvas canvas = handle.getScrollPane().getSVGCanvas();

			canvas.setURI(path, null);

		}
		return handle;
	}

	/**
	 * add by yux,2008-12-30 根据图属性bean打开文件
	 * 
	 * @param bean:文件属性bean
	 */
	public void open(GraphFileBean bean) {
		if (bean == null)
			return;

		// 保存至临时文件夹下
		String strPath = Constants.NCI_SVG_CACHE_DIR + "temp\\";

		String strFileName = "";
		strFileName = strPath + "\\" + bean.getFileName();
		File file = new File(strPath);
		file.mkdirs();
		file = new File(strFileName);
		if (file != null) {
			XMLPrinter.printStringToFile(bean.getContent(), file);
			SVGHandle handle = open(file);
			if (handle != null) {
				handle.getCanvas().setGraphFileBean(bean, true);
			}
			return;
		}
	}

	/**
	 * 获取js命令
	 * 
	 * @return
	 */
	public ArrayList<String> getJavascriptCommands() {
		return javascriptCommands;
	}

	/**
	 * 迭代导入use嵌套的图元
	 * 
	 * @param doc
	 * @param firstSymbolEle
	 * @param defsEle
	 */
	public void iteratorUseElementInserting(Document doc, Element firstSymbolEle) {
		// try {
		Element defsEle = null;
		NodeList defs = doc.getDocumentElement().getElementsByTagName("defs");
		if (defs.getLength() <= 0) {
			defsEle = doc.createElement("defs");
			doc.getDocumentElement().appendChild(defsEle);
		} else {
			defsEle = (Element) defs.item(0);
		}
		NodeList symbolsFromFirstSymbolEle = firstSymbolEle
				.getElementsByTagName("symbol");
		if (symbolsFromFirstSymbolEle != null
				&& symbolsFromFirstSymbolEle.getLength() > 0) {
			for (int k = 0; k < symbolsFromFirstSymbolEle.getLength(); k++) {
				Element element = (Element) doc.importNode(firstSymbolEle
						.getElementsByTagName("symbol").item(k), true);
				String id = element.getAttribute("id");

				NodeList nodes = defsEle.getElementsByTagName("symbol");
				boolean exist = false;
				for (int i = 0; i < nodes.getLength(); i++) {
					String existID = ((Element) nodes.item(i))
							.getAttribute("id");
					if (existID.equals(id)) {
						exist = true;
						break;
					}
				}
				if (!exist) {
					defsEle.appendChild(element);
				}
			}
		}

		// // 查找出来嵌套的use子节点
		// NodeList childUse = element.getElementsByTagName("use");
		//					
		// NCIEquipSymbolBean symbolBean;
		// Element useSymbolEle;
		// for (int i = 0; i < childUse.getLength(); i++) {
		// element = (Element) childUse.item(i);
		// // Utilities.printNode(doc.getDocumentElement(), true);
		// String idpre =
		// element.getAttributeNS("http://www.w3.org/1999/xlink","xlink:href");
		// id = idpre.substring(1);
		// symbolBean = editor.getGraphUnitManager()
		// .getSymbolMap_useID().get(id);
		// if (symbolBean != null
		// && symbolBean.getContent() != null) {
		// useSymbolEle = Utilities.getXMLDocumentByString(
		// symbolBean.getContent())
		// .getDocumentElement();
		// iteratorUseElementInserting(doc, useSymbolEle);
		// }
		// }
		// }
		// }

		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

	/**
	 * 通用方法
	 * 
	 * @param methodName
	 * @param params
	 * @param paramTypes
	 * @return
	 */
	public Object invoke(String methodName, Object[] params, Class[] paramTypes) {
		try {
			return this.getClass().getMethod(methodName, paramTypes).invoke(
					this, params);
		} catch (IllegalArgumentException e) {

			e.printStackTrace();
		} catch (SecurityException e) {

			e.printStackTrace();
		} catch (IllegalAccessException e) {

			e.printStackTrace();
		} catch (InvocationTargetException e) {

			e.printStackTrace();
		} catch (NoSuchMethodException e) {

			e.printStackTrace();
		}
		return null;
	}

	// /**
	// * 根据图元或模板的名称获取本地磁盘上该图元或模板的路径
	// *
	// * @param name
	// * 图元或模板的名称
	// * @return 本地磁盘上该图元或模板的路径
	// */
	// public String getSymbolPath(NCIEquipSymbolBean symbol) {
	// ResultBean result = editor
	// .getDataManage()
	// .getData(
	// DataManageAdapter.KIND_CODES,
	// symbol.getType().equalsIgnoreCase(
	// NCIEquipSymbolBean.SYMBOL_TYPE_GRAPHUNIT) ?
	// CodeConstants.SVG_GRAPHUNIT_VARIETY
	// : CodeConstants.SVG_TEMPLATE_VARIETY,
	// symbol.getVariety().getCode());
	// CodeInfoBean code = (CodeInfoBean) result.getReturnObj();
	// if (code == null) {
	// editor.getLogger().log(
	// editor.getModule(GraphUnitModuleAdapter.idGraphUnitManage),
	// LoggerAdapter.ERROR,
	// "无法找到对应的代码表值:" + symbol.getVariety().getCode() + "Type:"
	// + symbol.getType());
	// return null;
	// }
	// return new StringBuffer().append(Constants.NCI_SVG_SYMBOL_CACHE_DIR)
	// .append(symbol.getType()).append("/").append(code.getName())
	// .append("/").append(symbol.getName()).append(
	// Constants.NCI_SVG_EXTENDSION).toString();
	// }

	public void showMessageBox(String title, String info) {
		JOptionPane.showConfirmDialog(editor.findParentFrame(), info, title,
				JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
	}

	public String getNameFromCode(String type, String value) {
		if (value == null || type == null || type.length() == 0
				|| value.length() == 0)
			return null;
		ResultBean bean = editor.getDataManage().getData(
				DataManageAdapter.KIND_CODES, type, value);
		if (bean == null || bean.getReturnFlag() == ResultBean.RETURN_ERROR)
			return null;
		return ((CodeInfoBean) bean.getReturnObj()).getName();
	}

	/**
	 * add by yux,2009-1-20 根据图元名称，获取模型
	 * 
	 * @param symbolID
	 * @return
	 */
	public ModelBean getModelBeanBySymbolName(String symbolName) {
		String modelID = getModelIDBySymbolName(symbolName);
		if (modelID == null)
			return null;
		ResultBean resultBean = editor.getDataManage().getData(
				DataManageAdapter.KIND_MODEL, null, null);
		if (resultBean == null
				|| resultBean.getReturnFlag() == ResultBean.RETURN_ERROR)
			return null;
		HashMap<String, ModelBean> models = (HashMap<String, ModelBean>) resultBean
				.getReturnObj();
		if (models == null)
			return null;

		return models.get(modelID);

	}

	/**
	 * add by yux,2009-1-20 根据模型编号获取该模型对应的唯一标识数据
	 * 
	 * @param modelID
	 * @return
	 */
	public ModelRelaIndunormBean getModelIDProperty(String modelID) {
		ArrayList<ModelRelaIndunormBean> list = getModelRelaInfoByModelID(modelID);
		if (list == null)
			return null;

		for (ModelRelaIndunormBean bean : list) {
			if (bean.getModelPropertyType().equals(ModelPropertyBean.TYPE_ID)
					&& bean.getDescShortName() == null)
				return bean;
		}
		return null;
	}

	public ModelRelaIndunormBean getModelIDPropertyBySymbolID(String symbolName) {
		String modelID = getModelIDBySymbolName(symbolName);
		if (modelID == null)
			return null;
		return getModelIDProperty(modelID);
	}

	/**
	 * add by yux,2009-1-20 根据模型编号获取该模型与业务规范的关联信息
	 * 
	 * @param modelID:模型编号
	 * @return:关联关系，如不存在则返回null
	 */
	public ArrayList<ModelRelaIndunormBean> getModelRelaInfoByModelID(
			String modelID) {
		if (modelID == null || modelID.length() == 0)
			return null;
		ResultBean resultBean = editor.getDataManage().getData(
				DataManageAdapter.KIND_MODELRELAINDUNORM, null, null);
		if (resultBean == null
				|| resultBean.getReturnFlag() == ResultBean.RETURN_ERROR)
			return null;
		HashMap<String, ArrayList<ModelRelaIndunormBean>> relas = (HashMap<String, ArrayList<ModelRelaIndunormBean>>) resultBean
				.getReturnObj();
		ArrayList<ModelRelaIndunormBean> list = relas.get(modelID);
		if (list == null)
			return null;
		return list;
	}

	/**
	 * add by yux,2009-1-20 根据图元名称号获取该模型与业务规范的关联信息
	 * 
	 * @param symbolName:图元名称
	 * @return:关联关系，如不存在则返回null
	 */
	public ArrayList<ModelRelaIndunormBean> getModelRelaInfoBySymbolName(
			String symbolName) {
		String modelID = getModelIDBySymbolName(symbolName);
		return getModelRelaInfoByModelID(modelID);
	}

	/**
	 * add by yux,2009-1-20 根据图元名称获取模型编号
	 * 
	 * @param symbolName：图元名称
	 * @return：如存在则返回模型编号，否则返回null
	 */
	public String getModelIDBySymbolName(String symbolName) {
		Iterator<Map<String, NCIEquipSymbolBean>> iterator = editor
				.getSymbolManager().getAllSymbols().values().iterator();
		String modelID = null;
		while (iterator.hasNext() && modelID == null) {
			Iterator<NCIEquipSymbolBean> iterator2 = iterator.next().values()
					.iterator();
			while (iterator2.hasNext()) {
				NCIEquipSymbolBean bean = iterator2.next();
				if (bean.getName().equals(symbolName)) {
					modelID = bean.getModelID();
					break;
				}
			}
		}
		return modelID;
	}

	/**
	 * add by yux,2009-1-20 根据图形节点获取调用的图元或模板的名称
	 * 
	 * @param element：图形节点
	 * @return：名称，如非图元或模板，则返回null
	 */
	public String getSymbolName(Element element) {
		String symbolName = null;
		if (element instanceof SVGOMUseElement) {
			String href = ((SVGOMUseElement) element).getHref().getBaseVal()
					.toString().substring(1);
			symbolName = href.substring(0, href
					.indexOf(Constants.SYMBOL_STATUS_SEP));
		} else if (element instanceof SVGOMGElement) {
			if (element.getAttribute(Constants.SYMBOL_TYPE) != null
					&& element.getAttribute(Constants.SYMBOL_TYPE).equals(
							NCIEquipSymbolBean.SYMBOL_TYPE_TEMPLATE)) {
				symbolName = element.getAttribute(Constants.SYMBOL_ID);
			}
		}

		return symbolName;
	}

	/**
	 * add by yux,2009-1-21 根据节点和模型规范关联数据获取业务唯一标识
	 * 
	 * @param element：节点
	 * @param bean：模型关联关联数据
	 * @return：如存在则返回业务唯一标识，不存在则返回null
	 */
	public String getElementBussID(Element element, ModelRelaIndunormBean bean) {
		String id = null;
		Element metadata = (Element) element.getElementsByTagName("metadata")
				.item(0);
		if (metadata == null)
			return null;

		Element el = (Element) element
				.getElementsByTagName(
						bean.getIndunormShortName() + ":"
								+ bean.getMetadataShortName()).item(0);
		if (el == null)
			return null;

		id = el.getAttribute(bean.getFieldShortName());
		return id;
	}

}
