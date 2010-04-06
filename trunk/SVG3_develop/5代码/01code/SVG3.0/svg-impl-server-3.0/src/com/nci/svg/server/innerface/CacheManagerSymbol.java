package com.nci.svg.server.innerface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.nci.svg.sdk.CodeConstants;
import com.nci.svg.sdk.bean.CodeInfoBean;
import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.bean.SimpleCodeBean;
import com.nci.svg.sdk.communication.ActionParams;
import com.nci.svg.sdk.graphunit.NCIEquipSymbolBean;
import com.nci.svg.sdk.graphunit.OwnerVersionBean;
import com.nci.svg.sdk.graphunit.SymbolTypeBean;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.server.cache.CacheManagerAdapter;
import com.nci.svg.sdk.server.cache.CacheObject;
import com.nci.svg.sdk.server.graphstorage.GraphStorageManagerAdapter;
import com.nci.svg.sdk.server.service.OperationServiceModuleAdapter;
import com.nci.svg.server.cache.CacheManagerKeys;

/**
 * <p>
 * 标题：CacheManagerSymbol.java
 * </p>
 * <p>
 * 描述： 图元缓存管理服务类 图元缓存结构为四层结构（三层Map） 最上层为已发布和各操作人 第二层为SymbolTypeBean 第三层为图元名称
 * 第四层为图元对象
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2008-12-18
 * @version 1.0
 */
