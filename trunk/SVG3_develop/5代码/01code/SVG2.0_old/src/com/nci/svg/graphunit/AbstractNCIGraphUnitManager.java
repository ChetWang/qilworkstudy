package com.nci.svg.graphunit;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGDocument;

import com.nci.svg.ui.graphunit.NCIThumbnailPanel;
import com.nci.svg.util.Constants;
import com.nci.svg.util.Utilities;

import fr.itris.glips.svgeditor.Editor;
import fr.itris.glips.svgeditor.EditorToolkit;

public abstract class AbstractNCIGraphUnitManager {

    protected String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
    protected Document equipDoc = null;
    /**
     * ��ʼ��ͼԪ��ϣMap�������key��ͼԪID
     */
    protected LinkedHashMap<String,NCIEquipSymbolBean> originalSymbolMap = null;
    /**
     * ͼԪ���ϣ�ת��originalSymbolMap��ģ���key��ͼԪ�����ͺ����Ƶ���ϣ�����/���ƣ���value������NCIEquipSymbolBean����
     */
    protected LinkedHashMap<String, NCIEquipSymbolBean> symbolMap_type = null;
    /**
     * ͼԪ���ϣ�����use�ڵ��жϣ���use�ڵ�����Ϊkey��������#,����ͼԪ���ƣ�
     */
    protected LinkedHashMap<String, NCIEquipSymbolBean> symbolMap_useID = new LinkedHashMap<String, NCIEquipSymbolBean>();
    /**
     * ����ֵ�б�
     */
    protected ArrayList<String> symbolsTypes = null;
    /**
     * ͼԪ����ֵ�б�
     */
    protected ArrayList<String> symbolsTypesDefine = null;
    /**
     * ״ֵ̬�б�
     */
    protected ArrayList<String> symbolsStatus = null;

    protected Editor editor;
    /**
     * ͼԪ״̬���ϣ�keyΪͼԪ�飬value��״̬���󣬲�ͬ��״̬���ص��ַ������Ϊ�����ʾ��״̬�������ʾ��״̬ͼԪ������
     */
    protected LinkedHashMap<String, NCISymbolStatusBean> statusMap = new LinkedHashMap<String, NCISymbolStatusBean>();

    protected LinkedHashMap<String, NCIThumbnailPanel> thumbnailMap = new LinkedHashMap<String, NCIThumbnailPanel>();

    public AbstractNCIGraphUnitManager(Editor editor) {
        this.editor = editor;
    }

    /**
     * ��ȡͼԪ�����б�
     * 
     * @return
     */
    // public abstract ArrayList<NCIEquipSymbolBean> getEquipSymbolList() throws
    // IOException;
    public abstract ArrayList<NCIGraphUnitTypeBean> getEquipSymbolTypeList()
            throws IOException;

    /**
     * ��ȡ����ͼ
     * 
     * @param bean
     * @param type
     *            thumbnail ���ͣ�������outlook���ͣ���Ӧֵ��1����Ҳ������combobox���ͣ���Ӧֵ��0��
     * @return
     */
    public abstract ArrayList<NCIThumbnailPanel> getThumnailList(
            NCIGraphUnitTypeBean bean, int type);

    /**
     * ��ӡDocument��Ӧ��xml�ַ���
     * 
     * @param doc
     * @param symboName
     * @return
     * @throws Exception
     */
    public String printNode(Node doc, String symboName) throws Exception {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = tf.newTransformer();
        t.setOutputProperty("encoding", "UTF-8");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        t.transform(new DOMSource(doc), new StreamResult(bos));
        String xmlStr = bos.toString();
        System.out.print(symboName + ":");
        System.out.println(xmlStr);
        return xmlStr;
    }

