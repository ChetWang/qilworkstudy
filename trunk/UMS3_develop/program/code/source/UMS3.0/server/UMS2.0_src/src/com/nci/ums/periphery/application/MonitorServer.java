/**
 * <p>Title: AppChannel.java</p>
 * <p>Description:
 *    抽象类，提供给各个应用程序拨出渠道类来继承，实现了Send接口。
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering， Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering， Ltd.</p>
 * Date        Author      Changes
 * NOV. 17 2003   张志勇        Created
 * @version 1.0
 */

package com.nci.ums.periphery.application;

import java.sql.*;

import com.nci.ums.exception.*;
import com.nci.ums.channel.channelinfo.*;
import com.nci.ums.util.*;



public class MonitorServer extends Thread {
  //全部线程退出标志（共享变量）
  protected QuitLockFlag quitFlag;
  //本线程退出标志
  protected boolean stop = false;



  /*
   * 构造函数
   */
  public MonitorServer() {
    quitFlag = QuitLockFlag.getInstance();
    Res.log(Res.INFO, "UMS2.0应用程序监控对象产生！");
  }

  public static String getOutOkTable(){
  	StringBuffer result=new StringBuffer("out_ok_");
  	try{
	  	String currentTime = Util.getCurrentTimeStr("yyyyMMddHHmmss");
	  	int month=Integer.valueOf(currentTime.substring(4,6)).intValue();
	  	int year=Integer.valueOf(currentTime.substring(0,4)).intValue();
	  	if(month<12){
	  		month=month+1;
	  	}else{
	  		year=year+1;
	  		month=1;
	  	}
	  	
	  	result=result.append(year);
	  	if(month<10){
	  		result.append("0").append(month);
	  	}else{
	  		result.append(month);
	  	}
  	}catch(Exception e){
  		e.printStackTrace();
  	}
  	return result.toString();

  }
  
	public static String getNextDate(){
  		StringBuffer result=new StringBuffer("in_ok_");
    	String currentTime = Util.getCurrentTimeStr("yyyyMMddHHmmss");
    	
    	int day=Integer.valueOf(currentTime.substring(6,8)).intValue();
    	if(day>0&&day<=10){
    		//1号至10号
    		result.append(currentTime.substring(0,6)).append("11");
    	}else if(day>=11&&day<=20){
    		//11号至20号
    		result.append(currentTime.substring(0,6)).append("21");
    	}else{
    		//21号以后
    		if(Integer.valueOf(currentTime.substring(4,6)).intValue()==12){
    			//当前月为12月，转下一年1月01日
    			result.append(Integer.valueOf(currentTime.substring(0,4)).intValue()+1).append("0101");
    		}else{
    			//当前月为10月以前，转下一月01日,另在月份前补0
    			if(Integer.valueOf(currentTime.substring(4,6)).intValue()>9){
    				result.append(currentTime.substring(0,4)).append(Integer.valueOf(currentTime.substring(4,6)).intValue()+1).append("01");
    			}else{
    				//当前月在10月以后含10月，转下一月01日
    				result.append(currentTime.substring(0,4)).append("0").append(Integer.valueOf(currentTime.substring(4,6)).intValue()+1).append("01");
    			}
    		}
    		
    	}
    	
  		return result.toString();
  	}
  
	/*
	 * 根据日期生成接收成功后的新表，平均每10天生成一个新表，表命名根据yyyymmdd格式命名
	 */
	private String getCreateSQL(String tableName){ 
		StringBuffer result=new StringBuffer();        	
        result.append("CREATE TABLE ").
		  append(tableName).
  		  append("(BatchNo varchar(14) NOT NULL default '',").
  		  append("SerialNo bigint(11) NOT NULL default '1',").
  		  append("sequenceNO bigint(11) default '0',").
  		  append("RetCode varchar(4) default '[NUL',").
  		  append("Errmsg varchar(60) default '[NULL]',").
  		  append("statusFlag bigint(11) default '0',").
  		  append("AppId varchar(12) default '[NULL]',").
  		  append("AppSerialNo varchar(35) default '[NULL]',").
  		  append("mediaID varchar(10) default '[NULL]',").
  		  append("SendId varchar(60) default '[NULL]',").
  		  append("RecvId varchar(60) default '[NULL]',").
  		  append("SubmitDate varchar(8) default '[NULL]',").
  		  append("SubmitTime varchar(6) default '[NULL]',").
  		  append("FinishDate varchar(8) default '[NULL]',").
  		  append("FinishTime varchar(6) default '[NULL]',").
  		  append("content blob,").
  		  append("Ack bigint(11) default '0',").
  		  append("Reply varchar(30) default '[NULL]',").
  		  append("msgType bigint(11) default '0',").
  		  append("subApp varchar(6) default NULL,").
  		  append("PRIMARY KEY  (BatchNo,SerialNo)").
  		  append(") ");
      		
      return result.toString();
  }
	
