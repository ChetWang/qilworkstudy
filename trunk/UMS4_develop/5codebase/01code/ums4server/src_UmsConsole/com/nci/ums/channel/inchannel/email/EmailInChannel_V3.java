package com.nci.ums.channel.inchannel.email;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessageRemovedException;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;

import com.nci.ums.channel.channelinfo.ChannelMode;
import com.nci.ums.channel.channelinfo.MediaInfo;
import com.nci.ums.channel.inchannel.InChannel_V3;
import com.nci.ums.util.EmailMsgPlus;
import com.nci.ums.util.PropertyUtil;
import com.nci.ums.util.Res;
import com.nci.ums.util.SerialNO;
import com.nci.ums.util.Util;
import com.nci.ums.v3.message.UMSMsg;
import com.nci.ums.v3.message.basic.BasicMsg;
import com.nci.ums.v3.message.basic.MsgAttachment;
import com.nci.ums.v3.message.basic.MsgContent;
import com.nci.ums.v3.message.basic.Participant;
import com.nci.ums.v3.service.ServiceInfo;
import com.sun.mail.pop3.POP3Folder;
import com.thoughtworks.xstream.XStream;

public class EmailInChannel_V3 extends InChannel_V3 {

	/**
	 * 统一的应用服务email密码，为了方便起见，都统一为一个密码
	 */
	public String uniServiceMailPsw;
	// Email 服务器的Store和Folder属性
	private Store store = null;
	private Folder folder = null;
	private String emailAddr;
	// private String appService = "";
	private ServiceInfo serviceInfo;
	private XStream xstream;
	private boolean isLastTimeHasMsg = false;
	private Map lastTimeMessageIDs = new ConcurrentHashMap(22);// 22*0.75=16
	private byte[] msgObj = new byte[0];
	/**
	 * 链接email服务器的次数
	 */
	private int connectTimes = 0;

	public EmailInChannel_V3(MediaInfo mediaInfo) {
		super(mediaInfo, ChannelMode.CHANNEL_MODE_SCAN);
		this.mediaInfo = mediaInfo;
		xstream = new XStream();
		xstream.setClassLoader(getClass().getClassLoader());
		xstream.alias("EmailMsgPlus", EmailMsgPlus.class);
		xstream.alias("participant", Participant.class);
		init(true);
	}

	/**
	 * 初始化Email邮箱
	 * 
	 * @param connectionFlag
	 *            初始化标记位，true为系统启动时的初始化，false为系统运行中途某时刻的初始化
	 */
	private void init(boolean connectionFlag) {
		Properties props = System.getProperties();
		Session session = Session.getDefaultInstance(props, null);
		try {
			store = session.getStore("pop3");
			store.connect(PropertyUtil.getEmailProperty().getProperty("pop3"),
					PropertyUtil.getEmailProperty().getProperty("loginName"),
					PropertyUtil.getEmailProperty().getProperty("loginPwd"));
			connectTimes = 0;
			Res.log(Res.INFO, "UMS连接Email服务器"
					+ PropertyUtil.getEmailProperty().getProperty("pop3")
					+ "成功！");
		} catch (MessagingException e) {
			// 无法连上Email服务器
			Res.log(Res.ERROR, "UMS无法连接指定的Email服务器："
					+ PropertyUtil.getEmailProperty().getProperty("pop3")
					+ ",请检查网络，及相关的服务器和帐号设置！");
			if (!connectionFlag) {
				if (connectTimes >= 5) {
					super.stop();
					Res.log(Res.ERROR, "UMS连接指定的Email服务器失败："
							+ PropertyUtil.getEmailProperty().getProperty(
									"pop3") + ",请检查网络，及相关的服务器和帐号设置！");
				} else {
					connectTimes++;
					Res.log(Res.INFO, "UMS将在"
							+ connectTimes
							+ "分钟后重连Email服务器："
							+ PropertyUtil.getEmailProperty().getProperty(
									"pop3"));
					try {
						Thread.sleep(connectTimes * 60 * 1000);
					} catch (InterruptedException e1) {
					}
					init(false);
				}
			} else {
				super.setIsQuit(true);
			}
		}
	}

