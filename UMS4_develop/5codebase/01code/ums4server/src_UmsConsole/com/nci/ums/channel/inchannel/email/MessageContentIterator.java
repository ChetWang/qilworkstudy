/*
 * MessageContentIterator.java
 *
 * Created on 2007-10-12, 16:43:07
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nci.ums.channel.inchannel.email;

import java.io.IOException;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;

import com.nci.ums.util.Res;
import javax.mail.MessageRemovedException;

/**
 * 
 * @author Qil.Wong
 */
public class MessageContentIterator {

	StringBuffer contentText = new StringBuffer();

	public MessageContentIterator() {
	}

	public String getContentText(Part mm) throws MessageRemovedException{
		try {
//			String contentType = mm.getContentType();
//			 int nameindex = contentType.indexOf("name");
//			 boolean conname = false;
//			 if (nameindex == -1) {
//			 conname = true;
//			 }
			 // System.out.println("CONTENTTYPE: " + contentType);
//			 if ((mm.isMimeType("text/plain") || mm.isMimeType("text/html"))
//			 && !conname) {
//			 contentText.append((String) mm.getContent());
//			 }
			if (mm.isMimeType("text/plain") || mm.isMimeType("text/html")) {
				try {
					contentText.append((String) mm.getContent());
				} catch (ClassCastException cce) {
					Res.log(Res.ERROR, "接收的邮件消息转换出现错误:"+cce.getMessage());
                                        Res.logExceptionTrace(cce);
				}
			} else if (mm.isMimeType("multipart/*")) {
				Multipart multipart = (Multipart) mm.getContent();
				System.out.println(multipart.toString());
				int count = multipart.getCount();
				for (int i = 0; i < count; i++) {
					if (multipart.getBodyPart(i).getDisposition() == null) {
						String bodyContent = "";
						try {
							bodyContent = (String) multipart.getBodyPart(i)
									.getContent();
						} catch (ClassCastException cce) {
							Res.log(Res.ERROR, "接收的邮件消息转换出现错误");                     
						}
						if (bodyContent.indexOf("<!DOCTYPE HTML") < 0)
							contentText.append(bodyContent);
					}
				}
			} else if (mm.isMimeType("message/rfc822")) {
				getContentText((Part) mm.getContent());
			} else {
			}
		}catch(MessageRemovedException e){
                    throw e;
                } 
                catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return contentText.toString();
	}
}