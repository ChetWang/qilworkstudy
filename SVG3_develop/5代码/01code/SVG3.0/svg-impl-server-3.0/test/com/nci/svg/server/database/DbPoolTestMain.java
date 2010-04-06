package com.nci.svg.server.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.nci.svg.sdk.server.database.DBConnectionManagerAdapter;
import com.nci.svg.server.ServerModuleController;


public class DbPoolTestMain extends Thread {
	private static DBConnectionManagerAdapter DbPool;
	private String sql;
	private String poolname;
	private Connection c;

	public DbPoolTestMain(String poolname, Connection c, String sql) {
		this.poolname = poolname;
		this.c = c;
		this.sql = sql;
	}

	public static void main(String[] args) {
		ServerModuleController smc = new ServerModuleController(null);
		smc.init();
		DbPool = smc.getDBManager();
		
		for (int i = 0; i < 100; i++) {
			String sql;
			sql = "SELECT COUNT(*) FROM t_svg_code_commsets";
			new DbPoolTestMain("svg", DbPool.getConnection("svg"), sql)
					.start();
			
			sql = "select count(*) from jfw_oway_data";
			new DbPoolTestMain("system", DbPool.getConnection("system"), sql).start();
			
//			DbPool.reInitPool("svg");

//			sql = "SELECT * FROM xx";
//			new Test1("mysql", DbPool.getConnection("mysql"), sql).start();
		}

	}

	public void run() {
		try {
			ResultSet rs = c.createStatement().executeQuery(sql);
			while (rs.next()) {
				System.out.println(poolname+"½á¹û£º"+rs.getString(1));
			}
			System.out.println(DbPool.getConnectionStatus("svg"));
			System.out.println(DbPool.getConnectionStatus("system"));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(c!=null)
				try {
					c.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
}
