package com.nci.ums.test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.Timer;

public class UMSSocketPool {

	public static final int POOL_SIZE_STANDARD = 20;

	private ConcurrentHashMap poolMap = new ConcurrentHashMap(
			POOL_SIZE_STANDARD * 2);

	private int serial = 0;

	public static final int MAX_SERIAL = 100000;

	public String remoteIP = "127.0.0.1";

	/**
	 * UMS服务器端口,默认是10000
	 */
	public int remotePort = 10000;
	/**
	 * 应用ID
	 */
	public String appID = "1001";
	/**
	 * 应用密码
	 */
	public String appPsw = "1001";

	private Timer timer;

	private static UMSSocketPool pool;

	public static final int START_INDEX = 0;

	public static final int MAX_MESSAGE_ONE_GROUP = 200;

	/**
	 * 每个socket分发的短信数目
	 */
	public static final int COUNTS_MSG_PERSOCKET = 20;

	private ReentrantLock lock = new ReentrantLock();

	private UMSSocketPool() {
		iniSocketPool(POOL_SIZE_STANDARD);
		timer = new Timer(90 * 1000, new CheckPoolAction());
		timer.start();
	}

	public static synchronized UMSSocketPool getInstance() {
		if (pool == null) {
			pool = new UMSSocketPool();
		}
		return pool;
	}

	/**
	 * 初始化池。
	 * 
	 * @param size
	 *            指定的初始化的socket的个数，
	 */
	private void iniSocketPool(int size) {

		for (int i = 0; i < size; i++) {
			SocketBean bean = new SocketBean(remoteIP, remotePort);
			if (serial > MAX_SERIAL) {
				serial = 0;
			}
			poolMap.put(String.valueOf(serial), bean);
			serial++;

		}
	}

