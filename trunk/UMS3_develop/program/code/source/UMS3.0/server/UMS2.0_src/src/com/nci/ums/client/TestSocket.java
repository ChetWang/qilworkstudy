package com.nci.ums.client;


import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;


//import com.nci.ums.util.Util;

public class TestSocket{
	
	private final static String msg = "1�������ֻ��������Ϣ���ݡ�\n"
									+"2������exit�˳����ԡ�\n"
									+"ע������ݽ�����������ֻ����롣\n";
	
	public static void main(String[] argv){
		TestSocket test=new TestSocket();
		System.out.println(msg);
		String mobile=null,content=null,tmp;
		DataInputStream in = new DataInputStream(
								new BufferedInputStream(System.in));
		try {
			goto1:	while(true){
				System.out.print("����������ֻ����룺");

				while((tmp=in.readLine()).length()==0){
					System.out.print("��������ֻ����룺");
				}

				if(tmp.equalsIgnoreCase("exit")){
					System.exit(1);
				}
				
				if(tmp.length()!=0&&tmp.length()!=11){
					System.out.println("��������ȷ���ֻ����롣");
					continue goto1;
				}
				mobile = tmp;
				break;
			}

			goto2:	while(true){
				System.out.print("��������Ϣ���ݣ�");
	
				while((tmp=in.readLine()).length()==0){
					System.out.print("��������Ϣ���ݣ�");
				}
	
				if(tmp.equalsIgnoreCase("exit")){
					System.exit(1);
				}
				content = tmp;
				break;
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
//		try{
//			Properties msgPros = new Properties();
//			msgPros.put(UMSSession.UMSSERVER_LOGIN_APPID, "1001");
//			msgPros.put(UMSSession.UMSSERVER_LOGIN_PWD, "1001");
//			msgPros.put(UMSSession.UMSSERVER_IP, "192.168.0.56");
//			msgPros.put(UMSSession.UMSSERVER_PORT, "10000");
//			
//			UMSSendMessage message = new UMSSendMessage();	//������Ϣ��
//			message.addTo("15988898527");			//��ӷ��͵�ַ
//			message.setTradeCode("1002");			//���ý�����
//			message.setAppID("1001");				//����Ӧ�ú�
//			message.addContent(new UMSContent().setText("text"));//���������,������Ӷ��������
//			
//			UMSSession session = UMSSession.openSession(msgPros);//����ums���ӻỰ
//			session.sendMessage(message);	//������Ϣ
//			session.close();				//�رջỰ
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}
	}
}