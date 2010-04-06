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


 
public class MM7ThreadLockFlag
    extends LockFlag {
  static private MM7ThreadLockFlag threadLockFlag;

  /*
   * 工厂方法得到唯一实例
   */
  static synchronized public MM7ThreadLockFlag getInstance() {
    if (threadLockFlag == null) {
      threadLockFlag = new MM7ThreadLockFlag();
    }
    return threadLockFlag;
  }

  /*
   * 私有的构造函数
   */
  private MM7ThreadLockFlag() {
    super();
  }
   
}