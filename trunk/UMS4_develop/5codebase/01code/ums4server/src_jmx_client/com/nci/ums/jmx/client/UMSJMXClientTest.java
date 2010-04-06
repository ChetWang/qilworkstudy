package com.nci.ums.jmx.client;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import com.nci.ums.jmx.UMSManageStandardMBean;

public class UMSJMXClientTest {
	public static void main(String[] args) {
		try {
			// Create an RMI connector client and
			// connect it to the RMI connector server
			//
			System.out.println("\nCreate an RMI connector client and "
					+ "connect it to the RMI connector server");
			JMXServiceURL url = new JMXServiceURL(
					"service:jmx:rmi:///jndi/rmi://192.168.133.129:19820/server");
			JMXConnector jmxc = JMXConnectorFactory.connect(url, null);

			// Get an MBeanServerConnection
			//
			System.out.println("\nGet an MBeanServerConnection");
			MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
			ObjectName mbeanName = new ObjectName(
					"MBeans:type=com.nci.ums.jmx.UMSManageStandard");
//			mbsc.isRegistered(name)
			System.out.println("\nCreate UMSManageStandard MBean...");
			try {
				mbsc.createMBean("com.nci.ums.jmx.UMSManageStandard",
						mbeanName, null, null);
			} catch (javax.management.InstanceAlreadyExistsException e) {
				System.err.println(e.getMessage());
			}

			// Get MBean count
			//
			System.out.println("\nMBean count = " + mbsc.getMBeanCount());

			UMSManageStandardMBean proxy = (UMSManageStandardMBean) MBeanServerInvocationHandler
					.newProxyInstance(mbsc, mbeanName,
							UMSManageStandardMBean.class, false);
//			proxy.startServer();
			System.out.println("server status:"+proxy.isUMSStarted());
			NotificationListener listener = new NotificationListener() {

				public void handleNotification(Notification notification,
						Object handback) {
					System.out.println("UMS start complete");

				}
			};
			System.out.println("\nAdd notification listener...");
			mbsc.addNotificationListener(mbeanName, listener, null, null);

			System.out.println("\nWaiting for notification...");
			Thread.sleep(1000);

			// Remove notification listener on SimpleStandard MBean
			//
			System.out.println("\nRemove notification listener...");
			mbsc.removeNotificationListener(mbeanName, listener);

			// Unregister SimpleStandard MBean
			//
			System.out.println("\nUnregister SimpleStandard MBean...");
//			mbsc.unregisterMBean(mbeanName);

			// Close MBeanServer connection
			//
			System.out.println("\nClose the connection to the server");
			jmxc.close();
			System.out.println("\nBye! Bye!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
