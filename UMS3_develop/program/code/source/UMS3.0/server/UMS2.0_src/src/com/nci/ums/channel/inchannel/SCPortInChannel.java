/**
 * <p>Title: </p>
 * <p>Description:
 *    
 * </p>
 * <p>Copyright: 2006 Hangzhou NCI System Engineering， Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering， Ltd.</p>
 * Date            Author          Changes          Version
   2006-9-22          荣凯           Created           1.0
 */
package com.nci.ums.channel.inchannel;

import java.sql.ResultSet;

import com.nci.scport.ums.*;
import com.nci.ums.channel.channelinfo.MediaInfo;
import com.nci.ums.util.Res;

public class SCPortInChannel extends InChannel{

	  private MediaInfo mediaInfo;



	  public SCPortInChannel(MediaInfo mediaInfo) {
	    super(mediaInfo);
	    this.mediaInfo = mediaInfo;
	  }

	public void run() {
	    setIsQuit(false);
	    startServer();
	    while (!Thread.interrupted() && !quitFlag.getLockFlag() && !isQuit) {
	    	listen();
	      try {
	    	  Thread.sleep(3*1000);
		} catch (InterruptedException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
	    }
	    closeServer();
	    setIsQuit(true);
	  }

	  /*
	   * 建立服务器的函数
	   * 如果线程断开或者程序退出标志被设置或者线程推出标志被设置的时候，直接退出连接。
	   * 如果在前面没有发生的情况下，服务断口建立好，也退出循环。
	   */
	  public void startServer() {

	  }

	  /*
	   * 关闭服务器的函数
	   */
	  public void closeServer() {
	  }

	  /*
	   * 服务器进行侦听的函数
	   */
	  public void listen() {
	    int result=0;
	    SCSmsConnect dbconnect=null;

	    try {
	    	dbconnect=new SCSmsConnect();
	    	
	    	if(dbconnect!=null){
	    		//dbconnect.prepareStatement("update zhangfan.SM_DELIVER set status=2 where (status=0 or status=1) and ORGADDR=? and DESTADDR=? and MSG=?");
	    		
	    		ResultSet rs=dbconnect.executeQuery("select * from moqueue");
	    		Res.log(Res.DEBUG,"建立链接");
	    		while(rs.next()){
	    			String content=rs.getString("msgcontent");
	    			String orgaddr=rs.getString("srctermid").trim();
	    			String destaddr=rs.getString("desttermid").trim();
	    			int msgIndex = rs.getInt("ID");
	                //插入数据库表中。
	    	        insertTable(0,orgaddr,destaddr, content,
	    	                        mediaInfo.getMediaId(),"",1);            
	    	        Res.log(Res.DEBUG,"写接收数据:"+content);
	    	        SCSmsConnect tmp=new SCSmsConnect();
	    	        tmp.executeUpdate("delete From moqueue where ID=" + msgIndex);
	    	        result = result + 1;
	    	        if(result>30)
	    	        	break;
	    		}
	    	}
	    }
	    catch (Exception e) {
	      //e.printStackTrace();
	      e.printStackTrace();
	      Res.log(Res.ERROR,"接收连接错误！client断开连接");
	      return;
	    }finally{
	    	if(dbconnect!=null)
				try {
					dbconnect.close();
				} catch (Exception e1) {
					// TODO 自动生成 catch 块
					e1.printStackTrace();
				}
	    }
	  }
}
