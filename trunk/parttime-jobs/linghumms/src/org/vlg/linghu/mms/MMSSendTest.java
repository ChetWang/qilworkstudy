package org.vlg.linghu.mms;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.vlg.linghu.vac.VACNotifyHandler;

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
		req.setVASPID("62440"); // VASPID
		req.setVASID("10655"); // VASID
		req.setSenderAddress("106558738"); // 消息发送方（VASID +
															// ServiceCode）
															// req.setChargedPartyID("106558738");
		req.setChargedParty(MMConstants.ChargedParty.SENDER);
//		 req.addTo("8618606529811"); //8615558050237
		// 消息接收方，对于多个接收方可多次调用addTo()，addCc()，addBcc()
		req.addTo("8615558050237");
		req.setServiceCode("9036067000"); // 业务代码
		// req.setLinkedID(""); // LinkID
		req.setSubject("This is a test"); // 设置消息的标题
		req.setDeliveryReport(true); // 设置是否需要递送报告
		req.setReadReply(false); // 设置是否需要阅读报告
		req.setMessageClass(MMConstants.MessageClass.PERSONAL); // 设置消息类型
		req.setPriority(MMConstants.Priority.LOW); // 设置消息的优先级
		// req.set
		return req;
	}

	public static void testText() throws Exception {
		MMContent main = new MMContent();
		main.setContentType(MMConstants.ContentType.MULTIPART_MIXED);
		MMContent textMmc = MMContent.createFromBytes(new String("欢迎使用彩信")
				.getBytes("UTF-8"));

		// mmc.setContent(content, 0, content.length ); //添加内容体
		textMmc.setContentType(MMConstants.ContentType.TEXT); // 设置内容体格式。填写的格式一定要和内容的实际格式相同。
		textMmc.setContentID("mmstext"); // 设置该内容在该消息中的ID,必须设置,而且一定要保持它的唯一性,否则会丢失消息内容。

		MM7Sender sender = createMM7Sender();
		MM7SubmitReq req = createRequest();
		main.addSubContent(textMmc);
		req.setContent(main);
		req.setTransactionID("11234590999");
		MM7RSRes resp = sender.send(req);
		System.out.println(VACNotifyHandler.getBeanInfo(resp));

	}

	private static String getTextFromFile(String file) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String s = null;
			while((s=reader.readLine())!=null){
				sb.append(s);
			}
			reader.close();
//			return new String(sb.toString().getBytes("GBK"),"UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
//		return "";
		return sb.toString();
	}

	private static void testMultipart() throws Exception {
		MMContent main = new MMContent();
		main.setContentType(MMConstants.ContentType.MULTIPART_MIXED);

		MMContent mmcText = MMContent
				.createFromString(getTextFromFile("D:/Java/a.txt"));
		mmcText.setContentType(MMConstants.ContentType.TEXT);
		mmcText.setContentID("testmms1");
		mmcText.setCharset("GBK");
		main.addSubContent(mmcText);

		MMContent mmcPic = MMContent.createFromFile("D:/Java/a.jpg");
		mmcPic.setContentType(MMConstants.ContentType.JPEG);
		mmcPic.setContentID("testmms2");

		main.addSubContent(mmcPic);

		MMContent mmcText2 = MMContent
				.createFromString(getTextFromFile("D:/Java/b.txt"));
		mmcText2.setContentType(MMConstants.ContentType.TEXT);
		mmcText2.setContentID("testmms3");
		mmcText2.setCharset("GBK");
		main.addSubContent(mmcText2);

		MMContent mmcPic2 = MMContent.createFromFile("D:/Java/b.jpg");
		mmcPic2.setContentType(MMConstants.ContentType.JPEG);
		mmcPic2.setContentID("testmms4");

		main.addSubContent(mmcPic2);

		MM7Sender sender = createMM7Sender();
		MM7SubmitReq req = createRequest();
		req.setContent(main);
		req.setTransactionID("11234590999");
		
		MM7RSRes resp = sender.send(req);
		System.out.println(VACNotifyHandler.getBeanInfo(resp));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// testText();
			testMultipart();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
