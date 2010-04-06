package com.nci.ums.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import com.commerceware.cmpp.CMPP;
import com.commerceware.cmpp.DeliverFailException;
import com.commerceware.cmpp.OutOfBoundsException;
import com.commerceware.cmpp.UnknownPackException;
import com.commerceware.cmpp.cmppe_result;
import com.commerceware.cmpp.conn_desc;
import com.nci.ums.channel.CMPPBean;
import com.nci.ums.channel.channelinfo.MediaInfo;

import edu.emory.mathcs.backport.java.util.concurrent.ConcurrentHashMap;

public class CMPPUtil_V3 {

	// private static conn_desc con;
	// private static CMPP cmpp;
	private static String cmppIP;
	private static int cmppPort;
	private static String cmppLoginName;
	private static String cmppLoginPsw;
	// 连接测试有效性的时间
	private static int activeTime = 0;

	private static Map cmppMap;

	private static byte[] lock = new byte[0];
	


	public static void load() {
		synchronized (lock) {
			InputStream ins = new DynamicUMSStreamReader()
					.getInputStreamFromFile("/resources/cmppDirectChannel.props");
			Properties props = new Properties();
			try {
				props.load(ins);
				ins.close();
			} catch (IOException e) {
				Res.log(Res.ERROR, "读取CMPP配置文件错误。");
				e.printStackTrace();
			}
			
			cmppIP = props.getProperty("cmpp_ip");
			cmppPort = Integer.parseInt(props.getProperty("cmpp_port"));
			cmppLoginName = props.getProperty("cmpp_loginname");
			cmppLoginPsw = props.getProperty("cmpp_loginpsw");
			cmppMap = new ConcurrentHashMap();
		}
	}

	public static synchronized conn_desc getCMPPConn_desc(MediaInfo media) {
		boolean flag = false;
		int i = 0;
		if (cmppMap.get(media.getMediaId()) == null) {
			conn_desc con = new conn_desc();
			CMPP cmpp = new CMPP();
			CMPPBean cmppBean = new CMPPBean();
			cmppBean.setCmpp(cmpp);
			cmppBean.setConn_desc(con);
			cmppMap.put(media.getMediaId(), cmppBean);
			if (i++ == 0)
				Res.log(Res.DEBUG, "UMS开始连接服务器:");
			Res.log(Res.DEBUG, "UMS第" + i + "次连接服务器:");
			try {
				cmpp.cmpp_connect_to_ismg(cmppIP, cmppPort, con);
				flag = true;
			} catch (IOException e) {
				Res.log(Res.DEBUG, "UMS第" + i + "次连接服务器失败！");
				closeConnection(media);
			}
			// 验证合法，开始login
			if (flag == true) {
				Res.log(Res.DEBUG, "UMS第" + i + "次连接服务器成功！");
				Res.log(Res.DEBUG, "UMS开始和服务器验证！");
				flag = login(media);
				if (flag) {
					Res.log(Res.DEBUG, "UMS和服务器验证成功！");

				} else
					Res.log(Res.DEBUG, "UMS和服务器验证失败！");
			}
			// 如果验证身份失败，关闭连接。并且sleep一段时间，再与服务器连接并做验证
			if (flag == false) {
				// Res.log(Res.INFO, "UMS将间隔一分钟后，重新连接服务器");
				closeConnection(media);
				// // sleep一分钟。
				// try {
				// Thread.sleep(60 * 1000);
				// } catch (Exception e) {
				// e.printStackTrace();
				// Res.log(Res.INFO, "线程被中断");
				// pleaseStop();
				// }
			}
		} else {
			if (activeTime > 0) {
				Res.log(Res.DEBUG, "UMS CMPP activetime"
						+ (Util.getUTCTime() - activeTime));
				if (Util.getUTCTime() - activeTime >= 1 * 54) {
					activeTime = Util.getUTCTime();
					try {
						CMPPBean cmppBean = (CMPPBean) cmppMap.get(media
								.getMediaId());
						if (cmppBean != null) {
							CMPP cmpp = cmppBean.getCmpp();
							conn_desc con = cmppBean.getConn_desc();
							cmpp.cmpp_active_test(con);
							cmppe_result cr = cmpp.readResPack(con);
							if (cr.stat == 0) {
								flag = true;
							} else {
								flag = false;
								con = null;
							}
						}
					} catch (IOException e) {
						Res.log(Res.ERROR, "UMS CMPP获取连接时出现网络异常"
								+ e.getMessage());
						e.printStackTrace();
					} catch (UnknownPackException e) {
						Res.log(Res.ERROR, "UMS CMPP获取连接时收到了未知格式的包"
								+ e.getMessage());
						e.printStackTrace();
					} catch (DeliverFailException e) {
						Res.log(Res.ERROR, "UMS CMPP获取连接时出现网络异常"
								+ e.getMessage());
						e.printStackTrace();
					}
				} else {
					flag = true;
				}

			} else {
				activeTime = Util.getUTCTime();
				flag = true;
			}

		}
		CMPPBean cmppBean = (CMPPBean) cmppMap.get(media.getMediaId());
		if (cmppBean == null) {
			return null;
		} else {
			return cmppBean.getConn_desc();
		}
	}

