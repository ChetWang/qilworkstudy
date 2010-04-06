package com.nci.ums.v3.message;

import java.io.Serializable;

import com.nci.ums.v3.fee.FeeBean;
import com.nci.ums.v3.message.basic.BasicMsg;


/**
 * <p>Title: UMSMsg.java</p>
 * <p>Description:
 *    The main body of a UMS message, including the <b>BasicMsg</B> to be sent, and other descriptive attributes,
 *    such as status,fee,limit time and date,etc.
 * </p>
 * <p>Copyright: 2007 Hangzhou NCI System Engineering， Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering， Ltd.</p>
 * @author Qil.Wong
 * Created in 2007.09.19, since UMS3.0
 * @version 1.0
 */
public class UMSMsg implements Serializable,Cloneable{
	
	private static final long serialVersionUID = -5044937963288746844L;
	/**
	 * 基本的消息体，这个对象是发生者必须提供的对象
	 */
	private BasicMsg basicMsg = new BasicMsg();
	/**
	 * 消息的批号，是当前时间值，格式为“yyyyMMddHHmmss”
	 */
	private String batchNO = "";
	/**
	 * 消息的序列号，系统会自动给一个消息相应的序号
	 */
	private int serialNO = -1;	
	/**
	 * 消息的顺序编号集合，集合的元素与每个BasicMsg中的接收者数量对应，表示每个接收者对应一个消息
	 */
	private int[] sequenceNO = new int[]{-1};
	/**
	 * 短消息的序号，只在短消息中使用，如果是其它类型的消息，则不需要这个值
	 */
	private int smsMsgSeq;
	/**
	 * 批量模式，如果是单批，则为0，多批则为1
	 */
	private int batchMode = 0;
	/**
	 * 消息的状态标识，0为有效，1为无效
	 */
	private int statusFlag = -1;
	/**
	 * Times of UMS trying to send the message via current channel
	 * 当前渠道消息发送的次数
	 */
	private int rep = -1;
	/**
	 * Total times of UMS trying to send the message
	 * 消息发送总次数
	 */
	private int doCount = -1;
	/**
	 * 消息发送结果返回值
	 */
	private String returnCode = "";
	/**
	 * 消息发送结果返回描述
	 */
	private String errMsg = "";
	/**
	 * UMS的消息发送日期，与消息的提交日期不同
	 */
	private String finishDate = "";
	/**
	 * UMS的消息发送时间，与消息的提交时间不同
	 */
	private String finishTime = "";
	/**
	 * 消息ID
	 */
	private String msgID = "";
//	private int fee = -1;
//	private int feeType = -1;
	/**
	 * 费用对象，每个消息的费用情况说明
	 */
	private FeeBean feeInfo = new FeeBean();

	/**
	 * Normal flag,message could be sent
	 */
	public static final int UMSMSG_STATUS_VALID = 0;
	/**
	 * Abnormal flag, message could not be sent at this moment
	 */
	public static final int UMSMSG_STATUS_INVALID = 1;
	/**
	 * The channel of directly sent message is stopped.
	 */
	public static final int UMSMSG_STATUS_MEDIA_STOPPED=2;
	
	/**
	 * Fee flag, the message will cost somewhat fee;
	 */
	public static final int UMSMSG_FEE_SELFPAY = 1;
	/**
	 * Fee flag, the message will be free;
	 */
	public static final int UMSMSG_FEE_FREE = 0;
	/**
	 * Fee flag, the message will be payed by some third party;
	 */
	public static final int UMSMSG_FEE_THIRDPARTY = 2;
	
	/**
	 * At one message sending step, only send one message
	 */
	public static final int UMSMSG_BATCHMODE_SINGLE = 0;
	
	/**
	 * At one message sending step, send more than one messages
	 */
	public static final int UMSMSG_BATCHMODE_MULTIPLE = 1;

	public UMSMsg(){
		
	}


