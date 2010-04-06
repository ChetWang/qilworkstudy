/**
 * <p>Title: MediaInfo.java</p>
 * <p>Description:
 *    这个类主要用来保存各个渠道的信息，包括拨入渠道和拨出渠道
 *    信息的内容从数据库表中读出，供渠道管理线程使用
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering， Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering， Ltd.</p>
 * Date        Author      Changes
 * NOV. 5 2003   张志勇      Created
 * @version 1.0
 */

package com.nci.ums.channel.channelinfo;

import com.nci.ums.channel.*;

public class MediaInfo {
  private String mediaId;
  private String meidaName;

  private String ip;
  private int port;
  private int timeOut;
  private int repeatTimes;
  private int startWorkTime;
  private int endWorkTime;
  private String className;
  private int type = -1;
  private ChannelIfc channel;
  private String loginName;
  private String loginPassword;
  private int sleepTime;
  private int status;
  
  /**
   * 渠道形式，如手机形式，email形式等，与Participant的idType对应。
   */
  private int mediaStyle;
  
  /**
   * 渠道开启的状态标记
   */
  public static final int STATUS_STARTED = 0;
  /**
   * 渠道停止的状态标记
   */
  public static final int STATUS_STOPPED = 1;
  
  /**
   * 拨入渠道类型
   */
  public static final int TYPE_INCHANNEL = 0;
  /**
   * 拨出渠道类型
   */
  public static final int TYPE_OUTCHANNEL = 1;

  /*
   * 构造函数
   */
  public MediaInfo(String mediaId, String mediaName,String className, String ip, int port,
                   int timeOut, int repeatTimes, int startWorkTime,
                   int endWorkTime, int type,
                   String loginName, String loginPassword, int sleepTime,int status) {
    this.mediaId = mediaId;
    this.meidaName=mediaName;
    this.ip = ip;
    this.port=port;
    this.className = className;
    this.timeOut = timeOut;
    this.repeatTimes = repeatTimes;
    this.startWorkTime = startWorkTime;
    this.endWorkTime = endWorkTime;
    this.type = type;
    this.loginName = loginName;
    this.loginPassword = loginPassword;
    this.sleepTime = sleepTime;
    this.status=status;
  }
  
  public MediaInfo(){}

  public String getMediaId() {
    return mediaId;
  }
  public void setMediaID(String mediaId){
	  this.mediaId = mediaId;
  }
  public String getIp() {
    return ip;
  }

  public int getPort() {
    return port;
  }

  public int getTimeOut() {
    return timeOut;
  }

  public int getRepeatTimes() {
    return repeatTimes;
  }

  public int getStartWorkTime() {
    return startWorkTime;
  }

  public int getEndWorkTime() {
    return endWorkTime;
  }


  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public int getType() {
    return type;
  }
  
  public void setType(int type){
	  this.type = type;
  }

  public void setChannelObject(ChannelIfc channel) {
    this.channel = channel;
  }

  public ChannelIfc getChannelObject() {
    return channel;
  }

  public String getLoginName() {
    return loginName;
  }
  public void setLoginName(String loginName) {
    this.loginName = loginName;
  }
  public String getLoginPassword() {
    return loginPassword;
  }
  public void setLoginPassword(String loginPassword) {
    this.loginPassword = loginPassword;
  }
  public int getSleepTime() {
    return sleepTime;
  }
  public void setSleepTime(int sleepTime) {
    this.sleepTime = sleepTime;
  }
  public int getStatus() {
    return status;
  }
  public void setStatus(int status) {
    this.status = status;
  }
  public String getMediaName() {
    return meidaName;
  }

  public void setMediaName(String meidaName) {
    this.meidaName = meidaName;
  }

public String toString() {
    StringBuffer s = new StringBuffer();
    s.append(" mediaId =").append(mediaId).append(" meidaName=").append(
        meidaName).append(" ip =").append(ip).append(" port =").
        append(port).append(" status =").append(status).append(
        " className =").append(className).append(" channel =").append(channel).
        append(" endWorkTime =").append(endWorkTime).append(" startWorkTime =").
        append(startWorkTime).append(" loginName =").append(loginName).append(
        " loginPassword =").
        append(loginPassword).append(" repeatTimes =").append(repeatTimes).
        append(" sleepTime =").append(sleepTime).append(" timeOut =").append(
        timeOut).append(" type =").append(type);
    return s.toString();
  }

/**
 * @return the mediaStyle
 */
public int getMediaStyle() {
	return mediaStyle;
}

/**
 * @param mediaStyle the mediaStyle to set
 */
public void setMediaStyle(int mediaStyle) {
	this.mediaStyle = mediaStyle;
}
}