package nci.gps.util;

/**
 * @功能：读取gpsConfig配置到hash表中
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
	 * 构造函数
	 *
	 */
	private ReadConfig(){
		paramsHash = new HashMap();
		
		// 获取程序运行目录
		gpsGlobal.appRoot = System.getProperty("user.dir");
//		gpsGlobal.appRoot = this.getClass().getResource("/").getPath();
		System.out.println("程序运行目录：" + gpsGlobal.appRoot);
		
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
	 * 从指定文件中读取配置信息
	 * 配置信息在gps元素下两层信息
	 * 配置信息保存在两层hash表中
	 * @param filename 文件名
	 */
	private void readConfig(String filename){
		try {
			SAXReader saxReader = new SAXReader();
			Document doc = saxReader.read(new File(filename));
			// 获取gps节点
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
			// 配置文件不存在
			System.out.println("找不到系统配置文件！");
		}
	}

	/**
	 * 获取配置信息表
	 * @return
	 */
	public HashMap getParamsHash() {
		return paramsHash;
	}

	/**
	 * 将配置信息写入全局变量中
	 *
	 */
	private void initGlobal(){
		// 日志地址
		gpsGlobal.LOG_ADDRESS = (String) ((HashMap) paramsHash.get("LOG"))
				.get("ADDRESS");
		// 是否记录日志
		String logFlag = (String) ((HashMap) paramsHash.get("LOG"))
				.get("WRITEFLAG");
		gpsGlobal.LOG_FLAG = logFlag.equals("true") ? true : false;

		
		// 数据库URL
		gpsGlobal.DB_URL = (String) ((HashMap) paramsHash.get("DATABASE"))
				.get("URL");
		// 数据库驱动
		gpsGlobal.DB_DRIVER = (String) ((HashMap) paramsHash.get("DATABASE"))
				.get("DRIVER");
		// 数据库登录用户名
		gpsGlobal.DB_USER = (String) ((HashMap) paramsHash.get("DATABASE"))
				.get("USER");
		// 数据库登录密码
		gpsGlobal.DB_PASSWORD = (String) ((HashMap) paramsHash.get("DATABASE"))
				.get("PASSWORD");
		
		
		// 分包服务器地址
		gpsGlobal.MS_URL = (String) ((HashMap) paramsHash
				.get("MESSAGESERVER")).get("URL");
		// 分包服务器JMS主题
		gpsGlobal.MS_SUBJECT = (String) ((HashMap) paramsHash
				.get("MESSAGESERVER")).get("SUBJECT");
	}
}
