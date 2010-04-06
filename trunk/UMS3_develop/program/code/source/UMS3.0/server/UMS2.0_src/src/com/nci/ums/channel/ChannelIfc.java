/**
 * <p>Title: ChannelIfc.java</p>
 * <p>Description:
 *    �ӿڣ��ṩ��OutChannel ,InChannelʹ��
 * </p>
 * <p>Copyright: 2003 Hangzhou NCI System Engineering�� Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering�� Ltd.</p>
 * Date        Author      Changes
 * May 20 2003   ��־��      Created
 * @version 1.0
 */

package com.nci.ums.channel;

import com.nci.ums.channel.channelinfo.LockFlag;


public interface ChannelIfc {
  /*
   * ������������
   */
  public void start();

  /*
   * ������ֹ����
   */
  public void stop();

  /*
   * ����״̬���غ���
   */
  public boolean getThreadState();
  
  /**
   * �ж������Ƿ��Ѿ������������۽���ǳɹ�����ʧ��
   * @return trueΪ��������falseΪ��δ������
   */
  public boolean isStartOnce();
  
  /**
   * ��ȡ������
   * @since ums3.0
   * @return
   */
  public LockFlag getDataLockFlag();

}