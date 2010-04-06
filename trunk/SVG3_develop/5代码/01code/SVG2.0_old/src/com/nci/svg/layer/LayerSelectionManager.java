package com.nci.svg.layer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.xpath.XPathExpressionException;

import org.jdesktop.swingworker.SwingWorker;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nci.svg.util.Utilities;

import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.EditorToolkit;

public class LayerSelectionManager {

	public final static String VISIBILITY = "visibility";
	public final static String VISIBLE = "visible";
	public final static String HIDDEN = "hidden";
	public final static String LAYER_EFFECT = "layerEffect"; // layerselected="true",style变化--visibilitiy:visible
	// or hidden
	private Editor editor;
	
	private HashMap<String, Element> layerElementMap = new HashMap<String, Element>();

	public LayerSelectionManager(Editor editor) {
		this.editor = editor;
	}
	
	public HashMap<String,Element> getLayerElementMap(){
		return layerElementMap;
	}

	/**
	 * 显示选择的图层
	 * 
	 * @param allSelectedLayersMap
	 *            所有选择的图层,key是图层类型的ID（如设备类型图层是CimType，电压等级图层是class等等），value是每类图层中选择的具体层名
	 *            ，如变压器层、110kv层等等
	 */
	// public void showLayers(
	// final HashMap<String, ArrayList<String>> allSelectedLayersMap, final
	// boolean keepText) {
	// SwingWorker<Object, Object> worker = new SwingWorker<Object, Object>() {
	//
	// @Override
	// protected Object doInBackground() throws Exception {
	// Document svgDoc =
	// editor.getHandlesManager().getCurrentHandle().getCanvas().getDocument();
	// hideShapes(svgDoc.getDocumentElement(), keepText);
	// ArrayList<Element> visibleChildren =
	// getVisibleShapes(svgDoc.getDocumentElement(), allSelectedLayersMap);
	// Element nodeToBeVisible = null;
	// for (int i = 0; i < visibleChildren.size(); i++) {
	// if (visibleChildren.get(i) instanceof Element) {
	// nodeToBeVisible = (Element) visibleChildren.get(i);
	// while (!EditorToolkit.isElementAShape(nodeToBeVisible)) {
	// nodeToBeVisible = (Element) nodeToBeVisible.getParentNode();
	// }
	// EditorToolkit.setStyleProperty(nodeToBeVisible,
	// VISIBILITY, VISIBLE);
	// }
	// }
	// return null;
	// }
	//
	// @Override
	// public void done() {
	// editor.getSvgSession().refreshCurrentHandleImediately();
	// }
	// };
	// worker.execute();
	// }
	public void showLayers(
			final HashMap<String, ArrayList<String>> allSelectedLayersMap) {
		SwingWorker<Object, Object> worker = new SwingWorker<Object, Object>() {

			@Override
			protected Object doInBackground() throws Exception {
				Document svgDoc = editor.getHandlesManager().getCurrentHandle()
						.getCanvas().getDocument();
				restore(svgDoc);
//				 Utilities.printNode(svgDoc.getDocumentElement(), true);
				hideShapes_HN(allSelectedLayersMap);
////				hideShapes(svgDoc.getDocumentElement(), false);
////				 Utilities.printNode(svgDoc.getDocumentElement(), true);
//				ArrayList<Element> visibleChildren = getVisibleShapes(svgDoc
//						.getDocumentElement(), allSelectedLayersMap);
//				Element nodeToBeVisible = null;
//				for (int i = 0; i < visibleChildren.size(); i++) {
//					if (visibleChildren.get(i) instanceof Element) {
//						nodeToBeVisible = (Element) visibleChildren.get(i);
//						while (!EditorToolkit.isElementAShape(nodeToBeVisible)) {
//							nodeToBeVisible = (Element) nodeToBeVisible
//									.getParentNode();
//						}
//						//如果是g节点，还要破下去
//						if (nodeToBeVisible.getNodeName().equals("g")) {
//							NodeList children = nodeToBeVisible.getChildNodes();
//							for (int n = 0; n < children.getLength(); n++) {
//								if (children.item(n) instanceof Element) {
//									nodeToBeVisible = (Element) children
//											.item(n);
//									if (EditorToolkit
//											.isElementAnActualShape(nodeToBeVisible)) {
//										EditorToolkit.setStyleProperty(
//												nodeToBeVisible, VISIBILITY,
//												VISIBLE);
//									}
//								}
//							}
//						} else {
//							if(nodeToBeVisible.getNodeName().equalsIgnoreCase("text")){
////								nodeToBeVisible.setNodeValue(nodeToBeVisible.getNodeValue()+"xx");
//								NodeList tspans = nodeToBeVisible.getElementsByTagName("tspan");
//								Utilities.printNode(nodeToBeVisible, true);
//								for(int n=0;n<tspans.getLength();n++){
//									Utilities.printNode(tspans.item(n), true);
//									if(tspans.item(n) instanceof Element){
//										EditorToolkit.setStyleProperty((Element)tspans.item(n),
//												VISIBILITY, VISIBLE);
//									}
//									
//								}
//								EditorToolkit.setStyleProperty(nodeToBeVisible,
//										VISIBILITY, VISIBLE);
//							}else
//							EditorToolkit.setStyleProperty(nodeToBeVisible,
//									VISIBILITY, VISIBLE);
//						}
//					}
//				}

				return null;
			}

			@Override
			public void done() {
				editor.getSvgSession().refreshCurrentHandleImediately();
			}
		};
		worker.execute();
	}

