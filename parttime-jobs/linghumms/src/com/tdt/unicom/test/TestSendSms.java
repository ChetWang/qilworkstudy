package com.tdt.unicom.test;

import java.io.OutputStream;
import java.net.Socket;

import org.apache.xerces.impl.dv.util.Base64;


/**
 * 测试短信下发
 * @author Tank
 *
 */
public class TestSendSms {
	
	public static void main(String[] args) throws Exception {
		    //下发手机号码
			String phoneNumber ="15687150282";
			//产品接入号
			String spNumber="10655820";
			//业务代码
			String servcieType="90860230";
			//linkId
			String linkId = "MOODDDS";
			//消息内容
			String messageContent ="欢迎使用云南默诚科技有限公司手机增值服务，" +
					"收到该条短信说明您成功定制我司产品，感谢您的使用！（默诚科技短信平台短信下发测试）";
			//是否需要状态报告，1：需要 0：不需要
			char reportFlag ='1';
		
			String xmlbody = "{<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
					+ "<gwsmip>\n" + "  <message_header>\n"
					+ "    <command_id>0x3</command_id>\n"
					+ "    <sequence_number/>\n" + "  </message_header>\n"
					+ "  <message_body>\n" + "    <pk_total>1</pk_total>\n"
					+ "    <pk_number>1</pk_number>\n" + "    <user_numbers>\n"
					+ "       <user_number>"+phoneNumber+"</user_number>\n"
					+ "    </user_numbers>\n"
					+ "    <sp_number>"+spNumber+"</sp_number>\n"
					+ "    <service_type>"+servcieType+"</service_type>\n"
					+ "    <link_id>"+linkId+"</link_id>\n"
					+ "    <message_content>" + Base64.encode(messageContent.getBytes())
					+ "</message_content>\n"
					+ "    <report_flag>"+reportFlag+"</report_flag>\n"
					+ "   </message_body>\n" + "</gwsmip>\n}";
			//221.213.100.222
			Socket socket = new Socket("211.90.223.213", 8801);
			OutputStream out = socket.getOutputStream();
				
			for (int i =0 ; i < 1000; i ++) { //测试高并发
				out.write(xmlbody.getBytes());
				out.flush();
			}
			out.close();
			socket.close();
	}
}