    /**
     * �����յ�ͼԪ����SVGDocument
     * 
     * @return
     */
    public SVGDocument createEmptyGraphUnitDocument() {
        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        SVGDocument doc = (SVGDocument) impl.createDocument(svgNS, "svg", null);

        // gets the root element (the svg element)
        Element svgRoot = doc.getDocumentElement();
        svgRoot.setAttribute(Constants.NCI_SVG_PRODUCER_ATTR,
                Constants.NCI_SVG_PRODUCER_VALUE);
        svgRoot.setAttribute(Constants.NCI_SVG_Type_Attr,
                Constants.NCI_SVG_Type_GraphUnit_Value);
        svgRoot.setAttribute(Constants.NCI_SVG_XMLNS,
                Constants.NCI_SVG_XMLNS_VALUE);
        // set the width and height attribute on the root svg element
        svgRoot.setAttributeNS(null, "width",
                Constants.GRAPH_UNIT_WIDTH_StringValue);
        svgRoot.setAttributeNS(null, "height",
                Constants.GRAPH_UNIT_HEIGHT_StringValue);
        svgRoot
                .setAttribute(
                        "viewBox",
                        "0 0 "
                                + EditorToolkit
                                        .getPixelledNumber(Constants.GRAPH_UNIT_WIDTH_StringValue)
                                + " "
                                + EditorToolkit
                                        .getPixelledNumber(Constants.GRAPH_UNIT_HEIGHT_StringValue));
        // creating a defs element
        Element defsElement = doc
                .createElementNS(doc.getNamespaceURI(), "defs");
        svgRoot.appendChild(defsElement);
        return doc;
    }

    /**
     * ��ȡͼԪ�����б�keyΪ����+"/"+����
     * 
     * @return ͼԪ�����б�
     */
    public LinkedHashMap<String, NCIEquipSymbolBean> getSymbolMap_Type() {
        return symbolMap_type;
    }
    
    /**
     * ��ȡͼԪ�����б�,keyΪͼԪ����
     * 
     * @return ͼԪ�����б�
     */
    public LinkedHashMap<String, NCIEquipSymbolBean> getSymbolMap_useID() {
        return symbolMap_useID;
    }

    /**
     * ����ͼԪ�����б�
     * 
     * @param symbolMap
     *            �µ�ͼԪ�����б�
     */
    public void setSymbolMap(LinkedHashMap<String, NCIEquipSymbolBean> symbolMap) {
        this.symbolMap_type = symbolMap;
    }
    
    /**
     * ��ȡ���ʼ��ͼԪHashMap��������ͼԪID
     * @return
     */
    public LinkedHashMap<String, NCIEquipSymbolBean>  getOriginalSymbolMap(){
    	return originalSymbolMap;
    }

    /**
     * ��ȡͼԪ������
     * 
     * @return ArrayList<String> ͼԪ���ͼ���
     */
    @SuppressWarnings("unchecked")
    public ArrayList<String> getSymbolsTypes() {
        if (symbolsTypes == null) {
            try {
                symbolsTypes = (ArrayList<String>) Utilities
                        .communicateWithURL((String)editor.getGCParam("appRoot")
                                + (String)editor.getGCParam("servletPath")
                                + "?action=getSymbolsTypes", null);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return symbolsTypes;
    }
    
    /**
     * ��ȡͼԪ������
     * 
     * @return ArrayList<String> ͼԪ���ͼ���
     */
    @SuppressWarnings("unchecked")
    public ArrayList<String> getSymbolsTypesDefine() {
        if (symbolsTypesDefine == null) {
            try {
                symbolsTypesDefine = (ArrayList<String>) Utilities
                        .communicateWithURL((String)editor.getGCParam("appRoot")
                                + (String)editor.getGCParam("servletPath")
                                + "?action=getSymbolsTypesDefine", null);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return symbolsTypesDefine;
    }

    @SuppressWarnings("unchecked")
    /**
     * ��ȡͼԪ������״̬��Ϣ
     * @return ArrayList<String> ͼԪ������״̬��Ϣ����
     */
    public ArrayList<String> getSymbolsStatus() {
        if (symbolsStatus == null) {
            try {
                symbolsStatus = (ArrayList<String>) Utilities
                        .communicateWithURL((String)editor.getGCParam("appRoot")
                                + (String)editor.getGCParam("servletPath")
                                + "?action=getSymbolsStatus", null);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return symbolsStatus;
    }

    /**
     * @return the statusMap
     */
    public LinkedHashMap<String, NCISymbolStatusBean> getStatusMap() {
        return statusMap;
    }
    
    /**
     * ͼԪѡ�е����״̬
     */
    private int mouseStatus = -1;
    
    public int getMouseStatus(){
    	return mouseStatus;    	
    }
    
    public void setMouseStatus(int mouseStatus){
    	this.mouseStatus = mouseStatus;
    }
    
    public HashMap<String, NCIThumbnailPanel> getThumbnailMap(){
    	return thumbnailMap;
    }
}
