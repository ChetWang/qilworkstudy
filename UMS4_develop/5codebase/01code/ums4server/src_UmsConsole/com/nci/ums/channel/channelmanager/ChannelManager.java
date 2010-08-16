/**
 * <p>Title: ChannelManager.java</p>
 * <p>Description:
 *    用来管理所有渠道的对象
 *    负责扫描渠道表以及负责各个渠道线程的定时启动，定时终止
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering， Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering， Ltd.</p>
 * Date        Author      Changes
 * NOV. 5 2003   张志勇       Created
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
 * 2007.10.20：UMS3.0，新增outMediaInfoHash和inMediaInfoHash的getter、setter方法
 * 2007.11.10：UMS3.0，新增了MediaStyle，
 * 在outMediaInfoHash和inMediaInfoHash中将各media对象新增MediaStyle属性
 * 
 * @author Qil.Wong
 * 
 */
public class ChannelManager implements Runnable {

	// 查询的sql
	private static final String sql = "select * from UMS_MEDIA where MEDIA_TYPE=1 and MEDIA_STATUSFLAG=0  order by MEDIA_ID";
	private static final String sql2 = "select * from UMS_MEDIA where MEDIA_TYPE=0 and MEDIA_STATUSFLAG=0 order by MEDIA_ID";

	// 用来保存外拨渠道的map
	private static Map outMediaInfoHash;
	// 用来保存拨入渠道的map
	private static Map  inMediaInfoHash;
	// 线程变量
	Thread runner;
	
	boolean stop = false;

	// private EmailServer emailServer = new EmailServer();

	/*
	 * 构造函数 产生更新标志和退出标志 产生用来保存拨入和拨出信息的渠道map
	 */

	public ChannelManager() {

		outMediaInfoHash = new ConcurrentHashMap();
		inMediaInfoHash = new ConcurrentHashMap();
		Res.log(Res.INFO, "渠道管理对象产生");
	}

	/*
	 * 渠道信息管理线程启动函数
	 */
	public void start() {
		runner = new Thread(this, "ChannelManager");
		runner.start();
		Res.log(Res.INFO, "渠道管理线程启动");
	}

	/*
	 * 渠道信息管理线程停止函数
	 */
	public void stop() {
		if (runner != null) {
			runner.interrupt();
			stop = true;
			runner = null;
		}
		stopChannels();
		Res.log(Res.INFO, "渠道管理线程停止");
	}

