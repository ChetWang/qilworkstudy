package nci.gps.data;

import java.util.ArrayList;

import nci.gps.log.MsgLogger;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

/**
 * @���ܣ������������յ���xml�ַ������ֽ�ɲ�������
 * @ʱ�䣺2008-07-24
 * @author ZHOUHM
 *
 */
public class XmlToParams {
	// ��������
	private ArrayList paramList;
	// xml�ַ���
	String xml;
	
	public XmlToParams(String xml){
		paramList = new ArrayList();
		this.xml = xml;
		analyseXml();
	}
	
	/**
	 * ��xml�ַ����ֽ�ɲ�����������ֵ�����ַ�������
	 * Ȼ�������ַ���������Ϊһ���ڵ���ӵ�����������
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
							rowList[0].add(paraName); // ��ȡԪ����(������)
							rowList[1].add(paraValue); // ��ȡ����ֵ
						}
					}
					rowList[0].trimToSize();
					rowList[1].trimToSize();
					paramList.add(rowList);
				}
			}
			paramList.trimToSize();
		} catch (DocumentException e) {
			MsgLogger.logExceptionTrace("xml�ַ��������࣬����Documentʱ����", e);
			e.printStackTrace();
		}
	}
	
	/**
	 * ��ȡ��������
	 * @return
	 */
	public ArrayList getParamList(){
		return paramList;
	}
}
