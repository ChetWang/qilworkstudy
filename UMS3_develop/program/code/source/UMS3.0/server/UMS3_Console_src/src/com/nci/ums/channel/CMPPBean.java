package com.nci.ums.channel;

import com.commerceware.cmpp.CMPP;
import com.commerceware.cmpp.conn_desc;
/**
 * �򵥵�CMPP pojo������Ҫ�������CMPP����͸ö����Ӧ��conn_desc
 * @author Qil.Wong
 *
 */
public class CMPPBean {

	private CMPP cmpp;
	private conn_desc conn_desc;
	/**
	 * ��ȡCMPP����
	 * @return the cmpp
	 */
	public CMPP getCmpp() {
		return cmpp;
	}
	/**
	 * ����CMPP�����ֵ
	 * @param cmpp the cmpp to set
	 */
	public void setCmpp(CMPP cmpp) {
		this.cmpp = cmpp;
	}
	/**
	 * ��ȡCMPP�����Ӧ������conn_desc
	 * @return the conn_desc
	 */
	public conn_desc getConn_desc() {
		return conn_desc;
	}
	/**
	 * ����conn_desc��Ӧ��ֵ
	 * @param conn_desc the conn_desc to set
	 */
	public void setConn_desc(conn_desc conn_desc) {
		this.conn_desc = conn_desc;
	}

}
