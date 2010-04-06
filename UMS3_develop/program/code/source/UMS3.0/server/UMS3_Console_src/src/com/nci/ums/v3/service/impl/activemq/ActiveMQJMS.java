
package com.nci.ums.v3.service.impl.activemq;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import com.nci.ums.v3.message.UMSMsg;
import com.nci.ums.v3.service.common.jms.AbstractJMS;
import com.nci.ums.v3.service.common.jms.JMSConnBean;


/**
 * <p>Title: ActiveMQJMS.java</p>
 * <p>Description:
 *    The implementation of AbstractJMS, this is a implementation of a JMS server
 *    named ActiveMQ.
 * </p>
 * <p>Copyright: 2007 Hangzhou NCI System Engineering£¬ Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering£¬ Ltd.</p>
 * @author Qil.Wong
 * Created in 2007.09.20
 * @version 1.0
 */
public class ActiveMQJMS extends AbstractJMS {

	public ActiveMQJMS(JMSConnBean jmsInfo) {
		super(jmsInfo);
	}

	public void consumeMessage(ObjectMessage objectMessage) {
		try {
			System.out.println(((UMSMsg)objectMessage.getObject()).getBasicMsg().getSender().getParticipantID());
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
