package com.nci.ums.v3.service.common.jms;

import java.io.Serializable;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import org.apache.activemq.ActiveMQConnectionFactory;
import com.nci.ums.util.Res;


/**
 * <p>Title: AbstractJMS.java</p>
 * <p>Description:
 *    This is a general JMS definition for UMS.
 * </p>
 * <p>Copyright: 2007 Hangzhou NCI System Engineering， Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering， Ltd.</p>
 * @author Qil.Wong
 * Created in 2007.09.20
 * @version 1.0
 */

public abstract class AbstractJMS implements MessageListener {

    private String destinationSubject = "TOOL.DEFAULT";
    private Destination destination = null;
    private Connection connection = null;
    private Session session = null;
    private MessageProducer producer = null;
    private MessageConsumer consumer = null;
    private ObjectMessage objMsg;
//	private String jmsUser = null;
//	private String jmsPassword = null;
//	private String jmsURL = null;
    JMSConnBean jmsInfo;
    public static final String ERROR_CREATING_MSG = "JMS101";
    public static final String ERROR_STARTING_CONN = "JMS102";
    public static final String ERROR_SENDING_MSG = "JMS103";
    public static final String ERROR_CREATING_PRODUCER = "JMS104";
    public static final String SUCCESS_SENDING_MSG = "JMS001";

    public AbstractJMS() {
        initContext();
    }

    public AbstractJMS(JMSConnBean jmsInfo) {
        this.jmsInfo = jmsInfo;
        //set the jms url as the destination subject for the unique id
        this.setDestinationSubject(jmsInfo.getUrl());
        initContext();
    }

    /**
     * Method of how to consume the message.
     * @param objectMessage message received.
     */
    public abstract void consumeMessage(ObjectMessage objectMessage);

    private Connection getConnection() {
        ActiveMQConnectionFactory connFactory = new ActiveMQConnectionFactory(jmsInfo.getUser(), jmsInfo.getPassword(), jmsInfo.getUrl());
        try {
            return connFactory.createConnection();
        } catch (JMSException e) {
            System.out.println(e.getMessage());
            Res.log(Res.ERROR, "JMS Server ActiveMQ Connection Failed！");
            Res.logExceptionTrace(e);
            return null;
        }
    }

    /**
     * Initialize the whole context of a JMS server and Messaging.
     * Such as define destination, create message producer and consumer.
     */
    private void initContext() {
        connection = getConnection();
        try {
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (JMSException e) {
            System.out.println(e.getMessage());
            Res.log(Res.ERROR, "Creating JMS Session Failed!");
            Res.logExceptionTrace(e);
        }
        try {
            destination = session.createQueue(destinationSubject);
        } catch (JMSException e) {
            System.out.println(e.getMessage());
            Res.log(Res.ERROR, "Creating JMS Destination Failed!");
            Res.logExceptionTrace(e);
        }
        try {
            producer = session.createProducer(destination);
        } catch (JMSException e) {
            System.out.println(e.getMessage());
            Res.log(Res.ERROR, "Creating JMS Producer Failed!");
            Res.logExceptionTrace(e);
        }
        try {
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        } catch (JMSException e) {
            System.out.println(e.getMessage());
            Res.log(Res.ERROR, "Setting JMS DeliveryMode Failed!");
            Res.logExceptionTrace(e);
        }
        try {
            consumer = session.createConsumer(destination);
        } catch (JMSException e) {
            System.out.println(e.getMessage());
            Res.log(Res.ERROR, "Creating JMS Consumer Failed!");
            Res.logExceptionTrace(e);
        }
        try {
            connection.start();
        } catch (JMSException e) {
            System.out.println(e.getMessage());
            Res.log(Res.ERROR, "JMS Connection Start Failed!");
            Res.logExceptionTrace(e);
        }
    }

    /**
     * This is only for message consumer side, not for producer side.
     * Application will start to connect to jms server and set a listener.
     */
    public void startConsumeListening() {
        System.out.println("Consumer:->Begin listening...");
        // 开始监听
        try {
            consumer.setMessageListener(this);
        } catch (JMSException e) {
            System.out.println(e.getMessage());
            Res.log(Res.ERROR, "Creating JMS Consumer Failed!");
        }
    }

    public void startReceiving() {
        System.out.println("Consumer:->Begin Receiving...");
        // 开始监听
        try {
//			consumer.setMessageListener(this);
            Message message = consumer.receive();
            System.out.println(message);
        } catch (JMSException e) {
            System.out.println(e.getMessage());
            Res.log(Res.ERROR, "Creating JMS Consumer Failed!");
            Res.logExceptionTrace(e);
        }
    }

    public void onMessage(Message message) {
        ObjectMessage objectMsg = (ObjectMessage) message;
        this.setObjMsg(objectMsg);
        consumeMessage(objectMsg);
    }

    /**
     * Start to connect to a JMS server and send a message to the destination.
     * @param message the message need to be sent
     * @return int message send result flag.
     */
    public String produceMessage(Serializable message) {
//		MessageProducer producer;
        try {
            producer = session.createProducer(destination);
        } catch (JMSException e1) {
            System.out.println(e1.getMessage());
            Res.log(Res.ERROR, "Creating JMS Message Failed!");
            Res.logExceptionTrace(e1);
            return AbstractJMS.ERROR_CREATING_MSG;
        }
        try {
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        } catch (JMSException e) {
            System.out.println(e.getMessage());
            Res.log(Res.ERROR, "Setting JMS DeliveryMode Failed!");
            Res.logExceptionTrace(e);
            return AbstractJMS.ERROR_CREATING_MSG;
        }
        ObjectMessage objectMsg = null;
        try {
            objectMsg = session.createObjectMessage(message);
        } catch (JMSException e) {
            System.out.println(e.getMessage());
            Res.log(Res.ERROR, "Creating JMS Message Failed!");
            return AbstractJMS.ERROR_CREATING_MSG;
        }
//		try {
//			connection.start();
//		} catch (JMSException e) {
//			System.out.println(e.getMessage());
//			Res.log(Res.ERROR, "Start JMS Connection Failed!");
//			return AbstractJMS.ERROR_STARTING_CONN;
//		}
        try {
            producer.send(objectMsg);
        } catch (JMSException e) {
            System.out.println(e.getMessage());
            Res.log(Res.ERROR, "Sending JMS Message Failed!");
            return AbstractJMS.ERROR_SENDING_MSG;
        }
        return AbstractJMS.SUCCESS_SENDING_MSG;
    }

    // 关闭连接
    public void closeAll() throws JMSException {
        System.out.println("Consumer:->Closing connection");
        if (consumer != null) {
            consumer.close();
        }
        if (session != null) {
            session.close();
        }
        if (connection != null) {
            connection.close();
        }
        if (producer != null) {
            producer.close();
        }
    }

    public String getDestinationSubject() {
        return destinationSubject;
    }

    public void setDestinationSubject(String destinationSubject) {
        this.destinationSubject = destinationSubject;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public MessageProducer getProducer() {
        return producer;
    }

    public void setProducer(MessageProducer producer) {
        this.producer = producer;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public MessageConsumer getConsumer() {
        return consumer;
    }

    public void setConsumer(MessageConsumer consumer) {
        this.consumer = consumer;
    }

    public ObjectMessage getObjMsg() {
        return objMsg;
    }

    public void setObjMsg(ObjectMessage objMsg) {
        this.objMsg = objMsg;
    }
}