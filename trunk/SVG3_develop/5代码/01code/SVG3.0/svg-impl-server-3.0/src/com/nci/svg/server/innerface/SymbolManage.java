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
 * ���⣺SymbolManage.java
 * </p>
 * <p>
 * ������ͼԪ������
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2008-12-01
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
		// �ж��Ƿ�ɹ���ȡ�����������
		if (controller == null) {
			return returnErrMsg("ͼԪ����δ�ܻ�ȡ�����������!");
		}
		// �ж��Ƿ�ɹ���ȡ��־��������
		if (log == null) {
			return returnErrMsg("ͼԪ����δ�ܻ�ȡ��־��������!");
		}

		log
				.log(this, LoggerAdapter.DEBUG, "ͼԪ�����࣬��ȡ����" + actionName
						+ "���������");

		if (actionName.equalsIgnoreCase(ActionNames.SAVE_SYMBOL)) {
			// **********
			// ����ͼԪ
			// **********
			String symbolID = (String) getRequestParameter(requestParams,
					ActionParams.SYMBOL_ID);
			String symbolType = (String) getRequestParameter(requestParams,
					ActionParams.SYMBOL_TYPE); // ��ȡͼԪ����
			String operator = (String) getRequestParameter(requestParams,
					ActionParams.OPERATOR); // ��ȡͼԪ������Ա
			String name = (String) getRequestParameter(requestParams,
					ActionParams.NAME); // ��ȡͼԪ����
			String content = (String) getRequestParameter(requestParams,
					ActionParams.CONTENT); // ��ȡͼԪ����
			String variety = (String) getRequestParameter(requestParams,
					ActionParams.VARIETY); // ��ȡͼԪ���ͣ�С�ࣩ
			String param1 = (String) getRequestParameter(requestParams,
					ActionParams.PARAM + "1"); // ��ȡparam1����
			String param2 = (String) getRequestParameter(requestParams,
					ActionParams.PARAM + "2"); // ��ȡparam2����
			String param3 = (String) getRequestParameter(requestParams,
					ActionParams.PARAM + "3"); // ��ȡparam3����
			String modelID = (String) getRequestParameter(requestParams,
					ActionParams.MODEL_ID);
			String businessID = (String) getRequestParameter(requestParams,
					ActionParams.BUSINESS_ID); // ��ȡҵ��ϵͳ���

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
				// ֪ͨͼԪ������²���
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
			// ��ȡͼԪ����
			// ***********
			String name = (String) getRequestParameter(requestParams,
					ActionParams.NAME); // ��ȡͼԪ����
			rb = loadSymbol(name);
		} else if (actionName
				.equalsIgnoreCase(ActionNames.GET_SYMBOL_TYPE_LIST)) {
			// ***********
			// ��ȡͼԪ����
			// ***********
			String symbolType = (String) getRequestParameter(requestParams,
					ActionParams.SYMBOL_TYPE);

			rb = getSymbolTypeList(symbolType);
		} else if (actionName.equalsIgnoreCase(ActionNames.GET_SYMBOLS_LIST)) {
			// ***********
			// ��ȡͼԪ�б�
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
			// ��ȡ��������ͼԪ�汾��Ϣ
			// *********************
			String operator = (String) getRequestParameter(requestParams,
					ActionParams.OPERATOR);
			String owners = (String) getRequestParameter(requestParams,
					ActionParams.OWNERS);

			rb = checkGraphUnitEdition(operator, owners);
		} else if (actionName
				.equalsIgnoreCase(ActionNames.CHECK_SYMBOL_TEMPLATE_RELATION)) {
			// FIXME ���symbol�Ƿ�ģ���Ѿ����ã�������ģ�������б�
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
			rb = returnErrMsg(actionName + "���Ŀǰ������δʵ��!");
		}
		return rb;
	}

	/**
	 * ���������Ч��
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
	 * ������symbol���޸Ļ��桢���ݿ�
	 * 
	 * @param oldName:String:������
	 * @param newName:String:ԭ��
	 * @param symbolType:String:������
	 * @return
	 */
	private ResultBean renameSymbol(String owner, String oldName, String newName, String symbolType,String operator) {
		ResultBean bean = new ResultBean();
		GraphStorageManagerAdapter gsma = controller.getGraphStorageManager();
		String sysDate = gsma.renameSymbol(oldName, newName,symbolType,operator);
		if (sysDate==null || sysDate.equals("")) { // ���ݿ�������ʧ��
			bean.setErrorText("���ݿ�������ʧ��");
			bean.setReturnFlag(ResultBean.RETURN_ERROR);
		} else {
			CacheManagerAdapter cma = controller.getCacheManager();
			// ��ȡSymbol����
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
			//����symbol�汾����
			co = cma.getCacheObj(CacheManagerKeys.SYMBOL_VERSION_MANAGER);

			((Map)co.getCacheMap()).put(owner, sysDate);

			bean.setReturnFlag(ResultBean.RETURN_SUCCESS);
		}
		return bean;
	}

	/**
	 * ��ȡָ�����Ƶ�ͼԪ��ģ����Ϣ
	 * 
	 * @param name:ͼԪ��ģ������
	 * @return:ͼԪ����
	 */
	private ResultBean loadSymbol(String name) {
		GraphStorageManagerAdapter gsma = controller.getGraphStorageManager();
		return gsma.loadSymbol(name);
	}

	/**
	 * ����ͼԪ�����ݿ���
	 * 
	 * @param businessID:String:ҵ��ϵͳ���
	 * @param graphUnitType:String:ͼԪ����(ͼԪgraphunit��ģ��template)
	 * @param content:String:ͼԪ����
	 * @param obj:String:ͼԪ������Ϣ
	 * @return
	 */
	private ResultBean saveSymbol(String businessID, String symbolType,
			String content, NCIEquipSymbolBean obj) {
		GraphStorageManagerAdapter gsma = controller.getGraphStorageManager();
		return gsma.saveSymbol(businessID, symbolType, content, obj);
	}

	/**
	 * ��ȡͼԪ�汾��Ϣ
	 * 
	 * @param operator:String:������
	 * @param owners:String:ͼԪ��������Ϣ
	 * @return
	 */
	private ResultBean checkGraphUnitEdition(String operator, String owners) {
		CacheManagerAdapter cma = controller.getCacheManager();
		if (cma == null) {
			return returnErrMsg("��ȡͼԪ�汾��Ϣ���޷���ȡ�����������");
		}
		// ��ȡͼԪ�汾����
		CacheObject co = cma
				.getCacheObj(CacheManagerKeys.SYMBOL_VERSION_MANAGER);
		if (co == null)
			return returnErrMsg("��ȡͼԪ�汾��Ϣ����ȡͼԪ�汾����ʧ�ܣ�");
		Map editionMap = co.getCacheMap();
		// ��ȡ�����ͼԪ��������Ϣ
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
	 * ��ȡͼԪ�б���Ϣ
	 * 
	 * @param symbolType:String:ͼԪ����(ͼԪgraphunit��ģ��template)
	 * @param operator:String:������
	 * @param owner:String:ͼԪ����������
	 * @return
	 */
	private ResultBean getSymbols(String symbolType, String operator,
			String owner) {
		CacheManagerAdapter cma = controller.getCacheManager();
		if (cma == null) {
			return returnErrMsg("��ȡͼԪ�б���Ϣ���޷���ȡ�����������");
		}

		// ��ȡͼԪ����
		CacheObject co = cma.getCacheObj(CacheManagerKeys.SYMBOL_MANAGER);
		if (co == null)
			return returnErrMsg("��ȡͼԪ�б���Ϣ����ȡͼԪ����ʧ�ܣ�");
		Map symbolMap = co.getCacheMap();
		return returnSuccMsg("LinkedHashMap", symbolMap.get(owner));

	}

	/**
	 * ��ȡͼԪ����
	 * 
	 * @param graphUnitType:ͼԪ����(ͼԪgraphunit��ģ��template)
	 * @return ͼԪ������Ϣ�б�
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
			return returnErrMsg("��ȡͼԪ���ͣ�����ͼԪ��������!");
		}

		// ��ȡ���ݿ�����
		Connection conn = controller.getDBManager().getConnection("svg");
		if (conn == null) {
			log.log(this, LoggerAdapter.ERROR, "�޷������Ч���ݿ�����,�޷����ͼԪ�����嵥��");
			return returnErrMsg("��ȡͼԪ���ͣ��޷������Ч���ݿ����ӣ�");
		}
		ArrayList list = new ArrayList();
		try {
			Statement st = conn.createStatement();
			log.log(this, LoggerAdapter.DEBUG, "��ȡͼԪ���ͣ�" + sql.toString());
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
			return returnErrMsg("��ȡͼԪ���ͣ����ݿ����ʧ�ܣ�");
		} finally {
			controller.getDBManager().close(conn);
		}
		list.trimToSize();

		if (list.size() > 0)
			return returnSuccMsg("ArrayList.CodeInfoBean", list);
		else
			return returnErrMsg("��ȡͼԪ���ͣ���ȡ����Ϊ�գ�");
	}
}
