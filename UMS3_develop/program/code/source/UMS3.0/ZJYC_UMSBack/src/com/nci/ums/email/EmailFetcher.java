package com.nci.ums.email;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Security;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessageRemovedException;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;

import com.nci.ums.util.DataBaseOp;
import com.nci.ums.v3.message.UMSMsg;
import com.nci.ums.v3.message.basic.BasicMsg;
import com.nci.ums.v3.message.basic.MsgAttachment;
import com.nci.ums.v3.message.basic.MsgContent;
import com.nci.ums.v3.message.basic.Participant;
import com.nci.ums.v3.service.impl.UMSSendService;
import com.sun.net.ssl.internal.ssl.Provider;
import com.thoughtworks.xstream.XStream;

public class EmailFetcher extends Thread {
	private Session session;
	private Store store = null;
	private Folder folder = null;

	protected SimpleDateFormat sdf = null;

	protected XStream xstream = null;

	protected String mediaID = "015";
	public static final String REMOTE_ERROR = "REMOTE_ERR";
	public static final String INIT_FAILED = "INIT_FAILED";
	public static final String GROUP_MALFORM = "GRP_MALFORM";
	private int sleepTime;
	private String tempServiceID = "9527";

	public EmailFetcher() {
		setName("EmailFetcher");
		initXStream();
	}

