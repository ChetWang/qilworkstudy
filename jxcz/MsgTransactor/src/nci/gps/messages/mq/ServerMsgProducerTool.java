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
 * ��������Ϣ�������ߣ�����Ϣ�������ⷢ�͸���ͬ��Ӧ��
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

	// jms����
	private String jmsUser = null;
	private String jmsPsw = null;
	private String jmsURL = null;

	/**
	 * JMS�������Ͽ���ʱ������쳣������������onExceptionIndex��ʾ������������ţ�ԭ���ϣ�ֻҪ����һ�δ��������Ϳ�������
	 */
	private int onExceptionIndex = 0;

	/**
	 * ��ֻ֤Ҫ����һ�δ��������Ϳ�����������������������ע���˼������ᴥ��ͬһ�������������
	 */
	private byte[] onExceptionLock = new byte[0];

	public ServerMsgProducerTool(GPSMsgPool msgPool) {
		this.msgPool = msgPool;
		iniParams();
		setName("ServerMsgProducerTool");
	}

	/**
	 * ��ʼ��JMS����
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
		// �������£��ֱ����Ӻ�session
		while (appSubjectItrator.hasNext()) {
			subject = (String) appSubjectItrator.next();
			// Create the connection.������⽨�������
			MsgLogger.log(MsgLogger.INFO, "����Ϊ" + subject + "����JMS����...");
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
					jmsUser, jmsPsw, jmsURL);
			connection = connectionFactory.createConnection();
			connection.start();
			connection.setExceptionListener(this);
			MsgLogger.log(MsgLogger.INFO, "Ϊ" + subject + "����JMS���ӳɹ���");
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
		MsgLogger.log(MsgLogger.INFO, "��ʼ��������JMS��ϢServerMsgProducer���.");
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
							// �·���Ϣ��Ӧ��,���ӻ��������

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
				MsgLogger.log(MsgLogger.WARN, "ServerMsgProducer��JMS������ͨ�Ų����쳣��"
						+ e.getMessage());
				reconnect();
			}
		}
	}

	/**
	 * ���JMS���Ӻ�Session
	 */
	private void clearConnSession() {
		try {
			Iterator it = msgPool.getJmsConnectionMap().keySet().iterator();
			while (it.hasNext()) {
				String subject = (String) it.next();
				// SessionMap �� ConnectionMap�е���������subject����СҲ��һ����

				// �رղ��Ƴ�Session
				Session session = (Session) msgPool.getSessionMap()
						.get(subject);
				session.close();
				msgPool.getSessionMap().remove(session);
				// �رղ��Ƴ�Connection
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
		if (flag) {// ��һ���������¼�
			MsgLogger.log(MsgLogger.WARN, "ServerMsgProducer��JMS������ͨ�Ų����쳣��"
					+ jmsexception.getMessage());
			// SessionMap ��
			// ConnectionMap�е���������subject����СҲ��һ����,����ֻ��ʹ��һ���������Ϳ��������Map�ı���
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
	 * ����JMS������
	 */
	private void reconnect() {
		running = false;
		MsgLogger.log(MsgLogger.WARN, "ServerMsgProducer׼��10�������JMS.");
		try {
			sleep(10 * 1000);
		} catch (InterruptedException e) {
		}
		try {
			iniJMSConnection();
			startProducer();
		} catch (JMSException e) {
			MsgLogger.log(MsgLogger.ERROR, "ServerMsgProducer�޷���JMS��������������."
					+ e.getMessage());
			reconnect();
		}
	}

	/**
	 * ֹͣ��Ϣ�������߳�
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
						"ServerMsgProducer�޷���JMS��������������." + e.getMessage());
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
