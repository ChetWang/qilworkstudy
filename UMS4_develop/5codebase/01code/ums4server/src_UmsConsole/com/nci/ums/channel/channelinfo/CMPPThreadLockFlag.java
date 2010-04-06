/**
 * <p>Title: QuitLockFlag.java</p>
 * <p>Description:
 *    这个类用来线程锁，是一个单例类
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering， Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering， Ltd.</p>
 * Date        Author      Changes
 * NOV. 5 2003   张志勇        Created
 * @version 1.0
 */

package com.nci.ums.channel.channelinfo;

public class CMPPThreadLockFlag
    extends LockFlag {
  static private CMPPThreadLockFlag threadLockFlag;

  /*
   * 工厂方法得到唯一实例
   */
  static synchronized public CMPPThreadLockFlag getInstance() {
    if (threadLockFlag == null) {
      threadLockFlag = new CMPPThreadLockFlag();
    }
    return threadLockFlag;
  }

  /*
   * 私有的构造函数
   */
  private CMPPThreadLockFlag() {
    super();
  }

}