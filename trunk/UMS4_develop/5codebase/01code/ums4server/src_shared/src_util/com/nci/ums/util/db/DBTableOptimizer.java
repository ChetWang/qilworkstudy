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
 * ���ݿ��Ż��������зֱ�������ʷ�ȹ���
 * 
 * @author Qil.Wong
 * 
 */
public class DBTableOptimizer implements UMSModule {

	public static final String SEND_OK_TABLE = "UMS_SEND_OK";

	public static final String TRANSMIT_OK_TABLE = "UMS_TRANSMIT_MES";

	public static final String TRANSMIT_ERR_TABLE = "UMS_TRANSMIT_ERR";

	// ��ǰΨһʵ��
	private static DBTableOptimizer op = null;

	/**
	 * �������ڣ���������UMS��һ�����м�¼
	 */
	private Date deployedDate;

	/**
	 * �����ļ���׺��ʽ
	 */
	private final String SUFFIX_SUFFIX_FORMAT = "_suffix_format";

	/**
	 * �����ļ���Ч�ڸ�ʽ
	 */
	private final String SUFFIX_VALID_PERIOD = "_valid_period";

	/**
	 * ����������Ϣ�Ĵ�Ŷ���
	 */
	private Properties config = new Properties();

	/**
	 * �Ż���ʱ��
	 */
	private Timer timer = null;

	/**
	 * �Ż�����
	 */
	private TimerTask optTask = null;

	/**
	 * ���캯��
	 */
	private DBTableOptimizer() {

	}

	/**
	 * ��ȡ���ݿ��Ż���
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
		// ��ȡ�־û��ļ������ļ���ŵ�һ�β�������
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
		// ����������ʱ�����Ż�������
		timer = new Timer();
		optTask = new TimerTask() {
			public void run() {
				opt();
			}
		};
		Calendar current = Calendar.getInstance();
		// ��ʱ������Ϊ�����賿
		current.add(Calendar.DAY_OF_MONTH, 1);
		current.set(current.get(Calendar.YEAR), current.get(Calendar.MONTH),
				current.get(Calendar.DAY_OF_MONTH), 0, 05, 0);
		timer.schedule(optTask, current.getTime(), 24 * 60 * 60 * 1000);

	}

	/**
	 * ��ʼ�Ż�
	 */
	private void opt() {
		DBOptLock.dbLock = true;
		try {
			Thread.sleep(60 * 1000);// �ȴ�1���ӣ����������ݿ�������
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
	 * �Ż����ݿ��
	 * 
	 * @param table
	 *            ���ݿ��
	 */
	private void optimizeDB(String table) {
		List tables = new ArrayList();
		String suffix = timeToAlter(table, tables);
		if (!suffix.equals("")) {
			// ��ʼ��任
			Res.log(Res.INFO, "��ʼ�Ż����ݿ⣺" + table);
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
				// �޸ı���
				String[] renameSQLs = null;
				if (table.indexOf("UMS_SEND_OK") >= 0) {
					renameSQLs = DialectUtil.getDialect()
							.getRenameSEND_OKTableSQL(table, suffix);
				} else if (table.indexOf("UMS_TRANSMIT") >= 0) {
					renameSQLs = DialectUtil.getDialect()
							.getRenameTrantmitTableSQL(table, suffix);
				}
				Res.log(Res.INFO, "��" + table + "������Ϊ" + table + suffix);
				for (int i = 0; i < renameSQLs.length; i++) {
					Statement stmt = conn.createStatement();
					Res.log(Res.DEBUG, "������SQL:" + renameSQLs[i]);
					try {
						stmt.execute(renameSQLs[i]);
					} catch (Exception e) {
						e.printStackTrace();
					}
					stmt.close();
				}
				Res.log(Res.INFO, "�����±�" + table);
				// �����±�
				for (int i = 0; i < sqls.length; i++) {
					Statement stmt = conn.createStatement();
					Res.log(Res.DEBUG, "�����±�SQL:" + sqls[i]);
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
						Res.log(Res.INFO, "ͳ����������ʷ��--->" + table + "_HIS");
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
						Res.log(Res.INFO, "ɾ�����ڱ�:" + dropSQL);
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
			Res.log(Res.INFO, "�����Ż����ݿ⣺" + table);
		}
	}

	/**
	 * �Ƿ�ʱ����б��
	 * 
	 * @return �����׺�����Ϊ����������Ҫ���
	 */
	private String timeToAlter(String table, List tables) {
		Connection conn = null;
		String suffix = "";
		String propPrefix = "";
		SimpleDateFormat sdf;
		// String
		try {
			// ��ѯ��ǰ���µı�ʱ���
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
				// �ų��ظ���¼�����ʷ��
				if (!tables.contains(temp) && temp.lastIndexOf("HIS") < 0) {
					tables.add(temp);
					Res.log(Res.DEBUG, "���б�" + temp);
					last = temp;
				}
			}
			Res.log(Res.DEBUG, "�����" + last);
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
			Res.log(Res.DEBUG, "�������ں�׺:" + lastSuffix);
			// Date current = new Date();
			Calendar current = Calendar.getInstance();

			// current.add(Calendar.DAY_OF_MONTH, 6);// for test
			// ���������׺����ȣ�����Ҫ�����
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
