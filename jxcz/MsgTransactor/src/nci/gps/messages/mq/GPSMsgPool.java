package nci.gps.messages.mq;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;

import nci.gps.util.Utilities;

import org.apache.activemq.ActiveMQConnection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import edu.emory.mathcs.backport.java.util.concurrent.ConcurrentHashMap;

public class GPSMsgPool implements ExceptionListener{

	/**
	 * 待转的消息集合(给应用)
	 */
	private ConcurrentHashMap fromFrontMsgMap = new ConcurrentHashMap(1024);

	/**
	 * 待发的消息集合(给前置机)
	 */
	private ConcurrentHashMap toFrontMsgMap = new ConcurrentHashMap(1024);
	/**
	 * 外部注册的应用主题集合
	 */
	private ConcurrentHashMap appSubjectMap = new ConcurrentHashMap();
	/**
	 * 逻辑号的分配集合
	 */
	private ConcurrentHashMap appLogicalAddrsMap = new ConcurrentHashMap();

	/**
	 * 服务器消息生产者哈希Map
	 */
	private ConcurrentHashMap producerMap = new ConcurrentHashMap();
	/**
	 * 服务器JMS的Session哈希Map
	 * 
	 */
	private ConcurrentHashMap sessionMap = new ConcurrentHashMap();

	/**
	 * 服务器JMS的Cnnection哈希Map
	 * 
	 */
	private ConcurrentHashMap jmsConnectionMap = new ConcurrentHashMap();
	/**
	 * jms 的参数配置表
	 */
	private ConcurrentHashMap jmsParamMap = new ConcurrentHashMap();
	/**
	 * 消息在待发集合map中的序号
	 */
	private int fromSerial = 0;
	/**
	 * 消息在转发集合Map中的序号
	 */
	private int toSerial = 0;
	/**
	 * 最大序号数
	 */
	private int MAX_SERIAL = 10000;

	private String replyDestSubject = "";
	
	public void onException(JMSException arg0) {
		System.out.println("xxxx");
		
	}

	public GPSMsgPool() {
		Document sppSubject = Utilities.getConfigXMLDocument("appSubjects.xml");
		NodeList appNodes = sppSubject.getElementsByTagName("app");
		for (int i = 0; i < appNodes.getLength(); i++) {
			Element appEle = (Element) appNodes.item(i);
			appSubjectMap.put(appEle.getAttribute("subject"), appEle
					.getAttribute("ip"));
			NodeList addrNodes = appEle.getElementsByTagName("logicalAddr");
			for (int n = 0; n < addrNodes.getLength(); n++) {
				Element addrEle = (Element) addrNodes.item(n);
				appLogicalAddrsMap.put(addrEle.getAttribute("value"), appEle
						.getAttribute("subject"));
			}
		}
		Element replyDestEle = (Element) sppSubject.getElementsByTagName(
				"serverSubject").item(0);
		replyDestSubject = replyDestEle.getAttribute("subject");

		Document jmsDoc = Utilities.getConfigXMLDocument("jmsConf.xml");
		Element jmsEle = (Element) jmsDoc.getElementsByTagName("jms").item(0);
		jmsParamMap.put("url", jmsEle.getAttribute("url"));
		jmsParamMap.put("user", jmsEle.getAttribute("user"));
		jmsParamMap.put("password", jmsEle.getAttribute("password"));
	}

	/**
	 * 判断是否有前置机发送过来的消息
	 * 
	 * @return
	 */
	public boolean hasFromFrontMessage() {
		return fromFrontMsgMap.size() != 0;
	}

	/**
	 * 判断是否有要发送给前置机的消息
	 * 
	 * @return
	 */
	public boolean hasToFrontMessage() {
		return toFrontMsgMap.size() != 0;
	}

