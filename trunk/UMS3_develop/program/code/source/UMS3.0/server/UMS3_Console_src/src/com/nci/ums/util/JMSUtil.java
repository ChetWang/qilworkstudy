package com.nci.ums.util;

import com.nci.ums.v3.service.common.jms.JMSConnBean;

public class JMSUtil {

	private static JMSConnBean jmsConnBean;

	private static byte[] lock = new byte[0];

	static {
		synchronized (lock) {
			if (jmsConnBean == null) {
				jmsConnBean = new JMSConnBean(null, null,
						"tcp://localhost:61616");
			}
		}
	}

	public static JMSConnBean getJMSConnBean() {

		return jmsConnBean;

	}

}
