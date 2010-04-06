/**
 * <p>Title: </p>
 * <p>Description:
 *    
 * </p>
 * <p>Copyright: 2006 Hangzhou NCI System Engineering�� Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering�� Ltd.</p>
 * Date            Author          Changes          Version
   2006-9-21          �ٿ�           Created           1.0
 */
package com.nci.ums.channel.outchannel;

import java.sql.SQLException;
import com.nci.scport.ums.*;
import com.nci.ums.channel.channelinfo.MediaInfo;
import com.nci.ums.exception.UMSConnectionException;
import com.nci.ums.util.Res;

public class SCPortOutChannel extends OutChannel {

	/**
	 * @param args
	 */
	SCSmsConnect dbconnect=null;

	private MsgInfo[] msgInfos;
	private int submitCount;
	private int msgCount;

	//��ʱ����Ϊһ����
	private javax.swing.Timer my_timer;
	private SCPortOutChannel.ResponseOutTime  responseOutTime=new SCPortOutChannel.ResponseOutTime();

	/**
	 * @param mediaInfo
	 */
	public SCPortOutChannel(MediaInfo mediaInfo) {
		super(mediaInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		while (!Thread.interrupted() && !quitFlag.getLockFlag() && !stop) {
			if (stop)
				break;
			try {
				msgInfos = new MsgInfo[16];
				msgCount = getMsgInfos(msgInfos);
				//���ͻ�����������Ϣ
				submitCount = 0;
				for (int i = 0; i < msgCount; i++) {
					//����ʱΪ��ע
					int response = sendMsg(msgInfos[i]);
					//int response=0;
					if (response == 0) {
						submitCount = submitCount + 1;
					}
				}
			} catch (Exception e) {
				//endSubmit(msgInfos,msgCount);
				logout();
				Res.log(Res.ERROR, "HuaWeiOutChannel run error:" + e);
                                Res.logExceptionTrace(e);
				sleepTime(1);
			}
			if (msgCount == 0)
				sleepTime(10);
		}
	}

	/*
	 * ����һ����Ϣ��������ֵΪ������
	 */
	public int sendMsg(MsgInfo msgInfo) throws UMSConnectionException {
		int result = 0;
		dbconnect=null;
		try {
			//submitShortMessage api�ӿڣ�
			/*
			 * atTime�����Ͷ��ŵ�ʱ�䡣(Java.util.Date) sourceAddr�������Ͷ��ŵ�Դ��ַ��
			 * destAddr�������Ͷ��ŵ�Ŀ�ĵ�ַ�� content���������ݡ�
			 * needStateReport�����͸ö����Ƿ���Ҫ״̬���档��ע��ʹ��״̬�������ȷ�϶Է��Ƿ�һ���յ������ò�������ʹ������ֵ��0����ʾ����Ҫ״̬���棬1����ʾ��Ҫ״̬���档
			 * serviceID��ҵ�����͡�ҵ�����ͽ�������Ӫ�̶˶Զ��Ž��мƷ�ʱʹ�ã��ò������ܳ���10���ַ���
			 * feeType���ʷ����͡��ò���ֻ�������¼���ֵ����01����ʾ���û���ѣ���02����ʾ���û�������ȡ��Ϣ�ѣ������շѽ�������һ����������03����ʾ���û���������ȡ��Ϣ�ѡ�
			 * feeCode���ʷѴ��롣�ò���������һ����������ʾ�ö��ŵ���Ϣ�ѣ�ע���Է�Ϊ��λ�����ò������ܳ��������ַ��� �쳣�� 1002,
			 * 1003,100501 ��100509, 1010 ���ο��쳣�б�
			 *  
			 */
			
			dbconnect=new SCSmsConnect();

			String sourceAddr = msgInfo.getSendID();
			String destAddr = msgInfo.getRecvID();
			String content = msgInfo.getContent();

			dbconnect.prepareStatement("insert into mtqueue(destTermID,srcTermID,msgContent) values(?,?,?)");
			
			dbconnect.setString(1,destAddr);
			dbconnect.setString(2,sourceAddr);
			dbconnect.setString(3,content);

			dbconnect.executeUpdate();
			OpData(0,msgInfo,"");
			Res.log(Res.DEBUG, result + " message sent in progress...");
			
		} catch (SQLException e) {
			e.printStackTrace();
			Res.log(Res.ERROR, "SCYCPortOutChannel sendMsg error:���ݿ������쳣!"); 
                        Res.logExceptionTrace(e);
		} catch (Exception e) {
			Res.logExceptionTrace(e);
			e.printStackTrace();
		}finally{
			if(dbconnect!=null)
				try {
					dbconnect.close();
				} catch (Exception e1) {
					// TODO �Զ����� catch ��
					e1.printStackTrace();
				}
		}
		return result;
	}
	
	private void login()  {
		Res.log(Res.DEBUG, "��ʼ�����ƻ����������������������...");
		try {
			//1.��������
			dbconnect=new SCSmsConnect();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private void logout() {
		Res.log(Res.DEBUG, "��ʼ�����ƻ��������˳��������������...");
		try {
			if(dbconnect!=null){
				dbconnect.close();
			}
			Res.log(Res.DEBUG, "�����˳�");
		} catch (Exception ee) {
			Res.log(Res.DEBUG, "�˳��쳣");
		}
	}


	public static void main(String[] args) {
		/*
		 * MediaInfo���캯������Ҫ�Ĳ��� String mediaId, String mediaName,String
		 * className, String ip, int port, int timeOut, int repeatTimes, int
		 * startWorkTime,int endWorkTime, int type, String loginName, String
		 * loginPassword, int sleepTime,int status
		 */
		MediaInfo mediaInfo = new MediaInfo("1", "�Ĵ��̲�UMS",
				"com.nci.ums.channel.outchannel.SCPortOutChannel",
				"localhost", 80, 60, 5, 1, 1, 1, "ums", "ums", 60, 0);

		SCPortOutChannel out = new SCPortOutChannel(mediaInfo);
		MsgInfo msgInfo = new MsgInfo();
		msgInfo.setSendID("1300000001");
		msgInfo.setRecvID("1300000002");
		msgInfo.setContent("����������");
		msgInfo.setReply("1");
		//serviceID = "EIE";
		msgInfo.setFeeType(1);
		msgInfo.setFee(1);
		try {
			int result = out.sendMsg(msgInfo);
			Res.log(Res.DEBUG, "HuaWeiOutChannel ����OK");
		} catch (UMSConnectionException e) {
			e.printStackTrace();
		}
	}
	
    private void errorLogout()
    {
        Res.log(Res.DEBUG, "��ʼ�����ƻ��������˳��������������");
        try {
          if(dbconnect!=null)dbconnect.close();
          Res.log(Res.DEBUG, "�����˳�");
        }
        catch (Exception ee) {
          Res.log(Res.DEBUG, "�˳��쳣");
        }finally{
        	dbconnect=null;
        }
    }	
    class ResponseOutTime implements java.awt.event.ActionListener {
    	public void actionPerformed(java.awt.event.ActionEvent e) {
    		errorLogout();
    	};
    }

}
