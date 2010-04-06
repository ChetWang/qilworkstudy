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
 * Copyright: 2007 Hangzhou NCI System Engineering， Ltd.
 * </p>
 * <p>
 * Company: Hangzhou NCI System Engineering， Ltd.
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
					Res.log(Res.WARN, "JMS 收到 ObjectMessage，但不是BasicMsg[],而是："
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
									"JMS 收到 TextMessage，但不是BasicMsg[]所对应的xml,而是："
											+ txt);
				}
			} else {
				Res.log(Res.WARN, "JMS 收到 " + message.getClass().getName()
						+ ", 目前无法处理");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Res.logExceptionTrace(e);
		}

	}

	/**
	 * 处理UMS的BasicMsg集合对象，是消息类的
	 * @param message
	 * @param basicMsgs
	 */
	private void consumeBasicMsgs(Message message, BasicMsg[] basicMsgs) {
		String result = "FAIL";
		try {
			//交付UMSCommandCenter处理
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
			//如果要进行结果反馈，对方应用发送过来的JMSMessage必须有messageid和replyto
			if (jmsMessageID == null || jmsMessageID.equals("")) {
				Res.log(Res.WARN, "收到的JMS消息的jmsMessageID是空,无法反馈发送结果。");
				return;
			}
			if (message.getJMSReplyTo() == null) {
				Res
						.log(Res.WARN,
								"收到的JMS消息的ReplyTo Destination是null,无法反馈发送结果。");
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
