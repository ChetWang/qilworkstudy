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
			info.userName = "wzh001c";
			info.passWord = "wzh001c";
			info.maxLink = 1;

			// info.IP = "10.130.83.207";
			// info.port = 5577;
			// info.userName = "zhjec";
			// info.passWord = "zhjec";
			// info.maxLink =1;
			SGIPClient client = SGIPClient.getInstance();
			client.init(info);

			// SGIPBindBody body = new SGIPBindBody();
			// byte x = 1;
			// body.setLoginType(x);
			// body.setLoginName("zhjecclient");
			// body.setLoginPassword("zhjecclient");
			// SGIPMessageHead head = new SGIPMessageHead();
			// SGIPSequenceNo sno = new SGIPSequenceNo();
			// sno.setNode(1);
			// sno.setTime(999444);
			// sno.setNumber(999);
			//
			// head.setCommandID(1);
			// head.setSequenceNo(sno);
			// head.setMessageLength(body.getLength() + head.getLength());
			//
			// SGIPBind bind = new SGIPBind();
			// bind.setHead(head);
			// bind.setBody(body);

			SGIPSubmit submit = new SGIPSubmit();

			// submit.getBody().se
			// ��ҵ�ţ�CorpId�������ţ�SpNumber���Ĺ�ϵ?һ��SP����Ψһһ����ҵ�ţ����������һ��SP��������ͨ����������ţ�����ҵ�������ſ�Ϊһ�Զ�Ĺ�ϵ��
			submit.getBody().setSPNumber("90360670");
			submit.getBody().setChargeNumber("15558050237");
			submit.getBody().setUserCount(1);
			submit.getBody().setUserNumber("15558050237");
			submit.getBody().setCorpId("62440");
			submit.getBody().setFeeType(1);
			submit.getBody().setFeeValue("4");
//			submit.getBody().setGivenValue("987");
			submit.getBody().setGivenValue("0");
			submit.getBody().setAgentFlag(0);
			submit.getBody().setMorelatetoMTFlag(0);
			submit.getBody().setPriority(0);
//			submit.getBody().setExpireTime("090621010101");
//			submit.getBody().setScheduleTime("090621010101");
			submit.getBody().setReportFlag(0);
			submit.getBody().setTP_pid(1);
			submit.getBody().setTP_udhi(1);
			submit.getBody().setMessageCoding(15);
			submit.getBody().setMessageType(0);
			submit.getBody().setMessageContent("zhjt2222est".getBytes());

			submit.getBody().setReserve("dd");

			SGIPRsp rsphandler6 = null;
			Date date1 = new Date();
			for (int i = 0; i < 1; i++) {
				rsphandler6 = new SGIPRsp();
				submit.getHead().setSequenceNo(new SGIPSequenceNo());
				client.sendSubmit(submit, rsphandler6);
				SGIPSubmitResp rsp6 = rsphandler6.waitForSGIPSubmitResp();
				log.info("" + String.valueOf(i));
			}
			log.info("start time =" + date1.toString());
			log.info("end time=" + (new Date()).toString());
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
}