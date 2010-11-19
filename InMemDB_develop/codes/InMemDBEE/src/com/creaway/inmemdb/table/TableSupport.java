/*
 * @(#)TableSupport.java	1.0  08/27/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.table;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.management.ObjectName;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.creaway.inmemdb.core.InMemDBServer;
import com.creaway.inmemdb.core.InMemDBServerH2;
import com.creaway.inmemdb.trigger.MaxRowLimitTrigger;
import com.creaway.inmemdb.util.ConnectionManager;
import com.creaway.inmemdb.util.DBLogger;
import com.creaway.inmemdb.util.Functions;

/**
 * 内存数据库表支持模块，负责初始化时的表创建，退出时的数据持久化处理等
 * 
 * @author Qil.Wong
 * 
 */
public class TableSupport implements TableSupportIfc {

	/**
	 * 表结构的Map对象，主键是表名，值是列的顺序集合
	 */
	private Map<String, String[]> tableStructures;

	public final static String AUTO_INCREMENT_COLUMN = "autoint";

	private final static String AUTO_INCREMENT_SYNTAX = " AUTO_INCREMENT";

	private MaxRowLimiter inMemRowLimiter;

	public TableSupport() {

	}

	@Override
	public void startModule(Map<?, ?> params) {
		Connection conn = ConnectionManager.getConnection();
		DBLogger.log(DBLogger.INFO, "启动表支持模块，开始创建内存数据库表...");
		createTable(conn, true);
		DBLogger.log(DBLogger.INFO, "内存数据库表创建完成!");
		try {
			InMemDBServer.getInstance().registerMBean(
					this,
					new ObjectName(InMemDBServerH2.MBEAN_PREFIX
							+ "Table Support"));
		} catch (Exception e) {
			DBLogger.log(DBLogger.ERROR, "", e);
		}
		inMemRowLimiter = new InMemMaxRowLimiter();
		inMemRowLimiter.start();
	}

	public void rebuildTable() {

	}

