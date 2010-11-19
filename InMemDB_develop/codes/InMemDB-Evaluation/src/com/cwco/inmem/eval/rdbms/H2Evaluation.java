package com.cwco.inmem.eval.rdbms;

import java.sql.SQLException;

import org.h2.tools.Server;

public class H2Evaluation extends RDBMSEvaluation {

	Server server;

	public H2Evaluation(int concurrencySize) {
		super(concurrencySize);
		db_type = "H2";
	}

	protected void init() {
		try {
			 server = Server.createWebServer(
			 new String[] { "-webPort", "8889" })
			 .start();
//			server = Server.createTcpServer(new String[] {"-tcpPort", "10000"}).start();
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		Test.mian();
//		ProcessBuilder p = new ProcessBuilder();
//		try {
//			Properties sysP = System.getProperties();
//			p.command(sysP.getProperty("java.home")+"/bin/java"," -cp "+sysP.getProperty("java.class.path"),Test.class.getName());
//			Process pcs = p.start();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		super.init();
	}

	@Override
	public String[] getDriverInfo() {
//		 without MVCC=TRUE, the database is table lock level
		String[] info = { "org.h2.Driver", "jdbc:h2:mem:test;MVCC=TRUE", "sa",
				"" };
//		String[] info = { "org.h2.Driver",
//				"jdbc:h2:tcp://localhost:10000/mem:test;MVCC=TRUE;DB_CLOSE_DELAY=-1",
//				"sa", "" };

		return info;
	}

	public void shutdown() {
		// server.stop();
	}

}
