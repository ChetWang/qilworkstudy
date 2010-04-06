package com.nci.svg.sdk.server.cache;

import java.util.HashMap;

import com.nci.svg.sdk.server.ModuleDefines;
import com.nci.svg.sdk.server.module.ServerModuleAdapter;

/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author YUX
 * @时间：2008-11-21
 * @功能：服务器端缓存管理组件抽象类
 * @使用说明：
 *     本缓存管理组件管理的数据内容目前支持以下三类：String,ArrayList,HashMap,后两者内的数据可以自行定义
 *     对于需要采用ArrayList和HashMap进行保存的数据bean类定义，必须实现以下2个方法：
 *     1、toString()，返回该数据唯一的标识
 *     2、equals(Object obj),比对两个数据bean内关键或全部数据是否一致
 */
public abstract class CacheManagerAdapter extends ServerModuleAdapter {
	public CacheManagerAdapter(HashMap parameters)
	{
		super(parameters);
	}
    /**
     * 增加缓存类型，如果已存在则不添加
     * @param key:类型名称
     */
    public abstract void addCacheType(String key);
    
    /**
     * 删除缓存类型
     * @param key:类型名称
     */
    public abstract void removeCacheType(String key);
    
    /**
     * 增加指定缓存类型的数据
     * 如存在则不设置
     * @param type:类型
     * @param key:关键字
     * @param cacheInnerData:数据
     *     当type＝CacheObject.STRING_FLAG时，cacheInnerData为待保存数据
     *     当type＝CacheObject.LIST_FLAG时，
     *               cacheInnerData为待存储的内容,可以为单个数据bean
     *     当type＝CacheObject.MAP_FLAG时，
     *               cacheInnerData为待存储的内容,可以为单个数据bean
     *      其他不满足以上条件的均不进行操作，返回OPER_ERROR
     * @return:增加结果,成功返回OPER_SUCCESS,失败返回OPER_ERROR
     */
    public abstract int addCacheInnerData(String type, String key,Object cacheInnerData);
    
    /**
     * 删除符合传入的指定类型和数据关键值
     * @param type:类型
     * @param key:关键字
     * @param cacheInnerData:数据关键值
     *     当type＝CacheObject.STRING_FLAG时，cacheInnerData为待删除的key
     *     当type＝CacheObject.LIST_FLAG时
     *               cacheInnerData为待删除的内容,可以为单个数据bean
     *     当type＝CacheObject.MAP_FLAG时，
     *               cacheInnerData为待删除的内容,可以为单个数据bean
     *      其他不满足以上条件的均不进行操作，返回OPER_ERROR
     * @return:删除结果,成功返回OPER_SUCCESS,失败返回OPER_ERROR
     */
    public abstract int removeCacheInnerData(String type,String key, Object cacheInnerData);
    
    /**
     * 设置指定缓存类型和数据关键值的数据项
     * 如不存在则新增
     * @param type:类型
     * @param key:关键字
     * @param cacheInnerData:含数据关键值的数据项
     *     当type＝CacheObject.STRING_FLAG时，key为传入的待存储值，cacheInnerData无效
     *     当type＝CacheObject.LIST_FLAG时，key为传入的待存储值，
     *               cacheInnerData为待存储的内容,可以为单个数据bean
     *     当type＝CacheObject.MAP_FLAG时，
     *               cacheInnerData为待存储的内容,只能为单个数据bean
     *      其他不满足以上条件的均不进行操作，返回OPER_ERROR
     * @return:设置结果,成功返回OPER_SUCCESS,失败返回OPER_ERROR
     */
    public abstract int setCacheObj(String type,String key,Object cacheInnerData);
    
    
    /**
     * 根据传入的类型名称获取缓存数据
     * @param key:关键字
     * @return:该类型的缓存数据
     */
    public abstract CacheObject getCacheObj(String key);

	public String getModuleType() {
		return ModuleDefines.CACHE_MANAGER;
	}
    
}
