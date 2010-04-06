/**
 * <p>Title: CMPPOutChannel.java</p>
 * <p>Description:
 *    CMPPOutChannel���������࣬�̳�InChannel
 *    ʵ���ƶ���Ϣ�ķ��͹���
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering�� Ltd.</p>
 * <p>Company:   2003 Hangzhou NCI System Engineering�� Ltd.</p>
 * Date        Author      Changes
 * NOV.  5 2003   ��־��       Created
 * @version 1.0
 */

package com.nci.ums.channel.inchannel;

import java.io.*;
import com.commerceware.cmpp.*;
import com.nci.ums.channel.channelinfo.*;
import com.nci.ums.util.*;

public class CMPPInChannel extends InChannel {

	private MediaInfo mediaInfo;
	private conn_desc con;
	private CMPP cmpp;

	// private Test_Conn_to_MOBILE testconntomobile=new Test_Conn_to_MOBILE();

	public CMPPInChannel(MediaInfo mediaInfo) {
		super(mediaInfo);
		this.mediaInfo = mediaInfo;
		cmpp = new CMPP();
		// cmppActiveTest = new CMPPActiveTest(mediaInfo.getTimeOut(), cmpp);

	}

	public void run() {
		// my_timer = new javax.swing.Timer(1000 * 60 * 5, testconntomobile);
		// //5���Ӻ�����һ��
		// my_timer.setRepeats(false);
		// �Ȱ��˳���־����Ϊfalse;
		setIsQuit(false);
		while (!Thread.interrupted() && !quitFlag.getLockFlag() && !isQuit) {
			// �������ӡ�
			getConnection();
			// ���������߳�
			// cmppActiveTest.run();
			// my_timer.start();
			while (!Thread.interrupted() && !quitFlag.getLockFlag() && !isQuit) {
				// ��ʾ�����������쳣�������صĴ�����Ҫ�������ӷ�������
				if (readPa(con) < 0) {
					Res.log(Res.DEBUG, mediaInfo.getMediaName()
							+ "�����������쳣�������صĴ���");
					break;
				}
			}
			// my_timer.stop();
		}
		closeConnection();
		// ��״̬����Ϊtrue,��ʾ�Ѿ��˳����߳�,�Ա��ں����Ĳ�ѯ�߳�״̬��
		setIsQuit(true);
	}

	/*
	 * �ͷ����������ӣ��������������ѭ������sleep ����̶߳Ͽ����߳����˳���־�����û����߳��Ƴ���־�����õ�ʱ��ֱ���˳����ӡ�
	 * �ͷ������������֤�������֤���ɹ����������ӷ�������
	 */
	public void getConnection() {
		boolean flag = false;
		int i = 0;
		while ((!Thread.interrupted() && !quitFlag.getLockFlag() && !isQuit)
				&& (!flag)) {
			// ��ʾ��ǰ���Ӿ��ǶϿ���
			if (con == null) {
				con = new conn_desc();
				if (i++ == 0)
					Res.log(Res.DEBUG, mediaInfo.getMediaName() + "��ʼ���ӷ�����:");
				Res.log(Res.DEBUG, mediaInfo.getMediaName() + "��" + i
						+ "�����ӷ�����:");
				try {
					cmpp.cmpp_connect_to_ismg(mediaInfo.getIp(), mediaInfo
							.getPort(), con);
					flag = true;
				} catch (IOException e) {
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
					Res.log(Res.INFO, mediaInfo.getMediaName() + "�����"
							+ mediaInfo.getSleepTime() + "����������ӷ�����");
					closeConnection();
					// sleepһ��ʱ�䡣
					try {
						Thread.sleep(mediaInfo.getSleepTime() * 1000);
					} catch (Exception e) {
						e.printStackTrace();
						Res.log(Res.INFO, "�̱߳��ж�");
						setIsQuit(true);
					}
				}
			}
			// con!=null��ʾ�Ѿ�����������
			else
				flag = true;
		}
	}

	/*
	 * ��½���������ںͷ��������������֤ ����Ϊtrue��ʾ��֤�ɹ�������false��ʾ��֤ʧ�ܡ�
	 */
	private boolean login() {
		try {
			cmpp.cmpp_login(con, mediaInfo.getLoginName(), mediaInfo
					.getLoginPassword(), (byte) 1, 0x12, Util.getUTCTime());
			// ���ʱ�����Ҫ�޸ģ�����
			if (readPa(con) == 0)
				return true;
			else
				return false;
		} catch (OutOfBoundsException e) {
			e.printStackTrace();
			Res.log(Res.ERROR, "���͵���Ϣ���в��������涨����ֵ��");
                        Res.logExceptionTrace(e);
		} catch (IOException e) {
			e.printStackTrace();
			Res.log(Res.ERROR, "���ֵ�½����");
                        Res.logExceptionTrace(e);
		}
		return false;
	}

