package com.cmcc.mm7.vasp.common;

import com.cmcc.mm7.vasp.conf.MM7Config;
import com.cmcc.mm7.vasp.message.MM7CancelReq;
import com.cmcc.mm7.vasp.message.MM7ReplaceReq;
import com.cmcc.mm7.vasp.message.MM7SubmitReq;
import com.cmcc.mm7.vasp.message.MM7VASPReq;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class SOAPEncoder {
	private MM7VASPReq mm7VaspReq;
	private boolean bMessageExist;
	private boolean bEncoder;
	private ByteArrayOutputStream byteOutput;
	private MM7Config mm7Config;
	private boolean bErrorFlag;
	private StringBuffer errorMessage;

	public SOAPEncoder() {
		reset();
	}

	public SOAPEncoder(MM7Config config) {
		reset();
		this.mm7Config = config;
	}

	public void reset() {
		this.mm7VaspReq = null;
		this.bMessageExist = false;
		this.bEncoder = false;
		this.byteOutput = null;
		this.mm7Config = null;
		this.bErrorFlag = false;
		this.errorMessage = new StringBuffer();
	}

	public void setMessage(MM7VASPReq mm7vaspreq) {
		this.mm7VaspReq = mm7vaspreq;
		this.bMessageExist = true;
	}

	public byte[] getMessage() {
		if (this.bEncoder) {
			return this.byteOutput.toByteArray();
		}
		return null;
	}

	public boolean getErrorFlag() {
		return this.bErrorFlag;
	}

	public StringBuffer getErrorMessage() {
		return this.errorMessage;
	}

	public void encodeMessage() throws SOAPEncodeException {
		if (!this.bMessageExist)
			throw new SOAPEncodeException(
					"No Multimedia Messages set in the encoder!");
		try {
			this.byteOutput = new ByteArrayOutputStream();
			this.bEncoder = false;
			StringBuffer sb = new StringBuffer();
			StringBuffer ContentBuffer = new StringBuffer();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

			if ((this.mm7VaspReq instanceof MM7SubmitReq)) {
				MM7SubmitReq req = (MM7SubmitReq) this.mm7VaspReq;
				if (req.isContentExist()) {
					sb.append("this is a multi-part message in MIME format\r\n");
					sb.append("\r\n");
					sb.append("\r\n");
					sb.append("---------------------------------------------------------NextPart_1\r\n");
					sb.append("Content-Type:text/xml;charset=\""
							+ this.mm7Config.getCharSet() + "\"\r\n");

					sb.append("Content-Transfer-Encoding:8bit\r\n");
					sb.append("Content-ID:</tnn-200102/mm7-vasp>\r\n");
					sb.append("\r\n");
					this.byteOutput.write(sb.toString().getBytes());
				}
			} else if ((this.mm7VaspReq instanceof MM7ReplaceReq)) {
				MM7ReplaceReq req = (MM7ReplaceReq) this.mm7VaspReq;
				if (req.isContentExist()) {
					sb.append("this is a multi-part message in MIME format\r\n");
					sb.append("\r\n");
					sb.append("\r\n");
					sb.append("---------------------------------------------------------NextPart_1\r\n");
					sb.append("Content-Type:text/xml;charset=\""
							+ this.mm7Config.getCharSet() + "\"\r\n");

					sb.append("Content-Transfer-Encoding:8bit\r\n");
					sb.append("Content-ID:</tnn-200102/mm7-vasp>\r\n");
					sb.append("\r\n");
					this.byteOutput.write(sb.toString().getBytes());
				}

			}

			Document doc = new Document(new Element("Envelope", "env",
					"http://schemas.xmlsoap.org/soap/envelope/"));
			Element root = doc.getRootElement();
			Element header = new Element("Header", "env",
					"http://schemas.xmlsoap.org/soap/envelope/");
			root.addContent(header);
			if (this.mm7VaspReq.isTransactionIDExist()) {
				Element trans = new Element("TransactionID", "mm7",
						"http://www.3gpp.org/ftp/Specs/archive/23_series/23.140/schema/REL-6-MM7-1-0");
				trans.setText(this.mm7VaspReq.getTransactionID());
				trans.addAttribute(new Attribute("mustUnderstand", "env",
						"http://schemas.xmlsoap.org/soap/envelope/", "1"));
				header.addContent(trans);
			} else {
				this.bErrorFlag = true;
				this.errorMessage.append("TransactionID为空;");
			}
			Element body = new Element("Body", "env",
					"http://schemas.xmlsoap.org/soap/envelope/");
			root.addContent(body);

			if ((this.mm7VaspReq instanceof MM7SubmitReq)) {
				MM7SubmitReq submitReq = (MM7SubmitReq) this.mm7VaspReq;
				Element submitEle = new Element("SubmitReq",
						"http://www.3gpp.org/ftp/Specs/archive/23_series/23.140/schema/REL-6-MM7-1-0");
				if (submitReq.isMM7VersionExist()) {
					submitEle.addContent(new Element("MM7Version")
							.setText(submitReq.getMM7Version()));
				} else {
					this.bErrorFlag = true;
					this.errorMessage.append("MM7Version 为空;");
				}
				if ((submitReq.isVASPIDExist()) || (submitReq.isVASIDExist())
						|| (submitReq.isSenderAddressExist())) {
					Element SenderIndent = new Element("SenderIdentification");
					if (submitReq.isVASPIDExist()) {
						SenderIndent.addContent(new Element("VASPID")
								.setText(submitReq.getVASPID()));
					} else {
						this.bErrorFlag = true;
						this.errorMessage.append("SP代码VASPID为空;");
					}
					if (submitReq.isVASIDExist()) {
						SenderIndent.addContent(new Element("VASID")
								.setText(submitReq.getVASID()));
					} else {
						this.bErrorFlag = true;
						this.errorMessage.append("服务代码VASID为空;");
					}
					if (submitReq.isSenderAddressExist())
						SenderIndent.addContent(new Element("SenderAddress")
								.setText(submitReq.getSenderAddress()));
					submitEle.addContent(SenderIndent);
				} else {
					this.bErrorFlag = true;
					this.errorMessage.append("SP代码VASPID和服务代码VASID均为空;");
				}
				if ((submitReq.isToExist()) || (submitReq.isCcExist())
						|| (submitReq.isBccExist())) {
					Element recipients = new Element("Recipients");
					if (submitReq.isToExist()) {
						Element toEle = new Element("To");
						List ToList = new ArrayList();
						ToList = submitReq.getTo();
						for (int i = 0; i < ToList.size(); i++) {
							String strto = (String) ToList.get(i);
							if (strto.indexOf('@') > 0)
								toEle.addContent(new Element("RFC2822Address")
										.setText(strto));
							else if (strto.indexOf('a') > 0)
								toEle.addContent(new Element("ShortCode")
										.setText(strto));
							else
								toEle.addContent(new Element("Number")
										.setText(strto));
						}
						recipients.addContent(toEle);
					}
					if (submitReq.isCcExist()) {
						Element ccEle = new Element("Cc");
						List CcList = new ArrayList();
						CcList = submitReq.getCc();
						for (int i = 0; i < CcList.size(); i++) {
							String strcc = (String) CcList.get(i);
							if (strcc.indexOf('@') > 0)
								ccEle.addContent(new Element("RFC2822Address")
										.setText(strcc));
							else
								ccEle.addContent(new Element("Number")
										.setText(strcc));
						}
						recipients.addContent(ccEle);
					}
					if (submitReq.isBccExist()) {
						Element bccEle = new Element("Bcc");
						List BccList = new ArrayList();
						BccList = submitReq.getBcc();
						for (int i = 0; i < BccList.size(); i++) {
							String strbcc = (String) BccList.get(i);
							if (strbcc.indexOf('@') > 0)
								bccEle.addContent(new Element("RFC2822Address")
										.setText(strbcc));
							else
								bccEle.addContent(new Element("Number")
										.setText(strbcc));
						}
						recipients.addContent(bccEle);
					}
					submitEle.addContent(recipients);
				} else {
					this.bErrorFlag = true;
					this.errorMessage
							.append("接收方地址To、抄送方地址Cc和密送方地址Bcc中至少需要有一个不为空;");
				}
				if (submitReq.isServiceCodeExist()) {
					submitEle.addContent(new Element("ServiceCode")
							.setText(submitReq.getServiceCode()));
				} else {
					this.bErrorFlag = true;
					this.errorMessage.append("业务代码ServiceCode为空;");
				}
				if (submitReq.isLinkedIDExist())
					submitEle.addContent(new Element("LinkedID")
							.setText(submitReq.getLinkedID()));
				if (submitReq.isMessageClassExist())
					submitEle.addContent(new Element("MessageClass")
							.setText(submitReq.getMessageClass()));
				if (submitReq.isTimeStampExist())
					submitEle.addContent(new Element("TimeStamp").setText(sdf
							.format(submitReq.getTimeStamp()) + "+08:00"));
				if (submitReq.isReplyChargingExist()) {
					Element replyEle = new Element("ReplyCharging");
					if (submitReq.isReplyChargingSizeExist()) {
						replyEle.addAttribute("replyChargingSize", Integer
								.toString(submitReq.getReplyChargingSize()));
					}
					if (submitReq.isReplyDeadlineExist()) {
						Date deadtime = new Date(System.currentTimeMillis()
								+ submitReq.getReplyDeadlineRelative());

						replyEle.addAttribute("replyDeadline",
								sdf.format(deadtime) + "+08:00");
					}
					if (submitReq.isReplyDeadlineAbsoluteExist())
						sb.append(" replyDeadline=\""
								+ sdf.format(submitReq
										.getReplyDeadlineAbsolute())
								+ "+08:00\"");
					submitEle.addContent(replyEle);
				}
				if (submitReq.isEarliestDeliveryTimeAbsolute()) {
					submitEle.addContent(new Element("EarliestDeliveryTime")
							.setText(sdf.format(submitReq
									.getEarliestDeliveryTimeAbsolute())
									+ "+08:00"));
				} else if (submitReq.isEarliestDeliveryTimeExist()) {
					Date dd = new Date(System.currentTimeMillis()
							+ submitReq.getEarliestDeliveryTimeRelative());

					submitEle.addContent(new Element("EarliestDeliveryTime")
							.setText(sdf.format(dd) + "+08:00"));
				}

				if (submitReq.isExpiryDateAbsolute()) {
					submitEle.addContent(new Element("ExpiryDate").setText(sdf
							.format(submitReq.getExpiryDateAbsolute())
							+ "+08:00"));
				} else if (submitReq.isExpiryDateExist()) {
					Date exdate = new Date(System.currentTimeMillis()
							+ submitReq.getExpiryDateRelative());

					submitEle.addContent(new Element("ExpiryDate").setText(sdf
							.format(exdate) + "+08:00"));
				}

				if (submitReq.isDeliveryReportExist()) {
					submitEle.addContent(new Element("DeliveryReport")
							.setText(Boolean.toString(submitReq
									.getDeliveryReport())));
				}
				if (submitReq.isReadReplyExist()) {
					submitEle
							.addContent(new Element("ReadReply")
									.setText(Boolean.toString(submitReq
											.getReadReply())));
				}
				if (submitReq.isPriorityExist()) {
					String strprior = "Normal";
					if (submitReq.getPriority() == 0)
						strprior = "Low";
					else if (submitReq.getPriority() == 1)
						strprior = "Normal";
					else if (submitReq.getPriority() == 2)
						strprior = "High";
					submitEle.addContent(new Element("Priority")
							.setText(strprior));
				}
				if (submitReq.isSubjectExist()) {
					submitEle.addContent(new Element("Subject")
							.setText(submitReq.getSubject()));
				}
				if (submitReq.isChargedPartyExist()) {
					String strCharged = "ThirdParty";
					if (submitReq.getChargedParty() == 0)
						strCharged = "Sender";
					else if (submitReq.getChargedParty() == 1)
						strCharged = "Recipient";
					else if (submitReq.getChargedParty() == 2)
						strCharged = "Both";
					else if (submitReq.getChargedParty() == 3)
						strCharged = "Neither";
					else if (submitReq.getChargedParty() == 4)
						strCharged = "ThirdParty";
					submitEle.addContent(new Element("ChargedParty")
							.setText(strCharged));
				}
				if (submitReq.isChargedPartyIDExist()) {
					submitEle.addContent(new Element("ChargedPartyID")
							.setText(submitReq.getChargedPartyID()));
				}
				if (submitReq.isDistributionIndicatorExist()) {
					submitEle.addContent(new Element("DistributionIndicator")
							.setText(Boolean.toString(submitReq
									.getDistributionIndicator())));
				}
				if (submitReq.isContentExist()) {
					body.addContent(submitEle);
					XMLOutputter outer = new XMLOutputter("", false,
							this.mm7Config.getCharSet());
					outer.output(doc, this.byteOutput);
					StringBuffer tempsb = new StringBuffer();
					tempsb.append("\r\n");
					tempsb.append("---------------------------------------------------------NextPart_1\r\n");
					MMContent parentContent = submitReq.getContent();
					if (parentContent.getContentType() != null) {
						String strSubType = "";
						String strtempID = "<START>";
						strSubType = parentContent.getContentType()
								.getSubType();
						if (strSubType.equalsIgnoreCase("related")) {
							if (parentContent.isMultipart()) {
								List tempSub = new ArrayList();
								tempSub = parentContent.getSubContents();
								for (int x = 0; x < tempSub.size(); x++) {
									MMContent tempCon = (MMContent) tempSub
											.get(x);
									if (tempCon.getContentType().getSubType()
											.equalsIgnoreCase("smil")) {
										if (tempCon.isContentIDExist()) {
											strtempID = tempCon.getContentID();
											break;
										}
										strtempID = "<START>";
										break;
									}
								}
							}
							tempsb.append("Content-Type:multipart/related;start=\""
									+ strtempID
									+ "\";type=\"application/smil\""
									+ ";boundary=\"--------------------------------------------------------SubPart_2\""
									+ "\r\n");
						} else {
							tempsb.append("Content-Type:"
									+ parentContent.getContentType()
											.getPrimaryType()
									+ "/"
									+ parentContent.getContentType()
											.getSubType()
									+ ";boundary=\"--------------------------------------------------------SubPart_2\""
									+ "\r\n");
						}

					} else {
						tempsb.append("Content-Type:multipart/mixed;boundary=\"--------------------------------------------------------SubPart_2\"\r\n");
					}
					if (parentContent.isContentIDExist())
						tempsb.append("Content-ID:"
								+ parentContent.getContentID() + "\r\n");
					else
						tempsb.append("Content-ID:<SaturnPics-01020930>\r\n");
					tempsb.append("Content-Transfer-Encoding:8bit\r\n");
					if (parentContent.isContentLocationExist())
						tempsb.append("Content-Location:"
								+ parentContent.getContentLocation() + "\r\n");
					tempsb.append("\r\n");
					this.byteOutput.write(tempsb.toString().getBytes(
							this.mm7Config.getCharSet()));
					ByteArrayOutputStream Subbaos = new ByteArrayOutputStream();
					if (parentContent.isMultipart()) {
						List subContent = new ArrayList();
						subContent = parentContent.getSubContents();
						for (int i = 0; i < subContent.size(); i++) {
							ContentBuffer = new StringBuffer();
							ContentBuffer
									.append("----------------------------------------------------------SubPart_2\r\n");
							MMContent content = (MMContent) subContent.get(i);
							if (content.getContentType() != null) {
								ContentBuffer
										.append("Content-Type:"
												+ content.getContentType()
														.getPrimaryType()
												+ "/"
												+ content.getContentType()
														.getSubType());

								if (content.getContentType().getPrimaryType()
										.equalsIgnoreCase("text")) {
									if ((content.getCharset() == null)
											|| (content.getCharset().length() == 0))
										ContentBuffer.append(";charset="
												+ this.mm7Config.getCharSet());
									else
										ContentBuffer.append(";charset="
												+ content.getCharset());
								}
								ContentBuffer.append("\r\n");
							} else if (content.isContentIDExist()) {
								String strContentID = content.getContentID();
								int index = strContentID.indexOf(".");
								String type = strContentID.substring(index + 1);
								type = type.toLowerCase();
								if (type.equals("txt")) {
									ContentBuffer
											.append("Content-Type:text/plain;charset="
													+ this.mm7Config
															.getCharSet()
													+ "\r\n");
								} else if (type.equals("jpg")) {
									ContentBuffer
											.append("Content-Type:image/jpeg\r\n");
								} else if (type.equals("gif")) {
									ContentBuffer
											.append("Content-Type:image/gif\r\n");
								}
							}

							ContentBuffer
									.append("Content-Transfer-Encoding:8bit\r\n");
							if (content.getContentType().getSubType()
									.equalsIgnoreCase("related")) {
								if (content.isContentIDExist()) {
									ContentBuffer.append("Content-ID:"
											+ content.getContentID() + "\r\n");
								} else {
									ContentBuffer
											.append("Content-ID:<START>\r\n");
								}

							} else if (content.isContentIDExist()) {
								ContentBuffer.append("Content-ID:"
										+ content.getContentID() + "\r\n");
							}

							if (content.isContentLocationExist()) {
								ContentBuffer
										.append("Content-Location:"
												+ content.getContentLocation()
												+ "\r\n");
							}

							ContentBuffer.append("\r\n");
							try {
								Subbaos.write(ContentBuffer.toString()
										.getBytes());
								Subbaos.write(content.getContent());
								Subbaos.write("\r\n".getBytes());
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						Subbaos.write("----------------------------------------------------------SubPart_2--\r\n"
								.getBytes());
						Subbaos.write("---------------------------------------------------------NextPart_1--\r\n"
								.getBytes());
						this.byteOutput.write(Subbaos.toByteArray());
					}
				} else {
					body.addContent(submitEle);
					XMLOutputter outer = new XMLOutputter("", false,
							this.mm7Config.getCharSet());
					outer.output(doc, this.byteOutput);
				}

			} else if ((this.mm7VaspReq instanceof MM7CancelReq)) {
				MM7CancelReq cancelReq = (MM7CancelReq) this.mm7VaspReq;
				Element cancelEle = new Element("CancelReq",
						"http://www.3gpp.org/ftp/Specs/archive/23_series/23.140/schema/REL-6-MM7-1-0");
				if (cancelReq.isMM7VersionExist()) {
					cancelEle.addContent(new Element("MM7Version")
							.setText(cancelReq.getMM7Version()));
				} else {
					this.bErrorFlag = true;
					this.errorMessage.append("MM7Version为空;");
				}
				if ((cancelReq.isVASPIDExist()) || (cancelReq.isVASIDExist())) {
					Element SenderIndent = new Element("SenderIdentification");
					if (cancelReq.isVASPIDExist())
						cancelEle.addContent(new Element("VASPID")
								.setText(cancelReq.getVASPID()));
					if (cancelReq.isVASIDExist())
						cancelEle.addContent(new Element("VASID")
								.setText(cancelReq.getVASID()));
					cancelEle.addContent(SenderIndent);
				}
				if (cancelReq.isSenderAddressExist())
					cancelEle.addContent(new Element("SenderAddress")
							.setText(cancelReq.getSenderAddress()));
				if (cancelReq.isMessageIDExist()) {
					cancelEle.addContent(new Element("MessageID")
							.setText(cancelReq.getMessageID()));
				} else {
					this.bErrorFlag = true;
					this.errorMessage.append("待取消的消息的标识符MessageID为空;");
				}
				body.addContent(cancelEle);
				XMLOutputter outer = new XMLOutputter("", false,
						this.mm7Config.getCharSet());
				outer.output(doc, this.byteOutput);
			} else if ((this.mm7VaspReq instanceof MM7ReplaceReq)) {
				MM7ReplaceReq replaceReq = (MM7ReplaceReq) this.mm7VaspReq;
				Element replaceEle = new Element("",
						"http://www.3gpp.org/ftp/Specs/archive/23_series/23.140/schema/REL-6-MM7-1-0");
				if (replaceReq.isMM7VersionExist()) {
					replaceEle.addContent(new Element("MM7Version")
							.setText(replaceReq.getMM7Version()));
				} else {
					this.bErrorFlag = true;
					this.errorMessage.append("MM7Version为空;");
				}
				if ((replaceReq.isVASPIDExist()) || (replaceReq.isVASIDExist())) {
					Element SenderIdent = new Element("SenderIdentification");
					if (replaceReq.isVASPIDExist())
						SenderIdent.addContent(new Element("VASPID")
								.setText(replaceReq.getVASPID()));
					if (replaceReq.isVASIDExist())
						SenderIdent.addContent(new Element("VASID")
								.setText(replaceReq.getVASID()));
				}
				if (replaceReq.isMessageIDExist()) {
					replaceEle.addContent(new Element("MessageID")
							.setText(replaceReq.getMessageID()));
				} else {
					this.bErrorFlag = true;
					this.errorMessage.append("被当前消息所替换的消息的标识符MessageID为空;");
				}
				if (replaceReq.isServiceCodeExist())
					replaceEle.addContent(new Element("ServiceCode")
							.setText(replaceReq.getServiceCode()));
				if (replaceReq.isTimeStampExist())
					replaceEle.addContent(new Element("TimeStamp").setText(sdf
							.format(replaceReq.getTimeStamp()) + "+08:00"));
				if (replaceReq.isEarliestDeliveryTimeAbsoluteExist()) {
					replaceEle.addContent(new Element("EarliestDeliveryTime")
							.setText(sdf.format(replaceReq
									.getEarliestDeliveryTimeAbsolute())
									+ "+08:00"));
				} else if (replaceReq.isEarliestDeliveryTimeExist()) {
					Date datetime = new Date(System.currentTimeMillis()
							+ replaceReq.getEarliestDeliveryTimeRelative());

					replaceEle.addContent(new Element("EarliestDeliveryTime")
							.setText(sdf.format(datetime) + "+08:00"));
				}

				if (replaceReq.isReadReplyExist()) {
					replaceEle
							.addContent(new Element("ReadReply")
									.setText(Boolean.toString(replaceReq
											.getReadReply())));
				}
				if (replaceReq.isDistributionIndicatorExist()) {
					replaceEle.addContent(new Element("DistributionIndicator")
							.setText(Boolean.toString(replaceReq
									.getDistributionIndicator())));
				}
				if (replaceReq.isContentExist()) {
					body.addContent(replaceEle);
					XMLOutputter outer = new XMLOutputter("", false,
							this.mm7Config.getCharSet());
					outer.output(doc, this.byteOutput);
					StringBuffer tempsb = new StringBuffer();
					tempsb.append("\r\n");
					tempsb.append("---------------------------------------------------------NextPart_1\r\n");
					MMContent parentContent = replaceReq.getContent();
					if (parentContent.getContentType() != null) {
						String strSubType = "";
						String strtempID = "<START>";
						strSubType = parentContent.getContentType()
								.getSubType();
						if (strSubType.equalsIgnoreCase("related")) {
							if (parentContent.isMultipart()) {
								List tempSub = new ArrayList();
								tempSub = parentContent.getSubContents();
								for (int x = 0; x < tempSub.size(); x++) {
									MMContent tempCon = (MMContent) tempSub
											.get(x);
									if (tempCon.getContentType().getSubType()
											.equalsIgnoreCase("smil")) {
										if (tempCon.isContentIDExist()) {
											strtempID = tempCon.getContentID();
											break;
										}
										strtempID = "<START>";
										break;
									}
								}
							}
							tempsb.append("Content-Type:multipart/related;start=\""
									+ strtempID
									+ "\";type=\"application/smil\""
									+ ";boundary=\"--------------------------------------------------------SubPart_2\""
									+ "\r\n");
						} else {
							tempsb.append("Content-Type:"
									+ parentContent.getContentType()
											.getPrimaryType()
									+ "/"
									+ parentContent.getContentType()
											.getSubType()
									+ ";boundary=\"--------------------------------------------------------SubPart_2\""
									+ "\r\n");
						}

					} else {
						tempsb.append("Content-Type:multipart/mixed;boundary=\"--------------------------------------------------------SubPart_2\"\r\n");
					}
					if (parentContent.isContentIDExist())
						tempsb.append("Content-ID:"
								+ parentContent.getContentID() + "\r\n");
					else
						tempsb.append("Content-ID:<SaturnPics-01020930>\r\n");
					tempsb.append("Content-Transfer-Encoding:8bit\r\n");
					if (parentContent.isContentLocationExist())
						tempsb.append("Content-Location:"
								+ parentContent.getContentLocation() + "\r\n");
					tempsb.append("\r\n");
					this.byteOutput.write(tempsb.toString().getBytes(
							this.mm7Config.getCharSet()));
					ByteArrayOutputStream Subbaos = new ByteArrayOutputStream();
					if (parentContent.isMultipart()) {
						List subContent = new ArrayList();
						subContent = parentContent.getSubContents();
						for (int i = 0; i < subContent.size(); i++) {
							ContentBuffer = new StringBuffer();
							ContentBuffer
									.append("----------------------------------------------------------SubPart_2\r\n");
							MMContent content = (MMContent) subContent.get(i);
							if (content.getContentType() != null) {
								ContentBuffer
										.append("Content-Type:"
												+ content.getContentType()
														.getPrimaryType()
												+ "/"
												+ content.getContentType()
														.getSubType());

								if (content.getContentType().getPrimaryType()
										.equalsIgnoreCase("text")) {
									if ((content.getCharset() == null)
											|| (content.getCharset().length() == 0))
										ContentBuffer.append(";charset="
												+ this.mm7Config.getCharSet());
									else
										ContentBuffer.append(";charset="
												+ content.getCharset());
								}
								ContentBuffer.append("\r\n");
							} else if (content.isContentIDExist()) {
								String strContentID = content.getContentID();
								int index = strContentID.indexOf(".");
								String type = strContentID.substring(index + 1);
								type = type.toLowerCase();
								if (type.equals("txt")) {
									ContentBuffer
											.append("Content-Type:text/plain;charset="
													+ this.mm7Config
															.getCharSet()
													+ "\r\n");
								} else if (type.equals("jpg")) {
									ContentBuffer
											.append("Content-Type:image/jpeg\r\n");
								} else if (type.equals("gif")) {
									ContentBuffer
											.append("Content-Type:image/gif\r\n");
								}
							}

							ContentBuffer
									.append("Content-Transfer-Encoding:8bit\r\n");
							if (content.getContentType().getSubType()
									.equalsIgnoreCase("related")) {
								if (content.isContentIDExist()) {
									ContentBuffer.append("Content-ID:"
											+ content.getContentID() + "\r\n");
								} else {
									ContentBuffer
											.append("Content-ID:<START>\r\n");
								}

							} else if (content.isContentIDExist()) {
								ContentBuffer.append("Content-ID:"
										+ content.getContentID() + "\r\n");
							}

							if (content.isContentLocationExist()) {
								ContentBuffer
										.append("Content-Location:"
												+ content.getContentLocation()
												+ "\r\n");
							}

							ContentBuffer.append("\r\n");
							try {
								Subbaos.write(ContentBuffer.toString()
										.getBytes());
								Subbaos.write(content.getContent());
								Subbaos.write("\r\n\r\n".getBytes());
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						Subbaos.write("----------------------------------------------------------SubPart_2--\r\n"
								.getBytes());
						Subbaos.write("---------------------------------------------------------NextPart_1--\r\n"
								.getBytes());
						this.byteOutput.write(Subbaos.toByteArray());
					}
				} else {
					body.addContent(replaceEle);
					XMLOutputter outer = new XMLOutputter("", false,
							this.mm7Config.getCharSet());
					outer.output(doc, this.byteOutput);
				}

			}

			this.bEncoder = true;
		} catch (Exception e) {
			this.bErrorFlag = true;
			this.errorMessage.append("异常：" + e);
		}
	}
}