/**
 * <p>Title: ChannelIfc.java</p>
 * <p>Description:
 *    接口，提供给OutChannel ,InChannel使用
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering， Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering， Ltd.</p>
 * Date        Author      Changes
 * May 20 2003   张志勇      Created
 * @version 1.0
 */

package com.nci.ums.channel;

import com.nci.ums.channel.channelinfo.LockFlag;


public interface ChannelIfc {
  /*
   * 渠道启动函数
   */
  public void start();

  /*
   * 渠道终止函数
   */
  public void stop();

  /*
   * 渠道状态返回函数
   */
  public boolean getThreadState();
  
  /**
   * 判断渠道是否已经启动过，无论结果是成功还是失败
   * @return true为启动过，false为从未启动过
   */
  public boolean isStartOnce();
  
  /**
   * 获取渠道锁
   * @since ums3.0
   * @return
   */
  public LockFlag getDataLockFlag();

}