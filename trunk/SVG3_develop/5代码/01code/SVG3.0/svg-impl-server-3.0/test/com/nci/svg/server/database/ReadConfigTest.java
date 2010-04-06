package com.nci.svg.server.database;

import java.util.ArrayList;

import junit.framework.TestCase;

import com.nci.svg.server.bean.ConnBean;
import com.nci.svg.server.service.ServiceModuleManagerImpl;

public class ReadConfigTest extends TestCase {
	private DBConnectionManagerImpl DbPool;
	
	private void Display(ArrayList pools){
		for (int i = 0, size = pools.size(); i < size; i++) {
			ConnBean cb = (ConnBean) pools.get(i);
			System.out.println("pool name:" + cb.getName());
			System.out.println("user name:" + cb.getUsername());
			System.out.println("password:" + cb.getPassword());
			System.out.println("url:" + cb.getJdbcurl());
			System.out.println("Driver:" + cb.getDriver());
			System.out.println("wait:" + cb.getWait());
			System.out.println("max connection:" + cb.getMax());
		}
	}
	
	public void testRead(){
//		ServiceModuleManagerImpl smmi = new ServiceModuleManagerImpl();
//		DbPool = (DBConnectionManagerImpl) smmi.getDBManager();
//		ArrayList pools = DbPool.read();
		
//		ArrayList pools = ReadConfig.read();
		
//		Display(pools);
	}
}
