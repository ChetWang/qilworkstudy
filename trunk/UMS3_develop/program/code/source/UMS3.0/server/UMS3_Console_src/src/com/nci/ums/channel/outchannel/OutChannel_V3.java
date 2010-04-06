/*
 * OutChannel_V3.java
 *
 * Created on 2007-9-28, 16:13:54
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nci.ums.channel.outchannel;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.nci.ums.channel.ChannelIfc;
import com.nci.ums.channel.channelinfo.LockFlag;
import com.nci.ums.channel.channelinfo.MediaInfo;
import com.nci.ums.channel.channelinfo.QuitLockFlag;
import com.nci.ums.channel.channelmanager.ChannelManager;
import com.nci.ums.periphery.exception.OutOfMaxSequenceException;
import com.nci.ums.util.DBUtil_V3;
import com.nci.ums.util.DataBaseOp;
import com.nci.ums.util.PropertyUtil;
import com.nci.ums.util.Res;
import com.nci.ums.util.ServletTemp;
import com.nci.ums.util.Util;
import com.nci.ums.v3.message.UMSMsg;
import com.nci.ums.v3.message.basic.BasicMsg;
import com.nci.ums.v3.message.basic.Participant;
import com.nci.ums.v3.message.euis.EnterpriseUserInfo;
import com.nci.ums.v3.policy.ChannelPolicy;
import com.nci.ums.v3.policy.Policy;

/**
 * 
 * @author Qil.Wong
 */
public abstract class OutChannel_V3 implements ChannelIfc, Runnable {

	protected MediaInfo mediaInfo;
	protected int getMsgCounts = 0;
	protected String sql_queryUMSMsgs = "";
	protected Thread runner;
	// 全部线程退出标记
	protected QuitLockFlag quitFlag;
	// 本线程退出标志
	protected boolean stop = false;
	protected DBUtil_V3 dbUtil = new DBUtil_V3();
	protected EnterpriseUserInfo eui;

	protected boolean startOnce = false;

	public static String nciV3Media_ID;

	public static String nciV2Media_ID;

	protected DocumentBuilder db;

	protected Document doc;

	/**
	 * 是否格式化消息
	 */
	protected boolean formatMessage = false;

	protected boolean groupMessage = false;

	protected static int MSG_ACTUAL_EXIST = -741;

	protected String msgFmtFile = null;

	protected ArrayList msgFmts = null;

	protected boolean repeatMsgCheckFlag = false;

	/**
	 * 重复消息判断的最大间隔时间，单位为秒
	 */
	protected int durationTime = 10;

	protected boolean allMsgFiltered = true; // 所有消息都是过滤的消息标记，为区别无消息，特设次标记位。

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

	static {
		Properties props2 = new Properties();
		try {
			InputStream is = new ServletTemp()
					.getInputStreamFromFile("/resources/media.props");
			props2.load(is);
			is.close();
		} catch (IOException e) {
			Res.logExceptionTrace(e);
		}
		nciV2Media_ID = props2.getProperty("NCIMEDIA_ID");
		nciV3Media_ID = props2.getProperty("NCIV3MEDIA_ID");
	}

	public OutChannel_V3() {
	}

	public OutChannel_V3(MediaInfo mediaInfo) {
		this.mediaInfo = mediaInfo;
		quitFlag = QuitLockFlag.getInstance();
		sql_queryUMSMsgs = Util.getDialect().getOutMsgSelectSQL_V3();
		// eui = new UUMUserInfo();
		eui = Util.getEuiObject();
		String formatString = PropertyUtil.getProperty("format",
				"/resources/msgFmtConf.props");
		if (formatString != null && formatString.equalsIgnoreCase("true")) {
			formatMessage = true;
			try {
				DocumentBuilderFactory dbf = DocumentBuilderFactory
						.newInstance();
				db = dbf.newDocumentBuilder();
				readMsgFormat();
			} catch (Exception e) {
				e.printStackTrace();
				Res.log(Res.ERROR, mediaInfo.getMediaId() + "读取消息格式失败!");
				stop = true;
			}
		}
		String flag = PropertyUtil.getProperty("checkFlag",
				"/resources/repeatMsgCheck.props");
		if (flag != null && flag.equalsIgnoreCase("true")) {
			repeatMsgCheckFlag = true;
		}
		String duration = PropertyUtil.getProperty("durationTime",
				"/resources/repeatMsgCheck.props");
		durationTime = Integer.parseInt(duration);
		Res.log(Res.INFO, "开始产生" + mediaInfo.getMediaName() + "渠道线程...");
	}

