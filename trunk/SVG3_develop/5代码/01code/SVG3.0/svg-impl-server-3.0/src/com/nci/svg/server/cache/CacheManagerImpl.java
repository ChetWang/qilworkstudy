package com.nci.svg.server.cache;

import java.util.HashMap;
import java.util.List;

import com.nci.svg.sdk.module.ModuleStopedException;
import com.nci.svg.sdk.server.cache.CacheManagerAdapter;
import com.nci.svg.sdk.server.cache.CacheObject;

/**
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author YUX
 * @时间：2008-11-21
 * @功能：缓存组件类
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
		// 安全校验
		if (type == null || type.length() == 0 || key == null
				|| key.length() == 0 || cacheInnerData == null)
			return OPER_ERROR;
		CacheObject obj = null;
		// 先校验文字类型
		if (type.equals(CacheObject.STRING_FLAG)) {
			// 设置文字内容
			obj = getCacheObj(key);
			if (obj == null) {
				// 不存在则新增
				obj = new CacheObject();
				obj.setCacheType(key);
				obj.setCacheFlag(CacheObject.STRING_FLAG);
				obj.setCacheString((String) cacheInnerData);
				mapCache.put(key, obj);
			} else {
				// 存在则返回失败信息
				return OPER_ERROR;
			}
		} else if (type.equals(CacheObject.LIST_FLAG)) {
			// 再校验list类型
			obj = getCacheObj(key);
			if (obj == null) {
				// 不存在则新增
				obj = new CacheObject();
				obj.setCacheType(key);
				obj.setCacheList((List)cacheInnerData);
				obj.setCacheFlag(CacheObject.LIST_FLAG);
				mapCache.put(key, obj);
			} else {
				// 内部缓存类型不匹配，则返回错误
				if (!obj.getCacheFlag().equals(CacheObject.LIST_FLAG))
					return OPER_ERROR;
			}

			// 传入为单个bean时，匹配一下，如不在队列中，则新增
			if (obj.getCacheList().contains(cacheInnerData)) {
				return OPER_ERROR;
			}
//			obj.getCacheList().add(cacheInnerData);
		} else if (type.equals(CacheObject.MAP_FLAG)) {
			// 再校验map类型
			obj = getCacheObj(key);
			if (obj == null) {
				// 不存在则新增
				obj = new CacheObject();
				obj.setCacheType(key);
				obj.setCacheMap((HashMap)cacheInnerData);
				obj.setCacheFlag(CacheObject.MAP_FLAG);
				mapCache.put(key, obj);
			} else {
				// 内部缓存类型不匹配，则返回错误
				if (!obj.getCacheFlag().equals(CacheObject.MAP_FLAG))
					return OPER_ERROR;
				obj.setCacheMap((HashMap)cacheInnerData);
			}

			// 匹配key值，不存在则新增
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
			// 不存在，则新增
			obj = new CacheObject();
			obj.setCacheType(key);
			mapCache.put(key, obj);
		}
	}

	public CacheObject getCacheObj(String key) {
		// 根据key值获取缓存信息，转型返回
		CacheObject obj = (CacheObject) mapCache.get(key);
		return obj;
	}

	public int removeCacheInnerData(String type, String key,
			Object cacheInnerData) {
		// 安全校验
		if (type == null || type.length() == 0 || key == null
				|| key.length() == 0 || cacheInnerData == null)
			return OPER_ERROR;
		CacheObject obj = null;
		// 先校验文字类型
		if (type.equals(CacheObject.STRING_FLAG)) {
			// 设置文字内容
			obj = getCacheObj(key);
			if (obj != null) {
				// 如存在则删除
				mapCache.remove(key);
			} else
				return OPER_ERROR;
		} else if (type.equals(CacheObject.LIST_FLAG)) {
			// 再校验list类型
			obj = getCacheObj(key);
			if (obj == null) {
				// 如不存在则返回
				return OPER_ERROR;
			} else {
				// 内部缓存类型不匹配，则返回错误
				if (!obj.getCacheFlag().equals(CacheObject.LIST_FLAG))
					return OPER_ERROR;
			}

			// 单点删除
			obj.getCacheList().remove(cacheInnerData);
		} else if (type.equals(CacheObject.MAP_FLAG)) {
			// 再校验map类型
			obj = getCacheObj(key);
			if (obj == null) {
				// 不存在则返回错误
				return OPER_ERROR;
			} else {
				// 内部缓存类型不匹配，则返回错误
				if (!obj.getCacheFlag().equals(CacheObject.MAP_FLAG))
					return OPER_ERROR;
			}

			// 存在则删除
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
		// 安全校验
		if (type == null || type.length() == 0 || key == null
				|| key.length() == 0 || cacheInnerData == null)
			return OPER_ERROR;
		CacheObject obj = null;
		// 先校验文字类型
		if (type.equals(CacheObject.STRING_FLAG)) {
			// 设置文字内容
			obj = getCacheObj(key);
			if (obj == null) {
				// 不存在则新增
				obj = new CacheObject();
				obj.setCacheType(key);
				obj.setCacheFlag(CacheObject.STRING_FLAG);
				obj.setCacheString((String) cacheInnerData);

			} else if (!obj.getCacheType().equals(CacheObject.STRING_FLAG))
				return OPER_ERROR;
//			mapCache.put(key, obj);
			obj.setCacheString((String) cacheInnerData);
		} else if (type.equals(CacheObject.LIST_FLAG)) {
			// 再校验list类型
			obj = getCacheObj(key);
			if (obj == null) {
				// 不存在则新增
				obj = new CacheObject();
				obj.setCacheType(key);
				obj.setCacheFlag(CacheObject.LIST_FLAG);
				mapCache.put(key, obj);
			} else {
				// 内部缓存类型不匹配，则返回错误
				if (!obj.getCacheFlag().equals(CacheObject.LIST_FLAG))
					return OPER_ERROR;
			}

			{
				// 设置
//				obj.getCacheList().add(cacheInnerData);
				obj.setCacheList((List)cacheInnerData);
			}
		} else if (type.equals(CacheObject.MAP_FLAG)) {
			// 再校验map类型
			obj = getCacheObj(key);
			if (obj == null) {
				// 不存在则新增
				obj = new CacheObject();
				obj.setCacheType(key);
				obj.setCacheFlag(CacheObject.MAP_FLAG);
				mapCache.put(key, obj);
			} else {
				// 内部缓存类型不匹配，则返回错误
				if (!obj.getCacheFlag().equals(CacheObject.MAP_FLAG))
					return OPER_ERROR;
			}

			{
				// 设置
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
