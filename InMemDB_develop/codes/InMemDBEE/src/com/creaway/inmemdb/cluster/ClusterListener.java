/*
 * @(#)ClusterListener.java	1.0  09/02/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.cluster;

/**
 * 集群变化监听器
 * @author Qil.Wong
 *
 */
public interface ClusterListener {
	
	/**
	 * 从Master转变为slave时的动作
	 * @param cluster
	 */
	public void masterDeprived(ClusterIfc cluster);
	
	/**
	 * 获取到Master地位时的动作
	 * @param cluster
	 */
	public void masterGained(ClusterIfc cluster);

}
