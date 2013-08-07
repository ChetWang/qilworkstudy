package org.vlg.linghu;

import java.io.File;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.vlg.linghu.SingleThreadPool.VLGThreadFactory;
import org.vlg.linghu.mms.MMSSender;
import org.vlg.linghu.sms.SMSSender;
import org.vlg.linghu.sms.SMSSenderHuawei;
import org.vlg.linghu.sms.huawei.client.MySGIPReceiveProxy;
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

	@Autowired
	MySGIPReceiveProxy sgipReceiveProxy;

	public static WebApplicationContext sprintContext;

	private static ScheduledExecutorService scheduledExecutorService;

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
			// new Thread("SMS Receiver"){
			// public void run(){
			// smsReceiver.start();
			// }
			// }.start();
			// smsSender.start();
			sgipReceiveProxy.start();
			smsSenderHuawei.start();
			mmsSender.start();
			scheduledExecutorService = Executors.newScheduledThreadPool(2,
					new VLGThreadFactory("Linghu-ScheduledExecutorService-"));
			scheduledExecutorService.scheduleAtFixedRate(new Runnable() {

				@Override
				public void run() {
					checkArchivedAttachments();
				}
			}, 24 - Calendar.getInstance().get(Calendar.HOUR_OF_DAY), 24,
					TimeUnit.HOURS);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	protected void checkArchivedAttachments() {
		String archivedDirStr = new File(SPConfig.getAttachmentDir())
				.getParent() + "/mms_archived/";
		int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
		File[] files = new File(SPConfig.getAttachmentDir()).listFiles();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		int archivedCount = 0;
		for (File f : files) {
			long lastModify = f.lastModified();
			Date lastModifyDate = new Date(lastModify);
			Calendar cal = Calendar.getInstance();
			cal.setTime(lastModifyDate);
			int fileMonth = cal.get(Calendar.MONTH);
			if (fileMonth != currentMonth) {
				String archivedDir = archivedDirStr
						+ sdf.format(lastModifyDate);
				File ad = new File(archivedDir);
				if (!ad.exists()) {
					ad.mkdirs();
				}
				f.renameTo(new File(archivedDir + "/" + f.getName()));
				archivedCount++;
			}
		}
		if (archivedCount>0) {
			logger.info("清理归档完成，共归档"+archivedCount+"个文件");
		}
	}

	public void destroy() {
		super.destroy();
		scheduledExecutorService.shutdown();
	}

	public static ScheduledExecutorService getScheduledExecutorService() {
		return scheduledExecutorService;
	}

}
