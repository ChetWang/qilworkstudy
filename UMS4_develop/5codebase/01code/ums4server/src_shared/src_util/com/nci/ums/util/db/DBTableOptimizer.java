package com.nci.ums.util.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import com.nci.ums.basic.UMSModule;
import com.nci.ums.util.DialectUtil;
import com.nci.ums.util.DynamicUMSStreamReader;
import com.nci.ums.util.Res;
import com.nci.ums.util.uid.UMSUID;

/**
 * 数据库优化器，进行分表，整理历史等工作
 * 
 * @author Qil.Wong
 * 
 */
public class DBTableOptimizer implements UMSModule {

	public static final String SEND_OK_TABLE = "UMS_SEND_OK";

	public static final String TRANSMIT_OK_TABLE = "UMS_TRANSMIT_MES";

	public static final String TRANSMIT_ERR_TABLE = "UMS_TRANSMIT_ERR";

	// 当前唯一实例
	private static DBTableOptimizer op = null;

	/**
	 * 部署日期，该日期由UMS第一次运行记录
	 */
	private Date deployedDate;

	/**
	 * 配置文件后缀格式
	 */
	private final String SUFFIX_SUFFIX_FORMAT = "_suffix_format";

	/**
	 * 配置文件有效期格式
	 */
	private final String SUFFIX_VALID_PERIOD = "_valid_period";

	/**
	 * 各种配置信息的存放对象
	 */
	private Properties config = new Properties();

	/**
	 * 优化定时器
	 */
	private Timer timer = null;

	/**
	 * 优化任务
	 */
	private TimerTask optTask = null;

	/**
	 * 构造函数
	 */
	private DBTableOptimizer() {

	}

	/**
	 * 获取数据库优化器
	 * 
	 * @return
	 */
	public static synchronized DBTableOptimizer getInstance() {
		if (op == null) {
			op = new DBTableOptimizer();
		}
		return op;
	}

