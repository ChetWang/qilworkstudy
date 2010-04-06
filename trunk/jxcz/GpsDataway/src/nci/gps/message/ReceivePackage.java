package nci.gps.message;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import nci.gps.util.gpsGlobal;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class ReceivePackage implements MessageListener {
    private String user = ActiveMQConnection.DEFAULT_USER;    
    private String password = ActiveMQConnection.DEFAULT_PASSWORD;    
//    private String url = ActiveMQConnection.DEFAULT_BROKER_URL;    
    private String url = gpsGlobal.MS_URL;
    private String subject = "TOOL.DEFAULT";    
    private Destination destination = null;    
    private Connection connection = null;    
    private Session session = null;    
    private MessageConsumer consumer = null;    
   
    /**
     * 初始化函数
     * @throws JMSException
     * @throws Exception
     */   
    private void initialize() throws JMSException, Exception {    
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(    
                user, password, url);    
        connection = connectionFactory.createConnection();    
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);    
        destination = session.createQueue(subject);    
        consumer = session.createConsumer(destination);    
            
    }    
   
    /**
     * 消费消息
     * @throws JMSException
     * @throws Exception
     */  
    public void consumeMessage() throws JMSException, Exception {    
        initialize();    
        connection.start();    
            
        System.out.println("Consumer:->Begin listening...");    
        // 开始监听    
        consumer.setMessageListener(this);    
        // Message message = consumer.receive();    
    }    
   
    /**
     * 关闭连接
     * @throws JMSException
     */
    public void close() throws JMSException {    
        System.out.println("Consumer:->Closing connection");    
        if (consumer != null)    
            consumer.close();    
        if (session != null)    
            session.close();    
        if (connection != null)    
            connection.close();    
    }    
   
    /**
     * 消息处理
     */
    public void onMessage(Message message) {    
        try {    
            if (message instanceof TextMessage) {    
                TextMessage txtMsg = (TextMessage) message;    
                String msg = txtMsg.getText();    
                System.out.println("Consumer:->Received: " + msg);    
            } else {    
                System.out.println("Consumer:->Received: " + message);    
            }    
        } catch (JMSException e) {    
            // TODO Auto-generated catch block    
            e.printStackTrace();    
        }    
    }    

}
