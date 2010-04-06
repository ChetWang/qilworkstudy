/**
 * <p>Title: QuitLockFlag.java</p>
 * <p>Description:
 *    ����������ṩ���ݲ�ѯ������һ��������
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering�� Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering�� Ltd.</p>
 * Date        Author      Changes
 * NOV. 5 2003   ��־��        Created
 * @version 1.0
 */

package com.nci.ums.channel.channelinfo;

public class NCIDataLockFlag
    extends LockFlag {
  static private NCIDataLockFlag DataLockFlag;

  /*
   * ���������õ�Ψһʵ��
   */
  static synchronized public NCIDataLockFlag getInstance() {
    if (DataLockFlag == null) {
      DataLockFlag = new NCIDataLockFlag();
    }
    return DataLockFlag;
  }

  /*
   * ˽�еĹ��캯��
   */
  private NCIDataLockFlag() {
    super();
  }

}