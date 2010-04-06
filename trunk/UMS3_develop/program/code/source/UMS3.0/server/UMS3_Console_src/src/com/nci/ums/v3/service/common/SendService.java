
package com.nci.ums.v3.service.common;


/**
 * <p>Title: DefaultEnterpriseUserInfo.java</p>
 * <p>Description:
 *    Interface of UMS web service. Provide three methods for applications
 *    to send message.
 * </p>
 * <p>Copyright: 2007 Hangzhou NCI System Engineering£¬ Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering£¬ Ltd.</p>
 * @author Qil.Wong
 * Created in 2007.09.22, since UMS3.0
 * @version 1.0
 */
public interface SendService {

	/**
	 * Message sending
	 * @param umsMsgs array of UMSMsg objects
	 * @return result of message sending
	 */
//	public String send(BasicMsg[] basicMsgs);

	/**
	 * Message sending
	 * @param umsMsgXML each umsMsgXML is one UMSMsg object xml string
	 * @return result of message sending
	 */
//	public String send(String[] basicMsgXML);

	/**
	 * Message sending with acknowledge return 
	 * @param appid
	 * @param passowrd
	 * @param umsMsgsXML xml string of array of UMSMsg objects
	 * @return result of message sending
	 */
	public String sendWithAck(String appid, String password, String basicMsgsXML);
	
	/**
	 * Message sending without acknowledge
	 * @param appid
	 * @param password
	 * @param basicMsgsXML
	 */
	public void sendWithoutAck(String appid, String password, String basicMsgsXML);

}