	/**
	 * 检查池的有效性，如果未满，则增加；如果某些是无效的链接，则移除，并增加。
	 */
	private void checkPool() {
		lock.lock();
		try {
			int currentPoolSize = poolMap.size();
			if (currentPoolSize < POOL_SIZE_STANDARD) {
				iniSocketPool(POOL_SIZE_STANDARD - currentPoolSize);
			}
			Iterator it = poolMap.keySet().iterator();
			boolean closeFlag = false;
			while (it.hasNext()) {
				String serial = (String) it.next();
				SocketBean bean = (SocketBean) poolMap.get(serial);
				// 由于ums2.0没有心跳机制，因此，这里将checkpassword作为一个心跳，以此来保证链接
				if (bean.isActive()) {
					bean.checkPassword(appID, appPsw);
				}

				if (bean.isClosed() || !bean.isConnected() || !bean.isActive()
						|| closeFlag) {
					closeFlag = true; // 一旦出现一个closeFlag，则需要全部初始化链接,这样做牺牲了点效率，但方便处理，而且，在绝大部分情况下，一个链接的断开意味着服务器中断过，则所有的链接都断开；
					poolMap.remove(serial);
					try {
						bean.close();
					} catch (Exception e) {

					}
					bean = null;
				}
			}
			if (closeFlag) {
				iniSocketPool(POOL_SIZE_STANDARD);
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 消息发送，包括单发和群发
	 * 
	 * @param resultMap
	 *            结果表，这个表必须是并发表（推荐使用ConcurrentHashMap）
	 * @param phoneNumbers
	 *            消息接收的手机号
	 * @param content
	 *            消息内容
	 * @return Map对象(key是手机号，value是发送结果，0000代表成功，其他值代表失败)，由于是线程模式，因此需要轮循等待至Map对象的大小为phoneNumbers长度时，才能断定最终已经发送完成，哪些发送出去了，哪些没有
	 */
	public synchronized Map send(int frame_index, String[] phoneNumbers,
			String content) throws OutOfMaxMessageCountsException {
		if (phoneNumbers.length > MAX_MESSAGE_ONE_GROUP) {
			throw new OutOfMaxMessageCountsException();
		}
		lock.lock();
		try {
			timer.stop();
			Map resultMap = new ConcurrentHashMap();
			Iterator it = poolMap.keySet().iterator();
			int activeSocketCounts = poolMap.size();
			while (it.hasNext()) {
				String serial = (String) it.next();
				SocketBean bean = (SocketBean) poolMap.get(serial);
				new MessageSender(resultMap, bean, phoneNumbers, content,
						frame_index).start();
				frame_index++;
				if (phoneNumbers.length - frame_index * COUNTS_MSG_PERSOCKET <= 0) {
					break;
				}
			}
			if (frame_index > activeSocketCounts) {
				checkPool();
				// 下一个轮循发送
				send(frame_index, phoneNumbers, content);
			}
			while (resultMap.size() != phoneNumbers.length) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return resultMap;
		} finally {
			lock.unlock();
			timer.start();
		}
	}

	/**
	 * 消息发送线程
	 * 
	 * @author Qil.Wong
	 * 
	 */
	private class MessageSender extends Thread {
		private Map resultMap;
		private SocketBean bean;
		private String[] phoneNumbers;
		private String content;
		private int frame_index;

		/**
		 * 消息发送线程
		 * 
		 * @param resultMap
		 *            存放发送结果的Map，key是手机号，value是发送结果，0000表示成功，其他值表示失败
		 * @param bean
		 *            SocketBean对象
		 * @param phoneNumbers
		 *            接收消息的手机号集合
		 * @param content
		 *            发送的消息内容
		 * @param frame_index
		 *            发送帧序号，每个帧发送特定数量的消息；@see COUNTS_MSG_PERSOCKET
		 */
		public MessageSender(Map resultMap, SocketBean bean,
				String[] phoneNumbers, String content, int frame_index) {
			this.resultMap = resultMap;
			this.bean = bean;
			this.phoneNumbers = phoneNumbers;
			this.content = content;
			this.frame_index = frame_index;
		}

		public void run() {
			try {
				for (int i = frame_index * COUNTS_MSG_PERSOCKET; i < COUNTS_MSG_PERSOCKET
						* (frame_index + 1); i++) {
					// 当已经将所有信息发送完毕后，跳出循环
					if (i > phoneNumbers.length - 1)
						break;
					if (phoneNumbers[i] != null || !phoneNumbers[i].equals("")) {
						if (bean.isActive()) {
							int result = bean.sendSingleMessage(appID,
									phoneNumbers[i], content, "", "0", "", "",
									"", "0", "");
							resultMap.put(phoneNumbers[i], new Integer(result));
						} else {
							// 直接说明发送失败，7777是一个代号，说明该socket已经与服务器断开
							resultMap.put(phoneNumbers[i], new Integer(7777));
						}
					}
				}
				// 到这里不管成功还是失败，已经将一列消息发送出去

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 包装的socket对象，以此作为池中的一个元素
	 * 
	 * @author Qil.Wong
	 * 
	 */
	private class SocketBean {
		private Socket client;
		private DataInputStream in;
		private DataOutputStream out;
		private boolean active = true;
		public final int MAX_PACKET_SIZE = 1024;

		public byte[] buffer = new byte[MAX_PACKET_SIZE];

		private SocketBean(String remoteIP, int remotePort) {
			try {
				InetAddress addr = InetAddress.getByName(remoteIP);

				client = new Socket(addr, remotePort);
				client.setSoTimeout(10 * 1000);
				in = new DataInputStream(client.getInputStream());
				out = new DataOutputStream(client.getOutputStream());
				checkPassword(appID, appPsw);
			} catch (Exception e) {
				active = false;
				e.printStackTrace();
			}
		}

		/**
		 * 验证应用合法性
		 * 
		 * @param appID
		 *            应用ID
		 * @param password
		 *            应用密码
		 * @return
		 */
		public boolean checkPassword(String appID, String password) {
			boolean result = false;
			StringBuffer sb = new StringBuffer("38   100101").append(
					PoolUtil.getFixedString(appID, 12)).append(
					PoolUtil.getFixedString(password, 20));
			try {
				out.write(sb.toString().getBytes());
				byte[] rets = new byte[1024];
				in.read(rets);
				byte retCode[] = new byte[4];
				for (int i = 5; i <= 8; i++) {
					retCode[i - 5] = rets[i];
				}
				String retCodeStr = new String(retCode);
				if (retCodeStr.equalsIgnoreCase("0000")) {
					result = true;
					System.out.println("password invalid passed");
				}
			} catch (Exception e) {
				active = false;
				e.printStackTrace();
			}
			return result;
		}

		/**
		 * 单个消息的发送
		 * 
		 * @param appID
		 *            应用ID
		 * @param mobilePhone
		 *            消息接收的手机号
		 * @param content
		 *            消息内容
		 * @param reply
		 *            回复标志
		 * @param ack
		 *            回执标志
		 * @param id
		 * @param rep
		 * @param priority
		 * @param messageType
		 * @param subApp
		 * @return
		 */
		public int sendSingleMessage(String appID, String mobilePhone,
				String content, String reply, String ack, String id,
				String rep, String priority, String messageType, String subApp) {
			int result = 1;
			int ums_length = 63;
//			ArrayList subContent = new ArrayList();
//			while (content != null && !content.equals("")) {
//				if (content.length() > ums_length) {
//					String c1 = content.substring(0, ums_length);
//					subContent.add(c1);
//					content = content.substring(ums_length);
//				} else {
//					subContent.add(content);
//					content = null;
//				}
//			}
//			for (int n = 0; n < subContent.size(); n++) {
//				StringBuffer sb = new StringBuffer("506  100201");
//				sb.append(PoolUtil.getFixedString(appID, 12)).append(
//						PoolUtil.getFixedString(id, 35)).append(
//						PoolUtil.getFixedString(messageType, 3)).append(
//						PoolUtil.getFixedString(mobilePhone, 255)).append(
//						PoolUtil.getFixedString(subContent.get(n).toString(),
//								160)).append(ack).append(
//						PoolUtil.getFixedString(reply, 30)).append(
//						PoolUtil.getFixedString("", 2)).append(
//						PoolUtil.getFixedString("", 2));
//				try {
//					out.write(sb.toString().getBytes());
//					byte[] rets = new byte[1024];
//					in.read(rets);
//					byte retCode[] = new byte[4];
//					for (int i = 5; i <= 8; i++) {
//						retCode[i - 5] = rets[i];
//					}
//					String retCodeStr = new String(retCode);
//					if (retCodeStr.equalsIgnoreCase("0000")) {// 返回"0000"表示发送成功，如果不是"0000"则该消息发送失败
//						result = 0;
//					}
//				} catch (IOException e) {
//					active = false;
//					e.printStackTrace();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
			
			
			if (content.length() > ums_length) {
				//***********分析消息体长度****************
				int len = content.length();
				int count = 1;
				if (len % ums_length == 0) {
					count = len / ums_length;
				} else {
					count = len / ums_length + 1;
				}
				//************按每70个中文字符进行截断******************
				for (int j = 0; j < count; j++) {
					String tempConent = "";

					if (j == count - 1) {
						tempConent = content.substring(j * ums_length);
					} else {
						tempConent = content.substring(j * ums_length,
								(j + 1) * ums_length);
					}
                                        
					StringBuffer sb=new StringBuffer("506  100201");
				    sb.append(Util.getFixedString(appID,12)).append(Util.getFixedString(id,35)).
					append(Util.getFixedString(messageType,3)).append(Util.getFixedString(mobilePhone,255)).
					append(Util.getFixedString(tempConent,160)).append(ack).append(Util.getFixedString(reply,30)).
					append(Util.getFixedString("",2)).append(Util.getFixedString("",2));
				    	    	
				    //System.out.println(sb.toString().length());
					try{			
						out.write(sb.toString().getBytes());
						int size=in.read(buffer);
						byte retCode[]=new byte[4];
						for(int i = 5;i<=8;i++){
							retCode[i-5]= buffer[i];
						}
						String retCodeStr=new String(retCode);
						if (retCodeStr.equalsIgnoreCase("0000")) {// 返回"0000"表示发送成功，如果不是"0000"则该消息发送失败
							result = 0;
						} else {
							System.out.println("发送短信给"+mobilePhone+"失败，返回码是"+retCodeStr);
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}else{
				StringBuffer sb=new StringBuffer("506  100201");
			    sb.append(Util.getFixedString(appID,12)).append(Util.getFixedString(id,35)).
				append(Util.getFixedString(messageType,3)).append(Util.getFixedString(mobilePhone,255)).
				append(Util.getFixedString(content,160)).append(ack).append(Util.getFixedString(reply,30)).
				append(Util.getFixedString("",2)).append(Util.getFixedString("",2));
			    	    	
			    //System.out.println(sb.toString().length());
				try{			
					out.write(sb.toString().getBytes());
					int size=in.read(buffer);
					byte retCode[]=new byte[4];
					for(int i = 5;i<=8;i++){
						retCode[i-5]= buffer[i];
					}
					String retCodeStr=new String(retCode);
					if (retCodeStr.equalsIgnoreCase("0000")) {// 返回"0000"表示发送成功，如果不是"0000"则该消息发送失败
						result = 0;
					} else {
						System.out.println("发送短信给"+mobilePhone+"失败，返回码是"+retCodeStr);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
			
			return result;
		}

		public void close() throws Exception {
			client.close();
		}

		public boolean isConnected() {
			try {
				return client.isConnected();
			} catch (Exception e) {
				return false;
			}
		}

		public boolean isClosed() {
			try {
				return client.isClosed();
			} catch (Exception e) {
				return false;
			}

		}

		public boolean isActive() {
			return active;
		}

	}

	/**
	 * 定时器响应事件，每一定时间检查池中socke对象的有效性
	 * 
	 * @author Qil.Wong
	 * 
	 */
	private class CheckPoolAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			checkPool();
		}

	}

	public static void main(String[] xWin) {
		JFrame frame = new JFrame("Just4Test");
		JButton btn = new JButton("send");
		btn.setMnemonic(KeyEvent.VK_S);
		frame.getContentPane().add(btn);
		btn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				UMSSocketPool umsSocketPool = UMSSocketPool.getInstance();
				int n = 1;
				String[] phoneNumbers = new String[n];
				long p = 13819155409L;
				for (int i = 0; i < n; i++) {
					phoneNumbers[i] = String.valueOf(p);
					p++;
				}
				String content = "新世纪啊新世纪新世纪啊新世纪新世纪啊新世纪新世纪啊新世纪新世纪啊新世纪新世纪啊新世纪新世纪啊新世纪新世纪啊新世纪新世纪啊新世纪第七十啊第七十八十啊八十研究中心啊研究中心研究中心啊研究中心研究中心啊研究中心研究中心啊研究中心";
				Map resultMap = null;
				long startTime = System.currentTimeMillis();
				try {
					resultMap = umsSocketPool.send(UMSSocketPool.START_INDEX,
							phoneNumbers, content);
					// TODO 判断返回的结果，此处略。

				} catch (OutOfMaxMessageCountsException e1) {
					// 超出群发极限
					e1.printStackTrace();
				}
				System.out.println("发送" + n + "条信息，耗时"
						+ (System.currentTimeMillis() - startTime) + "毫秒");
			}

		});
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}

class PoolUtil {
	public static String getFixedString(String msg, int len) {
		if (msg == null) {
			msg = "";
		}

		StringBuffer sb = new StringBuffer(msg);
		for (int i = msg.getBytes().length + 1; i <= len; i++) {
			sb.append(" ");
		}
		return sb.toString();
	}

	public static byte[] getByte(String msg, int data_code) {
		try {
			if (data_code == 15) {
				return msg.getBytes("GBK");
			} else if (data_code == 0) {
				return msg.getBytes("8859_1");
			} else if (data_code == 21 || data_code == 4) {
				return PoolUtil.getByte(msg);
			} else {
				return msg.getBytes("UnicodeBig");
			}
		} catch (Exception e) {
			return msg.getBytes();
		}
	}

	public static byte[] getByte(String msg) {

		// 取消息长度
		int length = msg.getBytes().length;
		byte[] temp = new byte[length];
		int i = 0;
		int j = 0;

		// 按字节压缩
		for (i = 0; i < length; i++) {
			// 取一个字节
			int b1 = Integer.parseInt(msg.substring(i, i + 1), 16);
			// 移位
			b1 <<= 4;
			i++;
			if (i < length) {
				// 取下一个字节
				int b2 = Integer.parseInt(msg.substring(i, i + 1), 16);
				b1 = b1 + b2;
			}
			temp[j] = (byte) b1;
			j++;
		}
		byte[] result = new byte[j];
		for (int f = 0; f < j; f++) {
			result[f] = temp[f];
		}
		// result[j]=0x0;
		return result;
	}
}

class OutOfMaxMessageCountsException extends Exception {

	private static final long serialVersionUID = 2874610190973548906L;

	public OutOfMaxMessageCountsException() {
		super("最大单次群发数不能超过200！");
	}
}
