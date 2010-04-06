/**
 * <p>Title: QuitLockFlag.java</p>
 * <p>Description:
 *    ����������ṩ�˳�������һ��������
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering�� Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering�� Ltd.</p>
 * Date        Author      Changes
 * NOV. 5 2003   ��־��        Created
 * @version 1.0
 */

package com.nci.ums.channel.channelinfo;

public class QuitLockFlag
    extends LockFlag {
  static private QuitLockFlag quitLockFlag;

  /*
   * ���������õ�Ψһʵ��
   */
  static synchronized public QuitLockFlag getInstance() {
    if (quitLockFlag == null) {
      quitLockFlag = new QuitLockFlag();
    }
    return quitLockFlag;
  }

  /*
   * ˽�еĹ��캯��
   */
  private QuitLockFlag() {
    super();
  }

}