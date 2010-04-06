/**
 * <p>Title: InChannel.java</p>
 * <p>Description:
 *    抽象类，提供给各个拨入渠道类来继承，实现了ChannelIfc接口。
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering， Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering， Ltd.</p>
 * Date        Author      Changes
 *NOV. 5 2003   张志勇       Created
 * @version 1.0
 */

package com.nci.ums.channel.inchannel;

import java.sql.ResultSet;

import com.nci.ums.channel.ChannelIfc;
import com.nci.ums.channel.channelinfo.LockFlag;
import com.nci.ums.channel.channelinfo.MediaInfo;
import com.nci.ums.channel.channelinfo.QuitLockFlag;
import com.nci.ums.util.DBConnect;
import com.nci.ums.util.DialectUtil;
import com.nci.ums.util.Res;
import com.nci.ums.util.SerialNO;
import com.nci.ums.util.Util;

public abstract class InChannel implements ChannelIfc, Runnable {

	protected QuitLockFlag quitFlag;
	protected boolean isQuit;
	protected MediaInfo mediaInfo;
	private Thread runner;
	protected boolean startOnce = false;

	/*
	 * 让线程开始工作的函数。
	 */
	public void start() {
		runner = new Thread(this, mediaInfo.getMediaName());
		runner.start();
		if (!isQuit)
			Res.log(Res.INFO, mediaInfo.getMediaName() + "拨入渠道线程启动！");
		startOnce = true;
	}

	public boolean isStartOnce() {
		return startOnce;
	}

	/*
	 * 停止这个线程
	 */
	public void stop() {
		setIsQuit(true);
		runner.interrupt();
		Res.log(Res.INFO, mediaInfo.getMediaName() + "拨入渠道线程退出服务！");
	}

	/*
	 * 构造函数
	 */
	public InChannel(MediaInfo mediaInfo) {
		quitFlag = QuitLockFlag.getInstance();
		this.mediaInfo = mediaInfo;
		Res.log(Res.INFO, mediaInfo.getMediaId() + mediaInfo.getMediaName()
				+ "拨入渠道对象产生！");
	}

	/*
	 * 返回线程的状态，如果返回为true 表示现成还在运行中，如果为false,表示线程已经停止。
	 */
	public boolean getThreadState() {
		return !isQuit;
	}

	/*
	 * run函数，提供线程运行时的主要任务 一直循环直到被中断或者退出标志被设置。 每次扫描一次表以后，会sleep一段时间。
	 */
	public abstract void run();

	/*
	 * 设置线程退出标志
	 */
	public synchronized void setIsQuit(boolean isQuit) {
		this.isQuit = isQuit;
	}

	/*
	 * 返回线程退出标志
	 */
	public synchronized boolean getIsQuit() {
		return isQuit;
	}

