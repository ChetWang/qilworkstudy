package com.nci.svg.server.cache;

/**
 * <p>
 * 标题：CacheManagerKeys.java
 * </p>
 * <p>
 * 描述： 缓存管理中缓存类型类
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2008-12-18
 * @version 1.0
 */
public class CacheManagerKeys {
	/**
	 * 代码缓存管理
	 */
	public static final String CODE_MANAGER = "cacheManagerCode";

	/**
	 * 图元缓存管理
	 */
	public static final String SYMBOL_MANAGER = "cacheManagerSymbol";

	/**
	 * 图元版本缓存管理（仅用于图元缓存管理中）
	 */
	public static final String SYMBOL_VERSION_MANAGER = "cacheManagerSymbolVersion";
	
	/**
	 * 业务模型动作缓存管理
	 */
	public static final String MODEL_ACTIONS_MANAGER = "cacheManagermodleActions";
}
