/**
 * <p>Title: SGIPOutChannel.java</p>
 * <p>Description:
 *    SGIPInChannel拨入渠道类，继承InChannel
 *    实现联通信息的发送过程
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering， Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering， Ltd.</p>
 * Date        Author      Changes
 * NOV 20 2003   张志勇        Created
 * @version 1.0
 */

package com.nci.ums.channel.inchannel;


import java.sql.ResultSet;


import com.nci.nbport.ums.NBSmsConnect;
import com.nci.ums.channel.channelinfo.*;
import com.nci.ums.util.*;

public class NBPortInChannel
    extends InChannel {

  private MediaInfo mediaInfo;



  public NBPortInChannel(MediaInfo mediaInfo) {
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
    int result=-1;
    NBSmsConnect dbconnect=null;

    try {
    	dbconnect=new NBSmsConnect();
    	
    	if(dbconnect!=null){
    		dbconnect.prepareStatement("update zhangfan.SM_DELIVER set status=2 where (status=0 or status=1) and ORGADDR=? and DESTADDR=? and MSG=?");
    		
    		ResultSet rs=dbconnect.executeQuery("select * from zhangfan.SM_DELIVER where (status=1 or status=0) and rownum<30");
    		Res.log(Res.DEBUG,"建立链接");
    		while(rs.next()){
    			String  content=rs.getString("MSG");
    			String orgaddr=rs.getString("ORGADDR").trim();
    			String destaddr=rs.getString("DESTADDR").trim();
                //插入数据库表中。
    	        insertTable(0,orgaddr.substring(2,orgaddr.length()),destaddr, content,
    	                        mediaInfo.getMediaId(),"",1);            
    	        Res.log(Res.DEBUG,"写接收数据:"+content);
    	        
    	        dbconnect.setString(1,orgaddr);
    	        dbconnect.setString(2,destaddr);
    	        dbconnect.setString(3,content);
    	        dbconnect.executeUpdate();
    		}
    		
    	}
          

    }
    catch (Exception e) {
      //e.printStackTrace();
      e.printStackTrace();
      Res.log(Res.ERROR,"接收连接错误！client断开连接");
      return;
    }
    finally {
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

