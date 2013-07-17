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
 * @description  短信下发类
 */
public final class SPSender {
	private Logger log = Logger.getLogger(getClass());
	//等街下发短信的队列，队列数限定1000，防止发生内存消耗过大，而发生Java heap space异常
	private BlockingQueue<Runnable> mtReqQueue = new LinkedBlockingQueue<Runnable>(1000);
	private volatile boolean isSendUnbind = true;// 是否已向SMG发送 unbind命令的标志
	
	private static SPSender instance = null;

	private Socket socket = null;
	private DataOutputStream out = null;
	private DataInputStream in = null;
	
	private long bindstartTime;
	private long currentTime;
	private static Map<String, Submit> mtSendedMap;  //已发送的下发实例
	//以下信息在productionConf.xml里配置
	protected static String unicomIp;     //联通SMG的IP地址
	protected static int unicomPort;      //联通SMG监听的端口号
	protected static String spLoginName;  //登陆SMG所用到的用户名
	protected static String spLogPassword;//登陆SMG所用到的密码
	protected static String spCorpId;        //5位企业代码
	
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
    //每颗CPU分配两个线程，当有线程异常退出时，自动生成的新线程替代已异常退出的线程
	//当下发线程全部忙碌时，采用谁调用谁发送的策略
	//新生成的线程
	int threadAmount = Runtime.getRuntime().availableProcessors() * 2;
	private ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(threadAmount,threadAmount, 1000 * 30, 
			TimeUnit.SECONDS, mtReqQueue, threadFactory,new ThreadPoolExecutor.CallerRunsPolicy()); 

	/**
	 * 单态模式，在获取实例时作双重检测，防止多线程情况下产生多个实例
	 * @return　SPSender
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
	 * 向队列添加任务
	 * @param mtreq 短信下发请求
	 */
	public void addTask(final MTReq mtreq) {
		 final Runnable sendTask = new Runnable() {
			public void run() {
				try {
					sendMTReq(mtreq);
				} catch (IOException e) {
					// 发送短信异常,重新放回发送队列
					log.warn("短信下发时发生IO流异常:" + e.getMessage()
							+ ".　短信下发实例重新放回发送队列！");
					mtReqQueue.add(this);
				} 
			}
		};
		poolExecutor.execute(sendTask);
	}
	
	/**
	 * 与联通SMG建立短信通道
	 * @throws IOException
	 */
	public void bindToSMG() throws IOException{
			// --------------------连接
			socket = SocketFactory.getDefault().createSocket(unicomIp,unicomPort);
			socket.setTcpNoDelay(true);// 数据立即发送
			socket.setTrafficClass(0x04 | 0x10);//高可靠性和最小延迟
			bindstartTime = System.currentTimeMillis();
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
			log.info(Thread.currentThread().getName() + "已与SMG建立Socket连接");
			// --------------------绑定
			Bind bind = new Bind();
			//1：SP向SMG建立的连接，用于发送命令
			//2：SMG向SP建立的连接，用于发送命令
			//3：SMG之间建立的连接，用于转发命令
			bind.setLoginType((byte) 1); 
			bind.setLoginName(spLoginName);
			bind.setLoginPassword(spLogPassword);
			bind.write(out);
			log.info(Thread.currentThread().getName() + "向SMG发送bind请求");
			//--------------------绑定响应
			isSendUnbind = false;
			BindResp res = (BindResp) bind.read(in);
			if (res.getResult() != 0) {
				log.fatal("SMG拒绝连接。错误码：" + res.getResult()+"原因："+SGIPCodeHelper.getDescription(res.getResult()));
				throw new SGIPException(res.getResult());
			}
			log.info(Thread.currentThread().getName() + "已成功与SMG建立短消息下发通道。");
			this.launchTimer();//启动计时器
	}

