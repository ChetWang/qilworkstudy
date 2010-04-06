package com.nci.ums.periphery.core;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.sql.ResultSet;
import com.nci.ums.util.*;
import com.nci.ums.periphery.parser.Process1002;
import com.nci.ums.periphery.application.AppInfo;
import com.nci.ums.periphery.application.Send;
import com.nci.ums.periphery.application.EMailAppChannel;
import com.nci.ums.channel.channelinfo.QuitLockFlag;
import javax.mail.*;
import javax.mail.internet.InternetAddress;

/**
 * This class make a main class to server for other application request
 */
public class EmailServer extends Thread {

    private boolean stop = false; // flag to stop this main thread
    protected QuitLockFlag quitFlag;
    private String server;
    private String user;
    private String password;
    Properties props = null;
    Session session = null;

    public EmailServer() {
        try {
            Properties emailProps = new Properties();
            emailProps.load((new ServletTemp().getInputStreamFromFile("/resources/email.props")));
            quitFlag = QuitLockFlag.getInstance();
            this.server = emailProps.getProperty("host");
            this.user = emailProps.getProperty("loginName");
            this.password = emailProps.getProperty("loginPwd");
            /*quitFlag = QuitLockFlag.getInstance();
            this.server="mail.nci.com.cn";
            this.user="zhangzy";
            this.password="741218";
             */
            props = System.getProperties();
            session = Session.getDefaultInstance(props, null);
        } catch (Exception ex) {
            ex.printStackTrace();
            Res.log(Res.ERROR, "email配置文件读取错误.");
            Res.logExceptionTrace(ex);
        }
    }

    public void run() {
        Store store = null;
        Folder folder = null;
        // 得到缺省会话
        //得到一个POP3消息存储设备并与它连接
        while (!stop && !quitFlag.getLockFlag()) {
            try {
                store = null;
                folder = null;
                store = session.getStore("pop3");
                store.connect(server, user, password);
                //Res.log(Res.DEBUG,"store");
                //得到缺省文件夹
                folder = store.getDefaultFolder();
                //Res.log(Res.DEBUG,"folder");
                if (folder == null) {
                    throw new Exception("No default folder");
                }
                folder = folder.getFolder("INBOX"); // 取得收件箱
                Res.log(Res.DEBUG, "正在取得收件箱中的内容");
                if (folder == null) {
                    throw new Exception("No POP3 INBOX");
                }
                // 以读写方式打开文件夹
                folder.open(Folder.READ_WRITE);
                //Res.log(Res.DEBUG,"openfolder");
                // 得到消息包
                Message[] msgs = folder.getMessages();
                FetchProfile fp = new FetchProfile();
                fp.add(FetchProfile.Item.ENVELOPE);
                fp.add(FetchProfile.Item.FLAGS);
                fp.add("X-Mailer");
                folder.fetch(msgs, fp);

                for (int i = 0; i < msgs.length; i++) {
                    StringBuffer content = new StringBuffer();
                    //取内容
                    getMessageText(content, msgs[i]);
                    String messageStr = content.toString().trim();
                    //取主题
                    String subject = msgs[i].getSubject().trim();
                    String from = ((InternetAddress) msgs[i].getFrom()[0]).getAddress();
                    //处理邮件信息
                    if (subject != null) {
                        process(subject, messageStr, from);
                    }
                    //设置删除标志
                    msgs[i].setFlag(Flags.Flag.DELETED, true);
                }
                //folder.expunge();
                Res.log(Res.DEBUG, "邮件处理完毕");
            } catch (Exception e) {
                Res.log(Res.ERROR, "email服务" + e);
                Res.logExceptionTrace(e);
            } finally {
                try {
                    if (folder != null) {
                        folder.close(true);
                    }
                    if (store != null) {
                        store.close();
                    }
                } catch (Exception ex2) {
                    Res.log(Res.ERROR, "关闭email服务ERROR:" + ex2);
                    Res.logExceptionTrace(ex2);
                }
                sleepTime(1 * 60);
            }
        }
        // 关闭
    }

