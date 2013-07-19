package org.vlg.linghu.sms;

import spApi.*;
import java.net.*;
import java.io.*;

public class SMSSender {

	public static void main(String[] args) {
		SMSSender atestprocesse = new SMSSender();
	}

	public SMSSender() {
		int nodeid = 362440;// nodeID 3+CP_ID;
		Socket so = null;
		OutputStream out = null;
		InputStream input = null;
		Bind command = null;
		Bind com = null;
		SGIP_Command sgip = null;
		SGIP_Command tmp = null;
		int i = 0;
		try {
			sgip = new SGIP_Command();
			command = new Bind(nodeid,// nodeID 3+CP_ID
					1, // login type
					"xiangyu",// login name
					"xy28187688");// login password
			int err;
			byte[] byte_content = new byte[140];
			Deliver deliver = null;
			Submit submit = null;
			SubmitResp submitresp = null;
			Bind active = null;
			Unbind term = null;
			BindResp resp = null;
			UnbindResp Unresp = null;
			so = new Socket("211.90.223.213", 8801);
			out = new DataOutputStream(so.getOutputStream());
			input = new DataInputStream(so.getInputStream());
			// command=new Bind(399000);
			// command.SetLoginType(1);
			// command.SetLoginName("zhao");
			// command.SetLoginPassword("zhao");
			err = command.write(out);// 发送bind
			if (err != 0) {
				System.out.println("err" + err);
			}
			tmp = sgip.read(input);// 接收sgip消息
			if (sgip.getCommandID() == SGIP_Command.ID_SGIP_BIND_RESP) {
				resp = (BindResp) tmp;// 强制转换为bindresp
				resp.readbody();// 对消息进行解包
				System.out.println(tmp.getSeqno_1());

				System.out.println(tmp.getSeqno_2());
				System.out.println(tmp.getSeqno_3());
				System.out.println(resp.GetResult());
			}
			for (i = 0; i < 140; i++) {
				byte_content[i] = 51;
				i++;
				byte_content[i] = 51;
				i++;
				byte_content[i] = 52;
				i++;

				byte_content[i] = 53;
				i++;
				byte_content[i] = 54;
				i++;

				byte_content[i] = 55;
				i++;
				byte_content[i] = 56;
				i++;
				byte_content[i] = 57;
				i++;

				byte_content[i] = 58;
				i++;
				byte_content[i] = 59;
			}
			submit = new Submit(nodeid,// node id同上
					"106558738",// sp number
					"8615558050237",// 付费号码
					1,// 接收短消息的手机数
					//"8615558050237",// 手机号码前面加86
					"8615558050237",
					"62440",// cp_id
					"",// 业务代码
					0,// 计费类型
					"500",// 短消息收费值
					"500",// 赠送话费
					1,// 代收标志
					1,// 引起MT的原因
					9,// 优先级
					"",// 短消息终止时间
					"",// 011125120000032+短消息定时发送时间
					1,// 状态报告标志
					1,// GSM协议类型
					1,// GSM协议类型
					0,// 短消息编码格式
					0,// 信息类型
					12,// 短消息长度
					"123456789012");// 短消息内容
			// submit.setContent(0,"123");
			/*
			 * submit.setBinContent(10,byte_content); // submit=new
			 * Submit(399000); submit.setSPNumber("9200");
			 * submit.setChargeNumber("8613055555678");
			 * submit.setUserNumber("8613055551230,8613055551231");
			 * submit.setCorpId("99001"); submit.setServiceType("123");
			 * submit.setFeeType(2); submit.setFeeValue("50000");
			 * submit.setGivenValue("50001"); submit.setAgentFlag(2);
			 * submit.setMOrelatetoMTFlag(3); submit.setPriority(8);
			 * submit.setExpireTime("011125120000032+");
			 * submit.setScheduleTime("011125120000032+");
			 * submit.setReportFlag(0); submit.setTP_pid(1);
			 * submit.setTP_udhi(64); submit.setMessageType(1);
			 * 
			 * // submit.setBinContent(10,byte_content);
			 * //submit.setContent(0,"1234");
			 */
			/*
			 * submit = new Submit(399000,//该构造函数中各个参数的意义同上 "9000",
			 * "8613000061234", 2, "8613000061231,8613000061233", "99000", "",
			 * 0, "500", "500", 1, 1, 9, "", "", 1, 1, 1, 4, 0, 140,
			 * byte_content);
			 */
			submit.write(out);// 发送submit
			tmp = sgip.read(input);
			if (tmp.getCommandID() == SGIP_Command.ID_SGIP_SUBMIT_RESP) {
				submitresp = (SubmitResp) tmp;// 强制转换
				submitresp.readbody();// 解包
				System.out.println(tmp.getSeqno_1());
				System.out.println(tmp.getSeqno_2());
				System.out.println(tmp.getSeqno_3());
				System.out.println(submitresp.getResult());
			}

			// com.write(out);
			term = new Unbind(nodeid);
			term.write(out);// 发送unbind
			tmp = sgip.read(input);
			if (sgip.getCommandID() == SGIP_Command.ID_SGIP_UNBIND_RESP) {
				Unresp = (UnbindResp) tmp;
				System.out.println(tmp.getSeqno_1());
				System.out.println(tmp.getSeqno_2());
				System.out.println(tmp.getSeqno_3());
			}
			out.close();
			so.close();
		} catch (SGIP_Exception e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
