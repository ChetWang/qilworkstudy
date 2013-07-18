package org.vlg.linghu.sms.zte.client;

import java.util.Date;

import org.apache.log4j.Logger;

import com.zte.smgw.api.sgip.client.SGIPClient;
import com.zte.smgw.api.sgip.client.SGIPClientInitInfo;
import com.zte.smgw.api.sgip.client.SGIPRsp;
import com.zte.smgw.api.sgip.message.SGIPSubmit;
import com.zte.smgw.api.sgip.message.SGIPSubmitResp;
import com.zte.smgw.api.sgip.message.body.SGIPSequenceNo;

public class TestThread implements Runnable {
	private static Logger log = Logger.getLogger(TestThread.class);
	private String username = "";
	private String password = "";

	public TestThread(String username, String password) {
		this.username = username;
		this.password = password;

	}

	public void run() {
		try {
			SGIPClientInitInfo info = new SGIPClientInitInfo();
			info.IP = "10.130.72.181";
			info.port = 8021;
			info.userName = this.username;
			info.passWord = this.password;
			info.maxLink = 1;

			SGIPClient client = SGIPClient.getInstance();
			client.init(info);

			SGIPSubmit submit = new SGIPSubmit();

			submit.getBody().setSPNumber("1067262");
			submit.getBody().setChargeNumber("13996240466");
			submit.getBody().setUserCount(1);
			submit.getBody().setUserNumber("13996240467");
			submit.getBody().setCorpId("99999");
			submit.getBody().setFeeType(1);
			submit.getBody().setFeeValue("4");
			submit.getBody().setGivenValue("987");
			submit.getBody().setAgentFlag(0);
			submit.getBody().setMorelatetoMTFlag(0);
			submit.getBody().setPriority(0);
			submit.getBody().setExpireTime("090621010101");
			submit.getBody().setScheduleTime("090621010101");
			submit.getBody().setReportFlag(0);
			submit.getBody().setTP_pid(1);
			submit.getBody().setTP_udhi(1);
			submit.getBody().setMessageCoding(0);
			submit.getBody().setMessageType(0);
			submit.getBody().setMessageContent("zhjtest".getBytes());

			submit.getBody().setReserve("dd");

			SGIPRsp rsphandler6 = null;
			Date date1 = new Date();
			for (int i = 0; i < 2000; i++) {
				rsphandler6 = new SGIPRsp();
				submit.getHead().setSequenceNo(new SGIPSequenceNo());
				client.sendSubmit(submit, rsphandler6);
				SGIPSubmitResp rsp6 = rsphandler6.waitForSGIPSubmitResp();
				log.info("" + String.valueOf(i));
			}
			log.info("start time=" + date1.toString());
			log.info("end time=" + (new Date()).toString());
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

}
