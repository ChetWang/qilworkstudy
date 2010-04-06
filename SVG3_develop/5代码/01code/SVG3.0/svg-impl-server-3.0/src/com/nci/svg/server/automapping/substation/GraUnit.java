package com.nci.svg.server.automapping.substation;

import java.util.ArrayList;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.nci.svg.server.util.XmlUtil;

/**
 * <p>
 * 标题：GraUnit.java
 * </p>
 * <p>
 * 描述：
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2009-4-2
 * @version 1.0
 */
public class GraUnit {
	/**
	 * 图元编号
	 */
	private String id;
	/**
	 * 图元连接点
	 */
	private ArrayList terminal;
	/**
	 * 图元宽度
	 */
	private double width;
	/**
	 * 图元高度
	 */
	private double height;
	/**
	 * 图元内容
	 */
	private String content;

	public GraUnit() {

	}

	/**
	 * 构造函数
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
	 * 2009-4-2 Add by ZHM 获取图元编号
	 * 
	 * @return 图元编号
	 */
	public String getId() {
		return id;
	}

	/**
	 * 2009-4-2 Add by ZHM 设置图元编号
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 2009-4-2 Add by ZHM 获取连接点组
	 * 
	 * @return
	 */
	public ArrayList getTerminal() {
		return terminal;
	}

	/**
	 * 2009-4-2 Add by ZHM 设置连接点
	 * 
	 * @param terminal:ArrayList:连接点组
	 */
	public void setTerminal(ArrayList terminal) {
		this.terminal = terminal;
	}

	/**
	 * 2009-4-2 Add by ZHM 获取图元宽度
	 * 
	 * @return 图元宽度
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * 2009-4-2 Add by ZHM 设置图元宽度
	 * 
	 * @param width:double:宽度
	 */
	public void setWidth(double width) {
		this.width = width;
	}

	/**
	 * 2009-4-2 Add by ZHM 获取图元高度
	 * 
	 * @return 图元高度
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * 2009-4-2 Add by ZHM 设置图元高度
	 * 
	 * @param height:double:图元高度
	 */
	public void setHeight(double height) {
		this.height = height;
	}

	/**
	 * 2009-4-2 Add by ZHM 获取图元内容
	 * 
	 * @return String
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 2009-4-2 Add by ZHM 设置图元内容
	 * 
	 * @param content:String:图元内容
	 */
	public void setContent(String content) {
		this.content = content;
	}

}
