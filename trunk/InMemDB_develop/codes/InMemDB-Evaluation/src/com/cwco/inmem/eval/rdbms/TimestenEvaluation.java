package com.cwco.inmem.eval.rdbms;


public class TimestenEvaluation extends RDBMSEvaluation {

	public TimestenEvaluation(int concurrencySize) {
		super(concurrencySize);
		TEST_TABLE_NAME = "Timesten";
	}

	@Override
	public  String[] getDriverInfo() {
		// TODO Auto-generated method stub
		return new String[] { "com.timesten.jdbc.TimesTenDriver",
				"jdbc:timesten:client:ttc_server=localhost;ttc_server_dsn=my_ttdb;", "tt_qil", "tt_qil" };
	}

}
