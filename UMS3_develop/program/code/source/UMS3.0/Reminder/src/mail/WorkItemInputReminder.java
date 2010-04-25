package mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.xml.parsers.DocumentBuilderFactory;

import mail.alert.AlertSender;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class WorkItemInputReminder {
	public void send(String xmlName, boolean ssl) throws Exception {
		Document doc = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder().parse(
						super.getClass().getResourceAsStream(
								"/resources/" + xmlName));
		Element smtpEle = (Element) doc.getElementsByTagName("smtpserver")
				.item(0);
		String smtp = smtpEle.getAttribute("ip");
		String smtpport = smtpEle.getAttribute("port");
		System.out.println("smtp ip:" + smtp + ". port:" + smtpport);

		Element senderEle = (Element) doc.getElementsByTagName("sender")
				.item(0);
		String from = senderEle.getAttribute("alias");
		String loginName = senderEle.getAttribute("name");
		String psw = senderEle.getAttribute("psw");
		NodeList receivers = doc.getElementsByTagName("receiver");

		Properties props = new Properties();
		props.put("mail.smtp.host", smtp);
		props.put("mail.smtp.port", smtpport);
		SmtpAuth auth = new SmtpAuth(loginName, psw);
		props.put("mail.smtp.auth", "true");
		props.put("mail.transport.protocol", "smtp");

		props.put("mail.smtp.auth", "true");
		props.setProperty("mail.smtp.socketFactory.port", smtpport);
		if (ssl) {
			props.setProperty("mail.smtp.socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
			props.setProperty("mail.smtp.socketFactory.fallback", "false");
		}
		Session session = Session.getInstance(props, auth);
		session.setDebug(false);

		MimeMessage message = new MimeMessage(session);

		message.setFrom(new InternetAddress(from));
		InternetAddress[] receiversArr = new InternetAddress[receivers
				.getLength()];
		String[] mobiles = new String[receivers.getLength()];
		for (int i = 0; i < receivers.getLength(); ++i) {
			Element receElement = (Element) receivers.item(i);
			receiversArr[i] = new InternetAddress(receElement
					.getAttribute("mail"));
			mobiles[i] = receElement.getAttribute("mobile");
			message.addRecipient(Message.RecipientType.TO, receiversArr[i]);
		}
		message.setReplyTo(receiversArr);
		Element workItemEle = (Element) doc.getElementsByTagName("workitem")
				.item(0);
		message.setSubject(workItemEle.getAttribute("subject"));
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		String content = workItemEle.getAttribute("content");
		messageBodyPart.setText(content);
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);

		message.setContent(multipart);
		message.setHeader("X-Priority", "1");

		Transport.send(message);
		new AlertSender().sendAlert(mobiles, content);
	}

	public static void main(String[] args) throws Exception {
		if ((args == null) || (args.length == 0)) {
			args = new String[] { "card.xml", "ssl=true" };
		}
		boolean ssl = args[1].split("=")[1].equals("true");
		new WorkItemInputReminder().send(args[0], ssl);
	}
}