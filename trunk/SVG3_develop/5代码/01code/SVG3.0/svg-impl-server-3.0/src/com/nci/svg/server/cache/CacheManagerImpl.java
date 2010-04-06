package com.nci.svg.server.cache;

import java.util.HashMap;
import java.util.List;

import com.nci.svg.sdk.module.ModuleStopedException;
import com.nci.svg.sdk.server.cache.CacheManagerAdapter;
import com.nci.svg.sdk.server.cache.CacheObject;

/**
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author YUX
 * @ʱ�䣺2008-11-21
 * @���ܣ����������
 * 
 * 
 */
public class CacheManagerImpl extends CacheManagerAdapter {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3846153117175526545L;

	private HashMap mapCache = new HashMap();

	public CacheManagerImpl(HashMap parameters) {
		super(parameters);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nci.svg.sdk.server.cache.CacheManagerAdapter#addCacheInnerData(java.lang.String,
	 *      java.lang.String, java.lang.Object)
	 */
	public int addCacheInnerData(String type, String key, Object cacheInnerData) {
		// ��ȫУ��
		if (type == null || type.length() == 0 || key == null
				|| key.length() == 0 || cacheInnerData == null)
			return OPER_ERROR;
		CacheObject obj = null;
		// ��У����������
		if (type.equals(CacheObject.STRING_FLAG)) {
			// ������������
			obj = getCacheObj(key);
			if (obj == null) {
				// ������������
				obj = new CacheObject();
				obj.setCacheType(key);
				obj.setCacheFlag(CacheObject.STRING_FLAG);
				obj.setCacheString((String) cacheInnerData);
				mapCache.put(key, obj);
			} else {
				// �����򷵻�ʧ����Ϣ
				return OPER_ERROR;
			}
		} else if (type.equals(CacheObject.LIST_FLAG)) {
			// ��У��list����
			obj = getCacheObj(key);
			if (obj == null) {
				// ������������
				obj = new CacheObject();
				obj.setCacheType(key);
				obj.setCacheList((List)cacheInnerData);
				obj.setCacheFlag(CacheObject.LIST_FLAG);
				mapCache.put(key, obj);
			} else {
				// �ڲ��������Ͳ�ƥ�䣬�򷵻ش���
				if (!obj.getCacheFlag().equals(CacheObject.LIST_FLAG))
					return OPER_ERROR;
			}

			// ����Ϊ����beanʱ��ƥ��һ�£��粻�ڶ����У�������
			if (obj.getCacheList().contains(cacheInnerData)) {
				return OPER_ERROR;
			}
//			obj.getCacheList().add(cacheInnerData);
		} else if (type.equals(CacheObject.MAP_FLAG)) {
			// ��У��map����
			obj = getCacheObj(key);
			if (obj == null) {
				// ������������
				obj = new CacheObject();
				obj.setCacheType(key);
				obj.setCacheMap((HashMap)cacheInnerData);
				obj.setCacheFlag(CacheObject.MAP_FLAG);
				mapCache.put(key, obj);
			} else {
				// �ڲ��������Ͳ�ƥ�䣬�򷵻ش���
				if (!obj.getCacheFlag().equals(CacheObject.MAP_FLAG))
					return OPER_ERROR;
				obj.setCacheMap((HashMap)cacheInnerData);
			}

			// ƥ��keyֵ��������������
			if (obj.getCacheMap().get(cacheInnerData.toString()) != null) {
				return OPER_ERROR;
			}
//			obj.getCacheMap().put(cacheInnerData.toString(), cacheInnerData);
		} else
			return OPER_ERROR;
		return OPER_SUCCESS;
	}

	public void addCacheType(String key) {
		CacheObject obj = (CacheObject) mapCache.get(key);
		if (obj == null) {
			// �����ڣ�������
			obj = new CacheObject();
			obj.setCacheType(key);
			mapCache.put(key, obj);
		}
	}

	public CacheObject getCacheObj(String key) {
		// ����keyֵ��ȡ������Ϣ��ת�ͷ���
		CacheObject obj = (CacheObject) mapCache.get(key);
		return obj;
	}

