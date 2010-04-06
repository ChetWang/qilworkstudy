package com.nci.ums.transmit.client.sender;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.nci.ums.transmit.client.RequestHandler;
import com.nci.ums.transmit.client.TransmitClient;
import com.nci.ums.transmit.common.IllegalCommandException;
import com.nci.ums.transmit.common.IllegalFSEQException;
import com.nci.ums.transmit.common.OutOfMaxFrameSizeException;
import com.nci.ums.transmit.common.UMSTransmitException;
import com.nci.ums.transmit.common.message.GlobalConstant;
import com.nci.ums.transmit.util.SerialFSEQ;

/**
 * 文件发送对象
 * 
 * @author Qil.Wong
 * 
 */
public class TransmitFileSender extends TransmitSender {

	private File file;

	public TransmitFileSender(TransmitClient client, int toAddress,
			File sendFile, boolean reverseDirection) {
		super(client, toAddress, reverseDirection);
		this.file = sendFile;
	}

	public void send(int userCommand, RequestHandler request)
			throws UMSTransmitException, InterruptedException,
			IllegalCommandException, OutOfMaxFrameSizeException,
			IllegalFSEQException {
		if (userCommand < TransmitClient.COMMAND_NO
				|| userCommand > TransmitClient.DEFAULT_MAX_USER_COMMAND) {
			throw new IllegalCommandException(userCommand);
		}
		try {
			int direction = getDirection();
			FileInputStream fis = new FileInputStream(file);

			int fileSize = fis.available(); // 获取文件大小
			// 要排除2个自己的用户定义命令
			fileSize = fileSize + TransmitClient.COMMAND_BYTES_LENGTH;

			if (fileSize >= GlobalConstant.DATA_MAX_LENGTH
					* GlobalConstant.ISEQ_MAX_NUMBER) {
				throw new OutOfMaxFrameSizeException(fileSize);
			}

			int fnum; // 帧总数
			if (fileSize % GlobalConstant.DATA_MAX_LENGTH == 0) {
				fnum = fileSize / GlobalConstant.DATA_MAX_LENGTH;
			} else {
				fnum = fileSize / GlobalConstant.DATA_MAX_LENGTH + 1;
			}
			byte[] buff = new byte[GlobalConstant.DATA_MAX_LENGTH];
			int readCounts;
			int iseq = 1;
			int fseq = getFseq();
			while (-1 != (readCounts = fis.read(buff, 0, buff.length))) {
				if (readCounts < GlobalConstant.DATA_MAX_LENGTH) {
					if (iseq == 1) {
						// 单帧发送
						iseq = 0;
					} else {
						// 最后一帧
						iseq = GlobalConstant.ISEQ_MAX_NUMBER;
					}
					// 截取有效数据
					byte[] temp = new byte[readCounts];
					System.arraycopy(buff, 0, temp, 0, readCounts);
					buff = temp;
				}
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public File getFile() {
		return file;
	}

	public void setFile(File sendFile) {
		this.file = sendFile;
	}

}
