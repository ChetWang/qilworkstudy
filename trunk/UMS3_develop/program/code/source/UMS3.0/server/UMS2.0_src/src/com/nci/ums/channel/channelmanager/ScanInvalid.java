/**
 * <p>Title: ScanInvalid.java</p>
 * <p>Description:
 *    扫描失效数据的线程，主要负责把超过了规定时间发送的数据送到out_ok表中。
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering， Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering， Ltd.</p>
 * Date        Author      Changes
 *NOV. 5 2003   张志勇       Created
 * @version 1.0
 */

package com.nci.ums.channel.channelmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import com.nci.ums.channel.channelinfo.QuitLockFlag;
import com.nci.ums.util.AppFactory;
import com.nci.ums.util.DBConnect;
import com.nci.ums.util.DataBaseOp;
import com.nci.ums.util.Res;
import com.nci.ums.util.Util;

public class ScanInvalid implements Runnable {
	
	private static String INVALID_TYPE_MEDIA_ERROR = "media_error";
	
	private static String INVALID_TYPE_SEND_ERROR="send_error";

	// 全局退出标志。
	private QuitLockFlag quitFlag;
	// 插入sql语句
	// private static final String insertSQL = "insert into
	// out_ok(BatchNo,SerialNo,SequenceNo,retCode,errMsg,StatusFlag,AppId,AppSerialNo,MediaId,SendId,RecvId,SubmitDate,SubmitTime,finishDate,finishTime,rep,doCount,priority,BatchMode,ContentMode,Content,TimeSetFlag,SetDate,SetTime,InvalidDate,InvalidTime,
	// ack,replyDes,replyNO) values(";
	private static final String insertSQL = "insert into out_ok(BatchNo,SerialNo,SequenceNo,retCode,errMsg,StatusFlag,AppId,AppSerialNo,MediaId,SendId,RecvId,SubmitDate,SubmitTime,finishDate,finishTime,rep,doCount,priority,BatchMode,ContentMode,Content,TimeSetFlag,SetDate,SetTime,InvalidDate,InvalidTime, ack,replyDes,reply) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	// 删除sql语句
	private static final String deleteSQL = "delete from out_ready where SerialNo = ? and SequenceNo = ? ";
	// 线程变量
	private Thread runner;

	/*
	 * 构造函数，得到唯一的退出共享锁
	 */
	public ScanInvalid() {

		quitFlag = QuitLockFlag.getInstance();
		Res.log(Res.INFO, "扫描失效数据线程对象产生");
	}

	/*
	 * 启动函数 产生一个线程对象，并启动这个线程
	 */
	public void start() {
		runner = new Thread(this, "ScanInvalid");
		runner.start();
		Res.log(Res.INFO, "扫描失效数据线程启动");
	}

	/*
	 * 启动函数 停止这个线程
	 */
	public void stop() {
		if (runner != null) {
			runner.interrupt();
			runner = null;
		}
		Res.log(Res.INFO, "扫描失效数据线程结束");
	}

	/*
	 * run函数，提供线程运行时的主要任务 一直循环直到被中断或者退出标志被设置。 每次扫描一次表以后，会sleep一段时间。
	 */
	public void run() {
		while (!Thread.interrupted() && !quitFlag.getLockFlag()) {
			Res.log(Res.INFO, "扫描失效数据.");
			scanTable();
			scan_media_valid_message();
			scanReply();
			try {
				// 根据得到的信息条数来确定sleep的时间。
				// 没隔1小时扫描一次。UMS3中，将直接由控制台负责应用服务的注册和调度。
				AppFactory.getInstance().refresh();
				// Res.log(Res.DEBUG, "扫描失效数据线程将睡眠1小时");
				Thread.sleep(60 * 60 * 1000);
//				 Thread.sleep(3*1000);
			} catch (InterruptedException e) {
				Res.log(Res.ERROR, "线程睡眠被中断！");
			}
		}
	}

	public void scan_media_valid_message() {
		Iterator it = ChannelManager.getOutMediaInfoHash().keySet().iterator();

		StringBuffer scan_media_valid_message_sql = new StringBuffer(
				"select * from out_ready where MediaId not in (");
		boolean not_first = false;
		boolean scan_media_valid_message = false;
		while (it.hasNext()) {
			scan_media_valid_message = true;
			if (not_first) {
				scan_media_valid_message_sql.append(",");
			} else {
				not_first = true;
			}
			scan_media_valid_message_sql.append((String) it.next());
		}
		scan_media_valid_message_sql.append(")");
		if (scan_media_valid_message) {
			ResultSet rs = null;
			Connection conn = null;
			Statement stmt = null;
			PreparedStatement insertStmt = null;
			PreparedStatement deleteStmt = null;
			Statement stmt4 = null;
			try {
				conn = DriverManager.getConnection(DataBaseOp.getPoolName());
				stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				rs = stmt.executeQuery(scan_media_valid_message_sql.toString());
				rs.setFetchSize(100);

				// 如果数据集合为空的话，则不做下面的工作。
				if (rs.next()) {
					Res.log(Res.INFO, "处理非法渠道消息");
					insertStmt = conn.prepareStatement(insertSQL);
					deleteStmt = conn.prepareStatement(deleteSQL);
					stmt4 = conn.createStatement();
					rs.beforeFirst();
					process(rs, insertStmt, deleteStmt, stmt4,INVALID_TYPE_MEDIA_ERROR);
					// process(rs, stmt2, stmt3);
				}
			} catch (SQLException e) {
				Res.log(Res.ERROR, "1091", e.getMessage());
				Res.logExceptionTrace(e);
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
					Res.log(Res.ERROR, "1091", e.getMessage());
					Res.logExceptionTrace(e);
				}
				try {
					if (stmt != null) {
						stmt.close();
					}
					if (insertStmt != null) {
						insertStmt.close();
					}
					if (deleteStmt != null) {
						deleteStmt.close();
					}
					if (stmt4 != null) {
						stmt4.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
					Res.log(Res.ERROR, "1091", e.getMessage());
					Res.logExceptionTrace(e);
				}
			}

		}
	}

