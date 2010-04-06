package com.nci.svg.server.innerface;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.nci.svg.sdk.bean.CodeInfoBean;
import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.bean.SimpleCodeBean;
import com.nci.svg.sdk.communication.ActionNames;
import com.nci.svg.sdk.communication.ActionParams;
import com.nci.svg.sdk.graphunit.NCIEquipSymbolBean;
import com.nci.svg.sdk.graphunit.OwnerVersionBean;
import com.nci.svg.sdk.graphunit.SymbolVersionParser;
import com.nci.svg.sdk.logger.LoggerAdapter;
import com.nci.svg.sdk.server.cache.CacheManagerAdapter;
import com.nci.svg.sdk.server.cache.CacheObject;
import com.nci.svg.sdk.server.graphstorage.GraphStorageManagerAdapter;
import com.nci.svg.sdk.server.service.OperationServiceModuleAdapter;
import com.nci.svg.server.cache.CacheManagerKeys;

/**
 * <p>
 * 标题：SymbolManage.java
 * </p>
 * <p>
 * 描述：图元管理类
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2008-12-01
 * @version 1.0
 */
public class SymbolManage extends OperationServiceModuleAdapter {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7202222576396217882L;

	public SymbolManage(HashMap parameters) {
		super(parameters);
	}

	public ResultBean handleOper(String actionName, Map requestParams) {
		ResultBean rb = new ResultBean();
		// 判断是否成功获取管理组件对象
		if (controller == null) {
			return returnErrMsg("图元管理，未能获取管理组件对象!");
		}
		// 判断是否成功获取日志操作对象
		if (log == null) {
			return returnErrMsg("图元管理，未能获取日志操作对象!");
		}

		log
				.log(this, LoggerAdapter.DEBUG, "图元管理类，获取到‘" + actionName
						+ "’请求命令！");

		if (actionName.equalsIgnoreCase(ActionNames.SAVE_SYMBOL)) {
			// **********
			// 保存图元
			// **********
			String symbolID = (String) getRequestParameter(requestParams,
					ActionParams.SYMBOL_ID);
			String symbolType = (String) getRequestParameter(requestParams,
					ActionParams.SYMBOL_TYPE); // 获取图元大类
			String operator = (String) getRequestParameter(requestParams,
					ActionParams.OPERATOR); // 获取图元操作人员
			String name = (String) getRequestParameter(requestParams,
					ActionParams.NAME); // 获取图元名称
			String content = (String) getRequestParameter(requestParams,
					ActionParams.CONTENT); // 获取图元内容
			String variety = (String) getRequestParameter(requestParams,
					ActionParams.VARIETY); // 获取图元类型（小类）
			String param1 = (String) getRequestParameter(requestParams,
					ActionParams.PARAM + "1"); // 获取param1参数
			String param2 = (String) getRequestParameter(requestParams,
					ActionParams.PARAM + "2"); // 获取param2参数
			String param3 = (String) getRequestParameter(requestParams,
					ActionParams.PARAM + "3"); // 获取param3参数
			String modelID = (String) getRequestParameter(requestParams,
					ActionParams.MODEL_ID);
			String businessID = (String) getRequestParameter(requestParams,
					ActionParams.BUSINESS_ID); // 获取业务系统编号

			NCIEquipSymbolBean obj = new NCIEquipSymbolBean();
			obj.setId(symbolID);
			obj.setContent(content);
			obj.setName(name);
			obj.setOperator(operator);
			obj.setType(symbolType);
			obj.setVariety(new SimpleCodeBean(variety, ""));
			obj.setParam1(param1);
			obj.setParam2(param2);
			obj.setParam3(param3);
			obj.setModelID(modelID);

			rb = saveSymbol(businessID, symbolType, content, obj);
			if (rb != null && rb.getReturnFlag() == ResultBean.RETURN_SUCCESS) {
				// 通知图元缓存更新操作
				HashMap params = new HashMap();
				params.put(ActionParams.SYMBOL_TYPE,
						changeStringToArray(symbolType));
				params.put(ActionParams.SYMBOL_NAME, changeStringToArray(name));
				params.put(ActionParams.SUB_ACTION,
						changeStringToArray("modifySymbolCache"));
				refersTo(CacheManagerKeys.SYMBOL_MANAGER.toUpperCase(), params);
			}
		} else if (actionName.equalsIgnoreCase(ActionNames.GET_SYMBOL)) {
			// ***********
			// 获取图元内容
			// ***********
			String name = (String) getRequestParameter(requestParams,
					ActionParams.NAME); // 获取图元名称
			rb = loadSymbol(name);
		} else if (actionName
				.equalsIgnoreCase(ActionNames.GET_SYMBOL_TYPE_LIST)) {
			// ***********
			// 获取图元类型
			// ***********
			String symbolType = (String) getRequestParameter(requestParams,
					ActionParams.SYMBOL_TYPE);

			rb = getSymbolTypeList(symbolType);
		} else if (actionName.equalsIgnoreCase(ActionNames.GET_SYMBOLS_LIST)) {
			// ***********
			// 获取图元列表
			// ***********
			String symbolType = (String) getRequestParameter(requestParams,
					ActionParams.SYMBOL_TYPE);
			String operator = (String) getRequestParameter(requestParams,
					ActionParams.OPERATOR);
			String owner = (String) getRequestParameter(requestParams,
					ActionParams.OWNER);

			rb = getSymbols(symbolType, operator, owner);
		} else if (actionName.equalsIgnoreCase(ActionNames.GET_SYMBOLS_VERSION)) {
			// *********************
			// 获取服务器端图元版本信息
			// *********************
			String operator = (String) getRequestParameter(requestParams,
					ActionParams.OPERATOR);
			String owners = (String) getRequestParameter(requestParams,
					ActionParams.OWNERS);

			rb = checkGraphUnitEdition(operator, owners);
		} else if (actionName
				.equalsIgnoreCase(ActionNames.CHECK_SYMBOL_TEMPLATE_RELATION)) {
			// FIXME 检查symbol是否被模板已经引用，并返回模板名称列表
		} else if (actionName
				.equalsIgnoreCase(ActionNames.CHECK_SYMBOL_NAME_REPEAT)) {
			String newName = (String) getRequestParameter(requestParams,
					ActionParams.SYMBOL_NAME);
			rb = checkSymbolNameRepeat(newName);
		} else if (actionName.equalsIgnoreCase(ActionNames.RENAME_SYMBOL)) {
			String oldName = (String) getRequestParameter(requestParams,
					ActionParams.SYMBOL_OLD_NAME);
			String newName = (String) getRequestParameter(requestParams,
					ActionParams.SYMBOL_NEW_NAME);
			String owner = (String) getRequestParameter(requestParams,
					ActionParams.OWNER);
			String operator = (String)getRequestParameter(requestParams,
					ActionParams.OPERATOR);
			String symbolType = (String)getRequestParameter(requestParams,ActionParams.SYMBOL_TYPE);
			rb = renameSymbol(owner, oldName, newName,symbolType,operator);
		} else {
			rb = returnErrMsg(actionName + "命令，目前该请求未实现!");
		}
		return rb;
	}