	public void startModule() {
		InputStream is = new DynamicUMSStreamReader()
				.getInputStreamFromFile("/resources/dbtableoptimizer.prop");
		try {
			config.load(is);
			is.close();
		} catch (IOException e) {
			Res.logExceptionTrace(e);
		}
		// 读取持久化文件，该文件存放第一次部署日期
		String deployConfPath = System.getProperty("user.dir") + "/conf/deploy";
		File deployConfFile = new File(deployConfPath);
		try {
			if (!deployConfFile.exists()) {
				deployConfFile.createNewFile();
				FileOutputStream fos = new FileOutputStream(deployConfFile);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(new Date());
				oos.close();
			}
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
					deployConfFile));
			deployedDate = (Date) ois.readObject();
			ois.close();
		} catch (IOException e) {
			Res.logExceptionTrace(e);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		// 建立监听定时器，优化三个表
		timer = new Timer();
		optTask = new TimerTask() {
			public void run() {
				opt();
			}
		};
		Calendar current = Calendar.getInstance();
		// 将时间设置为明天凌晨
		current.add(Calendar.DAY_OF_MONTH, 1);
		current.set(current.get(Calendar.YEAR), current.get(Calendar.MONTH),
				current.get(Calendar.DAY_OF_MONTH), 0, 05, 0);
		timer.schedule(optTask, current.getTime(), 24 * 60 * 60 * 1000);

	}

	/**
	 * 开始优化
	 */
	private void opt() {
		DBOptLock.dbLock = true;
		try {
			Thread.sleep(60 * 1000);// 等待1分钟，让其它数据库操作完成
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		optimizeDB(SEND_OK_TABLE);
		optimizeDB(TRANSMIT_OK_TABLE);
		optimizeDB(TRANSMIT_ERR_TABLE);
		DBOptLock.dbLock = false;
	}

	public void stopModule() {
		if (optTask != null) {
			optTask.cancel();
			optTask = null;
		}
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	/**
	 * 优化数据库表
	 * 
	 * @param table
	 *            数据库表
	 */
	private void optimizeDB(String table) {
		List tables = new ArrayList();
		String suffix = timeToAlter(table, tables);
		if (!suffix.equals("")) {
			// 开始表变换
			Res.log(Res.INFO, "开始优化数据库：" + table);
			Connection conn = null;
			try {
				conn = Res.getConnection();
				String sqlFile = getClass().getResource(
						"/com/nci/ums/dialect/"
								+ DialectUtil.getDialect()
										.getOptimizeTableSQLFileName(table))
						.getFile();
				BufferedReader reader = new BufferedReader(new FileReader(
						sqlFile));
				String s = "";
				StringBuffer sb = new StringBuffer();
				while ((s = reader.readLine()) != null) {
					sb.append(s);
				}
				String[] sqls = sb.toString().split(";");
				conn.setAutoCommit(false);
				// 修改表名
				String[] renameSQLs = null;
				if (table.indexOf("UMS_SEND_OK") >= 0) {
					renameSQLs = DialectUtil.getDialect()
							.getRenameSEND_OKTableSQL(table, suffix);
				} else if (table.indexOf("UMS_TRANSMIT") >= 0) {
					renameSQLs = DialectUtil.getDialect()
							.getRenameTrantmitTableSQL(table, suffix);
				}
				Res.log(Res.INFO, "将" + table + "重命名为" + table + suffix);
				for (int i = 0; i < renameSQLs.length; i++) {
					Statement stmt = conn.createStatement();
					Res.log(Res.DEBUG, "重命名SQL:" + renameSQLs[i]);
					try {
						stmt.execute(renameSQLs[i]);
					} catch (Exception e) {
						e.printStackTrace();
					}
					stmt.close();
				}
				Res.log(Res.INFO, "创建新表：" + table);
				// 创建新表
				for (int i = 0; i < sqls.length; i++) {
					Statement stmt = conn.createStatement();
					Res.log(Res.DEBUG, "创建新表SQL:" + sqls[i]);
					stmt.execute(sqls[i]);
					stmt.close();
				}

				String propPrefix = "";
				if (table.indexOf("UMS_SEND") >= 0) {
					propPrefix = "message";
				} else if (table.indexOf("UMS_TRANSMIT") >= 0) {
					propPrefix = "transmit";
				}
				int validPeriod = Integer.parseInt(config
						.getProperty(propPrefix + SUFFIX_VALID_PERIOD));
				if (validPeriod > 0 && tables.size() > validPeriod
						&& table.indexOf("UMS_TRANSMIT") >= 0) {
					Collections.sort(tables);
					for (int i = tables.size() - validPeriod; i > 0; i--) {
						Res.log(Res.INFO, "统计数据至历史表--->" + table + "_HIS");
						Statement stmt = conn.createStatement();
						ResultSet rs = stmt
								.executeQuery("select trans_source ,trans_target,sum(trans_length) as trans_total from "
										+ tables.get(i)
										+ " group by trans_source, trans_target");
						while (rs.next()) {
							String seqkey = UMSUID
									.getUMSUID(UMSUID.UMSUID_SERVER);
							String source = rs.getString("trans_source");
							String target = rs.getString("trans_target");
							int totalLength = rs.getInt("trans_total");
							String transtime = ((String) tables.get(i))
									.substring(table.length());
							PreparedStatement insertStmt = conn
									.prepareStatement("INSERT INTO "
											+ table
											+ "_HIS "
											+ "(seqkey,trans_source,trans_target,trans_length,trans_time) VALUES(?,?,?,?,?)");
							insertStmt.setString(1, seqkey);
							insertStmt.setString(2, source);
							insertStmt.setString(3, target);
							insertStmt.setInt(4, totalLength);
							insertStmt.setString(5, transtime);
							insertStmt.executeUpdate();
							insertStmt.close();
						}
						stmt = conn.createStatement();
						String dropSQL = "DROP TABLE " + tables.get(i);
						Res.log(Res.INFO, "删除过期表:" + dropSQL);
						stmt.execute(dropSQL);
						stmt.close();
					}
				}
				conn.commit();

			} catch (SQLException e) {
				Res.logExceptionTrace(e);
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			} finally {
				if (conn != null)
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
			}

		} else {
			Res.log(Res.INFO, "无须优化数据库：" + table);
		}
	}

	/**
	 * 是否到时候进行变更
	 * 
	 * @return 变更后缀，如果为“”，则不需要变更
	 */
	private String timeToAlter(String table, List tables) {
		Connection conn = null;
		String suffix = "";
		String propPrefix = "";
		SimpleDateFormat sdf;
		// String
		try {
			// 查询当前最新的表时间戳
			conn = Res.getConnection();
			DatabaseMetaData metadata = conn.getMetaData();
			String catalog = conn.getCatalog();
			System.out.println("catalog:" + catalog);
			String[] types = new String[] { "TABLE" };
			ResultSet rs = metadata
					.getTables(catalog, null, table + "%", types);
			String last = "";

			while (rs.next()) {
				String temp = rs.getString(3);
				// 排除重复记录表和历史表
				if (!tables.contains(temp) && temp.lastIndexOf("HIS") < 0) {
					tables.add(temp);
					Res.log(Res.DEBUG, "现有表：" + temp);
					last = temp;
				}
			}
			Res.log(Res.DEBUG, "最近表：" + last);
			if (table.indexOf("UMS_SEND") >= 0) {
				propPrefix = "message";
			} else if (table.indexOf("UMS_TRANSMIT") >= 0) {
				propPrefix = "transmit";
			}
			sdf = new SimpleDateFormat(config.getProperty(propPrefix
					+ SUFFIX_SUFFIX_FORMAT));

			String lastSuffix;
			if (table.equals(last)) {
				lastSuffix = sdf.format(deployedDate);
			} else {
				lastSuffix = last.substring(table.length());
			}
			Res.log(Res.DEBUG, "最新日期后缀:" + lastSuffix);
			// Date current = new Date();
			Calendar current = Calendar.getInstance();

			// current.add(Calendar.DAY_OF_MONTH, 6);// for test
			// 如果两个后缀不相等，就需要变更表
			String currentSuffix = sdf.format(current.getTime());
			if (!currentSuffix.equals(lastSuffix)) {
				suffix = currentSuffix;
			}
		} catch (SQLException e) {
			Res.logExceptionTrace(e);
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return suffix;
	}

}
