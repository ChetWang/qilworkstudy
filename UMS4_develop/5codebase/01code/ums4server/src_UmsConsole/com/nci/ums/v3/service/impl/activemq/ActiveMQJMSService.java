package com.nci.ums.v3.service.impl.activemq;

import java.util.Properties;

import org.apache.activemq.broker.BrokerPlugin;
import org.apache.activemq.broker.BrokerService;

import com.nci.ums.basic.UMSModule;
import com.nci.ums.util.DynamicUMSStreamReader;
import com.nci.ums.util.Res;
import com.nci.ums.v3.service.common.jms.JMSConnBean;

public class ActiveMQJMSService implements UMSModule {
	/**
	 * JMS链接主题
	 */
	private String subject = "NCIUMS";

	private static ActiveMQJMSService service = new ActiveMQJMSService();

	// 消息断路器
	private BrokerService brokerService = null;

	// JMS链接实例
	private ActiveMQJMS activemqJMS = null;

	public static ActiveMQJMSService getInstance() {
		return service;
	}

	private ActiveMQJMSService() {

	}

	public void startModule() {
		Res.log(Res.INFO, "UMS开启JMS服务及Server端的接收器");
		brokerService = new BrokerService();
		try {
			brokerService.setPersistent(false);
			Properties p = new DynamicUMSStreamReader()
					.getProperties("/resources/jms.props");
			BrokerPlugin[] plugins = new BrokerPlugin[1];
			UMSJMSAuthorization auth = UMSJMSAuthorization.getInstance();
			plugins[0] = auth;
			brokerService.setPlugins(plugins);
			brokerService.addConnector("tcp://localhost:"
					+ p.getProperty("tcp_port", "61616"));
			brokerService.addConnector("stomp://localhost:"
					+ p.getProperty("stomp_port", "61613"));
			brokerService.start();
			JMSConnBean conn = new JMSConnBean("DESKTOPADMIN", "",
					"tcp://localhost:" + p.getProperty("tcp_port", "61616"),
					subject);
			activemqJMS = new ActiveMQJMS(conn);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void stopModule() {
		Res.log(Res.INFO, "UMS关闭JMS服务及Server端的接收器");
		try {
			activemqJMS.closeAll();
			brokerService.stop();
			brokerService = null;

		} catch (Exception e) {
			e.printStackTrace();
			Res.logExceptionTrace(e);
		}

	}

}
