/**
 * <p>Title: ChannelManager.java</p>
 * <p>Description:
 *    �����������������Ķ���
 *    ����ɨ���������Լ�������������̵߳Ķ�ʱ��������ʱ��ֹ
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering�� Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering�� Ltd.</p>
 * Date        Author      Changes
 * NOV. 5 2003   ��־��       Created
 * @version 1.0
 */
package com.nci.ums.channel.channelmanager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.nci.ums.channel.ChannelIfc;
import com.nci.ums.channel.DefaultChannelIfcImpl;
import com.nci.ums.channel.channelinfo.MediaInfo;
import com.nci.ums.channel.outchannel.NCIOutChannel_V3;
import com.nci.ums.util.Res;
import com.nci.ums.util.Util;

/**
 * 2007.10.20��UMS3.0������outMediaInfoHash��inMediaInfoHash��getter��setter����
 * 2007.11.10��UMS3.0��������MediaStyle��
 * ��outMediaInfoHash��inMediaInfoHash�н���media��������MediaStyle����
 * 
 * @author Qil.Wong
 * 
 */
public class ChannelManager implements Runnable {

	// ��ѯ��sql
	private static final String sql = "select * from UMS_MEDIA where MEDIA_TYPE=1 and MEDIA_STATUSFLAG=0  order by MEDIA_ID";
	private static final String sql2 = "select * from UMS_MEDIA where MEDIA_TYPE=0 and MEDIA_STATUSFLAG=0 order by MEDIA_ID";

	// ���������Ⲧ������map
	private static Map outMediaInfoHash;
	// �������沦��������map
	private static Map  inMediaInfoHash;
	// �̱߳���
	Thread runner;
	
	boolean stop = false;

	// private EmailServer emailServer = new EmailServer();

	/*
	 * ���캯�� �������±�־���˳���־ �����������沦��Ͳ�����Ϣ������map
	 */

	public ChannelManager() {

		outMediaInfoHash = new ConcurrentHashMap();
		inMediaInfoHash = new ConcurrentHashMap();
		Res.log(Res.INFO, "��������������");
	}

	/*
	 * ������Ϣ�����߳���������
	 */
	public void start() {
		runner = new Thread(this, "ChannelManager");
		runner.start();
		Res.log(Res.INFO, "���������߳�����");
	}

	/*
	 * ������Ϣ�����߳�ֹͣ����
	 */
	public void stop() {
		if (runner != null) {
			runner.interrupt();
			stop = true;
			runner = null;
		}
		stopChannels();
		Res.log(Res.INFO, "���������߳�ֹͣ");
	}

	/*
	 * �߳�run���� ��Ҫִ�еĹ�����ѭ���������е�������������ֹͣ������ ֻ�����̱߳��жϻ����˳���־�������Ժ󣬲Ż��˳����ѭ�����������߳�
	 */
	public void run() {
		while (!Thread.interrupted() && !stop) {
			// clear();
			scanTable();

			while (!Thread.interrupted()&& !stop ) {
				startChannel();
			}
		}
	}

	/*
	 * ��������sleeptime�ĺ��� ͨ����ǰʱ���������ʼʱ��ͽ���ʱ��ıȽϵ��� ע�����������ʱ���������е����⣡
	 */
	private int getSleepTime(MediaInfo mediaInfo) {
		int current = Util.getCurrentTime("HHmmss");
		int currentTime = Util.getTime(current);
		int time = 0;
		int startWorkTime = Util.getTime(mediaInfo.getStartWorkTime());
		int endWorkTime = Util.getTime(mediaInfo.getEndWorkTime());

		// Res.log(Res.DEBUG, "startWorkTime=" + startWorkTime + " endWorkTIme="
		// + endWorkTime + " currentTime=" + currentTime);
		// ����ͬһ��������
		if (startWorkTime < endWorkTime) {
			if (currentTime >= startWorkTime && currentTime < endWorkTime) {
				time = endWorkTime - currentTime;
			} else if (Util.DAYTIME >= currentTime
					&& currentTime >= endWorkTime) {
				time = Util.DAYTIME - currentTime + startWorkTime;
			} else if (currentTime <= startWorkTime && currentTime >= 0) {
				time = startWorkTime - currentTime;
			}
		} else if (startWorkTime > endWorkTime) {
			// ˵��û��������ʱ����
			if (currentTime >= endWorkTime && currentTime < startWorkTime) {
				time = startWorkTime - currentTime;
			} else if (currentTime >= startWorkTime
					&& currentTime <= Util.DAYTIME) {
				time = Util.DAYTIME - currentTime + endWorkTime;
			} else if (currentTime <= endWorkTime && currentTime >= 0) {
				time = endWorkTime - currentTime;
			}
		} else {
			time = Util.DAYTIME;
		}
		// Res.log(Res.DEBUG, "currentTime=" + current + " beginTime="
		// + mediaInfo.getStartWorkTime() + " endTime="
		// + mediaInfo.getEndWorkTime() + " time=" + time);
		return time;
	}

