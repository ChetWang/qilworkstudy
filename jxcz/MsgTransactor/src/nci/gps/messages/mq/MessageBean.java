package nci.gps.messages.mq;

import javax.jms.Message;

public class MessageBean {
	
	private Message message;
	
	private String subject;
	
	public MessageBean(String subject,Message message){
		this.message = message;
		this.subject = subject;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	

}
