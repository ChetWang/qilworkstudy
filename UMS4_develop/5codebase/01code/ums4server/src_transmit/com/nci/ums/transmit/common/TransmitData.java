package com.nci.ums.transmit.common;

import com.nci.ums.transmit.common.message.ControlCode;
import com.nci.ums.transmit.common.message.GlobalConstant;
import com.nci.ums.transmit.common.message.UnPackageMessage;

/**
 * 转发数据对象
 * 
 * @author Qil.Wong
 * 
 */
public class TransmitData {

	/**
	 * 单帧的数据
	 */
	private byte[] oneFrameData;
	/**
	 * 该帧数据对应的uid
	 */
	private String uid;
	/**
	 * 该帧数据传送成功与否的标记
	 */
	private boolean success = true;

	/**
	 * 数据接收时间
	 */
	private String receivedTime;

	/**
	 * 数据转发时间
	 */
	private String transmitTime;

	/**
	 * 数据目的地址
	 */
	private int destinationAddress;

	/**
	 * 数据源地址
	 */
	private int sourceAddress;

	/**
	 * 异常代号
	 */
	private int errorType = 0;

	/**
	 * 接收数据所在的子转发服务类型
	 */
	private int subServerType;

	/**
	 * 转发线程退出的信号量
	 */
	public static final TransmitData QUIT_SIGNAL = new TransmitData();

	/**
	 * only for quit
	 */
	private TransmitData() {
		oneFrameData = new byte[0];
	}

	/**
	 * 转发数据对象构造函数，包含转发数据本身byte数组，对应的uid，及传送成果与否的标记（初始化时默认成功）
	 * 
	 * @param uid
	 *            该帧数据对应的uid
	 * @param successFlag
	 *            该帧数据传送成功与否的标记
	 * @param oneFrameData
	 *            单帧的数据
	 * @param subServerType
	 *            数据接收处的子服务类型，如果是外部应用或终端子服务接收，则是2；如果是内部应用子服务接收，则是1；
	 */
	public TransmitData(String uid, boolean successFlag, byte[] oneFrameData,
			int subServerType) {
		this.uid = uid;
		this.success = successFlag;
		this.oneFrameData = oneFrameData;
		this.subServerType = subServerType;
		anlysisData();
	}

	/**
	 * 转发数据对象构造函数，包含转发数据本身byte数组，对应的uid，及传送成果与否的标记（初始化时默认成功）
	 * 
	 * @param oneFrameData
	 *            单帧的数据
	 * @param subServerType
	 *            数据接收处的子服务类型，如果是外部应用或终端子服务接收，则是2；如果是内部应用子服务接收，则是1；
	 */
	public TransmitData(byte[] oneFrameData, int subServerType) {
		this.uid = TransmitUID.getUMSUID(TransmitUID.UMSUID_SERVER);
		this.oneFrameData = oneFrameData;
		this.subServerType = subServerType;
		anlysisData();
	}

	private void anlysisData() {

		int terminalAdress = UnPackageMessage.getConsumerAddress(oneFrameData);
		int appAddress = UnPackageMessage.getManufactureAddress(oneFrameData);

		// 解析目的地址和源地址
		if (subServerType == ControlCode.DIRECTION_FROM_TREMINAL) {
			destinationAddress = appAddress;
			sourceAddress = terminalAdress;
		} else if (subServerType == ControlCode.DIRECTION_FROM_APPLICATION) {
			destinationAddress = terminalAdress;
			sourceAddress = appAddress;
		}
	}

	/**
	 * 获取单帧的数据
	 * 
	 * @return
	 */
	public byte[] getOneFrameData() {
		return oneFrameData;
	}

	/**
	 * 设置单帧的数据
	 * 
	 * @param oneFrameData
	 */
	public void setOneFrameData(byte[] oneFrameData) {
		this.oneFrameData = oneFrameData;
	}

	/**
	 * @功能 获取单帧中用户数据，不包括前2个字节的用户命令
	 * @return
	 * 
	 * @Add by ZHM 2009-10-23
	 */
	public byte[] getOneFrameUserDataWithoutUserCommand() {
		int length = this.oneFrameData.length;
		byte[] temp = new byte[length - GlobalConstant.MESSAGE_LENGTH - 2]; // -2表示去除数据域中“用户命令”
		System.arraycopy(this.oneFrameData, GlobalConstant.INDEX_DATA + 2, // +2表示去除数据域中前两位“用户命令”
				temp, 0, temp.length);
		return temp;
		// return UnPackageMessage.getData(oneFrameData);
	}

	/**
	 * 获取用户数据域中2个字节的用户命令
	 * @return
	 */
	public int getUserCommand() {
		int length = this.oneFrameData.length;
		byte[] temp = new byte[2]; // 2表示数据域中“用户命令”2个字节
		System.arraycopy(this.oneFrameData, GlobalConstant.INDEX_DATA, temp, 0,
				2);
		return UnPackageMessage.twoBytesToInt(temp);
	}

	/**
	 * 获取该帧数据对应的uid
	 * 
	 * @return
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * 设置该帧数据对应的uid
	 * 
	 * @param uid
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * 判断转发是否成功
	 * 
	 * @return
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * 设置转发成功与否标记
	 * 
	 * @param successFlag
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * 获取数据接收到的时间
	 * 
	 * @return
	 */
	public String getReceivedTime() {
		return receivedTime;
	}

	/**
	 * 设置数据接收到的时间
	 * 
	 * @param receivedTime
	 */
	public void setReceivedTime(String receivedTime) {
		this.receivedTime = receivedTime;
	}

	/**
	 * 获取数据转发的时间
	 * 
	 * @return
	 */
	public String getTransmitTime() {
		return transmitTime;
	}

	/**
	 * 设置数据转发的时间
	 * 
	 * @param transmitTime
	 */
	public void setTransmitTime(String transmitTime) {
		this.transmitTime = transmitTime;
	}

	/**
	 * 获取目的地址
	 * 
	 * @return
	 */
	public int getDestinationAddress() {
		return destinationAddress;
	}

	/**
	 * 设置目的地址
	 * 
	 * @param destinationAddress
	 */
	public void setDestinationAddress(int destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

	/**
	 * 获取源地址
	 * 
	 * @return
	 */
	public int getSourceAddress() {
		return sourceAddress;
	}

	/**
	 * 设置源地址
	 * 
	 * @param sourceAddress
	 */
	public void setSourceAddress(int sourceAddress) {
		this.sourceAddress = sourceAddress;
	}

	/**
	 * 获取接收数据所在子转发服务的类型
	 * 
	 * @return
	 */
	public int getSubServerType() {
		return subServerType;
	}

	/**
	 * 获取异常代号
	 * 
	 * @return
	 */
	public int getErrorType() {
		return errorType;
	}

	/**
	 * 设置异常代号
	 * 
	 * @param errorType
	 */
	public void setErrorType(int errorType) {
		this.errorType = errorType;
	}

}
