/*
 * <p>Title: CMPPActiveTest.java</p>
 * <p>Description:
 *    提供给移动拨入渠道用来发送测试包，当一端时间内没有收到信息的时候，程序将
 *    给服务器发送测试包，来保持连接。
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering， Ltd.</p>
 * <p>Company:   2003 Hangzhou NCI System Engineering， Ltd.</p>
 * Date        Author      Changes
 * NOV. 5 2003   张志勇      Created
 * @version 1.0
 */

package com.nci.ums.channel.inchannel;

import com.commerceware.cmpp.*;
import com.nci.ums.util.Res;
import java.io.*;
import com.nci.ums.channel.channelinfo.*;

public class CMPPActiveTest
    implements Runnable {
  //睡眠时间
  private int sleepTime;
  //发送端口
  private conn_desc con;
  //标志
  private boolean flag;
  private boolean startFlag;
  private Thread runner;
  private CMPP cmpp;
  private   QuitLockFlag quitFlag;


  public CMPPActiveTest(int sleepTime, CMPP cmpp) {
    this.sleepTime = sleepTime;
    quitFlag =QuitLockFlag.getInstance();
    runner = new Thread(this, "ActiveTest");
    this.cmpp = cmpp;
  }

  synchronized public void setFlag(boolean flag) {
    this.flag = flag;
  }

  synchronized public boolean getStartFlag() {
    return this.startFlag;
  }

  public void setStartFlag(boolean startFlag) {
    this.startFlag = startFlag;
  }

  public boolean getFlag() {
    return this.flag;
  }

  public void setCon(conn_desc con) {
    this.con = con;
  }

  public void start() {
    Res.log(Res.DEBUG, "启动测试线程");
    runner.start();
  }

  public void run() {
    //这段循环为了减少空loop的次数，提高效率
    int i = 3;
    while (!quitFlag.getLockFlag()) {
      while (getStartFlag() == false) {
        try {
          Thread.sleep(sleepTime * 1000);
        }
        catch (InterruptedException e) {
          Res.log(Res.ERROR, "线程被中断！将开始检查是否中断。");
          setStartFlag(true);
        }
      }

      //检查接受标志，如果已经接受到信息，则把这个标志设置为true，
      //则不做下面的工作，如果没有接收到信息，则等待一段时间后，再检查这个标志的数值，
      //如果还是false，表示没有收到信息，则发送测试包。
      //如果中途被中断，说明已经接受到信息了，不需要再发送测试包了。
      while (i-- > 0 && getFlag() == false) {
        if (getFlag() == false) {
          try {
            Thread.sleep(sleepTime * 1000);
          }
          catch (Exception e) {
            e.printStackTrace();
            Res.log(Res.ERROR, "已经接受到信息了，线程被中断了！");
            setFlag(true);
          }
        }
        //如果睡眠结束还没有收到信息，则发送测试包。
        if (getFlag() == false) {
          try {
            cmpp.cmpp_active_test(con);
          }
          catch (IOException e) {
            Res.log(Res.ERROR, "发送测试包的时候出错，网络断开！");
            Res.logExceptionTrace(e);
            break;
          }
        }
      }
    }

  }

  //用于结束第一部分的长睡眠
  public void endSleep() {
    setStartFlag(true);
    setFlag(false);
    runner.interrupt();
  }

  //用来结束测试过程。
  public void endTest() {
    setFlag(true);
    setStartFlag(false);
    runner.interrupt();
  }

}