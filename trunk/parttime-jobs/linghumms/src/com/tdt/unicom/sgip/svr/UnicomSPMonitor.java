package com.tdt.unicom.sgip.svr;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ServerSocketFactory;

import org.apache.log4j.Logger;

import com.tdt.unicom.domains.Bind;
import com.tdt.unicom.domains.BindResp;
import com.tdt.unicom.domains.Deliver;
import com.tdt.unicom.domains.DeliverResp;
import com.tdt.unicom.domains.Report;
import com.tdt.unicom.domains.ReportResp;
import com.tdt.unicom.domains.SGIPCommand;
import com.tdt.unicom.domains.SGIPCommandDefine;
import com.tdt.unicom.domains.Submit;
import com.tdt.unicom.domains.UnbindResp;
import com.tdt.unicom.domains.UserRpt;
import com.tdt.unicom.domains.UserRptResp;

/**
 * @project UNICOM
 * @author sunnylocus
 * @vresion 1.0 2009-8-17
 * @description  服务端监听器，监听来自SMG的数据
 */
public class UnicomSPMonitor {
	
	private ServerSocket spsvrSocket = null;
	private final static Logger log = Logger.getLogger(UnicomSPMonitor.class);
	
	protected static int spListenPort;
	protected static String smgLoginUserName;      //SMG登陆SP短信网关使用的用户名
	protected static String smgLoginPassword;      //SMG登陆SP短信网关使用的密码
	
	private Map<String, Submit> savedmap;
	private final static Map<String,SPProduction> TRANSMIT_MAP=new HashMap<String, SPProduction>();
	private final static LinkedList<Thread> THREAD_LSIT = new LinkedList<Thread>();
	
	private ExecutorService exec = Executors.newSingleThreadExecutor();
	
