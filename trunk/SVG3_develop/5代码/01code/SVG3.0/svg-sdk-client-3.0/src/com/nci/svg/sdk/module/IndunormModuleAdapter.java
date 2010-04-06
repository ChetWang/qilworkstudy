/**
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @ʱ�䣺2008-12-26
 * @���ܣ�TODO
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
	 * ��ȡͼ���͹淶����
	 * @param graphType��ͼҵ������
	 * @return��ͼ���͹淶�б�
	 */
	public abstract ArrayList<RelaIndunormGraphBean> getIndunormGraphRela(
			String graphType);

	/**
	 * add by yux,2009-1-2
	 * ��ȡͼ������������
	 * @param graphType��ͼҵ������
	 * @return��ͼ�淶�����б�
	 */
	public abstract ArrayList<RelaIndunormDescBean> getIndunormDescRela(
			String graphType);

	/**
	 * add by yux,2009-1-2
	 * ͼԪ����ͼ�Ĺ淶
	 * @param graphType��ͼҵ������
	 * @param symbolType��ͼԪ���ͣ�ģ���ͼԪ
	 * @param symbolID��ͼԪ���
	 * @return��ͼԪ����ģ���б�
	 */
	public abstract ArrayList<RelaIndunormSymbolBean> getIndunormSymbolRela(
			String graphType, String symbolType, String symbolID);
	
	/**
	 * add by yux,2009-1-12
	 * ���ݵ�ǰ��ҵ��ϵͳ��Ż�ȡ�������ڱ�ҵ��ϵͳ��ҵ��淶�ֶκ��������б��嵥
	 * @return:�б��嵥
	 */
	public abstract HashMap<String,SimpleIndunormBean> getIndunormList();
}
