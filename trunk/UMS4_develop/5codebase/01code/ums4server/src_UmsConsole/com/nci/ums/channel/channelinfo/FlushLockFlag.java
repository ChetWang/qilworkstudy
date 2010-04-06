/**
 * <p>Title: FlushLockFlag.java</p>
 * <p>Description:
 *    ����������ṩ����������һ��������
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering�� Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering�� Ltd.</p>
 * Date        Author      Changes
 * NOV. 5 2003   ��־��       Created
 * @version 1.0
 */

package com.nci.ums.channel.channelinfo;

public class FlushLockFlag
    extends LockFlag {
  static private FlushLockFlag flushLockFlag;

  /*
   * ���������õ�Ψһʵ��
   */
  static synchronized public FlushLockFlag getInstance() {
    if (flushLockFlag == null) {
      flushLockFlag = new FlushLockFlag();
    }
    return flushLockFlag;
  }

  /*
   * ˽�еĹ��캯��
   */
  private FlushLockFlag() {
    super();
  }
}