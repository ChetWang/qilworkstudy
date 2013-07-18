package org.vlg.linghu.mms;

import com.cmcc.mm7.vasp.common.MMConstants;
import com.cmcc.mm7.vasp.common.MMContent;
import com.cmcc.mm7.vasp.conf.MM7Config;
import com.cmcc.mm7.vasp.message.MM7RSRes;
import com.cmcc.mm7.vasp.message.MM7SubmitReq;
import com.cmcc.mm7.vasp.service.MM7Sender;

public class MMSSendTest {

	public static MM7Sender createMM7Sender() throws Exception {
		MM7Config mm7Config = new MM7Config(MMSSendTest.class.getResource(
				"/mm7Config.xml").getFile());
		mm7Config.setConnConfigName(MMSSendTest.class.getResource(
				"/mm7ConnConfig.xml").getFile());
		MM7Sender sender = new MM7Sender(mm7Config);
		
		return sender;
	}

	public static MM7SubmitReq createRequest() {
		MM7SubmitReq req = new MM7SubmitReq();
		req.setVASPID("106558738"); // VASPID
		req.setVASID("106558738"); // VASID
		req.setSenderAddress("106558738" + "31360129"); // ��Ϣ���ͷ���VASID +
														// ServiceCode��
		req.addTo("8615558050237"); // ��Ϣ���շ������ڶ�����շ��ɶ�ε���addTo()��addCc()��addBcc()
		req.setServiceCode("31360129"); // ҵ�����
		req.setLinkedID("0"); // LinkID
		req.setSubject("����һ������"); // ������Ϣ�ı���
		req.setDeliveryReport(false); // �����Ƿ���Ҫ���ͱ���
		req.setReadReply(false); // �����Ƿ���Ҫ�Ķ�����
		req.setMessageClass(MMConstants.MessageClass.PERSONAL); // ������Ϣ����
		req.setPriority(MMConstants.Priority.LOW); // ������Ϣ�����ȼ�
		// req.set
		return req;
	}

	public static void testText() throws Exception {
		MMContent main = new MMContent();
		main.setContentType(MMConstants.ContentType.MULTIPART_MIXED);
		MMContent mmc = MMContent.createFromBytes(new String("��ӭʹ�ò���")
				.getBytes("GBK"));

		// mmc.setContent(content, 0, content.length ); //���������
		mmc.setContentType(MMConstants.ContentType.TEXT); // �����������ʽ����д�ĸ�ʽһ��Ҫ�����ݵ�ʵ�ʸ�ʽ��ͬ��
		mmc.setContentID("testmms"); // ���ø������ڸ���Ϣ�е�ID,��������,����һ��Ҫ��������Ψһ��,����ᶪʧ��Ϣ���ݡ�

		MM7Sender sender = createMM7Sender();
		MM7SubmitReq req = createRequest();
		main.addSubContent(mmc);
		req.setContent(main);
		req.setTransactionID("11234590999");
		MM7RSRes resp = sender.send(req);
		System.out.println("mm7 version:" + resp.getMM7Version()
				+ ", status code:" + resp.getStatusCode() + ", status detail: "
				+ resp.getStatusDetail() + ", status text:"
				+ resp.getStatusText() + ", transaction id: "
				+ resp.getTransactionID());
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			testText();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
