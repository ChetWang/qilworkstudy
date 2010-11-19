/*
 * @(#)MasterRestoreNotification.java	1.0  09/07/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.cluster.restore;

import java.io.Serializable;

/**
 * Master主服务的响应命令
 * 
 * @author Qil.Wong
 * 
 */
public class MasterRestoreNotification implements Serializable {

	private static final long serialVersionUID = -7538295709009310921L;

	/**
	 * 等待数据恢复的从属服务地址
	 */
	private Serializable slaveAddress;

	public MasterRestoreNotification() {

	}

	public Serializable getSlaveAddress() {
		return slaveAddress;
	}

	public void setSlaveAddress(Serializable slaveAddress) {
		this.slaveAddress = slaveAddress;
	}

}
