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
     * 初始的图元哈希Map，这里的key是图元ID
     */
    protected LinkedHashMap<String,NCIEquipSymbolBean> originalSymbolMap = null;
    /**
     * 图元集合（转换originalSymbolMap后的），key是图元的类型和名称的组合（类型/名称），value是整个NCIEquipSymbolBean对象
     */
    protected LinkedHashMap<String, NCIEquipSymbolBean> symbolMap_type = null;
    /**
     * 图元集合，用于use节点判断，以use节点名作为key（不包含#,仅有图元名称）
     */
    protected LinkedHashMap<String, NCIEquipSymbolBean> symbolMap_useID = new LinkedHashMap<String, NCIEquipSymbolBean>();
    /**
     * 类型值列表
     */
    protected ArrayList<String> symbolsTypes = null;
    /**
     * 图元大类值列表
     */
    protected ArrayList<String> symbolsTypesDefine = null;
    /**
     * 状态值列表
     */
    protected ArrayList<String> symbolsStatus = null;

    protected Editor editor;
    /**
     * 图元状态集合，key为图元组，value是状态对象，不同的状态返回的字符串如果为空则表示无状态，否则表示有状态图元存在了
     */
    protected LinkedHashMap<String, NCISymbolStatusBean> statusMap = new LinkedHashMap<String, NCISymbolStatusBean>();

    protected LinkedHashMap<String, NCIThumbnailPanel> thumbnailMap = new LinkedHashMap<String, NCIThumbnailPanel>();

    public AbstractNCIGraphUnitManager(Editor editor) {
        this.editor = editor;
    }

    /**
     * 获取图元类型列表
     * 
     * @return
     */
    // public abstract ArrayList<NCIEquipSymbolBean> getEquipSymbolList() throws
    // IOException;
    public abstract ArrayList<NCIGraphUnitTypeBean> getEquipSymbolTypeList()
            throws IOException;

    /**
     * 获取缩略图
     * 
     * @param bean
     * @param type
     *            thumbnail 类型，可以是outlook类型（对应值是1），也可以是combobox类型（对应值是0）
     * @return
     */
    public abstract ArrayList<NCIThumbnailPanel> getThumnailList(
            NCIGraphUnitTypeBean bean, int type);

    /**
     * 打印Document对应得xml字符串
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
     * 创建空的图元管理SVGDocument
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
     * 获取图元符号列表，key为类型+"/"+名称
     * 
     * @return 图元符号列表
     */
    public LinkedHashMap<String, NCIEquipSymbolBean> getSymbolMap_Type() {
        return symbolMap_type;
    }
    
    /**
     * 获取图元符号列表,key为图元名称
     * 
     * @return 图元符号列表
     */
    public LinkedHashMap<String, NCIEquipSymbolBean> getSymbolMap_useID() {
        return symbolMap_useID;
    }

    /**
     * 设置图元符号列表
     * 
     * @param symbolMap
     *            新的图元符号列表
     */
    public void setSymbolMap(LinkedHashMap<String, NCIEquipSymbolBean> symbolMap) {
        this.symbolMap_type = symbolMap;
    }
    
    /**
     * 获取最初始的图元HashMap，主键是图元ID
     * @return
     */
    public LinkedHashMap<String, NCIEquipSymbolBean>  getOriginalSymbolMap(){
    	return originalSymbolMap;
    }

    /**
     * 获取图元的类型
     * 
     * @return ArrayList<String> 图元类型集合
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
     * 获取图元的类型
     * 
     * @return ArrayList<String> 图元类型集合
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
     * 获取图元的所有状态信息
     * @return ArrayList<String> 图元的所有状态信息集合
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
     * 图元选中的鼠标状态
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