	public static CMPP getCMPP(MediaInfo media) {
		CMPPBean cmppBean = (CMPPBean) cmppMap.get(media.getMediaId());
		if (cmppBean != null) {
			if (cmppBean.getConn_desc() == null) {
				Res.log(Res.DEBUG, "UMS CMPP 连接已关闭");
				return null;
			} else {
				return cmppBean.getCmpp();
			}
		}
		return null;
	}

	private static boolean login(MediaInfo media) {
		try {
			CMPPBean cmppBean = (CMPPBean) cmppMap.get(media.getMediaId());
			if (cmppBean != null) {
				CMPP cmpp = cmppBean.getCmpp();
				conn_desc con = cmppBean.getConn_desc();
				if (con == null)
					con = new conn_desc();
				cmpp.cmpp_login(con, cmppLoginName, cmppLoginPsw, (byte) 0,
						0x20, Util.getUTCTime());
				cmppe_result cr = cmpp.readResPack(con);
				if (cr.pack_id == CMPP.CMPPE_LOGIN_RESP) {
					if (cr.stat == 0)
						return true;
				} else {
					Res.log(Res.ERROR, "UMS CMPP登录时收到的包不是登录回应:"
							+ CMPP.CMPPE_LOGIN_RESP + ",而是：" + cr.pack_id);
				}
			}
		} catch (IOException e) {
			Res.log(Res.ERROR, "UMS CMPP登录出现网络异常" + e.getMessage());
			e.printStackTrace();
		} catch (OutOfBoundsException e) {
			Res.log(Res.ERROR, "UMS CMPP登录参数超出规定范围！" + e.getMessage());
			e.printStackTrace();
		} catch (UnknownPackException e) {
			Res.log(Res.ERROR, "UMS CMPP登录收到了未知格式的包" + e.getMessage());
			e.printStackTrace();
		} catch (DeliverFailException e) {
			Res.log(Res.ERROR, "UMS CMPP登录出现网络异常" + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	private static void closeConnection(MediaInfo media) {
		CMPPBean cmppBean = (CMPPBean) cmppMap.get(media.getMediaId());
		if (cmppBean != null) {
			CMPP cmpp = cmppBean.getCmpp();
			conn_desc con = cmppBean.getConn_desc();
			if (con != null) {
				logout(media);
				cmpp.cmpp_disconnect_from_ismg(con);
				con = null;
				cmppMap.remove(media.getMediaId());
			}
		}
	}

	private static boolean logout(MediaInfo media) {
		try {
			CMPPBean cmppBean = (CMPPBean) cmppMap.get(media.getMediaId());
			if (cmppBean != null) {
				CMPP cmpp = cmppBean.getCmpp();
				conn_desc con = cmppBean.getConn_desc();
				cmpp.cmpp_logout(con);
				con = null;
				// readPa(con);
			}
		} catch (IOException e) {
			Res.log(Res.ERROR, "UMS CMPP登出异常" + e.getMessage());
			e.printStackTrace();
		}
		return true;
	}

	public static void errorLogout(MediaInfo media) {
		Res.log(Res.DEBUG, "开始启动计划工作，UMS退出与CMPP服务器的连接");
		CMPPBean cmppBean = (CMPPBean) cmppMap.get(media.getMediaId());
		try {
			if (cmppBean != null) {
				CMPP cmpp = cmppBean.getCmpp();
				conn_desc con = cmppBean.getConn_desc();
				cmpp.cmpp_logout(con);
				Res.log(Res.DEBUG, "正常退出");
			}
		} catch (Exception ee) {
			Res.log(Res.DEBUG, "退出异常");
		} finally {
			if (cmppBean != null) {
				cmppBean.getCmpp().cmpp_disconnect_from_ismg(cmppBean.getConn_desc());
				cmppBean.setConn_desc(null);
			}
		}
	}
}
