package mail.alert;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;

import org.apache.log4j.Level;

public class MailDetector {
	public String uniServiceMailPsw;
	private Store store = null;
	private Folder folder = null;

	private List<String> avaliableMail = new ArrayList();

	public MailDetector() {
		init();

		this.avaliableMail.add("p_c_wang@hotmail.com");
	}

	private void init() {
		Properties props = System.getProperties();
		Session session = Session.getDefaultInstance(props, null);
		try {
			this.store = session.getStore("pop3");
			this.store.connect("mail.nci.com.cn", "wangql", "wangql.com");
		} catch (MessagingException e) {
			MsgLogger.log(Level.DEBUG, e.getMessage(), e);
		}
	}

	public boolean getMsg() throws Exception {
		boolean flag = false;
		if (!this.store.isConnected()) {
			init();
		}
		this.folder = this.store.getDefaultFolder();
		this.folder = this.folder.getFolder("INBOX");
		this.folder.open(2);
		Message[] emailMsgs = (Message[]) null;
		int totalcount = this.folder.getMessageCount();
		if (totalcount > 0) {
			try {
				Thread.sleep(5000L);
			} catch (Exception localException) {
			}
			totalcount = this.folder.getMessageCount();
		}
		if (totalcount > 16)
			emailMsgs = this.folder.getMessages(1, 16);
		else {
			emailMsgs = this.folder.getMessages();
		}

		for (Message msg : emailMsgs) {
			InternetAddress[] froms = (InternetAddress[]) msg.getFrom();
			MsgLogger.log(Level.DEBUG, msg.getContent(), null);
			if (this.avaliableMail.contains(froms[0].getAddress())) {
				flag = true;
				msg.setFlag(Flags.Flag.DELETED, true);
			}
		}
		this.folder.close(true);
		return flag;
	}
}