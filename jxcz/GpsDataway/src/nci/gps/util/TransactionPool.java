package nci.gps.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.Timer;

import nci.gps.db.DBExcute;
import nci.gps.log.MsgLogger;
import edu.emory.mathcs.backport.java.util.concurrent.ConcurrentHashMap;

public class TransactionPool {
	
	private static TransactionPool pool = null;
	private ConcurrentHashMap transactionMap = null;
	private ConcurrentHashMap timerMap = null;
	private int timeout = 90*1000;
	
	private TransactionPool() {
		transactionMap = new ConcurrentHashMap();
		timerMap = new ConcurrentHashMap();
	}
	
	public static TransactionPool getInstance(){
		if(pool==null){
			pool = new TransactionPool();
		}
		return pool;
	}
	
	
	/**
	 * 添加事务
	 * @param key 主键，逻辑地址+序号
	 * @param conn sql连接
	 */
	public void addTransaction(final String key,final DBExcute dbe, final boolean closeConnectionAfterRollback){
		transactionMap.put(key, dbe);
		MsgLogger.log(MsgLogger.INFO, key+":new add");
		ActionListener listener = new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				MsgLogger.log(MsgLogger.INFO, key+":超时取消");
				dbe.rollback();	
				if(closeConnectionAfterRollback)
					dbe.close();
				transactionMap.remove(key);
				
				if((Timer)timerMap.get(key) != null)
					((Timer)timerMap.get(key)).stop();
				
				timerMap.remove(key);
			}			
		};
		Timer timer = new Timer(timeout,listener);
		timerMap.put(key, timer);
		timer.start();
	}
	
	/**
	 * 提交事务
	 * @param key
	 */
	public void commit(String key, boolean closeConnection){
		try {
			((DBExcute)transactionMap.get(key)).commit();
			MsgLogger.log(MsgLogger.INFO, key+":commit");
		} catch (SQLException e) {
			MsgLogger.logExceptionTrace("提交事务出错，连接序号："+key, e);
		}finally{
			if(closeConnection){
				((DBExcute)transactionMap.get(key)).close();
			}
			transactionMap.remove(key);
			((Timer)timerMap.get(key)).stop();
			timerMap.remove(key);
			
		}
	}

}
