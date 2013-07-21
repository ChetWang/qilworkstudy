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


 
public class NCIThreadLockFlag
    extends LockFlag {
  static private NCIThreadLockFlag threadLockFlag;

  /*
   * ���������õ�Ψһʵ��
   */
  static synchronized public NCIThreadLockFlag getInstance() {
    if (threadLockFlag == null) {
      threadLockFlag = new NCIThreadLockFlag();
    }
    return threadLockFlag;
  }

  /*
   * ˽�еĹ��캯��
   */
  private NCIThreadLockFlag() {
    super();
  }
   
}