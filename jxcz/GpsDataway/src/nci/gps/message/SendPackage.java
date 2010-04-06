package nci.gps.message;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import nci.gps.util.gpsGlobal;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class SendPackage {
    private String user = ActiveMQConnection.DEFAULT_USER;    
    private String password = ActiveMQConnection.DEFAULT_PASSWORD;    
    private String url = gpsGlobal.MS_URL;
    private String subject = gpsGlobal.MS_SUBJECT; 
    private Destination destination = null;    
    private Connection connection = null;    
    private Session session = null;    
    private MessageProducer producer = null;    
   
    /**
     * 初始化函数
     * @throws JMSException
     * @throws Exception
     */   
    private void initialize() throws JMSException, Exception {    
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(    
                user, password, url);    
        connection = connectionFactory.createConnection();  
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);    
        destination = session.createQueue(subject);    
        producer = session.createProducer(destination);    
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);    
    }    
   
    /**
     * 发送字符串消息
     * @param message 消息
     * @throws JMSException
     * @throws Exception
     */    
    public void sendStrMessage(String message) throws JMSException, Exception {    
        initialize();    
        TextMessage msg = session.createTextMessage(message);    
        System.out.println("Producer:->Sending message: " + message);    
        producer.send(msg);    
        System.out.println("Producer:->Message sent complete!");    
    } 
    
    public void sendBytesMessage(byte[] message) throws JMSException, Exception{
    	initialize();
    	BytesMessage msg = session.createBytesMessage();
    	msg.writeBytes(message);
    	producer.send(msg);
    }
   
    /**
     * 关闭连接
     * @throws JMSException
     */
    public void close() throws JMSException {    
        System.out.println("Producer:->Closing connection");    
        if (producer != null)    
            producer.close();    
        if (session != null)    
            session.close();    
        if (connection != null)    
            connection.close();    
    }    

}