	/**
	 * 获取将要显示的图形元素
	 * 
	 * @param root
	 * @param allSelectedLayersMap
	 * @return
	 */
	private ArrayList<Element> getVisibleShapes(Element root,
			HashMap<String, ArrayList<String>> allSelectedLayersMap) {
		Iterator<String> it = allSelectedLayersMap.keySet().iterator();
		String layerTypeName = null;
		ArrayList<String> layers = null;
		ArrayList<ArrayList<Element>> result = new ArrayList<ArrayList<Element>>();
		while (it.hasNext()) {
			StringBuffer xpathExpr = new StringBuffer();
			layerTypeName = it.next();
			layers = allSelectedLayersMap.get(layerTypeName);
			if (layers.size() > 0) {
				ArrayList<Element> partResult = new ArrayList<Element>();
				for (int i = 0; i < layers.size(); i++) {
					if (i != 0) {// 只有在第一个循环的时候不需要加“|”，其它情况都需要；
						xpathExpr.append("|");
					}
					xpathExpr.append("//*").append("[@").append(layerTypeName)
							.append("='").append(layers.get(i)).append("']");
					try {
						NodeList firstSearchResult = Utilities.findNodes(
								xpathExpr.toString(), root);
						Node visiblePre = null;
						for (int k = 0; k < firstSearchResult.getLength(); k++) {
							visiblePre = firstSearchResult.item(k);
//							 Utilities.printNode(visiblePre, true);
							if (visiblePre instanceof Element) {
								while (!EditorToolkit
										.isElementAShape((Element) visiblePre)) {
//									 Utilities.printNode(visiblePre, true);
									visiblePre = (Element) visiblePre
											.getParentNode();
									if (visiblePre == null)
										break;
								}
//								 Utilities.printNode(visiblePre, true);
								if (visiblePre != null
										&& !isOriginalHiddenElement((Element) visiblePre)) {

									partResult.add((Element) visiblePre);
								}
							}
						}
					} catch (XPathExpressionException e) {
						e.printStackTrace();
					}
				}
				result.add(partResult);
			}
		}
		// 以第一个结果集作为基准，为下面的其它结果集比较，如果不存在相同的节点，则remove掉
		ArrayList<Element> finals = result.get(0);
		for (int i = 1; i < result.size(); i++) {
			ArrayList<Element> previous = result.get(i);
			for (int k = finals.size() - 1; k >= 0; k--) {
				boolean flag = false;
				for (int n = 0; n < previous.size(); n++) {

					if (finals.get(k).equals(previous.get(n))) {
						flag = true;
						break;
					}
				}
				if (!flag)
					finals.remove(k);
			}
		}
		return finals;
	}
	
	private  void hideShapes_HN(HashMap<String, ArrayList<String>> allSelectedLayersMap){
		Iterator<Element> it_layerEle = layerElementMap.values().iterator();
		while(it_layerEle.hasNext()){
			Element layerEle = it_layerEle.next();
//			Utilities.printNode(layerEle, true);
			EditorToolkit.setStyleProperty(layerEle, "visibility", "hidden");
		}
		Iterator<ArrayList<String>> it = allSelectedLayersMap.values().iterator();
		while(it.hasNext()){
			ArrayList<String> layers = it.next();
			for(int i=0;i<layers.size();i++){
				Element layerEle = layerElementMap.get(layers.get(i));
				EditorToolkit.setStyleProperty(layerEle, "visibility", "");
			}
		}
	}

