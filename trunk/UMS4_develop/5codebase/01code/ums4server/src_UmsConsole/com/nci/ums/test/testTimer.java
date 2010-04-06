package com.nci.ums.test;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import java.io.*;

public class testTimer implements Runnable{
  private boolean flag;
  private int sleepTime;
  private PrintWriter out;

  public void setOutStream(PrintWriter out){
    this.out=out;
  }

  public PrintWriter getOutStream(){
    return this.out;
  }

  synchronized public void setFlag(boolean flag){
    this.flag=flag;
  }

  synchronized public boolean getFlag(){
    return this.flag;
  }

  public testTimer(int sleepTime) {
    this.sleepTime=sleepTime;
  }

  public void start() {
    System.out.println("启动timer线程!");
    new Thread(this, "testTimer").start();
  }

  public void run() {

    if (getFlag() == false) {
      System.out.println("开始sleep!");
      try {
        Thread.sleep(sleepTime * 1000);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
      System.out.println("结束sleep");
    }
    if (getFlag() == false) {
      out.println("发送测试包！");
      out.flush();
    }
    System.out.println("线程结束!");
  }

}