	/*
	 * 根据日期生成接收成功后的新表，平均每10天生成一个新表，表命名根据yyyymmdd格式命名
	 */
	private String getOutOkCreateSQL(String tableName){ 
		StringBuffer result=new StringBuffer();        	
        result.append("CREATE TABLE ").
		  append(tableName).
		  append("(BatchNo varchar(14) NOT NULL default '',").
		  append("SerialNo bigint(11) NOT NULL default '1',").
		  append("SequenceNo bigint(11) NOT NULL default '1',").
		  append("RetCode varchar(4) default '[NUL',").
		  append("Errmsg varchar(60) default '[NULL]',").
		  append("statusFlag bigint(11) default '0',").
		  append("AppId varchar(12) default '[NULL]',").
		  append("AppSerialNo varchar(35) default '[NULL]',").
		  append("MediaId char(3) default '[NU',").
		  append("SendId varchar(21) default '[NULL]',").
		  append("RecvId varchar(255) default '[NULL]',").
		  append("SubmitDate varchar(8) default '[NULL]',").
		  append("SubmitTime varchar(6) default '[NULL]',").
		  append("FinishDate varchar(8) default '[NULL]',").
		  append("FinishTime varchar(6) default '[NULL]',").
		  append("Rep bigint(11) default '0',").
		  append("doCount bigint(11) default '0',").
		  append("Priority bigint(11) default '0',").
		  append("BatchMode char(1) default '[',").
		  append("ContentMode char(1) default '[',").
		  append("content blob,").
		  append("TimeSetFlag char(1) default '[',").
		  append("SetDate varchar(8) default '[NULL]',").
		  append("SetTime varchar(6) default '[NULL]',").
		  append("InvalidDate varchar(8) default '[NULL]',").
		  append("InvalidTime varchar(6) default '[NULL]',").
		  append("Ack bigint(11) default '0',").
		  append("ReplyDes varchar(64) default '[NULL]',").
		  append("reply varchar(30) default '[NULL]',").
		  append("fee bigint(11) default '0',").
		  append("feeType bigint(11) default '0',").
		  append("subApp varchar(6) default NULL,").
		  append("msgid varchar(20) default NULL,").
		  append("PRIMARY KEY  (BatchNo,SerialNo)"). 
  		  append(") ");
      		
      return result.toString();
  }

	/*
   * 判断下一时间间隔表是否存在，若不存在，则创建新表
   */
  private void createTable(){
    ResultSet rs = null;
    DBConnect db = null;
    Statement stmt = null;
    StringBuffer sql = null;
    
    //生成表名，根据每月10天间隔起始日获得
    String tableName=getNextDate();
    try {    		
      db=new DBConnect();      
      rs = db.executeQuery("select count(*) from "+tableName);      
      rs.close();      
    }catch (Exception e) {
      //若发生意外表示数据表不存在,则新建表
      try{
      	db.executeUpdate(getCreateSQL(tableName));
      	db.executeUpdate("CREATE INDEX "+tableName+"_appid_SubmitDate   ON "+tableName+"(appid,submitDate)");      	
      	db.executeUpdate("CREATE INDEX "+tableName+"_appid  ON "+tableName+"(appid)");
      	//若新建成功，则发送消息
      	sendMsg("新建表",tableName+"表新建成功!");
      	Res.log(Res.INFO,"create table success("+tableName);
      }catch(Exception ex){
      }
      
    }finally {
      try {
        if (rs != null)
          rs.close();
        if (db != null)
          db.close();
      }catch (Exception e) {
        //e.printStackTrace();        
      }
    }
  	
  }

	/*
   * 判断下一时间间隔表是否存在，若不存在，则创建新表
   */
  private void createOutOkTable(){
    ResultSet rs = null;
    DBConnect db = null;
    Statement stmt = null;
    StringBuffer sql = null;
    
    //生成表名，根据每月10天间隔起始日获得
    String tableName=getOutOkTable();    
    try {    		
      db=new DBConnect();      
      rs = db.executeQuery("select count(*) from "+tableName);      
      rs.close();      
    }catch (Exception e) {
      //若发生意外表示数据表不存在,则新建表
      try{
      	db.executeUpdate(getOutOkCreateSQL(tableName));
      	//若新建成功，则发送消息
      	db.executeUpdate("CREATE INDEX "+tableName+"_appid_SubmitDate   ON "+tableName+"(appid,submitDate)");      	
      	db.executeUpdate("CREATE INDEX "+tableName+"_appid  ON "+tableName+"(appid)");
      	
      	sendMsg("新建表",tableName+"表新建成功!");
      	Res.log(Res.INFO,"create table success("+tableName);
      }catch(Exception ex){}
      
    }finally {
      try {
        if (rs != null)
          rs.close();
        if (db != null)
          db.close();
      }catch (Exception e) {
        //e.printStackTrace();        
      }
    }
  	
  }