	/**
	 * get the basic message sent from a application service
	 * @return the basicMsg
	 */
	public BasicMsg getBasicMsg() {
		return basicMsg;
	}


	/**
	 * set the value of basic message sent from a application service
	 * @param basicMsg the basicMsg to set
	 */
	public void setBasicMsg(BasicMsg basicMsg) {
		this.basicMsg = basicMsg;
	}


	/**
	 * @return the batchNO
	 */
	public String getBatchNO() {
		return batchNO;
	}


	/**
	 * @param batchNO the batchNO to set
	 */
	public void setBatchNO(String batchNO) {
		this.batchNO = batchNO;
	}


	/**
	 * @return the serialNO
	 */
	public int getSerialNO() {
		return serialNO;
	}


	/**
	 * @param serialNO the serialNO to set
	 */
	public void setSerialNO(int serialNO) {
		this.serialNO = serialNO;
	}

        /**
         * get the value of <b>total</b> times of UMS trying to send the message, besides current
         * channel, it includes other channels which the message has been sent with
	 * @return the doCount total times of UMS trying to send the message
	 */
	public int getDoCount() {
		return doCount;
	}


	/**
         * set the value of <b>total</b> times of UMS trying to send the message, besides current
         * channel, it includes other channels which the message has been sent with.
	 * @param doCount the doCount to set, total times of UMS trying to send the message
	 */
	public void setDoCount(int doCount) {
		this.doCount = doCount;
	}


	/**
	 * @return the returnCode
	 */
	public String getReturnCode() {
		return returnCode;
	}


	/**
	 * @param returnCode the returnCode to set
	 */
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}


//	/**
//	 * @return the fee
//	 */
//	public int getFee() {
//		return fee;
//	}


//	/**
//	 * @param fee the fee to set
//	 */
//	public void setFee(int fee) {
//		this.fee = fee;
//	}
//
//
//	/**
//	 * @return the feeType
//	 */
//	public int getFeeType() {
//		return feeType;
//	}


