package org.vlg.linghu.mms;

import java.sql.Connection;

import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virbraligo.db.ConnectionManager;

public class InitServlet extends HttpServlet {

	private static final Logger logger = LoggerFactory
			.getLogger(InitServlet.class);

	public void init() {
		try {
			try {
				ConnectionManager.load();
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			logger.info("loading datasource!");
			Connection conn = ConnectionManager.getConnection();
			logger.info("loading datasource successfully, url is "
					+ conn.getMetaData().getURL());
		} catch (Exception e) {
			logger.error("", e);
		}
	}

}
