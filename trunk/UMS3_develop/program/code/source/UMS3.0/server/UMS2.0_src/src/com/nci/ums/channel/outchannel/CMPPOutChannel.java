/**
 * <p>Title: CMPPOutChannel.java</p>
 * <p>Description:
 *    CMPPOutChannel�Ⲧ�����࣬�̳�OutChannel
 *    ʵ���ƶ���Ϣ�ķ��͹���
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering�� Ltd.</p>
 * <p>Company:   2003 Hangzhou NCI System Engineering�� Ltd.</p>
 * Date        Author      Changes
 * NOV. 5 2003   ��־��        Created
 * @version 1.0
 */

package com.nci.ums.channel.outchannel;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Calendar;

import com.commerceware.cmpp.*;
import com.nci.ums.exception.*;
import com.nci.ums.channel.channelinfo.*;
import com.nci.ums.util.*;
import com.nci.ums.util.MD5;

public class CMPPOutChannel extends OutChannel {

	private conn_desc con;
	private CMPP cmpp;
	private MsgInfo[] msgInfos;
	private boolean responseFlag;
	private int submitCount;
	private int responseCount;
	private int msgCount;
	private int activeTime = 0;

	// ��ʱ����Ϊһ����
	private javax.swing.Timer my_timer;
	private CMPPOutChannel.ResponseOutTime responseOutTime = new CMPPOutChannel.ResponseOutTime();

	/*
	 * ���캯��
	 */
	public CMPPOutChannel(MediaInfo mediaInfo) {
		super(mediaInfo);
		this.mediaInfo = mediaInfo;
		cmpp = new CMPP();
		msgInfos = new MsgInfo[16];
	}

	// �򻬶����������ⷢ��Ϣ

	public void run() {
		my_timer = new javax.swing.Timer(1000 * 60 * 1, responseOutTime); // 1���Ӻ�δ��Ӧ����
		my_timer.setRepeats(false);
		while (!Thread.interrupted() && !quitFlag.getLockFlag() && !stop) {
			// �������ӡ�

			// ����ʱΪ��ע
			getConnection();

			if (stop)
				break;
			Res.log(Res.DEBUG, "�ɹ��������ӣ�");

			// ������Ϣ
			try {
				msgInfos = new MsgInfo[16];
				msgCount = getMsgInfos(msgInfos); // ��Ҫ�޸�,��ϸ�����Ķ��˺�������
				// ���ͻ�����������Ϣ
				submitCount = 0;
				for (int i = 0; i < msgCount; i++) {
					// ����ʱΪ��ע
					int response = sendMsg(msgInfos[i]);
					// int response=0;
					if (response == 0) {
						submitCount = submitCount + 1;
					}
				}
				// �ȴ���Ӧ

				responseFlag = false;
				responseCount = 0;
				if (submitCount > 0) {
					my_timer.start();
					while (!Thread.interrupted() && !quitFlag.getLockFlag()
							&& !stop && !responseFlag) {
						// testResponse();
						// ����ʱΪ��ע
						readPa(con);
					}
					my_timer.stop();
					Res.log(Res.DEBUG, "receive response end");
					endSubmit(msgInfos, msgCount);
				}
			} catch (Exception e) {
				errorLogout();
				Res.log(Res.ERROR, "cmppoutchannel run error:" + e);
                                Res.logExceptionTrace(e);
				endSubmit(msgInfos, msgCount);
				sleepTime(1000);
			}
			if (msgCount == 0)
				sleepTime(1000);

		}
	}

	private void errorLogout() {
		Res.log(Res.DEBUG, "��ʼ�����ƻ��������˳��������������");
		try {
			cmpp.cmpp_logout(con);
			Res.log(Res.DEBUG, "�����˳�");
		} catch (Exception ee) {
			Res.log(Res.DEBUG, "�˳��쳣");
		} finally {
			cmpp.cmpp_disconnect_from_ismg(con);
			con = null;
		}

	}

	// ����
	private void testResponse() {

		OpData(0, msgInfos[responseCount], "");
		// msgInfos[responseCount].setStatus(1);
		// msgInfos[responseCount].setRetCode(0);
		responseCount = responseCount + 1;
		if (responseCount == submitCount)
			responseFlag = true;
	}

	// ����

	/*
	 * ���ڶ�ȡ�ظ���Ϣ�ĺ����� ����0��ʱ�򣬱�ʾ���� ���ط�0��ʱ�򣬱�ʾ�쳣��
	 * ���з���8001��ʱ���ʾ�յ�δ֪��ʽ�İ�������8000��ʱ�򣬱�ʾ������󣬾���Ҫ�������ӣ�
	 */
	private int readPa(conn_desc con) throws IOException {
		cmppe_result cr = null;
		int result = 0;
		try {
			cr = cmpp.readResPack(con);
			System.out.println("��ʼ����");
			switch (cr.pack_id) {

			case CMPP.CMPPE_NACK_RESP:
				Res.log(Res.DEBUG, "�յ� nack pack���ݰ�");
				break;

			case CMPP.CMPPE_LOGIN_RESP:
				cmppe_login_result cl;
				cl = (cmppe_login_result) cr;
				// Res.log(Res.DEBUG, "�յ� login_resp���ݰ� :����״̬Ϊ " + cl.stat);
				result = cr.stat;
				break;

			case CMPP.CMPPE_LOGOUT_RESP:
				// Res.log(Res.DEBUG, "�յ� login_resp���ݰ� :����״̬Ϊ " + cr.stat);
				result = cr.stat;

				con = null;
				break;

			case CMPP.CMPPE_SUBMIT_RESP:

				cmppe_submit_result sr;
				sr = (cmppe_submit_result) cr;
				String msgID = new String(sr.msg_id).trim();
				// Res.log(Res.DEBUG, "�յ� submit_resp ���ݰ� :����״̬Ϊ " + sr.stat);
				result = sr.stat;
				// ����ɹ����ز���
				Res.log(Res.DEBUG, "send success:" + sr.stat);
				MsgInfo msgInfo = getMsgInfo(sr.seq);
				if (result == 0 || result == 42) {
					if (msgInfo != null) {
						// �����ִ��ȡ��ִMSGID��
						// if(msgInfo.getAck()==1)
						// msgInfo.setReplyDes(new
						// String(sr.msg_id).substring(0,16));
						OpData(result, msgInfo, msgID);
						msgInfo.setStatus(0);
						msgInfo.setRetCode(result);
					}
				} else if (result == 40) {
					if (msgInfo != null) {
						OpData(result, msgInfo, "");
						msgInfo.setStatus(0);
					}
					msgInfo.setRetCode(result);
					responseFlag = true;
				} else {
					msgInfo.setStatus(1);
					msgInfo.setRetCode(result);
				}

				responseCount = responseCount + 1;
				// Res.log(Res.DEBUG,"������"+responseCount);
				if (responseCount == submitCount)
					responseFlag = true;
				break;
			case CMPP.CMPPE_ACTIVE_RESP:
				// Res.log(Res.DEBUG, "�յ� active_resp ���ݰ� :����״̬Ϊ " + cr.stat);
				result = cr.stat;
				break;
			default:
				// Res.log(Res.DEBUG, "�յ� δ֪�����ݰ� :����״̬Ϊ " + cr.stat);
				break;
			}
		} catch (UnknownPackException e) {
			e.printStackTrace();
			result = 8001;
			Res.log(Res.ERROR, "�յ���δ֪��ʽ�İ�" + e);
                        Res.logExceptionTrace(e);
		} catch (DeliverFailException e) {
			e.printStackTrace();
			result = 8000;
			Res.log(Res.ERROR, "���������쳣" + e);
                        Res.logExceptionTrace(e);
		} catch (IOException e) {
			e.printStackTrace();
			result = 8000;
			Res.log(Res.ERROR, "���������쳣" + e);
                        Res.logExceptionTrace(e);
			throw e;
		}
		return result;
	}

	private MsgInfo getMsgInfo(int seq) {
		MsgInfo result = null;
		for (int i = 0; i < msgCount; i++) {
			if (msgInfos[i].getSeq() == seq)
				result = msgInfos[i];
		}
		return result;
	}

