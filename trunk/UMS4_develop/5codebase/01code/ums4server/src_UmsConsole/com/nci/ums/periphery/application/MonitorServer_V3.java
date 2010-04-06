package com.nci.ums.periphery.application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.nci.ums.basic.UMSModule;
import com.nci.ums.channel.channelinfo.QuitLockFlag;
import com.nci.ums.channel.outchannel.EmailOutChannel_V3;
import com.nci.ums.exception.UMSConnectionException;
import com.nci.ums.periphery.exception.ApplicationException;
import com.nci.ums.util.PropertyUtil;
import com.nci.ums.util.Res;
import com.nci.ums.v3.message.UMSMsg;
import com.nci.ums.v3.message.basic.BasicMsg;
import com.nci.ums.v3.message.basic.MsgContent;
import com.nci.ums.v3.message.basic.Participant;

public class MonitorServer_V3 implements UMSModule,Runnable {

	// 全部线程退出标志（共享变量）
	protected QuitLockFlag quitFlag;
	// 本线程退出标志
	protected boolean stop = false;

	public static final String ChannelCheckSQL = "SELECT COUNT(*) FROM UMS_SEND_READY WHERE STATUSFLAG="
			+ UMSMsg.UMSMSG_STATUS_VALID
			+ " AND TIMESETFLAG="
			+ BasicMsg.BASICMSG_SENDTIME_NOCUSTOM;

	private static MonitorServer_V3 monitor_v3;

	private MonitorServer_V3() {
		
	}

	public synchronized static MonitorServer_V3 getInstance() {
		if (monitor_v3 == null) {
			monitor_v3 = new MonitorServer_V3();
		}
		return monitor_v3;
	}
	
	public void startModule(){
		quitFlag = QuitLockFlag.getInstance();
		Res.log(Res.INFO, "UMS3.0应用程序监控对象产生！");
		Thread t = new Thread(this);
		t.setName("MonitorServer");
		t.start();
	}

	public void stopModule() {
		this.stop = true; // Set the stop flag
		
		Res.log(Res.INFO, "MonitorServer退出服务！");
	}

	private void scanTable() throws UMSConnectionException {
		Connection conn = null;
		int rows = 0;
		try {
			conn = Res.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(ChannelCheckSQL);
			while (rs.next()) {
				rows = rs.getInt(1);
			}
			stmt.close();
			rs.close();
			if (rows > 2000)
				try {
					sendWarning("3001", "UMS3.0消息系统可能已经阻塞，请检查原因：", PropertyUtil
							.getEmailProperty().getProperty("UMSAdminEmail"));
				} catch (ApplicationException e) {
					e.printStackTrace();
				}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public int sendWarning(String title, String msg, String destEmail)
			throws ApplicationException {

		boolean result = false;
		// AppInfo appInfo = new AppInfo();
		// if (destEmail == null)
		// appInfo.setEmail(PropertyUtil.getUMSAdminEmail());
		// else
		// appInfo.setEmail(destEmail);
		// Send send = null;
		// // 构造发给应用程序数据的对象
		// send = new EMailAppChannel();
		// send.initInterface(appInfo);
		// result = send.receiveMessage("", "", title, "", 0, "", "", "", "",
		// "UMS2.0监控通知您：" + msg);
		Properties emailProp = PropertyUtil.getEmailProperty();
		UMSMsg[] msgs = new UMSMsg[1];
		msgs[0] = new UMSMsg();
		Participant sender = new Participant(emailProp.getProperty("loginName")
				+ "@" + emailProp.getProperty("domain"),
				Participant.PARTICIPANT_MSG_FROM,
				Participant.PARTICIPANT_ID_EMAIL);
		Participant[] receiver = new Participant[1];
		receiver[0] = new Participant(destEmail,
				Participant.PARTICIPANT_MSG_TO,
				Participant.PARTICIPANT_ID_EMAIL);
		BasicMsg basic = new BasicMsg();
		msgs[0].setBasicMsg(basic);
		basic.setSender(sender);
		basic.setReceivers(receiver);
		MsgContent content = new MsgContent(title, msg);
		basic.setMsgContent(content);
		new EmailOutChannel_V3().sendViaChannel(msgs);
		if (result)
			return 0;
		else
			return 1;
	}

	/**
	 * 发送告警信息给UMS管理员
	 * @param title
	 * @param msg
	 * @return
	 * @throws ApplicationException
	 */
	public int sendWarningToAdmin(String title, String msg)
			throws ApplicationException {
		String dest = PropertyUtil.getEmailProperty().getProperty(
				"UMSAdminEmail");
		return sendWarning(title, msg, dest);
	}

	public void run() {
		try {
			scanTable();
		} catch (UMSConnectionException e) {
			e.printStackTrace();
		}
	}


}