	/*
	 * �رպͷ�����������
	 */
	public void closeConnection() {
		if (con != null) {
			// logout();
			cmpp.cmpp_disconnect_from_ismg(con);
			con = null;
		}
	}

	public int readPa(conn_desc con) {
		String sendNumber = null; // send�ֻ��ţ�
		String recvNumber = null; // recv�ֻ��ţ�
		String smtxt = null; // �������ݣ�

		cmppe_result cr = null;

		int result = 0;

		try {
			cr = cmpp.readResPack(con);
			Res.log(Res.DEBUG, "�յ����ݰ�");
			result = cr.stat;

			switch (cr.pack_id) {
			case CMPP.CMPPE_NACK_RESP:
				Res.log(Res.DEBUG, "�յ� nack pack���ݰ�");
				break;

			case CMPP.CMPPE_LOGIN_RESP:
				cmppe_login_result cl;
				cl = (cmppe_login_result) cr;
				Res.log(Res.DEBUG, "�յ� login_resp���ݰ� :����״̬Ϊ " + cl.stat);
				break;

			case CMPP.CMPPE_LOGOUT_RESP:
				Res.log(Res.DEBUG, "�յ� login_resp���ݰ� :����״̬Ϊ " + cr.stat);
				break;

			case CMPP.CMPPE_DELIVER:
				Res.log(Res.DEBUG, "�յ� deliver ���ݰ� :����״̬Ϊ " + cr.stat);

				cmppe_deliver_result cd = (cmppe_deliver_result) cr;
				// ��Ϊ��ִ��ȡ��Ϣ��ʶ
				String msgID = new String(cd.msg_id);
				int status = 0;
				// String msgID="";
				// Res.log(Res.ERROR,"msgid:"+new String(cd.msg_id));
				// 2004��2��11�������˻�ִ������ϢID
				// if(cd.status_from_rpt==1)
				// msgID=new String(cd.msg_id).substring(0,16);
				if (cd.status_rpt == 1)
					status = cd.stat;
				sendNumber = new String(cd.src_addr, 0, 11); // Դ�ֻ����룻
				recvNumber = new String(cd.dst_addr, 0, 11); // Ŀ���ֻ����룻
				// ���������ݽ��������
				int sm_len = cd.sm_len;

				if (sm_len < 0)
					sm_len = 256 + sm_len;
				int data_code = cd.data_coding;

				// Res.log(Res.ERROR,"data_code"+data_code);
				switch (data_code) {
				case 21: {
					smtxt = Util.getASCII(cd.short_msg, sm_len);
					break;
				}
				case 15: {
					smtxt = new String(cd.short_msg, 0, sm_len, "UnicodeBig");
					break;
				}
				case 0: {
					smtxt = new String(cd.short_msg, 0, sm_len, "GBK");
					break;
				}
				case 4: {
					smtxt = Util.getASCII(cd.short_msg, sm_len);
					break;
				}
				default:
					smtxt = new String(cd.short_msg, 0, sm_len, "UnicodeBig");
					break;
				}
				// 2004��2��11��������msgID����
				cmpp.cmpp_send_deliver_resp(con, cd.seq, cd.stat);
				// Res.log(Res.DEBUG, "�յ����ֻ�����Ϊ��" + sendNumber + " �յ�����Ϣ����Ϊ��" +
				// smtxt);
				insertTable(status, sendNumber, recvNumber, smtxt, mediaInfo
						.getMediaId(), msgID, data_code);
				break;

			case CMPP.CMPPE_CANCEL_RESP:
				Res.log(Res.DEBUG, "�յ� cancel ���ݰ� :����״̬Ϊ " + cr.stat);

			case CMPP.CMPPE_ACTIVE_RESP:
				Res.log(Res.DEBUG, "�յ� active_resp ���ݰ� :����״̬Ϊ " + cr.stat);
				break;
			case CMPP.CMPPE_ACTIVE:
				Res.log(Res.DEBUG, "�յ� active ���ݰ� :����״̬Ϊ " + cr.stat);
				break;

			default:
				Res.log(Res.DEBUG, "�յ� δ֪�����ݰ� :����״̬Ϊ " + cr.stat);
				break;
			}

		} catch (UnknownPackException e) {
			e.printStackTrace();
			result = -8001;
			Res.log(Res.DEBUG, "�յ���δ֪��ʽ�İ�");
		} catch (DeliverFailException e) {
			e.printStackTrace();
			result = -8000;
			Res.log(Res.DEBUG, "���������쳣");
			errorLogout();
		} catch (IOException e) {
			e.printStackTrace();
			result = -8000;
			Res.log(Res.DEBUG, "���������쳣");
			errorLogout();
		}
		return result;
	}

	private void errorLogout() {
		Res.log(Res.INFO, "��ʼ�����ƻ��������˳��������������");
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

	class Test_Conn_to_MOBILE implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			errorLogout();
		};
	}

}