package com.tdt.unicom.test;

import java.io.OutputStream;
import java.net.Socket;

import org.apache.xerces.impl.dv.util.Base64;


/**
 * ���Զ����·�
 * @author Tank
 *
 */
public class TestSendSms {
	
	public static void main(String[] args) throws Exception {
		    //�·��ֻ�����
			String phoneNumber ="15687150282";
			//��Ʒ�����
			String spNumber="10655820";
			//ҵ�����
			String servcieType="90860230";
			//linkId
			String linkId = "MOODDDS";
			//��Ϣ����
			String messageContent ="��ӭʹ������Ĭ�ϿƼ����޹�˾�ֻ���ֵ����" +
					"�յ���������˵�����ɹ�������˾��Ʒ����л����ʹ�ã���Ĭ�ϿƼ�����ƽ̨�����·����ԣ�";
			//�Ƿ���Ҫ״̬���棬1����Ҫ 0������Ҫ
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
				
			for (int i =0 ; i < 1000; i ++) { //���Ը߲���
				out.write(xmlbody.getBytes());
				out.flush();
			}
			out.close();
			socket.close();
	}
}
