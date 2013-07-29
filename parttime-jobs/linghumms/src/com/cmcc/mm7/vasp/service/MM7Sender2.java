package com.cmcc.mm7.vasp.service;

import com.cmcc.mm7.vasp.common.ConnectionPool;
import com.cmcc.mm7.vasp.common.ConnectionWrap;
import com.cmcc.mm7.vasp.common.RetriveApiVersion;
import com.cmcc.mm7.vasp.common.SOAPEncoder;
import com.cmcc.mm7.vasp.conf.MM7Config;
import com.cmcc.mm7.vasp.message.MM7CancelReq;
import com.cmcc.mm7.vasp.message.MM7CancelRes;
import com.cmcc.mm7.vasp.message.MM7RSErrorRes;
import com.cmcc.mm7.vasp.message.MM7RSRes;
import com.cmcc.mm7.vasp.message.MM7ReplaceReq;
import com.cmcc.mm7.vasp.message.MM7ReplaceRes;
import com.cmcc.mm7.vasp.message.MM7SubmitReq;
import com.cmcc.mm7.vasp.message.MM7SubmitRes;
import com.cmcc.mm7.vasp.message.MM7VASPReq;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import sun.misc.BASE64Encoder;

public class MM7Sender2
{
  private MM7Config mm7Config;
  private BufferedOutputStream sender;
  private BufferedInputStream receiver;
  private StringBuffer sb;
  private StringBuffer beforAuth;
  private String AuthInfor;
  private String DigestInfor;
  private StringBuffer afterAuth;
  private StringBuffer entityBody;
  private MM7RSRes res;
  private ByteArrayOutputStream baos;
  private ConnectionPool pool;
  private ConnectionWrap connWrap;
  private int ResendCount;
  private Socket client;
  private int ReadTimeOutCount = 0;
  private ByteArrayOutputStream sendBaos;
  private StringBuffer SevereBuffer;
  private StringBuffer InfoBuffer;
  private StringBuffer FinerBuffer;
  private ByteArrayOutputStream Severebaos;
  private ByteArrayOutputStream Infobaos;
  private ByteArrayOutputStream Finerbaos;
  private long LogTimeBZ;
  private long SameMinuteTime;
  private int N;
  private SimpleDateFormat sdf;
  private DecimalFormat df;
  private String logFileName;
  private byte[] TimeOutbCount;
  private ConnectionWrap TimeOutWrap;
  private boolean TimeOutFlag;
  public int tempnum = 0;
  private boolean bErrorFlag;
  private StringBuffer errorMessage;
  private String time;

  public MM7Sender2()
  {
    reset();
  }

  private void reset()
  {
    this.mm7Config = new MM7Config();
    this.sender = null;
    this.receiver = null;
    this.AuthInfor = "";
    this.DigestInfor = "";
    this.sb = new StringBuffer();
    this.beforAuth = new StringBuffer();
    this.afterAuth = new StringBuffer();
    this.entityBody = new StringBuffer();
    this.res = new MM7RSRes();
    this.baos = new ByteArrayOutputStream();
    this.ResendCount = 0;
    this.connWrap = null;
    this.pool = ConnectionPool.getInstance();
    this.client = null;
    this.sendBaos = new ByteArrayOutputStream();
    this.SevereBuffer = new StringBuffer();
    this.InfoBuffer = new StringBuffer();
    this.FinerBuffer = new StringBuffer();
    this.Severebaos = new ByteArrayOutputStream();
    this.Infobaos = new ByteArrayOutputStream();
    this.Finerbaos = new ByteArrayOutputStream();
    this.LogTimeBZ = System.currentTimeMillis();
    this.SameMinuteTime = System.currentTimeMillis();
    this.N = 1;
    this.sdf = new SimpleDateFormat("yyyyMMddHHmm");
    this.df = new DecimalFormat();
    this.df.applyLocalizedPattern("0000");
    this.logFileName = "";
    this.TimeOutbCount = null;
    this.TimeOutWrap = null;
    this.TimeOutFlag = false;
    this.bErrorFlag = false;
    this.errorMessage = new StringBuffer();
  }

  public MM7Sender2(MM7Config config) throws Exception
  {
    reset();
    this.mm7Config = config;
    this.pool.setConfig(this.mm7Config);
  }

  public void setConfig(MM7Config config) {
    this.mm7Config = config;
    this.pool.setConfig(this.mm7Config);
  }

  public MM7Config getConfig() {
    return this.mm7Config;
  }

  private void setSameMinuteTime(long time)
  {
    this.SameMinuteTime = time;
  }

  private long getSameMinuteTime() {
    return this.SameMinuteTime;
  }

  private void setErrorFlag(boolean flag) {
    this.bErrorFlag = flag;
  }

  private boolean getErrorFlag() {
    return this.bErrorFlag;
  }

  private void setErrorMessage(StringBuffer mes) {
    this.errorMessage = mes;
  }

  private StringBuffer getErrorMessage() {
    return this.errorMessage;
  }