	/*
	 * 线程run函数 主要执行的工作有循环管理所有的渠道的启动和停止工作。 只有在线程被中断或者退出标志被设置以后，才会退出这个循环，结束本线程
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
	 * 用来计算sleeptime的函数 通过当前时间和渠道开始时间和结束时间的比较得来 注意这个函数的时间计算好象还有点问题！
	 */
	private int getSleepTime(MediaInfo mediaInfo) {
		int current = Util.getCurrentTime("HHmmss");
		int currentTime = Util.getTime(current);
		int time = 0;
		int startWorkTime = Util.getTime(mediaInfo.getStartWorkTime());
		int endWorkTime = Util.getTime(mediaInfo.getEndWorkTime());

		// Res.log(Res.DEBUG, "startWorkTime=" + startWorkTime + " endWorkTIme="
		// + endWorkTime + " currentTime=" + currentTime);
		// 这是同一天的情况。
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
			// 说明没有在启动时间内
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
	 * 扫描渠道的函数， 需要扫描拨出和拨入两个渠道。
	 */
	public void startChannel() {
		// 用来记录做完这个函数以后，可以sleep多长时间，以减少轮训次数。
		int sleepTime = Util.DAYTIME;
		int tempTime = Util.DAYTIME;

		sleepTime = checkChannel(outMediaInfoHash);
		tempTime = checkChannel(inMediaInfoHash);
		// Res.log(Res.DEBUG, "sleepTime=" + sleepTime + " tempTime=" +
		// tempTime);

		// if (!emailServer.isAlive()) {
		// Res.log(Res.ERROR, "emailServer 重新启动!");
		// // emailServer.start();
		// }

		if (sleepTime > tempTime) {
			sleepTime = tempTime;
		}
		// Res.log(Res.INFO, "扫描渠道的线程将睡眠5分钟");

		try {
			Thread.sleep(5 * 60 * 1000);
		} catch (InterruptedException e) {
			Res.log(Res.INFO, "线程睡眠被中断！");
		}
		// Res.log(Res.DEBUG, "结束睡眠！");
	}

	/*
	 * 用于从数据库中扫描media和service表 得到外拨渠道和拨入渠道的详细信息
	 */
	public void scanTable() {
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = Res.getConnection();
			stmt = conn.createStatement();
			// 做拨出渠道信息的查询。
			Res.log(Res.DEBUG, "sql=" + sql);
			rs1 = stmt.executeQuery(sql);
			rs1.setFetchSize(10);
			// 把数据集合中的信息解析放到map中
			parseChannelInfo(outMediaInfoHash, rs1);
			// 做拨入渠道信息的查询。
			Res.log(Res.DEBUG, "sql2=" + sql2);
			rs2 = stmt.executeQuery(sql2);
			rs2.setFetchSize(10);
			// 把数据集合中的信息解析放到map中
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
	 * 循环检查渠道队列，如果达到了启动时间，则把这个线程启动，如果超过了结束时间，则把这个线程停掉。 返回为计算出来的线程应该sleep的时间。
	 */
	private int checkChannel(Map mediaInfoHash) {
		Iterator keyIterator = mediaInfoHash.keySet().iterator();
		MediaInfo mediaInfo = null;
		int systemTime = Util.getCurrentTime("HHmmss");

		// 用来记录做完这个函数以后，可以sleep多长时间，以减少轮循次数。
		int sleepTime = 60 * 60 * 24;
		while (keyIterator.hasNext()) {
			String mediaId = (String) keyIterator.next();
			mediaInfo = (MediaInfo) mediaInfoHash.get(mediaId);
			// Res.log(Res.DEBUG, "mediaInfo: " + mediaInfo);
			// 表示到了应该启动线程的时间.
			// 表示这个渠道不在使用中。
			if (mediaInfo.getStatus() == 1) {
				continue;
			}
			// 如果没有跨天！！
			if (mediaInfo.getStartWorkTime() <= mediaInfo.getEndWorkTime()) {
				if ((systemTime >= mediaInfo.getStartWorkTime() && systemTime < mediaInfo
						.getEndWorkTime())
						|| (mediaInfo.getStartWorkTime() == 0 && mediaInfo
								.getEndWorkTime() == 0)) {
					ChannelIfc threadObject = mediaInfo.getChannelObject();
					// 表示还没有产生过这个线程对象， 需要现在产生。
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
														+ "所对应的渠道类构造函数有误，没有找到带MediaInfo参数的构造函数！");
							} else {
								threadObject.start();
							}
						} catch (Exception e) {
							e.printStackTrace();
							Res.log(Res.ERROR, "产生" + mediaInfo.getMediaName()
									+ "渠道对象失败！" + e.getMessage());
							Res.logExceptionTrace(e);
						}
					} else if (threadObject.getThreadState() == false) {
						// 启动线程。
						if (threadObject.getClass().equals(
								NCIOutChannel_V3.class)) {
							try {
								threadObject = ChannelManager
										.constructMediaChannel(mediaInfo);
								mediaInfo.setChannelObject(threadObject);
							} catch (Exception e) {
								Res.log(Res.ERROR, "产生"
										+ mediaInfo.getMediaName() + "渠道对象失败！"
										+ e.getMessage());
								Res.logExceptionTrace(e);
							}
						}
						threadObject.start();
						/*
						 * UMSThread umsThread=new
						 * UMSThread("UMS短消息平台",mediaInfo
						 * .getMeidaName(),Util.getCurrentTimeStr
						 * ("yyyyMMddHHmmss"),"已启动",threadObject,"");
						 * Res.getUMSThreads().add(umsThread);
						 */

						Res.log(Res.INFO, "启动" + mediaInfo.getMediaName()
								+ "渠道线程");
					}
				} else if (systemTime >= mediaInfo.getEndWorkTime()) {
					ChannelIfc threadObject = mediaInfo.getChannelObject();
					// 停止这个线程对象。
					if (threadObject != null
							&& threadObject.getThreadState() == true) {
						threadObject.stop();
						Res.log(Res.INFO, "停止" + mediaInfo.getMediaName()
								+ "渠道线程");
					}
				}
			}
			// 表示出现跨天的情况。
			if (mediaInfo.getStartWorkTime() > mediaInfo.getEndWorkTime()) {
				if ((systemTime >= mediaInfo.getStartWorkTime() && systemTime < 246060)
						|| (systemTime < mediaInfo.getEndWorkTime() && systemTime > 0)) {
					ChannelIfc threadObject = mediaInfo.getChannelObject();
					// 表示还没有产生过这个线程对象， 需要现在产生。
					if (threadObject == null
							|| threadObject.getClass().equals(
									DefaultChannelIfcImpl.class)) {
						try {
							threadObject = ChannelManager
									.constructMediaChannel(mediaInfo);
							mediaInfo.setChannelObject(threadObject);
							Res.log(Res.INFO, "产生" + mediaInfo.getMediaName()
									+ "渠道对象成功！");
						} catch (Exception e) {
							e.printStackTrace();
							Res.log(Res.ERROR, "产生" + mediaInfo.getMediaName()
									+ "渠道对象失败！");
							Res.logExceptionTrace(e);
						}
						if (threadObject != null)
							threadObject.start();

					} else if (threadObject.getThreadState() == false) {
						// 启动线程。
						if (threadObject.getClass().equals(
								NCIOutChannel_V3.class)) {
							try {
								threadObject = ChannelManager
										.constructMediaChannel(mediaInfo);
								mediaInfo.setChannelObject(threadObject);
							} catch (Exception e) {
								Res.log(Res.ERROR, "产生"
										+ mediaInfo.getMediaName() + "渠道对象失败！"
										+ e.getMessage());
								Res.logExceptionTrace(e);
							}
						}
						threadObject.start();
						/*
						 * UMSThread umsThread=new
						 * UMSThread("UMS短消息平台",mediaInfo
						 * .getMeidaName(),Util.getCurrentTimeStr
						 * ("yyyyMMddHHmmss"),"已启动",threadObject,"");
						 * Res.getUMSThreads().add(umsThread);
						 */
						Res.log(Res.INFO, "启动" + mediaInfo.getMediaName()
								+ "渠道线程");
					}
				} else {
					ChannelIfc threadObject = mediaInfo.getChannelObject();
					// 停止这个线程对象。
					if (threadObject != null
							&& threadObject.getThreadState() == true) {
						threadObject.stop();
						Res.log(Res.INFO, "停止" + mediaInfo.getMediaName()
								+ "渠道线程");
					}
				}
			}

