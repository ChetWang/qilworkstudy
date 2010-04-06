package nci.gps.messages.mq;

import java.util.Iterator;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import nci.gps.util.MsgLogger;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.util.IndentPrinter;

/**
 * 服务器消息的生产者，将消息根据主题发送给不同的应用
 * 
 * @version $Revision: 1.1 $
 */
public class ServerMsgProducerTool extends Thread implements ExceptionListener {

	// private Destination destination;
	// private int messageCount = 10;
	private long sleepTime = 0L;
	// private boolean verbose = true;
	// private int messageSize = 255;
	private long timeToLive;
	// private boolean topic = false;
	private boolean transacted = false;
	private boolean persistent = false;

	private GPSMsgPool msgPool;

	private boolean running = false;

	// jms参数
	private String jmsUser = null;
	private String jmsPsw = null;
	private String jmsURL = null;

	/**
	 * JMS服务器断开的时候产生异常，触发监听，onExceptionIndex表示监听触发的序号，原则上，只要运行一次触发方法就可以重连
	 */
	private int onExceptionIndex = 0;

	/**
	 * 保证只要运行一次触发方法就可以重连的锁，否则多个连接注册了监听，会触发同一个监听方法多次
	 */
	private byte[] onExceptionLock = new byte[0];

	public ServerMsgProducerTool(GPSMsgPool msgPool) {
		this.msgPool = msgPool;
		iniParams();
		setName("ServerMsgProducerTool");
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

	private void iniJMSConnection() throws JMSException {

		Connection connection = null;
		Iterator appSubjectItrator = msgPool.getAppSubjectMap().keySet()
				.iterator();
		String subject = null;
		Destination desti = null;
		MessageProducer messageProducer = null;
		// 多主题下，分别建连接和session
		while (appSubjectItrator.hasNext()) {
			subject = (String) appSubjectItrator.next();
			// Create the connection.多个主题建多个连接
			MsgLogger.log(MsgLogger.INFO, "正在为" + subject + "建立JMS连接...");
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
					jmsUser, jmsPsw, jmsURL);
			connection = connectionFactory.createConnection();
			connection.start();
			connection.setExceptionListener(this);
			MsgLogger.log(MsgLogger.INFO, "为" + subject + "建立JMS连接成功！");
			// Create the session
			Session session = connection.createSession(transacted,
					Session.AUTO_ACKNOWLEDGE);

			desti = session.createQueue(subject);
			messageProducer = session.createProducer(desti);
			messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
			if (timeToLive != 0)
				messageProducer.setTimeToLive(timeToLive);
			msgPool.getProducerMap().put(subject, messageProducer);
			msgPool.getSessionMap().put(subject, session);
			msgPool.getJmsConnectionMap().put(subject, connection);
		}
		MsgLogger.log(MsgLogger.INFO, "初始化服务器JMS消息ServerMsgProducer完成.");
		running = true;
	}

	public void run() {

		startProducer();
		Iterator msgKeyIt;
		String keySerial;
		MessageBean msgBean;
		while (true) {
			try {
				if (running) {
					if (msgPool.hasFromFrontMessage()) {
						msgKeyIt = msgPool.getFromFrontMsgMap().keySet()
								.iterator();
						while (msgKeyIt.hasNext()) {
							keySerial = (String) msgKeyIt.next();
							msgBean = (MessageBean) msgPool
									.getFromFrontMsgMap().get(keySerial);
							// 下发消息到应用,并从缓存中清除

							((MessageProducer) msgPool.getProducerMap().get(
									msgBean.getSubject())).send(msgBean
									.getMessage());

							msgPool.removeFromMessage(keySerial);
						}
					} else {
						try {
							sleep(100);
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
	 * 清空JMS连接和Session
	 */
	private void clearConnSession() {
		try {
			Iterator it = msgPool.getJmsConnectionMap().keySet().iterator();
			while (it.hasNext()) {
				String subject = (String) it.next();
				// SessionMap 和 ConnectionMap中的主键都是subject，大小也是一样的

				// 关闭并移除Session
				Session session = (Session) msgPool.getSessionMap()
						.get(subject);
				session.close();
				msgPool.getSessionMap().remove(session);
				// 关闭并移除Connection
				Connection jmsConn = (Connection) msgPool.getJmsConnectionMap()
						.get(subject);
				jmsConn.close();
				ActiveMQConnection c = (ActiveMQConnection) jmsConn;
				c.getConnectionStats().dump(new IndentPrinter());
				msgPool.getJmsConnectionMap().remove(subject);
				msgPool.getSessionMap().clear();
				msgPool.getJmsConnectionMap().clear();
			}
		} catch (Throwable ignore) {
		}
	}

	public void onException(JMSException jmsexception) {
		running = false;
		boolean flag = false;
		synchronized (onExceptionLock) {
			onExceptionIndex++;
			if (onExceptionIndex == 1) {
				flag = true;
			}
		}
		if (flag) {// 第一个触发的事件
			MsgLogger.log(MsgLogger.WARN, "ServerMsgProducer与JMS服务器通信产生异常："
					+ jmsexception.getMessage());
			// SessionMap 和
			// ConnectionMap中的主键都是subject，大小也是一样的,所以只需使用一个迭代器就可完成两个Map的遍历
			Iterator connectionIt = msgPool.getJmsConnectionMap().keySet()
					.iterator();
			while (connectionIt.hasNext()) {
				String subject = (String) connectionIt.next();
				Connection conn = (Connection) msgPool.getJmsConnectionMap()
						.get(subject);
				Session session = (Session) msgPool.getSessionMap()
						.get(subject);
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (JMSException e) {
				}
				try {
					if (session != null) {
						session.close();
					}
				} catch (JMSException e) {
				}
				conn = null;
				session = null;
				msgPool.getJmsConnectionMap().remove(subject);
				msgPool.getSessionMap().remove(subject);
			}
			msgPool.getJmsConnectionMap().clear();
			msgPool.getSessionMap().clear();
			reconnect();
			onExceptionIndex = 0;
		}
	}

	/**
	 * 重连JMS服务器
	 */
	private void reconnect() {
		running = false;
		MsgLogger.log(MsgLogger.WARN, "ServerMsgProducer准备10秒后重连JMS.");
		try {
			sleep(10 * 1000);
		} catch (InterruptedException e) {
		}
		try {
			iniJMSConnection();
			startProducer();
		} catch (JMSException e) {
			MsgLogger.log(MsgLogger.ERROR, "ServerMsgProducer无法与JMS服务器建立连接."
					+ e.getMessage());
			reconnect();
		}
	}

	/**
	 * 停止消息生产者线程
	 */
	public void stopProducer() {
		running = false;
		clearConnSession();
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

	public void setSleepTime(long sleepTime) {
		this.sleepTime = sleepTime;
	}

	public void setTimeToLive(long timeToLive) {
		this.timeToLive = timeToLive;
	}

	public void setTransacted(boolean transacted) {
		this.transacted = transacted;
	}

}
