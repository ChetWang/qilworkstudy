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
 * 服务器端数据缓存管理器，管理服务端不怎么变动的数据，如机构部门、人员信息等。 程序启动阶段就全部获取到此类信息
 * 
 * @author Qil.Wong
 * 
 */
public class WfCacheManager {

	private Map<Object, Object> cache = new HashMap<Object, Object>();

	WfEditor editor;
	/**
	 * 系统参数
	 */
	public static final String SYSTEM_ARGUMENTS = "SystemArgs";

	public WfCacheManager(WfEditor editor) {
		this.editor = editor;
		WfRunnable run = new WfRunnable("正在获取基础数据") {

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
	 * @功能 初始化事件相关数据
	 * 
	 * @Add by ZHM 2010-3-29
	 */
	@SuppressWarnings("unchecked")
	private void initEvents() {
		// FIXME 增加消息场景类型、消息发送方式、事件类型后台定义
//		// ***********
//		// 获取事件分类
//		// ***********
//		WofoNetBean param = new WofoNetBean();
//		param.setActionName(WofoActions.GET_EVENT_CATEGORY);
//		// 传递过去的参数
//		param.setParam(null);
//		// 用户
//		param.setUser(editor.getUserID());
//		WofoNetBean result = Functions.getReturnNetBean(
//				editor.getServletPath(), param);
//		ArrayList<WofoEventCategoryBean> categoryBeans = (ArrayList<WofoEventCategoryBean>) result
//				.getParam();
//		cache(WofoActions.GET_EVENT_CATEGORY, categoryBeans);
		// **************
		// 获取事件监听类型
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
	 * 初始化部门树，只到部门信息级别，不包含部门下面的组、办公室等下级组织
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
	 * 获取缓存值，如果是null，则等待
	 * 
	 * @param key
	 * @return
	 */
	public Object waitWhileNullGet(String key) {
		int count = 0;
		while (cache.get(key) == null && count < 20) {
			try {
				count++;
				System.out.println("cache未取到" + key + "对应数据，等待...");
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return cache.get(key);
	}

	/**
	 * 获取缓存值，如果是null，则返回null
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
