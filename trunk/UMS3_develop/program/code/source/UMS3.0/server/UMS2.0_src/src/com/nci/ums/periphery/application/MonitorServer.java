/**
 * <p>Title: AppChannel.java</p>
 * <p>Description:
 *    �����࣬�ṩ������Ӧ�ó��򲦳����������̳У�ʵ����Send�ӿڡ�
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering�� Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering�� Ltd.</p>
 * Date        Author      Changes
 * NOV. 17 2003   ��־��        Created
 * @version 1.0
 */

package com.nci.ums.periphery.application;

import java.sql.*;

import com.nci.ums.exception.*;
import com.nci.ums.channel.channelinfo.*;
import com.nci.ums.util.*;



public class MonitorServer extends Thread {
  //ȫ���߳��˳���־�����������
  protected QuitLockFlag quitFlag;
  //���߳��˳���־
  protected boolean stop = false;



  /*
   * ���캯��
   */
  public MonitorServer() {
    quitFlag = QuitLockFlag.getInstance();
    Res.log(Res.INFO, "UMS2.0Ӧ�ó����ض��������");
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
    		//1����10��
    		result.append(currentTime.substring(0,6)).append("11");
    	}else if(day>=11&&day<=20){
    		//11����20��
    		result.append(currentTime.substring(0,6)).append("21");
    	}else{
    		//21���Ժ�
    		if(Integer.valueOf(currentTime.substring(4,6)).intValue()==12){
    			//��ǰ��Ϊ12�£�ת��һ��1��01��
    			result.append(Integer.valueOf(currentTime.substring(0,4)).intValue()+1).append("0101");
    		}else{
    			//��ǰ��Ϊ10����ǰ��ת��һ��01��,�����·�ǰ��0
    			if(Integer.valueOf(currentTime.substring(4,6)).intValue()>9){
    				result.append(currentTime.substring(0,4)).append(Integer.valueOf(currentTime.substring(4,6)).intValue()+1).append("01");
    			}else{
    				//��ǰ����10���Ժ�10�£�ת��һ��01��
    				result.append(currentTime.substring(0,4)).append("0").append(Integer.valueOf(currentTime.substring(4,6)).intValue()+1).append("01");
    			}
    		}
    		
    	}
    	
