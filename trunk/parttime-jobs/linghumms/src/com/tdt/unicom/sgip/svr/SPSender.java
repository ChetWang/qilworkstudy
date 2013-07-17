package com.tdt.unicom.sgip.svr;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.net.SocketFactory;

import org.apache.log4j.Logger;

import com.tdt.unicom.domains.Bind;
import com.tdt.unicom.domains.BindResp;
import com.tdt.unicom.domains.SGIPCommandDefine;
import com.tdt.unicom.domains.SGIPException;
import com.tdt.unicom.domains.Submit;
import com.tdt.unicom.domains.SubmitResp;
import com.tdt.unicom.domains.Unbind;
import com.tdt.unicom.domains.UnbindResp;
import com.tdt.unicom.spsvr.MTReq;
import com.tdt.unicom.util.SGIPCodeHelper;

/**
 * @project UNICOM
 * @author sunnylocus
 * @vresion 1.0 2009-8-21
 * @description  �����·���
 */
public final class SPSender {
	private Logger log = Logger.getLogger(getClass());
	//�Ƚ��·����ŵĶ��У��������޶�1000����ֹ�����ڴ����Ĺ��󣬶�����Java heap space�쳣
	private BlockingQueue<Runnable> mtReqQueue = new LinkedBlockingQueue<Runnable>(1000);
	private volatile boolean isSendUnbind = true;// �Ƿ�����SMG���� unbind����ı�־
	
	private static SPSender instance = null;

	private Socket socket = null;
	private DataOutputStream out = null;
	private DataInputStream in = null;
	
	private long bindstartTime;
	private long currentTime;
	private static Map<String, Submit> mtSendedMap;  //�ѷ��͵��·�ʵ��
	//������Ϣ��productionConf.xml������
	protected static String unicomIp;     //��ͨSMG��IP��ַ
	protected static int unicomPort;      //��ͨSMG�����Ķ˿ں�
	protected static String spLoginName;  //��½SMG���õ����û���
	protected static String spLogPassword;//��½SMG���õ�������
	protected static String spCorpId;        //5λ��ҵ����
	
	private final ThreadFactory threadFactory = new ThreadFactory() {
		int threadNo = 0;
		@Override
		public Thread newThread(Runnable task) {
			threadNo ++;
			Thread thread = new Thread(task,"MT-"+threadNo);
			thread.setUncaughtExceptionHandler(new UEHLogger());
			
			return thread;
		}
	};
    //ÿ��CPU���������̣߳������߳��쳣�˳�ʱ���Զ����ɵ����߳�������쳣�˳����߳�
	//���·��߳�ȫ��æµʱ������˭����˭���͵Ĳ���
	//�����ɵ��߳�
	int threadAmount = Runtime.getRuntime().availableProcessors() * 2;
	private ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(threadAmount,threadAmount, 1000 * 30, 
			TimeUnit.SECONDS, mtReqQueue, threadFactory,new ThreadPoolExecutor.CallerRunsPolicy()); 

	/**
	 * ��̬ģʽ���ڻ�ȡʵ��ʱ��˫�ؼ�⣬��ֹ���߳�����²������ʵ��
	 * @return��SPSender
	 */
	public static SPSender getInstance(Map<String, Submit> map) {
		if (instance == null) {
			synchronized (SPSender.class) {
				if (instance == null) {
					instance = new SPSender();
					if(map!=null){
						mtSendedMap=map;
					}
				}
			}
		}
		return instance;
	}
	/**
	 * ������������
	 * @param mtreq �����·�����
	 */
	public void addTask(final MTReq mtreq) {
		 final Runnable sendTask = new Runnable() {
			public void run() {
				try {
					sendMTReq(mtreq);
				} catch (IOException e) {
					// ���Ͷ����쳣,���·Żط��Ͷ���
					log.warn("�����·�ʱ����IO���쳣:" + e.getMessage()
							+ ".�������·�ʵ�����·Żط��Ͷ��У�");
					mtReqQueue.add(this);
				} 
			}
		};
		poolExecutor.execute(sendTask);
	}
	
	/**
	 * ����ͨSMG��������ͨ��
	 * @throws IOException
	 */
	public void bindToSMG() throws IOException{
			// --------------------����
			socket = SocketFactory.getDefault().createSocket(unicomIp,unicomPort);
			socket.setTcpNoDelay(true);// ������������
			socket.setTrafficClass(0x04 | 0x10);//�߿ɿ��Ժ���С�ӳ�
			bindstartTime = System.currentTimeMillis();
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
			log.info(Thread.currentThread().getName() + "����SMG����Socket����");
			// --------------------��
			Bind bind = new Bind();
			//1��SP��SMG���������ӣ����ڷ�������
			//2��SMG��SP���������ӣ����ڷ�������
			//3��SMG֮�佨�������ӣ�����ת������
			bind.setLoginType((byte) 1); 
			bind.setLoginName(spLoginName);
			bind.setLoginPassword(spLogPassword);
			bind.write(out);
			log.info(Thread.currentThread().getName() + "��SMG����bind����");
			//--------------------����Ӧ
			isSendUnbind = false;
			BindResp res = (BindResp) bind.read(in);
			if (res.getResult() != 0) {
				log.fatal("SMG�ܾ����ӡ������룺" + res.getResult()+"ԭ��"+SGIPCodeHelper.getDescription(res.getResult()));
				throw new SGIPException(res.getResult());
			}
			log.info(Thread.currentThread().getName() + "�ѳɹ���SMG��������Ϣ�·�ͨ����");
			this.launchTimer();//������ʱ��
	}