  public MM7RSRes send(MM7VASPReq mm7VASPReq)
  {
    this.tempnum += 1;
    this.sb = new StringBuffer();
    this.beforAuth = new StringBuffer();
    this.afterAuth = new StringBuffer();
    this.entityBody = new StringBuffer();
    this.sendBaos = new ByteArrayOutputStream();
    this.SevereBuffer = new StringBuffer();
    this.InfoBuffer = new StringBuffer();
    this.FinerBuffer = new StringBuffer();

    this.Severebaos = new ByteArrayOutputStream();
    this.Finerbaos = new ByteArrayOutputStream();
    this.Infobaos = new ByteArrayOutputStream();

    this.sender = null;
    this.receiver = null;

    SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    if (mm7VASPReq == null)
    {
      MM7RSErrorRes ErrorRes = new MM7RSErrorRes();
      ErrorRes.setStatusCode(-105);
      ErrorRes.setStatusText("待发送的消息为空!");
      this.SevereBuffer.append("[Message_Type=MM7RSErrorRes]");
      this.SevereBuffer.append("[Comments={" + ErrorRes.getStatusCode());
      this.SevereBuffer.append(";" + ErrorRes.getStatusText() + "}]");
      return ErrorRes;
    }MM7RSErrorRes ErrorRes;
    try {
      String mmscURL = this.mm7Config.getMMSCURL();
      int httpindex = -1;
      int index = -1;
      if (mmscURL == null)
        mmscURL = "";
      this.beforAuth.append("POST " + mmscURL + " HTTP/1.1\r\n");

      if (this.pool.getIPCount() < this.mm7Config.getMMSCIP().size())
      {
        this.beforAuth.append("Host:" + (String)this.mm7Config.getMMSCIP().get(this.pool.getIPCount()) + "\r\n");

        this.pool.setIPCount(this.pool.getIPCount() + 1);
      }
      else
      {
        this.pool.setIPCount(0);
        this.beforAuth.append("Host:" + (String)this.mm7Config.getMMSCIP().get(0) + "\r\n");
        this.pool.setIPCount(this.pool.getIPCount() + 1);
      }
      this.SevereBuffer.append("Host:" + (String)this.mm7Config.getMMSCIP().get(0));

      if ((mm7VASPReq instanceof MM7SubmitReq))
      {
        MM7SubmitReq submitReq = (MM7SubmitReq)mm7VASPReq;
        this.InfoBuffer.append("[TransactionID=" + submitReq.getTransactionID() + "]");
        this.InfoBuffer.append("[Message_Type=MM7SubmitReq]");
        this.InfoBuffer.append("[Sender_Address=" + submitReq.getSenderAddress() + "]");
        this.InfoBuffer.append("[Recipient_Address={");
        if (submitReq.isToExist())
        {
          this.InfoBuffer.append("To={");
          List to = new ArrayList();
          to = submitReq.getTo();
          for (int i = 0; i < to.size(); i++)
          {
            this.InfoBuffer.append((String)to.get(i) + ",");
          }
          this.InfoBuffer.append("}");
        }
        if (submitReq.isCcExist())
        {
          this.InfoBuffer.append("Cc={");
          List cc = new ArrayList();
          cc = submitReq.getCc();
          for (int i = 0; i < cc.size(); i++)
          {
            this.InfoBuffer.append((String)cc.get(i) + ",");
          }
          this.InfoBuffer.append("}");
        }
        if (submitReq.isBccExist())
        {
          this.InfoBuffer.append("Bcc={");
          List bcc = new ArrayList();
          bcc = submitReq.getBcc();
          for (int i = 0; i < bcc.size(); i++)
          {
            this.InfoBuffer.append((String)bcc.get(i) + ",");
          }
          this.InfoBuffer.append("}");
        }
        this.InfoBuffer.append("}]\r\n");
        if (submitReq.isContentExist()) {
          this.beforAuth.append("Content-Type:multipart/related; boundary=\"-------------------------------------------------------NextPart_1\";type=\"text/xml\";start=\"</tnn-200102/mm7-vasp>\"\r\n");
        }
        else
          this.beforAuth.append("Content-Type:text/xml;charset=\"" + this.mm7Config.getCharSet() + "\"" + "\r\n");
      }
      else if ((mm7VASPReq instanceof MM7ReplaceReq))
      {
        MM7ReplaceReq replaceReq = (MM7ReplaceReq)mm7VASPReq;
        this.InfoBuffer.append("[TransactionID=" + replaceReq.getTransactionID() + "]");
        this.InfoBuffer.append("[Message_Type=MM7ReplaceReq]\r\n");
        if (replaceReq.isContentExist())
          this.beforAuth.append("Content-Type:multipart/related; boundary=\"-------------------------------------------------------NextPart_1\";\r\n");
        else
          this.beforAuth.append("Content-Type:text/xml;charset=\"" + this.mm7Config.getCharSet() + "\"" + "\r\n");
      }
      else if ((mm7VASPReq instanceof MM7CancelReq))
      {
        MM7CancelReq cancelReq = (MM7CancelReq)mm7VASPReq;
        this.InfoBuffer.append("[TransactionID=" + cancelReq.getTransactionID() + "]");
        this.InfoBuffer.append("[Message_Type=MM7CancelReq]\r\n");
        this.beforAuth.append("Content-Type:text/xml;charset=\"" + this.mm7Config.getCharSet() + "\"" + "\r\n");
      }
      else
      {
        ErrorRes = new MM7RSErrorRes();
        ErrorRes.setStatusCode(-106);
        ErrorRes.setStatusText("没有匹配的消息，请确认要发送的消息是否正确！");
        this.SevereBuffer.append("[Message_Type=MM7RSErrorRes]");
        this.SevereBuffer.append("[Comments={" + ErrorRes.getStatusCode());
        this.SevereBuffer.append(";" + ErrorRes.getStatusText() + "}]");
        return ErrorRes;
      }

      this.beforAuth.append("Content-Transfer-Encoding:8bit\r\n");
      this.AuthInfor = ("Authorization:Basic " + getBASE64(new StringBuffer().append(this.mm7Config.getUserName()).append(":").append(this.mm7Config.getPassword()).toString()) + "\r\n");

      this.afterAuth.append("SOAPAction:\"\"\r\n");
      RetriveApiVersion apiver = new RetriveApiVersion();
      this.afterAuth.append("MM7APIVersion:" + apiver.getApiVersion() + "\r\n");

      if (this.pool.getKeepAlive().equals("on"))
      {
        this.afterAuth.append("Connection: Keep-Alive\r\n");
      }
      else
      {
        this.afterAuth.append("Connection:Close\r\n");
      }

      byte[] bcontent = getContent(mm7VASPReq);
      if (getErrorFlag()) {
         ErrorRes = new MM7RSErrorRes();
        ErrorRes.setStatusCode(-114);
        ErrorRes.setStatusText(getErrorMessage().toString());
        this.SevereBuffer.append("[Message_Type=MM7RSErrorRes]");
        this.SevereBuffer.append("[Comments={" + ErrorRes.getStatusCode());
        this.SevereBuffer.append(";" + ErrorRes.getStatusText() + "}]");
        return ErrorRes;
      }

      if (bcontent.length > this.mm7Config.getMaxMsgSize())
      {
        ErrorRes = new MM7RSErrorRes();
        ErrorRes.setStatusCode(-113);
        ErrorRes.setStatusText("消息内容的尺寸超出允许发送的大小！");
        this.SevereBuffer.append("[Message_Type=MM7RSErrorRes]");
        this.SevereBuffer.append("[Comments={" + ErrorRes.getStatusCode());
        this.SevereBuffer.append(";" + ErrorRes.getStatusText() + "}]");
        return ErrorRes;
      }
      this.TimeOutbCount = bcontent;
      String env = "";
      try {
        ByteArrayOutputStream tempbaos = new ByteArrayOutputStream();
        tempbaos.write(bcontent);
        int envbeg = tempbaos.toString().indexOf("<?xml");
        int envend = tempbaos.toString().indexOf("</env:Envelope>");
        env = tempbaos.toString().substring(envbeg, envend);
        env = env + "</env:Envelope>";

        this.afterAuth.append("Content-Length:" + bcontent.length + "\r\n");
        this.afterAuth.append("Mime-Version:1.0\r\n");
        this.afterAuth.append("\r\n");
        this.entityBody.append(new String(bcontent));
        this.sendBaos = getSendMessage(bcontent);
        String time = "[" + simple.format(new Date(System.currentTimeMillis())) + "]";

        if (this.sendBaos != null) {
          this.res = SendandReceiveMessage(this.sendBaos);
          this.TimeOutFlag = false;
        }
        else {
          this.TimeOutFlag = false;
           ErrorRes = new MM7RSErrorRes();
          ErrorRes.setStatusCode(-104);
          ErrorRes.setStatusText("Socket不通！");
          this.SevereBuffer.append("[Message_Type=MM7RSErrorRes]");
          this.SevereBuffer.append("[Comments={" + ErrorRes.getStatusCode());
          this.SevereBuffer.append(";" + ErrorRes.getStatusText() + "}]");
          return ErrorRes;
        }

        this.FinerBuffer.append(this.InfoBuffer);
        this.InfoBuffer.append(env);
        this.SevereBuffer.insert(0, "\r\n\r\n" + time + "[1]");
        this.InfoBuffer.insert(0, "\r\n\r\n" + time + "[3]");
        this.FinerBuffer.insert(0, "\r\n\r\n" + time + "[6]");

        this.Severebaos.write(this.SevereBuffer.toString().getBytes());
        this.Infobaos.write(this.SevereBuffer.toString().getBytes());
        this.Infobaos.write(this.InfoBuffer.toString().getBytes());
        this.Finerbaos.write(this.SevereBuffer.toString().getBytes());
        this.Finerbaos.write(this.InfoBuffer.toString().getBytes());
        this.Finerbaos.write(this.FinerBuffer.toString().getBytes());
        this.Finerbaos.write(bcontent);

        this.InfoBuffer = new StringBuffer();
        this.FinerBuffer = new StringBuffer();
        time = "[" + simple.format(new Date(System.currentTimeMillis())) + "]";
        this.InfoBuffer.append("\r\n\r\n" + time + "[3]");
        this.FinerBuffer.append("\r\n\r\n" + time + "[6]");
        this.InfoBuffer.append("[TransactionID=" + this.res.getTransactionID() + "]");
        this.FinerBuffer.append("[TransactionID=" + this.res.getTransactionID() + "]");
        if ((this.res instanceof MM7SubmitRes)) {
          this.InfoBuffer.append("[Message_Type=MM7SubmitRes]");
          this.FinerBuffer.append("[Message_Type=MM7SubmitRes]");
        }
        else if ((this.res instanceof MM7CancelRes)) {
          this.InfoBuffer.append("[Message_Type=MM7CancelRes]");
          this.FinerBuffer.append("[Message_Type=MM7CancelRes]");
        }
        else if ((this.res instanceof MM7ReplaceRes)) {
          this.InfoBuffer.append("[Message_Type=MM7ReplaceRes]");
          this.FinerBuffer.append("[Message_Type=MM7ReplaceRes]");
        }
        else if ((this.res instanceof MM7RSErrorRes)) {
          this.InfoBuffer.append("[Message_Type=MM7RSErrorRes]");
          this.FinerBuffer.append("[Message_Type=MM7RSErrorRes]");
        }
        this.InfoBuffer.append("[Comments={" + this.res.getStatusCode() + ";" + this.res.getStatusText() + "}]\r\n");

        this.FinerBuffer.append("[Comments={" + this.res.getStatusCode() + ";" + this.res.getStatusText() + "}]\r\n");

        envbeg = this.baos.toString().indexOf("<?xml");
         envend = this.baos.toString().indexOf("</env:Envelope>");
        if ((envbeg > 0) && (envend > 0))
          env = this.baos.toString().substring(envbeg);
        this.InfoBuffer.append(env);
        this.FinerBuffer.append(env);

        this.Infobaos.write(this.InfoBuffer.toString().getBytes());
        this.Finerbaos.write(this.InfoBuffer.toString().getBytes());
        this.Finerbaos.write(this.FinerBuffer.toString().getBytes());

        int LogLevel = this.mm7Config.getLogLevel();
        if (LogLevel > 0) {
          String LogPath = this.mm7Config.getLogPath();
          int LogNum = this.mm7Config.getLogNum();
          int LogInterval = this.mm7Config.getLogInterval();
          int LogSize = this.mm7Config.getLogSize();
          long Interval = System.currentTimeMillis() - this.LogTimeBZ;
          String sTimeNow = this.sdf.format(new Date(System.currentTimeMillis()));
          long timeFile = 0L;
          long timeNow = this.sdf.parse(sTimeNow).getTime();
          if (this.logFileName.length() > 0) {
            File logFile = new File(this.logFileName);
            int index1 = this.logFileName.indexOf(this.mm7Config.getMmscId() + "_");
            int index11 = index1 + this.mm7Config.getMmscId().length() + 1;
            int index2 = this.logFileName.indexOf(".", index11);
            String sTimeFile = this.logFileName.substring(index1 + this.mm7Config.getMmscId().length() + 1, index2);

            timeFile = this.sdf.parse(sTimeFile).getTime();

            if (timeNow - timeFile > LogInterval * 60L * 1000L) {
              this.N = 1;
              deleteFile(LogPath, LogNum, this.mm7Config.getMmscId());
              this.logFileName = (LogPath + "/" + this.mm7Config.getMmscId() + "_" + sTimeNow + "." + this.df.format(this.N) + ".log");
            }
            else if (logFile.length() > LogSize * 1024) {
              if (this.N < LogNum)
                this.N += 1;
              else
                this.N = 1;
              deleteFile(LogPath, LogNum, this.mm7Config.getMmscId());
              this.logFileName = (LogPath + "/" + this.mm7Config.getMmscId() + "_" + sTimeFile + "." + this.df.format(this.N) + ".log");
            }

          }
          else
          {
            this.N = 1;
            deleteFile(LogPath, LogNum, this.mm7Config.getMmscId());
            this.logFileName = (LogPath + "/" + this.mm7Config.getMmscId() + "_" + sTimeNow + "." + this.df.format(this.N) + ".log");
          }

          switch (LogLevel) {
          case 1:
            try {
              FileOutputStream fos = new FileOutputStream(this.logFileName, true);
              fos.write(this.Severebaos.toByteArray());
              fos.close();
              this.SevereBuffer = new StringBuffer();
            }
            catch (IOException ioe) {
              ioe.printStackTrace();
            }
            break;
          case 2:
            break;
          case 3:
            try {
              FileOutputStream fos = new FileOutputStream(this.logFileName, true);
              fos.write(this.Infobaos.toByteArray());
              fos.close();
              this.Infobaos.reset();
            }
            catch (IOException ioe) {
              ioe.printStackTrace();
            }
            break;
          case 4:
            break;
          case 6:
            try {
              FileOutputStream fos = new FileOutputStream(this.logFileName, true);
              fos.write(this.Finerbaos.toByteArray());
              fos.close();
              this.Finerbaos = new ByteArrayOutputStream();
            }
            catch (IOException ioe) {
              ioe.printStackTrace();
            }
            break;
          case 7:
          case 5:
          }
        }
      }
      catch (IOException ioe)
      {
        ioe.printStackTrace();

        this.afterAuth.append("Content-Length:" + bcontent.length + "\r\n");
        this.afterAuth.append("Mime-Version:1.0\r\n");
        this.afterAuth.append("\r\n");
        this.entityBody.append(new String(bcontent));
        this.sendBaos = getSendMessage(bcontent);
        String time = "[" + simple.format(new Date(System.currentTimeMillis())) + "]";

        if (this.sendBaos != null) {
          this.res = SendandReceiveMessage(this.sendBaos);
          this.TimeOutFlag = false;
        }
        else {
          this.TimeOutFlag = false;
           ErrorRes = new MM7RSErrorRes();
          ErrorRes.setStatusCode(-104);
          ErrorRes.setStatusText("Socket不通！");
          this.SevereBuffer.append("[Message_Type=MM7RSErrorRes]");
          this.SevereBuffer.append("[Comments={" + ErrorRes.getStatusCode());
          this.SevereBuffer.append(";" + ErrorRes.getStatusText() + "}]");
          return ErrorRes;
        }

        this.FinerBuffer.append(this.InfoBuffer);
        this.InfoBuffer.append(env);
        this.SevereBuffer.insert(0, "\r\n\r\n" + time + "[1]");
        this.InfoBuffer.insert(0, "\r\n\r\n" + time + "[3]");
        this.FinerBuffer.insert(0, "\r\n\r\n" + time + "[6]");

        this.Severebaos.write(this.SevereBuffer.toString().getBytes());
        this.Infobaos.write(this.SevereBuffer.toString().getBytes());
        this.Infobaos.write(this.InfoBuffer.toString().getBytes());
        this.Finerbaos.write(this.SevereBuffer.toString().getBytes());
        this.Finerbaos.write(this.InfoBuffer.toString().getBytes());
        this.Finerbaos.write(this.FinerBuffer.toString().getBytes());
        this.Finerbaos.write(bcontent);

        this.InfoBuffer = new StringBuffer();
        this.FinerBuffer = new StringBuffer();
        time = "[" + simple.format(new Date(System.currentTimeMillis())) + "]";
        this.InfoBuffer.append("\r\n\r\n" + time + "[3]");
        this.FinerBuffer.append("\r\n\r\n" + time + "[6]");
        this.InfoBuffer.append("[TransactionID=" + this.res.getTransactionID() + "]");
        this.FinerBuffer.append("[TransactionID=" + this.res.getTransactionID() + "]");
        if ((this.res instanceof MM7SubmitRes)) {
          this.InfoBuffer.append("[Message_Type=MM7SubmitRes]");
          this.FinerBuffer.append("[Message_Type=MM7SubmitRes]");
        }
        else if ((this.res instanceof MM7CancelRes)) {
          this.InfoBuffer.append("[Message_Type=MM7CancelRes]");
          this.FinerBuffer.append("[Message_Type=MM7CancelRes]");
        }
        else if ((this.res instanceof MM7ReplaceRes)) {
          this.InfoBuffer.append("[Message_Type=MM7ReplaceRes]");
          this.FinerBuffer.append("[Message_Type=MM7ReplaceRes]");
        }
        else if ((this.res instanceof MM7RSErrorRes)) {
          this.InfoBuffer.append("[Message_Type=MM7RSErrorRes]");
          this.FinerBuffer.append("[Message_Type=MM7RSErrorRes]");
        }
        this.InfoBuffer.append("[Comments={" + this.res.getStatusCode() + ";" + this.res.getStatusText() + "}]\r\n");

        this.FinerBuffer.append("[Comments={" + this.res.getStatusCode() + ";" + this.res.getStatusText() + "}]\r\n");

        int envbeg = this.baos.toString().indexOf("<?xml");
        int envend = this.baos.toString().indexOf("</env:Envelope>");
        if ((envbeg > 0) && (envend > 0))
          env = this.baos.toString().substring(envbeg);
        this.InfoBuffer.append(env);
        this.FinerBuffer.append(env);

        this.Infobaos.write(this.InfoBuffer.toString().getBytes());
        this.Finerbaos.write(this.InfoBuffer.toString().getBytes());
        this.Finerbaos.write(this.FinerBuffer.toString().getBytes());

        int LogLevel = this.mm7Config.getLogLevel();
        if (LogLevel > 0) {
          String LogPath = this.mm7Config.getLogPath();
          int LogNum = this.mm7Config.getLogNum();
          int LogInterval = this.mm7Config.getLogInterval();
          int LogSize = this.mm7Config.getLogSize();
          long Interval = System.currentTimeMillis() - this.LogTimeBZ;
          String sTimeNow = this.sdf.format(new Date(System.currentTimeMillis()));
          long timeFile = 0L;
          long timeNow = this.sdf.parse(sTimeNow).getTime();
          if (this.logFileName.length() > 0) {
            File logFile = new File(this.logFileName);
            int index1 = this.logFileName.indexOf(this.mm7Config.getMmscId() + "_");
            int index11 = index1 + this.mm7Config.getMmscId().length() + 1;
            int index2 = this.logFileName.indexOf(".", index11);
            String sTimeFile = this.logFileName.substring(index1 + this.mm7Config.getMmscId().length() + 1, index2);

            timeFile = this.sdf.parse(sTimeFile).getTime();

            if (timeNow - timeFile > LogInterval * 60L * 1000L) {
              this.N = 1;
              deleteFile(LogPath, LogNum, this.mm7Config.getMmscId());
              this.logFileName = (LogPath + "/" + this.mm7Config.getMmscId() + "_" + sTimeNow + "." + this.df.format(this.N) + ".log");
            }
            else if (logFile.length() > LogSize * 1024) {
              if (this.N < LogNum)
                this.N += 1;
              else
                this.N = 1;
              deleteFile(LogPath, LogNum, this.mm7Config.getMmscId());
              this.logFileName = (LogPath + "/" + this.mm7Config.getMmscId() + "_" + sTimeFile + "." + this.df.format(this.N) + ".log");
            }

          }
          else
          {
            this.N = 1;
            deleteFile(LogPath, LogNum, this.mm7Config.getMmscId());
            this.logFileName = (LogPath + "/" + this.mm7Config.getMmscId() + "_" + sTimeNow + "." + this.df.format(this.N) + ".log");
          }

          switch (LogLevel) {
          case 1:
            try {
              FileOutputStream fos = new FileOutputStream(this.logFileName, true);
              fos.write(this.Severebaos.toByteArray());
              fos.close();
              this.SevereBuffer = new StringBuffer();
            }
            catch (IOException e) {
              e.printStackTrace();
            }
            break;
          case 2:
            break;
          case 3:
            try {
              FileOutputStream fos = new FileOutputStream(this.logFileName, true);
              fos.write(this.Infobaos.toByteArray());
              fos.close();
              this.Infobaos.reset();
            }
            catch (IOException e) {
              e.printStackTrace();
            }
            break;
          case 4:
            break;
          case 6:
            try {
              FileOutputStream fos = new FileOutputStream(this.logFileName, true);
              fos.write(this.Finerbaos.toByteArray());
              fos.close();
              this.Finerbaos = new ByteArrayOutputStream();
            }
            catch (IOException e) {
              e.printStackTrace();
            }
            break;
          case 7:
          case 5:
          }
        }
      }
      finally
      {
        this.afterAuth.append("Content-Length:" + bcontent.length + "\r\n");
        this.afterAuth.append("Mime-Version:1.0\r\n");
        this.afterAuth.append("\r\n");
        this.entityBody.append(new String(bcontent));
        this.sendBaos = getSendMessage(bcontent);
       String time = "[" + simple.format(new Date(System.currentTimeMillis())) + "]";

        if (this.sendBaos != null) {
          this.res = SendandReceiveMessage(this.sendBaos);
          this.TimeOutFlag = false;
        }
        else {
          this.TimeOutFlag = false;
           ErrorRes = new MM7RSErrorRes();
          ErrorRes.setStatusCode(-104);
          ErrorRes.setStatusText("Socket不通！");
          this.SevereBuffer.append("[Message_Type=MM7RSErrorRes]");
          this.SevereBuffer.append("[Comments={" + ErrorRes.getStatusCode());
          this.SevereBuffer.append(";" + ErrorRes.getStatusText() + "}]");
          return ErrorRes;
        }
      }

      this.FinerBuffer.append(this.InfoBuffer);
      this.InfoBuffer.append(env);
      this.SevereBuffer.insert(0, "\r\n\r\n" + time + "[1]");
      this.InfoBuffer.insert(0, "\r\n\r\n" + time + "[3]");
      this.FinerBuffer.insert(0, "\r\n\r\n" + time + "[6]");

      this.Severebaos.write(this.SevereBuffer.toString().getBytes());
      this.Infobaos.write(this.SevereBuffer.toString().getBytes());
      this.Infobaos.write(this.InfoBuffer.toString().getBytes());
      this.Finerbaos.write(this.SevereBuffer.toString().getBytes());
      this.Finerbaos.write(this.InfoBuffer.toString().getBytes());
      this.Finerbaos.write(this.FinerBuffer.toString().getBytes());
      this.Finerbaos.write(bcontent);

      this.InfoBuffer = new StringBuffer();
      this.FinerBuffer = new StringBuffer();
       time = "[" + simple.format(new Date(System.currentTimeMillis())) + "]";
      this.InfoBuffer.append("\r\n\r\n" + time + "[3]");
      this.FinerBuffer.append("\r\n\r\n" + time + "[6]");
      this.InfoBuffer.append("[TransactionID=" + this.res.getTransactionID() + "]");
      this.FinerBuffer.append("[TransactionID=" + this.res.getTransactionID() + "]");
      if ((this.res instanceof MM7SubmitRes)) {
        this.InfoBuffer.append("[Message_Type=MM7SubmitRes]");
        this.FinerBuffer.append("[Message_Type=MM7SubmitRes]");
      }
      else if ((this.res instanceof MM7CancelRes)) {
        this.InfoBuffer.append("[Message_Type=MM7CancelRes]");
        this.FinerBuffer.append("[Message_Type=MM7CancelRes]");
      }
      else if ((this.res instanceof MM7ReplaceRes)) {
        this.InfoBuffer.append("[Message_Type=MM7ReplaceRes]");
        this.FinerBuffer.append("[Message_Type=MM7ReplaceRes]");
      }
      else if ((this.res instanceof MM7RSErrorRes)) {
        this.InfoBuffer.append("[Message_Type=MM7RSErrorRes]");
        this.FinerBuffer.append("[Message_Type=MM7RSErrorRes]");
      }
      this.InfoBuffer.append("[Comments={" + this.res.getStatusCode() + ";" + this.res.getStatusText() + "}]\r\n");

      this.FinerBuffer.append("[Comments={" + this.res.getStatusCode() + ";" + this.res.getStatusText() + "}]\r\n");

      int envbeg = this.baos.toString().indexOf("<?xml");
      int envend = this.baos.toString().indexOf("</env:Envelope>");
      if ((envbeg > 0) && (envend > 0))
        env = this.baos.toString().substring(envbeg);
      this.InfoBuffer.append(env);
      this.FinerBuffer.append(env);

      this.Infobaos.write(this.InfoBuffer.toString().getBytes());
      this.Finerbaos.write(this.InfoBuffer.toString().getBytes());
      this.Finerbaos.write(this.FinerBuffer.toString().getBytes());

      int LogLevel = this.mm7Config.getLogLevel();
      if (LogLevel > 0) {
        String LogPath = this.mm7Config.getLogPath();
        int LogNum = this.mm7Config.getLogNum();
        int LogInterval = this.mm7Config.getLogInterval();
        int LogSize = this.mm7Config.getLogSize();
        long Interval = System.currentTimeMillis() - this.LogTimeBZ;
        String sTimeNow = this.sdf.format(new Date(System.currentTimeMillis()));
        long timeFile = 0L;
        long timeNow = this.sdf.parse(sTimeNow).getTime();
        if (this.logFileName.length() > 0) {
          File logFile = new File(this.logFileName);
          int index1 = this.logFileName.indexOf(this.mm7Config.getMmscId() + "_");
          int index11 = index1 + this.mm7Config.getMmscId().length() + 1;
          int index2 = this.logFileName.indexOf(".", index11);
          String sTimeFile = this.logFileName.substring(index1 + this.mm7Config.getMmscId().length() + 1, index2);

          timeFile = this.sdf.parse(sTimeFile).getTime();

          if (timeNow - timeFile > LogInterval * 60L * 1000L) {
            this.N = 1;
            deleteFile(LogPath, LogNum, this.mm7Config.getMmscId());
            this.logFileName = (LogPath + "/" + this.mm7Config.getMmscId() + "_" + sTimeNow + "." + this.df.format(this.N) + ".log");
          }
          else if (logFile.length() > LogSize * 1024) {
            if (this.N < LogNum)
              this.N += 1;
            else
              this.N = 1;
            deleteFile(LogPath, LogNum, this.mm7Config.getMmscId());
            this.logFileName = (LogPath + "/" + this.mm7Config.getMmscId() + "_" + sTimeFile + "." + this.df.format(this.N) + ".log");
          }

        }
        else
        {
          this.N = 1;
          deleteFile(LogPath, LogNum, this.mm7Config.getMmscId());
          this.logFileName = (LogPath + "/" + this.mm7Config.getMmscId() + "_" + sTimeNow + "." + this.df.format(this.N) + ".log");
        }

        switch (LogLevel) {
        case 1:
          try {
            FileOutputStream fos = new FileOutputStream(this.logFileName, true);
            fos.write(this.Severebaos.toByteArray());
            fos.close();
            this.SevereBuffer = new StringBuffer();
          }
          catch (IOException ioe) {
            ioe.printStackTrace();
          }
          break;
        case 2:
          break;
        case 3:
          try {
            FileOutputStream fos = new FileOutputStream(this.logFileName, true);
            fos.write(this.Infobaos.toByteArray());
            fos.close();
            this.Infobaos.reset();
          }
          catch (IOException ioe) {
            ioe.printStackTrace();
          }
          break;
        case 4:
          break;
        case 6:
          try {
            FileOutputStream fos = new FileOutputStream(this.logFileName, true);
            fos.write(this.Finerbaos.toByteArray());
            fos.close();
            this.Finerbaos = new ByteArrayOutputStream();
          }
          catch (IOException ioe) {
            ioe.printStackTrace();
          }
          break;
        case 7:
          break;
        case 5:
        }
      }

      return this.res;
    }
    catch (Exception e) {
      ErrorRes = new MM7RSErrorRes();
      e.printStackTrace();
      ErrorRes.setStatusCode(-100);
      ErrorRes.setStatusText("系统错误！原因：" + e);
    }return ErrorRes;
  }

