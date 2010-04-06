package com.nci.ums.channel.outchannel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

import com.nci.ums.channel.channelinfo.LCS_V3DataLockFlag;
import com.nci.ums.channel.channelinfo.LCS_V3ThreadLockFlag;
import com.nci.ums.channel.channelinfo.LockFlag;
import com.nci.ums.channel.channelinfo.MediaInfo;
import com.nci.ums.util.Res;
import com.nci.ums.util.DynamicUMSStreamReader;
import com.nci.ums.v3.message.UMSMsg;
import com.nci.ums.v3.message.basic.BasicMsg;
import com.nci.ums.v3.message.basic.Participant;

public class LCSOutChannel_V3 extends OutChannel {

	private LCS_V3DataLockFlag lcsDataLock;
	private LCS_V3ThreadLockFlag lcsThreadLock;

	private String agentWsdl = null;

	private String nameSpace = null;

	private String methodName = null;

	private Call call = null;

	public LCSOutChannel_V3(MediaInfo lcsMediaInfo) {
		super(lcsMediaInfo);
		lcsDataLock = LCS_V3DataLockFlag.getInstance();
		lcsThreadLock = LCS_V3ThreadLockFlag.getInstance();
		try {
			loadAgentInfo();
		} catch (IOException e) {
			Res.log(Res.ERROR, "装载LCS属性文件失败.");
			Res.logExceptionTrace(e);
		} catch (ServiceException e) {
			Res.log(Res.ERROR, "初始化LCS Web服务失败.");
			Res.logExceptionTrace(e);
		}
	}

	private void loadAgentInfo() throws IOException, ServiceException {
		InputStream is = new DynamicUMSStreamReader()
				.getInputStreamFromFile("/resources/lcs.props");
		Properties prop = new Properties();
		prop.load(is);
		is.close();
		agentWsdl = prop.getProperty("agentWsdl");
		nameSpace = prop.getProperty("nameSpace");
		methodName = prop.getProperty("methodName");
		Service service = new Service();
		call = (Call) service.createCall();
		call.setOperationName(new QName(nameSpace, methodName));
		call.setTargetEndpointAddress(agentWsdl);
	}

	public void sendViaChannel(UMSMsg[] msgs) {
		try {
			for (int i = 0; i < msgs.length; i++) {
				if (msgs[i] == null) {
					break;
				}
				UMSMsg[] msgPlus = unpack(msgs[i]);
				for (int n = 0; n < msgPlus.length; n++) {
					// String ret = (String) call.invoke(new Object[] {
					// msgPlus[n].getBasicMsg().getSender()
					// .getParticipantID(),
					// msgPlus[n].getBasicMsg().getReceivers()[0]
					// .getParticipantID(),
					// msgPlus[n].getBasicMsg().getMsgContent()
					// .getContent() });
					// if (ret.equals("-1")) {// 发送失败
					// OpData(5891, msgPlus[n]);
					// } else if (ret.equals("0")) {// 发送成功
					// OpData(0, msgPlus[n]);
					// }
					String xml = createLCSXml(msgs[i]);

					String ret = (String) call.invoke(new Object[] { xml });
					if (ret.equalsIgnoreCase("SUCC")) {
						OpData(0, msgPlus[n]);
					} else {
						OpData(5891, msgPlus[n]);
					}
				}
			}
		} catch (Exception e) {
			Res.logExceptionTrace(e);
		}

	}
	
	public String getMsgFmtFile() {
		return "/resources/MsgFmt_LCS.xml";
	}

	private String createLCSXml(UMSMsg msg) {
		StringBuffer invokeDoc = new StringBuffer("");
		invokeDoc.append("<?xml version=\"1.0\" encoding=\"gb2312\"?>");
		invokeDoc.append("<message>");
		invokeDoc.append("	<sender>");
		invokeDoc.append("		<property name=\"fromUserId\" value=\"").append(
				msg.getBasicMsg().getSender().getParticipantID())
				.append("\"/>");
		invokeDoc.append("		<property name=\"fromUserName\" value=\"").append(
				"").append("\"/>");
		invokeDoc.append("		<property name=\"fromDeptName\" value=\"").append(
				"").append("\"/>");
		invokeDoc.append("		<property name=\"sendDateTime\" value=\"").append(
				getLcsSendDate(msg)).append("\"/>");
		invokeDoc.append("		<property name=\"replyTo\" value=\"").append(
				msg.getBasicMsg().getReplyDestination()).append("\"/>");
		invokeDoc.append("	</sender>");
		invokeDoc.append("	<receivers defaultChannelType=\"IM\">");
		invokeDoc.append("		<!--直送:1..n-->");
		invokeDoc.append("		<receiver type=\"to\">");
		invokeDoc.append(
				"			<property name=\"UID\" channelType=\"IM\" value=\"")
				.append(msg.getBasicMsg().getReceivers()[0].getParticipantID())
				.append("\"/>");
		invokeDoc.append(
				"			<property name=\"EMAIL\" channelType=\"EMAIL\" value=\"")
				.append(msg.getBasicMsg().getReceivers()[0].getParticipantID())
				.append("\"/>");
		invokeDoc.append("		</receiver>");
		invokeDoc.append("	</receivers>");
		invokeDoc.append("	<content>");
		invokeDoc.append("		<!--消息标题-->");
		invokeDoc.append("		<subject>协同系统信息</subject>");
		invokeDoc.append("		<!--消息描述-->");
		invokeDoc.append("		<description>").append(
				msg.getBasicMsg().getMsgContent().getContent()).append(
				"</description>");
		invokeDoc.append("	</content>");
		invokeDoc.append("</message>");
		return invokeDoc.toString();
	}

