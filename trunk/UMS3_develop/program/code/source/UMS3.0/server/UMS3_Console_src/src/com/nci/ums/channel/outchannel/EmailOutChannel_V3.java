/*
 * EmailOutChannel_V3.java
 *
 * Created on 2007-10-10, 22:06:07
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nci.ums.channel.outchannel;

import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.nci.ums.channel.channelinfo.Email_V3DataLockFlag;
import com.nci.ums.channel.channelinfo.Email_V3ThreadLockFlag;
import com.nci.ums.channel.channelinfo.LockFlag;
import com.nci.ums.channel.channelinfo.MediaInfo;
import com.nci.ums.periphery.application.SmtpAuth;
import com.nci.ums.util.PropertyUtil;
import com.nci.ums.util.Res;
import com.nci.ums.v3.message.UMSMsg;
import com.nci.ums.v3.message.basic.MsgAttachment;
import com.nci.ums.v3.message.basic.Participant;

/**
 * 
 * @author Qil.Wong
 */
public class EmailOutChannel_V3 extends OutChannel_V3 {

	private Properties mailprop;
	private Session session;
	protected Email_V3DataLockFlag email_v3DataLockFlag;
	protected Email_V3ThreadLockFlag emailv3_ThreadLockFlag;
	// ��ʶ���EmailOutChannel_V3�Ƿ����������͸澯��Ϣ�ģ���Ϊtrue������Ϊfalse
	private boolean warningFlag;
	private String mailFromSuffix = null;

	public EmailOutChannel_V3(MediaInfo mediaInfo) {
		super(mediaInfo);
		this.iniMailProp();
		this.iniMailMimeMessage();
		warningFlag = false;
		groupMessage = true;
	}

	public EmailOutChannel_V3() {
		this.iniMailProp();
		this.iniMailMimeMessage();
		warningFlag = true;
	}

	private void iniMailProp() {
		email_v3DataLockFlag = Email_V3DataLockFlag.getInstance();
		emailv3_ThreadLockFlag = Email_V3ThreadLockFlag.getInstance();
		mailprop = PropertyUtil.getEmailProperty();
		mailFromSuffix = "@" + mailprop.getProperty("domain");
	}

	private void iniMailMimeMessage() {

		Properties props = new Properties();

		// Setup smtp mail server
		props.put("mail.smtp.host", mailprop.getProperty("host"));
		// �����Ҫ��֤
		SmtpAuth auth = new SmtpAuth(mailprop.getProperty("loginName"),
				mailprop.getProperty("loginPwd"));
		props.put("mail.smtp.auth", "true");
		props.put("mail.transport.protocol", "smtp");
		session = Session.getInstance(props, auth);
		session.setDebug(false);
		// �������Ҫ��֤
		// session = Session.getInstance(props, null);
	}

	public void processAckMsg(UMSMsg msg, Connection conn) {
	}