  		return result.toString();
  	}
  
	/*
	 * �����������ɽ��ճɹ�����±�ƽ��ÿ10������һ���±�����������yyyymmdd��ʽ����
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
	 * �����������ɽ��ճɹ�����±�ƽ��ÿ10������һ���±�����������yyyymmdd��ʽ����
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
   * �ж���һʱ�������Ƿ���ڣ��������ڣ��򴴽��±�
   */
  private void createTable(){
    ResultSet rs = null;
    DBConnect db = null;
    Statement stmt = null;
    StringBuffer sql = null;
    
    //���ɱ���������ÿ��10������ʼ�ջ��
    String tableName=getNextDate();
    try {    		
      db=new DBConnect();      
      rs = db.executeQuery("select count(*) from "+tableName);      
      rs.close();      
    }catch (Exception e) {
      //�����������ʾ���ݱ�����,���½���
      try{
      	db.executeUpdate(getCreateSQL(tableName));
      	db.executeUpdate("CREATE INDEX "+tableName+"_appid_SubmitDate   ON "+tableName+"(appid,submitDate)");      	
      	db.executeUpdate("CREATE INDEX "+tableName+"_appid  ON "+tableName+"(appid)");
      	//���½��ɹ���������Ϣ
      	sendMsg("�½���",tableName+"���½��ɹ�!");
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
   * �ж���һʱ�������Ƿ���ڣ��������ڣ��򴴽��±�
   */
  private void createOutOkTable(){
    ResultSet rs = null;
    DBConnect db = null;
    Statement stmt = null;
    StringBuffer sql = null;
    
    //���ɱ���������ÿ��10������ʼ�ջ��
    String tableName=getOutOkTable();    
    try {    		
      db=new DBConnect();      
      rs = db.executeQuery("select count(*) from "+tableName);      
      rs.close();      
    }catch (Exception e) {
      //�����������ʾ���ݱ�����,���½���
      try{
      	db.executeUpdate(getOutOkCreateSQL(tableName));
      	//���½��ɹ���������Ϣ
      	db.executeUpdate("CREATE INDEX "+tableName+"_appid_SubmitDate   ON "+tableName+"(appid,submitDate)");      	
      	db.executeUpdate("CREATE INDEX "+tableName+"_appid  ON "+tableName+"(appid)");
      	
      	sendMsg("�½���",tableName+"���½��ɹ�!");
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
    //�Ȱ��˳���־����Ϊfalse;
    stop=false;

    while (!Thread.interrupted() && !quitFlag.getLockFlag() && !stop) {
      try {
          //��ʼ��Ӧ�ó���ע����Ϣ��
//        Res.log(Res.DEBUG,"UMS����������");

          //ɨ���跢����Ӧ�ó������Ϣ
        scanTable();
        createTable();
        createOutOkTable();
//        Res.log(Res.DEBUG,"UMS��������������");
        sleepTime(60*60);
      }catch (UMSConnectionException e) {
        e.printStackTrace();
      }
    }
    //��״̬����Ϊtrue,��ʾ�Ѿ��˳����߳�,�Ա��ں����Ĳ�ѯ�߳�״̬��
  }

  /*
   * ɨ�����ݿ��ĺ������ṩ���ݼ�
   * ���������Ҫɨ�����û��ʧЧ�ģ���Ҫ���͵���Ϣ,��ɨ����ͨ��Ϣ��
   * ���ݲ���scanType�Ĳ�ͬ������ͬ��ɨ��
   * scanType=1 ɨ�輰ʱ��Ϣ�� scanType=2ɨ�趨ʱ������Ϣ��
   * ���������źͷ����һ��ɨ�裬��ֹ�����Ѿ�ʧЧ�˵ķ������Ϣ��ɨ���������������ˮ��������
   * ����ɨ���Ժ󣬵���process���������д���
   */

  public void scanTable() throws UMSConnectionException {
    //�����ӳ��еõ�����

    ResultSet rs = null;
    Connection conn = null;
    Statement stmt = null;
    StringBuffer sql = null;
    int i = 0;

//    Res.log(Res.INFO, "�������ݵ�ɨ�裡");

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
            sendMsg("3001","SMS�������Ŀ��ܷ������������������ԭ��!");
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
   * �����̵߳�״̬���������Ϊtrue ��ʾ�ֳɻ��������У����Ϊfalse,��ʾ�߳��Ѿ�ֹͣ��
   */
  public boolean getThreadState() {
    return !stop;
  }

  /*
   * ������Ϣ�ĺ���
   */
  public int sendMsg(String title,String msg) throws UMSConnectionException {
    boolean result=false;
    try{
        AppInfo appInfo=new AppInfo();
        appInfo.setEmail(PropertyUtil.getUMSAdminEmail());
        Send send=null;
        //���췢��Ӧ�ó������ݵĶ���
        send = new EMailAppChannel();
        send.initInterface(appInfo);
        result=send.receiveMessage("","",title,"",0,"","","","","UMS2.0���֪ͨ����"+msg);
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
     sleep һ��ʱ��
    */
     private void sleepTime(int sleepTime)
     {
         try {
             Thread.sleep(sleepTime * 1000);
//             Res.log(Res.INFO, "�߳�˯��"+sleepTime+"��!");
         }catch (InterruptedException e) {
//             e.printStackTrace();
             Res.log(Res.INFO, "�̱߳��ж�");
             pleaseStop();;
         }
     }
   /*
    * ֹͣ����߳�
    */
   public void pleaseStop() {
       this.stop = true;              // Set the stop flag
       this.interrupt();
       Res.log(Res.INFO,  "Ӧ�ó����Ⲧ�����߳��˳�����");
   }

    public static void main(String[] args) throws UMSConnectionException {
        MonitorServer monitorServer = new MonitorServer();
        monitorServer.sendMsg("xx", "msg");
    }

}