	/*
	 * 扫描数据库表的函数，提供数据集 这个函数主要扫描的是失效的，不需要发送的信息。 做完扫描以后，调用process函数，进行处理。
	 */
	public void scanTable() {
		ResultSet rs = null;
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement insertStmt = null;
		PreparedStatement deleteStmt = null;
		Statement stmt4 = null;
		StringBuffer sql = null;
		int currentDate = Util.getCurrentTime("yyyyMMdd");
		int currentTime = Util.getCurrentTime("HHmmss");

		sql = new StringBuffer(
				"select * from out_ready where doCount>3 and ( InvalidDate < ")
				.append(currentDate).append(" or (InvalidDate = ").append(
						currentDate).append(" and InvalidTime < ").append(
						currentTime).append("))");
		// Res.log(Res.DEBUG, "sql=" + sql);

		try {
			conn = DriverManager.getConnection(DataBaseOp.getPoolName());
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(sql.toString());
			rs.setFetchSize(100);

			// 如果数据集合为空的话，则不做下面的工作。
			if (rs.next()) {
				Res.log(Res.INFO, "处理发送失败的消息.");
				insertStmt = conn.prepareStatement(insertSQL);
				deleteStmt = conn.prepareStatement(deleteSQL);
				stmt4 = conn.createStatement();
				rs.beforeFirst();
				process(rs, insertStmt, deleteStmt, stmt4,INVALID_TYPE_SEND_ERROR);
				// process(rs, stmt2, stmt3);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			Res.log(Res.ERROR, "1091", e.getMessage());
			Res.logExceptionTrace(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				Res.log(Res.ERROR, "1091", e.getMessage());
				Res.logExceptionTrace(e);
			}
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (insertStmt != null) {
					insertStmt.close();
				}
				if (deleteStmt != null) {
					deleteStmt.close();
				}
				if (stmt4 != null) {
					stmt4.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				Res.log(Res.ERROR, "1091", e.getMessage());
				Res.logExceptionTrace(e);
			}
		}
	}

	public void scanReply() {
		DBConnect conn = null;
		int currentDate = Util.getCurrentTime("yyyyMMdd") - 1;

		try {
			conn = new DBConnect();
			conn.executeUpdate("delete from out_reply where submitDate<"
					+ currentDate);
			conn.executeUpdate("delete from in_ready where submitDate<"
					+ currentDate);
			ResultSet rs = conn
					.executeQuery("select appid,count(*) from in_ready group by appid");
			while (rs.next()) {
				if (rs.getInt(2) > 1000) {
					String sql = Util.getDialect().getScanInvalid_scanReplySQL(
							rs);
					ResultSet rs1 = conn.executeQuery(sql);
					String batchno = null;
					if (rs1.next()) {
						batchno = rs1.getString("batchno");
						StringBuffer sql_del = new StringBuffer(
								"delete from in_ready where appid='").append(
								rs.getString("appid"))
								.append("' and batchno<=").append(batchno);
						conn.executeUpdate(sql_del.toString());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Res.log(Res.ERROR, "1091", e.getMessage());
			Res.logExceptionTrace(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				Res.log(Res.ERROR, "1091", e.getMessage());
				Res.logExceptionTrace(e);
			}
		}
	}

	/*
	 * 处理函数， 只是简单的把失效的数据从out_ready取出，放到out_ok表中。 注意errMsg 和retCode 的数值
	 */
	public void process(ResultSet rs, PreparedStatement insertStmt,
			PreparedStatement deleteStmt, Statement stmt4,String invalidType) {
		String currentTime = Util.getCurrentTimeStr("yyyyMMddHHmmss");
		String finishDate = currentTime.substring(0, 8);
		String finishTime = currentTime.substring(8, 14);
		try {
			while (rs.next()) {
				insertStmt.clearParameters();
				insertStmt.setString(1, Util.replaceNULL(rs
						.getString("BatchNo")));
				insertStmt.setInt(2, rs.getInt("SerialNo"));
				insertStmt.setInt(3, rs.getInt("SequenceNo"));
				insertStmt.setString(4, rs.getString("retcode"));
				int retcode = rs.getInt("retcode");
				String errMsg = "";
				if (retcode == 0) {
					errMsg = "发送时间失效";
				} else if (retcode == 11) {
					errMsg = "用户已停机";
				} else if (retcode == 13) {
					errMsg = "用户暂停使用";
				} else if (retcode == 27) {
					errMsg = "用户已关机";
				} else if (retcode == 50) {
					errMsg = "用户手机无存储空间";
				} else if (retcode == 93) {
					errMsg = "计费手机欠费";
				} else if (retcode == 172) {
					errMsg = "网络无响应";
				} else if (retcode == 40) {
					errMsg = "不合法参数";
				} else if (retcode == 42) {
					errMsg = "无此权限";
				} else {
					errMsg = "其它错误";
				}
				if(invalidType.equals(INVALID_TYPE_MEDIA_ERROR)){
					errMsg = "指定的渠道不存在";
				}
				insertStmt.setString(5, errMsg);
				insertStmt.setInt(6, rs.getInt("StatusFlag"));
				insertStmt.setString(7, rs.getString("AppId"));
				insertStmt.setString(8, Util.replaceNULL(rs
						.getString("AppSerialNo")));
				insertStmt.setString(9, Util.replaceNULL(rs
						.getString("mediaID")));
				insertStmt.setString(10, rs.getString("SendId"));
				insertStmt.setString(11, rs.getString("RecvId"));
				insertStmt.setString(12, Util.replaceNULL(rs
						.getString("SubmitDate")));
				insertStmt.setString(13, Util.replaceNULL(rs
						.getString("SubmitTime")));
				insertStmt.setString(14, Util.replaceNULL(finishDate));
				insertStmt.setString(15, Util.replaceNULL(finishTime));
				insertStmt.setInt(16, rs.getInt("rep"));
				insertStmt.setInt(17, rs.getInt("doCount"));
				insertStmt.setInt(18, rs.getInt("priority"));
				insertStmt.setString(19, rs.getString("BatchMode"));
				insertStmt.setString(20, rs.getString("ContentMode"));
				insertStmt.setString(21, Util.replaceNULL(rs
						.getString("Content")));
				insertStmt.setString(22, rs.getString("TimeSetFlag"));
				insertStmt.setString(23, Util.replaceNULL(rs
						.getString("SetDate")));
				insertStmt.setString(24, Util.replaceNULL(rs
						.getString("SetTime")));
				insertStmt.setString(25, Util.replaceNULL(rs
						.getString("InvalidDate")));
				insertStmt.setString(26, Util.replaceNULL(rs
						.getString("InvalidTime")));
				insertStmt.setInt(27, rs.getInt("ack"));
				insertStmt.setString(28, Util.replaceNULL(rs
						.getString("replyDes")));
				insertStmt.setString(29, Util
						.replaceNULL(rs.getString("reply")));
				insertStmt.executeUpdate();

				deleteStmt.clearParameters();
				deleteStmt.setInt(1, rs.getInt("SerialNo"));
				deleteStmt.setInt(2, rs.getInt("SequenceNo"));
				int i = deleteStmt.executeUpdate();

				// out_ready中信息若需回执或回复，这里是发送不成功
				if (rs.getInt("ack") != 0) {
					StringBuffer sb = new StringBuffer("");
					String retCode = "1005";
					sb = sb
							.append(
									"insert into in_ready(BatchNo,SerialNo,sequenceNO,retCode,errMsg,statusFlag,AppId,AppSerialNo,MediaId,SendId,RecvId,SubmitDate,SubmitTime,finishDate,finishTime,content,ack,reply,doCount)")
							.append(" values('").append(finishDate)
							.append("',").append(
									Integer.parseInt(rs.getString("serialNO")))
							.append(",0,'").append(retCode).append("','")
							.append(errMsg).append("',0,'").append(
									rs.getString("appID")).append("','")
							.append(rs.getString("appSerialNO")).append("','")
							.append(rs.getString("mediaID")).append("','")
							.append(rs.getString("sendID")).append("','")
							.append(rs.getString("recvID")).append("','")
							.append(rs.getString("submitDate")).append("','")
							.append(rs.getString("submitTime")).append(
									"','','','").append("发送不成功:" + errMsg)
							.append("',").append(rs.getInt("ack")).append(",'")
							.append(rs.getString("reply")).append("',0)");
					// Res.log(Res.DEBUG,"insertSQL=" + insertSQL);
					stmt4.executeUpdate(sb.toString());
					Res.log(Res.DEBUG, "插入一条信息到数据库成功！");
				} // if需回执或回复
			}
		} catch (SQLException e) {
			e.printStackTrace();
			Res.log(Res.ERROR, "1091", e.getMessage());
			Res.logExceptionTrace(e);
		}
	}

	public static void main(String[] args) {
		ScanInvalid scanInvalid1 = new ScanInvalid();
		Res.log(Res.INFO, "扫描失效数据测试！");
		scanInvalid1.start();
	}
}