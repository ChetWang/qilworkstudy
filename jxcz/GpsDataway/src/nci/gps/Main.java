package nci.gps;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

import nci.gps.log.MsgLogger;
import nci.gps.message.jms.AbstractComsumer;
import nci.gps.message.jms.AbstractProducer;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Main {

	public Main() {
		Document doc = null;
		SAXReader saxReader = new SAXReader();
		try {
			String filePath = System.getProperty("user.dir")+ "/config/gpsConfig.xml";
			MsgLogger.log(MsgLogger.INFO, "Path:"+filePath);
			doc = saxReader.read(new File(filePath));
		} catch (DocumentException e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
		}
		List comsumers = doc.selectNodes("//*[@owner='JMSConsumer']");
		List producers = doc.selectNodes("//*[@owner='JMSProducer']");
		for (int i = 0; i < comsumers.size(); i++) {
			if (comsumers.get(i) instanceof Element) {
				Element e = (Element) comsumers.get(i);
				String className = e.element("classname").getStringValue();
				try {
					Class c = Class.forName(className);
					Method m = c.getMethod("getInstance", null);
					((AbstractComsumer)m.invoke(null, null)).start();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			}
		}
		for (int i = 0; i < producers.size(); i++) {
			if (producers.get(i) instanceof Element) {
				Element e = (Element) producers.get(i);
				String className = e.element("classname").getStringValue();
				try {
					Class c = Class.forName(className);
					Method m = c.getMethod("getInstance", null);
					((AbstractProducer)m.invoke(null, null)).start();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		new Main();
	}

}
