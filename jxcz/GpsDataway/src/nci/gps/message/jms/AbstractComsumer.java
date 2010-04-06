package nci.gps.message.jms;

import java.io.IOException;
import java.util.HashMap;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import nci.gps.log.MsgLogger;
import nci.gps.util.ReadConfig;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public abstract class AbstractComsumer implements MessageListener,
		ExceptionListener {
	/**
	 * Consumer001���Ӧ��id
	 */
	protected String id = null;

	/**
	 * Consumer001��Ӧ������
	 */
	private String subject = null;

	private boolean running = true;

	/**
	 * JMS session
	 */
	private Session session;
	/**
	 * ��ϢĿ�ĵ�
	 */
	private Destination destination;
	
	/**
	 * JMS����
	 */
	private Connection connection;
	/**
	 * ��Ϣ������
	 */
	MessageConsumer consumer;
	/**
	 * �ظ��������ߣ�������Ϣ��Ҫ�ظ�
	 */
	private MessageProducer replyProducer;

	private boolean pauseBeforeShutdown;
	private boolean verbose = true;
	private int maxiumMessages = 0;
	private boolean topic = false;
	// private String user = "a";
	private String user = "";
	private String password = "";
	private String url = "";
	private boolean transacted = false;
	private boolean durable = false;
	private String clientId;
	private int ackMode = Session.AUTO_ACKNOWLEDGE;
	// private String consumerName = "James";
	private long sleepTime = 0;
	private long receiveTimeOut = 0;

	protected AbstractComsumer() throws NoSubjectException {
		id = getConsumerID();
		subject = (String) ((HashMap) ReadConfig.getInstance().getParamsHash()
				.get(id.toUpperCase())).get("SUBJECT");
		if (subject == null) {
			throw new NoSubjectException();
		}
		user = (String) ((HashMap) ReadConfig.getInstance().getParamsHash()
				.get(id.toUpperCase())).get("USER");
		if (user.equals("")) {
			user = ActiveMQConnection.DEFAULT_USER;
		}
		password = (String) ((HashMap) ReadConfig.getInstance().getParamsHash()
				.get(id.toUpperCase())).get("PASSWORD");
		if (password.equals("")) {
			password = ActiveMQConnection.DEFAULT_PASSWORD;
		}
		url = (String) ((HashMap) ReadConfig.getInstance().getParamsHash().get(
				"messageServer".toUpperCase())).get("URL");
		if (url.equals("")) {
			url = ActiveMQConnection.DEFAULT_BROKER_URL;
		}
	}
	
	private void iniJMSConnection() {
		try {
			// MsgLogger.log(MsgLogger.INFO, "��ʼ��������JMS��ϢConsumer.");
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
					user, password, url);
			connection = connectionFactory.createConnection();
			if (durable && clientId != null && clientId.length() > 0
					&& !"null".equals(clientId)) {
				connection.setClientID(clientId);
			}
			connection.setExceptionListener(this);
			connection.start();

			session = connection.createSession(transacted, ackMode);
			if (topic) {
				destination = session.createTopic(subject);
			} else {
				destination = session.createQueue(subject);
			}

			replyProducer = session.createProducer(null);
			replyProducer.setDeliveryMode(DeliveryMode.PERSISTENT);

			consumer = session.createConsumer(destination);

			if (maxiumMessages > 0) {
				consumeMessagesAndClose(connection, session, consumer);
			} else {
				if (receiveTimeOut == 0) {
					consumer.setMessageListener(this);
				} else {
					consumeMessagesAndClose(connection, session, consumer,
							receiveTimeOut);
				}
			}
			MsgLogger.log(MsgLogger.INFO, "��ʼ��������JMS��Ϣ"+getConsumerID()+"���.");
		} catch (JMSException e) {
			MsgLogger.log(MsgLogger.WARN,getConsumerID()+"��JMS������ͨ�Ų����쳣��" + e.getMessage());
			reconnect();
		} catch (IOException e) {
			System.out.println("Caught: " + e);
			MsgLogger.logExceptionTrace(null, e);
			// reconnect();
		}
	}

	public void start() {
		iniJMSConnection();
	}

	public void onMessage(Message message) {// ���յ���Ϣ����Ӧ
		try {

			if (message instanceof TextMessage) {
				TextMessage txtMsg = (TextMessage) message;
				if (verbose) {

					String msg = txtMsg.getText();
					if (msg.length() > 50) {
						msg = msg.substring(0, 50) + "...";
					}
					MsgLogger.log(MsgLogger.ERROR, "Received: " + msg
							+ ". �����ֽ��������ַ�����");
				}
			} else if (message instanceof BytesMessage) {
				try {
					processMsg((BytesMessage)message);
				} catch (OutofBytesMessageIndexException e) {
					MsgLogger.logExceptionTrace("����֡���ȳ���", e);
				}
			}

			if (message.getJMSReplyTo() != null) {
				replyProducer.send(message.getJMSReplyTo(), session
						.createTextMessage("Reply: "
								+ message.getJMSMessageID()));
			}

			if (transacted) {
				session.commit();
			} else if (ackMode == Session.CLIENT_ACKNOWLEDGE) {
				message.acknowledge();
			}

		} catch (JMSException e) {
			System.out.println("Caught: " + e);
			MsgLogger.logExceptionTrace(null, e);
		} finally {
			if (sleepTime > 0) {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
				}
			}
		}
	}
	
	/**
	 * ����JMS
	 */
	private void reconnect() {
		MsgLogger.log(MsgLogger.WARN, getConsumerID()+"׼��10�������JMS.");
		try {
			Thread.sleep(10 * 1000);
		} catch (InterruptedException e) {
		}
		stopConsumer();
		iniJMSConnection();
	}
	
	/**
	 * ֹͣ��Ϣ�����߼���
	 */
	public void stopConsumer() {
		running = false;

		if (consumer != null) {
			try {
				consumer.close();
			} catch (JMSException e) {
			}
		}
		if (session != null) {

			try {
				session.close();
			} catch (JMSException e) {
			}
		}
		if (connection != null) {

			try {
				connection.close();
			} catch (JMSException e) {
			}
		}

		consumer = null;
		session = null;
		connection = null;
	}

	synchronized public void onException(JMSException ex) {
		MsgLogger.log(MsgLogger.WARN, getConsumerID()+"��JMS������ͨ�Ų����쳣�����ӹر�.");
		reconnect();
	}

	synchronized boolean isRunning() {
		return running;
	}

	protected void consumeMessagesAndClose(Connection connection,
			Session session, MessageConsumer consumer) throws JMSException,
			IOException {
		System.out.println("We are about to wait until we consume: "
				+ maxiumMessages + " message(s) then we will shutdown");

		for (int i = 0; i < maxiumMessages && isRunning();) {
			Message message = consumer.receive(1000);
			if (message != null) {
				i++;
				onMessage(message);
			}
		}
		System.out.println("Closing connection");
		consumer.close();
		session.close();
		connection.close();
		if (pauseBeforeShutdown) {
			System.out.println("Press return to shut down");
			System.in.read();
		}
	}

	protected void consumeMessagesAndClose(Connection connection,
			Session session, MessageConsumer consumer, long timeout)
			throws JMSException, IOException {
		System.out
				.println("We will consume messages while they continue to be delivered within: "
						+ timeout + " ms, and then we will shutdown");

		Message message;
		while ((message = consumer.receive(timeout)) != null) {
			onMessage(message);
		}

		System.out.println("Closing connection");
		consumer.close();
		session.close();
		connection.close();
		if (pauseBeforeShutdown) {
			System.out.println("Press return to shut down");
			System.in.read();
		}
	}

	public void setAckMode(String ackMode) {
		if ("CLIENT_ACKNOWLEDGE".equals(ackMode)) {
			this.ackMode = Session.CLIENT_ACKNOWLEDGE;
		}
		if ("AUTO_ACKNOWLEDGE".equals(ackMode)) {
			this.ackMode = Session.AUTO_ACKNOWLEDGE;
		}
		if ("DUPS_OK_ACKNOWLEDGE".equals(ackMode)) {
			this.ackMode = Session.DUPS_OK_ACKNOWLEDGE;
		}
		if ("SESSION_TRANSACTED".equals(ackMode)) {
			this.ackMode = Session.SESSION_TRANSACTED;
		}
	}

	public void setClientId(String clientID) {
		this.clientId = clientID;
	}

	public void setDurable(boolean durable) {
		this.durable = durable;
	}

	public void setMaxiumMessages(int maxiumMessages) {
		this.maxiumMessages = maxiumMessages;
	}

	public void setPauseBeforeShutdown(boolean pauseBeforeShutdown) {
		this.pauseBeforeShutdown = pauseBeforeShutdown;
	}

	public void setPassword(String pwd) {
		this.password = pwd;
	}

	public void setReceiveTimeOut(long receiveTimeOut) {
		this.receiveTimeOut = receiveTimeOut;
	}

	public void setSleepTime(long sleepTime) {
		this.sleepTime = sleepTime;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setTopic(boolean topic) {
		this.topic = topic;
	}

	public void setQueue(boolean queue) {
		this.topic = !queue;
	}

	public void setTransacted(boolean transacted) {
		this.transacted = transacted;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}
	/**
	 * ��ȡConsumer��id��ÿ��consumerҪ��һ��Ψһ��id
	 * @return
	 */
	public abstract String getConsumerID();
	
	public abstract void processMsg(BytesMessage msg) throws OutofBytesMessageIndexException;
}