	public int iniMsgs(UMSMsg[] msgs) {
		lastTimeMessageIDs.clear();
		int msgCount = -1;
		try {
			if (store.isConnected() == false) {
				this.init(false);
			}
			folder = store.getDefaultFolder();
			folder = folder.getFolder("INBOX");
			folder.open(Folder.READ_WRITE);
			Message[] emailMsgs = null;
			int totalcount = folder.getMessageCount();
			if (totalcount > 0 && isLastTimeHasMsg) {
				try {
					// 邮件服务器在邮件标识为删除后，会有一段缓冲期，然后才会被服务器彻底删除，这里先等待3秒，以防重复得到邮件
					// 根据NCI邮箱的测试，20-50并发需睡眠2000ms以上，50-100并发需睡眠3000ms以上，100以上并发需要睡眠4000ms以上
					// UMS的邮箱理论上最好与UMS Server的链接网络要非常通畅。
					// 通过这里的睡眠等待和记住上一轮循的邮件UID，与新一轮进行比较，基本上可以避免重复邮件的接收
					Thread.sleep(5000);
				} catch (Exception e) {
				}
			}
			// 再来一次获取过程，进一步确保该删除的已被删除
			totalcount = folder.getMessageCount();
			if (totalcount > 0) {// 告诉下一个轮循，上一次是有消息的
				isLastTimeHasMsg = true;
			} else {
				isLastTimeHasMsg = false;
			}
			if (totalcount > 16) {
				msgCount = 16;
				emailMsgs = folder.getMessages(1, 16);
			} else {
				emailMsgs = folder.getMessages();
				msgCount = emailMsgs.length;
			}
			for (int i = 0; i < msgCount; i++) {
				String pop3UID = ((POP3Folder) folder).getUID(emailMsgs[i]);
				if (!lastTimeMessageIDs.containsKey(pop3UID)) {// 上次没有收到同样id号的邮件
					MsgContent msgContent = this.getMsgContent(emailMsgs[i]);// 此时的邮件内容是一个xml
					// 邮件正文内容，UMS的接收邮件是有特定格式的，详情参见EmailMsgPlus
					String emailMsgPlusXML = msgContent.getContent();
					if (!emailMsgPlusXML.equals("")) {
						EmailMsgPlus emailMsgPlus = null;
						try {
							emailMsgPlus = (EmailMsgPlus) xstream
									.fromXML(emailMsgPlusXML);
							msgContent.setContent(emailMsgPlus
									.getRealMsgContent());
						} catch (Exception e) {
							Res
									.log(Res.ERROR, new StringBuffer(
											"接收邮件的消息格式不正确:").append(
											e.getMessage()).append("\r\n")
											.append(emailMsgPlusXML).toString());
							emailMsgs[i].setFlag(Flags.Flag.DELETED, true);
							msgCount = 0;
							break;
						}
						// 获取邮件唯一ID
						lastTimeMessageIDs.put(pop3UID, msgObj);
						Participant receiver = new Participant(emailMsgPlus
								.getIdOfReceiveService(),
								Participant.PARTICIPANT_MSG_TO,
								Participant.PARTICIPANT_ID_APPSERVICE);
						Participant sender = emailMsgPlus.getSender();
						BasicMsg basic = new BasicMsg();
						basic.setReceivers(new Participant[] { receiver });
						basic.setSender(sender);
						basic.setMediaID(mediaInfo.getMediaId());
						basic
								.setMsgAttachment(this
										.getMailAttach(emailMsgs[i]));
						basic.setAck(this.getMimeMessageAck(emailMsgs[i]));
						basic.setMsgContent(msgContent);
						basic.setNeedReply(BasicMsg.BASICMSG_NEEDREPLY_NO);
						basic.setPriority(BasicMsg.UMSMsg_PRIORITY_NORMAL);
						basic.setReplyDestination(sender.getParticipantID());
						basic
								.setServiceID(emailMsgPlus
										.getIdOfReceiveService());
						basic.setAppSerialNO(emailMsgPlus.getAppSerialNO());
						UMSMsg msg = new UMSMsg();
						msg.setBasicMsg(basic);
						msgs[i] = fullFillMsg4Email(msg);
						
					}
				} else {
					Res.log(Res.INFO, "收到重复消息" + "，UMS将丢弃该消息！");
				}
				System.out.println("#####收到一条Email#####");
				emailMsgs[i].setFlag(Flags.Flag.DELETED, true);
			}
			folder.close(true);
		} catch (MessageRemovedException e) {
			Res.log(Res.INFO,
					"Email接收渠道未与Email Server同步，此邮件不存在，UMS Server将跳过该邮件。"
							+ e.getMessage());
		} catch (MessagingException e) {
			if (e.getMessage().equalsIgnoreCase("Not connected")) {
				// 如果无法连接Email服务器，则重新初始化
				init(false);
			} else {
				Res.log(Res.ERROR, new StringBuffer("Email拨入渠道").append(
						"读取接收消息失败.").append(e.getMessage()).toString());
				Res.logExceptionTrace(e);
			}
			return -1;
		}
		return msgCount;
	}

