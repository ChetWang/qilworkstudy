/**
 * <p>Title: </p>
 * <p>Description:
 *    
 * </p>
 * <p>Copyright: 2006 Hangzhou NCI System Engineering�� Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering�� Ltd.</p>
 * Date            Author          Changes          Version
   2006-9-22          �ٿ�           Created           1.0
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
	    int result=0;
	    SCSmsConnect dbconnect=null;

	    try {
	    	dbconnect=new SCSmsConnect();
	    	
	    	if(dbconnect!=null){
	    		//dbconnect.prepareStatement("update zhangfan.SM_DELIVER set status=2 where (status=0 or status=1) and ORGADDR=? and DESTADDR=? and MSG=?");
	    		
	    		ResultSet rs=dbconnect.executeQuery("select * from moqueue");
	    		Res.log(Res.DEBUG,"��������");
	    		while(rs.next()){
	    			String content=rs.getString("msgcontent");
	    			String orgaddr=rs.getString("srctermid").trim();
	    			String destaddr=rs.getString("desttermid").trim();
	    			int msgIndex = rs.getInt("ID");
	                //�������ݿ���С�
	    	        insertTable(0,orgaddr,destaddr, content,
	    	                        mediaInfo.getMediaId(),"",1);            
	    	        Res.log(Res.DEBUG,"д��������:"+content);
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
	      Res.log(Res.ERROR,"�������Ӵ���client�Ͽ�����");
	      return;
	    }finally{
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