	/**
	 * 检查名称有效性
	 * 
	 * @param rb
	 * @param requestParams
	 */
	private ResultBean checkSymbolNameRepeat(String newName) {
		ResultBean rb = new ResultBean();
		GraphStorageManagerAdapter gsma = controller.getGraphStorageManager();
		String result = gsma.getSameSymbol(newName);
		if (result != null) {
			rb.setReturnFlag(ResultBean.RETURN_ERROR);
		} else {
			rb.setReturnFlag(ResultBean.RETURN_SUCCESS);
		}
		return rb;
	}

	/**
	 * 重命名symbol，修改缓存、数据库
	 * 
	 * @param oldName:String:所有者
	 * @param newName:String:原名
	 * @param symbolType:String:新名称
	 * @return
	 */
	private ResultBean renameSymbol(String owner, String oldName, String newName, String symbolType,String operator) {
		ResultBean bean = new ResultBean();
		GraphStorageManagerAdapter gsma = controller.getGraphStorageManager();
		String sysDate = gsma.renameSymbol(oldName, newName,symbolType,operator);
		if (sysDate==null || sysDate.equals("")) { // 数据库重命名失败
			bean.setErrorText("数据库重命名失败");
			bean.setReturnFlag(ResultBean.RETURN_ERROR);
		} else {
			CacheManagerAdapter cma = controller.getCacheManager();
			// 获取Symbol缓存
			CacheObject co = cma
					.getCacheObj(CacheManagerKeys.SYMBOL_MANAGER);
			Map map = (Map) co.getCacheMap().get(owner);
			Iterator it = map.values().iterator();
			while (it.hasNext()) {
				Map temp = (Map) it.next();
				if (temp.containsKey(oldName)) {
					NCIEquipSymbolBean o = (NCIEquipSymbolBean)temp.get(oldName);
					temp.remove(oldName);
					o.setName(newName);
					o.setModifyTime(sysDate);
					o.setOperator(operator);
					temp.put(newName, o);
					break;
				}
			}
			//更改symbol版本缓存
			co = cma.getCacheObj(CacheManagerKeys.SYMBOL_VERSION_MANAGER);

			((Map)co.getCacheMap()).put(owner, sysDate);

			bean.setReturnFlag(ResultBean.RETURN_SUCCESS);
		}
		return bean;
	}

	/**
	 * 获取指定名称的图元或模板信息
	 * 
	 * @param name:图元或模板名称
	 * @return:图元内容
	 */
	private ResultBean loadSymbol(String name) {
		GraphStorageManagerAdapter gsma = controller.getGraphStorageManager();
		return gsma.loadSymbol(name);
	}

