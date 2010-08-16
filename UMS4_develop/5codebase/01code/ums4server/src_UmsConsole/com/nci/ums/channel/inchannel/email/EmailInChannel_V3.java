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
	 * ͳһ��Ӧ�÷���email���룬Ϊ�˷����������ͳһΪһ������
	 */
	public String uniServiceMailPsw;
	// Email ��������Store��Folder����
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
	 * ����email�������Ĵ���
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
	 * ��ʼ��Email����
	 * 
	 * @param connectionFlag
	 *            ��ʼ�����λ��trueΪϵͳ����ʱ�ĳ�ʼ����falseΪϵͳ������;ĳʱ�̵ĳ�ʼ��
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
			Res.log(Res.INFO, "UMS����Email������"
					+ PropertyUtil.getEmailProperty().getProperty("pop3")
					+ "�ɹ���");
		} catch (MessagingException e) {
			// �޷�����Email������
			Res.log(Res.ERROR, "UMS�޷�����ָ����Email��������"
					+ PropertyUtil.getEmailProperty().getProperty("pop3")
					+ ",�������磬����صķ��������ʺ����ã�");
			if (!connectionFlag) {
				if (connectTimes >= 5) {
					super.stop();
					Res.log(Res.ERROR, "UMS����ָ����Email������ʧ�ܣ�"
							+ PropertyUtil.getEmailProperty().getProperty(
									"pop3") + ",�������磬����صķ��������ʺ����ã�");
				} else {
					connectTimes++;
					Res.log(Res.INFO, "UMS����"
							+ connectTimes
							+ "���Ӻ�����Email��������"
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
					// �ʼ����������ʼ���ʶΪɾ���󣬻���һ�λ����ڣ�Ȼ��Żᱻ����������ɾ���������ȵȴ�3�룬�Է��ظ��õ��ʼ�
					// ����NCI����Ĳ��ԣ�20-50������˯��2000ms���ϣ�50-100������˯��3000ms���ϣ�100���ϲ�����Ҫ˯��4000ms����
					// UMS�����������������UMS Server����������Ҫ�ǳ�ͨ����
					// ͨ�������˯�ߵȴ��ͼ�ס��һ��ѭ���ʼ�UID������һ�ֽ��бȽϣ������Ͽ��Ա����ظ��ʼ��Ľ���
					Thread.sleep(5000);
				} catch (Exception e) {
				}
			}
			// ����һ�λ�ȡ���̣���һ��ȷ����ɾ�����ѱ�ɾ��
			totalcount = folder.getMessageCount();
			if (totalcount > 0) {// ������һ����ѭ����һ��������Ϣ��
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
				if (!lastTimeMessageIDs.containsKey(pop3UID)) {// �ϴ�û���յ�ͬ��id�ŵ��ʼ�
					MsgContent msgContent = this.getMsgContent(emailMsgs[i]);// ��ʱ���ʼ�������һ��xml
					// �ʼ��������ݣ�UMS�Ľ����ʼ������ض���ʽ�ģ�����μ�EmailMsgPlus
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
											"�����ʼ�����Ϣ��ʽ����ȷ:").append(
											e.getMessage()).append("\r\n")
											.append(emailMsgPlusXML).toString());
							emailMsgs[i].setFlag(Flags.Flag.DELETED, true);
							msgCount = 0;
							break;
						}
						// ��ȡ�ʼ�ΨһID
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
					Res.log(Res.INFO, "�յ��ظ���Ϣ" + "��UMS����������Ϣ��");
				}
				System.out.println("#####�յ�һ��Email#####");
				emailMsgs[i].setFlag(Flags.Flag.DELETED, true);
			}
			folder.close(true);
		} catch (MessageRemovedException e) {
			Res.log(Res.INFO,
					"Email��������δ��Email Serverͬ�������ʼ������ڣ�UMS Server���������ʼ���"
							+ e.getMessage());
		} catch (MessagingException e) {
			if (e.getMessage().equalsIgnoreCase("Not connected")) {
				// ����޷�����Email�������������³�ʼ��
				init(false);
			} else {
				Res.log(Res.ERROR, new StringBuffer("Email��������").append(
						"��ȡ������Ϣʧ��.").append(e.getMessage()).toString());
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
	 * �ж�Email��Ϣ�Ƿ���Ҫ��ִ
	 * 
	 * @param mm
	 *            Email Message����
	 * @return ��ִ��־��1Ϊ��Ҫ��ִ��0Ϊ�����ִ
	 */
	private int getMimeMessageAck(Message mm) {
		String[] needAck = null;
		try {
			needAck = mm.getHeader("Disposition-Notification-To");
		} catch (MessagingException e) {
			Res.log(Res.ERROR, new StringBuffer("Email��������").append(
					"��ȡ��Ϣ��ִ��ʶʧ��.").append(e.getMessage()).toString());
			Res.logExceptionTrace(e);
			e.printStackTrace();
		}
		if (needAck != null) {
			return BasicMsg.UMSMsg_ACK_YES;
		}
		return BasicMsg.UMSMsg_ACK_NO;
	}

	/**
	 * ��ȡ�ʼ�������Ϣ
	 * 
	 * @param mm
	 *            Email Message����
	 * @return UMS�ĸ�������
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
		// System.out.println("�ʼ���������Ϊ��" + contentText);
		String contentSubject = "";
		try {
			contentSubject = mm.getSubject();
		} catch (MessagingException e) {
			Res.log(Res.ERROR, new StringBuffer("Email��������").append(
					"��ȡ��Ϣ���������ʧ��.").append(e.getMessage()).toString());
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
