package com.nci.svg.sdk.graphunit;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGDocument;

import com.nci.svg.sdk.CodeConstants;
import com.nci.svg.sdk.bean.CodeInfoBean;
import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.bean.SimpleCodeBean;
import com.nci.svg.sdk.client.DataManageAdapter;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.communication.CommunicationBean;
import com.nci.svg.sdk.client.util.Constants;
import com.nci.svg.sdk.client.util.EditorToolkit;
import com.nci.svg.sdk.communication.ActionNames;
import com.nci.svg.sdk.communication.ActionParams;
import com.nci.svg.sdk.ui.graphunit.NCIThumbnailPanel;

public abstract class AbstractSymbolManager {

	protected String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;

	/**
	 * 所有图元和模板的HashMap集合，key是特定的SymbolTypeBean对象（包括图元或模板，类型代码，类型值），第二个是某个类型下的所有图元或模板
	 */
	protected Map<SymbolTypeBean, Map<String, NCIEquipSymbolBean>> symbolsMap = null;

	/**
	 * 图元类型值列表
	 */
	protected ArrayList<SymbolTypeBean> graphUnitTypes = null;

	/**
	 * 模板类型值列表
	 */
	protected ArrayList<SymbolTypeBean> templateTypes = null;
	/**
	 * 图元大类值列表
	 */
	protected ArrayList<String> symbolsTypesDefine = null;

	protected EditorAdapter editor;

	/**
	 * 当前已经显示过的thumbnail集合
	 */
	protected Map<SymbolTypeBean, Map<String, NCIThumbnailPanel>> thumbnailShownMap = new HashMap<SymbolTypeBean, Map<String, NCIThumbnailPanel>>();

	public AbstractSymbolManager(EditorAdapter editor) {
		this.editor = editor;
	}

	/**
	 * 获取缩略图
	 * 
	 * @param bean
	 * @param type
	 *            thumbnail 类型，可以是outlook类型（对应值是1），也可以是combobox类型（对应值是0）
	 * @return
	 */
	public abstract ArrayList<NCIThumbnailPanel> createThumnailList(
			SymbolTypeBean bean, int type);

	/**
	 * 打印Document对应得xml字符串
	 * 
	 * @param doc
	 * @param symboName
	 * @return
	 * @throws Exception
	 */
	public String printNode(Node doc, String symboName) throws Exception {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer t = tf.newTransformer();
		t.setOutputProperty("encoding", "UTF-8");
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		t.transform(new DOMSource(doc), new StreamResult(bos));
		String xmlStr = bos.toString();
		System.out.print(symboName + ":");
		System.out.println(xmlStr);
		return xmlStr;
	}

	/**
	 * 创建空的图元管理SVGDocument
	 * 
	 * @return
	 * @deprecated
	 */
	public SVGDocument createEmptyGraphUnitDocument() {
		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
		SVGDocument doc = (SVGDocument) impl.createDocument(svgNS, "svg", null);

		// gets the root element (the svg element)
		Element svgRoot = doc.getDocumentElement();
		svgRoot.setAttribute(Constants.NCI_SVG_Type_Attr,
				Constants.NCI_SVG_Type_GraphUnit_Value);
		svgRoot.setAttribute(Constants.NCI_SVG_XMLNS,
				Constants.NCI_SVG_XMLNS_VALUE);
		// set the width and height attribute on the root svg element
		svgRoot.setAttributeNS(null, "width",
				Constants.GRAPH_UNIT_WIDTH_StringValue);
		svgRoot.setAttributeNS(null, "height",
				Constants.GRAPH_UNIT_HEIGHT_StringValue);
		svgRoot
				.setAttribute(
						"viewBox",
						"0 0 "
								+ EditorToolkit
										.getPixelledNumber(Constants.GRAPH_UNIT_WIDTH_StringValue)
								+ " "
								+ EditorToolkit
										.getPixelledNumber(Constants.GRAPH_UNIT_HEIGHT_StringValue));
		// creating a defs element
		Element defsElement = doc
				.createElementNS(doc.getNamespaceURI(), "defs");
		svgRoot.appendChild(defsElement);
		return doc;
	}

	/**
	 * 获取图元或模板大类的各种类型
	 */
	public List<SymbolTypeBean> getSymbolTypeNames(String symbolType) {
		if (NCIEquipSymbolBean.SYMBOL_TYPE_GRAPHUNIT.equals(symbolType)) {
			if (graphUnitTypes == null) {
				graphUnitTypes = new ArrayList<SymbolTypeBean>();
				Iterator<SymbolTypeBean> it = symbolsMap.keySet().iterator();
				SymbolTypeBean typeBean = null;
				while (it.hasNext()) {
					typeBean = it.next();
					if (typeBean.getSymbolType().equals(symbolType)) {
						CodeInfoBean value = (CodeInfoBean) editor
								.getDataManage().getData(
										DataManageAdapter.KIND_CODES,
										CodeConstants.SVG_GRAPHUNIT_VARIETY,
										typeBean.getVariety().getCode())
								.getReturnObj();
						graphUnitTypes.add(new SymbolTypeBean(symbolType,
								new SimpleCodeBean(value.getValue(), value
										.getName())));
					}
				}
			}
			return graphUnitTypes;
		} else if (NCIEquipSymbolBean.SYMBOL_TYPE_TEMPLATE.equals(symbolType)) {
			if (templateTypes == null) {
				templateTypes = new ArrayList<SymbolTypeBean>();
				Iterator<SymbolTypeBean> it = symbolsMap.keySet().iterator();
				SymbolTypeBean typeBean = null;
				while (it.hasNext()) {
					typeBean = it.next();
					if (typeBean.getSymbolType().equals(symbolType)) {
						CodeInfoBean value = (CodeInfoBean) editor
								.getDataManage().getData(
										DataManageAdapter.KIND_CODES,
										CodeConstants.SVG_TEMPLATE_VARIETY,
										typeBean.getVariety().getCode())
								.getReturnObj();
						templateTypes.add(new SymbolTypeBean(symbolType,
								new SimpleCodeBean(value.getValue(), value
										.getName())));
					}
				}
			}
			return templateTypes;
		}
		return null;
	}