	/*
	 * 把得到的信息送入in_ready表中 因联通sp号为3595，且查找原消息时还有问题，所有还需修改
	 */
	public void insertTable(int status, String sendNumber, String recvNumber,
			String msg, String mediaId, String msgID1, int data_code) {
		StringBuffer insertSQL = new StringBuffer();
		String content = msg;
		if (data_code != 4 && data_code != 21) {
			content = Util.convertHalfToFull(msg);
		}
		String currentTime = Util.getCurrentTimeStr("yyyyMMddHHmmss");
		String submitDate = currentTime.substring(0, 8);
		String submitTime = currentTime.substring(8);
		recvNumber = recvNumber.trim();
		String msgID = msgID1.trim();
		String serial = null;

		// 若为回复消息,appID,appSerialNO设为原消息的ID与消息序号
		String src_appID = "";
		String src_appSerialNO = "";
		String src_sendID = "";
		String src_reply = "";
		String subApp = "";
		boolean replyFlag = false;
		String retCode = "1003";
		int ack = 0;
		DBConnect db = null;

		try {
			db = new DBConnect();
			// serial = db.getSerial();
			serial = SerialNO.getInstance().getSerial();
			// 取得回复消息的原消息参数
			// 955980
			String replyNO = "";
			if (msgID.equalsIgnoreCase("")) {
				if (mediaInfo.getMediaId().equalsIgnoreCase("013")) {
					if (recvNumber.length() > 9
							&& recvNumber.substring(9, 10)
									.equalsIgnoreCase("0")) {
						Res.log(Res.DEBUG, recvNumber.substring(9, recvNumber
								.length()));
						replyNO = recvNumber.substring(9, recvNumber.length())
								.trim();
						replyFlag = true;
					} else {
						replyNO = recvNumber.substring(9, recvNumber.length())
								.trim();
					}

					System.out.println("---------------receivernumber="
							+ recvNumber);
					System.out.println("---------------replyno=" + replyNO);
				} else if (mediaInfo.getMediaId().equalsIgnoreCase("010")) {
					if (recvNumber.length() > 5) {
						if (recvNumber.length() > 5
								&& recvNumber.substring(5, 6).equalsIgnoreCase(
										"0")) {
							Res.log(Res.DEBUG, recvNumber.substring(5,
									recvNumber.length()));
							replyNO = recvNumber.substring(5,
									recvNumber.length()).trim();
							replyFlag = true;
						} else {
							replyNO = recvNumber.substring(5,
									recvNumber.length()).trim();
						}
					}
				} else {
					if (recvNumber.length() > 6) {
						if (recvNumber.length() > 6
								&& recvNumber.substring(6, 7).equalsIgnoreCase(
										"0")) {
							Res.log(Res.DEBUG, recvNumber.substring(6, 7));
							replyNO = recvNumber.substring(6,
									recvNumber.length()).trim();
							replyFlag = true;
						} else {
							replyNO = recvNumber.substring(6,
									recvNumber.length()).trim();
						}
					}
				}
			} else {
				replyNO = msgID;
				replyFlag = true;
			}
			// Res.log(Res.DEBUG,"replyNO"+replyNO);
			if (replyFlag) {
				ack = 1;
				if (!msgID.equalsIgnoreCase("")) {
					db.prepareStatement(DialectUtil.getDialect()
							.getInchannel_replyFlagWithMsgIDSQL());
				} else {
					db.prepareStatement(DialectUtil.getDialect()
							.getInchannel_replyFlagWithoutMsgIDSQL());
				}
				db.setString(1, replyNO);
				ResultSet rs1 = db.executeQuery();
				if (rs1.next()) {
					src_appID = rs1.getString("appID");
					src_appSerialNO = rs1.getString("appSerialNO");
					src_sendID = rs1.getString("sendID");
					src_reply = rs1.getString("reply");
					if (rs1.getString("subApp") != null) {
						subApp = rs1.getString("subApp");
					}
					ack = rs1.getInt("ack");
					if (!msgID.equalsIgnoreCase("")) {
						if (status == 0) {
							content = "回执-发送成功!";
							retCode = "1001";
						} else {
							content = "回执-发送失败!";
							retCode = Integer.toString(status);
						}
					} else {
						retCode = "1002";
					}

					if (ack == 2 || ack == 1) {
						StringBuffer delSQL = new StringBuffer(
								"delete from out_reply where batchNO='")
								.append(rs1.getString("batchNO")).append(
										"' and SerialNo = ").append(
										rs1.getInt("SerialNo")).append(
										" and  SequenceNo=").append(
										rs1.getInt("SequenceNo"));
						Res.log(Res.DEBUG, "delSQL=" + delSQL);
						db.executeUpdate(delSQL.toString());
						Res.log(Res.DEBUG, "成功从out_reply表中删除一条记录！");
					}
				} else {
					src_appID = "";
				}
				rs1.close();
			} else {
				if (replyNO.length() > 0) {
					String tempStr = replyNO;
					if (replyNO.length() > 4) {
						replyNO = tempStr.substring(0, 4); // 前4位是取应用号
						subApp = tempStr.substring(4, tempStr.length()); // 4
						// 位后面全部是应用号
					}
					db
							.prepareStatement("select * from UMS_APPLICATION where APPT_SPNO=?");
					db.setString(1, replyNO);
					ResultSet rs1 = db.executeQuery();
					if (rs1.next()) {
						src_appID = rs1.getString("APPT_ID");
						src_sendID = recvNumber;
						Res.log(Res.DEBUG, src_appID);
					}
					rs1.close();
				} else {
					/*
					 * 暂时去掉转发功能 db.prepareStatement("select * from filterMessage
					 * where content=?"); db.setString(1,content); ResultSet
					 * rs1=db.executeQuery(); if(rs1.next()) {
					 * src_appID=rs1.getString("appID"); src_sendID=recvNumber;
					 * Res.log(Res.DEBUG,src_appID); }else{ HashMap outMsg=new
					 * HashMap(); outMsg.put("APP","3010"); outMsg.put("ID","");
					 * outMsg.put("MSG","指令不对！请确认！");
					 * outMsg.put("UMS_TO",sendNumber); outMsg.put("ACK",new
					 * String(Integer.toString(0)));
					 * outMsg.put("MESSAGETYPE",new
					 * String(Integer.toString(0)));
					 * Res.log(Res.DEBUG,"处理转发内容"); Process1002 process=new
					 * Process1002(); process.process(outMsg,1,serial); }
					 * rs1.close();
					 */
				}
			}
			// 若非回复信息或回复至邮件信息
			if (src_reply == null) {
				src_reply = "";
			}

			// 进来肯定给应用
			if (src_appID.length() > 0) {
				if (src_reply.length() == 0
						|| (src_reply.length() > 0 && src_reply.indexOf("@") >= 0)) {
					insertSQL = insertSQL
							.append(
									"insert into in_ready(BatchNo,SerialNo,sequenceNO,retCode,errMsg,statusFlag,AppId,AppSerialNo,MediaId,SendId,RecvId,SubmitDate,SubmitTime,finishDate,finishTime,content,ack,reply,msgType,subApp)")
							.append(" values('").append(currentTime).append(
									"',").append(Integer.parseInt(serial))
							.append(",0,'").append(retCode).append("','',0,'")
							.append(src_appID).append("','").append(
									src_appSerialNO).append("','").append(
									mediaId).append("','").append(sendNumber)
							.append("','").append(src_sendID).append("','")
							.append(submitDate).append("','")
							.append(submitTime).append("','','','").append(
									content).append("',").append(ack).append(
									",'").append(src_reply).append("',")
							.append(data_code).append(",'").append(subApp)
							.append("')");
					Res.log(Res.DEBUG, "insertSQL=" + insertSQL);
					db.executeUpdate(insertSQL.toString());
					Res.log(Res.DEBUG, "插入一条信息到数据库成功！");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Res.log(Res.ERROR, "1091", e.getMessage());
			Res.logExceptionTrace(e);
		} finally {
			if (db != null) {
				try {
					db.close();
				} catch (Exception ex) {
					Res.logExceptionTrace(ex);
				}
			}
		}
	}

	public LockFlag getDataLockFlag() {
		// TODO Auto-generated method stub
		return null;
	}
}