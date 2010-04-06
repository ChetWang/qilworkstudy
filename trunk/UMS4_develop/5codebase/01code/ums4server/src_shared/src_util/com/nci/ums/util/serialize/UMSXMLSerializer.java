package com.nci.ums.util.serialize;

import com.nci.ums.filter.FilterInfo;
import com.nci.ums.forward.ForwardContentBean;
import com.nci.ums.media.MediaBean;
import com.nci.ums.periphery.application.AppInfo;
import com.nci.ums.util.EmailMsgPlus;
import com.nci.ums.v3.fee.FeeBean;
import com.nci.ums.v3.message.UMSMsg;
import com.nci.ums.v3.message.basic.BasicMsg;
import com.nci.ums.v3.message.basic.MsgAttachment;
import com.nci.ums.v3.message.basic.MsgContent;
import com.nci.ums.v3.message.basic.Participant;
import com.nci.ums.v3.service.ServiceInfo;
import com.thoughtworks.xstream.XStream;

public class UMSXMLSerializer {
	XStream xstream;

	public UMSXMLSerializer() {
		xstream = new XStream();
//		xstream.
		xstream.setClassLoader(getClass().getClassLoader());
		// UMS ��Ϣ
		xstream.alias("UMSMsg", UMSMsg.class);
		xstream.alias("BasicMsg", BasicMsg.class);
		xstream.alias("MsgAttachment", MsgAttachment.class);
		xstream.alias("MsgContent", MsgContent.class);
		xstream.alias("Participant", Participant.class);
		xstream.alias("EmailMsgPlus", EmailMsgPlus.class);

		/*************************************/

		// Ӧ��
		xstream.alias("AppInfo", AppInfo.class);
		// ����
		xstream.alias("MediaBean", MediaBean.class);
		// ����
		xstream.alias("FeeBean", FeeBean.class);
		// ����
		xstream.alias("ServiceInfo", ServiceInfo.class);
		// ��ת��Ϣ
		xstream.alias("ForwardContentBean", ForwardContentBean.class);
		// ������
		xstream.alias("FilterInfo", FilterInfo.class);
	}

	/**
	 * ��xml�ַ��������л�Ϊ����
	 * 
	 * @param xml
	 * @return
	 */
	public Object deserialize(String xml) {
		return xstream.fromXML(xml);
	}

	/**
	 * ���������л�Ϊxml�ַ���
	 * 
	 * @param o
	 * @return
	 */
	public String serialize(Object o) {
		return xstream.toXML(o);
	}

	public static void main(String[] xx) {
		UMSXMLSerializer serializer = new UMSXMLSerializer();
//		AppInfo app = new AppInfo();
//		app.setAppID("1111");
//		app.setAppName("ppww");
//		app.setChannelType(1);
//		app.setCompanyID("������");
//		String xml = serializer.serialize(app);
//		System.out.println(xml);
//		AppInfo app2 = (AppInfo)serializer.deserialize(xml);
//		System.out.println(app2);
//		System.out.println(app2.getCompanyID());
		
		String strXml = "<BasicMsg><sender><participantID></participantID><idType>1</idType><participantType>1</participantType></sender><receivers><Participant><participantID>15906622497</participantID><idType>1</idType><participantType>2</participantType></Participant></receivers><msgContent><content>383</content></msgContent><appSerialNO>0</appSerialNO><sendDirectly>0</sendDirectly><mediaID>015</mediaID><submitDate>FriOct2314:06:58CST2009</submitDate><submitTime>1256278018234</submitTime><priority>0</priority><ack>1</ack><timeSetFlag>0</timeSetFlag><setSendDate></setSendDate><setSendTime></setSendTime><invalidDate></invalidDate><invalidTime></invalidTime><replyDestination></replyDestination><needReply>0</needReply><umsFlag>-1</umsFlag><companyID></companyID><contentMode>15</contentMode></BasicMsg>";
		BasicMsg app2 = (BasicMsg) serializer.deserialize(strXml);
		System.out.print("");
	}

}