//	/**
//	 * @param feeType the feeType to set
//	 */
//	public void setFeeType(int feeType) {
//		this.feeType = feeType;
//	}


	/**
         * get the value of count of message sending via <b>current</b> channel
	 * @return the count of message sending via <b>current</b> channel 
	 */
	public int getRep() {
		return rep;
	}


	/**
         * set the value of count of message sending via <b>current</b> channel
	 * @param rep the value of count of message sending via <b>current</b> channel
	 */
	public void setRep(int rep) {
		this.rep = rep;
	}


	/**
	 * get the sequence array to a UMSMsg, each sequenceNO maps a unique receiver.
	 * @return the sequenceNO
	 */
	public int[] getSequenceNO() {
		return sequenceNO;
	}


	/**
	 * set the value of sequence array to a UMSMsg, each sequenceNO maps a unique receiver.
	 * @param sequenceNO the sequenceNO to set
	 */
	public void setSequenceNO(int[] sequenceNO) {
		this.sequenceNO = sequenceNO;
	}


	/**
	 * @return the batchMode
	 */
	public int getBatchMode() {
		return batchMode;
	}


	/**
	 * @param batchMode the batchMode to set
	 */
	public void setBatchMode(int batchMode) {
		this.batchMode = batchMode;
	}


	/**
	 * @return the statusFlag
	 */
	public int getStatusFlag() {
		return statusFlag;
	}


	/**
	 * @param statusFlag the statusFlag to set
	 */
	public void setStatusFlag(int statusFlag) {
		this.statusFlag = statusFlag;
	}


	/**
	 * @return the errMsg
	 */
	public String getErrMsg() {
		return errMsg;
	}


	/**
	 * @param errMsg the errMsg to set
	 */
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}


	/**
	 * @return the finishDate
	 */
	public String getFinishDate() {
		return finishDate;
	}


	/**
	 * @param finishDate the finishDate to set
	 */
	public void setFinishDate(String finishDate) {
		this.finishDate = finishDate;
	}


	/**
	 * @return the finishTime
	 */
	public String getFinishTime() {
		return finishTime;
	}


	/**
	 * @param finishTime the finishTime to set
	 */
	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}


	/**
	 * @return the msgID
	 */
	public String getMsgID() {
		return msgID;
	}


	/**
	 * @param msgID the msgID to set
	 */
	public void setMsgID(String msgID) {
		this.msgID = msgID;
	}


	/**
	 * @return the feeInfo
	 */
	public FeeBean getFeeInfo() {
		return feeInfo;
	}


	/**
	 * @param feeInfo the feeInfo to set
	 */
	public void setFeeInfo(FeeBean feeInfo) {
		this.feeInfo = feeInfo;
	}

	public Object clone(){
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String args[]){
		UMSMsg msg = new UMSMsg();
		UMSMsg msg1 = (UMSMsg)msg.clone();
		UMSMsg msg2 = (UMSMsg)msg.clone();
		msg.setBatchNO("aa");
		System.out.println(msg.getBatchNO());
		System.out.println(msg1.getBatchNO());
		System.out.println(msg2.getBatchNO());
	}


	/**
	 * @return the smsMsgSeq
	 */
	public int getSmsMsgSeq() {
		return smsMsgSeq;
	}


	/**
	 * @param smsMsgSeq the smsMsgSeq to set
	 */
	public void setSmsMsgSeq(int smsMsgSeq) {
		this.smsMsgSeq = smsMsgSeq;
	}

    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.basicMsg != null ? this.basicMsg.hashCode() : 0);
        hash = 53 * hash + (this.batchNO != null ? this.batchNO.hashCode() : 0);
        hash = 53 * hash + this.serialNO;
        hash = 53 * hash + (this.sequenceNO != null ? this.sequenceNO.hashCode() : 0);
        hash = 53 * hash + this.smsMsgSeq;
        hash = 53 * hash + this.batchMode;
        hash = 53 * hash + this.statusFlag;
        hash = 53 * hash + this.rep;
        hash = 53 * hash + this.doCount;
        hash = 53 * hash + (this.returnCode != null ? this.returnCode.hashCode() : 0);
        hash = 53 * hash + (this.errMsg != null ? this.errMsg.hashCode() : 0);
        hash = 53 * hash + (this.finishDate != null ? this.finishDate.hashCode() : 0);
        hash = 53 * hash + (this.finishTime != null ? this.finishTime.hashCode() : 0);
        hash = 53 * hash + (this.msgID != null ? this.msgID.hashCode() : 0);
        hash = 53 * hash + (this.feeInfo != null ? this.feeInfo.hashCode() : 0);
        return hash;
    }
        
        public boolean equals(Object o){
            if(o instanceof UMSMsg){
                return ((UMSMsg)o).getBasicMsg().equals(this.getBasicMsg()) && ((UMSMsg)o).getBatchMode() == this.getBatchMode()
                        && ((UMSMsg)o).getBatchNO().equals(this.getBatchNO()) && ((UMSMsg)o).getDoCount() == this.getDoCount()
                        && ((UMSMsg)o).getErrMsg().equals(this.getErrMsg()) && ((UMSMsg)o).getFeeInfo().equals(this.getFeeInfo())
                        && ((UMSMsg)o).getFinishDate().equals(this.getFinishDate()) && ((UMSMsg)o).getFinishTime().equals(this.getFinishTime())
                        && ((UMSMsg)o).getMsgID().equals(this.getMsgID()) && ((UMSMsg)o).getRep()==this.getRep()
                        && ((UMSMsg)o).getReturnCode().equals(this.getReturnCode()) && ((UMSMsg)o).getSequenceNO().equals(this.getSequenceNO())
                        && ((UMSMsg)o).getSerialNO() == this.getSerialNO() && ((UMSMsg)o).getSmsMsgSeq() == this.getSmsMsgSeq()
                        && ((UMSMsg)o).getStatusFlag() == this.getStatusFlag();
            }
            return false;
        }
	
}
