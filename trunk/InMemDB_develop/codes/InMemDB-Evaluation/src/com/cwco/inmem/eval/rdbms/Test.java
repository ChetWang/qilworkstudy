package com.cwco.inmem.eval.rdbms;

import java.sql.SQLException;

import org.h2.tools.Server;

public class Test {

	
	public static void mian(String... a){
		try {
//			 server = Server.createWebServer(
//			 new String[] { "-webPort", "8889", "-tcpPort", "10000" })
//			 .start();
			Server server = Server.createTcpServer(new String[] {"-tcpPort", "10000"}).start();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
