package org.vlg.linghu;

import java.io.File;
import java.sql.Connection;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.vlg.linghu.mms.MMSSender;
import org.vlg.linghu.sms.SMSSender;
import org.vlg.linghu.sms.SMSSenderHuawei;
import org.vlg.linghu.sms.zte.client.ZteSMSReceiver;

public class InitServlet extends HttpServlet {

	private static final Logger logger = LoggerFactory
			.getLogger(InitServlet.class);

	public static String WEB_INF;
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	ZteSMSReceiver smsReceiver;
	
	@Autowired
	SMSSender smsSender;
	
	@Autowired
	MMSSender mmsSender;
	
	@Autowired
	SMSSenderHuawei smsSenderHuawei;
	
	public static WebApplicationContext sprintContext;
	
	//FIXME, 老附件清理，30天
	private Timer timer = new Timer();

	public void init(ServletConfig config) {
		try {
			super.init(config);
			sprintContext = WebApplicationContextUtils
					.getRequiredWebApplicationContext(getServletContext());
			sprintContext.getAutowireCapableBeanFactory().autowireBean(this);
			SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(
					this, getServletContext());
			WEB_INF = getServletContext().getRealPath("/WEB-INF/")
					+ File.separator;
			System.out.println(WEB_INF);
			logger.info("Test loading datasource!");
			Connection conn = dataSource.getConnection();
			logger.info("Test loading datasource successfully, url is "
					+ conn.getMetaData().getURL());
			conn.close();
			new Thread("SMS Receiver"){
				public void run(){
					smsReceiver.start();
				}
			}.start();
//			smsSender.start();
			smsSenderHuawei.start();
			mmsSender.start();
			timer.scheduleAtFixedRate(new TimerTask(){

				@Override
				public void run() {
					checkArchivedAttachments();
				}
				
			}, 1000, 1000*60*60*24);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	protected void checkArchivedAttachments() {
		//FIXME
		
	}

}
