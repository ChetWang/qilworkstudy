package com.nci.ums.sichuan.ctcc;

import com.nci.ums.channel.channelinfo.LockFlag;

public class CTCCDataLockFlag extends LockFlag
{
  static CTCCDataLockFlag flag;

  public static CTCCDataLockFlag getInstance()
  {
    if (flag == null) {
      flag = new CTCCDataLockFlag();
    }
    return flag;
  }
}