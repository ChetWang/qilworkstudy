package com.nci.svg.sdk.client.business;

/**
 * 业务图上图元或模板被选择后可能会触发的业务相关的处理接口
 * @author qi
 *
 */
public interface BusinessSelectionIfc {
	
	/**
	 * 触发的业务相关的处理, 该种处理只在正常模式下触发
	 */
	public void handleSelection();

}
