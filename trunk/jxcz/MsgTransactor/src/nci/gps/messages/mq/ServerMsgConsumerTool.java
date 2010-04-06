package nci.gps.messages.mq;

import java.io.IOException;

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

import nci.gps.util.MsgLogger;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * A simple tool for consuming messages
 * 
 * @version $Revision: 1.1 $
 */
public class ServerMsgConsumerTool implements MessageListener,
		ExceptionListener {
	/**
	 * 线程运行标记
	 */
	private boolean running = true;
	private Connection connection;
	private Session session;
	private MessageConsumer consumer;
	private Destination destination;
	private MessageProducer replyProducer;

	private boolean pauseBeforeShutdown;
	private boolean verbose = true;
	private int maxiumMessages = 0;
	private String subject = "";
	private boolean topic = false;
	// private String user = "a";
	// private String user = ActiveMQConnection.DEFAULT_USER;
	// private String password = ActiveMQConnection.DEFAULT_PASSWORD;
	// private String url = ActiveMQConnection.DEFAULT_BROKER_URL;
	private boolean transacted = false;
	private boolean durable = false;
	private String clientId;
	private int ackMode = Session.AUTO_ACKNOWLEDGE;
	// private String consumerName = "James";
	private long sleepTime = 0;
	private long receiveTimeOut = 0;

	private GPSMsgPool msgPool;

	// jms参数
	private String jmsUser = null;
	private String jmsPsw = null;
	private String jmsURL = null;

	public ServerMsgConsumerTool(GPSMsgPool msgPool) {
		this.msgPool = msgPool;
		subject = msgPool.getReplyServerSubject();
		iniParams();

	}

	/**
	 * 初始化JMS参数
	 */
	private void iniParams() {
		jmsUser = msgPool.getJMSParamMap().get("user").equals("") ? ActiveMQConnection.DEFAULT_USER
				: (String) msgPool.getJMSParamMap().get("user");
		jmsPsw = msgPool.getJMSParamMap().get("password").equals("") ? ActiveMQConnection.DEFAULT_USER
				: (String) msgPool.getJMSParamMap().get("password");
		jmsURL = msgPool.getJMSParamMap().get("url").equals("") ? ActiveMQConnection.DEFAULT_USER
				: (String) msgPool.getJMSParamMap().get("url");
	}

	private void iniJMSConnection() {
		try {
			// MsgLogger.log(MsgLogger.INFO, "初始化服务器JMS消息Consumer.");
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
					jmsUser, jmsPsw, jmsURL);
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
			replyProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

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
			MsgLogger.log(MsgLogger.INFO, "初始化服务器JMS消息ServerMsgConsumer完成.");
		} catch (JMSException e) {
			MsgLogger.log(MsgLogger.WARN, "ServerMsgConsumer与JMS服务器通信产生异常："
					+ e.getMessage());
			reconnect();
		} catch (IOException e) {
			System.out.println("Caught: " + e);
			MsgLogger.logExceptionTrace(null, e);
			// reconnect();
		}
	}

	/**
	 * 重连JMS
	 */
	private void reconnect() {
		MsgLogger.log(MsgLogger.WARN, "ServerMsgConsumer准备5秒后重连JMS.");
		try {
			Thread.sleep(5 * 1000);
		} catch (InterruptedException e) {
		}
		stopConsumer();
		iniJMSConnection();

	}

	public void start() {
		iniJMSConnection();
	}

	public void onMessage(Message message) {
		
		try {
			if (message instanceof TextMessage) {
				TextMessage txtMsg = (TextMessage) message;
				if (verbose) {

					String msg = txtMsg.getText();
					if (msg.length() > 50) {
						msg = msg.substring(0, 50) + "...";
					}
					MsgLogger.log(MsgLogger.WARN, "Received: " + msg
							+ ". 不是字节流而是字符串，不会被转发至应用！");
				}
			} else if (message instanceof BytesMessage) {
				// 将消息放入缓冲
				byte[] b = new byte[(int) ((BytesMessage) message)
						.getBodyLength()];
				((BytesMessage) message).readBytes(b);
				msgPool.addMessageIntoToPool(b);
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
			} else {
				MsgLogger.log(MsgLogger.WARN, "Received: "
						+ message.getJMSType() + ". 不是字节流Message，不会被转发至应用！");
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
	 * 停止消息消费者监听
	 */
	public void stopConsumer() {
		running = false;

		if (consumer != null) {
			try {
				consumer.setMessageListener(null);
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
		MsgLogger.log(MsgLogger.WARN, "ServerMsgConsumer与JMS服务器通信产生异常，连接关闭.");
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

	public MessageConsumer getConsumer() {
		return consumer;
	}

	public Connection getJMSConnection() {
		return connection;
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

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

}
