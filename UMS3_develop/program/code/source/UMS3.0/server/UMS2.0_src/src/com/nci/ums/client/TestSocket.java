package com.nci.ums.client;


import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;


//import com.nci.ums.util.Util;

public class TestSocket{
	
	private final static String msg = "1、输入手机号码和消息内容。\n"
									+"2、输入exit退出测试。\n"
									+"注：请根据接入情况输入手机号码。\n";
	
	public static void main(String[] argv){
		TestSocket test=new TestSocket();
		System.out.println(msg);
		String mobile=null,content=null,tmp;
		DataInputStream in = new DataInputStream(
								new BufferedInputStream(System.in));
		try {
			goto1:	while(true){
				System.out.print("请输入测试手机号码：");

				while((tmp=in.readLine()).length()==0){
					System.out.print("输入测试手机号码：");
				}

				if(tmp.equalsIgnoreCase("exit")){
					System.exit(1);
				}
				
				if(tmp.length()!=0&&tmp.length()!=11){
					System.out.println("请输入正确的手机号码。");
					continue goto1;
				}
				mobile = tmp;
				break;
			}

			goto2:	while(true){
				System.out.print("请输入消息内容：");
	
				while((tmp=in.readLine()).length()==0){
					System.out.print("请输入消息内容：");
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
//			UMSSendMessage message = new UMSSendMessage();	//构建消息体
//			message.addTo("15988898527");			//添加发送地址
//			message.setTradeCode("1002");			//设置交易码
//			message.setAppID("1001");				//设置应用号
//			message.addContent(new UMSContent().setText("text"));//添加内容体,可以添加多个内容体
//			
//			UMSSession session = UMSSession.openSession(msgPros);//建立ums连接会话
//			session.sendMessage(message);	//发送消息
//			session.close();				//关闭会话
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}
	}
}