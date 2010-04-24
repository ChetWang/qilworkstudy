package mail.alert;

import com.huawei.insa2.comm.cmpp.message.CMPPSubmitMessage;
import com.huawei.insa2.util.Args;
import com.huawei.insa2.util.Cfg;
import com.huawei.smproxy.SMProxy;
import java.io.IOException;
import java.net.URL;
import org.apache.log4j.Level;

public class AlertSender
{
  private SMProxy proxy;

  public AlertSender()
  {
    try
    {
      Args args = new Cfg(
        super.getClass().getResource("/mail/alert/mmsconfig.xml").toString(), false).getArgs("ismg");
      this.proxy = new SMProxy(args);
    } catch (IOException e) {
      MsgLogger.log(Level.ERROR, e.getMessage(), e);
    }
  }

  public void sendAlert() throws IOException {
    String[] dest = { "13819155409" };
    CMPPSubmitMessage submitMsg = new CMPPSubmitMessage(1, 1, 
      0, 0, "9999", 0, "", 0, 0, 15, "911337", "02", "000010", null, 
      null, "106573071242", dest, "part-time alert".getBytes(), "");
    this.proxy.send(submitMsg);
  }

  public void sendAlert(String[] dest, String content) throws IOException
  {
    CMPPSubmitMessage submitMsg = new CMPPSubmitMessage(1, 1, 
      0, 0, "9999", 0, "", 0, 0, 15, "911337", "02", "000010", null, 
      null, "106573071242", dest, content.getBytes(), "");
    this.proxy.send(submitMsg);
  }

  public SMProxy getProxy() {
    return this.proxy;
  }
}