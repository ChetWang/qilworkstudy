package com.nci.domino.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.nci.domino.WfEditor;
import com.nci.domino.beans.WofoActions;
import com.nci.domino.beans.WofoCodeBean;
import com.nci.domino.beans.WofoNetBean;
import com.nci.domino.concurrent.WfRunnable;
import com.nci.domino.concurrent.WfStartupEndException;
import com.nci.domino.help.Functions;

/**
 * �����������ݻ�����������������˲���ô�䶯�����ݣ���������š���Ա��Ϣ�ȡ� ���������׶ξ�ȫ����ȡ��������Ϣ
 * 
 * @author Qil.Wong
 * 
 */
public class WfCacheManager {

	private Map<Object, Object> cache = new HashMap<Object, Object>();

	WfEditor editor;
	/**
	 * ϵͳ����
	 */
	public static final String SYSTEM_ARGUMENTS = "SystemArgs";

	public WfCacheManager(WfEditor editor) {
		this.editor = editor;
		WfRunnable run = new WfRunnable("���ڻ�ȡ��������") {

			public void run() {
				init();
			}
		};
		try {
			editor.getBackgroundManager().enqueueStartupQueue(run);
		} catch (WfStartupEndException e) {
			e.printStackTrace();
		}
	}

	private void init() {
		initDepartmentNodeOnly();
//		initEvents();
	}

	/**
	 * @���� ��ʼ���¼��������
	 * 
	 * @Add by ZHM 2010-3-29
	 */
	@SuppressWarnings("unchecked")
	private void initEvents() {
		// FIXME ������Ϣ�������͡���Ϣ���ͷ�ʽ���¼����ͺ�̨����
//		// ***********
//		// ��ȡ�¼�����
//		// ***********
//		WofoNetBean param = new WofoNetBean();
//		param.setActionName(WofoActions.GET_EVENT_CATEGORY);
//		// ���ݹ�ȥ�Ĳ���
//		param.setParam(null);
//		// �û�
//		param.setUser(editor.getUserID());
//		WofoNetBean result = Functions.getReturnNetBean(
//				editor.getServletPath(), param);
//		ArrayList<WofoEventCategoryBean> categoryBeans = (ArrayList<WofoEventCategoryBean>) result
//				.getParam();
//		cache(WofoActions.GET_EVENT_CATEGORY, categoryBeans);
		// **************
		// ��ȡ�¼���������
		// **************
		WofoNetBean param = new WofoNetBean();
		param.setActionName(WofoActions.GET_LISTENER_TYPE);
		WofoNetBean result = Functions.getReturnNetBean(editor.getServletPath(), param);
		// Map<String, String> listenerTypeMap = (Map<String, String>) result;
		// cache(WofoActions.GET_LISTENER_TYPE, listenerTypeMap);
		ArrayList<WofoCodeBean> listenerBeans = (ArrayList<WofoCodeBean>) result
				.getParam();
		cache(WofoActions.GET_LISTENER_TYPE, listenerBeans);
	}

	/**
	 * ��ʼ����������ֻ��������Ϣ���𣬲���������������顢�칫�ҵ��¼���֯
	 */
	private void initDepartmentNodeOnly() {
		WofoNetBean param = new WofoNetBean(WofoActions.GET_DEPT_TREE, editor
				.getUserID(), "");
		final WofoNetBean o = Functions.getReturnNetBean(editor
				.getServletPath(), param);
		if (o != null)
			cache.put(WofoActions.GET_DEPT_TREE, o.getParam());
	}

	/**
	 * ��ȡ����ֵ�������null����ȴ�
	 * 
	 * @param key
	 * @return
	 */
	public Object waitWhileNullGet(String key) {
		int count = 0;
		while (cache.get(key) == null && count < 20) {
			try {
				count++;
				System.out.println("cacheδȡ��" + key + "��Ӧ���ݣ��ȴ�...");
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return cache.get(key);
	}

	/**
	 * ��ȡ����ֵ�������null���򷵻�null
	 * 
	 * @param key
	 * @return
	 */
	public Object nowaitWhileNullGet(String key) {
		return cache.get(key);
	}

	public void cache(Object key, Object value) {
		cache.put(key, value);
	}

}
