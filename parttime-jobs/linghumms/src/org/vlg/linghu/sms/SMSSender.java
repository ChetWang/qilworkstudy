package org.vlg.linghu.sms;

import spApi.*;
import java.net.*;
import java.io.*;
public class SMSSender {

  public static void main(String[] args) {
         SMSSender atestprocesse=new SMSSender();
  }

  public SMSSender() {
         Socket so=null;
         OutputStream out=null;
         InputStream input=null;
         Bind command=null;
         Bind com=null;
         SGIP_Command sgip=null;
         SGIP_Command tmp=null;
         int i=0;
         try {
             sgip=new SGIP_Command();
             command=new Bind(399000,//nodeID 3+CP_ID
                              1, //login type
                              "zhao",//login name
                              "zhao");//login password
             int err;
             byte [] byte_content = new byte[140];
             Deliver deliver=null;
             Submit submit = null;
             SubmitResp submitresp=null;
             Bind active=null;
             Unbind term=null;
             BindResp resp=null;
             UnbindResp Unresp=null;
             so=new Socket("211.90.223.213",8801);
             out=new DataOutputStream(so.getOutputStream());
             input = new DataInputStream(so.getInputStream());
//             command=new Bind(399000);
//             command.SetLoginType(1);
//             command.SetLoginName("zhao");
//             command.SetLoginPassword("zhao");
             err=command.write(out);//����bind
             if(err!=0)
             {
                System.out.println("err"+err);
            }
             tmp=sgip.read(input);//����sgip��Ϣ
	          if(sgip.getCommandID()==SGIP_Command.ID_SGIP_BIND_RESP)
		        {
		          resp=(BindResp)tmp;//ǿ��ת��Ϊbindresp
              resp.readbody();//����Ϣ���н��
              System.out.println(tmp.getSeqno_1());

              System.out.println(tmp.getSeqno_2());
              System.out.println(tmp.getSeqno_3());
              System.out.println(resp.GetResult());
              }
              for(i=0;i<140;i++)
              {
                    byte_content[i] = 51;
                    i++;
                    byte_content[i] = 51;
                    i++;
                    byte_content[i] = 52;
                    i++;

                    byte_content[i] = 53;
                    i++;
                    byte_content[i] = 54;
                    i++;

                    byte_content[i] = 55;
                    i++;
                    byte_content[i] = 56;
                    i++;
                    byte_content[i] = 57;
                    i++;

                    byte_content[i] = 58;
                    i++;
                    byte_content[i] = 59;
              }
              submit = new Submit(399000,//node idͬ��
                                  "9000",//cp_phone
                                  "8613000061234",//���Ѻ���
                                  2,//���ն���Ϣ���ֻ���
                                  "8613000061231,8613000061233",//�ֻ�����ǰ���86
                                  "99000",//cp_id
                                  "",//ҵ�����
                                  0,//�Ʒ�����
                                  "500",//����Ϣ�շ�ֵ
                                  "500",//���ͻ���
                                  1,//���ձ�־
                                  1,//����MT��ԭ��
                                  9,//���ȼ�
                                  "",//����Ϣ��ֹʱ��
                                  "",//011125120000032+����Ϣ��ʱ����ʱ��
                                  1,//״̬�����־
                                  1,//GSMЭ������
                                  1,//GSMЭ������
                                  0,//����Ϣ�����ʽ
                                  0,//��Ϣ����
                                  12,//����Ϣ����
                                  "123456789012");//����Ϣ����
              //submit.setContent(0,"123");
/*              submit.setBinContent(10,byte_content);
//              submit=new Submit(399000);
              submit.setSPNumber("9200");
              submit.setChargeNumber("8613055555678");
              submit.setUserNumber("8613055551230,8613055551231");
              submit.setCorpId("99001");
              submit.setServiceType("123");
              submit.setFeeType(2);
              submit.setFeeValue("50000");
              submit.setGivenValue("50001");
              submit.setAgentFlag(2);
              submit.setMOrelatetoMTFlag(3);
              submit.setPriority(8);
              submit.setExpireTime("011125120000032+");
              submit.setScheduleTime("011125120000032+");
              submit.setReportFlag(0);
              submit.setTP_pid(1);
              submit.setTP_udhi(64);
              submit.setMessageType(1);

//              submit.setBinContent(10,byte_content);
              //submit.setContent(0,"1234"); */
/*             submit = new Submit(399000,//�ù��캯���и�������������ͬ��
                                  "9000",
                                  "8613000061234",
                                  2,
                                  "8613000061231,8613000061233",
                                  "99000",
                                  "",
                                  0,
                                  "500",
                                  "500",
                                  1,
                                  1,
                                  9,
                                  "",
                                  "",
                                  1,
                                  1,
                                  1,
                                  4,
                                  0,
                                  140,
                                  byte_content); */
              submit.write(out);//����submit
              tmp=sgip.read(input);
              if(tmp.getCommandID()==SGIP_Command.ID_SGIP_SUBMIT_RESP)
              {
                  submitresp=(SubmitResp)tmp;//ǿ��ת��
                  submitresp.readbody();//���
                  System.out.println(tmp.getSeqno_1());
                  System.out.println(tmp.getSeqno_2());
                  System.out.println(tmp.getSeqno_3());
                  System.out.println(submitresp.getResult());
              }

              //com.write(out);
              term=new Unbind(399000);
              term.write(out);//����unbind
              tmp=sgip.read(input);
              if(sgip.getCommandID()==SGIP_Command.ID_SGIP_UNBIND_RESP)
              {
              Unresp=(UnbindResp)tmp;
              System.out.println(tmp.getSeqno_1());
              System.out.println(tmp.getSeqno_2());
              System.out.println(tmp.getSeqno_3());
              }
             out.close();
            so.close();
         }catch (SGIP_Exception e){
            System.out.println(e.toString());
            }
         catch (Exception e) {
                 System.out.println(e.toString());
         } finally {
                   try {
                       System.in.read();
                       //it just for debug
                   } catch (Exception s) {
                           System.out.println(s.toString());
                   }
         }

  }

}
