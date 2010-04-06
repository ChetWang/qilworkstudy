package com.nci.svg.sdk.communication;

/**
 * <p>
 * ���⣺BussinessModuleSearch.java
 * </p>
 * <p>
 * ������ �ͻ�������������
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2008-12-12
 * @version 1.0 �����еĲ������ƾ�������ActionParams��
 */
public class ActionNames {
	/**
	 * ϵͳ����
	 */
	public static final String action = "action";

	/**
	 * ��ȡ�ͻ�������ģ��
	 * 
	 * @param MODULE_TYPE:
	 *            String ������ͣ���Ϊƽ̨�����ҵ������� ���������Ϊ��ʱ����ȫ���ͻ�������İ汾��Ϣ
	 */
	public static final String GET_UPGRADE_MODULE = "getUpgradeModule";

	/**
	 * ���ؿͻ�������ģ��
	 * 
	 * @param MODULE_SHORT_NAME
	 *            String �������
	 */
	public static final String DOWN_UPGRADE_MODULE = "downloadUpgradeModule";

	/**
	 * ��ȡϵͳ�����б�
	 * 
	 * @param MODULE_NAME
	 *            String ����ģ������
	 * @param EXPRSTRING
	 *            String ���������ַ���
	 */
	public static final String GET_SVG_CODES = "getSvgCodes";

	/**
	 * ��ȡϵͳ֧��ͼ���ļ������б�
	 * 
	 * @param BUSINESS_ID
	 *            String ҵ��ϵͳ���
	 */
	public static final String GET_SUPPORT_FILE_TYPE = "getSupportFileType";

	/**
	 * ����ͼԪ
	 * 
	 * @param ID
	 *            String ͼԪ���
	 * @param SYMBOL_TYPE
	 *            String ��ȡͼԪ���ࣨgraphunit��template��
	 * @param OPERATOR
	 *            String ��ȡͼԪ������Ա
	 * @param NAME
	 *            String ��ȡͼԪ����
	 * @param CONTENT
	 *            String ��ȡͼԪ����
	 * @param VARIETY
	 *            String ��ȡͼԪ����
	 * @param PARAM1
	 *            String ��ȡparam1����
	 * @param PARAM2
	 *            String ��ȡparam2����
	 * @param PARAM3
	 *            String ��ȡparam3����
	 */
	public static final String SAVE_SYMBOL = "saveSymbol";

	/**
	 * ��ȡͼԪ
	 * 
	 * @param NAME
	 *            String ͼԪ��ģ������
	 */
	public static final String GET_SYMBOL = "getSymbol";

	/**
	 * ��ȡͼԪ����
	 * 
	 * @param SYMBOL_TYPE
	 *            StringͼԪ����(ͼԪgraphunit��ģ��template)
	 */
	public static final String GET_SYMBOL_TYPE_LIST = "getSymbolTypeList";

	/**
	 * ��ȡͼԪ�б�
	 * 
	 * @param SYMBOL_TYPE
	 *            String ͼԪ����(ͼԪgraphunit��ģ��template)
	 * @param OPERATOR
	 *            String�����ˣ����������Ϊ����ȡ�������˹���
	 */
	public static final String GET_SYMBOLS_LIST = "getSymbolsList";

	/**
	 * ��ȡ��������ͼԪ�汾��Ϣ
	 * 
	 * @param OPERATOR
	 *            String �����ˣ����������Ϊ����ȡ�������˹���
	 * @param OWNERS
	 *            String ������
	 */
	public static final String GET_SYMBOLS_VERSION = "getSymbolsVersion";

	/**
	 * �ͻ������������ͨ�ż��
	 */
	public static final String CHECK_URL_CONN = "checkURLConn";

	/**
	 * ��ȡҵ��ģ���б�
	 * 
	 * @param BUSINESS_ID
	 *            ҵ��ϵͳ���
	 * @param MODULE_ID:
	 *            ��ģ�ͱ��,��Ϊ��,���ѯ����ģ��
	 */
	public static final String GET_MODEL_LIST = "getModelList";

