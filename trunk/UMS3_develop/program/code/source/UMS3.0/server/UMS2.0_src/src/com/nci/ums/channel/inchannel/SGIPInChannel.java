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

import spApi.*;
import java.io.*;
import java.math.BigInteger;
import java.net.*;


import com.nci.ums.channel.channelinfo.*;
import com.nci.ums.util.*;

public class SGIPInChannel
    extends InChannel {
	BigInteger bnSourceID=new BigInteger("3057162292");
	private final int NODEID =bnSourceID.intValue() ;	
  //private final long NODEID = new Long("3057162292").longValue();

  private MediaInfo mediaInfo;

  private ServerSocket serversocket = null;

  public SGIPInChannel(MediaInfo mediaInfo) {
    super(mediaInfo);
    this.mediaInfo = mediaInfo;
  }

public void run() {
    setIsQuit(false);
    startServer();
    while (!Thread.interrupted() && !quitFlag.getLockFlag() && !isQuit) {
      listen();
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
    while ( (!Thread.interrupted() && !quitFlag.getLockFlag() && !isQuit) &&
           (serversocket == null || serversocket.isClosed())) {
      try {
        serversocket = new ServerSocket(mediaInfo.getPort());
          Res.log(Res.INFO,"��ͨ���ط���������");
      }
      catch (IOException e) {
        e.printStackTrace();
        Res.log(Res.ERROR,"8001","��ͨ");
        Res.logExceptionTrace(e);
        serversocket = null;
      }
      if (serversocket == null) {
        Res.log(Res.INFO, "������ͨ���ط������쳣������" + mediaInfo.getPort() + "�Ƿ��Ѿ���ռ�ã�");
        Res.log(Res.INFO,
                "sleep" + mediaInfo.getSleepTime() + "��󣬽��ٴ���ͼ���½���������");
        try {
          Thread.sleep(mediaInfo.getSleepTime() * 1000);
        }
        catch (InterruptedException e) {
          Res.log(Res.INFO, "��ͨ���ط������̱߳��ж�");
          setIsQuit(true);
        }
      }
    }
  }

  /*
   * �رշ������ĺ���
   */
  public void closeServer() {
    try {
      if (serversocket != null || !serversocket.isClosed())
        serversocket.close();
    }
    catch (IOException e) {
      e.printStackTrace();
      Res.log(Res.INFO,"�ر�����������ʧ�ܣ�");
    }
  }

  /*
   * ���������������ĺ���
   */
  public void listen() {
    Socket so = null;
    OutputStream output = null;
    InputStream input = null;
    SGIP_Command command = null;
    int result=-1;

    try {
      so = serversocket.accept();
      input = so.getInputStream();
      output = so.getOutputStream();

      command = new SGIP_Command();
      SGIP_Command tmpCMD = null;

      Deliver deliver = null;
      DeliverResp deliverresp = null;

      Bind active = null;
      Unbind term = null;

      BindResp resp = null;
      UnbindResp Unresp = null;

      Report report = null;
      ReportResp reportresp = null;

      Userrpt userrpt = null;
      UserrptResp userrptresp = null;
      String content=null;

      boolean loop = true;
      Res.log(Res.INFO, "receive lianTong Response���ܵ���ͨ��Ӧ��");
      while ((!Thread.interrupted() && !quitFlag.getLockFlag() && !isQuit) &&loop) {
        tmpCMD = command.read(input); //����sgip��Ϣ
        switch (command.getCommandID()) {
          case SGIP_Command.ID_SGIP_BIND: {
            Res.log(Res.INFO, "recieve lianTong login���ܵ���½����");
            active = (Bind) tmpCMD; //ǿ��ת��
            result = active.readbody(); //���
            /*System.out.println(tmpCMD.getTotalLength());
            System.out.println(tmpCMD.getCommandID());
            System.out.println(tmpCMD.getSeqno_1());
            System.out.println(tmpCMD.getSeqno_2());
            System.out.println(tmpCMD.getSeqno_3());
            System.out.println(active.GetLoginType());
            System.out.println(active.GetLoginName());
            System.out.println(active.GetLoginPassword());*/
            
            resp = new BindResp(tmpCMD.getMsgHead());
            resp.SetResult(0);
            resp.write(output);
            break;
          }
          case SGIP_Command.ID_SGIP_UNBIND: {
            //Res.log(Res.DEBUG, "receive lianTong logout���ܵ��˳���½����");
            term = (Unbind) tmpCMD; //ǿ��ת��
            /*System.out.println(tmpCMD.getTotalLength());
            System.out.println(tmpCMD.getCommandID());
            System.out.println(tmpCMD.getSeqno_1());
            System.out.println(tmpCMD.getSeqno_2());
            System.out.println(tmpCMD.getSeqno_3());
            System.out.println(term.GetFlag());*/
            Unresp = new UnbindResp(tmpCMD.getMsgHead()); //node id 3��CP_id
            Unresp.write(output);
            loop = false;
            break;
          }
          case SGIP_Command.ID_SGIP_DELIVER: {
            //Res.log(Res.DEBUG, "receive lianTong message���ܵ���ͨ����Ϣ");
            deliver = (Deliver) tmpCMD; //ǿ��ת��
            result=deliver.readbody(); //���
            /*Res.log(Res.DEBUG, "���͵��ֻ�����" + deliver.getUserNumber());
            Res.log(Res.DEBUG, "seqno_1:" + deliver.getSeqno_1());
            Res.log(Res.DEBUG, "seqno_2:" + deliver.getSeqno_2());
            Res.log(Res.DEBUG, "seqno_3:" + deliver.getSeqno_3());

            Res.log(Res.DEBUG, "��Ϣ���뷽ʽΪ" + deliver.getMessageCoding());
            Res.log(Res.DEBUG, "��Ϣ����Ϊ" + deliver.getMessageLength());
            */
            int data_code=0;
            switch (deliver.getMessageCoding()) {
              case 0x04:{
              	data_code=4;
                content = Util.getASCII(deliver.getMessageByte(),  deliver.getMessageLength());
                break;
              }
              case 0x21:{
              	data_code=21;
                content = Util.getASCII(deliver.getMessageByte(),  deliver.getMessageLength());
                break;
              }            	
              case 0x08:{
              	data_code=0;
                content = new String(deliver.getMessageByte(), 0, deliver.getMessageLength(), "UnicodeBig");
                break;
              }
              case 0x15:
                content = new String(deliver.getMessageByte(), 0, deliver.getMessageLength(), "UnicodeBig");
                break;
              case 0x00:{
              	data_code=1;
                content = new String(deliver.getMessageByte(), 0, deliver.getMessageLength(), "GBK");
                break;
              }
              default:
                content = new String(deliver.getMessageByte(), 0,deliver.getMessageLength(),"UnicodeBig");
                break;
            }

            //Res.log(Res.DEBUG, "��Ϣ����Ϊ" + content);
            //Res.log(Res.DEBUG, "result" + result);


            //�������ݿ���С�
            insertTable(0,deliver.getUserNumber().trim().substring(2,deliver.getUserNumber().trim().length()), deliver.getSPNumber().trim(), content,
                        mediaInfo.getMediaId(),"",data_code);            
            deliverresp = new DeliverResp(tmpCMD.getMsgHead()); //result
            deliverresp.SetResult(0);
            deliverresp.write(output);
            break;
          }
          case SGIP_Command.ID_SGIP_REPORT: {
            report = (Report) tmpCMD; //ǿ��ת��
            result = report.readbody(); //���
            Res.log(Res.DEBUG, "���ܵ�������Ϣ");
            reportresp = new ReportResp(tmpCMD.getMsgHead()); //result
            reportresp.SetResult(0);
            reportresp.write(output);
            break;
          }
          case SGIP_Command.ID_SGIP_USERRPT: {
            userrpt = (Userrpt) tmpCMD; //ǿ��ת��
            result = userrpt.readbody(); //���            
            userrptresp = new UserrptResp(tmpCMD.getMsgHead());
            userrptresp.SetResult(12);
            userrptresp.write(output);
            break;
          }

          default:
            Res.log(Res.DEBUG, "���ܵ�����δ֪��Ϣ");
            break;
        }
      }
    }
    catch (Exception e) {
      //e.printStackTrace();
      Res.log(Res.ERROR,"�������Ӵ���client�Ͽ�����");
      Res.logExceptionTrace(e);
      return;
    }
    finally {
      try {
        if (input != null)
          input.close();
        if (output != null)
          output.close();
        if (so != null)
          so.close();
      }
      catch (Exception e) {
        e.printStackTrace();
        Res.log(Res.ERROR,"�ر����Ӷ˿ڴ���"+e);
        Res.logExceptionTrace(e);
        return;
      }
    }
  }


}

