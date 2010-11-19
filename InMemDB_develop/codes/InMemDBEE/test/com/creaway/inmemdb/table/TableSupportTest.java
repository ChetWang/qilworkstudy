package com.creaway.inmemdb.table;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.creaway.inmemdb.core.InMemDBServer;

public class TableSupportTest {
	TableSupport tb;
	InMemDBServer s;

	public TableSupportTest() {
		if (s == null) {
			s = InMemDBServer.getInstance();
			s.startModule(null);
		}
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {

		tb = new TableSupport();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testStartModule() {

		tb.startModule(null);
	}

	@Test
	public void testShutdownModule() {
		tb.startModule(null);
		tb.shutdownModule(null);
	}

}
