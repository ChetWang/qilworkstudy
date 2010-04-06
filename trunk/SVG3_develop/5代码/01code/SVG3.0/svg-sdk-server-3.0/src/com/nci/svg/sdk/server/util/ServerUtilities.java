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
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author yx.nci
 * @ʱ�䣺2008-11-25
 * @���ܣ��������˻���������
 * 
 */
public class ServerUtilities {
	/**
	 * ���ݴ���������������
	 * 
	 * @param strClassName���������������
	 * @return���������������
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
			System.err.println("����" + className + "�࣬ʧ�ܣ�");
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
			log.log(null, LoggerAdapter.INFO, "��ʼ Class.forName");
			
			Class cl = Thread.currentThread().getContextClassLoader().loadClass(className);
//			Class cl = Class.forName(className);//NCIClassLoader.loadSystemClass(className);//
			log.log(null, LoggerAdapter.INFO, "��ʼ����Constructor");
			Constructor constructor = cl.getConstructor(classargs);
			log.log(null, LoggerAdapter.INFO, "��ʼ����object");
			obj = constructor.newInstance(args);
		} catch (Exception ex) {
			ex.printStackTrace();
			log.log(null, LoggerAdapter.ERROR, "����" + className + "�࣬ʧ�ܣ�");
			return null;
		}
		log.log(null, LoggerAdapter.INFO, "startModule:ok:" + className);
		return obj;
	}

	/**
	 * ���ݴ�������ü����������ò�������ȡ���ò���ֵ
	 * 
	 * @param fileName:�����ļ���
	 * @param section�����ü�����
	 * @param profile�����ò�����
	 * @return�����ò���ֵ ��ʽΪ <���ڵ�> <���ü�����> <���ò�����>���ò���ֵ</���ò�����> </���ü�����> </���ڵ�>
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
	 * ��ȡ��ЧUUID
	 * 
	 * @return��UUID
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
