package org.vlg.linghu.sms.huawei.client;

import com.huawei.insa2.comm.sgip.message.SGIPDeliverMessage;
import com.huawei.insa2.comm.sgip.message.SGIPMessage;
import com.huawei.insa2.util.Args;
import com.huawei.smproxy.SGIPSMProxy;

public class MySGIPSendSMProxy extends SGIPSMProxy
{

  public MySGIPSendSMProxy(Args args) {
    super(args);
//    startService("10.70.116.79", 8801);
//    this.demo = demo;
  }

  public SGIPMessage onDeliver(SGIPDeliverMessage msg)
  {
//    this.demo.ProcessRecvDeliverMsg(msg);
	  System.out.println("huawei deliver!!!!!");
    return super.onDeliver(msg);
  }

  public void OnTerminate() {
//    this.demo.Terminate();
  }
}
