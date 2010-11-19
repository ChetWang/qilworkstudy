package com.cwco.inmem.eval.rdbms;


public class DerbyEvaluation extends RDBMSEvaluation {

	public DerbyEvaluation(int concurrencySize) {
		super(concurrencySize);
		db_type = "Derby";
	}


	@Override
	public String[] getDriverInfo() {
		return new String[] { "org.apache.derby.jdbc.EmbeddedDriver",
				"jdbc:derby:memory:myDB;create=true", "", "" };
	}
}
