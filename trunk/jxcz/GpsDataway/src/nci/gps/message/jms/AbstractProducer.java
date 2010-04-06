package nci.gps.message.jms;

import java.util.HashMap;
import java.util.Iterator;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import nci.gps.log.MsgLogger;
import nci.gps.util.ReadConfig;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import sun.security.action.GetIntegerAction;

import edu.emory.mathcs.backport.java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractProducer extends Thread implements
		ExceptionListener {
	private Destination destination;
	private int messageCount = 10;
	private long sleepTime = 0L;
	private boolean verbose = true;
	private int messageSize = 255;
	private long timeToLive;
	private String user = "";
	private String password = "";
	private String url = "";
	private String subject = "";
	private boolean topic = false;
	private boolean transacted = false;
	private boolean persistent = true;
	private String id = null;
	/**
	 * JMS Session
	 */
	private Session session;

	private Connection connection = null;

	/**
	 * JMS消息生产者
	 */
	private MessageProducer producer;
	/**
	 * 存放待发消息
	 */
	private ConcurrentHashMap msgs = new ConcurrentHashMap();

	private boolean running = false;
	
	private int serial = 0;
	
	private int MAX_SERIAL = 10000;

	/**
	 * 构造函数，创建时就启动线程
	 * 
	 * @throws NoSubjectException
	 */
	protected AbstractProducer() throws NoSubjectException {
		id = getProducerID();
		if (id == null) {
			NullPointerException e = new NullPointerException("生产者ID不能为null");
			e.printStackTrace();
			throw e;
		}
		subject = getServerSubject();
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
		setName(id);
		try {
			iniJMSConnection();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			reconnect();
		}
	}

	private void iniJMSConnection() throws JMSException {

		// Create the connection.
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				user, password, url);
		connection = connectionFactory.createConnection();
		connection.start();
		connection.setExceptionListener(this);
		// Create the session
		session = connection
				.createSession(transacted, Session.AUTO_ACKNOWLEDGE);
		if (topic) {
			destination = session.createTopic(subject);
		} else {
			destination = session.createQueue(subject);
		}

		// 创建生产者
		producer = session.createProducer(destination);
		if (persistent) {
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
		} else {
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		}
		if (timeToLive != 0)
			producer.setTimeToLive(timeToLive);

		running = true;
		MsgLogger.log(MsgLogger.INFO, "初始化服务器JMS消息" + getProducerID() + "完成");
	}

	public void run() {

		startProducer();
		// 轮循扫描，发送消息
		Iterator msgIterator = null;
		BytesMessage bmsg = null;
		String serial = "";
		while (true) {
			try {
				if (running) {
					if (msgs.size() != 0) {
						msgIterator = msgs.keySet().iterator();
						while (msgIterator.hasNext()) {
							serial = (String)msgIterator.next();
							bmsg = (BytesMessage) msgs.get(serial);							
							producer.send(bmsg);
							msgs.remove(serial);
						}
					} else {
						try {
							sleep(200);
						} catch (InterruptedException e) {
						}
					}
				} else {
					try {
						sleep(1000);
					} catch (InterruptedException e) {
					}
				}
			} catch (JMSException e) {
				MsgLogger.log(MsgLogger.WARN, "ServerMsgProducer与JMS服务器通信产生异常："
						+ e.getMessage());
				reconnect();
			}
		}

	}

	/**
	 * 重连JMS
	 */
	private void reconnect() {
		MsgLogger.log(MsgLogger.WARN, getProducerID() + "准备10秒后重连JMS服务器.");
		try {
			sleep(10 * 1000);
		} catch (InterruptedException e) {
		}
		stopRunning();
		try {
			iniJMSConnection();
			run();
		} catch (JMSException e) {
			MsgLogger.log(MsgLogger.WARN, getProducerID() + "连接JMS服务器失败.");
			reconnect();
		}
	}

	public void onException(JMSException e) {
		MsgLogger.log(MsgLogger.WARN, getProducerID() + "与JMS服务器通信产生异常，连接关闭.");
		reconnect();
	}

	/**
	 * 停止当前线程
	 */
	public void stopRunning() {
		running = false;
	}

	public void startProducer() {
		if (!running) {
			try {
				iniJMSConnection();
				running = true;
			} catch (JMSException e) {
				MsgLogger.log(MsgLogger.ERROR,
						"ServerMsgProducer无法与JMS服务器建立连接." + e.getMessage());
				reconnect();
			}
		}
	}

	public void setPersistent(boolean durable) {
		this.persistent = durable;
	}

	public void setMessageCount(int messageCount) {
		this.messageCount = messageCount;
	}

	public void setMessageSize(int messageSize) {
		this.messageSize = messageSize;
	}

	public void setPassword(String pwd) {
		this.password = pwd;
	}

	public void setSleepTime(long sleepTime) {
		this.sleepTime = sleepTime;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setTimeToLive(long timeToLive) {
		this.timeToLive = timeToLive;
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
	 * 发送消息，将消息转换后的字节放入消息队列，等待生产者发送
	 * 
	 * @param b
	 *            待发送的字节
	 * @throws OutofBytesMessageIndexException
	 * @throws JMSException
	 */
	public void addBytesIntoSendQueue(byte[] b)
			throws OutofBytesMessageIndexException, JMSException,
			ProducerNotRunningException {
		while (!running) {
			throw new ProducerNotRunningException(getProducerID());
		}
		if (b.length > OutofBytesMessageIndexException.MAX_ONE_FRAME_BYTES) {
			OutofBytesMessageIndexException e = new OutofBytesMessageIndexException();
			throw e;
		}
		BytesMessage bMsg = session.createBytesMessage();
		bMsg.writeBytes(b);
		if(serial>MAX_SERIAL)
			serial = 0;
		msgs.put(String.valueOf(serial),bMsg);
		serial++;
	}

	public abstract String getProducerID();

	public abstract String getServerSubject();
}
