package mail.alert;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Priority;

public class MsgLogger
{
  protected static Logger commonLogger = null;
  private static String FQCN = MsgLogger.class.getName();

  static { commonLogger = Logger.getLogger(MsgLogger.class);
    String loggerFolderPath = System.getProperty("user.dir") + "/log/";
    System.out.println("log path:" + loggerFolderPath);

    if (loggerFolderPath != null) {
      File loggerFolder = new File(loggerFolderPath);
      if (!loggerFolder.exists()) {
        loggerFolder.mkdirs();
      }
    }

    PatternLayout layout = new PatternLayout(getLogLayoutPattern());

    ConsoleAppender consoleAppender = new ConsoleAppender(layout);
    commonLogger.addAppender(consoleAppender);
    if (loggerFolderPath != null) {
      DailyRollingFileAppender fileAppender = null;
      try {
        fileAppender = new DailyRollingFileAppender(layout, 
          loggerFolderPath + "mailalert-log.txt", 
          "'.'yyyy-MM-dd");
      } catch (IOException e) {
        System.err.println("创建文件路径出错！路径：" + loggerFolderPath);
      }
      fileAppender.setEncoding("GBK");
      commonLogger.addAppender(fileAppender);
    }

    commonLogger.setLevel(getLogLevel()); }


  public static synchronized void log(Priority priority, Object message, Throwable t)
  {
    commonLogger.log(FQCN, priority, message, t);
  }

  public static synchronized Level getLogLevel() {
    String priorityName = "DEBUG";
    if (priorityName.equalsIgnoreCase("INFO"))
      return Level.INFO;
    if (priorityName.equalsIgnoreCase("DEBUG"))
      return Level.DEBUG;
    if (priorityName.equalsIgnoreCase("WARN"))
      return Level.WARN;
    if (priorityName.equalsIgnoreCase("ERROR"))
      return Level.ERROR;
    if (priorityName.equalsIgnoreCase("FATAL")) {
      return Level.FATAL;
    }
    return Level.INFO;
  }

  public static synchronized String getLogLayoutPattern() {
    String layout = "layout=%d{ISO8601}:[%p] --> %m  %l%n";
    return layout;
  }
}