	/*
	 * ɨ�������ĺ����� ��Ҫɨ�貦���Ͳ�������������
	 */
	public void startChannel() {
		// ������¼������������Ժ󣬿���sleep�೤ʱ�䣬�Լ�����ѵ������
		int sleepTime = Util.DAYTIME;
		int tempTime = Util.DAYTIME;

		sleepTime = checkChannel(outMediaInfoHash);
		tempTime = checkChannel(inMediaInfoHash);
		// Res.log(Res.DEBUG, "sleepTime=" + sleepTime + " tempTime=" +
		// tempTime);

		// if (!emailServer.isAlive()) {
		// Res.log(Res.ERROR, "emailServer ��������!");
		// // emailServer.start();
		// }

		if (sleepTime > tempTime) {
			sleepTime = tempTime;
		}
		// Res.log(Res.INFO, "ɨ���������߳̽�˯��5����");

		try {
			Thread.sleep(5 * 60 * 1000);
		} catch (InterruptedException e) {
			Res.log(Res.INFO, "�߳�˯�߱��жϣ�");
		}
		// Res.log(Res.DEBUG, "����˯�ߣ�");
	}

	/*
	 * ���ڴ����ݿ���ɨ��media��service�� �õ��Ⲧ�����Ͳ�����������ϸ��Ϣ
	 */
	public void scanTable() {
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = Res.getConnection();
			stmt = conn.createStatement();
			// ������������Ϣ�Ĳ�ѯ��
			Res.log(Res.DEBUG, "sql=" + sql);
			rs1 = stmt.executeQuery(sql);
			rs1.setFetchSize(10);
			// �����ݼ����е���Ϣ�����ŵ�map��
			parseChannelInfo(outMediaInfoHash, rs1);
			// ������������Ϣ�Ĳ�ѯ��
			Res.log(Res.DEBUG, "sql2=" + sql2);
			rs2 = stmt.executeQuery(sql2);
			rs2.setFetchSize(10);
			// �����ݼ����е���Ϣ�����ŵ�map��
			parseChannelInfo(inMediaInfoHash, rs2);
		} catch (SQLException e) {
			e.printStackTrace();
			Res.log(Res.ERROR, "1091", e.getMessage());
			Res.logExceptionTrace(e);
		} finally {
			try {
				if (rs1 != null) {
					rs1.close();
				}
				if (rs2 != null) {
					rs2.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				Res.log(Res.ERROR, "1091", e.getMessage());
				Res.logExceptionTrace(e);
			}
		}
	}


	public static ChannelIfc constructMediaChannel(MediaInfo mediaInfo)
			throws ClassNotFoundException, IllegalArgumentException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {
		ChannelIfc threadObject = null;
		System.out.println(mediaInfo.getClassName());
		Class tempClass = Class.forName(mediaInfo.getClassName());
		Constructor[] constructor = tempClass.getConstructors();
		for (int i = 0; i < constructor.length; i++) {
			Class[] parameters = constructor[i].getParameterTypes();
			if (parameters.length == 1 && parameters[0].equals(MediaInfo.class)) {
				threadObject = (ChannelIfc) constructor[i]
						.newInstance(new Object[] { mediaInfo });
				break;
			}
		}
		return threadObject;
	}

