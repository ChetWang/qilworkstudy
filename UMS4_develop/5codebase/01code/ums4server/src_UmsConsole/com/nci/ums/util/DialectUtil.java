package com.nci.ums.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.nci.ums.dialect.DB2Dialect;
import com.nci.ums.dialect.MysqlDialect;
import com.nci.ums.dialect.OracleDialect;
import com.nci.ums.dialect.SQLServerDialect;
import com.nci.ums.dialect.UMSDialect;

public class DialectUtil {
	
	private static UMSDialect dialect;
	
	public static final int UMS_DATABASE_SQLSERVER = 1;
	public static final int UMS_DATABASE_ORACLE = 2;
	public static final int UMS_DATABASE_DB2 = 3;
	public static final int UMS_DATABASE_SYBASE = 4;
	public static final int UMS_DATABASE_DERBY = 5;
	public static final int UMS_DATABASE_MYSQL = 6;
	private static int dbType = -1;

	
	public static void load(){
		dbType = getDataBaseType();
	}
	
	public synchronized static UMSDialect getDialect() {
		if (dialect == null) {
			switch (dbType) {
			case UMS_DATABASE_DB2:
				dialect = new DB2Dialect();
				break;
			case UMS_DATABASE_DERBY:
				break;
			case UMS_DATABASE_MYSQL:
				dialect = new MysqlDialect();
				break;
			case UMS_DATABASE_ORACLE:
				dialect = new OracleDialect();
				break;
			case UMS_DATABASE_SQLSERVER:
				dialect = new SQLServerDialect();
				break;
			case UMS_DATABASE_SYBASE:
				break;
			default:
				return null;
			}
		}
		return dialect;
	}
	
	/**
	 * get the database type which ums use. the database property file is
	 * restrict to be pattern of which the "driver" property must be at first
	 * line.
	 * 
	 * Since UMS3.0, created by Qil.Wong
	 * 
	 * @return database type. UMS_DATABASE_SQLSERVER = 1; UMS_DATABASE_ORACLE =
	 *         2; UMS_DATABASE_DB2 = 3; UMS_DATABASE_SYBASE = 4;
	 *         UMS_DATABASE_DERBY = 5; UMS_DATABASE_MYSQL = 6;
	 */
	public static int getDataBaseType() {
		BufferedReader reader = null;
		if (dbType == -1) {
			try {
				reader = new BufferedReader(
						new InputStreamReader(
								(new DynamicUMSStreamReader()
										.getInputStreamFromFile("/resources/umsdb.props"))));
				try {
					String s = reader.readLine();
					if (s.indexOf("OracleDriver") > 0) {
						return UMS_DATABASE_ORACLE;
					}
					if (s.indexOf("SQLServerDriver") > 0) {
						return UMS_DATABASE_SQLSERVER;
					}
					if (s.indexOf("DB2Driver") > 0) {
						return UMS_DATABASE_DB2;
					}
					if (s.indexOf("mysql") > 0) {
						return UMS_DATABASE_MYSQL;
					}
					if (s.indexOf("derby") > 0) {
						return UMS_DATABASE_DERBY;
					}
					if (s.indexOf("sybase") > 0) {
						return UMS_DATABASE_SYBASE;
					}
				} catch (IOException e) {
					Res
							.log(Res.ERROR,
									"Read the database property file error!");
					return -1;
				}
			} catch (Exception e) {
				Res.log(Res.ERROR, "Can not read the database property file!"
						+ e.getMessage());
				return -1;
			}finally{
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
		
		return dbType;
	}
}
