/**
 * <p>公司：Hangzhou NCI System Engineering, Ltd.</p>
 * 
 * @author ZHOUHM
 * @时间：2008-09-28
 * @功能：数据库连接池基础类，用来保存数据库连接池基本信息
 *
 */
package com.nci.svg.server.bean;

public class ConnBean {
	private String name;	// 连接池名称
	private String dbType;    //数据库类型
	private String username;	// 数据库登录用户名
	private String password;	// 数据库登录密码
	private String jdbcurl;		// 数据库地址
	private int max;		// 连接池最大连接数
	private long wait;		// 连接池最长等待时间
	private String driver;	// 数据库连接池启动类名称
	
	/**
	 * 获连接池名称
	 * @return String 连接池名称
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 设置连接池名称
	 * @param name String 连接池名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 获取连接池登录用户名
	 * @return String 用户名
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * 设置连接池登录用户名
	 * @param username String 登录用户名
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * 获取连接池登录密码
	 * @return String 登录密码
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * 设置连接池登录密码
	 * @param password String 登录密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * 获取连接池连接地址
	 * @return String 连接地址
	 */
	public String getJdbcurl() {
		return jdbcurl;
	}
	
	/**
	 * 设置连接池连接地址
	 * @param jdbcurl String 连接地址
	 */
	public void setJdbcurl(String jdbcurl) {
		this.jdbcurl = jdbcurl;
	}
	
	/**
	 * 获取连接池最大连接数
	 * @return integer 最大连接数
	 */
	public int getMax() {
		return max;
	}
	
	/**
	 * 设置连接池最大连接数
	 * @param max integer 最大连接数
	 */
	public void setMax(int max) {
		this.max = max;
	}
	
	/**
	 * 获取连接池等待时间
	 * @return long 等待时间
	 */
	public long getWait() {
		return wait;
	}
	
	/**
	 * 设置连接池等待时间
	 * @param wait long 等待时间
	 */
	public void setWait(long wait) {
		this.wait = wait;
	}
	
	/**
	 * 获取连接池驱动类名
	 * @return String 驱动类名
	 */
	public String getDriver() {
		return driver;
	}
	
	/**
	 * 设置连接池驱动类名
	 * @param driver String 驱动类名
	 */
	public void setDriver(String driver) {
		this.driver = driver;
	}

	/**
	 * 获取数据库类型
	 * @return String 数据库类型
	 */
	public String getDbType() {
		return dbType;
	}

	/**
	 * 设置数据库类型
	 * @param dbType 数据库类型
	 */
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
}