	/**
	 * 保存图元到数据库中
	 * 
	 * @param businessID:String:业务系统编号
	 * @param graphUnitType:String:图元大类(图元graphunit、模板template)
	 * @param content:String:图元内容
	 * @param obj:String:图元基本信息
	 * @return
	 */
	private ResultBean saveSymbol(String businessID, String symbolType,
			String content, NCIEquipSymbolBean obj) {
		GraphStorageManagerAdapter gsma = controller.getGraphStorageManager();
		return gsma.saveSymbol(businessID, symbolType, content, obj);
	}

	/**
	 * 获取图元版本信息
	 * 
	 * @param operator:String:操作人
	 * @param owners:String:图元所有者信息
	 * @return
	 */
	private ResultBean checkGraphUnitEdition(String operator, String owners) {
		CacheManagerAdapter cma = controller.getCacheManager();
		if (cma == null) {
			return returnErrMsg("获取图元版本信息，无法获取缓存管理器！");
		}
		// 获取图元版本缓存
		CacheObject co = cma
				.getCacheObj(CacheManagerKeys.SYMBOL_VERSION_MANAGER);
		if (co == null)
			return returnErrMsg("获取图元版本信息，获取图元版本缓存失败！");
		Map editionMap = co.getCacheMap();
		// 获取请求的图元所以者信息
		OwnerVersionBean[] ovbeans = SymbolVersionParser
				.parseOwnerVersion(owners);
		for (int i = 0, size = ovbeans.length; i < size; i++) {
			String owner = ovbeans[i].getOwner();
			Object verObj = editionMap.get(owner);
			if (verObj instanceof String) {
				String version = (String) verObj;
				ovbeans[i].setVersion(version);
			}
		}
		String serverSymbolVersions = SymbolVersionParser
				.createSymbolVersion(ovbeans);
		return returnSuccMsg("String", serverSymbolVersions);
	}

	/**
	 * 获取图元列表信息
	 * 
	 * @param symbolType:String:图元大类(图元graphunit、模板template)
	 * @param operator:String:操作人
	 * @param owner:String:图元所有者名称
	 * @return
	 */
	private ResultBean getSymbols(String symbolType, String operator,
			String owner) {
		CacheManagerAdapter cma = controller.getCacheManager();
		if (cma == null) {
			return returnErrMsg("获取图元列表信息，无法获取缓存管理器！");
		}

		// 获取图元缓存
		CacheObject co = cma.getCacheObj(CacheManagerKeys.SYMBOL_MANAGER);
		if (co == null)
			return returnErrMsg("获取图元列表信息，获取图元缓存失败！");
		Map symbolMap = co.getCacheMap();
		return returnSuccMsg("LinkedHashMap", symbolMap.get(owner));

	}

	/**
	 * 获取图元类型
	 * 
	 * @param graphUnitType:图元大类(图元graphunit、模板template)
	 * @return 图元类型信息列表
	 */
	private ResultBean getSymbolTypeList(String symbolType) {
		StringBuffer sql = new StringBuffer();
		if (symbolType == null || symbolType.length() == 0) {
			sql.append("SELECT * FROM t_svg_code_commsets ").append(
					"WHERE cc_shortname='SVG_TEMPLATE_VARIETY'").append(
					" AND cc_shortname='SVG_SYMBOL_VARIETY'");
		} else if (symbolType
				.equalsIgnoreCase(NCIEquipSymbolBean.SYMBOL_TYPE_GRAPHUNIT)) {
			sql.append("SELECT * FROM t_svg_code_commsets ").append(
					"WHERE cc_shortname='SVG_SYMBOL_VARIETY'");
		} else if (symbolType
				.equalsIgnoreCase(NCIEquipSymbolBean.SYMBOL_TYPE_TEMPLATE)) {
			sql.append("SELECT * FROM t_svg_code_commsets ").append(
					"WHERE cc_shortname='SVG_TEMPLATE_VARIETY'");
		} else {
			return returnErrMsg("获取图元类型，输入图元大类有误!");
		}

		// 获取数据库连接
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "无法获得有效数据库连接,无法获得图元类型清单！");
			return returnErrMsg("获取图元类型，无法获得有效数据库连接！");
		}
		ArrayList list = new ArrayList();
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "获取图元类型：" + sql.toString());
			ResultSet rs = st.executeQuery(sql.toString());
			CodeInfoBean bean;
			while (rs.next()) {
				bean = new CodeInfoBean();
				bean.setShortName(rs.getString("cc_shortname"));
				bean.setValue(rs.getString("cc_code"));
				bean.setName(rs.getString("cc_name"));
				list.add(bean);
			}
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			log.log(this, LoggerAdapter.ERROR, e);
			return returnErrMsg("获取图元类型，数据库操作失败！");
		} finally {
			controller.getDBManager().close(conn);
		}
		list.trimToSize();

		if (list.size() > 0)
			return returnSuccMsg("ArrayList.CodeInfoBean", list);
		else
			return returnErrMsg("获取图元类型，获取数据为空！");
	}
}