  private void deleteFile(String logpath, int lognum, String MMSCID)
  {
    File parfile = new File(logpath);
    if (parfile.isDirectory())
    {
      File[] subfile = parfile.listFiles();
      List list = new ArrayList();
      for (int i = 0; i < subfile.length; i++) {
        String name = subfile[i].getName();
        if (name.indexOf(MMSCID) >= 0) {
          list.add(name);
        }
      }
      if (list.size() >= lognum)
      {
        int deleteLength = list.size() - lognum + 1;
        Comparator comp = Collections.reverseOrder();
        Collections.sort(list, comp);
        for (int i = list.size() - deleteLength; i < list.size(); i++) {
          String strfile = (String)list.get(i);
          File ff = new File(logpath + "/" + strfile);
          ff.delete();
        }
      }
    }
  }

  private byte[] getContent(MM7VASPReq mm7VASPReq)
  {
    byte[] b = null;
    SOAPEncoder encoder = new SOAPEncoder(this.mm7Config);
    encoder.setMessage(mm7VASPReq);
    try {
      encoder.encodeMessage();
      if (encoder.getErrorFlag())
      {
        setErrorFlag(true);
        setErrorMessage(encoder.getErrorMessage());
      }
    } catch (Exception e) {
      System.err.println(e);
    }
    b = encoder.getMessage();
    return b;
  }

