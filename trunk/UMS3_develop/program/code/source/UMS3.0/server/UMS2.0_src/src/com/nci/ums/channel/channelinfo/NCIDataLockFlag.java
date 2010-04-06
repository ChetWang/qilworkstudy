/**
 * <p>Title: QuitLockFlag.java</p>
 * <p>Description:
 *    这个类用来提供数据查询锁，是一个单例类
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering， Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering， Ltd.</p>
 * Date        Author      Changes
 * NOV. 5 2003   张志勇        Created
 * @version 1.0
 */

package com.nci.ums.channel.channelinfo;

public class NCIDataLockFlag
    extends LockFlag {
  static private NCIDataLockFlag DataLockFlag;

  /*
   * 工厂方法得到唯一实例
   */
  static synchronized public NCIDataLockFlag getInstance() {
    if (DataLockFlag == null) {
      DataLockFlag = new NCIDataLockFlag();
    }
    return DataLockFlag;
  }

  /*
   * 私有的构造函数
   */
  private NCIDataLockFlag() {
    super();
  }

}