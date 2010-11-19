/*
 * @(#)ClusterTrigger.java	1.0  08/23/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.persistent;

import java.sql.SQLException;
import java.util.Map;

import com.creaway.inmemdb.InMemSession;
import com.creaway.inmemdb.cluster.DBOperationNote;
import com.creaway.inmemdb.core.InMemDBServer;
import com.creaway.inmemdb.trigger.InMemDBTrigger;

/**
 * 内存数据库集群触发器，用于在集群状况下的数据通知处理
 * 
 * @author Qil.Wong
 * 
 */
public class BackupTrigger extends InMemDBTrigger {

	public BackupTrigger() {
	}

	@Override
	public void fireTrigger(InMemSession session, Object[] oldRow, Object[] newRow)
			throws SQLException {
		if (!session.isSlave()) { //只有在ClusterDBOperationHandler,和restore中产生的session，才是slave session
			
			DBOperationNote operNote = new DBOperationNote();
			operNote.setNewValue(newRow);
			operNote.setOldValue(oldRow);
			operNote.setTableName(tableName);
			operNote.setType(type);
			operNote.setSessionNO(session.getId());
			PersistentBackupRestoreIfc backupRestore = (PersistentBackupRestoreIfc) InMemDBServer.getInstance().getModule(
					PersistentBackupRestoreIfc.class);
			// 在触发器工作的时候，存储活动session；在commit/rollback时，移除活动session
			//更好的设计是将backuprestore和cluster使用的idleSessions放在两个map中，这里的话由于是同步统一，用一个共享的map即可
			Map<Integer, InMemSession> idleSessions = InMemDBServer.getInstance()
					.getIdleMasterDataBaseSessions();
			if (!idleSessions.containsKey(session.getId())) {
				idleSessions.put(session.getId(), session);
			}
			backupRestore.insertSignal(operNote);			
		}
	}
}
