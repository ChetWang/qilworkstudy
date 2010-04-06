/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2008-12-26
 * @功能：TODO
 *
 */
package com.nci.svg.sdk.module;

import java.util.ArrayList;
import java.util.HashMap;

import com.nci.svg.sdk.bean.RelaIndunormDescBean;
import com.nci.svg.sdk.bean.RelaIndunormGraphBean;
import com.nci.svg.sdk.bean.RelaIndunormSymbolBean;
import com.nci.svg.sdk.bean.SimpleIndunormBean;
import com.nci.svg.sdk.client.EditorAdapter;
import com.nci.svg.sdk.client.function.ModuleAdapter;

/**
 * @author yx.nci
 * 
 */
public abstract class IndunormModuleAdapter extends ModuleAdapter {

	public final static String MODULE_ID = "b0a026b6-dadd-4283-827a-8ad2ace421d2";
	public final static String DATA_ID = "INDUNORM_LIST";

	public IndunormModuleAdapter(EditorAdapter editor) {
		super(editor);
		moduleUUID = MODULE_ID;
	}

	/**
	 * add by yux,2009-1-2
	 * 获取图类型规范关联
	 * @param graphType：图业务类型
	 * @return：图类型规范列表
	 */
	public abstract ArrayList<RelaIndunormGraphBean> getIndunormGraphRela(
			String graphType);

	/**
	 * add by yux,2009-1-2
	 * 获取图类型描述关联
	 * @param graphType：图业务类型
	 * @return：图规范描述列表
	 */
	public abstract ArrayList<RelaIndunormDescBean> getIndunormDescRela(
			String graphType);

	/**
	 * add by yux,2009-1-2
	 * 图元符合图的规范
	 * @param graphType：图业务类型
	 * @param symbolType：图元类型，模板或图元
	 * @param symbolID：图元编号
	 * @return：图元关联模型列表
	 */
	public abstract ArrayList<RelaIndunormSymbolBean> getIndunormSymbolRela(
			String graphType, String symbolType, String symbolID);
	
	/**
	 * add by yux,2009-1-12
	 * 根据当前的业务系统编号获取所有属于本业务系统的业务规范字段和描述的列表清单
	 * @return:列表清单
	 */
	public abstract HashMap<String,SimpleIndunormBean> getIndunormList();
}