	/**
	 * 获取图元的类型
	 * 
	 * @return ArrayList<String> 图元类型集合
	 */
	public synchronized ArrayList<String> getSymbolsTypesDefine() {
		if (symbolsTypesDefine == null) {
			symbolsTypesDefine = new ArrayList<String>();
			symbolsTypesDefine.add(Constants.SINO_NAMED_GRAPHUNIT);
			symbolsTypesDefine.add(Constants.SINO_NAMED_TEMPLATE);
		}
		return symbolsTypesDefine;
	}

	/**
	 * 重命名Symbol
	 * 
	 * @param oldName
	 * @param newName
	 */
	public boolean renameSymbol(String oldName, String newName,
			String symbolType) {
		Iterator<SymbolTypeBean> it = symbolsMap.keySet().iterator();
		Map<String, NCIEquipSymbolBean> tempMap = null;
		SymbolTypeBean typeBean = null;
		boolean flag = false;
		boolean saved = false;
		while (it.hasNext()) {
			if (flag)
				break;
			typeBean = it.next();
			tempMap = symbolsMap.get(typeBean);
			Iterator<String> it2 = tempMap.keySet().iterator();
			String symbolName = null;
			NCIEquipSymbolBean symbolBean = null;
			while (it2.hasNext()) {
				symbolName = it2.next();
				if (symbolName.equals(oldName)) {
					saved = true;
					symbolBean = tempMap.get(symbolName);
					//  远程修改掉服务器上的信息---缓存+DB
					String[][] params = new String[4][2];
					params[0][0] = ActionParams.OWNER;
					params[0][1] = symbolBean.isReleased() ? OwnerVersionBean.OWNER_RELEASED
							: editor.getSvgSession().getUser();
					params[1][0] = ActionParams.SYMBOL_NEW_NAME;
					params[1][1] = newName;
					params[2][0] = ActionParams.SYMBOL_OLD_NAME;
					params[2][1] = oldName;
					params[3][0] = ActionParams.SYMBOL_TYPE;
					params[3][1] = symbolType;
					ResultBean rb = editor.getCommunicator().communicate(
							new CommunicationBean(ActionNames.RENAME_SYMBOL,
									params));
					if (rb.getReturnFlag() == ResultBean.RETURN_SUCCESS) {
						tempMap.remove(symbolName);
						tempMap.put(newName, symbolBean);
						Map<String, NCIThumbnailPanel> thumbMap = thumbnailShownMap
								.get(typeBean);
						if (thumbMap != null) {
							NCIThumbnailPanel p = thumbMap.get(oldName);
							if (p != null) {
								p.setText(newName);
								p.getSymbolBean().setName(newName);
								thumbMap.remove(oldName);
								thumbMap.put(newName, p);
							}
						}

					} else {
						JOptionPane.showConfirmDialog(editor
								.findParentFrame(), rb.getErrorText(), "ERROR",
								JOptionPane.CLOSED_OPTION,
								JOptionPane.ERROR_MESSAGE);
					}
					flag = true;
					break;
				}
			}
		}
		if(!saved){ //新建的，从未保存过
			flag = true;
		}
		return flag;
	}

	/**
	 * 图元选中的鼠标状态
	 */
	private int mouseStatus = -1;

	public int getMouseStatus() {
		return mouseStatus;
	}

	public void setMouseStatus(int mouseStatus) {
		this.mouseStatus = mouseStatus;
	}

	/**
	 * 获取已经显示过的Thumbnail Map
	 * 
	 * @return
	 */
	public Map<SymbolTypeBean, Map<String, NCIThumbnailPanel>> getThumbnailShownMap() {
		return thumbnailShownMap;
	}

	/**
	 * 获取所有图元和模板
	 * 
	 * @return
	 */
	public Map<SymbolTypeBean, Map<String, NCIEquipSymbolBean>> getAllSymbols() {
		return symbolsMap;
	}

	// /**
	// * 将缩略图放入指定的类型下的HashMap中
	// *
	// * @param bean
	// * @param thumbname
	// * @param thumbPanel
	// */
	// public void putIntoShownThumbnailMap(SymbolTypeBean bean, String
	// thumbname,
	// NCIThumbnailPanel thumbPanel) {
	// Iterator<SymbolTypeBean> it = thumbnailShownMap.keySet().iterator();
	// SymbolTypeBean temp = null;
	// // FIXME 完善显示的Thumbnail操作
	// while (it.hasNext()) {
	// temp = it.next();
	//
	// }
	// }

	/**
	 * 判断制定名称的图元或模板是否已经显示过了
	 * 
	 * @param symbolName
	 * @return
	 */
	public boolean isSymbolShown(String symbolName) {
		Iterator<Map<String, NCIThumbnailPanel>> it = thumbnailShownMap
				.values().iterator();
		Map<String, NCIThumbnailPanel> temp = null;
		while (it.hasNext()) {
			temp = it.next();
			if (temp.containsKey(symbolName)) {
				return true;
			}
		}
		return false;
	}

}