public class CacheManagerSymbol extends OperationServiceModuleAdapter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8518029008441608659L;
	/**
	 * 初始化命令
	 */
	private final String INIT = "init";
	/**
	 * 维护指定图元
	 */
	private final String MODIFY_SYMBOL_CACHE = "modifySymbolCache";

	/**
	 * 图元缓存名称
	 */
	private final String SYMBOL_HASH = "symbolHash";
	/**
	 * 版本缓存名称
	 */
	private final String VERSION_HASH = "versionHash";

	public CacheManagerSymbol(HashMap parameters) {
		super(parameters);
	}

	public ResultBean handleOper(String actionName, Map requestParams) {
		// 判断是否成功获取管理组件对象
		if (controller == null) {
			return returnErrMsg("图元缓存管理，未能获取管理组件对象!");
		}
		// 判断是否成功获取日志操作对象
		if (log == null) {
			return returnErrMsg("图元缓存管理，未能获取日志操作对象!");
		}

		String subAction = (String) getRequestParameter(requestParams,
				ActionParams.SUB_ACTION);
		log.log(this, LoggerAdapter.DEBUG, "图元缓存管理类，获取到‘" + actionName
				+ "’请求命令！");

		ResultBean rb = new ResultBean();
		if (subAction.equalsIgnoreCase(INIT)) {
			// *************
			// 初始化图元缓存
			// *************
			int result = init();
			if (result == OPER_ERROR) {
				rb.setErrorText("未能成功运行图元缓存管理！");
				rb.setReturnFlag(ResultBean.RETURN_ERROR);
			} else {
				rb.setReturnFlag(ResultBean.RETURN_SUCCESS);
				rb.setReturnObj("成功运行图元缓存管理！");
				rb.setReturnType("String");
			}
		} else if (subAction.equalsIgnoreCase(MODIFY_SYMBOL_CACHE)) {
			// **************
			// 维护指定图元信息
			// **************
			String symbolType = (String) getRequestParameter(requestParams,
					ActionParams.SYMBOL_TYPE); // 获取图元大类
			String symbolName = (String) getRequestParameter(requestParams,
					ActionParams.SYMBOL_NAME); // 获取图元名称
			rb = manageSymbolCache(symbolType, symbolName);
		} else {
			rb.setReturnFlag(ResultBean.RETURN_ERROR);
			rb.setErrorText(actionName + "命令:子命令" + subAction + "，目前该请求未实现!");
		}
		return rb;
	}

	/**
	 * 维护指定大类和名称的图元缓存信息
	 * 
	 * @param symbolType:图元大类
	 * @param symbolName:图元名称
	 * @return
	 */
	private ResultBean manageSymbolCache(String symbolType, String symbolName) {
		if (symbolType == null || symbolType.length() <= 0) {
			return returnErrMsg("图元缓存维护，获取到图元大类参数为空！");
		}
		if (symbolName == null || symbolName.length() <= 0) {
			return returnErrMsg("图元缓存维护，获取到的图元名称为空！");
		}
		// 获取缓存管理器
		CacheManagerAdapter cma = controller.getCacheManager();
		if (cma == null) {
			return returnErrMsg("图元缓存维护，获取不到缓存管理器！");
		}
		// 获取图元缓存
		Map cacheHash = cma.getCacheObj(CacheManagerKeys.SYMBOL_MANAGER)
				.getCacheMap();
		if (cacheHash == null) {
			return returnErrMsg("图元缓存维护，获取不到图元缓存！");
		}
		// 获取图元版本缓存
		Map editionHash = cma.getCacheObj(
				CacheManagerKeys.SYMBOL_VERSION_MANAGER).getCacheMap();
		if (editionHash == null) {
			return returnErrMsg("图元缓存维护，获取不到图元版本缓存！");
		}
		// 获取指定名称的图元对象
		NCIEquipSymbolBean symbolBean = getSymbolIDFromName(symbolName);
		if (symbolBean == null) {
			return returnErrMsg("图元缓存维护，获取不到指定名称的图元对象！");
		}
		// 生成key
		SymbolTypeBean typeBean = new SymbolTypeBean();
		typeBean.setSymbolType(symbolBean.getType());
		typeBean.setVariety(new SimpleCodeBean(symbolBean.getVariety()
				.getCode(), symbolBean.getVariety().getName()));
		// 获取图元操作用户
		String operator = symbolBean.getOperator();
		// 获取指定操作用户的缓存
		Object tempObj = cacheHash.get(operator);
		Map tempMap;
		if (tempObj instanceof LinkedHashMap) {
			tempMap = (LinkedHashMap) tempObj;
			// **************
			// 更新图元缓存内容
			// **************
			Object subObject = tempMap.get(typeBean);
			LinkedHashMap subHashMap;
			if (subObject instanceof LinkedHashMap) {
				// 缓存中包含了该类型图元
				subHashMap = (LinkedHashMap) subObject;
			} else {
				// 缓存中不包含该类型图元
				subHashMap = new LinkedHashMap();
			}
			subHashMap.put(symbolBean.getName(), symbolBean);
			tempMap.put(typeBean, subHashMap);
			cacheHash.put(operator, tempMap);
			cma.setCacheObj(CacheObject.MAP_FLAG,
					CacheManagerKeys.SYMBOL_MANAGER, cacheHash);
			// *****************
			// 更新图元版本缓存内容
			// *****************
			String edition = symbolBean.getModifyTime();
			String cacheEditon = (String) editionHash.get(operator);
			if (edition.compareTo(cacheEditon) < 0) {
				edition = cacheEditon;
			}
			editionHash.put(operator, edition);
			cma.setCacheObj(CacheObject.MAP_FLAG,
					CacheManagerKeys.SYMBOL_VERSION_MANAGER, editionHash);
			return returnSuccMsg("String", "成功进行缓存更新操作！");
		} else {
			return returnErrMsg("图元缓存维护，获取不到指定用户缓存！");
		}

		// // 更新图元缓存内容
		// Object subObject = cacheHash.get(typeBean);
		// LinkedHashMap subHashMap;
		// if (subObject instanceof LinkedHashMap) {
		// // 缓存中包含了该类型图元
		// subHashMap = (LinkedHashMap) subObject;
		// } else {
		// // 缓存中不包含该类型图元
		// subHashMap = new LinkedHashMap();
		// }
		// subHashMap.put(symbolBean.getName(), symbolBean);
		// cacheHash.put(typeBean, subHashMap);
		//
		// cma.setCacheObj(CacheObject.MAP_FLAG,
		// CacheManagerKeys.SYMBOL_MANAGER,
		// cacheHash);
	}

	public int init() {
		if (controller == null)
			return OPER_ERROR;
		// ****************
		// 将代码信息载入缓存
		// ****************
		CacheManagerAdapter cma = controller.getCacheManager();
		if (cma == null) {
			return OPER_ERROR;
		}

		Map map = getSymbols(null);
		// 添加图元缓存数据
		cma.addCacheInnerData(CacheObject.MAP_FLAG,
				CacheManagerKeys.SYMBOL_MANAGER, map.get(SYMBOL_HASH));
		// 添加图元版本缓存数据
		cma.addCacheInnerData(CacheObject.MAP_FLAG,
				CacheManagerKeys.SYMBOL_VERSION_MANAGER, map.get(VERSION_HASH));
		// cma.getCacheObj(CacheManagerKeys.SYMBOL_MANAGER)
		return OPER_SUCCESS;
	}

	/**
	 * 将从数据库中获取到的图元信息列表转换成缓存结构，并获取各自的版本缓存
	 * 
	 * @param symbolType:String:图元大类(图元graphunit、模板template)
	 * @return 图元缓存
	 */
	protected Map getSymbols(String symbolType) {
		// 图元缓存和图元版本缓存
		HashMap hash = new HashMap();
		// 图元缓存
		Map symbolHash = new LinkedHashMap();
		// 版本缓存
		HashMap editionHash = new HashMap();
		// 获取图元表和模板表操作用户信息
		ArrayList operatorList = getSymbolOperators();

		// 获取图元详细信息列表，列表按图元大类和图元类型排序
		ArrayList symbolList = getSymbolList(symbolType);
		// 添加各用户未发布图元缓存
		for (int i = 0, size = operatorList.size(); i < size; i++) {
			String operatorName = (String) operatorList.get(i);
			ArrayList tempList = filtrateSymbolsList(operatorName, symbolList);
			symbolHash.put(operatorName, getSymbolMap(tempList));
			editionHash.put(operatorName, getSymbolEdition(tempList));
		}

		// 获取发布图元信息列表
		ArrayList tempList = getReleasedSymbolsList(symbolList);
		// 添加已发布图元缓存
		symbolHash.put(OwnerVersionBean.OWNER_RELEASED, getSymbolMap(tempList));
		// 添加已发布图元版本
		editionHash.put(OwnerVersionBean.OWNER_RELEASED,
				getSymbolEdition(tempList));

		hash.put(SYMBOL_HASH, symbolHash);
		hash.put(VERSION_HASH, editionHash);
		return hash;
	}

	/**
	 * 根据输入的图元信息列表组织成缓存结构
	 * 
	 * @param symbolList:ArrayList:图元信息列表
	 * @return
	 */
	private Map getSymbolMap(ArrayList symbolList) {
		// 获取包含图元大类和小类信息的缓存结构
		Map symbolHash = getSymbolTypeMap();

		// 获取图元信息列表长度
		int size = symbolList.size();
		// 判断图元是否被访问
		boolean[] symbolFlag = new boolean[size];
		for (int i = 0; i < size; i++) {
			symbolFlag[i] = false;
		}
		// 访问缓存组件，获取代码表信息
		CacheObject cacheObj = controller.getCacheManager().getCacheObj(
				CacheManagerKeys.CODE_MANAGER);
		Map codes = cacheObj.getCacheMap();
		CodeInfoBean codeInfo = null;

		Map subHash;
		SymbolTypeBean typeBean = null;
		for (int i = 0; i < size; i++) {
			NCIEquipSymbolBean symbolBean = (NCIEquipSymbolBean) symbolList
					.get(i);
			if (symbolBean == null || symbolFlag[i] == true)
				continue;
			// System.out.println(codes);
			String type = symbolBean.getType();
			// String variety = symbolBean.getVariety().getCode();
			// variety对应的名称也需要加上，而不是空字符串

			if (type.equalsIgnoreCase(NCIEquipSymbolBean.SYMBOL_TYPE_GRAPHUNIT)) {
				codeInfo = (CodeInfoBean) ((HashMap) codes
						.get(CodeConstants.SVG_GRAPHUNIT_VARIETY))
						.get(symbolBean.getVariety().getCode());
			} else if (type
					.equalsIgnoreCase(NCIEquipSymbolBean.SYMBOL_TYPE_TEMPLATE)) {
				codeInfo = (CodeInfoBean) ((HashMap) codes
						.get(CodeConstants.SVG_TEMPLATE_VARIETY))
						.get(symbolBean.getVariety().getCode());
				// typeBean = new SymbolTypeBean(type, variety,
				// codeInfo.getName());
			}
			typeBean = new SymbolTypeBean(type, new SimpleCodeBean(symbolBean
					.getVariety().getCode(), codeInfo.getName()));
			subHash = new LinkedHashMap();
			for (int j = i; j < size; j++) {
				NCIEquipSymbolBean subSymbolBean = (NCIEquipSymbolBean) symbolList
						.get(j);
				if (subSymbolBean == null || symbolFlag[j] == true)
					continue;
				if (!subSymbolBean.getVariety().getCode().equals(
						symbolBean.getVariety().getCode())
						|| !subSymbolBean.getType().equals(type))
					break;
				subHash.put(subSymbolBean.getName(), subSymbolBean);
				symbolFlag[j] = true;
			}
			symbolHash.put(typeBean, subHash);
		}

		return symbolHash;
	}

	/**
	 * 获取图形信息列表版本信息（所有图元最后修改时间）
	 * 
	 * @param symbolList
	 * @return
	 */
	private String getSymbolEdition(ArrayList symbolList) {
		String edition = "00000000000000";
		for (int i = 0, size = symbolList.size(); i < size; i++) {
			NCIEquipSymbolBean symbolBean = (NCIEquipSymbolBean) symbolList
					.get(i);
			String temp = symbolBean.getModifyTime();
			if (edition.compareTo(temp) < 0)
				edition = temp;
		}
		return edition;
	}

	/**
	 * 获取图元列表中被发布的图元
	 * 
	 * @param symbolList:ArrayList:图元信息列表
	 * @return
	 */
	private ArrayList getReleasedSymbolsList(ArrayList symbolList) {
		ArrayList tempList = new ArrayList();

		for (int i = 0, size = symbolList.size(); i < size; i++) {
			NCIEquipSymbolBean symbolBean = (NCIEquipSymbolBean) symbolList
					.get(i);
			if (symbolBean.isReleased()) {
				tempList.add(symbolBean);
			}
		}

		tempList.trimToSize();
		return tempList;
	}

	/**
	 * 将图元列表按照指定操作人过滤出来，其中图元数据不包含已发布的图元
	 * 
	 * @param operator:String:操作人
	 * @param symbolList:ArrayList:图元信息列表
	 * @return
	 */
	private ArrayList filtrateSymbolsList(String operator, ArrayList symbolList) {
		ArrayList tempList = new ArrayList();

		for (int i = 0, size = symbolList.size(); i < size; i++) {
			NCIEquipSymbolBean symbolBean = (NCIEquipSymbolBean) symbolList
					.get(i);
			if (!symbolBean.isReleased()
					&& symbolBean.getOperator().equals(operator)) {
				tempList.add(symbolBean);
			}
		}

		tempList.trimToSize();
		return tempList;
	}

	/**
	 * 获取包含图元大类和小类信息的缓存结构
	 * 
	 * @return
	 */
	private Map getSymbolTypeMap() {
		Map tempHash = new LinkedHashMap();
		// 获取图元大类和小类信息
		ArrayList symbolCodes = getSymbolCodes();
		// 按图元大类和小类先填充返回数据
		for (int i = 0, size = symbolCodes.size(); i < size; i++) {
			SymbolTypeBean typeBean = (SymbolTypeBean) symbolCodes.get(i);
			tempHash.put(typeBean, new LinkedHashMap());
		}
		return tempHash;
	}

	/**
	 * 获取图元大类和小类信息
	 * 
	 * @return
	 */
	private ArrayList getSymbolCodes() {
		CacheManagerAdapter cma = controller.getCacheManager();
		if (cma == null) {
			// 判断是否获得缓存管理器
			return null;
		}
		// 获取代码缓存
		Map cacheHash = cma.getCacheObj(CacheManagerKeys.CODE_MANAGER)
				.getCacheMap();
		if (cacheHash == null) {
			return null;
		}

		ArrayList symbolCodes = new ArrayList();
		SymbolTypeBean symbolType;

		HashMap graphUnitMap = (HashMap) cacheHash.get("SVG_GRAPHUNIT_VARIETY");
		Iterator it = graphUnitMap.entrySet().iterator();
		String type = NCIEquipSymbolBean.SYMBOL_TYPE_GRAPHUNIT;
		SimpleCodeBean codeBean;
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			Object key = entry.getKey();
			if (key instanceof String) {
				CodeInfoBean bean = (CodeInfoBean) entry.getValue();
				codeBean = new SimpleCodeBean((String) key, bean.getName());
				symbolType = new SymbolTypeBean(type, codeBean);
				symbolCodes.add(symbolType);
			}
		}
		HashMap templateMap = (HashMap) cacheHash.get("SVG_TEMPLATE_VARIETY");
		type = NCIEquipSymbolBean.SYMBOL_TYPE_TEMPLATE;
		it = templateMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			Object key = entry.getKey();
			if (key instanceof String) {
				CodeInfoBean bean = (CodeInfoBean) entry.getValue();
				codeBean = new SimpleCodeBean((String) key, bean.getName());
				symbolType = new SymbolTypeBean(type, codeBean);
				symbolCodes.add(symbolType);
			}
		}
		symbolCodes.trimToSize();
		return symbolCodes;
	}

	/**
	 * 获取图元列表
	 * 
	 * @param symbolType:图元大类(图元graphunit、模板template)
	 * @param operator:操作人，如果操作人为空则取消操作人过滤
	 * @return
	 */
	private ArrayList getSymbolList(String symbolType) {
		GraphStorageManagerAdapter gsma = controller.getGraphStorageManager();
		return gsma.getSymbolList(symbolType, null, null);
	}

	/**
	 * 获取图元表和模板表操作用户信息
	 * 
	 * @return
	 */
	private ArrayList getSymbolOperators() {
		GraphStorageManagerAdapter gsma = controller.getGraphStorageManager();
		return gsma.getSymbolOperators();
	}

	/**
	 * 根据图元名称获取该图元的对象
	 * 
	 * @param symbolName
	 * @return
	 */
	private NCIEquipSymbolBean getSymbolIDFromName(String symbolName) {
		GraphStorageManagerAdapter gsma = controller.getGraphStorageManager();
		return gsma.getSymbolIDFromName(symbolName);
	}
}
