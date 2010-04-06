package com.nci.svg.sdk.client;

import com.nci.svg.sdk.bean.ResultBean;
import com.nci.svg.sdk.client.function.ModuleAdapter;

/**
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @ʱ�䣺2008-12-11
 * @���ܣ����ݹ������
 *
 */
public abstract class DataManageAdapter extends ModuleAdapter {
	/**
	 * ϵͳ��������
	 */
	public static final int KIND_SYSSET = 0;
	/**
	 * �����
	 */
	public static final int KIND_CODES = 1;
	/**
	 * ͼԪ��ģ��
	 */
	public static final int KIND_SYMBOL = 2;
//	/**
//	 * ģ��
//	 */
//	public static final int KIND_TEMPLATE = 3;
	/**
	 * ͼ
	 */
	public static final int KIND_GRAPH = 4;
	/**
	 * �����洢��
	 */
	public static final int KIND_GLOBAL = 5;
	
	/**
	 * add by yux,2009-1-18
	 * ҵ��淶
	 */
	public static final int KIND_INDUNORM = 501;
	public static final String KINDTYPE_INDUNORM = "KINDTYPE_INDUNORM";
	
	/**
	 * add by yux,2009-1-18
	 * ģ��
	 */
	public static final int KIND_MODEL = 502 ;
	public static final String KINDTYPE_MODEL = "KINDTYPE_MODEL";
	
	
	/**
	 * add by yux,2009-1-18
	 * ģ�͹���ҵ��淶
	 */
	public static final int KIND_MODELRELAINDUNORM = 503;
	public static final String KINDTYPE_MODELRELAINDUNORM = "KINDTYPE_MODELRELAINDUNORM";
	
	public static final String KINDTYPE_SYMBOLRELA_MODELANDINDUNORM = "KINDTYPE_SYMBOLRELA_MODELANDINDUNORM";
    public DataManageAdapter(EditorAdapter editor)
    {
    	super(editor);
    }
    
    /**
     * ���ݴ�����������ͻ�ȡ����
     * @param kind:�������࣬���룬ͼԪ��ģ��...
     * @param type���������ͣ�����Ϊ��
     * @param obj���������󣬸�����������ȷ�����������
     * @return��������򷵻����ݣ��������򷵻�null
     * ��kind��KIND_SYSSETʱ��type����Ϊ�գ�objΪ�գ�������ڸ�����ϵͳ���ò������򷵻أ����򷵻�null
     * ��kind=KIND_CODEʱ��type����Ϊ��
     *            objΪ�գ���ѯ�ô������͵����д������ݣ�����HashMap<String,CodeInfoBean>
	 *            objΪString����ʱ��objΪ����ֵ����ѯ�ô��������·��ϸô���ֵ�Ĵ�������,����CodeInfoBean
	 *            objΪCodeInfoBean����ʱ��
	 *            ��valueֵ��Ϊ��ʱ��ѯ�ô��������·��ϸô���ֵ�Ĵ������ݣ�����CodeInfoBean
	 *            ��nameֵ��Ϊ��ʱ��ѯ�ô��������·��ϸô������Ƶĵ�һ���������ݣ�����CodeInfoBean
	 *            ��parentValueֵ��Ϊ��ʱ��ѯ�ô��������·��ϸø�����ֵ�����д������ݣ�����HashMap<String,CodeInfoBean>
	 *            �����������������ѯ��������ʱ������OPER_ERROR
	 * ��kind��KIND_GRAPHUNIT��KIND_TEMPLATEʱ��type����Ϊ��
	 *            ��objΪ��ʱ�������ͼԪ����������ͼԪ,����HashMap<String, NCIEquipSymbolBean>
	 * ��objΪNCIEquipSymbolBeanʱ
	 *            ��name��Ϊ��ʱ���򷵻ظ�ͼԪ�����·��ϸ�name��ͼԪNCIEquipSymbolBean���������򷵻ش���
	 *            �����������������ѯ��������ʱ������OPER_ERROR
	 * 
     */
    public abstract ResultBean getData(int kind,String type,Object obj);
    