	/*
	 * ѭ������������У�����ﵽ������ʱ�䣬�������߳���������������˽���ʱ�䣬�������߳�ͣ���� ����Ϊ����������߳�Ӧ��sleep��ʱ�䡣
	 */
	private int checkChannel(Map mediaInfoHash) {
		Iterator keyIterator = mediaInfoHash.keySet().iterator();
		MediaInfo mediaInfo = null;
		int systemTime = Util.getCurrentTime("HHmmss");

		// ������¼������������Ժ󣬿���sleep�೤ʱ�䣬�Լ�����ѭ������
		int sleepTime = 60 * 60 * 24;
		while (keyIterator.hasNext()) {
			String mediaId = (String) keyIterator.next();
			mediaInfo = (MediaInfo) mediaInfoHash.get(mediaId);
			// Res.log(Res.DEBUG, "mediaInfo: " + mediaInfo);
			// ��ʾ����Ӧ�������̵߳�ʱ��.
			// ��ʾ�����������ʹ���С�
			if (mediaInfo.getStatus() == 1) {
				continue;
			}
			// ���û�п��죡��
			if (mediaInfo.getStartWorkTime() <= mediaInfo.getEndWorkTime()) {
				if ((systemTime >= mediaInfo.getStartWorkTime() && systemTime < mediaInfo
						.getEndWorkTime())
						|| (mediaInfo.getStartWorkTime() == 0 && mediaInfo
								.getEndWorkTime() == 0)) {
					ChannelIfc threadObject = mediaInfo.getChannelObject();
					// ��ʾ��û�в���������̶߳��� ��Ҫ���ڲ�����
					if (threadObject == null
							|| threadObject.getClass().equals(
									DefaultChannelIfcImpl.class)) {
						try {
							threadObject = ChannelManager
									.constructMediaChannel(mediaInfo);
							mediaInfo.setChannelObject(threadObject);
							if (threadObject == null) {
								Res
										.log(
												Res.ERROR,
												"["
														+ mediaInfo
																.getMediaId()
														+ "]"
														+ mediaInfo
																.getMediaName()
														+ "����Ӧ�������๹�캯������û���ҵ���MediaInfo�����Ĺ��캯����");
							} else {
								threadObject.start();
							}
						} catch (Exception e) {
							e.printStackTrace();
							Res.log(Res.ERROR, "����" + mediaInfo.getMediaName()
									+ "��������ʧ�ܣ�" + e.getMessage());
							Res.logExceptionTrace(e);
						}
					} else if (threadObject.getThreadState() == false) {
						// �����̡߳�
						if (threadObject.getClass().equals(
								NCIOutChannel_V3.class)) {
							try {
								threadObject = ChannelManager
										.constructMediaChannel(mediaInfo);
								mediaInfo.setChannelObject(threadObject);
							} catch (Exception e) {
								Res.log(Res.ERROR, "����"
										+ mediaInfo.getMediaName() + "��������ʧ�ܣ�"
										+ e.getMessage());
								Res.logExceptionTrace(e);
							}
						}
						threadObject.start();
						/*
						 * UMSThread umsThread=new
						 * UMSThread("UMS����Ϣƽ̨",mediaInfo
						 * .getMeidaName(),Util.getCurrentTimeStr
						 * ("yyyyMMddHHmmss"),"������",threadObject,"");
						 * Res.getUMSThreads().add(umsThread);
						 */

						Res.log(Res.INFO, "����" + mediaInfo.getMediaName()
								+ "�����߳�");
					}
				} else if (systemTime >= mediaInfo.getEndWorkTime()) {
					ChannelIfc threadObject = mediaInfo.getChannelObject();
					// ֹͣ����̶߳���
					if (threadObject != null
							&& threadObject.getThreadState() == true) {
						threadObject.stop();
						Res.log(Res.INFO, "ֹͣ" + mediaInfo.getMediaName()
								+ "�����߳�");
					}
				}
			}
			// ��ʾ���ֿ���������
			if (mediaInfo.getStartWorkTime() > mediaInfo.getEndWorkTime()) {
				if ((systemTime >= mediaInfo.getStartWorkTime() && systemTime < 246060)
						|| (systemTime < mediaInfo.getEndWorkTime() && systemTime > 0)) {
					ChannelIfc threadObject = mediaInfo.getChannelObject();
					// ��ʾ��û�в���������̶߳��� ��Ҫ���ڲ�����
					if (threadObject == null
							|| threadObject.getClass().equals(
									DefaultChannelIfcImpl.class)) {
						try {
							threadObject = ChannelManager
									.constructMediaChannel(mediaInfo);
							mediaInfo.setChannelObject(threadObject);
							Res.log(Res.INFO, "����" + mediaInfo.getMediaName()
									+ "��������ɹ���");
						} catch (Exception e) {
							e.printStackTrace();
							Res.log(Res.ERROR, "����" + mediaInfo.getMediaName()
									+ "��������ʧ�ܣ�");
							Res.logExceptionTrace(e);
						}
						if (threadObject != null)
							threadObject.start();

					} else if (threadObject.getThreadState() == false) {
						// �����̡߳�
						if (threadObject.getClass().equals(
								NCIOutChannel_V3.class)) {
							try {
								threadObject = ChannelManager
										.constructMediaChannel(mediaInfo);
								mediaInfo.setChannelObject(threadObject);
							} catch (Exception e) {
								Res.log(Res.ERROR, "����"
										+ mediaInfo.getMediaName() + "��������ʧ�ܣ�"
										+ e.getMessage());
								Res.logExceptionTrace(e);
							}
						}
						threadObject.start();
						/*
						 * UMSThread umsThread=new
						 * UMSThread("UMS����Ϣƽ̨",mediaInfo
						 * .getMeidaName(),Util.getCurrentTimeStr
						 * ("yyyyMMddHHmmss"),"������",threadObject,"");
						 * Res.getUMSThreads().add(umsThread);
						 */
						Res.log(Res.INFO, "����" + mediaInfo.getMediaName()
								+ "�����߳�");
					}
				} else {
					ChannelIfc threadObject = mediaInfo.getChannelObject();
					// ֹͣ����̶߳���
					if (threadObject != null
							&& threadObject.getThreadState() == true) {
						threadObject.stop();
						Res.log(Res.INFO, "ֹͣ" + mediaInfo.getMediaName()
								+ "�����߳�");
					}
				}
			}

			// ���ϴεõ���sleepTime���Ƚϣ���¼��С��sleeptime��
			if (getSleepTime(mediaInfo) < sleepTime) {
				sleepTime = getSleepTime(mediaInfo);
			}
		}
		return sleepTime;
	}