	public int removeCacheInnerData(String type, String key,
			Object cacheInnerData) {
		// ��ȫУ��
		if (type == null || type.length() == 0 || key == null
				|| key.length() == 0 || cacheInnerData == null)
			return OPER_ERROR;
		CacheObject obj = null;
		// ��У����������
		if (type.equals(CacheObject.STRING_FLAG)) {
			// ������������
			obj = getCacheObj(key);
			if (obj != null) {
				// �������ɾ��
				mapCache.remove(key);
			} else
				return OPER_ERROR;
		} else if (type.equals(CacheObject.LIST_FLAG)) {
			// ��У��list����
			obj = getCacheObj(key);
			if (obj == null) {
				// �粻�����򷵻�
				return OPER_ERROR;
			} else {
				// �ڲ��������Ͳ�ƥ�䣬�򷵻ش���
				if (!obj.getCacheFlag().equals(CacheObject.LIST_FLAG))
					return OPER_ERROR;
			}

			// ����ɾ��
			obj.getCacheList().remove(cacheInnerData);
		} else if (type.equals(CacheObject.MAP_FLAG)) {
			// ��У��map����
			obj = getCacheObj(key);
			if (obj == null) {
				// �������򷵻ش���
				return OPER_ERROR;
			} else {
				// �ڲ��������Ͳ�ƥ�䣬�򷵻ش���
				if (!obj.getCacheFlag().equals(CacheObject.MAP_FLAG))
					return OPER_ERROR;
			}

			// ������ɾ��
			if (obj.getCacheMap().containsKey(cacheInnerData.toString()))
				obj.getCacheMap().remove(cacheInnerData.toString());
		} else
			return OPER_ERROR;
		return OPER_SUCCESS;
	}

	public void removeCacheType(String key) {
		CacheObject obj = (CacheObject) mapCache.get(key);
		if (obj != null) {
			mapCache.remove(key);
		}
	}

	public int setCacheObj(String type, String key, Object cacheInnerData) {
		// ��ȫУ��
		if (type == null || type.length() == 0 || key == null
				|| key.length() == 0 || cacheInnerData == null)
			return OPER_ERROR;
		CacheObject obj = null;
		// ��У����������
		if (type.equals(CacheObject.STRING_FLAG)) {
			// ������������
			obj = getCacheObj(key);
			if (obj == null) {
				// ������������
				obj = new CacheObject();
				obj.setCacheType(key);
				obj.setCacheFlag(CacheObject.STRING_FLAG);
				obj.setCacheString((String) cacheInnerData);

			} else if (!obj.getCacheType().equals(CacheObject.STRING_FLAG))
				return OPER_ERROR;
//			mapCache.put(key, obj);
			obj.setCacheString((String) cacheInnerData);
		} else if (type.equals(CacheObject.LIST_FLAG)) {
			// ��У��list����
			obj = getCacheObj(key);
			if (obj == null) {
				// ������������
				obj = new CacheObject();
				obj.setCacheType(key);
				obj.setCacheFlag(CacheObject.LIST_FLAG);
				mapCache.put(key, obj);
			} else {
				// �ڲ��������Ͳ�ƥ�䣬�򷵻ش���
				if (!obj.getCacheFlag().equals(CacheObject.LIST_FLAG))
					return OPER_ERROR;
			}

			{
				// ����
//				obj.getCacheList().add(cacheInnerData);
				obj.setCacheList((List)cacheInnerData);
			}
		} else if (type.equals(CacheObject.MAP_FLAG)) {
			// ��У��map����
			obj = getCacheObj(key);
			if (obj == null) {
				// ������������
				obj = new CacheObject();
				obj.setCacheType(key);
				obj.setCacheFlag(CacheObject.MAP_FLAG);
				mapCache.put(key, obj);
			} else {
				// �ڲ��������Ͳ�ƥ�䣬�򷵻ش���
				if (!obj.getCacheFlag().equals(CacheObject.MAP_FLAG))
					return OPER_ERROR;
			}

			{
				// ����
//				obj.getCacheMap()
//						.put(cacheInnerData.toString(), cacheInnerData);
				obj.setCacheMap((HashMap)cacheInnerData);
			}
		} else
			return OPER_ERROR;
		return OPER_SUCCESS;
	}

	public String getModuleID() {
		return "CacheManager";
	}

	public Object handleOper(int index, Object obj)
			throws ModuleStopedException {
		return null;
	}

}
