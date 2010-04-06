
package com.nci.svg.sdk.bean;

import java.io.Serializable;

/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @时间：2009-1-7
 * @功能：拓扑信息实体类
 *
 */
public class TopologyBean implements Serializable {
	private static final long serialVersionUID = -6240437900110008517L;
	/**
     * add by yux,2009-1-7
     * 起始点业务编号
     */
    private String beginID;
    /**
     * add by yux,2009-1-7
     * 终止点业务编号
     */
    private String endID;
    /**
     * add by yux,2009-1-7
     * 方向
     */
    private String direction;
	/**
	 * 返回起始点业务编号
	 * @return the beginID
	 */
	public String getBeginID() {
		return beginID;
	}
	/**
	 * 设置起始点业务编号
	 * @param beginID the beginID to set
	 */
	public void setBeginID(String beginID) {
		this.beginID = beginID;
	}
	/**
	 * 返回终止点业务编号
	 * @return the endID
	 */
	public String getEndID() {
		return endID;
	}
	/**
	 * 设置终止点业务编号
	 * @param endID the endID to set
	 */
	public void setEndID(String endID) {
		this.endID = endID;
	}
	/**
	 * 返回方向
	 * @return the direction
	 */
	public String getDirection() {
		return direction;
	}
	/**
	 * 设置方向
	 * @param direction the direction to set
	 */
	public void setDirection(String direction) {
		this.direction = direction;
	}
    
}
