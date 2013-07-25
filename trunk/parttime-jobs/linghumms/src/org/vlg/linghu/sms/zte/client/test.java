package org.vlg.linghu.sms.zte.client;

import java.util.Date;

import org.apache.log4j.Logger;

import com.zte.smgw.api.sgip.client.SGIPClient;
import com.zte.smgw.api.sgip.client.SGIPClientInitInfo;
import com.zte.smgw.api.sgip.client.SGIPRsp;
import com.zte.smgw.api.sgip.message.SGIPSubmit;
import com.zte.smgw.api.sgip.message.SGIPSubmitResp;
import com.zte.smgw.api.sgip.message.body.SGIPSequenceNo;
import com.zte.smgw.socket.client.ClientSendThread;

public class test {
	private static Logger log = Logger.getLogger(ClientSendThread.class);

	public static void main(String[] args) {
		try {

			SGIPClientInitInfo info = new SGIPClientInitInfo();
			info.IP = "211.90.223.213";
			info.port = 8801;
			info.userName = "xiangyu";
			info.passWord = "xy28187688";
			info.maxLink = 1;
			SGIPClient client = SGIPClient.getInstance();
			client.init(info);

			SGIPSubmit submit = new SGIPSubmit();

			// submit.getBody().se
			// 企业号（CorpId）与接入号（SpNumber）的关系?一个SP具有唯一一个企业号，特殊情况下一个SP允许向联通申请多个接入号，即企业号与接入号可为一对多的关系。
			submit.getBody().setCorpId("62440");
			submit.getBody().setSPNumber("106558738");
			submit.getBody().setServiceType("9036067001");
//			submit.getBody().setChargeNumber("106558738");
			submit.getBody().setUserCount(1);
			
//			submit.getBody().setUserNumber("8615558050237");
			submit.getBody().setUserNumber("8618657158100");
			submit.getBody().setFeeType(1);
			submit.getBody().setFeeValue("0");
//			submit.getBody().setGivenValue("987");
			submit.getBody().setGivenValue("0");
			submit.getBody().setAgentFlag(0);
			submit.getBody().setMorelatetoMTFlag(0);
			submit.getBody().setPriority(0);
//			submit.getBody().setExpireTime("090621010101");
//			submit.getBody().setScheduleTime("090621010101");
			submit.getBody().setReportFlag(1);
			submit.getBody().setTP_pid(0);
			submit.getBody().setTP_udhi(0);
			submit.getBody().setMessageCoding(15);
			submit.getBody().setMessageType(0);
			String content = "xiangyu sms test,后面是中文";
			submit.getBody().setMessageContent(content.getBytes());
//			submit.getBody().setMessageLength(content.getBytes().length);
//			submit.getBody().setReserve("dd");//reserve(Linkid) 保留，扩张用

			SGIPRsp rsphandler6 = null;
			Date date1 = new Date();
			for (int i = 0; i < 1; i++) {
				rsphandler6 = new SGIPRsp();
				submit.getHead().setSequenceNo(new SGIPSequenceNo());
				client.sendSubmit(submit, rsphandler6);
				SGIPSubmitResp rsp6 = rsphandler6.waitForSGIPSubmitResp();
//				rsp6.getBody().getResult();
				log.info("result" + String.valueOf(i+" "+rsp6.getBody().getResult()));
			}
			log.info("start time =" + date1.toString());
			log.info("end time=" + (new Date()).toString());
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
}