	public UnicomSPMonitor(Map<String, Submit> map) {
		try {
			this.savedmap = map;
			Class.forName("com.tdt.unicom.sgip.svr.SPProduction");
			spsvrSocket = ServerSocketFactory.getDefault().createServerSocket(spListenPort);
			log.info("短消息上行(MO))接收端启动,监听端口:"+spListenPort);
		} catch (IOException e) {
			log.error("launch local server error!",e);
			throw new ExceptionInInitializerError(e);
		} catch(ClassNotFoundException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	/**
	 * 启动服务，监听端口用于接收联通上行信息
	 */
	public void startSvr() throws IOException{
		while(true) {
			Socket unicomSocket = null;
			unicomSocket = spsvrSocket.accept();
			unicomSocket.setSoLinger(true, 0);   	//socket关闭时，不再发送缓冲区里的数据，立即释放低层资源
			unicomSocket.setTcpNoDelay(true);   	//不使用缓冲区，立即发送数据
			unicomSocket.setTrafficClass(0x04|0x10);
			exec.execute(new Handler(unicomSocket));
			} 
		}
	//负责与联通的服务器的通信并将MO信息转发相应的业务层
	class Handler extends SGIPCommand implements Runnable {  
		private Socket socket = null;
		private DataInputStream unicomIn = null;
		private DataOutputStream spout = null;
		
		public Handler(Socket socket) {
			this.socket = socket;
			log.info("New connection accepted from "+ socket.getInetAddress()+":"+socket.getPort());
		}
		public void run() {
			try {
				this.executeMO();
			} catch(RuntimeException e) {
				log.warn("SMG强制关闭通信链路");
			} catch(IOException e){
				log.error("IO流异常", e);
			}finally {
				try {
					if(socket !=null) {
						spout.close();
						unicomIn.close();
						socket.close();
						log.info("SMG与SP通信结束,链路关闭.\n");
					}
				}catch(IOException e) {log.warn("释放Socket资源时异常",e);}
			}
		}
		/**
		 * 处理联通上行信息
		 * @throws IOException 接收联通上行时有可能出现的IO流异常
		 */
		private void executeMO() throws IOException {
			boolean isUnbind = false;  //收到unbind命令后，退出循环
			unicomIn = new DataInputStream(socket.getInputStream());
			spout=new DataOutputStream(socket.getOutputStream());
			//读取联通发送来的字节流
			while( ! isUnbind && !socket.isInputShutdown()){
				SGIPCommand command=read(unicomIn);
				log.info("【"+Thread.currentThread().getName()+"收到SMG "+SGIPCommandDefine.getCommandName(this.header.getCommandId())+"命令】,{长度="+command.header.getTotalmsglen()+"序列="+command.header.getSequenceNumber()+"}");
				switch (Bytes4ToInt(command.header.getCommandId())) {
				    //-----------------------------------
					case 0x1:  //联通向SP发送的绑定命令
						log.info("收到SMG ->Bind命令");
						Bind bind = (Bind)command;
						log.info("LoginType:"+bind.getLoginType());
						log.info("LoginName:"+bind.getLoginName());
						log.info("LoginPassword:"+bind.getLoginPassword());
						if(bind.getLoginType()==2) { // 登陆类型2为SMG向SP建立的连接，用于发送命令
							BindResp bindresp = new BindResp(command.header.getUnicomSN()); //绑定响应命令
							bindresp.setResult((byte)1);
							if(bind.getLoginName().equals(UnicomSPMonitor.smgLoginUserName) && bind.getLoginPassword().equals(UnicomSPMonitor.smgLoginPassword)) {
								log.info("SMG登陆SP,验证通过！");
								bindresp.setResult((byte) 0);
							} 
							log.info("SMG登陆SP验证失败,SMG使用的用户名与密码与SP配置的参数不匹配！");
							bindresp.write(spout);
							log.info("Bind_Resp响应码："+bindresp.getResult());
						}
						break;
				    //------------------------------------
					case 0x2: //联通向SP发送的注销绑定命令
						//响应
						log.info("收到SMG ->Unbind命令");
						UnbindResp resp = new UnbindResp(command.header.getUnicomSN());
						resp.write(spout);
						isUnbind = true;
						break;
					//------------------------------------
					case 0x4: //联通向SP上行一条用户短信
						log.info("收到SMG ->Deliver命令");
						Deliver deliver = (Deliver)command;
						log.info("SPNumber:"+deliver.getSPNumber());
						log.info("UserNumber:"+deliver.getUserNumber());
						log.info("MessageContent:"+deliver.getMessageContent());
						log.info("LinkID:"+deliver.getLinkID());
						//收到响应
						DeliverResp deliverresp = new DeliverResp(command.header.getUnicomSN());
						deliverresp.setResult((byte)0);
						deliverresp.write(spout);
						transmitDeliverMsg(deliver); //上行转发
						break;
					//-------------------------------------
					case 0x5: //联通向SP报告之前一条MT的状态
						log.info("收到SMG ->Report命令");
						final Report report =(Report) command;
						log.info("ReportType:"+report.getReportType());
						log.info("UserNumber:"+report.getUserNumber());
						log.info("State:"+report.getState());
						log.info("ErrorCode:"+report.getErrorCode());
						//返回响应
						ReportResp reportResp = new ReportResp(command.header.getUnicomSN());
						reportResp.setResult((byte)0);
						reportResp.write(spout);
						if(report.getReportType()==0) {//对先前的一条Submit命令的状态报告
							transmitDeliverMsg(report);
						}
						break;
					//--------------------------------------
					case 0x11: //联通向SP报告一条手机用户的状态信息
						log.info("收到SMG ->UserRpt命令");
						UserRpt userRpt = (UserRpt) command;
						log.info("SPNumber:"+userRpt.getSPNumber());
						log.info("UserNumber:"+userRpt.getUserNumber());
						log.info("UserCondition:"+userRpt.getUserCondition());
						//响应
						UserRptResp userRptresp = new UserRptResp(command.header.getUnicomSN());
						userRptresp.setResult((byte)0);
						break;
					default:
						log.error("error!! -->default:"+Bytes4ToInt(command.header.getCommandId()));
						break;
				}
			}
		}
		/**
		 * 将收到的短信转发给相应的业务逻辑处理层
		 * 转发方式Socket、Http、WebService
		 * @param command
		 */
		public void transmitDeliverMsg(final SGIPCommand command) {

		} // end transmit method
	}
}
