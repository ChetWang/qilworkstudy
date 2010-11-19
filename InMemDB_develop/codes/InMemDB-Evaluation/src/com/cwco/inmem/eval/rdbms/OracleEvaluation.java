package com.cwco.inmem.eval.rdbms;


public class OracleEvaluation extends RDBMSEvaluation {

	public OracleEvaluation(int concurrencySize) {
		super(concurrencySize);
		db_type = "Oracle 11g";
	}

	@Override
	public String[] getDriverInfo() {
		String[] info = { "oracle.jdbc.driver.OracleDriver",
				"jdbc:oracle:thin:@127.0.0.1:1521:qil", "inmemevaluation",
				"inmemevaluation" };
		return info;
	}


}
