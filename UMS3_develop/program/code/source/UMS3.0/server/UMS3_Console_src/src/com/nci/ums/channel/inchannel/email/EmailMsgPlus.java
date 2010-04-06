/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nci.ums.channel.inchannel.email;

import java.io.Serializable;

import com.nci.ums.v3.message.basic.Participant;

/**
 * 是Email回复信息的附加对象，一般邮箱或邮件客户端回复时缺少必须要的属性，UMS通过EmailMsgPlus来获取这些属性
 * @author Qil.Wong
 */
public class EmailMsgPlus implements Serializable {

	private static final long serialVersionUID = -9026363251573441058L;
	//email消息的接收方服务id
    private String idOfReceiveService="";
    //外部应用发送过来的消息的应用序列号
    private String appSerialNO="";
    //真正的消息内容
    private String realMsgContent="";
    //消息发送者
    private Participant sender;
    
    public EmailMsgPlus(){}
    
    public EmailMsgPlus(String idOfReceiveService,String appSerialNO,String realMsgContent){
    	this.idOfReceiveService = idOfReceiveService;
    	this.appSerialNO = appSerialNO;
    	this.realMsgContent = realMsgContent;
    }
    
    public EmailMsgPlus(String idOfReceiveService,String appSerialNO,String realMsgContent,Participant sender){
    	this.idOfReceiveService = idOfReceiveService;
    	this.appSerialNO = appSerialNO;
    	this.realMsgContent = realMsgContent;
    	this.sender = sender;
    }
    
	/**
	 * @return the idOfReceiveService
	 */
	public String getIdOfReceiveService() {
		return idOfReceiveService;
	}
	/**
	 * @param idOfReceiveService the idOfReceiveService to set
	 */
	public void setIdOfReceiveService(String idOfReceiveService) {
		this.idOfReceiveService = idOfReceiveService;
	}
	/**
	 * @return the appSerialNO
	 */
	public String getAppSerialNO() {
		return appSerialNO;
	}
	/**
	 * @param appSerialNO the appSerialNO to set
	 */
	public void setAppSerialNO(String appSerialNO) {
		this.appSerialNO = appSerialNO;
	}
	/**
	 * @return the realMsgContent
	 */
	public String getRealMsgContent() {
		return realMsgContent;
	}
	/**
	 * @param realMsgContent the realMsgContent to set
	 */
	public void setRealMsgContent(String realMsgContent) {
		this.realMsgContent = realMsgContent;
	}

	public String toString(){
		return new StringBuffer("AppSerialNO:").append(this.appSerialNO)
		.append(",idOfReceiveService:").append(this.idOfReceiveService)
		.append(",realMsgContent:").append(this.realMsgContent).toString();
	}

	/**
	 * @return the sender
	 */
	public Participant getSender() {
		return sender;
	}

	/**
	 * @param sender the sender to set
	 */
	public void setSender(Participant sender) {
		this.sender = sender;
	}
	

}
