
package com.nci.svg.sdk.server.graphstorage;

import java.util.ArrayList;
import java.util.HashMap;

import com.nci.svg.sdk.bean.GraphFileBean;
import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.graphunit.NCIEquipSymbolBean;
import com.nci.svg.sdk.server.ModuleDefines;
import com.nci.svg.sdk.server.module.ServerModuleAdapter;

/**
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author YUX
 * @ʱ�䣺2008-11-21
 * @���ܣ���������ͼ��������������
 *
 */
public abstract class GraphStorageManagerAdapter extends ServerModuleAdapter {
	public static final String graphUnitType = NCIEquipSymbolBean.SYMBOL_TYPE_GRAPHUNIT;
	public static final String templateType = NCIEquipSymbolBean.SYMBOL_TYPE_TEMPLATE;

	public GraphStorageManagerAdapter(HashMap parameters) {
		super(parameters);
	}

	/**
	 * ��ͼ(��ͼԪͼ��)
	 * 
	 * @param strBussID:ҵ��ϵͳ���
	 * @param logs:ͼ��־����
	 * @param obj:�ļ�����
	 * @return:��ͼ���,�ɹ�����0,ʧ�ܷ���<0
	 */
	public abstract ResultBean saveGraph(String strBussID, String logs,
			GraphFileBean obj);

	/**
	 * ȡͼ(��ͼԪ)
	 * 
	 * @param strBussID:ҵ��ϵͳ���
	 * @param strGraphType:ͼ����
	 * @param strFilter:ͼ�ļ���ʽ
	 * @param obj:�ļ�����
	 * @return:ͼ����
	 */
	public abstract ResultBean loadGraph(String strBussID, GraphFileBean obj);

	/**
	 * ��ͼԪ
	 * 
	 * @param businessID:String:ҵ��ϵͳ���
	 * @param strSymbolType:ͼԪ����
	 * @param content:ͼԪ����
	 * @param obj:ͼԪ�ļ�����
	 * @return:������
	 */
	public abstract ResultBean saveSymbol(String businessID,
			String strSymbolType, String content, NCIEquipSymbolBean obj);

	/**
	 * ȡͼԪ
	 * 
	 * @param strSymbolType:ͼԪ����
	 * @param obj:ͼԪ�ļ�����
	 * @return:ͼԪ����
	 */
	public abstract ResultBean loadSymbol(String name);

	/**
	 * ����ͼԪ���ͻ�ȡͼԪ�嵥
	 * 
	 * @param strSymbolType��ͼԪ���࣬ͼԪ/ģ��
	 * @param releaseFlag:�������
	 * @param person���޸���
	 * @return��ͼԪ�嵥
	 */
	public abstract ArrayList getSymbolList(String strSymbolType,
			String releaseFlag, String person);

	/**
	 * ��ȡͼԪ���ģ����в����û���Ϣ
	 * 
	 * @return �����û���Ϣ�б�
	 */
	public abstract ArrayList getSymbolOperators();

	/**
	 * ����ͼԪ���ƻ�ȡ��ͼԪ�Ķ���
	 * 
	 * @param symbolName:ͼԪ����
	 * @return:ָ�����Ƶ�ͼԪ����
	 */
	public abstract NCIEquipSymbolBean getSymbolIDFromName(String symbolName);

	/**
	 * ��ȡָ��ҵ��ϵͳ֧�ֵ�ͼ���ļ�����
	 * 
	 * @param bussID:String:ҵ��ϵͳ���
	 * @return:ָ��ϵͳ֧�ֵ�ͼ���ļ�����
	 */
	public abstract ResultBean getSupportFileType(String bussID);

	/**
	 * ��ȡָ��ҵ��ϵͳ�ļ��б�
	 * 
	 * @param businessID:String:ϵͳ���
	 * @param graphBusinessType:String:ҵ������
	 * @return ����ָ��ϵͳ��ָ��ҵ�����͵�ͼ�ļ��б����ҵ������Ϊ���򷵻ظ�ϵͳ�������ļ��嵥
	 */
	public abstract ResultBean getFilesInformation(String businessID,
			GraphFileBean fileBean);

	/**
	 * ���ݴ����ҵ��ϵͳ��ź����ݿ����ӻ�ȡ��Ӧ��ͼ���ʶ
	 * 
	 * @param strBussID��ҵ��ϵͳ���
	 * @return��ͼ���ʶ���������򷵻�null
	 */
	public abstract String getRelateString(String strBussID);

	/**
	 * ��ȡ���ݿ��е�ͼԪ���ģ�ͱ�����ָ�����Ƶ�ͼԪ��ģ��
	 * 
	 * @param symbolName
	 * @return
	 */
	public abstract String getSameSymbol(String symbolName);
	
	/**
	 * ������symbol���޸Ļ��桢���ݿ�
	 * 
	 * @param oldName:String:������
	 * @param newName:String:ԭ��
	 * @param symbolType:String:������
	 * @return
	 */
	public abstract String renameSymbol(String oldName, String newName,String symbolType,String operator);

	public String getModuleType() {
		return ModuleDefines.GRAPH_STORAGE_MANAGER;
	}

}
