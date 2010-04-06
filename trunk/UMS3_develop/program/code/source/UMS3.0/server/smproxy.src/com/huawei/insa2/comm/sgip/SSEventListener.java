package com.huawei.insa2.comm.sgip;

import java.net.Socket;

public abstract interface SSEventListener
{
  public abstract void onConnect(Socket paramSocket);
}

/* Location:           D:\Documents\work\UMS3_develop\program\code\source\UMS3.0\server\lib\smproxy.jar
 * Qualified Name:     com.huawei.insa2.comm.sgip.SSEventListener
 * JD-Core Version:    0.5.3
 */