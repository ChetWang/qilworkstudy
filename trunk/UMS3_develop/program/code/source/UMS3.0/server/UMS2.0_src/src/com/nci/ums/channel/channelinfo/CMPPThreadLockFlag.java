/**
 * <p>Title: QuitLockFlag.java</p>
 * <p>Description:
 *    ����������߳�������һ��������
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering�� Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering�� Ltd.</p>
 * Date        Author      Changes
 * NOV. 5 2003   ��־��        Created
 * @version 1.0
 */

package com.nci.ums.channel.channelinfo;

public class CMPPThreadLockFlag
    extends LockFlag {
  static private CMPPThreadLockFlag threadLockFlag;

  /*
   * ���������õ�Ψһʵ��
   */
  static synchronized public CMPPThreadLockFlag getInstance() {
    if (threadLockFlag == null) {
      threadLockFlag = new CMPPThreadLockFlag();
    }
    return threadLockFlag;
  }

  /*
   * ˽�еĹ��캯��
   */
  private CMPPThreadLockFlag() {
    super();
  }

}