  public static String getBASE64(String value)
  {
    if (value == null)
      return null;
    BASE64Encoder BaseEncode = new BASE64Encoder();
    return BaseEncode.encode(value.getBytes());
  }
  private MM7RSRes parseXML() {
    SAXBuilder sax = new SAXBuilder();
    this.SevereBuffer.append("recv=" + this.baos.toString() + "\r\n");
    MM7RSErrorRes error;
    try { if ((this.baos.toString() == null) || (this.baos.toString().equals("")))
      {
        MM7RSErrorRes errRes = new MM7RSErrorRes();
        errRes.setStatusCode(-107);
        errRes.setStatusText("错误！接收到的消息为空！");
        this.SevereBuffer.append("[Message_Type=MM7RSErrorRes]");
        this.SevereBuffer.append("[Comments={-107;错误！接收到的消息为空！}]");
        return errRes;
      }

      int index = -1;
      int xmlend = -1;
      index = this.baos.toString().indexOf("<?xml");
      if (index == -1)
      {
        int httpindex = -1;
        httpindex = this.baos.toString().indexOf("HTTP1.1");
        String strstat = "";
        if (httpindex >= 0)
        {
          int index11 = this.baos.toString().indexOf("\r\n");
          strstat = this.baos.toString().substring(httpindex + 7, index11);
        }
        MM7RSErrorRes err = new MM7RSErrorRes();
        err.setStatusCode(-108);
        if (!strstat.equals(""))
          err.setStatusText(strstat);
        else
          err.setStatusText("Bad Request");
        this.SevereBuffer.append("[Message_Type=MM7RSErrorRes]");
        this.SevereBuffer.append("[Comments={" + err.getStatusCode() + ";" + err.getStatusText() + "}]");
        return err;
      }
      String xmlContent = this.baos.toString().substring(index, this.baos.toString().length());
      String xmlContentTemp = "";
      byte[] byteXML = this.baos.toByteArray();
      int index1 = xmlContent.indexOf("encoding=\"UTF-8\"");
      if (index1 > 0)
      {
        xmlContentTemp = new String(byteXML, "UTF-8");
        int xmlind = xmlContentTemp.indexOf("<?xml");
        int xmlindend = xmlContentTemp.lastIndexOf("Envelope>");
        if (xmlindend > 0)
        {
          xmlContentTemp = xmlContentTemp.substring(xmlind, xmlindend + 9);
          String xml = xmlContentTemp.substring(0, index1) + "encoding=\"GB2312\"" + xmlContentTemp.substring(index1 + "encoding=\"UTF-8\"".length());

          xmlContent = xml;
        }
      }
      this.SevereBuffer.append("!xmlContent=" + xmlContent + "!\r\n");
      ByteArrayInputStream in = new ByteArrayInputStream(xmlContent.getBytes());
      Document doc = sax.build(in);
      Element root = doc.getRootElement();
      Element first = (Element)root.getChildren().get(0);
      if (first.getName().equalsIgnoreCase("body"))
      {
        Element Message = (Element)first.getChildren().get(0);
        if (Message.getName().equals("Fault"))
        {
          MM7RSErrorRes errRes = new MM7RSErrorRes();
          errRes.setStatusCode(-110);
          errRes.setStatusText("Server could not fulfill the request");
          this.SevereBuffer.append("[Message_Type=MM7RSErrorRes]");
          this.SevereBuffer.append("[Comments={-110;Server could not fulfill the request}]");

          return errRes;
        }

        MM7RSErrorRes errRes = new MM7RSErrorRes();
        errRes.setStatusCode(-111);
        errRes.setStatusText("Server error");
        this.SevereBuffer.append("[Message_Type=MM7RSErrorRes]");
        this.SevereBuffer.append("[Comments={-111;Server error}]");
        return errRes;
      }

      Element envBody = (Element)root.getChildren().get(1);
      Element Message = (Element)envBody.getChildren().get(0);
      Element envHeader = (Element)root.getChildren().get(0);
      Element transID = (Element)envHeader.getChildren().get(0);
      String transactionID = transID.getTextTrim();
      int size = Message.getChildren().size();
      this.SevereBuffer.append("\r\nMessage.getName()=" + Message.getName() + "\r\n");

      if (Message.getName().equals("SubmitRsp")) {
        MM7SubmitRes submitRes = new MM7SubmitRes();
        submitRes.setTransactionID(transactionID);
        for (int i = 0; i < size; i++) {
          Element ele = (Element)Message.getChildren().get(i);
          if (ele.getName().equals("Status")) {
            for (int j = 0; j < ele.getChildren().size(); j++) {
              Element subEle = (Element)ele.getChildren().get(j);
              if (subEle.getName().equals("StatusCode"))
                submitRes.setStatusCode(Integer.parseInt(subEle.getTextTrim()));
              else if (subEle.getName().equals("StatusText"))
                submitRes.setStatusText(subEle.getTextTrim());
              else if (subEle.getName().equals("Details"))
                submitRes.setStatusDetail(subEle.getTextTrim());
            }
          }
          else if (ele.getName().equals("MessageID")) {
            submitRes.setMessageID(ele.getTextTrim());
          }
        }
        return submitRes;
      }
      if (Message.getName().equals("CancelRsp")) {
        MM7CancelRes cancelRes = new MM7CancelRes();
        cancelRes.setTransactionID(transactionID);
        for (int i = 0; i < size; i++) {
          Element ele = (Element)Message.getChildren().get(i);
          if (ele.getName().equals("Status")) {
            for (int j = 0; j < ele.getChildren().size(); j++) {
              Element subEle = (Element)ele.getChildren().get(j);
              if (subEle.getName().equals("StatusCode"))
                cancelRes.setStatusCode(Integer.parseInt(subEle.getTextTrim()));
              else if (subEle.getName().equals("StatusText"))
                cancelRes.setStatusText(subEle.getTextTrim());
              else if (subEle.getName().equals("Details"))
                cancelRes.setStatusDetail(subEle.getTextTrim());
            }
          }
        }
        return cancelRes;
      }
      if (Message.getName().equals("ReplaceRsp")) {
        MM7ReplaceRes replaceRes = new MM7ReplaceRes();
        replaceRes.setTransactionID(transactionID);
        for (int i = 0; i < size; i++) {
          Element ele = (Element)Message.getChildren().get(i);
          if (ele.getName().equals("Status")) {
            for (int j = 0; j < ele.getChildren().size(); j++) {
              Element subEle = (Element)ele.getChildren().get(j);
              if (subEle.getName().equals("StatusCode")) {
                replaceRes.setStatusCode(Integer.parseInt(subEle.getTextTrim()));
              }
              else if (subEle.getName().equals("StatusText"))
                replaceRes.setStatusText(subEle.getTextTrim());
              else if (subEle.getName().equals("Details"))
                replaceRes.setStatusDetail(subEle.getTextTrim());
            }
          }
        }
        return replaceRes;
      }

      MM7RSRes res = new MM7RSRes();
      res.setTransactionID(transactionID);
      for (int i = 0; i < size; i++) {
        Element ele = (Element)Message.getChildren().get(i);
        if (ele.getName().equals("Status")) {
          for (int j = 0; j < ele.getChildren().size(); j++) {
            Element subEle = (Element)ele.getChildren().get(j);
            if (subEle.getName().equals("StatusCode"))
              res.setStatusCode(Integer.parseInt(subEle.getTextTrim()));
            else if (subEle.getName().equals("StatusText"))
              res.setStatusText(subEle.getTextTrim());
            else if (subEle.getName().equals("Details"))
              res.setStatusDetail(subEle.getTextTrim());
          }
        }
      }
      return res;
    }
    catch (JDOMException jdome)
    {
       error = new MM7RSErrorRes();
      System.err.print(jdome);
      error.setStatusCode(-109);
      error.setStatusText("XML解析错误！原因：" + jdome);
      this.SevereBuffer.append("[Message_Type=MM7RSErrorRes]");
      this.SevereBuffer.append("[Comments={-109;" + error.getStatusText() + "}]");
      return error;
    }
    catch (Exception e) {
      e.printStackTrace();
      error = new MM7RSErrorRes();
      error.setStatusCode(-100);
      error.setStatusText("系统错误！原因：" + e);
    }return error;
  }