	public void sendViaChannel(UMSMsg[] msgs) {

		loop_all: for (int i = 0; i < msgs.length; i++) {
			if (msgs[i] == null) {
				continue loop_all;
			}
			MimeMessage message = new MimeMessage(session);
			try {
				MimeBodyPart messageBodyPart = new MimeBodyPart();
				Multipart multipart = new MimeMultipart();
				Participant sender = msgs[i].getBasicMsg().getSender();
				String from = "";
				if (eui != null) {
					from = eui.getAccout(sender,
							Participant.PARTICIPANT_ID_EMAIL);
				}

				if (from == null || from.equals("")) {// һ��Ҫ�жϣ���Ȼ����NullPointerException����������ֹͣ
					from = sender.getParticipantID();
					if (from.indexOf("@") < 0) {
						from = from + mailFromSuffix;
					}
					Res.log(Res.DEBUG, "û���ҵ�" + sender.getParticipantID()
							+ "��Ӧ��Email�ʺţ�UMS��ֱ��ʹ��" + sender.getParticipantID()
							+ "��Ϊ���ͷ�");
				} else if (from.indexOf("@") < 0) {
					from = from + mailFromSuffix;
				}
				message.setFrom(new InternetAddress(from));
				for (int n = 0; n < msgs[i].getBasicMsg().getReceivers().length; n++) {
					Participant receiver = msgs[i].getBasicMsg().getReceivers()[n];
					if (receiver.getParticipantID() == null
							|| receiver.getParticipantID().equals("")) {
						receiver.setParticipantID("null_user_email");
						// || receiver.getParticipantID().equals("")) {
						// Res.log(Res.INFO, "���շ���Email��ַ������,���ʼ���������...");
						// msgs[i].setErrMsg("���շ���Email��ַ������");
						// ����ʧ�ܵ���Ϣ����
						// super.OpData(-1, msgs[i]);
						// continue loop_all;
					}
					InternetAddress toAddr = new InternetAddress(receiver
							.getParticipantID());
					if (receiver.getParticipantType() == Participant.PARTICIPANT_MSG_TO) {
						message.addRecipient(Message.RecipientType.TO, toAddr);
					}
					if (receiver.getParticipantType() == Participant.PARTICIPANT_MSG_CC) {
						message.addRecipient(Message.RecipientType.CC, toAddr);
					}
				}
				if (warningFlag)
					// ��ʾ�Ǹ澯��Ϣ
					message.setSubject(msgs[i].getBasicMsg().getMsgContent()
							.getSubject());
				else if (msgs[i].getBasicMsg().getAppSerialNO().equals(
						"appSerialNO")) {
					// ��ʾ��web�ͻ��˷��͵��ʼ�
					message.setSubject(msgs[i].getBasicMsg().getMsgContent()
							.getSubject());
				} else
					// ��ʾͨ���������͵�Ӧ�÷�������Ϣ
					message.setSubject("Service:"
							+ msgs[i].getBasicMsg().getServiceID()
							+ ";AppSerial:"
							+ msgs[i].getBasicMsg().getAppSerialNO()
							+ ";"
							+ msgs[i].getBasicMsg().getMsgContent()
									.getSubject());
				if (msgs[i].getBasicMsg().getReplyDestination() != null
						&& !msgs[i].getBasicMsg().getReplyDestination().equals(
								"")
						&& msgs[i].getBasicMsg().getReplyDestination().indexOf(
								"@") > 0) {
					message
							.setReplyTo(new InternetAddress[] { new InternetAddress(
									msgs[i].getBasicMsg().getReplyDestination()) });
				}
				// part 1 is text content
				messageBodyPart.setText(msgs[i].getBasicMsg().getMsgContent()
						.getContent());
				multipart.addBodyPart(messageBodyPart);
				// other parts is attachment content
				MsgAttachment[] attach = msgs[i].getBasicMsg()
						.getMsgAttachment();
				if (attach != null && attach.length > 0) {
					for (int k = 0; k < attach.length; k++) {
						messageBodyPart = new MimeBodyPart();
						if (attach[k].getFileName().equals(""))
							break;
						String base64String = attach[k].getFileByteBase64();
						byte[] bytes = MsgAttachment.getBASE64Decoder()
								.decodeBuffer(base64String);
						DataSource source = new ByteArrayDataSource(bytes,
								"text/html");
						messageBodyPart.setDataHandler(new DataHandler(source));
						String filename = attach[k].getFileName();
						messageBodyPart.setFileName("=?GBK?B?"
								+ MsgAttachment.getBASE64Encoder().encode(
										filename.getBytes()) + "?=");
						multipart.addBodyPart(messageBodyPart);
					}
				}
				// Put parts in message
				message.setContent(multipart);
				// Send the message
				try {
					Transport.send(message);
					Res.log(Res.DEBUG, "��ɷ���һ��email");
				} catch (MessagingException me) {
					me.printStackTrace();
					if (me instanceof javax.mail.AuthenticationFailedException) {
						Res.log(Res.ERROR, new StringBuffer("����").append(
								mediaInfo.getMediaId()).append(
								"��Ϣ����ʧ��,����޷�ͨ����֤!").append(me.getMessage())
								.toString());
						super.stop();
					} else {
						if (!warningFlag) {
							if (me.getMessage().equals("Invalid Addresses"))
								super.OpData(3300, msgs[i]);
							else
								super.OpData(4321, msgs[i]);
						}
						System.out.println(me.getMessage());
						Res.log(Res.ERROR, new StringBuffer("����").append(
								mediaInfo.getMediaId()).append("��Ϣ����ʧ��.")
								.append(me.getMessage()).toString());
					}
					return;
				}
				if (!warningFlag)
					super.OpData(0, msgs[i]);
			} catch (IOException ex) {
				Res.log(Res.ERROR, new StringBuffer("����").append(
						mediaInfo.getMediaId()).append("�ļ���ȡ������Ϣ����ʧ��.")
						.append(ex.getMessage()).toString());
			} catch (AddressException e) {
				e.printStackTrace();
				Res.log(Res.ERROR, new StringBuffer("����").append(
						mediaInfo.getMediaId()).append("�ʼ���ַ������Ϣ����ʧ��.")
						.append(e.getMessage()).toString());
				try {
					if (!warningFlag)
						super.OpData(3300, msgs[i]);
				} catch (Exception ex) {
				}
			} catch (MessagingException e) {
				e.printStackTrace();
				Res.log(Res.ERROR, new StringBuffer("����").append(
						mediaInfo.getMediaId()).append("�ʼ���Ϣ���ɹ��̳��ִ�����Ϣ����ʧ��.")
						.append(e.getMessage()).toString());
				try {
					if (!warningFlag)
						super.OpData(3301, msgs[i]);
				} catch (Exception ex) {
				}
			} catch (Exception e) {
				try {
					if (!warningFlag)
						super.OpData(4321, msgs[i]);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	public boolean isLocked() {
		return this.email_v3DataLockFlag.getLockFlag()
				|| this.emailv3_ThreadLockFlag.getLockFlag();
	}

	public void setLocked(boolean flag) {
		this.emailv3_ThreadLockFlag.setLockFlag(flag);
	}

	public String getMsgFmtFile() {
		return "/resources/MsgFmt_Email.xml";
	}

	public LockFlag getDataLockFlag() {
		return email_v3DataLockFlag;
	}
}

