/**
 * <p>
 * Title: HuaWeiChannel.java
 * </p>
 * <p>
 * Description: ��Ϊ��Ϣ���Ⲧ�����࣬�̳�OutChannel ʵ�ֻ�Ϊ��Ϣ������Ϣ�ķ��͹���
 * ˼·����㶨ʱ��������ɨ��UMS..out_ready���ݱ��еļ�¼�� 
 * 		������Щ��¼ͨ����Ϊ�ṩ��API(smentry.jar)ת������Ϊ��Ϣ����sms..tbl_SMToSend���ݱ�
 * </p>
 * <p>
 * Copyright: 2005 Hangzhou NCI System Engineering�� Ltd.
 * </p>
 * <p>
 * Company: Hangzhou NCI System Engineering�� Ltd.
 * </p>
 * Date Author Changes 2005-4-20 ֣��ȫ Created
 * 
 * @version 1.0
 */
package com.nci.ums.channel.outchannel;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.nci.nbport.ums.NBSmsConnect;
import com.nci.ums.channel.channelinfo.MediaInfo;
import com.nci.ums.exception.UMSConnectionException;
import com.nci.ums.util.Res;
import com.nci.ums.util.Util;

public class NBPortOutChannel extends OutChannel {
	NBSmsConnect dbconnect=null;

	private MsgInfo[] msgInfos;
	private boolean responseFlag;
	private int submitCount;
	private int responseCount;
	private int msgCount;
	private int activeTime = 0;

	//��ʱ����Ϊһ����
	private javax.swing.Timer my_timer;
	private NBPortOutChannel.ResponseOutTime  responseOutTime=new NBPortOutChannel.ResponseOutTime();

	/**
	 * @param mediaInfo
	 */
	public NBPortOutChannel(MediaInfo mediaInfo) {
		super(mediaInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		//System.out.println("running...");
        //my_timer = new javax.swing.Timer(1000 * 60 * 1, responseOutTime); //1���Ӻ�δ��Ӧ����
		//my_timer.setRepeats(false);
		while (!Thread.interrupted() && !quitFlag.getLockFlag() && !stop) {
			//1.��������
//____________________����---------------------
			stop = true;
			if (stop)
				break;
//---------------------------------------------
			//2.������Ϣ
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
				
				//my_timer.stop();				
				//endSubmit(msgInfos,msgCount);
				
				
				//�ȴ���Ӧ
				/*
				 * responseFlag = false; responseCount = 0; if (submitCount > 0) {
				 * my_timer.start(); while (!Thread.interrupted() &&
				 * !quitFlag.getLockFlag() && !stop && !responseFlag) {
				 * //testResponse(); // ����ʱΪ��ע readPa(con); } my_timer.stop();
				 * Res.log(Res.DEBUG, "receive response end");
				 * endSubmit(msgInfos, msgCount); }
				 */
			} catch (Exception e) {
				//endSubmit(msgInfos,msgCount);
				logout();
				Res.log(Res.ERROR, "HuaWeiOutChannel run error:" + e);
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
			
			dbconnect=new NBSmsConnect();

			String sourceAddr = msgInfo.getSendID();
			String destAddr = "86"+msgInfo.getRecvID();
			String content = msgInfo.getContent();
			//int needStateReport = Integer.parseInt(msgInfo.getReply());
			String serviceID = "";
			String feeType = msgInfo.getFeeType() + "";
			String feeCode = msgInfo.getFee() + "";
			int sm_id=getSequenceNO(dbconnect);

			dbconnect.prepareStatement("insert into zhangfan.SM_SEND(SM_ID,ORGADDR,DESTADDR,MSG,MSG_TYPE,INPUT_TIME,SCHEDULE) values(?,?,?,?,?,?,?)");
			
			dbconnect.setString(1,"s"+sm_id);
			dbconnect.setString(2,sourceAddr);
			dbconnect.setString(3,destAddr);
			dbconnect.setString(4,content);
			dbconnect.setInt(5,1);
			
			dbconnect.setTimestamp(6,Util.getTimeStamp());
			dbconnect.setTimestamp(7,Util.getTimeStamp());
			dbconnect.executeUpdate();
			OpData(0,msgInfo,"");
			Res.log(Res.DEBUG, result + " message sent in progress...");
			
		} catch (SQLException e) {
			e.printStackTrace();
			Res.log(Res.ERROR, "NBPortOutChannel sendMsg error:���ݿ������쳣!"); 
		} catch (Exception e) {
			// TODO �Զ����� catch ��
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
			dbconnect=new NBSmsConnect();
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
		MediaInfo mediaInfo = new MediaInfo("1", "��Ϊ��Ϣ��",
				"com.nci.ums.channel.outchannel.HuaWeiOutChannel",
				"192.168.0.74", 80, 60, 5, 1, 1, 1, "ums", "ums", 60, 0);

		NBPortOutChannel out = new NBPortOutChannel(mediaInfo);
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
	
	
	private int getSequenceNO(NBSmsConnect connect){
		int result=0;
		ResultSet rs=null;		
		try {
			rs = connect.executeQuery("select zhangfan.sms_send.nextval from dual");
			if(rs.next()){
				result=rs.getInt(1);
			}
			rs.close();
		}
		catch (SQLException sqle) {
			sqle.printStackTrace();
			try{

				connect.executeUpdate("create sequence zhangfan.sms_send");
				
				rs = connect.executeQuery("select zhangfan.sms_send.nextval from dual");
				if(rs.next()){
					result=rs.getInt(1);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		finally {
			if(rs!=null){try{rs.close();}catch(Exception e){}}
		}
		
		return result;
		
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