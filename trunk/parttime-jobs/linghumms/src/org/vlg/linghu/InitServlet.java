package org.vlg.linghu;

import java.io.File;
import java.sql.Connection;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.virbraligo.db.ConnectionManager;

public class InitServlet extends HttpServlet {

	private static final Logger logger = LoggerFactory
			.getLogger(InitServlet.class);

	public static String WEB_INF;
	
	@Autowired
	DataSource dataSource;

	public void init(ServletConfig config) {
		try {
			super.init(config);
			WebApplicationContextUtils
					.getRequiredWebApplicationContext(getServletContext())
					.getAutowireCapableBeanFactory().autowireBean(this);
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
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public void destroy() {
		// ConnectionM
		super.destroy();
		ConnectionManager.destroy();
		System.out.println("destroy");
	}

}