	/*
	 * ����һ����Ϣ��������ֵΪ������ ����0��ʾ���ͳɹ��� ����8000��ʾ���������Ҫ�������ӡ�
	 * ���з���8001��ʱ���ʾ�յ�δ֪��ʽ�İ�����Ҫ�������ӡ� ����8002��ʱ���ʾ����Խ�磬����Ҫ�������ӡ�
	 */
	public int sendMsg(MsgInfo msgInfo) throws UMSConnectionException {
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

		}
		return result;
	}

	/*
	 * ����һ����Ϣ��������ֵΪ������ ����0��ʾ���ͳɹ��� ����8000��ʾ���������Ҫ�������ӡ� ����8002��ʱ���ʾ����Խ�磬����Ҫ�������ӡ�
	 */
	public int sendMsg(byte dst_addr[][], int sm_len, byte short_msg[],
			byte src_addr[][], byte count, MsgInfo msgInfo)
			throws UMSConnectionException {
		cmppe_submit sub = new cmppe_submit();
		int result = 0;
		// sp ID���룬���7�ֽ�
		byte icp_id[] = new byte[7];
		icp_id[0] = 0x39;
		icp_id[1] = 0x31;
		icp_id[2] = 0x31;
		icp_id[3] = 0x33;
		icp_id[4] = 0x30;
		icp_id[5] = 0x37;
		icp_id[6] = 0x0;
		// ��������
		byte svc_type[] = new byte[10];
		svc_type[0] = 0x39;
		svc_type[1] = 0x33;
		svc_type[2] = 0x31;
		svc_type[3] = 0x30;
		svc_type[5] = 0x0;
		byte fee_type = 1;
		if (msgInfo.getFeeType() != 0)
			fee_type = 2; // �ʷ������
		// �Է�Ϊ��λ
		int info_fee = 0;
		if (msgInfo.getFeeType() != 0)
			// info_fee=(int)msgInfo.getFee()*100;
			info_fee = msgInfo.getFee();
		// int info_fee = 0; //�ʷѴ���
		byte proto_id = 1; // Э���ʶ��
		byte msg_mode = 0;

		// Ҫ���ִ
		if (msgInfo.getAck() == 1 || msgInfo.getAck() == 3)
			msg_mode = 1; // Submit���ġ��Ƿ�Ҫ�󷵻�״̬ȷ�ϱ���(0--����Ҫ��1--��Ҫ)����

		byte priority = 8; // Submit������Ϣ����(0��9)��
		byte fee_utype = 2; // �Ʒ��û����
		if (msgInfo.getFeeType() != 0)
			fee_utype = 0;
		byte fee_user[] = new byte[CMPP.CMPPE_MAX_MSISDN_LEN]; // Submit���ļƷ��û�
		fee_user[0] = 0x0;

		byte validate[] = new byte[10];
		validate[0] = 0; // Submit���Ĵ����Ч��
		byte schedule[] = new byte[2];
		schedule[0] = 0; // ��ʱ����ʱ��

		byte du_count = count; // Submit����Ŀ���ֻ�������������0��

		byte data_coding = (byte) msgInfo.getContentMode(); // GB2312����
		try {
			sub.set_icpid(icp_id);
			sub.set_svctype(svc_type);
			sub.set_feetype(fee_type);
			sub.set_infofee(info_fee);
			sub.set_protoid(proto_id);
			sub.set_msgmode(msg_mode);
			sub.set_priority(priority);
			sub.set_validate(validate);
			sub.set_schedule(schedule);
			sub.set_feeutype(fee_utype);
			sub.set_feeuser(fee_user);
			sub.set_srcaddr(src_addr[0]);
			sub.set_ducount(du_count);
			sub.set_dstaddr(dst_addr);
			sub.set_msg(data_coding, sm_len, short_msg);
			// ��ʽ����

			msgInfo.setSeq(con.seq);
			msgInfo.setStatus(1);
			cmpp.cmpp_submit(con, sub);

			// ��ʽ����

			// ���Դ���
			/*
			 * CMPPTest cmppTest=new CMPPTest("192.168.0.175",10001);
			 * cmppTest.cmpp_submit(sub);
			 */
			// ���Դ���
		} catch (OutOfBoundsException e) {
			e.printStackTrace();
			result = 8002; // ����Խ�磻
			Res.log(Res.ERROR, "����Խ��" + e);
                        Res.logExceptionTrace(e);
		} catch (IOException e) {
			e.printStackTrace();
			result = 8000; // ���ӹ����й��ϣ�
			Res.log(Res.ERROR, "8002" + e);
                        Res.logExceptionTrace(e);
			errorLogout();
			// throw new UMSConnectionException(e);
		}
		return result;
	}

	/*
	 * �ͷ����������ӣ��������������ѭ������sleep ����̶߳Ͽ����߳����˳���־�����û����߳��Ƴ���־�����õ�ʱ��ֱ���˳����ӡ�
	 * �ͷ������������֤�������֤���ɹ����������ӷ�������
	 */
	public void getConnection() {
		boolean flag = false;
		int i = 0;
		while ((!Thread.interrupted() && !quitFlag.getLockFlag() && !stop)
				&& (!flag)) {
			// ��ʾ��ǰ���Ӿ��ǶϿ���
			if (con == null) {
				con = new conn_desc();
				if (i++ == 0)
					Res.log(Res.DEBUG, mediaInfo.getMediaName() + "��ʼ���ӷ�����:");
				Res.log(Res.DEBUG, mediaInfo.getMediaName() + "��" + i
						+ "�����ӷ�����:");
				try {
					System.out.println(mediaInfo.getIp() + mediaInfo.getPort()
							+ "");
					cmpp.cmpp_connect_to_ismg(mediaInfo.getIp(), mediaInfo
							.getPort(), con);
					String inetAddr_str = con.sock.getInetAddress().toString();
					String localAddr_str = con.sock.getLocalAddress().toString();
					int port = con.sock.getPort();
					flag = true;
				} catch (Exception e) {
					e.printStackTrace();
					Res.log(Res.DEBUG, mediaInfo.getMediaName() + "��" + i
							+ "�����ӷ�����ʧ�ܣ�");
					closeConnection();
				}
				// ������������ӣ�����֤�û��ĺϷ��ԡ�
				if (flag == true) {
					Res.log(Res.DEBUG, mediaInfo.getMediaName() + "��" + i
							+ "�����ӷ������ɹ���");
					Res.log(Res.DEBUG, mediaInfo.getMediaName() + "��ʼ�ͷ�������֤��");
					flag = login();
					if (flag)
						Res.log(Res.DEBUG, mediaInfo.getMediaName()
								+ "�ͷ�������֤�ɹ���");
					else
						Res.log(Res.DEBUG, mediaInfo.getMediaName()
								+ "�ͷ�������֤ʧ�ܣ�");
				}
				// �����֤���ʧ�ܣ��ر����ӡ�����sleepһ��ʱ�䣬������������Ӳ�����֤
				if (flag == false) {
					Res.log(Res.INFO, mediaInfo.getMediaName()
							+ "�����һ���Ӻ��������ӷ�����");
					closeConnection();
					// sleepһ���ӡ�
					try {
						Thread.sleep(60 * 1000);
					} catch (Exception e) {
						e.printStackTrace();
						Res.log(Res.INFO, "�̱߳��ж�");
						pleaseStop();
					}
				}
			} else {
				try {

					if (activeTime > 0) {
						Res.log(Res.DEBUG, "time"
								+ (Util.getUTCTime() - activeTime));
						if (Util.getUTCTime() - activeTime >= 1 * 54) {
							activeTime = Util.getUTCTime();
							cmpp.cmpp_active_test(con);
							if (readPa(con) == 0) {
								flag = true;
							} else {
								flag = false;
								con = null;
							}
						} else {
							flag = true;
						}

					} else {
						activeTime = Util.getUTCTime();
						flag = true;
					}

				} catch (Exception e) {
				}

			}
			// con!=null��ʾ�Ѿ�����������
		}
	}

	public void pleaseStop() {
		stop = true;
	}

	/*
	 * ��½���������ںͷ��������������֤ ����Ϊtrue��ʾ��֤�ɹ�������false��ʾ��֤ʧ�ܡ�
	 */
	private boolean login() {
		try {
			// 0 ��ֻ���ͣ� 0x12�ǰ汾���룬 ���һ����ʱ�����
			cmpp.cmpp_login(con, mediaInfo.getLoginName(), mediaInfo.getLoginPassword(),
					(byte) 0, 0x20, Util.getUTCTime());

			if (readPa(con) == 0)
				return true;
		} catch (OutOfBoundsException e) {
			e.printStackTrace();
			Res.log(Res.ERROR, "���������涨��Χ��");
                        Res.logExceptionTrace(e);
		} catch (IOException e) {
			e.printStackTrace();
			Res.log(Res.ERROR, "8002", mediaInfo.getMediaName());
                        Res.logExceptionTrace(e);
		}
		// ���readPa������ֵ��Ϊ0������;�����쳣�����ʾ��½ʧ�ܣ���Ҫ���µ�½��
		return false;
	}

	/*
	 * �˳���½�ĺ���������֪ͨ����������½�˳� ����Ϊtrue��ʾ��½�ɹ�������false��ʾ��½ʧ�ܡ�
	 */
	private boolean logout() {
		try {
			cmpp.cmpp_logout(con);
			readPa(con);
		} catch (Exception e) {
			Res.log(Res.ERROR, "8002", mediaInfo.getMediaName());
                        Res.logExceptionTrace(e);
		}
		return true;
	}

	/*
	 * �رպͷ�����������
	 */
	public void closeConnection() {
		if (con != null) {
			logout();
			cmpp.cmpp_disconnect_from_ismg(con);
			con = null;
		}
	}

	class ResponseOutTime implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			errorLogout();
		};
	}
	
	public static void main(String[] args){
		byte icp_id[] = new byte[7];
		icp_id[0] = 0x39;
		icp_id[1] = 0x31;
		icp_id[2] = 0x31;
		icp_id[3] = 0x33;
		icp_id[4] = 0x30;
		icp_id[5] = 0x37;
		icp_id[6] = 0x0;
		System.out.println(new String(icp_id));
	}

	public LockFlag getLockFlag() {
		// TODO Auto-generated method stub
		return CMPPThreadLockFlag.getInstance();
	}

}