	//����ͨ�ݽ������·�
	//�˵ط���synchronized������ԭ���Ƿ�ֹ��ʱ���߳������ʱ���ѵ���SMG����unbind����
	//��SMG��SP���ӶϿ�������ʱ�պ����·�ʵ��������У��������ͬ�����ƣ���ô��ʱ������Ͽ����ӣ����·��߳�����Ͽ������ӷ���
	//���ݵ���IO���쳣
	public  void sendMTReq(MTReq mtreq) throws IOException {
		if( isSendUnbind ){
			synchronized (this) {
				if (isSendUnbind ) { // �������SMG������unbind�����������SMG��������
					bindToSMG();
				}
			}
		}
		// --------------------�·�
		for (int i = 0; i < mtreq.getPhoneList().size(); i++) {
			Submit submit = new Submit();
			submit.setSPNumber(mtreq.getSpNumber());
			submit.setChargeNumber("000000000000000000000");// ����21,���ȫ0��ʾ��SP֧���������ŷ���
			submit.setUserNumber(new String[] { mtreq.getPhoneList().get(i) });
			submit.setCorpId(spCorpId); //��λ��ҵ����
			submit.setFeeType((byte) 0);
			submit.setFeeValue("0");
			submit.setGivenValue("0");
			submit.setAgentFlag((byte) 0);
			submit.setMorelatetoMTFlag((byte) 2); // 0-MO�㲥����ĵ�һ��MT��Ϣ
			// 1-MO�㲥����ķǵ�һ����Ϣ
			// 2-��MO�㲥�������MT��Ϣ(����ҵ��)
			// 3ϵͳ���������MT��Ϣ
			submit.setPriority((byte) 0);
			submit.setExpireTime("");
			submit.setScheduleTime("");
			submit.setReportFlag(Byte.valueOf(mtreq.getReportFlag())); // �Ƿ���SP����״̬
			submit.setTP_pid((byte) 0);
			submit.setTP_udhi((byte) 0);
			submit.setMessageCoding((byte) 15);
			submit.setMessageType((byte) 0);
			submit.setMessageContent(mtreq.getMessageContent());
			submit.setUserCoun((byte) 1); // ����sgip1.2��չЭ�������1,������Ϊҵ��Ƿ�������
			submit.setServiceType(mtreq.getServiceType());
			submit.setLinkID(mtreq.getLinkId());
			submit.write(out);
			SubmitResp submitres = (SubmitResp) submit.read(in);
			if (submitres.getResult() == 0) {
				log.info("��" + Thread.currentThread().getName()+ " ���͵�MT����ɹ��ݽ���SMG ��");
				//���·�ʵ����ӵ��ѷ���������
				mtSendedMap.put(submit.header.getSequenceNumber(),submit);
				continue ; //����������һ������
			}
			log.warn("��" + Thread.currentThread().getName()+ " ���͵�MT����ݽ���SMGʧ��!,������ " + submitres.getResult() + "��");
		}
	}
	
	/**
	 * �ü�ʱ�����ڼ����SMG������ʱ�䣬�������Ϣ���Ͷ���Ϊ�գ��ҿ���ʱ�䳬��30��
	 * ����SMG����unbind������յ�SMG��unbind_resp��Ӧ��SP�Ͽ�����
	 * 
	 * �޸ģ�2011-04-15
	 *         ��Timer��ΪScheduledExecutorService,���Timer�����Ὣ���⴫Ⱦ����ù�ĵ�����
	 * �����������·��߳�ȫ���ж�
	 * 
	 */
	public void launchTimer() {
		//�̳߳��ܰ�ʱ��ƻ���ִ�����������û��趨�ƻ�ִ�������ʱ�䣬int���͵Ĳ������趨   
	    //�̳߳����̵߳���С��Ŀ��������϶�ʱ���̳߳ؿ��ܻ��Զ���������Ĺ����߳���ִ������   
	    final ScheduledExecutorService scheduExec = Executors.newScheduledThreadPool(1);   

		Runnable task = new Runnable() {
			public void run() {
				if(mtReqQueue.isEmpty()) {
					currentTime = System.currentTimeMillis();
					int passedTime = (int) ((currentTime - bindstartTime) / 1000);
					if(passedTime > 30) {
						synchronized (this) { //���ж���������ֹ�ڲ��SMG��·ʱ�������߳���SMG��������,����
							                  //SMG��Ϊ�û�״̬���������ܾ������·�����
							// ��SMG����unbind����
							Unbind unbind = new Unbind();
							UnbindResp resp = null;
							try {
								unbind.write(out);
								log.info(Thread.currentThread().getName()+" ��SMG����unbind����");
								resp = (UnbindResp) unbind.read(in);
							} 	catch (IOException ex) { log.error("��SMG����unbind����ʱIO�쳣", ex); }
							
							if (Arrays.equals(resp.header.getCommandId(),SGIPCommandDefine.SGIP_UNBIND_RESP)) {
								isSendUnbind = true; //����ѷ���unbind����
								log.info("SMG�յ�unbind���SP�ر�����");
								scheduExec.shutdown(); //��ʱֹͣ
								//�ͷ�socket��Դ
								try {
									if(in !=null) in.close();
									if(out !=null) out.close();
									if(socket !=null) socket.close();
								} catch(IOException e) {
									log.warn("�ͷ�socket��Դ�����쳣");
								}
							}
						}
					}
				}
			}
		};
		scheduExec.scheduleWithFixedDelay(task, 0, 1,TimeUnit.SECONDS);   //1���Ӽ��һ��
	}
	
	/**
	 *�߳�й¶����
	 */
	class UEHLogger implements Thread.UncaughtExceptionHandler {
		@Override
		public void uncaughtException(Thread t, Throwable e) {
			log.warn("�߳� "+t.getName()+" ��" + e.getCause().getMessage()+"ԭ�����ֹ!");
		}
    }   
}