/**
 * <p>Title: SendOut.java</p>
 * <p>Description:
 *    �����࣬�ṩ����Χ�ӿ�ʹ�ã�������������ģ�����������ֹ������
 * </p>
 * <p>Copyright: 2003 Hangzhou NCISystem Engineering�� Ltd.</p>
 * <p>Company:   Hangzhou NCI System Engineering�� Ltd.</p>
 * Date        Author      Changes
 * NOV. 5 2003   ��־��       Created
 * @version 1.0
 */

package com.nci.ums.channel.channelmanager;




import com.nci.ums.util.*;


public class SendOut extends Thread{
  //������Ϣ�������
  private ChannelManager channelManager;
  //ʧЧ����ɨ�����
  private ScanInvalid scandInvalid;


  /*
   * ���캯��
   * ����������Ϣ��������ʧЧ����ɨ�����
   */
  public SendOut() {
    Res.log(Res.INFO, "sendOut���������");
    channelManager = new ChannelManager();
    scandInvalid = new ScanInvalid();
  }

  /*
   * �ṩ����Χ�ӿڵ�����������
   * �������������ʧЧ����ɨ���̺߳�������Ϣ�����߳�
   */
public void run() {
    channelManager.start();
    /*UMSThread umsThread=new UMSThread("UMS����Ϣƽ̨","���������߳�",Util.getCurrentTimeStr("yyyyMMddHHmmss"),"������",channelManager,"");
    Res.getUMSThreads().add(umsThread);*/

    scandInvalid.start();
     /*umsThread=new UMSThread("UMS����Ϣƽ̨","ʧЧ����ɨ���߳�",Util.getCurrentTimeStr("yyyyMMddHHmmss"),"������",scandInvalid,"");
     Res.getUMSThreads().add(umsThread);*/
  }

  /*
   * �ṩ����Χ�ӿڵ�����������
   * �����������ֹʧЧ����ɨ���̺߳�������Ϣ�����߳�
   */
  public void stopThread() {
    channelManager.stop();
    scandInvalid.stop();
  }

  /*
   * �ṩ����Χ�ӿڵ�����������
   * ���������ˢ������������Ϣ����������������Ϣ�����߳�
   */
  public void flush() {

  }

  public static void main(String[] args) {
    SendOut sendOut1 = new SendOut();
    sendOut1.start();
    /*   try{
         Thread.sleep(5000);
       }
       catch(Exception e){
         e.printStackTrace();
       }
       sendOut1.stop();
     */
  }
}