	/**
	 * ��ȡҵ��ģ������
	 * 
	 * @param BUSINESS_ID
	 *            ҵ��ϵͳ���
	 * @param MODULE_ID:
	 *            ģ�ͱ��
	 */
	public static final String GET_MODEL_PARAMS = "getModelParams";

	/**
	 * ��ȡҵ��ģ�Ͷ���
	 */
	public static final String GET_MODEL_ACTIONS = "getModelActions";

	/**
	 * ��ȡҵ��ģ��״̬
	 * 
	 * @param BUSINESS_ID
	 *            ҵ��ϵͳ���
	 * @param MODULE_ID:
	 *            ģ�ͱ��
	 */
	public static final String GET_MODEL_STATUSES = "getModelStatuses";

	/**
	 * ͼԪ��ģ�͹�ϵ��ȡ
	 * 
	 * @param BUSINESS_ID
	 *            ҵ��ϵͳ���
	 * @param MODULE_ID:
	 *            ģ�ͱ��
	 */
	public static final String GET_GRAPH_UNIT_AND_MODULE_RELA = "getGraphUnitAndModuleRela";

	/**
	 * ͼԪ��ģ�͹�ϵά��
	 */
	public static final String MODIFY_GRAPH_UNIT_AND_MODULE_RELA = "modifyGraphUnitAndModuleRela";

	/**
	 * ת��ͼԪ��jpg��ʽ
	 * 
	 * @param NAME
	 *            ͼԪ����
	 */
	public static final String TRANSFORM_SYMBOL_TO_JPG = "transformSymbolToJpg";

	/**
	 * ת��svgͼ��jpg��ʽ
	 * 
	 * @param BUSINESS_ID
	 *            ҵ��ϵͳ���
	 * @param FILE_NAME:
	 *            �ļ�����
	 */
	public static final String TRANSFORM_SVGFILE_TO_JPG = "transformSvgFileToJpg";

	/**
	 * Web���� ���������Ӧ��
	 */
	public static final String CONFIG_CODE_DATA = "configCodeData";

	/**
	 * Web���� �������������Ӧ��
	 */
	public static final String CONFIG_SERVICE_MANAGER = "configServiceManager";

	/**
	 * Web���� ����״̬�����Ӧ��
	 */
	public static final String GET_SERVICE_STATUS = "getServiceStatus";

	/**
	 * Web���� ���������Ӧ��
	 */
	public static final String CONFIG_SERVICE_MODULE = "configServiceModule";

	/**
	 * Web���� ͼԪ������Ӧ��
	 */
	public static final String CONFIG_SYMBOL_MANAGER = "configSymbolManager";

	/**
	 * Web���� ҵ��淶��Ӧ��
	 */
	public static final String CONFIG_INDUNORM = "configIndunorm";

	/**
	 * ��ȡϵͳ���ò���
	 * 
	 * @param SHORT_NAME:���������
	 */
	public static final String GET_SYSSETS = "getSysSets";

	/**
	 * ��ͼ���ļ����浽ָ��ҵ��ϵͳͼ�α���
	 * 
	 * @param BUSINESS_ID:ҵ��ϵͳ���
	 * @param ID:ͼ���
	 * @param FILE_NAME��ͼ����
	 * @param GRAPH_TYPE��ͼ���ͣ���׼ͼ������ͼ
	 * @param BUSINESS_TYPE:ҵ��ͼ���ͣ��磬ϵͳͼ��һ�ν���ͼ�ȣ�
	 * @param FILE_FORMAT��ͼ��ʽ��svg��jpg
	 * @param PARAM+0..9:��Ӧ������
	 * @param OPERATOR:�޸���
	 * @param CONTENT:ͼ���ļ�����
	 * @param LOGS:ͼ���޸ļ�¼
	 * @return ResultBean,obj=GraphFileBean
	 */
	public static final String SAVE_GRAPH_FILE = "saveGraphFile";

