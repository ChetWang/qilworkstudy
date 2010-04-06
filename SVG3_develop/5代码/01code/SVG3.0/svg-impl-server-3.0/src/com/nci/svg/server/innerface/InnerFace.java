package com.nci.svg.server.innerface;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.communication.ActionParams;
import com.nci.svg.sdk.server.service.OperationServiceModuleAdapter;
import com.nci.svg.server.cache.CacheManagerKeys;

/**
 * <p>
 * ���⣺InnerFace.java
 * </p>
 * <p>
 * ������ �ڲ��ӿڹ�����
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
public class InnerFace extends OperationServiceModuleAdapter {

	public InnerFace(HashMap parameters) {
		super(parameters);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3606879659765425672L;

	public int init() {
		// ��ʼ���������
		String[] strInitName = {"init"};
		HashMap params = new HashMap();
		params.put(ActionParams.SUB_ACTION, strInitName);

		ArrayList keyList = getCacheManagerKeys(new CacheManagerKeys());
		for (int i = 0, size = keyList.size(); i < size; i++) {
			String[] var = (String[]) keyList.get(i);
			refersTo(var[1], params);
		}
		// System.out.println(keyList);
		return OPER_SUCCESS;
	}

	public ResultBean handleOper(String actionName, Map requestParams) {
		return null;
	}

	/**
	 * ��ȡ�����������е�����
	 * 
	 * @param obj
	 *            ����������Ķ���
	 * @return
	 */
	private ArrayList getCacheManagerKeys(Object obj) {
		ArrayList list = new ArrayList();
		Field[] fields = obj.getClass().getDeclaredFields();
		String[] var;
		for (int i = 0, len = fields.length; i < len; i++) {
			var = new String[2];
			// ����ÿ�����ԣ���ȡ������
			var[0] = fields[i].getName();
			try {
				var[1] = ((String) fields[i].get(obj)).toUpperCase();
				list.add(var);
			} catch (IllegalArgumentException ex) {
				ex.printStackTrace();
			} catch (IllegalAccessException ex) {
				ex.printStackTrace();
			}
		}
		list.trimToSize();
		return list;
	}

}
