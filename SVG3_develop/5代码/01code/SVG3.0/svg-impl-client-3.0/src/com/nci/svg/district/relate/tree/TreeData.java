package com.nci.svg.district.relate.tree;

import java.awt.Color;
import java.awt.Font;

/**
 * <p>
 * 标题：DynamicTreeNodeBean.java
 * </p>
 * <p>
 * 描述： 关联结果显示树节点信息类
 * </p>
 * <p>
 * 版权：2003 Hangzhou NCI System Engineering, Ltd.
 * </p>
 * <p>
 * 公司：Hangzhou NCI System Engineering, Ltd.
 * </p>
 * 
 * @author ZHM
 * @时间: 2009-5-1
 * @version 1.0
 */
public class TreeData {

	/**
	 * 节点显示字体
	 */
	protected Font font;

	/**
	 * 节点显示颜色
	 */
	protected Color color;

	/**
	 * 节点显示名称
	 */
	protected String string;

	/**
	 * @功能 构造函数
	 * @param newFont
	 * @param newColor
	 * @param newString
	 */
	public TreeData(Font newFont, Color newColor, String newString) {
		font = newFont;
		color = newColor;
		string = newString;
	}

	/**
	 * 2009-5-1 Add by ZHM
	 * 
	 * @功能 设置字体格式
	 * @param newFont
	 */
	public void setFont(Font newFont) {
		font = newFont;
	}

	/**
	 * 2009-5-1 Add by ZHM
	 * 
	 * @功能 获取字体格式
	 * @return
	 */
	public Font getFont() {
		return font;
	}

	/**
	 * 2009-5-1 Add by ZHM
	 * 
	 * @功能 设置字体颜色
	 * @param newColor
	 */
	public void setColor(Color newColor) {
		color = newColor;
	}

	/**
	 * 2009-5-1 Add by ZHM
	 * 
	 * @功能 获取字体颜色
	 * @return
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * 2009-5-1 Add by ZHM
	 * 
	 * @功能 设置显示信息
	 * @param newString
	 */
	public void setString(String newString) {
		string = newString;
	}

	/**
	 * 2009-5-1 Add by ZHM
	 * 
	 * @功能 获取显示信息
	 * @return
	 */
	public String string() {
		return string;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return string;
	}

}
