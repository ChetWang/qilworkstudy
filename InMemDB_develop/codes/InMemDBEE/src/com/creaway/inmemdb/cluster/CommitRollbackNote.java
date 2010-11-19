/*
 * @(#)CommitRollbackNote.java	1.0  09/01/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.cluster;

import java.io.Serializable;

/**
 * 用于提交事务通知的集群消息对象
 * 
 * @author Qil.Wong
 * 
 */
public class CommitRollbackNote implements Serializable {


	private static final long serialVersionUID = 5656356517289403087L;

	/**
	 * 集群中对应同一事务的连接序号
	 */
	private int sessionNo;

	/**
	 * commmit标记
	 */
	public static final int COMMIT = 1;

	/**
	 * rollback标记
	 */
	public static final int ROLLBACK = 2;

	/**
	 * 通知类型
	 */
	private int type;

	public CommitRollbackNote() {

	}

	public CommitRollbackNote(int sessionNo, int type) {
		this.sessionNo = sessionNo;
		this.type = type;
	}

	/**
	 * 获取集群主连接序号
	 * 
	 * @return 主连接序号
	 */
	public int getSessionNo() {
		return sessionNo;
	}

	/**
	 * 设置集群主连接序号
	 * 
	 * @param sessionNo
	 *            主连接序号
	 */
	public void setSessionNo(int sessionNo) {
		this.sessionNo = sessionNo;
	}

	/**
	 * 获取通知类型
	 * 
	 * @return 通知类型
	 * @see CommitRollbackNote.COMMIT
	 * @see CommitRollbackNote.ROLLBACK
	 */
	public int getType() {
		return type;
	}

	/**
	 * 设置通知类型
	 * 
	 * @param type通知类型
	 * @see CommitRollbackNote.COMMIT
	 * @see CommitRollbackNote.ROLLBACK
	 */
	public void setType(int type) {
		this.type = type;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Master Connection NO.=").append(sessionNo);
		sb.append("  ").append("Type=");
		switch (type) {
		case COMMIT:
			sb.append("COMMIT");
			break;
		default:
			sb.append("ROLLBACK");
			break;
		}
		return sb.toString();
	}

}
