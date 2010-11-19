/*
 * @(#)InMemDBReadOnlyRowSet.java	1.0  08/24/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.api;

import java.sql.SQLException;
import java.util.Hashtable;

import com.sun.rowset.WebRowSetImpl;

/**
 * 只允许读数据的RowSet
 * @author Qil.Wong
 *
 */
public class InMemDBReadOnlyRowSet extends WebRowSetImpl {

	public InMemDBReadOnlyRowSet() throws SQLException {
		super();
	}

	public InMemDBReadOnlyRowSet(Hashtable hashtable) throws SQLException {
		super(hashtable);
	}

	public void deleteRow() throws SQLException {
		// DO NOTHING
	}

	public void insertRow() throws SQLException {
		// DO NOTHING
	}

	public void updateRow() throws SQLException {
		// DO NOTHING
	}

}
