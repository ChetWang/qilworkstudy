package com.nci.ums.v3.service.impl.activemq;

import java.sql.SQLException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import com.nci.ums.periphery.exception.OutOfMaxSequenceException;
import com.nci.ums.util.Res;
import com.nci.ums.util.serialize.UMSXMLSerializer;
import com.nci.ums.v3.message.basic.BasicMsg;
import com.nci.ums.v3.service.common.jms.AbstractJMS;
import com.nci.ums.v3.service.common.jms.JMSConnBean;
import com.nci.ums.v3.service.impl.UMSCommandCenter;

/**
 * <p>
 * Title: ActiveMQJMS.java
 * </p>
 * <p>
 * Description: The implementation of AbstractJMS, this is a implementation of a
 * JMS server named ActiveMQ.
 * </p>
 * <p>
 * Copyright: 2007 Hangzhou NCI System Engineering�� Ltd.
 * </p>
 * <p>
 * Company: Hangzhou NCI System Engineering�� Ltd.
 * </p>
 * 
 * @author Qil.Wong Created in 2007.09.20
 * @version 1.0
 */
public class ActiveMQJMS extends AbstractJMS {

	private UMSXMLSerializer serializer = new UMSXMLSerializer();

	private UMSCommandCenter umsCenter = new UMSCommandCenter();

	public ActiveMQJMS(JMSConnBean jmsInfo) {
		super(jmsInfo);
		startConsumeListening();
	}

	public void onMessage(Message message) {
		try {
			if (message instanceof ObjectMessage) {
				Object o = ((ObjectMessage) message).getObject();
				if (o instanceof BasicMsg[]) {
					consumeBasicMsgs(message, (BasicMsg[]) o);
				} else {
					Res.log(Res.WARN, "JMS �յ� ObjectMessage��������BasicMsg[],���ǣ�"
							+ o.getClass().getName());
				}
			} else if (message instanceof TextMessage) {
				String txt = ((TextMessage) message).getText();
				if (txt.startsWith("<BasicMsg-array>")) {
					BasicMsg[] basicMsgs = (BasicMsg[]) serializer
							.deserialize(txt);
					consumeBasicMsgs(message, basicMsgs);
				} else {
					Res
							.log(Res.WARN,
									"JMS �յ� TextMessage��������BasicMsg[]����Ӧ��xml,���ǣ�"
											+ txt);
				}
			} else {
				Res.log(Res.WARN, "JMS �յ� " + message.getClass().getName()
						+ ", Ŀǰ�޷�����");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Res.logExceptionTrace(e);
		}

	}

	/**
	 * ����UMS��BasicMsg���϶�������Ϣ���
	 * @param message
	 * @param basicMsgs
	 */
	private void consumeBasicMsgs(Message message, BasicMsg[] basicMsgs) {
		String result = "FAIL";
		try {
			//����UMSCommandCenter����
			result = umsCenter.start(basicMsgs);
		} catch (SQLException e) {
			e.printStackTrace();
			Res.logExceptionTrace(e);
		} catch (OutOfMaxSequenceException e) {
			e.printStackTrace();
			Res.logExceptionTrace(e);
		}
		try {
			String jmsMessageID = message.getJMSMessageID();
			//���Ҫ���н���������Է�Ӧ�÷��͹�����JMSMessage������messageid��replyto
			if (jmsMessageID == null || jmsMessageID.equals("")) {
				Res.log(Res.WARN, "�յ���JMS��Ϣ��jmsMessageID�ǿ�,�޷��������ͽ����");
				return;
			}
			if (message.getJMSReplyTo() == null) {
				Res
						.log(Res.WARN,
								"�յ���JMS��Ϣ��ReplyTo Destination��null,�޷��������ͽ����");
				return;
			}
			TextMessage txtMsg = this.getSession().createTextMessage(result);
			txtMsg.setJMSMessageID(message.getJMSMessageID());

			this.getReplyProducer().send(message.getJMSReplyTo(), txtMsg);

		} catch (JMSException e) {
			e.printStackTrace();
			Res.logExceptionTrace(e);
		}
	}

}
