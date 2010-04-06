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
   * 建立服务器的函数
   * 如果线程断开或者程序退出标志被设置或者线程推出标志被设置的时候，直接退出连接。
   * 如果在前面没有发生的情况下，服务断口建立好，也退出循环。
   */
  public void startServer() {
    while ( (!Thread.interrupted() && !quitFlag.getLockFlag() && !isQuit) &&
           (serversocket == null || serversocket.isClosed())) {
      try {
        serversocket = new ServerSocket(mediaInfo.getPort());
          Res.log(Res.INFO,"联通本地服务器建立");
      }
      catch (IOException e) {
        e.printStackTrace();
        Res.log(Res.ERROR,"8001","联通");
        Res.logExceptionTrace(e);
        serversocket = null;
      }
      if (serversocket == null) {
        Res.log(Res.INFO, "建立联通本地服务器异常，请检查" + mediaInfo.getPort() + "是否已经被占用！");
        Res.log(Res.INFO,
                "sleep" + mediaInfo.getSleepTime() + "秒后，将再次试图重新建立服务器");
        try {
          Thread.sleep(mediaInfo.getSleepTime() * 1000);
        }
        catch (InterruptedException e) {
          Res.log(Res.INFO, "联通本地服务器线程被中断");
          setIsQuit(true);
        }
      }
    }
  }

  /*
   * 关闭服务器的函数
   */
  public void closeServer() {
    try {
      if (serversocket != null || !serversocket.isClosed())
        serversocket.close();
    }
    catch (IOException e) {
      e.printStackTrace();
      Res.log(Res.INFO,"关闭侦听服务器失败！");
    }
  }

  /*
   * 服务器进行侦听的函数
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
      Res.log(Res.INFO, "receive lianTong Response接受到联通响应！");
      while ((!Thread.interrupted() && !quitFlag.getLockFlag() && !isQuit) &&loop) {
        tmpCMD = command.read(input); //接收sgip消息
        switch (command.getCommandID()) {
          case SGIP_Command.ID_SGIP_BIND: {
            Res.log(Res.INFO, "recieve lianTong login接受到登陆请求");
            active = (Bind) tmpCMD; //强制转换
            result = active.readbody(); //解包
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
            //Res.log(Res.DEBUG, "receive lianTong logout接受到退出登陆请求");
            term = (Unbind) tmpCMD; //强制转换
            /*System.out.println(tmpCMD.getTotalLength());
            System.out.println(tmpCMD.getCommandID());
            System.out.println(tmpCMD.getSeqno_1());
            System.out.println(tmpCMD.getSeqno_2());
            System.out.println(tmpCMD.getSeqno_3());
            System.out.println(term.GetFlag());*/
            Unresp = new UnbindResp(tmpCMD.getMsgHead()); //node id 3＋CP_id
            Unresp.write(output);
            loop = false;
            break;
          }
          case SGIP_Command.ID_SGIP_DELIVER: {
            //Res.log(Res.DEBUG, "receive lianTong message接受到联通短消息");
            deliver = (Deliver) tmpCMD; //强制转换
            result=deliver.readbody(); //解包
            /*Res.log(Res.DEBUG, "发送的手机号码" + deliver.getUserNumber());
            Res.log(Res.DEBUG, "seqno_1:" + deliver.getSeqno_1());
            Res.log(Res.DEBUG, "seqno_2:" + deliver.getSeqno_2());
            Res.log(Res.DEBUG, "seqno_3:" + deliver.getSeqno_3());

            Res.log(Res.DEBUG, "信息编码方式为" + deliver.getMessageCoding());
            Res.log(Res.DEBUG, "信息长度为" + deliver.getMessageLength());
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

            //Res.log(Res.DEBUG, "信息内容为" + content);
            //Res.log(Res.DEBUG, "result" + result);


            //插入数据库表中。
            insertTable(0,deliver.getUserNumber().trim().substring(2,deliver.getUserNumber().trim().length()), deliver.getSPNumber().trim(), content,
                        mediaInfo.getMediaId(),"",data_code);            
            deliverresp = new DeliverResp(tmpCMD.getMsgHead()); //result
            deliverresp.SetResult(0);
            deliverresp.write(output);
            break;
          }
          case SGIP_Command.ID_SGIP_REPORT: {
            report = (Report) tmpCMD; //强制转换
            result = report.readbody(); //解包
            Res.log(Res.DEBUG, "接受到报告消息");
            reportresp = new ReportResp(tmpCMD.getMsgHead()); //result
            reportresp.SetResult(0);
            reportresp.write(output);
            break;
          }
          case SGIP_Command.ID_SGIP_USERRPT: {
            userrpt = (Userrpt) tmpCMD; //强制转换
            result = userrpt.readbody(); //解包            
            userrptresp = new UserrptResp(tmpCMD.getMsgHead());
            userrptresp.SetResult(12);
            userrptresp.write(output);
            break;
          }

          default:
            Res.log(Res.DEBUG, "接受到其他未知消息");
            break;
        }
      }
    }
    catch (Exception e) {
      //e.printStackTrace();
      Res.log(Res.ERROR,"接收连接错误！client断开连接");
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
        Res.log(Res.ERROR,"关闭连接端口错误！"+e);
        Res.logExceptionTrace(e);
        return;
      }
    }
  }


}

