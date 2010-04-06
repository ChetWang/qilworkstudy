package com.nci.ums.sichuan.ctcc;

import com.ctcc.www.service.Ctcc_ema_wbsStub;
import com.ctcc.www.service.Ctcc_ema_wbsStub.MessageFormat;
import com.ctcc.www.service.Ctcc_ema_wbsStub.SendMethodType;
import com.ctcc.www.service.Ctcc_ema_wbsStub.SendSmsRequest;
import com.ctcc.www.service.Ctcc_ema_wbsStub.SendSmsResponse;
import com.nci.ums.channel.channelinfo.LockFlag;
import com.nci.ums.channel.channelinfo.MediaInfo;
import com.nci.ums.channel.outchannel.OutChannel;
import com.nci.ums.channel.outchannel.OutChannel_V3;
import com.nci.ums.util.Res;
import com.nci.ums.v3.message.UMSMsg;
import com.nci.ums.v3.message.basic.BasicMsg;
import com.nci.ums.v3.message.basic.MsgContent;
import com.nci.ums.v3.message.basic.Participant;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Properties;
import javax.swing.Timer;
import org.apache.axis2.AxisFault;
import org.apache.axis2.databinding.types.URI;

public class CTCCSMSOutChannel extends OutChannel_V3
  implements ActionListener
{
  protected CTCCDataLockFlag dataLockFlag;
  protected CTCCThreadLockFlag threadLockFlag;
  private Ctcc_ema_wbsStub stub = null;

  private String ctcc_endpoint = "";

  private String ctcc_applicationid = "";

  private String ctcc_ecid = "";

  private String ctcc_extendCode = "";

  private Properties errorcodes = new Properties();

  private Timer timer = new Timer(3000, this);

  public CTCCSMSOutChannel(MediaInfo nciMediaInfo)
  {
    super(nciMediaInfo);
    this.dataLockFlag = CTCCDataLockFlag.getInstance();
    this.threadLockFlag = CTCCThreadLockFlag.getInstance();
    try {
      Properties prop = new Properties();
      prop.load(super.getClass().getResourceAsStream("scCtccConf.prop"));
      this.ctcc_endpoint = prop.getProperty("endpoint");
      this.ctcc_applicationid = prop.getProperty("applicationid");
      this.ctcc_ecid = prop.getProperty("ecid");
      this.ctcc_extendCode = prop.getProperty("extendcode");
      this.stub = new Ctcc_ema_wbsStub(this.ctcc_endpoint);
      this.errorcodes.load(super.getClass().getResourceAsStream("errorcode.prop"));
    } catch (AxisFault e) {
      Res.log(4, "加载四川电信WebService Stub出错！");
      Res.logExceptionTrace(e);
    } catch (IOException e) {
      Res.logExceptionTrace(e);
    }
  }

  public String getMsgFmtFile()
  {
    return "/resources/MsgFmt_SMS.xml";
  }

  public boolean isLocked()
  {
    return ((this.dataLockFlag.getLockFlag()) || 
      (this.threadLockFlag.getLockFlag()));
  }

  public void sendViaChannel(UMSMsg[] msgs) {
    for (int i = 0; i < msgs.length; ++i) {
      if (msgs[i] == null)
        return;
      try
      {
        Ctcc_ema_wbsStub.SendSmsRequest request = new Ctcc_ema_wbsStub.SendSmsRequest();
        Participant[] receivers = msgs[i].getBasicMsg().getReceivers();
        URI[] destinationAddresses = new URI[receivers.length];
        for (int n = 0; n < receivers.length; ++n) {
          destinationAddresses[n] = 
            new URI("tel:" + 
            receivers[n].getParticipantID());
        }
        request.setDestinationAddresses(destinationAddresses);
        request.setApplicationID(this.ctcc_applicationid);

        request.setMessage(
          msgs[i].getBasicMsg().getMsgContent().getContent());
        request.setEcID(this.ctcc_ecid);
        request.setExtendCode(this.ctcc_extendCode);
        request.setMessageFormat(Ctcc_ema_wbsStub.MessageFormat.GB18030);
        request.setSendMethod(Ctcc_ema_wbsStub.SendMethodType.Normal);

        Ctcc_ema_wbsStub.SendSmsResponse response = this.stub.sendSms(request);
        String requestIdentifier = response.getRequestIdentifier();
        int result = -1;
        if (requestIdentifier.length() > 20)
          result = 0;
        else {
          Res.log(4, "消息发送失败，错误号：" + requestIdentifier + 
            ",错误内容：" + 
            this.errorcodes.getProperty(requestIdentifier));
        }
        super.OpData(result, msgs[i]);
      } catch (IOException e) {
        Res.log(4, "读取消息信息出错" + e.getMessage());
        e.printStackTrace();
        Res.logExceptionTrace(e);
        try {
          super.OpData(4321, msgs[i]);
        } catch (Exception localException1) {
        }
      } catch (IllegalArgumentException e) {
        Res.log(4, "读取消息信息出错" + e.getMessage());
        Res.logExceptionTrace(e);
        try {
          super.OpData(4321, msgs[i]);
        } catch (Exception localException2) {
        }
      } catch (Exception e) {
        try {
          super.OpData(4321, msgs[i]);
        } catch (Exception localException3) {
        }
      }
    }
  }

  public void setLocked(boolean flag) {
    this.threadLockFlag.setLockFlag(flag);
  }

  public LockFlag getDataLockFlag() {
    return this.dataLockFlag;
  }

  public void actionPerformed(ActionEvent e)
  {
  }

//  private class CTCC_V2 extends OutChannel
//  {
//    public CTCC_V2() {
//      super(CTCCSMSOutChannel.access$0(CTCCSMSOutChannel.this));
//    }
//
//    public void run()
//    {
//    }
//  }
}