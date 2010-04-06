/**
 * <p>Title: SGIPOutChannel.java</p>
 * <p>Description:
 *    SGIPInChannel���������࣬�̳�InChannel
 *    ʵ����ͨ��Ϣ�ķ��͹���
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering�� Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering�� Ltd.</p>
 * Date        Author      Changes
 * NOV 20 2003   ��־��        Created
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
		// TODO �Զ����� catch ��
		e.printStackTrace();
	}
    }
    closeServer();
    setIsQuit(true);
  }

  /*
   * �����������ĺ���
   * ����̶߳Ͽ����߳����˳���־�����û����߳��Ƴ���־�����õ�ʱ��ֱ���˳����ӡ�
   * �����ǰ��û�з���������£�����Ͽڽ����ã�Ҳ�˳�ѭ����
   */
  public void startServer() {

  }

  /*
   * �رշ������ĺ���
   */
  public void closeServer() {
  }

  /*
   * ���������������ĺ���
   */
  public void listen() {
    int result=-1;
    NBSmsConnect dbconnect=null;

    try {
    	dbconnect=new NBSmsConnect();
    	
    	if(dbconnect!=null){
    		dbconnect.prepareStatement("update zhangfan.SM_DELIVER set status=2 where (status=0 or status=1) and ORGADDR=? and DESTADDR=? and MSG=?");
    		
    		ResultSet rs=dbconnect.executeQuery("select * from zhangfan.SM_DELIVER where (status=1 or status=0) and rownum<30");
    		Res.log(Res.DEBUG,"��������");
    		while(rs.next()){
    			String  content=rs.getString("MSG");
    			String orgaddr=rs.getString("ORGADDR").trim();
    			String destaddr=rs.getString("DESTADDR").trim();
                //�������ݿ���С�
    	        insertTable(0,orgaddr.substring(2,orgaddr.length()),destaddr, content,
    	                        mediaInfo.getMediaId(),"",1);            
    	        Res.log(Res.DEBUG,"д��������:"+content);
    	        
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
      Res.log(Res.ERROR,"�������Ӵ���client�Ͽ�����");
      return;
    }
    finally {
    	if(dbconnect!=null)
			try {
				dbconnect.close();
			} catch (Exception e1) {
				// TODO �Զ����� catch ��
				e1.printStackTrace();
			}
    	

    }
  }


}

