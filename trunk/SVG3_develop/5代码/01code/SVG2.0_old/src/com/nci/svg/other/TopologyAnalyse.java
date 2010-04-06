/**
 * 类名：com.nci.svg.other.TopologyAnalyse
 * 创建人:yx
 * 创建日期：2008-5-12
 * 类作用:提取svg中设备关联关系
 * 修改日志：
 */
package com.nci.svg.other;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.nci.svg.util.Constants;
import com.nci.svg.util.NCIGlobal;
import com.nci.svg.util.ServletActionConstants;
import com.nci.svg.util.Utilities;

import fr.itris.glips.library.util.XMLPrinter;
import fr.itris.glips.svgeditor.Editor;

/**
 * @author yx
 *
 */
public class TopologyAnalyse {

    public HashMap<String, EquipRelaData> mapRela = null;

    public HashMap<String, Element> mapElement = null;
    private Editor editor = null;
    public TopologyAnalyse(Editor editor)
    {
       this.editor = editor; 
    }

    /**从输入的svg文档中拆分获取设备关联信息
     * @param doc:svg文档
     */
    public void splitRela(Document doc) {
        if (mapRela != null) {
            mapRela.clear();
            mapRela = null;
        }

        if (mapElement != null) {
            mapElement.clear();
            mapElement = null;
        }

        if (doc == null)
            return;

        mapRela = new HashMap<String, EquipRelaData>();
        mapElement = new HashMap<String, Element>();
        readDocToMap(doc);
        String strRela = null;

        Element eRoot = doc.getDocumentElement();

        if (eRoot == null)
            return;

        int nImageCount = eRoot.getElementsByTagName("image").getLength();

        for (int i = 0; i < nImageCount; i++) {
            Element element = (Element) eRoot.getElementsByTagName("image")
                    .item(i);
            if (element == null)
                continue;

            String strNciType = element.getAttribute("nciType");
            if (strNciType == null || !strNciType.equals("GraphUnit")) {
                continue;
            }
            buildEquipRela(element);
        }
        
        final Document doc22 = buildXmlFromMap();
        try
        {
        if(saveRelaDataToServer(doc22) >= 0)
        {
            System.out.println("保存成功");    
        }
        else
        {
            System.out.println("保存失败"); 
        }
        }
    catch(Exception ex)
    {
        ex.printStackTrace();
        System.out.println("保存失败"); 

    }
        return;
    }

    /**读取xml对象中设备及连线信息，放置在map中，便于查找
     * @param doc
     */
    public void readDocToMap(Document doc) {
        Element eRoot = doc.getDocumentElement();
        NodeList nodelist = eRoot.getChildNodes();
        for (int i = 0; i < nodelist.getLength(); i++) {
            Element element = (Element) nodelist.item(i);

            String strID = element.getAttribute("id");

            if (strID != null && strID.length() > 0) {
                mapElement.put(strID, element);
            }
        }
        return;
    }

    /**根据传入的设备节点，生成该设备的关联信息，加载至maprela中
     * @param element:设备节点
     */
    public void buildEquipRela(Element element) {
        Element terminal = (Element) element.getElementsByTagName("terminal")
                .item(0);
        if (terminal == null)
            return;

        /*
        Element csElement = (Element)metadata.getElementsByTagName("PSR:CimClass").item(0);
        Element obElement = (Element)metadata.getElementsByTagName("PSR:ObjRef").item(0);
        if(obElement == null || csElement == null)
            return;
        
        String strCimClass = csElement.getAttribute("CimType");
        String strAppCode = obElement.getAttribute("AppCode");
         */
        String strAppCode = getTempAppCode(element);
        if (strAppCode == null)
            return;

        int nPoint = terminal.getElementsByTagName("nci:POINT").getLength();
        for (int i = 0; i < nPoint; i++) {
            Element npElement = (Element) terminal.getElementsByTagName(
                    "nci:POINT").item(i);

            String strLineCount = npElement.getAttribute("LineCount");

            if (strLineCount == null || strLineCount.equals("0")
                    || strLineCount.length() == 0) {
                continue;
            }

            int nLineCount = new Integer(strLineCount).intValue();
            for (int j = 0; j < nLineCount; j++) {

                String strTmp = new String().format("line%d", j);
                String strLineInfo = npElement.getAttribute(strTmp);
                String strEquipID = getAppCodeFromLineID(strLineInfo);

                if (strEquipID == null)
                    continue;

                String[] strTemp = strEquipID.split(":");
                String strID = null;

                if (strTemp[0].compareToIgnoreCase(strAppCode) > 0) {
                    strID = new String()
                            .format("%s-%s", strAppCode, strTemp[0]);
                } else
                    strID = new String()
                            .format("%s-%s", strTemp[0], strAppCode);

                if (mapRela.get(strID) == null) {
                    EquipRelaData data = new EquipRelaData();

                    if (strTemp[0].compareToIgnoreCase(strAppCode) > 0) {
                        data.setFirstEquipID(strAppCode);
                        data.setSecondEquipID(strTemp[0]);
                    } else {
                        data.setFirstEquipID(strTemp[0]);
                        data.setSecondEquipID(strAppCode);
                    }

                    if (strTemp.length == 2) {
                        data.setBetweenEquipID(strTemp[1]);
                    }
                    mapRela.put(strID, data);
                    System.out.println("key:" + strID);
                    data.print();
                }
            }
        }
        return;
    }