	/**
	 * ��ָ��ҵ��ϵͳͼ�α��л�ȡͼ��
	 * 
	 * @param BUSINESS_ID:ҵ��ϵͳ���
	 * @param ID:ͼ���
	 * @param FILE_NAME:ͼ����
	 * @return ResultBean,obj=GraphFileBean
	 */
	public static final String GET_GRAPH_FILE = "getGraphFile";

	/**
	 * ��ָ��ҵ��ϵͳͼ�α��л�ȡͼ���б�
	 * 
	 * @param BUSINESS_ID:ҵ��ϵͳ���
	 * @param GRAPH_TYPE��ͼ���ͣ���׼ͼ������ͼ
	 * @param BUSINESS_TYPE:ҵ��ͼ���ͣ��磬ϵͳͼ��һ�ν���ͼ�ȣ�
	 * @param FILE_FORMAT��ͼ��ʽ��svg��jpg
	 * @param PARAM+0..9:��Ӧ������
	 * @param OPERATOR:�޸���
	 * @return ResultBean,obj=ArrayList<GraphFileBean>
	 */
	public static final String GET_GRAPHFILE_LIST = "getGraphFileList";

	/**
	 * ��ȡ�淶��ͼ���͹�����Ϣ
	 * 
	 * @param BUSINESS_ID:ҵ��ϵͳ���
	 * @param BUSINESS_TYPE:ҵ��ͼ���ͣ��磬ϵͳͼ��һ�ν���ͼ�ȣ�
	 */
	public static final String GET_INDUNORM_GRAPHTYPE_RELA = "getIndunormGraphTypeRela";
	/**
	 * ��ȡͼ������淶����������Ϣ
	 * 
	 * @param BUSINESS_ID:ҵ��ϵͳ���
	 * @param BUSINESS_TYPE:ҵ��ͼ���ͣ��磬ϵͳͼ��һ�ν���ͼ�ȣ�
	 */
	public static final String GET_INDUNORM_DESC_RELA = "getIndunormDescRela";
	/**
	 * ��ȡ�淶��������ͼԪ������Ϣ
	 * 
	 * @param BUSINESS_ID:ҵ��ϵͳ���
	 * @param BUSINESS_TYPE:ҵ��ͼ���ͣ��磬ϵͳͼ��һ�ν���ͼ�ȣ�
	 * @param SYMBOL_TYPE:ͼԪ����(ͼԪ/ģ��)
	 * @param SYMBOL_ID:ͼԪ���(������ģ����)
	 */
	public static final String GET_INDUNORM_FIELD_SYMBOL_RELA = "getIndunormFieldSymbolRela";
	/**
	 * ��ȡ�淶��ҵ��ģ�͹�����Ϣ
	 * 
	 * @param BUSINESS_ID:ҵ��ϵͳ���
	 * @param MODEL_ID:ҵ��ģ�ͱ��
	 * @param INDUNORM_ID:�淶���
	 */
	public static final String GET_INDUNORM_MODEL_RELA = "getIndunormModelRela";

	/**
	 * add by yux,2009-1-12 ��ȡ��ǰҵ��ϵͳ���й淶�ļ����б�
	 * 
	 * @param BUSINESS_ID:ҵ��ϵͳ���
	 */
	public static final String GET_SIMPLE_INDUNORM_LIST = "getSimpleIndunormList";

	/**
	 * ��ȡָ��ҵ��ϵͳ���ļ����͵ı��������Ϣ
	 * 
	 * @param BUSINESS_ID:ҵ��ϵͳ���
	 * @param BUSINESS_TYPE:ҵ��ͼ���ͣ��磬ϵͳͼ��һ�ν���ͼ�ȣ�
	 * @return:�����򷵻�GraphFileParamsBean
	 */
	public static final String GET_GRAPHFILE_PARAMS = "getGraphFileParams";

