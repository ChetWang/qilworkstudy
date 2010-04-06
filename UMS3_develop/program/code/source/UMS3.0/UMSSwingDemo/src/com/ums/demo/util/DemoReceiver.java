/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ums.demo.util;

import com.nci.ums.channel.inchannel.email.EmailMsgPlus;
import com.nci.ums.v3.message.UMSMsg;
import com.nci.ums.v3.message.basic.BasicMsg;
import com.nci.ums.v3.message.basic.MsgAttachment;
import com.nci.ums.v3.message.basic.MsgContent;
import com.nci.ums.v3.message.basic.Participant;
import com.nci.ums.v3.service.client.DuplicateServiceException;
import com.nci.ums.v3.service.client.ReceiveEvent;
import com.nci.ums.v3.service.client.UMSReceiver_Socket;
import com.thoughtworks.xstream.XStream;
import java.io.IOException;
import javax.swing.JTextArea;

/**
 *
 * @author Administrator
 */
public class DemoReceiver extends UMSReceiver_Socket {

    JTextArea component;
    XStream xstream = new XStream();

    public DemoReceiver(String propfile, JTextArea component) throws IOException, ClassNotFoundException,DuplicateServiceException {
        super(propfile);
        this.component = component;
        xstream.setClassLoader(getClass().getClassLoader());
        xstream.alias("UMSMsg", UMSMsg.class);
        xstream.alias("BasicMsg", BasicMsg.class);
        xstream.alias("MsgAttachment", MsgAttachment.class);
        xstream.alias("MsgContent", MsgContent.class);
        xstream.alias("Participant", Participant.class);
        xstream.alias("EmailMsgPlus", EmailMsgPlus.class);
    }

    public void onUMSMsg(ReceiveEvent evt) {
        BasicMsg[] basics = (BasicMsg[]) xstream.fromXML(evt.getReceivedObj().toString());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < basics.length; i++) {
            sb.append("发送者：" + basics[i].getSender().getParticipantID() + "\r\n");
            sb.append("回复对应应用号:" + basics[i].getAppSerialNO() + "\r\n");
            sb.append("接收服务ID:" + basics[i].getServiceID() + "\r\n");
            sb.append("消息正文:" + basics[i].getMsgContent().getContent() + "\r\n");
            sb.append("\r\n\r\n");
        }
        component.setText(sb.toString());
    }
}
