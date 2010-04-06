package com.nci.ums.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;


/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2003-5-13
 * Time: 11:18:55
 * To change this template use Options | File Templates.
 */
public class BatchUpdate  extends Thread{
	private int beginPos=1012000;
	private int count;
	
     
	public void run() {
    	
        while(true) {
        	
           try{
           		if(beginPos<=count){
           			process(beginPos);           			
           		}else{
           			break;
           		}
            }catch(Exception e){
            	
            }finally{
            	beginPos=beginPos+1001;
            	try{Thread.sleep(5*1000);}catch(Exception ex){}
            }
        }
        System.out.println(beginPos);
    // 关闭
    }
	
    private void init(){
    	setupDriver();
    	Connection conn=null;
        try {
        	conn = DriverManager.getConnection("jdbc:apache:commons:dbcp:ums02");
        	ResultSet rs=conn.createStatement().executeQuery("select count(*) as msgCount from out_ok where batchno like '200507%'");
        	if(rs.next()){
        		count=rs.getInt("msgCount");
        	}
        	System.out.println(count);
        }catch(Exception e){
        }finally{
        	if(conn!=null)try{conn.close();}catch(Exception ex){}
        }
    }
    
	private  void setupDriver() {
		System.setProperty("jdbc.drivers", "com.mysql.jdbc.Driver");
	    ObjectPool connectionPool = new GenericObjectPool(null);
	    ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
	    		"jdbc:mysql://localhost:3306/ums?useUnicode=true&characterEncoding=gb2312", "root","xh3s9ibm87");

	    try {
	      PoolableConnectionFactory poolableConnectionFactory = new
	          PoolableConnectionFactory(connectionFactory, connectionPool, null, null, false, true);
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    }
	    PoolingDriver driver = new PoolingDriver();	    
	    driver.registerPool("ums02", connectionPool);
	    
	    
	}
	
    public void process(int beginPos) {
    	Connection conn = null;
    	
        try {
        	conn = DriverManager.getConnection("jdbc:apache:commons:dbcp:ums02");

            ResultSet rs=conn.createStatement().executeQuery("select * from out_ok where batchno like '200507%' limit "+beginPos+",1000");
            
            
            Statement stmt=conn.createStatement();
            
            while(rs.next()){
            	StringBuffer insertSQL=new StringBuffer("insert into out_ok_200507(BatchNo,SerialNo,SequenceNo,retCode,errMsg,StatusFlag,AppId,AppSerialNo,MediaId,SendId,RecvId,SubmitDate,SubmitTime,finishDate,finishTime,rep,doCount,priority,BatchMode,ContentMode,Content,TimeSetFlag,SetDate,SetTime,InvalidDate,InvalidTime, ack, replyDes,reply,feeType,fee,subApp,msgID) values(");
            	insertSQL.append("'").append(rs.getString("batchNO")).append("',").
            		append(rs.getInt("SerialNo")).append(",").
            		append(rs.getInt("SequenceNo")).append(",'").
					append(rs.getString("retCode")).append("','").
            		append(rs.getString("errMsg")).append("',").
					append(rs.getString("StatusFlag")).append(",'").
            		append(rs.getString("AppId")).append("','").
					append(rs.getString("AppSerialNo")).append("','").
            		append(rs.getString("MediaId")).append("','").
            		append(rs.getString("SendId")).append("','").
					append(rs.getString("RecvId")).append("','").            	            	
            		append(rs.getString("SubmitDate")).append("','").
					append(rs.getString("SubmitTime")).append("','").
					append(rs.getString("finishDate")).append("','").
					append(rs.getString("finishTime")).append("',").
            		append(rs.getInt("rep")).append(",").
					append(rs.getInt("doCount")).append(",").
					append(rs.getInt("priority")).append(",").
					append(rs.getInt("BatchMode")).append(",").
					append(rs.getInt("ContentMode")).append(",'").
					append(rs.getString("Content")).append("',").            	
					append(rs.getInt("TimeSetFlag")).append(",'").
					append(rs.getString("SetDate")).append("','").
					append(rs.getString("SetTime")).append("','").
					append(rs.getString("InvalidDate")).append("','").
					append(rs.getString("InvalidTime")).append("',").
					append(rs.getInt("ack")).append(",'").
					append(rs.getString("replyDes")).append("','").
					append(rs.getString("reply")).append("',").            	
					append(rs.getInt("fee")).append(",").
					append(rs.getInt("feeType")).append(",'").
					append(rs.getString("subApp")).append("','").
					append(rs.getString("msgID")).append("')");
            	
            		stmt.executeUpdate(insertSQL.toString());
            	
            }
                     
        }catch (Exception ex) {
        	ex.printStackTrace();
            System.out.println("处理交易1002出错:"+ex.toString());
        }finally {
        	if(conn!=null)try{conn.close();}catch(Exception e){}
        }
    }


    public static void main(String[] args) {
        BatchUpdate process = new BatchUpdate();
        try{
            process.init();
            process.start();
        }catch(Exception e){}
    }


}
