package com.nci.ums.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2003-5-13
 * Time: 11:18:55
 * To change this template use Options | File Templates.
 */
public class UpdatePos {

	
    private void init(){
    	setupDriver();
    	Connection conn=null;
        try {
        	conn = DriverManager.getConnection("jdbc:apache:commons:dbcp:kpi");
			Document dom = XmlUtil.loadXmlFromFile("e:/POS.xml");
			
			//获取触发器列表
			if(dom!=null){
				NodeList nodeList = dom.getElementsByTagName("POSITIONS");
				if (nodeList != null) {
					int nodeLength= nodeList.getLength();
					Statement stmt=conn.createStatement();
					for (int i = 0; i < nodeLength; i++) {
						Element node = (Element) nodeList.item(i);
						String pos_id=XmlUtil.getValueFromElement(node,"OBJID");
						String belong_id=XmlUtil.getValueFromElement(node,"BELONGED_ORG");
						StringBuffer sql=new StringBuffer("update pos set belonged_org='").append(belong_id).append("' where objid='").append(pos_id).append("'");
						stmt.executeUpdate(sql.toString());
					}
				}
			}
        	
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	if(conn!=null)try{conn.close();}catch(Exception ex){}
        }
    }
    
	private  void setupDriver() {
		System.setProperty("jdbc.drivers", "com.microsoft.jdbc.sqlserver.SQLServerDriver");
	    ObjectPool connectionPool = new GenericObjectPool(null);
	    ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
	    		"jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=kpi", "sa","741218");

	    try {
	      PoolableConnectionFactory poolableConnectionFactory = new
	          PoolableConnectionFactory(connectionFactory, connectionPool, null, null, false, true);
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    }
	    PoolingDriver driver = new PoolingDriver();	    
	    driver.registerPool("kpi", connectionPool);
	    
	    
	}
	



    public static void main(String[] args) {
        UpdatePos process = new UpdatePos();
        try{
            process.init();
        }catch(Exception e){}
    }


}