	/**
	 * 将消息放置在待转表（这些消息将被发给应用）,key是serial，value是messagebean
	 * 
	 * @param bean
	 */
	public void addMessageIntoFromPool(MessageBean bean) {
		fromSerial++;
		if (fromSerial > MAX_SERIAL) {
			fromSerial = 0;
		}
		fromFrontMsgMap.put(String.valueOf(fromSerial), bean);
	}

	/**
	 * 将消息放置在待发表（这些消息将发给前置机）,key是serial，value是messagebean
	 * 
	 * @param bean
	 */
//	public void addMessageIntoToPool(MessageBean bean) {
//		toSerial++;
//		if (toSerial > MAX_SERIAL) {
//			toSerial = 0;
//		}
//		toFrontMsgMap.put(String.valueOf(toSerial), bean);
//	}
	
	public void addMessageIntoToPool(byte[] bytes) {
		toSerial++;
		if (toSerial > MAX_SERIAL) {
			toSerial = 0;
		}
		toFrontMsgMap.put(String.valueOf(toSerial), bytes);
	}

	/**
	 * 将待转表中的消息移除
	 * 
	 * @param serial
	 */
	public void removeFromMessage(String serial) {
		fromFrontMsgMap.remove(serial);
	}

	/**
	 * 获取待转表中的消息
	 * 
	 * @param serial
	 *            消息序号
	 * @return
	 */
	public MessageBean getFromMessageBean(String serial) {
		return (MessageBean) fromFrontMsgMap.get(serial);
	}

	/**
	 * 将待发表中的消息移除
	 * 
	 * @param serial
	 */
	public void removeToMessage(String serial) {
		toFrontMsgMap.remove(serial);
	}

	/**
	 * 根据消息序号，获取待发表中的消息
	 * 
	 * @param serial
	 *            消息序号
	 * @return
	 */
	public byte[] getToMessageBytes(String serial) {
		return (byte[])toFrontMsgMap.get(serial);
	}

	/**
	 * 获取逻辑地址-主题对照哈希Map，key是逻辑地址，value是subject
	 * 
	 * @return
	 */
	public ConcurrentHashMap getLogicalAddrsSubjectMap() {
		return appLogicalAddrsMap;
	}

	/**
	 * 获取应用主题对照哈希Map，key是subject，vlaue是ip
	 * 
	 * @return
	 */
	public ConcurrentHashMap getAppSubjectMap() {
		return appSubjectMap;
	}

	/**
	 * 获取JMS连接对照哈希表,key是subject，value是jms connection
	 * 
	 * @return
	 */
	public ConcurrentHashMap getJmsConnectionMap() {
		return jmsConnectionMap;
	}

	/**
	 * 获取从前置机接收过来的消息缓存哈希Map
	 * 
	 * @return
	 */
	public ConcurrentHashMap getFromFrontMsgMap() {
		return fromFrontMsgMap;
	}

	/**
	 * 获取要发送给前置机的消息缓存哈希Map
	 * 
	 * @return
	 */
	public ConcurrentHashMap getToFrontMsgMap() {
		return toFrontMsgMap;
	}

	/**
	 * 获取服务器需要接受客户应用返回的消息所用的主题
	 * 
	 * @return
	 */
	public String getReplyServerSubject() {
		return replyDestSubject;
	}

	/**
	 * 获取消息生产者哈希Map，key是subject，value是Producer
	 * 
	 * @return
	 */
	public ConcurrentHashMap getProducerMap() {
		return producerMap;
	}

	/**
	 * 获取JMS 消息Session哈希Map，key是subject，value是Session
	 * 
	 * @return
	 */
	public ConcurrentHashMap getSessionMap() {
		return sessionMap;
	}

	/**
	 * 获取jms 参数配置
	 * 
	 * @return
	 */
	public ConcurrentHashMap getJMSParamMap() {
		return jmsParamMap;
	}

	public static void main(String[] s) {
		GPSMsgPool sss = new GPSMsgPool();
		System.out.print(sss);
	}
}
