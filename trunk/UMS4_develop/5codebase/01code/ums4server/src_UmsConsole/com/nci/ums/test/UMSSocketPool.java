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
	 * UMS�������˿�,Ĭ����10000
	 */
	public int remotePort = 10000;
	/**
	 * Ӧ��ID
	 */
	public String appID = "1001";
	/**
	 * Ӧ������
	 */
	public String appPsw = "1001";

	private Timer timer;

	private static UMSSocketPool pool;

	public static final int START_INDEX = 0;

	public static final int MAX_MESSAGE_ONE_GROUP = 200;

	/**
	 * ÿ��socket�ַ��Ķ�����Ŀ
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
	 * ��ʼ���ء�
	 * 
	 * @param size
	 *            ָ���ĳ�ʼ����socket�ĸ�����
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
	 * ���ص���Ч�ԣ����δ���������ӣ����ĳЩ����Ч�����ӣ����Ƴ��������ӡ�
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
				// ����ums2.0û���������ƣ���ˣ����ｫcheckpassword��Ϊһ���������Դ�����֤����
				if (bean.isActive()) {
					bean.checkPassword(appID, appPsw);
				}

				if (bean.isClosed() || !bean.isConnected() || !bean.isActive()
						|| closeFlag) {
					closeFlag = true; // һ������һ��closeFlag������Ҫȫ����ʼ������,�����������˵�Ч�ʣ������㴦�����ң��ھ��󲿷�����£�һ�����ӵĶϿ���ζ�ŷ������жϹ��������е����Ӷ��Ͽ���
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
	 * ��Ϣ���ͣ�����������Ⱥ��
	 * 
	 * @param resultMap
	 *            ��������������ǲ������Ƽ�ʹ��ConcurrentHashMap��
	 * @param phoneNumbers
	 *            ��Ϣ���յ��ֻ���
	 * @param content
	 *            ��Ϣ����
	 * @return Map����(key���ֻ��ţ�value�Ƿ��ͽ����0000����ɹ�������ֵ����ʧ��)���������߳�ģʽ�������Ҫ��ѭ�ȴ���Map����Ĵ�СΪphoneNumbers����ʱ�����ܶ϶������Ѿ�������ɣ���Щ���ͳ�ȥ�ˣ���Щû��
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
				// ��һ����ѭ����
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
	 * ��Ϣ�����߳�
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
		 * ��Ϣ�����߳�
		 * 
		 * @param resultMap
		 *            ��ŷ��ͽ����Map��key���ֻ��ţ�value�Ƿ��ͽ����0000��ʾ�ɹ�������ֵ��ʾʧ��
		 * @param bean
		 *            SocketBean����
		 * @param phoneNumbers
		 *            ������Ϣ���ֻ��ż���
		 * @param content
		 *            ���͵���Ϣ����
		 * @param frame_index
		 *            ����֡��ţ�ÿ��֡�����ض���������Ϣ��@see COUNTS_MSG_PERSOCKET
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
					// ���Ѿ���������Ϣ������Ϻ�����ѭ��
					if (i > phoneNumbers.length - 1)
						break;
					if (phoneNumbers[i] != null || !phoneNumbers[i].equals("")) {
						if (bean.isActive()) {
							int result = bean.sendSingleMessage(appID,
									phoneNumbers[i], content, "", "0", "", "",
									"", "0", "");
							resultMap.put(phoneNumbers[i], new Integer(result));
						} else {
							// ֱ��˵������ʧ�ܣ�7777��һ�����ţ�˵����socket�Ѿ���������Ͽ�
							resultMap.put(phoneNumbers[i], new Integer(7777));
						}
					}
				}
				// �����ﲻ�ܳɹ�����ʧ�ܣ��Ѿ���һ����Ϣ���ͳ�ȥ

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * ��װ��socket�����Դ���Ϊ���е�һ��Ԫ��
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
		 * ��֤Ӧ�úϷ���
		 * 
		 * @param appID
		 *            Ӧ��ID
		 * @param password
		 *            Ӧ������
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
		 * ������Ϣ�ķ���
		 * 
		 * @param appID
		 *            Ӧ��ID
		 * @param mobilePhone
		 *            ��Ϣ���յ��ֻ���
		 * @param content
		 *            ��Ϣ����
		 * @param reply
		 *            �ظ���־
		 * @param ack
		 *            ��ִ��־
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
//					if (retCodeStr.equalsIgnoreCase("0000")) {// ����"0000"��ʾ���ͳɹ����������"0000"�����Ϣ����ʧ��
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
				//***********������Ϣ�峤��****************
				int len = content.length();
				int count = 1;
				if (len % ums_length == 0) {
					count = len / ums_length;
				} else {
					count = len / ums_length + 1;
				}
				//************��ÿ70�������ַ����нض�******************
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
						if (retCodeStr.equalsIgnoreCase("0000")) {// ����"0000"��ʾ���ͳɹ����������"0000"�����Ϣ����ʧ��
							result = 0;
						} else {
							System.out.println("���Ͷ��Ÿ�"+mobilePhone+"ʧ�ܣ���������"+retCodeStr);
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
					if (retCodeStr.equalsIgnoreCase("0000")) {// ����"0000"��ʾ���ͳɹ����������"0000"�����Ϣ����ʧ��
						result = 0;
					} else {
						System.out.println("���Ͷ��Ÿ�"+mobilePhone+"ʧ�ܣ���������"+retCodeStr);
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
	 * ��ʱ����Ӧ�¼���ÿһ��ʱ�������socke�������Ч��
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
				String content = "�����Ͱ������������Ͱ������������Ͱ������������Ͱ������������Ͱ������������Ͱ������������Ͱ������������Ͱ������������Ͱ������͵���ʮ������ʮ��ʮ����ʮ�о����İ��о������о����İ��о������о����İ��о������о����İ��о�����";
				Map resultMap = null;
				long startTime = System.currentTimeMillis();
				try {
					resultMap = umsSocketPool.send(UMSSocketPool.START_INDEX,
							phoneNumbers, content);
					// TODO �жϷ��صĽ�����˴��ԡ�

				} catch (OutOfMaxMessageCountsException e1) {
					// ����Ⱥ������
					e1.printStackTrace();
				}
				System.out.println("����" + n + "����Ϣ����ʱ"
						+ (System.currentTimeMillis() - startTime) + "����");
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

		// ȡ��Ϣ����
		int length = msg.getBytes().length;
		byte[] temp = new byte[length];
		int i = 0;
		int j = 0;

		// ���ֽ�ѹ��
		for (i = 0; i < length; i++) {
			// ȡһ���ֽ�
			int b1 = Integer.parseInt(msg.substring(i, i + 1), 16);
			// ��λ
			b1 <<= 4;
			i++;
			if (i < length) {
				// ȡ��һ���ֽ�
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
		super("��󵥴�Ⱥ�������ܳ���200��");
	}
}
