package com.nci.ums.transmit.client.sender;

import com.nci.ums.transmit.client.RequestHandler;
import com.nci.ums.transmit.client.TransmitClient;
import com.nci.ums.transmit.common.IllegalCommandException;
import com.nci.ums.transmit.common.IllegalFSEQException;
import com.nci.ums.transmit.common.OutOfMaxFrameSizeException;
import com.nci.ums.transmit.common.UMSTransmitException;
import com.nci.ums.transmit.common.message.GlobalConstant;

public class TransmitByteSender extends TransmitSender {

	private byte[] bytes = null;
	
	
	

	public TransmitByteSender(TransmitClient client, int toAddress, byte[] b,
			boolean reverseDirection) {
		super(client, toAddress,reverseDirection);
		this.bytes = b;
	}

	public void send(int userCommand, RequestHandler request)
			throws UMSTransmitException, InterruptedException,
			IllegalCommandException, OutOfMaxFrameSizeException, IllegalFSEQException {
		if (userCommand < TransmitClient.COMMAND_NO
				|| userCommand > TransmitClient.DEFAULT_MAX_USER_COMMAND) {
			throw new IllegalCommandException(userCommand);
		}
		if (bytes == null) {
			throw new NullPointerException("byte数组不能为空");
		}

		int direction = getDirection();
		// FileInputStream fis = new FileInputStream(file);

		int size = bytes.length; // 获取文件大小
		// 要排除2个自己的用户定义命令
		size = size + TransmitClient.COMMAND_BYTES_LENGTH;

		if (size >= GlobalConstant.DATA_MAX_LENGTH
				* GlobalConstant.ISEQ_MAX_NUMBER) {
			throw new OutOfMaxFrameSizeException(size);
		}

		int fnum; // 帧总数
		if (size % GlobalConstant.DATA_MAX_LENGTH == 0) {
			fnum = size / GlobalConstant.DATA_MAX_LENGTH;
		} else {
			fnum = size / GlobalConstant.DATA_MAX_LENGTH + 1;
		}

		int iseq = 1;
		int fseq = getFseq();
		int length = 0;
		for (int i = 0; i < fnum; i++) {

			if (fnum == 1) {
				// 单帧发送
				iseq = 0;
				length = bytes.length;
			} else if (i == fnum - 1) {
				// 最后一帧
				iseq = GlobalConstant.ISEQ_MAX_NUMBER;
				length = bytes.length - (fnum - 1)
						* GlobalConstant.DATA_MAX_LENGTH;
			} else {
				length = GlobalConstant.DATA_MAX_LENGTH;
			}
			byte[] buff = new byte[length];
			System.arraycopy(bytes, i * GlobalConstant.DATA_MAX_LENGTH, buff,
					0, length);
			// 发送消息
			byte[] data = null;
			// 0是默认命令序号
			if (userCommand >= 0 && (iseq == 0 || iseq == 1)) {
				data = new byte[buff.length + 2];
				// 填充命令
				data[0] = (byte) (0xFF & userCommand);
				data[1] = (byte) ((0xFF00 & userCommand) >> 8);
				//				
				System.arraycopy(buff, 0, data, 2, buff.length);
			} else {
				data = buff;
			}
			byte[] message = getDataMessage(manufacturerAddress,
					consumerAddress, direction, data, fseq, fnum, iseq);

			client.enqueueOutData(message);
			iseq++;
		}
		// 正常有用户命令并且帧命令序号不超过有效范围的情况下，可以使用回调
		if (userCommand != TransmitClient.COMMAND_NO && fseq != -1
				&& request != null) {
			request.setFseq(fseq);
			client.getRequestHandlers().put(new Integer(fseq), request);
		}
	}

	/**
	 * 设置bytes内容
	 * 
	 * @param bytes
	 */
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public byte[] getBytes() {
		return bytes;
	}

}
