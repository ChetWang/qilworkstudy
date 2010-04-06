package com.nci.ums.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UMSSession {
	private Socket socket;
//	private DataInputStream in;
//	private DataOutputStream out;
	protected static Log log = LogFactory.getLog(UMSSession.class);
	
	private UMSSession(Socket temp) {
		this.socket	= temp;
//			this.in 	= new DataInputStream(socket.getInputStream());			
//			this.out 	= new DataOutputStream(socket.getOutputStream());
	}
	
	public static UMSSession openSession(Properties pro) {
		try {
			InetAddress addr 	= InetAddress.getByName(pro.getProperty(UMSSERVER_IP));
			Socket conSocket	= new Socket(addr,Integer.parseInt(pro.getProperty(UMSSERVER_PORT)));
			conSocket.setSoTimeout(3 * 60 * 1000);
			if( checkPassword(conSocket,pro.getProperty(UMSSERVER_LOGIN_APPID),pro.getProperty(UMSSERVER_LOGIN_PWD)) ) {
				return new  UMSSession(conSocket);
			}
			conSocket.close();
			log.info("用户登陆验证错误");
		} catch (NumberFormatException e) {
			log.error("格式话端口异常",e);
		} catch (IOException e) {
			log.error("socket连接异常",e);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public UMSMessageSender getSender() {
		return null;
	}

	public void close() {
		if( socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				log.error("关闭scoket异常",e);
			}
		}
	}
	
	public int sendMessage(UMSMessage msg) {
		System.out.println("----------->" + msg.toString());
		int result=1;
//	    StringBuffer sb = new StringBuffer("506  ");
//	    sb.append(msg.getTradeCode()).
//	    append(getFixedString(msg.getAppID(),12)).
//	    append(getFixedString(msg.getID(),35)).
//	    append(getFixedString(msg.getType(),3));
//	    Iterator it = msg.getDests();
//	    StringBuffer temp = new StringBuffer();
//	    while(it.hasNext()) {
//	    	temp.append(it.next());
//	    	temp.append(",");
//	    }
//	    temp.deleteCharAt(temp.length() - 1);
//	    sb.append(getFixedString(temp.toString(),255));
//	    temp = null;
//	    temp = new StringBuffer("");
//	    it = msg.getUMSContents();
//	    UMSContent content;
//	    while(it.hasNext()) {
//	    	content = (UMSContent) it.next();
//	    	if(content.getType().equals(UMSContent.TYPE_TEXT)) {
//	    		temp.append(content.getString());
//	    		temp.append("\n");
//	    	}
//	    }
//	    
//	    sb.append(getFixedString(temp.toString(),160)).
//	    append(msg.getAck()).
//	    append(getFixedString(msg.getReply(),30)).
//	    append(getFixedString("",2)).
//	    append(getFixedString("",2));
//		System.out.println(sb.toString().length());
//		out.write(sb.toString().getBytes());
		byte[] buffer 			= new byte[1024];
		try{
//			DataInputStream in 		= new DataInputStream(socket.getInputStream());			
//			DataOutputStream out 	= new DataOutputStream(socket.getOutputStream());
			ObjectOutputStream objOut 	= new ObjectOutputStream(socket.getOutputStream());
//			ObjectInputStream objIn		= new ObjectInputStream(new DataInputStream(socket.getInputStream()));
			objOut.writeObject(msg);
//			UMSReturnMessage returnMsg	 = (UMSReturnMessage) objIn.readObject();
//			int size		= in.read(buffer);
//			byte retCode[]	= new byte[4];
//			for( int i = 5;i <= 8; i++ ){
//				retCode[i-5]= buffer[i];
//			}
//			String retCodeStr=new String(retCode);
//			if ( returnMsg.getReturnCode().equalsIgnoreCase("0000") ){				
//				result = 0;
//			}		
			socket.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	private synchronized static String getFixedString(String msg,int len){
	 	if(msg==null){
	 		msg="";
	 	}
	    StringBuffer sb=new StringBuffer(msg);
	    for(int i=msg.getBytes().length+1;i<=len;i++)
	    	sb.append(" ");
	    return sb.toString();
	}
	
	public static boolean checkPassword(Socket socket,String appID,String password){		
		boolean result			= false;
		try{
//			DataInputStream in 		= new DataInputStream(socket.getInputStream());
//			DataOutputStream out 	= new DataOutputStream(socket.getOutputStream());
			
			UMSSendMessage message = new UMSSendMessage();	//构建消息体
			message.setTradeCode(appID);					//设置交易码
			message.setAppID("1001");
			message.put("PASSWORD", password);
/*			StringBuffer sb			= new StringBuffer("38   100101").
										append(getFixedString(appID,12)).
										append(getFixedString(password,20));*/
			
			ObjectOutputStream objOut = new ObjectOutputStream(socket.getOutputStream());
			objOut.writeObject(message);
			ObjectInputStream objIn	= new ObjectInputStream(socket.getInputStream());
//			byte[] buffer = new byte[1024];
//			int size = in.read(buffer);
//			byte retCode[] = new byte[4];
//			for(int i = 5; i <= 8 ; i++){
//				retCode[i-5] = buffer[i];
//			}
//			String retCodeStr = new String(retCode);
			UMSReturnMessage reMsg = (UMSReturnMessage) objIn.readObject();
			if (reMsg.getReturnMsg().equalsIgnoreCase("0000")){				
				result = true;
				log.info("password invalid passed");
			}
		} catch(IOException e){
			log.error("验证密码-IO异常", e);
		} catch (ClassNotFoundException e) {
			log.error("验证密码-数据返回类型错误", e);
		} catch (Exception e) {
			log.error("验证密码-失败", e);
		}
		return result;
	}
	
	public void sendAsynMessage(UMSMessage msg) {
		
	}
	
	public static final String UMSSERVER_IP			= "com.nci.ums.session.ip";
	public static final String UMSSERVER_PORT			= "com.nci.ums.session.port";
	public static final String UMSSERVER_LOGIN_APPID	= "com.nci.ums.session.login.user";
	public static final String UMSSERVER_LOGIN_PWD	= "com.nci.ums.session.login.pwd";
}
