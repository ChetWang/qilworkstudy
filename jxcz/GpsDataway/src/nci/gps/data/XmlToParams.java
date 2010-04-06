package nci.gps.data;

import java.util.ArrayList;

import nci.gps.log.MsgLogger;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

/**
 * @功能：将服务器接收到的xml字符串，分解成参数数组
 * @时间：2008-07-24
 * @author ZHOUHM
 *
 */
public class XmlToParams {
	// 参数队列
	private ArrayList paramList;
	// xml字符串
	String xml;
	
	public XmlToParams(String xml){
		paramList = new ArrayList();
		this.xml = xml;
		analyseXml();
	}
	
	/**
	 * 将xml字符串分解成参数名、参数值两个字符串数组
	 * 然后将两个字符串数组作为一个节点添加到参数队列中
	 */
	private void analyseXml(){
		try {
			Document doc = DocumentHelper.parseText(xml);
			Element root = doc.getRootElement();
			
			for (int i = 0, rSize = root.nodeCount(); i < rSize; i++) {
				Node rNode = root.node(i);
				if (rNode instanceof Element) {
					Element rowE = (Element) rNode;
					ArrayList[] rowList = new ArrayList[] { new ArrayList(),
							new ArrayList() };
					for (int j = 0, cSize = rowE.nodeCount(); j < cSize; j++) {
						Node cNode = rowE.node(j);
						if (cNode instanceof Element) {
							Element cE = (Element) cNode;
							String paraName = cE.attribute(0).getValue();
							String paraValue = cE.getStringValue();
							rowList[0].add(paraName); // 获取元素名(参数名)
							rowList[1].add(paraValue); // 获取参数值
						}
					}
					rowList[0].trimToSize();
					rowList[1].trimToSize();
					paramList.add(rowList);
				}
			}
			paramList.trimToSize();
		} catch (DocumentException e) {
			MsgLogger.logExceptionTrace("xml字符串分析类，生成Document时错误！", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取参数队列
	 * @return
	 */
	public ArrayList getParamList(){
		return paramList;
	}
}
