package com.creaway.inmemdb;

import java.sql.Connection;

/**
 * 支持集群的数据库服务端session接口
 * 
 * @author Qil.Wong
 * 
 */
public interface InMemSession {

	/**
	 * 判断当前数据库Session是否是slave状态，如果是slave状态，不需要进行群发集群消息
	 * 
	 * @return boolean
	 */
	public boolean isSlave();

	/**
	 * 设置当前数据库Session是否是slave状态
	 * @param slave
	 */
	public void setSlave(boolean slave);

	/**
	 * 创建数据库JDBC连接
	 * @return
	 */
	public Connection createConnection();

	/**
	 * 获取Session ID，一个Session对应一个唯一的Session ID
	 * @return
	 */
	public int getId();

	/**
	 * 关闭Session
	 */
	public void close();
}
