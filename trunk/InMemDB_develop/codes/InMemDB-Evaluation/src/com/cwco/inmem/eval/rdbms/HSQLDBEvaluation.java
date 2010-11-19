package com.cwco.inmem.eval.rdbms;


public class HSQLDBEvaluation extends RDBMSEvaluation {

	public HSQLDBEvaluation(int concurrencySize) {
		super(concurrencySize);
		db_type = "HSQLDB";
		// Server server = new Server();
		// server.setPort(11111);
		// server.start();
	}

	@Override
	public String[] getDriverInfo() {
		String[] info = { "org.hsqldb.jdbcDriver", "jdbc:hsqldb:mem:testdb",
				"sa", "" };
		return info;
	}


}
