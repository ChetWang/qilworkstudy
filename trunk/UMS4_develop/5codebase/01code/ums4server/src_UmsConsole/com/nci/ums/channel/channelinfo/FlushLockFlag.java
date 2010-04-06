/**
 * <p>Title: FlushLockFlag.java</p>
 * <p>Description:
 *    这个类用来提供更新锁，是一个单例类
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering， Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering， Ltd.</p>
 * Date        Author      Changes
 * NOV. 5 2003   张志勇       Created
 * @version 1.0
 */

package com.nci.ums.channel.channelinfo;

public class FlushLockFlag
    extends LockFlag {
  static private FlushLockFlag flushLockFlag;

  /*
   * 工厂方法得到唯一实例
   */
  static synchronized public FlushLockFlag getInstance() {
    if (flushLockFlag == null) {
      flushLockFlag = new FlushLockFlag();
    }
    return flushLockFlag;
  }

  /*
   * 私有的构造函数
   */
  private FlushLockFlag() {
    super();
  }
}