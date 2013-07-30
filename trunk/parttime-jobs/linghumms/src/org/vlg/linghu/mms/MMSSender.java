package org.vlg.linghu.mms;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.ByteOrderMarkDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vlg.linghu.SPConfig;
import org.vlg.linghu.SingleThreadPool;
import org.vlg.linghu.mybatis.bean.MmsSendMessage;
import org.vlg.linghu.mybatis.bean.MmsSendMessageExample;
import org.vlg.linghu.mybatis.bean.MmsSendMessageWithBLOBs;
import org.vlg.linghu.mybatis.bean.VacReceiveMessage;
import org.vlg.linghu.mybatis.bean.VacReceiveMessageExample;
import org.vlg.linghu.mybatis.mapper.MmsSendMessageMapper;
import org.vlg.linghu.mybatis.mapper.VacReceiveMessageMapper;

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
										createContent(msg, req);
										MM7RSRes resp = sender.send(req);
										if (resp instanceof MM7SubmitRes) {
											MM7SubmitRes submitResp = (MM7SubmitRes) resp;
											msg.setMsgid(submitResp
													.getMessageID());
										}
										msg.setSendStatus(resp.getStatusCode());
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
				MMContent mmc = MMContent.createFromFile(attLocation + att);
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

}
