package com.nci.svg.sdk.server.cache;

import java.util.HashMap;

import com.nci.svg.sdk.server.ModuleDefines;
import com.nci.svg.sdk.server.module.ServerModuleAdapter;

/**
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author YUX
 * @ʱ�䣺2008-11-21
 * @���ܣ��������˻���������������
 * @ʹ��˵����
 *     �������������������������Ŀǰ֧���������ࣺString,ArrayList,HashMap,�������ڵ����ݿ������ж���
 *     ������Ҫ����ArrayList��HashMap���б��������bean�ඨ�壬����ʵ������2��������
 *     1��toString()�����ظ�����Ψһ�ı�ʶ
 *     2��equals(Object obj),�ȶ���������bean�ڹؼ���ȫ�������Ƿ�һ��
 */
public abstract class CacheManagerAdapter extends ServerModuleAdapter {
	public CacheManagerAdapter(HashMap parameters)
	{
		super(parameters);
	}
    /**
     * ���ӻ������ͣ�����Ѵ��������
     * @param key:��������
     */
    public abstract void addCacheType(String key);
    
    /**
     * ɾ����������
     * @param key:��������
     */
    public abstract void removeCacheType(String key);
    
    /**
     * ����ָ���������͵�����
     * �����������
     * @param type:����
     * @param key:�ؼ���
     * @param cacheInnerData:����
     *     ��type��CacheObject.STRING_FLAGʱ��cacheInnerDataΪ����������
     *     ��type��CacheObject.LIST_FLAGʱ��
     *               cacheInnerDataΪ���洢������,����Ϊ��������bean
     *     ��type��CacheObject.MAP_FLAGʱ��
     *               cacheInnerDataΪ���洢������,����Ϊ��������bean
     *      �������������������ľ������в���������OPER_ERROR
     * @return:���ӽ��,�ɹ�����OPER_SUCCESS,ʧ�ܷ���OPER_ERROR
     */
    public abstract int addCacheInnerData(String type, String key,Object cacheInnerData);
    
    /**
     * ɾ�����ϴ����ָ�����ͺ����ݹؼ�ֵ
     * @param type:����
     * @param key:�ؼ���
     * @param cacheInnerData:���ݹؼ�ֵ
     *     ��type��CacheObject.STRING_FLAGʱ��cacheInnerDataΪ��ɾ����key
     *     ��type��CacheObject.LIST_FLAGʱ
     *               cacheInnerDataΪ��ɾ��������,����Ϊ��������bean
     *     ��type��CacheObject.MAP_FLAGʱ��
     *               cacheInnerDataΪ��ɾ��������,����Ϊ��������bean
     *      �������������������ľ������в���������OPER_ERROR
     * @return:ɾ�����,�ɹ�����OPER_SUCCESS,ʧ�ܷ���OPER_ERROR
     */
    public abstract int removeCacheInnerData(String type,String key, Object cacheInnerData);
    
    /**
     * ����ָ���������ͺ����ݹؼ�ֵ��������
     * �粻����������
     * @param type:����
     * @param key:�ؼ���
     * @param cacheInnerData:�����ݹؼ�ֵ��������
     *     ��type��CacheObject.STRING_FLAGʱ��keyΪ����Ĵ��洢ֵ��cacheInnerData��Ч
     *     ��type��CacheObject.LIST_FLAGʱ��keyΪ����Ĵ��洢ֵ��
     *               cacheInnerDataΪ���洢������,����Ϊ��������bean
     *     ��type��CacheObject.MAP_FLAGʱ��
     *               cacheInnerDataΪ���洢������,ֻ��Ϊ��������bean
     *      �������������������ľ������в���������OPER_ERROR
     * @return:���ý��,�ɹ�����OPER_SUCCESS,ʧ�ܷ���OPER_ERROR
     */
    public abstract int setCacheObj(String type,String key,Object cacheInnerData);
    
    
    /**
     * ���ݴ�����������ƻ�ȡ��������
     * @param key:�ؼ���
     * @return:�����͵Ļ�������
     */
    public abstract CacheObject getCacheObj(String key);

	public String getModuleType() {
		return ModuleDefines.CACHE_MANAGER;
	}
    
}
