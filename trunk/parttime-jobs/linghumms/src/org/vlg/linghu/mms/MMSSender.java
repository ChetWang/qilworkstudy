package org.vlg.linghu.mms;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.ByteOrderMarkDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.virbraligo.util.VLGFunction;
import org.virbraligo.util.XMLPrinter;
import org.vlg.linghu.InitServlet;
import org.vlg.linghu.SPConfig;
import org.vlg.linghu.SingleThreadPool;
import org.vlg.linghu.mybatis.bean.MmsSendMessage;
import org.vlg.linghu.mybatis.bean.MmsSendMessageExample;
import org.vlg.linghu.mybatis.bean.MmsSendMessageWithBLOBs;
import org.vlg.linghu.mybatis.bean.VacReceiveMessage;
import org.vlg.linghu.mybatis.bean.VacReceiveMessageExample;
import org.vlg.linghu.mybatis.mapper.MmsSendMessageMapper;
import org.vlg.linghu.mybatis.mapper.VacReceiveMessageMapper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.cmcc.mm7.vasp.common.MMConstants;
import com.cmcc.mm7.vasp.common.MMContent;
import com.cmcc.mm7.vasp.conf.MM7Config;
import com.cmcc.mm7.vasp.message.MM7RSRes;
import com.cmcc.mm7.vasp.message.MM7SubmitReq;
import com.cmcc.mm7.vasp.message.MM7SubmitRes;
import com.cmcc.mm7.vasp.service.MM7Sender;

public class MMSSender extends Thread {

	private static final Logger logger = LoggerFactory
			.getLogger(MMSSender.class);

	public static final int SEND_READY = 0;
	public static final int SEND_SENDING = 19999;

	// private static final String CHARSET = "UTF-8";

	@Autowired
	MmsSendMessageMapper mmsSendMessageMapper;

	@Autowired
	VacReceiveMessageMapper vacReceiveMessageMapper;

	CodepageDetectorProxy detector;

	public MMSSender() {
		setDaemon(true);
		setName("MMS-Sender");
		detector = CodepageDetectorProxy.getInstance();
		detector.add(new ByteOrderMarkDetector());
		// The first instance delegated to tries to detect the meta charset
		// attribut in html pages.
		detector.add(new ParsingDetector(true)); // be verbose about parsing.
		// This one does the tricks of exclusion and frequency detection, if
		// first implementation is
		// unsuccessful:
		detector.add(JChardetFacade.getInstance()); // Another singleton.
		detector.add(ASCIIDetector.getInstance()); // Fallback, see javadoc.
	}

	public void run() {
		logger.info("MMS Sender sarted");
		MmsSendMessageExample ex = new MmsSendMessageExample();
		ex.createCriteria().andSendStatusEqualTo(SEND_READY);
		try {
			final MM7Sender sender = createMM7Sender();

			while (true) {
				try {
					List<MmsSendMessageWithBLOBs> messages = mmsSendMessageMapper
							.selectByExampleWithBLOBs(ex);
					if (messages.size() > 0) {
						logger.info("获取到{}条待发彩信", messages.size());

						for (final MmsSendMessageWithBLOBs msg : messages) {
							msg.setSendStatus(SEND_SENDING);
							mmsSendMessageMapper
									.updateByPrimaryKeySelective(msg);
							Runnable run = new Runnable() {
								public void run() {
									logger.info("发送彩信 至{}", msg.getSendMobile());
									MM7SubmitReq req = createRequest(msg);
									if (req != null) {
										// createContent(msg, req);
										createSmilContent(msg, req);
										MM7RSRes resp = sender.send(req);
										if (resp instanceof MM7SubmitRes) {
											MM7SubmitRes submitResp = (MM7SubmitRes) resp;
											msg.setMsgid(submitResp
													.getMessageID());
										}
										msg.setSendStatus(resp.getStatusCode() + 50000);
										msg.setSendDowntime(new Date());
										mmsSendMessageMapper
												.updateByPrimaryKey(msg);
										logger.info("sent to gateway, "
												+ msg.getSendMobile()
												+ " complete, status code:"
												+ resp.getStatusCode()
												+ ", status detail: "
												+ resp.getStatusDetail()
												+ ", status text:"
												+ resp.getStatusText()
												+ ", transaction id: "
												+ resp.getTransactionID()
												+ ", mm7 version:"
												+ resp.getMM7Version());

									}
								}
							};
							SingleThreadPool.execute(run);
							sleep(SPConfig.getMsgSendDuration());
						}
					} else {
						sleep(SPConfig.getMsgDetectDuration());
					}
				} catch (Exception e) {
					logger.error("", e);
				}
			}
		} catch (Exception e1) {
			logger.error("failed to create mm7 sender", e1);
			return;
		}

	}

