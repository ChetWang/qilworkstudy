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

public class YDDataLockFlag
    extends LockFlag {
  static private YDDataLockFlag DataLockFlag;

  /*
   * ���������õ�Ψһʵ��
   */
  static synchronized public YDDataLockFlag getInstance() {
    if (DataLockFlag == null) {
      DataLockFlag = new YDDataLockFlag();
    }
    return DataLockFlag;
  }

  /*
   * ˽�еĹ��캯��
   */
  private YDDataLockFlag() {
    super();
  }

}