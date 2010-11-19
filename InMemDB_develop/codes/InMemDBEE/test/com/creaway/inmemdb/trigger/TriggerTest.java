package com.creaway.inmemdb.trigger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CountDownLatch;

import javax.management.ObjectName;
import javax.sql.rowset.CachedRowSet;

import com.creaway.inmemdb.core.InMemDBServer;
import com.creaway.inmemdb.core.InMemDBServerH2;
import com.creaway.inmemdb.util.ConnectionManager;
import com.sun.rowset.CachedRowSetImpl;

public class TriggerTest implements TriggerTestMBean {

	public static void main(String[] xxx) throws Exception {
		new InMemDBServerH2().startModule(null);
		InMemDBServer.getInstance().registerMBean(new TriggerTest(),
				new ObjectName(InMemDBServerH2.MBEAN_PREFIX + "TriggerTest"));

		new TriggerTest().startTest();
//		for(int i=0;i<200000;i++){
//			System.out.println("this is "+i);
//		}

	}

	public void startTest() {

		int threadCount = 30;
		int rowsPerThread = 1000;
		long t1 = System.nanoTime();
		String[] tableNames = new String[] { "tttt"};
		for (String tableName : tableNames) {
			CountDownLatch cdl = new CountDownLatch(threadCount);
			for (int i = 0; i < threadCount; i++) {
				new CThread(i, rowsPerThread, cdl, tableName).start();
			}
			try {
				cdl.await();
				System.out.println("Thread: " + threadCount +", table:"+tableName+ ", Total Rows: "
						+ threadCount * rowsPerThread + ", spent:"
						+ (System.nanoTime() - t1) / 1000000 + " ms");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			t1 = System.nanoTime();
			select(tableName);
			System.out.println("SLEECT " + tableName + " spent:"
					+ (System.nanoTime() - t1) / 1000000 + " ms");
		}

	}

	private static void select(String tableName) {
		// Connection conn = ConnectionManager.getConnection();
		Connection conn = InMemDBServer.getInstance().createDataBaseSession()
				.createConnection();
		try {
			CachedRowSet crs = new CachedRowSetImpl();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT TOP 2000 * FROM "
					+ tableName + " order by II ");
			crs.populate(rs);
			rs.close();
			st.close();
			conn.close();
			// // System.out.println(crs);
			// while (crs.next()) {
			// System.out.println("SELECT TEST:" + crs.getInt(1));
			// }
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static class CThread extends Thread {

		private int index;
		CountDownLatch cdl;
		int count;
		String tableName;

		public CThread(int index, int count, CountDownLatch cdl,
				String tableName) {
			this.index = index;
			this.cdl = cdl;
			this.count = count;
			this.tableName = tableName;
		}

		public void run() {
			Connection conn = ConnectionManager.getConnection();
			try {
				Statement st = conn.createStatement();
				for (int i = 0; i < count; i++) {

					st.execute("insert into " + tableName
							+ " (ii,aa,bb) values(" + (count * index + i)
							+ ",'aaa','bbbb')");

				}
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {

					conn.commit();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				cdl.countDown();
				ConnectionManager.releaseConnection(conn);
			}
		}
	}

}
