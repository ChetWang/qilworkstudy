/*
 * Created on 2007-2-28
 *
 * TODO To change the template for this generated file go to
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.nci.ums.channel.myproxy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.huawei.insa2.comm.PException;
import com.huawei.insa2.comm.cmpp.message.CMPPDeliverMessage;
import com.huawei.insa2.comm.cmpp.message.CMPPMessage;
import com.huawei.insa2.comm.cmpp.message.CMPPSubmitMessage;
import com.huawei.insa2.comm.cmpp.message.CMPPSubmitRepMessage;
import com.huawei.insa2.util.Args;
import com.huawei.insa2.util.Cfg;
import com.nci.ums.channel.channelinfo.MediaInfo;
import com.nci.ums.channel.outchannel.MsgInfo;
import com.nci.ums.exception.UMSConnectionException;
import com.nci.ums.periphery.parser.MobileCheck;
import com.nci.ums.util.FeeUtil;
import com.nci.ums.util.Res;
import com.nci.ums.util.Util;
import com.nci.ums.v3.fee.FeeBean;
import com.nci.ums.v3.message.UMSMsg;
import com.nci.ums.v3.message.basic.BasicMsg;

public class SendMessage {

	private Args args = null;
	private Map argsmap = null;
	static MySMProxy myProxy = null;
	static Map senderMediaMap = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
	private static SendMessage sender;
	private String umsCenter_cm;

	private String enterpriseCode;

	private int totalReconnCounts = 0;

	/**
	 * 普通短信的发送方式
	 */
	public static final int SUBMITMESSAGE_TP_UDHI_ONE_NORMAL_MESSAGE = 0;
	/**
	 * 以超长短信形式发送
	 */
	public static final int SUBMITMESSAGE_TP_UDHI_LONGTEXT_MESSAGE = 1;

	private static final int UCS2 = 8;

	public static int uid = 0;

	private MediaInfo mediaInfo;

	private byte[] sendLock = new byte[0];

	private SendMessage(MediaInfo mediaInfo) throws Exception {
		System.out.println("file encoding:"
				+ System.getProperty("file.encoding"));
		this.args = new Cfg(getClass().getResource("/resources/hw.xml")
				.toString(), false).getArgs("ismg");
		// this.argsmap = getConfigMap();
		this.mediaInfo = mediaInfo;
		umsCenter_cm = MobileCheck.getChinaMobileMap().get("UMSCenter_cm")
				.toString();
		enterpriseCode = MobileCheck.getChinaMobileMap().get(
				"EnterpriseCode_CM").toString();
		senderMediaMap = new ConcurrentHashMap();
		senderMediaMap.put(mediaInfo.getMediaId(), mediaInfo);
		myProxy = new MySMProxy(this, args);
		// myProxy =
		// (MySMProxy)SMProxyFactory.getInstance().createMSProxy();
		Res.log(Res.INFO, "渠道" + mediaInfo.getMediaId() + "初始化SMS网关成功！");
	}

	public static int getUID() {

		uid++;
		if (uid > 16)
			uid = 1;
		return uid;
	}

	public static void stop() {
		if (senderMediaMap != null)
			senderMediaMap.clear();
		senderMediaMap = null;
		sender = null;
		if (myProxy != null) {
			myProxy.close();
		}
	}

	public synchronized static SendMessage getInstance(MediaInfo media) {
		if (sender == null) {
			try {
				sender = new SendMessage(media);
			} catch (Exception ex) {
				Res.log(Res.ERROR, "UMS初始化SMS网关错误");
				Res.logExceptionTrace(ex);
				sender = null;
			}
		} else {
			if (senderMediaMap.get(media.getMediaId()) == null) {
				senderMediaMap.put(media.getMediaId(), media);
			}
		}
		return sender;
	}

	public void close() {
		myProxy.close();
	}

	public MySMProxy getMyProxy() {
		return myProxy;
	}

	public void ProcessRecvDeliverMsg(CMPPDeliverMessage msg) {

		if (msg == null) {
			System.out.println("DeliverMessage is null.");
			return;
		}

		if (msg.getRegisteredDeliver() == 1) {
			System.out.println("It is a report.");
			// System.out.println("ID: "+ new String(msg.getMsgId()).trim());
			printByte(msg.getMsgId());
			// System.out.println(msg.getStatusMsgId());
			int n = msg.getSequenceId();

			int n1 = n / (256 * 256 * 256);
			n = n % (256 * 256 * 256);
			int n2 = n / (256 * 256);
			n = n % (256 * 256);
			int n3 = n / (256);
			int n4 = n % (256);

			System.out.println("trace :" + n1 + "   " + n2 + "  " + n3 + "  "
					+ n4);

			int m = msg.getSMSCSequence();

			System.out.println("Stat: " + msg.getStat());
		} else {
			System.out.println("It is a deliver.");
			// printByte(msg.getMsgId());
			try {
				// System.out.println("Content: " +
				// parse(msg.getMsgContent(),msg.getMsgFmt()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void printByte(byte[] b) {

		if (b == null) {
			System.out.println("byte is null");
		}
		if (b == null) {
			return;
		}
		for (int i = 0; i < b.length; i++) {
			System.out.println(i + "  " + b[i]);
		}
		System.out.println();
	}

	private String parse(byte[] content, int msgFormat) {
		try {
			switch (msgFormat) {
			case 0:
				// ascii
				// return new String((new String(content,
				// "ASCII")).getBytes("GBK", "GBK"));
			case 8:
				// return new String((new String(content,
				// "iso-10646-ucs-2").getBytes("GBK", "GBK")));
			case 15:
				// return new String((new String(content,
				// "GBK").getBytes("GBK")));
				// return new String((new String(content, "GBK")));
				return new String(content, 0, content.length, "UnicodeBig");
			case 3:
			case 4:
			default:
				return null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public int getSendResult(byte[] bResult) {
		if (bResult.length < 13) {
			System.out.println("消息长度不对");
		}
		return bResult[12];
	}

	/**
	 * 程序入口。
	 */
	public int sendMsg(byte[][] dst_addr, int sm_len, byte[] short_msg,
			byte[][] src_addr, byte count, MsgInfo msgInfo)
			throws UMSConnectionException {

		int nResult = -1;

		try {

			String[] tel = new String[dst_addr.length - 1];

			for (int i = 0; i < dst_addr.length - 1; i++) {
				tel[i] = new String(dst_addr[i], 0, 11);
			}

			CMPPSubmitMessage submitMsg = new CMPPSubmitMessage(1, 1, 0, 0,
					"9999", 0, "", 0, 0, 15, enterpriseCode, "02", "000010",
					null, null, new String(src_addr[0], 0,
							src_addr[0].length - 1), tel, short_msg, "");
			CMPPMessage submitRepMsg = myProxy.send(submitMsg);

			switch (nResult = getSendResult(submitRepMsg.getBytes())) {
			case 0:
				// 成功
				System.out.println("------------完成发送一条消息---------");

				break;
			default:
				System.out.println("------------没有完成完成发送一条消息原因--------- "
						+ nResult);
				// 不成功处理 ， 分为多种，具体可以看cmpp2的协议
			}
		} catch (Exception e) {

			System.out.println("发生异常:" + e);
			e.printStackTrace();
		}

		return nResult;
	}

	/**
	 * send the message. since UMS3.0
	 * 
	 * @param msg
	 *            the message which will be sent
	 * @return result of message sending
	 */
	public int sendMsg(UMSMsg msg) {
		// CMPPSubmitMessage submitMsg =
		// new CMPPSubmitMessage(
		// int pk_Total, //
		// int pk_Number, // pk_Number 相同msg_Id的消息序号
		// int registered_Delivery, // registered_Delivery 是否要求返回状态报告
		// int msg_Level, // msg_Level 信息级别
		// String service_Id, // service_Id 业务类型
		// int fee_UserType, // fee_UserType 计费用户类型字段
		// String fee_Terminal_Id, // fee_Terminal_Id 被计费用户的号码
		// int tp_Pid, // tp_Pid GSM协议类型
		// int tp_Udhi, // tp_Udhi GSM协议类型
		// int msg_Fmt, // msg_Fmt 消息格式
		// String msg_Src, // msg_Src 消息内容来源
		// String fee_Type, // fee_Type 资费类别
		// String fee_Code, // fee_Code 资费代码(以分为单位)
		// Date valid_Time, // valid_Time 存活有效期
		// Date at_Time, // at_Time 定时发送时间
		// String src_Terminal_Id, // src_Terminal_Id 源号码
		// String[] dest_Terminal_Id, // dest_Terminal_Id 接收短信的MSISDN号码
		// byte[] msg_Content, // msg_Content 消息内容
		// String reserve // LinkID 点播业务使用的LinkID
		// ) ;

		int nResult = -1;
		CMPPSubmitMessage submitMsg;
		Date invalidDate = null;
		Date sendDate = null;
		try {
			invalidDate = sdf.parse(msg.getBasicMsg().getInvalidDate()
					+ msg.getBasicMsg().getInvalidTime());
		} catch (ParseException e) {
			// e.printStackTrace();
			// Res.log(Res.ERROR, "CMPP短消息日期属性转换出错!" + e.getMessage());
		}
		try {
			if (msg.getBasicMsg().getTimeSetFlag() == BasicMsg.BASICMSG_SENDTIME_CUSTOM) {
				sendDate = sdf.parse(msg.getBasicMsg().getSetSendDate()
						+ msg.getBasicMsg().getSetSendTime());
			}
		} catch (ParseException e) {
			//			
		}
		// 短消息拆分
		List msgList = this.cutUMSMsgByLength(msg);

		FeeBean feeBean = new FeeBean();
		boolean feeflag = false;
		if (FeeUtil.getFeeMap().get(msg.getBasicMsg().getFeeServiceNO()) != null) {
			feeBean = (FeeBean) FeeUtil.getFeeMap().get(
					msg.getBasicMsg().getFeeServiceNO());
			feeflag = true;
		}
		msg.setFeeInfo(feeBean);
		// 根据应用服务命名协议规则，这里的服务号是四位，应用号是服务号前两位;如果个人发生，这里将拆分工号的最后四位，但考虑与应用代号可能重叠，后面将会进一步处理掉
		String sendService = getLastFourNumber(msg.getBasicMsg().getSender()
				.getParticipantID());
		if (sendService == null) {
			return 4320;
		}
		String[] dest_Terminal_Id = new String[msg.getBasicMsg().getReceivers().length];
		for (int p = 0; p < msg.getBasicMsg().getReceivers().length; p++) {
			dest_Terminal_Id[p] = msg.getBasicMsg().getReceivers()[p]
					.getParticipantID();
			if (dest_Terminal_Id[p] == null || dest_Terminal_Id[p].equals("")) {
				msg.getBasicMsg().getReceivers()[p]
						.setParticipantID("invalid user mobile");
				dest_Terminal_Id[p] = "";
			}
		}
		// web发送时的特殊序列号，不是数字的，这里要取消掉
		// boolean webFlag = false;
		if (msg.getBasicMsg().getAppSerialNO().equalsIgnoreCase("appSerialNO")) {
			msg.getBasicMsg().setAppSerialNO("");
			// webFlag = true;
			// msg.getBasicMsg().getSender().setParticipantID("");
		} else {
			if (msg.getBasicMsg().getAppSerialNO().length() > 4)
				return 4322;
			try {
				if (msg.getBasicMsg().getAppSerialNO().length() > 0)
					Float.parseFloat(msg.getBasicMsg().getAppSerialNO());
			} catch (Exception e) {
				return 4322;
			}
		}
		// String lastID = webFlag ? "" : getLastFourNumber(msg.getBasicMsg() //
		// 这四位其实是约定的应用服务ServiceID
		// .getSender().getParticipantID());
		// String src_Terminal_Id = umsCenter_cm + lastID
		// + msg.getBasicMsg().getAppSerialNO();//
		// 再加上特定的应用序列号,如果是web管理平台应用发送，则为空
		String src_Terminal_Id = umsCenter_cm
				+ msg.getBasicMsg().getServiceID()
				+ msg.getBasicMsg().getAppSerialNO();// 再加上特定的应用序列号,如果是web管理平台应用发送，则为空
		int udhi_unique = SendMessage.getUID();
		for (int i = 0; i < msgList.size(); i++) {
			try {
				byte[] tp_Udhi = new byte[6];// tpUdhi 6位协议
				tp_Udhi[0] = 0x05;
				tp_Udhi[1] = 0x00;
				tp_Udhi[2] = 0x03;
				tp_Udhi[3] = (byte) 16;
				tp_Udhi[4] = (byte) msgList.size();
				tp_Udhi[5] = (byte) (i + 1);
				int deleverdStatus = msg.getBasicMsg().getAck() == BasicMsg.UMSMsg_ACK?1:0;
				// UMSMsg cutMsg = (UMSMsg) msgList.get(i);
				if (!feeflag) {
					// submitMsg = new CMPPSubmitMessage(msgList.size(), i + 1,
					// msg.getBasicMsg().getAck(), msg.getBasicMsg()
					// .getPriority(), "9999", 0, "", 0,
					// SUBMITMESSAGE_TP_UDHI_ONE_NORMAL_MESSAGE, msg
					// .getBasicMsg().getContentMode(), "911337",
					// "02", "000010", invalidDate, sendDate,
					// src_Terminal_Id, dest_Terminal_Id, Util.getByte(
					// (String) msgList.get(i), msg.getBasicMsg()
					// .getContentMode()), "");
					submitMsg = new CMPPSubmitMessage(msgList.size(), i + 1,
							deleverdStatus, msg.getBasicMsg()
									.getPriority(), "9999", 0, "", 0,
							SUBMITMESSAGE_TP_UDHI_LONGTEXT_MESSAGE, UCS2,
							enterpriseCode, "02", "000010", invalidDate,
							sendDate, src_Terminal_Id, dest_Terminal_Id, Util
									.getByteFrom_tp_Ddhi((String) msgList
											.get(i), UCS2, tp_Udhi), "");

				} else {
					int fee_user_type = feeBean.getFeeType();
					String fee_terminal_id = feeBean.getFee_terminal_Id();
					String fee_code = parseFee(feeBean.getFee());
					submitMsg = new CMPPSubmitMessage(msgList.size(), i + 1,
							msg.getBasicMsg().getAck(), msg.getBasicMsg()
									.getPriority(), "9999", fee_user_type,
							fee_terminal_id, 0,
							SUBMITMESSAGE_TP_UDHI_LONGTEXT_MESSAGE, UCS2,
							enterpriseCode, "02", fee_code, invalidDate,
							sendDate, src_Terminal_Id, dest_Terminal_Id, Util
									.getByte((String) msgList.get(i), UCS2), "");

				}

				if (myProxy == null) {
					return -1;
				}
				CMPPSubmitRepMessage submitRepMsg = (CMPPSubmitRepMessage)myProxy.send(submitMsg);
				int messageID = Res.bytesToInt(submitRepMsg.getMsgId());
				System.out.println("messageid:"+messageID);
				msg.setMsgID(String.valueOf(messageID));
				byte[] repMsg = submitRepMsg.getBytes();
				nResult = this.getSendResult(repMsg);
				switch (nResult) {
				case 0:
					// success
					// System.out.println("------------完成发送一条消息---------");
					break;
				default:
					System.out.println("------------没有完成完成发送一条消息原因--------- "
							+ nResult);
				}
				// try {
				// if (msgList.size() > 1)
				// Thread.sleep(3000);
				// } catch (InterruptedException e) {
				// }
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				Res.log(Res.ERROR, "CMPP短消息参数有误：" + e.getMessage());
				Res.logExceptionTrace(e);
			} catch (Exception e) {
				Res.log(Res.ERROR, "CMPP短消息发送出错!" + e.getMessage());
				Res.logExceptionTrace(e);
				// if (e.getMessage().indexOf("登录不成功") >= 0
				// || e.getMessage().indexOf("连接尚未初始化") >= 0) {
				if (e instanceof PException) {
					try {
						Res.log(Res.INFO, "开始重登录消息网关...");
						myProxy.close();
					} catch (Exception ex) {
					}
					myProxy = null;
					try {
						myProxy = new MySMProxy(this, args);

						totalReconnCounts = 0;
						Res.log(Res.INFO, "消息网关登录成功！");
					} catch (Exception ex) {
						Res.log(Res.ERROR, "消息网关重登录失败！" + ex.getMessage());
						try {
							Thread.sleep(10 * 1000);
							Res.log(Res.INFO, "等待重登录消息网关...");
						} catch (InterruptedException e1) {
						}
						// totalReconnCounts++;
						// if (totalReconnCounts <= 10) {
						// try {
						// Thread.sleep(10 * 1000);
						// Res.log(Res.INFO, "等待重登录消息网关...");
						// } catch (InterruptedException e1) {
						// }
						// } else {
						// // 渠道退出
						// String err = "消息网关无法登陆，" + mediaInfo.getMediaId()
						// + "渠道停止运行.";
						// Res.log(Res.INFO, err);
						// try {
						// MonitorServer_V3.getInstance()
						// .sendWarningToAdmin("UMS告警", err);
						// } catch (ApplicationException e1) {
						// Res.log(Res.ERROR, "UMS发送告警信息出错");
						// Res.logExceptionTrace(e);
						// }
						// totalReconnCounts = 0;
						// mediaInfo.getChannelObject().stop();
						// }
					}
				}

			}
		}
		// 计费信息要重新算，以拆分后为准
		if (feeflag) {
			feeBean.setFee(feeBean.getFee() * msgList.size());
			msg.setFeeInfo(feeBean);
		}
		return nResult;
	}

	/**
	 * UMS对超长字符短信的处理
	 * 
	 * @param msg
	 * @return
	 */
	private List cutUMSMsgByLength(UMSMsg msg) {
		// int reservedLength = ReservedString.getSMSReservedString_cm(
		// msg.getBasicMsg().getContentMode()).length();
		int reservedLength = 0;// 将签名的长度设为0是因为在发送的时候现在不用考虑签名了
		int maxLength = 70;
		switch (msg.getBasicMsg().getContentMode()) {
		case BasicMsg.BASICMSG_CONTENTE_MODE_8859_1:
			maxLength = 160;
			break;
		case BasicMsg.BASICMSG_CONTENTE_MODE_GBK:
			maxLength = 67;
		}
		ArrayList arr = new ArrayList();
		String content = msg.getBasicMsg().getMsgContent().getContent();
		// /////////////////////////////////////////////
		// if (content.length() <= maxLength - reservedLength) {
		// arr.add(content);
		// } else {
		// int appendLength = 0; //
		// appendLength是指名多条短信下的具体哪条的信息长度，如（1-3）表示共有三条，当前是第一条。
		// // 先假设总条数为个位数，最小为2，最大为9，新加(1-3）之类的文字，长度为5
		// if (this.getCutCounts(content.length(), maxLength, reservedLength,
		// 5) <= 9) {
		// appendLength = 5;
		//
		// } else if (this.getCutCounts(content.length(), maxLength,
		// reservedLength, 7) <= 99
		// && this.getCutCounts(content.length(), maxLength,
		// reservedLength, 7) > 9) {
		// // 这种情况是短信拆分的总条数已经超过9条，并还不到100条，新加的文字类似(1-20)、(12-33)，为简化处理，全部按7个字符处理
		// appendLength = 7;
		// } else if (this.getCutCounts(content.length(), maxLength,
		// reservedLength, 7) <= 999
		// && this.getCutCounts(content.length(), maxLength,
		// reservedLength, 7) > 99) {
		// // 这种情况是短信拆分的总条数已经超过9条，并还不到100条，新加的文字类似(1-20)、(12-33)，为简化处理，全部按7个字符处理
		// appendLength = 9;
		// }
		// int counts = this.getCutCounts(content.length(), maxLength,
		// reservedLength, appendLength);
		// for (int i = 1; i <= counts - 1; i++) {
		// StringBuffer subContentBuffer = new StringBuffer();
		// subContentBuffer.append("[").append(counts).append("-").append(i).append("]");
		// subContentBuffer.append(content.substring(0, maxLength
		// - reservedLength - appendLength));
		// // StringBuffer subContentBuffer = new
		// StringBuffer(content.substring(0, maxLength
		// // - reservedLength -
		// appendLength)).append("[").append(i).append("-").append(counts).append("]");
		// content = content.substring(maxLength - reservedLength
		// - appendLength);
		// arr.add(subContentBuffer.toString());
		// }
		// // arr.add(content + "[" + String.valueOf(counts) + "-"
		// // + String.valueOf(counts) + "]");
		// arr.add(new
		// StringBuffer("[").append(counts).append("-").append(counts).append("]").append(content).toString());
		// }
		// /////////////////////////////////////////
		if (content.length() <= maxLength - reservedLength) {
			arr.add(content);
		} else {
			while (content.length() > maxLength) {
				String subContent = content.substring(0, maxLength);
				content = content.substring(maxLength);
				arr.add(subContent);
			}
			arr.add(content);
		}
		return arr;
	}

	private int getCutCounts(int contentLength, int maxLength,
			int reservedLength, int appendLength) {
		int counts = 0;
		if (contentLength % (maxLength - reservedLength - appendLength) == 0)
			counts = contentLength
					/ (maxLength - reservedLength - appendLength);
		else {
			counts = (int) Math.floor(contentLength
					/ (maxLength - reservedLength - appendLength)) + 1;
		}
		return counts;
	}

	private String parseFee(int fee) {
		String fee_code_original = "000000";
		String feeString = String.valueOf(fee);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < fee_code_original.length() - feeString.length(); i++) {
			sb.append("0");
		}
		sb.append(feeString);
		return sb.toString();
	}

	private String getLastFourNumber(String src_terminal) {
		String s = "";
		if (src_terminal.length() <= 4) {
			s = src_terminal;
		} else {
			s = src_terminal.substring(src_terminal.length() - 4);
		}
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			if (!s.equals("")) {
				Res.log(Res.INFO, "短消息发送者的ID不是数字:" + src_terminal);
				return null;
			}
		}
		return s;
	}

	/**
	 * 获取中国移动sp号码
	 * 
	 * @return sp号码
	 */
	public String getUmsCenter_cm() {
		return umsCenter_cm;
	}

	public void setUmsCenter_cm(String umsCenter_cm) {
		this.umsCenter_cm = umsCenter_cm;
	}

	// private static Map getConfigMap() {
	// Document doc = null;
	// InputStream is = new ServletTemp()
	// .getInputStreamFromFile("/resources/hw.xml");
	// DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	// DocumentBuilder bulider = null;
	// try {
	// bulider = factory.newDocumentBuilder();
	// } catch (ParserConfigurationException e) {
	//
	// e.printStackTrace();
	// }
	//
	// try {
	// doc = bulider.parse(is);
	// } catch (SAXException e) {
	//
	// e.printStackTrace();
	// } catch (IOException e) {
	//
	// e.printStackTrace();
	// }
	// doc.normalize();
	// Map args = new HashMap();
	// Element ismgEle = (Element) doc.getDocumentElement()
	// .getElementsByTagName("ismg").item(0);
	// NodeList params = ismgEle.getChildNodes();
	// for (int i = 0; i < params.getLength(); i++) {
	// Node node = params.item(i);
	// if (node instanceof Element)
	// args.put(node.getNodeName(), node.getTextContent());
	// }
	//
	// return args;
	// }
}
