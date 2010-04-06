package com.nci.ums.transmit.common.message;

import com.nci.ums.transmit.common.IllegalAdressException;
import com.nci.ums.transmit.util.ByteCoding;
import com.nci.ums.transmit.util.SerialFSEQ;

/**
 * <p>
 * 标题：CommonMessage.java
 * </p>
 * <p>
 * 描述： 常用报文生成类
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2009-8-25
 * @version 1.0
 */
public class CommonMessage {

	/**
	 * @功能 将0～65536间的数据转换成byte[2]数组
	 * @param address
	 *            int 要转换的参数
	 * @return byte[2]
	 * @throws Exception
	 *             数据不在范围内
	 * 
	 * @Add by ZHM 2009-8-26
	 */
	private static byte[] addressIntToByte(int address) throws IllegalAdressException {
		if (address < 0 || address >= 65536) {
			throw new IllegalAdressException(address);
		}
		byte[] bt = new byte[2];
		bt[0] = (byte) (0xFF & address);
		bt[1] = (byte) ((0xFF00 & address) >> 8);
		return bt;
	}

	/**
	 * @功能 生成登录报文
	 * @param manufacturerAddress
	 *            int 应用逻辑地址(0～65536)
	 * @param consumerAddress
	 *            int 终端逻辑地址(0～65536)
	 * @param direction
	 *            int 登录方，0:主站，1:终端
	 * @return 登录报文
	 * @throws Exception
	 *             逻辑地址不在范围内
	 * 
	 * @Add by ZHM 2009-8-26
	 */
	public static byte[] getLoginMessage(int manufacturerAddress,
			int consumerAddress, int direction)
			throws IllegalAdressException {
		byte[] manufacturer = addressIntToByte(manufacturerAddress);
		byte[] consumer = addressIntToByte(consumerAddress);
		return getLoginMessage(manufacturer, consumer, direction);
	}

	/**
	 * @功能 生成退出登录报文
	 * @param manufacturerAddress
	 *            int 应用逻辑地址(0～65536)
	 * @param consumerAddress
	 *            int 终端逻辑地址(0～65536)
	 * @param direction
	 *            int 登录方，0:主站，1:终端
	 * @return 退出登录报文
	 * @throws Exception
	 *             逻辑地址不在范围内
	 * 
	 * @Add by ZHM 2009-8-26
	 */
	public static byte[] getLogoutMessage(int manufacturerAddress,
			int consumerAddress, int direction)
			throws IllegalAdressException {
		byte[] manufacturer = addressIntToByte(manufacturerAddress);
		byte[] consumer = addressIntToByte(consumerAddress);
		return getLogoutMessage(manufacturer, consumer, direction);
	}

	/**
	 * @功能 生成登录报文
	 * @param manufacturerAddress
	 *            byte[] 应用逻辑地址
	 * @param consumerAddress
	 *            byte[] 终端逻辑地址
	 * @param direction
	 *            int 登录方，0:主站，1:终端
	 * 
	 * @return 登录报文
	 * 
	 * @Add by ZHM 2009-8-25
	 */
	private static byte[] getLoginMessage(byte[] manufacturerAddress,
			byte[] consumerAddress, int direction) {
		PackageMessage pm = new PackageMessage();

		pm.setManufacturerAddress(manufacturerAddress);
		pm.setConsumerAddress(consumerAddress);
		// pm.setMstaSeq(0, SerialFSEQ.getInstance().getSerial());
		pm.setMstaSeq(35, SerialFSEQ.getInstance().getSerial(), 0, 0);
		if (direction > 0) {
			pm.setControlCode(true, false, 33);
		} else {
			pm.setControlCode(false, false, 33);
		}

		return pm.packageMessage();
	}

	/**
	 * @功能 生成退出登录报文
	 * @param manufacturerAddress
	 *            byte[] 应用逻辑地址
	 * @param consumerAddress
	 *            byte[] 终端逻辑地址
	 * @param direction
	 *            int 登录方，0:主站，1:终端
	 * 
	 * @return 退出登录报文
	 * 
	 * @Add by ZHM 2009-8-25
	 */
	private static byte[] getLogoutMessage(byte[] manufacturerAddress,
			byte[] consumerAddress, int direction) {
		PackageMessage pm = new PackageMessage();

		pm.setManufacturerAddress(manufacturerAddress);
		pm.setConsumerAddress(consumerAddress);
		// pm.setMstaSeq(0, SerialFSEQ.getInstance().getSerial());
		pm.setMstaSeq(35, SerialFSEQ.getInstance().getSerial(), 0, 0);
		if (direction > 0) {
			pm.setControlCode(true, false, 34);
		} else {
			pm.setControlCode(false, false, 34);
		}

		return pm.packageMessage();
	}

	/**
	 * @功能 心跳报文生成
	 * @param manufacturerAddress
	 *            byte[] 应用逻辑地址
	 * @param consumerAddress
	 *            byte[] 终端逻辑地址
	 * @param direction
	 *            传输方向，false:主站→终端，true:终端→主站
	 * @return 心跳报文
	 * 
	 * @Add by ZHM 2009-8-25
	 */
	public static byte[] getHeartBeatPackage(byte[] manufacturerAddress,
			byte[] consumerAddress, boolean direction) {
		PackageMessage pm = new PackageMessage();

		pm.setManufacturerAddress(manufacturerAddress);
		pm.setConsumerAddress(consumerAddress);
		// pm.setMstaSeq(0, SerialFSEQ.getInstance().getSerial());
		pm.setMstaSeq(35, SerialFSEQ.getInstance().getSerial(), 0, 0);
		pm.setControlCode(direction, false, 36);

		return pm.packageMessage();
	}

	/**
	 * 获取心跳字节组
	 * 
	 * @return 心跳报文
	 */
	public static byte[] getServerHeartBeatPackage() {
		PackageMessage pm = new PackageMessage();

		// pm.setMstaSeq(0, SerialFSEQ.getInstance().getSerial());
		pm.setMstaSeq(35, SerialFSEQ.getInstance().getSerial(), 0, 0);
		pm.setControlCode(false, false, 36);
		// byte[] logicalAddress = {(byte) 0x96,0x00,0x00,0x02};
		// pm.setLogicalAddress(logicalAddress);

		/**
		 * 返回字节组，如 68000000002000682400001416
		 */
		return pm.packageMessage();
	}

	/**
	 * @功能 根据发送的报文生成响应的异常应答帧
	 * @param message
	 *            byte[] 接收到的报文
	 * @param errType
	 *            byte 错误类型
	 * @return 异常应答帧
	 * 
	 * @Add by ZHM 2009-8-25
	 */
	public static byte[] getReturnErrMessage(byte[] message, byte errType) {
		byte[] manufacturerAddress = UnPackageMessage
				.getManufacturerAddressBytes(message);
		byte[] consumerAddress = UnPackageMessage
				.getConsumerAddressBytes(message);
		byte controlCode = UnPackageMessage.getControlCode(message);
		byte[] mstaSeq = UnPackageMessage.getMstaSeq(message);

		// 数据域
		byte[] data = new byte[1];
		data[0] = errType;
		// 生成返回控制码
		ByteCoding bc = new ByteCoding(controlCode);
		bc.setBitTrue(6); // 设置异常标志
		controlCode = bc.getData();

		PackageMessage pm = new PackageMessage();
		pm.setConsumerAddress(consumerAddress);
		pm.setManufacturerAddress(manufacturerAddress);
		pm.setControlCode(controlCode);
		pm.setMstaSeq(mstaSeq);
		pm.setData(data);
		return pm.packageMessage();
	}
}