	private static String getTextFromFile(String file) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String s = null;
			while ((s = reader.readLine()) != null) {
				sb.append(s);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	// private

	private void createContent(MmsSendMessageWithBLOBs msg, MM7SubmitReq req) {
		MMContent main = new MMContent();
		main.setContentType(MMConstants.ContentType.MULTIPART_MIXED);
		String template = msg.getSendText();
		String[] atts = template.split(",");
		String attLocation = SPConfig.getAttachmentDir();
		for (String s : atts) {
			String att = s.trim().toLowerCase();
			if (!"".equals(att)) {
				MMContent mmc = createFromFile(attLocation + att);
				if (att.endsWith(".txt")) {
					// mmc = MMContent
					// .createFromString(getTextFromFile(attLocation + att));
					mmc.setContentType(MMConstants.ContentType.TEXT);
					java.nio.charset.Charset charset = null;
					try {
						charset = detector.detectCodepage(new File(attLocation
								+ att).toURI().toURL());
					} catch (Exception e) {
						logger.error("", e);
					}
					String charsetStr = null;
					if (charset == null) {
						charsetStr = "utf-8";
					} else {
						charsetStr = charset.displayName();
					}
					mmc.setCharset(charsetStr);
				} else if (att.endsWith(".jpg") || att.endsWith(".jpeg")) {
					mmc.setContentType(MMConstants.ContentType.JPEG);
				} else if (att.endsWith(".gif")) {
					mmc.setContentType(MMConstants.ContentType.GIF);
				} else if (att.endsWith(".png")) {
					mmc.setContentType(MMConstants.ContentType.PNG);
				} else {
					logger.error("Cannot recogonize file type '" + attLocation
							+ att + "', send to user " + msg.getSendMobile());
				}
				// mmc.setContentLocation(contentLocation)
				mmc.setContentID(att);
				main.addSubContent(mmc);
			}
		}
		req.setContent(main);
		req.setTransactionID(System.nanoTime() + "");
	}

	private MM7Sender createMM7Sender() throws Exception {
		MM7Config mm7Config = new MM7Config(getClass().getResource(
				"/mm7Config.xml").getFile());
		mm7Config.setConnConfigName(getClass()
				.getResource("/mm7ConnConfig.xml").getFile());
		MM7Sender sender = new MM7Sender(mm7Config);
		return sender;
	}

	private MM7SubmitReq createRequest(MmsSendMessage msg) {
		MM7SubmitReq req = new MM7SubmitReq();
		req.setVASPID(SPConfig.getVaspId()); // VASPID
		req.setVASID(SPConfig.getVasId()); // VASID
		String serviceId = getServiceId(msg);
		msg.setServiceid(serviceId);
		req.setSenderAddress(SPConfig.getSpNumber() + msg.getServiceid());
		req.setChargedParty(MMConstants.ChargedParty.SENDER);
		req.addTo(msg.getSendMobile());

		if (serviceId == null) {
			return null;
		}
		req.setServiceCode(serviceId); // 业务代码
		// req.setLinkedID(""); // LinkID
		req.setSubject(msg.getSendTitle()); // 设置消息的标题
		req.setDeliveryReport(true); // 设置是否需要递送报告
		req.setReadReply(false); // 设置是否需要阅读报告
		req.setMessageClass(MMConstants.MessageClass.PERSONAL); // 设置消息类型
		req.setPriority(MMConstants.Priority.LOW); // 设置消息的优先级
		// req.set
		return req;
	}

	public String getServiceId(MmsSendMessage ms) {
		VacReceiveMessageExample ex = new VacReceiveMessageExample();
		ex.createCriteria().andUseridEqualTo(ms.getSendMobile());
		List<VacReceiveMessage> vacs = vacReceiveMessageMapper
				.selectByExample(ex);
		if (vacs.size() > 0) {
			return vacs.get(0).getProductid();
		}
		logger.warn("Cannot find serviceId for user " + ms.getSendMobile());
		return null;
	}

	private String getCharset(String file) {
		java.nio.charset.Charset charset = null;
		try {
			charset = detector.detectCodepage(new File(file).toURI().toURL());
		} catch (Exception e) {
			logger.error("", e);
		}
		String charsetStr = null;
		if (charset == null) {
			charsetStr = "utf-8";
		} else {
			charsetStr = charset.displayName();
		}
		return charsetStr;
	}

	public static MMContent createFromFile(String filename) {
		try {
			FileInputStream fis = new FileInputStream(filename);
			DataInputStream input = new DataInputStream(fis);
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			byte[] data = null;
			while (input.available() != 0) {
				output.write(input.readByte());
			}
			if (filename.endsWith(".txt")) {
				output.write("\r\n".getBytes());
			}
			data = output.toByteArray();
			ByteArrayInputStream bin = new ByteArrayInputStream(data);
			MMContent content = MMContent.createFromStream(bin);
			bin.close();
			fis.close();
			input.close();
			return content;
		} catch (IOException ioe) {
			System.err.println(ioe);
		}
		return null;
	}

	private void createSmilContent(MmsSendMessageWithBLOBs msg, MM7SubmitReq req) {
		String smil = createSmilFile(msg);
		MMContent presentation = createFromFile(smil);
		presentation.setContentType(MMConstants.ContentType.SMIL);
		presentation.setContentID("smil");
		String template = msg.getSendText();
		String[] atts = template.split(",");
		MMContent maincontent = new MMContent();
		maincontent.setContentType(MMConstants.ContentType.MULTIPART_RELATED);

		maincontent.addSubContent(presentation);

		String attLocation = SPConfig.getAttachmentDir();
		for (int i = 0; i < atts.length; i++) {
			String att = atts[i].trim().toLowerCase();
			if (!"".equals(att)) {
				MMContent mmc = createFromFile(attLocation + att);
				if (att.endsWith(".txt")) {

					mmc.setContentType(MMConstants.ContentType.TEXT);

					mmc.setCharset(getCharset(attLocation + att));
					// mmc.setCharset("GBK");
				} else {
					
					if (att.endsWith(".jpg") || att.endsWith(".jpeg")) {
						mmc.setContentType(MMConstants.ContentType.JPEG);
					} else if (att.endsWith(".gif")) {
						mmc.setContentType(MMConstants.ContentType.GIF);
					} else if (att.endsWith(".png")) {
						mmc.setContentType(MMConstants.ContentType.PNG);
					} else {
						logger.error("Cannot recogonize file type '"
								+ attLocation + att + "', send to user "
								+ msg.getSendMobile());
					}
				}
				mmc.setContentLocation(att);
				mmc.setContentID(att);
				maincontent.addSubContent(mmc);
			}
		}
		req.setContent(maincontent);
		req.setTransactionID(System.nanoTime() + "");
	}

	private Element getHeadLayout(Document templateDoc) {
		return VLGFunction.findElement("//smil/head/layout",
				templateDoc.getDocumentElement());
	}

	private Element getSmilBody(Document templateDoc) {
		return VLGFunction.findElement("//smil/body",
				templateDoc.getDocumentElement());
	}

	public String createSmilFile(MmsSendMessageWithBLOBs msg) {
		Document templateDoc = VLGFunction.getXMLDocument(InitServlet.WEB_INF
				+ "smil-template.xml");
		Element headLayoutEle = getHeadLayout(templateDoc);
		Element bodyEle = getSmilBody(templateDoc);
		String template = msg.getSendText();
		String[] atts = template.split(",");
		String attLocation = SPConfig.getAttachmentDir();
		String name = SPConfig.getAttachmentDir()
				+ UUID.randomUUID().toString() + ".smil";
		for (int i = 0; i < atts.length; i++) {
			String att = atts[i].trim().toLowerCase();
			if (!"".equals(att)) {
				// Element regionEle = templateDoc.createElement("region");
				// headLayoutEle.appendChild(regionEle);
				// regionEle.setAttribute("id", arg1)
			}
		}
		for (int i = 0; i < atts.length; i++) {
			String att = atts[i].trim().toLowerCase();
			if (!"".equals(att)) {
				Element parEle = templateDoc.createElement("par");
				bodyEle.appendChild(parEle);
				parEle.setAttribute("id", "par" + i);

				if (att.endsWith(".txt")) {
//					parEle.setAttribute("dur", "10000ms");
					Element textEle = templateDoc.createElement("text");
					parEle.appendChild(textEle);
					textEle.setAttribute("src", att);
					textEle.setAttribute("region", "text");
				} else if (att.endsWith(".jpg") || att.endsWith(".jpeg")
						|| att.endsWith(".gif") || att.endsWith(".png")) {
//					parEle.setAttribute("dur", "3000ms");
					Element imgEle = templateDoc.createElement("img");
					parEle.appendChild(imgEle);
					imgEle.setAttribute("src", att);
					imgEle.setAttribute("region", "img");
				} else {
					logger.error("Cannot recogonize file type '" + attLocation
							+ att + "', send to user " + msg.getSendMobile());
				}
			}
		}
		try {
			String xml = XMLPrinter.printNode(templateDoc.getDocumentElement(),
					false);
			FileOutputStream fos = new FileOutputStream(name);
			fos.write(xml.getBytes());
			fos.flush();
			fos.close();
		} catch (IOException e) {
			logger.error("", e);
		}
		return name;
	}

}