			// 和上次得到的sleepTime做比较，记录最小的sleeptime。
			if (getSleepTime(mediaInfo) < sleepTime) {
				sleepTime = getSleepTime(mediaInfo);
			}
		}
		return sleepTime;
	}

	/*
	 * 这个函数用于解析数据集合中的渠道信息， 解析以后的信息放到map中。
	 */
	private void parseChannelInfo(Map mediaInfoHash, ResultSet rs) {
		MediaInfo mediaInfo = null;
		String mediaId = null;
		try {
			while (rs.next()) {
				mediaId = rs.getString("MEDIA_ID");
				// 表示是同一个meidaId,则很多数据不需要取.
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
					Res.log(Res.INFO, mediaName + "渠道将在" + startWorkTime
							+ "启动，在" + endWorkTime + "停止工作！");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			Res.log(Res.ERROR, "1091", e.getMessage());
			Res.logExceptionTrace(e);
		}
	}

	/*
	 * 停止各个渠道线程运行的函数。
	 */
	private void stopChannels() {
		Iterator it = inMediaInfoHash.values().iterator();
		while (it.hasNext()) {
			MediaInfo mediaInfo = (MediaInfo) it.next();
			ChannelIfc ifc = mediaInfo.getChannelObject();
			// 表示这个对象已经产生，并且处于运行状态
			if (ifc != null && ifc.getThreadState() == true) {
				ifc.stop();
			}
		}
		it = outMediaInfoHash.values().iterator();
		while (it.hasNext()) {
			MediaInfo mediaInfo = (MediaInfo) it.next();
			ChannelIfc ifc = mediaInfo.getChannelObject();
			// 表示这个对象已经产生，并且处于运行状态
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
