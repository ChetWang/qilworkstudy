package com.nci.svg.server.operinterface;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.bean.SimpleCodeBean;
import com.nci.svg.sdk.bean.SysSetBean;
import com.nci.svg.sdk.communication.ActionNames;
import com.nci.svg.sdk.communication.ActionParams;
import com.nci.svg.sdk.graphunit.SymbolModelBean;
import com.nci.svg.sdk.server.operationinterface.OperInterfaceAdapter;
import com.nci.svg.server.util.Utilities;

/**
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author yx.nci
 * @时间：2008-12-16
 * @功能：PSMS系统接口
 * 
 */
public class PsmsModelOperInterface extends OperInterfaceAdapter {
	private static final long serialVersionUID = 3587091643213973802L;

	private Set topEquip_parent_cls_uri = new HashSet();

	private String shortName = "PSMS_MODEL_URL";

	public PsmsModelOperInterface(HashMap parameters) {
		super(parameters);
		topEquip_parent_cls_uri.add("OWCLASS://EPMS_TO/EPMS_EQUI");
		topEquip_parent_cls_uri
				.add("OWCLASS://EPMS_TO/EPMS_FLOC/EPMS_FUNCLOC_F/EPMS_PSR_F");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.server.service.OperationServiceModuleAdapter#handleOper(java.lang.String,
	 *      java.util.Map)
	 */
	public ResultBean handleOper(String actionName, Map requestParams) {
		ResultBean resultBean = null;
		if (actionName
				.equalsIgnoreCase(OperInterfaceDefines.GET_MODEL_LIST_NAME)) {
			// 获取模型列表
			resultBean = getModuleList((String) requestParams.get(ActionParams.MODULE_ID),
					(String) requestParams.get(ActionParams.TYPE));
		} else if (actionName
				.equalsIgnoreCase(OperInterfaceDefines.GET_MODEL_PARAMS_NAME)) {
			// 获取模型参数
			resultBean = getModelParamsOutterFace((String) requestParams
					.get(ActionParams.MODULE_ID));
		} else if (actionName
				.equalsIgnoreCase(OperInterfaceDefines.GET_MODEL_ACTIONLIST_NAME)) {
			// 获取模型动作
			resultBean = getModuleActions((String) requestParams
					.get(ActionParams.MODULE_ID));
		} else if (actionName
				.equalsIgnoreCase(OperInterfaceDefines.GET_GRAPH_UNIT_AND_MODULE_RELA_NAME)) {
			// 图元与模型关系获取
			resultBean = getGraphUnitAndModuleRela((String) requestParams
					.get(ActionParams.SYMBOL_TYPE), (String) requestParams.get(ActionParams.MODULE_ID));
		} else if (actionName
				.equalsIgnoreCase(OperInterfaceDefines.MODIFY_GRAPH_UNIT_AND_MODULE_RELA_NAME)) {
			// 图元与模型关系维护
			resultBean = modifyGraphUnitAndModuleRela((String) requestParams
					.get(ActionParams.SYMBOL_ID),
					(String) requestParams.get(ActionParams.MODULE_ID),
					((Integer) requestParams.get("option")).intValue(),
					(String) requestParams.get("optionObj"));
		} else {
			resultBean = new ResultBean(ResultBean.RETURN_ERROR, "无此服务", null,
					null);
		}
		return resultBean;
	}

	/**
	 * 获取模型列表
	 * 
	 * @param moduleID:模型编号
	 * @return ResultBean 存放相当于二维数组的arraylist
	 * @throws Exception
	 */
	private ResultBean getModuleList(String moduleID, String type) {

		Map requestParams = new HashMap();
		requestParams.put("shortName", shortName);

		String url = null;
		ResultBean referBean = refersTo(ActionNames.GET_SYSSETS, requestParams);
		if (referBean == null
				|| referBean.getReturnFlag() == ResultBean.RETURN_ERROR) {
			new ResultBean(ResultBean.RETURN_ERROR, "获取服务器地址失败", null, null);
		}
		url = ((SysSetBean) referBean.getReturnObj()).getParam1();
		HashMap map = new HashMap();
		String res;
		try {
			if (moduleID != null) {
				map.put("MODELID", moduleID);
				String param = Utilities.getModXml(map);
				String[][] params = { { "action", "getModuleList" },
						{ "param", param } };
				res = (String) new Utilities().communicateWithURL(url, params);
			} else {
				String[][] params = { { "action", "getModuleList" } };
				res = (String) new Utilities().communicateWithURL(url, params);
			}
			// System.out.println(res);
			// if (type == null || type.length() == 0)
			// return new ResultBean(1, null, "ArrayList", parseXml(res,
			// "getModulList"));
			// else {
			return new ResultBean(ResultBean.RETURN_SUCCESS, null, "HashMap",
					parseXml(res));
			// }
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultBean(ResultBean.RETURN_ERROR, e.getMessage(),
					null, null);
		}
	}

