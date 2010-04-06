/*
 * <p>Title: CMPPActiveTest.java</p>
 * <p>Description:
 *    �ṩ���ƶ����������������Ͳ��԰�����һ��ʱ����û���յ���Ϣ��ʱ�򣬳���
 *    �����������Ͳ��԰������������ӡ�
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering�� Ltd.</p>
 * <p>Company:   2003 Hangzhou NCI System Engineering�� Ltd.</p>
 * Date        Author      Changes
 * NOV. 5 2003   ��־��      Created
 * @version 1.0
 */

package com.nci.ums.channel.inchannel;

import com.commerceware.cmpp.*;
import com.nci.ums.util.Res;
import java.io.*;
import com.nci.ums.channel.channelinfo.*;

public class CMPPActiveTest
    implements Runnable {
  //˯��ʱ��
  private int sleepTime;
  //���Ͷ˿�
  private conn_desc con;
  //��־
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
    Res.log(Res.DEBUG, "���������߳�");
    runner.start();
  }

  public void run() {
    //���ѭ��Ϊ�˼��ٿ�loop�Ĵ��������Ч��
    int i = 3;
    while (!quitFlag.getLockFlag()) {
      while (getStartFlag() == false) {
        try {
          Thread.sleep(sleepTime * 1000);
        }
        catch (InterruptedException e) {
          Res.log(Res.ERROR, "�̱߳��жϣ�����ʼ����Ƿ��жϡ�");
          setStartFlag(true);
        }
      }

      //�����ܱ�־������Ѿ����ܵ���Ϣ����������־����Ϊtrue��
      //��������Ĺ��������û�н��յ���Ϣ����ȴ�һ��ʱ����ټ�������־����ֵ��
      //�������false����ʾû���յ���Ϣ�����Ͳ��԰���
      //�����;���жϣ�˵���Ѿ����ܵ���Ϣ�ˣ�����Ҫ�ٷ��Ͳ��԰��ˡ�
      while (i-- > 0 && getFlag() == false) {
        if (getFlag() == false) {
          try {
            Thread.sleep(sleepTime * 1000);
          }
          catch (Exception e) {
            e.printStackTrace();
            Res.log(Res.ERROR, "�Ѿ����ܵ���Ϣ�ˣ��̱߳��ж��ˣ�");
            setFlag(true);
          }
        }
        //���˯�߽�����û���յ���Ϣ�����Ͳ��԰���
        if (getFlag() == false) {
          try {
            cmpp.cmpp_active_test(con);
          }
          catch (IOException e) {
            Res.log(Res.ERROR, "���Ͳ��԰���ʱ���������Ͽ���");
            Res.logExceptionTrace(e);
            break;
          }
        }
      }
    }

  }

  //���ڽ�����һ���ֵĳ�˯��
  public void endSleep() {
    setStartFlag(true);
    setFlag(false);
    runner.interrupt();
  }

  //�����������Թ��̡�
  public void endTest() {
    setFlag(true);
    setStartFlag(false);
    runner.interrupt();
  }

}