  private ByteArrayOutputStream getSendMessage(byte[] bcontent)
  {
    try
    {
      if (this.pool.getKeepAlive().equals("on")) {
        this.connWrap = null;
        if (this.TimeOutFlag == true)
        {
          this.SevereBuffer.append("TimeOutFlag=true\r\n");
          if (this.pool.deleteURL(this.TimeOutWrap))
          {
            this.SevereBuffer.append("  TimeOutWrap=" + this.TimeOutWrap.getSocket());
            this.SevereBuffer.append("  pool.deleteURL(TimeOutWrap)");
            Socket tempSoc = this.TimeOutWrap.getSocket();
            String tempip = tempSoc.getInetAddress().getHostAddress() + ":" + tempSoc.getPort();
            this.connWrap = this.pool.getConnWrap(tempip);
            this.TimeOutWrap = this.connWrap;
            if (this.connWrap != null) {
              this.SevereBuffer.append("  connWrap != null");
              this.client = this.connWrap.getSocket();
            }
            else
            {
              this.SevereBuffer.append("   client=null");
              this.client = null;
              return null;
            }
          }
          else
          {
            this.SevereBuffer.append("deleteURL is false!");
            return null;
          }
        }
        else
        {
          this.SevereBuffer.append("No.815 mm7config.mmscip=" + (String)this.mm7Config.getMMSCIP().get(0) + "!");
          this.connWrap = this.pool.getConnWrap((String)this.mm7Config.getMMSCIP().get(0));
          this.TimeOutWrap = this.connWrap;
          if (this.connWrap != null)
          {
            this.client = this.connWrap.getSocket();
            this.SevereBuffer.append("!!!!client=" + this.client);

            if ((!this.connWrap.getUserfulFlag()) || (!this.client.isConnected())) {
              this.SevereBuffer.append("!771!connWrap.getUserfulFlag() == false || client.isConnected() == false");
              this.pool.deleteURL(this.connWrap);
              Socket tempSoc = this.connWrap.getSocket();
              String tempip = tempSoc.getInetAddress().getHostAddress() + ":" + tempSoc.getPort();
              this.connWrap = this.pool.getConnWrap(tempip);
              this.TimeOutWrap = this.connWrap;
            }
            if (this.connWrap.getAuthFlag() == true) {
              this.AuthInfor = this.connWrap.getDigestInfor();
            }

          }
          else
          {
            this.client = null;
            return null;
          }
        }
      }
      else {
        String MMSCIP = (String)this.mm7Config.getMMSCIP().get(0);
        int index = MMSCIP.indexOf(":");
        String ip;
        int port;
        if (index == -1)
        {
          ip = MMSCIP;
          port = 80;
        }
        else
        {
          ip = MMSCIP.substring(0, index);
          port = Integer.parseInt(MMSCIP.substring(index + 1));
        }
        this.client = new Socket(ip, port);
      }
      if (this.client != null) {
        this.SevereBuffer.append("!835!client != null and equals = " + this.client);
        this.sender = new BufferedOutputStream(this.client.getOutputStream());
        this.receiver = new BufferedInputStream(this.client.getInputStream());
        this.client.setSoTimeout(this.mm7Config.getTimeOut());

        this.client.setKeepAlive(true);

        this.sb = new StringBuffer();
        this.sb.append(this.beforAuth);
        this.sb.append(this.AuthInfor);
        this.sb.append(this.afterAuth);
        try {
          this.sendBaos = new ByteArrayOutputStream();
          this.SevereBuffer.append("!part of send message is:" + this.sb.toString() + "!\r\n");
          this.sendBaos.write(this.sb.toString().getBytes());
          this.sendBaos.write(bcontent);
          return this.sendBaos;
        }
        catch (IOException e) {
          this.SevereBuffer.append("IOException=" + e + "\r\n");
          return null;
        }

      }

      this.SevereBuffer.append("!853!client == null");
      return null;
    }
    catch (UnknownHostException uhe)
    {
      this.SevereBuffer.append("UnknownHostExcepion=" + uhe + "\r\n");
      return null;
    }
    catch (SocketException se)
    {
      this.SevereBuffer.append("SocketException=" + se + "\r\n");
      return null;
    }
    catch (InterruptedIOException iioe)
    {
      this.TimeOutFlag = true;
      for (int j = 0; j < this.mm7Config.getReSendCount(); j++)
      {
        this.sendBaos = getSendMessage(bcontent);
        if (this.sendBaos != null)
          this.res = SendandReceiveMessage(this.sendBaos);
      }
      this.res.setStatusCode(-101);
      this.res.setStatusText("超时发送失败！原因：" + iioe);
      this.SevereBuffer.append("[Comments={-101;超时发送失败！原因：" + iioe + "}]\r\n");
      return null;
    }
    catch (Exception e)
    {
      e.printStackTrace();
      this.res.setStatusCode(-100);
      this.res.setStatusText("系统错误！原因：" + e);
      this.SevereBuffer.append("[Comments={-100;系统错误！原因：" + e + "}]\r\n");
    }return null;
  }

