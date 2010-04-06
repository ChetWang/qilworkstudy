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
            Res.log(Res.ERROR, "email�����ļ���ȡ����.");
            Res.logExceptionTrace(ex);
        }
    }

    public void run() {
        Store store = null;
        Folder folder = null;
        // �õ�ȱʡ�Ự
        //�õ�һ��POP3��Ϣ�洢�豸����������
        while (!stop && !quitFlag.getLockFlag()) {
            try {
                store = null;
                folder = null;
                store = session.getStore("pop3");
                store.connect(server, user, password);
                //Res.log(Res.DEBUG,"store");
                //�õ�ȱʡ�ļ���
                folder = store.getDefaultFolder();
                //Res.log(Res.DEBUG,"folder");
                if (folder == null) {
                    throw new Exception("No default folder");
                }
                folder = folder.getFolder("INBOX"); // ȡ���ռ���
                Res.log(Res.DEBUG, "����ȡ���ռ����е�����");
                if (folder == null) {
                    throw new Exception("No POP3 INBOX");
                }
                // �Զ�д��ʽ���ļ���
                folder.open(Folder.READ_WRITE);
                //Res.log(Res.DEBUG,"openfolder");
                // �õ���Ϣ��
                Message[] msgs = folder.getMessages();
                FetchProfile fp = new FetchProfile();
                fp.add(FetchProfile.Item.ENVELOPE);
                fp.add(FetchProfile.Item.FLAGS);
                fp.add("X-Mailer");
                folder.fetch(msgs, fp);

                for (int i = 0; i < msgs.length; i++) {
                    StringBuffer content = new StringBuffer();
                    //ȡ����
                    getMessageText(content, msgs[i]);
                    String messageStr = content.toString().trim();
                    //ȡ����
                    String subject = msgs[i].getSubject().trim();
                    String from = ((InternetAddress) msgs[i].getFrom()[0]).getAddress();
                    //�����ʼ���Ϣ
                    if (subject != null) {
                        process(subject, messageStr, from);
                    }
                    //����ɾ����־
                    msgs[i].setFlag(Flags.Flag.DELETED, true);
                }
                //folder.expunge();
                Res.log(Res.DEBUG, "�ʼ��������");
            } catch (Exception e) {
                Res.log(Res.ERROR, "email����" + e);
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
                    Res.log(Res.ERROR, "�ر�email����ERROR:" + ex2);
                    Res.logExceptionTrace(ex2);
                }
                sleepTime(1 * 60);
            }
        }
        // �ر�
    }

    private void sleepTime(int sleepTime) {
        try {
            Thread.sleep(sleepTime * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Res.log(Res.INFO, "�̱߳��ж�");
            pleaseStop();
        }
    }

    private void process(String subject, String content, String from) {
        try {
            //��������ֵ��
            HashMap itemMap = parser(subject, content);
            //Res.log(Res.DEBUG,"����:"+content);
            String serial = "99999999";

            //��֤����
            if (checkPwd(itemMap)) {
                //�������к�
                serial = SerialNO.getInstance().getSerial();
                Res.log(Res.DEBUG, "�����ʼ���������");
                Process1002 process = new Process1002();

                //����1002���״������
                try {
                    if (Res.getValue(itemMap, "MSG").length() > 0) {
                        process.process(itemMap, 1, serial);
                    } else {
                        sendFeedback("2001", from, itemMap, "UMS����:����д���ʼ��޷������ݡ���������·���!ԭ���ݣ�" + content);
                        sendFeedback("2001", "zhangzy@nci.com.cn", itemMap, "UMS����:����д���ʼ��޷������ݡ���������·���!ԭ���ݣ�" + content);
                    }
                } catch (Exception e) {
                    sendFeedback("2002", from, itemMap, "UMS����:����д����Ϣ�����շ��ֻ�����ȷ�����ݲ��Ϸ�����������·���!");
                }
            } else {
                if (Res.getValue(itemMap, "APP").length() > 0) {
                    sendFeedback("2003", from, itemMap, "UMS����:" + Res.getValue(itemMap, "APP") + "Ӧ�����벻��ȷ����������·���!");
                }
            }
        } catch (Exception ex) {
            Res.log(Res.ERROR, ex.toString());
            Res.logExceptionTrace(ex);
        }
    }

    //���ͻ�ִ
    private void sendFeedback(String retCode, String from, HashMap itemMap, String msg) {
        AppInfo appInfo = new AppInfo();
        appInfo.setEmail(from);
        Send send = null;
        //���췢��Ӧ�ó������ݵĶ���
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
            //�滻�ַ�����[�ַ�
            Pattern p = Pattern.compile("\\[");
            Matcher m = p.matcher(subject);
            subject = m.replaceAll("");

            //��]�ַ��ֽ��ַ���
            String[] itemStrs = subject.split("]");

            //���ֽ���ֵ�Էŵ�map��
            for (int i = 0; i < itemStrs.length; i++) {
                String itemStr = itemStrs[i];
                String[] item = itemStr.split("=");
                if (item.length == 2) {
                    result.put(item[0].trim().toUpperCase(), item[1].trim());
                }
            }

            //��]�ַ��ֽ��ַ���,[����Ҫʹ��ת���
            itemStrs = content.split("\\[");

            //���ֽ���ֵ�Էŵ�map��
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
                    Res.log(Res.DEBUG, "���ݣ�" + buf);
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