	//向联通递交短信下发
	//此地方用synchronized防护的原因是防止计时器线程因空闲时间已到向SMG发送unbind命令
	//将SMG与SP连接断开，而此时刚好有下发实例进入队列，如果不加同步控制，那么计时器请求断开连接，而下发线程则向断开的连接发送
	//数据导致IO流异常
	public  void sendMTReq(MTReq mtreq) throws IOException {
		if( isSendUnbind ){
			synchronized (this) {
				if (isSendUnbind ) { // 如果已向SMG发送了unbind命令，则重新与SMG建立连接
					bindToSMG();
				}
			}
		}
		// --------------------下发
		for (int i = 0; i < mtreq.getPhoneList().size(); i++) {
			Submit submit = new Submit();
			submit.setSPNumber(mtreq.getSpNumber());
			submit.setChargeNumber("000000000000000000000");// 长度21,如果全0表示由SP支付该条短信费用
			submit.setUserNumber(new String[] { mtreq.getPhoneList().get(i) });
			submit.setCorpId(spCorpId); //四位企业代码
			submit.setFeeType((byte) 0);
			submit.setFeeValue("0");
			submit.setGivenValue("0");
			submit.setAgentFlag((byte) 0);
			submit.setMorelatetoMTFlag((byte) 2); // 0-MO点播引起的第一条MT信息
			// 1-MO点播引起的非第一条信息
			// 2-非MO点播引引起的MT消息(定购业务)
			// 3系统反馈引起的MT消息
			submit.setPriority((byte) 0);
			submit.setExpireTime("");
			submit.setScheduleTime("");
			submit.setReportFlag(Byte.valueOf(mtreq.getReportFlag())); // 是否向SP报告状态
			submit.setTP_pid((byte) 0);
			submit.setTP_udhi((byte) 0);
			submit.setMessageCoding((byte) 15);
			submit.setMessageType((byte) 0);
			submit.setMessageContent(mtreq.getMessageContent());
			submit.setUserCoun((byte) 1); // 根据sgip1.2扩展协议必须填1,否则视为业务非法包处理
			submit.setServiceType(mtreq.getServiceType());
			submit.setLinkID(mtreq.getLinkId());
			submit.write(out);
			SubmitResp submitres = (SubmitResp) submit.read(in);
			if (submitres.getResult() == 0) {
				log.info("【" + Thread.currentThread().getName()+ " 发送的MT请求成功递交到SMG 】");
				//将下发实例添加到已发送容器中
				mtSendedMap.put(submit.header.getSequenceNumber(),submit);
				continue ; //继续发送下一条短信
			}
			log.warn("【" + Thread.currentThread().getName()+ " 发送的MT请求递交到SMG失败!,错误码 " + submitres.getResult() + "】");
		}
	}
	
	/**
	 * 该计时器用于检测与SMG建立的时间，如果短消息发送队列为空，且空闲时间超过30秒
	 * 则向SMG发送unbind命令，在收到SMG的unbind_resp响应后SP断开连接
	 * 
	 * 修改：2011-04-15
	 *         将Timer改为ScheduledExecutorService,如果Timer出错，会将问题传染给倒霉的调用者
	 * 　　　导致下发线程全部中断
	 * 
	 */
	public void launchTimer() {
		//线程池能按时间计划来执行任务，允许用户设定计划执行任务的时间，int类型的参数是设定   
	    //线程池中线程的最小数目。当任务较多时，线程池可能会自动创建更多的工作线程来执行任务   
	    final ScheduledExecutorService scheduExec = Executors.newScheduledThreadPool(1);   

		Runnable task = new Runnable() {
			public void run() {
				if(mtReqQueue.isEmpty()) {
					currentTime = System.currentTimeMillis();
					int passedTime = (int) ((currentTime - bindstartTime) / 1000);
					if(passedTime > 30) {
						synchronized (this) { //持有对象锁，防止在拆掉SMG链路时，其它线程与SMG建立连接,导致
							                  //SMG认为用户状态不正常并拒绝短信下发请求
							// 向SMG发送unbind命令
							Unbind unbind = new Unbind();
							UnbindResp resp = null;
							try {
								unbind.write(out);
								log.info(Thread.currentThread().getName()+" 向SMG发送unbind命令");
								resp = (UnbindResp) unbind.read(in);
							} 	catch (IOException ex) { log.error("向SMG发送unbind命令时IO异常", ex); }
							
							if (Arrays.equals(resp.header.getCommandId(),SGIPCommandDefine.SGIP_UNBIND_RESP)) {
								isSendUnbind = true; //标记已发送unbind命令
								log.info("SMG收到unbind命令，SP关闭连接");
								scheduExec.shutdown(); //计时停止
								//释放socket资源
								try {
									if(in !=null) in.close();
									if(out !=null) out.close();
									if(socket !=null) socket.close();
								} catch(IOException e) {
									log.warn("释放socket资源发生异常");
								}
							}
						}
					}
				}
			}
		};
		scheduExec.scheduleWithFixedDelay(task, 0, 1,TimeUnit.SECONDS);   //1秒钟检测一次
	}
	
	/**
	 *线程泄露捕获
	 */
	class UEHLogger implements Thread.UncaughtExceptionHandler {
		@Override
		public void uncaughtException(Thread t, Throwable e) {
			log.warn("线程 "+t.getName()+" 因" + e.getCause().getMessage()+"原因而终止!");
		}
    }   
}