	private String getLcsSendDate(UMSMsg msg) {
		try {
			StringBuffer sb = new StringBuffer();
			String submitDate = msg.getBasicMsg().getSubmitDate();
			String submitTime = msg.getBasicMsg().getSubmitTime();
			sb.append(submitDate.substring(0, 4)).append("-").append(
					submitDate.substring(4, 6)).append("-").append(
					submitDate.substring(6, 8)).append(" ").append(
					submitTime.substring(0, 2)).append(":").append(
					submitTime.substring(2, 4)).append(":").append(
					submitTime.substring(4, 6));
			return sb.toString();
		} catch (Exception e) {
			return "";
		}
	}

	private UMSMsg[] unpack(UMSMsg msg) {
		UMSMsg[] msgPlus = new UMSMsg[msg.getBasicMsg().getReceivers().length];
		for (int i = 0; i < msg.getBasicMsg().getReceivers().length; i++) {
			msgPlus[i] = new UMSMsg();
			BasicMsg basicMsg = new BasicMsg();
			basicMsg.setAck(msg.getBasicMsg().getAck());
			basicMsg.setAppSerialNO(msg.getBasicMsg().getAppSerialNO());
			basicMsg.setCompanyID(msg.getBasicMsg().getCompanyID());
			basicMsg.setContentMode(msg.getBasicMsg().getContentMode());
			basicMsg.setDirectSendFlag(msg.getBasicMsg().getDirectSendFlag());
			basicMsg.setFeeServiceNO(msg.getBasicMsg().getFeeServiceNO());
			basicMsg.setInvalidDate(msg.getBasicMsg().getInvalidDate());
			basicMsg.setInvalidTime(msg.getBasicMsg().getInvalidTime());
			basicMsg.setMediaID(msg.getBasicMsg().getMediaID());
			basicMsg.setMsgAttachment(msg.getBasicMsg().getMsgAttachment());
			basicMsg.setMsgContent(msg.getBasicMsg().getMsgContent());
			basicMsg.setNeedReply(msg.getBasicMsg().getNeedReply());
			basicMsg.setPriority(msg.getBasicMsg().getPriority());
			basicMsg.setReceivers(new Participant[] { msg.getBasicMsg()
					.getReceivers()[i] });
			basicMsg.setReplyDestination(msg.getBasicMsg()
					.getReplyDestination());
			basicMsg.setSender(msg.getBasicMsg().getSender());
			basicMsg.setServiceID(msg.getBasicMsg().getServiceID());
			basicMsg.setSetSendDate(msg.getBasicMsg().getSetSendDate());
			basicMsg.setSetSendTime(msg.getBasicMsg().getSetSendTime());
			basicMsg.setSubmitDate(msg.getBasicMsg().getSubmitDate());
			basicMsg.setSubmitTime(msg.getBasicMsg().getSubmitTime());
			basicMsg.setTimeSetFlag(msg.getBasicMsg().getTimeSetFlag());
			basicMsg.setUmsFlag(msg.getBasicMsg().getUmsFlag());
			msgPlus[i].setBasicMsg(basicMsg);
			msgPlus[i].setBatchMode(msg.getBatchMode());
			msgPlus[i].setBatchNO(msg.getBatchNO());
			msgPlus[i].setDoCount(msg.getDoCount());
			msgPlus[i].setErrMsg(msg.getErrMsg());
			msgPlus[i].setFeeInfo(msg.getFeeInfo());
			msgPlus[i].setFinishDate(msg.getFinishDate());
			msgPlus[i].setFinishTime(msg.getFinishTime());
			msgPlus[i].setMsgID(msg.getMsgID());
			msgPlus[i].setRep(msg.getRep());
			msgPlus[i].setReturnCode(msg.getReturnCode());
			msgPlus[i].setSequenceNO(new int[] { msg.getSequenceNO()[i] });
			msgPlus[i].setSerialNO(msg.getSerialNO());
			msgPlus[i].setSmsMsgSeq(msg.getSmsMsgSeq());
			msgPlus[i].setStatusFlag(msg.getStatusFlag());
		}
		return msgPlus;
	}

	public boolean isLocked() {
		return this.lcsDataLock.getLockFlag()
				|| this.lcsThreadLock.getLockFlag();
	}

	public void setLocked(boolean flag) {
		this.lcsThreadLock.setLockFlag(flag);
	}
	
	public LockFlag getDataLockFlag(){
		return lcsDataLock;
	}

}
