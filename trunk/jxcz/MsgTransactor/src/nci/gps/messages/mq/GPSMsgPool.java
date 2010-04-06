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
	 * ��ת����Ϣ����(��Ӧ��)
	 */
	private ConcurrentHashMap fromFrontMsgMap = new ConcurrentHashMap(1024);

	/**
	 * ��������Ϣ����(��ǰ�û�)
	 */
	private ConcurrentHashMap toFrontMsgMap = new ConcurrentHashMap(1024);
	/**
	 * �ⲿע���Ӧ�����⼯��
	 */
	private ConcurrentHashMap appSubjectMap = new ConcurrentHashMap();
	/**
	 * �߼��ŵķ��伯��
	 */
	private ConcurrentHashMap appLogicalAddrsMap = new ConcurrentHashMap();

	/**
	 * ��������Ϣ�����߹�ϣMap
	 */
	private ConcurrentHashMap producerMap = new ConcurrentHashMap();
	/**
	 * ������JMS��Session��ϣMap
	 * 
	 */
	private ConcurrentHashMap sessionMap = new ConcurrentHashMap();

	/**
	 * ������JMS��Cnnection��ϣMap
	 * 
	 */
	private ConcurrentHashMap jmsConnectionMap = new ConcurrentHashMap();
	/**
	 * jms �Ĳ������ñ�
	 */
	private ConcurrentHashMap jmsParamMap = new ConcurrentHashMap();
	/**
	 * ��Ϣ�ڴ�������map�е����
	 */
	private int fromSerial = 0;
	/**
	 * ��Ϣ��ת������Map�е����
	 */
	private int toSerial = 0;
	/**
	 * ��������
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
	 * �ж��Ƿ���ǰ�û����͹�������Ϣ
	 * 
	 * @return
	 */
	public boolean hasFromFrontMessage() {
		return fromFrontMsgMap.size() != 0;
	}

	/**
	 * �ж��Ƿ���Ҫ���͸�ǰ�û�����Ϣ
	 * 
	 * @return
	 */
	public boolean hasToFrontMessage() {
		return toFrontMsgMap.size() != 0;
	}

	/**
	 * ����Ϣ�����ڴ�ת����Щ��Ϣ��������Ӧ�ã�,key��serial��value��messagebean
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
	 * ����Ϣ�����ڴ�������Щ��Ϣ������ǰ�û���,key��serial��value��messagebean
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
	 * ����ת���е���Ϣ�Ƴ�
	 * 
	 * @param serial
	 */
	public void removeFromMessage(String serial) {
		fromFrontMsgMap.remove(serial);
	}

	/**
	 * ��ȡ��ת���е���Ϣ
	 * 
	 * @param serial
	 *            ��Ϣ���
	 * @return
	 */
	public MessageBean getFromMessageBean(String serial) {
		return (MessageBean) fromFrontMsgMap.get(serial);
	}

	/**
	 * ���������е���Ϣ�Ƴ�
	 * 
	 * @param serial
	 */
	public void removeToMessage(String serial) {
		toFrontMsgMap.remove(serial);
	}

	/**
	 * ������Ϣ��ţ���ȡ�������е���Ϣ
	 * 
	 * @param serial
	 *            ��Ϣ���
	 * @return
	 */
	public byte[] getToMessageBytes(String serial) {
		return (byte[])toFrontMsgMap.get(serial);
	}

	/**
	 * ��ȡ�߼���ַ-������չ�ϣMap��key���߼���ַ��value��subject
	 * 
	 * @return
	 */
	public ConcurrentHashMap getLogicalAddrsSubjectMap() {
		return appLogicalAddrsMap;
	}

	/**
	 * ��ȡӦ��������չ�ϣMap��key��subject��vlaue��ip
	 * 
	 * @return
	 */
	public ConcurrentHashMap getAppSubjectMap() {
		return appSubjectMap;
	}

	/**
	 * ��ȡJMS���Ӷ��չ�ϣ��,key��subject��value��jms connection
	 * 
	 * @return
	 */
	public ConcurrentHashMap getJmsConnectionMap() {
		return jmsConnectionMap;
	}

	/**
	 * ��ȡ��ǰ�û����չ�������Ϣ�����ϣMap
	 * 
	 * @return
	 */
	public ConcurrentHashMap getFromFrontMsgMap() {
		return fromFrontMsgMap;
	}

	/**
	 * ��ȡҪ���͸�ǰ�û�����Ϣ�����ϣMap
	 * 
	 * @return
	 */
	public ConcurrentHashMap getToFrontMsgMap() {
		return toFrontMsgMap;
	}

	/**
	 * ��ȡ��������Ҫ���ܿͻ�Ӧ�÷��ص���Ϣ���õ�����
	 * 
	 * @return
	 */
	public String getReplyServerSubject() {
		return replyDestSubject;
	}

	/**
	 * ��ȡ��Ϣ�����߹�ϣMap��key��subject��value��Producer
	 * 
	 * @return
	 */
	public ConcurrentHashMap getProducerMap() {
		return producerMap;
	}

	/**
	 * ��ȡJMS ��ϢSession��ϣMap��key��subject��value��Session
	 * 
	 * @return
	 */
	public ConcurrentHashMap getSessionMap() {
		return sessionMap;
	}

	/**
	 * ��ȡjms ��������
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
