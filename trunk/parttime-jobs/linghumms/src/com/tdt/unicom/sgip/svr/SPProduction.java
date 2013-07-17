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
	//存放所有产品配置实例
	private final static List<SPProduction> productions = new ArrayList<SPProduction>();
	
	private String name; // 产品名称
	private String spnumber; // 产品服务商号
	private String serviceType; // 产品服务代码
	private String transmitIp; // 短信上行及状态报告转发地址
	private int destPort; // 目的端口

	static {
		try {
			SAXBuilder builder = new SAXBuilder();
			Document doc = null;
			String osname = System.getProperty("os.name");
			Pattern ospn = Pattern.compile("^windows.*$",Pattern.CASE_INSENSITIVE); // 大小写不敏感
			String confpath = Thread.currentThread().getContextClassLoader().getResource(".").getPath();
			if (ospn.matcher(osname).matches()) { // windows操作系统
				log.info("操作系统名称:" + osname + ",配置文件路径:"	+ System.getProperty("user.dir")+ "\\productionConf.xml");
				doc = builder.build(confpath + "\\productionConf.xml");
			} else {// Linux系统
				log.info("操作系统名称:" + osname + ",配置文件路径:"+ System.getProperty("user.dir")+ "/productionConf.xml");
				doc = builder.build(confpath + "/productionConf.xml");
			}
			// 判断操作系统
			// 根目录
			Element productionConf = doc.getRootElement();
			Element uncomConf = productionConf.getChild("unicomconf");
			// 获取联通短信网关ip及port
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
			// 配置实例
			List<Element> productconfList = productionConf.getChildren("productconf");
			log.info("***************** 加载网关配置  ********************");
			log.info("联通SMG地址:" + SPSender.unicomIp);
			log.info("联通SMG端口:" + SPSender.unicomPort);
			log.info("源节点编号：" + SGIPHeader.getSrcNodeId());
			log.info("SMG连接SP端口:" + UnicomSPMonitor.spListenPort);
			log.info("SP登陆SMG用户名：" + SPSender.spLoginName);
			log.info("SP登陆SMG密码：" + SPSender.spLogPassword);
			log.info("SMG登陆SP用户名：" + UnicomSPMonitor.smgLoginUserName);
			log.info("SMG登陆SP密码：" + UnicomSPMonitor.smgLoginPassword);
			log.info("***************** 网关配置完成  ********************\n");
			log.info("***************** 加载转发配置  ********************");
			for (Element productconf : productconfList) {
				// --------------------------产品配置类实例化
				SPProduction productbean = new SPProduction();
				productbean.setName(productconf.getChildTextTrim("name"));
				productbean.setSpnumber(productconf.getChildTextTrim("spnumber"));
				productbean.setServiceType(productconf.getChildTextTrim("servicetype"));
				productbean.setTransmitIp(productconf.getChildTextTrim("transmitip"));
				productbean.setDestPort(Integer.valueOf(productconf.getChildTextTrim("destport")));
				productions.add(productbean);
				// --------------------------添加到PRODUCT_REPLY_MAP中
				log.info("产品名称:" + productbean.getName());
				log.info("SP接入号码:" + productbean.getSpnumber());
				log.info("业务代码:" + productbean.getServiceType());
				log.info("转发地址:" + productbean.getTransmitIp() + ":"+ productbean.getDestPort() + "\n");
			}
			log.info("***************** 转发配置结束  ********************");
		} catch (JDOMException e) {
			log.error("解析转发配置文件出错!", e);
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
	 * 根据产品接入号获得对应的配置实例
	 * @param spNumber 产品接入号
	 * @return　SPProduction
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
