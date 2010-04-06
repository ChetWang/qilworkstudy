package nci.gps.util;

/**
 * @���ܣ���ȡgpsConfig���õ�hash����
 * @author ZHOUHM
 *
 */
import java.io.File;
import java.util.HashMap;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class ReadConfig {
	private HashMap paramsHash;
	
	private static ReadConfig readConfig;
	
	/**
	 * ���캯��
	 *
	 */
	private ReadConfig(){
		paramsHash = new HashMap();
		
		// ��ȡ��������Ŀ¼
		gpsGlobal.appRoot = System.getProperty("user.dir");
//		gpsGlobal.appRoot = this.getClass().getResource("/").getPath();
		System.out.println("��������Ŀ¼��" + gpsGlobal.appRoot);
		
		String filename = "gpsConfig.xml";
		readConfig(gpsGlobal.appRoot +"/config/" + filename);
		initGlobal();
//		System.out.println(paramsHash.toString());
	}
	
	public static ReadConfig getInstance(){
		if(readConfig == null)
			readConfig =  new ReadConfig();
		return readConfig;
	}
	
	/**
	 * ��ָ���ļ��ж�ȡ������Ϣ
	 * ������Ϣ��gpsԪ����������Ϣ
	 * ������Ϣ����������hash����
	 * @param filename �ļ���
	 */
	private void readConfig(String filename){
		try {
			SAXReader saxReader = new SAXReader();
			Document doc = saxReader.read(new File(filename));
			// ��ȡgps�ڵ�
			Element root = doc.getRootElement();
			Element gRoot = root.element("gps");
			
			for(int i=0,gSize=gRoot.nodeCount(); i<gSize; i++){
				Node gNode = (Node)gRoot.node(i);
				if (gNode instanceof Element) {
					Element cElement = (Element)gNode;
					HashMap cHash = new HashMap();
					for(int j=0,cSize=cElement.nodeCount(); j<cSize; j++){
						Node cNode = (Node)cElement.node(j);
						if(cNode instanceof Element){
							Element rEle = (Element)cNode;
							String eleName = rEle.getName().toUpperCase();
							String eleValue = rEle.getStringValue();
//							System.out.println(eleName + ":" + eleValue);
							cHash.put(eleName, eleValue);
						}
					}
					paramsHash.put(cElement.getName().toUpperCase(), cHash);
				}
			}
		} catch (DocumentException e) {
			// �����ļ�������
			System.out.println("�Ҳ���ϵͳ�����ļ���");
		}
	}

	/**
	 * ��ȡ������Ϣ��
	 * @return
	 */
	public HashMap getParamsHash() {
		return paramsHash;
	}

	/**
	 * ��������Ϣд��ȫ�ֱ�����
	 *
	 */
	private void initGlobal(){
		// ��־��ַ
		gpsGlobal.LOG_ADDRESS = (String) ((HashMap) paramsHash.get("LOG"))
				.get("ADDRESS");
		// �Ƿ��¼��־
		String logFlag = (String) ((HashMap) paramsHash.get("LOG"))
				.get("WRITEFLAG");
		gpsGlobal.LOG_FLAG = logFlag.equals("true") ? true : false;

		
		// ���ݿ�URL
		gpsGlobal.DB_URL = (String) ((HashMap) paramsHash.get("DATABASE"))
				.get("URL");
		// ���ݿ�����
		gpsGlobal.DB_DRIVER = (String) ((HashMap) paramsHash.get("DATABASE"))
				.get("DRIVER");
		// ���ݿ��¼�û���
		gpsGlobal.DB_USER = (String) ((HashMap) paramsHash.get("DATABASE"))
				.get("USER");
		// ���ݿ��¼����
		gpsGlobal.DB_PASSWORD = (String) ((HashMap) paramsHash.get("DATABASE"))
				.get("PASSWORD");
		
		
		// �ְ���������ַ
		gpsGlobal.MS_URL = (String) ((HashMap) paramsHash
				.get("MESSAGESERVER")).get("URL");
		// �ְ�������JMS����
		gpsGlobal.MS_SUBJECT = (String) ((HashMap) paramsHash
				.get("MESSAGESERVER")).get("SUBJECT");
	}
}
