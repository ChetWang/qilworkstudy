
package com.nci.svg.sdk.bean;

import java.io.Serializable;

/**
 * <p>��˾��Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author yx.nci
 * @ʱ�䣺2009-1-7
 * @���ܣ�������Ϣʵ����
 *
 */
public class TopologyBean implements Serializable {
	private static final long serialVersionUID = -6240437900110008517L;
	/**
     * add by yux,2009-1-7
     * ��ʼ��ҵ����
     */
    private String beginID;
    /**
     * add by yux,2009-1-7
     * ��ֹ��ҵ����
     */
    private String endID;
    /**
     * add by yux,2009-1-7
     * ����
     */
    private String direction;
	/**
	 * ������ʼ��ҵ����
	 * @return the beginID
	 */
	public String getBeginID() {
		return beginID;
	}
	/**
	 * ������ʼ��ҵ����
	 * @param beginID the beginID to set
	 */
	public void setBeginID(String beginID) {
		this.beginID = beginID;
	}
	/**
	 * ������ֹ��ҵ����
	 * @return the endID
	 */
	public String getEndID() {
		return endID;
	}
	/**
	 * ������ֹ��ҵ����
	 * @param endID the endID to set
	 */
	public void setEndID(String endID) {
		this.endID = endID;
	}
	/**
	 * ���ط���
	 * @return the direction
	 */
	public String getDirection() {
		return direction;
	}
	/**
	 * ���÷���
	 * @param direction the direction to set
	 */
	public void setDirection(String direction) {
		this.direction = direction;
	}
    
}
