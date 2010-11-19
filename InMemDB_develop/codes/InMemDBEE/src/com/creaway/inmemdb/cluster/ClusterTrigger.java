/*
 * @(#)ClusterTrigger.java	1.0  08/23/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.cluster;

import java.sql.SQLException;
import java.util.Map;

import com.creaway.inmemdb.InMemSession;
import com.creaway.inmemdb.core.InMemDBServer;
import com.creaway.inmemdb.trigger.InMemDBTrigger;

/**
 * 内存数据库集群触发器，用于在集群状况下的数据通知处理
 * 
 * @author Qil.Wong
 * 
 */
public class ClusterTrigger extends InMemDBTrigger {

	private ClusterIfc cluster;

	public ClusterTrigger() {

	}

	@Override
	public void fireTrigger(InMemSession session, Object[] oldRow,
			Object[] newRow) throws SQLException {
		if (!session.isSlave()) { // 只有在ClusterDBOperationHandler中产生的session，才是slave
									// session
			if (cluster == null) {
				cluster = (ClusterIfc) InMemDBServer.getInstance().getModule(
						ClusterIfc.class);
			}
				if (cluster != null) {
					DBOperationNote operNote = new DBOperationNote();
					operNote.setNewValue(newRow);
					operNote.setOldValue(oldRow);
					operNote.setTableName(tableName);
					operNote.setType(type);
					operNote.setSessionNO(session.getId());

					// 在触发器工作的时候，存储活动session；在commit时，移除活动session
					Map<Integer, InMemSession> idleSessions = InMemDBServer
							.getInstance().getIdleMasterDataBaseSessions();
					if (!idleSessions.containsKey(session.getId())) {
						idleSessions.put(session.getId(), session);
					}
					cluster.sendDataToMembers(operNote, null);
				}
		}
	}
}