    private void sleepTime(int sleepTime) {
        try {
            Thread.sleep(sleepTime * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Res.log(Res.INFO, "线程被中断");
            pleaseStop();
        }
    }

    private void process(String subject, String content, String from) {
        try {
            //增加内容值对
            HashMap itemMap = parser(subject, content);
            //Res.log(Res.DEBUG,"内容:"+content);
            String serial = "99999999";

            //验证密码
            if (checkPwd(itemMap)) {
                //产生序列号
                serial = SerialNO.getInstance().getSerial();
                Res.log(Res.DEBUG, "处理邮件接收内容");
                Process1002 process = new Process1002();

                //捕获1002交易处理错误
                try {
                    if (Res.getValue(itemMap, "MSG").length() > 0) {
                        process.process(itemMap, 1, serial);
                    } else {
                        sendFeedback("2001", from, itemMap, "UMS中心:您填写的邮件无发送内容。请检查后，重新发送!原内容：" + content);
                        sendFeedback("2001", "zhangzy@nci.com.cn", itemMap, "UMS中心:您填写的邮件无发送内容。请检查后，重新发送!原内容：" + content);
                    }
                } catch (Exception e) {
                    sendFeedback("2002", from, itemMap, "UMS中心:您填写的消息，接收方手机不正确或内容不合法。请检查后，重新发送!");
                }
            } else {
                if (Res.getValue(itemMap, "APP").length() > 0) {
                    sendFeedback("2003", from, itemMap, "UMS中心:" + Res.getValue(itemMap, "APP") + "应用密码不正确。请检查后，重新发送!");
                }
            }
        } catch (Exception ex) {
            Res.log(Res.ERROR, ex.toString());
            Res.logExceptionTrace(ex);
        }
    }

    //发送回执
    private void sendFeedback(String retCode, String from, HashMap itemMap, String msg) {
        AppInfo appInfo = new AppInfo();
        appInfo.setEmail(from);
        Send send = null;
        //构造发给应用程序数据的对象
        send = new EMailAppChannel();
        try {
            send.initInterface(appInfo);
            boolean result = send.receiveMessage("", "", retCode, Res.getValue(itemMap, "ID"), 0, Res.getValue(itemMap, "UMS_TO"), "", "", "", msg);
        } catch (Exception e) {
        }
    }

    private HashMap parser(String subject, String content) {
        HashMap result = new HashMap();
        try {
            //替换字符串中[字符
            Pattern p = Pattern.compile("\\[");
            Matcher m = p.matcher(subject);
            subject = m.replaceAll("");

            //以]字符分解字符串
            String[] itemStrs = subject.split("]");

            //将分解后的值对放到map中
            for (int i = 0; i < itemStrs.length; i++) {
                String itemStr = itemStrs[i];
                String[] item = itemStr.split("=");
                if (item.length == 2) {
                    result.put(item[0].trim().toUpperCase(), item[1].trim());
                }
            }

            //以]字符分解字符串,[符号要使用转义符
            itemStrs = content.split("\\[");

            //将分解后的值对放到map中
            for (int i = 0; i < itemStrs.length; i++) {
                String itemStr = itemStrs[i];
                String[] item = itemStr.split("]");
                if (item.length == 2) {
                    Res.log(Res.INFO, item[0].trim().toUpperCase() + ":" + item[1].trim());
                    if (item[0].trim().equalsIgnoreCase("MSG")) {
                        result.put(item[0].trim().toUpperCase(), item[1].trim());
                    } else {
                        result.put(item[0].trim().toUpperCase(), item[1].trim());
                    }
                }
            }
        } catch (Exception e) {
            Res.log(Res.ERROR, e.toString());
            Res.logExceptionTrace(e);
        }
        return result;
    }

    /**
     * This is the polite way to get a Listener to stop accepting
     * connections
     ***/
    public void pleaseStop() {
        this.stop = true; // Set the stop flag
        this.interrupt();
    }

    private StringBuffer getMessageText(StringBuffer buf, Part part) {
        try {
            Object content = getPartContent(part);
            if (content == null) {
            } else if (content instanceof Multipart) {
                Multipart mPart = (Multipart) content;
                int partCount = mPart.getCount();
                for (int i = 0; i < partCount; i++) {
                    this.getMessageText(buf, mPart.getBodyPart(i));
                    //buf.append( "\n" );
                }
            } else if (content instanceof String) {
                if (part.getContentType().toLowerCase().indexOf("text/plain") >= 0) {
                    if (part.getContentType().toLowerCase().indexOf("gb2312") >= 0 || part.getContentType().toLowerCase().indexOf("gbk") >= 0) {
                        buf.append(new String(((String) content).getBytes("gb2312"), "GBK"));
                    } else {
                        buf.append(Res.native2Unicode((String) content));
                    }
                    //buf.append( "\n" );
                    Res.log(Res.DEBUG, "内容：" + buf);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return buf;
    }

    public Object getPartContent(Part part) throws MessagingException {
        Object result = null;

        try {
            result = part.getContent();
        } catch (IllegalArgumentException ex) {
            throw new MessagingException("content charset is not recognized: " + ex.getMessage());
        } catch (IOException ex) {
            throw new MessagingException("getPartContent(): " + ex.getMessage());
        }
        return result;
    }

    public boolean checkPwd(Map map) {
        DBConnect db = null;
        boolean result = false;
        ResultSet rs = null;
        try {
            db = new DBConnect();
            db.prepareStatement("select * from application where appID=? and md5Pwd=?");
            db.setString(1, Res.getValue(map, "APP"));
            db.setString(2, Res.getValue(map, "PASS"));
            rs = db.executeQuery();
            if (rs.next()) {
                result = true;
            }
        } catch (Exception ex) {
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                }
            }
            try {
                db.close();
            } catch (Exception e) {
            }
        }
        return result;
    }

    public static void main(String[] args) {
        EmailServer server = new EmailServer();
        server.start();
    }
}