    /**
     * ������������������������
     * �������ظ������򸲸�
     * @param kind:�������࣬���룬ͼԪ��ģ��...
     * @param nType�������ͣ�����Ϊ��
     * @param obj���������󣬲���Ϊ�գ�������������ȷ�����������
     * @return���洢������ɹ�����OPER_SUCCESS��ʧ�ܷ���OPER_ERROR
     * ��kind��KIND_SYSSETʱ��objΪString����
     * ��kind=KIND_CODEʱ��objΪCodeInfoBean����
	 * ��kind��KIND_GRAPHUNIT��KIND_TEMPLATEʱ��objΪNCIEquipSymbolBean
	 *            ����������������������Ч������OPER_ERROR
     */
    public abstract int setData(int kind,String type,Object obj);
    
    /**
     * �����������������������
     * �������ظ����ݲ�����
     * @param kind:�������࣬���룬ͼԪ��ģ��...
     * @param nType��������
     * @param obj���������󣬲���Ϊ�գ�������������ȷ�����������
     * @return���洢������ɹ�����OPER_SUCCESS��ʧ�ܷ���OPER_ERROR
     * ��kind��KIND_SYSSETʱ��objΪString����
     * ��kind=KIND_CODEʱ��objΪCodeInfoBean����
	 * ��kind��KIND_GRAPHUNIT��KIND_TEMPLATEʱ��objΪNCIEquipSymbolBean
	 *            ����������������������Ч������OPER_ERROR
     */
    public abstract int addData(int kind,String type,Object obj);
    
    /**
     * ������������ɾ����������
     * @param kind:�������࣬���룬ͼԪ��ģ��...
     * @param nType��������
     * @param obj���������󣬸�����������ȷ�����������
     * @return��ɾ��������ɹ�����OPER_SUCCESS��ʧ�ܷ���OPER_ERROR
     * ��kind��KIND_SYSSETʱ��objΪ��
     * ��kind=KIND_CODEʱ��
     *             objΪ��ʱ����ɾ������������
     *             obj��Ϊ��ʱ,objΪCodeInfoBean����ɾ�����ϸ�CodeInfoBean�����Ĵ�������
	 * ��kind��KIND_GRAPHUNIT��KIND_TEMPLATEʱ��
     *              objΪ��ʱ����ɾ����ͼԪ����
     *             obj��Ϊ��ʱ,objΪNCIEquipSymbolBean����ɾ�����ϸ�CodeInfoBean�����Ĵ�������
     *             ����������������������Ч������OPER_ERROR
     */
    public abstract int removeData(int kind,String type,Object obj);
    
    /**
     * �ӱ��ض�ȡ��Ӧ������������������͵�����
     * ����������Ϊnull��ȫ����ȡ
     * @param kind���������࣬���룬ͼԪ��ģ��...
     * @param type����������
     * @return����ȡ������ɹ�����OPER_SUCCESS��ʧ�ܷ���OPER_ERROR
     */
    public abstract int loadLocal(int kind,String type);
    
    /**
     * ��Զ�̶�ȡ��Ӧ������������������͵�����
     * ����������Ϊnull��ȫ����ȡ
     * @param kind���������࣬���룬ͼԪ��ģ��...
     * @param type����������
     * @return����ȡ������ɹ�����OPER_SUCCESS��ʧ�ܷ���OPER_ERROR
     */
    public abstract int loadRemote(int kind,String type);
    
    /**
     * �����ݹ�����ָ��������������л�������
     * @param kind:�������࣬���룬ͼԪ��ģ�塣��
     * @return�����л�������ɹ�����OPER_SUCCESS��ʧ�ܷ���OPER_ERROR
     */
    public abstract int saveLocal(int kind);
}
