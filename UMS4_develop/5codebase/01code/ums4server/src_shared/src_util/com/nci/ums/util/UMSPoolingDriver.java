package com.nci.ums.util;

import java.util.Map;

import org.apache.commons.dbcp.PoolingDriver;

public class UMSPoolingDriver extends PoolingDriver{
	
	public UMSPoolingDriver(){
		super();
	}
	
	public Map getPool(){
		return _pools;
	}
}
