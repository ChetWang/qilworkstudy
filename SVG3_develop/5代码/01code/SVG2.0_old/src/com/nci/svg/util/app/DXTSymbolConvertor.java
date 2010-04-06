package com.nci.svg.util.app;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 剩下的单线图的转换器
 * @author Qil.Wong
 *
 */
public class DXTSymbolConvertor {
	
	private Connection conn = null;

	
	private DXTSymbolConvertor() throws Exception{
		Class.forName("oracle.jdbc.driver.OracleDriver");
		conn = DriverManager.getConnection(
				"jdbc:oracle:thin:@10.147.218.232:1521:jxjxsc", "svgtool",
//				"jdbc:oracle:thin:@127.0.0.1:1521:qil", "svgtool",
				"svgtool");
		conn.setAutoCommit(false);
	}
	
	
}