	/*
	 * ����������ڽ������ݼ����е�������Ϣ�� �����Ժ����Ϣ�ŵ�map�С�
	 */
	private void parseChannelInfo(Map mediaInfoHash, ResultSet rs) {
		MediaInfo mediaInfo = null;
		String mediaId = null;
		try {
			while (rs.next()) {
				mediaId = rs.getString("MEDIA_ID");
				// ��ʾ��ͬһ��meidaId,��ܶ����ݲ���Ҫȡ.
				String mediaName = rs.getString("MEDIA_NAME");

				String className = rs.getString("MEDIA_CLASS");
				String ip = rs.getString("MEDIA_IP");
				String loginName = rs.getString("MEDIA_LOGINNAME");
				String loginPassword = rs.getString("MEDIA_LOGINPASSWORD");

				int port = rs.getInt("MEDIA_PORT");
				int timeOut = rs.getInt("MEDIA_TIMEOUT");
				int repeatTimes = rs.getInt("MEDIA_REPEATTIMES");
				int startWorkTime = rs.getInt("MEDIA_STARTWORKTIME");
				int endWorkTime = rs.getInt("MEDIA_ENDWORKTIME");
				int type = rs.getInt("MEDIA_TYPE");
				int sleepTime = rs.getInt("MEDIA_SLEEPTIME");
				int status = rs.getInt("MEDIA_STATUSFLAG");

				mediaInfo = new MediaInfo(mediaId, mediaName, className, ip,
						port, timeOut, repeatTimes, startWorkTime, endWorkTime,
						type, loginName, loginPassword, sleepTime, status);
				// @Since UMS3.0
				mediaInfo.setMediaStyle(rs.getInt("MEDIA_STYLE"));
				mediaInfo.setChannelObject(new DefaultChannelIfcImpl());
				// mediaInfoHash.put(mediaId + mediaName, mediaInfo);
				mediaInfoHash.put(mediaId, mediaInfo);
				if (status == 0) {
					Res.log(Res.INFO, mediaName + "��������" + startWorkTime
							+ "��������" + endWorkTime + "ֹͣ������");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			Res.log(Res.ERROR, "1091", e.getMessage());
			Res.logExceptionTrace(e);
		}
	}

	/*
	 * ֹͣ���������߳����еĺ�����
	 */
	private void stopChannels() {
		Iterator it = inMediaInfoHash.values().iterator();
		while (it.hasNext()) {
			MediaInfo mediaInfo = (MediaInfo) it.next();
			ChannelIfc ifc = mediaInfo.getChannelObject();
			// ��ʾ��������Ѿ����������Ҵ�������״̬
			if (ifc != null && ifc.getThreadState() == true) {
				ifc.stop();
			}
		}
		it = outMediaInfoHash.values().iterator();
		while (it.hasNext()) {
			MediaInfo mediaInfo = (MediaInfo) it.next();
			ChannelIfc ifc = mediaInfo.getChannelObject();
			// ��ʾ��������Ѿ����������Ҵ�������״̬
			if (ifc != null && ifc.getThreadState() == true) {
				ifc.stop();
			}
		}
		inMediaInfoHash.clear();
		outMediaInfoHash.clear();
	}

	public static void main(String[] args) {
		ChannelManager dd = new ChannelManager();
		dd.run();
	}

	public static Map getOutMediaInfoHash() {
		return outMediaInfoHash;
	}

	public synchronized static void setOutMediaInfoHash(Map outMediaInfoHash) {
		ChannelManager.outMediaInfoHash = outMediaInfoHash;
	}

	public static Map getInMediaInfoHash() {
		return inMediaInfoHash;
	}

	public synchronized static void setInMediaInfoHash(Map inMediaInfoHash) {
		ChannelManager.inMediaInfoHash = inMediaInfoHash;
	}

}
