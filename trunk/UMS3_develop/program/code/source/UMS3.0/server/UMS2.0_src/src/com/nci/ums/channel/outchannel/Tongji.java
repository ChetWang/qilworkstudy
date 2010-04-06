package com.nci.ums.channel.outchannel;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import com.nci.ums.util.Res;

public class Tongji {
  private String name;

  public  long success=0;
  public  long failure=0;
  public  long total=0;
  public  long totalTimes=0;
  public  long singleTimes=0;
  public  long singleTotal=0;

  public  void init() {
    success = 0;
    failure = 0;
    total = 0;
    totalTimes = 0;
    singleTimes = 0;
    singleTotal = 0;
  }

  public  void initSingle() {
    singleTimes = 0;
    singleTotal = 0;
  }


  public  void printInfo() {
    Res.log(Res.DEBUG, "在"+name+"的测试类中“");
    Res.log(Res.DEBUG, "总共发送信息" + total + "条");
    Res.log(Res.DEBUG, "总共发送信息失败" + failure + "条");
    Res.log(Res.DEBUG, "总共发送信息成功" + success + "条");
    Res.log(Res.DEBUG, "总共发送信息消耗时间" + totalTimes);
   // Res.log(Res.DEBUG, "平均发送信息消耗时间" + totalTimes / total + "条");
  }

  public Tongji(String name) {
    this.name=name;
  }
}