	/**
	 * 读取消息格式的配置文件
	 * 
	 * @throws Exception
	 */
	protected void readMsgFormat() throws Exception {
		msgFmts = new ArrayList();
		StringBuffer sb = new StringBuffer();
		URL url = getClass().getResource(getMsgFmtFile());
		BufferedReader reader = new BufferedReader(new FileReader(new File(url
				.toString().substring(5))));
		String s = null;
		while ((s = reader.readLine()) != null) {
			sb.append(s);
		}
		doc = db
				.parse(new ByteArrayInputStream(sb.toString().getBytes("utf-8")));
		doc.normalize();
		NodeList nl = doc.getElementsByTagName("parameter");
		for (int i = 0; i < nl.getLength(); i++) {
			Node my_node = nl.item(i);
			String parname = my_node.getAttributes().getNamedItem("name")
					.getNodeValue();
			msgFmts.add(parname);
		}
	}

	/**
	 * 格式化消息内容。很多情况下，消息会在各个渠道之间传递，如EMAIL渠道传给SMS渠道，EMAIL渠道包含更多的内容，如标题、附件等等，
	 * 但是SMS只需要内容，
	 * 这里是为了避免BasicMsg对象定义的消息属性需要扩展时使用。用户在BasicMsg满足需求的情况下，可以不必设定消息格式。
	 * 
	 * @param msg
	 */
	protected void formatUMSmsg(UMSMsg msg) {
		String content = msg.getBasicMsg().getMsgContent().getContent();
		try {
			doc = db.parse(new ByteArrayInputStream(content.toString()
					.getBytes("utf-8")));
			doc.normalize();
			if (doc.getElementsByTagName("MsgFmt").getLength() > 0) {// 为避免直接发送一个xml作为消息内容，通过MsgFmt过滤，可以杜绝
				NodeList nl = doc.getElementsByTagName("Item");
				StringBuffer contentBuff = new StringBuffer();
				for (int i = 0; i < nl.getLength(); i++) {
					Node my_node = nl.item(i);
					String parname = my_node.getAttributes().getNamedItem(
							"name").getNodeValue();
					// String value = my_node.getFirstChild().getNodeName();
					if (msgFmts.contains(parname)) {
						String message = null;
						if (my_node.getFirstChild() != null)
							message = my_node.getFirstChild().getNodeValue();
						contentBuff.append(message);
					}
				}
				msg.getBasicMsg().getMsgContent().setContent(
						contentBuff.toString());
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			// 未使用xml格式发送的消息都会抛这个异常
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public abstract String getMsgFmtFile();

	public void run() {
		while (!Thread.interrupted() && !quitFlag.getLockFlag() && !stop) {
			try {
				if (stop) {
					break;
				}
				if (getDataLockFlag().getLockFlag()) {
					Thread.sleep(100);
					continue;
				}
				UMSMsg[] msgs = new UMSMsg[16];
				// 获取待发消息
				int count = this.iniMsgs(msgs);
				if (count > 0) {
					if (groupMessage) {
						try {
							// 有可能是群发消息，通过重新组合消息，可以减少消息发送次数
							msgs = this.groupMsgByReceivers(msgs);
						} catch (OutOfMaxSequenceException e) {
							Res.log(Res.ERROR, "超过单次发送最大数");
							Res.logExceptionTrace(e);
						}
					}
					if (formatMessage) {
						for (int i = 0; i < msgs.length; i++) {
							if (msgs[i] != null)
								formatUMSmsg(msgs[i]);
						}
					}
					this.sendViaChannel(msgs);
				} else {
					if (count != MSG_ACTUAL_EXIST)
						getDataLockFlag().setLockFlag(true);
				}
			} catch (InterruptedException ex) {
				ex.printStackTrace();
				Res.log(Res.ERROR, "外发线程" + mediaInfo.getMediaId() + "被中断");
				Res.logExceptionTrace(ex);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Res.log(Res.ERROR, "消息发送过程中读取消息信息失败");
				Res.logExceptionTrace(e);
			}
		}
		Res.log(Res.INFO, mediaInfo.getMediaId() + mediaInfo.getMediaName()
				+ " 终止，线程退出!");
	}

	/**
	 * 外拨渠道发送消息过程，将之前取到的当前渠道的待发消息通过特定的方式发送给指定的个体。
	 * 
	 * @param msgs
	 *            待发消息集合
	 * @param msgCount
	 *            待发消息个数
	 */
	public abstract void sendViaChannel(UMSMsg[] msgs);

	/**
	 * 判断当前线程锁标记是否已锁状态
	 * 
	 * @return true已锁，false已解锁
	 */
	public abstract boolean isLocked();

	/**
	 * 设置当前线程锁标记状态
	 * 
	 * @param flag
	 *            true已锁，false已解锁
	 */
	public abstract void setLocked(boolean flag);

	/**
	 * Initiate the array of UMSMsg. At first, the array is empty but with size
	 * 16. At this step, UMS will get the top 16 UMSMsg(s) and set these
	 * object(s) into the array.
	 * 
	 * @param msgs
	 *            the first UMSMsg array to be initialized.
	 * @return message count.
	 * @throws IOException
	 */
	public int iniMsgs(UMSMsg[] msgs) throws IOException {
		Connection conn = null;
		if (!isLocked()) {
			//
			// 加锁
			this.setLocked(true);
			getMsgCounts = getMsgCounts + 1;
			try {
				conn = DriverManager.getConnection(DataBaseOp.getPoolName());
				conn.setAutoCommit(false);
				PreparedStatement msgPrep = conn
						.prepareStatement(this.sql_queryUMSMsgs);
				PreparedStatement attPrep = conn
						.prepareStatement("SELECT * FROM IN_OUT_ATTACHMENTS_V3 WHERE BATCHNO = ? AND SERIALNO = ? AND SEQUENCENO =?");
				msgPrep.setString(1, mediaInfo.getMediaId());
				ResultSet rs = msgPrep.executeQuery();
				ArrayList msgArr = dbUtil.queryOutMsgs(rs, attPrep);
				ArrayList filteredMsgs = this.filterRepeatedMsgs(msgArr);
				if (filteredMsgs != null) {
					for (int i = 0; i < filteredMsgs.size(); i++) {
						msgs[i] = (UMSMsg) filteredMsgs.get(i);
					}
				}
				msgPrep.close();
				attPrep.close();
				conn.commit();
				if (filteredMsgs == null)
					return 0;
				else if (allMsgFiltered) {
					return MSG_ACTUAL_EXIST;
				}
				return filteredMsgs.size();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				Res.log(Res.ERROR, "外发渠道：" + mediaInfo.getMediaId()
						+ "获取待发消息失败!");
				Res.logExceptionTrace(e);
			} finally {
				try {
					// 解锁
					this.setLocked(false);
					conn.close();
				} catch (SQLException e) {
					Res.log(Res.ERROR, "外发渠道：" + mediaInfo.getMediaId()
							+ "数据库连接关闭失败!" + e.getMessage());
					Res.logExceptionTrace(e);
				}
			}
		}
		return -1;
	}

	/**
	 * 过滤重发的消息
	 * 
	 * @param msgs
	 * @return
	 */
	private ArrayList filterRepeatedMsgs(ArrayList msgs) {
		if (msgs.size() == 0)
			return null;
		if (!repeatMsgCheckFlag) {
			return msgs;
		}
		allMsgFiltered = true;
		ArrayList filteredMsgs = new ArrayList();
		HashMap temp = new HashMap(30);
		UMSMsg msg = null;
		for (int i = 0; i < msgs.size(); i++) {
			if (repeatMsgCheckFlag) {
				msg = (UMSMsg) msgs.get(i);
				Connection conn = null;
				try {
					conn = DriverManager
							.getConnection(DataBaseOp.getPoolName());
					Date d = sdf.parse(msg.getBatchNO());
					long b2 = d.getTime() - durationTime * 1000;
					d = new Date(b2);
					PreparedStatement prep = conn
							.prepareStatement("select count(*) from out_ok_v3 where content=? and recvid=? and batchno>? and retCode=?");
					prep.setString(1, msg.getBasicMsg().getMsgContent()
							.getContent());
					prep.setString(2, msg.getBasicMsg().getReceivers()[0]
							.getParticipantID());
					prep.setString(3, sdf.format(d));
					prep.setString(4, "0000");
					ResultSet rs2 = prep.executeQuery();
					boolean flag = false;
					while (rs2.next()) {
						int count = rs2.getInt(1);
						if (count == 0) { // 如果还没有之前重发的记录，则在当前发送集合下也要找重发的记录
							if (temp.containsKey(msg.getBasicMsg()
									.getReceivers()[0].getParticipantID())) {
								if (temp.get(
										msg.getBasicMsg().getReceivers()[0]
												.getParticipantID()).equals(
										msg.getBasicMsg().getMsgContent()
												.getContent())) {
									flag = true;
								}
							}
						} else if (count > 0) {
							flag = true;
						}
					}
					prep.close();
					rs2.close();
					if (flag) {
						Res.log(Res.WARN, "重复消息，UMS阻拦.接收者："
								+ msg.getBasicMsg().getReceivers()[0]
										.getParticipantID()
								+ ", 内容："
								+ msg.getBasicMsg().getMsgContent()
										.getContent());
						OpData(-9922, msg);
					} else {
						allMsgFiltered = false;
						filteredMsgs.add(msg);
						temp.put(msg.getBasicMsg().getReceivers()[0]
								.getParticipantID(), msg.getBasicMsg()
								.getMsgContent().getContent());
					}
				} catch (Exception e) {
					Res.logExceptionTrace(e);
					try {
						OpData(-9922, msg);
					} catch (IOException e1) {
						Res.logExceptionTrace(e1);
					}
				} finally {

					if (conn != null) {
						try {
							conn.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		temp.clear();
		temp = null;
		return filteredMsgs;
	}

	/**
	 * transmit the <b>UMSMsg</b> to the right position, select a record from
	 * out_ready_v3 table and put it into out_ok_v3 table, if it is a reply
	 * needed message, it will be put into out_reply_v3 table.
	 * 
	 * @param result
	 *            the message sending result, if succeed the result is 0, else
	 *            there will be some policy to make.
	 * @param msg
	 *            this message is the returned message from service
	 *            provider(such as China Mobile, China Unicom).
	 * @throws IOException
	 */
	protected void OpData(int result, UMSMsg msg) throws IOException {
		String ret_code = "";
		String err_msg = "";
		if (result == 0) {
			ret_code = "0000";
			err_msg = "发送成功";
		} else if (result == 40) {
			ret_code = "0040";
			err_msg = "不合法参数";
		} else if (result == 42) {
			ret_code = "0042";
			err_msg = "无此权限";
		} else if (result == 11) {
			ret_code = "0011";
			err_msg = "用户已停机";
		} else if (result == 13) {
			ret_code = "0013";
			err_msg = "用户暂停使用或号码不存在";
		} else if (result == 27) {
			ret_code = "0027";
			err_msg = "用户已关机";
		} else if (result == 50) {
			ret_code = "0050";
			err_msg = "用户手机无存储空间";
		} else if (result == 93) {
			ret_code = "0093";
			err_msg = "计费手机欠费";
		} else if (result == 172) {
			ret_code = "0172";
			err_msg = "网络无响应";
		} else if (result == 4320) {
			ret_code = "4320";
			err_msg = "号码不是数字";
		} else if (result == 3300) {// 3000量级的为email返回
			ret_code = "3300";
			err_msg = "Email地址有误";
		} else if (result == 3301) {
			ret_code = "3301";
			err_msg = "Email消息生成过程产生错误";
		} else if (result == 4322) {
			ret_code = "4322";
			err_msg = "短消息应用流水号不是数字或超过4位";
		} else if (result == 5891) {
			ret_code = "5891";
			err_msg = "LCS消息发送失败.";
		} else if (result == -9922) {
			ret_code = "9922";
			err_msg = "重复消息发送，UMS阻拦";
		} else {
			ret_code = "4321";
			err_msg = "其它错误,error code:"+result;
		}
		msg.setReturnCode(ret_code);
		// if (msg.getErrMsg() != null && !msg.getErrMsg().equals(""))
		msg.setErrMsg(err_msg);
		if (msg.getRep() == -1 || msg.getRep() == 0) {
			msg.setRep(1);
		} else {
			msg.setRep(msg.getRep() + 1);
		}
		if (msg.getDoCount() == -1 || msg.getDoCount() == 0) {
			msg.setDoCount(1);
		} else {
			msg.setDoCount(msg.getDoCount() + 1);
		}
		Connection conn = null;
		try {
			String deleteOutReadySQL = DBUtil_V3
					.createDeleteMsgSQL(DBUtil_V3.MESSAGE_OUT_READY_V3);
			String insertOutOKSQl = DBUtil_V3
					.createOutMsgInsertSQL(DBUtil_V3.MESSAGE_OUT_OK_V3);
			conn = DriverManager.getConnection(DataBaseOp.getPoolName());
			conn.setAutoCommit(false);
			if (result == 0 || result == -9922) {
				// 发送成功
				// delete message from out_ready_v3
				PreparedStatement msgDeletePrep = conn
						.prepareStatement(deleteOutReadySQL);
				// meanwhile, insert this message into out_ok_v3
				PreparedStatement msgPrep = conn
						.prepareStatement(insertOutOKSQl);
				MediaInfo media = (MediaInfo) ChannelManager
						.getOutMediaInfoHash().get(
								msg.getBasicMsg().getMediaID());
				// 如果是发给手机的，则需计费；如果其它渠道也需要计费，则可以在此放入类似判断条件
				switch (media.getMediaStyle()) {
				case Participant.PARTICIPANT_ID_MOBILE:
					break;// 因考虑消息拆分，在这里不好处理，因此费用处理放在拆分完发送后处理
				default:
					break;// 暂时不处理
				}
				dbUtil.executeOutMsgInsertStatement(msgPrep, null, msg,
						DBUtil_V3.MESSAGE_OUT_OK_V3, conn);
				dbUtil.excuteMsgDeleteStatement(msg.getBatchNO(), msg
						.getSerialNO(), msg.getSequenceNO(), msgDeletePrep);
				if (result == 0
						&& msg.getBasicMsg().getNeedReply() == BasicMsg.BASICMSG_NEEDREPLY_YES) {
					this.processReplyNeededMsg(msg, conn);
				}
				msgPrep.close();
				msgDeletePrep.close();
			} else {
				// message sending with error
				ChannelPolicy channelPolicy = ChannelPolicy.getInstance();
				PreparedStatement failedPrep = conn.prepareStatement(DBUtil_V3
						.createFailedMsgUpdateSQL());
				// next channel
				if (msg.getRep() >= 3) {
					msg = channelPolicy.implChannelPolicy(msg, DBUtil_V3
							.getUMSMainChannelPolicy(),
							Policy.POLICY_NEXT_PRIORITY_YES);
					if (msg.getBasicMsg().getMediaID().equals("")) {
						// all channels has been used to send and failed
						msg.setStatusFlag(UMSMsg.UMSMSG_STATUS_INVALID);
					}
					msg.setRep(0);
				}
				StringBuffer sb = new StringBuffer();
				sb.append("渠道").append(mediaInfo.getMediaId()).append(
						"未成功发送的消息.    ");
				sb.append("原因：").append(msg.getErrMsg());
				sb.append("      接收者：");
				for (int i = 0; i < msg.getBasicMsg().getReceivers().length; i++) {
					sb.append(msg.getBasicMsg().getReceivers()[i] == null ? "无"
							: msg.getBasicMsg().getReceivers()[i]
									.getParticipantID());
					if (i != msg.getBasicMsg().getReceivers().length - 1)
						sb.append(",");
					else
						sb.append(".    ");
				}
				sb.append("内容：");
				sb.append(msg.getBasicMsg().getMsgContent().getContent())
						.append(".");
				Res.log(Res.INFO, sb.toString());
				dbUtil.executeFailedMsgUpdate(failedPrep, msg);
				failedPrep.close();
			}
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			Res.log(Res.ERROR, "渠道" + mediaInfo.getMediaId()
					+ "OpData()数据库操作失败:" + e.getMessage());
			Res.logExceptionTrace(e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				Res.log(Res.ERROR, "渠道" + mediaInfo.getMediaId()
						+ "OpData数据库连接关闭失败:" + e.getMessage());
				Res.logExceptionTrace(e);
			}
		}
	}

	/**
	 * if it is a reply needed message, it will be stored as a copy in
	 * out_reply_v3 after this message is sent correctly.
	 * 
	 * @param msg
	 *            message which has been sent
	 * @param conn
	 *            sql connecton
	 * @throws SQLException
	 * @throws IOException
	 */
	private void processReplyNeededMsg(UMSMsg msg, Connection conn)
			throws SQLException, IOException {
		String insertReplyMsgSQL = DBUtil_V3
				.createOutMsgInsertSQL(DBUtil_V3.MESSAGE_OUT_REPLY_V3);
		PreparedStatement replyStatement = conn
				.prepareStatement(insertReplyMsgSQL);
		dbUtil.executeOutMsgInsertStatement(replyStatement, null, msg,
				DBUtil_V3.MESSAGE_OUT_REPLY_V3, conn);
		replyStatement.close();
	}

	/**
	 * 数据库表中查出来的结果是每个接收者一条记录，然而对于群发消息可能是多个接收者一条记录，
	 * 因此，需要重新整合，规则是按照消息的唯一性：BatchNO,SerialNO,SequenceNO
	 * 
	 * @param msgs
	 *            待发消息集合
	 * @return 经过整合后的消息集合
	 * @throws OutOfMaxSequenceException
	 *             超过单次发送最大值异常
	 */
	protected UMSMsg[] groupMsgByReceivers(UMSMsg[] msgs)
			throws OutOfMaxSequenceException {
		UMSMsg[] groupedMsgs = new UMSMsg[16];
		int groupNO = 0;
		for (int n = 0; n < msgs.length; n++) {
			if (msgs[n] != null) {
				// 如果之前
				if (msgs[n].getReturnCode() != null) {
					if (!msgs[n].getReturnCode().trim().equals("0000"))
						if (!msgs[n].getReturnCode().trim().equals(""))
							return msgs;
				}
				ArrayList receiverArr = new ArrayList();
				ArrayList sequenceArr = new ArrayList();
				if (receiverArr
						.contains(msgs[n].getBasicMsg().getReceivers()[0])) {
					continue;
				}
				receiverArr.add(msgs[n].getBasicMsg().getReceivers()[0]);
				// 未重组前，每个UMSMsg都只有一个sequenceNO元素
				sequenceArr.add(new Integer(msgs[n].getSequenceNO()[0]));
				for (int k = n + 1; k < msgs.length; k++) {
					if (msgs[k] != null) {
						// 同一个源UMSMsg，要还原成发送过来时候的接收者集合，但大小不能超过16
						if (msgs[n].getBatchNO().equals(msgs[k].getBatchNO())
								&& msgs[n].getSerialNO() == msgs[k]
										.getSerialNO()) {
							receiverArr.add(msgs[k].getBasicMsg()
									.getReceivers()[0]);
							sequenceArr.add(new Integer(
									msgs[k].getSequenceNO()[0]));
							msgs[k] = null;
						}
					}
				}
				Participant[] receivers = new Participant[receiverArr.size()];
				int[] sequenceNO = new int[receiverArr.size()];
				for (int i = 0; i < receiverArr.size(); i++) {
					receivers[i] = (Participant) receiverArr.get(i);
					sequenceNO[i] = ((Integer) sequenceArr.get(i)).intValue();
				}
				groupedMsgs[groupNO] = msgs[n];
				groupedMsgs[groupNO].getBasicMsg().setReceivers(receivers);
				groupedMsgs[groupNO].setSequenceNO(sequenceNO);
				groupNO++;
			}
		}
		return groupedMsgs;
	}

	/**
	 * @return the mediaInfo
	 */
	public MediaInfo getMediaInfo() {
		return mediaInfo;
	}

	/**
	 * @param mediaInfo
	 *            the mediaInfo to set
	 */
	public void setMediaInfo(MediaInfo mediaInfo) {
		this.mediaInfo = mediaInfo;
	}

	/**
	 * start the current thread
	 */
	public void start() {
		if (!stop) {
			runner = new Thread(this, mediaInfo.getMediaName());
			runner.start();
			Res.log(Res.INFO, mediaInfo.getMediaName() + "外拨渠道线程启动！在"
					+ Util.getCurrentTimeStr("yyyyMMddHHmmss"));
		} else {// 渠道开启失败后重启
			if (runner != null) {
				stop = false;
				runner.start();
			}
		}
		startOnce = true;
	}

	public boolean isStartOnce() {
		return startOnce;
	}

	public void stop() {
		stop = true;
		Res.log(Res.INFO, mediaInfo.getMediaName() + "外拨渠道线程退出服务！");
	}

	/**
	 * get the current thread state
	 * 
	 * @return boolean true: the thread is running; false: the thread is
	 *         stopped;
	 */
	public boolean getThreadState() {
		return !stop;
	}

	public LockFlag getDataLockFlag() {
		// TODO Auto-generated method stub
		return null;
	}

}
