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
 * @description  ����˼���������������SMG������
 */
public class UnicomSPMonitor {
	
	private ServerSocket spsvrSocket = null;
	private final static Logger log = Logger.getLogger(UnicomSPMonitor.class);
	
	protected static int spListenPort;
	protected static String smgLoginUserName;      //SMG��½SP��������ʹ�õ��û���
	protected static String smgLoginPassword;      //SMG��½SP��������ʹ�õ�����
	
	private Map<String, Submit> savedmap;
	private final static Map<String,SPProduction> TRANSMIT_MAP=new HashMap<String, SPProduction>();
	private final static LinkedList<Thread> THREAD_LSIT = new LinkedList<Thread>();
	
	private ExecutorService exec = Executors.newSingleThreadExecutor();
	
	public UnicomSPMonitor(Map<String, Submit> map) {
		try {
			this.savedmap = map;
			Class.forName("com.tdt.unicom.sgip.svr.SPProduction");
			spsvrSocket = ServerSocketFactory.getDefault().createServerSocket(spListenPort);
			log.info("����Ϣ����(MO))���ն�����,�����˿�:"+spListenPort);
		} catch (IOException e) {
			log.error("launch local server error!",e);
			throw new ExceptionInInitializerError(e);
		} catch(ClassNotFoundException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	/**
	 * �������񣬼����˿����ڽ�����ͨ������Ϣ
	 */
	public void startSvr() throws IOException{
		while(true) {
			Socket unicomSocket = null;
			unicomSocket = spsvrSocket.accept();
			unicomSocket.setSoLinger(true, 0);   	//socket�ر�ʱ�����ٷ��ͻ�����������ݣ������ͷŵͲ���Դ
			unicomSocket.setTcpNoDelay(true);   	//��ʹ�û�������������������
			unicomSocket.setTrafficClass(0x04|0x10);
			exec.execute(new Handler(unicomSocket));
			} 
		}
	//��������ͨ�ķ�������ͨ�Ų���MO��Ϣת����Ӧ��ҵ���
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
				log.warn("SMGǿ�ƹر�ͨ����·");
			} catch(IOException e){
				log.error("IO���쳣", e);
			}finally {
				try {
					if(socket !=null) {
						spout.close();
						unicomIn.close();
						socket.close();
						log.info("SMG��SPͨ�Ž���,��·�ر�.\n");
					}
				}catch(IOException e) {log.warn("�ͷ�Socket��Դʱ�쳣",e);}
			}
		}
		/**
		 * ������ͨ������Ϣ
		 * @throws IOException ������ͨ����ʱ�п��ܳ��ֵ�IO���쳣
		 */
		private void executeMO() throws IOException {
			boolean isUnbind = false;  //�յ�unbind������˳�ѭ��
			unicomIn = new DataInputStream(socket.getInputStream());
			spout=new DataOutputStream(socket.getOutputStream());
			//��ȡ��ͨ���������ֽ���
			while( ! isUnbind && !socket.isInputShutdown()){
				SGIPCommand command=read(unicomIn);
				log.info("��"+Thread.currentThread().getName()+"�յ�SMG "+SGIPCommandDefine.getCommandName(this.header.getCommandId())+"���,{����="+command.header.getTotalmsglen()+"����="+command.header.getSequenceNumber()+"}");
				switch (Bytes4ToInt(command.header.getCommandId())) {
				    //-----------------------------------
					case 0x1:  //��ͨ��SP���͵İ�����
						log.info("�յ�SMG ->Bind����");
						Bind bind = (Bind)command;
						log.info("LoginType:"+bind.getLoginType());
						log.info("LoginName:"+bind.getLoginName());
						log.info("LoginPassword:"+bind.getLoginPassword());
						if(bind.getLoginType()==2) { // ��½����2ΪSMG��SP���������ӣ����ڷ�������
							BindResp bindresp = new BindResp(command.header.getUnicomSN()); //����Ӧ����
							bindresp.setResult((byte)1);
							if(bind.getLoginName().equals(UnicomSPMonitor.smgLoginUserName) && bind.getLoginPassword().equals(UnicomSPMonitor.smgLoginPassword)) {
								log.info("SMG��½SP,��֤ͨ����");
								bindresp.setResult((byte) 0);
							} 
							log.info("SMG��½SP��֤ʧ��,SMGʹ�õ��û�����������SP���õĲ�����ƥ�䣡");
							bindresp.write(spout);
							log.info("Bind_Resp��Ӧ�룺"+bindresp.getResult());
						}
						break;
				    //------------------------------------
					case 0x2: //��ͨ��SP���͵�ע��������
						//��Ӧ
						log.info("�յ�SMG ->Unbind����");
						UnbindResp resp = new UnbindResp(command.header.getUnicomSN());
						resp.write(spout);
						isUnbind = true;
						break;
					//------------------------------------
					case 0x4: //��ͨ��SP����һ���û�����
						log.info("�յ�SMG ->Deliver����");
						Deliver deliver = (Deliver)command;
						log.info("SPNumber:"+deliver.getSPNumber());
						log.info("UserNumber:"+deliver.getUserNumber());
						log.info("MessageContent:"+deliver.getMessageContent());
						log.info("LinkID:"+deliver.getLinkID());
						//�յ���Ӧ
						DeliverResp deliverresp = new DeliverResp(command.header.getUnicomSN());
						deliverresp.setResult((byte)0);
						deliverresp.write(spout);
						transmitDeliverMsg(deliver); //����ת��
						break;
					//-------------------------------------
					case 0x5: //��ͨ��SP����֮ǰһ��MT��״̬
						log.info("�յ�SMG ->Report����");
						final Report report =(Report) command;
						log.info("ReportType:"+report.getReportType());
						log.info("UserNumber:"+report.getUserNumber());
						log.info("State:"+report.getState());
						log.info("ErrorCode:"+report.getErrorCode());
						//������Ӧ
						ReportResp reportResp = new ReportResp(command.header.getUnicomSN());
						reportResp.setResult((byte)0);
						reportResp.write(spout);
						if(report.getReportType()==0) {//����ǰ��һ��Submit�����״̬����
							transmitDeliverMsg(report);
						}
						break;
					//--------------------------------------
					case 0x11: //��ͨ��SP����һ���ֻ��û���״̬��Ϣ
						log.info("�յ�SMG ->UserRpt����");
						UserRpt userRpt = (UserRpt) command;
						log.info("SPNumber:"+userRpt.getSPNumber());
						log.info("UserNumber:"+userRpt.getUserNumber());
						log.info("UserCondition:"+userRpt.getUserCondition());
						//��Ӧ
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
		 * ���յ��Ķ���ת������Ӧ��ҵ���߼������
		 * ת����ʽSocket��Http��WebService
		 * @param command
		 */
		public void transmitDeliverMsg(final SGIPCommand command) {

		} // end transmit method
	}
}
