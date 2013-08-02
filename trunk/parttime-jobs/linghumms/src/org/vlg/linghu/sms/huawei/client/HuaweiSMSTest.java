package org.vlg.linghu.sms.huawei.client;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vlg.linghu.SPConfig;

import com.huawei.insa2.comm.sgip.message.SGIPMessage;
import com.huawei.insa2.comm.sgip.message.SGIPSubmitMessage;
import com.huawei.insa2.comm.sgip.message.SGIPSubmitRepMessage;
import com.huawei.insa2.util.Args;
import com.huawei.insa2.util.Cfg;

public class HuaweiSMSTest {

	Logger logger = LoggerFactory.getLogger(HuaweiSMSTest.class);

	public static void main(String[] xx) throws Exception {

		Cfg config = new Cfg(HuaweiSMSTest.class
				.getResource("/huawei-sgip.xml").toString());
		Args a = config.getArgs("SGIPConnect");
		MySGIPSMProxy smProxy = new MySGIPSMProxy(a);
		boolean result = smProxy.connect(SPConfig.getUserName(),
				SPConfig.getPassword());
		System.out.println("login result: " + result);
		String serviceType = "9036067000";
		String corpId = "62440";
		String spNumber = "106558738";
		String chargeNumber = "";
		int feeType = 1;
		String feeValue = "0";
		String givenValue = "0";
		int agentFlag = 0;
		int morelatetoMTFlag=0;
		int priority=0;
		Date expireTime =null;
		Date scheduledTime = null;
		int reportFlag = 1;
		int TP_pid=0;
		int TP_udhi=0;
		int messageCoding =15;
		int messageType =0;
		String content = "this is a test, 带六个字中文";
		String reserve = "";
		SGIPSubmitMessage msg = new SGIPSubmitMessage(spNumber, chargeNumber, new String[]{"8615558050236"}
				, corpId, serviceType, feeType, feeValue, givenValue, agentFlag, morelatetoMTFlag,
				priority, expireTime, scheduledTime, reportFlag, TP_pid, TP_udhi, messageCoding, messageType,
				content.getBytes().length, content.getBytes(), reserve);
		SGIPSubmitRepMessage resp = (SGIPSubmitRepMessage)smProxy.send(msg);
		System.out.println("msgid: "+resp.getSrcNodeId()+resp.getTimeStamp()+resp.getSequenceId());
		System.out.println(resp);
	}

}