  private MM7RSRes SendandReceiveMessage(ByteArrayOutputStream sendbaos) {
    MM7RSErrorRes error;
    try {
      this.sender.write(sendbaos.toByteArray());
      this.sender.flush();
      if (receiveMessge())
      {
        this.res = parseXML();
        return this.res;
      }

      error = new MM7RSErrorRes();

      error.setStatusCode(-102);
      error.setStatusText("接收失败！");
      this.SevereBuffer.append("[Message_Type=MM7RSErrorRes]");
      this.SevereBuffer.append("[Comments={-102;" + error.getStatusText() + "}]");
      return error;
    }
    catch (IOException ioe)
    {
      this.SevereBuffer.append("enter SendandReceiveMessage's IOException=" + ioe + "\r\n");
      error = new MM7RSErrorRes();
      error.setStatusCode(-115);
      error.setStatusText("IO异常");
      this.SevereBuffer.append("[Message_Type=MM7RSErrorRes]");
      this.SevereBuffer.append("[Comments={-115;" + error.getStatusText() + "}]");
    }return error;
  }

  public boolean receiveMessge() throws IOException
  {
    try
    {
      long temptime1 = System.currentTimeMillis();
      this.baos.reset();
      boolean bReceive = false;
      byte[] data = new byte[256];
      int word = -1;
      int readline = -1;
      int totalline = 0;
      boolean flag = false;
      boolean bHead = false;
      int readlineOut = 0;
      int envelopecount = 0;

      while (!flag)
      {
        readline = this.receiver.read(data);
        long temptime2 = System.currentTimeMillis();
        if ((readlineOut >= 10) || (temptime2 - temptime1 >= this.mm7Config.getTimeOut()))
        {
          break;
        }

        if (readline != -1) {
          this.baos.write(data, 0, readline);
          totalline += readline;
          if (this.baos.toString().indexOf("\r\n\r\n") < 0)
            continue;
          if (!bHead) {
            int httpindex = this.baos.toString().indexOf("HTTP/1.1");
            if (httpindex != -1) {
              String httpCode = this.baos.toString().substring(httpindex + 8, httpindex + 12).trim();

              int httpsepindex = this.baos.toString().indexOf("\r\n\r\n");
              if (httpCode.equals("401")) {
                if (this.baos.toString().indexOf("Digest") != -1) {
                  if (this.ResendCount < this.mm7Config.getReSendCount()) {
                    this.ResendCount += 1;
                    this.pool.setInitNonceCount();
                    String clientAuthInfor = "";
                    String authInfor = "";
                    int authindex = this.baos.toString().indexOf("WWW-Authenticate");

                    if (authindex > 0) {
                      int lineend = this.baos.toString().indexOf("\r", authindex + 1);
                      int linebeg = authindex + "WWW-Authenticate".length() + 1;

                      authInfor = this.baos.toString().substring(linebeg, lineend);
                      clientAuthInfor = setDigestAuth(authInfor);
                    }
                    int connectionindex = this.baos.toString().toLowerCase().indexOf("connection");

                    int connlength = connectionindex + "connection".length() + 1;

                    int connectionend = this.baos.toString().indexOf("\r\n", connectionindex);

                    String ConnectionFlag = "";
                    if ((connectionindex != -1) && (connectionend != -1)) {
                      ConnectionFlag = this.baos.toString().substring(connlength, connectionend);
                    }
                    this.sb = new StringBuffer();
                    this.sb.append(this.beforAuth);
                    this.sb.append(clientAuthInfor);
                    this.sb.append(this.afterAuth);
                    this.sb.append(this.entityBody);
                    this.sender.write(this.sb.toString().getBytes());
                    this.sender.flush();
                    this.baos = new ByteArrayOutputStream();
                    data = new byte[256];
                    int resline = -1;
                    int totalresline = 0;
                    boolean excuteFlag = false;
                    int httpseper = -1;
                    int contlen3 = -1;
                    while (true) {
                      resline = this.receiver.read(data);
                      if (resline == -1)
                        break;
                      this.baos.write(data, 0, resline);
                      totalresline += resline;
                      if (this.baos.toString().indexOf("\r\n\r\n") < 0)
                        continue;
                      if (!excuteFlag) {
                        httpindex = this.baos.toString().indexOf("HTTP/1.1");
                        httpCode = this.baos.toString().substring(httpindex + 8, httpindex + 12).trim();

                        int conlen1 = this.baos.toString().toLowerCase().indexOf("content-length");

                        int conlen2 = this.baos.toString().indexOf("\r", conlen1);
                        contlen3 = Integer.parseInt(this.baos.toString().substring(conlen1 + "content-length".length() + 1, conlen2).trim());

                        httpseper = this.baos.toString().indexOf("\r\n\r\n");
                        int index1;
                        int index2;
                        int conn_index;
                        int conn_end;
                        String strConn;
                        int contlength;
                        int encodingindex;
                        int encodingend;
                        String strenc;
                        int endencindex2;
                        int xmlbeg;
                        String strTempContLen;
                        if (httpCode.equals("200"))
                        {
                          this.ResendCount = 0;
                          excuteFlag = true;
                          if (ConnectionFlag.trim().toLowerCase().equals("keep-alive"))
                          {
                            this.pool.setNonceCount(Integer.toString(Integer.parseInt(this.pool.getNonceCount(), 8) + 1));

                            this.connWrap.setDigestInfor(setDigestAuth(authInfor));
                            continue;
                          }
                        }
                      }

                      if (totalresline == httpseper + contlen3 + 4) {
                        if (this.pool.getKeepAlive().equals("off")) {
                          this.sender.close();
                          this.receiver.close();
                          this.client.close();
                        }
                        else {
                          this.connWrap.setUserfulFlag(true);
                          this.connWrap.setFree(true);
                        }
                        flag = true;
                        bReceive = true;
                      }
                    }

                  }
                  else if (this.baos.toString().indexOf("Basic") != -1) {
                    if (this.ResendCount < this.mm7Config.getReSendCount()) {
                      this.ResendCount += 1;
                      receiveMessge();
                    }
                    else {
                      bReceive = false;

                      break;
                    }
                  }
                }
                else {
                  bReceive = false;

                  break;
                }
              }
              else {
                int index1 = this.baos.toString().toLowerCase().indexOf("content-length");

               int index2 = this.baos.toString().indexOf("\r", index1);

                int conn_index = this.baos.toString().toLowerCase().indexOf("connection:");
                int conn_end = this.baos.toString().indexOf("\r\n", conn_index);
                String strConn = "";
                if ((conn_index >= 0) && (conn_end >= 0))
                {
                  strConn = this.baos.toString().substring(conn_index + 11, conn_end);
                }

               int contlength = 0;
                if ((index1 == -1) || (index2 == -1)) {
                 int encodingindex = this.baos.toString().toLowerCase().indexOf("transfer-encoding:");

                  if (encodingindex >= 0) {
                   int encodingend = this.baos.toString().indexOf("\r\n", encodingindex);

                    if (encodingend >= 0) {
                      String strenc = this.baos.toString().substring(encodingindex + "transfer-encoding:".length(), encodingend).trim();

                      if (strenc.equalsIgnoreCase("chunked"))
                      {
                        int endencindex2 = this.baos.toString().indexOf("\r\n", encodingend + 1);

                        if (endencindex2 >= 0) {
                         int  xmlbeg = this.baos.toString().indexOf("<?xml", endencindex2 + 1);

                          if (xmlbeg > 0) {
                           String strTempContLen = this.baos.toString().substring(endencindex2, xmlbeg).trim();

                            contlength = Integer.parseInt(strTempContLen, 16);
                          }
                        }
                      }
                      else {
                        bReceive = false;

                        break;
                      }
                    }
                    else {
                      bReceive = false;

                      break;
                    }

                    if (totalline >= httpsepindex + contlength + 8) {
                      this.SevereBuffer.append("receive end");
                      this.SevereBuffer.append("baos.toString()==" + this.baos.toString());
                      if (this.pool.getKeepAlive().equals("off")) {
                        this.sender.close();
                        this.receiver.close();
                        this.client.close();
                      } else if (strConn.equalsIgnoreCase("close")) {
                        this.pool.deleteURL(this.connWrap);
                      }
                      else {
                        this.connWrap.setUserfulFlag(true);
                        this.connWrap.setFree(true);
                      }
                      bReceive = true;
                      break;
                    }
                  }
                }
                else {
                  contlength = Integer.parseInt(this.baos.toString().substring(index1 + "content-length".length() + 1, index2).trim());

                  if (totalline == httpsepindex + contlength + 4)
                  {
                    if (this.TimeOutFlag == true) {
                      this.SevereBuffer.append("baos.tostring()==" + this.baos.toString());
                    }
                    if (this.pool.getKeepAlive().equals("off")) {
                      this.sender.close();
                      this.receiver.close();
                      this.client.close();
                    }
                    else if (strConn.equalsIgnoreCase("close")) {
                      this.pool.deleteURL(this.connWrap);
                    }
                    else {
                      this.connWrap.setUserfulFlag(true);
                      this.connWrap.setFree(true);
                    }
                    bReceive = true;
                    break;
                  }
                }
              }
            }
            else {
              bReceive = false;

              break;
            }
          }
        }
        else {
          readlineOut++;
        }
      }
      return bReceive;
    }
    catch (SocketTimeoutException ste)
    {
      this.TimeOutFlag = true;
      this.ReadTimeOutCount += 1;
      if (this.ReadTimeOutCount < this.mm7Config.getReSendCount()) {
        this.sendBaos = getSendMessage(this.TimeOutbCount);
        if (this.sendBaos != null)
        {
          this.res = SendandReceiveMessage(this.sendBaos);
          if (this.res != null) {
            this.TimeOutFlag = false;
            return true;
          }
        }
        else {
          return false;
        }
      }
      this.TimeOutFlag = false;
      this.res.setStatusCode(-101);
      this.res.setStatusText("超时发送失败！");
      this.SevereBuffer.append("[Comments={-101;" + this.res.getStatusText() + "}]");
      this.ReadTimeOutCount = 0;
    }return false;
  }

