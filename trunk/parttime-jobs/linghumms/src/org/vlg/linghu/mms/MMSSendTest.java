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
		req.setSenderAddress("106558738" + "31360129"); // 消息发送方（VASID +
														// ServiceCode）
		req.addTo("8615558050237"); // 消息接收方，对于多个接收方可多次调用addTo()，addCc()，addBcc()
		req.setServiceCode("31360129"); // 业务代码
		req.setLinkedID("0"); // LinkID
		req.setSubject("这是一个测试"); // 设置消息的标题
		req.setDeliveryReport(false); // 设置是否需要递送报告
		req.setReadReply(false); // 设置是否需要阅读报告
		req.setMessageClass(MMConstants.MessageClass.PERSONAL); // 设置消息类型
		req.setPriority(MMConstants.Priority.LOW); // 设置消息的优先级
		// req.set
		return req;
	}

	public static void testText() throws Exception {
		MMContent main = new MMContent();
		main.setContentType(MMConstants.ContentType.MULTIPART_MIXED);
		MMContent mmc = MMContent.createFromBytes(new String("欢迎使用彩信")
				.getBytes("GBK"));

		// mmc.setContent(content, 0, content.length ); //添加内容体
		mmc.setContentType(MMConstants.ContentType.TEXT); // 设置内容体格式。填写的格式一定要和内容的实际格式相同。
		mmc.setContentID("testmms"); // 设置该内容在该消息中的ID,必须设置,而且一定要保持它的唯一性,否则会丢失消息内容。

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
