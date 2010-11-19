/*
 * @(#)DBLogger.java	1.0  08/23/2010
 *
 * Copyright (c) 2010, CREAWAY and/or its affiliates. All rights reserved.
 * CREAWAY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.creaway.inmemdb.util;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * 内存数据库日志记录器
 * 
 * @author Qil.Wong
 * 
 */
public class DBLogger {

	/**
	 * 调试模式
	 */
	public static final int DEBUG = 0;

	/**
	 * 通知模式
	 */
	public static final int INFO = 1;

	/**
	 * 告警模式
	 */
	public static final int WARN = 2;

	/**
	 * 错误模式
	 */
	public static final int ERROR = 3;

	/**
	 * log4j的logger对象
	 */
	private static Logger logger;

	/**
	 * FQCN名称
	 */
	private static String fqcnName;

	static {
		logger = Logger.getLogger(DBLogger.class);
		fqcnName = DBLogger.class.getName();
	}

	/**
	 * 日志记录
	 * 
	 * @param level
	 *            日志级别
	 * @param msg
	 *            日志消息
	 */
	public static void log(int level, String msg) {
		log(level, msg, null);
	}

	/**
	 * 日志记录
	 * 
	 * @param level
	 *            日志级别
	 * @param msg
	 *            日志消息
	 * @param t
	 *            抛出的异常
	 */
	public static void log(int level, String msg, Throwable e) {
		Level l = null; //log4j的Level级别，通过参数level判断
		switch (level) {
		case DEBUG:
			l = Level.DEBUG;
			break;
		case INFO:
			l = Level.INFO;
			break;
		case WARN:
			l = Level.WARN;
			break;
		case ERROR:
			l = Level.ERROR;
			break;
		default:
			break;
		}
		logger.log(fqcnName, l, msg, e);
	}

}