	public void createTable(Connection conn, boolean storeTableStructure) {
		if (storeTableStructure) {
			tableStructures = new HashMap<String, String[]>();
		}
		Document tableConfig = Functions.getXMLDocument(getClass().getResource(
				"/resources/tables.xml"));
		NodeList tables = tableConfig.getDocumentElement()
				.getElementsByTagName("table");
		try {
			// ConnectionManager.setSlave(conn, true);
			Statement st = conn.createStatement();
			for (int i = 0; i < tables.getLength(); i++) {
				Element table = (Element) tables.item(i);
				String tableName = table.getAttribute("name").toUpperCase();
				int maxRowCount = -1;
				if (!table.getAttribute("maxRowCount").equals("")) {
					maxRowCount = Integer.parseInt(table
							.getAttribute("maxRowCount"));
				}
				if (storeTableStructure
						&& tableStructures.containsKey(tableName)) {
					// 说明有两个或两个以上同名的表配置
					DBLogger.log(DBLogger.ERROR, "重复的表名：" + tableName);
					continue;
				}
				// 创建db table的字符串缓存对象
				StringBuffer tableCreate = new StringBuffer("CREATE TABLE "
						+ tableName);
				// 创建索引的字符串缓存对象
				StringBuffer indexCreate = new StringBuffer("CREATE INDEX "
						+ tableName + "_IDX ON " + tableName);
				// 创建关键字的字符串缓存对象
				StringBuffer primaryKeyCreate = new StringBuffer("PRIMARY KEY ");
				NodeList columns = table.getElementsByTagName("column");
				int columnCounts = maxRowCount > 0 ? columns.getLength() + 1
						: columns.getLength();
				String[] columnArr = new String[columnCounts];
				boolean hasKey = false;
				boolean extraIndex = false;
				for (int n = 0; n < columns.getLength(); n++) {
					Element column = (Element) columns.item(n);
					String name = column.getAttribute("name").trim()
							.toUpperCase();
					columnArr[n] = name;
					String type = column.getAttribute("type").trim();

					boolean autoIncrement = column
							.getAttribute("autoIncrement").trim().toLowerCase()
							.equals("true");
					// 字段长度
					int length = column.getAttribute("length").trim()
							.equals("") ? -1 : Integer.parseInt(column
							.getAttribute("length"));
					// 是否是主键
					boolean isKey = column.getAttribute("primarykey").trim()
							.toLowerCase().equals("true");
					// 是否需要索引
					boolean isIndex = column.getAttribute("index").trim()
							.toLowerCase().equals("true");
					if (n == 0) {
						tableCreate.append("(");
					} else {
						tableCreate.append(",");
					}
					tableCreate.append(name).append(" ").append(type);
					if (length != -1) {
						tableCreate.append("(").append(length).append(")");
					}
					if (autoIncrement && storeTableStructure) {// 只有在存储表格式时（即内存数据库表而非持久数据库表时，才建立自增长）
						tableCreate.append(AUTO_INCREMENT_SYNTAX);
					}
					if (isKey) {
						tableCreate.append(" NOT NULL"); // 主键必须要加NOT NULL限制
						// 生成主键sql
						if (!hasKey) {
							primaryKeyCreate.append("(");
							hasKey = true;
						} else {
							primaryKeyCreate.append(",");
						}
						primaryKeyCreate.append(name);
					}
					if (isIndex) {
						// 索引生成sql
						if (!extraIndex) {
							indexCreate.append("(");
							extraIndex = true;
						} else {
							indexCreate.append(",");
						}
						indexCreate.append(name);
					}
				}
				if (maxRowCount > 0) {
					tableCreate.append(",").append(
							AUTO_INCREMENT_COLUMN + " number ");
					if (storeTableStructure) { // 只有在存储表格式时（即内存数据库表而非持久数据库表时，才建立自增长）
						tableCreate.append(AUTO_INCREMENT_SYNTAX);
						if (!extraIndex) {
							indexCreate.append("(");
							extraIndex = true;
						} else {
							indexCreate.append(",");
						}
						indexCreate.append(AUTO_INCREMENT_COLUMN);
					}
					columnArr[columnArr.length - 1] = AUTO_INCREMENT_COLUMN;

				}
				if (hasKey) {
					primaryKeyCreate.append(")");
					tableCreate.append(",").append(primaryKeyCreate);
				}
				if (extraIndex) {
					indexCreate.append(")");
				}
				tableCreate.append(")");
				try {
					DBLogger.log(DBLogger.DEBUG, "创建表" + tableName + " SQL:"
							+ tableCreate.toString());
					// 创建表
					st.execute(tableCreate.toString());

				} catch (SQLException e) {
					DBLogger.log(DBLogger.ERROR, "创建表" + tableName + "错误: "
							+ tableCreate, e);
				}
				// 创建索引
				if (extraIndex) {
					try {
						DBLogger.log(DBLogger.DEBUG, "创建表" + tableName
								+ "索引 SQL:" + indexCreate.toString());
						st.execute(indexCreate.toString());
					} catch (SQLException e) {
						DBLogger.log(DBLogger.ERROR, "在表" + tableName
								+ "上创建索引错误: " + indexCreate, e);
					}
				}
				if (storeTableStructure) {
					tableStructures.put(tableName, columnArr);
				}
				if (maxRowCount > 0 && storeTableStructure) { // 添加限制行数的触发器，只在内存数据库表中建立，持久层不做考虑
					StringBuffer triggerSQL = new StringBuffer();
					triggerSQL.append("CREATE TRIGGER ");
					triggerSQL.append(tableName)
							.append("_MAXROW_LIMIT AFTER INSERT ON ")
							.append(tableName).append(" CALL \"")
							.append(MaxRowLimitTrigger.class.getName())
							.append("\"");
					st.execute(triggerSQL.toString());
				}
			}
			st.close();
			conn.commit();

		} catch (SQLException e) {
			DBLogger.log(DBLogger.ERROR, "创建数据库表失败!", e);
			ConnectionManager.rollbackConnection(conn);
		} finally {
			ConnectionManager.releaseConnection(conn);
		}
	}

	@Override
	public void shutdownModule(Map<?, ?> params) {
		DBLogger.log(DBLogger.INFO, "关闭数据库表支持模块");
		inMemRowLimiter.stopLimiter();
		inMemRowLimiter = null;
		
		Connection conn = ConnectionManager.getConnection();
		try {
			Statement st = conn.createStatement();
			Iterator<String> it = tableStructures.keySet().iterator();
			while (it.hasNext()) {
				st.execute("DROP TABLE " + it.next());
			}
			st.close();
			conn.commit();
		} catch (SQLException e) {
			DBLogger.log(DBLogger.ERROR, "移除数据库表失败！", e);
			ConnectionManager.rollbackConnection(conn);
		} finally {
			ConnectionManager.releaseConnection(conn);
		}

	}

	public MaxRowLimiter getInMemMaxRowLimiter() {
		return inMemRowLimiter;
	}

	@Override
	public Map<String, String[]> getTableStructure() {
		return tableStructures;
	}

	@Override
	public void rebuildTable(String user, String password, String tableName) {
		if (InMemDBServer.getInstance().checkJMXSecurity(user, password)) {
			Connection conn = ConnectionManager.getConnection();
			try {
				Statement st = conn.createStatement();
				st.execute("DROP TABLE " + tableName);
				getTableStructure().remove(tableName);
				createTable(conn, true);
				DBLogger.log(DBLogger.INFO, "重建内存数据库表" + tableName + "成功");
			} catch (SQLException e) {
				DBLogger.log(DBLogger.ERROR, "重建内存数据库表" + tableName + "失败", e);
			} finally {
				ConnectionManager.releaseConnection(conn);
			}
		}
	}

}
