package com.cwco.inmem.eval.rdbms;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import com.cwco.inmem.eval.Constants;
import com.cwco.inmem.eval.Evaluation;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;

public abstract class RDBMSEvaluation extends Evaluation {
	/**
	 * get the initial database driver information, include <B>driver class
	 * name</B>, <B>URl</B>, <B>user name</B>, <B>password</B> in sequence.
	 * 
	 * @return driver informations
	 */
	public abstract String[] getDriverInfo();

	protected Connection[] conns;

	protected static String TEST_TABLE_NAME = "inmemtest";

	public RDBMSEvaluation(int concurrencySize) {
		super(concurrencySize);
	}

	protected void init() {
		conns = createConnection(concurrencySize);
		try {
			createTestTable();
			// createTestTable_NoIndex();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected Connection[] createConnection(int connectionSize) {
		try {
			Connection[] conns = new Connection[connectionSize];
			String[] driverInfo = getDriverInfo();
			// Driver driver = (Driver)
			// Class.forName(driverInfo[0]).newInstance();
			// DriverManager.registerDriver(driver);
			// for (int i = 0; i < conns.length; i++) {
			// conns[i] = DriverManager.getConnection(driverInfo[1],
			// driverInfo[2], driverInfo[3]);
			// }

			// DataSource unpooled =
			// DataSources.unpooledDataSource(driverInfo[0],
			// driverInfo[1], driverInfo[2]);
			// PoolBackedDataSource pooled =(PoolBackedDataSource)
			// DataSources.pooledDataSource(unpooled);
			MLog.getLogger().setLevel(MLevel.SEVERE);
			ComboPooledDataSource p = new ComboPooledDataSource();
			p.setDriverClass(driverInfo[0]);
			p.setJdbcUrl(driverInfo[1]);
			p.setUser(driverInfo[2]);
			p.setPassword(driverInfo[3]);
			p.setMaxPoolSize(200);
			p.setMinPoolSize(1);
			p.setInitialPoolSize(10);
			p.setMaxIdleTime(60);
			for (int i = 0; i < conns.length; i++) {
				conns[i] = p.getConnection();
			}

			return conns;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * finally, close the connection, not release, it is close!!
	 */
	protected void closeConnection(Connection c) {
		if (c != null) {
			try {
				c.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * generate the sql for value column when creating the test db table
	 * 
	 * @return
	 */
	protected String generateCreateTableValueColumnSQL() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < Constants.VALUE_COLUMN_COUNT; i++) {
			sb.append(",").append("c_value").append(i).append(" varchar(255)");
		}
		return sb.toString();
	}

	protected String generateInsertValueColumnSQL() {

		StringBuffer sb = new StringBuffer();
		sb.append("insert into ").append(TEST_TABLE_NAME).append(" (c_id");
		for (int i = 0; i < Constants.VALUE_COLUMN_COUNT; i++) {
			sb.append(",").append("c_value").append(i);
		}
		sb.append(")");
		sb.append("values (");
		for (int i = 0; i < Constants.VALUE_COLUMN_COUNT + 1; i++) {
			if (i > 0)
				sb.append(",");
			sb.append("?");

		}
		sb.append(")");
		return sb.toString();
	}

	/**
	 * create the test table with no index
	 * 
	 * @throws SQLException
	 */
	protected void createTestTable_NoIndex() throws SQLException {
		Statement st = conns[0].createStatement();
		try {
			st.execute("DROP TABLE  " + TEST_TABLE_NAME);
		} catch (SQLException e) {
			// DO NOTHING -_-!
		}
		st.close();
		st = conns[0].createStatement();
		st.addBatch("CREATE TABLE " + TEST_TABLE_NAME + " (c_id int "
				+ generateCreateTableValueColumnSQL() + ")");
		st.executeBatch();
		st.close();

	}

	/**
	 * create the test table
	 * 
	 * @throws SQLException
	 */
	protected void createTestTable() throws SQLException {

		Statement st = conns[0].createStatement();
		try {
			st.execute("DROP TABLE  " + TEST_TABLE_NAME);
		} catch (SQLException e) {
			// DO NOTHING -_-!
		}
		st.close();
		st = conns[0].createStatement();
		st.addBatch("CREATE TABLE " + TEST_TABLE_NAME + " (c_id int "
				+ generateCreateTableValueColumnSQL() + " ,primary key (c_id))");
		st.executeBatch();
		st.close();

	}

	@Override
	public void insert(int counts) {
		// test for multi-connection
		// conn = createConnection();
		System.out.println(db_type + " INSERT START.   ROW COUNTS = " + counts);
		System.out.println(db_type + " TOTAL CONCURRENT THREADS = "
				+ concurrencySize);
		long t = System.nanoTime();
		CountDownLatch countDown = new CountDownLatch(concurrencySize);
		printMemStatus(db_type + " MEM STATUS WHEN STARTED: ");
		for (int i = 0; i < concurrencySize; i++) {
			new InsertThread(countDown, i, counts / concurrencySize).start();
		}
		try {
			countDown.await();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(db_type + " INSERT FINISHED.  Total time spends="
				+ (System.nanoTime() - t) / 1000000 + " ms");
		printMemStatus(db_type + " MEM STATUS WHEN FINISHED: ");
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

	@Override
	public void get(int fromIndex, int toIndex) {
		System.out.println(db_type + " SINGLE QUERY(SINGLE KEY) STARTED.");

		CountDownLatch cdl1 = new CountDownLatch(conns.length);
		CountDownLatch cdl2 = new CountDownLatch(conns.length);
		long t1 = System.nanoTime();
		for (int i = 0; i < conns.length; i++) {
			new SingleValueSearchThread(cdl1, i, fromIndex).start();
		}
		try {
			cdl1.await();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		long t2 = System.nanoTime();

		System.out.println(db_type
				+ " QUERY(SINGLE KEY) FINISHED.  Total time spends="
				+ (t2 - t1) / 1000000 + " ms");

		System.out.println(db_type + " SINGLE QUERY(COMPLEX) STARTED.");
		for (int i = 0; i < conns.length; i++) {
			new FromToSearchThread(cdl2, i, fromIndex, toIndex).start();
		}
		try {
			cdl2.await();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		long t3 = System.nanoTime();
		System.out.println(db_type
				+ " QUERY(COMPLEX) FINISHED.  Total time spends=" + (t3 - t2)
				/ 1000000 + " ms");

	}

	/**
	 * The thread for inserting data
	 * 
	 * @author Qil.Wong
	 * 
	 */
	public class InsertThread extends Thread {

		CountDownLatch cdl;
		int concurrencyIndex;
		/**
		 * total row counts
		 */
		int count;

		public InsertThread(CountDownLatch cdl, int concurrencyIndex, int count) {
			this.cdl = cdl;
			this.concurrencyIndex = concurrencyIndex;
			this.count = count;
		}

		public void run() {
			// define which table these data belongs to

			try {
				// conns[concurrencyIndex].setAutoCommit(false);
				PreparedStatement prep = conns[concurrencyIndex]
						.prepareStatement(generateInsertValueColumnSQL());

				for (int i = 0; i < count; i++) {
					Serializable[] keyValue = generateObject(concurrencyIndex
							* count + i);
					prep.setInt(1, (Integer) keyValue[0]);
					String[] value = (String[]) keyValue[1];
					for (int n = 0; n < value.length; n++) {
						prep.setString(2 + n, value[n]);
					}
					// prep.addBatch();
					prep.executeUpdate();
				}
				// prep.executeBatch();
				// auto comit ==true, no need to commit
				// conns[concurrencyIndex].commit();
				prep.close();
				conns[concurrencyIndex].setAutoCommit(true);
				cdl.countDown();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Ensure same objects to insert,contain key-value array
		 * 
		 * @return
		 */
		public Serializable[] generateObject(int index) {
			Serializable[] s = { index, createString() };
			return s;
		}

	}

	public class SingleValueSearchThread extends Thread {

		CountDownLatch cdl;
		int searchIndexValue;
		int concurrencyIndex;
		ArrayList list = new ArrayList();

		public SingleValueSearchThread(CountDownLatch cdl,
				int concurrencyIndex, int searchIndexValue) {
			this.cdl = cdl;
			this.searchIndexValue = searchIndexValue;
			this.concurrencyIndex = concurrencyIndex;
		}

		public void run() {
			try {
				PreparedStatement prep1 = conns[concurrencyIndex]
						.prepareStatement("select c_value0 from "
								+ TEST_TABLE_NAME + " where c_id="
								+ searchIndexValue);
				ResultSet rs1 = prep1.executeQuery();

				while (rs1.next()) {
					list.add(rs1.getObject(1));
				}
				rs1.close();
				prep1.close();
				cdl.countDown();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public class FromToSearchThread extends Thread {

		CountDownLatch cdl;
		int fromIndex;
		int toIndex;
		int concurrencyIndex;
		ArrayList list = new ArrayList();

		public FromToSearchThread(CountDownLatch cdl, int concurrencyIndex,
				int fromIndex, int toIndex) {
			this.cdl = cdl;
			this.fromIndex = fromIndex;
			this.toIndex = toIndex;
			this.concurrencyIndex = concurrencyIndex;
		}

		public void run() {
			try {
				String sql = "select c_value0 from " + TEST_TABLE_NAME
						+ " where c_id between " + fromIndex + " and "
						+ toIndex;
				// String sql = "select c_value from " + TEST_TABLE_NAME
				// + " where c_value like '%abcd%'";
				PreparedStatement prep2 = conns[concurrencyIndex]
						.prepareStatement(sql);
				ResultSet rs2 = prep2.executeQuery();
				while (rs2.next()) {
					list.add(rs2.getObject(1));
				}
				rs2.close();
				prep2.close();
				cdl.countDown();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

}