	/**
	 * 隐藏所有图形
	 * 
	 * @param allChildren
	 */
	private void hideShapes(Node node, boolean keepText) {
		NodeList children = node.getChildNodes();
		Node child = null;
		for (int i = 0; i < children.getLength(); i++) {
			child = children.item(i);
			if (child.getNodeName().equals("g")) {
				hideShapes(child, keepText);
			}
			if (child instanceof Element) {
				if (EditorToolkit.isElementAnActualShape((Element) child)) {
					// 最最开始已经是隐藏的图元就不必再作任何动作
					if (editor.getSVGToolkit().getStyleProperty(
							(Element) child, VISIBILITY).equals(HIDDEN)
							&& isOriginalHiddenElement((Element) child)) {
						continue;
					}
					markAsLayerEffected((Element) child);
					if (child.getNodeName().equalsIgnoreCase("text")) {
						
						if (keepText) {
							EditorToolkit.setStyleProperty((Element) child,
									VISIBILITY, VISIBLE);
							continue;
						}
					}
					EditorToolkit.setStyleProperty((Element) child, VISIBILITY,
							HIDDEN);
				}
			}
		}

	}

	/**
	 * 判断该元素的层过滤前是否已经隐藏了
	 * 
	 * @param ele
	 * @return
	 */
	private boolean isOriginalHiddenElement(Element ele) {
//		Utilities.printNode(ele, true);
		if(ele.getNodeName().equals("g")){
		NodeList children = ele.getChildNodes();
		if (children.getLength() > 0) {
			for (int i = 0; i < children.getLength(); i++) {
				if (children.item(i) instanceof Element) {
					if (EditorToolkit.isElementAShape((Element) children
							.item(i))) {
						String layerEffect = ((Element) children.item(i))
								.getAttribute(LAYER_EFFECT);
						if (layerEffect != null && !layerEffect.equals(""))
							return false;
					}
				}
			}
		}
		}else {
			String layerEffect = ele.getAttribute(LAYER_EFFECT);
			if (layerEffect != null && !layerEffect.equals(""))
				return false;
		}
		return true;
	}

	/**
	 * 做一个标签，表明这个元素是受图层影响的
	 * 
	 * @param ele
	 */
	private void markAsLayerEffected(Element ele) {
		ele.setAttribute(LAYER_EFFECT, "1");
	}

	/**
	 * 将层过滤影响到的所有节点都还原成原有可视状态，并将层影响标签去除
	 * 
	 * @param svgDoc
	 */
//	public void restore(Document svgDoc) {
//		try {
//			NodeList effectNodes = Utilities.findNodes("//*[@" + LAYER_EFFECT
//					+ "]", svgDoc.getDocumentElement());
//			Element ele = null;
//			for (int i = 0; i < effectNodes.getLength(); i++) {
//				ele = (Element) effectNodes.item(i);
//				ele.removeAttributeNS(null, LAYER_EFFECT);
//				EditorToolkit.setStyleProperty(ele, VISIBILITY, null);
//			}
//		} catch (XPathExpressionException ex) {
//			ex.printStackTrace();
//		}
//	}
	public void restore(Document svgDoc) {
		Iterator<Element> layer_it = layerElementMap.values().iterator();
		while(layer_it.hasNext()){
			EditorToolkit.setStyleProperty(layer_it.next(), VISIBILITY, "");
		}
	}

	/**
	 * 仅仅将层影响的标签去除
	 * 
	 * @param svgDoc
	 */
	public void removeEffectAttrib(Document svgDoc) {
		try {
			NodeList effectNodes = Utilities.findNodes("//*[@" + LAYER_EFFECT
					+ "]", svgDoc.getDocumentElement());
			Element ele = null;
			for (int i = 0; i < effectNodes.getLength(); i++) {
				ele = (Element) effectNodes.item(i);
				ele.removeAttributeNS(null, LAYER_EFFECT);
			}
		} catch (XPathExpressionException ex) {
			ex.printStackTrace();
		}
	}
}
