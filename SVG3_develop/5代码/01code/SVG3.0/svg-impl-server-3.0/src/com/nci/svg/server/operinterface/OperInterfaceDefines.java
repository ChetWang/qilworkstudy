/**
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @ʱ�䣺2008-12-15
 * @���ܣ�TODO
 *
 */
package com.nci.svg.server.operinterface;

/**
 * @author yx.nci
 * 
 */
public class OperInterfaceDefines {
	/**
	 * ��ȡҵ��ϵͳģ���б� String moduleID:ģ�ͱ��
	 */
	public static final int GET_MODEL_LIST = 1;
	/**
	 * ��ȡҵ��ϵͳģ���б��ⲿ�ӿ���
	 */
	public static final String GET_MODEL_LIST_NAME = "getModelListOutterFace";

	/**
	 * ��ȡҵ��ϵͳģ������ String moduleID:ģ�ͱ��
	 */
	public static final int GET_MODEL_PARAMS = 2;
	/**
	 * ��ȡҵ��ϵͳģ�������ⲿ�ӿ���
	 */
	public static final String GET_MODEL_PARAMS_NAME = "getModelParamsOutterFace";

	/**
	 * ��ȡҵ��ϵͳģ�Ͷ����б� String moduleID:ģ�ͱ��
	 */
	public static final int GET_MODEL_ACTIONLIST = 3;
	/**
	 * ��ȡҵ��ϵͳģ�Ͷ����б��ⲿ�ӿ���
	 */
	public static final String GET_MODEL_ACTIONLIST_NAME = "getModelActionsOutterFace";

	/**
	 * ��ȡҵ��ģ��״̬ String moduleID:ģ�ͱ��
	 */
	public static final int GET_MODEL_STATUS = 4;
	/**
	 * ��ȡҵ��ģ��״̬�ⲿ�ӿ���
	 */
	public static final String GET_MODEL_STATUS_NAME = "getModelStatusOutterFace";

	/**
	 * ͼԪ��ģ�͹�ϵ��ȡ String graphUnitID:ͼԪ��� String moduleID:ģ�ͱ��
	 */
	public static final int GET_GRAPH_UNIT_AND_MODULE_RELA = 5;
	/**
	 * ͼԪ��ģ�͹�ϵ��ȡ�ⲿ�ӿ���
	 */
	public static final String GET_GRAPH_UNIT_AND_MODULE_RELA_NAME = "getSymbolModuleRelaOutterFace";

	/**
	 * ͼԪ��ģ�͹�ϵά�� String graphUnitID:ͼԪ��� String moduleID:ģ�ͱ��
	 */
	public static final int MODIFY_GRAPH_UNIT_AND_MODULE_RELA = 6;
	/**
	 * ͼԪ��ģ�͹�ϵά���ⲿ�ӿ���
	 */
	public static final String MODIFY_GRAPH_UNIT_AND_MODULE_RELA_NAME = "modifySymbolModuleRelaOutterFace";

}
