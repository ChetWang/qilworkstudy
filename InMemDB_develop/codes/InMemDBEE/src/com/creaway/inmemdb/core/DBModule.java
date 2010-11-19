/*
 * @(#)DBModule.java	1.0  08/23/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.core;

import java.util.Map;

/**
 * 内存数据库模块接口
 * @author Qil.Wong
 *
 */
public interface DBModule {
	
	/**
	 *启动模块
	 */
	public void startModule(Map<?, ?> params);
	
	/**
	 * 关闭模块
	 */
	public void shutdownModule(Map<?, ?> params);

}