    public String getAppCodeFromLineID(String strLineInfo) {
        String strEquipID = null;
        String[] strLine = strLineInfo.split(":");

        Element element = mapElement.get(strLine[0]);
        if (element == null)
            return null;

        String strTemp = null;

        if (strLine[1].equals("p0")) {
            strTemp = element.getAttribute("p1");
        } else {
            strTemp = element.getAttribute("p0");
        }

        if (strTemp == null || strTemp.length() == 0)
            return null;

        String[] strEquip = strTemp.split(":");

        return getAppCode(strEquip[0]);
    }

    public String getTempAppCode(Element element) {
        Element params = (Element) element.getElementsByTagName("Params").item(
                0);
        if (params == null)
            return null;

        String strAppCode = null;
        int nElementCount = params.getElementsByTagName("Element").getLength();
        for (int i = 0; i < nElementCount; i++) {
            Element obElement = (Element) params
                    .getElementsByTagName("Element").item(i);
            if (obElement != null) {
                if (obElement.getAttribute("name").equals("1")) {
                    strAppCode = obElement.getAttribute("value");
                    return strAppCode;
                }
            }
        }
        return strAppCode;
    }

    public String getAppCode(Element element) {
        Element terminal = (Element) element.getElementsByTagName("terminal")
                .item(0);
        if (terminal == null)
            return null;

        Element obElement = (Element) terminal.getElementsByTagName(
                Constants.NCI_SVG_PSR_OBJREF).item(0);
        if (obElement == null)
            return null;

        String strAppCode = obElement.getAttribute(Constants.NCI_SVG_APPCODE_ATTR);
        return strAppCode;
    }

    public String getAppCode(String strID) {
        Element element = mapElement.get(strID);
        if (element == null)
            return null;

        return getTempAppCode(element);

    }

    public Document buildXmlFromMap() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElementNS(null, "Params");
            doc.appendChild(root);
            Iterator<EquipRelaData> it = mapRela.values().iterator();
            
            while (it.hasNext()) {
                EquipRelaData data = it.next();
                insertDataToXml(doc,root, data);
            }
            return doc;
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void insertDataToXml(Document doc,Element eRoot, EquipRelaData data) {
        Element element = doc.createElementNS(null,"EquipRela");

        eRoot.appendChild(element);
        element.setAttributeNS(null, "firstequip", data.getFirstEquipID());
        element.setAttributeNS(null, "secondequip", data.getSecondEquipID());
        element.setAttributeNS(null, "betweenequip", data.getBetweenEquipID());
        element.setAttributeNS(null, "level", new String().format("%d", data.getNSvgLevel()));
        
        return;
    }
    
    public int saveRelaDataToServer(Document doc)throws XPathExpressionException,
    ParserConfigurationException, IOException, ClassNotFoundException
    {
        StringBuffer xmlData = new StringBuffer();
        //将xml document数据写入一个StringBuffer对象
                
        XMLPrinter.writeNode(doc.getDocumentElement(), xmlData, 0,
                false, null);
//        System.out.println(xmlData.toString());
        StringBuffer baseURL = new StringBuffer();
        baseURL.append(editor.getGCParam("appRoot")).append(editor.getGCParam("servletPath")).append(
                "?action=").append(ServletActionConstants.RELATION_OPERATION);
        String[][] param = new String[2][2];
        param[0][0] = "svgID";
        param[0][1] = "wdb-500";
        param[1][0] = "metaData";
        param[1][1] = xmlData.toString();
//        StringBuffer saveURL = Utilities.parseURL(baseURL.toString(), param);
////        System.out.println(saveURL);
//
//        URL saveActionURl = new URL(saveURL.toString());
//        ObjectInputStream ois = new ObjectInputStream(saveActionURl.openStream());
//        return Integer.parseInt(ois.readObject().toString());
        return Integer.parseInt(Utilities.communicateWithURL(baseURL.toString(), param).toString());
    }
}