	/**
	 * 获取模型参数
	 * 
	 * @param moduleID:模型编号
	 * @return
	 * @throws Exception
	 */
	public ResultBean getModelParamsOutterFace(String moduleID) {

		Map requestParams = new HashMap();
		requestParams.put("shortName", shortName);

		String url = null;
		ResultBean referBean = refersTo(ActionNames.GET_SYSSETS, requestParams);
		if (referBean == null
				|| referBean.getReturnFlag() == ResultBean.RETURN_ERROR) {
			new ResultBean(ResultBean.RETURN_ERROR, "获取服务器地址失败", null, null);
		}
		url = ((SysSetBean) referBean.getReturnObj()).getParam1();
		HashMap map = new HashMap();
		String res;
		map.put("MODELID", moduleID);
		List pro = new ArrayList();
		HashMap property;
		Iterator it = null;
		try {
			String param = Utilities.getModXml(map);
			String[][] params = { { "action", "getModeProperty" },
					{ "param", param } };
			res = (String) new Utilities().communicateWithURL(url, params);
			property = Utilities.parseXml(res);

			it = property.keySet().iterator();
			while (it.hasNext()) {
				String code = (String) it.next();
				pro.add(new SimpleCodeBean(code, (String) property.get(code)));
			}

			return new ResultBean(ResultBean.RETURN_SUCCESS, null, "ArrayList",
					pro);
		} catch (Exception e) {
			return new ResultBean(ResultBean.RETURN_ERROR, e.getMessage(),
					null, null);
		}
	}

	/**
	 * 获取模型动作
	 * 
	 * @param moduleID:模型编号
	 * @return
	 */
	private ResultBean getModuleActions(String moduleID) {

		Map requestParams = new HashMap();
		requestParams.put("shortName", shortName);

		String url = null;
		ResultBean referBean = refersTo(ActionNames.GET_SYSSETS, requestParams);
		if (referBean == null
				|| referBean.getReturnFlag() == ResultBean.RETURN_ERROR) {
			new ResultBean(ResultBean.RETURN_ERROR, "获取服务器地址失败", null, null);
		}
		url = ((SysSetBean) referBean.getReturnObj()).getParam1();
		HashMap map = new HashMap();
		String res;
		map.put("MODELID", moduleID);
		try {
			String param = Utilities.getModXml(map);
			String[][] params = { { "action", "getModuleActions" },
					{ "param", param } };
			res = (String) new Utilities().communicateWithURL(url, params);
			System.out.println(res);
			return new ResultBean(ResultBean.RETURN_SUCCESS, null, "HashMap",
					Utilities.parseXml(res));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultBean(ResultBean.RETURN_ERROR, e.getMessage(),
					null, null);
		}
	}

	/**
	 * 图元与模型关系获取
	 * 
	 * @param graphUnitID:图元编号
	 * @param moduleID:模型编号
	 * @return
	 */
	private ResultBean getGraphUnitAndModuleRela(String graphUnitID,
			String moduleID) {
		return null;
	}

