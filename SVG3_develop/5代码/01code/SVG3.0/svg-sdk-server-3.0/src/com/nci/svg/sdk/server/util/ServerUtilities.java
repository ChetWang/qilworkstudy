package com.nci.svg.sdk.server.util;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nci.svg.sdk.logger.LoggerAdapter;

/**
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author yx.nci
 * @时间：2008-11-25
 * @功能：服务器端基础方法类
 * 
 */
public class ServerUtilities {
	/**
	 * 根据传入的类名启动组件
	 * 
	 * @param strClassName：组件调用主类名
	 * @return：启动后组件对象
	 */
	public static Object startModule(String className, HashMap mapParams) {
		Object obj = null;
		try {
			Class[] classargs = { HashMap.class };
			Object[] args = { mapParams };
			obj = Class.forName(className).getConstructor(classargs)
					.newInstance(args);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("启动" + className + "类，失败！");
			return null;
		}
		return obj;
	}

	public synchronized static Object startModule(LoggerAdapter log,
			String className, HashMap mapParams) {
		Object obj = null;
		log.log(null, LoggerAdapter.INFO, "startModule:" + className);
		try {
			Class[] classargs = { HashMap.class };
			Object[] args = { mapParams };
			log.log(null, LoggerAdapter.INFO, "开始 Class.forName");
			
			Class cl = Thread.currentThread().getContextClassLoader().loadClass(className);
//			Class cl = Class.forName(className);//NCIClassLoader.loadSystemClass(className);//
			log.log(null, LoggerAdapter.INFO, "开始构造Constructor");
			Constructor constructor = cl.getConstructor(classargs);
			log.log(null, LoggerAdapter.INFO, "开始构造object");
			obj = constructor.newInstance(args);
		} catch (Exception ex) {
			ex.printStackTrace();
			log.log(null, LoggerAdapter.ERROR, "启动" + className + "类，失败！");
			return null;
		}
		log.log(null, LoggerAdapter.INFO, "startModule:ok:" + className);
		return obj;
	}

	/**
	 * 根据传入的配置集合名和配置参数名获取配置参数值
	 * 
	 * @param fileName:配置文件名
	 * @param section：配置集合名
	 * @param profile：配置参数名
	 * @return：配置参数值 格式为 <根节点> <配置集合名> <配置参数名>配置参数值</配置参数名> </配置集合名> </根节点>
	 */
	public static String readCfg(String fileName, String section, String profile) {
		Document document = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setValidating(false);
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.parse(new File(fileName));

			Element root = document.getDocumentElement();
			Element element = (Element) root.getElementsByTagName(section)
					.item(0);
			if (element == null)
				return null;

			element = (Element) element.getElementsByTagName(profile).item(0);
			if (element == null)
				return null;
			return element.getFirstChild().getNodeValue();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取有效UUID
	 * 
	 * @return：UUID
	 */
	public static String getUUIDString() {
		UUIDGenerator generator = UUIDGenerator.getInstance();
		UUID uuid = generator.generateRandomBasedUUID();
		return uuid.toString();
	}

	public static void main(String[] arc) {
		System.out.println(getUUIDString());
	}

}
