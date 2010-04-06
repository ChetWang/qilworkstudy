package com.nci.ums.sync;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.nci.ums.util.DataBaseOp;

public abstract class AbstractProcess {
	// protected static Logger log = Log.logger;
	private static HashMap dsHashMap = new HashMap();
	private static HashMap templateHashMap = new HashMap();

	private synchronized DataSource getDataSource(String key) {
		oracle.jdbc.pool.OracleDataSource ds = null;
		try {
			 ds = (oracle.jdbc.pool.OracleDataSource) dsHashMap.get(key);
			if (ds == null) {
				Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();

				ds = new oracle.jdbc.pool.OracleDataSource();
				ds.setServerName("ums");
//				ds.setURL("jdbc:oracle:thin:@127.0.0.1:1521:qil");
				ds.setURL(DataBaseOp.getConnectURI());
				ds.setUser(DataBaseOp.getUserName());
				ds.setPassword(DataBaseOp.getPassWord());
				ds.setPortNumber(1521);
				ds.setDescription("JTDS Datasource");
				dsHashMap.put(key, ds);
			}

		} catch (Exception e) {
		}

		return ds;
	}

	protected JdbcTemplate getJdbcTemplate() {
		return getJdbcTemplate("DEFAULT");
	}

	protected synchronized JdbcTemplate getJdbcTemplate(String key) {
		JdbcTemplate template = (JdbcTemplate) templateHashMap.get(key);
		if (template == null) {
			template = new JdbcTemplate(getDataSource(key));
			templateHashMap.put(key, template);
		}
		return template;
	}

	protected PlatformTransactionManager getJdbcTransactionManager() {
		return new DataSourceTransactionManager(getDataSource("DEFAULT"));
	}
	/**
	 * 
	 */

}
