package com.nci.ums.basic;

/**
 * UMS基础模块，所有应用、渠道都扩展自UMSModule，UMS服务器通过该接口可以控制所有模块
 * 
 * @author Qil.Wong
 * 
 */
public interface UMSModule {
	/**
	 * 模块启动
	 */
	public void startModule();

	/**
	 * 模块停止
	 */
	public void stopModule();
}
