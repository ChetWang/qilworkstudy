package com.creaway.inmemdb.table;

import com.creaway.inmemdb.core.InMemDBServer;

public class TableSupportTest2 {

	public static void main(String[] xx) {
		InMemDBServer s = InMemDBServer.getInstance();
		s.startModule(null);
		TableSupport tb = new TableSupport();
		tb.startModule(null);
	}

}