  public void run() {
    //先把退出标志设置为false;
    stop=false;

    while (!Thread.interrupted() && !quitFlag.getLockFlag() && !stop) {
      try {
          //裉始化应用程序注册信息表
//        Res.log(Res.DEBUG,"UMS报告待发情况");

          //扫描需发送至应用程序的信息
        scanTable();
        createTable();
        createOutOkTable();
//        Res.log(Res.DEBUG,"UMS报告待发情况结束");
        sleepTime(60*60);
      }catch (UMSConnectionException e) {
        e.printStackTrace();
      }
    }
    //把状态设置为true,表示已经退出了线程,以便于后来的查询线程状态。
  }

  /*
   * 扫描数据库表的函数，提供数据集
   * 这个函数主要扫描的是没有失效的，需要发送的信息,不扫描普通信息。
   * 根据参数scanType的不同，做不同的扫描
   * scanType=1 扫描及时信息， scanType=2扫描定时发送信息，
   * 根据渠道号和服务号一起扫描，防止出现已经失效了的服务的信息被扫描出来，并按照流水号来排序。
   * 做完扫描以后，调用process函数，进行处理。
   */

  public void scanTable() throws UMSConnectionException {
    //从连接池中得到连接

    ResultSet rs = null;
    Connection conn = null;
    Statement stmt = null;
    StringBuffer sql = null;
    int i = 0;

//    Res.log(Res.INFO, "待发数据的扫描！");

    sql = new StringBuffer("select * from out_ready order by serialNO desc");

//    Res.log(Res.DEBUG, "sql=" + sql);

    try {
      conn = DriverManager.getConnection(DataBaseOp.getPoolName());
      stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                    ResultSet.CONCUR_READ_ONLY);
      rs = stmt.executeQuery("select count(*) from out_ready where doCount<4 and statusflag=0 and timesetflag = 0");
      if(rs.next())
      {
          int ready_count=rs.getInt(1);
          if(ready_count>2000){
            sendMsg("3001","SMS短信中心可能发生阻塞或崩溃，请检查原因!");
          }
      }
      rs.close();
    }catch (SQLException e) {
      e.printStackTrace();
      Res.log(Res.ERROR, "1091", e.getMessage());
      Res.logExceptionTrace(e);
    }
    finally {
      try {
        if (rs != null)
          rs.close();
        if (stmt != null)
          stmt.close();
        if (conn != null)
          conn.close();
      }catch (SQLException e) {
        e.printStackTrace();
        Res.log(Res.ERROR, "1091", e.getMessage());
        Res.logExceptionTrace(e);
      }
    }
  }


  /*
   * 返回线程的状态，如果返回为true 表示现成还在运行中，如果为false,表示线程已经停止。
   */
  public boolean getThreadState() {
    return !stop;
  }

  /*
   * 发送信息的函数
   */
  public int sendMsg(String title,String msg) throws UMSConnectionException {
    boolean result=false;
    try{
        AppInfo appInfo=new AppInfo();
        appInfo.setEmail(PropertyUtil.getUMSAdminEmail());
        Send send=null;
        //构造发给应用程序数据的对象
        send = new EMailAppChannel();
        send.initInterface(appInfo);
        result=send.receiveMessage("","",title,"",0,"","","","","UMS2.0监控通知您："+msg);
    }catch(Exception e){
        Res.log(Res.ERROR,"sendMsg error:"+e);
        Res.logExceptionTrace(e);
        throw new UMSConnectionException();
    }

    if(result)
        return 0;
      else
        return 1;
  }



    /*
     sleep 一段时间
    */
     private void sleepTime(int sleepTime)
     {
         try {
             Thread.sleep(sleepTime * 1000);
//             Res.log(Res.INFO, "线程睡眠"+sleepTime+"秒!");
         }catch (InterruptedException e) {
//             e.printStackTrace();
             Res.log(Res.INFO, "线程被中断");
             pleaseStop();;
         }
     }
   /*
    * 停止这个线程
    */
   public void pleaseStop() {
       this.stop = true;              // Set the stop flag
       this.interrupt();
       Res.log(Res.INFO,  "应用程序外拨渠道线程退出服务！");
   }

    public static void main(String[] args) throws UMSConnectionException {
        MonitorServer monitorServer = new MonitorServer();
        monitorServer.sendMsg("xx", "msg");
    }

}