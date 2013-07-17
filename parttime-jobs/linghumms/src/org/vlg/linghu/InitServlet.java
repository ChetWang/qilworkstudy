package org.vlg.linghu;

import java.io.File;
import java.sql.Connection;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virbraligo.db.ConnectionManager;

public class InitServlet extends HttpServlet {

	private static final Logger logger = LoggerFactory
			.getLogger(InitServlet.class);

	public static String WEB_INF;

	public void init(ServletConfig config) {
		try {
			super.init(config);
			try {
				ConnectionManager.load();
			} catch (Exception e) {
				logger.error("This error could be ignored!");
			}
			WEB_INF = getServletContext().getRealPath("/WEB-INF/")
					+ File.separator;
			System.out.println(WEB_INF);
			logger.info("loading datasource!");
			Connection conn = ConnectionManager.getConnection();
			logger.info("loading datasource successfully, url is "
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
