package com.nci.svg.server.automapping.substation;

import java.util.ArrayList;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.nci.svg.server.util.XmlUtil;

/**
 * <p>
 * ���⣺GraUnit.java
 * </p>
 * <p>
 * ������
 * </p>
 * <p>
 * ��Ȩ��2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * ��˾��Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @ʱ��: 2009-4-2
 * @version 1.0
 */
public class GraUnit {
	/**
	 * ͼԪ���
	 */
	private String id;
	/**
	 * ͼԪ���ӵ�
	 */
	private ArrayList terminal;
	/**
	 * ͼԪ���
	 */
	private double width;
	/**
	 * ͼԪ�߶�
	 */
	private double height;
	/**
	 * ͼԪ����
	 */
	private String content;

	public GraUnit() {

	}

	/**
	 * ���캯��
	 * @param symbolNode
	 */
	public GraUnit(Node symbolNode) {
		NamedNodeMap attr = symbolNode.getAttributes();
		for (int i = 0, size = attr.getLength(); i < size; i++) {
			String name = attr.item(i).getNodeName();
			String value = attr.item(i).getNodeValue();
			if(name.equalsIgnoreCase("id")){
				this.id = value;
			}else if(name.equalsIgnoreCase("viewBox")){
				String[] box = value.split(" ");
				this.width = Double.parseDouble(box[2]);
				this.height = Double.parseDouble(box[3]);
			}
		}
		this.content = XmlUtil.nodeToString(symbolNode);
	}
	
	public String toString(){
		StringBuffer mess = new StringBuffer();
		mess.append("id:").append(id);
		mess.append(";").append("width:").append(width);
		mess.append(";").append("height:").append(height);
		return mess.toString();
	}
	
	/**
	 * 2009-4-2 Add by ZHM ��ȡͼԪ���
	 * 
	 * @return ͼԪ���
	 */
	public String getId() {
		return id;
	}

	/**
	 * 2009-4-2 Add by ZHM ����ͼԪ���
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 2009-4-2 Add by ZHM ��ȡ���ӵ���
	 * 
	 * @return
	 */
	public ArrayList getTerminal() {
		return terminal;
	}

	/**
	 * 2009-4-2 Add by ZHM �������ӵ�
	 * 
	 * @param terminal:ArrayList:���ӵ���
	 */
	public void setTerminal(ArrayList terminal) {
		this.terminal = terminal;
	}

	/**
	 * 2009-4-2 Add by ZHM ��ȡͼԪ���
	 * 
	 * @return ͼԪ���
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * 2009-4-2 Add by ZHM ����ͼԪ���
	 * 
	 * @param width:double:���
	 */
	public void setWidth(double width) {
		this.width = width;
	}

	/**
	 * 2009-4-2 Add by ZHM ��ȡͼԪ�߶�
	 * 
	 * @return ͼԪ�߶�
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * 2009-4-2 Add by ZHM ����ͼԪ�߶�
	 * 
	 * @param height:double:ͼԪ�߶�
	 */
	public void setHeight(double height) {
		this.height = height;
	}

	/**
	 * 2009-4-2 Add by ZHM ��ȡͼԪ����
	 * 
	 * @return String
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 2009-4-2 Add by ZHM ����ͼԪ����
	 * 
	 * @param content:String:ͼԪ����
	 */
	public void setContent(String content) {
		this.content = content;
	}

}
