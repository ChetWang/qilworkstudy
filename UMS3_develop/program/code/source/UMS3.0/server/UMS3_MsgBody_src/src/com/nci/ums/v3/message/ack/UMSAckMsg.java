package com.nci.ums.v3.message.ack;

public class UMSAckMsg {

	private String msgid;
	private String batchNO;
	private int serialNO;
	private int sequenceNO;
	private String status;
	private String seqKey;

	public UMSAckMsg() {
	}

	public UMSAckMsg(String batchNO, int serialNO, int sequenceNO,
			String seqKey, String status) {
		msgid = batchNO + "-" + serialNO + "-" + sequenceNO;
		this.status = status;
		this.batchNO = batchNO;
		this.serialNO = serialNO;
		this.sequenceNO = sequenceNO;
		this.seqKey = seqKey;
	}

	public String getMsgid() {
		return msgid;
	}

	public String getStatus() {
		return status;
	}

	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBatchNO() {
		return batchNO;
	}

	public int getSerialNO() {
		return serialNO;
	}

	public int getSequenceNO() {
		return sequenceNO;
	}

	public void setBatchNO(String batchNO) {
		this.batchNO = batchNO;
	}

	public void setSerialNO(int serialNO) {
		this.serialNO = serialNO;
	}

	public void setSequenceNO(int sequenceNO) {
		this.sequenceNO = sequenceNO;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("<msg ").append("id=\"").append(getMsgid()).append(
				"\" status=\"").append(getStatus()).append("\" />");
		return sb.toString();
	}

	public String getSeqKey() {
		return seqKey;
	}

	public void setSeqKey(String seqKey) {
		this.seqKey = seqKey;
	}
}
