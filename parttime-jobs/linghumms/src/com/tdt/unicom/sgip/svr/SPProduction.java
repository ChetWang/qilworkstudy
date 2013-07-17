package com.tdt.unicom.sgip.svr;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.tdt.unicom.domains.SGIPHeader;

public class SPProduction {
	private static Logger log = Logger.getLogger(SPProduction.class);
	//������в�Ʒ����ʵ��
	private final static List<SPProduction> productions = new ArrayList<SPProduction>();
	
	private String name; // ��Ʒ����
	private String spnumber; // ��Ʒ�����̺�
	private String serviceType; // ��Ʒ�������
	private String transmitIp; // �������м�״̬����ת����ַ
	private int destPort; // Ŀ�Ķ˿�

	static {
		try {
			SAXBuilder builder = new SAXBuilder();
			Document doc = null;
			String osname = System.getProperty("os.name");
			Pattern ospn = Pattern.compile("^windows.*$",Pattern.CASE_INSENSITIVE); // ��Сд������
			String confpath = Thread.currentThread().getContextClassLoader().getResource(".").getPath();
			if (ospn.matcher(osname).matches()) { // windows����ϵͳ
				log.info("����ϵͳ����:" + osname + ",�����ļ�·��:"	+ System.getProperty("user.dir")+ "\\productionConf.xml");
				doc = builder.build(confpath + "\\productionConf.xml");
			} else {// Linuxϵͳ
				log.info("����ϵͳ����:" + osname + ",�����ļ�·��:"+ System.getProperty("user.dir")+ "/productionConf.xml");
				doc = builder.build(confpath + "/productionConf.xml");
			}
			// �жϲ���ϵͳ
			// ��Ŀ¼
			Element productionConf = doc.getRootElement();
			Element uncomConf = productionConf.getChild("unicomconf");
			// ��ȡ��ͨ��������ip��port
			SPSender.unicomIp = uncomConf.getChildText("ipaddr");
			SPSender.unicomPort = Integer.valueOf(uncomConf.getChildText("addrport"));
			String srcNodeId = uncomConf.getChildText("spNodeid");
			SGIPHeader.setSrcNodeId(srcNodeId);
			SPSender.spCorpId = srcNodeId.substring(5, 10);
			SPSender.spLoginName = uncomConf.getChildText("spUserName");
			SPSender.spLogPassword = uncomConf.getChildText("spPassword");
			UnicomSPMonitor.spListenPort = Integer.parseInt(uncomConf.getChildText("spListenPort"));
			UnicomSPMonitor.smgLoginUserName = uncomConf.getChildText("smgUserName");
			UnicomSPMonitor.smgLoginPassword = uncomConf.getChildText("smgPassword");
			// ����ʵ��
			List<Element> productconfList = productionConf.getChildren("productconf");
			log.info("***************** ������������  ********************");
			log.info("��ͨSMG��ַ:" + SPSender.unicomIp);
			log.info("��ͨSMG�˿�:" + SPSender.unicomPort);
			log.info("Դ�ڵ��ţ�" + SGIPHeader.getSrcNodeId());
			log.info("SMG����SP�˿�:" + UnicomSPMonitor.spListenPort);
			log.info("SP��½SMG�û�����" + SPSender.spLoginName);
			log.info("SP��½SMG���룺" + SPSender.spLogPassword);
			log.info("SMG��½SP�û�����" + UnicomSPMonitor.smgLoginUserName);
			log.info("SMG��½SP���룺" + UnicomSPMonitor.smgLoginPassword);
			log.info("***************** �����������  ********************\n");
			log.info("***************** ����ת������  ********************");
			for (Element productconf : productconfList) {
				// --------------------------��Ʒ������ʵ����
				SPProduction productbean = new SPProduction();
				productbean.setName(productconf.getChildTextTrim("name"));
				productbean.setSpnumber(productconf.getChildTextTrim("spnumber"));
				productbean.setServiceType(productconf.getChildTextTrim("servicetype"));
				productbean.setTransmitIp(productconf.getChildTextTrim("transmitip"));
				productbean.setDestPort(Integer.valueOf(productconf.getChildTextTrim("destport")));
				productions.add(productbean);
				// --------------------------��ӵ�PRODUCT_REPLY_MAP��
				log.info("��Ʒ����:" + productbean.getName());
				log.info("SP�������:" + productbean.getSpnumber());
				log.info("ҵ�����:" + productbean.getServiceType());
				log.info("ת����ַ:" + productbean.getTransmitIp() + ":"+ productbean.getDestPort() + "\n");
			}
			log.info("***************** ת�����ý���  ********************");
		} catch (JDOMException e) {
			log.error("����ת�������ļ�����!", e);
		} 
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpnumber() {
		return spnumber;
	}

	public void setSpnumber(String spnumber) {
		this.spnumber = spnumber;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getTransmitIp() {
		return transmitIp;
	}

	public void setTransmitIp(String transmitIp) {
		this.transmitIp = transmitIp;
	}

	public int getDestPort() {
		return destPort;
	}

	public void setDestPort(int destPort) {
		this.destPort = destPort;
	}
	
	/**
	 * ���ݲ�Ʒ����Ż�ö�Ӧ������ʵ��
	 * @param spNumber ��Ʒ�����
	 * @return��SPProduction
	 */
	public static SPProduction getProduction(String spNumber) {
		SPProduction spProduction = new SPProduction();
		for ( SPProduction production : productions) {
			if(spNumber.trim().equals(production.getSpnumber())) {
				spProduction = production;
				break;
			}
		}
		return spProduction;
	}
}
