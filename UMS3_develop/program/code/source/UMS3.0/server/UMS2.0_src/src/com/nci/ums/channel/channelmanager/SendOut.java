/**
 * <p>Title: SendOut.java</p>
 * <p>Description:
 *    是主类，提供给外围接口使用，负责整个渠道模块的启动和终止工作。
 * </p>
 * <p>Copyright: 2003 Hangzhou NCISystem Engineering， Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering， Ltd.</p>
 * Date        Author      Changes
 * NOV. 5 2003   张志勇       Created
 * @version 1.0
 */

package com.nci.ums.channel.channelmanager;




import com.nci.ums.util.*;


public class SendOut extends Thread{
  //渠道信息管理对象
  private ChannelManager channelManager;
  //失效数据扫描对象
  private ScanInvalid scandInvalid;


  /*
   * 构造函数
   * 产生渠道信息管理对象和失效数据扫描对象
   */
  public SendOut() {
    Res.log(Res.INFO, "sendOut对象产生！");
    channelManager = new ChannelManager();
    scandInvalid = new ScanInvalid();
  }

  /*
   * 提供给外围接口的启动函数。
   * 这个函数将启动失效数据扫描线程和渠道信息管理线程
   */
public void run() {
    channelManager.start();
    /*UMSThread umsThread=new UMSThread("UMS短消息平台","渠道管理线程",Util.getCurrentTimeStr("yyyyMMddHHmmss"),"已启动",channelManager,"");
    Res.getUMSThreads().add(umsThread);*/

    scandInvalid.start();
     /*umsThread=new UMSThread("UMS短消息平台","失效数据扫描线程",Util.getCurrentTimeStr("yyyyMMddHHmmss"),"已启动",scandInvalid,"");
     Res.getUMSThreads().add(umsThread);*/
  }

  /*
   * 提供给外围接口的启动函数。
   * 这个函数将终止失效数据扫描线程和渠道信息管理线程
   */
  public void stopThread() {
    channelManager.stop();
    scandInvalid.stop();
  }

  /*
   * 提供给外围接口的启动函数。
   * 这个函数将刷新渠道管理信息，重新启动渠道信息管理线程
   */
  public void flush() {

  }

  public static void main(String[] args) {
    SendOut sendOut1 = new SendOut();
    sendOut1.start();
    /*   try{
         Thread.sleep(5000);
       }
       catch(Exception e){
         e.printStackTrace();
       }
       sendOut1.stop();
     */
  }
}