  public String calcMD5(String str)
  {
    try
    {
      MessageDigest alga = MessageDigest.getInstance("MD5");
      alga.update(str.getBytes());
      byte[] digesta = alga.digest();
      return byte2hex(digesta);
    }
    catch (NoSuchAlgorithmException ex) {
      System.out.println("出错了！！没有这种算法！");
    }
    return "NULL";
  }

  public String byte2hex(byte[] b)
  {
    String hs = "";
    String stmp = "";
    for (int n = 0; n < b.length; n++) {
      stmp = Integer.toHexString(b[n] & 0xFF);
      if (stmp.length() == 1)
        hs = hs + "0" + stmp;
      else
        hs = hs + stmp;
      if (n < b.length - 1)
        hs = hs + "";
    }
    return hs;
  }

  private String setDigestAuth(String authinfor)
  {
    String auth = authinfor + "\r\n";
    int equal = -1;
    int comma = -1;
    StringBuffer authBuffer = new StringBuffer();
    authBuffer.append("Authorization: Digest ");

    String cnonceValue = "";

    String uri = this.mm7Config.getMMSCURL();
    if (uri == null)
      uri = "";
    String username = this.mm7Config.getUserName();
    authBuffer.append("uri=\"" + uri + "\",");
    authBuffer.append("username=\"" + username + "\",");

    String passwd = this.mm7Config.getPassword();

    int realmindex = auth.indexOf("realm");
    equal = auth.indexOf("=", realmindex + 1);
    comma = auth.indexOf("\"", equal + 2);
    String realmValue = auth.substring(equal + 1, comma);
    if (realmValue.startsWith("\""))
      realmValue = realmValue.substring(1, realmValue.length());
    authBuffer.append("realm=\"" + realmValue + "\",");

    int nonceindex = auth.indexOf("nonce");
    equal = auth.indexOf("=", nonceindex + 1);
    comma = auth.indexOf("\"", equal + 2);
    String nonceValue = auth.substring(equal + 1, comma);
    if (nonceValue.startsWith("\""))
      nonceValue = nonceValue.substring(1, nonceValue.length());
    authBuffer.append("nonce=\"" + nonceValue + "\",");

    int opaqueindex = auth.indexOf("opaque");
    if (opaqueindex > 0)
    {
      equal = auth.indexOf("=", opaqueindex + 1);
      comma = auth.indexOf("\"", equal + 2);
      authBuffer.append("opaque=" + auth.substring(equal + 1, comma + 1));
    }

    int algindex = auth.indexOf("algorithm");
    String algValue;
    if (algindex > 0)
    {
      equal = auth.indexOf("=", algindex);
      comma = auth.indexOf(",", equal + 2);
      if (comma >= 0)
      {
        algValue = auth.substring(equal + 1, comma);
        if (algValue.startsWith("\""))
          algValue = algValue.substring(1, algValue.length() - 1);
      }
      else
      {
        comma = auth.indexOf("/r", equal);
        algValue = auth.substring(equal + 1, comma);
        if (algValue.startsWith("\""))
          algValue = algValue.substring(1, algValue.length());
      }
    }
    else
    {
      algValue = "MD5";
    }

    int qopindex = auth.indexOf("qop");
    if ((algValue.equals("MD5")) || (algValue.equals("MD5-sess")))
    {
      String MD5A1 = calcMD5(username + ":" + realmValue + ":" + passwd);
      String MD5A2;
      String responseValue;
      if (qopindex > 0) {
        equal = auth.indexOf("=", qopindex);
        comma = auth.indexOf("\"", equal + 2);
        String qopValue = auth.substring(equal + 1, comma);
        if (qopValue.startsWith("\""))
          qopValue = qopValue.substring(1, qopValue.length());
        String ncValue;
        if (qopValue.indexOf(",") > 0) {
          if (this.mm7Config.getDigest().equals("auth-int")) {
            MD5A2 = calcMD5("POST:" + uri + ":" + calcMD5(this.entityBody.toString()));
          }
          else
          {
            MD5A2 = calcMD5("POST:" + uri);
          }
          ncValue = String.valueOf(this.pool.getNonceCount());
          cnonceValue = getBASE64(ncValue);
          responseValue = calcMD5(MD5A1 + ":" + nonceValue + ":" + ncValue + ":" + cnonceValue + ":" + qopValue + ":" + MD5A2);

          authBuffer.append("qop=\"" + this.mm7Config.getDigest() + "\",");
          authBuffer.append("nc=" + ncValue + ",");
          authBuffer.append("cnonce=\"" + cnonceValue + "\",");
          authBuffer.append("response=\"" + responseValue + "\",");
        }
        else
        {
          if (qopValue.equals("auth-int")) {
            MD5A2 = calcMD5("POST:" + uri + ":" + calcMD5(this.entityBody.toString()));
          }
          else
          {
            MD5A2 = calcMD5("POST:" + uri);
          }ncValue = String.valueOf(this.pool.getNonceCount());
          cnonceValue = getBASE64(ncValue);
          responseValue = calcMD5(MD5A1 + ":" + nonceValue + ":" + ncValue + ":" + cnonceValue + ":" + qopValue + ":" + MD5A2);

          authBuffer.append("qop=\"" + qopValue + "\",");
          authBuffer.append("nc=" + ncValue + ",");
          authBuffer.append("cnonce=\"" + cnonceValue + "\",");
          authBuffer.append("response=\"" + responseValue + "\",");
        }
      }
      else
      {
        MD5A2 = calcMD5("POST:" + uri);
        responseValue = calcMD5(MD5A1 + ":" + nonceValue + ":" + MD5A2);
        authBuffer.append("response=\"" + responseValue + "\",");
      }
    }

    int lastcommaindex = authBuffer.lastIndexOf(",");
    authBuffer.delete(lastcommaindex, lastcommaindex + 1);
    authBuffer.append("\r\n");
    return authBuffer.toString();
  }
}