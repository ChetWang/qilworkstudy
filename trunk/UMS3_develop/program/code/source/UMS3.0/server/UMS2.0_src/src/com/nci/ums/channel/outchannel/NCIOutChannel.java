/*
 * Created on 2007-1-23
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.nci.ums.channel.outchannel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.huawei.insa2.util.Args;
import com.nci.ums.channel.channelinfo.MediaInfo;
import com.nci.ums.channel.myproxy.MySMProxy;
import com.nci.ums.channel.myproxy.SendMessage;
import com.nci.ums.exception.UMSConnectionException;
import com.nci.ums.util.DBConnect;
import com.nci.ums.util.DataBaseOp;
import com.nci.ums.util.PropertyUtil;
import com.nci.ums.util.Res;
import com.nci.ums.util.Util;

/**
 * @author c
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class NCIOutChannel extends OutChannel {

	private static Args args = null;
	private static MySMProxy myProxy = null;
	private String sPath = "c:\\ums\\config\\";
	private MsgInfo[] msgInfos;
	private boolean responseFlag;
	private int submitCount;
	private int responseCount;
	private int msgCount;
	private int activeTime = 0;
	private SendMessage sender = null;

	protected boolean repeatMsgCheckFlag = false;

	protected int durationTime = 10;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

	public NCIOutChannel(MediaInfo mediaInfo) {
		super(mediaInfo);
		this.mediaInfo = mediaInfo;
		msgInfos = new MsgInfo[16];
		// sender = new SendMessage(mediaInfo);
		sender = SendMessage.getInstance(mediaInfo);

		String flag = PropertyUtil.getProperty("checkFlag",
				"/resources/repeatMsgCheck.props");
		if (flag != null && flag.equalsIgnoreCase("true")) {
			repeatMsgCheckFlag = true;
		}
		String duration = PropertyUtil.getProperty("durationTime",
				"/resources/repeatMsgCheck.props");
		durationTime = Integer.parseInt(duration);
	}

	public void run() {
		// my_timer = new javax.swing.Timer(1000 * 60 * 1, responseOutTime);
		// //1���Ӻ�δ��Ӧ����
		// my_timer.setRepeats(false);
		long lCount = 0;
		while (!Thread.interrupted() && !quitFlag.getLockFlag() && !stop ) {
			// �������ӡ�
			if (stop) {
				break;
			}
			try {
				/*
				 * ���ǵ����ڵĹ�˾�㽭�ƶ��������޷�ȡ��sequence_id ��ʱ��������
				 */
				msgInfos = new MsgInfo[16];
				msgCount = getMsgInfos(msgInfos); // ��Ҫ�޸�
				// ���ͻ�����������Ϣ
				submitCount = 0;
				for (int i = 0; i < msgCount; i++) {
					int response = sendMsg(msgInfos[i]); // ��ȡ����
					// int response=0;
					if (response == 0 || response == -9922) {//-9922���ظ�����Ϣ
						submitCount = submitCount + 1;
						MsgInfo msgInfo = msgInfos[i];
						OpData(response, msgInfo, "");
						msgInfo.setStatus(0);
						msgInfo.setRetCode(0);
						responseCount = responseCount + 1;
						if (responseCount == submitCount) {
							responseFlag = true;
							// �ڴ˴���
						}
					}
				}
				// �ȴ���ӦҪ�޸�
				// this.nciDataLockFlag.setLockFlag(false);
				responseFlag = false;
				responseCount = 0;
				// Res.log(Res.DEBUG, "receive response end");
				endSubmit(msgInfos, msgCount);
			} catch (Exception e) {
				Res.log(Res.ERROR, "cmppoutchannel run error:" + e);
				Res.logExceptionTrace(e);
				endSubmit(msgInfos, msgCount);
				sleepTime(1000);
			}
			if (msgCount == 0) {
				sleepTime(2000);
			}
		}
	}

	public int sendMsg(MsgInfo msgInfo) throws UMSConnectionException {
		// �ظ����͵���Ϣ����
		Connection conn = null;
		if (repeatMsgCheckFlag) {
			try {
				conn = DriverManager.getConnection(DataBaseOp.getPoolName());
				Date d = sdf.parse(msgInfo.getBatchNO());
				long b2 = d.getTime() - durationTime * 1000;
				d = new Date(b2);
				PreparedStatement prep = conn
						.prepareStatement("select count(*) from out_ok where content=? and recvid=? and batchno>? and retCode=?");
				prep.setString(1, msgInfo.getContent());
				prep.setString(2, msgInfo.getRecvID());
				prep.setString(3, sdf.format(d));
				prep.setString(4, "0000");
				ResultSet rs2 = prep.executeQuery();
				boolean flag = false;
				while (rs2.next()) {
					int count = rs2.getInt(1);
					if (count > 0)
						flag = true;
				}
				prep.close();
				rs2.close();
				if (flag) {
					Res.log(Res.WARN, "�ظ���Ϣ��UMS����.�����ߣ�" + msgInfo.getRecvID()
							+ ", ���ݣ�" + msgInfo.getContent());
					return -9922;
				}

			} catch (Exception e) {
				e.printStackTrace();
				return -9922;
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		int result = 1;
		try {
			byte[] short_msg = Util.getByte(msgInfo.getContent(), msgInfo
					.getContentMode());
			// ����Ⱥ���ֻ���
			String[] temp_dst = msgInfo.getRecvID().split(",");

			int count = temp_dst.length;
			byte[][] dstCode = new byte[count + 1][15];
			for (int i = 0; i < count; i++) {
				dstCode[i] = Util.getByteString(temp_dst[i]);
			}
			// Ⱥ���������
			// ����dstCode[0] = Util.getByteString(dst);
			byte[][] srcCode = new byte[2][15];
			srcCode[0] = Util.getByteString(msgInfo.getSendID());
			int sm_len = short_msg.length;
			result = sendMsg(dstCode, sm_len, short_msg, srcCode, (byte) count,
					msgInfo);
		} catch (Exception e) {
			Res.log(Res.ERROR, e.getMessage());
			Res.logExceptionTrace(e);
		}
		return result;
	}

	// ���͵�����¼���п����ǵ�������Ϣ��Ҳ�п�����Ⱥ������Ϣ
	public int sendMsg(byte[][] dst_addr, int sm_len, byte[] short_msg,
			byte[][] src_addr, byte count, MsgInfo msgInfo)
			throws UMSConnectionException {

		int nResult = -1;

		nResult = sender.sendMsg(dst_addr, sm_len, short_msg, src_addr, count,
				msgInfo);
		// sleepTime(3000);

		return nResult;
	}
	// ������ļ��������Բο�MM7OutChannel ����
}