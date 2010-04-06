package com.nci.ums.sichuan.ctcc;

import com.nci.ums.channel.channelinfo.LockFlag;

public class CTCCThreadLockFlag extends LockFlag
{
  static CTCCThreadLockFlag flag;

  public static CTCCThreadLockFlag getInstance()
  {
    if (flag == null) {
      flag = new CTCCThreadLockFlag();
    }
    return flag;
  }
}