/**
 * <p>Title: CMPPOutChannel.java</p>
 * <p>Description:
 *    CMPPOutChannel外拨渠道类，继承OutChannel
 *    实现移动信息的发送过程
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering， Ltd.</p>
 * <p>Company:   2003 Hangzhou NCI System Engineering， Ltd.</p>
 * Date        Author      Changes
 * NOV. 17 2003   张志勇        Created
 * @version 1.0
 */

package com.nci.ums.periphery.application;

import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.nci.ums.periphery.exception.ApplicationException;
import com.nci.ums.util.Res;
import com.nci.ums.util.ServletTemp;

public class EMailAppChannel
    implements Send {

    private Session session;
    private AppInfo appInfo=null;
    String props = "/resources/email.props";
    Properties res = null;
    private String umsEmail="";
    private String host="";
    private String loginEmailName="";
    private String loginEmailPwd="";

    public EMailAppChannel() {
    }

    public boolean initInterface() throws ApplicationException
    {
        boolean result=true;
        res = new Properties(); // Empty properties
        try {
          InputStream in = new ServletTemp().getInputStreamFromFile(props);
          res.load(in); // Try to load properties
          //取得UMS的email发送信息
          host=res.getProperty("host");
          umsEmail=res.getProperty("UMSAdminEmail");
          loginEmailName=res.getProperty("loginName");
          loginEmailPwd=res.getProperty("loginPwd");
          in.close();
        }catch (Exception e) {
            result=false;
            Res.log(Res.ERROR,"Email外发程序初始化出错"+e);
            Res.logExceptionTrace(e);
        } // Load code properties finished
        return result;
    }

    public boolean initInterface(AppInfo appInfo) throws ApplicationException
    {
        boolean result=initInterface();
        this.appInfo=appInfo;
        return result;
    }

    public boolean receiveMessage(String loginName,String loginPwd,String retCode,String msgID,int ack,String receive,String from,String receiveDate,String receiveTime,String content)throws ApplicationException
    {
        boolean result=false;
        StringBuffer sb=new StringBuffer("");
        try {
            Properties emailProps = System.getProperties();
            emailProps.put("mail.smtp.auth", "true");
            emailProps.put("mail.host",host);
            emailProps.put("mail.transport.protocol", "smtp");

            // 设置mail调试环境，不显示调试信息
            SmtpAuth auth = new SmtpAuth(loginEmailName,
            		loginEmailPwd);
            session = Session.getDefaultInstance(emailProps, auth);
            session.setDebug(false);

            //构造主题
            sb.append("[RetCode=").append(retCode).append("] ").
                    append("[Ack=").append(ack).append("] ").
                    append("[Receive=").append(receive).append("] ").
                    append("[From=").append(from).append("] ");

            // 填充发送内容
            MimeMessage msg = new MimeMessage(session);

            msg.setFrom(new InternetAddress(umsEmail));
//            msg.setFrom(new InternetAddress("UMS控制中心"));
            InternetAddress[] address = {new InternetAddress(appInfo.getEmail())};
//            InternetAddress[] address = {new InternetAddress("mail.163.com")};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(sb.toString());
            StringBuffer msgContent=new StringBuffer("");
            //填充内容
            msgContent.append("[msg]") .append(content).append("  ").
                append("[MsgID]").append(msgID);
            msg.setContent(msgContent.toString(), "text/plain; charset=gb2312");

            msg.setSentDate(new Date());
            msg.saveChanges();
            // 转发
            Transport transport = session.getTransport("smtp");
            transport.connect(host, this.loginEmailName,this.loginEmailPwd);
            transport.sendMessage(msg,msg.getAllRecipients());
            transport.close();
            result=true;
        }catch (Exception mex) {
            Res.log(Res.ERROR,mex.toString());
            Res.logExceptionTrace(mex);
            mex.printStackTrace();
            result=false;
        }
        return result;
    }
    
   public static void main(String[] args) {
        EMailAppChannel  test = new EMailAppChannel();
        try {
			test.initInterface();
		} catch (ApplicationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        test.appInfo = new AppInfo();
       try{
            test.receiveMessage("","","1003","",0,"13575471065","955983","","","test");
       }catch(Exception e){Res.log(Res.DEBUG,e.toString());}
    }

}