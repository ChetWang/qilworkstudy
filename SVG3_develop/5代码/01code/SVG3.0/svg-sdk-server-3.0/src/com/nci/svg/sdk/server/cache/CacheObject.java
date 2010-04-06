
package com.nci.svg.sdk.server.cache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author YUX
 * @时间：2008-11-21
 * @功能：服务器端缓存实体类
 *
 */
public class CacheObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2328672378296290567L;

	/**
	 * list类型描述
	 */
	public static final String LIST_FLAG = "ArrayList";
	
	/**
	 * map类型描述
	 */
	public static final String MAP_FLAG = "Map";
	/**
	 * String类型描述
	 */
	public static final String STRING_FLAG = "String";
    /**
     * 缓存类型
     */
    private String cacheType;
    
    /**
     * 缓存存储方式,目前支持三种LIST和MAP，String
     */
    private String cacheFlag;
    
    /**
     * 列表缓存存储
     */
    private List cacheList;
    
     /**
     * 字典缓存存储
     */
    private Map cacheMap;
    
    /**
     * 存储字符串
     */
    private String cacheString;
    
    /**
     * 获取缓存类型
     * @return
     */
	public String getCacheType() {
		return cacheType;
	}
	/**
	 * 设置缓存类型
	 * @param cacheType:缓存类型
	 */
	public void setCacheType(String cacheType) {
		this.cacheType = cacheType;
	}
	
	/**
	 * 获取缓存列表
	 * @return
	 */
	public List getCacheList() {
		return cacheList;
	}
	/**
	 * 设置缓存列表
	 * @param cacheList
	 */
	public void setCacheList(List cacheList) {
		this.cacheList = cacheList;
	}
	
	/**
	 * 获取缓存字典
	 * @return
	 */
	public Map getCacheMap() {
		return cacheMap;
	}
	/**
	 * 设置缓存字典
	 * @param cacheMap
	 */
	public void setCacheMap(HashMap cacheMap) {
		this.cacheMap = cacheMap;
	}
	
	/**
	 * 获取缓存存储方式
	 * @return LIST和MAP，String
	 */
	public String getCacheFlag() {
		return cacheFlag;
	}
	
	/**
	 * 设置缓存存储方式
	 * @param cacheFlag:LIST和MAP，String
	 */
	public void setCacheFlag(String cacheFlag) {
		this.cacheFlag = cacheFlag;
	}
	
	/**
	 * 获取缓存存储字符串
	 * @return
	 */
	public String getCacheString() {
		return cacheString;
	}
	
	/**
	 * 设置缓存存储字符串
	 * @param cacheString
	 */
	public void setCacheString(String cacheString) {
		this.cacheString = cacheString;
	}
	
	public String toString() {
		// TODO Auto-generated method stub
		return cacheType;
	}
}
