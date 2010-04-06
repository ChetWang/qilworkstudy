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
    Res.log(Res.DEBUG, "��"+name+"�Ĳ������С�");
    Res.log(Res.DEBUG, "�ܹ�������Ϣ" + total + "��");
    Res.log(Res.DEBUG, "�ܹ�������Ϣʧ��" + failure + "��");
    Res.log(Res.DEBUG, "�ܹ�������Ϣ�ɹ�" + success + "��");
    Res.log(Res.DEBUG, "�ܹ�������Ϣ����ʱ��" + totalTimes);
   // Res.log(Res.DEBUG, "ƽ��������Ϣ����ʱ��" + totalTimes / total + "��");
  }

  public Tongji(String name) {
    this.name=name;
  }
}