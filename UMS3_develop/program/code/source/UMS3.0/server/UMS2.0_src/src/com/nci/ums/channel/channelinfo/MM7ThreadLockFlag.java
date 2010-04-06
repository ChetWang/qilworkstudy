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


 
public class MM7ThreadLockFlag
    extends LockFlag {
  static private MM7ThreadLockFlag threadLockFlag;

  /*
   * ���������õ�Ψһʵ��
   */
  static synchronized public MM7ThreadLockFlag getInstance() {
    if (threadLockFlag == null) {
      threadLockFlag = new MM7ThreadLockFlag();
    }
    return threadLockFlag;
  }

  /*
   * ˽�еĹ��캯��
   */
  private MM7ThreadLockFlag() {
    super();
  }
   
}