	private UMSMsg fullFillMsg4Email(UMSMsg msg) {
		msg.setBatchMode(UMSMsg.UMSMSG_BATCHMODE_SINGLE);
		String serialNO = SerialNO.getInstance().getSerial();
		String batchNO = Util.getCurrentTimeStr("yyyyMMddHHmmss");
		msg.setBatchNO(batchNO);
		msg.setSerialNO(new Integer(serialNO).intValue());
		msg.setSequenceNO(new int[] { 0 });
		msg.setDoCount(0);
		msg.setErrMsg("");
		msg.setFinishDate(batchNO.substring(0, 8));
		msg.setFinishTime(batchNO.substring(8, 14));
		msg.setStatusFlag(UMSMsg.UMSMSG_STATUS_VALID);
		return msg;
	}

	/**
	 * 判断Email消息是否需要回执
	 * 
	 * @param mm
	 *            Email Message对象
	 * @return 回执标志，1为需要回执，0为不需回执
	 */
	private int getMimeMessageAck(Message mm) {
		String[] needAck = null;
		try {
			needAck = mm.getHeader("Disposition-Notification-To");
		} catch (MessagingException e) {
			Res.log(Res.ERROR, new StringBuffer("Email拨入渠道").append(
					"读取消息回执标识失败.").append(e.getMessage()).toString());
			Res.logExceptionTrace(e);
			e.printStackTrace();
		}
		if (needAck != null) {
			return BasicMsg.UMSMsg_ACK_YES;
		}
		return BasicMsg.UMSMsg_ACK_NO;
	}

	/**
	 * 获取邮件附件信息
	 * 
	 * @param mm
	 *            Email Message对象
	 * @return UMS的附件集合
	 */
	private MsgAttachment[] getMailAttach(Message mm) {
		MsgAttachment[] atts = null;
		ArrayList arr = new ArrayList();
		try {
			if (mm.isMimeType("multipart/*")) {
				Multipart mpart = (Multipart) mm.getContent();
				for (int i = 0; i < mpart.getCount(); i++) {
					BodyPart bodypart = mpart.getBodyPart(i);
					String dispotion = bodypart.getDisposition();
					if (dispotion != null
							&& dispotion.equalsIgnoreCase(Part.ATTACHMENT)) {
						String fileName = new String(new String(bodypart
								.getFileName().getBytes(), "GB2312"));
						BufferedInputStream bis = new BufferedInputStream(
								bodypart.getInputStream());
						byte[] b = new byte[bis.available()];
						bis.read(b);
						String base64Att = MsgAttachment.getBASE64Encoder()
								.encode(b);
						bis.close();
						// String base64Att = new BASE64Encoder().encode(b);
						arr.add(new MsgAttachment(fileName, base64Att));
					}
				}
				atts = new MsgAttachment[arr.size()];
				for (int i = 0; i < arr.size(); i++) {
					atts[i] = (MsgAttachment) arr.get(i);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return atts;
	}

	private MsgContent getMsgContent(Message mm) throws MessageRemovedException {
		String contentText = new MessageContentIterator().getContentText(mm);
		// System.out.println("邮件文字内容为：" + contentText);
		String contentSubject = "";
		try {
			contentSubject = mm.getSubject();
		} catch (MessagingException e) {
			Res.log(Res.ERROR, new StringBuffer("Email拨入渠道").append(
					"读取消息标题和内容失败.").append(e.getMessage()).toString());
			Res.logExceptionTrace(e);
		}
		return new MsgContent(contentSubject, contentText);
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Folder getFolder() {
		return folder;
	}

	public void setFolder(Folder folder) {
		this.folder = folder;
	}

	public String getEmailAddr() {
		return emailAddr;
	}

	public void setEmailAddr(String emailAddr) {
		this.emailAddr = emailAddr;
	}

	public ServiceInfo getServiceInfo() {
		return serviceInfo;
	}

	public void setServiceInfo(ServiceInfo serviceInfo) {
		this.serviceInfo = serviceInfo;
	}

	/**
	 * @return the xstream
	 */
	public XStream getXstream() {
		return xstream;
	}

	/**
	 * @param xstream
	 *            the xstream to set
	 */
	public void setXstream(XStream xstream) {
		this.xstream = xstream;
	}
}