	/**
	 * 图元与模型关系维护
	 * 
	 * @param graphUnitID:图元编号
	 * @param moduleID:模型编号
	 * @param option
	 *            操作类型 1、新增操作；2、删除操作
	 * @param optionObj
	 *            操作对象：SYMBOL、图元维护 TEMPLATE 模板维护）
	 * @return
	 */
	private ResultBean modifyGraphUnitAndModuleRela(String graphUnitID,
			String moduleID, int option, String optionObj) {
		Map requestParams = new HashMap();
		requestParams.put("shortName", shortName);

		String url = null;
		ResultBean referBean = refersTo(ActionNames.GET_SYSSETS, requestParams);
		if (referBean == null
				|| referBean.getReturnFlag() == ResultBean.RETURN_ERROR) {
			new ResultBean(ResultBean.RETURN_ERROR, "获取服务器地址失败", null, null);
		}
		url = ((SysSetBean) referBean.getReturnObj()).getParam1();
		HashMap map = new HashMap();
		Boolean res;
		map.put("OPTION", "" + option);
		map.put("OPTIONOBJ", optionObj);
		map.put("SYMBOLID", graphUnitID);
		map.put("MODULEID", moduleID);

		try {
			String param = Utilities.getModXml(map);
			String[][] params = { { "action", "modifyGraphUnitAndModuleRela" },
					{ "param", param } };
			res = (Boolean) new Utilities().communicateWithURL(url, params);
			if (res.booleanValue()) {
				return new ResultBean(ResultBean.RETURN_SUCCESS, null,
						"Boolean", res);
			} else {
				return new ResultBean(ResultBean.RETURN_ERROR,
						"数据库操作异常，操作不成功！", "Boolean", res);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultBean(ResultBean.RETURN_ERROR, e.getMessage(),
					null, null);
		}
	}

	/**
	 * @function：业务工具类-将传入以xml的参数转换成HashMap
	 * @param xml
	 *            传入以xml形式的参数
	 * @return ArrayList
	 * @author ZHANGSF
	 */
	private TreeModel parseXml(String xml, String method) throws Exception {
		// List list=new ArrayList();
		Map map = new HashMap();
		// SAXBuilder bld = new SAXBuilder();
		String description = null, cls_uri = null, parent_cls_uri = null, fieldname = null, displayname = null;

		HashMap property = null;
		List state = null;
		Iterator it = null;
		// 状态集合，现都为开关
		state = new ArrayList();
		// 测试用数据
		state.add("开");
		state.add("关");

		SymbolModelBean smb;
		if (xml != null) {
			DocumentBuilderFactory docBuildFactory = DocumentBuilderFactory
					.newInstance();
			Document doc = docBuildFactory.newDocumentBuilder().parse(
					new ByteArrayInputStream(xml.getBytes()));
			Element root = doc.getDocumentElement();
			NodeList nodeList = root.getElementsByTagName("param");
			int size = nodeList.getLength();
			for (int i = 0; i < size; i++) {
				NodeList childList = nodeList.item(i).getChildNodes();
				property = new HashMap();
				for (int j = 0; j < childList.getLength(); j++) {
					if (childList.item(j) instanceof Element) {
						Element e = (Element) childList.item(j);
						if (e.getNodeName().equalsIgnoreCase("cls_uri")) {
							cls_uri = e.getNodeValue();
						} else if (e.getNodeName().equalsIgnoreCase(
								"parent_cls_uri")) {
							parent_cls_uri = e.getNodeValue();
						} else if (e.getNodeName().equalsIgnoreCase(
								"description")) {
							description = e.getNodeValue();
						} else if (e.getNodeName().equalsIgnoreCase("rparam")) {
							fieldname = e.getElementsByTagName("fieldname")
									.item(0).getNodeValue();
							displayname = e.getElementsByTagName("displayname")
									.item(0).getNodeValue();
							property.put(fieldname, displayname);
						}
					}
				}
				smb = new SymbolModelBean(cls_uri, description, parent_cls_uri,
						state, property);
				map.put(cls_uri, smb);
			}
		}
		return null;
		// XPath xpath = XPath.newInstance("//param");
		// List childNodes = xpath.selectNodes(doc);
		//
		// it = childNodes.iterator();
		// SymbolModelBean smb;
		// while (it.hasNext()) {
		// e = (Element) it.next();
		// description = e.getChild("description").getText();
		// cls_uri = e.getChild("cls_uri").getText();
		// parent_cls_uri = e.getChild("parent_cls_uri").getText();
		// // FIXME 这里只取了状态， zhansf需要更正
		// ResultBean rs = getModelParamsOutterFace(cls_uri);
		// // property = (HashMap) rs.getReturnObj();
		// state=(List) getModelParamsOutterFace(cls_uri).getReturnObj();
		// smb = new SymbolModelBean(cls_uri, description, parent_cls_uri,
		// state, property);
		// map.put(cls_uri, smb);
		// }
		// }
		// root = new DefaultMutableTreeNode("模型");
		// parseToTreeModel(map);
		// NCITreeModel treeModel = new NCITreeModel(root);
		// return treeModel;
	}

	private HashMap parseXml(String xml) throws Exception {
		HashMap map = new HashMap();
		// SAXBuilder bld = new SAXBuilder();
		String description = null, cls_uri = null, parent_cls_uri = null, fieldname = null, displayname = null;

		HashMap property = null;
		List state = null;
		Iterator it = null;
		// 状态集合，现都为开关
		state = new ArrayList();
		// 测试用数据
		state.add("开");
		state.add("关");

		SymbolModelBean smb;
		if (xml != null) {
			DocumentBuilderFactory docBuildFactory = DocumentBuilderFactory
					.newInstance();
			Document doc = docBuildFactory.newDocumentBuilder().parse(
					new ByteArrayInputStream(xml.getBytes()));
			Element root = doc.getDocumentElement();
			NodeList nodeList = root.getElementsByTagName("param");
			int size = nodeList.getLength();
			for (int i = 0; i < size; i++) {
				NodeList childList = nodeList.item(i).getChildNodes();
				property = new HashMap();
				for (int j = 0; j < childList.getLength(); j++) {
					if (childList.item(j) instanceof Element) {
						Element e = (Element) childList.item(j);
						if (e.getNodeName().equalsIgnoreCase("cls_uri")) {
							cls_uri = e.getFirstChild().getNodeValue();
						} else if (e.getNodeName().equalsIgnoreCase(
								"parent_cls_uri")) {
							parent_cls_uri = e.getFirstChild().getNodeValue();
						} else if (e.getNodeName().equalsIgnoreCase(
								"description")) {
							description = e.getFirstChild().getNodeValue();
						} else if (e.getNodeName().equalsIgnoreCase("rparam")) {
							fieldname = e.getElementsByTagName("fieldname")
									.item(0).getFirstChild().getNodeValue();
							displayname = e.getElementsByTagName("displayname")
									.item(0).getFirstChild().getNodeValue();
							property.put(fieldname, displayname);
						}
					}
				}
				smb = new SymbolModelBean(cls_uri, description, parent_cls_uri,
						state, property);
				map.put(cls_uri, smb);
			}
		}
		return map;
	}

	DefaultMutableTreeNode root = null;

	private void parseToTreeModel(Map models) {
		Iterator it = models.keySet().iterator();
		HashMap nodesMap = new HashMap();
		while (it.hasNext()) {
			String clsID = (String) it.next();
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(models
					.get(clsID));
			nodesMap.put(clsID, node);
		}
		Iterator it2 = models.keySet().iterator();
		while (it2.hasNext()) {
			SymbolModelBean modelBean = (SymbolModelBean) models
					.get((String) it2.next());
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) nodesMap
					.get(modelBean.getId());
			if (nodesMap.get(modelBean.getParentId()) == null) {
				root.add(node);
			} else {
				((DefaultMutableTreeNode) nodesMap.get(modelBean.getParentId()))
						.add(node);
			}
		}
	}

	public static void main(String[] args) {

		PsmsModelOperInterface ps = new PsmsModelOperInterface(new HashMap());
		ResultBean rb = ps.getModuleList(null, null);
		System.out.println(rb.getReturnFlag());
		System.out.println(((HashMap) rb.getReturnObj()).size());
		System.out.println(rb.getErrorText());
		// System.out.println(((List) rb.getReturnObj()).size());
		System.out.println("over");
		// JFrame f = new JFrame("xxxxx");
		// f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// JScrollPane scrool = new JScrollPane();
		// f.getContentPane().add(scrool);
		// JTree tree = new JTree();
		// DefaultTreeModel model = (DefaultTreeModel) rb.getReturnObj();
		// tree.setModel(model);
		// scrool.setViewportView(tree);
		// f.pack();
		// f.setVisible(true);
	}

}
