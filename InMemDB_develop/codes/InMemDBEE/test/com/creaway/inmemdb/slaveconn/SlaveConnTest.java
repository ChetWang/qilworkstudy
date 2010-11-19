package com.creaway.inmemdb.slaveconn;

import java.sql.Connection;
import java.sql.ResultSet;

import com.creaway.inmemdb.api.InMemDBReadOnlyRowSet;
import com.creaway.inmemdb.cluster.SlaveConnectionFactory;
import com.creaway.inmemdb.core.InMemDBServerH2;
import com.creaway.inmemdb.trigger.TriggerTest;

public class SlaveConnTest {

	public void test() throws Exception {
		TriggerTest h2 = new TriggerTest();
		h2.startTest();
		Thread.sleep(1000);
		Connection conn = SlaveConnectionFactory.getSlaveConnection();
		System.out.println(conn.getClass());
		ResultSet rs = conn.createStatement().executeQuery(
				"select * from tttt where ii<10");
		InMemDBReadOnlyRowSet rowset = new InMemDBReadOnlyRowSet();
		rowset.populate(rs);
		rowset.writeXml(System.out);
		conn.close();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		new InMemDBServerH2().startModule(null);
		new SlaveConnTest().test();
	}

}
