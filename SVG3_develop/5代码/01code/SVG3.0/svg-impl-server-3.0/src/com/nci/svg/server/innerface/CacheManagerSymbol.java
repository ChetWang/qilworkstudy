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
 * ���⣺CacheManagerSymbol.java
 * </p>
 * <p>
 * ������ ͼԪ������������ ͼԪ����ṹΪ�Ĳ�ṹ������Map�� ���ϲ�Ϊ�ѷ����͸������� �ڶ���ΪSymbolTypeBean ������ΪͼԪ����
 * ���Ĳ�ΪͼԪ����
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2008-12-18
 * @version 1.0
 */
public class CacheManagerSymbol extends OperationServiceModuleAdapter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8518029008441608659L;
	/**
	 * ��ʼ������
	 */
	private final String INIT = "init";
	/**
	 * ά��ָ��ͼԪ
	 */
	private final String MODIFY_SYMBOL_CACHE = "modifySymbolCache";

	/**
	 * ͼԪ��������
	 */
	private final String SYMBOL_HASH = "symbolHash";
	/**
	 * �汾��������
	 */
	private final String VERSION_HASH = "versionHash";

	public CacheManagerSymbol(HashMap parameters) {
		super(parameters);
	}

	public ResultBean handleOper(String actionName, Map requestParams) {
		// �ж��Ƿ�ɹ���ȡ�����������
		if (controller == null) {
			return returnErrMsg("ͼԪ�������δ�ܻ�ȡ�����������!");
		}
		// �ж��Ƿ�ɹ���ȡ��־��������
		if (log == null) {
			return returnErrMsg("ͼԪ�������δ�ܻ�ȡ��־��������!");
		}

		String subAction = (String) getRequestParameter(requestParams,
				ActionParams.SUB_ACTION);
		log.log(this, LoggerAdapter.DEBUG, "ͼԪ��������࣬��ȡ����" + actionName
				+ "���������");

		ResultBean rb = new ResultBean();
		if (subAction.equalsIgnoreCase(INIT)) {
			// *************
			// ��ʼ��ͼԪ����
			// *************
			int result = init();
			if (result == OPER_ERROR) {
				rb.setErrorText("δ�ܳɹ�����ͼԪ�������");
				rb.setReturnFlag(ResultBean.RETURN_ERROR);
			} else {
				rb.setReturnFlag(ResultBean.RETURN_SUCCESS);
				rb.setReturnObj("�ɹ�����ͼԪ�������");
				rb.setReturnType("String");
			}
		} else if (subAction.equalsIgnoreCase(MODIFY_SYMBOL_CACHE)) {
			// **************
			// ά��ָ��ͼԪ��Ϣ
			// **************
			String symbolType = (String) getRequestParameter(requestParams,
					ActionParams.SYMBOL_TYPE); // ��ȡͼԪ����
			String symbolName = (String) getRequestParameter(requestParams,
					ActionParams.SYMBOL_NAME); // ��ȡͼԪ����
			rb = manageSymbolCache(symbolType, symbolName);
		} else {
			rb.setReturnFlag(ResultBean.RETURN_ERROR);
			rb.setErrorText(actionName + "����:������" + subAction + "��Ŀǰ������δʵ��!");
		}
		return rb;
	}

	/**
	 * ά��ָ����������Ƶ�ͼԪ������Ϣ
	 * 
	 * @param symbolType:ͼԪ����
	 * @param symbolName:ͼԪ����
	 * @return
	 */
	private ResultBean manageSymbolCache(String symbolType, String symbolName) {
		if (symbolType == null || symbolType.length() <= 0) {
			return returnErrMsg("ͼԪ����ά������ȡ��ͼԪ�������Ϊ�գ�");
		}
		if (symbolName == null || symbolName.length() <= 0) {
			return returnErrMsg("ͼԪ����ά������ȡ����ͼԪ����Ϊ�գ�");
		}
		// ��ȡ���������
		CacheManagerAdapter cma = controller.getCacheManager();
		if (cma == null) {
			return returnErrMsg("ͼԪ����ά������ȡ���������������");
		}
		// ��ȡͼԪ����
		Map cacheHash = cma.getCacheObj(CacheManagerKeys.SYMBOL_MANAGER)
				.getCacheMap();
		if (cacheHash == null) {
			return returnErrMsg("ͼԪ����ά������ȡ����ͼԪ���棡");
		}
		// ��ȡͼԪ�汾����
		Map editionHash = cma.getCacheObj(
				CacheManagerKeys.SYMBOL_VERSION_MANAGER).getCacheMap();
		if (editionHash == null) {
			return returnErrMsg("ͼԪ����ά������ȡ����ͼԪ�汾���棡");
		}
		// ��ȡָ�����Ƶ�ͼԪ����
		NCIEquipSymbolBean symbolBean = getSymbolIDFromName(symbolName);
		if (symbolBean == null) {
			return returnErrMsg("ͼԪ����ά������ȡ����ָ�����Ƶ�ͼԪ����");
		}
		// ����key
		SymbolTypeBean typeBean = new SymbolTypeBean();
		typeBean.setSymbolType(symbolBean.getType());
		typeBean.setVariety(new SimpleCodeBean(symbolBean.getVariety()
				.getCode(), symbolBean.getVariety().getName()));
		// ��ȡͼԪ�����û�
		String operator = symbolBean.getOperator();
		// ��ȡָ�������û��Ļ���
		Object tempObj = cacheHash.get(operator);
		Map tempMap;
		if (tempObj instanceof LinkedHashMap) {
			tempMap = (LinkedHashMap) tempObj;
			// **************
			// ����ͼԪ��������
			// **************
			Object subObject = tempMap.get(typeBean);
			LinkedHashMap subHashMap;
			if (subObject instanceof LinkedHashMap) {
				// �����а����˸�����ͼԪ
				subHashMap = (LinkedHashMap) subObject;
			} else {
				// �����в�����������ͼԪ
				subHashMap = new LinkedHashMap();
			}
			subHashMap.put(symbolBean.getName(), symbolBean);
			tempMap.put(typeBean, subHashMap);
			cacheHash.put(operator, tempMap);
			cma.setCacheObj(CacheObject.MAP_FLAG,
					CacheManagerKeys.SYMBOL_MANAGER, cacheHash);
			// *****************
			// ����ͼԪ�汾��������
			// *****************
			String edition = symbolBean.getModifyTime();
			String cacheEditon = (String) editionHash.get(operator);
			if (edition.compareTo(cacheEditon) < 0) {
				edition = cacheEditon;
			}
			editionHash.put(operator, edition);
			cma.setCacheObj(CacheObject.MAP_FLAG,
					CacheManagerKeys.SYMBOL_VERSION_MANAGER, editionHash);
			return returnSuccMsg("String", "�ɹ����л�����²�����");
		} else {
			return returnErrMsg("ͼԪ����ά������ȡ����ָ���û����棡");
		}

		// // ����ͼԪ��������
		// Object subObject = cacheHash.get(typeBean);
		// LinkedHashMap subHashMap;
		// if (subObject instanceof LinkedHashMap) {
		// // �����а����˸�����ͼԪ
		// subHashMap = (LinkedHashMap) subObject;
		// } else {
		// // �����в�����������ͼԪ
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
		// ��������Ϣ���뻺��
		// ****************
		CacheManagerAdapter cma = controller.getCacheManager();
		if (cma == null) {
			return OPER_ERROR;
		}

		Map map = getSymbols(null);
		// ���ͼԪ��������
		cma.addCacheInnerData(CacheObject.MAP_FLAG,
				CacheManagerKeys.SYMBOL_MANAGER, map.get(SYMBOL_HASH));
		// ���ͼԪ�汾��������
		cma.addCacheInnerData(CacheObject.MAP_FLAG,
				CacheManagerKeys.SYMBOL_VERSION_MANAGER, map.get(VERSION_HASH));
		// cma.getCacheObj(CacheManagerKeys.SYMBOL_MANAGER)
		return OPER_SUCCESS;
	}

	/**
	 * �������ݿ��л�ȡ����ͼԪ��Ϣ�б�ת���ɻ���ṹ������ȡ���Եİ汾����
	 * 
	 * @param symbolType:String:ͼԪ����(ͼԪgraphunit��ģ��template)
	 * @return ͼԪ����
	 */
	protected Map getSymbols(String symbolType) {
		// ͼԪ�����ͼԪ�汾����
		HashMap hash = new HashMap();
		// ͼԪ����
		Map symbolHash = new LinkedHashMap();
		// �汾����
		HashMap editionHash = new HashMap();
		// ��ȡͼԪ���ģ�������û���Ϣ
		ArrayList operatorList = getSymbolOperators();

		// ��ȡͼԪ��ϸ��Ϣ�б��б�ͼԪ�����ͼԪ��������
		ArrayList symbolList = getSymbolList(symbolType);
		// ��Ӹ��û�δ����ͼԪ����
		for (int i = 0, size = operatorList.size(); i < size; i++) {
			String operatorName = (String) operatorList.get(i);
			ArrayList tempList = filtrateSymbolsList(operatorName, symbolList);
			symbolHash.put(operatorName, getSymbolMap(tempList));
			editionHash.put(operatorName, getSymbolEdition(tempList));
		}

		// ��ȡ����ͼԪ��Ϣ�б�
		ArrayList tempList = getReleasedSymbolsList(symbolList);
		// ����ѷ���ͼԪ����
		symbolHash.put(OwnerVersionBean.OWNER_RELEASED, getSymbolMap(tempList));
		// ����ѷ���ͼԪ�汾
		editionHash.put(OwnerVersionBean.OWNER_RELEASED,
				getSymbolEdition(tempList));

		hash.put(SYMBOL_HASH, symbolHash);
		hash.put(VERSION_HASH, editionHash);
		return hash;
	}

	/**
	 * ���������ͼԪ��Ϣ�б���֯�ɻ���ṹ
	 * 
	 * @param symbolList:ArrayList:ͼԪ��Ϣ�б�
	 * @return
	 */
	private Map getSymbolMap(ArrayList symbolList) {
		// ��ȡ����ͼԪ�����С����Ϣ�Ļ���ṹ
		Map symbolHash = getSymbolTypeMap();

		// ��ȡͼԪ��Ϣ�б���
		int size = symbolList.size();
		// �ж�ͼԪ�Ƿ񱻷���
		boolean[] symbolFlag = new boolean[size];
		for (int i = 0; i < size; i++) {
			symbolFlag[i] = false;
		}
		// ���ʻ����������ȡ�������Ϣ
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
			// variety��Ӧ������Ҳ��Ҫ���ϣ������ǿ��ַ���

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
	 * ��ȡͼ����Ϣ�б�汾��Ϣ������ͼԪ����޸�ʱ�䣩
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
	 * ��ȡͼԪ�б��б�������ͼԪ
	 * 
	 * @param symbolList:ArrayList:ͼԪ��Ϣ�б�
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
	 * ��ͼԪ�б���ָ�������˹��˳���������ͼԪ���ݲ������ѷ�����ͼԪ
	 * 
	 * @param operator:String:������
	 * @param symbolList:ArrayList:ͼԪ��Ϣ�б�
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
	 * ��ȡ����ͼԪ�����С����Ϣ�Ļ���ṹ
	 * 
	 * @return
	 */
	private Map getSymbolTypeMap() {
		Map tempHash = new LinkedHashMap();
		// ��ȡͼԪ�����С����Ϣ
		ArrayList symbolCodes = getSymbolCodes();
		// ��ͼԪ�����С������䷵������
		for (int i = 0, size = symbolCodes.size(); i < size; i++) {
			SymbolTypeBean typeBean = (SymbolTypeBean) symbolCodes.get(i);
			tempHash.put(typeBean, new LinkedHashMap());
		}
		return tempHash;
	}

	/**
	 * ��ȡͼԪ�����С����Ϣ
	 * 
	 * @return
	 */
	private ArrayList getSymbolCodes() {
		CacheManagerAdapter cma = controller.getCacheManager();
		if (cma == null) {
			// �ж��Ƿ��û��������
			return null;
		}
		// ��ȡ���뻺��
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
	 * ��ȡͼԪ�б�
	 * 
	 * @param symbolType:ͼԪ����(ͼԪgraphunit��ģ��template)
	 * @param operator:�����ˣ����������Ϊ����ȡ�������˹���
	 * @return
	 */
	private ArrayList getSymbolList(String symbolType) {
		GraphStorageManagerAdapter gsma = controller.getGraphStorageManager();
		return gsma.getSymbolList(symbolType, null, null);
	}

	/**
	 * ��ȡͼԪ���ģ�������û���Ϣ
	 * 
	 * @return
	 */
	private ArrayList getSymbolOperators() {
		GraphStorageManagerAdapter gsma = controller.getGraphStorageManager();
		return gsma.getSymbolOperators();
	}

	/**
	 * ����ͼԪ���ƻ�ȡ��ͼԪ�Ķ���
	 * 
	 * @param symbolName
	 * @return
	 */
	private NCIEquipSymbolBean getSymbolIDFromName(String symbolName) {
		GraphStorageManagerAdapter gsma = controller.getGraphStorageManager();
		return gsma.getSymbolIDFromName(symbolName);
	}
}
