package mail.alert;

import org.apache.log4j.Level;

public class MailAlert
{
  private MailDetector detector;
  private AlertSender sender;

  public MailAlert()
  {
    this.detector = new MailDetector();
    this.sender = new AlertSender();
  }

  public static void main(String[] args)
  {
    MailAlert alert = new MailAlert();
    try
    {
      MsgLogger.log(Level.DEBUG, "Detect email", null);
      boolean flag = alert.getDetector().getMsg();
      if (flag) {
        MsgLogger.log(Level.DEBUG, "Send alert", null);
        alert.getSender().sendAlert();
      } else {
        MsgLogger.log(Level.DEBUG, "None", null);
      }
    }
    catch (Exception e) {
      MsgLogger.log(Level.ERROR, e.getMessage(), e);
    }
  }

  public MailDetector getDetector()
  {
    return this.detector;
  }

  public AlertSender getSender() {
    return this.sender;
  }
}