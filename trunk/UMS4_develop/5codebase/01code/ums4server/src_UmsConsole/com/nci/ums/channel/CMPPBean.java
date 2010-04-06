package com.nci.ums.channel;

import com.commerceware.cmpp.CMPP;
import com.commerceware.cmpp.conn_desc;
/**
 * 简单的CMPP pojo对象，主要用来存放CMPP对象和该对象对应的conn_desc
 * @author Qil.Wong
 *
 */
public class CMPPBean {

	private CMPP cmpp;
	private conn_desc conn_desc;
	/**
	 * 获取CMPP对象
	 * @return the cmpp
	 */
	public CMPP getCmpp() {
		return cmpp;
	}
	/**
	 * 设置CMPP对象的值
	 * @param cmpp the cmpp to set
	 */
	public void setCmpp(CMPP cmpp) {
		this.cmpp = cmpp;
	}
	/**
	 * 获取CMPP对象对应的链接conn_desc
	 * @return the conn_desc
	 */
	public conn_desc getConn_desc() {
		return conn_desc;
	}
	/**
	 * 设置conn_desc对应的值
	 * @param conn_desc the conn_desc to set
	 */
	public void setConn_desc(conn_desc conn_desc) {
		this.conn_desc = conn_desc;
	}

}