	/**
	 * �ж�ͼԪ��ģ���Ƿ�������ģ���б�����
	 * 
	 * @param SYMBOL_NAME
	 *            symbol����
	 * @return List ����Щģ��Ӧ�õ���Ԫ��ΪString������ģ������
	 */
	public static final String CHECK_SYMBOL_TEMPLATE_RELATION = "checkSymbolTemplateRelation";

	/**
	 * �ж�symbol���Ƶ���Ч�ԣ��Ƿ��ظ�
	 * 
	 * @param SYMBOL_NAME
	 *            ��symbol���ơ���symbolName
	 * @return Boolean trueΪ��Ч��falseΪ��Ч
	 */
	public static final String CHECK_SYMBOL_NAME_REPEAT = "checkSymbolNameRepeat";

	/**
	 * ������,���Ļ����е����Ƽ����ݿ��еļ�¼����
	 * 
	 * @param OLDNAME
	 *            ԭsymbol����
	 * @param NEWNAME
	 *            ��symbol����
	 * 
	 */
	public static final String RENAME_SYMBOL = "renameSymbol";

	/**
	 * add by yux,2009-1-18 ��ȡ��ҵ��ϵͳ֧�ֵ�����ҵ��淶�嵥�б�
	 * 
	 * @param BUSINESS_ID:ҵ��ϵͳ���
	 */
	public static final String GET_BUSINESS_INDUNORM = "getBusinessIndunorm";

	/**
	 * add by yux,2009-1-18 ��ȡ��ҵ��ϵͳ֧�ֵ�����ģ���б�
	 * 
	 * @param BUSINESS_ID:ҵ��ϵͳ���
	 */
	public static final String GET_BUSINESS_MODEL = "getBusinessModel";
	public static final String GET_BUSINESS_MODEL2 = "getBusinessModel2";

	/**
	 * add by yux,2009-1-18 ��ȡ��ҵ��ϵͳ֧�ֵ�����ģ����ҵ��淶������ϵ�б�
	 * 
	 * @param BUSINESS_ID:ҵ��ϵͳ���
	 */
	public static final String GET_BUSINESS_MOELRELAINDUNORM = "getModelRelaIndunorm";

	/**
	 * add by yux,2009-2-19 ��ȡҵ������ֵ �����ڿͻ����ڲ����Կ򲿷�ʹ��
	 */
	public static final String GET_BUSINESS_PROPERTYVALUE = "getBusinessPropertyValue";

	/**
	 * add by yux,2009-3-5 ��ȡ��ҵ��ϵͳ��ǰҵ��ͼ���͵Ĳ����ӿ�����
	 * 
	 * @param BUSINESS_ID:ҵ��ϵͳ���
	 * @param BUSINESS_TYPE:ҵ��ͼ����
	 */
	public static final String GET_GRAPHBUSINESSTYPE_CANVASOPER = "getGraphCanvasOper";

	/**
	 * add by zhangsf,2009-3-26 ��ȡҵ��ϵͳ̨��ͼ�б�
	 * 
	 * @param BUSINESS_ID:ҵ��ϵͳ���
	 */
	public static final String GET_NESTDISTRICTLIST = "getNestDistrictList";
	/**
	 * add by zhangsf,2009-3-27 ��ȡָ��̨��������̨��ͼ������豸��Ϣ
	 * 
	 * @param DISTRICTID:̨�����
	 */
	public static final String GETNESTDISTRICTDEVICES = "getNestDistrictDevices";

	public static final String GET_MODEL_DEMO_DATA = "getModelDemoData";
	/**
	 * ��ȡҵ��ģ��ģ������
	 */
	public static final String GET_AREA_DEMO_DATA = "getAreaDemoData";

	// FIXME:������˵��
	// public static final String GET_NESTDISTRICTLIST = "getNestDistrictList";
}