	private void init() {
		this.sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Properties mailprop = new Properties();
		try {
			Security.addProvider(new Provider());
			// String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
			InputStream is = super.getClass().getResourceAsStream(
					"email-proxy.props");
			mailprop.load(is);
			is.close();
			Properties props = new Properties();
			this.sleepTime = Integer
					.parseInt(mailprop.getProperty("sleepTime"));

			props.put("mail.smtp.host", mailprop.getProperty("smtphost"));

			SmtpAuth auth = new SmtpAuth(mailprop.getProperty("loginName"),
					mailprop.getProperty("loginPwd"));
			props.setProperty("mail.pop3.socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
			props.setProperty("mail.pop3.socketFactory.fallback", "false");
			props.put("mail.smtp.auth", "true");
			props.setProperty("mail.pop3.port", mailprop
					.getProperty("pop3port"));
			props.setProperty("mail.pop3.socketFactory.port", mailprop
					.getProperty("pop3port"));
			props.put("mail.transport.protocol", "smtp");
			this.session = Session.getInstance(props, auth);
			this.store = this.session.getStore("pop3");
			this.store
					.connect(mailprop.getProperty("pop3host"), mailprop
							.getProperty("loginName"), mailprop
							.getProperty("loginPwd"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initXStream() {
		this.xstream = new XStream();
		this.xstream.setClassLoader(super.getClass().getClassLoader());
		this.xstream.alias("UMSMsg", UMSMsg.class);
		this.xstream.alias("BasicMsg", BasicMsg.class);
		this.xstream.alias("MsgAttachment", MsgAttachment.class);
		this.xstream.alias("MsgContent", MsgContent.class);
		this.xstream.alias("Participant", Participant.class);
	}

	public synchronized String sendMessage(String receiver, String content) {
		String[] receivers = { receiver };
		return sendMessage(receivers, content);
	}

	public synchronized String sendMessage(String[] receivers, String content) {
		String[][] groupReceivers = new String[1][receivers.length];
		for (int i = 0; i < receivers.length; ++i) {
			groupReceivers[0][i] = receivers[i];
		}
		String[] contents = { content };
		return sendMessage(groupReceivers, contents);
	}

	public synchronized String sendMessage(String receiver, String content,
			String appid, String appPsw) {
		String[] receivers = { receiver };
		return sendMessage(receivers, content, appid, appPsw);
	}

	public synchronized String sendMessage(String[] receivers, String content,
			String appid, String appPsw) {
		String[][] groupReceivers = new String[1][receivers.length];
		for (int i = 0; i < receivers.length; ++i) {
			groupReceivers[0][i] = receivers[i];
		}
		String[] contents = { content };
		return sendMessage(groupReceivers, contents);
	}

	public synchronized String sendMessage(String[][] groupReceivers,
			String[] contents) {
		if (groupReceivers.length != contents.length) {
			return "GRP_MALFORM";
		}
		BasicMsg[] basicMsgs = new BasicMsg[groupReceivers.length];
		for (int i = 0; i < groupReceivers.length; ++i) {
			basicMsgs[i] = new BasicMsg();
			basicMsgs[i].setAck(0);
			basicMsgs[i].setContentMode(15);
			basicMsgs[i].setDirectSendFlag(1);
			basicMsgs[i].setMediaID(this.mediaID);
			basicMsgs[i].setMsgContent(new MsgContent("", contents[i]));
			basicMsgs[i].setPriority(0);
			ArrayList rawReceivers = new ArrayList();
			for (int n = 0; n < groupReceivers[i].length; ++n) {
				if ((groupReceivers[i][n] == null)
						|| (groupReceivers[i][n].equals("")))
					continue;
				rawReceivers.add(groupReceivers[i][n]);
			}

			Participant[] receivers = new Participant[rawReceivers.size()];
			for (int n = 0; n < rawReceivers.size(); ++n) {
				receivers[n] = new Participant((String) rawReceivers.get(n), 2,
						1);
			}
			basicMsgs[i].setReceivers(receivers);
			basicMsgs[i].setSender(new Participant("", 1, 1));
			basicMsgs[i].setServiceID(this.tempServiceID);
			String s = this.sdf.format(new Date());
			basicMsgs[i].setSubmitDate(s.substring(0, 8));
			basicMsgs[i].setSubmitTime(s.substring(8, 14));
			basicMsgs[i].setTimeSetFlag(0);
		}
		String xmlMsg = this.xstream.toXML(basicMsgs);
		return xmlMsg;
	}

	public static void main(String[] x) {
		new EmailFetcher().start();
	}

	public void run() {
		while (true) {
			try {
				try {
					EmailFetcher fetcher = new EmailFetcher();
					fetcher.init();
					fetcher.fetch();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} finally {
				Connection conn = null;
				try {
					conn = DriverManager
							.getConnection(DataBaseOp.getPoolName());
					conn.createStatement().execute(
							"delete from out_ok_v3 where serviceid='"
									+ this.tempServiceID + "'");
					Thread.sleep(this.sleepTime * 1000);
				} catch (Exception e) {
					e.printStackTrace();
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
	}

	public void fetch() {
		try {
			this.folder = this.store.getDefaultFolder();
			this.folder = this.folder.getFolder("INBOX");
			this.folder.open(2);
			Message[] emailMsgs = (Message[]) null;
			int totalcount = this.folder.getMessageCount();
			System.out.println(totalcount);
			if (totalcount > 0) {
				emailMsgs = this.folder.getMessages();
				File localConfFile = new File(System.getProperty("user.home")
						+ "/.vlg/contact.ctt");
				for (int i = 0; i < emailMsgs.length; ++i) {
					try {
						if (emailMsgs[i].getSubject().toLowerCase().indexOf(
								"contact-modify") >= 0) {
							String contentText = new EmailMessageIterator()
									.getContentPlainText(emailMsgs[i]);
							File localConfFolder = new File(System
									.getProperty("user.home")
									+ "/.vlg");
							if (!localConfFolder.exists()) {
								localConfFolder.mkdirs();
							}
							if (!localConfFile.exists()) {
								localConfFile.createNewFile();
							}
							FileOutputStream fos = new FileOutputStream(
									localConfFile);
							fos.write(contentText.getBytes());
							fos.close();
						} else {
							Properties contact = new Properties();
							if (localConfFile.exists()) {
								FileInputStream fis = new FileInputStream(
										localConfFile);
								contact.load(fis);
								fis.close();
							} else {
								contact.load(super.getClass()
										.getResourceAsStream(
												"mail-mobile.props"));
							}
							InternetAddress[] from = (InternetAddress[]) emailMsgs[i]
									.getFrom();
							String[] forwordFor = emailMsgs[i]
									.getHeader("X-Forwarded-For");

							if ((forwordFor != null) && (forwordFor.length > 0)) {
								String[] s = forwordFor[0].split(" ");
								if ((s != null)
										&& (s.length > 0)
										&& (contact.getProperty(s[0].trim()) != null)) {
									String painTextContent = new EmailMessageIterator()
											.getContentPlainText(emailMsgs[i]);
									String sendText = "空内容";
									if (painTextContent != null) {
										sendText = (painTextContent.length() > 500) ? painTextContent
												.substring(0, 499)
												+ "..."
												: painTextContent;
									}
									String receivers = contact.getProperty(s[0]
											.trim());
									String xml = sendMessage(receivers
											.split(","), "你的" + s[0]
											+ "收到一封邮件！\n标题：\n"
											+ emailMsgs[i].getSubject()
											+ "；\n来自：" + from[0].getAddress()
											+ "；\n内容：\n" + sendText);
									new UMSSendService().sendWithAck(
											"DESKTOPADMIN", "", xml);
								}
							}
						}
					} finally {
						emailMsgs[i].setFlag(Flags.Flag.DELETED, true);
					}
				}
			}
			this.folder.close(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class EmailMessageIterator {
		StringBuffer contentText = new StringBuffer();

		public EmailMessageIterator() {
		}

		public String getContentPlainText(Part mm)
				throws MessageRemovedException {
			try {
				if ((mm.isMimeType("text/plain"))
						|| (mm.isMimeType("text/html"))) {
					try {
						this.contentText.append((String) mm.getContent());
					} catch (ClassCastException cce) {
						cce.printStackTrace();
					}
				} else if (mm.isMimeType("multipart/*")) {
					Multipart multipart = (Multipart) mm.getContent();
					int count = multipart.getCount();
					for (int i = 0; i < count; ++i) {
						if ((multipart.getBodyPart(i).getDisposition() != null)
								|| (!isPaintTextPart(multipart.getBodyPart(i))))
							continue;
						String bodyContent = "";
						try {
							bodyContent = (String) multipart.getBodyPart(i)
									.getContent();
						} catch (ClassCastException cce) {
							cce.printStackTrace();
						}
						if (bodyContent.indexOf("<!DOCTYPE HTML") < 0)
							this.contentText.append(bodyContent);
					}
				} else if (mm.isMimeType("message/rfc822")) {
					getContentPlainText((Part) mm.getContent());
				}
			} catch (MessageRemovedException e) {
				throw e;
			} catch (MessagingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return this.contentText.toString();
		}

		private boolean isPaintTextPart(BodyPart bodyPart) {
			try {
				String[] s = bodyPart.getHeader("content-type");
				if (s[0].indexOf("plain") < 0)
					return false;
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
	}

	private class SmtpAuth extends Authenticator {
		private String username;
		private String password;

		public SmtpAuth(String username, String password) {
			this.username = username;
			this.password = password;
		}

		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(this.username, this.password);
		}
	}
}